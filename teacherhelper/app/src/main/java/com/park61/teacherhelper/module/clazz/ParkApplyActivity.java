package com.park61.teacherhelper.module.clazz;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.CanBackWebViewActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.databinding.ActivityParkApplyBinding;
import com.park61.teacherhelper.module.clazz.adapter.IconAdapter;
import com.park61.teacherhelper.module.clazz.adapter.ParkApplyAdapter;
import com.park61.teacherhelper.module.clazz.bean.IconBean;
import com.park61.teacherhelper.module.clazz.bean.ParkApplyBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.listview.ObservableScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chenlie on 2018/5/16.
 * <p>
 * 基地园明星园申请首页
 */

public class ParkApplyActivity extends BaseActivity {

    private ActivityParkApplyBinding binding;
    private List<IconBean> list;
    private List<ParkApplyBean> applyList;
    private IconAdapter mAdapter;
    private ParkApplyAdapter listAdapter;
    private ObservableScrollView scroll_content;
    private ImageView img_back;
    private RelativeLayout top_bar;
    private TextView page_title;
    private TextView bottom_bar_list;

    @Override
    public void setLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_park_apply);
    }

    @Override
    public void initView() {
        binding.gridInfo.setVerticalScrollBarEnabled(false);
        binding.applyLv.setVerticalScrollBarEnabled(false);
        scroll_content = (ObservableScrollView) findViewById(R.id.scroll_content);
        top_bar = (RelativeLayout) findViewById(R.id.top_bar);
        img_back = (ImageView) findViewById(R.id.img_back);
        page_title = (TextView) findViewById(R.id.page_title);
        bottom_bar_list = (TextView) findViewById(R.id.bottom_bar_list);
    }

    @Override
    public void initData() {
        list = new ArrayList<>();
        list.add(new IconBean());
        mAdapter = new IconAdapter(this, list);
        binding.gridInfo.setAdapter(mAdapter);

        //已申请列表，跳转到申请详情页面
        applyList = new ArrayList<>();
        listAdapter = new ParkApplyAdapter(this, applyList);
        binding.applyLv.setAdapter(listAdapter);
        binding.gridInfo.setSelector(new ColorDrawable(Color.TRANSPARENT));// 去掉默认点击背景
    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncApplyList();
    }

    @Override
    public void initListener() {
        //顶部图片点击跳转 到介绍
        binding.setTopClick(v -> asyncIntroUrl());

        //立即申请，跳转申请页面
        binding.setApplyClick(v -> startActivity(new Intent(mContext, ParkSubmitActivity.class)));

        binding.applyLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParkApplyBean b = applyList.get(position);
                if (b.getStatus() == 0) {
                    //待提交,用申请id跳转提交页面
                    Intent it = new Intent(mContext, ParkSubmitActivity.class);
                    it.putExtra("applyId", b.getId());
                    startActivity(it);
                } else {
                    //其他状态去详情页面
                    Intent it = new Intent(mContext, ParkApplyDetailActivity.class);
                    it.putExtra("applyId", b.getId());
                    startActivity(it);
                }
            }
        });
        scroll_content.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(int x, int y, int oldx, int oldy) {
                int scrollY = scroll_content.getScrollY();
                int maxHeight = DevAttr.dip2px(mContext, 100);
                if (scrollY <= 0) {   //设置标题的背景颜色
                    top_bar.setBackgroundColor(getResources().getColor(R.color.transparent));
                    img_back.setImageResource(R.mipmap.icon_content_backimg);
                    page_title.setVisibility(View.GONE);
                } else if (scrollY > 0 && scrollY <= maxHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                    float scale = (float) scrollY / maxHeight;
                    float alpha = (255 * scale);
                    top_bar.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    img_back.setImageResource(R.mipmap.icon_content_backimg);
                    page_title.setVisibility(View.GONE);
                } else {
                    top_bar.setBackgroundColor(getResources().getColor(R.color.gffffff));
                    img_back.setImageResource(R.mipmap.icon_red_backimg);
                    page_title.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 请求申请列表
     */
    private void asyncApplyList() {
        String url = AppUrl.host + AppUrl.parkApplyList;
        String requestData = ParamBuild.buildParams(new HashMap<String, Object>());
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, listListener);
    }

    BaseRequestListener listListener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            binding.content.setVisibility(View.VISIBLE);
            asyncIconList();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray arr = jsonResult.optJSONArray("list");
            if (arr != null && arr.length() > 0) {
                //有申请列表
                binding.applyArea.setVisibility(View.VISIBLE);
                binding.content.setVisibility(View.GONE);
                bottom_bar_list.setVisibility(View.VISIBLE);
                applyList.clear();
                for (int i = 0; i < arr.length(); i++) {
                    ParkApplyBean tmp = gson.fromJson(arr.optJSONObject(i).toString(), ParkApplyBean.class);
                    applyList.add(tmp);
                }
                listAdapter.notifyDataSetChanged();
            } else {
                binding.content.setVisibility(View.VISIBLE);
                binding.applyArea.setVisibility(View.GONE);
                bottom_bar_list.setVisibility(View.GONE);
                asyncIconList();
            }
        }
    };

    /**
     * 需要显示的图标
     * "id": 1,
     * "imgUrl": "http://park61.oss-cn-zhangjiakou.aliyuncs.com/test/20171120135232375_204.png",
     * "isFinished": 1,
     * "itemId": 1,
     * "name": "荣誉资质",
     * "sort": 1
     */
    private void asyncIconList() {
        String url = AppUrl.host + AppUrl.infoIconList;
        String requestData = ParamBuild.buildParams(new HashMap<String, Object>());
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, iconListener);
    }

    BaseRequestListener iconListener = new JsonRequestListener() {

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
            JSONArray arr = jsonResult.optJSONArray("list");
            if (arr != null && arr.length() > 0) {
                list.clear();
                list.add(new IconBean());
                for (int i = 0; i < arr.length(); i++) {
                    IconBean tmp = gson.fromJson(arr.optJSONObject(i).toString(), IconBean.class);
                    list.add(tmp);
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    /**
     * 顶部图片点击申请介绍
     */
    private void asyncIntroUrl() {
        String url = AppUrl.host + AppUrl.introParkApply;
        String requestData = ParamBuild.buildParams(new HashMap<String, Object>());
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, urlListener);
    }

    BaseRequestListener urlListener = new JsonRequestListener() {

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
            String introUrl = jsonResult.optString("data");
            if (!TextUtils.isEmpty(introUrl)) {
                Intent intent = new Intent(mContext, CanBackWebViewActivity.class);
                intent.putExtra("url", introUrl);
                startActivity(intent);
            }
        }
    };
}
