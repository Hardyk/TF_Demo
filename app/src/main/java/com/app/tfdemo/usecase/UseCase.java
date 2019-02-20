package com.app.tfdemo.usecase;

import com.app.tfdemo.ui.base.listeners.BaseCallback;

/**
 * Created by hardik on 20/2/19.
 */

public interface UseCase {
    void getVideos(final BaseCallback callback, String search, int page);
}
