package com.park61.teacherhelper.module.my.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.my.bean.ClassBean;

import java.util.List;

/**
 * Created by chenlie on 2018/4/11.
 * 选择班级
 */

public class ChooseClassAdapter extends RecyclerView.Adapter<ChooseClassAdapter.ClassAdapterHolder> {

    private Context mContext;
    private List<ClassBean> list;

    public ChooseClassAdapter(Context context, List<ClassBean> datas){
        mContext = context;
        list = datas;
    }

    @Override
    public ClassAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.choose_class_item, parent, false);
        return new ClassAdapterHolder(v);
    }

    @Override
    public void onBindViewHolder(ClassAdapterHolder holder, int position) {
        ClassBean b = list.get(position);
        holder.className.setText(b.getName());
        holder.imgCheck.setImageResource(b.isCheck() ? R.mipmap.download_check :R.mipmap.download_uncheck);

        holder.itemView.setOnClickListener(v -> {
            b.setCheck(!b.isCheck());
            notifyDataSetChanged();
        });

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

        TextView className;
        ImageView imgCheck;
        View line;

        public ClassAdapterHolder(View itemView) {
            super(itemView);
            className = (TextView) itemView.findViewById(R.id.class_name);
            imgCheck = (ImageView) itemView.findViewById(R.id.check_state);
            line = itemView.findViewById(R.id.bt_line);
        }
    }
}
