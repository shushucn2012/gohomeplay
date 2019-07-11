package com.park61.teacherhelper.module.clazz.adapter;

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
import com.park61.teacherhelper.module.clazz.bean.FamilyConBookComtTempBean;
import com.park61.teacherhelper.module.workplan.bean.TaskPerson;
import com.park61.teacherhelper.module.workplan.bean.TaskRole;
import com.park61.teacherhelper.widget.listview.ListViewForScrollView;

import java.util.List;

public class FamilyConBookTempsAdapter extends BaseAdapter {

    private List<FamilyConBookComtTempBean> mList;
    private Context mContext;
    private OnCheckedListener mOnCheckedListener;

    public FamilyConBookTempsAdapter(Context _context, List<FamilyConBookComtTempBean> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public FamilyConBookComtTempBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.familyconbook_temps_item, null);
            holder = new ViewHolder();
            holder.area_title = convertView.findViewById(R.id.area_title);
            holder.tv_role_title = (TextView) convertView.findViewById(R.id.tv_role_title);
            holder.img_down_arrow = convertView.findViewById(R.id.img_down_arrow);
            holder.lv_persion = (ListViewForScrollView) convertView.findViewById(R.id.lv_persion);
            holder.lv_persion.setFocusable(false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final FamilyConBookComtTempBean item = mList.get(position);

        holder.tv_role_title.setText(item.getTitle());
        holder.lv_persion.setAdapter(new PersonAdapter(item.getTeachCommentTemplateDetailList()));
        final ViewHolder finalHolder = holder;
        holder.area_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalHolder.lv_persion.getVisibility() == View.VISIBLE) {
                    finalHolder.lv_persion.setVisibility(View.GONE);
                    finalHolder.img_down_arrow.setImageResource(R.mipmap.xiala);
                } else {
                    finalHolder.lv_persion.setVisibility(View.VISIBLE);
                    finalHolder.img_down_arrow.setImageResource(R.mipmap.xiala_up);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        View area_title;
        ImageView img_down_arrow;
        TextView tv_role_title;
        ListViewForScrollView lv_persion;
    }

    private class PersonAdapter extends BaseAdapter {

        private List<FamilyConBookComtTempBean.TeachCommentTemplateDetailListBean> listPictrue;

        public PersonAdapter(List<FamilyConBookComtTempBean.TeachCommentTemplateDetailListBean> list) {
            this.listPictrue = list;
        }

        @Override
        public int getCount() {
            return listPictrue.size();
        }

        @Override
        public FamilyConBookComtTempBean.TeachCommentTemplateDetailListBean getItem(int position) {
            return listPictrue.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.familyconbook_temps_item_child, null);
            View cardview_item_root = convertView.findViewById(R.id.cardview_item_root);
            ImageView img_check = (ImageView) convertView.findViewById(R.id.img_check);
            TextView tv_temp_content = (TextView) convertView.findViewById(R.id.tv_temp_content);
            View bottom_line = convertView.findViewById(R.id.bottom_line);
            if (listPictrue.get(position).isChecked()) {
                img_check.setImageResource(R.mipmap.download_check);
            } else {
                img_check.setImageResource(R.mipmap.download_uncheck);
            }
            tv_temp_content.setText(listPictrue.get(position).getContent());
            cardview_item_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listPictrue.get(position).isChecked()) {
                        listPictrue.get(position).setChecked(false);
                    } else {
                        listPictrue.get(position).setChecked(true);
                    }
                    FamilyConBookTempsAdapter.this.notifyDataSetChanged();
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

}
