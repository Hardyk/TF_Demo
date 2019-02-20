package com.app.tfdemo.data.remote.service;

import com.app.tfdemo.data.remote.dto.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hardik on 20/2/19.
 */

public interface SearchVideoService {
    @GET("search")
    Call<Example> searchVideos(@Query("q") String q,
                               @Query("limit") int limit,
                               @Query("api_key") String api_key,
                               @Query("offset") int offset);
}
