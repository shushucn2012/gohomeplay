package com.park61.teacherhelper.module.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.home.bean.FileItemSource;
import com.park61.teacherhelper.module.okdownload.PlayPopWin;
import com.park61.teacherhelper.module.okdownload.fragment.DownloadedVAdioFragment;

import java.util.List;


/**
 * 视频详情页-附件列表adapter
 * creatby super on2018/2/1
 */
public class FileSourcesAdapter extends BaseAdapter {

    private List<FileItemSource> mList;
    private Activity mContext;
    private OnDownLoadClickedLsner mOnDownLoadClickedLsner;
    private long lastClickTime = 0;

    public FileSourcesAdapter(Activity context, List<FileItemSource> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.file_source_listitem, null);
            holder = new ViewHolder();
            holder.img_file_cover = (ImageView) convertView.findViewById(R.id.img_file_cover);
            holder.tv_source_title = (TextView) convertView.findViewById(R.id.tv_source_title);
            holder.tv_source_format = (TextView) convertView.findViewById(R.id.tv_source_format);
            holder.tv_download_times = (TextView) convertView.findViewById(R.id.tv_download_times);
            holder.img_download_state = (ImageView) convertView.findViewById(R.id.img_download_state);
            holder.linear_bottom = convertView.findViewById(R.id.linear_bottom);
            holder.area_download_state = convertView.findViewById(R.id.area_download_state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FileItemSource itemSource = mList.get(position);
        holder.tv_source_title.setText(itemSource.getSourceName());
        holder.tv_source_format.setText(itemSource.getFileFormat());
        holder.tv_download_times.setText(itemSource.getTotalDownloadNum() + "次下载");
        ImageManager.getInstance().displayImg(holder.img_file_cover, itemSource.getIconUrl());

        if (itemSource.getStatus() == -1) {//未下载
            holder.img_download_state.setImageResource(R.mipmap.icon_file_down);
        } else if (itemSource.getStatus() == 0) {//下载中
            holder.img_download_state.setImageResource(R.mipmap.icon_attach_downloading);
        } else if (itemSource.getStatus() == 1) {//已完成
            holder.img_download_state.setImageResource(R.mipmap.icon_attach_export);
        } else {
            holder.img_download_state.setImageResource(R.mipmap.icon_attach_downloading);
        }

        holder.area_download_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis() - lastClickTime > 500) {
                    if (itemSource.getStatus() == 1) {
                        //弹出操作框
                        Intent it = new Intent(mContext, PlayPopWin.class);
                        it.putExtra("isFile", true);
                        it.putExtra("position", position);
                        it.putExtra("from", "file_source");
                        mContext.startActivityForResult(it, DownloadedVAdioFragment.DOWNLOADED_REFRESH);
                    }
                    mOnDownLoadClickedLsner.onCliked(position);
                    lastClickTime = System.currentTimeMillis();
                }
            }
        });

        if (position == mList.size() - 1) {
            holder.linear_bottom.setVisibility(View.GONE);
        } else {
            holder.linear_bottom.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_file_cover, img_download_state;
        TextView tv_source_title;
        TextView tv_source_format;
        TextView tv_download_times;
        View linear_bottom, area_download_state;
    }

    public interface OnDownLoadClickedLsner {
        void onCliked(int index);
    }

    public void setOnDownLoadClickedLsner(OnDownLoadClickedLsner listener) {
        this.mOnDownLoadClickedLsner = listener;
    }
}
