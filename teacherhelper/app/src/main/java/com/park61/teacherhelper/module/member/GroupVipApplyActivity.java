package com.park61.teacherhelper.module.member;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.common.tool.StringUtils;
import com.park61.teacherhelper.module.my.ChooseGroupListActivity;
import com.park61.teacherhelper.module.my.KidGardenActivity;
import com.park61.teacherhelper.module.my.bean.GroupBean;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.net.request.interfa.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 会员园所认证申请提交
 *
 * @author shubei
 * @time 2019/1/21 17:43
 */
public class GroupVipApplyActivity extends BaseActivity {

    private static final int REQ_CHOOSE_GROUP = 1;

    private TextView tv_groupp_name_value, tv_groupp_addr_big_value, tv_groupp_addr_value;
    private EditText et_groupp_master_name, et_groupp_mobile;

    private int groupId;//所选幼儿园id
    private String kindergartenName, kindergartenMobile;


    @Override
    public void setLayout() {
        setContentView(R.layout.activity_group_vip_apply);
    }

    @Override
    public void initView() {
        setPagTitle("园所认证");
        tv_groupp_name_value = findViewById(R.id.tv_groupp_name_value);
        tv_groupp_addr_big_value = findViewById(R.id.tv_groupp_addr_big_value);
        tv_groupp_addr_value = findViewById(R.id.tv_groupp_addr_value);
        et_groupp_master_name = findViewById(R.id.et_groupp_master_name);
        et_groupp_mobile = findViewById(R.id.et_groupp_mobile);
    }

    @Override
    public void initData() {
        asyncGetMyAddress();
    }

    @Override
    public void initListener() {
        ((TextView) findViewById(R.id.tv_area_right)).setText("认证说明");
        area_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, VipAuthIntroActivity.class));
            }
        });
        findViewById(R.id.group_name_area).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ChooseGroupListActivity.class);
                it.putExtra("from", "register");
                startActivityForResult(it, REQ_CHOOSE_GROUP);
            }
        });
        findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupId <= 0) {
                    showShortToast("请选择幼儿园！");
                    return;
                }
                kindergartenName = et_groupp_master_name.getText().toString().trim();
                if (TextUtils.isEmpty(kindergartenName)) {
                    showShortToast("请填写园长姓名！");
                    return;
                }
                kindergartenMobile = et_groupp_mobile.getText().toString().trim();
                if (TextUtils.isEmpty(kindergartenMobile)) {
                    showShortToast("请填写联系电话！");
                    return;
                }
                asyncApply();
            }
        });
    }

    /**
     * 提交
     */
    private void asyncApply() {
        String wholeUrl = AppUrl.host + AppUrl.memberGroupAuthenticate;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupId", groupId);
        map.put("kindergartenName", kindergartenName);
        map.put("kindergartenMobile", kindergartenMobile);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, eLsner);
    }

    BaseRequestListener eLsner = new StringRequestListener() {

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
        public void onSuccess(int requestId, String url, String result) {
            dismissDialog();
            JSONObject jsonResponse;
            try {
                jsonResponse = new JSONObject(result);
                String code = jsonResponse.optString("code");
                String msg = jsonResponse.optString("msg");
                if (code.equals("0") || code.equals("0000044013")) {
                    dDialog.showDialog("提示", msg, "取消", "确认",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dDialog.dismissDialog();
                                    finish();
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dDialog.dismissDialog();
                                    finish();
                                }
                            });
                } else {
                    showShortToast(msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                showShortToast(getString(R.string.net_request_error));
            }
        }
    };

    /**
     * 查询用户所在幼儿园信息
     */
    private void asyncGetMyAddress() {
        String wholeUrl = AppUrl.host + AppUrl.findTeachMemberByUserId;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, pLsner);
    }

    BaseRequestListener pLsner = new JsonRequestListener() {

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
            GroupBean groupBean = gson.fromJson(jsonResult.toString(), GroupBean.class);
            groupId = groupBean.getId();
            tv_groupp_name_value.setText(groupBean.getSchoolName());
            tv_groupp_addr_big_value.setText(StringUtils.getStr(groupBean.getProvince()) + StringUtils.getStr(groupBean.getCity()) + StringUtils.getStr(groupBean.getCounty()));
            tv_groupp_addr_value.setText(groupBean.getStreetAddress());
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CHOOSE_GROUP) {
            if (resultCode == RESULT_OK) {
                groupId = data.getIntExtra("groupId", -1);
                tv_groupp_name_value.setText(data.getStringExtra("groupName"));
                tv_groupp_addr_big_value.setText(data.getStringExtra("groupBigAddr"));
                tv_groupp_addr_value.setText(data.getStringExtra("groupDetailAddr"));
            }
        }
    }
}
