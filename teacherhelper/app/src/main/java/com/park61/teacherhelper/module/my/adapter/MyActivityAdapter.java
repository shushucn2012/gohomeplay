package com.park61.teacherhelper.module.my.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.my.SignDataActivity;
import com.park61.teacherhelper.module.my.bean.ActivityBean;
import com.park61.teacherhelper.widget.pw.SharePopWin;

import java.util.List;

/**
 * Created by chenlie on 2017/12/27.
 *
 */

public class MyActivityAdapter extends RecyclerView.Adapter<MyActivityAdapter.ActivityViewHolder>{

    private List<ActivityBean> datas;
    private Context mContext;

    public MyActivityAdapter(Context context, List<ActivityBean> list){
        mContext = context;
        datas = list;
    }

    @Override
    public ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_activity, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActivityViewHolder holder, int position) {
        ActivityBean b = datas.get(position);

        holder.activityData.setOnClickListener(v->{
            Intent it = new Intent(mContext, SignDataActivity.class);
            it.putExtra("id", b.getId());
            it.putExtra("classId", b.getClassId());
            mContext.startActivity(it);
        });
        holder.activityShare.setOnClickListener(v -> toShare(b));

        ImageManager.getInstance().displayImg(holder.img, b.getCoverUrl());
        holder.activityTitle.setText(b.getName());
        holder.activityClass.setText(b.getSchoolName()+" "+b.getClassName());
        holder.activityTime.setText(b.getStartDateStr());
        holder.activityState.setText(b.getShowStatusName());
        holder.activityState.setTextColor(Color.parseColor(b.getShowStatus() == 2 ? "#999999" : "#ff5a80"));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ActivityViewHolder extends RecyclerView.ViewHolder{

        public ImageView img;
        public TextView activityTitle;
        public TextView activityState;
        public TextView activityClass;
        public TextView activityTime;
        public TextView activityData;
        public TextView activityShare;

        public ActivityViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.activity_img);
            activityTitle = (TextView) itemView.findViewById(R.id.activity_title);
            activityState = (TextView) itemView.findViewById(R.id.activity_state);
            activityClass = (TextView) itemView.findViewById(R.id.activity_class);
            activityTime = (TextView) itemView.findViewById(R.id.activity_time);
            activityData = (TextView) itemView.findViewById(R.id.activity_data);
            activityShare = (TextView) itemView.findViewById(R.id.share);
        }
    }

    /**
     * 右上角分享按钮
     */
    public void toShare(ActivityBean b) {
        Intent its = new Intent(mContext, SharePopWin.class);
        String shareUrl = "http://m.61park.cn/#/fm/fmindex/"+b.getId()+"/"+b.getClassId();
        its.putExtra("shareUrl", shareUrl);
        its.putExtra("picUrl", b.getCoverUrl());
        its.putExtra("title", b.getName());
        its.putExtra("description", b.getIntro());
        mContext.startActivity(its);
    }
}
