package com.zhongsm.aehearsal.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.zhongsm.aehearsal.BuildConfig;
import com.zhongsm.aehearsal.R;
import com.zhongsm.commlib.android.BaseActivity;
import com.zhongsm.commlib.utils.AppVersionUtil;
import com.zhongsm.commlib.utils.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity {

    /**
     * Splash页面倒计时时长
     */
    private int delaySecond = 5;
    private MyHandler handler;
    private int msgWhat = 0;

    @BindView(R.id.tvSkip)
    public TextView tvSkip;

    @BindView(R.id.tvVersionInfo)
    TextView tvVersion;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void runOnCreate() {
        handler = new MyHandler();

        tvVersion.setText((BuildConfig.DEBUG ? "Debug" : "Release")
                .concat("\n").concat(BuildConfig.ReleaseTime)
                .concat("\nv").concat(AppVersionUtil.getVersionName(this)));
    }

    @Override
    protected void runOnResume() {
        handler.sendEmptyMessage(msgWhat);
    }

    @OnClick(R.id.tvSkip)
    protected void jumpToStart() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // Splash页不允许手动退出
    }

    @Override
    public void onStop() {
        super.onStop();

        if (handler.hasMessages(msgWhat)) {
            LogUtil.d(TAG, "handler's MessageQueue not null and clear");
            handler.removeMessages(msgWhat);
        }
        handler = null;
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0) {
                if (delaySecond >= 0) {
                    tvSkip.setText(String.valueOf(delaySecond--).concat("s 跳过"));
                    sendEmptyMessageDelayed(msgWhat, 1000);
                } else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}
