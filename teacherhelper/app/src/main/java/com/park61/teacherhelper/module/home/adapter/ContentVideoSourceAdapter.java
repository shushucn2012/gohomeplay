package com.park61.teacherhelper.module.home.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.course.bean.ContentRecommendItemBean;
import com.park61.teacherhelper.module.home.bean.VideoItemSource;

import java.util.List;

/**
 * Created by nieyu on 2017/10/16.
 */

public class ContentVideoSourceAdapter extends RecyclerView.Adapter<ContentVideoSourceAdapter.MyViewHolder> {

    public Context mContext;
    public List<VideoItemSource> videoItemSources;
    private OnItemClickListener mOnItemClickListener = null;

    public ContentVideoSourceAdapter(Context mcontext, List<VideoItemSource> videoItemSources) {
        this.mContext = mcontext;
        this.videoItemSources = videoItemSources;
    }

    @Override
    public ContentVideoSourceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_item_video_source, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ContentVideoSourceAdapter.MyViewHolder holder, int position) {
        holder.tv_source_name.setText(videoItemSources.get(position).getTitle());

        if (videoItemSources.get(position).isPlaying()) {
            holder.cardview_video_cover.setCardBackgroundColor(mContext.getResources().getColor(R.color.g333333));
            holder.tv_source_name.setTextColor(mContext.getResources().getColor(R.color.com_orange));
        } else {
            holder.cardview_video_cover.setCardBackgroundColor(mContext.getResources().getColor(R.color.gd5d5de));
            holder.tv_source_name.setTextColor(mContext.getResources().getColor(R.color.g333333));
        }

        if (position == 0) {
            holder.space_left.setVisibility(View.VISIBLE);
        } else {
            holder.space_left.setVisibility(View.GONE);
        }
        holder.cardview_video_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return videoItemSources.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_source_name;
        CardView cardview_video_cover;
        View space_left;

        public MyViewHolder(View view) {
            super(view);
            cardview_video_cover = view.findViewById(R.id.cardview_video_cover);
            tv_source_name = (TextView) view.findViewById(R.id.tv_source_name);
            space_left = view.findViewById(R.id.space_left);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
