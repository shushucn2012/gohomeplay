package com.park61.teacherhelper.module.member.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.member.bean.VipGroupMemberBean;

import java.util.List;

public class UnAuthMemberListAdapter extends BaseAdapter {

    private Context mContext;
    private List<VipGroupMemberBean> datas;
    private OnOperateListener mOnOperateListener;

    public UnAuthMemberListAdapter(Context context, List<VipGroupMemberBean> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_unauth_memberlist, null);
            holder = new ViewHolder();
            holder.img_member_header = convertView.findViewById(R.id.img_member_header);
            holder.tv_member_name = convertView.findViewById(R.id.tv_member_name);
            holder.tv_member_duty = convertView.findViewById(R.id.tv_member_duty);
            holder.tv_member_mobile = convertView.findViewById(R.id.tv_member_mobile);
            holder.tv_passed = convertView.findViewById(R.id.tv_passed);
            holder.area_pass = convertView.findViewById(R.id.area_pass);
            holder.area_reject = convertView.findViewById(R.id.area_reject);
            holder.bottom_line = convertView.findViewById(R.id.bottom_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        VipGroupMemberBean itemBean = datas.get(position);
        ImageManager.getInstance().displayImg(holder.img_member_header, itemBean.getPictureUrl(), R.mipmap.mine_icon);
        holder.tv_member_name.setText(itemBean.getUserName());
        holder.tv_member_duty.setText(itemBean.getDutyName());
        holder.tv_member_mobile.setText(itemBean.getUserMobile());
        if (itemBean.getStatus() == 2) {//已通过
            holder.tv_passed.setVisibility(View.VISIBLE);
            holder.area_pass.setVisibility(View.GONE);
            holder.area_reject.setVisibility(View.GONE);
        } else {
            holder.tv_passed.setVisibility(View.GONE);
            holder.area_pass.setVisibility(View.VISIBLE);
            holder.area_reject.setVisibility(View.VISIBLE);
        }
        if (position == datas.size() - 1) {
            holder.bottom_line.setVisibility(View.INVISIBLE);
        } else {
            holder.bottom_line.setVisibility(View.VISIBLE);
        }
        holder.area_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnOperateListener != null)
                    mOnOperateListener.onPassClicked(position);
            }
        });
        holder.area_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnOperateListener != null)
                    mOnOperateListener.onRejectClicked(position);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView img_member_header;
        TextView tv_member_name, tv_member_duty, tv_member_mobile, tv_passed;
        View bottom_line, area_pass, area_reject;
    }

    public interface OnOperateListener {
        void onPassClicked(int pos);

        void onRejectClicked(int pos);
    }

    public void setOnOperateListener(OnOperateListener listener) {
        this.mOnOperateListener = listener;
    }

}
