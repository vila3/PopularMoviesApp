package com.pedrocova.popularmoviesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pedrocova.popularmoviesapp.Utils.ApiUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by pedro on 26/01/2017.
 */

public class MovieActivity extends AppCompatActivity {

    private Movie movie;
    private TextView mTitleTextView;
    private TextView mSynopsisTextView;
    private TextView mUserRatingTextView;
    private TextView mReleaseDateTextView;
    private ImageView mPosterImageView;
    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie);

        initViews();
        Intent intentStartedActivity = getIntent();

        movie = new Gson().fromJson(intentStartedActivity.getStringExtra("movie"), Movie.class);

        Uri imageUri = ApiUtils.getImageUri(getApplicationContext(), movie.getPoster_path());
        Log.d("MovieActivity", "Poster url: " + imageUri.toString());

        Picasso.with(getApplicationContext()).load(imageUri)
                .placeholder(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_image))
                .into(mPosterImageView);

        mTitleTextView.setText(movie.getOriginal_title());
        mSynopsisTextView.setText(movie.getOverview());
        mUserRatingTextView.setText(String.valueOf(movie.getVote_average()));
        mReleaseDateTextView.setText(movie.getRelease_date());

    }

    private void initViews() {
        mScrollView = (ScrollView) findViewById(R.id.sv_movie_information);
        mTitleTextView = (TextView) findViewById(R.id.tv_movie_title);
        mSynopsisTextView = (TextView) findViewById(R.id.tv_movie_synopsis);
        mUserRatingTextView = (TextView) findViewById(R.id.tv_movie_user_rating);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_movie_release_date);
        mPosterImageView = (ImageView) findViewById(R.id.img_movie_poster);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("scroll_position", mScrollView.getScrollY());
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mScrollView.setScrollY(savedInstanceState.getInt("scroll_position"));
    }
}
