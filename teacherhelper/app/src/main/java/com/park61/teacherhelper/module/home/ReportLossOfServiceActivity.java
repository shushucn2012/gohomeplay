package com.park61.teacherhelper.module.home;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.module.my.bean.AddressBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.address.BottomDialog;
import com.park61.teacherhelper.widget.address.DataProvider;
import com.park61.teacherhelper.widget.address.ISelectAble;
import com.park61.teacherhelper.widget.address.SelectedListener;
import com.park61.teacherhelper.widget.address.Selector;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubei on 2018/3/13.
 */

public class ReportLossOfServiceActivity extends BaseActivity {

    private TextView tv_status_tip, contact_site;
    private View bottom_bar;
    private EditText contact_name, contact_phone, contact_school, edit_remark;

    private DataProvider.DataReceiver currentReceiver;
    private long pId, cityId, countryId;
    private String linkName, linkMobile, schoolName, remark;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_reportloss_of_service);
    }

    @Override
    public void initView() {
        setPagTitle("挂失申请");
        tv_status_tip = (TextView) findViewById(R.id.tv_status_tip);
        contact_site = (TextView) findViewById(R.id.contact_site);
        bottom_bar = findViewById(R.id.bottom_bar);
        contact_name = (EditText) findViewById(R.id.contact_name);
        contact_phone = (EditText) findViewById(R.id.contact_phone);
        contact_school = (EditText) findViewById(R.id.contact_school);
        edit_remark = (EditText) findViewById(R.id.edit_remark);
    }

    @Override
    public void initData() {
        anyscGetReportTip();
    }

    @Override
    public void initListener() {
        contact_site.setOnClickListener(v -> showAddressDialog());
        bottom_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate())
                    return;
                anyscCommitReport();
            }
        });
        contact_name.setOnEditorActionListener((v, actionId, event) -> (event.getKeyCode() == KeyEvent.KEYCODE_ENTER));
        contact_school.setOnEditorActionListener((v, actionId, event) -> (event.getKeyCode() == KeyEvent.KEYCODE_ENTER));
    }

    /**
     * 验证输入
     */
    private boolean validate() {
        linkName = contact_name.getText().toString().trim();
        linkMobile = contact_phone.getText().toString().trim();
        schoolName = contact_school.getText().toString().trim();
        remark = edit_remark.getText().toString().trim();
        if (TextUtils.isEmpty(linkName)) {
            showShortToast("联系人未填写");
            return false;
        }
        if (TextUtils.isEmpty(linkMobile)) {
            showShortToast("联系电话未填写");
            return false;
        }
        if (TextUtils.isEmpty(schoolName)) {
            showShortToast("园所未填写");
            return false;
        }
        if (TextUtils.isEmpty(contact_site.getText().toString().trim())) {
            showShortToast("地址未填写");
            return false;
        }
        return true;
    }

    /**
     * 请求挂失提示
     */
    public void anyscGetReportTip() {
        String wholeUrl = AppUrl.host + AppUrl.serviceStatusDetail;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, tlisenter);
    }

    BaseRequestListener tlisenter = new JsonRequestListener() {

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
            tv_status_tip.setText(jsonResult.optString("data"));
        }
    };

    /**
     * 请求提交挂失
     */
    public void anyscCommitReport() {
        String wholeUrl = AppUrl.host + AppUrl.addLostRecord;
        Map<String, Object> map = new HashMap<>();
        map.put("linkName", linkName);
        map.put("linkMobile", linkMobile);
        map.put("schoolName", schoolName);
        if (!TextUtils.isEmpty(remark)) {//备注不为空的时候提交
            map.put("remark", remark);
        }
        map.put("provinceId", pId);
        map.put("cityId", cityId);
        map.put("countyId", countryId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestData, 0, clisenter);
    }

    BaseRequestListener clisenter = new JsonRequestListener() {

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
            //showShortToast("申请提交成功！服务人员会尽快与您联系确认");
            startActivity(new Intent(mContext, ReportLossSucActivity.class));
            finish();
        }
    };

    private void showAddressDialog() {
        Selector selector = new Selector(this, 3);
        BottomDialog dialog = new BottomDialog(this);

        selector.setDataProvider(new DataProvider() {
            @Override
            public void provideData(int currentDeep, long preId, String preName, DataReceiver receiver) {
                //根据tab的深度和前一项选择的id，获取下一级菜单项
                Log.i("address", "currentDeep >>> " + currentDeep + " preId >>> " + preId);
                currentReceiver = receiver;
                if (currentDeep == 0) {
                    asyncGetProvince();
                } else if (currentDeep == 1) {
                    pId = preId;
                    asyncGetCity(preId);
                } else if (currentDeep == 2) {
                    cityId = preId;
                    asyncGetArea(preId);
                }
            }
        });
        selector.setSelectedListener(new SelectedListener() {
            @Override
            public void onAddressSelected(ArrayList<ISelectAble> selectAbles) {
                String result = "";
                for (ISelectAble selectAble : selectAbles) {
                    result += selectAble.getName() + " ";
                }
                countryId = selectAbles.get(2).getId();
                dialog.dismiss();
                contact_site.setText(result);
            }
        });
        dialog.init(this, selector);
        dialog.show();
    }

    /**
     * 获取省份名
     */
    private void asyncGetProvince() {
        String wholeUrl = AppUrl.host + AppUrl.address;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parentId", 1);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, provinceListener);
    }

    BaseRequestListener provinceListener = new JsonRequestListener() {

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
            JSONArray jay = jsonResult.optJSONArray("list");
            List<ISelectAble> list = new ArrayList<>();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                AddressBean p = gson.fromJson(jot.toString(), AddressBean.class);
                list.add(new ISelectAble() {
                    @Override
                    public String getName() {
                        return p.getName();
                    }

                    @Override
                    public long getId() {
                        return p.getProvinceId();
                    }

                    @Override
                    public Object getArg() {
                        return this;
                    }
                });
            }
            currentReceiver.send(list);
        }
    };

    /**
     * 获取城市名
     */
    private void asyncGetCity(long provinceId) {
        String wholeUrl = AppUrl.host + AppUrl.address;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parentId", provinceId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, cityListener);
    }

    BaseRequestListener cityListener = new JsonRequestListener() {

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
            JSONArray jay = jsonResult.optJSONArray("list");
            List<ISelectAble> list = new ArrayList<>();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                AddressBean p = gson.fromJson(jot.toString(), AddressBean.class);
                list.add(new ISelectAble() {
                    @Override
                    public String getName() {
                        return p.getName();
                    }

                    @Override
                    public long getId() {
                        return p.getCityId();
                    }

                    @Override
                    public Object getArg() {
                        return this;
                    }
                });
            }
            currentReceiver.send(list);
        }
    };

    /**
     * 获取区域名
     */
    private void asyncGetArea(long cityId) {
        String wholeUrl = AppUrl.host + AppUrl.address;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parentId", cityId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, areaListener);
    }

    BaseRequestListener areaListener = new JsonRequestListener() {

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
            JSONArray jay = jsonResult.optJSONArray("list");
            List<ISelectAble> list = new ArrayList<>();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                AddressBean p = gson.fromJson(jot.toString(), AddressBean.class);
                list.add(new ISelectAble() {
                    @Override
                    public String getName() {
                        return p.getName();
                    }

                    @Override
                    public long getId() {
                        return p.getCountyId();
                    }

                    @Override
                    public Object getArg() {
                        return this;
                    }
                });
            }
            currentReceiver.send(list);
        }
    };
}
