package com.zhongsm.aehearsal.ui.card;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.zhongsm.aehearsal.R;
import com.zhongsm.aehearsal.ui.card.adapter.InvoiceListAdapter;
import com.zhongsm.commlib.android.BaseActivity;
import com.zhongsm.commlib.constant.ServiceCode;
import com.zhongsm.commlib.utils.LogUtil;
import com.zhongsm.wechatlib.bean.WXInvoiceIdentification;
import com.zhongsm.wechatlib.bean.invoicedetail.InvoiceDetail;
import com.zhongsm.wechatlib.util.WXCardSelectorUtil;

import org.jetbrains.annotations.NotNull;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CardPackage extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    InvoiceListAdapter adapter;
    List<InvoiceDetail> mList;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_card_package;
    }

    @Override
    protected void runOnCreate() {
        mList = new ArrayList<>();
        mList.add(new InvoiceDetail());
        mList.add(new InvoiceDetail());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new InvoiceListAdapter();
        recyclerView.setAdapter(adapter);

        WXCardSelectorUtil.getInstance().execute(this, new WXCardSelectorUtil.WXInvoiceEventListener() {
            @Override
            public void onReceiveEvent(WXInvoiceIdentification event) {
                getInvoiceInfo(event);
            }
        });
    }

    @Override
    protected void runOnResume() {

    }


    private void getInvoiceInfo(WXInvoiceIdentification event) {
        JSONArray array = JSONObject.parseArray(event.getCardItemList());
        if (array.size() <= 0) {
            LogUtil.e(TAG, "WXInvoiceEvent's CardList size is 0");
            return;
        }

        // 单张请求示例
//        JSONObject jsonObject = (JSONObject) array.get(0);
//        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json; charset=utf-8"));
//        Request request = new Request.Builder()
//                .url(String.format(ServiceCode.WX_INVOICE_INFO_SINGLE, event.getAccessToken()))
//                .post(body)
//                .build();
//
//        Call call = new OkHttpClient().newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                LogUtil.e(TAG, e.getMessage());
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                String data = response.body().string();
//                LogUtil.d(TAG, data);
//            }
//        });

        // 批量请求示例
        JSONObject object = new JSONObject();
        object.put("item_list", array);  // "item_list"为微信指定的列表名
        RequestBody body = RequestBody.create(object.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(String.format(ServiceCode.WX_INVOICE_INFO_BATCH, event.getAccessToken()))
                .post(body)
                .build();
        Call call = new OkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                LogUtil.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String data = Objects.requireNonNull(response.body()).string();
                LogUtil.d(TAG, data);

                JSONObject root = JSONObject.parseObject(data);
                List<InvoiceDetail> list = JSONObject.parseArray(root.getString("item_list"), InvoiceDetail.class);

                runOnUiThread(() -> adapter.setList(list));
            }
        });
    }
}
