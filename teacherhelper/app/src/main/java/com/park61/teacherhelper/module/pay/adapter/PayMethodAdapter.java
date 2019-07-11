package com.park61.teacherhelper.module.pay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.pay.bean.PaymentMethod;

import java.util.List;

public class PayMethodAdapter extends BaseAdapter {

    private List<PaymentMethod> mList;
    private Context mContext;
    private LayoutInflater factory;
    private String balanceStr = "";
    private String totalStr = "";

    public PayMethodAdapter(Context _context, List<PaymentMethod> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }

    public void setBalanceValue(String balance, String total) {
        balanceStr = balance;
        totalStr = total;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public PaymentMethod getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = factory.inflate(R.layout.paymethod_list_item, null);
            holder = new ViewHolder();
            holder.img_pay = (ImageView) convertView.findViewById(R.id.img_pay);
            holder.tv_paymethod_name = (TextView) convertView.findViewById(R.id.tv_paymethod_name);
            holder.chk_pay = (CheckBox) convertView.findViewById(R.id.chk_pay);
            holder.chk_pay.setClickable(false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PaymentMethod pm = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_pay, pm.getPaymentMethodPicUrl());
        if (pm.getIsDefault() == 1)
            holder.chk_pay.setChecked(true);
        else
            holder.chk_pay.setChecked(false);

        if (pm.getPaymentMethodType() == 2) {
            String itemAccountBalanceStr = FU.strBFmt(pm.getAccountBalance());
            if ("0.00".equals(itemAccountBalanceStr)) {
                if(pm.getId() == 1){// 余额支付
                    holder.tv_paymethod_name.setText(pm.getPaymentMethodName() + " (可用￥" + balanceStr + ")");
                    if (FU.paseDb(balanceStr) < FU.paseDb(totalStr)) {
                        holder.tv_paymethod_name.setTextColor(mContext.getResources().getColor(R.color.gaaaaaa));
                        pm.setEnough(false);
                    } else {
                        holder.tv_paymethod_name.setTextColor(mContext.getResources().getColor(R.color.g333333));
                    }
                } else {
                    holder.tv_paymethod_name.setText(pm.getPaymentMethodName() + " (可用￥" + itemAccountBalanceStr + ")");
                    holder.tv_paymethod_name.setTextColor(mContext.getResources().getColor(R.color.gaaaaaa));
                    pm.setEnough(false);
                }
            } else {
                holder.tv_paymethod_name.setText(pm.getPaymentMethodName() + " (可用￥" + itemAccountBalanceStr + ")");
                if (FU.paseDb(itemAccountBalanceStr) < FU.paseDb(totalStr)) {
                    holder.tv_paymethod_name.setTextColor(mContext.getResources().getColor(R.color.gaaaaaa));
                    pm.setEnough(false);
                } else {
                    holder.tv_paymethod_name.setTextColor(mContext.getResources().getColor(R.color.g333333));
                }
            }
        } else {
            holder.tv_paymethod_name.setText(pm.getPaymentMethodName());
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_pay;
        TextView tv_paymethod_name;
        CheckBox chk_pay;
    }

}
