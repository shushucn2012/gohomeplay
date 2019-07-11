package com.park61.teacherhelper.module.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.module.activity.bean.TrainEvaAnswerBean;
import com.park61.teacherhelper.module.activity.bean.TrainEvaQuestionBean;
import com.park61.teacherhelper.module.course.ExpertCourseMainActivity;
import com.park61.teacherhelper.module.my.bean.ComponBean;
import com.park61.teacherhelper.module.umeng.adapter.MsgCenterAdapter;
import com.park61.teacherhelper.widget.AutoLinefeedLayout;

import java.util.List;

public class TrainEvaQuestionRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<TrainEvaQuestionBean> mList;

    public TrainEvaQuestionRvAdapter(Context context, List<TrainEvaQuestionBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tran_eva_questions, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        TrainEvaQuestionBean itemBean = mList.get(position);
        Log.out("=======mList.size==========" + mList.size() + " and ===========position========" + position);
        myViewHolder.tv_question_name.setText(itemBean.getSort() + "、" + itemBean.getQuestionName());
        if (itemBean.getType() == 4 && position == mList.size() - 1) {
            myViewHolder.tv_question_name.setVisibility(View.GONE);
        } else {
            myViewHolder.tv_question_name.setVisibility(View.VISIBLE);
        }
        if (itemBean.getType() == 1) {//评分题
            myViewHolder.area_score_answers.setVisibility(View.VISIBLE);
            myViewHolder.area_pick_answers.setVisibility(View.GONE);
            myViewHolder.area_content_answers.setVisibility(View.GONE);
            initItemChosenListener(myViewHolder, itemBean);
        } else if (itemBean.getType() == 2 || itemBean.getType() == 3) {//选择题
            myViewHolder.area_score_answers.setVisibility(View.GONE);
            myViewHolder.area_pick_answers.setVisibility(View.VISIBLE);
            myViewHolder.area_content_answers.setVisibility(View.GONE);
            setAnswers(itemBean.getListAnswer(), myViewHolder.view_wordwrap, itemBean.getType());
        } else if (itemBean.getType() == 4) {
            myViewHolder.area_score_answers.setVisibility(View.GONE);
            myViewHolder.area_pick_answers.setVisibility(View.GONE);
            myViewHolder.area_content_answers.setVisibility(View.VISIBLE);
            myViewHolder.edit_answer_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    itemBean.setAnswerContent(s.toString());
                }
            });
        } else {
            myViewHolder.area_score_answers.setVisibility(View.GONE);
            myViewHolder.area_pick_answers.setVisibility(View.GONE);
            myViewHolder.area_content_answers.setVisibility(View.GONE);
        }
    }

    private void initItemChosenListener(MyViewHolder holder, TrainEvaQuestionBean itemBean) {
        setChosenState(holder, itemBean);
        holder.area_eva_answer_score4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemBean.getChosenAnswerIndex() == 4) {//当前选项选中
                    holder.img_eva_score4.setImageResource(R.mipmap.icon_eva_score4_normal);
                    itemBean.setChosenAnswerIndex(0);//设置0表示没有选项选中
                } else {
                    holder.img_eva_score4.setImageResource(R.mipmap.icon_eva_score4_chosen);
                    itemBean.setChosenAnswerIndex(4);
                }
                setChosenState(holder, itemBean);
            }
        });
        holder.area_eva_answer_score3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemBean.getChosenAnswerIndex() == 3) {//当前选项选中
                    holder.img_eva_score3.setImageResource(R.mipmap.icon_eva_score3_normal);
                    itemBean.setChosenAnswerIndex(0);//设置0表示没有选项选中
                } else {
                    holder.img_eva_score3.setImageResource(R.mipmap.icon_eva_score3_chosen);
                    itemBean.setChosenAnswerIndex(3);
                }
                setChosenState(holder, itemBean);
            }
        });
        holder.area_eva_answer_score2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemBean.getChosenAnswerIndex() == 2) {//当前选项选中
                    holder.img_eva_score2.setImageResource(R.mipmap.icon_eva_score2_normal);
                    itemBean.setChosenAnswerIndex(0);//设置0表示没有选项选中
                } else {
                    holder.img_eva_score2.setImageResource(R.mipmap.icon_eva_score2_chosen);
                    itemBean.setChosenAnswerIndex(2);
                }
                setChosenState(holder, itemBean);
            }
        });
        holder.area_eva_answer_score1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemBean.getChosenAnswerIndex() == 1) {//当前选项选中
                    holder.img_eva_score1.setImageResource(R.mipmap.icon_eva_score1_normal);
                    itemBean.setChosenAnswerIndex(0);//设置0表示没有选项选中
                } else {
                    holder.img_eva_score1.setImageResource(R.mipmap.icon_eva_score1_chosen);
                    itemBean.setChosenAnswerIndex(1);
                }
                setChosenState(holder, itemBean);
            }
        });
    }

    private void setChosenState(MyViewHolder holder, TrainEvaQuestionBean itemBean) {
        if (itemBean.getChosenAnswerIndex() == 4) {
            holder.img_eva_score4.setImageResource(R.mipmap.icon_eva_score4_chosen);
            holder.img_eva_score3.setImageResource(R.mipmap.icon_eva_score3_normal);
            holder.img_eva_score2.setImageResource(R.mipmap.icon_eva_score2_normal);
            holder.img_eva_score1.setImageResource(R.mipmap.icon_eva_score1_normal);
        } else if (itemBean.getChosenAnswerIndex() == 3) {
            holder.img_eva_score4.setImageResource(R.mipmap.icon_eva_score4_normal);
            holder.img_eva_score3.setImageResource(R.mipmap.icon_eva_score3_chosen);
            holder.img_eva_score2.setImageResource(R.mipmap.icon_eva_score2_normal);
            holder.img_eva_score1.setImageResource(R.mipmap.icon_eva_score1_normal);
        } else if (itemBean.getChosenAnswerIndex() == 2) {
            holder.img_eva_score4.setImageResource(R.mipmap.icon_eva_score4_normal);
            holder.img_eva_score3.setImageResource(R.mipmap.icon_eva_score3_normal);
            holder.img_eva_score2.setImageResource(R.mipmap.icon_eva_score2_chosen);
            holder.img_eva_score1.setImageResource(R.mipmap.icon_eva_score1_normal);
        } else if (itemBean.getChosenAnswerIndex() == 1) {
            holder.img_eva_score4.setImageResource(R.mipmap.icon_eva_score4_normal);
            holder.img_eva_score3.setImageResource(R.mipmap.icon_eva_score3_normal);
            holder.img_eva_score2.setImageResource(R.mipmap.icon_eva_score2_normal);
            holder.img_eva_score1.setImageResource(R.mipmap.icon_eva_score1_chosen);
        } else {
            holder.img_eva_score4.setImageResource(R.mipmap.icon_eva_score4_normal);
            holder.img_eva_score3.setImageResource(R.mipmap.icon_eva_score3_normal);
            holder.img_eva_score2.setImageResource(R.mipmap.icon_eva_score2_normal);
            holder.img_eva_score1.setImageResource(R.mipmap.icon_eva_score1_normal);
        }
    }

    /**
     * 设置选择题答案
     */
    private void setAnswers(List<TrainEvaAnswerBean> hotList, AutoLinefeedLayout view_wordwrap, int answerType) {
        view_wordwrap.removeAllViews();
        Log.out("=================hotList===================" + hotList.size());
        for (int i = 0; i < hotList.size(); i++) {
            LinearLayout ll = new LinearLayout(mContext);
            ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DevAttr.dip2px(mContext, 40)));
            ll.setGravity(Gravity.CENTER);

            TextView textview = new TextView(mContext);
            textview.setTag(i);
            String words = hotList.get(i).getName();
            boolean isChosen = hotList.get(i).isChosen();
            if (words.length() > 20) {
                words = words.substring(0, 20) + "...";
            }
            textview.setText(words);
            textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            if (!isChosen) {
                textview.setTextColor(mContext.getResources().getColor(R.color.g999999));
                textview.setBackgroundResource(R.drawable.rec_gray_stroke_trans_solid_corner30);
            } else {
                textview.setTextColor(mContext.getResources().getColor(R.color.com_orange));
                textview.setBackgroundResource(R.mipmap.icon_multi_chosen_bg);
            }
            textview.setPadding(DevAttr.dip2px(mContext, 10), DevAttr.dip2px(mContext, 5), DevAttr.dip2px(mContext, 10), DevAttr.dip2px(mContext, 5));
            ll.addView(textview);

            int finalI = i;
            textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (answerType == 2) {//题目类型:1:评分型题目;2:单选题;3:多选题;4:开放型题目
                        if (!hotList.get(finalI).isChosen()) {
                            hotList.get(finalI).setChosen(true);
                            unChosenOthers(finalI, hotList);
                        } else {
                            hotList.get(finalI).setChosen(false);
                        }
                    } else {//多选题
                        if (!hotList.get(finalI).isChosen()) {
                            hotList.get(finalI).setChosen(true);
                        } else {
                            hotList.get(finalI).setChosen(false);
                        }
                    }
                    setAnswers(hotList, view_wordwrap, answerType);
                }
            });

            View view = new View(mContext);
            LinearLayout.LayoutParams linearParams2 = new LinearLayout.LayoutParams(DevAttr.dip2px(mContext, 12), DevAttr.dip2px(mContext, 12));
            view.setLayoutParams(linearParams2);
            ll.addView(view);

            view_wordwrap.addView(ll);
        }
    }

    private void unChosenOthers(int index, List<TrainEvaAnswerBean> hotList) {
        for (int i = 0; i < hotList.size(); i++) {
            if (index != i) {
                hotList.get(i).setChosen(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_question_name;
        public View area_score_answers, area_pick_answers, area_content_answers,
                area_eva_answer_score4, area_eva_answer_score3, area_eva_answer_score2, area_eva_answer_score1;
        public AutoLinefeedLayout view_wordwrap;
        public ImageView img_eva_score4, img_eva_score3, img_eva_score2, img_eva_score1;
        public EditText edit_answer_input;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_question_name = itemView.findViewById(R.id.tv_question_name);
            area_score_answers = itemView.findViewById(R.id.area_score_answers);
            area_pick_answers = itemView.findViewById(R.id.area_pick_answers);
            area_content_answers = itemView.findViewById(R.id.area_content_answers);
            view_wordwrap = itemView.findViewById(R.id.view_wordwrap);
            area_eva_answer_score4 = itemView.findViewById(R.id.area_eva_answer_score4);
            area_eva_answer_score3 = itemView.findViewById(R.id.area_eva_answer_score3);
            area_eva_answer_score2 = itemView.findViewById(R.id.area_eva_answer_score2);
            area_eva_answer_score1 = itemView.findViewById(R.id.area_eva_answer_score1);
            img_eva_score4 = itemView.findViewById(R.id.img_eva_score4);
            img_eva_score3 = itemView.findViewById(R.id.img_eva_score3);
            img_eva_score2 = itemView.findViewById(R.id.img_eva_score2);
            img_eva_score1 = itemView.findViewById(R.id.img_eva_score1);
            edit_answer_input = itemView.findViewById(R.id.edit_answer_input);
        }
    }
}
