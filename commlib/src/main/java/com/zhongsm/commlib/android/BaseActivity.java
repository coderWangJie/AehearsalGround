package com.zhongsm.commlib.android;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zhongsm.commlib.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity 基类
 *
 * @author WangJ
 * @since 2020/6/30
 * @version 0.1
 */
public abstract class BaseActivity extends AppCompatActivity {
    /** 日志TAG  */
    protected static String TAG;

    private Unbinder unbinder;


    /**
     * 设置Activity页面资源
     */
    protected abstract int getLayoutResource();

    protected abstract void runOnCreate();

    protected abstract void runOnResume();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = getClass().getSimpleName();

        // 设置页面
        setContentView(getLayoutResource() > 0 ? getLayoutResource() : R.layout.layout_activity_not_set);

        unbinder = ButterKnife.bind(this);

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
        unbinder.unbind();
    }

    @Override
    public Resources getResources() {
        return super.getResources();
    }
}
