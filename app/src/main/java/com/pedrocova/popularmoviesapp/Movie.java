package com.pedrocova.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pedro on 26/01/2017.
 */

public class Movie implements Parcelable {

    private int id;
    private String original_title;
    private String poster_path;
    private String overview;
    private float vote_average;
    private String release_date;
    private int runtime;

    public Movie(int id, String original_title, String poster_path, String overview, float vote_average, String release_date, int runtime) {
        this.id = id;
        this.original_title = original_title;
        this.poster_path = poster_path;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.runtime = runtime;
    }

    private Movie(Parcel in) {
        id = in.readInt();
        original_title = in.readString();
        poster_path = in.readString();
        overview = in.readString();
        vote_average = in.readFloat();
        release_date = in.readString();
        runtime = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(original_title);
        parcel.writeString(poster_path);
        parcel.writeString(overview);
        parcel.writeFloat(vote_average);
        parcel.writeString(release_date);
        parcel.writeInt(runtime);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }
}
