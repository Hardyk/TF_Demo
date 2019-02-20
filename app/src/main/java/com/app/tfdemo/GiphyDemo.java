package com.app.tfdemo;

import android.app.Application;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.app.tfdemo.data.remote.dto.Datum;
import com.app.tfdemo.di.DaggerMainComponent;
import com.app.tfdemo.di.MainComponent;
import com.app.tfdemo.objectBox.MyObjectBox;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.List;

import io.objectbox.BoxStore;

/**
 * Created by hardik on 20/2/19.
 */

public class GiphyDemo extends Application {
    private MainComponent mainComponent;
    private static Context context;

    private BoxStore boxStore;
    private static GiphyDemo application;
    private static List<Datum> datas;

    public static RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        mainComponent = DaggerMainComponent.create();
        context = getApplicationContext();
        application = this;
        boxStore = MyObjectBox.builder().androidContext(this).build();
        refWatcher = LeakCanary.install(this);
    }

    public static GiphyDemo getApplication() {
        return application;
    }

    public MainComponent getMainComponent() {
        return mainComponent;
    }

    public static Context getContext() {
        return context;
    }

    @VisibleForTesting
    public void setComponent(MainComponent mainComponent) {
        this.mainComponent = mainComponent;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        context = null;
    }


    public BoxStore getBoxStore() {
        return boxStore;
    }
}
