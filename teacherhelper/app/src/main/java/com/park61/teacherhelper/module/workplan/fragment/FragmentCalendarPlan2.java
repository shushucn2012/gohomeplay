package com.park61.teacherhelper.module.workplan.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.okhttp.OkHttpUtils;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DateTool;
import com.park61.teacherhelper.common.tool.ExitAppUtils;
import com.park61.teacherhelper.module.workplan.CalendarAllTaskActivity;
import com.park61.teacherhelper.module.workplan.CalendarPlanMainActivity;
import com.park61.teacherhelper.module.workplan.CalendarTaskActivity;
import com.park61.teacherhelper.module.workplan.GroupTempInfoMainActivity;
import com.park61.teacherhelper.module.workplan.ManageTaskActivity;
import com.park61.teacherhelper.module.workplan.WorkManageMainActivity;
import com.park61.teacherhelper.module.workplan.bean.TaskCellBean;
import com.park61.teacherhelper.module.workplan.calendarview.MonthCellDescriptor;
import com.park61.teacherhelper.module.workplan.calendarview.MonthDescriptor;
import com.park61.teacherhelper.module.workplan.calendarview.MonthView;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.dialog.CommonProgressDialog;
import com.park61.teacherhelper.widget.pw.CalandarMainTipPopWin;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * 日历任务计划
 * super create on 2018/7/31
 */

public class FragmentCalendarPlan2 extends ViewPagerFragment {

    public OkHttpUtils netRequest;
    public CommonProgressDialog commonProgressDialog;

    //private CalendarPickerView calendarPickerView;
    private LinearLayout area_content;

    private int taskCalendarClassId;//班级模板id
    private int index;//第几月
    private String firstDayStr, lastDayStr;
    private Date thisDayDate;//本月第一天
    private boolean mIsVisible;//是否是显示状态
    private boolean mReceiverTag = false;   //广播是否注册标识

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_calendar_plan, container, false);
        }
        return rootView;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        Log.out("========================onFragmentVisibleChange=========================" + isVisible);
        mIsVisible = isVisible;
        if (isVisible) {
            initView();
            initData();
            initListener();
        } else {

        }
    }

    public void initView() {
        commonProgressDialog = new CommonProgressDialog(getActivity());
        area_content = (LinearLayout) rootView.findViewById(R.id.area_content);
    }

    public void initData() {
        index = getArguments().getInt("index");
        taskCalendarClassId = getArguments().getInt("taskCalendarClassId");
        netRequest = OkHttpUtils.getInstance();
        netRequest.setMainActivity(getActivity());

        String curYear = "2018";
        if (getActivity() instanceof CalendarPlanMainActivity) {
            curYear = ((CalendarPlanMainActivity) getActivity()).curYear;
        } else if (getActivity() instanceof WorkManageMainActivity) {
            curYear = ((WorkManageMainActivity) getActivity()).curYear;
        }
        String thisMonth = index + 1 < 10 ? "0" + (index + 1) : "" + (index + 1);
        String firstDay = "01";
        thisDayDate = DateTool.parseDateStr(curYear + "-" + thisMonth + "-" + firstDay);
        firstDayStr = DateTool.getMonthFirstDay(thisDayDate);
        lastDayStr = DateTool.getMonthLastDay(thisDayDate);

        asyncGetPlan();
    }

    public void initListener() {
        registerRefreshReceiver();
    }

    private void asyncGetPlan() {
        String url = AppUrl.host + AppUrl.getStatisticalCalendarList;
        Map<String, Object> map = new HashMap<>();
        map.put("startDate", firstDayStr);
        map.put("endDate", lastDayStr);
        if (taskCalendarClassId > 0)
            map.put("taskCalendarClassId", taskCalendarClassId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            //Toast.makeText(getActivity(), "1231231313", Toast.LENGTH_LONG).show();
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            /*calendarPickerView = new CalendarPickerView(getActivity());
            calendarPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
                @Override
                public void onDateSelected(MonthCellDescriptor cellDescriptor) {
                    if (GlobalParam.isTempManager) {//园中校-到单日任务管理界面
                        Intent it = new Intent(getActivity(), ManageTaskActivity.class);
                        it.putExtra("gotDateStr", DateTool.getTheDateStrP4(cellDescriptor.getDate()));
                        if (cellDescriptor.getTaskCellBean() != null)
                            it.putExtra("taskCalendarClassId", cellDescriptor.getTaskCellBean().getTaskCalendarClassId());
                        startActivity(it);
                    } else {//到个人单日任务查看页面
                        if (ExitAppUtils.getInstance().getBeforeActivity() instanceof GroupTempInfoMainActivity) {//幼儿园管理界面
                            Intent it = new Intent(getActivity(), ManageTaskActivity.class);
                            it.putExtra("gotDateStr", DateTool.getTheDateStrP4(cellDescriptor.getDate()));
                            if (cellDescriptor.getTaskCellBean() != null)
                                it.putExtra("taskCalendarClassId", cellDescriptor.getTaskCellBean().getTaskCalendarClassId());
                            startActivity(it);
                        } else {
                            Intent it = new Intent(getActivity(), CalendarTaskActivity.class);
                            it.putExtra("gotDateStr", DateTool.getTheDateStrP4(cellDescriptor.getDate()));
                            startActivity(it);
                        }
                    }
                }
            });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            area_content.addView(calendarPickerView, lp);
            Calendar nextYear = Calendar.getInstance();
            nextYear.add(Calendar.YEAR, 1);
            calendarPickerView.init(thisDayDate, nextYear.getTime(), jsonResult);*/

            area_content.removeAllViews();
            Calendar today = Calendar.getInstance();
            today.setTime(thisDayDate);
            MonthDescriptor month = new MonthDescriptor(today.get(MONTH), today.get(YEAR), today.getTime(), new SimpleDateFormat("yyyy.MM").format(today.getTime()));
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            DateFormat weekdayNameFormat = new SimpleDateFormat(getContext().getString(R.string.day_name_format), Locale.getDefault());
            List<List<MonthCellDescriptor>> cell = getMonthCells(month, today, jsonResult);
            MonthView.Listener listener = new CellClickedListener();
            MonthView monthView = MonthView.create(area_content, inflater, weekdayNameFormat, listener, today);
            monthView.init(month, cell, false);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            area_content.addView(monthView, lp);

            if (getActivity() instanceof WorkManageMainActivity) {
                ((WorkManageMainActivity) getActivity()).showTips();
            } else if (getActivity() instanceof CalendarPlanMainActivity) {
                ((CalendarPlanMainActivity) getActivity()).showTips();
            }
        }
    };

    private class CellClickedListener implements MonthView.Listener {
        @Override
        public void handleClick(MonthCellDescriptor cellDescriptor) {
            if (GlobalParam.isTempManager) {//园中校-到单日任务管理界面
                Intent it = new Intent(getActivity(), ManageTaskActivity.class);
                it.putExtra("gotDateStr", DateTool.getTheDateStrP4(cellDescriptor.getDate()));
                if (cellDescriptor.getTaskCellBean() != null)
                    it.putExtra("taskCalendarClassId", cellDescriptor.getTaskCellBean().getTaskCalendarClassId());
                startActivity(it);
            } else {//到个人单日任务查看页面
                if (ExitAppUtils.getInstance().getBeforeActivity() instanceof GroupTempInfoMainActivity) {//幼儿园管理界面
                    if (GlobalParam.isGroupManager) {//幼儿园管理员可以查看所有并操作
                        Intent it = new Intent(getActivity(), ManageTaskActivity.class);
                        it.putExtra("gotDateStr", DateTool.getTheDateStrP4(cellDescriptor.getDate()));
                        if (cellDescriptor.getTaskCellBean() != null)
                            it.putExtra("taskCalendarClassId", cellDescriptor.getTaskCellBean().getTaskCalendarClassId());
                        startActivity(it);
                    } else {//幼儿园普通可以查看所有不能操作
                        Intent it = new Intent(getActivity(), CalendarAllTaskActivity.class);
                        it.putExtra("gotDateStr", DateTool.getTheDateStrP4(cellDescriptor.getDate()));
                        if (cellDescriptor.getTaskCellBean() != null)
                            it.putExtra("taskCalendarClassId", cellDescriptor.getTaskCellBean().getTaskCalendarClassId());
                        startActivity(it);
                    }
                } else {
                    Intent it = new Intent(getActivity(), CalendarTaskActivity.class);
                    it.putExtra("gotDateStr", DateTool.getTheDateStrP4(cellDescriptor.getDate()));
                    startActivity(it);
                }
            }
        }
    }

    private List<List<MonthCellDescriptor>> getMonthCells(MonthDescriptor month, Calendar startCal, JSONObject jsonResult) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startCal.getTime());
        List<List<MonthCellDescriptor>> cells = new ArrayList<List<MonthCellDescriptor>>();
        cal.set(DAY_OF_MONTH, 1);
        int firstDayOfWeek = cal.get(DAY_OF_WEEK);
        int offset = cal.getFirstDayOfWeek() - firstDayOfWeek;
        if (offset > 0) {
            offset -= 7;
        }
        cal.add(Calendar.DATE, offset);

        while ((cal.get(MONTH) < month.getMonth() + 1 || cal.get(YEAR) < month.getYear()) && cal.get(YEAR) <= month.getYear()) {
            List<MonthCellDescriptor> weekCells = new ArrayList<>();
            cells.add(weekCells);
            for (int c = 0; c < 7; c++) {
                boolean isSelectable = ((int) cal.get(MONTH) == month.getMonth());
                Date date = cal.getTime();
                int value = cal.get(DAY_OF_MONTH);
                TaskCellBean taskCellBean = getTaskCellByDate(DateTool.getTheDateStrP3(date), jsonResult);
                weekCells.add(new MonthCellDescriptor(date, value, isSelectable, taskCellBean));
                cal.add(DATE, 1);
            }
        }
        return cells;
    }

    /**
     * 获取当前日期优先级最高的任务
     *
     * @param dateStr
     * @param jsonResult
     * @return TaskCellBean
     */
    public TaskCellBean getTaskCellByDate(String dateStr, JSONObject jsonResult) {
        if (jsonResult == null)
            return null;
        JSONArray jsonArray = jsonResult.optJSONArray(dateStr);
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                TaskCellBean itemBean = new Gson().fromJson(jsonArray.optJSONObject(i).toString(), TaskCellBean.class);
                if (itemBean.getStatus() == -1) {//如果逾期优先级最高当前日期展示该任务
                    if (jsonArray.length() > 1)//当天有多个任务
                        itemBean.setIsMulti(true);
                    return itemBean;
                }
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                TaskCellBean itemBean = new Gson().fromJson(jsonArray.optJSONObject(i).toString(), TaskCellBean.class);
                if (itemBean.getStatus() == 0) {//如果待办，没有逾期则当前日期展示该任务
                    if (jsonArray.length() > 1)//当天有多个任务
                        itemBean.setIsMulti(true);
                    return itemBean;
                }
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                TaskCellBean itemBean = new Gson().fromJson(jsonArray.optJSONObject(i).toString(), TaskCellBean.class);
                if (itemBean.getStatus() == 1) {//如果已完成，没有逾期和待办，则当前日期展示该任务
                    if (jsonArray.length() > 1)//当天有多个任务
                        itemBean.setIsMulti(true);
                    return itemBean;
                }
            }
        }
        return null;
    }

    public void showDialog() {
        try {
            if (commonProgressDialog.isShow()) {
                return;
            } else {
                commonProgressDialog.showDialog(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissDialog() {
        try {
            if (commonProgressDialog.isShow()) {
                commonProgressDialog.dialogDismiss();
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 其他页面要改变当前页的广播
     */
    private void registerRefreshReceiver() {
        try {
            if (!mReceiverTag) {
                IntentFilter filter = new IntentFilter();
                filter.addAction("ACTION_PARENT_ACTIVITY_RESUME");
                getActivity().registerReceiver(workChangenNumReceiver, filter);
                mReceiverTag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private BroadcastReceiver workChangenNumReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //接收父activity发送的广播去更新数据，fragment的onresume方法不可靠，并不能表示activity的onresume
            //当前显示的fragment刷新，不显示不刷新
            if (mIsVisible) {
                String curYear = "2018";
                if (getActivity() instanceof CalendarPlanMainActivity) {
                    curYear = ((CalendarPlanMainActivity) getActivity()).curYear;
                } else if (getActivity() instanceof WorkManageMainActivity) {
                    curYear = ((WorkManageMainActivity) getActivity()).curYear;
                }
                String thisMonth = index + 1 < 10 ? "0" + (index + 1) : "" + (index + 1);
                String firstDay = "01";
                thisDayDate = DateTool.parseDateStr(curYear + "-" + thisMonth + "-" + firstDay);
                firstDayStr = DateTool.getMonthFirstDay(thisDayDate);
                lastDayStr = DateTool.getMonthLastDay(thisDayDate);

                asyncGetPlan();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mReceiverTag) {
                getActivity().unregisterReceiver(workChangenNumReceiver);
                mReceiverTag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
