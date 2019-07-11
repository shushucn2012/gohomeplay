package com.park61.teacherhelper.module.my;

import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.CanBackWebViewActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.my.adapter.TrainRespListAdapter;
import com.park61.teacherhelper.module.my.bean.TrainRespBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 反馈问卷
 * Created by shubei on 2018/3/30.
 */

public class TrainRespDetailWvActivity extends CanBackWebViewActivity {

    public void setLayout() {
        setContentView(R.layout.activity_trainresp_detail_webview);
    }

    public void initListener() {
        super.initListener();
        area_close.setVisibility(View.GONE);
        //shareDescription = "时间：" + getIntent().getStringExtra("desc");
        mShareInfoBean.setDescription("时间：" + getIntent().getStringExtra("desc"));
    }

    protected void setShareAndNowTitle(String mtitle) {
        logout("====================mtitle====================" + mtitle);
        //DO NOTHING
    }

}
