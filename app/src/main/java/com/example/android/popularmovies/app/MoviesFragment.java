/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.popularmovies.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MoviesFragment extends Fragment {

    private ArrayList<Movie> movieArray;
    private MovieAdapter movieAdapter;


    public MoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Restore state if it exists
        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")){
            movieArray = new ArrayList<Movie>();
        }
        else {
            movieArray = savedInstanceState.getParcelableArrayList("movies");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outstate){
        outstate.putParcelableArrayList("movies", movieArray);
        super.onSaveInstanceState(outstate);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        movieAdapter =  new MovieAdapter( getActivity(), movieArray);

        View rootView = inflater.inflate(R.layout.movie_fragment, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movie);
        gridView.setAdapter(movieAdapter);

        // gridView.setColumnWidth(Integer.parseInt(getString(R.string.tmdb_image_size_342)));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movie = movieAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void updateMovies() {
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = prefs.getString(getString(R.string.pref_sort_order_key),
                getString(R.string.pref_sort_order_most_popular));
        moviesTask.execute(sortOrder);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        private Movie[] getMovieDataFromJson(String popMoviesJsonStr )
                throws JSONException {

            JSONObject popMoviesJson = new JSONObject(popMoviesJsonStr);
            JSONArray popMoviesArray = popMoviesJson.getJSONArray(getString(R.string.tmdb_results));

            Movie[] movies = new Movie[popMoviesArray.length()];

            for(int i = 0; i < popMoviesArray.length(); i++) {
                JSONObject movieObject = popMoviesArray.getJSONObject(i);

                movies[i] = new Movie(
                        movieObject.getString(getString(R.string.tmdb_poster_path)),
                        movieObject.getString(getString(R.string.tmdb_overview)),
                        movieObject.getString(getString(R.string.tmdb_release_date)),
                        movieObject.getString(getString(R.string.tmdb_id)),
                        movieObject.getString(getString(R.string.tmdb_title)),
                        movieObject.getInt(getString(R.string.tmdb_vote_count)),
                        movieObject.getDouble(getString(R.string.tmdb_vote_average))
                );
            }

            return movies;
        }



        @Override
        protected Movie[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection movieUrlConnection = null;
            BufferedReader movieReader = null;
            String popMoviesJsonStr = null;

            try {

                Uri builtMoviesUri = Uri.parse(getString(R.string.tmdb_base_url)).buildUpon()
                        .appendQueryParameter(getString(R.string.tmdb_sort_by_key), params[0])
                        .appendQueryParameter(getString(R.string.tmdb_api_key_key), BuildConfig.THE_MOVIE_DB_API_KEY)
                        .build();

                URL movieURL = new URL(builtMoviesUri.toString());

                movieUrlConnection = (HttpURLConnection) movieURL.openConnection();
                movieUrlConnection.setRequestMethod("GET");
                movieUrlConnection.connect();

                InputStream movieInputStream = movieUrlConnection.getInputStream();
                StringBuffer movieBuffer = new StringBuffer();
                if (movieInputStream == null)
                    return null;

                movieReader = new BufferedReader(new InputStreamReader(movieInputStream));
                String line;
                while ((line = movieReader.readLine()) != null)
                    movieBuffer.append(line + "\n");

                if (movieBuffer.length() == 0)
                    return null;  // Don't parse of there's no data

                popMoviesJsonStr = movieBuffer.toString();

                // Log.v(LOG_TAG, "Movie Data string: " + popMoviesJsonStr);


            } catch (IOException e) {
                Log.e(LOG_TAG, getString(R.string.log_error), e);
                return null;
            } finally {
                if (movieUrlConnection != null) {
                    movieUrlConnection.disconnect();
                }
                if (movieReader != null) {
                    try {
                        movieReader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, getString(R.string.log_stream_close_error), e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(popMoviesJsonStr );
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Movie[] result) {
            if (result != null) {
                movieAdapter.clear();
                for(Movie movieStr : result) {
                    movieAdapter.add(movieStr);
                }
            }
        }
    }
}
