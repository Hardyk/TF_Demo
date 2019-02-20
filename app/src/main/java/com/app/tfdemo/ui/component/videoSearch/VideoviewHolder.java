package com.app.tfdemo.ui.component.videoSearch;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.app.tfdemo.R;
import com.app.tfdemo.data.remote.dto.Datum;
import com.app.tfdemo.misc.FourThreeImageView;
import com.app.tfdemo.ui.base.listeners.RecyclerItemListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.TextUtils.isEmpty;
import static com.app.tfdemo.utils.ResourcesUtil.getDrawableById;

/**
 * Created by hardik on 20/2/19.
 */

public class VideoviewHolder extends RecyclerView.ViewHolder {

    private RecyclerItemListener onItemClickListener;

    @BindView(R.id.grid_item_iv)
    FourThreeImageView imageView;
    @BindView(R.id.grid_item_name)
    TextView tvTitle;
    @BindView(R.id.grid_item_main)
    FrameLayout videoItemView;

    private int page = 1;
    private int visibleThreshold = 1;
    private int lastVisibleItem;
    private int totalItemCount;

    public VideoviewHolder(View itemView, RecyclerItemListener onItemClickListener) {
        super(itemView);
        this.onItemClickListener = onItemClickListener;
        ButterKnife.bind(this, itemView);
    }

    public void bind(int position, Datum video, RecyclerItemListener recyclerItemListener) {
        if (!isEmpty(video.getTitle())) {
            tvTitle.setText(video.getId());
            tvTitle.setTextColor(Color.RED);
            tvTitle.setVisibility(View.GONE);
        }
        String URL = null;

        try {
            Picasso.with(imageView.getContext()).load(video.getImages().getFixedWidthSmallStill().getUrl()).placeholder(getDrawableById(R.mipmap.ic_launcher)).into(imageView);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        videoItemView.setOnClickListener(v -> recyclerItemListener.onItemSelected(position));
    }

}

