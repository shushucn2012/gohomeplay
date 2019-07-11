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
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.home.bean.ContentItem;
import com.park61.teacherhelper.module.home.bean.CourseBean;
import com.park61.teacherhelper.module.home.bean.CourseSectionBean;
import com.park61.teacherhelper.widget.circleimageview.CircleImageView;

import java.util.List;

public class DateGvAdapter extends BaseAdapter {

    private List<ContentItem> mList;
    private Context mContext;

    public DateGvAdapter(Context _context, List<ContentItem> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ContentItem getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.shophome_date_item_layout, null);
            holder = new ViewHolder();
            holder.img_date = (ImageView) convertView.findViewById(R.id.img_date);
            holder.img_user = (CircleImageView) convertView.findViewById(R.id.img_user);
            holder.img_can_play = (ImageView) convertView.findViewById(R.id.img_can_play);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_teacher = (TextView) convertView.findViewById(R.id.tv_teacher);
            holder.tv_read_num = (TextView) convertView.findViewById(R.id.tv_read_num);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.bottom_line = convertView.findViewById(R.id.bottom_line);
            holder.img_vip = convertView.findViewById(R.id.img_vip);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageManager.getInstance().displayImg(holder.img_date, mList.get(position).getCoverImg());
        ImageManager.getInstance().displayImg(holder.img_user, mList.get(position).getAuthorPic(), R.mipmap.headimg_default_img);
        holder.tv_title.setText(mList.get(position).getTitle());
        holder.tv_teacher.setText(mList.get(position).getAuthorName());
        holder.tv_read_num.setText(TextUtils.isEmpty(mList.get(position).getViewNum()) ? "0" : mList.get(position).getViewNum());
        String sLevel1CateName = mList.get(position).getLevel1CateName();
        if (TextUtils.isEmpty(sLevel1CateName)) {
            holder.tv_type.setVisibility(View.GONE);
        } else {
            holder.tv_type.setVisibility(View.VISIBLE);
            holder.tv_type.setText(mList.get(position).getLevel1CateName());
        }

        if (mList.get(position).getContentType() == 1) {//视频
            holder.img_can_play.setVisibility(View.VISIBLE);
        } else {//图文，音频
            holder.img_can_play.setVisibility(View.GONE);
        }

        if (mList.get(position).getIsTag() == 1) {//是否显示vip标记
            holder.img_vip.setVisibility(View.VISIBLE);
        } else {
            holder.img_vip.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_date, img_can_play, img_vip;
        CircleImageView img_user;
        TextView tv_title;
        TextView tv_teacher;
        TextView tv_type;
        TextView tv_read_num;
        View bottom_line;
    }
}
