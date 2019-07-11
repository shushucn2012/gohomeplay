package com.park61.teacherhelper.module.clazz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.dmcbig.mediapicker.entity.Media;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.CanBackWebViewActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.RegexValidator;
import com.park61.teacherhelper.databinding.ActivityParkSubmitBinding;
import com.park61.teacherhelper.module.clazz.adapter.MediaInfoAdapter;
import com.park61.teacherhelper.module.clazz.bean.ApplyGardenItem;
import com.park61.teacherhelper.module.clazz.bean.IconBean;
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

import static com.park61.teacherhelper.common.tool.ViewInitTool.addJudgeBtnEnableListener;

/**
 * Created by chenlie on 2018/5/17.
 * <p>
 * 基地园申请页面
 */

public class ParkSubmitActivity extends BaseActivity {

    private static final int TO_PUBLISH = 11;
    ActivityParkSubmitBinding bd;
    //园所类型:0明星;1:基地园
    private int parkType;
    private long pId, cityId, areaId;
    private int applyId;
    private String name, address, detailAddress, contact, phone;

    private DataProvider.DataReceiver currentReceiver = null;
    private List<IconBean> list;
    private MediaInfoAdapter mAdapter;
    private ArrayList<Media> select;
    private int chosenPos = -1;//当前点击项

    @Override
    public void setLayout() {
        bd = DataBindingUtil.setContentView(this, R.layout.activity_park_submit);
    }

    @Override
    public void initView() {
        registerRefreshReceiver();
        setPagTitle("提交申请");
        bd.setIsStarPark(true);
    }

    @Override
    public void initData() {
        applyId = getIntent().getIntExtra("applyId", -1);
        list = new ArrayList<>();
        mAdapter = new MediaInfoAdapter(this, list);
        bd.gridInfo.setAdapter(mAdapter);
        bd.gridInfo.setSelector(new ColorDrawable(Color.TRANSPARENT));
        if (applyId != -1) {//已经提交了基础信息
            //获取基础信息
            asyncParkBase();
            bd.bottomBtSaveBase.setVisibility(View.GONE);//隐藏保存按钮，显示提交按钮
            bd.bottomBtAllComt.setVisibility(View.VISIBLE);
            bd.bottomBtAllComt.setEnabled(false);
            bd.bottomBtAllComt.setBackgroundColor(getResources().getColor(R.color.gc9bec2));
        }
    }

    @Override
    public void initListener() {
        bd.setDescribeClick(v -> asyncDesUrl());
        bd.starPark.setOnClickListener(v -> {
            parkType = 0;
            bd.setIsStarPark(true);
        });
        bd.basePark.setOnClickListener(v -> {
            parkType = 1;
            bd.setIsStarPark(false);
        });
        bd.bottomBtSaveBase.setOnClickListener(v -> {
            validateAndSubmitBaseInfo();
        });
        //监听输入框判断按钮是否可点击
        List<EditText> etList = new ArrayList<>();
        etList.add(bd.schoolName);
        etList.add(bd.schoolDetailAddress);
        etList.add(bd.schoolContact);
        etList.add(bd.schoolContactPhone);
        addJudgeBtnEnableListener(etList, bd.bottomBtSaveBase,
                R.color.gffffff, R.color.gffffff, R.color.gc9bec2, R.color.color_text_red_deep, mContext);

        bd.schoolAddress.setOnClickListener(v -> showAddressDialog());

        bd.gridInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击某一项
                chosenPos = position;
                if (list.get(position).getIsFinished() == 1) {//已经完成去详情
                    asyncGetGardenItem();
                } else {//没有完成去选择
                    Intent it = new Intent(mContext, ChoosePublishTypeActivity.class);
                    it.putExtra("title", list.get(chosenPos).getName());
                    it.putExtra("applyId", applyId);
                    it.putExtra("classifyId", list.get(chosenPos).getId());
                    it.putExtra("id", list.get(chosenPos).getItemId());
                    startActivity(it);
                }
            }
        });

        bd.bottomBtAllComt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSubmitBaseInfo();
            }
        });
    }

    /**
     * 根据申请id获取基础信息
     */
    private void asyncParkBase() {
        String url = AppUrl.host + AppUrl.getParkBase;
        Map<String, Object> map = new HashMap<>();
        map.put("id", applyId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, getBaseListener);
    }

    BaseRequestListener getBaseListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject j) {
            dismissDialog();
            //设置信息,园所类型
            parkType = j.optInt("type");
            bd.setIsStarPark(parkType == 0);
            //园所名称
            bd.schoolName.setText(j.optString("schoolName"));
            //省市区
            pId = j.optInt("provinceId");
            cityId = j.optInt("cityId");
            areaId = j.optInt("countyId");
            bd.schoolAddress.setText(j.optString("provinceName") + j.optString("cityName") + j.optString("countyName"));
            bd.schoolDetailAddress.setText(j.optString("addressDetail"));
            //联系人
            bd.schoolContact.setText(j.optString("applyName"));
            bd.schoolContactPhone.setText(j.optString("applyMobile"));
            //请求完基础信息后，请求各资质信息
            asyncMediaItem();
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

    /**
     * 填写说明url
     */
    private void asyncDesUrl() {
        String url = AppUrl.host + AppUrl.findNoteUrl;
        String requestData = ParamBuild.buildParams(new HashMap<String, Object>());
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, desUrlListener);
    }

    BaseRequestListener desUrlListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            String desUrl = jsonResult.optString("data");
            if (!TextUtils.isEmpty(desUrl)) {
                Intent intent = new Intent(mContext, CanBackWebViewActivity.class);
                intent.putExtra("url", desUrl);
                startActivity(intent);
            }
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


    /**
     * 验证数据并提交基础信息
     */
    private void validateAndSubmitBaseInfo() {
        name = bd.schoolName.getText().toString();
        address = bd.schoolAddress.getText().toString();
        detailAddress = bd.schoolDetailAddress.getText().toString();
        contact = bd.schoolContact.getText().toString();
        phone = bd.schoolContactPhone.getText().toString();
        if (TextUtils.isEmpty(name)) {
            showShortToast("请补充园所基础信息，园所名称未填写");
            return;
        }
        if (TextUtils.isEmpty(address) || TextUtils.isEmpty(detailAddress)) {
            showShortToast("请补充园所基础信息，园所地址未填写");
            return;
        }
        if (TextUtils.isEmpty(contact)) {
            showShortToast("请补充园所基础信息，园所联系人未填写");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            showShortToast("请补充园所基础信息，园所联系电话未填写");
            return;
        }
        if (!RegexValidator.isMobile(phone)) {
            showShortToast("手机号输入有误！");
            return;
        }
        asyncSubmitBaseInfo();
    }

    /**
     * 向服务器提交基础信息
     */
    private void asyncSubmitBaseInfo() {
        String url = AppUrl.host + AppUrl.submitParkBase;
        HashMap<String, Object> map = new HashMap<>();
        map.put("addressDetail", detailAddress);
        map.put("applyMobile", phone);
        map.put("applyName", contact);
        map.put("cityId", cityId);
        map.put("countyId", areaId);
        map.put("provinceId", pId);
        map.put("schoolName", name);
        map.put("type", parkType);
        if (applyId != -1) {
            map.put("id", applyId);
        }
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, baseListener);
    }

    BaseRequestListener baseListener = new JsonRequestListener() {

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
            if (applyId <= 0) {//首次提交基础信息
                //返回申请id
                applyId = jsonResult.optInt("data");
                //改变底部按钮为提交申请
                bd.bottomBtSaveBase.setVisibility(View.GONE);//隐藏保存按钮，显示提交按钮
                bd.bottomBtAllComt.setVisibility(View.VISIBLE);
                bd.bottomBtAllComt.setEnabled(false);
                bd.bottomBtAllComt.setBackgroundColor(getResources().getColor(R.color.gc9bec2));

                //获取需要提交的非基础信息
                asyncMediaItem();
            } else {//点击立即提交，先提交基础信息再提交申请
                submitApply();
            }
        }
    };

    /**
     * 提交申请
     */
    private void submitApply() {
        String url = AppUrl.host + AppUrl.submitApply;
        HashMap<String, Object> map = new HashMap<>();
        map.put("applyId", applyId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, applyListener);
    }

    BaseRequestListener applyListener = new JsonRequestListener() {

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
            showShortToast("申请提交成功");
            finish();
        }
    };

    /**
     * 获取需要上传的图片和视频项
     */
    private void asyncMediaItem() {
        String url = AppUrl.host + AppUrl.infoIconList;
        Map<String, Object> map = new HashMap<>();
        map.put("applyId", applyId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, iconListener);
    }

    BaseRequestListener iconListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            boolean isAllFinish = true;//是否全部完成
            JSONArray arr = jsonResult.optJSONArray("list");
            if (arr != null && arr.length() > 0) {
                list.clear();
                for (int i = 0; i < arr.length(); i++) {
                    IconBean tmp = gson.fromJson(arr.optJSONObject(i).toString(), IconBean.class);
                    if (tmp.getIsFinished() == 0) {//有一项未完成就是未完成
                        isAllFinish = false;
                    }
                    list.add(tmp);
                }
                mAdapter.notifyDataSetChanged();
            } else {
                isAllFinish = false;
            }
            if (isAllFinish) {//全部完成后按钮可点击
                bd.bottomBtAllComt.setEnabled(true);
                bd.bottomBtAllComt.setBackgroundColor(getResources().getColor(R.color.color_text_red_deep));
            } else {
                bd.bottomBtAllComt.setEnabled(false);
                bd.bottomBtAllComt.setBackgroundColor(getResources().getColor(R.color.gc9bec2));
            }
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
                areaId = selectAbles.get(2).getId();
                dialog.dismiss();
                //设置选择的省市区
                bd.schoolAddress.setText(result);
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

    /**
     * 获取非基础信息
     */
    private void asyncGetGardenItem() {
        String wholeUrl = AppUrl.host + AppUrl.getGardenItem;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applyId", applyId);
        map.put("classifyId", list.get(chosenPos).getId());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, gListener);
    }

    BaseRequestListener gListener = new JsonRequestListener() {

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
            ApplyGardenItem item = gson.fromJson(jsonResult.toString(), ApplyGardenItem.class);
            if (item.getSourceType() == 0) {//资源类型:0图片,1音视频
                Intent intent = new Intent(mContext, PhotoPublishNewActivity.class);
                ArrayList<String> urls = new ArrayList<>();
                for (ApplyGardenItem.SourceListBean media : item.getSourceList()) {
                    urls.add(media.getSource());
                }
                intent.putExtra("selectPath", urls);
                intent.putExtra("title", list.get(chosenPos).getName());
                intent.putExtra("content", item.getDetail());
                intent.putExtra("applyId", applyId);
                intent.putExtra("classifyId", list.get(chosenPos).getId());
                intent.putExtra("id", list.get(chosenPos).getItemId());
                startActivityForResult(intent, TO_PUBLISH);
            } else {
                if (!CommonMethod.isListEmpty(item.getSourceList())) {
                    Intent intent = new Intent(mContext, FhVideoPublishActivity.class);
                    intent.putExtra("videoId", item.getSourceList().get(0).getSource());
                    intent.putExtra("videoImg", item.getSourceList().get(0).getVideoImg());
                    intent.putExtra("title", list.get(chosenPos).getName());
                    intent.putExtra("applyId", applyId);
                    intent.putExtra("classifyId", list.get(chosenPos).getId());
                    intent.putExtra("id", list.get(chosenPos).getItemId());
                    intent.putExtra("content", item.getDetail());
                    startActivityForResult(intent, TO_PUBLISH);
                }
            }
        }
    };

    /**
     * 其他页面要改变当前页的广播
     */
    private void registerRefreshReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_REFRESH_SUBMIT_ITEM");
        registerReceiver(submitChangedReceiver, filter);
    }

    private BroadcastReceiver submitChangedReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            asyncMediaItem();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(submitChangedReceiver);
    }
}
