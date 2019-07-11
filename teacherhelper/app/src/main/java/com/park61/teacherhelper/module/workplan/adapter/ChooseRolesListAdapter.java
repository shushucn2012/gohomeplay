package com.park61.teacherhelper.module.workplan.adapter;

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
import com.park61.teacherhelper.module.workplan.bean.TaskPerson;
import com.park61.teacherhelper.module.workplan.bean.TaskRole;
import com.park61.teacherhelper.widget.listview.ListViewForScrollView;

import java.util.List;

public class ChooseRolesListAdapter extends BaseAdapter {

    private List<TaskRole> mList;
    private Context mContext;
    private OnCheckedListener mOnCheckedListener;

    public ChooseRolesListAdapter(Context _context, List<TaskRole> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public TaskRole getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.role_list_item, null);
            holder = new ViewHolder();
            holder.area_title = convertView.findViewById(R.id.area_title);
            holder.tv_role_title = (TextView) convertView.findViewById(R.id.tv_role_title);
            holder.lv_persion = (ListViewForScrollView) convertView.findViewById(R.id.lv_persion);
            holder.lv_persion.setFocusable(false);
            holder.img_check = (ImageView) convertView.findViewById(R.id.img_check);
            holder.tv_all_check = (TextView) convertView.findViewById(R.id.tv_all_check);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TaskRole roleItem = mList.get(position);

        boolean curCheckState = true;
        for (int i = 0; i < roleItem.getPersons().size(); i++) {
            if (!roleItem.getPersons().get(i).isChecked()) {
                curCheckState = false;
                break;
            }
        }
        roleItem.setChecked(curCheckState);

        if (roleItem.isChecked()) {
            holder.tv_all_check.setText("全不选");
            holder.img_check.setImageResource(R.mipmap.download_check);
        } else {
            holder.tv_all_check.setText("全选");
            holder.img_check.setImageResource(R.mipmap.download_uncheck);
        }
        holder.tv_role_title.setText(roleItem.getTitle());
        holder.lv_persion.setAdapter(new PersonAdapter(roleItem.getPersons()));
        holder.img_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roleItem.isChecked()) {
                    roleItem.setChecked(false);
                    for (int i = 0; i < roleItem.getPersons().size(); i++) {
                        roleItem.getPersons().get(i).setChecked(false);
                    }
                } else {
                    roleItem.setChecked(true);
                    for (int i = 0; i < roleItem.getPersons().size(); i++) {
                        roleItem.getPersons().get(i).setChecked(true);
                    }
                }
                notifyDataSetChanged();
                if (mOnCheckedListener != null)
                    mOnCheckedListener.onChecked();
            }
        });
        final ViewHolder finalHolder = holder;
        holder.area_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalHolder.lv_persion.getVisibility() == View.VISIBLE) {
                    finalHolder.lv_persion.setVisibility(View.GONE);
                } else {
                    finalHolder.lv_persion.setVisibility(View.VISIBLE);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        View area_title;
        ImageView img_check;
        TextView tv_role_title, tv_all_check;
        ListViewForScrollView lv_persion;
    }

    private class PersonAdapter extends BaseAdapter {

        private List<TaskPerson> listPictrue;

        public PersonAdapter(List<TaskPerson> list) {
            this.listPictrue = list;
        }

        @Override
        public int getCount() {
            return listPictrue.size();
        }

        @Override
        public TaskPerson getItem(int position) {
            return listPictrue.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.person_item, null);
            ImageView img_check = (ImageView) convertView.findViewById(R.id.img_check);
            ImageView img_person = (ImageView) convertView.findViewById(R.id.img_person);
            TextView tv_person_name = (TextView) convertView.findViewById(R.id.tv_person_name);
            TextView tv_person_role = (TextView) convertView.findViewById(R.id.tv_person_role);
            View bottom_line = convertView.findViewById(R.id.bottom_line);
            if (listPictrue.get(position).isChecked()) {
                img_check.setImageResource(R.mipmap.download_check);
            } else {
                img_check.setImageResource(R.mipmap.download_uncheck);
            }
            tv_person_name.setText(TextUtils.isEmpty(listPictrue.get(position).getName()) ? listPictrue.get(position).getMobile() : listPictrue.get(position).getName());
            tv_person_role.setText(getAllStr(listPictrue.get(position).getClassName()));
            ImageManager.getInstance().displayImg(img_person, listPictrue.get(position).getPictureUrl());
            img_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listPictrue.get(position).isChecked()) {
                        listPictrue.get(position).setChecked(false);
                    } else {
                        listPictrue.get(position).setChecked(true);
                    }
                    ChooseRolesListAdapter.this.notifyDataSetChanged();
                    if (mOnCheckedListener != null)
                        mOnCheckedListener.onChecked();
                }
            });
            if (position == listPictrue.size() - 1) {
                bottom_line.setVisibility(View.INVISIBLE);
            } else {
                bottom_line.setVisibility(View.VISIBLE);
            }
            return convertView;
        }
    }

    public interface OnCheckedListener {
        public void onChecked();
    }

    public void setOnCheckedListener(OnCheckedListener listener) {
        this.mOnCheckedListener = listener;
    }

    private String getAllStr(String[] arr) {
        if (arr == null || arr.length == 0)
            return "";
        String ss = "";
        for (int i = 0; i < arr.length; i++) {
            ss += arr[i];
            ss += ",";
        }
        return ss.substring(0, ss.length() - 1);
    }
}
