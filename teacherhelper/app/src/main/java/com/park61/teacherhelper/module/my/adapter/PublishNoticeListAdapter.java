package com.park61.teacherhelper.module.my.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.my.ShowBigPicActivity;
import com.park61.teacherhelper.module.my.bean.PublishItem;
import com.park61.teacherhelper.module.my.bean.PublishNoticeItem;
import com.park61.teacherhelper.widget.gridview.GridViewForScrollView;

import java.util.ArrayList;
import java.util.List;

public class PublishNoticeListAdapter extends BaseAdapter {

    private List<PublishNoticeItem> mList;
    private Context mContext;

    public PublishNoticeListAdapter(Context _context, List<PublishNoticeItem> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public PublishNoticeItem getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.publish_list_item, null);
            holder = new ViewHolder();
            holder.img_user = (ImageView) convertView.findViewById(R.id.img_user);
            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.tv_comt_content = (TextView) convertView.findViewById(R.id.tv_comt_content);
            holder.tv_comt_content.setVisibility(View.VISIBLE);
            holder.tv_comt_title = (TextView) convertView.findViewById(R.id.tv_comt_title);
            holder.tv_comt_time = (TextView) convertView.findViewById(R.id.tv_comt_time);
            holder.tv_class_name = (TextView) convertView.findViewById(R.id.tv_class_name);
            holder.gv_eva_pic = (GridViewForScrollView) convertView.findViewById(R.id.gv_eva_pic);
            holder.list_line = convertView.findViewById(R.id.list_line);
            holder.lv_eva_imgs = convertView.findViewById(R.id.lv_eva_imgs);
            holder.lv_eva_imgs.setVisibility(View.GONE);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final PublishNoticeItem publishItem = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_user, publishItem.getUserUrl());
        holder.tv_username.setText(publishItem.getUserName());
        holder.tv_comt_title.setText(TextUtils.isEmpty(publishItem.getTitle()) ? "" : publishItem.getTitle());
        holder.tv_comt_content.setText(TextUtils.isEmpty(publishItem.getContent()) ? "" : publishItem.getContent());
        if (TextUtils.isEmpty(publishItem.getShowDate())) {
            holder.tv_comt_time.setText(publishItem.getCreateDate());
        } else {
            holder.tv_comt_time.setText(publishItem.getShowDate());
        }
        /*if (CommonMethod.isListEmpty(publishItem.getTeachIssuePhotoSourceList())) {
            List<PublishItem.TeachIssuePhotoSourceListBean> urlList = new ArrayList<>();
            holder.gv_eva_pic.setAdapter(new GvEvaPicAdapter(urlList));
        } else {
            holder.gv_eva_pic.setAdapter(new GvEvaPicAdapter(publishItem.getTeachIssuePhotoSourceList()));
        }*/
        holder.gv_eva_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*final PublishItem.TeachIssuePhotoSourceListBean urlBean = publishItem.getTeachIssuePhotoSourceList().get(position);
                Intent bIt = new Intent(mContext, ShowBigPicActivity.class);
                bIt.putExtra("picUrl", urlBean.getImageUrl());
                List<String> urlList = new ArrayList<>();
                for (PublishItem.TeachIssuePhotoSourceListBean bean : publishItem.getTeachIssuePhotoSourceList()) {
                    urlList.add(bean.getImageUrl());
                }
                if (urlList.size() > 1)// url集合
                    bIt.putStringArrayListExtra("urlList", (ArrayList<String>) urlList);
                mContext.startActivity(bIt);*/
            }
        });
        holder.tv_class_name.setText(publishItem.getClassName());
        if (position == mList.size() - 1) {
            holder.list_line.setVisibility(View.GONE);
        } else {
            holder.list_line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_user;
        TextView tv_username;
        TextView tv_comt_content, tv_comt_title;
        TextView tv_comt_time, tv_class_name;
        GridViewForScrollView gv_eva_pic;
        View list_line, lv_eva_imgs;
    }

    private class GvEvaPicAdapter extends BaseAdapter {

        private List<PublishItem.TeachIssuePhotoSourceListBean> listPictrue;

        public GvEvaPicAdapter(List<PublishItem.TeachIssuePhotoSourceListBean> list) {
            this.listPictrue = list;
        }

        @Override
        public int getCount() {
            return listPictrue.size();
        }

        @Override
        public PublishItem.TeachIssuePhotoSourceListBean getItem(int position) {
            return listPictrue.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_course_eva_show_item, null);
            ImageView img_input = (ImageView) convertView.findViewById(R.id.img_input);
            ImageManager.getInstance().displayImg(img_input, listPictrue.get(position).getImageUrl());
            return convertView;
        }
    }
}
