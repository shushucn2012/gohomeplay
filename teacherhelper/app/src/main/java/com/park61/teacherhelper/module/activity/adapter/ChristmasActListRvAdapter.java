package com.park61.teacherhelper.module.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.activity.bean.ChristmasActItem;
import com.park61.teacherhelper.module.home.CourseDetailsActivity;

import java.util.List;

/**
 * Created by shubei on 2017/12/13.
 */

public class ChristmasActListRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TOP_ITEM = 0;
    private static final int OTHER_ITEM = 1;
    private List<ChristmasActItem> mList;
    private Context mContext;
    private LayoutInflater inflater;

    public ChristmasActListRvAdapter(Context context, List<ChristmasActItem> list) {
        this.mContext = context;
        this.mList = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TOP_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.christmasactlist_item_top, parent, false);
            return new MyViewHolder(view);
        } else if (viewType == OTHER_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.christmasactlist_item, parent, false);
            return new MyViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        ChristmasActItem itemEntity = mList.get(position);
        ImageManager.getInstance().displayImg(myViewHolder.img_act, itemEntity.getCoverImg());
        ImageManager.getInstance().displayImg(myViewHolder.img_teacher, itemEntity.getAuthorPic(), R.mipmap.headimg_default_img);
        myViewHolder.tv_title.setText(itemEntity.getTitle());
        myViewHolder.tv_intro.setText(itemEntity.getIntro());
        if (!TextUtils.isEmpty(itemEntity.getKeyWords())) {
            String kwWhole = "";
            String[] keywordsArr = itemEntity.getKeyWords().split(",");
            for (int i = 0; i < keywordsArr.length; i++) {
                kwWhole += "#" + keywordsArr[i] + "   ";
            }
            myViewHolder.tv_keywords.setText(kwWhole);
            myViewHolder.tv_keywords.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.tv_keywords.setVisibility(View.GONE);
        }
        myViewHolder.tv_teacher_name.setText(itemEntity.getAuthorName());
        myViewHolder.tv_read_num.setText(itemEntity.getPraiseTotal());
        myViewHolder.cardview_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, CourseDetailsActivity.class);
                it.putExtra("coursePlanId", mList.get(position).getId());
                mContext.startActivity(it);
            }
        });
        if (!TextUtils.isEmpty(itemEntity.getNewTag())) {
            Bitmap b = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_new_tag);
            ImageSpan imgSpan = new ImageSpan(mContext, b);
            SpannableString spanString = new SpannableString("icon");
            spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            myViewHolder.tv_title.setText(spanString);
            myViewHolder.tv_title.append(" " + itemEntity.getTitle());
        } else {
            myViewHolder.tv_title.setText(itemEntity.getTitle());
        }
        if (itemEntity.getContentType() == 1) {//视频
            myViewHolder.img_can_play.setVisibility(View.VISIBLE);
        } else {//图文，音频
            myViewHolder.img_can_play.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public int getItemViewType(int position) {
        if (position == 0 || position == 1) {
            return TOP_ITEM;
        }
        return OTHER_ITEM;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_act, img_teacher, img_can_play;
        private TextView tv_title, tv_intro, tv_keywords, tv_teacher_name, tv_read_num;
        private View cardview_root;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_act = (ImageView) itemView.findViewById(R.id.img_act);
            img_teacher = (ImageView) itemView.findViewById(R.id.img_teacher);
            img_can_play = (ImageView) itemView.findViewById(R.id.img_can_play);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_intro = (TextView) itemView.findViewById(R.id.tv_intro);
            tv_keywords = (TextView) itemView.findViewById(R.id.tv_keywords);
            tv_teacher_name = (TextView) itemView.findViewById(R.id.tv_teacher_name);
            tv_read_num = (TextView) itemView.findViewById(R.id.tv_read_num);
            cardview_root = itemView.findViewById(R.id.cardview_root);
        }
    }
}
