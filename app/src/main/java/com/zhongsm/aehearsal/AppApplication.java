package com.zhongsm.aehearsal;

import android.app.Application;
import android.util.Log;

import com.zhongsm.commlib.android.BaseApplication;
import com.zhongsm.commlib.utils.LogUtil;

/**
 * 项目Application
 * @author WangJ 2019-12-26
 */
public class AppApplication extends Application {
    private static final String TAG = AppApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "Application onCreate");

        LogUtil.setLogLevel(Log.DEBUG);

    }

    /**
     * 这个方法在程序结束的时候会调用
     * 但是这个方法只用于在Android仿真机测试的时候，在android产品机上是不会调用的，所以这个方法并没有什么用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
