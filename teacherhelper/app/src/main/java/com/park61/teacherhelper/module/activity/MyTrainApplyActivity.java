package com.park61.teacherhelper.module.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.module.activity.adapter.GardenPropertyAdapter;
import com.park61.teacherhelper.module.activity.bean.GardenProperty;
import com.park61.teacherhelper.module.activity.bean.ServiceApplyBean;
import com.park61.teacherhelper.module.my.ChooseGroupListActivity;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.gridview.GridViewForScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新版培训提报页面
 *
 * @author shubei
 * @time 2019/3/7 16:43
 */
public class MyTrainApplyActivity extends BaseActivity {

    private static final int REQ_CHOOSE_GROUP = 1;

    private TextView tv_groupp_name_value, tv_groupp_addr_big_value, tv_groupp_addr_value, tv_groupmaster_name_label, tv_group_phone_label;
    private EditText et_groupp_master_name, et_groupp_mobile, edit_agentname, edit_agentmobile;
    private ImageView img_only_contactor, img_only_phone, img_choose_group_arrow;
    private View line_below_groupname;

    private int intensifiedTrainingId;//提报id，若传过来不为空，代表展示详情
    private ServiceApplyBean mServiceApplyBean;//当展示详情时，当前的提报实体对象

    private int groupId;//所选幼儿园id
    private String groupType;//所选园所性质id
    private String purchaseSuit;//所选套装，多个逗号连接
    private String kindergartenName, kindergartenMobile, agentName, agentMobile;
    private GridViewForScrollView gv_garden_property, gv_goods_suit;

    private List<GardenProperty> gardenPropertyList, goodsSuitList;
    private GardenPropertyAdapter mGardenPropertyAdapter, mGoodsSuitAdapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_my_service_apply);
    }

    @Override
    public void initView() {
        setPagTitle("申请培训");
        et_groupp_master_name = findViewById(R.id.et_groupp_master_name);
        et_groupp_mobile = findViewById(R.id.et_groupp_mobile);
        edit_agentname = findViewById(R.id.edit_agentname);
        edit_agentmobile = findViewById(R.id.edit_agentmobile);
        tv_groupp_name_value = findViewById(R.id.tv_groupp_name_value);
        tv_groupp_addr_big_value = findViewById(R.id.tv_groupp_addr_big_value);
        tv_groupp_addr_value = findViewById(R.id.tv_groupp_addr_value);
        gv_garden_property = findViewById(R.id.gv_garden_property);
        gv_goods_suit = findViewById(R.id.gv_goods_suit);
        img_only_contactor = findViewById(R.id.img_only_contactor);
        img_only_phone = findViewById(R.id.img_only_phone);
        line_below_groupname = findViewById(R.id.line_below_groupname);
        tv_groupmaster_name_label = findViewById(R.id.tv_groupmaster_name_label);
        tv_group_phone_label = findViewById(R.id.tv_group_phone_label);
        img_choose_group_arrow = findViewById(R.id.img_choose_group_arrow);
    }

    @Override
    public void initData() {
        intensifiedTrainingId = getIntent().getIntExtra("intensifiedTrainingId", -1);
        if (intensifiedTrainingId > 0) {
            setPagTitle("详情");
            img_only_contactor.setVisibility(View.INVISIBLE);
            img_only_phone.setVisibility(View.INVISIBLE);
            line_below_groupname.setVisibility(View.VISIBLE);
            et_groupp_master_name.setEnabled(false);
            et_groupp_mobile.setEnabled(false);
            edit_agentname.setEnabled(false);
            edit_agentmobile.setEnabled(false);
            tv_group_phone_label.setTextColor(getResources().getColor(R.color.g999999));
            tv_groupmaster_name_label.setTextColor(getResources().getColor(R.color.g999999));
            img_choose_group_arrow.setVisibility(View.GONE);

            //动态的对margin属性进行修改
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv_groupp_name_value.getLayoutParams();
            layoutParams.rightMargin = DevAttr.dip2px(mContext, 15);
            tv_groupp_name_value.setLayoutParams(layoutParams);

            findViewById(R.id.btn_commit).setVisibility(View.GONE);

            asyncGetDetail();
        }

        gardenPropertyList = new ArrayList<>();
        gardenPropertyList.add(new GardenProperty(0, "示范园"));
        gardenPropertyList.add(new GardenProperty(4, "特色园"));
        gardenPropertyList.add(new GardenProperty(2, "实践园"));
        gardenPropertyList.add(new GardenProperty(1, "单品园"));
        mGardenPropertyAdapter = new GardenPropertyAdapter(mContext, gardenPropertyList);
        gv_garden_property.setAdapter(mGardenPropertyAdapter);

        goodsSuitList = new ArrayList<>();
        goodsSuitList.add(new GardenProperty(1, "STEAM启蒙课程"));
        goodsSuitList.add(new GardenProperty(2, "APPLE幼儿体育"));
        goodsSuitList.add(new GardenProperty(3, "STEAM工程课程"));
        goodsSuitList.add(new GardenProperty(4, "其他"));
        mGoodsSuitAdapter = new GardenPropertyAdapter(mContext, goodsSuitList);
        gv_goods_suit.setAdapter(mGoodsSuitAdapter);
    }

    @Override
    public void initListener() {
        findViewById(R.id.group_name_area).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (intensifiedTrainingId > 0) {//如果是详情页，不可点击
                    return;
                }
                Intent it = new Intent(mContext, ChooseGroupListActivity.class);
                it.putExtra("from", "register");
                startActivityForResult(it, REQ_CHOOSE_GROUP);
            }
        });
        gv_garden_property.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (intensifiedTrainingId > 0) {//如果是详情页，不可点击
                    return;
                }
                if (!gardenPropertyList.get(position).isChecked()) {
                    gardenPropertyList.get(position).setChecked(true);
                    for (int i = 0; i < gardenPropertyList.size(); i++) {
                        if (i != position)
                            gardenPropertyList.get(i).setChecked(false);
                    }
                } else {
                    gardenPropertyList.get(position).setChecked(false);
                }
                mGardenPropertyAdapter.notifyDataSetChanged();
            }
        });
        gv_goods_suit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (intensifiedTrainingId > 0) {//如果是详情页，不可点击
                    return;
                }
                if (!goodsSuitList.get(position).isChecked()) {
                    goodsSuitList.get(position).setChecked(true);
                } else {
                    goodsSuitList.get(position).setChecked(false);
                }
                mGoodsSuitAdapter.notifyDataSetChanged();
            }
        });
        findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                if (groupId <= 0) {
                    showShortToast("请选择幼儿园！");
                    return;
                }
                agentName = edit_agentname.getText().toString().trim();
                if (TextUtils.isEmpty(agentName)) {
                    showShortToast("请代理商姓名！");
                    return;
                }
                agentMobile = edit_agentmobile.getText().toString().trim();
                if (TextUtils.isEmpty(agentMobile)) {
                    showShortToast("请代理商手机号！");
                    return;
                }
                groupType = mGardenPropertyAdapter.getSelectedItemIds();
                if (TextUtils.isEmpty(groupType)) {
                    showShortToast("请选择园所性质！");
                    return;
                }
                purchaseSuit = mGoodsSuitAdapter.getSelectedItemIds();
                if (TextUtils.isEmpty(purchaseSuit)) {
                    showShortToast("请选择采购套装！");
                    return;
                }
                asyncApplyComit();
            }
        });
    }

    /**
     * 获取提报详情
     */
    private void asyncGetDetail() {
        String wholeUrl = AppUrl.host + AppUrl.findByIntensifiedTrainingId;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("intensifiedTrainingId", intensifiedTrainingId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, dLsner);
    }

    BaseRequestListener dLsner = new JsonRequestListener() {

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
            mServiceApplyBean = gson.fromJson(jsonResult.toString(), ServiceApplyBean.class);
            et_groupp_master_name.setText(mServiceApplyBean.getKindergartenName());
            et_groupp_mobile.setText(mServiceApplyBean.getKindergartenMobile());
            tv_groupp_name_value.setText(mServiceApplyBean.getGroupName());
            tv_groupp_addr_big_value.setText(mServiceApplyBean.getAreaAddress());
            tv_groupp_addr_value.setText(mServiceApplyBean.getStreetAddress());
            edit_agentname.setText(mServiceApplyBean.getAgentName());
            edit_agentmobile.setText(mServiceApplyBean.getAgentMobile());
            setGroupTypeChosen();
            setGoodsSuitChosen();
        }
    };

    private void setGroupTypeChosen() {
        if (mServiceApplyBean.getGroupType() == 0) {
            gardenPropertyList.get(0).setChecked(true);
        } else if (mServiceApplyBean.getGroupType() == 4) {
            gardenPropertyList.get(1).setChecked(true);
        } else if (mServiceApplyBean.getGroupType() == 2) {
            gardenPropertyList.get(2).setChecked(true);
        } else if (mServiceApplyBean.getGroupType() == 1) {
            gardenPropertyList.get(3).setChecked(true);
        }
        mGardenPropertyAdapter.notifyDataSetChanged();
    }

    private void setGoodsSuitChosen() {
        if (TextUtils.isEmpty(mServiceApplyBean.getPurchaseSuit()))
            return;
        if (mServiceApplyBean.getPurchaseSuit().indexOf(",") > -1) {//多个
            String[] ids = mServiceApplyBean.getPurchaseSuit().split(",");
            for (int i = 0; i < ids.length; i++) {
                goodsSuitList.get(FU.paseInt(ids[i]) - 1).setChecked(true);
            }
        } else {//单个
            goodsSuitList.get(FU.paseInt(mServiceApplyBean.getPurchaseSuit()) - 1).setChecked(true);
        }
        mGoodsSuitAdapter.notifyDataSetChanged();
    }

    /**
     * 提交
     */
    private void asyncApplyComit() {
        String wholeUrl = AppUrl.host + AppUrl.saveIntensifiedTraining;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("kindergartenName", kindergartenName);
        map.put("kindergartenMobile", kindergartenMobile);
        map.put("groupId", groupId);
        map.put("agentName", agentName);
        map.put("agentMobile", agentMobile);
        map.put("groupType", groupType);
        map.put("purchaseSuit", purchaseSuit);
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
            showShortToast("提交成功");
            startActivity(new Intent(mContext, MyTrainApplyListActivity.class));
            finish();
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
