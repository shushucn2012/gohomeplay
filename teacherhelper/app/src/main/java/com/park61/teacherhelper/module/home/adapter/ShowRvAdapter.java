package com.park61.teacherhelper.module.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.home.CourseDetailsActivity;
import com.park61.teacherhelper.module.home.bean.ContentItem;
import com.park61.teacherhelper.module.workplan.WorkResponseActivity;
import com.park61.teacherhelper.module.workplan.bean.TaskCalendarListBean;
import com.park61.teacherhelper.module.workplan.bean.WorkMonthBean;
import com.park61.teacherhelper.widget.circleimageview.CircleImageView;
import com.park61.teacherhelper.widget.listview.ListViewForScrollView;

import java.util.List;

import static com.park61.teacherhelper.common.tool.ViewInitTool.AddStatistics;

/**
 * Created by shubei on 2017/12/13.
 */

public class ShowRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater factory;
    private List<ContentItem> mlist;

    public ShowRvAdapter(Context context, List<ContentItem> list) {
        this.mContext = context;
        this.mlist = list;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.show_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        ContentItem item = mlist.get(position);
        ImageManager.getInstance().displayImg(myViewHolder.img_toy, item.getCoverImg());
        ImageManager.getInstance().displayImg(myViewHolder.img_user, item.getAuthorPic(), R.mipmap.headimg_default_img);
        myViewHolder.tv_title.setText(item.getTitle());
        myViewHolder.tv_name.setText(item.getAuthorName());
        //holder.tv_time.setText(item.getSortTime());
        myViewHolder.tv_time.setText(item.getShowDate());
        if (position == mlist.size() - 1) {
            myViewHolder.bottom_line.setVisibility(View.GONE);
        } else {
            myViewHolder.bottom_line.setVisibility(View.VISIBLE);
        }

        if (mlist.get(position).getContentType() == 1) {//视频
            myViewHolder.img_can_play.setVisibility(View.VISIBLE);
        } else {//图文和音频
            myViewHolder.img_can_play.setVisibility(View.GONE);
        }

        if (mlist.get(position).getIsTag() == 1) {//显示vip标签
            myViewHolder.img_vip.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.img_vip.setVisibility(View.GONE);
        }

        myViewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStatistics(mContext, mlist.get(position).getId(), "more");
                Intent it = new Intent(mContext, CourseDetailsActivity.class);
                it.putExtra("coursePlanId", mlist.get(position).getId());
                mContext.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_toy, img_can_play, img_vip;
        CircleImageView img_user;
        TextView tv_title;
        TextView tv_name;
        TextView tv_time;
        View bottom_line, root;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_toy = (ImageView) itemView.findViewById(R.id.img_toy);
            img_user = (CircleImageView) itemView.findViewById(R.id.img_user);
            img_can_play = (ImageView) itemView.findViewById(R.id.img_can_play);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            bottom_line = itemView.findViewById(R.id.bottom_line);
            img_vip = itemView.findViewById(R.id.img_vip);
            root = itemView.findViewById(R.id.root);
        }
    }


}
