package com.pedrocova.popularmoviesapp.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.pedrocova.popularmoviesapp.AsyncTaskCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by pedro on 30/01/2017.
 */

public class ApiQueryTask extends AsyncTask<URL, Void, JSONObject> {

    private static final String TAG = "FetchMyDataTask";

    private Context context;
    private AsyncTaskCompleteListener<JSONObject> listener;

    public ApiQueryTask(Context context, AsyncTaskCompleteListener<JSONObject> listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected JSONObject doInBackground(URL... urls) {
        URL url = urls[0];
        Log.d(TAG, url.toString());
        try {
            String response = NetworkUtils.getResponseFromHttpUrl(url);
            Log.d("ApiQueryTask", "API response: " + response);
            return new JSONObject(response);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        try {
            listener.onTaskComplete(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
