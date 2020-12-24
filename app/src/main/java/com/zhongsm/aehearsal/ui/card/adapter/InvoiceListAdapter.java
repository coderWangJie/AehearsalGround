package com.zhongsm.aehearsal.ui.card.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhongsm.aehearsal.R;
import com.zhongsm.wechatlib.bean.invoicedetail.InvoiceDetail;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 发票列表展示适配器
 */
public class InvoiceListAdapter extends RecyclerView.Adapter<InvoiceListAdapter.InvoiceItemHolder> {

    private List<InvoiceDetail> list;

    private Date date;
    private DecimalFormat format;
    private SimpleDateFormat dateFormat;

    public InvoiceListAdapter() {
        this.list = new ArrayList<>();

        date = new Date();
        format = new DecimalFormat("0.00");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    /**
     * 设置列表数据
     * @param newList 新数据
     */
    public void setList(List<InvoiceDetail> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    /**
     * 增加列表数据
     * @param subList 要添加的数据
     */
    public void addList(List<InvoiceDetail> subList) {
        list.addAll(subList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InvoiceItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice, parent, false);
        return new InvoiceItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceItemHolder holder, int position) {
        InvoiceDetail item = list.get(position);
        holder.tvCompanyName.setText(item.getPayee());
        holder.tvNumber.setText(getShowAmount(item.getUser_info().getFee_without_tax()));
        holder.tvBuyer.setText(item.getUser_info().getTitle());
        holder.tvDateTime.setText(getBillDate(item.getUser_info().getBilling_time()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 金额 格式化为 "0.00" 的字符串
     *
     * @param amountOnCent "分"为单位的金额
     * @return "元"为单位金额，用于页面显示
     */
    private String getShowAmount(int amountOnCent) {
        double result = amountOnCent / 100d;
        return format.format(result);
    }

    /**
     * 10位发票时间戳 转 yyyy-MM-dd 日期
     *
     * @param billTimeStamp10 10位时间戳
     * @return yyyy-MM-dd
     */
    private String getBillDate(int billTimeStamp10) {
        long timestamp13 = 0L;
        String temp = String.valueOf(billTimeStamp10);
        if (temp.length() == 10) {
            timestamp13 = Long.valueOf(temp.concat("000"));
        }

        date.setTime(timestamp13);
        return dateFormat.format(date);
    }

    /**
     * 发票列表Item ViewHolder
     */
    static class InvoiceItemHolder extends RecyclerView.ViewHolder {

        TextView tvCompanyName;
        TextView tvNumber;
        TextView tvBuyer;
        TextView tvDateTime;

        InvoiceItemHolder(@NonNull View itemView) {
            super(itemView);

            tvCompanyName = itemView.findViewById(R.id.tvCompanyName);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvBuyer = itemView.findViewById(R.id.tvBuyer);
            tvDateTime = itemView.findViewById(R.id.tvDate);
        }
    }
}