package com.park61.teacherhelper.module.okdownload.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aliyun.vodplayer.downloader.AliyunDownloadManager;
import com.aliyun.vodplayer.downloader.AliyunDownloadMediaInfo;
import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.aliyun.vodplayer.media.AliyunVodPlayer;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.park61.teacherhelper.AudioBaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.databinding.FragmentDownloadedVadioBinding;
import com.park61.teacherhelper.module.home.VideoLocalPlayActivity;
import com.park61.teacherhelper.module.okdownload.DownloadListActivity;
import com.park61.teacherhelper.module.okdownload.adapter.DownloadedAdapter;
import com.park61.teacherhelper.module.okdownload.db.DownloadDAO;
import com.park61.teacherhelper.module.okdownload.db.DownloadTask;
import com.park61.teacherhelper.module.okdownload.widget.SimpleTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlie on 2018/1/25.
 * modify by asuper on 2018/11/9
 * <p>
 * 音视频已下载列表
 */

public class DownloadedVAdioFragment extends AudioBaseFragment {

    public static final int DOWNLOADED_REFRESH = 0x0007;
    FragmentDownloadedVadioBinding binding;
    private List<DownloadTask> datas = new ArrayList<>();
    private List<DownloadTask> videos = new ArrayList<>();
    private List<DownloadTask> audios = new ArrayList<>();
    LRecyclerViewAdapter mLRVAdapter;
    DownloadedAdapter adapter;
    //0音频 1视频
    public int currentTitle = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_downloaded_vadio, container, false);
        parentView = binding.getRoot();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        binding.audioTv.setTextColor(Color.parseColor("#ff5a80"));
    }

    @Override
    public void initData() {
        //分为音频和视频两个列表
        seperatData();
        //初始显示音频列表
        datas.addAll(audios);
        adapter = new DownloadedAdapter(getActivity(), datas);
        adapter.setOnPlayClickedListener(new DownloadedAdapter.OnPlayClickedListener() {
            @Override
            public void OnPlayClicked(int position) {
                play(position);
            }
        });
        mLRVAdapter = new LRecyclerViewAdapter(adapter);
        binding.downloadedLv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.downloadedLv.setAdapter(mLRVAdapter);
    }

    /**
     * 查询音视频已下载数据
     */
    private void seperatData() {
        List<DownloadTask> temp = DownloadDAO.getInstance().selectAllComplete();
        for (DownloadTask task : temp) {
            if (task.getTask_type() == 0) {
                //视频
                videos.add(task);
            } else if (task.getTask_type() == 1) {
                //音频
                audios.add(task);
            }
        }
    }

    @Override
    public void initListener() {
        binding.downloadedLv.setPullRefreshEnabled(false);
        setListNum(audios.size());
        //点击事件
        binding.audioTv.setOnClickListener(v -> {
            if (currentTitle != 0) {
                //点击音频
                currentTitle = 0;
                adapter.setFileType(currentTitle);
                binding.audioTv.setTextColor(Color.parseColor("#ff5a80"));
                binding.videoTv.setTextColor(Color.parseColor("#222222"));
                binding.line1.setVisibility(View.VISIBLE);
                binding.line2.setVisibility(View.INVISIBLE);
                binding.downloadedLv.setVisibility(View.VISIBLE);
                setListNum(audios.size());
                datas.clear();
                datas.addAll(audios);
                mLRVAdapter.notifyDataSetChanged();
            }
        });

        binding.videoTv.setOnClickListener(v -> {
            if (currentTitle != 1) {
                currentTitle = 1;
                adapter.setFileType(currentTitle);
                if (aliyunVodPlayer != null && aliyunVodPlayer.isPlaying()) {
                    aliyunVodPlayer.pause();
                }
                adapter.setCurrentPos(-1);
                adapter.setIsPlaying(false);
                binding.audioTv.setTextColor(Color.parseColor("#222222"));
                binding.videoTv.setTextColor(Color.parseColor("#ff5a80"));
                binding.line2.setVisibility(View.VISIBLE);
                binding.line1.setVisibility(View.INVISIBLE);
                binding.downloadedLv.setVisibility(View.VISIBLE);
                setListNum(videos.size());
                datas.clear();
                datas.addAll(videos);
                mLRVAdapter.notifyDataSetChanged();
            }
        });

        binding.setManageClick(v -> {
            if (datas.size() > 0) {
                //管理音视频
                goDownListActivity();
            }
        });
    }

    /**
     * ... 弹窗后删除 点击项
     */
    public void delete(int position) {
        //数据库删除
        String task_vid = datas.get(position).getTask_vid();
        DownloadDAO.getInstance().delete(task_vid);
        if (currentTitle == 0) {
            audios.remove(position);
        } else if (currentTitle == 1) {
            videos.remove(position);
        }
        datas.remove(position);
        setListNum(datas.size());
        mLRVAdapter.notifyDataSetChanged();
        //阿里云任务删除
        List<AliyunDownloadMediaInfo> infos = AliyunDownloadManager.getInstance(getActivity()).getUnfinishedDownload();
        for (AliyunDownloadMediaInfo info : infos) {
            if (info.getVid().equals(task_vid)) {
                AliyunDownloadManager.getInstance(getActivity()).removeDownloadMedia(info);
            }
        }
    }

    /**
     * 弹窗后 点击播放
     */
    public void play(int position) {
        if (currentTitle == 0) {
            if (aliyunVodPlayer == null) {
                aliyunVodPlayer = new AliyunVodPlayer(parentActivity);
                aliyunVodPlayer.setOnPreparedListener(new IAliyunVodPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared() {
                        aliyunVodPlayer.start();
                    }
                });
                aliyunVodPlayer.setOnCompletionListener(new IAliyunVodPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion() {
                        //adapter.setCurrentPos(-1);
                        adapter.setIsPlaying(false);
                        mLRVAdapter.notifyDataSetChanged();
                    }
                });
            }
            if (adapter.getCurrentPos() == position) {
                //暂停当前
               /* if (aliyunVodPlayer.isPlaying()) {
                    //adapter.setCurrentPos(-1);
                    adapter.setIsPlaying(false);
                    aliyunVodPlayer.pause();
                } else {
                    if (aliyunVodPlayer.getPlayerState() == IAliyunVodPlayer.PlayerState.Paused) {
                        adapter.setIsPlaying(true);
                        aliyunVodPlayer.start();
                    }
                }*/

                mPlayerState = aliyunVodPlayer.getPlayerState();
                if (mPlayerState == IAliyunVodPlayer.PlayerState.Started) {//如果已经开始，就暂停
                    aliyunVodPlayer.pause();
                    adapter.setIsPlaying(false);
                } else {//其他状态
                    aliyunVodPlayer.start();
                    adapter.setIsPlaying(true);
                }
            } else {
                String videoPath = datas.get(position).getTask_filePath();
                AliyunLocalSource.AliyunLocalSourceBuilder alsb = new AliyunLocalSource.AliyunLocalSourceBuilder();
                alsb.setSource(videoPath);
                AliyunLocalSource localSource = alsb.build();
                audioStartPlayLocal(localSource);
                adapter.setCurrentPos(position);
                adapter.setIsPlaying(true);
            }
            mLRVAdapter.notifyDataSetChanged();
        } else if (currentTitle == 1) {
            String videoPath = datas.get(position).getTask_filePath();
            Intent it = new Intent(getActivity(), VideoLocalPlayActivity.class);
            it.putExtra("videoPath", videoPath);
            startActivity(it);
        }
    }

    private void setListNum(int num) {
        if (num > 0) {
            //隐藏缺省图
            binding.lvEmpty.setVisibility(View.GONE);
        } else {
            //显示缺省图
            binding.lvEmpty.setVisibility(View.VISIBLE);
        }
        if (currentTitle == 0) {
            binding.tvTypeLabel.setText("全部音频");
            binding.numTv.setText("(共" + num + "首)");
        } else {
            binding.tvTypeLabel.setText("全部视频");
            binding.numTv.setText("(" + num + ")");
        }
    }

    /**
     * 跳管理界面
     */
    private void goDownListActivity() {
        List<SimpleTask> simpleTasks = new ArrayList<>();
        for (DownloadTask t : datas) {
            SimpleTask s = new SimpleTask();
            s.setVid(t.getTask_vid());
            s.setTitle(t.getTask_name());
            s.setSize(Formatter.formatFileSize(getActivity(), t.getTask_size()));
            simpleTasks.add(s);
        }

        Intent it = new Intent(getActivity(), DownloadListActivity.class);
        it.putExtra("flag", DOWNLOADED_REFRESH);
        it.putExtra("list", (Serializable) simpleTasks);
        getActivity().startActivity(it);
    }

    /**
     * 管理界面删除后刷新界面
     */
    public void refresh() {
        //刷新音视频列表
        datas.clear();
        audios.clear();
        videos.clear();
        seperatData();
        if (currentTitle == 0) {
            //音频
            datas.addAll(audios);
            setListNum(audios.size());
        } else if (currentTitle == 1) {
            //视频
            datas.addAll(videos);
            setListNum(videos.size());
        }
        mLRVAdapter.notifyDataSetChanged();
    }

}
