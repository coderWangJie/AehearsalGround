package com.zhongsm.commlib.utils;

import android.text.TextUtils;

public class ServiceManager {
    private static final String TAG = ServiceManager.class.getSimpleName();

    static String serviceHost = "";
    static String resourceHost = "";

    public static String getServiceUrl(String path) {
        if (TextUtils.isEmpty(path)) {
            LogUtil.e(TAG, "");
            return "";
        }

        if (path.toLowerCase().startsWith("http://")
                || path.toLowerCase().startsWith("https://")) {
            return path;
        }
//        serviceHost
        return "";
    }
}
