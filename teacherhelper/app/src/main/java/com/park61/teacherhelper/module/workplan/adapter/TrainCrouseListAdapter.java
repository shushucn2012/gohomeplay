package com.park61.teacherhelper.module.workplan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.DateTool;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.clazz.adapter.ParkApplyAdapter;
import com.park61.teacherhelper.module.course.SubCourseAudioActivity;
import com.park61.teacherhelper.module.course.SubCourseVideoActivity;
import com.park61.teacherhelper.module.home.CourseDetailsActivity;
import com.park61.teacherhelper.module.workplan.bean.KnowledgeBean;
import com.park61.teacherhelper.widget.modular.ImageVerticalModule;

import java.util.List;

/**
 * 工作学习任务-训练课程列表
 * Created by super on 20180803
 */

public class TrainCrouseListAdapter extends BaseAdapter {

    private List<KnowledgeBean> datas;
    private Context mContext;
    private OnDeleteClickedListener mOnDeleteClickedListener;
    private boolean isEditOrAdd;//是否是添加编辑页面的列表
    private int type;//任务类型 0工作1学习
    private int taskCalendarId;//任务id

    public TrainCrouseListAdapter(Context context, List<KnowledgeBean> list, boolean isEditOrAdd, int type, int taskCalendarId) {
        mContext = context;
        datas = list;
        this.isEditOrAdd = isEditOrAdd;
        this.type = type;
        this.taskCalendarId = taskCalendarId;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public KnowledgeBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_knowledge_adapter, parent, false);
            holder = new ViewHolder();
            holder.iv_isplay = (ImageView) convertView.findViewById(R.id.iv_isplay);
            holder.iv_content = (ImageView) convertView.findViewById(R.id.iv_content);
            holder.img_delete = (ImageView) convertView.findViewById(R.id.img_delete);
            holder.title = (TextView) convertView.findViewById(R.id.content_title);
            holder.time = (TextView) convertView.findViewById(R.id.content_time);
            holder.tv_learning_num = (TextView) convertView.findViewById(R.id.tv_learning_num);
            holder.line = convertView.findViewById(R.id.bline);
            holder.btn_to_study = (Button) convertView.findViewById(R.id.btn_to_study);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        KnowledgeBean knowledgeBean = datas.get(position);
        if (1 == knowledgeBean.getCourseType()) {
            holder.iv_isplay.setVisibility(View.VISIBLE);
            holder.iv_isplay.setImageResource(R.mipmap.icon_watch);
        } else if (3 == knowledgeBean.getCourseType()) {
            holder.iv_isplay.setVisibility(View.VISIBLE);
            holder.iv_isplay.setImageResource(R.mipmap.icon_listener);
        } else {
            holder.iv_isplay.setVisibility(View.GONE);
        }

        ImageManager.getInstance().displayImg(holder.iv_content, knowledgeBean.getCoverImg());
        holder.title.setText(knowledgeBean.getTitle());
        if (isEditOrAdd) {//添加编辑时展示学习信息和删除按钮
            holder.time.setText(knowledgeBean.getStringUpdateDate());
            holder.tv_learning_num.setText(knowledgeBean.getLearningNum() + "人学习");
            holder.img_delete.setVisibility(View.VISIBLE);
            holder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnDeleteClickedListener != null)
                        mOnDeleteClickedListener.onClicked(position);
                }
            });
        } else {//详情中，可以跳转，不展示信息没有删除
            if (type == 0) {//0工作
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (datas.get(position).getCourseType() == 1) { //视频
                            Intent it = new Intent(mContext, SubCourseVideoActivity.class);
                            it.putExtra("subCourseId", datas.get(position).getId());
                            mContext.startActivity(it);
                        } else if (datas.get(position).getCourseType() == 3) {//音频
                            Intent it = new Intent(mContext, SubCourseAudioActivity.class);
                            it.putExtra("subCourseId", datas.get(position).getId());
                            mContext.startActivity(it);
                        }
                    }
                });
                holder.time.setText("");
                holder.tv_learning_num.setText("");
                holder.img_delete.setVisibility(View.GONE);
            } else {//1学习
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (datas.get(position).getCourseType() == 1) { //视频
                            Intent it = new Intent(mContext, SubCourseVideoActivity.class);
                            it.putExtra("subCourseId", datas.get(position).getId());
                            it.putExtra("taskCalendarId", taskCalendarId);
                            mContext.startActivity(it);
                        } else if (datas.get(position).getCourseType() == 3) {//音频
                            Intent it = new Intent(mContext, SubCourseAudioActivity.class);
                            it.putExtra("subCourseId", datas.get(position).getId());
                            it.putExtra("taskCalendarId", taskCalendarId);
                            mContext.startActivity(it);
                        }
                    }
                });
                if (knowledgeBean.getLearningTime() == 0) {
                    holder.time.setText("还未开始学习");
                    holder.time.setTextColor(mContext.getResources().getColor(R.color.com_orange));
                } else if (knowledgeBean.getLearningTime() > 0 && knowledgeBean.getLearningTime() < 60) {
                    holder.time.setText("已学习1分钟");
                    holder.time.setTextColor(mContext.getResources().getColor(R.color.g9b9b9b));
                } else {
                    holder.time.setText("已学习" + DateTool.formatDuring(datas.get(position).getLearningTime() * 1000)[2] + "分钟");
                    holder.time.setTextColor(mContext.getResources().getColor(R.color.g9b9b9b));
                }
                holder.tv_learning_num.setText("");
                holder.img_delete.setVisibility(View.GONE);
                holder.btn_to_study.setVisibility(View.VISIBLE);
                holder.btn_to_study.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (datas.get(position).getCourseType() == 1) { //视频
                            Intent it = new Intent(mContext, SubCourseVideoActivity.class);
                            it.putExtra("subCourseId", datas.get(position).getId());
                            it.putExtra("taskCalendarId", taskCalendarId);
                            mContext.startActivity(it);
                        } else if (datas.get(position).getCourseType() == 3) {//音频
                            Intent it = new Intent(mContext, SubCourseAudioActivity.class);
                            it.putExtra("subCourseId", datas.get(position).getId());
                            it.putExtra("taskCalendarId", taskCalendarId);
                            mContext.startActivity(it);
                        }
                    }
                });
            }
        }
        if (position == datas.size() - 1) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView iv_isplay;
        private ImageView iv_content;
        private ImageView img_delete;
        private TextView title;
        private TextView time;
        private TextView tv_learning_num;
        private View line;
        private Button btn_to_study;
    }

    public interface OnDeleteClickedListener {
        void onClicked(int position);
    }

    public void setOnDeleteClickedListener(OnDeleteClickedListener listener) {
        this.mOnDeleteClickedListener = listener;
    }

}
