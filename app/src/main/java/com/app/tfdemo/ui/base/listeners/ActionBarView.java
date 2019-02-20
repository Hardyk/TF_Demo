package com.app.tfdemo.ui.base.listeners;

/**
 * Created by hardik on 20/2/19.
 */

public interface ActionBarView {

    void setUpIconVisibility(boolean visible);

    void setTitle(String titleKey);

    void setSettingsIconVisibility(boolean visibility);

    void setRefreshVisibility(boolean visibility);
}
