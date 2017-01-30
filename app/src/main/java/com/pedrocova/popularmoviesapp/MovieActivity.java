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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pedro on 26/01/2017.
 */

public class MovieActivity extends AppCompatActivity {

    @BindView(R.id.tv_movie_title) TextView mTitleTextView;
    @BindView(R.id.tv_movie_synopsis) TextView mSynopsisTextView;
    @BindView(R.id.tv_movie_user_rating) TextView mUserRatingTextView;
    @BindView(R.id.tv_movie_release_date) TextView mReleaseDateTextView;
    @BindView(R.id.img_movie_poster) ImageView mPosterImageView;
    @BindView(R.id.sv_movie_information) ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie);

        ButterKnife.bind(this);

        Intent intentStartedActivity = getIntent();

        Movie movie = new Gson().fromJson(intentStartedActivity.getStringExtra("movie"), Movie.class);

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
