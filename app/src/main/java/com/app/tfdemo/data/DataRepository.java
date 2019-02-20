package com.app.tfdemo.data;

import com.app.tfdemo.data.local.LocalRepository;
import com.app.tfdemo.data.remote.RemoteRepository;
import com.app.tfdemo.data.remote.dto.Example;

import javax.inject.Inject;

import io.reactivex.Single;

/**
 * Created by hardik on 20/2/19.
 */

public class DataRepository implements DataSource {
    private RemoteRepository remoteRepository;
    private LocalRepository localRepository;

    @Inject
    public DataRepository(RemoteRepository remoteRepository, LocalRepository localRepository) {
        this.remoteRepository = remoteRepository;
        this.localRepository = localRepository;
    }

    @Override
    public Single<Example> requestVideos(String searchKey, int limit, String api_key, int page) {
        return remoteRepository.getVideos( searchKey,  limit,  api_key,  page);
    }
}
