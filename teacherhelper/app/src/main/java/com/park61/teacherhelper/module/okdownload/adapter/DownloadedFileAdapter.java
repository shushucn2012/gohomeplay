package com.park61.teacherhelper.module.okdownload.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okserver.download.DownloadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.okdownload.PlayPopWin;
import com.park61.teacherhelper.module.okdownload.fragment.DownloadedVAdioFragment;
import com.park61.teacherhelper.module.okdownload.widget.FileModel;

import java.util.List;

/**
 * Created by chenlie on 2018/1/26.
 */

public class DownloadedFileAdapter extends RecyclerView.Adapter<DownloadedFileAdapter.ViewHolder> {

    private List<DownloadTask> datas;
    private Activity mContext;

    public DownloadedFileAdapter(Activity context, List<DownloadTask> tasks) {
        this.mContext = context;
        this.datas = tasks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_downloaded, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.flyt.setVisibility(View.GONE);
        holder.flyt_audio.setVisibility(View.GONE);
        holder.flyt_attachment.setVisibility(View.VISIBLE);
        DownloadTask task = datas.get(position);
        FileModel model = (FileModel) task.progress.extra1;
        //图标
        ImageManager.getInstance().displayImg(holder.icon, model.getIconUrl(), R.mipmap.qita);
        holder.title.setText(model.getName());
        holder.time.setText(model.getCreateTime());
        holder.size.setText(model.getSize());
        holder.playImg.setVisibility(View.GONE);
        holder.line_bottom_file.setVisibility(View.VISIBLE);
        //弹出操作框
        holder.operate.setOnClickListener(v -> {
            Intent it = new Intent(mContext, PlayPopWin.class);
            it.putExtra("isFile", true);
            it.putExtra("position", position);
            mContext.startActivityForResult(it, DownloadedVAdioFragment.DOWNLOADED_REFRESH);
        });
    }


    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        //播放状态 图标
        ImageView playImg;
        TextView title;
        //下载时间
        TextView time;
        //文件时长
        TextView size;
        //操作图标
        LinearLayout operate;
        View flyt, flyt_audio, flyt_attachment, line_bottom_file;

        public ViewHolder(View v) {
            super(v);
            icon = (RoundedImageView) v.findViewById(R.id.icon);
            playImg = (ImageView) v.findViewById(R.id.play_state);
            title = (TextView) v.findViewById(R.id.title);
            time = (TextView) v.findViewById(R.id.download_time);
            size = (TextView) v.findViewById(R.id.size);
            operate = (LinearLayout) v.findViewById(R.id.operate);
            flyt = v.findViewById(R.id.flyt);
            flyt_audio = v.findViewById(R.id.flyt_audio);
            flyt_attachment = v.findViewById(R.id.flyt_attachment);
            line_bottom_file = v.findViewById(R.id.line_bottom_file);
        }
    }
}
