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
import com.park61.teacherhelper.widget.gridview.GridViewForScrollView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PublishListAdapter extends BaseAdapter {

    private final int TYPE_0NE_PIC = 0;
    private final int TYPE_MORE_PIC = 1;

    private List<PublishItem> mList;
    private Context mContext;

    public PublishListAdapter(Context _context, List<PublishItem> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public PublishItem getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).getTeachIssuePhotoSourceList().size() <= 2) {
            return TYPE_0NE_PIC;
        } else {
            return TYPE_MORE_PIC;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_0NE_PIC:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.publish_list_item_one, null);
                    holder = new ViewHolder();
                    holder.img_user = (ImageView) convertView.findViewById(R.id.img_user);
                    holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
                    holder.tv_comt_title = (TextView) convertView.findViewById(R.id.tv_comt_title);
                    holder.tv_comt_content = (TextView) convertView.findViewById(R.id.tv_comt_content);
                    holder.tv_comt_time = (TextView) convertView.findViewById(R.id.tv_comt_time);
                    holder.tv_class_name = (TextView) convertView.findViewById(R.id.tv_class_name);
                    holder.img_photo = (ImageView) convertView.findViewById(R.id.img_photo);
                    holder.list_line = convertView.findViewById(R.id.list_line);
                    holder.tv_banner_pos_and_total = (TextView) convertView.findViewById(R.id.tv_banner_pos_and_total);
                    break;
                case TYPE_MORE_PIC:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.publish_list_item, null);
                    holder = new ViewHolder();
                    holder.img_user = (ImageView) convertView.findViewById(R.id.img_user);
                    holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
                    holder.tv_comt_title = (TextView) convertView.findViewById(R.id.tv_comt_title);
                    holder.tv_comt_content = (TextView) convertView.findViewById(R.id.tv_comt_content);
                    holder.tv_comt_time = (TextView) convertView.findViewById(R.id.tv_comt_time);
                    holder.tv_class_name = (TextView) convertView.findViewById(R.id.tv_class_name);
                    holder.gv_eva_pic = (GridViewForScrollView) convertView.findViewById(R.id.gv_eva_pic);
                    holder.list_line = convertView.findViewById(R.id.list_line);
                    holder.tv_banner_pos_and_total = (TextView) convertView.findViewById(R.id.tv_banner_pos_and_total);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final PublishItem publishItem = mList.get(position);
        ImageManager.getInstance().displayImg(holder.img_user, publishItem.getUserPictureUrl());
        holder.tv_username.setText(publishItem.getUserName());
        holder.tv_comt_title.setText(TextUtils.isEmpty(publishItem.getTitle()) ? "" : publishItem.getTitle());
        holder.tv_comt_content.setText(TextUtils.isEmpty(publishItem.getContent()) ? "" : publishItem.getContent());
        if (TextUtils.isEmpty(publishItem.getShowDate())) {
            holder.tv_comt_time.setText(publishItem.getCreateTime());
        } else {
            holder.tv_comt_time.setText(publishItem.getShowDate());
        }
        //holder.tv_comt_time.setText(DateTool.toPDateStr(new Date().getTime(), FragmentSectionDetails.StrToDate(publishItem.getCreateTime()).getTime()));
        holder.tv_class_name.setText(publishItem.getClassName());
        if (position == mList.size() - 1) {
            holder.list_line.setVisibility(View.GONE);
        } else {
            holder.list_line.setVisibility(View.VISIBLE);
        }

        switch (type) {
            case TYPE_0NE_PIC:
                ImageManager.getInstance().displayImg(holder.img_photo, publishItem.getTeachIssuePhotoSourceList().get(0).getImageUrl());
                holder.img_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent bIt = new Intent(mContext, ShowBigPicActivity.class);
                        bIt.putExtra("picUrl", publishItem.getTeachIssuePhotoSourceList().get(0).getImageUrl());
                        List<String> urlList = new ArrayList<>();
                        for (PublishItem.TeachIssuePhotoSourceListBean bean : publishItem.getTeachIssuePhotoSourceList()) {
                            urlList.add(bean.getImageUrl());
                        }
                        if (urlList.size() > 1)// url集合
                            bIt.putStringArrayListExtra("urlList", (ArrayList<String>) urlList);
                        mContext.startActivity(bIt);
                    }
                });
                if (publishItem.getTeachIssuePhotoSourceList().size() == 2) {
                    holder.tv_banner_pos_and_total.setVisibility(View.VISIBLE);
                    holder.tv_banner_pos_and_total.setText("共" + publishItem.getTeachIssuePhotoSourceList().size() + "张");
                } else {
                    holder.tv_banner_pos_and_total.setVisibility(View.GONE);
                }
                break;
            case TYPE_MORE_PIC:
                if (CommonMethod.isListEmpty(publishItem.getTeachIssuePhotoSourceList())) {
                    List<PublishItem.TeachIssuePhotoSourceListBean> urlList = new ArrayList<>();
                    holder.tv_banner_pos_and_total.setVisibility(View.GONE);
                    holder.gv_eva_pic.setAdapter(new GvEvaPicAdapter(urlList));
                } else {
                    if (publishItem.getTeachIssuePhotoSourceList().size() > 3) {
                        holder.tv_banner_pos_and_total.setVisibility(View.VISIBLE);
                        holder.tv_banner_pos_and_total.setText("共" + publishItem.getTeachIssuePhotoSourceList().size() + "张");
                        holder.gv_eva_pic.setAdapter(new GvEvaPicAdapter(publishItem.getTeachIssuePhotoSourceList().subList(0, 3)));
                    } else {
                        holder.tv_banner_pos_and_total.setVisibility(View.GONE);
                        holder.gv_eva_pic.setAdapter(new GvEvaPicAdapter(publishItem.getTeachIssuePhotoSourceList()));
                    }
                }
                holder.gv_eva_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final PublishItem.TeachIssuePhotoSourceListBean urlBean = publishItem.getTeachIssuePhotoSourceList().get(position);
                        Intent bIt = new Intent(mContext, ShowBigPicActivity.class);
                        bIt.putExtra("picUrl", urlBean.getImageUrl());
                        List<String> urlList = new ArrayList<>();
                        for (PublishItem.TeachIssuePhotoSourceListBean bean : publishItem.getTeachIssuePhotoSourceList()) {
                            urlList.add(bean.getImageUrl());
                        }
                        if (urlList.size() > 1)// url集合
                            bIt.putStringArrayListExtra("urlList", (ArrayList<String>) urlList);
                        mContext.startActivity(bIt);
                    }
                });
                break;
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_user;
        TextView tv_username;
        TextView tv_comt_title;
        TextView tv_comt_time, tv_class_name, tv_comt_content;
        GridViewForScrollView gv_eva_pic;
        View list_line;
        TextView tv_banner_pos_and_total;
        ImageView img_photo;
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
