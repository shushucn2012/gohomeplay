package com.park61.teacherhelper.module.workplan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lijiankun24.shadowlayout.ShadowLayout;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.home.bean.ContentItem;
import com.park61.teacherhelper.module.workplan.MySubsWpsActivity;
import com.park61.teacherhelper.module.workplan.bean.TempBean;
import com.park61.teacherhelper.widget.circleimageview.CircleImageView;

import java.util.List;

public class MyWpTempAdapter extends BaseAdapter {

    private List<TempBean> mList;
    private Context mContext;

    public MyWpTempAdapter(Context _context, List<TempBean> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public TempBean getItem(int position) {
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
            holder.tv_item_name = convertView.findViewById(R.id.tv_item_name);
            holder.tv_progresstext = convertView.findViewById(R.id.tv_progresstext);
            holder.cardview_front_progress = convertView.findViewById(R.id.cardview_front_progress);
            //holder.shadow_img = convertView.findViewById(R.id.shadow_img);
            //holder.img_shadow_bg = convertView.findViewById(R.id.img_shadow_bg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TempBean tempBean = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_item, tempBean.getTemplateUrl(), R.mipmap.img_default_h);
        //ImageManager.getInstance().displayImg(holder.img_shadow_bg, tempBean.getTemplateUrl(), R.mipmap.img_default_h);
        //holder.shadow_img.setShadowColor(Color.parseColor("#66000000"));
        holder.tv_item_name.setText(tempBean.getTemplateName());
        if (tempBean.getFinishNum() > 0 && tempBean.getUnFinishNum() == 0) {
            holder.tv_progresstext.setText("完结");
            holder.tv_progresstext.setTextColor(mContext.getResources().getColor(R.color.com_orange));
        } else if (tempBean.getFinishNum() == 0 && tempBean.getUnFinishNum() > 0) {
            holder.tv_progresstext.setText("未开始");
            holder.tv_progresstext.setTextColor(mContext.getResources().getColor(R.color.btn_goods_evaluate));
        } else {
            holder.tv_progresstext.setText(tempBean.getFinishNum() + "/" + (tempBean.getFinishNum() + tempBean.getUnFinishNum()));
            holder.tv_progresstext.setTextColor(mContext.getResources().getColor(R.color.g999999));
        }
        //进度条
        ViewGroup.LayoutParams params = holder.cardview_front_progress.getLayoutParams();
        double percent = (tempBean.getFinishNum() * 1.0 / (tempBean.getFinishNum() + tempBean.getUnFinishNum()));
        params.width = DevAttr.dip2px(mContext, (float) (percent * 90));
        holder.cardview_front_progress.setLayoutParams(params);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, MySubsWpsActivity.class);
                it.putExtra("taskCalendarTemplateId", mList.get(position).getTaskCalendarTemplateId());
                mContext.startActivity(it);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView img_item;
        //ImageView img_item, img_shadow_bg;
        TextView tv_item_name, tv_progresstext;
        CardView cardview_front_progress;
        //ShadowLayout shadow_img;
    }
}
