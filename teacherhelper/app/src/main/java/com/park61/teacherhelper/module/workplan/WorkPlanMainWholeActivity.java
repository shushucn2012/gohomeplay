package com.park61.teacherhelper.module.workplan;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.module.workplan.adapter.MyWpTempAdapter;
import com.park61.teacherhelper.module.workplan.adapter.MyWpTempByCateAdapter;
import com.park61.teacherhelper.module.workplan.adapter.TodayWpListAdapter;
import com.park61.teacherhelper.module.workplan.bean.MySubWorkPlanCateBean;
import com.park61.teacherhelper.module.workplan.bean.TaskInfoBean;
import com.park61.teacherhelper.module.workplan.bean.TempBean;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.gridview.GridViewForScrollView;
import com.park61.teacherhelper.widget.listview.ListViewForScrollView;
import com.park61.teacherhelper.widget.modular.TempManagerModule;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

/**
 * 新版行事历首页
 *
 * @author shubei
 * @time 2018/12/20 16:39
 */
public class WorkPlanMainWholeActivity extends BaseActivity {

    private ListViewForScrollView lv_wp_today, lv_my_wp;
    //private GridViewForScrollView gv_my_wp;

    private List<TaskInfoBean> mList;
    private TodayWpListAdapter mTodayWpListAdapter;

    //private List<TempBean> dList;
    //private MyWpTempAdapter myWpTempAdapter;
    private List<MySubWorkPlanCateBean> dList;
    private MyWpTempByCateAdapter myWpTempAdapter;
    private int specialTaskNum;//专业版行事历任务数
    private boolean isFirstIn = true;//是否是第一次打开

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_workplan_main_whole);
    }

    @Override
    public void initView() {
        lv_wp_today = findViewById(R.id.lv_wp_today);
        lv_wp_today.setFocusable(false);
        //gv_my_wp = findViewById(R.id.gv_my_wp);
        //gv_my_wp.setFocusable(false);
        lv_my_wp = findViewById(R.id.lv_my_wp);
        lv_my_wp.setFocusable(false);
    }

    @Override
    public void initData() {
        setPagTitle("行事历");
        area_left.setVisibility(View.INVISIBLE);
        area_left.setOnClickListener(null);

        mList = new ArrayList<>();
        mTodayWpListAdapter = new TodayWpListAdapter(mContext, mList);
        lv_wp_today.setAdapter(mTodayWpListAdapter);

        dList = new ArrayList<>();
        myWpTempAdapter = new MyWpTempByCateAdapter(mContext, dList);
        lv_my_wp.setAdapter(myWpTempAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncUserIsOperationAdmin();
    }

    @Override
    public void initListener() {
        findViewById(R.id.area_top_banner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, WorkPlanAllCateActivity.class));
            }
        });
        findViewById(R.id.area_special_entry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, WorkManageMainActivity.class));
            }
        });
    }

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
            if (isFirstIn)
                showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            findViewById(R.id.area_temp_manager).setVisibility(View.GONE);
            //findViewById(R.id.area_wps_normal).setVisibility(View.VISIBLE);
            asyncGetTodayTasks();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            String dataRes = jsonResult.optString("data");
            if ("1".equals(dataRes)) {//是圆中校管理员进入管理页面
                GlobalParam.isTempManager = true;
                findViewById(R.id.area_temp_manager).setVisibility(View.VISIBLE);
                findViewById(R.id.area_wps_normal).setVisibility(View.GONE);
                TempManagerModule tempManagerModule = new TempManagerModule(mContext, findViewById(R.id.area_temp_manager), netRequest);
                tempManagerModule.init();
            } else {
                GlobalParam.isTempManager = false;
                findViewById(R.id.area_temp_manager).setVisibility(View.GONE);
                //findViewById(R.id.area_wps_normal).setVisibility(View.VISIBLE);
                asyncUserIsKindergartenAdmin();
                asyncGetTodayTasks();
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

    /**
     * 请求今日待办任务
     */
    private void asyncGetTodayTasks() {
        String url = AppUrl.host + AppUrl.getTodayTaskCalendarList;
        netRequest.startRequest(url, Request.Method.POST, "", 0, tlistener);
    }

    BaseRequestListener tlistener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            if (isFirstIn)
                showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            //dismissDialog();
            JSONArray arr = jsonResult.optJSONArray("list");
            if (arr != null && arr.length() > 0) {
                findViewById(R.id.area_today_wp).setVisibility(View.VISIBLE);
                mList.clear();
                for (int i = 0; i < arr.length(); i++) {
                    TaskInfoBean taskInfoBean = gson.fromJson(arr.optJSONObject(i).toString(), TaskInfoBean.class);
                    mList.add(taskInfoBean);
                }
                mTodayWpListAdapter.notifyDataSetChanged();
            } else {
                findViewById(R.id.area_today_wp).setVisibility(View.GONE);
            }
            asyncCountTaskCalendarNum();
        }
    };

    /**
     * 请求当前用户专业版的任务数
     */
    private void asyncCountTaskCalendarNum() {
        String url = AppUrl.host + AppUrl.countTaskCalendarNum;
        netRequest.startRequest(url, Request.Method.POST, "", 0, clistener);
    }

    BaseRequestListener clistener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            //showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            //dismissDialog();
            String taskNum = jsonResult.optString("data");
            specialTaskNum = FU.paseInt(taskNum);
            if (specialTaskNum > 0) // 有定制行事历的时候显示该板块，否则隐藏
                findViewById(R.id.area_custom_workplan).setVisibility(View.VISIBLE);
            else
                findViewById(R.id.area_custom_workplan).setVisibility(View.GONE);
            asyncFindMySubscribeTasks();
        }
    };

    /**
     * 请求我的行事历列表（订阅版）
     */
    private void asyncFindMySubscribeTasks() {
        //String url = AppUrl.host + AppUrl.findMySubscribeTaskCalendarList;
        String url = AppUrl.host + AppUrl.findMySubscribeTaskCalendarListV2;
        netRequest.startRequest(url, Request.Method.POST, "", 0, flistener);
    }

    BaseRequestListener flistener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            //showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray arr = jsonResult.optJSONArray("list");
            if (arr != null && arr.length() > 0) {
                findViewById(R.id.area_mysubs_wps).setVisibility(View.VISIBLE);
                dList.clear();
                for (int i = 0; i < arr.length(); i++) {
                    MySubWorkPlanCateBean tempBean = gson.fromJson(arr.optJSONObject(i).toString(), MySubWorkPlanCateBean.class);
                    dList.add(tempBean);
                }
                myWpTempAdapter.notifyDataSetChanged();
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        logout("=====================myWpTempAdapter_notifyDataSetChanged_again========================");
                        myWpTempAdapter.notifyDataSetChanged();
                    }
                }, 500);
            } else {
                findViewById(R.id.area_mysubs_wps).setVisibility(View.GONE);
            }
            //请求都完成了，再显示我的行事历页面
            findViewById(R.id.area_wps_normal).setVisibility(View.VISIBLE);
            if (CommonMethod.isListEmpty(mList) && CommonMethod.isListEmpty(dList) && specialTaskNum == 0) {
                findViewById(R.id.area_empty).setVisibility(View.VISIBLE);
                findViewById(R.id.btn_to_subs).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext, WorkPlanAllCateActivity.class));
                    }
                });
            } else {
                findViewById(R.id.area_empty).setVisibility(View.GONE);
            }
            isFirstIn = false;
        }
    };
}
