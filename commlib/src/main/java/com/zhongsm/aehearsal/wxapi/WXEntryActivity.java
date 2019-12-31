package com.zhongsm.aehearsal.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.ChooseCardFromWXCardPackage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhongsm.commlib.BuildConfig;
import com.zhongsm.commlib.R;
import com.zhongsm.commlib.bean.WXInvoiceEvent;
import com.zhongsm.commlib.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

/**
 * 微信SDK默认事件接收Activity，包名、类名不允许变动
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static String TAG = "MicroMsg.WXEntryActivity";

    private IWXAPI api;
    private MyHandler handler;

    private static class MyHandler extends Handler {
        private final WeakReference<WXEntryActivity> wxEntryActivityWeakReference;

        public MyHandler(WXEntryActivity wxEntryActivity) {
            wxEntryActivityWeakReference = new WeakReference<>(wxEntryActivity);
        }

        @Override
        public void handleMessage(Message msg) {
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, BuildConfig.WX_APP_ID, false);
        handler = new MyHandler(this);

        try {
            Intent intent = getIntent();
            api.handleIntent(intent, this);
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            default:
                break;
        }
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        int result;
        switch (resp.errCode) {
            /* 取消操作也会返回 errCode == BaseResp.ErrCode.ERR_OK，微信的问题 */
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.wx_err_ok;
                break;

            /* 取消分支跑不到，取消后微信仍然返回errCode == BaseResp.ErrCode.ERR_OK */
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.wx_err_user_cancel;
                break;

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.wx_err_auth_denied;
                break;

            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = R.string.wx_err_unsupport;
                break;

            default:
                result = R.string.wx_err_unknown;
                break;
        }
        LogUtil.d(TAG, "" + result);

        if (resp.getType() == ConstantsAPI.COMMAND_CHOOSE_CARD_FROM_EX_CARD_PACKAGE) {
            ChooseCardFromWXCardPackage.Resp chooseResp = (ChooseCardFromWXCardPackage.Resp) resp;

            WXInvoiceEvent event = new WXInvoiceEvent();
            event.setStatus(BaseResp.ErrCode.ERR_OK == chooseResp.errCode ? "success" : "fail");
            event.setCardItemList(chooseResp.cardItemList);

            EventBus.getDefault().post(event);
        }

        finish();
    }
}