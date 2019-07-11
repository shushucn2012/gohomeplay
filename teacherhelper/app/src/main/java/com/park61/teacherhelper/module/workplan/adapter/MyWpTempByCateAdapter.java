package com.park61.teacherhelper.module.workplan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lijiankun24.shadowlayout.ShadowLayout;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.workplan.MySubsWpsActivity;
import com.park61.teacherhelper.module.workplan.bean.MySubWorkPlanCateBean;
import com.park61.teacherhelper.module.workplan.bean.TempBean;
import com.park61.teacherhelper.module.workplan.bean.WorkPlanCateBean;
import com.park61.teacherhelper.widget.gridview.GridViewForScrollView;

import java.util.List;

public class MyWpTempByCateAdapter extends BaseAdapter {

    private List<MySubWorkPlanCateBean> mList;
    private Context mContext;

    public MyWpTempByCateAdapter(Context context, List<MySubWorkPlanCateBean> list) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public MySubWorkPlanCateBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_mysubwp_cates, null);
            holder = new ViewHolder();
            holder.tv_wpcate_name = convertView.findViewById(R.id.tv_wpcate_name);
            holder.gv_my_wp = convertView.findViewById(R.id.gv_my_wp);
            holder.gv_my_wp.setFocusable(false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MySubWorkPlanCateBean item = mList.get(position);
        holder.tv_wpcate_name.setText(item.getCategoryName());
        MyWpTempAdapter mInnerWpTempAdapter = new MyWpTempAdapter(mContext, item.getSubscribeTaskCalendars());
        holder.gv_my_wp.setAdapter(mInnerWpTempAdapter);
//        new android.os.Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.out("=====================mInnerWpTempAdapter_notifyDataSetChanged_again========================");
//                mInnerWpTempAdapter.notifyDataSetChanged();
//            }
//        }, 500);
        return convertView;
    }

    class ViewHolder {
        TextView tv_wpcate_name;
        GridViewForScrollView gv_my_wp;
    }

}
