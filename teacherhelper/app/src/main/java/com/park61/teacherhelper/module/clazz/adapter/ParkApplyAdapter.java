package com.park61.teacherhelper.module.clazz.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.clazz.bean.ParkApplyBean;

import java.util.List;

/**
 * Created by chenlie on 2018/5/16.
 */

public class ParkApplyAdapter extends BaseAdapter {

    private Context mContext;
    private List<ParkApplyBean> datas;

    public ParkApplyAdapter(Context c, List<ParkApplyBean> list){
        mContext = c;
        datas = list;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public ParkApplyBean getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IconHolder holder = null;
        if(convertView == null){
            holder = new IconHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_park_list, null);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.address = (TextView) convertView.findViewById(R.id.area_text);
            convertView.setTag(holder);
        }else{
            holder = (IconHolder) convertView.getTag();
        }

        ParkApplyBean b = datas.get(position);
        holder.title.setText(b.getSchoolName());
        holder.status.setText(b.getStatusName());
        if(!TextUtils.isEmpty(b.getTypeName())){
            holder.type.setVisibility(View.VISIBLE);
            holder.type.setText(b.getStatusName());
        }else{
            holder.type.setVisibility(View.GONE);
        }
        holder.address.setText(b.getTotalAddress());

        return convertView;
    }

    class IconHolder{
        TextView title;
        TextView status;
        TextView type;
        TextView address;
    }
}
