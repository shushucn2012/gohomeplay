package com.park61.teacherhelper.module.workplan;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.park61.teacherhelper.module.my.AddGroupRegActivity;
import com.park61.teacherhelper.module.my.ChooseClassActivity;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分配任务模板给幼儿园
 * Created by shubei on 2018/7/30
 */

public class ChooseGroupAllocateActivity extends BaseActivity {

    private static final int REQ_ADD_GROUP = 1;

    private static final int PAGE_SIZE = 7;

    private int taskCalendarTemplateId;//模板id
    private int PAGE_NUM = 1;
    private List<GroupBean> groupList;
    private List<AddressBean> countryList = new ArrayList<>();
    private long pId, cityId, countyId;
    private String schoolName = "";
    private DataProvider.DataReceiver currentReceiver = null;

    private PullToRefreshListView mPullRefreshListView;
    private GroupListAdapter adapter;
    private TextView tv_cur_country;
    private View area_country, area_add_now;
    private EditText edit_sousuo;
    private Button btn_to_create;
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
        btn_to_create = (Button) findViewById(R.id.btn_to_create);
        img_del = (ImageView) findViewById(R.id.img_del);
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.plv_group);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, mContext);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                asyncGetGroupList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGetGroupList();
            }
        });

        area_add_now = findViewById(R.id.area_add_now);
        area_add_now.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        setPagTitle("园所选择");
        taskCalendarTemplateId = getIntent().getIntExtra("taskCalendarTemplateId", -1);
        groupList = new ArrayList<>();
        adapter = new GroupListAdapter(mContext, groupList);
        mPullRefreshListView.setAdapter(adapter);
        asyncGetGroupList();
    }

    @Override
    public void initListener() {
        area_right.setVisibility(View.GONE);//隐藏手动添加
        edit_sousuo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    schoolName = edit_sousuo.getText().toString().trim();
                    PAGE_NUM = 1;
                    asyncGetGroupList();
                }
                return false;
            }
        });
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //asyncUpdateGroup(groupList.get(position - 1).getId());
                /*Intent returnData = new Intent();
                returnData.putExtra("groupName", groupList.get(position - 1).getSchoolName());
                returnData.putExtra("groupId", groupList.get(position - 1).getId());
                setResult(RESULT_OK, returnData);
                finish();*/
                Intent intent = new Intent(mContext, ChooseClassAllocateActivity.class);
                intent.putExtra("taskCalendarTemplateId", taskCalendarTemplateId);
                intent.putExtra("groupName", groupList.get(position - 1).getSchoolName());
                intent.putExtra("teachGroupId", groupList.get(position - 1).getId());
                startActivity(intent);
            }
        });
        area_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*List<String> slist = new ArrayList<String>();
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
       /* btn_to_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, AddGroupRegActivity.class);
                startActivityForResult(it, REQ_ADD_GROUP);
            }
        });*/
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

    private void asyncGetGroupList() {
        String wholeUrl = AppUrl.host + AppUrl.groupList;
        Map<String, Object> map = new HashMap<String, Object>();
        if (cityId != 0) {
            map.put("cityId", cityId);
        }
        if (countyId != 0) {
            map.put("countyId", countyId);
        }
        if (!TextUtils.isEmpty(schoolName)) {
            map.put("schoolName", schoolName);
        }
        map.put("curPage", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, eLsner);
    }

    BaseRequestListener eLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            if (PAGE_NUM == 1) {
                showDialog();
            }
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            showShortToast(errorMsg);
            if (PAGE_NUM > 1) {//翻页出错回滚
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            ViewInitTool.listStopLoadView(mPullRefreshListView);
            tv_cur_country.setText(jsonResult.optString("countyName"));
            ArrayList<GroupBean> currentPageList = new ArrayList<>();
            JSONArray actJay = jsonResult.optJSONArray("groupList");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
                groupList.clear();
                adapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyByDefaultTipPic(mContext, mPullRefreshListView.getRefreshableView());
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);

                JSONArray countryJay = jsonResult.optJSONArray("countyList");
                if (countryJay != null && countryJay.length() > 0) {
                    countryList.clear();
                    for (int i = 0; i < countryJay.length(); i++) {
                        AddressBean addressBean = gson.fromJson(countryJay.optJSONObject(i).toString(), AddressBean.class);
                        countryList.add(addressBean);
                    }
                   /* if (TextUtils.isEmpty(tv_cur_country.getText().toString())) {
                        tv_cur_country.setText(countryList.get(0).getName());
                    }*/
                }
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                groupList.clear();
                ViewInitTool.setPullToRefreshViewBoth(mPullRefreshListView);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (groupList.size() + actJay.length() >= jsonResult.optInt("totalSize")) {
                ViewInitTool.setPullToRefreshViewEnd(mPullRefreshListView);
            }
            for (int i = 0; i < actJay.length(); i++) {
                currentPageList.add(gson.fromJson(actJay.optJSONObject(i).toString(), GroupBean.class));
            }
            groupList.addAll(currentPageList);
            adapter.notifyDataSetChanged();

            JSONArray countryJay = jsonResult.optJSONArray("countyList");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (countryJay != null && countryJay.length() > 0) {
                countryList.clear();
                for (int i = 0; i < countryJay.length(); i++) {
                    AddressBean addressBean = gson.fromJson(countryJay.optJSONObject(i).toString(), AddressBean.class);
                    countryList.add(addressBean);
                   /* if (addressBean.getId() == groupList.get(0).getCountyId()) {
                        tv_cur_country.setText(addressBean.getName());
                    }*/
                }
            }
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
                countyId = selectAbles.get(2).getId();
                dialog.dismiss();
                //设置选择的省市区
                tv_cur_country.setText(selectAbles.get(2).getName());
                PAGE_NUM = 1;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ADD_GROUP) {
            if (resultCode == RESULT_OK) {
                String groupName = data.getStringExtra("groupName");
                int groupId = data.getIntExtra("groupId", -1);
                Intent returnData = new Intent();
                returnData.putExtra("groupName", groupName);
                returnData.putExtra("groupId", groupId);
                setResult(RESULT_OK, returnData);
                finish();
            }
        }
    }
}
