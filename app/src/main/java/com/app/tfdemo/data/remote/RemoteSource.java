package com.app.tfdemo.data.remote;

import io.reactivex.Single;

/**
 * Created by hardik on 20/2/19.
 */

interface RemoteSource {
    Single getVideos(String searchKey, int limit, String api_key, int page);
}
