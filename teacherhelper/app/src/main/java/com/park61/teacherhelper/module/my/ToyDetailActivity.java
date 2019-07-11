package com.park61.teacherhelper.module.my;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.HtmlParserTool;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.databinding.ActivityGoodsdetailsToyBinding;
import com.park61.teacherhelper.module.my.bean.ToysBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.webview.ShowImageWebView;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenlie on 2018/1/4.
 * <p>
 * 亲子活动商品详情 pmInfoId 和 规格选择页面
 */

public class ToyDetailActivity extends BaseActivity {

    public static final int REQUEST_CODE = 0x0021;
    public static final int RESULT_CODE = 0x0022;

    ActivityGoodsdetailsToyBinding binding;
    //需要回传的商品
    private ToysBean selectBean;

    @Override
    public void setLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_goodsdetails_toy);
    }

    @Override
    public void initView() {
        binding.setGoodTvColor(Color.parseColor("#FF5A80"));
        binding.setDetailTvColor(Color.parseColor("#000000"));
        binding.topLine1.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        selectBean = getIntent().getParcelableExtra("toyBean");

        WebSettings ws1 = binding.toyDescription.getSettings();
        ws1.setJavaScriptEnabled(true);
        ws1.setJavaScriptCanOpenWindowsAutomatically(true);
        ws1.setDomStorageEnabled(true);
        ws1.setAppCacheEnabled(true);
        ws1.setLoadWithOverviewMode(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncDetail();
    }

    private void asyncDetail() {
        String url = AppUrl.host + AppUrl.toyDetail;
        Map<String, Object> map = new HashMap<>();
        map.put("id", selectBean.getId());
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, detailListener);
    }

    BaseRequestListener detailListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            setDetail(jsonResult);
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
            dismissDialog();
        }
    };

    /**
     * "currentUnifyPrice": 0.01,
     * "id": 86,
     * "marketPrice": 5555,
     * "productCname": "联想笔记本电脑Y80",
     * "productDescription": "测试内容h52q",
     * "productPicUrl": "http://park61.oss-cn-zhangjiakou.aliyuncs.com/product/20170817105240281_752.png"
     */
    private void setDetail(JSONObject j) {
        ImageManager.getInstance().displayImg(binding.toyImg, j.optString("productPicUrl"), R.mipmap.img_default_h);
        binding.toyName.setText(j.optString("productCname"));
        binding.toyPrice.setText(j.optDouble("currentUnifyPrice") + "");
        double p = j.optDouble("marketPrice");
        if (p > 0) {
            binding.toyOrigPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            binding.toyOrigPrice.setText("￥" + p);
        }
        setWebData(binding.toyDescription, j.optString("productDescription"));
    }

    /**
     * 设置后台编辑的富文本
     */
    private void setWebData(ShowImageWebView wv, String webContent) {
        ViewInitTool.initShowimgWebview(wv);
        String htmlDetailsStr = "";
        try {
            htmlDetailsStr = HtmlParserTool.replaceImgStr(webContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        wv.loadDataWithBaseURL("file:///android_asset", htmlDetailsStr, "text/html", "UTF-8", "");
    }

    @Override
    public void initListener() {

        binding.setGoodClick(v->{
            binding.contentScroll.scrollTo(0,0);
        });

        binding.setDetailClick(v->{
            binding.contentScroll.scrollTo(0, binding.content.getHeight()+1);
        });


        binding.contentScroll.setScrollBottomListener(() -> titleChanged(false));

        binding.contentScroll.setScrollViewListener((l,t,oldL,oldT) -> {
            if(t > binding.content.getHeight()){
                titleChanged(false);
            }else{
                titleChanged(true);
            }
        });
    }

    private void titleChanged(boolean isLeft){
        if(isLeft){
            binding.setGoodTvColor(Color.parseColor("#FF5A80"));
            binding.setDetailTvColor(Color.parseColor("#000000"));
            binding.topLine1.setVisibility(View.VISIBLE);
            binding.topLine2.setVisibility(View.GONE);
        }else{
            binding.setGoodTvColor(Color.parseColor("#000000"));
            binding.setDetailTvColor(Color.parseColor("#FF5A80"));
            binding.topLine1.setVisibility(View.GONE);
            binding.topLine2.setVisibility(View.VISIBLE);
        }
    }
}
