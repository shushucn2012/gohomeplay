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
import com.park61.teacherhelper.module.clazz.bean.StudentBean;
import com.park61.teacherhelper.module.workplan.bean.TaskPerson;
import com.park61.teacherhelper.module.workplan.bean.TaskRole;
import com.park61.teacherhelper.widget.listview.ListViewForScrollView;

import java.util.List;

public class FamilyChoseStuListAdapter extends BaseAdapter {

    private List<StudentBean> mList;
    private Context mContext;
    private OnCheckedListener mOnCheckedListener;

    public FamilyChoseStuListAdapter(Context _context, List<StudentBean> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public StudentBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.familyconbook_chose_stus_item, null);
            holder = new ViewHolder();
            holder.area_item_content = convertView.findViewById(R.id.area_item_content);
            holder.tv_stu_name = convertView.findViewById(R.id.tv_stu_name);
            holder.img_check = convertView.findViewById(R.id.img_check);
            holder.img_header = convertView.findViewById(R.id.img_header);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final StudentBean roleItem = mList.get(position);
        if (roleItem.isChecked()) {
            holder.img_check.setImageResource(R.mipmap.download_check);
        } else {
            holder.img_check.setImageResource(R.mipmap.download_uncheck);
        }
        holder.tv_stu_name.setText(roleItem.getName());
        ImageManager.getInstance().displayImg(holder.img_header, roleItem.getPictureUrl(), R.mipmap.headimg_default_img);
        holder.area_item_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roleItem.isChecked()) {
                    roleItem.setChecked(false);
                } else {
                    roleItem.setChecked(true);
                }
                notifyDataSetChanged();
                if (mOnCheckedListener != null)
                    mOnCheckedListener.onChecked();
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_stu_name;
        ImageView img_check, img_header;
        View area_item_content;
    }

    public interface OnCheckedListener {
        public void onChecked();
    }

    public void setOnCheckedListener(OnCheckedListener listener) {
        this.mOnCheckedListener = listener;
    }
}
