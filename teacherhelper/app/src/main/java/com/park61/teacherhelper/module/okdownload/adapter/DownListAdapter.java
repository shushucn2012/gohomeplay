package com.park61.teacherhelper.module.okdownload.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.okdownload.widget.SimpleTask;

import java.util.List;

/**
 * Created by chenlie on 2018/2/5.
 *
 */

public class DownListAdapter extends BaseAdapter {

    private Context mContext;
    private List<SimpleTask> datas;

    public DownListAdapter(Context context, List<SimpleTask> tasks){
        mContext = context;
        datas = tasks;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder v;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.delete_download_item, parent, false);
            v = new ViewHolder();
            v.img = (ImageView) convertView.findViewById(R.id.check_select);
            v.title = (TextView) convertView.findViewById(R.id.title);
            v.size = (TextView) convertView.findViewById(R.id.size);
            convertView.setTag(v);
        }else{
            v = (ViewHolder) convertView.getTag();
        }

        SimpleTask simpleTask = datas.get(position);
        if(simpleTask.isCheck()){
            v.img.setEnabled(true);
        }else{
            v.img.setEnabled(false);
        }

        v.title.setText(simpleTask.getTitle());
        v.size.setText(simpleTask.getSize());

        return convertView;
    }

    private class ViewHolder{
        private ImageView img;
        private TextView title;
        private TextView size;
    }

}
