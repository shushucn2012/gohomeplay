package com.park61.teacherhelper.module.my;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.ExitAppUtils;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.module.login.bean.UserBean;
import com.park61.teacherhelper.module.my.bean.AddressBean;
import com.park61.teacherhelper.module.my.bean.DutyBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.address.BottomDialog;
import com.park61.teacherhelper.widget.address.DataProvider;
import com.park61.teacherhelper.widget.address.ISelectAble;
import com.park61.teacherhelper.widget.address.SelectedListener;
import com.park61.teacherhelper.widget.address.Selector;
import com.park61.teacherhelper.widget.dialog.ComSelectDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubei on 2017/10/11.
 */

public class AddGroupRegActivity extends BaseActivity {

    private EditText et_group_name_value, et_groupp_addr_value;
    private TextView tv_groupp_addr_big_value;
    private View group_addr_big_area;
    private Button btn_commit;

    private String schoolName, address, province, city, county;
    private DataProvider.DataReceiver currentReceiver = null;
    private long pId, cityId, countryId;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_add_group_reg);
    }

    @Override
    public void initView() {
        et_group_name_value = (EditText) findViewById(R.id.et_group_name_value);
        tv_groupp_addr_big_value = (TextView) findViewById(R.id.tv_groupp_addr_big_value);
        et_groupp_addr_value = (EditText) findViewById(R.id.et_groupp_addr_value);
        group_addr_big_area = findViewById(R.id.group_addr_big_area);
        btn_commit = (Button) findViewById(R.id.btn_commit);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        group_addr_big_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //asyncGetProvince();
                showAddressDialog();
            }
        });

        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schoolName = et_group_name_value.getText().toString().trim();
                if (TextUtils.isEmpty(schoolName)) {
                    showShortToast("幼儿园名称未填写！");
                    return;
                }

                if (countryId == 0) {
                    showShortToast("幼儿园所在地区未选择！");
                    return;
                }

                address = et_groupp_addr_value.getText().toString().trim();
                if (TextUtils.isEmpty(address)) {
                    showShortToast("幼儿园地址未填写！");
                    return;
                }
                asyncAddGroup();
            }
        });
    }

    private void asyncAddGroup() {
        String wholeUrl = AppUrl.host + AppUrl.createGroup;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("schoolName", schoolName);
        map.put("address", province + city + county + address);
        map.put("provinceId", pId);
        map.put("province", province);
        map.put("cityId", cityId);
        map.put("city", city);
        map.put("countyId", countryId);
        map.put("county", county);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, eLsner);
    }

    BaseRequestListener eLsner = new JsonRequestListener() {

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
            Intent returnData = new Intent();
            returnData.putExtra("groupName", schoolName);
            returnData.putExtra("groupId", FU.paseInt(jsonResult.optString("data")));

            //会员申请提报时幼儿园选择需要详细地址
            returnData.putExtra("groupBigAddr", province + city + county);
            returnData.putExtra("groupDetailAddr", address);

            setResult(RESULT_OK, returnData);
            finish();
        }
    };

    private void showAddressDialog() {
        Selector selector = new Selector(mContext, 3);
        BottomDialog dialog = new BottomDialog(mContext);

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
                province = selectAbles.get(0).getName();
                city = selectAbles.get(1).getName();
                county = selectAbles.get(2).getName();

                countryId = selectAbles.get(2).getId();
                dialog.dismiss();
                //设置选择的省市区
                tv_groupp_addr_big_value.setText(result);
            }
        });
        dialog.init(mContext, selector);
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
