package com.pedrocova.popularmoviesapp;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.pedrocova.popularmoviesapp.Adapters.MoviesAdapter;
import com.pedrocova.popularmoviesapp.Utils.ApiUtils;
import com.pedrocova.popularmoviesapp.Utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Spinner mSortBySpinner;
    private RecyclerView mMoviesGridRecyclerView;
    private TextView mErrorMessageTextView;
    private ProgressBar mLoadingProgressBar;

    List<Movie> mMovies;
    MoviesAdapter moviesAdapter;
    GridLayoutManager gridLayoutManager;

    private NetworkChangeReceiver internetStatusChange;

    boolean isUserTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar mActionBar = getActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setIcon(R.drawable.movie_icon);
        }

        initViews();

        mSortBySpinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.sort_by_readable_options,
                android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item));

        Log.d(TAG, "onCreate - isUserTouch: " + String.valueOf(isUserTouch));

        mSortBySpinner.setOnTouchListener(new AdapterView.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isUserTouch = true;
                return false;
            }
        });

        mSortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d(TAG, "onItemSelected - isUserTouch: " + String.valueOf(isUserTouch));
                // Prevent from running on the first time
                if (!isUserTouch) {
                    return;
                } else {
                    Log.d(TAG, "Sort by: " + adapterView.getItemAtPosition(i).toString());
                    getDataFromApi(i);
                }

                isUserTouch = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Register a BroadCasterReceiver to know when internet connection as changed -->
        internetStatusChange = new NetworkChangeReceiver();
        registerReceiver(internetStatusChange,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        // <--

        // Initialize mMovies List based if new activity or screen has rotated -->
        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            getDataFromApi(0);
        }
        else {
            mMovies = savedInstanceState.getParcelableArrayList("movies");
        }
        // <--

        // Change Spinner place according screen orientation -->
        RelativeLayout.LayoutParams spinnerParams = (RelativeLayout.LayoutParams) mSortBySpinner.getLayoutParams();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spinnerParams.addRule(RelativeLayout.END_OF, R.id.activity_main_title);
        }
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            spinnerParams.removeRule(RelativeLayout.ABOVE);
            spinnerParams.addRule(RelativeLayout.BELOW, R.id.activity_main_title);

            RelativeLayout.LayoutParams moviesGridParams = (RelativeLayout.LayoutParams) mMoviesGridRecyclerView.getLayoutParams();
            moviesGridParams.removeRule(RelativeLayout.BELOW);
            moviesGridParams.addRule(RelativeLayout.BELOW, R.id.sp_sort_by);
        }
        // <--

        // Set RecyclerView configurations (LayoutManager & Adapter) -->
        gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        mMoviesGridRecyclerView.setLayoutManager(gridLayoutManager);

        moviesAdapter = new MoviesAdapter(mMovies);
        mMoviesGridRecyclerView.setAdapter(moviesAdapter);
        // <--
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstance - isUserTouch: " + String.valueOf(isUserTouch));
        outState.putParcelableArrayList("movies", new ArrayList<>(mMovies));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(internetStatusChange);
    }

    private void initViews() {
        mSortBySpinner = (Spinner) findViewById(R.id.sp_sort_by);
        mMoviesGridRecyclerView = (RecyclerView) findViewById(R.id.rv_movies_grid);
        mErrorMessageTextView = (TextView) findViewById(R.id.tv_main_error_message);
        mLoadingProgressBar = (ProgressBar) findViewById(R.id.pb_main_loading);
    }

    private void showErrorMessage(String message) {
        mErrorMessageTextView.setText(message);
        mMoviesGridRecyclerView.setVisibility(View.GONE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    private void hideErrorMessage() {
        mErrorMessageTextView.setVisibility(View.GONE);
        mMoviesGridRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        mMoviesGridRecyclerView.setVisibility(View.GONE);
        mLoadingProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mLoadingProgressBar.setVisibility(View.GONE);
        mMoviesGridRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     *
     * @param sort_by_option int that represents the position of the option
     *                       on the "sort_by_options" array
     */
    public boolean getDataFromApi(int sort_by_option) {
        hideErrorMessage();
        showProgressBar();

        Uri uri = ApiUtils.getMoviesUriBuilder(getApplicationContext())
                .appendPath(getResources().getStringArray(R.array.sort_by_options)[sort_by_option])
                .build();

        URL url = NetworkUtils.builURL(uri);

        if (NetworkUtils.isOnline(getApplicationContext())) {
            new ApiQueryTask().execute(url);
            return true;
        }
        else {
            hideProgressBar();
            showErrorMessage("Sorry, without internet connection!");
            return false;
        }
    }


    public class ApiQueryTask extends AsyncTask<URL, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(URL... urls) {
            URL url = urls[0];
            try {
                String response = NetworkUtils.getResponseFromHttpUrl(url);
                Log.d("ApiQueryTask", "API response: " + response);
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                return ApiUtils.getMoviesListFromJsonArray(jsonArray.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);

            mMovies = movies;
            moviesAdapter.setMoviesData(movies);
            moviesAdapter.notifyDataSetChanged();
            mMoviesGridRecyclerView.scrollToPosition(0);

            hideProgressBar();
            hideErrorMessage();
        }
    }

    /**
     * Broadcast Receiver to check network status change
     * and execute
     */
    public class NetworkChangeReceiver extends BroadcastReceiver {

        private static final String LOG_TAG = "NetworkChangeReceiver";
        private boolean isConnected = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (isInitialStickyBroadcast())
                return;
            Log.v(LOG_TAG, "Receieved notification about network status");
            isNetworkAvailable(context);
        }

        private boolean isNetworkAvailable(Context context) {
            int position = mSortBySpinner.getSelectedItemPosition();
            isConnected = getDataFromApi(position);
            return isConnected;
        }
    }
}
