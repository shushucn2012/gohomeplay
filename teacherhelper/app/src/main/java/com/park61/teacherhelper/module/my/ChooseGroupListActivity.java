package com.park61.teacherhelper.module.my;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.my.adapter.GroupListAdapter;
import com.park61.teacherhelper.module.my.bean.AddressBean;
import com.park61.teacherhelper.module.my.bean.GroupBean;
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

public class ChooseGroupListActivity extends BaseActivity {

    private static final int REQ_ADD_GROUP = 1;

    private static final int PAGE_SIZE = 20;

    private int PAGE_NUM = 0;
    private String from;//入口 register：注册完善信息进入；否则是个人中心进入
    private List<GroupBean> groupList;
    private List<AddressBean> countryList = new ArrayList<>();
    private long provinceId = -1, cityId = -1, countyId = -1;
    private String provinceName, cityName;
    private String schoolName = "";
    private DataProvider.DataReceiver currentReceiver = null;
    private int searchCount;//搜索次数，点击搜索的次数

    private PullToRefreshListView plv_group;
    private GroupListAdapter adapter;
    private TextView tv_cur_country;
    private View area_country;
    private EditText edit_sousuo;
    private ImageView img_del;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_choose_group_list);
    }

    @Override
    public void initView() {
        tv_cur_country = (TextView) findViewById(R.id.tv_cur_country);
        area_country = findViewById(R.id.area_country);
        edit_sousuo = (EditText) findViewById(R.id.edit_sousuo);
        img_del = (ImageView) findViewById(R.id.img_del);
        plv_group = (PullToRefreshListView) findViewById(R.id.plv_group);
        ViewInitTool.initPullToRefresh(plv_group, mContext);
        plv_group.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 0;
                asyncGetGroupList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetGroupList();
            }
        });
    }

    @Override
    public void initData() {
        from = getIntent().getStringExtra("from");
        groupList = new ArrayList<>();
        adapter = new GroupListAdapter(mContext, groupList);
        plv_group.setAdapter(adapter);

        asyncCityIdByPosition();
    }

    @Override
    public void initListener() {
        /*if ("register".equals(from)) {
            area_right.setVisibility(View.GONE);//隐藏手动添加
            findViewById(R.id.area_add_now).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_to_create).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, AddGroupActivity.class);
                    startActivityForResult(it, REQ_ADD_GROUP);
                }
            });
        }*/

        area_right.setVisibility(View.GONE);//隐藏手动添加
        findViewById(R.id.area_add_now).setVisibility(View.GONE);
        findViewById(R.id.btn_to_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if ("register".equals(from)) {
                Intent it = new Intent(mContext, AddGroupRegActivity.class);
                startActivityForResult(it, REQ_ADD_GROUP);
//                } else {
//                    Intent it = new Intent(mContext, AddGroupActivity.class);
//                    startActivityForResult(it, REQ_ADD_GROUP);
//                }
            }
        });
        edit_sousuo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    schoolName = edit_sousuo.getText().toString().trim();
                    PAGE_NUM = 0;
                    asyncGetGroupList();
                    searchCount++;
                }
                return false;
            }
        });
        plv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("register".equals(from)) {
                    Intent returnData = new Intent();
                    returnData.putExtra("groupName", groupList.get(position - 1).getSchoolName());
                    returnData.putExtra("groupId", groupList.get(position - 1).getId());

                    //会员申请提报时幼儿园选择需要详细地址
                    GroupBean groupBean = groupList.get(position - 1);
                    String groupBigAddr = groupBean.getProvince() + groupBean.getCity() + groupBean.getCounty();
                    returnData.putExtra("groupBigAddr", groupBigAddr);
                    returnData.putExtra("groupDetailAddr", groupBean.getStreetAddress());

                    setResult(RESULT_OK, returnData);
                    finish();
                } else {
                    asyncUpdateGroup(groupList.get(position - 1).getId());
                }
            }
        });
        area_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* List<String> slist = new ArrayList<String>();
                for (int i = 0; i < countryList.size(); i++) {
                    slist.add(countryList.get(i).getName());
                }
                ComSelectDialog mComSelectDialog = new ComSelectDialog(mContext, slist);
                mComSelectDialog.setOnItemSelectLsner(new ComSelectDialog.OnItemSelect() {
                    @Override
                    public void onSelect(int position) {
                        cityId = countryList.get(position).getParentId();
                        countyId = countryList.get(position).getId();
                        tv_cur_country.setText(countryList.get(position).getName());
                        PAGE_NUM = 1;
                        asyncGetGroupList();
                    }
                });
                mComSelectDialog.showDialog();*/

                showAddressDialog();
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
                    schoolName = "";
                    PAGE_NUM = 0;
                    asyncGetGroupList();
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
     * 通过定位信息获取当前城市id
     */
    private void asyncCityIdByPosition() {
        String wholeUrl = AppUrl.host + AppUrl.getCityIdByPosition;
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
            provinceId = 0;
            cityId = jsonResult.optInt("cityId");
            tv_cur_country.setText(jsonResult.optString("cityName"));
            asyncGetGroupList();
        }
    };

    private void asyncGetGroupList() {
        String wholeUrl = AppUrl.host + AppUrl.getTeachGroupList;
        Map<String, Object> map = new HashMap<String, Object>();
        if (provinceId != 0) {//省市区id三者最少一个不为空，当provinceId为-1时表示全国
            map.put("provinceId", provinceId);
        }

        if (cityId != -1) {
            map.put("cityId", cityId);
        }
        if (countyId != -1) {
            map.put("countyId", countyId);
        }
        if (!TextUtils.isEmpty(schoolName)) {
            map.put("schoolName", schoolName);
        }
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, eLsner);
    }

    BaseRequestListener eLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            if (PAGE_NUM == 0) {
                showDialog();
            }
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            ViewInitTool.listStopLoadView(plv_group);
            showShortToast(errorMsg);
            if (PAGE_NUM > 0) {//翻页出错回滚
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            ViewInitTool.listStopLoadView(plv_group);
            ArrayList<GroupBean> currentPageList = new ArrayList<>();
            JSONArray actJay = jsonResult.optJSONArray("rows");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 0 && (actJay == null || actJay.length() <= 0)) {
                groupList.clear();
                adapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyByDefaultTipPic(mContext, plv_group.getRefreshableView());
                ViewInitTool.setPullToRefreshViewEnd(plv_group);
                if (searchCount > 0) {//点击了搜索，但是没有数据，显示添加框框
                    findViewById(R.id.area_add_now).setVisibility(View.VISIBLE);
                }
                return;
            } else {//有数据，不显示添加框框
                if (searchCount > 0) {//点击了搜索，但是没有数据，显示添加框框
                    findViewById(R.id.area_add_now).setVisibility(View.VISIBLE);
                }
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 0) {
                groupList.clear();
                ViewInitTool.setPullToRefreshViewBoth(plv_group);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("pageCount") - 1) {
                ViewInitTool.setPullToRefreshViewEnd(plv_group);
            }
            for (int i = 0; i < actJay.length(); i++) {
                currentPageList.add(gson.fromJson(actJay.optJSONObject(i).toString(), GroupBean.class));
            }
            groupList.addAll(currentPageList);
            adapter.notifyDataSetChanged();
        }
    };

    private void asyncUpdateGroup(long groupId) {
        String wholeUrl = AppUrl.host + AppUrl.updateGroup;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupId", groupId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, uLsner);
    }

    BaseRequestListener uLsner = new JsonRequestListener() {

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
            showShortToast("绑定幼儿园成功！");
            setResult(RESULT_OK, new Intent());
            finish();
        }
    };

    private void showAddressDialog() {
        provinceId = -1;
        cityId = -1;
        countyId = -1;

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
                    provinceId = preId;
                    provinceName = preName;
                    asyncGetCity(preId);
                } else if (currentDeep == 2) {
                    cityId = preId;
                    cityName = preName;
                    asyncGetArea(preId);
                }
            }
        });
        selector.setSelectedListener(new SelectedListener() {
            @Override
            public void onAddressSelected(ArrayList<ISelectAble> selectAbles) {
                dialog.dismiss();
                if (provinceId == -1 && cityId == -1 && countyId == -1) {//选择全国
                    provinceId = selectAbles.get(0).getId();
                    tv_cur_country.setText(selectAbles.get(0).getName());
                } else if (provinceId != -1 && cityId == -1 && countyId == -1) {//选择全省
                    cityId = selectAbles.get(1).getId();
                    tv_cur_country.setText(selectAbles.get(1).getName().replace("全", ""));
                } else {
                    //设置选择的省市区：selectAbles第一项是省，第二项是市，第三项是区
                    countyId = selectAbles.get(2).getId();
                    tv_cur_country.setText(selectAbles.get(2).getName().replace("全", ""));
                }
                PAGE_NUM = 0;
                asyncGetGroupList();
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
            list.add(new ISelectAble() {
                @Override
                public String getName() {
                    return "全国";
                }

                @Override
                public long getId() {
                    return -1;
                }

                @Override
                public Object getArg() {
                    return this;
                }
            });
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
            list.add(new ISelectAble() {
                @Override
                public String getName() {
                    return "全" + provinceName;
                }

                @Override
                public long getId() {
                    return -1;
                }

                @Override
                public Object getArg() {
                    return this;
                }
            });
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
            list.add(new ISelectAble() {
                @Override
                public String getName() {
                    return "全" + cityName;
                }

                @Override
                public long getId() {
                    return -1;
                }

                @Override
                public Object getArg() {
                    return this;
                }
            });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ADD_GROUP) {
            if (resultCode == RESULT_OK) {
                if ("register".equals(from)) {
                    String groupName = data.getStringExtra("groupName");
                    int groupId = data.getIntExtra("groupId", -1);
                    Intent returnData = new Intent();
                    returnData.putExtra("groupName", groupName);
                    returnData.putExtra("groupId", groupId);

                    //会员申请提报时幼儿园选择需要详细地址
                    returnData.putExtra("groupBigAddr",data.getStringExtra("groupBigAddr"));
                    returnData.putExtra("groupDetailAddr", data.getStringExtra("groupDetailAddr"));

                    setResult(RESULT_OK, returnData);
                    finish();
                } else {
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
            }
        }
    }
}
