package com.park61.teacherhelper.module.workplan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DateTool;
import com.park61.teacherhelper.common.tool.ExitAppUtils;
import com.park61.teacherhelper.module.workplan.CalendarTaskActivity;
import com.park61.teacherhelper.module.workplan.GroupTempInfoMainActivity;
import com.park61.teacherhelper.module.workplan.ManageTaskActivity;
import com.park61.teacherhelper.module.workplan.bean.TaskCellBean;
import com.park61.teacherhelper.module.workplan.calendarview.CalendarPickerView;
import com.park61.teacherhelper.module.workplan.calendarview.MonthCellDescriptor;
import com.park61.teacherhelper.module.workplan.calendarview.MonthDescriptor;
import com.park61.teacherhelper.module.workplan.calendarview.MonthView;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

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

import static com.park61.teacherhelper.module.dict.PageParam.PAGE_NUM;
import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * 日历任务计划
 * super create on 2018/7/31
 */

public class FragmentCalendarPlan extends BaseFragment {

    //private CalendarPickerView calendarPickerView;
    private LinearLayout area_content;

    private int taskCalendarClassId;//班级模板id
    private int index;//第几月
    private String firstDayStr, lastDayStr;
    private Date thisDayDate;//本月第一天

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_calendar_plan, container, false);
        index = getArguments().getInt("index");
        taskCalendarClassId = getArguments().getInt("taskCalendarClassId");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        area_content = (LinearLayout) parentView.findViewById(R.id.area_content);
    }

    @Override
    public void initData() {
        String curYear = DateTool.getSysYear();
        String thisMonth = index + 1 < 10 ? "0" + (index + 1) : "" + (index + 1);
        String firstDay = "01";
        thisDayDate = DateTool.parseDateStr(curYear + "-" + thisMonth + "-" + firstDay);
        firstDayStr = DateTool.getMonthFirstDay(thisDayDate);
        lastDayStr = DateTool.getMonthLastDay(thisDayDate);

        asyncGetPlan();
    }

    @Override
    public void initListener() {
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
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            /*calendarPickerView = new CalendarPickerView(parentActivity);
            calendarPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
                @Override
                public void onDateSelected(MonthCellDescriptor cellDescriptor) {
                    if (GlobalParam.isTempManager) {//园中校-到单日任务管理界面
                        Intent it = new Intent(parentActivity, ManageTaskActivity.class);
                        it.putExtra("gotDateStr", DateTool.getTheDateStrP4(cellDescriptor.getDate()));
                        if (cellDescriptor.getTaskCellBean() != null)
                            it.putExtra("taskCalendarClassId", cellDescriptor.getTaskCellBean().getTaskCalendarClassId());
                        startActivity(it);
                    } else {//到个人单日任务查看页面
                        if (ExitAppUtils.getInstance().getBeforeActivity() instanceof GroupTempInfoMainActivity) {//幼儿园管理界面
                            Intent it = new Intent(parentActivity, ManageTaskActivity.class);
                            it.putExtra("gotDateStr", DateTool.getTheDateStrP4(cellDescriptor.getDate()));
                            if (cellDescriptor.getTaskCellBean() != null)
                                it.putExtra("taskCalendarClassId", cellDescriptor.getTaskCellBean().getTaskCalendarClassId());
                            startActivity(it);
                        } else {
                            Intent it = new Intent(parentActivity, CalendarTaskActivity.class);
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

            LayoutInflater inflater = LayoutInflater.from(parentActivity);

            DateFormat weekdayNameFormat = new SimpleDateFormat(getContext().getString(R.string.day_name_format), Locale.getDefault());

            List<List<MonthCellDescriptor>>  cell = getMonthCells(month, today, jsonResult);

            MonthView.Listener listener = new CellClickedListener();

            MonthView   monthView = MonthView.create(area_content, inflater, weekdayNameFormat, listener, today);

            monthView.init(month, cell, true);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            area_content.addView(monthView, lp);
        }
    };

    private class CellClickedListener implements MonthView.Listener {
        @Override
        public void handleClick(MonthCellDescriptor cell) {
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
            List<MonthCellDescriptor> weekCells = new ArrayList<MonthCellDescriptor>();
            cells.add(weekCells);
            for (int c = 0; c < 7; c++) {
                boolean isSelectable = cal.get(MONTH) == month.getMonth();
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



}
