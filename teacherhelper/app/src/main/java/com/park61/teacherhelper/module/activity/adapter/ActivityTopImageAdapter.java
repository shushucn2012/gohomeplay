package com.park61.teacherhelper.module.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.activity.bean.BannerListDataBean;
import com.park61.teacherhelper.module.home.adapter.ContentRecomendAdapter;

import java.util.List;

/**
 * Created by nieyu on 2017/10/31.
 */

public class ActivityTopImageAdapter extends RecyclerView.Adapter<ActivityTopImageAdapter.MyViewHolder> implements View.OnClickListener {
    private ContentRecomendAdapter.OnItemClickListener mOnItemClickListener = null;
    private int currepage = -1;

    private List<BannerListDataBean> bannerListDataBeen;
    public Context ctx;

    public ActivityTopImageAdapter(Context ctx, List<BannerListDataBean> bannerListDataBeen) {
        this.ctx = ctx;
        this.bannerListDataBeen = bannerListDataBeen;
    }

    @Override
    public ActivityTopImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.adapter_activitytopimage, parent, false);
        ActivityTopImageAdapter.MyViewHolder holder = new ActivityTopImageAdapter.MyViewHolder(view);
        view.setOnClickListener(this);

        return holder;
    }

    @Override
    public void onBindViewHolder(ActivityTopImageAdapter.MyViewHolder holder, int position) {
        WindowManager manager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        int witd = display.getWidth();
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//       ll.width=witd/3;
        holder.iv_top.getLayoutParams().width = witd / 3;
        holder.iv_top.setLayoutParams(ll);
        if (bannerListDataBeen.get(position).getIndicatorAdLink() != null) {
            if (bannerListDataBeen.get(position).getIndicatorAdLink().getSelected() == 1 && !TextUtils.isEmpty(bannerListDataBeen.get(position).getIndicatorAdLink().getSelectedPicUrl())) {
                ImageManager.getInstance().displayImg(holder.iv_top, bannerListDataBeen.get(position).getIndicatorAdLink().getSelectedPicUrl());
            } else {
                ImageManager.getInstance().displayImg(holder.iv_top, bannerListDataBeen.get(position).getIndicatorAdLink().getLinkPic());
            }
            if (currepage == -1) {

            } else {
                if (currepage == position) {
                    ImageManager.getInstance().displayImg(holder.iv_top, bannerListDataBeen.get(position).getIndicatorAdLink().getSelectedPicUrl());
                } else {
                    ImageManager.getInstance().displayImg(holder.iv_top, bannerListDataBeen.get(position).getIndicatorAdLink().getLinkPic());
                }
            }
        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return bannerListDataBeen.size();
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(view, (int) view.getTag());
        }
//        view.setBackgroundColor(ctx.getResources().getColor(R.color.transbg));
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_top;

        public MyViewHolder(View view) {
            super(view);
            iv_top = (ImageView) view.findViewById(R.id.iv_top);

        }
    }

    public void setOnItemClickListener(ContentRecomendAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void currentselect(int pageselect) {
        currepage = pageselect;
    }


}
