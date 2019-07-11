package com.park61.teacherhelper.module.course.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.course.bean.BigCate;

import java.util.List;

public class BigCateAdapter extends BaseAdapter {
    private Context context;
    private List<BigCate> cateList;

    public BigCateAdapter(Context c, List<BigCate> list) {
        this.context = c;
        this.cateList = list;
    }

    @Override
    public int getCount() {
        return cateList.size();
    }

    @Override
    public BigCate getItem(int position) {
        return cateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.bigcate_list_item, null);
            holder = new ViewHolder();
            holder.bigcate_item_root = convertView.findViewById(R.id.bigcate_item_root);
            holder.tv_cate_name = (TextView) convertView.findViewById(R.id.tv_cate_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_cate_name.setText(cateList.get(position).getName());

		if (cateList.get(position).isSelected()) {
			holder.tv_cate_name.setTextColor(context.getResources().getColor(R.color.color_text_red_deep));
            holder.bigcate_item_root.setBackgroundColor(context.getResources().getColor(R.color.gffffff));
		} else {
			holder.tv_cate_name.setTextColor(context.getResources().getColor(R.color.g333333));
            holder.bigcate_item_root.setBackgroundColor(context.getResources().getColor(R.color.transparent));
		}
        return convertView;
    }

    class ViewHolder {
        TextView tv_cate_name;
        View bigcate_item_root;
    }

}
