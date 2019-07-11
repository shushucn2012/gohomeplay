package com.park61.teacherhelper.module.course.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.course.bean.AgeCate;
import com.park61.teacherhelper.module.course.bean.BigCate;

import java.util.List;

public class TypeCateAdapter extends BaseAdapter {
    private Context context;
    private List<AgeCate> cateList;

    public TypeCateAdapter(Context c, List<AgeCate> list) {
        this.context = c;
        this.cateList = list;
    }

    @Override
    public int getCount() {
        return cateList.size();
    }

    @Override
    public AgeCate getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.smallcate_list_item, null);
            holder = new ViewHolder();
            holder.tv_cate_name = (TextView) convertView.findViewById(R.id.tv_cate_name);
            holder.img_gou = (ImageView) convertView.findViewById(R.id.img_gou);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_cate_name.setText(cateList.get(position).getLabel());

		if (cateList.get(position).isSelected()) {
			holder.tv_cate_name.setTextColor(context.getResources().getColor(R.color.color_text_red_deep));
            holder.img_gou.setVisibility(View.VISIBLE);
		} else {
			holder.tv_cate_name.setTextColor(context.getResources().getColor(R.color.g333333));
            holder.img_gou.setVisibility(View.GONE);
		}
        return convertView;
    }

    class ViewHolder {
        TextView tv_cate_name;
        ImageView img_gou;
    }

}
