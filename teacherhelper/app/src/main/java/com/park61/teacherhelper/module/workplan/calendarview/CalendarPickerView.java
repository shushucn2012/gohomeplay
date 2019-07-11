// Copyright 2012 Square, Inc.
package com.park61.teacherhelper.module.workplan.calendarview;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.DateTool;
import com.park61.teacherhelper.module.workplan.bean.TaskCellBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;

/**
 * Android component to allow picking a date from a calendar view (a list of months).  Must be
 * initialized after inflation with  and can be customized with any of the
 * {@link FluentInitializer} methods returned.  The currently selected date can be retrieved with
 */
public class CalendarPickerView extends ListView {

    private final CalendarPickerView.MonthAdapter adapter;
    private final List<List<List<MonthCellDescriptor>>> cells = new ArrayList<List<List<MonthCellDescriptor>>>();
    final MonthView.Listener listener = new CellClickedListener();
    final List<MonthDescriptor> months = new ArrayList<MonthDescriptor>();
    private Locale locale;
    private DateFormat monthNameFormat;
    private DateFormat weekdayNameFormat;
    private DateFormat fullDateFormat;
    private Calendar minCal;
    private Calendar maxCal;
    private Calendar monthCounter;
    private boolean displayOnly;
    private Calendar today;

    private OnDateSelectedListener dateListener;
    private DateSelectableFilter dateConfiguredListener;
    private OnInvalidDateSelectedListener invalidDateListener = new DefaultOnInvalidDateSelectedListener();

    private JSONObject jsonResult;

    public CalendarPickerView(Context context) {
        super(context);
        Resources res = context.getResources();
        adapter = new MonthAdapter();
        setDivider(null);
        setDividerHeight(0);
        setBackgroundColor(res.getColor(R.color.transparent));
        setCacheColorHint(res.getColor(R.color.transparent));
        locale = Locale.getDefault();
        today = Calendar.getInstance(locale);
        minCal = Calendar.getInstance(locale);
        maxCal = Calendar.getInstance(locale);
        monthCounter = Calendar.getInstance(locale);
        monthNameFormat = new SimpleDateFormat(context.getString(R.string.month_name_format), locale);
        weekdayNameFormat = new SimpleDateFormat(context.getString(R.string.day_name_format), locale);
        fullDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
    }

    /*public CalendarPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Resources res = context.getResources();
        adapter = new MonthAdapter();
        setDivider(null);
        setDividerHeight(0);
        setBackgroundColor(res.getColor(R.color.transparent));
        setCacheColorHint(res.getColor(R.color.transparent));
        locale = Locale.getDefault();
        today = Calendar.getInstance(locale);
        minCal = Calendar.getInstance(locale);
        maxCal = Calendar.getInstance(locale);
        monthCounter = Calendar.getInstance(locale);
        monthNameFormat = new SimpleDateFormat(context.getString(R.string.month_name_format), locale);
        weekdayNameFormat = new SimpleDateFormat(context.getString(R.string.day_name_format), locale);
        fullDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);

        if (isInEditMode()) {
            Calendar nextYear = Calendar.getInstance(locale);
            nextYear.add(Calendar.YEAR, 1);
            init(new Date(), nextYear.getTime(), null);
        }
    }*/

    /**
     * Both date parameters must be non-null and their {@link Date#getTime()} must not return 0. Time
     * of day will be ignored.  For instance, if you pass in {@code minDate} as 11/16/2012 5:15pm and
     * {@code maxDate} as 11/16/2013 4:30am, 11/16/2012 will be the first selectable date and
     * 11/15/2013 will be the last selectable date ({@code maxDate} is exclusive).
     * <p>
     * {@link FluentInitializer} this method returns.
     * <p>
     * The calendar will be constructed using the given locale. This means that all names
     * (months, days) will be in the language of the locale and the weeks start with the day
     * specified by the locale.
     *
     * @param minDate Earliest selectable date, inclusive.  Must be earlier than {@code maxDate}.
     * @param maxDate Latest selectable date, exclusive.  Must be later than {@code minDate}.
     */
    public FluentInitializer init(Date minDate, Date maxDate, Locale locale, JSONObject jsonResult) {
        if (minDate == null || maxDate == null) {
            throw new IllegalArgumentException(
                    "minDate and maxDate must be non-null.  " + dbg(minDate, maxDate));
        }
        if (minDate.after(maxDate)) {
            throw new IllegalArgumentException(
                    "minDate must be before maxDate.  " + dbg(minDate, maxDate));
        }
        if (minDate.getTime() == 0 || maxDate.getTime() == 0) {
            throw new IllegalArgumentException(
                    "minDate and maxDate must be non-zero.  " + dbg(minDate, maxDate));
        }
        if (locale == null) {
            throw new IllegalArgumentException("Locale is null.");
        }

        // Make sure that all calendar instances use the same locale.
        this.jsonResult = jsonResult;
        this.locale = locale;
        today = Calendar.getInstance(locale);
        minCal = Calendar.getInstance(locale);
        maxCal = Calendar.getInstance(locale);
        monthCounter = Calendar.getInstance(locale);
        monthNameFormat = new SimpleDateFormat(getContext().getString(R.string.month_name_format), locale);
        for (MonthDescriptor month : months) {
            month.setLabel(monthNameFormat.format(month.getDate()));
        }
        weekdayNameFormat = new SimpleDateFormat(getContext().getString(R.string.day_name_format), locale);
        fullDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);

        // Clear previous state.
        cells.clear();
        months.clear();
        minCal.setTime(minDate);
        maxCal.setTime(maxDate);
        setMidnight(minCal);
        setMidnight(maxCal);
        displayOnly = false;

        // maxDate is exclusive: bump back to the previous day so if maxDate is the first of a month,
        // we don't accidentally include that month in the view.
        maxCal.add(MINUTE, -1);

        // Now iterate between minCal and maxCal and build up our list of months to show.
        monthCounter.setTime(minCal.getTime());
        final int maxMonth = maxCal.get(MONTH);
        final int maxYear = maxCal.get(YEAR);
//        while ((monthCounter.get(MONTH) <= maxMonth // Up to, including the month.
//                || monthCounter.get(YEAR) < maxYear) // Up to the year.
//                && monthCounter.get(YEAR) < maxYear + 1) { // But not > next yr.
        Date date = monthCounter.getTime();
        MonthDescriptor month = new MonthDescriptor(monthCounter.get(MONTH), monthCounter.get(YEAR), date, monthNameFormat.format(date));
        cells.add(getMonthCells(month, monthCounter));
        months.add(month);
//            monthCounter.add(MONTH, 1);
//        }
        validateAndUpdate();
        return new FluentInitializer();
    }

    /**
     * Both date parameters must be non-null and their {@link Date#getTime()} must not return 0. Time
     * of day will be ignored.  For instance, if you pass in {@code minDate} as 11/16/2012 5:15pm and
     * {@code maxDate} as 11/16/2013 4:30am, 11/16/2012 will be the first selectable date and
     * 11/15/2013 will be the last selectable date ({@code maxDate} is exclusive).
     * <p>
     * {@link FluentInitializer} this method returns.
     * <p>
     * The calendar will be constructed using the default locale as returned by
     * {@link Locale#getDefault()}. If you wish the calendar to be constructed using a
     * different locale, use .
     *
     * @param minDate Earliest selectable date, inclusive.  Must be earlier than {@code maxDate}.
     * @param maxDate Latest selectable date, exclusive.  Must be later than {@code minDate}.
     */
    public FluentInitializer init(Date minDate, Date maxDate, JSONObject jsonResult) {
        return init(minDate, maxDate, Locale.getDefault(), jsonResult);
    }

    public class FluentInitializer {
        public FluentInitializer setShortWeekdays(String[] newShortWeekdays) {
            DateFormatSymbols symbols = new DateFormatSymbols(locale);
            symbols.setShortWeekdays(newShortWeekdays);
            weekdayNameFormat =
                    new SimpleDateFormat(getContext().getString(R.string.day_name_format), symbols);
            return this;
        }

        public FluentInitializer displayOnly() {
            displayOnly = true;
            return this;
        }
    }

    private void validateAndUpdate() {
        if (getAdapter() == null) {
            setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (months.isEmpty()) {
            throw new IllegalStateException(
                    "Must have at least one month to display.  Did you forget to call init()?");
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Returns a string summarizing what the client sent us for init() params.
     */
    private static String dbg(Date minDate, Date maxDate) {
        return "minDate: " + minDate + "\nmaxDate: " + maxDate;
    }

    /**
     * Clears out the hours/minutes/seconds/millis of a Calendar.
     */
    static void setMidnight(Calendar cal) {
        cal.set(HOUR_OF_DAY, 0);
        cal.set(MINUTE, 0);
        cal.set(SECOND, 0);
        cal.set(MILLISECOND, 0);
    }

    private class CellClickedListener implements MonthView.Listener {
        @Override
        public void handleClick(MonthCellDescriptor cell) {
            dateListener.onDateSelected(cell);
        }
    }

    private void validateDate(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Selected date must be non-null.");
        }
        if (date.getTime() == 0) {
            throw new IllegalArgumentException("Selected date must be non-zero.  " + date);
        }
        if (date.before(minCal.getTime()) || date.after(maxCal.getTime())) {
            throw new IllegalArgumentException(
                    String.format("SelectedDate must be between minDate and maxDate."
                                    + "%nminDate: %s%nmaxDate: %s%nselectedDate: %s",
                            minCal.getTime(), maxCal.getTime(), date));
        }
    }

    /**
     * Hold a cell with a month-index.
     */
    private static class MonthCellWithMonthIndex {
        public MonthCellDescriptor cell;
        public int monthIndex;

        public MonthCellWithMonthIndex(MonthCellDescriptor cell, int monthIndex) {
            this.cell = cell;
            this.monthIndex = monthIndex;
        }
    }

    /**
     * Return cell and month-index (for scrolling) for a given Date.
     */
  /*  private MonthCellWithMonthIndex getMonthCellWithIndexByDate(Date date) {
        int index = 0;
        Calendar searchCal = Calendar.getInstance(locale);
        searchCal.setTime(date);
        Calendar actCal = Calendar.getInstance(locale);

        for (List<List<MonthCellDescriptor>> monthCells : cells) {
            for (List<MonthCellDescriptor> weekCells : monthCells) {
                for (MonthCellDescriptor actCell : weekCells) {
                    actCal.setTime(actCell.getDate());
                    if (sameDate(actCal, searchCal) && actCell.isSelectable()) {
                        return new MonthCellWithMonthIndex(actCell, index);
                    }
                }
            }
            index++;
        }
        return null;
    }*/

    private class MonthAdapter extends BaseAdapter {
        private final LayoutInflater inflater;

        private MonthAdapter() {
            inflater = LayoutInflater.from(getContext());
        }

        @Override
        public boolean isEnabled(int position) {
            // Disable selectability: each cell will handle that itself.
            return false;
        }

        @Override
        public int getCount() {
            return months.size();
        }

        @Override
        public Object getItem(int position) {
            return months.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MonthView monthView = (MonthView) convertView;
            if (monthView == null) {
                monthView = MonthView.create(parent, inflater, weekdayNameFormat, listener, today);
            }
            monthView.init(months.get(position), cells.get(position), displayOnly);
            return monthView;
        }
    }

    private List<List<MonthCellDescriptor>> getMonthCells(MonthDescriptor month, Calendar startCal) {
        Calendar cal = Calendar.getInstance(locale);
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

    private static boolean containsDate(List<Calendar> selectedCals, Calendar cal) {
        for (Calendar selectedCal : selectedCals) {
            if (sameDate(cal, selectedCal)) {
                return true;
            }
        }
        return false;
    }

    private static Calendar minDate(List<Calendar> selectedCals) {
        if (selectedCals == null || selectedCals.size() == 0) {
            return null;
        }
        Collections.sort(selectedCals);
        return selectedCals.get(0);
    }

    private static Calendar maxDate(List<Calendar> selectedCals) {
        if (selectedCals == null || selectedCals.size() == 0) {
            return null;
        }
        Collections.sort(selectedCals);
        return selectedCals.get(selectedCals.size() - 1);
    }

    private static boolean sameDate(Calendar cal, Calendar selectedDate) {
        return cal.get(MONTH) == selectedDate.get(MONTH)
                && cal.get(YEAR) == selectedDate.get(YEAR)
                && cal.get(DAY_OF_MONTH) == selectedDate.get(DAY_OF_MONTH);
    }

    private static boolean betweenDates(Calendar cal, Calendar minCal, Calendar maxCal) {
        final Date date = cal.getTime();
        return betweenDates(date, minCal, maxCal);
    }

    static boolean betweenDates(Date date, Calendar minCal, Calendar maxCal) {
        final Date min = minCal.getTime();
        return (date.equals(min) || date.after(min)) // >= minCal
                && date.before(maxCal.getTime()); // && < maxCal
    }

    /*private static boolean sameMonth(Calendar cal, MonthDescriptor month) {
        return (cal.get(MONTH) == month.getMonth() && cal.get(YEAR) == month.getYear());
    }*/

    private boolean isDateSelectable(Date date) {
        return dateConfiguredListener == null || dateConfiguredListener.isDateSelectable(date);
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        dateListener = listener;
    }

    /**
     * Set a listener to react to user selection of a disabled date.
     *
     * @param listener the listener to set, or null for no reaction
     */
    public void setOnInvalidDateSelectedListener(OnInvalidDateSelectedListener listener) {
        invalidDateListener = listener;
    }

    /**
     * Set a listener used to discriminate between selectable and unselectable dates. Set this to
     * disable arbitrary dates as they are rendered.
     * <p>
     * Important: set this before you call  methods.  If called afterwards,
     * it will not be consistently applied.
     */
    public void setDateSelectableFilter(DateSelectableFilter listener) {
        dateConfiguredListener = listener;
    }

    /**
     * Interface to be notified when a new date is selected or unselected. This will only be called
     * listener will not be notified.
     *
     * @see #setOnDateSelectedListener(OnDateSelectedListener)
     */
    public interface OnDateSelectedListener {
        void onDateSelected(MonthCellDescriptor monthCellDescriptor);
    }

    /**
     * Interface to be notified when an invalid date is selected by the user. This will only be
     * listener will not be notified.
     *
     * @see #setOnInvalidDateSelectedListener(OnInvalidDateSelectedListener)
     */
    public interface OnInvalidDateSelectedListener {
        void onInvalidDateSelected(Date date);
    }

    /**
     * Interface used for determining the selectability of a date cell when it is configured for
     * display on the calendar.
     *
     * @see #setDateSelectableFilter(DateSelectableFilter)
     */
    public interface DateSelectableFilter {
        boolean isDateSelectable(Date date);
    }

    private class DefaultOnInvalidDateSelectedListener implements OnInvalidDateSelectedListener {
        @Override
        public void onInvalidDateSelected(Date date) {
            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
        }
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
