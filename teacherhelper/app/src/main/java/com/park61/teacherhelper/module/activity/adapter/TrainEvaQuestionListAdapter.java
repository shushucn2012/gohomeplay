package com.park61.teacherhelper.module.activity.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.module.activity.bean.TrainEvaAnswerBean;
import com.park61.teacherhelper.module.activity.bean.TrainEvaQuestionBean;
import com.park61.teacherhelper.widget.AutoLinefeedLayout;

import java.util.List;

public class TrainEvaQuestionListAdapter extends BaseAdapter {

    private List<TrainEvaQuestionBean> mList;
    private Context mContext;
    private LayoutInflater factory;

    public TrainEvaQuestionListAdapter(Context _context, List<TrainEvaQuestionBean> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public TrainEvaQuestionBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.out("=================getView===================" + position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = factory.inflate(R.layout.item_tran_eva_questions, null);
            holder = new ViewHolder();
            holder.tv_question_name = convertView.findViewById(R.id.tv_question_name);
            holder.area_score_answers = convertView.findViewById(R.id.area_score_answers);
            holder.area_pick_answers = convertView.findViewById(R.id.area_pick_answers);
            holder.area_content_answers = convertView.findViewById(R.id.area_content_answers);
            holder.view_wordwrap = convertView.findViewById(R.id.view_wordwrap);
            holder.area_eva_answer_score4 = convertView.findViewById(R.id.area_eva_answer_score4);
            holder.area_eva_answer_score3 = convertView.findViewById(R.id.area_eva_answer_score3);
            holder.area_eva_answer_score2 = convertView.findViewById(R.id.area_eva_answer_score2);
            holder.area_eva_answer_score1 = convertView.findViewById(R.id.area_eva_answer_score1);
            holder.img_eva_score4 = convertView.findViewById(R.id.img_eva_score4);
            holder.img_eva_score3 = convertView.findViewById(R.id.img_eva_score3);
            holder.img_eva_score2 = convertView.findViewById(R.id.img_eva_score2);
            holder.img_eva_score1 = convertView.findViewById(R.id.img_eva_score1);
            holder.edit_answer_input = convertView.findViewById(R.id.edit_answer_input);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TrainEvaQuestionBean itemBean = mList.get(position);
        holder.tv_question_name.setText(itemBean.getSort() + "、" + itemBean.getQuestionName());
        if (itemBean.getType() == 4 && position == mList.size() - 1) {
            holder.tv_question_name.setVisibility(View.GONE);
        } else {
            holder.tv_question_name.setVisibility(View.VISIBLE);
        }
        if (itemBean.getType() == 1) {//评分题
            holder.area_score_answers.setVisibility(View.VISIBLE);
            holder.area_pick_answers.setVisibility(View.GONE);
            holder.area_content_answers.setVisibility(View.GONE);
            initItemChosenListener(holder, itemBean);
        } else if (itemBean.getType() == 2 || itemBean.getType() == 3) {//选择题
            holder.area_score_answers.setVisibility(View.GONE);
            holder.area_pick_answers.setVisibility(View.VISIBLE);
            holder.area_content_answers.setVisibility(View.GONE);
            setAnswers(itemBean.getListAnswer(), holder.view_wordwrap, itemBean.getType());
        } else if (itemBean.getType() == 4) {
            holder.area_score_answers.setVisibility(View.GONE);
            holder.area_pick_answers.setVisibility(View.GONE);
            holder.area_content_answers.setVisibility(View.VISIBLE);
            holder.edit_answer_input.addTextChangedListener(new TextWatcher() {
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
            holder.area_score_answers.setVisibility(View.GONE);
            holder.area_pick_answers.setVisibility(View.GONE);
            holder.area_content_answers.setVisibility(View.GONE);
        }

        return convertView;
    }

    private void initItemChosenListener(ViewHolder holder, TrainEvaQuestionBean itemBean) {
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

    private void setChosenState(ViewHolder holder, TrainEvaQuestionBean itemBean) {
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

    class ViewHolder {
        TextView tv_question_name;
        View area_score_answers, area_pick_answers, area_content_answers,
                area_eva_answer_score4, area_eva_answer_score3, area_eva_answer_score2, area_eva_answer_score1;
        AutoLinefeedLayout view_wordwrap;
        ImageView img_eva_score4, img_eva_score3, img_eva_score2, img_eva_score1;
        EditText edit_answer_input;
    }

    /**
     * 设置选择题答案
     *
     * @param hotList
     * @param view_wordwrap
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

}
