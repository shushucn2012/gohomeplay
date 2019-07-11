package com.park61.teacherhelper.module.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.home.bean.CommentItemBean;
import com.park61.teacherhelper.common.tool.ImageManager;

import java.util.List;

/**
 * Created by nieyu on 2017/10/16.
 */

public class ContentCommentListAdapter extends BaseAdapter {
    private Context mcontext;
    private List<CommentItemBean> commentItemBean;

    public ContentCommentListAdapter(Context mcontext, List<CommentItemBean> commentItemBean) {
        this.mcontext = mcontext;
        this.commentItemBean = commentItemBean;
    }

    @Override
    public int getCount() {
        return commentItemBean.size();
    }

    @Override
    public Object getItem(int i) {
        return commentItemBean.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    ViewHolder holder;

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.adpteitem_commentlist, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(commentItemBean.get(i).getUserUrl())) {
            ImageManager.getInstance().displayImg(holder.iv_icon, commentItemBean.get(i).getUserUrl());
        }
        holder.tv_name.setText(commentItemBean.get(i).getUserName());
        holder.tvContent.setText(commentItemBean.get(i).getContent());
        holder.tv_time.setText(commentItemBean.get(i).getCreateTime() + "");
        return convertView;
    }

    class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_time;
        TextView tvContent;

        public ViewHolder(View view) {
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tvContent = (TextView) view.findViewById(R.id.tvContent);
        }
    }


}
