package com.park61.teacherhelper.widget.pw;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.park61.teacherhelper.CanBackWebViewActivity;
import com.park61.teacherhelper.MainTabActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.tool.ExitAppUtils;
import com.park61.teacherhelper.module.home.HomeModularListActivity;
import com.park61.teacherhelper.module.my.FeebackActivity;
import com.park61.teacherhelper.module.my.MyMainActivity;
import com.park61.teacherhelper.module.workplan.WorkManageMainActivity;

/**
 * 二级导航窗口
 *
 * @author super
 */
public class NaviShopPopWin extends PopupWindow {

    private Context mContext;

    private View toolView, view_home, view_msg, view_helper, view_feed;

    public NaviShopPopWin(Context context) {
        super(context);
        this.mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        toolView = inflater.inflate(R.layout.pw_navi_shop_layout, null);
        view_home = toolView.findViewById(R.id.view_home);
        view_msg = toolView.findViewById(R.id.view_msg);
        view_helper = toolView.findViewById(R.id.view_helper);
        view_feed = toolView.findViewById(R.id.view_feed);
        // 设置按钮监听
        view_home.setOnClickListener(rightMenuClickedLsner);
        view_msg.setOnClickListener(rightMenuClickedLsner);
        view_helper.setOnClickListener(rightMenuClickedLsner);
        view_feed.setOnClickListener(rightMenuClickedLsner);
        // 设置SelectPicPopupWindow的View
        this.setContentView(toolView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(null);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        toolView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    dismiss();
                }
                return true;
            }
        });
    }

    public OnClickListener rightMenuClickedLsner = new OnClickListener() {

        @Override
        public void onClick(View v) {
            dismiss();
            switch (v.getId()) {
                case R.id.view_home:
                    backToHome();
                    break;
                case R.id.view_msg:
                    Intent its = new Intent(mContext, SharePopWin.class);
                    String shareUrl = "http://m.61park.cn/html-sui/homepage/home-commend.html";
                    String picUrl = "http://park61.oss-cn-zhangjiakou.aliyuncs.com/content/20170830/1504073848515077136.png?x-oss-process=style/logo_img";
                    String title = "61区推荐";
                    String description = "61区真好玩呀！";
                    its.putExtra("shareUrl", shareUrl);
                    its.putExtra("picUrl", picUrl);
                    its.putExtra("title", title);
                    its.putExtra("description", description);
                    mContext.startActivity(its);
                    break;
                case R.id.view_helper:
                    Intent it = new Intent(mContext, CanBackWebViewActivity.class);
                    String url = AppUrl.host + "/teacher/html/help.html";
                    it.putExtra("url", url);
                    it.putExtra("PAGE_TITLE", "帮助中心");
                    mContext.startActivity(it);
                    break;
                case R.id.view_feed:
                    mContext.startActivity(new Intent(mContext, FeebackActivity.class));
                    break;
            }
        }
    };

    /**
     * 返回首页
     */
    public void backToHome() {
        for (Activity mActivity : ExitAppUtils.getInstance().getActList()) {
            if (!(mActivity instanceof MainTabActivity)
                    && !(mActivity instanceof HomeModularListActivity)
                    && !(mActivity instanceof WorkManageMainActivity)
                    && !(mActivity instanceof MyMainActivity)) {
                if (mActivity != null) {// 关闭所有页面，置回首页
                    mActivity.finish();
                }
            }
        }
        Intent changeIt = new Intent();
        changeIt.setAction("ACTION_TAB_CHANGE");
        changeIt.putExtra("TAB_NAME", "tab_main");
        mContext.sendBroadcast(changeIt);
    }

}
