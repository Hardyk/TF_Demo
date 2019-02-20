package com.app.tfdemo.ui.component.videoSearch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tfdemo.R;
import com.app.tfdemo.data.remote.dto.Datum;
import com.app.tfdemo.ui.base.listeners.RecyclerItemListener;

import java.util.List;

/**
 * Created by hardik on 20/2/19.
 */

public class VideoGridAdapter extends RecyclerView.Adapter<VideoviewHolder> {
    private final List<Datum> videos;
    private RecyclerItemListener onItemClickListener;

    public VideoGridAdapter(RecyclerItemListener onItemClickListener, List<Datum> videos) {
        this.onItemClickListener = onItemClickListener;
        this.videos = videos;
    }

    @Override
    public VideoviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new VideoviewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(VideoviewHolder holder, int position) {
        holder.bind(position, videos.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }
}

