package com.zhongsm.aehearsal;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zhongsm.commlib.android.BaseActivity;
import com.zhongsm.commlib.bean.WXInvoiceEvent;
import com.zhongsm.commlib.constant.ServiceCode;
import com.zhongsm.commlib.utils.LogUtil;
import com.zhongsm.commlib.utils.WXCardSelectorUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static long lastBackPressTime;

    private Button btnGetWeChatCard;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void runOnCreate() {
        btnGetWeChatCard = findViewById(R.id.btn_wxCard);

        btnGetWeChatCard.setOnClickListener(this);
    }

    @Override
    protected void runOnResume() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_wxCard:
                WXCardSelectorUtil util = new WXCardSelectorUtil(this) {
                    @Override
                    public void onReceiveData(WXInvoiceEvent event) {
                        LogUtil.d("WangJ", "" + event.getInvoiceString());

                        getInvoiceInfo(event);
                    }
                };
                util.execute();
                break;
        }
    }

    private void getInvoiceInfo(WXInvoiceEvent event) {
        try {
            JSONObject jsonObject = (JSONObject) new JSONArray(event.getCardItemList()).get(0);


            RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(String.format(ServiceCode.WX_INVOICE_INFO_SINGLE, event.getAccessToken()))
                    .post(body)
                    .build();
            Call call = new OkHttpClient().newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    LogUtil.e("WangJ", e.getMessage());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String data = response.body().string();
                    LogUtil.e("WangJ", data);

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        long newBackPressTime = System.currentTimeMillis();
        if (newBackPressTime - lastBackPressTime > 1000) {
            lastBackPressTime = newBackPressTime;
            Toast.makeText(this, R.string.hint_double_click_to_quit, Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }
}
