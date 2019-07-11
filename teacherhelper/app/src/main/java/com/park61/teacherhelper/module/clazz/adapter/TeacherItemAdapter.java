package com.park61.teacherhelper.module.clazz.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.clazz.bean.TeacherItemBean;
import com.park61.teacherhelper.module.home.adapter.FhCommtAdapter;

import java.util.List;

/**
 * Created by nieyu on 2017/10/19.
 */

public class TeacherItemAdapter extends BaseAdapter {
    private List<TeacherItemBean> mList;
    private Context mContext;
    private LayoutInflater factory;
    private FhCommtAdapter.OnReplyClickedLsner mOnReplyClickedLsner;

    public TeacherItemAdapter(Context _context, List<TeacherItemBean> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.adapter_teacherlistitem, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TeacherItemBean teacherItemBean = mList.get(i);
        holder.tv_teach_title.setText(teacherItemBean.getTitle());
        holder.tv_teach_read.setText(teacherItemBean.getViewNum()+"阅读");
        holder.tv_teachtime.setText(teacherItemBean.getShowDate());
        if (!TextUtils.isEmpty(teacherItemBean.getCoverImg())) {
            ImageManager.getInstance().displayImg(holder.iv_teacher, teacherItemBean.getCoverImg());
        }
        if(1==teacherItemBean.getContentType()){
            holder.iv_isplay.setVisibility(View.VISIBLE);
            holder.iv_isplay.setImageResource(R.mipmap.icon_watch);
        }else if(2==teacherItemBean.getContentType()) {
            holder.iv_isplay.setVisibility(View.VISIBLE);
            holder.iv_isplay.setImageResource(R.mipmap.icon_listener);
        }else{
            holder.iv_isplay.setVisibility(View.GONE);
        }
        return convertView;
    }
    class ViewHolder {
        RoundedImageView iv_teacher;
        TextView tv_teach_title;
        TextView tv_teachtime;
        TextView tv_teach_read;
        ImageView iv_isplay;
        public ViewHolder(View view) {
            iv_teacher = (RoundedImageView) view.findViewById(R.id.iv_teacher);
            tv_teach_title = (TextView) view.findViewById(R.id.tv_teach_title);
            tv_teachtime = (TextView) view.findViewById(R.id.tv_teachtime);
            tv_teach_read = (TextView) view.findViewById(R.id.tv_teach_read);
            iv_isplay = (ImageView) view.findViewById(R.id.iv_isplay);
        }
    }
}