package com.park61.teacherhelper.common.tool;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by shushucn2012 on 2016/6/6.
 */
public class ListViewFootState {

    private ListView listView;
    private TextView footLoadingView;
    private Context context;
    private int currState = 0;

    public static final int LOADING = 0;//加载中
    public static final int ERROR = 1;//错误
    public static final int END = 2;//到底了
    /**
     * 空闲状态，即为非LOADING，ERROR，END
     */
    public static final int IDLE = 4;

    public ListViewFootState(ListView lv,Context c){
        this.listView = lv;
        this.context = c;
        initListFootView();
    }

    public ListViewFootState(TextView textView){
        this.footLoadingView = textView;
    }

    public void initListFootView() {
        footLoadingView = new TextView(context);
        footLoadingView.setLayoutParams(new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, DevAttr.dip2px(context,
                40)));
        footLoadingView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        footLoadingView.setGravity(Gravity.CENTER);
        footLoadingView.setText("");
        listView.addFooterView(footLoadingView);
    }

    public void addListFootLoading() {
        currState = LOADING;
        footLoadingView.setText("加载中...");
    }

    public void addListFootErr() {
        currState = ERROR;
        footLoadingView.setText("加载失败，请重试！");
    }

    public void addListFootEnd() {
        currState = END;
        footLoadingView.setText("已经是最后一页了");
    }

    public void addListFootEndNew() {
        currState = END;
        footLoadingView.setText("亲~已经到底了");
    }

    public void addListFootIdle() {
        currState = IDLE;
        footLoadingView.setText("");
    }

    public int getCurrState(){
        return currState;
    }
}
