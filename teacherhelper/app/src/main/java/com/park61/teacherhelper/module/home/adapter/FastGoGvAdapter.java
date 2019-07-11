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
import com.park61.teacherhelper.module.home.bean.CmsItem;
import com.park61.teacherhelper.module.home.bean.ContentItem;
import com.park61.teacherhelper.widget.circleimageview.CircleImageView;

import java.util.List;

public class FastGoGvAdapter extends BaseAdapter {

    private List<CmsItem> mList;
    private Context mContext;

    public FastGoGvAdapter(Context _context, List<CmsItem> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CmsItem getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fastgo_grid_item, null);
            holder = new ViewHolder();
            holder.img_quick = (ImageView) convertView.findViewById(R.id.img_quick);
            holder.tv_quick = (TextView) convertView.findViewById(R.id.tv_quick);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageManager.getInstance().displayImg(holder.img_quick, mList.get(position).getLinkPic());
        holder.tv_quick.setText(mList.get(position).getTitle());
        return convertView;
    }

    class ViewHolder {
        ImageView img_quick;
        TextView tv_quick;
    }
}
