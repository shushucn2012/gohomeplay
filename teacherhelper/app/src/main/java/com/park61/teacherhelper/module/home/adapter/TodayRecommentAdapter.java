package com.park61.teacherhelper.module.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.home.bean.TeacherCourseBean;

import java.util.List;

/**
 * Created by chenlie on 2018/3/20.
 */

public class TodayRecommentAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private Context mContext;
    private List<TeacherCourseBean> list;

    public TodayRecommentAdapter(Context _context, List<TeacherCourseBean> mList){
        this.list = mList;
        this.mContext = _context;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.today_recom_item, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TeacherCourseBean item = list.get(position);
        ImageManager.getInstance().displayImg(holder.image, item.getListCover(), R.mipmap.img_default_h);
        return convertView;
    }

    class ViewHolder {
        ImageView image;
    }
}
