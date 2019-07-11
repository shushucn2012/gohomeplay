package com.park61.teacherhelper.module.workplan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.workplan.bean.TeacherBean;

import java.util.List;

/**
 * Created by chenlie on 2018/4/11.
 * 选择老师
 */

public class ChooseTeacherAdapter extends RecyclerView.Adapter<ChooseTeacherAdapter.ClassAdapterHolder> {

    private Context mContext;
    private List<TeacherBean> list;

    public ChooseTeacherAdapter(Context context, List<TeacherBean> datas){
        mContext = context;
        list = datas;
    }

    @Override
    public ClassAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.choose_teacher_item, parent, false);
        return new ClassAdapterHolder(v);
    }

    @Override
    public void onBindViewHolder(ClassAdapterHolder holder, int position) {
        TeacherBean b = list.get(position);
        holder.name.setText(b.getName());
        holder.duty.setText(b.getDutyName());

        if(b.getIsCheck() == 0){
            //可勾选
            holder.itemView.setOnClickListener(v -> {
                b.setSelect(!b.isSelect());
                notifyDataSetChanged();
            });
            holder.name.setTextColor(Color.parseColor("#333333"));
            holder.duty.setTextColor(Color.parseColor("#333333"));
            if(b.isSelect()){
                holder.imgCheck.setImageResource(R.mipmap.task_gou);
            }else{
                holder.imgCheck.setImageResource(R.mipmap.download_uncheck);
            }
        }else{
            //不可勾选
            holder.itemView.setOnClickListener(null);
            holder.name.setTextColor(Color.parseColor("#999999"));
            holder.duty.setTextColor(Color.parseColor("#999999"));
            holder.imgCheck.setImageResource(R.mipmap.task_nogou);
        }

        if(position == list.size()-1){
            holder.line.setVisibility(View.GONE);
        }else{
            holder.line.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ClassAdapterHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView duty;
        ImageView imgCheck;
        View line;

        public ClassAdapterHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.teacher_name);
            duty = (TextView) itemView.findViewById(R.id.teacher_duty);
            imgCheck = (ImageView) itemView.findViewById(R.id.check_state);
            line = itemView.findViewById(R.id.bt_line);
        }
    }
}
