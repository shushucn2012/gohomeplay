package com.park61.teacherhelper.module.member.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.course.ExpertCourseListActivity;
import com.park61.teacherhelper.module.home.bean.TeacherCourseBean;
import com.park61.teacherhelper.module.member.bean.VipGroupMemberBean;

import java.util.List;

public class CurMemberListAdapter extends BaseAdapter {

    private Context mContext;
    private List<VipGroupMemberBean> datas;
    private boolean isDeleteBtnShow = false;
    private OnOperateClickedListener mOnOperateClickedListener;

    public CurMemberListAdapter(Context context, List<VipGroupMemberBean> list) {
        mContext = context;
        datas = list;
    }

    @Override
    public int getCount() {
        return datas.size();
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_cur_memberlist, null);
            holder = new ViewHolder();
            holder.img_member_header = convertView.findViewById(R.id.img_member_header);
            holder.img_admin_label = convertView.findViewById(R.id.img_admin_label);
            holder.tv_member_name = convertView.findViewById(R.id.tv_member_name);
            holder.tv_member_duty = convertView.findViewById(R.id.tv_member_duty);
            holder.btn_delete = convertView.findViewById(R.id.btn_delete);
            holder.bottom_line = convertView.findViewById(R.id.bottom_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        VipGroupMemberBean itemBean = datas.get(position);
        ImageManager.getInstance().displayImg(holder.img_member_header, itemBean.getPictureUrl(), R.mipmap.mine_icon);
        holder.tv_member_name.setText(itemBean.getUserName());
        holder.tv_member_duty.setText(itemBean.getDutyName());
        if (position == datas.size() - 1) {
            holder.bottom_line.setVisibility(View.INVISIBLE);
        } else {
            holder.bottom_line.setVisibility(View.VISIBLE);
        }
        if (isDeleteBtnShow && itemBean.getIsAdmin() == 0) {
            holder.btn_delete.setVisibility(View.VISIBLE);
        } else {
            holder.btn_delete.setVisibility(View.GONE);
        }
        if (itemBean.getIsAdmin() == 0) {
            holder.img_admin_label.setVisibility(View.GONE);
        } else {
            holder.img_admin_label.setVisibility(View.VISIBLE);
        }
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnOperateClickedListener != null)
                    mOnOperateClickedListener.onDelete(position);
            }
        });
        return convertView;
    }

    public void showDeleteBtn(boolean isShow) {
        isDeleteBtnShow = isShow;
        notifyDataSetChanged();
    }

    public boolean isDeleteBtnShow() {
        return isDeleteBtnShow;
    }

    public interface OnOperateClickedListener {
        void onDelete(int pos);
    }

    public void setOnOperateClickedListener(OnOperateClickedListener listener) {
        this.mOnOperateClickedListener = listener;
    }

    class ViewHolder {
        ImageView img_member_header, img_admin_label;
        TextView tv_member_name, tv_member_duty;
        View bottom_line;
        Button btn_delete;
    }
}
