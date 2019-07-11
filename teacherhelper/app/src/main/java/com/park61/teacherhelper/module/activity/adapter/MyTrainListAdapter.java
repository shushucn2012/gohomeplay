package com.park61.teacherhelper.module.activity.adapter;

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
import com.park61.teacherhelper.module.activity.TrainFeedBackActivity;
import com.park61.teacherhelper.module.activity.TrainFeedBackSucActivity;
import com.park61.teacherhelper.module.activity.bean.ServiceApplyBean;
import com.park61.teacherhelper.module.activity.bean.TrainBean;

import java.util.List;

public class MyTrainListAdapter extends BaseAdapter {

    private List<TrainBean> mList;
    private Context mContext;
    private LayoutInflater factory;
    private OnContactTrainerClickedListener mOnContactTrainerClickedListener;

    public MyTrainListAdapter(Context _context, List<TrainBean> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public TrainBean getItem(int position) {
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
            convertView = factory.inflate(R.layout.item_train_list, null);
            holder = new ViewHolder();
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_start_time = convertView.findViewById(R.id.tv_start_time);
            holder.tv_end_time = convertView.findViewById(R.id.tv_end_time);
            holder.tv_duration = convertView.findViewById(R.id.tv_duration);
            holder.tv_teacher_name = convertView.findViewById(R.id.tv_teacher_name);
            holder.tv_contact_teacher = convertView.findViewById(R.id.tv_contact_teacher);
            holder.btn_evaluate = convertView.findViewById(R.id.btn_evaluate);
            holder.img_state = convertView.findViewById(R.id.img_state);
            holder.top_space = convertView.findViewById(R.id.top_space);
            holder.area_contact_trainer = convertView.findViewById(R.id.area_contact_trainer);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TrainBean itemBean = mList.get(position);
        holder.tv_title.setText(itemBean.getTrainingType());
        holder.tv_start_time.setText("培训开始时间：" + itemBean.getShowStartTime());
        holder.tv_end_time.setText("培训结束时间：" + itemBean.getShowEndTime());
        holder.tv_duration.setText("培训时长：" + itemBean.getDuration() + "小时");
        holder.tv_teacher_name.setText("培训师：" + itemBean.getTrainerName());
        if (itemBean.getIsEvaluate() == 0) {//0，评价反馈，1，已评价
            holder.btn_evaluate.setText("评价反馈");
            holder.btn_evaluate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, TrainFeedBackActivity.class);
                    intent.putExtra("trainingId", itemBean.getId());
                    mContext.startActivity(intent);
                }
            });
        } else {
            holder.btn_evaluate.setText("已评价");
            holder.btn_evaluate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, TrainFeedBackSucActivity.class);
                    it.putExtra("trainingId", itemBean.getId());
                    mContext.startActivity(it);
                }
            });
        }
        if (itemBean.getStatus() == 3) {//已完成
            holder.img_state.setVisibility(View.VISIBLE);
            holder.btn_evaluate.setVisibility(View.GONE);
        } else {
            holder.img_state.setVisibility(View.GONE);
            holder.btn_evaluate.setVisibility(View.VISIBLE);
        }

        if (position == 0) {
            holder.top_space.setVisibility(View.VISIBLE);
        } else {
            holder.top_space.setVisibility(View.GONE);
        }

        holder.area_contact_trainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnContactTrainerClickedListener != null)
                    mOnContactTrainerClickedListener.onClicked(position);
            }
        });

       /* convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TrainFeedBackActivity.class);
                intent.putExtra("trainingId", itemBean.getId());
                mContext.startActivity(intent);
            }
        });*/

        return convertView;
    }

    class ViewHolder {
        TextView tv_title, tv_start_time, tv_end_time, tv_duration, tv_teacher_name, tv_contact_teacher;
        Button btn_evaluate;
        ImageView img_state;
        View top_space, area_contact_trainer;
    }

    public void setOnContactTrainerClickedListener(OnContactTrainerClickedListener listener) {
        this.mOnContactTrainerClickedListener = listener;
    }

    public interface OnContactTrainerClickedListener {
        void onClicked(int pos);
    }

}
