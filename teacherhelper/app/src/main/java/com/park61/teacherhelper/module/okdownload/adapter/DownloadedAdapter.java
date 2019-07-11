package com.park61.teacherhelper.module.okdownload.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.okdownload.PlayPopWin;
import com.park61.teacherhelper.module.okdownload.db.DownloadTask;
import com.park61.teacherhelper.module.okdownload.fragment.DownloadedVAdioFragment;

import java.util.List;

/**
 * Created by chenlie on 2018/1/26.
 */

public class DownloadedAdapter extends RecyclerView.Adapter<DownloadedAdapter.ViewHolder> {

    private List<DownloadTask> datas;
    private Activity mContext;
    private OnPlayClickedListener mOnPlayClickedListener;
    public int currentPos = -1;
    private int fileType;//文件类型 0音频 1视频 2课件
    private boolean isPlaying;//是否正在播放音频

    public DownloadedAdapter(Activity context, List<DownloadTask> tasks) {
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

        DownloadTask task = datas.get(position);
        ImageManager.getInstance().displayImg(holder.icon, task.getTask_icon());
        holder.title.setText(task.getTask_name());
        holder.time.setText(task.getTask_time());
        holder.size.setText(Formatter.formatFileSize(mContext, task.getTask_size()));
        holder.tv_audio_index.setText((position + 1) + "");

        //弹出操作框
        holder.operate.setOnClickListener(v -> {
            Intent it = new Intent(mContext, PlayPopWin.class);
            it.putExtra("position", position);
            it.putExtra("fileType", fileType);
            it.putExtra("contentId", datas.get(position).getContentId());
            mContext.startActivityForResult(it, DownloadedVAdioFragment.DOWNLOADED_REFRESH);
        });

        holder.itemView.setOnClickListener(v ->
                mOnPlayClickedListener.OnPlayClicked(position)
        );

        if (fileType == 1) {//视频
            holder.flyt.setVisibility(View.VISIBLE);
            holder.flyt_audio.setVisibility(View.GONE);
            holder.line_bottom_video.setVisibility(View.VISIBLE);
            holder.line_bottom_audio.setVisibility(View.GONE);
        } else {//音频
            holder.flyt.setVisibility(View.GONE);
            holder.flyt_audio.setVisibility(View.VISIBLE);
            if (currentPos == position) {
                holder.play_state_audio.setVisibility(View.VISIBLE);
                holder.title.setTextColor(mContext.getResources().getColor(R.color.com_orange));
                if (isPlaying)
                    holder.play_state_audio.setImageResource(R.mipmap.icon_bofanging);
                else
                    holder.play_state_audio.setImageResource(R.mipmap.icon_audio_stop);
            } else {
                holder.play_state_audio.setVisibility(View.GONE);
                holder.title.setTextColor(mContext.getResources().getColor(R.color.g333333));
            }
            holder.line_bottom_audio.setVisibility(View.VISIBLE);
            holder.line_bottom_video.setVisibility(View.GONE);
        }
    }

    public void setCurrentPos(int pos) {
        currentPos = pos;
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        //播放状态 图标
        ImageView playImg, play_state_audio;
        TextView title;
        //下载时间
        TextView time;
        //文件时长
        TextView size;
        //操作图标
        LinearLayout operate;
        View flyt, flyt_audio, line_bottom_audio, line_bottom_video, line_bottom_file;
        TextView tv_audio_index;

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
            play_state_audio = v.findViewById(R.id.play_state_audio);
            tv_audio_index = v.findViewById(R.id.tv_audio_index);
            line_bottom_audio = v.findViewById(R.id.line_bottom_audio);
            line_bottom_video = v.findViewById(R.id.line_bottom_video);
            line_bottom_file = v.findViewById(R.id.line_bottom_file);
        }
    }

    public interface OnPlayClickedListener {
        void OnPlayClicked(int position);
    }

    public void setOnPlayClickedListener(OnPlayClickedListener listener) {
        this.mOnPlayClickedListener = listener;
    }

    /**
     * 设置当前操作的文件类型
     */
    public void setFileType(int fileType) {
        this.fileType = fileType;
    }
}
