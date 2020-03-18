package com.zhongsm.commlib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppVersionUtil {
    private static final String TAG = AppVersionUtil.class.getSimpleName();

    /**
     * 获取APP版本号
     * @param context 上下文
     * @return app build.gradle中设置等versionName
     */
    public static String getVersionName(Context context) {
        String versionName = "";

        try {
            PackageInfo packageInfo = context.getApplicationContext().getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            if (packageInfo != null) {
                versionName = packageInfo.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e(TAG, "获取版本号失败：" + e.getMessage());
        }

        return versionName;
    }
}
