package com.park61.teacherhelper.module.activity.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.tool.DateTool;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.UIUtil;
import com.park61.teacherhelper.module.activity.bean.ActivityListBean;
import com.park61.teacherhelper.module.home.adapter.ContentCommentListAdapter;
import com.park61.teacherhelper.widget.textview.EasyCountDownTextureView;

import java.util.List;

/**
 * Created by nieyu on 2017/11/2.
 */

public class Adapteractivitylist extends BaseAdapter {
    private Context ctx;
    private  List<ActivityListBean> listBeen;
    private String backcolor;
    public Adapteractivitylist(Context ctx, List<ActivityListBean> listBeen,String backcolor){
     this.ctx=ctx;
    this.listBeen=listBeen;
        this.backcolor=backcolor;
    }
    @Override
    public int getCount() {
        if(listBeen!=null){
            return listBeen.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    Adapteractivitylist.ViewHolder holder;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(
                    R.layout.adapter_activitylist, null, false);
            holder = new Adapteractivitylist.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Adapteractivitylist.ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(listBeen.get(i).getCoverImg())) {
            ImageManager.getInstance().displayImg(holder.iv_content, listBeen.get(i).getCoverImg());
        }
        holder.tv_cityname.setText(listBeen.get(i).getAddress());
        holder.tv_price.setText(listBeen.get(i).getPrice()+"");
        holder.tv_activityname.setText(listBeen.get(i).getName());

        RelativeLayout.LayoutParams ll=new RelativeLayout.LayoutParams(UIUtil.dp(ctx,22),UIUtil.dp(ctx,200));
        if(null!=listBeen.get(i).getApplyPer()){
            ll.width= (int) (UIUtil.dp(ctx,200)*100*listBeen.get(i).getApplyPer()*100/10000);
        }
        holder.ll_progress.setLayoutParams(ll);
if(listBeen.get(i).getBtnStatus()==2){
//    活动状态:0:未开始;1:报名中2:完成3:已抢完
    ll.width=UIUtil.dp(ctx,200);
    holder.tv_progresstext.setText("已结束");
    holder.iv_shopstate.setBackground(ctx.getResources().getDrawable(R.mipmap.overshop));
    holder.iv_activitystate.setBackground(ctx.getResources().getDrawable(R.mipmap.activity_timeover));
    holder.tv_activityover.setVisibility(View.VISIBLE);
    holder.setting_countdown_text.setVisibility(View.GONE);
}else if(3==listBeen.get(i).getBtnStatus()){
    holder.iv_shopstate.setBackground(ctx.getResources().getDrawable(R.mipmap.overpickshop));
    holder.tv_progresstext.setText("已抢光");
    holder.iv_activitystate.setBackground(ctx.getResources().getDrawable(R.mipmap.timered));
    holder.tv_activityover.setVisibility(View.GONE);
    holder.setting_countdown_text.setVisibility(View.VISIBLE);
    long timelimit= listBeen.get(i).getLeftSecond();
    long datearry[]= DateTool.formatDuring(timelimit*1000);
    holder.setting_countdown_text.setTimeDay(datearry[0]);
    holder.setting_countdown_text.setTimeHour(datearry[1]);
    holder.setting_countdown_text.setTimeMinute(datearry[2]);
    holder.setting_countdown_text.setTimeSecond(datearry[3]);

}else if(0==listBeen.get(i).getBtnStatus()){
    holder.iv_shopstate.setBackground(ctx.getResources().getDrawable(R.mipmap.waitshop));
//    holder.tv_progresstext.setTextColor(ctx.getColor(R.color.gray_red));
    holder.tv_progresstext.setText("仅限"+listBeen.get(i).getMaxApplyNum()+"人");
    holder.iv_activitystate.setBackground(ctx.getResources().getDrawable(R.mipmap.timered));
    holder.tv_activityover.setVisibility(View.VISIBLE);
   holder.tv_activityover.setText(listBeen.get(i).getApplyStartShowTime()+"开抢");
    holder.tv_activityover.setTextColor(ctx.getResources().getColor(R.color.white));
    holder.setting_countdown_text.setVisibility(View.GONE);


}else {
    holder.iv_shopstate.setBackground(ctx.getResources().getDrawable(R.mipmap.goshop));
    if(listBeen.get(i).getApplyPer()!=null){
        holder.tv_progresstext.setText(listBeen.get(i).getApplyPer()*100+"%");
    }

//    holder.tv_progresstext.setTextColor(ctx.getResources().getColor(R.color.com_red));
    holder.iv_activitystate.setBackground(ctx.getResources().getDrawable(R.mipmap.timered));
    holder.tv_activityover.setVisibility(View.GONE);
    holder.setting_countdown_text.setVisibility(View.VISIBLE);
   long timelimit= listBeen.get(i).getLeftSecond();
   long datearry[]= DateTool.formatDuring(timelimit*1000);
    holder.setting_countdown_text.setTimeDay(datearry[0]);
    holder.setting_countdown_text.setTimeHour(datearry[1]);
    holder.setting_countdown_text.setTimeMinute(datearry[2]);
    holder.setting_countdown_text.setTimeSecond(datearry[3]);
}
holder.v_blank.setBackgroundColor(Color.parseColor("#"+backcolor));
        if(listBeen.get(i).getLimit()){
            holder.rr_progress.setVisibility(View.VISIBLE);
            holder.tv_progresstext.setVisibility(View.VISIBLE);
            holder.ll_progress.setVisibility(View.VISIBLE);
        }else {
            holder.rr_progress.setVisibility(View.GONE);
            holder.tv_progresstext.setVisibility(View.GONE);
            holder.ll_progress.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        RelativeLayout rr_progress;
        ImageView iv_content;
        TextView tv_cityname;
        TextView tv_price;
        TextView ttv_time;
        TextView tv_activityname;
        TextView tv_progresstext;
        LinearLayout ll_progress;
        ImageView iv_shopstate;
        EasyCountDownTextureView setting_countdown_text;
        TextView tv_activityover;
        ImageView iv_activitystate;
        View v_blank;
        public ViewHolder(View view) {
            iv_content = (ImageView) view.findViewById(R.id.iv_content);
            tv_cityname=(TextView) view.findViewById(R.id.tv_cityname);
            tv_price=(TextView) view.findViewById(R.id.tv_price);
            ttv_time=(TextView)view.findViewById(R.id.ttv_time);
            tv_activityname=(TextView)view.findViewById(R.id.tv_activityname);
            tv_progresstext=(TextView)view.findViewById(R.id.tv_progresstext);
            ll_progress=(LinearLayout)view.findViewById(R.id.ll_progress);
            iv_shopstate=(ImageView)view.findViewById(R.id.iv_shopstate);
            setting_countdown_text=(EasyCountDownTextureView)view.findViewById(R.id.setting_countdown_text);
            tv_activityover=(TextView)view.findViewById(R.id.tv_activityover);
            iv_activitystate=(ImageView)view.findViewById(R.id.iv_activitystate);
            v_blank=(View)view.findViewById(R.id.v_blank);
            rr_progress=(RelativeLayout)view.findViewById(R.id.rr_progress);
        }
    }

}
