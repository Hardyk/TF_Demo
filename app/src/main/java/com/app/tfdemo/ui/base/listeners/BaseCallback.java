package com.app.tfdemo.ui.base.listeners;

import com.app.tfdemo.data.remote.dto.Example;

/**
 * Created by hardik on 20/2/19.
 */

public interface BaseCallback {
    void onSuccess(Example videos);

    void onFail();
}
