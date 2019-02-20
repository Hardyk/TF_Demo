package com.app.tfdemo.ui.component.videoSearch;

import com.app.tfdemo.data.remote.dto.Datum;
import com.app.tfdemo.ui.base.listeners.BaseView;
import com.app.tfdemo.ui.base.listeners.RecyclerItemListener;

import java.util.List;

/**
 * Created by hardik on 20/2/19.
 */

public interface VideoGridContract {

    interface View extends BaseView {
        void initializeVideosGrid(List<Datum> news);

        void setLoaderVisibility(boolean isVisible);

        void navigateToDetailsScreen(int position);

        void setNoDataVisibility(boolean isVisible);

        void setListVisibility(boolean isVisible);

        void showSearchError();

        void showNoVideoError();
    }


    interface Presenter  {
        void getVideos(String search_key, int page);

        void onSearchClick(String newsTitle ,int page);

        void unSubscribe();

        RecyclerItemListener getRecyclerItemListener();
    }
}
