
package com.example.android.popularmovies.app;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieAdapter extends ArrayAdapter<Movie> {

    public MovieAdapter(Activity context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ImageView imageView;
        Movie movie = getItem(position);

        if (view == null) {
            imageView  = new ImageView(getContext());
            imageView.setLayoutParams(new GridView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundColor(Color.BLACK);
            imageView.setPadding(0,0,0,0);
        }
        else {
            imageView = (ImageView) view;
        }

        String fullPosterPath =
                getContext().getString(R.string.tmdb_base_image_url) +
                getContext().getString(R.string.tmdb_image_size_185) +
                movie.posterPath;
        Picasso.with(getContext()).load(fullPosterPath).into(imageView);

        return imageView;
    }
}

