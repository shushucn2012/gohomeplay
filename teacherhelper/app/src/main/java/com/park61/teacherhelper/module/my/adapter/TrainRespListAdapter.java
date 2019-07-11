package com.park61.teacherhelper.module.my.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.park61.teacherhelper.CanBackWebViewActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.my.TrainRespDetailWvActivity;
import com.park61.teacherhelper.module.my.bean.TrainRespBean;

import java.util.List;

public class TrainRespListAdapter extends BaseAdapter {

    private List<TrainRespBean> mList;
    private Context mContext;
    private OnFocusClickedLsner mOnFocusClickedLsner;

    public TrainRespListAdapter(Context _context, List<TrainRespBean> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public TrainRespBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.train_resp_item, null);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_submit_num = (TextView) convertView.findViewById(R.id.tv_submit_num);
            holder.tv_share = (TextView) convertView.findViewById(R.id.tv_share);
            holder.space_bottom = convertView.findViewById(R.id.space_bottom);
            holder.space_top = convertView.findViewById(R.id.space_top);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final TrainRespBean trainRespBean = mList.get(position);
        holder.tv_title.setText(trainRespBean.getName());
        String timeWhole = trainRespBean.getStartTime().substring(0, trainRespBean.getStartTime().length() - 3).replace("-", ".")
                + " - "
                + trainRespBean.getEndTime().substring(5, trainRespBean.getEndTime().length() - 3).replace("-", ".");
        holder.tv_time.setText("培训时间：" + trainRespBean.getStartEndTime());
        holder.tv_submit_num.setText(trainRespBean.getServiceEvaluateNum() + "");
        if (position == 0) {
            holder.space_top.setVisibility(View.VISIBLE);
        } else {
            holder.space_top.setVisibility(View.GONE);
        }
        holder.tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnFocusClickedLsner.onFocus(position);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareUrl = "http://m.61park.cn/teach/#/feedback/questionnaire/" + trainRespBean.getId();
                Intent it = new Intent(mContext, TrainRespDetailWvActivity.class);
                it.putExtra("PAGE_TITLE", trainRespBean.getName());
                it.putExtra("url", shareUrl);
                it.putExtra("desc", trainRespBean.getStartEndTime());
                mContext.startActivity(it);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_title;
        TextView tv_time;
        TextView tv_submit_num;
        View space_bottom, space_top;
        TextView tv_share;
    }

    public interface OnFocusClickedLsner {
        void onFocus(int position);
    }

    public void setOnFocusClickedLsner(OnFocusClickedLsner lsner) {
        this.mOnFocusClickedLsner = lsner;
    }

}
