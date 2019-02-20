package com.app.tfdemo.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by hardik on 20/2/19.
 */

public abstract class BaseController extends  RefWatchingController {

    private boolean mActive = false;


    public BaseController() {
    }

    public BaseController(Bundle args) {
    }

    @Override
    protected void onActivityStarted(@NonNull Activity activity) {
        super.onActivityStarted(activity);
    }

    @Override
    protected void onDestroyView(View view) {
        super.onDestroyView(view);
        setActive(false);
    }

    protected void setActive(boolean active) {
        mActive = active;
    }

    public boolean isActive() {
        return mActive;
    }


}