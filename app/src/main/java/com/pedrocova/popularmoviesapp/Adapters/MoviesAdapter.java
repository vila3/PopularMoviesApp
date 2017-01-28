package com.pedrocova.popularmoviesapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pedrocova.popularmoviesapp.Movie;
import com.pedrocova.popularmoviesapp.MovieActivity;
import com.pedrocova.popularmoviesapp.R;
import com.pedrocova.popularmoviesapp.Utils.ApiUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by pedro on 25/01/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.FilmHolder> {

    private int mNumberItems = 0;
    private List<Movie> mMovies;

    public MoviesAdapter(List<Movie> movies) {
        if (movies != null)
            mNumberItems = movies.size();
        mMovies = movies;
    }

    @Override
    public FilmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.movie_grid_cell, parent, false);

        return new FilmHolder(view);
    }

    @Override
    public void onBindViewHolder(FilmHolder holder, int position) {
        final Movie movie = mMovies.get(position);
        final Context context = holder.itemView.getContext();

        Uri imageUri = ApiUtils.getImageUri(context, movie.getPoster_path());
        Log.d("FilmsAdapter", "Poster url: " + imageUri.toString());
        Picasso.with(context).load(imageUri.toString()).placeholder(ContextCompat.getDrawable(context, R.drawable.default_image)).into(holder.imageViewCover);

        holder.textViewTitle.setText(movie.getOriginal_title());

        holder.parent.setTag(String.valueOf(movie.getId()));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startMovieActivityIntent = new Intent(context, MovieActivity.class);
                startMovieActivityIntent.putExtra("movie", new Gson().toJson(movie));

                context.startActivity(startMovieActivityIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public void setMoviesData(List<Movie> movies) {
        if (movies != null)
            mNumberItems = movies.size();
        else
            mNumberItems = 0;
        mMovies = movies;
    }

    class FilmHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        ImageView imageViewCover;
        View parent;

        FilmHolder(View itemView) {
            super(itemView);
            parent = itemView;
            textViewTitle = (TextView) itemView.findViewById(R.id.tv_movie_title);
            imageViewCover = (ImageView) itemView.findViewById(R.id.img_movie_cover);
        }
    }
}
