package com.app.tfdemo.data.remote;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;
import android.util.Log;

import com.app.tfdemo.GiphyDemo;
import com.app.tfdemo.data.remote.dto.Example;
import com.app.tfdemo.data.remote.service.SearchVideoService;
import com.app.tfdemo.utils.Constants;
import com.app.tfdemo.utils.NetworkUtils;
import com.google.gson.Gson;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.plugins.RxJavaPlugins;
import retrofit2.Call;

import static com.app.tfdemo.data.remote.ServiceError.NETWORK_ERROR;
import static com.app.tfdemo.data.remote.ServiceError.SUCCESS_CODE;
import static com.app.tfdemo.utils.Constants.ERROR_UNDEFINED;
import static com.app.tfdemo.utils.NetworkUtils.isConnected;
import static com.app.tfdemo.utils.ObjectUtil.isNull;

/**
 * Created by hardik on 20/2/19.
 */

public class RemoteRepository implements RemoteSource {
    private ServiceGenerator serviceGenerator;
    private final String UNDELIVERABLE_EXCEPTION_TAG = "Undeliverable exception";

    @Inject
    public RemoteRepository(ServiceGenerator serviceGenerator) {
        this.serviceGenerator = serviceGenerator;
    }

    @Override
    public Single getVideos(String searchKey, int limit, String api_key, int page) {
        RxJavaPlugins.setErrorHandler(throwable -> {
            Log.i(UNDELIVERABLE_EXCEPTION_TAG, throwable.getMessage());
            return;
        });
        Single<Example> videoList = Single.create(singleOnSubscribe -> {
                    if (!isConnected(GiphyDemo.getContext())) {
                        Exception exception = new NetworkErrorException();
                        singleOnSubscribe.onError(exception);
                    } else {
                        try {
                            SearchVideoService newsService = serviceGenerator.createService(SearchVideoService.class, Constants.BASE_URL);
                            ServiceResponse serviceResponse = processCall(newsService.searchVideos(searchKey,limit,api_key,page), false);
                            if (serviceResponse.getCode() == SUCCESS_CODE) {
                                Example newsModel = (Example) serviceResponse.getData();
                                singleOnSubscribe.onSuccess(newsModel);
                            } else {
                                Throwable throwable = new NetworkErrorException();
                                singleOnSubscribe.onError(throwable);
                            }
                        } catch (Exception e) {
                            singleOnSubscribe.onError(e);
                        }
                    }
                }
        );
        return videoList;
    }

    @NonNull
    private ServiceResponse processCall(Call call, boolean isVoid) {
        if (!NetworkUtils.isConnected(GiphyDemo.getContext())) {
            return new ServiceResponse(new ServiceError());
        }
        try {
            retrofit2.Response response = call.execute();
            Gson gson = new Gson();
            if (isNull(response)) {
                return new ServiceResponse(new ServiceError(NETWORK_ERROR, ERROR_UNDEFINED));
            }
            int responseCode = response.code();
            if (response.isSuccessful()) {
                return new ServiceResponse(responseCode, isVoid ? null : response.body());
            } else {
                ServiceError ServiceError;
                ServiceError = new ServiceError(response.message(), responseCode);
                return new ServiceResponse(ServiceError);
            }
        } catch (IOException e) {
            return new ServiceResponse(new ServiceError(NETWORK_ERROR, ERROR_UNDEFINED));
        }
    }
}
