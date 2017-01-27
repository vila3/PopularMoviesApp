package com.pedrocova.popularmoviesapp.Utils;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pedrocova.popularmoviesapp.Config.ApiConfig;
import com.pedrocova.popularmoviesapp.Movie;
import com.pedrocova.popularmoviesapp.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedro on 25/01/2017.
 */

public class ApiUtils {

    final static String API_KEY_PARAM_QUERY = "api_key";

    private static Uri.Builder getUriBuilder(Context context) {
        return Uri.parse(context.getString(R.string.api_url)).buildUpon().appendQueryParameter(API_KEY_PARAM_QUERY, ApiConfig.API_KEY);
    }

    public static Uri.Builder getMoviesUriBuilder(Context context) {
        return getUriBuilder(context).appendPath("movie");
    }

    public static List<Movie> getMoviesListFromJsonArray(String jsonString) {
        Type listType = new TypeToken<ArrayList<Movie>>(){}.getType();
        return new Gson().fromJson(jsonString, listType);
    }

    public static Uri getImageUri(Context context, String poster_path) {
        return Uri.parse(context.getString(R.string.api_images_url)).buildUpon()
                .appendPath("w185").appendEncodedPath(poster_path).build();
    }
}
