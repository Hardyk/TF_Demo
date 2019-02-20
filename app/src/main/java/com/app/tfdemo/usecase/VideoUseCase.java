package com.app.tfdemo.usecase;

import com.app.tfdemo.data.DataRepository;
import com.app.tfdemo.data.remote.dto.Example;
import com.app.tfdemo.ui.base.listeners.BaseCallback;
import com.app.tfdemo.utils.Constants;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by hardik on 20/2/19.
 */

public class VideoUseCase implements UseCase {
    private DataRepository dataRepository;
    private CompositeDisposable compositeDisposable;
    private Disposable newsDisposable;
    Single<Example> newsModelSingle;
    private DisposableSingleObserver<Example> disposableSingleObserver;

    @Inject
    public VideoUseCase(DataRepository dataRepository, CompositeDisposable compositeDisposable) {
        this.dataRepository = dataRepository;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void getVideos(BaseCallback callback, String search, int page) {
        disposableSingleObserver = new DisposableSingleObserver<Example>() {
            @Override
            public void onSuccess(Example newsModel) {
                callback.onSuccess(newsModel);
            }

            @Override
            public void onError(Throwable e) {
                callback.onFail();
            }
        };
        if (!compositeDisposable.isDisposed()) {
            newsModelSingle = dataRepository.requestVideos(search, 15, Constants.KEY, page*15);
            newsDisposable = newsModelSingle.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribeWith(disposableSingleObserver);
            compositeDisposable.add(newsDisposable);
        }
    }

//    @Override
//    public Example searchByTitle(List<Example> news, String keyWord) {
//        for (Example newsItem : news) {
//            if (newsItem.getData().toLowerCase().contains(keyWord.toLowerCase())) {
//                return newsItem;
//            }
//        }
//        return null;
//    }

    public void unSubscribe() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.remove(newsDisposable);
        }
    }
}
