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

import com.lzy.okgo.model.Progress;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.okdownload.MyDownLoadVAdioActivity;
import com.park61.teacherhelper.module.okdownload.widget.FileModel;
import com.park61.teacherhelper.module.okdownload.widget.NumberProgressBar;

import java.io.File;
import java.util.List;

/**
 * Created by chenlie on 2018/1/26.
 *
 * 下载中的adapter
 */

public class DownloadingFileAdapter extends RecyclerView.Adapter<DownloadingFileAdapter.DownloadingFileHolder>{

    public static final String TAG = "DownloadingFileAdapter";
    private List<DownloadTask> datas;
    private Context context;

    public DownloadingFileAdapter(Context context, List<DownloadTask> tasks){
        this.context = context;
        datas =  tasks;
    }

    @Override
    public DownloadingFileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_download_ing, parent, false);
        return new DownloadingFileHolder(view);
    }

    @Override
    public void onBindViewHolder(DownloadingFileHolder holder, int position) {

        DownloadTask task = datas.get(position);
        task.register(new ListDownloadListener(task.progress.tag, holder));
        holder.setTag(task.progress.tag);
        holder.setTask(task);
        holder.refresh(task.progress);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class DownloadingFileHolder extends RecyclerView.ViewHolder{

        ImageView icon;
        TextView title;
        NumberProgressBar progressBar;
        TextView netSpeed;
        TextView size;
        private String tag;
        private DownloadTask task;

        public void setTask(DownloadTask task) {
            this.task = task;
        }


        public DownloadingFileHolder(View v) {
            super(v);
            icon = (ImageView) v.findViewById(R.id.left_icon);
            title = (TextView) v.findViewById(R.id.download_title);
            progressBar = (NumberProgressBar) v.findViewById(R.id.pbProgress);
            netSpeed = (TextView) v.findViewById(R.id.netSpeed);
            size = (TextView) v.findViewById(R.id.download_size);
        }

        public void refresh(Progress progress) {
            FileModel model = (FileModel) task.progress.extra1;
            if(task.progress.status == Progress.LOADING){
                icon.setImageResource(R.mipmap.downloading);
                netSpeed.setText("正在下载");
            }else{
                netSpeed.setText("等待下载中...");
                icon.setImageResource(R.mipmap.icon_wait);
            }
            title.setText(model.getName());

            //item点击事件
            itemView.setOnClickListener(v -> {
                int status = task.progress.status;
                if(status == Progress.LOADING){
                    //点击下载中
                    Log.e(TAG, "暂停下载");
                    task.pause();
                }else{
                    Log.e(TAG, "继续下载");
                    task.start();
                }
            });

            progressBar.setMax(100);
            progressBar.setProgress((int) (task.progress.fraction * 100));
            //下载大小显示
            String currentSize = Formatter.formatFileSize(context, task.progress.currentSize);
            size.setText(currentSize + "/" + model.getSize());
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }
    }

    private class ListDownloadListener extends DownloadListener {

        private DownloadingFileHolder holder;

        ListDownloadListener(Object tag, DownloadingFileHolder holder) {
            super(tag);
            this.holder = holder;
        }

        @Override
        public void onStart(Progress progress) {
        }

        @Override
        public void onProgress(Progress progress) {
            if (tag == holder.getTag()) {
                holder.refresh(progress);
            }
        }

        @Override
        public void onError(Progress progress) {
            Throwable throwable = progress.exception;
            if (throwable != null) throwable.printStackTrace();
        }

        @Override
        public void onFinish(File file, Progress progress) {
            for(int i=datas.size()-1; i>=0; i--){
                DownloadTask task = datas.get(i);
                if( task.progress.tag == tag){
                    datas.remove(i);
                    break;
                }
            }
            Log.e(TAG, "finish:" + progress.filePath);
            context.sendBroadcast(new Intent(MyDownLoadVAdioActivity.ACTION_COMPLETE));
            notifyDataSetChanged();
        }

        @Override
        public void onRemove(Progress progress) {
        }
    }
}
