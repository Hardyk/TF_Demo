package com.app.tfdemo.data;

import com.app.tfdemo.data.remote.dto.Example;

import io.reactivex.Single;

/**
 * Created by hardik on 20/2/19.
 */

interface DataSource {
    Single<Example> requestVideos(String searchKey, int limit, String api_key, int page);
}
