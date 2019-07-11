package com.park61.teacherhelper.module.workplan;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
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

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DateTool;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.workplan.adapter.CalendarPlanPagerAdapter;
import com.park61.teacherhelper.module.workplan.adapter.MCPageChangeListener;
import com.park61.teacherhelper.module.workplan.adapter.TTaskDoneInfoAdapter;
import com.park61.teacherhelper.module.workplan.bean.TaskDoneInfoBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.pw.CalandarMainTipPopWin;
import com.park61.teacherhelper.widget.viewpager.MyPagerSlidingTabStrip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 行事历首页
 * 1、如果是园中校管理员----》管理员行事历首页-查看所有班级模板任务完成情况
 * 2、如果不是----》个人日历首页
 * Created by shubei on 2018/7/26.
 */

public class WorkManageMainActivity extends BaseActivity {

    //管理员行事历首页属性---------------start//
    private ListView lv_task_done_info;
    private EditText edit_sousuo;
    private ImageView img_del;

    private List<TaskDoneInfoBean> dataList = new ArrayList<>();
    private TTaskDoneInfoAdapter adapter;
    private String name;//搜索关键字
    //管理员行事历首页属性---------------end//

    //个人日历首页---------------start//
    private MyPagerSlidingTabStrip tabs;
    private ViewPager pager;
    private View click_tv;
    private TimePickerView pvTime;
    private TextView tv_date_top;
    public String curYear = "2018"; //当前所选时间年份，默认系统时间年份
    //个人日历首页---------------end//

    public void setCurYearAndTextView(String year) {
        curYear = year;
        tv_date_top.setText(curYear + "年");
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_workmanage_main);
    }

    @Override
    public void initView() {
        //管理员行事历首页属性---------------start//
        edit_sousuo = (EditText) findViewById(R.id.edit_sousuo);
        lv_task_done_info = (ListView) findViewById(R.id.lv_task_done_info);
        img_del = (ImageView) findViewById(R.id.img_del);
        //管理员行事历首页属性---------------end//

        //个人日历首页---------------start//
        tabs = (MyPagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        click_tv = findViewById(R.id.click_tv);
        tv_date_top = (TextView) findViewById(R.id.tv_date_top);
        //个人日历首页---------------end//
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewInitTool.AddStatistics(mContext, "ClicktaskCalendar");
        if (GlobalParam.userToken != null) {
            asyncUserIsOperationAdmin();
        }
        sendBroadcast(new Intent("ACTION_PARENT_ACTIVITY_RESUME"));//发送广播，通知当前显示的fragment刷新数据
    }

    @Override
    public void initData() {
        //管理员行事历首页属性---------------start//
        setPagTitle("行事历");
        area_left.setVisibility(View.INVISIBLE);
        area_right.setVisibility(View.VISIBLE);
        ((ImageView) findViewById(R.id.img_right)).setImageResource(R.mipmap.icon_to_temp_manage);

        adapter = new TTaskDoneInfoAdapter(mContext, dataList);
        lv_task_done_info.setAdapter(adapter);
        //管理员行事历首页属性---------------end//

        //个人日历首页---------------start//
        curYear = DateTool.getSysYear();//默认系统时间年份
        tv_date_top.setText(curYear + "年");
        pager.setAdapter(new CalendarPlanPagerAdapter(getSupportFragmentManager(), 0));
        tabs.setViewPager(pager);
        tabs.setParentActivity(WorkManageMainActivity.this);
        tabs.setTextSize(DevAttr.dip2px(mContext, 16));
        tabs.setTextColor(getResources().getColor(R.color.gc9bec2));
        tabs.setSelectedTextColor(getResources().getColor(R.color.g333333));
        tabs.setSelectedTextSize(DevAttr.dip2px(mContext, 17));
        pager.setOffscreenPageLimit(1);
        pager.setCurrentItem(new Date().getMonth() + 1, false);

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //改变日历控件
                curYear = String.valueOf(date.getYear() + 1900);
                tv_date_top.setText(curYear + "年");
                if (date.getMonth() + 1 == pager.getCurrentItem()) {//如果相等，代表显示的page不会切换不会重新initData去取数据，只能发广播让他重拿
                    sendBroadcast(new Intent("ACTION_PARENT_ACTIVITY_RESUME"));//发送广播，通知当前显示的fragment刷新数据
                } else {//如果不等，可以切换数据
                    pager.setCurrentItem(date.getMonth() + 1, false);
                }
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {

            }
        }).setType(new boolean[]{true, true, false, false, false, false}).build();
        //个人日历首页---------------end//
    }

    @Override
    public void initListener() {
        //管理员行事历首页属性---------------start//
        findViewById(R.id.img_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, TempMainActivity.class));
            }
        });
        edit_sousuo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    name = edit_sousuo.getText().toString().trim();
                    asyncAssignedTaskCalendarList();
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
        adapter.setOnNoticeClickedListener(new TTaskDoneInfoAdapter.OnNoticeClickedListener() {
            @Override
            public void onClicked(int position) {
                asyncNotifyExecutor(dataList.get(position));
            }

            @Override
            public void onTitle(int position) {
                GlobalParam.teachClassId = dataList.get(position).getTeachClassId();
                Intent it = new Intent(mContext, CalendarPlanMainActivity.class);
                GlobalParam.taskCalendarClassId = dataList.get(position).getId();
                it.putExtra("taskCalendarClassId", dataList.get(position).getId());
                startActivity(it);
            }

            @Override
            public void deleteTask(int id, int position) {
                dDialog.showDialog("提示", "行事历一经删除，所有任务失效，不可再恢复，是否确认删除?", "取消", "确定", null,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dDialog.dismissDialog();
                                asyncDelTaskCalendarTemplateClass(id);
                            }
                        });
            }
        });
        //管理员行事历首页属性---------------end//

        //个人日历首页---------------start//
        findViewById(R.id.area_right_normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, GroupTempInfoMainActivity.class));
            }
        });
        click_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出日期选择
                pvTime.show(click_tv);
            }
        });
        pager.addOnPageChangeListener(new MCPageChangeListener(pager, WorkManageMainActivity.this));
        findViewById(R.id.area_back).setOnClickListener(v -> finish());
        //个人日历首页---------------end//
    }

    /**
     * 已分配模板列表
     */
    public void asyncAssignedTaskCalendarList() {
        String wholeUrl = AppUrl.host + AppUrl.assignedTaskCalendarList;
        Map<String, Object> map = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(name)) {
            map.put("name", name);
        }
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, alistener);
    }

    BaseRequestListener alistener = new JsonRequestListener() {

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
            dataList.clear();
            JSONArray actJay = jsonResult.optJSONArray("list");
            if (actJay != null && actJay.length() > 0) {
                for (int i = 0; i < actJay.length(); i++) {
                    JSONObject itemJot = actJay.optJSONObject(i);
                    TaskDoneInfoBean g = gson.fromJson(itemJot.toString(), TaskDoneInfoBean.class);
                    dataList.add(g);
                }
                adapter.notifyDataSetChanged();
            }
        }
    };

    /**
     * 通知督办
     */
    public void asyncNotifyExecutor(TaskDoneInfoBean taskDoneInfoBean) {
        String wholeUrl = AppUrl.host + AppUrl.notifyExecutor;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", taskDoneInfoBean.getId());
        map.put("taskCalendarTemplateName", taskDoneInfoBean.getTaskCalendarTemplateName());
        map.put("taskOverdueNum", taskDoneInfoBean.getTaskOverdueNum());
        map.put("teachClassName", taskDoneInfoBean.getTeachClassName());
        map.put("teachGroupId", taskDoneInfoBean.getTeachGroupId());
        map.put("teachGroupName", taskDoneInfoBean.getTeachGroupName());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, tlistener);
    }

    BaseRequestListener tlistener = new JsonRequestListener() {

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
            showShortToast("通知短信已发送");
        }
    };

    /**
     * 行事历已分配模板列表删除
     */
    public void asyncDelTaskCalendarTemplateClass(int id) {
        String wholeUrl = AppUrl.host + AppUrl.delTaskCalendarTemplateClass;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskCalendarTemplateClassId", id);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, dlistener);
    }

    BaseRequestListener dlistener = new JsonRequestListener() {

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
            showShortToast("删除成功");
            //重刷列表
            asyncAssignedTaskCalendarList();
        }
    };

    /**
     * 是否是园中校用户
     */
    public void asyncUserIsOperationAdmin() {
        String wholeUrl = AppUrl.host + AppUrl.userIsOperationAdmin;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, ulistener);
    }

    BaseRequestListener ulistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            //showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            //dismissDialog();
            showShortToast(errorMsg);
            if ("0000042014".equals(errorCode)) {//为开通行事历
                findViewById(R.id.area_empty).setVisibility(View.VISIBLE);
                findViewById(R.id.area_temp_normal).setVisibility(View.GONE);
                findViewById(R.id.area_temp_manager).setVisibility(View.GONE);
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            //dismissDialog();
            String dataRes = jsonResult.optString("data");
            if ("1".equals(dataRes)) {
                GlobalParam.isTempManager = true;
                findViewById(R.id.area_temp_manager).setVisibility(View.VISIBLE);
                findViewById(R.id.area_temp_normal).setVisibility(View.GONE);
                asyncAssignedTaskCalendarList();
            } else {
                GlobalParam.isTempManager = false;
                findViewById(R.id.area_temp_manager).setVisibility(View.GONE);
                findViewById(R.id.area_temp_normal).setVisibility(View.VISIBLE);
                asyncUserIsKindergartenAdmin();
            }
        }
    };

    /*
    * 用户是否幼儿园管理员
    */
    public void asyncUserIsKindergartenAdmin() {
        String wholeUrl = AppUrl.host + AppUrl.userIsKindergartenAdmin;//"http://10.10.10.18:8380/service/teachMember/userIsKindergartenAdmin";//
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, slistener);
    }

    BaseRequestListener slistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            if ("0".equals(jsonResult.optString("data"))) {//是否管理员：0不是，1是
                GlobalParam.isGroupManager = false;
            } else {
                GlobalParam.isGroupManager = true;
            }
        }
    };

    public void showTips() {
        // 幼儿园用户首页，判断是否是第一次进入，给出引导提示
        SharedPreferences isFirstRunSp = getSharedPreferences("isFirstWmmFlag", Activity.MODE_PRIVATE);
        boolean isFirstRun = isFirstRunSp.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            CalandarMainTipPopWin mCalandarMainTipPopWin = new CalandarMainTipPopWin(mContext);
            mCalandarMainTipPopWin.showAsDropDown(findViewById(R.id.top_line));
            SharedPreferences sp = getSharedPreferences("isFirstWmmFlag", Activity.MODE_PRIVATE);
            SharedPreferences.Editor mEditor = sp.edit();
            mEditor.putBoolean("isFirstRun", false);
            mEditor.commit();
        }
    }
}
