package com.park61.teacherhelper.module.home.adapter;

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
import com.park61.teacherhelper.module.home.MyServiceDetailsActivity;
import com.park61.teacherhelper.module.home.bean.FileItemSource;
import com.park61.teacherhelper.module.home.bean.MServiceItem;

import java.util.List;


/**
 * 我的服务列表adapter
 * creatby super on2018/2/1
 */
public class MyServiceListAdapter extends BaseAdapter {

    private List<MServiceItem> mList;
    private Context mContext;
    private OnConfirmClickedLsner mOnConfirmClickedLsner;

    public MyServiceListAdapter(Context context, List<MServiceItem> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.my_service_listitem, null);
            holder = new ViewHolder();
            holder.tv_service_no = (TextView) convertView.findViewById(R.id.tv_service_no);
            holder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
            holder.tv_group_addr = (TextView) convertView.findViewById(R.id.tv_group_addr);
            holder.tv_status_name = (TextView) convertView.findViewById(R.id.tv_status_name);
            holder.btn_confirm_finish = (Button) convertView.findViewById(R.id.btn_confirm_finish);
            holder.area_item_root = convertView.findViewById(R.id.area_item_root);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MServiceItem item = mList.get(position);
        holder.tv_service_no.setText(item.getBatchCode());
        holder.tv_group_name.setText(item.getSchoolName());
        holder.tv_group_addr.setText(item.getAddressName());
        holder.tv_status_name.setText(item.getStatusName());
        holder.btn_confirm_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnConfirmClickedLsner != null)
                    mOnConfirmClickedLsner.onCliked(position);
            }
        });
        if (item.getStatus() == 0) {//只有“待受理”状态的高亮显示
            holder.tv_status_name.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
        } else {
            holder.tv_status_name.setTextColor(mContext.getResources().getColor(R.color.g333333));
        }

        if (item.getStatus() == 1) {//已受理
            holder.btn_confirm_finish.setVisibility(View.VISIBLE);
        } else {
            holder.btn_confirm_finish.setVisibility(View.GONE);
        }
        holder.area_item_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, MyServiceDetailsActivity.class);
                it.putExtra("applyId", mList.get(position).getId());
                mContext.startActivity(it);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_service_no;
        TextView tv_group_name;
        TextView tv_group_addr;
        TextView tv_status_name;
        Button btn_confirm_finish;
        View area_item_root;
    }

    public interface OnConfirmClickedLsner {
        void onCliked(int index);
    }

    public void setOnConfirmClickedLsner(OnConfirmClickedLsner listener) {
        this.mOnConfirmClickedLsner = listener;
    }
}
