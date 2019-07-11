package com.park61.teacherhelper.module.my.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.my.bean.SignDataBean;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by chenlie on 2017/12/27.
 *
 */

public class SignDataAdapter extends RecyclerView.Adapter<SignDataAdapter.SignDataHolder>{

    private List<SignDataBean> datas;
    DecimalFormat format;

    public SignDataAdapter(List<SignDataBean> list){
        datas = list;
        format = new DecimalFormat("###0.00");
    }

    @Override
    public SignDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sign_data, parent, false);
        return new SignDataHolder(view);
    }

    @Override
    public void onBindViewHolder(SignDataHolder holder, int position) {

        SignDataBean b = datas.get(position);
        holder.babyName.setText(b.getChildName());
        if(b.getStatus() == 1){
            holder.signImg.setVisibility(View.VISIBLE);
        }else{
            holder.signImg.setVisibility(View.GONE);
        }
        holder.parentName.setText(b.getUserName());
        holder.relation.setText(TextUtils.isEmpty(b.getRelationName()) ? "" : "("+b.getRelationName()+")");
        if(b.getBuyStatus() == 1){
            holder.buyArms.setText("已购买装备");
            holder.buyArms.setTextColor(Color.parseColor("#999999"));
        }else{
            holder.buyArms.setText("未购买装备");
            holder.buyArms.setTextColor(Color.parseColor("#FF5A80"));
        }
        holder.mobile.setText(b.getUserTel());
        if(b.getProductPaidAccount() > 0){
            holder.payNum.setVisibility(View.VISIBLE);
            holder.payNum.setText(format.format(b.getProductPaidAccount())+"元");
        }else{
            holder.payNum.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class SignDataHolder extends RecyclerView.ViewHolder{

        public ImageView signImg;
        public TextView babyName;
        public TextView buyArms;
        public TextView parentName;
        public TextView relation;
        public TextView mobile;
        TextView payNum;

        public SignDataHolder(View itemView) {
            super(itemView);
            signImg = (ImageView) itemView.findViewById(R.id.sign_state);
            babyName = (TextView) itemView.findViewById(R.id.baby_name);
            buyArms = (TextView) itemView.findViewById(R.id.buy_arms);
            parentName = (TextView) itemView.findViewById(R.id.parent_name);
            relation = (TextView) itemView.findViewById(R.id.relation);
            mobile = (TextView) itemView.findViewById(R.id.mobile);
            payNum = (TextView) itemView.findViewById(R.id.pay_money);
        }
    }

}
