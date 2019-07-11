package com.park61.teacherhelper.module.my.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.my.PublishLibActivity;
import com.park61.teacherhelper.module.my.bean.ActivityBean;

import java.util.List;

/**
 * Created by chenlie on 2017/12/27.
 *
 */

public class MyLibAdapter extends RecyclerView.Adapter<MyLibAdapter.ActivityViewHolder>{

    private List<ActivityBean> datas;
    private Context mContext;

    public MyLibAdapter(Context context, List<ActivityBean> list){
        mContext = context;
        datas = list;
    }

    @Override
    public ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_lib, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActivityViewHolder holder, int position) {
        ActivityBean b = datas.get(position);

        holder.activityPub.setOnClickListener(v -> {
            //发布
            Intent it = new Intent(mContext, PublishLibActivity.class);
            it.putExtra("id", b.getId());
            mContext.startActivity(it);
        });

        ImageManager.getInstance().displayImg(holder.img, b.getCoverUrl());
        holder.activityTitle.setText(b.getName());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ActivityViewHolder extends RecyclerView.ViewHolder{

        public ImageView img;
        public TextView activityTitle;
        public TextView activityPub;

        public ActivityViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.activity_img);
            activityTitle = (TextView) itemView.findViewById(R.id.activity_title);
            activityPub = (TextView) itemView.findViewById(R.id.publish);
        }
    }

}
