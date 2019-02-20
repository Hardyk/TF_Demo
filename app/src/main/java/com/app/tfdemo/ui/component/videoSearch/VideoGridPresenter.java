package com.app.tfdemo.ui.component.videoSearch;

import android.os.Bundle;

import com.app.tfdemo.data.remote.dto.Datum;
import com.app.tfdemo.data.remote.dto.Example;
import com.app.tfdemo.ui.base.Presenter;
import com.app.tfdemo.ui.base.listeners.BaseCallback;
import com.app.tfdemo.ui.base.listeners.RecyclerItemListener;
import com.app.tfdemo.usecase.VideoUseCase;

import java.util.List;

import javax.inject.Inject;

import static com.app.tfdemo.utils.ObjectUtil.isEmpty;
import static com.app.tfdemo.utils.ObjectUtil.isNull;

/**
 * Created by hardik on 20/2/19.
 */

public class VideoGridPresenter extends Presenter<VideoGridContract.View> implements VideoGridContract.Presenter {

    private final VideoUseCase videoUseCase;
    private Example videoSearchData;

    @Inject
    public VideoGridPresenter(VideoUseCase videoUseCase) {
        this.videoUseCase = videoUseCase;
    }

    @Override
    public void initialize(Bundle extras) {
        super.initialize(extras);
    }

    @Override
    public void getVideos(String search_key, int page) {
        getView().setLoaderVisibility(true);
        getView().setNoDataVisibility(false);
        videoUseCase.getVideos(callback, search_key,page);
    }

    @Override
    public void unSubscribe() {
        videoUseCase.unSubscribe();
    }

    @Override
    public RecyclerItemListener getRecyclerItemListener() {
        return recyclerItemListener;
    }

    @Override
    public void onSearchClick(String search_key, int page) {
        if (!isEmpty(search_key)) {
            getView().setLoaderVisibility(true);
            getView().setNoDataVisibility(false);
//            getView().setListVisibility(false);
            videoUseCase.getVideos(callback, search_key,page);
        } else {
            getView().showSearchError();
        }
    }

    private void showList(boolean isVisible) {
        getView().setNoDataVisibility(!isVisible);
        getView().setListVisibility(isVisible);
    }

    private final RecyclerItemListener recyclerItemListener = position -> {
        getView().navigateToDetailsScreen((position));
    };

    private final BaseCallback callback = new BaseCallback() {
        @Override
        public void onSuccess(Example videoSearchData) {
            VideoGridPresenter.this.videoSearchData = videoSearchData;
            List<Datum> videoList = null;
            if (!isNull(videoSearchData)) {
                videoList = videoSearchData.getData();
            }
            if (!isNull(videoList) && !videoList.isEmpty()) {
                getView().initializeVideosGrid(videoList);
                showList(true);
            } else {
                showList(false);
            }
            getView().setLoaderVisibility(false);
        }

        @Override
        public void onFail() {
            showList(false);
            getView().setLoaderVisibility(false);
        }
    };
}
