package com.park61.teacherhelper.module.clazz.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.clazz.ClazzChildListActivity;
import com.park61.teacherhelper.module.clazz.ClazzEditActivity;
import com.park61.teacherhelper.module.clazz.ClazzQRCodeActivity;
import com.park61.teacherhelper.module.clazz.ClazzQRCodeNewActivity;
import com.park61.teacherhelper.module.clazz.ClazzTeacherListActivity;
import com.park61.teacherhelper.module.clazz.bean.TeachGClass;
import com.park61.teacherhelper.module.dict.RequestCode;

import org.w3c.dom.Text;

import java.util.List;

/**
 * 班级管理首页班级列表Adapter
 * Created by zhangchi on 2017/8/16.
 */

public class ClazzMgrListAdapter extends BaseAdapter {

    private List<TeachGClass> dataList;
    private Activity mContext;

    public ClazzMgrListAdapter(Context _context, List<TeachGClass> _list) {
        mContext = (Activity) _context;
        dataList = _list;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final TeachGClass clazz = dataList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.clazz_mgr_list_item, null);
            holder = new ViewHolder();
            holder.qrCodeView = (ImageView) convertView.findViewById(R.id.img_course);
            holder.fullNameView = (TextView) convertView.findViewById(R.id.tv_full_name);
            holder.schoolNameView = (TextView) convertView.findViewById(R.id.tv_school_name);
            holder.teacherNumView = (TextView) convertView.findViewById(R.id.tv_teacher_num);
            holder.kidNumView = (TextView) convertView.findViewById(R.id.tv_kid_num);
            holder.lay2 = (RelativeLayout) convertView.findViewById(R.id.lay_2);
            holder.lay3 = (LinearLayout) convertView.findViewById(R.id.lay_3);
            holder.lay4 = (LinearLayout) convertView.findViewById(R.id.lay_4);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //ImageManager.getInstance().displayImg(holder.qrCodeView, clazz.getQrCode());
        holder.fullNameView.setText(clazz.getFullName());
        holder.schoolNameView.setText(clazz.getSchoolName());
        holder.teacherNumView.setText(String.valueOf(clazz.getCountTeacher()));
        holder.kidNumView.setText(String.valueOf(clazz.getCountChild()));


        //初始化点击事件
        //Get class Id
        final String clazzId = String.valueOf(clazz.getId());

        holder.lay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ClazzEditActivity.class);
                intent.putExtra("clazzId", clazzId);
                intent.putExtra("list_location", position);

                Bundle bundle = new Bundle();
                bundle.putSerializable("clazz", clazz);

                intent.putExtras(bundle);

                mContext.startActivityForResult(intent, RequestCode.REQUET_CODE_EDIT_CLAZZ.getCode());
            }
        });

        holder.lay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ClazzTeacherListActivity.class);
                intent.putExtra("clazzId", clazzId);
                mContext.startActivity(intent);
            }
        });

        holder.lay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ClazzChildListActivity.class);
                intent.putExtra("clazzId", clazzId);
                intent.putExtra("clazzName", clazz.getName());
                mContext.startActivity(intent);
            }
        });


        holder.qrCodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, ClazzQRCodeActivity.class);
//
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("clazz", clazz);
//                intent.putExtras(bundle);
//
//                mContext.startActivity(intent);
                Intent it = new Intent(mContext, ClazzQRCodeNewActivity.class);
                it.putExtra("teachClassId", clazz.getId() + "");
                mContext.startActivity(it);
            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView qrCodeView;
        TextView fullNameView;
        TextView schoolNameView;
        TextView teacherNumView;
        TextView kidNumView;
        RelativeLayout lay2;
        LinearLayout lay3;
        LinearLayout lay4;
    }
}
