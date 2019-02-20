package com.app.tfdemo.di;

import com.app.tfdemo.ui.component.VideoSearchActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by hardik on 20/2/19.
 */

@Singleton
@Component(modules = MainModule.class)
public interface MainComponent {
    void inject(VideoSearchActivity activity);
}
