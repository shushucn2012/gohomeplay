package com.park61.teacherhelper.module.home.adapter;

import android.content.Context;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.course.bean.ContentRecommendItemBean;

import java.util.List;

/**
 * Created by nieyu on 2017/10/16.
 */

public class ContentRecomendAdapter extends RecyclerView.Adapter<ContentRecomendAdapter.MyViewHolder> implements View.OnClickListener {

    public Context mcontext;
    public List<ContentRecommendItemBean> contentRecommendItemBeen;
    private OnItemClickListener mOnItemClickListener = null;

    public ContentRecomendAdapter(Context mcontext, List<ContentRecommendItemBean> contentRecommendItemBeen) {
        this.mcontext = mcontext;
        this.contentRecommendItemBeen = contentRecommendItemBeen;
    }

    @Override
    public ContentRecomendAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.adapter_contentrecoment, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(this);

        return holder;
    }

    @Override
    public void onBindViewHolder(ContentRecomendAdapter.MyViewHolder holder, int position) {
        holder.tv.setText(contentRecommendItemBeen.get(position).getTitle());
        ImageManager.getInstance().displayImg(holder.ivImage, contentRecommendItemBeen.get(position).getCoverImg());
        if (1 == contentRecommendItemBeen.get(position).getContentType()) {
            holder.iv_isplay.setVisibility(View.VISIBLE);
        } else {
            holder.iv_isplay.setVisibility(View.GONE);
        }

        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return contentRecommendItemBeen.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        ImageView ivImage;
        ImageView iv_isplay;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv_recommend);
            ivImage = (ImageView) view.findViewById(R.id.iv_recommend);
            iv_isplay = (ImageView) view.findViewById(R.id.iv_isplay);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
