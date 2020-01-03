package com.zhongsm.aehearsal;

import android.widget.Toast;

import com.zhongsm.commlib.android.BaseActivity;
import com.zhongsm.wechatlib.bean.WXInvoiceEvent;
import com.zhongsm.commlib.constant.ServiceCode;
import com.zhongsm.commlib.utils.LogUtil;
import com.zhongsm.wechatlib.util.WXCardSelectorUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends BaseActivity {
    private static long lastBackPressTime;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void runOnCreate() {
        ButterKnife.bind(this);
    }

    @Override
    protected void runOnResume() {

    }

    /** 微信发票工具类，一个UI中应该只存在一个，否则会因为EventBus出发多次 */
    private WXCardSelectorUtil wxCardSelectorUtil;

    @OnClick(R.id.btn_wxCard)
    protected void ChooseInvoice() {
        if (wxCardSelectorUtil == null) {
            wxCardSelectorUtil = new WXCardSelectorUtil(this) {
                @Override
                public void onReceiveData(WXInvoiceEvent event) {
                    LogUtil.d("WangJ", "" + event.getInvoiceString());

                    getInvoiceInfo(event);
                }
            };
        }
        wxCardSelectorUtil.execute();
    }

    private void getInvoiceInfo(WXInvoiceEvent event) {
        try {
            JSONArray array = new JSONArray(event.getCardItemList());
            if (array.length() <= 0) {
                LogUtil.e("WangJ", "WXInvoiceEvent's CardList size is 0");
                return;
            }

            JSONObject jsonObject = (JSONObject) array.get(0);
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
            LogUtil.e(TAG, e.getMessage());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wxCardSelectorUtil = null;
    }
}
