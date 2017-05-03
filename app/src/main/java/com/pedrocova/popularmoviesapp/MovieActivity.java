package com.pedrocova.popularmoviesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pedrocova.popularmoviesapp.Adapters.TrailersAdapter;
import com.pedrocova.popularmoviesapp.Utils.ApiQueryTask;
import com.pedrocova.popularmoviesapp.Utils.ApiUtils;
import com.pedrocova.popularmoviesapp.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.tv_movie_run_time) TextView mRuntimeTextView;
    @BindView(R.id.rv_trailers) RecyclerView mTrailersRecyclerView;

    private RecyclerView.Adapter mRvAdapter;
    private RecyclerView.LayoutManager mRvLayoutManager;

    private List<String> mTrailersData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this);

        Intent intentStartedActivity = getIntent();

        final Movie movie = new Gson().fromJson(intentStartedActivity.getStringExtra("movie"), Movie.class);

        URL movieInfoUrl = NetworkUtils.builURL(ApiUtils.getMovieUri(getApplicationContext(), movie.getId()));
        new ApiQueryTask(this, new AsyncTaskCompleteListener<JSONObject>() {
            @Override
            public void onTaskComplete(JSONObject result) throws JSONException {
                if (result == null) return;
                displayMoviesInfo(new Gson().fromJson(result.toString(), Movie.class));
            }
        }).execute(movieInfoUrl);

        displayMoviesInfo(movie);

    }

    public void displayMoviesInfo(Movie movie) {

        mTrailersData = new ArrayList<>();

        Uri imageUri = ApiUtils.getImageUri(getApplicationContext(), movie.getPoster_path());
        Picasso.with(getApplicationContext()).load(imageUri)
                .placeholder(ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_image))
                .into(mPosterImageView);

        Uri trailersUri = ApiUtils.getVideosUri(getApplicationContext(), movie.getId());
        URL trailerUrl = NetworkUtils.builURL(trailersUri);
        new ApiQueryTask(this, new AsyncTaskCompleteListener<JSONObject>() {
            @Override
            public void onTaskComplete(JSONObject result) throws JSONException {
                if (result == null || !result.has("results")) return;
                JSONArray resultsArray = result.getJSONArray("results");
                for (int i=0; i<resultsArray.length(); i++)
                {
                    JSONObject trailer = resultsArray.getJSONObject(i);
                    if (trailer.getString("type").toLowerCase().equals("trailer") &&
                            trailer.getString("site").toLowerCase().equals("youtube"))
                        mTrailersData.add(trailer.getString("key"));
                }
                mRvAdapter.notifyDataSetChanged();
            }
        }).execute(trailerUrl);

        mTitleTextView.setText(movie.getOriginal_title());
        mSynopsisTextView.setText(movie.getOverview());
        mUserRatingTextView.setText(String.valueOf(movie.getVote_average()));
        mReleaseDateTextView.setText(movie.getRelease_date().substring(0, 4));
        mRuntimeTextView.setText(String.valueOf(movie.getRuntime()));

        mRvLayoutManager = new LinearLayoutManager(this);
        mRvAdapter = new TrailersAdapter(mTrailersData);

        mTrailersRecyclerView.setLayoutManager(mRvLayoutManager);
        mTrailersRecyclerView.setAdapter(mRvAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
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
