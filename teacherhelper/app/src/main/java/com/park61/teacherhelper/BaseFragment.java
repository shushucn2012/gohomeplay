package com.park61.teacherhelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.park61.teacherhelper.common.json.NullStringToEmptyAdapterFactory;
import com.park61.teacherhelper.common.okhttp.OkHttpUtils;
import com.park61.teacherhelper.widget.dialog.CommonProgressDialog;
import com.park61.teacherhelper.widget.dialog.DoubleDialog;

/**
 * 基础fragment
 *
 * @author super
 */
public abstract class BaseFragment extends Fragment {
    /**
     * onCreateView返回的主view
     */
    public View parentView;
    /**
     * fragment的主activity
     */
    public Activity parentActivity;
    /**
     * log打印需要用到的标签
     */
    public String Tag = "BaseFragment";
    public CommonProgressDialog commonProgressDialog;
    public DoubleDialog dDialog;
    /**
     * 传入参数为字符串拼接方式的网络请求实例
     */
    public OkHttpUtils netRequest;
    public Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initEnvironment();
        initView();
        initData();
        initListener();
        return parentView;
    }

    private void initEnvironment() {
        parentActivity = getActivity();
        Tag = this.getClass().getSimpleName();
        netRequest = OkHttpUtils.getInstance();
        netRequest.setMainActivity(parentActivity);
        commonProgressDialog = new CommonProgressDialog(parentActivity);
        dDialog = new DoubleDialog(parentActivity);
        gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
    }

    /**
     * 初始化视图
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 初始化监听事件
     */
    public abstract void initListener();

    /**
     * toast短提示
     *
     * @param text 提示语
     */
    public void showShortToast(String text) {
        Toast.makeText(parentActivity, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * toast长提示
     *
     * @param text 提示语
     */
    public void showLongToast(String text) {
        Toast.makeText(parentActivity, text, Toast.LENGTH_LONG).show();
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

    public void showDialog(String msg) {
        try {
            if (commonProgressDialog.isShow()) {
                commonProgressDialog.setMessage(msg);
            } else {
                commonProgressDialog.showDialog(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCancelable(boolean is) {
        commonProgressDialog.setCancelable(is);
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

    public boolean isDialogShowing() {
        return commonProgressDialog.isShow();
    }

    /**
     * 设置列表为空提示
     */
    public void setListEmptyView(ListView lv) {
        TextView emptyView = new TextView(parentActivity);
        emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setTextColor(parentActivity.getResources().getColor(
                R.color.g666666));
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setText("暂无数据");
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) lv.getParent()).addView(emptyView);
        lv.setEmptyView(emptyView);
    }

    /**
     * 设置列表为空提示
     */
    public void setGridEmptyView(GridView lv) {
        TextView emptyView = new TextView(parentActivity);
        emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setTextColor(parentActivity.getResources().getColor(
                R.color.g666666));
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setText("暂无数据");
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) lv.getParent()).addView(emptyView);
        lv.setEmptyView(emptyView);
    }

}
