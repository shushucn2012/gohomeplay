package com.park61.teacherhelper.module.course;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.databinding.ActivityCourseListBinding;
import com.park61.teacherhelper.module.course.adapter.SubCourseAdapter;
import com.park61.teacherhelper.module.course.bean.SubCourseListBean;
import com.park61.teacherhelper.module.home.ExpertCoursePaysActivity;
import com.park61.teacherhelper.module.home.TeachHouseActivity;
import com.park61.teacherhelper.module.home.bean.TeacherCourseBean;
import com.park61.teacherhelper.module.member.MemberMainActivity;
import com.park61.teacherhelper.module.pay.bean.CouponBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.AutoLinefeedLayout;
import com.park61.teacherhelper.widget.dialog.CouponChooseGetPopWin;
import com.park61.teacherhelper.widget.listview.ObservableScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenlie on 2018/3/22.
 * <p>
 * 专家分销系列课程首页详情(含课时列表)界面
 */

public class ExpertCourseListActivity extends BaseActivity {


    private ActivityCourseListBinding bind;
    private AutoLinefeedLayout view_wordwrap;
    private WindowManager.LayoutParams params;
    private View area_get_coupon;
    private CouponChooseGetPopWin mCouponChooseGetPopWin;

    private int courseId;
    private TeacherCourseBean bean;
    private boolean isShowAll = true;
    private boolean isFollow = false;
    private boolean isLeft = false;
    private List<SubCourseListBean> list;
    private SubCourseAdapter adapter;
    private int relativeTop, relativeHeight;
    private boolean isFromTeachHouse = false;
    private List<CouponBean> couponList;

    @Override
    public void setLayout() {
        bind = DataBindingUtil.setContentView(this, R.layout.activity_course_list);
    }

    @Override
    public void initView() {
        bind.openCourse.setEnabled(false);
        bind.setVisible(View.VISIBLE);
        bind.setGone(View.GONE);
        bind.setLeftSelect(isLeft = true);
        view_wordwrap = (AutoLinefeedLayout) findViewById(R.id.view_wordwrap);
        area_get_coupon = findViewById(R.id.area_get_coupon);

        if (GlobalParam.currentUser.getIsMember() > 0) {//黄金会员,不显示vip广告
            findViewById(R.id.img_to_open_vip).setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {
        courseId = getIntent().getIntExtra("courseId", -1);
        isFromTeachHouse = getIntent().getBooleanExtra("fromTeachHouse", false);
        list = new ArrayList<>();
        adapter = new SubCourseAdapter(mContext, list);
        bind.coursesLv.setAdapter(adapter);
        bind.coursesLv.setVerticalScrollBarEnabled(false);
        bind.coursesLv.setFastScrollEnabled(false);
        relativeTop = DevAttr.dip2px(this, 350);
        relativeHeight = DevAttr.dip2px(this, 69);
        couponList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncCourseDetail();
        asyncCouponList();
    }

    @Override
    public void initListener() {
        //关闭当前页面
        bind.setBackClick(v -> finish());
        //分享课程
        bind.setShareClick(v -> share());
        //展示全部课程介绍
        bind.showAllBt.setOnClickListener(v -> {
            if (bean == null) return;
            ViewGroup.LayoutParams p = bind.introWv.getLayoutParams();
            if (isShowAll) {
                bind.showAllBt.setText("收起内容");
                //设置webview wrap_content
                p.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                bind.showAllArrow.setRotation(180);
            } else {
                //设置webview 100dp
                p.height = DevAttr.dip2px(mContext, 100);
                bind.showAllBt.setText("展开内容");
                bind.showAllArrow.setRotation(0);
            }
            bind.introWv.setLayoutParams(p);
            isShowAll = !isShowAll;
        });
        //关注专家
        bind.followTv.setOnClickListener(v -> {
            if (isFollow) {
                unfollow();
            } else {
                followExpert();
            }
        });

        //滚动显示title监听
        bind.totalSv.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(int x, int y, int oldx, int oldy) {
                if (y > relativeTop) {
                    //简介课程一栏快到顶部
                    int[] a = new int[2];
                    bind.floatTitle.getLocationInWindow(a);
                    if (a[1] > relativeHeight) {
                        //隐藏
                        bind.virtualFloatTitle.setVisibility(View.GONE);
                    } else {
                        //显示
                        bind.virtualFloatTitle.setVisibility(View.VISIBLE);
                    }

                    bind.setLeftSelect(isLeft = (y < bind.bottomL.getTop() - DevAttr.dip2px(mContext, 90)));
                } else if (y > 20) {
                    //显示title
                    bind.virtualTitle.setVisibility(View.VISIBLE);
                    bind.virtualFloatTitle.setVisibility(View.GONE);
                } else {
                    bind.virtualTitle.setVisibility(View.GONE);
                    bind.virtualFloatTitle.setVisibility(View.GONE);
                }
            }
        });
        //底部按钮点击事件
        bind.openCourse.setOnClickListener(v -> {
            if (bean == null) {
                return;
            }
            //已购买或者免费或者会员
            if (bean.getChargeType() != 1 || bean.getBuyStatus() != 0 || GlobalParam.currentUser.getIsMember() > -1) {
                //听课
                if (list.size() > 0)
                    gotoDetail(0);
            } else {
                //去购买
                GlobalParam.ORDER_TYPE = "expert_course";
                Intent it = new Intent(mContext, ExpertCoursePaysActivity.class);
                it.putExtra("courseSeriesId", courseId);
                startActivity(it);
            }
        });
        //课时列表点击事件
        bind.coursesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //去课时详情页面播放音视频
                if (bean != null) {
                    //试看，免费，已购买，会员
                    if (list.get(position).getIsProbation() == 1 || bean.getChargeType() != 1 || bean.getBuyStatus() != 0 || GlobalParam.currentUser.getIsMember() > -1) {
                        gotoDetail(position);
                    } else {
                        showShortToast("请先购买课程");
                    }
                }
            }
        });

        bind.setLeftClick(v -> {
            if (!isLeft) {
                bind.setLeftSelect(isLeft = true);
                bind.totalSv.smoothScrollTo(0, bind.floatTitle.getTop() - DevAttr.dip2px(mContext, 45));
            }
        });

        bind.setRightClick(v -> {
            if (isLeft) {
                bind.setLeftSelect(isLeft = false);
                bind.totalSv.smoothScrollTo(0, bind.bottomL.getTop() - DevAttr.dip2px(mContext, 45));
            }
        });

        bind.expertName.setOnClickListener(v -> {
            //点击专家姓名，判断是否从专家首页跳转
            if (!isFromTeachHouse && bean != null) {
                //去专家首页
                Intent intent = new Intent(mContext, TeachHouseActivity.class);
                intent.putExtra("teachId", bean.getTrainerId());
                startActivity(intent);
            } else {
                finish();
            }
        });
        area_get_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopFormBottom();
            }
        });
        findViewById(R.id.img_to_open_vip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MemberMainActivity.class));
            }
        });
    }

    private void setCouponWarp() {
        view_wordwrap.removeAllViews();
        for (int i = 0; i < couponList.size(); i++) {
            LinearLayout ll = new LinearLayout(mContext);
            ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DevAttr.dip2px(mContext, 22)));
            ll.setGravity(Gravity.CENTER_HORIZONTAL);
            ll.setPadding(0, 0, 0, DevAttr.dip2px(mContext, 6));

            TextView textview = new TextView(mContext);
            textview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DevAttr.dip2px(mContext, 15)));
            String words = "满" + couponList.get(i).getRuleValue1() + "减" + couponList.get(i).getRuleValue2();
            if (words.length() > 20) {
                words = words.substring(0, 20) + "...";
            }
            textview.setText(words);
            textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            textview.setTextColor(getResources().getColor(R.color.gfb4364));
            textview.setBackgroundResource(R.drawable.coupon_bg);
            textview.setPadding(DevAttr.dip2px(mContext, 8), 0, DevAttr.dip2px(mContext, 8), 0);
            textview.setGravity(Gravity.CENTER);
            ll.addView(textview);

            View view = new View(mContext);
            LinearLayout.LayoutParams linearParams2 = new LinearLayout.LayoutParams(DevAttr.dip2px(mContext, 2), DevAttr.dip2px(mContext, 2));
            view.setLayoutParams(linearParams2);
            ll.addView(view);

            view_wordwrap.addView(ll);
        }
    }

    private void gotoDetail(int position) {
        if (bean.getForm() == 0) {
            //音频
            Intent it = new Intent(mContext, SubCourseAudioActivity.class);
            it.putExtra("subCourseId", list.get(position).getId());
            startActivity(it);
        } else if (bean.getForm() == 1) {
            //视频
            Intent it = new Intent(mContext, SubCourseVideoActivity.class);
            it.putExtra("subCourseId", list.get(position).getId());
            startActivity(it);
        }
    }

    private void share() {
        if (bean != null) {
            showShareDialog("http://m.61park.cn/teach/#/expert/index/" + courseId,
                    bean.getListCover(), bean.getTitle(), bean.getDigest());
        }
    }

    /**
     * 查询优惠券列表数据
     */
    private void asyncCouponList() {
        String wholeUrl = AppUrl.host + AppUrl.courseReceiveCouponList;//"http://10.10.10.18:8380/service/coupon/courseReceiveCouponList";//
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("courseSeriesId", courseId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
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
            couponList.clear();
            JSONArray actJay = jsonResult.optJSONArray("list");
            if (actJay != null && actJay.length() > 0) {
                for (int i = 0; i < actJay.length(); i++) {
                    JSONObject couponJot = actJay.optJSONObject(i);
                    CouponBean g = gson.fromJson(couponJot.toString(), CouponBean.class);
                    couponList.add(g);
                }
                setCouponWarp();
                area_get_coupon.setVisibility(View.VISIBLE);
                if (mCouponChooseGetPopWin != null) {
                    mCouponChooseGetPopWin.getmCouponChooseGetListAdapter().notifyDataSetChanged();
                }
            } else {
                area_get_coupon.setVisibility(View.GONE);
            }
        }
    };

    /**
     * 课程首页详情
     */
    private void asyncCourseDetail() {
        String url = AppUrl.host + AppUrl.getCourseDetailById;
        Map<String, Object> map = new HashMap<>();
        map.put("trainerCourseSeriesId", courseId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, detailListener);
    }

    BaseRequestListener detailListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            bean = gson.fromJson(jsonResult.toString(), TeacherCourseBean.class);
            fillData();
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
        }
    };

    private void fillData() {

        if (bean != null) {
            bind.openCourse.setEnabled(true);
            //大图
            ImageManager.getInstance().displayImg(bind.topImg, bean.getDetailsCover());
            //标题
            bind.setTitleText(bean.getTitle());
            if (bean.getType() == 1) {
                //系列课,显示更新课时
                bind.xilieTv.setVisibility(View.VISIBLE);
                bind.xilieTv.setText("系列课 已更新至" + bean.getCourseTotal() + " | 共" + bean.getCourseNum() + "课时");
            }
            //查询课时列表
            asyncGetCourseList();
            //售价，原价
            if (bean.getChargeType() == 0) {
                //免费
                bind.salePrice.setText("免费");
                bind.originPrice.setVisibility(View.GONE);
            } else {
                bind.salePrice.setText("￥" + bean.getSalePrice());
                bind.originPrice.setText("￥" + bean.getOriginalPrice());
                bind.originPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            //专家头像
            ImageManager.getInstance().displayImg(bind.expertHead, bean.getHeadPictureUrl());
            //姓名
            bind.expertName.setText(bean.getTrainerName());
            //课程介绍
            if (!TextUtils.isEmpty(bean.getIntroduce())) {
                bind.emptyWv.setVisibility(View.GONE);
                bind.introArea.setVisibility(View.VISIBLE);
                ViewInitTool.initShowimgWebview(bind.introWv);
                ViewInitTool.setWebData(bind.introWv, bean.getIntroduce());
            }
            //购买按钮 buyStatus 购买状态：0未购买 1已购买   chargeType收费类型 0免费 1收费 //或者会员
            if (bean.getChargeType() != 1 || bean.getBuyStatus() != 0 || GlobalParam.currentUser.getIsMember() > -1) {
                bind.openCourse.setText("一键听课");
            } else {
                bind.openCourse.setText("购买课程");
                //支付成功的广播 ACTION_REFRESH_ORDER
                registerReceiver(receiver, new IntentFilter("ACTION_REFRESH_ORDER"));
            }
            bind.openCourse.setVisibility(View.VISIBLE);
            //查询是否关注专家
            asyncGetFollowStatus();
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && "ACTION_REFRESH_ORDER".equals(intent.getAction())) {
                if (bean != null) {
                    bean.setBuyStatus(1);
                    bind.openCourse.setText("一键听课");
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            //ignore
        }
    }

    /**
     * 课时列表
     */
    private void asyncGetCourseList() {
        String url = AppUrl.host + AppUrl.getCourseListById;
        Map<String, Object> map = new HashMap<>();
        map.put("trainerCourseSeriesId", courseId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, subCourseListener);
    }

    BaseRequestListener subCourseListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray arr = jsonResult.optJSONArray("list");
            if (arr != null && arr.length() > 0) {
                for (int i = 0; i < arr.length(); i++) {
                    list.add(gson.fromJson(arr.optJSONObject(i).toString(), SubCourseListBean.class));
                }
                adapter.setType(bean.getForm());
                adapter.notifyDataSetChanged();
            } else {
                //隐藏课程
                bind.courseArea.setVisibility(View.GONE);
            }
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
        }
    };

    /**
     * 查询是否已关注专家
     */
    public void asyncGetFollowStatus() {
        String url = AppUrl.host + AppUrl.isFollowById;
        Map<String, Object> map = new HashMap<>();
        map.put("trainerId", bean.getTrainerId());
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, isFollowListener);
    }

    BaseRequestListener isFollowListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            String result = jsonResult.optString("data");
            if ("true".equalsIgnoreCase(result)) {
                isFollow = true;
            } else {
                isFollow = false;
            }
            switchFollow(isFollow);
        }

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            bind.followTv.setEnabled(true);
        }
    };

    private void switchFollow(boolean b) {
        if (b) {
            bind.followTv.setText("已关注");
            bind.followTv.setTextColor(Color.parseColor("#9b9b9b"));
            bind.followTv.setBackgroundResource(R.drawable.shape_corner_gray);
        } else {
            bind.followTv.setText("+关注");
            bind.followTv.setTextColor(Color.parseColor("#ff5a80"));
            bind.followTv.setBackgroundResource(R.drawable.shape_corner_red);
        }
    }

    /**
     * 关注、取消关注专家
     */
    private void followExpert() {
        String url = AppUrl.host + AppUrl.followTeacher;
        Map<String, Object> map = new HashMap<>();
        map.put("trainerId", bean.getTrainerId());
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, followListener);
    }

    BaseRequestListener followListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            switchFollow(isFollow = true);
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }
    };

    private void unfollow() {
        String url = AppUrl.host + AppUrl.unfollowTeacher;
        Map<String, Object> map = new HashMap<>();
        map.put("trainerId", bean.getTrainerId());
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, unfollowListener);
    }

    BaseRequestListener unfollowListener = new JsonRequestListener() {
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            switchFollow(isFollow = false);
        }

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
        }
    };

    public void showPopFormBottom() {
        mCouponChooseGetPopWin = new CouponChooseGetPopWin(mContext, couponList);
        // 设置Popupwindow显示位置（从底部弹出）
        mCouponChooseGetPopWin.showAtLocation(findViewById(R.id.main_view), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        params = getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        mCouponChooseGetPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
        mCouponChooseGetPopWin.getLvTeachers().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (couponList.get(position).getCouponReceiveStatus() == 0) {//优惠券领取状态(0:立即领取 1:已领完)
                    asyncReceiveSingleCoupon(couponList.get(position).getId());
                }
            }
        });
    }

    /**
     * 领取优惠券
     */
    private void asyncReceiveSingleCoupon(int couponId) {
        String url = AppUrl.host + AppUrl.receiveSingleCoupon;//"http://10.10.10.18:8380/service/coupon/receiveSingleCoupon";
        Map<String, Object> map = new HashMap<>();
        map.put("couponId", couponId);
        String requestData = ParamBuild.buildParams(map);
        netRequest.startRequest(url, Request.Method.POST, requestData, 0, rListener);
    }

    BaseRequestListener rListener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            if ("0000042004".equals(errorCode)) {//领取成功
                asyncCouponList();
            }
            if (!TextUtils.isEmpty(errorMsg) && !"null".equalsIgnoreCase(errorMsg)) {
                showShortToast(errorMsg);
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
        }
    };


}
