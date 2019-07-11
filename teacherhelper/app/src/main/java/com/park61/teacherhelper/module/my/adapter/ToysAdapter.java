package com.park61.teacherhelper.module.my.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.my.ToyDetailActivity;
import com.park61.teacherhelper.module.my.bean.ToysBean;

import java.util.List;

/**
 * Created by chenlie on 2017/12/27.
 *
 */

public class ToysAdapter extends RecyclerView.Adapter<ToysAdapter.ToysAdapterHolder>{

    private List<ToysBean> datas;
    private Context mContext;

    public ToysAdapter(Context context, List<ToysBean> list){
        mContext = context;
        datas = list;
    }

    @Override
    public ToysAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_toy, parent, false);
        return new ToysAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(ToysAdapterHolder holder, int position) {
        ToysBean b = datas.get(position);

        ImageManager.getInstance().displayImg(holder.toyImg, b.getProductPicUrl());
        holder.toyName.setText(b.getProductCname());
        holder.toyCurrPrice.setText("￥"+b.getCurrentUnifyPrice());
        holder.toyOrigPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.toyOrigPrice.setText("￥"+b.getMarketPrice());
        holder.toyColor.setText(b.getProductColor());
        holder.toySize.setText(b.getProductSize());

        if(position == datas.size() -1){
            holder.line.setVisibility(View.GONE);
        }else{
            holder.line.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {
            //去详情
            Intent it = new Intent(mContext, ToyDetailActivity.class);
            it.putExtra("toyBean", b);
            mContext.startActivity(it);
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ToysAdapterHolder extends RecyclerView.ViewHolder{

         ImageView toyImg;
         TextView toyName;
         TextView toyCurrPrice;
         TextView toyOrigPrice;
         TextView toySize;
         TextView toyColor;
         View line;

        public ToysAdapterHolder(View itemView) {
            super(itemView);
            toyImg = (ImageView) itemView.findViewById(R.id.toy_img);
            toyName = (TextView) itemView.findViewById(R.id.toy_name);
            toyCurrPrice = (TextView) itemView.findViewById(R.id.toy_curr_price);
            toyOrigPrice = (TextView) itemView.findViewById(R.id.toy_orig_price);
            toySize = (TextView) itemView.findViewById(R.id.toy_model);
            toyColor = (TextView) itemView.findViewById(R.id.toy_color);
            line = itemView.findViewById(R.id.line);
        }
    }

}
