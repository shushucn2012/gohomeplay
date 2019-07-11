package com.park61.teacherhelper.module.activity.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.activity.bean.ActivityCityListBean;
import com.park61.teacherhelper.module.home.adapter.ContentRecomendAdapter;
import com.park61.teacherhelper.widget.textview.CirButton;

import java.util.List;

/**
 * Created by nieyu on 2017/11/2.
 */

public class ActivityCityAdapter extends RecyclerView.Adapter<ActivityCityAdapter.MyViewHolder> implements View.OnClickListener {
    private ActivityCityAdapter.OnItemClickListener mOnItemClickListener = null;
    private Context ctx;
    private List<ActivityCityListBean> cityListBeen;
    private     ActivityCityAdapter.MyViewHolder holder;
    int selectPosetion=-1;
    public ActivityCityAdapter(Context ctx, List<ActivityCityListBean> cityListBeen){
        this.ctx=ctx;
        this.cityListBeen=cityListBeen;
    }
    @Override
    public ActivityCityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(ctx).inflate(R.layout.adapter_activitycity, parent, false);
      holder = new ActivityCityAdapter.MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ActivityCityAdapter.MyViewHolder holder, int position) {
        holder.tv_city.setText(cityListBeen.get(position).getName());
        if(-1==selectPosetion){
            if(cityListBeen.get(position).getHasActive()!=null){
                if(cityListBeen.get(position).getHasActive()){
                    holder.tv_city.setTextColor(Color.parseColor("#"+cityListBeen.get(position).getFontColorActive()));
                    holder.tv_city.setColor(cityListBeen.get(position).getBackgroundColorActive());
                }else {
                    holder.tv_city.setTextColor(Color.parseColor("#"+cityListBeen.get(position).getFontColor()));
                    holder.tv_city.setColor(cityListBeen.get(position).getBackgroundColor());
                }
            }
        }else {
            if(selectPosetion==position){
                if(cityListBeen.get(position).getHasActive()!=null){
//                    if(cityListBeen.get(position).getHasActive()){
                        holder.tv_city.setTextColor(Color.parseColor("#"+cityListBeen.get(position).getFontColorActive()));
                        holder.tv_city.setColor(cityListBeen.get(position).getBackgroundColorActive());
//                    }else {
//                        holder.tv_city.setTextColor(Color.parseColor("#"+cityListBeen.get(position).getFontColor()));
//                        holder.tv_city.setColor(cityListBeen.get(position).getBackgroundColor());
//                    }
                }
            }else {
                    holder.tv_city.setTextColor(Color.parseColor("#"+cityListBeen.get(position).getFontColor()));
                    holder.tv_city.setColor(cityListBeen.get(position).getBackgroundColor());
            }


        }

        holder.itemView.setTag(position);
    }
    @Override
    public int getItemCount() {
        return cityListBeen.size();
    }

    @Override
    public void onClick(View view) {
        if(mOnItemClickListener !=null){
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemCityClick(view,(int) view.getTag());
        }
        this.selectPosetion=(int)view.getTag();
//        view.setBackgroundColor(Color.parseColor("#"+cityListBeen.get(selectPosetion).getBackgroundColorActive()));

    }
    class MyViewHolder extends RecyclerView.ViewHolder
    {

        CirButton tv_city;
        public MyViewHolder(View view)
        {
            super(view);
            tv_city = (CirButton) view.findViewById(R.id.tv_city);
        }
    }
    public void setOnItemClickListener(ActivityCityAdapter.OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    public static interface OnItemClickListener{
        void onItemCityClick(View view,int position);
    }
}
