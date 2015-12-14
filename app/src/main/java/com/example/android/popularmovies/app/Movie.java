package com.example.android.popularmovies.app;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    String  posterPath;
    String  overview;
    String  releaseDate;
    String  id;
    String  title;
    int     voteCount;
    double  vote_average;

    public Movie (
            String posterPath,
            String  overview,
            String  releaseDate,
            String  id,
            String  title,
            int     voteCount,
            double  vote_average
    ){
       this.posterPath = posterPath;
       this.overview = overview;
       this.releaseDate = releaseDate;
       this.id = id;
       this.title = title;
       this.voteCount = voteCount;
       this.vote_average = vote_average;

    }


    private Movie(Parcel in){
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        id = in.readString();
        title = in.readString();
        voteCount = in.readInt();
        vote_average = in.readDouble();

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(id);
        dest.writeString(title);
        dest.writeInt(voteCount);
        dest.writeDouble(vote_average);
    }

    public static final Parcelable.Creator<Movie> CREATOR= new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);  //using parcelable constructor
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
