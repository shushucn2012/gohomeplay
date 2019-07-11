package com.park61.teacherhelper.module.my.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.module.course.ExpertCourseMainActivity;
import com.park61.teacherhelper.module.my.bean.ComponBean;

import java.util.List;

/**
 * Created by chenlie on 2018/5/8.
 */

public class ComponListAdapter extends RecyclerView.Adapter<ComponListAdapter.ComponHolder> {

    private Context mContext;
    private List<ComponBean> datas;

    public ComponListAdapter(Context context, List<ComponBean> tmp) {
        datas = tmp;
        this.mContext = context;
    }

    @Override
    public ComponHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reward_list_item, parent, false);
        return new ComponHolder(v);
    }

    @Override
    public void onBindViewHolder(ComponHolder holder, int position) {
        ComponBean b = datas.get(position);
        holder.price.setText(FU.strDbFmt(b.getRuleValue2()));
        holder.name.setText(b.getTitle());
        holder.time.setText(b.getLimitStart() + "-" + b.getLimitEnd());
        holder.tv_goto_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abilityIt = new Intent(mContext, ExpertCourseMainActivity.class);
                abilityIt.putExtra("couponId", b.getId());
                mContext.startActivity(abilityIt);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ComponHolder extends RecyclerView.ViewHolder {

        TextView price;
        TextView name;
        TextView time;
        TextView tv_goto_use;

        public ComponHolder(View itemView) {
            super(itemView);

            price = (TextView) itemView.findViewById(R.id.compon_price);
            name = (TextView) itemView.findViewById(R.id.compon_name);
            time = (TextView) itemView.findViewById(R.id.time_limit);
            tv_goto_use = (TextView) itemView.findViewById(R.id.tv_goto_use);
        }
    }
}
