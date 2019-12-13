package com.zhongsm.commlib.android;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zhongsm.commlib.R;

public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 设置Activity页面资源
     */
    protected abstract int getLayoutResource();

    protected abstract void runOnCreate();

    protected abstract void runOnResume();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置页面
        setContentView(getLayoutResource() > 0 ? getLayoutResource() : R.layout.layout_activity_not_set);

        runOnCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        runOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Resources getResources() {
        return super.getResources();
    }
}
