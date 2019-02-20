package com.app.tfdemo.ui.component.videoSearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.tfdemo.R;
import com.app.tfdemo.data.remote.dto.Datum;
import com.app.tfdemo.ui.base.BaseController;
import com.app.tfdemo.ui.component.videoPlay.VideoPlayController;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by hardik on 20/2/19.
 */

public class VideoGridController extends BaseController implements VideoGridContract.View {

    VideoGridPresenter presenter;

    @BindView(R.id.rv_video_list)
    RecyclerView rvVideos;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;
    @BindView(R.id.rl_video_list)
    RelativeLayout rlVideoList;
    @BindView(R.id.btn_search)
    ImageButton btnSearch;
    @BindView(R.id.et_search)
    EditText editTextSearch;

    private GridLayoutManager gridLayoutManager;
    private VideoGridAdapter videoGridAdapter;
    private List<Datum> mVideoList;

    private RecyclerView.OnScrollListener mScrollListner;
    private boolean isLoading = false;
    private int mCurrentPage = 1;

    public VideoGridController() {

    }

    public VideoGridController(VideoGridPresenter mPresenter) {
        this.presenter = mPresenter;
        mVideoList = new ArrayList<>();
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.content_video_search, container, false);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        presenter.setView(this);
        if (presenter != null) {
            presenter.initialize(getActivity().getIntent().getExtras());
        }

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvVideos.setLayoutManager(gridLayoutManager);
        rvVideos.setHasFixedSize(true);
        videoGridAdapter = new VideoGridAdapter(presenter.getRecyclerItemListener(), mVideoList);
        rvVideos.setAdapter(videoGridAdapter);

        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideSoftInput(view, getActivity());
                    refreshVideoList();
                    presenter.onSearchClick(editTextSearch.getText().toString(), mCurrentPage);
                    return true;
                }
                return false;
            }
        });

        if (mVideoList.size() >= 15) {
            setNoDataVisibility(false);
            initializeScrollListner();
        }
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        if (presenter != null) {
            presenter.start();
        }
    }

    @Override
    protected void onDetach(@NonNull View view) {
        super.onDetach(view);
        if (presenter != null) {
            presenter.finalizeView();
        }
    }

    @Override
    public void setLoaderVisibility(boolean isVisible) {
        pbLoading.setVisibility(isVisible ? VISIBLE : GONE);
    }

    @Override
    public void initializeVideosGrid(List<Datum> mVideos) {
        mVideoList.addAll(mVideos);

        videoGridAdapter.notifyDataSetChanged();

        if (mCurrentPage == 0 && mVideos.size() == 15) {
            initializeScrollListner();
        }
        isLoading = false;
    }

    @Override
    public void navigateToDetailsScreen(int position) {
        getRouter().pushController(RouterTransaction.with(new VideoPlayController(VideoGridController.this, mVideoList.get(position)))
                .popChangeHandler(new FadeChangeHandler())
                .pushChangeHandler(new FadeChangeHandler()));

    }

    @Override
    public void setNoDataVisibility(boolean isVisible) {
        tvNoData.setVisibility(isVisible ? VISIBLE : GONE);
    }

    @Override
    public void setListVisibility(boolean isVisible) {
        rvVideos.setVisibility(isVisible ? VISIBLE : GONE);
        if (mVideoList.size() == 0) {
            showNoVideoError();
        }
    }

    @OnClick({R.id.btn_search})
    public void onClick(View view) {
        hideSoftInput(view, getActivity());
        switch (view.getId()) {
            case R.id.btn_search:
                refreshVideoList();
                presenter.onSearchClick(editTextSearch.getText().toString(), mCurrentPage);
        }
    }

    private void initializeScrollListner() {
        if (mScrollListner == null) {
            mScrollListner = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visibleItemCount = gridLayoutManager.getChildCount();
                    int totalItemCount = gridLayoutManager.getItemCount();
                    int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();

                    if (!isLoading) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                            isLoading = true;
                            mCurrentPage++;
                            presenter.getVideos(editTextSearch.getText().toString().trim(), mCurrentPage);
                        }
                    }
                }
            };
        }
        registerScrollListener();
    }

    public void registerScrollListener() {
        if (mScrollListner != null) {
            rvVideos.addOnScrollListener(mScrollListner);
        }
    }

    public void unRegisterScrollListener() {
        if (mScrollListner != null) {
            rvVideos.removeOnScrollListener(mScrollListner);
        }
    }

    private void refreshVideoList() {
        mVideoList.clear();
        mCurrentPage = 0;
        if (videoGridAdapter != null) {
            videoGridAdapter.notifyDataSetChanged();
        }
        unRegisterScrollListener();
    }

    @Override
    public void showSearchError() {
        Snackbar.make(rlVideoList, "Search Error", LENGTH_SHORT).show();
    }

    @Override
    public void showNoVideoError() {
        Snackbar.make(rlVideoList, "No Videos available", LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroyView(View view) {
        super.onDestroyView(view);
        presenter.unSubscribe();
    }

    public static void hideSoftInput(View view, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
