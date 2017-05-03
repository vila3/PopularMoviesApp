package com.pedrocova.popularmoviesapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pedrocova.popularmoviesapp.R;

import java.util.List;

/**
 * Created by pcova on 30-04-2017.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.SimpleViewHolder> {


    private List<String> dataSource;
    public TrailersAdapter(List<String> dataArgs){
        dataSource = dataArgs;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_row, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.textView.setText(dataSource.get(position));
    }

    @Override
    public int getItemCount() {
        if (dataSource == null)
            return 0;
        return dataSource.size();
    }

    static class SimpleViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        SimpleViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }
}
