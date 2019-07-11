package com.park61.teacherhelper.module.workplan;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.module.workplan.adapter.WpCatesAdapter;
import com.park61.teacherhelper.module.workplan.bean.WorkPlanCateBean;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 全部行事历页面
 *
 * @author shubei
 * @time 2018/12/20 16:06
 */
public class WorkPlanAllCateActivity extends BaseActivity {

    private ListView lv_wp_cates;
    private View img_wpcate_cover;

    private List<WorkPlanCateBean> mList;
    private WpCatesAdapter mWpCatesAdapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_workplan_allcate);

    }

    @Override
    public void initView() {
        setPagTitle("全部行事历");
        lv_wp_cates = findViewById(R.id.lv_wp_cates);
        lv_wp_cates.setFocusable(false);
        img_wpcate_cover = findViewById(R.id.img_wpcate_cover);
    }

    @Override
    public void initData() {
        mList = new ArrayList<>();
        mWpCatesAdapter = new WpCatesAdapter(mContext, mList);
        lv_wp_cates.setAdapter(mWpCatesAdapter);
        asyncGetAllTaskCalendarTemplate();
    }

    @Override
    public void initListener() {
        img_wpcate_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SpecialWpSubsActivity.class));
            }
        });
    }

    /**
     * 全部行事历模板
     */
    private void asyncGetAllTaskCalendarTemplate() {
        String url = AppUrl.host + AppUrl.getAllTaskCalendarTemplate;
        netRequest.startRequest(url, Request.Method.POST, "", 0, tlistener);
    }

    BaseRequestListener tlistener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray arr = jsonResult.optJSONArray("list");
            if (arr != null && arr.length() > 0) {
                mList.clear();
                for (int i = 0; i < arr.length(); i++) {
                    WorkPlanCateBean workPlanCateBean = gson.fromJson(arr.optJSONObject(i).toString(), WorkPlanCateBean.class);
                    mList.add(workPlanCateBean);
                }
                mWpCatesAdapter.notifyDataSetChanged();
            }
        }
    };
}
