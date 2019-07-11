package com.park61.teacherhelper.module.workplan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lijiankun24.shadowlayout.ShadowLayout;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.workplan.MySubsWpsActivity;
import com.park61.teacherhelper.module.workplan.bean.TempBean;
import com.park61.teacherhelper.module.workplan.bean.WorkPlanCateBean;
import com.park61.teacherhelper.widget.gridview.GridViewForScrollView;

import java.util.List;

public class WpCatesAdapter extends BaseAdapter {

    private List<WorkPlanCateBean> mList;
    private Context mContext;

    public WpCatesAdapter(Context context, List<WorkPlanCateBean> list) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public WorkPlanCateBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_wp_cates, null);
            holder = new ViewHolder();
            holder.tv_wpcate_name = convertView.findViewById(R.id.tv_wpcate_name);
            holder.img_wpcate_cover = convertView.findViewById(R.id.img_wpcate_cover);
            holder.gv_my_wp = convertView.findViewById(R.id.gv_my_wp);
            holder.gv_my_wp.setFocusable(false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final WorkPlanCateBean item = mList.get(position);
        holder.tv_wpcate_name.setText(item.getCategoryName());
        ImageManager.getInstance().displayImg(holder.img_wpcate_cover, item.getCategoryUrl(), R.mipmap.img_default_h);
        InnerWpTempAdapter mInnerWpTempAdapter = new InnerWpTempAdapter(mContext, item.getTaskCalendarTemplates());
        holder.gv_my_wp.setAdapter(mInnerWpTempAdapter);
        /*new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.out("=====================mInnerWpTempAdapter_notifyDataSetChanged_again========================");
                mInnerWpTempAdapter.notifyDataSetChanged();
            }
        }, 500);*/
        return convertView;
    }

    class ViewHolder {
        TextView tv_wpcate_name;
        ImageView img_wpcate_cover;
        GridViewForScrollView gv_my_wp;
    }

    public class InnerWpTempAdapter extends BaseAdapter {

        private List<WorkPlanCateBean.TaskCalendarTemplatesBean> mList;
        private Context mContext;

        public InnerWpTempAdapter(Context _context, List<WorkPlanCateBean.TaskCalendarTemplatesBean> _list) {
            this.mList = _list;
            this.mContext = _context;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public WorkPlanCateBean.TaskCalendarTemplatesBean getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_mywp_temp, null);
                holder = new ViewHolder();
                holder.img_item = convertView.findViewById(R.id.img_item);
                //holder.img_shadow_bg = convertView.findViewById(R.id.img_shadow_bg);
                holder.tv_item_name = convertView.findViewById(R.id.tv_item_name);
                //holder.shadow_img = convertView.findViewById(R.id.shadow_img);
                holder.area_bottom_progress = convertView.findViewById(R.id.area_bottom_progress);
                holder.img_vip_right = convertView.findViewById(R.id.img_vip_right);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.area_bottom_progress.setVisibility(View.GONE);
            ImageManager.getInstance().displayImg(holder.img_item, mList.get(position).getCoverUrl(), R.mipmap.img_default_h);
            //ImageManager.getInstance().displayImg(holder.img_shadow_bg, mList.get(position).getCoverUrl(), R.mipmap.img_default_h);
            //holder.shadow_img.setShadowColor(Color.parseColor("#66000000"));
            holder.tv_item_name.setText(mList.get(position).getName());
            if (mList.get(position).getMemberTag() == 1) {
                holder.img_vip_right.setVisibility(View.VISIBLE);
            } else {
                holder.img_vip_right.setVisibility(View.GONE);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, MySubsWpsActivity.class);
                    it.putExtra("taskCalendarTemplateId", mList.get(position).getId());
                    mContext.startActivity(it);
                }
            });
            return convertView;
        }

        class ViewHolder {
            ImageView img_item, img_vip_right;
            TextView tv_item_name;
            View area_bottom_progress;
            //ShadowLayout shadow_img;
        }
    }

}
