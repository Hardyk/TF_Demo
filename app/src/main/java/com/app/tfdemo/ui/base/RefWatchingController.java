package com.app.tfdemo.ui.base;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.app.tfdemo.GiphyDemo;
import com.bluelinelabs.conductor.ControllerChangeHandler;
import com.bluelinelabs.conductor.ControllerChangeType;

public abstract class RefWatchingController extends ButterKnifeController {

    protected RefWatchingController() { }
    protected RefWatchingController(Bundle args) {
        super(args);
    }

    private boolean hasExited;

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (hasExited) {
            GiphyDemo.refWatcher.watch(this);
        }
    }

    @Override
    protected void onChangeEnded(@NonNull ControllerChangeHandler changeHandler, @NonNull ControllerChangeType changeType) {
        super.onChangeEnded(changeHandler, changeType);

        hasExited = !changeType.isEnter;
        if (isDestroyed()) {
            GiphyDemo.refWatcher.watch(this);
        }
    }
}
