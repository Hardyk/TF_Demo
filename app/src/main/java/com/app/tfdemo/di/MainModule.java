package com.app.tfdemo.di;


import com.app.tfdemo.data.local.LocalRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by hardik on 20/2/19.
 */

@Module
public class MainModule {
    @Provides
    @Singleton
    public LocalRepository provideLocalRepository() {
        return new LocalRepository();
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        Gson gson = new GsonBuilder().create();
        return gson;
    }

    @Provides
    public CompositeDisposable provideCompositeSubscription() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        return compositeDisposable;
    }
}
