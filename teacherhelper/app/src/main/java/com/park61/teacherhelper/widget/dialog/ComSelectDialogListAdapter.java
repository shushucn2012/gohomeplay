package com.park61.teacherhelper.widget.dialog;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.teacherhelper.R;

import java.util.List;

/**
 * 选择城市Dialog的adapter
 *
 * @author Lucia
 */
public class ComSelectDialogListAdapter extends BaseAdapter {

    private List<String> lists;
    private LayoutInflater factory;
    private int selectPosition = 0;

    public ComSelectDialogListAdapter(List<String> lists, Context mContext) {
        super();
        this.lists = lists;
        factory = LayoutInflater.from(mContext);
    }

    public void selectItem(int position) {
        selectPosition = position;
        notifyDataSetChanged();
    }

    public int getSelectItem() {
        return selectPosition;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = factory.inflate(R.layout.address_select_dialog_item, null);
            holder = new ViewHolder();

            holder.tv_select_address = (TextView) convertView.findViewById(R.id.tv_select_address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_select_address.setText(lists.get(position));

        return convertView;
    }

    class ViewHolder {
        TextView tv_select_address;
    }

}
