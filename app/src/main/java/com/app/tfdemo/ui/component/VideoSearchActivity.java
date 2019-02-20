package com.app.tfdemo.ui.component;

import android.os.Bundle;

import com.app.tfdemo.GiphyDemo;
import com.app.tfdemo.R;
import com.app.tfdemo.ui.base.BaseActivity;
import com.app.tfdemo.ui.component.videoSearch.VideoGridController;
import com.app.tfdemo.ui.component.videoSearch.VideoGridPresenter;
import com.bluelinelabs.conductor.ChangeHandlerFrameLayout;
import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by hardik on 20/2/19.
 */

public class VideoSearchActivity extends BaseActivity {

    @Inject
    VideoGridPresenter mPresenter;

    @BindView(R.id.activity_video_search_controller_container)
    ChangeHandlerFrameLayout container;

    private Router mRouter;

    @Override
    protected void initializeDagger() {
        GiphyDemo app = (GiphyDemo) getApplicationContext();
        app.getMainComponent().inject(VideoSearchActivity.this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.video_search_activity;
    }

    @Override
    protected void initializeRouter(Bundle savedInstanceState) {
        mRouter = Conductor.attachRouter(this, container, savedInstanceState);
        if (!mRouter.hasRootController()) {
            mRouter.setRoot(RouterTransaction.with(new VideoGridController(mPresenter)).popChangeHandler(new FadeChangeHandler())
                    .pushChangeHandler(new FadeChangeHandler()));
        }
    }

    @Override
    public void onBackPressed() {
        if (!mRouter.handleBack()) {
            super.onBackPressed();
        }
    }
}
