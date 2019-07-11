package com.park61.teacherhelper.module.okdownload;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.databinding.ActivityPlayPopBinding;
import com.park61.teacherhelper.module.pay.CouponChooseActivity;
import com.park61.teacherhelper.module.pay.bean.CouponBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by chenlie on 2018/1/29.
 * <p>
 * 音视频下载管理单条记录操作界面
 */

public class PlayPopWin extends BaseActivity {

    ActivityPlayPopBinding binding;
    int position = -1;
    boolean isFile = false;

    private int fileType = 0; //0音频 1视频 2课件
    private String contentId;//资源id
    private String from;//从附件下载页面来吗

    @Override
    public void setLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_play_pop);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        //接收下载管理界面传递的参数
        position = getIntent().getIntExtra("position", -1);
        isFile = getIntent().getBooleanExtra("isFile", false);
        fileType = getIntent().getIntExtra("fileType", -1);
        contentId = getIntent().getStringExtra("contentId");
        from = getIntent().getStringExtra("from");
    }

    @Override
    public void initListener() {
        if (isFile) {
            binding.play.setVisibility(View.GONE);
            binding.toWx.setVisibility(View.VISIBLE);
            binding.toQQ.setVisibility(View.VISIBLE);
            if ("file_source".equals(from)) {
                binding.delete.setVisibility(View.GONE);
            }
        } else {
            //修改为音频也不导出
            /*if (fileType == 0) {//0音频
                binding.tvOutport.setVisibility(View.VISIBLE);
            }*/
        }

        binding.play.setOnClickListener(v -> {
            //跳转详情页播放
            Intent it = new Intent();
            it.putExtra("operate", "play");
            it.putExtra("position", position);
            setResult(RESULT_OK, it);
            finish();
        });

        binding.delete.setOnClickListener(v -> {
            //删除
            Intent it = new Intent();
            it.putExtra("operate", "delete");
            it.putExtra("position", position);
            setResult(RESULT_OK, it);
            finish();
        });

        binding.toWx.setOnClickListener(v -> {
            //分享到微信
            Intent it = new Intent();
            it.putExtra("operate", "toWx");
            it.putExtra("position", position);
            it.putExtra("fileType", fileType);
            setResult(RESULT_OK, it);
            finish();
        });

        binding.toQQ.setOnClickListener(v -> {
            //分享到QQ
            Intent it = new Intent();
            it.putExtra("operate", "toQQ");
            it.putExtra("position", position);
            it.putExtra("fileType", fileType);
            setResult(RESULT_OK, it);
            finish();
        });

        binding.tvOutport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(contentId)) {
                    showShortToast("该文件不可导出");
                    return;
                }
                asyncIsAudioCanExport();
            }
        });

        binding.cancel.setOnClickListener(v -> finish());
        findViewById(R.id.area_outside).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 音频是否可导出
     */
    private void asyncIsAudioCanExport() {
        String wholeUrl = AppUrl.host + AppUrl.isAudioCanExport;//"http://10.10.10.18:8380/service/content/isAudioCanExport";//
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contentId", contentId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            String resultData = jsonResult.optString("data");
            if ("1".equals(resultData)) {
                binding.play.setVisibility(View.GONE);
                binding.toWx.setVisibility(View.VISIBLE);
                binding.toQQ.setVisibility(View.VISIBLE);
                binding.tvOutport.setVisibility(View.GONE);
                binding.delete.setVisibility(View.GONE);
            } else {
                showShortToast("该文件不可导出");
            }
        }
    };


}
