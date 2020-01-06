package com.zhongsm.wechatlib.bean;

import com.zhongsm.commlib.bean.BaseVO;
import com.zhongsm.commlib.utils.LogUtil;
import com.zhongsm.commlib.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WXInvoiceIdentification extends BaseVO {
    /**
     * 获取到的微信accessToken，后台获取微信卡券（发票）详情时需要此值
     */
    private String accessToken;

//    /**
//     * 获取到到微信apiTicket
//     */
//    private String apiTicket;

    /**
     * 交易回调状态
     */
    private String status;

    /**
     * 卡券JSON数组的字符串表达
     */
    private String cardItemList;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCardItemList() {
        return cardItemList;
    }

    public void setCardItemList(String cardItemList) {
        this.cardItemList = cardItemList;
    }

    /**
     * 返回给每刻报销JS的数据
     *
     * @return JSON对象的字符串形式，具体结构参考JSON组装代码
     */
    public String getInvoiceString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", status);

            /*
             * 将accessToken、apiTicket、卡券JSON数组封装到一个JSONObject中
             */
            JSONObject cardJson = new JSONObject();
            cardJson.put("accessToken", accessToken);
//            cardJson.put("apiTicket", apiTicket);
            if (StringUtil.isNotEmpty(cardItemList)) {
                cardJson.put("cardList", new JSONArray(cardItemList));
            }
            jsonObject.put("cardData", cardJson);
        } catch (JSONException e) {
            LogUtil.e(getClass().getSimpleName(), e.getMessage());
        }
        return jsonObject.toString();
    }
}
