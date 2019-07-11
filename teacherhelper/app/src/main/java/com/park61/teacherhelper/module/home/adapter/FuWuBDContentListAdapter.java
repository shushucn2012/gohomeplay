package com.park61.teacherhelper.module.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.home.bean.ContentItem;
import com.park61.teacherhelper.widget.circleimageview.CircleImageView;

import java.util.List;

public class FuWuBDContentListAdapter extends BaseAdapter {

    private List<ContentItem> mList;
    private Context mContext;
    private LayoutInflater factory;

    public FuWuBDContentListAdapter(Context _context, List<ContentItem> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = factory.inflate(R.layout.fuwubd_content_list_item, null);
            holder = new ViewHolder();
            holder.img_toy = (ImageView) convertView.findViewById(R.id.img_toy);
            holder.img_can_play = (ImageView) convertView.findViewById(R.id.img_can_play);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_viewnum = (TextView) convertView.findViewById(R.id.tv_viewnum);
            holder.tv_zannum = (TextView) convertView.findViewById(R.id.tv_zannum);
            holder.bottom_line = convertView.findViewById(R.id.bottom_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ContentItem item = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_toy, item.getCoverImg());
        holder.tv_title.setText(item.getTitle());
        holder.tv_name.setText(item.getAuthorName());
        holder.tv_viewnum.setText(item.getViewNum());
        holder.tv_zannum.setText(item.getPraiseNum() + "");
        if (position == mList.size() - 1) {
            holder.bottom_line.setVisibility(View.GONE);
        } else {
            holder.bottom_line.setVisibility(View.VISIBLE);
        }

        if (mList.get(position).getContentType() == 1) {//视频
            holder.img_can_play.setVisibility(View.VISIBLE);
        } else {//图文和音频
            holder.img_can_play.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_toy, img_can_play;
        TextView tv_title;
        TextView tv_name;
        TextView tv_viewnum;
        TextView tv_zannum;
        View bottom_line;
    }

}
