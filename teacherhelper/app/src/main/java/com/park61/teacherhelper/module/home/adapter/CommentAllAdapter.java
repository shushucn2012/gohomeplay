package com.park61.teacherhelper.module.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.home.bean.CommentItemBean;
import com.park61.teacherhelper.common.tool.ImageManager;

import java.util.List;

/**
 * Created by nieyu on 2017/10/18.
 */

public class CommentAllAdapter extends BaseAdapter {

    private List<CommentItemBean> mList;
    private Context mContext;
    private LayoutInflater factory;
    private OnReplyClickedLsner mOnReplyClickedLsner;

    public CommentAllAdapter(Context _context, List<CommentItemBean> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentAllAdapter.ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adpteitem_commentlist, null, false);
            holder = new CommentAllAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
//            convertView = factory.inflate(R.layout.adpteitem_commentlist, null);
//            holder = new CommentAllAdapter.ViewHolder();
//            holder.item_area = convertView.findViewById(R.id.item_area);
//            holder.area_commt_content = convertView.findViewById(R.id.area_commt_content);
//            holder.img = (CircleImageView) convertView.findViewById(R.id.img);
//            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
//            holder.tv_evaluate_date = (TextView) convertView.findViewById(R.id.tv_evaluate_date);
//            holder.tv_replay = (TextView) convertView.findViewById(R.id.tv_replay);
//            holder.tv_comt_content = (TextView) convertView.findViewById(R.id.tv_comt_content);
//            holder.img_face = (ImageView) convertView.findViewById(R.id.img_face);
            convertView.setTag(holder);
        } else {
            holder = (CommentAllAdapter.ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(mList.get(position).getUserUrl())) {
            ImageManager.getInstance().displayImg(holder.iv_icon, mList.get(position).getUserUrl());
        }
        holder.tv_name.setText(mList.get(position).getUserName());
        holder.tvContent.setText(mList.get(position).getContent());
        holder.tv_time.setText(mList.get(position).getCreateTime() + "");
//        final CommentItemBean item = mList.get(position);
//        ImageManager.getInstance().displayImg(holder.img, item.getPictureUrl(), R.mipmap.headimg_default_img);
//        holder.tv_name.setText(item.getPetName());
//        holder.tv_evaluate_date.setText(item.getCreateTime());
//        holder.tv_comt_content.setText(item.getContent());
       /* if (!TextUtils.isEmpty(item.getParentUserName())) {
            holder.tv_replay.setVisibility(View.VISIBLE);
            holder.tv_replay.setText("回复:" + item.getParentUserName());
        } else {
            holder.tv_replay.setVisibility(View.GONE);
        }*/


//        if (item.getScore() == 1) {
//            holder.img_face.setImageResource(R.mipmap.icon_face1);
//        } else if (item.getScore() == 2) {
//            holder.img_face.setImageResource(R.mipmap.icon_face2);
//        } else if (item.getScore() == 3) {
//            holder.img_face.setImageResource(R.mipmap.icon_face3);
//        } else if (item.getScore() == 4) {
//            holder.img_face.setImageResource(R.mipmap.icon_face4);
//        } else if (item.getScore() == 5) {
//            holder.img_face.setImageResource(R.mipmap.icon_face5);
//        }
//        holder.area_commt_content.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mOnReplyClickedLsner != null) {
//                    mOnReplyClickedLsner.onComtClicked(item.getId());
//                }
//            }
//        });
        return convertView;
    }

    //    class ViewHolder {
//        CircleImageView img;
//        TextView tv_name, tv_evaluate_date, tv_replay, tv_comt_content;
//        View item_area, area_commt_content;
//        ImageView img_face;
//    }
    class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_time;
        TextView tvContent;

        public ViewHolder(View view) {
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tvContent = (TextView) view.findViewById(R.id.tvContent);
        }
    }

    public interface OnReplyClickedLsner {
        void onComtClicked(View view, int position);
    }

    public void setOnReplyClickedLsner(OnReplyClickedLsner lsner) {
        this.mOnReplyClickedLsner = lsner;
    }
}
