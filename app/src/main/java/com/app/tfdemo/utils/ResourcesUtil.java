package com.app.tfdemo.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.app.tfdemo.GiphyDemo;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;

/**
 * Created by hardik on 20/2/19.
 */

public class ResourcesUtil {
    private static Context context = GiphyDemo.getContext();
    private static Resources.Theme theme = GiphyDemo.getContext().getTheme();

    public static Drawable getDrawableById(int resId) {
        return SDK_INT >= LOLLIPOP ? context.getResources().getDrawable(resId, theme) :
            context.getResources().getDrawable(resId);
    }
    public static String getString(int resId) {
        return SDK_INT >= LOLLIPOP ? context.getResources().getString(resId) :
                context.getResources().getString(resId);
    }
}
