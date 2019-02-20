package com.app.tfdemo.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tfdemo.R;
import com.app.tfdemo.ui.base.listeners.ActionBarView;
import com.app.tfdemo.ui.base.listeners.BaseView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by hardik on 20/2/19.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseView,
        ActionBarView {

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @BindView(R.id.ic_toolbar_setting)
    ImageView icSettings;

    @Nullable
    @BindView(R.id.ic_toolbar_refresh)
    ImageView icHome;

    private Unbinder unbinder;

    protected abstract void initializeDagger();

    protected abstract void initializeRouter(Bundle savedInstanceState);

    public abstract int getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        initializeDagger();
        initializeRouter(savedInstanceState);
        initializeToolbar();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void initializeToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
        }
    }

    @Override
    public void setUpIconVisibility(boolean visible) {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(visible);
        }
    }

    @Override
    public void setTitle(String titleKey) {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            TextView title = ButterKnife.findById(this, R.id.txt_toolbar_title);
            if (title != null) {
                title.setText(titleKey);
            }
        }
    }

    @Override
    public void setSettingsIconVisibility(boolean visible) {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            ImageView icon = ButterKnife.findById(this, R.id.ic_toolbar_setting);
            if (icon != null) {
                icon.setVisibility(visible ? VISIBLE : GONE);
            }
        }
    }

    @Override
    public void setRefreshVisibility(boolean visible) {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            ImageView icon = ButterKnife.findById(this, R.id.ic_toolbar_refresh);
            if (icon != null) {
                icon.setVisibility(visible ? VISIBLE : GONE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
