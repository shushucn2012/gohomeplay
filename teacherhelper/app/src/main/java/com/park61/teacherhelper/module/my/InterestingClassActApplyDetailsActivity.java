package com.park61.teacherhelper.module.my;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.RegexValidator;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.my.adapter.InteresClassActApplyDataAdapter;
import com.park61.teacherhelper.module.my.adapter.InterestingCrouseListAdapter;
import com.park61.teacherhelper.module.my.bean.InterestClassActApplyDataItem;
import com.park61.teacherhelper.module.my.bean.InterestingClassActBean;
import com.park61.teacherhelper.module.my.bean.InterestingCourseBean;
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
 * 兴趣班报名数据详情
 * Created by shubei on 2018/8/16.
 */

public class InterestingClassActApplyDetailsActivity extends BaseActivity {

    private ListView lv_datas;
    private TextView tv_count;
    private EditText edit_sousuo;
    private ImageView img_del;

    private int interestClassActivityId;
    private int interestClassId;
    private List<InterestClassActApplyDataItem> dataList;
    private InteresClassActApplyDataAdapter adapter;
    private String childName, phoneNum;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_interestingclassact_applydetails);
    }

    @Override
    public void initView() {
        tv_count = (TextView) findViewById(R.id.tv_count);
        lv_datas = (ListView) findViewById(R.id.lv_datas);
        edit_sousuo = (EditText) findViewById(R.id.edit_sousuo);
        img_del = (ImageView) findViewById(R.id.img_del);
    }

    @Override
    public void initData() {
        setPagTitle(getIntent().getStringExtra("classActName"));
        interestClassActivityId = getIntent().getIntExtra("interestClassActivityId", -1);
        interestClassId = getIntent().getIntExtra("interestClassId", -1);
        dataList = new ArrayList<>();
        adapter = new InteresClassActApplyDataAdapter(mContext, dataList);
        lv_datas.setAdapter(adapter);
        asyncGetDetailById();
    }

    @Override
    public void initListener() {
        edit_sousuo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = edit_sousuo.getText().toString().trim();
                    if (RegexValidator.isNum(keyword)) {
                        phoneNum = keyword;
                        childName = "";
                    } else {
                        childName = keyword;
                        phoneNum = "";
                    }
                    asyncGetDetailById();
                }
                return false;
            }
        });
        edit_sousuo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(edit_sousuo.getText().toString())) {
                    img_del.setVisibility(View.GONE);
                } else {
                    img_del.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_sousuo.setText("");
                img_del.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 获取详情
     */
    private void asyncGetDetailById() {
        String url = AppUrl.host + AppUrl.interestClassApplyDataDetail;
        Map<String, Object> map = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(phoneNum))
            map.put("mobile", phoneNum);
        if (!TextUtils.isEmpty(childName))
            map.put("childName", childName);
        map.put("interestClassActivityId", interestClassActivityId);
        map.put("interestClassId", interestClassId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestBodyData, 0, taskListener);
    }

    BaseRequestListener taskListener = new JsonRequestListener() {
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
            tv_count.setText(jsonResult.optInt("buyNum") + "");
            JSONArray actJay = jsonResult.optJSONArray("classSoList");
            if (actJay != null && actJay.length() > 0) {
                dataList.clear();
                for (int i = 0; i < actJay.length(); i++) {
                    dataList.add(gson.fromJson(actJay.optJSONObject(i).toString(), InterestClassActApplyDataItem.class));
                }
                adapter.notifyDataSetChanged();
            } else {
                dataList.clear();
                adapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyView(mContext, lv_datas, "暂无数据", R.mipmap.icon_cotent_empty, null);
            }
        }
    };
}
