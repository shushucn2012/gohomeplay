package com.park61.teacherhelper.module.okdownload.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alivc.player.AliyunErrorCode;
import com.aliyun.vodplayer.downloader.AliyunDownloadManager;
import com.aliyun.vodplayer.downloader.AliyunDownloadMediaInfo;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.okdownload.DownloadService;
import com.park61.teacherhelper.module.okdownload.MyDownLoadVAdioActivity;
import com.park61.teacherhelper.module.okdownload.widget.NumberProgressBar;

import java.util.List;

/**
 * Created by chenlie on 2018/1/26.
 *
 * 下载中的adapter
 */

public class DownloadingAdapter extends RecyclerView.Adapter<DownloadingAdapter.DownloadingHolder> implements DownloadService.ProgressListener {

    public static final String TAG = "DownloadingAdapter";
    private List<AliyunDownloadMediaInfo> datas;
    private Context context;
    private AliyunDownloadManager downloadManager;
    public ReDownloadListener mListener;

    public DownloadingAdapter(Context context, List<AliyunDownloadMediaInfo> tasks){
        this.context = context;
        //数据库存储的下载中的任务
        downloadManager = AliyunDownloadManager.getInstance(context);
        datas =  tasks;
    }

    @Override
    public DownloadingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_download_ing, parent, false);
        return new DownloadingHolder(view);
    }

    @Override
    public void onBindViewHolder(DownloadingHolder holder, int position) {
        AliyunDownloadMediaInfo task = datas.get(position);

        if(task.getStatus() == AliyunDownloadMediaInfo.Status.Start){
            holder.icon.setImageResource(R.mipmap.downloading);
            //显示网速
            holder.netSpeed.setText("正在下载");
        }else{
            holder.netSpeed.setText("等待下载中...");
            holder.icon.setImageResource(R.mipmap.icon_wait);
        }
        holder.title.setText(task.getTitle());

        //item点击事件
        holder.itemView.setOnClickListener(v -> {
            AliyunDownloadMediaInfo.Status status = task.getStatus();
            if(status == AliyunDownloadMediaInfo.Status.Start){
                //点击下载中
                Log.e(TAG, "暂停下载");
                downloadManager.stopDownloadMedia(task);
            }else{
                int tmp = downloadManager.startDownloadMedia(task);
                Log.e(TAG, "继续下载:"+tmp);
                //如果退出应用重新打开app时，tmp不为0无法继续下载，重新添加到service
                if(tmp != AliyunErrorCode.ALIVC_SUCCESS.getCode()){
                    Log.e(TAG, "error, renew add");
                    if(mListener != null){
                        mListener.reDownload(task.getVid());
                    }
                }
//                if(task.getStatus() == AliyunDownloadMediaInfo.Status.Error){
//                    Log.e(TAG, "Error");
//                }else if (task.getStatus() == AliyunDownloadMediaInfo.Status.Idle){
//                    Log.e(TAG, "Idle");
//                }else if (task.getStatus() == AliyunDownloadMediaInfo.Status.Start){
//                    Log.e(TAG, "Start");
//                }else if (task.getStatus() == AliyunDownloadMediaInfo.Status.Stop){
//                    Log.e(TAG, "Stop");
//                }else if (task.getStatus() == AliyunDownloadMediaInfo.Status.Wait){
//                    Log.e(TAG, "Wait");
//                }
            }
        });

        holder.progressBar.setMax(100);
        holder.progressBar.setProgress(task.getProgress());
        //下载大小显示
        String currentSize = Formatter.formatFileSize(context, task.getSize() * task.getProgress()/100);
        String totalSize = Formatter.formatFileSize(context, task.getSize());
        holder.size.setText(currentSize + "/" + totalSize);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public void newProgress(AliyunDownloadMediaInfo aliyunDownloadMediaInfo) {
        for(AliyunDownloadMediaInfo info : datas){
            if(info.getVid().equals(aliyunDownloadMediaInfo.getVid())){
                info.setProgress(aliyunDownloadMediaInfo.getProgress());
                info.setStatus(aliyunDownloadMediaInfo.getStatus());
                Log.e(TAG, info.getTitle()+"--进度--"+info.getProgress());
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onStop(AliyunDownloadMediaInfo aliyunDownloadMediaInfo) {
        for(AliyunDownloadMediaInfo info : datas){
            if(info.getVid().equals(aliyunDownloadMediaInfo.getVid())){
                info.setStatus(aliyunDownloadMediaInfo.getStatus());
                Log.e(TAG, info.getTitle()+"--停止--");
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onComplete(AliyunDownloadMediaInfo aliyunDownloadMediaInfo) {
        for(int i=datas.size()-1; i>=0; i--){
            AliyunDownloadMediaInfo info = datas.get(i);
            if(info.getVid().equals(aliyunDownloadMediaInfo.getVid())){
                datas.remove(info);
                Log.e(TAG, "下载完成");
            }
        }
        context.sendBroadcast(new Intent(MyDownLoadVAdioActivity.ACTION_COMPLETE));
        notifyDataSetChanged();
    }

    @Override
    public void onError(AliyunDownloadMediaInfo aliyunDownloadMediaInfo) {
        for(AliyunDownloadMediaInfo info : datas){
            if(info.getVid().equals(aliyunDownloadMediaInfo.getVid())){
                info.setStatus(aliyunDownloadMediaInfo.getStatus());
                Log.e(TAG, info.getTitle()+"--错误--");
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onWait() {
        notifyDataSetChanged();
    }

    class DownloadingHolder extends RecyclerView.ViewHolder{

        ImageView icon;
        TextView title;
        NumberProgressBar progressBar;
        TextView netSpeed;
        TextView size;

        public DownloadingHolder(View v) {
            super(v);
            icon = (ImageView) v.findViewById(R.id.left_icon);
            title = (TextView) v.findViewById(R.id.download_title);
            progressBar = (NumberProgressBar) v.findViewById(R.id.pbProgress);
            netSpeed = (TextView) v.findViewById(R.id.netSpeed);
            size = (TextView) v.findViewById(R.id.download_size);
        }

    }

    public void setReDownloadListener(ReDownloadListener listener){
        mListener = listener;
    }

    public interface ReDownloadListener{
        void reDownload(String vid);
    }
}
