package com.zhongsm.wechatlib.bean.invoicedetail;

import java.util.List;

public class User_info {
    private int fee;

    private String title;

    private int billing_time;

    private String billing_no;

    private String billing_code;

    private List<Info> info;

    private int fee_without_tax;

    private int tax;

    private String pdf_url;

    private String reimburse_status;

    private String order_id;

    private String check_code;

    private String buyer_number;

    private String buyer_address_and_phone;

    private String buyer_bank_account;

    private String seller_number;

    private String seller_address_and_phone;

    private String seller_bank_account;

    private String remarks;

    private String cashier;

    private String maker;

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBilling_time() {
        return billing_time;
    }

    public void setBilling_time(int billing_time) {
        this.billing_time = billing_time;
    }

    public String getBilling_no() {
        return billing_no;
    }

    public void setBilling_no(String billing_no) {
        this.billing_no = billing_no;
    }

    public String getBilling_code() {
        return billing_code;
    }

    public void setBilling_code(String billing_code) {
        this.billing_code = billing_code;
    }

    public List<Info> getInfo() {
        return info;
    }

    public void setInfo(List<Info> info) {
        this.info = info;
    }

    public int getFee_without_tax() {
        return fee_without_tax;
    }

    public void setFee_without_tax(int fee_without_tax) {
        this.fee_without_tax = fee_without_tax;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(String pdf_url) {
        this.pdf_url = pdf_url;
    }

    public String getReimburse_status() {
        return reimburse_status;
    }

    public void setReimburse_status(String reimburse_status) {
        this.reimburse_status = reimburse_status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCheck_code() {
        return check_code;
    }

    public void setCheck_code(String check_code) {
        this.check_code = check_code;
    }

    public String getBuyer_number() {
        return buyer_number;
    }

    public void setBuyer_number(String buyer_number) {
        this.buyer_number = buyer_number;
    }

    public String getBuyer_address_and_phone() {
        return buyer_address_and_phone;
    }

    public void setBuyer_address_and_phone(String buyer_address_and_phone) {
        this.buyer_address_and_phone = buyer_address_and_phone;
    }

    public String getBuyer_bank_account() {
        return buyer_bank_account;
    }

    public void setBuyer_bank_account(String buyer_bank_account) {
        this.buyer_bank_account = buyer_bank_account;
    }

    public String getSeller_number() {
        return seller_number;
    }

    public void setSeller_number(String seller_number) {
        this.seller_number = seller_number;
    }

    public String getSeller_address_and_phone() {
        return seller_address_and_phone;
    }

    public void setSeller_address_and_phone(String seller_address_and_phone) {
        this.seller_address_and_phone = seller_address_and_phone;
    }

    public String getSeller_bank_account() {
        return seller_bank_account;
    }

    public void setSeller_bank_account(String seller_bank_account) {
        this.seller_bank_account = seller_bank_account;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCashier() {
        return cashier;
    }

    public void setCashier(String cashier) {
        this.cashier = cashier;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }
}
