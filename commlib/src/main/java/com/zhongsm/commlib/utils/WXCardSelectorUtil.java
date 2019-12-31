package com.zhongsm.commlib.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbiz.ChooseCardFromWXCardPackage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhongsm.commlib.BuildConfig;
import com.zhongsm.commlib.bean.WXInvoiceEvent;
import com.zhongsm.commlib.constant.ServiceCode;
import com.zhongsm.commlib.constant.SharedPreferenceKey;

import org.apache.commons.codec.digest.DigestUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 拉取微信发票插件
 * 从微信发票夹多选发票，将发票信息回调给每刻H5页面，供后续获取发票详细信息使用
 *
 * @author WangJ 2019-12-30
 */
public abstract class WXCardSelectorUtil {
    private static final String TAG = WXCardSelectorUtil.class.getSimpleName();

    protected static Context mContext;

    private String accessToken;
    private String apiTicket;

    private OkHttpClient mOkHttpClient;

    public abstract void onReceiveData(WXInvoiceEvent event);

    public WXCardSelectorUtil(Context context) {
        // 注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mContext = context;
        mOkHttpClient = new OkHttpClient();
    }

    public void execute() {
        accessToken = SharedPreferenceUtil.getInfoFromShared(SharedPreferenceKey.EXPENSE_WX_ACCESS_TOKEN_s);
        apiTicket = SharedPreferenceUtil.getInfoFromShared(SharedPreferenceKey.EXPENSE_WX_API_TICKET_s);
        String timestamp = SharedPreferenceUtil.getInfoFromShared(SharedPreferenceKey.EXPENSE_WX_TOKEN_TIMESTAMP_s, "0");

        // 如果上次accessToken、apiTicket在有效期内，可直接使用
        if (StringUtil.isNotEmpty(accessToken)
                && StringUtil.isNotEmpty(apiTicket)
                && System.currentTimeMillis() - Long.parseLong(timestamp) < 1000 * 7200) {
            choose(apiTicket);
        } else {
            getAccessToken(BuildConfig.WX_APP_ID, BuildConfig.WX_APP_SECRECT);
        }
    }

    /**
     * 向微信服务器申请 accessToken
     *
     * @param appId     微信AppID
     * @param appSecret 微信AppSecret
     */
    private void getAccessToken(String appId, String appSecret) {
        Request request = new Request.Builder()
                .url(String.format(ServiceCode.WX_CARD_ACCESS_TOKEN, appId, appSecret))
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respBody = response.body().string();
                LogUtil.d(TAG, respBody);

                try {
                    JSONObject obj = new JSONObject(respBody);
                    accessToken = obj.optString("access_token");
                    SharedPreferenceUtil.setInfoToShared(SharedPreferenceKey.EXPENSE_WX_ACCESS_TOKEN_s, accessToken);
                    // 时间戳以为accessToken的时间为准,使用long记录会丢失精度，使用String存然后取出转型
                    SharedPreferenceUtil.setInfoToShared(SharedPreferenceKey.EXPENSE_WX_TOKEN_TIMESTAMP_s,
                            String.valueOf(System.currentTimeMillis()));

                    getApiTicket(accessToken);
                } catch (JSONException e) {
                    LogUtil.e(TAG, e.getMessage());
                }
            }
        });
    }

    /**
     * 使用 accessToken 获取 apiTicket
     *
     * @param token 上一步获取的 accessToken
     */
    private void getApiTicket(String token) {
        Request request = new Request.Builder()
                .url(String.format(ServiceCode.WX_CARD_API_TICKET, token))
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respBody = response.body().string();
                LogUtil.d(TAG, respBody);

                try {
                    JSONObject obj = new JSONObject(respBody);
                    apiTicket = obj.optString("ticket");
                    // 本地记录apiTicket
                    SharedPreferenceUtil.setInfoToShared(SharedPreferenceKey.EXPENSE_WX_API_TICKET_s, apiTicket);

                    choose(apiTicket);
                } catch (JSONException e) {
                    LogUtil.d(TAG, e.getMessage());
                }
            }
        });
    }

    /**
     * 从微信发票夹拉取发票列表
     *
     * @param apiTicket 上一步获取的 apiTicket
     */
    private void choose(String apiTicket) {
        final IWXAPI api = WXAPIFactory.createWXAPI(mContext, BuildConfig.WX_APP_ID, true);
        // 将应用的appId注册到微信
        api.registerApp(BuildConfig.WX_APP_ID);
        //建议动态监听微信启动广播进行注册到微信
        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 将该app注册到微信
                api.registerApp(BuildConfig.WX_APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));

        ChooseCardFromWXCardPackage.Req req = new ChooseCardFromWXCardPackage.Req();
        // 必传字段
        req.appId = BuildConfig.WX_APP_ID;
        req.timeStamp = String.valueOf(System.currentTimeMillis());   // 时间戳
        req.nonceStr = String.valueOf(System.currentTimeMillis());   // 随机字符串
        req.signType = "SHA1";   // 签名方式，目前仅支持SHA1
        // 可选字段
        req.cardType = "INVOICE";   // "INVOICE"-发票夹；
        req.cardId = "";   // 卡券ID，有值时拉取对应卡券，为空时拉取所有卡券
        req.canMultiSelect = "true";     // 是否支持多选
        req.locationId = "";   // ??

        // api_ticket、appid、location_id、timestamp、nonce_str、card_id、card_type
        List<String> list = new ArrayList<>();
        list.add(apiTicket);
        list.add(req.appId);
        list.add(req.locationId);
        list.add(req.timeStamp);
        list.add(req.nonceStr);
        list.add(req.cardId);
        list.add(req.cardType);
        // 微信签名规则，按字典顺序排序
        Collections.sort(list);
        StringBuffer buffer = new StringBuffer();
        for (String item : list) {
            buffer.append(item);
        }
        // 对字符串sha1加密
        byte[] sha1 = DigestUtils.sha1(buffer.toString().getBytes());

        req.cardSign = new String(sha1);    // 签名，也是必传字段

        // 发起请求
        api.sendReq(req);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiverWX(WXInvoiceEvent event) {
        //
        event.setAccessToken(accessToken);
        onReceiveData(event);
    }

    @Override
    protected void finalize() throws Throwable {
        EventBus.getDefault().unregister(this);
        super.finalize();
    }
}
