package com.park61.teacherhelper.module.okdownload;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;

import com.aliyun.vodplayer.downloader.AliyunDownloadMediaInfo;
import com.lzy.okserver.OkDownload;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.databinding.ActivityDownloadListBinding;
import com.park61.teacherhelper.module.okdownload.adapter.DownListAdapter;
import com.park61.teacherhelper.module.okdownload.db.DownloadDAO;
import com.park61.teacherhelper.module.okdownload.fragment.DownloadingFragment;
import com.park61.teacherhelper.module.okdownload.widget.SimpleTask;

import java.util.List;

/**
 * Created by chenlie on 2018/2/5.
 *
 *  已下载和下载中管理界面
 */

public class DownloadListActivity extends BaseActivity {

    private ActivityDownloadListBinding binding;
    private List<SimpleTask> list;
    private DownListAdapter mAdapter;
    private boolean isSelectAll = true;
    private int refreshFlag = -1;
    private boolean isFiles = false;
    private int fileNum;

    @Override
    public void setLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_download_list);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        refreshFlag = getIntent().getIntExtra("flag", -1);
        //传过来的列表是否包含课件
        isFiles = getIntent().getBooleanExtra("isFile", false);
        fileNum = getIntent().getIntExtra("fileNum", 0);
        list = (List<SimpleTask>) getIntent().getSerializableExtra("list");
        if(list != null){
            mAdapter = new DownListAdapter(this, list);
            binding.lv.setAdapter(mAdapter);

            binding.lv.setOnItemClickListener((parent, view, position, id) ->{
                SimpleTask simpleTask = list.get(position);
                if(simpleTask.isCheck()){
                    simpleTask.setCheck(false);
                }else{
                    simpleTask.setCheck(true);
                }
                mAdapter.notifyDataSetChanged();
            });
        }
    }

    @Override
    public void initListener() {

        binding.complete.setOnClickListener(v -> finish());
        binding.selectAll.setOnClickListener(v -> updateSelect());
        binding.setDeleteData(v -> {
            //点击删除按钮
            if(mAdapter != null){
                if(!confirmDialog()){
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
                    dialogBuilder.setMessage("请选择需要删除的文件");
                    dialogBuilder.setPositiveButton("确定", (dialog, which) -> {
                        dialog.dismiss();
                    });
                    dialogBuilder.create().show();
                    return;
                }
                if(isFiles){
                    deleteFiles();
                }else{
                    deleteSelect();
                }

                //刷新已下载fragment
                Intent it = new Intent();
                it.setAction(MyDownLoadVAdioActivity.ACTION_DOWNLOADED);
                sendBroadcast(it);
                if(refreshFlag == DownloadingFragment.DOWNLOADING_REFRESH){
                    //刷新下载中列表
                    Intent it2 = new Intent();
                    it2.setAction(MyDownLoadVAdioActivity.ACTION_DOWNLOADING);
                    sendBroadcast(it2);
                }
            }
        });
    }

    private boolean confirmDialog(){
        if(list != null){
            for(SimpleTask t :list){
                if(t.isCheck()){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 全选 和 取消全选
     */
    private void updateSelect(){
        if(list != null){
            for(SimpleTask s : list){
                if(isSelectAll){
                    if(!s.isCheck()){
                        s.setCheck(true);
                    }
                }else{
                    if(s.isCheck()){
                        s.setCheck(false);
                    }
                }
            }
            mAdapter.notifyDataSetChanged();

            isSelectAll = !isSelectAll;

            binding.selectAll.setText(isSelectAll ? "全选" : "取消");
        }
    }

    /**
     * 删除课件任务
     */
    private void deleteFiles(){
        showDialog();

        for(int i=list.size()-1; i>=0 ; i--){
            SimpleTask task = list.get(i);
            if(!task.isCheck()){
                continue;
            }
            if(i < list.size()-fileNum){
                //下载中管理列表是 音视频 + 课件，如果删除位置小于，删除音视频任务，否则删除课件任务
                //删除阿里云
                deleteALiYun(task.getVid());
            }else{
                //删除课件
                OkDownload.getInstance().getTask(task.getVid()).remove(true);
                fileNum--;
            }
            //当前界面刷新
            list.remove(i);
        }
        mAdapter.notifyDataSetChanged();
        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                dismissDialog();
            }
        }.sendEmptyMessageDelayed(0, 1500);
    }

    /**
     * 删除所选音视频任务
     */
    private void deleteSelect(){
        showDialog();
        for(int i=list.size()-1; i>=0 ; i--){
            SimpleTask task = list.get(i);
            if(!task.isCheck()){
                continue;
            }
            //删除本地数据库
            DownloadDAO.getInstance().delete(task.getVid());
            //删除阿里云
            deleteALiYun(task.getVid());
            //当前界面刷新
            list.remove(i);
        }
        mAdapter.notifyDataSetChanged();
        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                dismissDialog();
            }
        }.sendEmptyMessageDelayed(0, 1500);
    }

    private void deleteALiYun(String vid){
        List<AliyunDownloadMediaInfo> infos = downloadManager.getUnfinishedDownload();
        for(AliyunDownloadMediaInfo info : infos){
            if (info.getVid().equals(vid)){
                downloadManager.removeDownloadMedia(info);
            }
        }
    }

}
