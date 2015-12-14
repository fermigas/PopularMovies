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

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailsFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class DetailsFragment extends Fragment {

        private String mMovieStr;

        public DetailsFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.details_fragment, container, false);

            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra("movie")) {
                Movie movie = intent.getParcelableExtra("movie");

                String fullPosterPath =
                        getContext().getString(R.string.tmdb_base_image_url) +
                        getContext().getString(R.string.tmdb_image_size_342) +
                        movie.posterPath;
                ImageView imageView = (ImageView) rootView.findViewById(R.id.details_movie_poster);
                Picasso.with(getContext()).load(fullPosterPath).into(imageView);

                ((TextView) rootView.findViewById(R.id.details_title)).setText(movie.title);
                TextView tv = ((TextView) rootView.findViewById(R.id.details_overview));
                tv.setMovementMethod(new ScrollingMovementMethod());
                tv.setText(movie.overview);
                ((TextView) rootView.findViewById(R.id.details_release_date))
                        .setText("Release Date\n" + movie.releaseDate);
                ((TextView) rootView.findViewById(R.id.details_rating))
                        .setText("Average Rating\n" +  movie.vote_average + "/10");

            }

            return rootView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.details_fragment_menu, menu);

       }


    }
}
