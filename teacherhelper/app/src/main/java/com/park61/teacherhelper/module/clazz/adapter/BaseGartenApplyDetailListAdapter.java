package com.park61.teacherhelper.module.clazz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.clazz.bean.ApplyGartenDetail;
import com.park61.teacherhelper.widget.gridview.GridViewForScrollView;

import java.util.ArrayList;
import java.util.List;

public class BaseGartenApplyDetailListAdapter extends BaseAdapter {

    private final int TYPE_0NE_PIC = 0;
    private final int TYPE_MORE_PIC = 1;

    private List<ApplyGartenDetail.ItemListBean> mList;
    private Context mContext;

    public BaseGartenApplyDetailListAdapter(Context _context, List<ApplyGartenDetail.ItemListBean> _list) {
        this.mList = _list;
        this.mContext = _context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ApplyGartenDetail.ItemListBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).getPicList().size() == 1) {
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
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.basegarten_applydetail_listitem_one, null);
                    holder = new ViewHolder();
                    holder.tv_zzname = (TextView) convertView.findViewById(R.id.tv_zzname);
                    holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
                    holder.img_photo = (ImageView) convertView.findViewById(R.id.img_photo);
                    holder.list_line = convertView.findViewById(R.id.list_line);
                    break;
                case TYPE_MORE_PIC:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.basegarten_applydetail_listitem_more, null);
                    holder = new ViewHolder();
                    holder.tv_zzname = (TextView) convertView.findViewById(R.id.tv_zzname);
                    holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
                    holder.gv_eva_pic = (GridViewForScrollView) convertView.findViewById(R.id.gv_eva_pic);
                    holder.gv_eva_pic.setFocusable(false);
                    holder.list_line = convertView.findViewById(R.id.list_line);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ApplyGartenDetail.ItemListBean publishItem = mList.get(position);
        holder.tv_zzname.setText(publishItem.getClassifyName());
        holder.tv_desc.setText(publishItem.getDetail());
        if (position == mList.size() - 1) {
            holder.list_line.setVisibility(View.GONE);
        } else {
            holder.list_line.setVisibility(View.VISIBLE);
        }
        switch (type) {
            case TYPE_0NE_PIC:
                ImageManager.getInstance().displayImg(holder.img_photo, publishItem.getPicList().get(0));
                break;
            case TYPE_MORE_PIC:
                if (CommonMethod.isListEmpty(publishItem.getPicList())) {
                    List<String> urlList = new ArrayList<>();
                    holder.gv_eva_pic.setAdapter(new GvEvaPicAdapter(urlList));
                } else {
                    holder.gv_eva_pic.setAdapter(new GvEvaPicAdapter(publishItem.getPicList()));
                }
                break;
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_zzname;
        TextView tv_desc;
        GridViewForScrollView gv_eva_pic;
        View list_line;
        ImageView img_photo;
    }

    private class GvEvaPicAdapter extends BaseAdapter {

        private List<String> listPictrue;

        public GvEvaPicAdapter(List<String> list) {
            this.listPictrue = list;
        }

        @Override
        public int getCount() {
            return listPictrue.size();
        }

        @Override
        public String getItem(int position) {
            return listPictrue.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_course_eva_show_item2, null);
            ImageView img_input = (ImageView) convertView.findViewById(R.id.img_input);
            ImageManager.getInstance().displayImg(img_input, listPictrue.get(position));
            return convertView;
        }
    }

}
