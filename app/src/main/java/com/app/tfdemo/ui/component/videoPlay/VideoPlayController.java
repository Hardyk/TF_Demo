package com.app.tfdemo.ui.component.videoPlay;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tfdemo.R;
import com.app.tfdemo.data.remote.dto.Datum;
import com.app.tfdemo.objectBox.SelectedVideo;
import com.app.tfdemo.ui.base.BaseController;
import com.app.tfdemo.ui.component.VideoSearchActivity;
import com.app.tfdemo.ui.component.videoSearch.VideoGridController;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hardik on 20/2/19.
 */

public class VideoPlayController extends BaseController {

    @BindView(R.id.details_tvDownVote)
    TextView tvTxtDownVote;

    @BindView(R.id.details_tvUpVote)
    TextView tvTxtUpVote;

    @BindView(R.id.details_player_view)
    SimpleExoPlayerView simpleExoPlayerView;

    @BindView(R.id.exo_controller)
    ImageView ivHideControllerButton;

    private Datum _video;
    private SimpleExoPlayer player;

    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;

    protected VideoPlayPresenter videoPlayPresenter;

    private SelectedVideo selectedVideo;

    public VideoPlayController(@Nullable Bundle args) {
        super(args);
    }

    public VideoPlayController(VideoGridController listener, Datum _video) {
        this._video = _video;
        setTargetController(listener);
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.details_layout, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        ((VideoSearchActivity)getActivity()).setTitle("Video");
        videoPlayPresenter = new VideoPlayPresenter(getActivity());
        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();

        getSelectedVideoVotes();
    }


    private void getSelectedVideoVotes() {
        selectedVideo = videoPlayPresenter.checkVideVotes(_video.getId());
        if (selectedVideo != null) {
            updateVotes(tvTxtUpVote, selectedVideo.getUpVote());
            updateVotes(tvTxtDownVote, selectedVideo.getDownVote());
        } else {
            updateVotes(tvTxtUpVote, 0);
            updateVotes(tvTxtDownVote, 0);
        }
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializeExoPlayer();
        }
    }

    @Override
    protected void onDetach(@NonNull View view) {
        super.onDetach(view);
        if (Util.SDK_INT <= 23) {
            releaseExoPlayer();
        }
    }

    @Override
    protected void onDestroyView(View view) {
        super.onDestroyView(view);
        if (Util.SDK_INT <= 23) {
            releaseExoPlayer();
        }
    }

    @Override
    public boolean handleBack() {
        return super.handleBack();
    }

    @OnClick({R.id.details_tvUpVote, R.id.details_tvDownVote})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.details_tvUpVote:
                selectedVideo = videoPlayPresenter.updateVideoVote(_video.getId(), VideoPlayPresenter.UPDATE_UP_VOTE);
                updateVotes(tvTxtUpVote, selectedVideo.getUpVote());
                break;
            case R.id.details_tvDownVote:
                selectedVideo = videoPlayPresenter.updateVideoVote(_video.getId(), VideoPlayPresenter.UPDATE_DOWN_VOTE);
                updateVotes(tvTxtDownVote, selectedVideo.getDownVote());
                break;
        }
    }

    private void updateVotes(TextView tvVote, int vote) {
        tvVote.setText(vote + " Votes");
    }

    private void initializeExoPlayer() {

        simpleExoPlayerView.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

        simpleExoPlayerView.setPlayer(player);

        player.setPlayWhenReady(shouldAutoPlay);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(_video.getImages().getOriginal().getMp4()),
                mediaDataSourceFactory, extractorsFactory, null, null);

        player.prepare(mediaSource);

        ivHideControllerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleExoPlayerView.hideController();
            }
        });
    }

    private void releaseExoPlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
    }


}
