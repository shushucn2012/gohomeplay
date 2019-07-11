package com.park61.teacherhelper.module.home;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.databinding.ActivityServiceApplyBinding;
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
 * Created by chenlie on 2018/3/9.
 *
 * 首页 服务申请 入口
 */

public class ServiceApplyActivity extends BaseActivity {

    ActivityServiceApplyBinding bind;
    LinearLayout areaRight;
    private long clickTime = 0;
    DataProvider.DataReceiver currentReceiver = null;
    long pId,cityId,countryId;
    String currentBatchCode;
    int batchId;

    @Override
    public void setLayout() {
        bind = DataBindingUtil.setContentView(this, R.layout.activity_service_apply);
    }

    @Override
    public void initView() {
        //标题，右边按钮
        setPagTitle(getString(R.string.service_apply_text));
        areaRight = (LinearLayout) bind.getRoot().findViewById(R.id.area_right);
        ((TextView)bind.getRoot().findViewById(R.id.tv_area_right)).setText("我的服务");

        int screenWidth = DevAttr.getScreenWidth(mContext) - DevAttr.dip2px(mContext, 20);
        ViewGroup.LayoutParams lp = bind.serverImg.getLayoutParams();
        lp.width = screenWidth;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        bind.serverImg.setLayoutParams(lp);
        bind.serverImg.setMaxWidth(screenWidth);
        bind.serverImg.setMaxHeight(screenWidth * 2);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        areaRight.setOnClickListener(v -> {
            //跳转我的服务
            startActivity(new Intent(mContext, MyServiceListActivity.class));
        });
        bind.serverTv.setEnabled(false);
        bind.inputNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(" ")) {
                    showShortToast("禁止输入空格");
                    String[] str = s.toString().split(" ");
                    String str1 = "";
                    for (int i = 0; i < str.length; i++) {
                        str1 += str[i];
                    }
                    bind.inputNo.setText(str1);
                    bind.inputNo.setSelection(start);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    bind.serverTv.setTextColor(Color.parseColor("#ffffff"));
                    bind.serverTv.setEnabled(true);
                }else{
                    bind.serverTv.setTextColor(Color.parseColor("#dadada"));
                    bind.serverTv.setEnabled(false);
                }
            }
        });
        bind.contactName.setOnEditorActionListener((v, actionId, event) -> event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
        bind.contactName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 30){
                    showShortToast("最多输入30个字符");
                    bind.contactName.setText(s.subSequence(0, 30));
                    bind.contactName.setSelection(start);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        bind.contactPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(" ")) {
                    showShortToast("禁止输入空格");
                    String[] str = s.toString().split(" ");
                    String str1 = "";
                    for (int i = 0; i < str.length; i++) {
                        str1 += str[i];
                    }
                    bind.contactPhone.setText(str1);
                    bind.contactPhone.setSelection(start);
                }else if(s.length() > 11){
                    showShortToast("最多输入11位");
                    bind.contactPhone.setText(s.subSequence(0, 11));
                    bind.contactPhone.setSelection(start);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        bind.contactSchool.setOnEditorActionListener((v, actionId, event) -> event != null &&  event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
        bind.contactSchool.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 30){
                    showShortToast("最多输入30个字符");
                    bind.contactSchool.setText(s.subSequence(0, 30));
                    bind.contactSchool.setSelection(start);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        bind.setSelectClick(v -> {
            //查询权益
            if(System.currentTimeMillis() - clickTime > 1000){
                selectService(bind.inputNo.getText().toString());
                clickTime = System.currentTimeMillis();
            }
        });

        bind.contactSite.setOnClickListener(v -> showAddressDialog());

        bind.setApplyClick(v -> {
            //申请权益
            if(checkInput()){
                applyService();
            }
        });
        bind.tvReportloss.setOnClickListener(v -> startActivity(new Intent(mContext, ReportLossOfServiceActivity.class)));
    }

    /**
     *   "batchCode": "SD123ACD",
     "description": "服务已完成。",
     "id": 1,
     "rightsUrl": "http://park61.oss-cn-zhangjiakou.aliyuncs.com/teach/20180302102745167_153.jpg",
     "status": 0,   0，待受理，1，已受理，2，已完成，3,已驳回
     "statusName": "已完成"
     */
    private void selectService(String batchCode){
        String url = AppUrl.host + AppUrl.selectMyService;
        Map<String,Object> map = new HashMap<>();
        map.put("batchCode", batchCode);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, sListener);
    }

    BaseRequestListener sListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            bind.areaMissing.setVisibility(View.GONE);
            bind.areaNull.setVisibility(View.GONE);
            bind.content.setVisibility(View.VISIBLE);
            dismissDialog();
            batchId = jsonResult.optInt("id");
            currentBatchCode = jsonResult.optString("batchCode");
            ImageManager.getInstance().displayImg(bind.serverImg, jsonResult.optString("rightsUrl"));
            int status = jsonResult.optInt("status");
            if(status == 0){
                //待受理
                bind.stateArea.setVisibility(View.GONE);
                bind.inputInfoArea.setVisibility(View.VISIBLE);
                bind.applyBt.setVisibility(View.VISIBLE);
            }else{
                //显示服务状态
                bind.stateArea.setVisibility(View.VISIBLE);
                bind.inputInfoArea.setVisibility(View.GONE);
                bind.applyBt.setVisibility(View.GONE);
                bind.serverState.setText(jsonResult.optString("statusName"));
                bind.serverMsg.setText(jsonResult.optString("description"));
            }
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            //显示空页面
            bind.areaMissing.setVisibility(View.GONE);
            bind.areaNull.setVisibility(View.VISIBLE);
            bind.content.setVisibility(View.GONE);
        }
    };

    private boolean checkInput(){
        if(TextUtils.isEmpty(bind.contactName.getText().toString())){
            showShortToast("请输入联系人");
            return false;
        }
        if(TextUtils.isEmpty(bind.contactPhone.getText().toString())){
            showShortToast("请输入联系电话");
            return false;
        }
        if(TextUtils.isEmpty(bind.contactSchool.getText().toString())){
            showShortToast("请输入园所");
            return false;
        }
        if(TextUtils.isEmpty(bind.contactSite.getText())){
            showShortToast("请选择地址");
            return false;
        }
        return true;
    }

    private void applyService(){
        String url = AppUrl.host + AppUrl.applyMyService;
        Map<String,Object> map = new HashMap<>();
        map.put("applyMobile", bind.contactPhone.getText().toString());
        map.put("applyName", bind.contactName.getText().toString());
        map.put("batchId", batchId);
        map.put("provinceId", pId);
        map.put("cityId", cityId);
        map.put("countyId", countryId);
        map.put("schoolName", bind.contactSchool.getText().toString());
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, aListener);
    }

    BaseRequestListener aListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            showShortToast("申请成功");
            //清空之前的信息
            bind.contactName.setText("");
            bind.contactPhone.setText("");
            bind.contactSchool.setText("");
            bind.contactSite.setText("");
            selectService(currentBatchCode);
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }
    };

    private void showAddressDialog(){
        Selector selector = new Selector(this, 3);
        BottomDialog dialog = new BottomDialog(this);

        selector.setDataProvider(new DataProvider() {
            @Override
            public void provideData(int currentDeep, long preId, String preName, DataReceiver receiver) {
                //根据tab的深度和前一项选择的id，获取下一级菜单项
                Log.i("address", "currentDeep >>> "+currentDeep+" preId >>> "+preId);
                currentReceiver = receiver;
                if(currentDeep == 0){
                    asyncGetProvince();
                }else if(currentDeep == 1){
                    pId = preId;
                    asyncGetCity(preId);
                }else if(currentDeep == 2){
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
                    result += selectAble.getName()+" ";
                }
                countryId = selectAbles.get(2).getId();
                dialog.dismiss();
                bind.contactSite.setText(result);
            }
        });
        dialog.init(this,selector);
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
