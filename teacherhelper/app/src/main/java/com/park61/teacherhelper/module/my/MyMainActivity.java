package com.park61.teacherhelper.module.my;

import android.content.Intent;
import android.graphics.Outline;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.CanBackWebViewActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.module.activity.Double11ActTicketActivity;
import com.park61.teacherhelper.module.activity.MyTrainApplyListActivity;
import com.park61.teacherhelper.module.activity.MyTrainListActivity;
import com.park61.teacherhelper.module.activity.TrainFeedBackActivity;
import com.park61.teacherhelper.module.clazz.ClazzMainActivity;
import com.park61.teacherhelper.module.clazz.FamilyConBookMainActivity;
import com.park61.teacherhelper.module.login.bean.UserBean;
import com.park61.teacherhelper.module.login.bean.UserManager;
import com.park61.teacherhelper.module.member.MemberMainActivity;
import com.park61.teacherhelper.module.okdownload.MyDownLoadAttachActivity;
import com.park61.teacherhelper.module.okdownload.MyDownLoadVAdioActivity;
import com.park61.teacherhelper.module.pay.MyCouponActiity;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

/**
 * 我的个人中心
 * create by super on 20180326
 */
public class MyMainActivity extends BaseActivity {

    private View area_class, area_notice, area_focus_expert, area_collect, area_my_ticket, area_download, area_wallet,
            area_coupon, area_orders, area_course, area_train_resp, area_feeback, area_help, area_custom_service, area_setting, area_activity;
    private ImageView img_user, img_glod_vip, img_exp_vip;
    private TextView tv_username, tv_ticket_number, tv_vip_expire_date, tv_no_vip;
    private boolean isFirstIn = true;//是否是第一次进入

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_my_main_new2);
    }

    @Override
    public void initView() {
        area_class = findViewById(R.id.area_class);
        area_notice = findViewById(R.id.area_notice);

        area_focus_expert = findViewById(R.id.area_focus_expert);
        area_collect = findViewById(R.id.area_collect);
        area_my_ticket = findViewById(R.id.area_my_ticket);
        area_download = findViewById(R.id.area_download);

//        area_wallet = findViewById(R.id.area_wallet);
        area_coupon = findViewById(R.id.area_coupon);
        area_orders = findViewById(R.id.area_orders);
        area_course = findViewById(R.id.area_course);

        area_train_resp = findViewById(R.id.area_train_resp);
        area_feeback = findViewById(R.id.area_feeback);
        area_help = findViewById(R.id.area_help);
        area_custom_service = findViewById(R.id.area_custom_service);

        area_setting = findViewById(R.id.area_setting);
        area_activity = findViewById(R.id.area_activity);

        img_user = (ImageView) findViewById(R.id.img_user);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_ticket_number = (TextView) findViewById(R.id.tv_ticket_number);
        tv_vip_expire_date = findViewById(R.id.tv_vip_expire_date);
        img_glod_vip = findViewById(R.id.img_glod_vip);
        img_exp_vip = findViewById(R.id.img_exp_vip);
        tv_no_vip = findViewById(R.id.tv_no_vip);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    //x，y轴表示位置，后两个参数表示长，宽
                    outline.setOval(0, 0, img_user.getWidth(), img_user.getHeight());
                }
            };
            img_user.setOutlineProvider(viewOutlineProvider);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncGetMyInfor();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        area_focus_expert.setOnClickListener(v -> startActivity(new Intent(mContext, MyFocusMenListActivity.class)));
        area_activity.setOnClickListener(v -> startActivity(new Intent(mContext, MyActivityActivity.class)));
        area_orders.setOnClickListener(v -> startActivity(new Intent(mContext, MeOrderActivity.class)));
        area_coupon.setOnClickListener(v -> startActivity(new Intent(mContext, MyCouponActiity.class)));
        area_course.setOnClickListener(v -> startActivity(new Intent(mContext, MyCourseActivity.class)));
        area_collect.setOnClickListener(v -> startActivity(new Intent(mContext, MyCollectActivity.class)));
        area_feeback.setOnClickListener(v -> startActivity(new Intent(mContext, FeebackActivity.class)));
//        area_feeback.setOnClickListener(v -> startActivity(new Intent(mContext, ParkApplyActivity.class)));
        area_train_resp.setOnClickListener(v -> startActivity(new Intent(mContext, TrainRespActivity.class)));
        area_custom_service.setOnClickListener(v -> {
            String phoneShow = "027-6552-4760";
            final String phoneReal = "02765524760";
            dDialog.showDialog("提示", "确认拨打" + phoneShow + "?", "取消", "确认", null, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneReal));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    dDialog.dismissDialog();
                }
            });
        });
        area_class.setOnClickListener(v -> startActivity(new Intent(mContext, ClazzMainActivity.class)));
        //area_notice.setOnClickListener(v -> startActivity(new Intent(mContext, ClassNoticeListActivity.class)));

        //家园联系薄入口
        area_notice.setOnClickListener(v -> startActivity(new Intent(mContext, FamilyConBookMainActivity.class)));

        area_help.setOnClickListener(v -> {
            Intent it = new Intent(mContext, CanBackWebViewActivity.class);
            String url = AppUrl.host + "/teacher/html/help.html";
            it.putExtra("url", url);
            it.putExtra("PAGE_TITLE", "帮助中心");
            startActivity(it);
        });
        area_setting.setOnClickListener(v -> startActivity(new Intent(mContext, SettingActivity.class)));
        img_user.setOnClickListener(v -> startActivity(new Intent(mContext, MeInfoActivity.class)));
        area_download.setOnClickListener(v -> startActivity(new Intent(mContext, MyDownLoadAttachActivity.class)));
        area_my_ticket.setOnClickListener(v -> startActivity(new Intent(mContext, Double11ActTicketActivity.class)));
        findViewById(R.id.area_interesting_class).setOnClickListener(v -> startActivity(new Intent(mContext, MyInterestingClassActivity.class)));
        /*findViewById(R.id.area_jixun).setOnClickListener(v -> {
            Intent it = new Intent(mContext, MyJiXunActivity.class);
            it.putExtra("PAGE_TITLE", "集训记录");
            it.putExtra("url", "http://m.61park.cn/teach/#/training/activitylist");
            startActivity(it);
        });*/
        findViewById(R.id.area_tibao).setOnClickListener(v -> {
            /*Intent it = new Intent(mContext, MyTiBaoActivity.class);
            it.putExtra("PAGE_TITLE", "提报记录");
            it.putExtra("url", "http://m.61park.cn/teach/#/training/list");
            startActivity(it);*/
            startActivity(new Intent(mContext, MyTrainApplyListActivity.class));
        });
        findViewById(R.id.area_cache).setOnClickListener(v -> startActivity(new Intent(mContext, MyDownLoadVAdioActivity.class)));
        findViewById(R.id.img_vip_entry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MemberMainActivity.class));
            }
        });
        findViewById(R.id.area_my_members).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MemberMainActivity.class));
            }
        });
        findViewById(R.id.area_jixun).setOnClickListener(v -> startActivity(new Intent(mContext, MyTrainListActivity.class)));
//        findViewById(R.id.area_jixun).setOnClickListener(v -> startActivity(new Intent(mContext, TrainFeedBackActivity.class)));
    }

    /**
     * 获取我的信息
     */
    protected void asyncGetMyInfor() {
        String wholeUrl = AppUrl.host + AppUrl.myInfor;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, getRegVcodeLsner);
    }

    BaseRequestListener getRegVcodeLsner = new JsonRequestListener() {

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
            dismissDialog();
            UserBean userBean = new Gson().fromJson(jsonResult.toString(), UserBean.class);
            ImageManager.getInstance().displayImg(img_user, userBean.getPictureUrl(), R.mipmap.mine_icon);
            tv_username.setText(userBean.getName());

            //---------------会员标志设定------------begin//
            if (userBean.getIsMember() == -1) {//非会员
                img_glod_vip.setVisibility(View.INVISIBLE);
                img_exp_vip.setVisibility(View.INVISIBLE);
                tv_no_vip.setVisibility(View.VISIBLE);
                tv_vip_expire_date.setVisibility(View.INVISIBLE);
            } else if (userBean.getIsMember() == 0) {//体验会员
                img_glod_vip.setVisibility(View.INVISIBLE);
                img_exp_vip.setVisibility(View.VISIBLE);
                tv_no_vip.setVisibility(View.INVISIBLE);
                tv_vip_expire_date.setVisibility(View.VISIBLE);
            } else if (userBean.getIsMember() == 1) {//黄金会员
                img_glod_vip.setVisibility(View.VISIBLE);
                img_exp_vip.setVisibility(View.INVISIBLE);
                tv_no_vip.setVisibility(View.INVISIBLE);
                tv_vip_expire_date.setVisibility(View.VISIBLE);
            } else {
                img_glod_vip.setVisibility(View.INVISIBLE);
                img_exp_vip.setVisibility(View.INVISIBLE);
                tv_no_vip.setVisibility(View.INVISIBLE);
                tv_vip_expire_date.setVisibility(View.INVISIBLE);
            }
            tv_vip_expire_date.setText(userBean.getExpireDate());
            //---------------会员标志设定------------end//

            if (0 == userBean.getPassNum()) {
                tv_ticket_number.setVisibility(View.GONE);
            } else {
                tv_ticket_number.setVisibility(View.VISIBLE);
                tv_ticket_number.setText(userBean.getPassNum() + "");
            }
            GlobalParam.currentUser = userBean;
            UserManager.getInstance().setAccountInfo(GlobalParam.currentUser, mContext);
            if (TextUtils.isEmpty(userBean.getUserDuty())) {//userDuty为空
                startActivity(new Intent(mContext, CompleteInfoActivity.class));
            }
            isFirstIn = false;
        }
    };
}
