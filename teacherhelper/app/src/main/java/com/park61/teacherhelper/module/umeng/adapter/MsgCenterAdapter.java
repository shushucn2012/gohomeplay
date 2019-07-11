package com.park61.teacherhelper.module.umeng.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.home.bean.CmsItem;
import com.park61.teacherhelper.module.umeng.bean.MsgBean;
import com.park61.teacherhelper.module.workplan.WorkResponseActivity;
import com.park61.teacherhelper.module.workplan.bean.TaskCalendarListBean;
import com.park61.teacherhelper.module.workplan.bean.WorkMonthBean;
import com.park61.teacherhelper.widget.listview.ListViewForScrollView;

import java.util.List;

/**
 * 消息中心adapter
 * Created by shubei on 2018/6/7.
 */

public class MsgCenterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<MsgBean> mlist;
    private OnItemClickedListener mOnItemClickedListener;

    public MsgCenterAdapter(Context context, List<MsgBean> list) {
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.msg_center_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        MsgBean item = mlist.get(position);
        myViewHolder.tv_title.setText(item.getTitle());
        myViewHolder.tv_time.setText(item.getCreateDate());
        if (item.getMessageStatus() == 0) {//0正常1已结束
            myViewHolder.tv_over.setVisibility(View.GONE);
        } else {
            myViewHolder.tv_over.setVisibility(View.VISIBLE);
        }
        ImageManager.getInstance().displayImg(myViewHolder.img_msg_pic, item.getImage());
        myViewHolder.tv_title.setTextColor(Color.parseColor("#" + item.getTitleColor()));
        CmsItem cmsItem = new CmsItem();
        cmsItem.setLinkType(item.getLinkType());
        cmsItem.setLinkData(item.getLinkData());
        cmsItem.setLinkPic(item.getImage());
        cmsItem.setLinkUrl(item.getLinkUrl());
        cmsItem.setTitle(item.getTitle());
        if (item.getMessageStatus() == 0) {//0正常1已结束
            myViewHolder.tv_over.setVisibility(View.GONE);
            myViewHolder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickedListener != null)
                        mOnItemClickedListener.onClicked(position);
                    ViewInitTool.judgeGoWhere(cmsItem, mContext);
                }
            });
        } else {
            myViewHolder.tv_over.setVisibility(View.VISIBLE);
            myViewHolder.root.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title, tv_time, tv_over;
        private ImageView img_msg_pic;
        private View root;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            img_msg_pic = (ImageView) itemView.findViewById(R.id.img_msg_pic);
            root = itemView.findViewById(R.id.root);
            tv_over = (TextView) itemView.findViewById(R.id.tv_over);
        }
    }

    public interface OnItemClickedListener {
        void onClicked(int position);
    }

    public void setOnItemClickedListener(OnItemClickedListener listener) {
        mOnItemClickedListener = listener;
    }

}
