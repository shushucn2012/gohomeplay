package com.park61.teacherhelper.module.workplan;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.activity.bean.ShareInfoBean;
import com.park61.teacherhelper.module.clazz.FamilyConBookMainActivity;
import com.park61.teacherhelper.module.course.SubCourseVideoActivity;
import com.park61.teacherhelper.module.home.CourseDetailsActivity;
import com.park61.teacherhelper.module.member.MemberMainActivity;
import com.park61.teacherhelper.module.workplan.bean.SubsTaskBean;
import com.park61.teacherhelper.module.workplan.bean.WorkPlanCateBean;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.listview.ListViewForScrollView;
import com.park61.teacherhelper.widget.pw.CalandarMainTipPopWin;
import com.park61.teacherhelper.widget.pw.MySubsWorkplanTipPopWin;
import com.park61.teacherhelper.widget.pw.SubsWpsCatalogPopWin;
import com.park61.teacherhelper.widget.webview.ShowImageWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的订阅行事历左右滑动列表
 *
 * @author shubei
 * @time 2018/12/20 17:26
 */
public class MySubsWpsActivity extends BaseActivity {

    private ViewPager vp_subs_wps;//viewpager
    private View area_temp_intro;//介绍view
    private View shadow_card;//为了显示阴影，放在area_temp_intro后面的视图，可见性与其相同
    private Button btn_subs;
    private SubsWpsCatalogPopWin mSubsWpsCatalogPopWin;
    //private TextView tv_temp_intro;
    private ShowImageWebView showImageWebView;
    private TextView tv_name;

    private List<View> viewList;
    private List<SubsTaskBean> tList = new ArrayList<>();
    private int taskCalendarTemplateId;
    private WorkPlanCateBean workPlanCateBean;
    private String isSubs;//是否已订阅
    private String isVisible;//该用户是否可见

    private int count = 0;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_mysubs_wps);
    }

    @Override
    public void initView() {
        setPagTitle("方案名称");
        area_temp_intro = findViewById(R.id.area_temp_intro);
        shadow_card = findViewById(R.id.shadow_card);
        vp_subs_wps = findViewById(R.id.vp_subs_wps);
        btn_subs = findViewById(R.id.btn_subs);
        //tv_temp_intro = findViewById(R.id.tv_temp_intro);
        showImageWebView = findViewById(R.id.wv_content);
        tv_name = findViewById(R.id.tv_name);
    }

    @Override
    public void initData() {
        taskCalendarTemplateId = getIntent().getIntExtra("taskCalendarTemplateId", -1);
        asyncIsSubscribeCalendar();
    }

    @Override
    public void initListener() {
        //((ImageView) findViewById(R.id.img_right)).setImageResource(R.mipmap.icon_circle_share);
        //area_right.setVisibility(View.VISIBLE);
       /* area_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (workPlanCateBean == null)
                    return;
                ShareInfoBean shareInfoBean = new ShareInfoBean();
                String shareUrl = "http://m.61park.cn/teach/#/share/introduce/" + taskCalendarTemplateId;
                shareInfoBean.setShareUrl(shareUrl);
                shareInfoBean.setDescription(workPlanCateBean.getName());
                shareInfoBean.setPicUrl(workPlanCateBean.getCoverUrl());
                shareInfoBean.setTitle("行事历");
                showShareDialog(shareInfoBean);
            }
        });*/
        btn_subs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncSubs();
            }
        });
    }

    /**
     * 创建每一页的视图
     */
    private View createNewView(int index) {
        LayoutInflater lf = getLayoutInflater().from(this);
        View mView = null;
        if (index == 0) {
            mView = lf.inflate(R.layout.item_mysubs_wps_start, null);
            //TextView tv_temp_intro = mView.findViewById(R.id.tv_temp_intro);
            //tv_temp_intro.setText(workPlanCateBean.getIntro());
            TextView tv_name_start = mView.findViewById(R.id.tv_name_start);
            tv_name_start.setText(workPlanCateBean.getName());
            judgeAndChangeTextViewSize(tv_name_start);

            ShowImageWebView wv_content_start = mView.findViewById(R.id.wv_content_start);
            ViewInitTool.initShowimgWebview(wv_content_start);
            ViewInitTool.setWebData(wv_content_start, workPlanCateBean.getIntro());
            mView.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int nextPage = 0;
                    for (int i = 0; i < tList.size(); i++) {
                        if (tList.get(i).getStatus() == 0) {
                            nextPage = i;
                            break;
                        }
                    }
                    vp_subs_wps.setCurrentItem(nextPage + 1);
                }
            });
        } else {
            int vpPosition = index - 1;//加了一个开始页，内容往后移
            mView = lf.inflate(R.layout.item_mysubs_wps, null);
            TextView tv_no = mView.findViewById(R.id.tv_no);
            TextView tv_name = mView.findViewById(R.id.tv_name);
            TextView tv_intro = mView.findViewById(R.id.tv_intro);
            ListViewForScrollView lv_tasks = mView.findViewById(R.id.lv_tasks);
            Button btn_finish = mView.findViewById(R.id.btn_finish);
            tv_no.setText("NO." + index);
            tv_name.setText(tList.get(vpPosition).getTemplateDetailName());
            judgeAndChangeTextViewSize(tv_name);
            tv_intro.setText(tList.get(vpPosition).getTemplateDetailIntro());
            if (!CommonMethod.isListEmpty(tList.get(vpPosition).getSubscribeTaskCalendarTemplateDetailResources())) {
                lv_tasks.setAdapter(new ResourceListAdapter(tList.get(vpPosition).getSubscribeTaskCalendarTemplateDetailResources()));
                mView.findViewById(R.id.area_resources).setVisibility(View.VISIBLE);
            } else {
                lv_tasks.setAdapter(new ResourceListAdapter(new ArrayList<>()));
                mView.findViewById(R.id.area_resources).setVisibility(View.GONE);
            }
            if (tList.get(vpPosition).getStatus() == 0) {
                btn_finish.setText("完成");
                btn_finish.setBackgroundResource(R.mipmap.btn_bg_wps_subs);
                btn_finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        asyncFinishSubscribeTask(index);
                    }
                });
            } else {
                btn_finish.setText("已完成");
                btn_finish.setBackgroundResource(R.mipmap.btn_bg_wps_unsubs);
                btn_finish.setOnClickListener(null);
            }
        }
        return mView;
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private List<View> mListViews;

        public MyViewPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;//构造方法，参数是我们的页卡，这样比较方便。
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));//删除页卡
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
            container.addView(mListViews.get(position), 0);//添加页卡
            return mListViews.get(position);
        }

        @Override
        public int getCount() {
            return mListViews.size();//返回页卡的数量
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;//官方提示这样写
        }
    }

    /**
     * 请求用户是否已订阅行事历模板
     */
    private void asyncIsSubscribeCalendar() {
        String url = AppUrl.host + AppUrl.isSubscribeCalendar;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskCalendarTemplateId", taskCalendarTemplateId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestBodyData, 0, clistener);
    }

    BaseRequestListener clistener = new JsonRequestListener() {
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
            isSubs = jsonResult.optString("isSubscribeCalendar");
            isVisible = jsonResult.optString("isVisibleCalendar");
            if ("1".equals(isSubs) && "1".equals(isVisible)) {//已经订阅，并且可见
                vp_subs_wps.setVisibility(View.VISIBLE);
                area_temp_intro.setVisibility(View.GONE);
                shadow_card.setVisibility(View.GONE);
                showRightCatalogBtn();
            } else if ("1".equals(isSubs) && "0".equals(isVisible)) {//已经订阅，但不可见，没有权限或者权限被取消
                vp_subs_wps.setVisibility(View.GONE);
                area_temp_intro.setVisibility(View.VISIBLE);
                shadow_card.setVisibility(View.VISIBLE);
                btn_subs.setText("开始");
                btn_subs.setOnClickListener(null);
            } else {//未订阅
                vp_subs_wps.setVisibility(View.GONE);
                area_temp_intro.setVisibility(View.VISIBLE);
                shadow_card.setVisibility(View.VISIBLE);
            }
            asyncDetail();
        }
    };

    /**
     * 请求行事历模板详情
     */
    private void asyncDetail() {
        String url = AppUrl.host + AppUrl.subscribeCalendarTemplateDetail;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskCalendarTemplateId", taskCalendarTemplateId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestBodyData, 0, dlistener);
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
            workPlanCateBean = gson.fromJson(jsonResult.toString(), WorkPlanCateBean.class);
            //tv_temp_intro.setText(workPlanCateBean.getIntro());
            showRightShareBtn();
            tv_name.setText(workPlanCateBean.getName());
            judgeAndChangeTextViewSize(tv_name);
            ViewInitTool.initShowimgWebview(showImageWebView);
            ViewInitTool.setWebData(showImageWebView, workPlanCateBean.getIntro());
            asyncGetSubscribeTaskCalendarList(2);
        }
    };

    /**
     * 订阅
     */
    private void asyncSubs() {
        String url = AppUrl.host + AppUrl.subscribeCalendar;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskCalendarTemplateId", taskCalendarTemplateId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestBodyData, 0, slistener);
    }

    BaseRequestListener slistener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            if ("0000043008".equals(errorCode)) {//未开通
                dDialog.showDialog("提示", errorMsg, "取消", "去开通",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dDialog.dismissDialog();
                                finish();
                                ;
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dDialog.dismissDialog();
                                startActivity(new Intent(mContext, MemberMainActivity.class));
                                finish();
                            }
                        });
            } else {
                showShortToast(errorMsg);
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            showShortToast("订阅成功");
            vp_subs_wps.setVisibility(View.VISIBLE);
            area_temp_intro.setVisibility(View.GONE);
            shadow_card.setVisibility(View.GONE);
            showRightCatalogBtn();
            asyncGetSubscribeTaskCalendarList(1);
        }
    };

    /**
     * 任务列表
     * preStep 上一步是什么，1，订阅成功后拉去列表；2，直接拉取列表
     */
    private void asyncGetSubscribeTaskCalendarList(int preStep) {
        String url = AppUrl.host + AppUrl.subscribeTaskCalendarList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskCalendarTemplateId", taskCalendarTemplateId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestBodyData, preStep, tlistener);
    }

    BaseRequestListener tlistener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            JSONArray arr = jsonResult.optJSONArray("list");
            if (arr != null && arr.length() > 0) {
                tList.clear();
                for (int i = 0; i < arr.length(); i++) {
                    SubsTaskBean subsTaskBean = gson.fromJson(arr.optJSONObject(i).toString(), SubsTaskBean.class);
                    tList.add(subsTaskBean);
                }
                //vp_subs_wps.setPageTransformer(true, new ZoomOutPageTransformer());
                vp_subs_wps.setPageMargin(getResources().getDimensionPixelSize(R.dimen.page_margin));
                vp_subs_wps.setOffscreenPageLimit(tList.size());

                viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
                for (int i = 0; i < tList.size() + 1; i++) {
                    View questionView = createNewView(i);
                    viewList.add(questionView);
                }
                vp_subs_wps.setAdapter(new MyViewPagerAdapter(viewList));
                vp_subs_wps.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (position == 0) {
                            showRightShareBtn();
                        } else {
                            showRightCatalogBtn();
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
            dismissDialog();
            if (requestId == 1) {//订阅成功后判断是否需要弹层提示,第一次拉取任务列表
                showFirstSubTips();
            } else {//非第一次拉取任务列表，判断是否有更新
                if ("1".equals(isSubs) && "1".equals(isVisible)) {
                    asyncTaskCalendarIsUpdate();
                }
            }
        }
    };

    /**
     * 判断内容是否有更新
     */
    private void asyncTaskCalendarIsUpdate() {
        String url = AppUrl.host + AppUrl.taskCalendarIsUpdate;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskCalendarTemplateId", taskCalendarTemplateId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestBodyData, 0, ilistener);
    }

    BaseRequestListener ilistener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            int isUpdate = jsonResult.optInt("isUpdate");
            if (isUpdate != 0) {//有更新
                dDialog.showDialog("提示", "内容新增啦！一键更新，领取新增内容吧。", "暂不更新", "一键更新",
                        null, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dDialog.dismissDialog();
                                asyncUpdateTaskCalendar();
                            }
                        });
            }
        }
    };

    /**
     * 全量更新个人下的所有任务
     */
    private void asyncUpdateTaskCalendar() {
        String url = AppUrl.host + AppUrl.updateTaskCalendar;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskCalendarTemplateId", taskCalendarTemplateId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestBodyData, 0, new JsonRequestListener() {
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
                showShortToast("更新成功！");
                //重新拉取任务列表
                asyncGetSubscribeTaskCalendarList(2);
            }
        });
    }

    /**
     * 请求完成任务
     */
    private void asyncFinishSubscribeTask(int index) {
        String url = AppUrl.host + AppUrl.finishSubscribeTask;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", tList.get(index - 1).getTaskCalendarId());
        map.put("status", 0);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestBodyData, index, flistener);
    }

    BaseRequestListener flistener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
        }

        @Override
        public void onSuccess(int index, String url, JSONObject jsonResult) {
            dismissDialog();
            tList.get(index - 1).setStatus(1);
            Button btn = viewList.get(index).findViewById(R.id.btn_finish);
            btn.setText("已完成");
            btn.setBackgroundResource(R.mipmap.btn_bg_wps_unsubs);
            btn.setOnClickListener(null);
        }
    };

    /**
     * 顶部右侧设置为目录按钮
     */
    public void showRightCatalogBtn() {
        setPagTitle(workPlanCateBean == null ? "" : workPlanCateBean.getName());
        area_right.setVisibility(View.VISIBLE);
        ((ImageView) findViewById(R.id.img_right)).setImageResource(R.mipmap.calendar_main_menu);
        area_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopFormBottom();
            }
        });
    }

    /**
     * 顶部右侧设置为分享按钮
     */
    public void showRightShareBtn() {
        setPagTitle("方案名称");
        area_right.setVisibility(View.INVISIBLE);
        //((ImageView) findViewById(R.id.img_right)).setImageResource(R.mipmap.icon_circle_share);
       /* area_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (workPlanCateBean == null)
                    return;
                ShareInfoBean shareInfoBean = new ShareInfoBean();
                String shareUrl = "http://m.61park.cn/teach/#/share/introduce/" + taskCalendarTemplateId;
                shareInfoBean.setShareUrl(shareUrl);
                shareInfoBean.setDescription(workPlanCateBean.getName());
                shareInfoBean.setPicUrl(workPlanCateBean.getCoverUrl());
                shareInfoBean.setTitle("行事历");
                showShareDialog(shareInfoBean);
            }
        });*/
    }

    public void showPopFormBottom() {
        mSubsWpsCatalogPopWin = new SubsWpsCatalogPopWin(mContext, tList);
        // 设置Popupwindow显示位置（从底部弹出）
        mSubsWpsCatalogPopWin.showAtLocation(findViewById(R.id.main_view), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        final WindowManager.LayoutParams[] params = {getWindow().getAttributes()};
        //当弹出Popupwindow时，背景变半透明
        params[0].alpha = 0.7f;
        getWindow().setAttributes(params[0]);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        mSubsWpsCatalogPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params[0] = getWindow().getAttributes();
                params[0].alpha = 1f;
                getWindow().setAttributes(params[0]);
            }
        });
        mSubsWpsCatalogPopWin.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vp_subs_wps.setCurrentItem(position + 1);//多了一个开始页，全部往后移一位
                mSubsWpsCatalogPopWin.dismiss();
            }
        });
    }

    public class ResourceListAdapter extends BaseAdapter {

        private List<SubsTaskBean.SubscribeTaskCalendarTemplateDetailResourcesBean> resourceList;

        public ResourceListAdapter(List<SubsTaskBean.SubscribeTaskCalendarTemplateDetailResourcesBean> _list) {
            this.resourceList = _list;
        }

        @Override
        public int getCount() {
            return resourceList.size();
        }

        @Override
        public Object getItem(int position) {
            return resourceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_mysubs_wps_sources, null);
                holder = new ViewHolder();
                holder.tv_title = convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            SubsTaskBean.SubscribeTaskCalendarTemplateDetailResourcesBean item = resourceList.get(position);
            holder.tv_title.setText(item.getSourceName());
            ViewInitTool.underlineText(holder.tv_title);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //资源类型[0内容1课程2工具]
                    if (item.getSourceType() == 0) {
                        //跳转到内容详情页
                        Intent it = new Intent(mContext, CourseDetailsActivity.class);
                        it.putExtra("coursePlanId", item.getRelationId());
                        startActivity(it);
                    } else if (item.getSourceType() == 1) {
                        //跳转到课程详情页
                        Intent it = new Intent(mContext, SubCourseVideoActivity.class);
                        it.putExtra("subCourseId", item.getRelationId());
                        it.putExtra("taskCalendarId", item.getTaskCalendarTemplateDetailId());
                        startActivity(it);
                    } else {
                        //跳转到家园联系薄
                        //Intent it = new Intent(mContext, FamilyConBookMainActivity.class);
                        //startActivity(it);
                        ViewInitTool.appUrlGo(Uri.parse(item.getSourceUrl()), mContext);
                    }
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView tv_title;
        }
    }

    /**
     * 判断文字长度，如果大于一行，缩小文字大小，最多2行显示
     *
     * @param textView
     */
    private void judgeAndChangeTextViewSize(TextView textView) {
        textView.post(new Runnable() {
            @Override
            public void run() {
                Layout l = textView.getLayout();
                if (l != null) {
                    int lines = l.getLineCount();
                    logout("lines = " + lines);
                    if (lines > 0) {
                        if (l.getEllipsisCount(lines - 1) > 0) {
                            logout("Text is ellipsized");
                            textView.setMaxLines(2);
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        }
                    }
                } else {
                    logout("Layout is null");
                }
            }
        });
    }

    /**
     * 第一次订阅成功蒙层提示
     */
    public void showFirstSubTips() {
        // 幼儿园用户首页，判断是否是第一次进入，给出引导提示
        SharedPreferences isFirstRunSp = getSharedPreferences("isFirstWorkplanSubFlag", Activity.MODE_PRIVATE);
        boolean isFirstRun = isFirstRunSp.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            MySubsWorkplanTipPopWin tipPopWin = new MySubsWorkplanTipPopWin(mContext);
            tipPopWin.showAsDropDown(findViewById(R.id.top_line));
            SharedPreferences sp = getSharedPreferences("isFirstWorkplanSubFlag", Activity.MODE_PRIVATE);
            SharedPreferences.Editor mEditor = sp.edit();
            mEditor.putBoolean("isFirstRun", false);
            mEditor.commit();
        }
    }

}
