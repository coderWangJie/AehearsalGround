package com.zhongsm.aehearsal.ui;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhongsm.aehearsal.R;
import com.zhongsm.aehearsal.ui.card.CardPackage;
import com.zhongsm.commlib.android.BaseActivity;
import com.zhongsm.commlib.constant.Constant;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    /**
     *
     */
    private static long lastBackPressTime;

    @BindView(R.id.ivTest)
    ImageView ivTest;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void runOnCreate() {
        Glide.with(this).load(Constant.imgResourceUrl).into(ivTest);
    }

    @Override
    protected void runOnResume() {
    }

    @OnClick(R.id.btn_wxCard)
    protected void gotoInvoiceCard() {
        Intent intent = new Intent(this, CardPackage.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        long newBackPressTime = System.currentTimeMillis();
        if (newBackPressTime - lastBackPressTime > 2000) {
            lastBackPressTime = newBackPressTime;
            Toast.makeText(this, R.string.hint_double_click_to_quit, Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }
}
