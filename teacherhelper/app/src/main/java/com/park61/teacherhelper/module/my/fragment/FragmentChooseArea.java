package com.park61.teacherhelper.module.my.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.databinding.FragmentChooseAreaBinding;
import com.park61.teacherhelper.module.my.AgentInforActivity;
import com.park61.teacherhelper.module.my.KidGardenActivity;
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
 * Created by chenlie on 2018/5/7.
 */

public class FragmentChooseArea extends BaseFragment {

    FragmentChooseAreaBinding binding;
    DataProvider.DataReceiver currentReceiver = null;
    long pId, cityId, countryId;
    private int couponActivityId;//优惠券id
    private int retryCountMax = 3;//最大领取重试次数

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_area, container, false);
        parentView = binding.getRoot();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        asyncDefaultAddress();
    }

    @Override
    public void initListener() {
        binding.agentArea.setOnClickListener(v -> {
            //地址列表选择
            showAddressDialog();
        });

        binding.areaName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.goNext.setBackgroundResource(R.drawable.shape_corner_100f4);
                } else {
                    binding.goNext.setBackgroundResource(R.drawable.shape_corner_100c9);
                }
            }
        });

        binding.goNext.setOnClickListener(v -> {
            if (binding.areaName.getText().toString().length() > 0) {
                // 提交代理商信息去下一页
                submitAgentInfo();
            }
        });
    }


    /**
     * "data": {
     * "city": "武汉市",
     * "cityId": 205,
     * "county": "武昌区",
     * "countyId": 1940,
     * "province": "湖北省",
     * "provinceId": 18}
     * },
     */
    private void asyncDefaultAddress() {
        String url = AppUrl.host + AppUrl.getDefaultAddress;
        String requestData = ParamBuild.buildParams(new HashMap<String, Object>());
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, defaultListener);
    }

    BaseRequestListener defaultListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject j) {
            dismissDialog();
            cityId = j.optInt("cityId");
            pId = j.optInt("provinceId");
            binding.areaName.setText(j.optString("province") + " " + j.optString("city") + " " + j.optString("county"));
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
        }
    };

    private void showAddressDialog() {
        Selector selector = new Selector(parentActivity, 3);
        BottomDialog dialog = new BottomDialog(parentActivity);

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
                //设置选择的省市区
                binding.areaName.setText(result);
            }
        });
        dialog.init(parentActivity, selector);
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
     * 提交代理商 昵称和区域
     */
    private void submitAgentInfo() {
        String url = AppUrl.host + AppUrl.submitAgent;
        Map<String, Object> map = new HashMap<>();
        map.put("cityId", cityId);
        map.put("name", ((AgentInforActivity) getActivity()).getNickName());
        map.put("provinceId", pId);
        map.put("userDuty", ((AgentInforActivity) getActivity()).getUserDuty());
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, submitListener);
    }

    BaseRequestListener submitListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            //返回优惠券活动id
            couponActivityId = jsonResult.optInt("couponActivityId");
            int status = jsonResult.optInt("status");
            //关闭引导页面
            getActivity().sendBroadcast(new Intent("RECEIVE_SUCCESS"));

            if (status == 0) {
                getActivity().finish();
            } else {
                ((AgentInforActivity) getActivity()).setCouponActivityId(couponActivityId);
                //领取优惠券
                receiveCoupon(couponActivityId);
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

    private void receiveCoupon(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("couponActivityId", id);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(AppUrl.receiveCoupon, Request.Method.POST, requestData, 0, receiceListener);
    }

    BaseRequestListener receiceListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            ((AgentInforActivity) getActivity()).goNext();
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            if ("0000042007".equals(errorCode)) {//优惠券已领，成功
                ((AgentInforActivity) getActivity()).goNext();
            } else if ("0000042010".equals(errorCode)) {//已领完，或者活动失败
                AlertDialog.Builder d = new AlertDialog.Builder(parentActivity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                d.setMessage("活动已结束，优惠券领完啦~");
                d.setPositiveButton("确定", (dialog, which) -> {
                    dialog.dismiss();
                    getActivity().finish();
                });
                d.setCancelable(false);
                d.create();
                d.show();
            } else {
                if (retryCountMax > 0) {
                    AlertDialog.Builder d = new AlertDialog.Builder(parentActivity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    d.setMessage("奖励领取失败，请重试");
                    d.setNegativeButton("放弃领取", (dialog, which) -> {
                        dialog.dismiss();
                        getActivity().finish();
                    });
                    d.setPositiveButton("重试", (dialog, which) -> {
                        dialog.dismiss();
                        receiveCoupon(couponActivityId);
                    });
                    d.setCancelable(false);
                    d.create();
                    d.show();
                    retryCountMax--;
                } else {
                    showShortToast("奖励领取失败");
                    getActivity().finish();
                }
            }
        }
    };
}
