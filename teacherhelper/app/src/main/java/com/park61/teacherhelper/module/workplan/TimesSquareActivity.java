package com.park61.teacherhelper.module.workplan;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.widget.TimesSquare.CalendarPickerView;
import com.park61.teacherhelper.widget.TimesSquare.MonthView;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class TimesSquareActivity extends BaseActivity implements CalendarPickerView.OnDateSelectedListener {
    private CalendarPickerView calendar;

    private Date preDate;//上一个点击的时间
    public static boolean isStartEndInOne = false;//开始和结束是否在一个格子里

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_times_square);
    }

    @Override
    public void initView() {
        setPagTitle("执行时间选择");
        MonthView.tag[0] = "开始";
        MonthView.tag[1] = "结束";

        List<Date> dateList = new ArrayList<>();

        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");
        if (!TextUtils.isEmpty(startDate)) {
            dateList.add(strToDateLong(startDate));
            dateList.add(strToDateLong(endDate));

            if(startDate.equals(endDate)){
                isStartEndInOne = true;
            }
        }

        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        calendar.setOnDateSelectedListener(this);

        if (CommonMethod.isListEmpty(dateList)) {
            calendar.init(new Date(), nextYear.getTime())
                    .inMode(CalendarPickerView.SelectionMode.RANGE);
        } else {
            Date beginDate = new Date();
            if(beginDate.after(dateList.get(0)))
                beginDate = dateList.get(0);

            calendar.init(beginDate, nextYear.getTime())
                    .inMode(CalendarPickerView.SelectionMode.RANGE)
                    .withSelectedDates(dateList);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void onDateSelected(Date date) {
       /* if (ticketChoose.isRoundTrip()) {
            if (calendar.getSelectedDates().size() < 2) {
                Toast.makeText(this, "请选择返程日期", Toast.LENGTH_SHORT).show();
            } else {
                Date date1 = calendar.getSelectedDates().get(0);
                ticketChoose.setSDetailDate(formatDate(date1)[0]);
                ticketChoose.setSDate(formatDate(date1)[1]);
                ticketChoose.setSWeek(formatDate(date1)[2]);
                Date date2 = calendar.getSelectedDates().get(calendar.getSelectedDates().size() - 1);
                ticketChoose.setEDetailDate(formatDate(date2)[0]);
                ticketChoose.setEDate(formatDate(date2)[1]);
                ticketChoose.setEWeek(formatDate(date2)[2]);
                EventBus.getDefault().post(ticketChoose);
                finish();
            }
        } else {
            ticketChoose.setSDetailDate(formatDate(date)[0]);
            ticketChoose.setSDate(formatDate(date)[1]);
            ticketChoose.setSWeek(formatDate(date)[2]);
            EventBus.getDefault().post(ticketChoose);
            finish();
        }*/
        if (preDate != null && preDate.equals(date)) {//如果两次点击同一个日期
            Intent returnData = new Intent();
            returnData.putExtra("startDate", formatDate(date)[0]);
            returnData.putExtra("endDate", formatDate(date)[0]);
            setResult(RESULT_OK, returnData);
            finish();
            return;
        }

        preDate = date;


        if (calendar.getSelectedDates().size() < 2) {
            Toast.makeText(this, "请选择结束时间", Toast.LENGTH_SHORT).show();
        } else {
            Date date1 = calendar.getSelectedDates().get(0);
            Date date2 = calendar.getSelectedDates().get(calendar.getSelectedDates().size() - 1);
            Intent returnData = new Intent();
            returnData.putExtra("startDate", formatDate(date1)[0]);
            returnData.putExtra("endDate", formatDate(date2)[0]);
            setResult(RESULT_OK, returnData);
            finish();
        }
    }

    public String[] formatDate(Date date) {
        String[] formateDate = new String[3];
        SimpleDateFormat formatStr = new SimpleDateFormat("MM月dd日", Locale.getDefault());
        String month = formatStr.format(date);
        formatStr = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String detailDate = formatStr.format(date);
        formatStr = new SimpleDateFormat("EEE", Locale.getDefault());
        String day = formatStr.format(date);

        formateDate[0] = detailDate;
        formateDate[1] = month;
        formateDate[2] = day;
        return formateDate;
    }

    @Override
    public void onDateUnselected(Date date) {

    }

    /**
     * 获取两个日期之间的日期
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 日期集合
     */
    private List<Date> getBetweenDates(Date start, Date end) {
        List<Date> result = new ArrayList<Date>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        tempStart.add(Calendar.DAY_OF_YEAR, 1);

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        while (tempStart.before(tempEnd)) {
            result.add(tempStart.getTime());
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;
    }

    public Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }
}
