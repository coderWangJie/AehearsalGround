package com.zhongsm.commlib.constant;

public class ServiceCode {
    /** 微信卡包，获取accessToken */
    public static final String WX_CARD_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    /** 微信卡包，获取apiTicket */
    public static final String WX_CARD_API_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=wx_card&access_token=%s";

    /** 微信服务器获取单张发票详情 */
    public static final String WX_INVOICE_INFO_SINGLE = "https://api.weixin.qq.com/card/invoice/reimburse/getinvoiceinfo?access_token=%s";
    /** 微信服务器批量获取发票详情 */
    public static final String WX_INVOICE_INFO_BATCH = "https://api.weixin.qq.com/card/invoice/reimburse/getinvoicebatch?access_token=%s";
}
