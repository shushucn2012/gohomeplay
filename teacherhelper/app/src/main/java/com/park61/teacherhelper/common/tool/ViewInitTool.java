package com.park61.teacherhelper.common.tool;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.park61.teacherhelper.CanBackWebViewActivity;
import com.park61.teacherhelper.FuWuBaoDianNativeActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.Log;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.ObservableScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.park61.teacherhelper.module.activity.ActivityList;
import com.park61.teacherhelper.module.activity.ActivityWebViewActivity;
import com.park61.teacherhelper.module.activity.ChristmasActMainActivity;
import com.park61.teacherhelper.module.activity.Double11ActDetailsActivity;
import com.park61.teacherhelper.module.activity.JiXunActDetailActivity;
import com.park61.teacherhelper.module.activity.MyTrainApplyActivity;
import com.park61.teacherhelper.module.clazz.ClazzMainActivity;
import com.park61.teacherhelper.module.clazz.FamilyConBookMainActivity;
import com.park61.teacherhelper.module.clazz.ParkApplyActivity;
import com.park61.teacherhelper.module.course.CourseListByCateActivity;
import com.park61.teacherhelper.module.course.CourseSearchResultActivity;
import com.park61.teacherhelper.module.home.CourseDetailsActivity;
import com.park61.teacherhelper.module.home.PhysicalFitnessTestWvActivity;
import com.park61.teacherhelper.module.home.ServiceApplyActivity;
import com.park61.teacherhelper.module.home.bean.CmsItem;
import com.park61.teacherhelper.module.my.MyTiBaoActivity;
import com.park61.teacherhelper.module.okdownload.db.DownloadDAO;
import com.park61.teacherhelper.module.okdownload.db.DownloadTask;
import com.park61.teacherhelper.module.workplan.MySubsWpsActivity;
import com.park61.teacherhelper.module.workplan.WorkPlanMainActivity;
import com.park61.teacherhelper.widget.webview.ShowImageWebView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class ViewInitTool {

    public static final String CLICK_CATEGORY = "Click_Category";

    /**
     * 初始化上下拉刷新控件 文字样式
     */
    public static void initPullToRefresh(PullToRefreshScrollView mPullRefreshScrollView, Context context) {
        ILoadingLayout startLabels = mPullRefreshScrollView.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
//        Drawable ld = context.getResources().getDrawable(R.drawable.animation_list_small_loading);
//        startLabels.setLoadingDrawable(ld);

        ILoadingLayout endLabels = mPullRefreshScrollView.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
    }

    /**
     * 初始化上下拉刷新控件 文字样式
     */
    public static void initPullToRefresh(PullToRefreshListView mPullRefreshListView, Context context) {
        ILoadingLayout startLabels = mPullRefreshListView.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
//        Drawable ld = context.getResources().getDrawable(R.drawable.animation_list_small_loading);
//        startLabels.setLoadingDrawable(ld);

        ILoadingLayout endLabels = mPullRefreshListView.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
    }

    /**
     * 初始化上下拉刷新控件 文字样式
     */
    public static void initPullToRefresh(PullToRefreshGridView mPullToRefreshGridView) {
        ILoadingLayout startLabels = mPullToRefreshGridView.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = mPullToRefreshGridView.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
    }

    /**
     * 初始化黏贴控件
     */
    public static void initObservableScrollView(
            final ObservableScrollView scrollView,
            final ObservableScrollView.Callbacks mCallbacks) {
        scrollView.setCallbacks(mCallbacks);
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        mCallbacks.onScrollChanged(scrollView.getScrollY());
                    }
                });
        scrollView.scrollTo(0, 0);
    }

    /**
     * 设置列表为空提示
     */
    public static void setListEmptyView(Context mContext, ListView lv) {
        TextView emptyView = new TextView(mContext);
        emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setTextColor(mContext.getResources()
                .getColor(R.color.g666666));
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setText("暂无数据");
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) lv.getParent()).addView(emptyView);
        lv.setEmptyView(emptyView);
    }

    /**
     * 设置列表为空提示（设置高度）
     */
    public static void setListEmptyView(Context mContext, ListView lv, int mTop) {
        TextView emptyView = new TextView(mContext);
        emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setTextColor(mContext.getResources()
                .getColor(R.color.g666666));
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setText("暂无数据");
        emptyView.setPadding(mTop, mTop, mTop, mTop);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) lv.getParent()).addView(emptyView);
        lv.setEmptyView(emptyView);
    }

    /**
     * 设置列表为空提示(设置图片) 有文字，图片默认
     */
    public static void setListEmptyByDefaultTipPic(Context mContext, ListView lv) {
        setListEmptyView(mContext, lv, "暂无数据", R.mipmap.icon_cotent_empty, null, 160, 135);
    }

    /**
     * 设置列表为空提示(设置图片)
     */
    public static void setListEmptyView(Context mContext, ListView lv, String tip, int res, OnClickListener lsner) {
        setListEmptyView(mContext, lv, tip, res, lsner, 0, 0);
    }

    public static void setListEmptyView(Context mContext, ListView lv, String tip, int res, OnClickListener lsner, int pWidth, int pHeight) {
        LinearLayout ll = new LinearLayout(mContext);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);

        ImageView img = new ImageView(mContext);
        img.setImageResource(res);
        if (pWidth > 0 && pHeight > 0) {
            img.setLayoutParams(new LayoutParams(DevAttr.dip2px(mContext, pWidth), DevAttr.dip2px(mContext, pHeight)));
        } else {
            img.setLayoutParams(new LayoutParams(DevAttr.dip2px(mContext, 160), DevAttr.dip2px(mContext, 134)));
        }
        img.setOnClickListener(lsner);

        TextView emptyView = new TextView(mContext);
        LinearLayout.LayoutParams emptyLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        emptyLp.setMargins(0, DevAttr.dip2px(mContext, 20), 0, 0);
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        emptyView.setTextColor(mContext.getResources().getColor(R.color.g999999));
        emptyView.setText(tip);

        ll.addView(img);
        ll.addView(emptyView, emptyLp);
        ll.setVisibility(View.GONE);
        ll.setTag("empty_tag");

        //-----避免重复加载空提示视图 start------//
        ViewGroup parentGroup = (ViewGroup) lv.getParent();
        boolean hasEmptyView = false;
        for (int i = 0; i < parentGroup.getChildCount(); i++) {
            if (parentGroup.getChildAt(i).getTag() != null && parentGroup.getChildAt(i).getTag().equals("empty_tag")) {
                hasEmptyView = true;
            }
        }
        //-----避免重复加载空提示视图 end------//
        if (!hasEmptyView) { // 不存在空提示视图时才加上去
            ((ViewGroup) lv.getParent()).addView(ll);
            lv.setEmptyView(ll);
        }
    }

    public static void setListEmptyView(Context mContext, LRecyclerView lv, String tip, int res, OnClickListener lsner, int pWidth, int pHeight) {
        LinearLayout ll = new LinearLayout(mContext);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);

        ImageView img = new ImageView(mContext);
        img.setImageResource(res);
        if (pWidth > 0 && pHeight > 0) {
            img.setLayoutParams(new LayoutParams(DevAttr.dip2px(mContext, pWidth), DevAttr.dip2px(mContext, pHeight)));
        } else {
            img.setLayoutParams(new LayoutParams(DevAttr.dip2px(mContext, 200), DevAttr.dip2px(mContext, 200)));
        }
        img.setOnClickListener(lsner);

        TextView emptyView = new TextView(mContext);
        LinearLayout.LayoutParams emptyLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        emptyLp.setMargins(0, DevAttr.dip2px(mContext, 20), 0, 0);
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        emptyView.setTextColor(mContext.getResources().getColor(R.color.g999999));
        emptyView.setText(tip);

        ll.addView(img);
        ll.addView(emptyView, emptyLp);
        ll.setVisibility(View.GONE);
        ll.setTag("empty_tag");

        //-----避免重复加载空提示视图 start------//
        ViewGroup parentGroup = (ViewGroup) lv.getParent();
        boolean hasEmptyView = false;
        for (int i = 0; i < parentGroup.getChildCount(); i++) {
            if (parentGroup.getChildAt(i).getTag() != null && parentGroup.getChildAt(i).getTag().equals("empty_tag")) {
                hasEmptyView = true;
            }
        }
        //-----避免重复加载空提示视图 end------//

        if (!hasEmptyView) { // 不存在空提示视图时才加上去
            ((ViewGroup) lv.getParent()).addView(ll);
            lv.setEmptyView(ll);
        }
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v
                    .getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }


    /**
     * 停止列表进度条
     */
    public static void listStopLoadView(PullToRefreshListView mPullRefreshListView) {
        mPullRefreshListView.onRefreshComplete();
    }

    /**
     * 将上下拉控件设为到底
     */
    public static void setPullToRefreshViewEnd(PullToRefreshListView mPullRefreshListView) {
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    }

    /**
     * 将上下拉控件设为可上下拉
     */
    public static void setPullToRefreshViewBoth(PullToRefreshListView mPullRefreshListView) {
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
    }


    /**
     * 给输入框加小数点后只能输入2位的限制
     *
     * @param edit
     */
    public static void addEditTextLimit2AfterPoint(final EditText edit) {
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        edit.setText(s);
                        edit.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    edit.setText(s);
                    edit.setSelection(2);
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        edit.setText(s.subSequence(0, 1));
                        edit.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 给输入框增加验证按钮是否可以点击
     */
    public static void addJudgeBtnEnableListener(final List<EditText> etList, final Button okBtn) {
        okBtn.setTextColor(Color.parseColor("#FF8989"));
        okBtn.setEnabled(false);
        for (int i = 0; i < etList.size(); i++) {
            EditText edit = etList.get(i);
            edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boolean isOkBtnEnable = true;
                    Log.out("s=======" + s);
                    if (!TextUtils.isEmpty(s)) {
                        for (EditText etItem : etList) {
                            String inputStr = etItem.getText().toString().trim();
                            Log.out("inputStr=======" + inputStr);
                            if (TextUtils.isEmpty(inputStr)) {
                                isOkBtnEnable = false;
                                break;
                            }
                        }
                    } else {
                        Log.out("isOkBtnEnable1=======" + isOkBtnEnable);
                        isOkBtnEnable = false;
                    }
                    Log.out("isOkBtnEnable1=======" + isOkBtnEnable);
                    if (isOkBtnEnable) {
                        okBtn.setTextColor(Color.parseColor("#ffffff"));
                        okBtn.setEnabled(true);
                    } else {
                        okBtn.setTextColor(Color.parseColor("#FF8989"));
                        okBtn.setEnabled(false);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    /**
     * 给输入框增加验证按钮是否可以点击
     */
    public static void addJudgeBtnEnableListener(final List<EditText> etList, final Button okBtn,
                                                 int defTextColor, int focusTextColor, int defBgColor, int focusBgColor, Context context) {
        okBtn.setTextColor(context.getResources().getColor(defTextColor));
        okBtn.setBackgroundColor(context.getResources().getColor(defBgColor));
        okBtn.setEnabled(false);
        for (int i = 0; i < etList.size(); i++) {
            EditText edit = etList.get(i);
            edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boolean isOkBtnEnable = true;
                    if (!TextUtils.isEmpty(s)) {
                        for (EditText etItem : etList) {
                            String inputStr = etItem.getText().toString().trim();
                            if (TextUtils.isEmpty(inputStr)) {
                                isOkBtnEnable = false;
                                break;
                            }
                        }
                    } else {
                        isOkBtnEnable = false;
                    }
                    if (isOkBtnEnable) {
                        okBtn.setTextColor(context.getResources().getColor(focusTextColor));
                        okBtn.setBackgroundColor(context.getResources().getColor(focusBgColor));
                        okBtn.setEnabled(true);
                    } else {
                        okBtn.setTextColor(context.getResources().getColor(defTextColor));
                        okBtn.setBackgroundColor(context.getResources().getColor(defBgColor));
                        okBtn.setEnabled(false);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    /**
     * 给输入框增加验证按钮是否可以点击
     */
    public static void addJudgeBtnAndDelEnableListener(
            final List<EditText> etList, final List<ImageView> delViewList, final Button okBtn) {
        okBtn.setTextColor(Color.parseColor("#FF8989"));
        okBtn.setEnabled(false);
        for (int i = 0; i < etList.size(); i++) {
            EditText edit = etList.get(i);
            final ImageView delImg = delViewList.get(i);
            edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boolean isOkBtnEnable = true;
                    if (!TextUtils.isEmpty(s)) {
                        delImg.setVisibility(View.VISIBLE);
                        for (EditText etItem : etList) {
                            String inputStr = etItem.getText().toString().trim();
                            if (TextUtils.isEmpty(inputStr)) {
                                isOkBtnEnable = false;
                                break;
                            }
                        }
                    } else {
                        delImg.setVisibility(View.INVISIBLE);
                        isOkBtnEnable = false;
                    }
                    if (isOkBtnEnable) {
                        okBtn.setTextColor(Color.parseColor("#ffffff"));
                        okBtn.setEnabled(true);
                    } else {
                        okBtn.setTextColor(Color.parseColor("#FF8989"));
                        okBtn.setEnabled(false);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    /**
     * 给输入框增加验证按钮是否可以点击
     */
    public static void addJudgeBtnEnable2Listener(final List<EditText> etList, final Button okBtn) {
        //okBtn.setTextColor(Color.parseColor("#FF8989"));
        okBtn.setBackgroundResource(R.drawable.rec_unpress_stroke_unpress_solid_corner30);
        okBtn.setEnabled(false);
        for (int i = 0; i < etList.size(); i++) {
            EditText edit = etList.get(i);
            edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boolean isOkBtnEnable = true;
                    Log.out("s=======" + s);
                    if (!TextUtils.isEmpty(s)) {
                        for (EditText etItem : etList) {
                            String inputStr = etItem.getText().toString().trim();
                            Log.out("inputStr=======" + inputStr);
                            if (TextUtils.isEmpty(inputStr)) {
                                isOkBtnEnable = false;
                                break;
                            }
                        }
                    } else {
                        Log.out("isOkBtnEnable1=======" + isOkBtnEnable);
                        isOkBtnEnable = false;
                    }
                    Log.out("isOkBtnEnable1=======" + isOkBtnEnable);
                    if (isOkBtnEnable) {
                        //okBtn.setTextColor(Color.parseColor("#ffffff"));
                        okBtn.setBackgroundResource(R.drawable.rec_deepred_stroke_deepred_solid_corner30);
                        okBtn.setEnabled(true);
                    } else {
                        //okBtn.setTextColor(Color.parseColor("#FF8989"));
                        okBtn.setBackgroundResource(R.drawable.rec_unpress_stroke_unpress_solid_corner30);
                        okBtn.setEnabled(false);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    public static void lineText(TextView tv) {
        tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public static void underlineText(TextView tv) {
        tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
    }

    public static void judgeGoWhere(CmsItem bi, Context context) {
        Log.out("==============CmsItem===========" + bi.toString());
        if (bi.getLinkType() == 1 || bi.getLinkType() == 2) {//外链和内链
            String url = bi.getLinkUrl(); // web address
            Intent intent = new Intent(context, CanBackWebViewActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("picUrl", bi.getLinkPic());
            intent.putExtra("PAGE_TITLE", bi.getTitle());
            context.startActivity(intent);
        } else if (bi.getLinkType() == 3) {//搜索
            try {
                JSONObject jsonObject = new JSONObject(bi.getLinkData());
                int id = jsonObject.optInt("id");
                int level = jsonObject.optInt("level");
                Intent intent = new Intent(context, CourseSearchResultActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("level", level);
                context.startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (bi.getLinkType() == 4) {//活动id
            Intent intent = new Intent(context, ActivityWebViewActivity.class);
            intent.putExtra("activityId", FU.paseInt(bi.getLinkData()));
            intent.putExtra("picUrl", bi.getLinkPic());
            intent.putExtra("PAGE_TITLE", bi.getTitle());
            context.startActivity(intent);
        } else if (bi.getLinkType() == 5) {//内容id
            Intent intent = new Intent(context, CourseDetailsActivity.class);
            intent.putExtra("coursePlanId", FU.paseInt(bi.getLinkData()));
            context.startActivity(intent);
        } else if (bi.getLinkType() == 8) {
//            Intent intent = new Intent(context, ActivityList.class);
//            intent.putExtra("pageId", FU.paseInt(bi.getLinkData()));
//            context.startActivity(intent);
            Intent intent = new Intent(context, FuWuBaoDianNativeActivity.class);
            intent.putExtra("pageId", bi.getLinkData());
            //intent.putExtra("PAGE_TITLE", bi.getTitle());
            context.startActivity(intent);
        } else if (bi.getLinkType() == 9) {
            try {
                JSONObject jsonObject = new JSONObject(bi.getLinkData());
                int id = jsonObject.optInt("id");
                int type = jsonObject.optInt("type");
                switch (type) {
                    case 1://峰会活动
                        Intent intent = new Intent(context, ActivityWebViewActivity.class);
                        intent.putExtra("activityId", id);
                        intent.putExtra("picUrl", bi.getLinkPic());
                        intent.putExtra("PAGE_TITLE", bi.getTitle());
                        context.startActivity(intent);
                        break;
                    case 2://培训活动
                        Intent itt = new Intent(context, ActivityList.class);
                        itt.putExtra("pageId", id);
                        context.startActivity(itt);
                        break;
                    case 3://点赞活动（圣诞）
                        Intent it = new Intent(context, ChristmasActMainActivity.class);
                        it.putExtra("activityId", id);
                        context.startActivity(it);
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (bi.getLinkType() == 10) {//app 内链跳转
            try {
                Uri mUri = Uri.parse(bi.getLinkUrl());
                appUrlGo(mUri, context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (bi.getLinkType() == 13) {//集训活动
            try {
                JSONObject jsonObject = new JSONObject(bi.getLinkData());
                int id = jsonObject.optInt("id");
                Intent intent = new Intent(context, JiXunActDetailActivity.class);
                intent.putExtra("intensifiedTrainingActivityId", id);
                context.startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (bi.getLinkType() == 14) {//分类组件，跳分类展示页
            try {
                JSONObject jsonObject = new JSONObject(bi.getLinkData());
                int id = jsonObject.optInt("id");
                int level = jsonObject.optInt("level");
                Intent intent = new Intent(context, CourseListByCateActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("level", level);
                context.startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (bi.getLinkType() == 0) {//不跳转，无反应
            //nothing to do
        } else {
            Toast.makeText(context, "当前版本不支持该链接，请下载最新版本！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * app 内链跳转
     *
     * @param mUri    内链
     * @param context 上下文
     */
    public static void appUrlGo(Uri mUri, Context context) {
        if ("teach61".equals(mUri.getScheme()) && "serviceRights".equals(mUri.getHost())) {//服务权益
            context.startActivity(new Intent(context, ServiceApplyActivity.class));
        } else if ("teach61".equals(mUri.getScheme()) && "Double11ActDetail".equals(mUri.getHost())) {//双11活动详情
            Intent it = new Intent(context, Double11ActDetailsActivity.class);
            it.putExtra("activityId", FU.paseInt(mUri.getQueryParameter("activityId")));
            context.startActivity(it);
        } else if ("teach61".equals(mUri.getScheme()) && "taskCalendar".equals(mUri.getHost())) {//行事历
            context.startActivity(new Intent(context, WorkPlanMainActivity.class));
        } else if ("teach61".equals(mUri.getScheme()) && "parkApply".equals(mUri.getHost())) {//基地圆申请
            context.startActivity(new Intent(context, ParkApplyActivity.class));
        } else if ("teach61".equals(mUri.getScheme()) && "calenderTaskList".equals(mUri.getHost())) {//订阅版行事历卡片
            Intent it = new Intent(context, MySubsWpsActivity.class);
            it.putExtra("taskCalendarTemplateId", FU.paseInt(mUri.getQueryParameter("id")));
            context.startActivity(it);
        } else if ("teach61".equals(mUri.getScheme()) && "classManagement".equals(mUri.getHost())) {//添加班级
            Intent it = new Intent(context, ClazzMainActivity.class);
            it.putExtra("operateAdd", FU.paseInt(mUri.getQueryParameter("add")));
            context.startActivity(it);
        } else if ("teach61".equals(mUri.getScheme()) && "contackBook".equals(mUri.getHost())) {//家园联系薄
            context.startActivity(new Intent(context, FamilyConBookMainActivity.class));
        } else if ("teach61".equals(mUri.getScheme()) && "open61teach".equals(mUri.getHost())) {//内容
            Intent intent = new Intent(context, CourseDetailsActivity.class);
            intent.putExtra("coursePlanId", FU.paseInt(mUri.getQueryParameter("id")));
            context.startActivity(intent);
        } else if ("teach61".equals(mUri.getScheme()) && "campServiceDetail".equals(mUri.getHost())) {//集训
            Intent intent = new Intent(context, JiXunActDetailActivity.class);
            intent.putExtra("intensifiedTrainingActivityId", FU.paseInt(mUri.getQueryParameter("id")));
            context.startActivity(intent);
        } else if ("teach61".equals(mUri.getScheme()) && "ServiceReport".equals(mUri.getHost())) {//提报申请
            Intent intent = new Intent(context, MyTrainApplyActivity.class);
            context.startActivity(intent);
        } else if ("teach61".equals(mUri.getScheme()) && "physicalFitnessTest".equals(mUri.getHost())) {//悦来接入
            Intent it = new Intent(context, PhysicalFitnessTestWvActivity.class);
            it.putExtra("PAGE_TITLE", "体能测试");
            if (GlobalParam.currentUser == null || TextUtils.isEmpty(GlobalParam.currentUser.getMobile())) {
                return;
            }
            it.putExtra("url", "http://61park.imreliable.net/?UserName=" + GlobalParam.currentUser.getMobile());
            context.startActivity(it);
        } else {
            Toast.makeText(context, "当前版本不支持该链接，请下载最新版本！", Toast.LENGTH_SHORT).show();
        }
    }

    public static void judgeIsShow(Activity activity, JSONObject jsonResult) {
        if (jsonResult.toString().contains("cmsTopBanner")) {
            activity.findViewById(R.id.banner_area).setVisibility(View.VISIBLE);
        } else {
            activity.findViewById(R.id.banner_area).setVisibility(View.GONE);
        }

        if (jsonResult.toString().contains("cmsFastGoTo")) {
            activity.findViewById(R.id.area_banner2).setVisibility(View.VISIBLE);
        } else {
            activity.findViewById(R.id.area_banner2).setVisibility(View.GONE);
        }

        if (jsonResult.toString().contains("cmsMiddleBannerOne")) {
            activity.findViewById(R.id.area_banner3_out).setVisibility(View.VISIBLE);
        } else {
            activity.findViewById(R.id.area_banner3_out).setVisibility(View.GONE);
        }

        if (jsonResult.toString().contains("cmsGoldTeacher")) {
            activity.findViewById(R.id.area_gold_teacher).setVisibility(View.VISIBLE);
        } else {
            activity.findViewById(R.id.area_gold_teacher).setVisibility(View.GONE);
        }

        if (jsonResult.toString().contains("cmsMiddleBannerTwo")) {
            activity.findViewById(R.id.area_mid_banner2).setVisibility(View.VISIBLE);
        } else {
            activity.findViewById(R.id.area_mid_banner2).setVisibility(View.GONE);
        }

        if (jsonResult.toString().contains("cmsGuessYourLove")) {
            activity.findViewById(R.id.area_date_show).setVisibility(View.VISIBLE);
        } else {
            activity.findViewById(R.id.area_date_show).setVisibility(View.GONE);
        }

        if (jsonResult.toString().contains("cmsMoreContent")) {
            activity.findViewById(R.id.area_show).setVisibility(View.VISIBLE);
        } else {
            activity.findViewById(R.id.area_show).setVisibility(View.GONE);
        }
    }

    /**
     * 初始化点击可放大图片的详情webview
     *
     * @param wvContent
     */
    public static void initShowimgWebview(ShowImageWebView wvContent) {
        wvContent.getSettings().setJavaScriptEnabled(true);
        wvContent.getSettings().setDefaultTextEncodingName("UTF -8");
        wvContent.setWebViewClient(new WebViewClient() {
            // 网页跳转
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            // 网页加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // web 页面加载完成，添加监听图片的点击 js 函数
                wvContent.setImageClickListner();
                //解析 HTML
                wvContent.parseHTML(view);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(wvContent.getContext(), "请检查您的网络设置", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void setWebData(WebView webview, String content) {
        String htmlDetailsStr = "";
        try {
            htmlDetailsStr = HtmlParserTool.replaceImgStr(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String baseUrl = "file:///android_asset";
        webview.loadDataWithBaseURL(baseUrl, htmlDetailsStr, "text/html", "utf-8", null);
    }

    /**
     * 获取当前资源的下载状态
     *
     * @param sourceId 资源id
     * @return 下载状态
     */
    public static int getSourceItemDownLoadStatus(int sourceId, String itemId) {
        List<DownloadTask> downloadTaskList = DownloadDAO.getInstance().selectContentList(itemId);
        for (int i = 0; i < downloadTaskList.size(); i++) {
            if (FU.paseInt(downloadTaskList.get(i).getSourceId()) == sourceId) {
                return downloadTaskList.get(i).getTask_status();//0未完成, 1完成, 2下载中
            }
        }
        return -1;//-1未下载
    }

    /**
     * 获取当前附件的下载状态
     *
     * @param sourceUrl 资源url
     * @return 下载状态
     */
    public static int getFilesItemDownLoadStatus(String sourceUrl) {
        List<Progress> progressList = DownloadManager.getInstance().getAll();
        for (int i = 0; i < progressList.size(); i++) {
            Progress curProgress = progressList.get(i);
            if (curProgress.url.equals(sourceUrl)) {
                if (curProgress.status == Progress.FINISH) {
                    return 1;//1完成
                } else {
                    return 0;//0下载中
                }
            }
        }
        return -1;//-1未下载
    }

    /**
     * 友盟埋点
     */
    public static void AddStatistics(Context context, String type) {
        if ("contentdetails".equals(type)) {
            MobclickAgent.onEvent(context, "contentdetails");
        } else if ("contentshare".equals(type)) {
            MobclickAgent.onEvent(context, "contentshare");
        } else if ("ClicktaskCalendar".equals(type)) {
            MobclickAgent.onEvent(context, "ClicktaskCalendar");
        }
    }

    public static void AddStatistics(Context mContext, int id, String type) {
        if ("guess".equals(type)) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("target", "LV_" + id);
            MobclickAgent.onEvent(mContext, "clicklovely", map);
        } else if ("more".equals(type)) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("target", "TB_" + "TM_" + id);
            MobclickAgent.onEvent(mContext, "ClickTheMore", map);
        } else if ("home".equals(type)) {
            MobclickAgent.onEvent(mContext, "Home");
            MobclickAgent.onEvent(mContext, "ClickOnHome");
        } else if ("search".equals(type)) {
            HashMap<String, String> map = new HashMap<String, String>();
            MobclickAgent.onEvent(mContext, "ClickSearch", map);
        } else if ("goldTeach".equals(type)) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("target", "GT_" + id);
            MobclickAgent.onEvent(mContext, "clickgoldtrainer", map);
        }
    }

    public static void AddStatistics(Context context, CmsItem bi, boolean isBanner) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (1 == bi.getLinkType() || 2 == bi.getLinkType()) {
            map.put("target", "TB_" + bi.getLinkUrl());
        } else if (3 == bi.getLinkType()) {//分类
            try {
                JSONObject jsonObject = new JSONObject(bi.getLinkData());
                map.put("target", "TB_CLA_" + jsonObject.optInt("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (4 == bi.getLinkType()) {//活动
            map.put("target", "TB_ACT_" + bi.getLinkData());
        } else {//内容
            map.put("target", "TB_CNT_" + bi.getLinkData());
        }
        if (isBanner) {
            MobclickAgent.onEvent(context, "ClickMiddleBanner2", map);
        } else {
            MobclickAgent.onEvent(context, "clickentrance", map);
        }
    }

    public static void addYouMengPoint(Context context, CmsItem bi, String youmengKey) {
        Log.out("===============addYouMengPoint===============CLICK_CATEGORY============" + CLICK_CATEGORY);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("target", bi.getLinkData());
        MobclickAgent.onEvent(context, youmengKey, map);
        MobclickAgent.onEvent(context, "clickcategory");
    }
}
