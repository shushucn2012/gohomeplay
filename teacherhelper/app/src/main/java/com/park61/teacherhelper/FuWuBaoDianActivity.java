package com.park61.teacherhelper;

import com.park61.teacherhelper.common.set.AppUrl;

/**
 * 服务宝典主页
 * Created by shubei on 2018/3/13.
 */

public class FuWuBaoDianActivity extends CanBackWebViewActivity {

    public void initData() {
        super.initData();
        //服务宝典用应用图标
        String sharePic = AppUrl.SHARE_APP_ICON;
        mShareInfoBean.setPicUrl(sharePic);
    }

    /*protected void setShareAndNowTitle(String mtitle) {
        shareTitle = mtitle;
        logout("====================mtitle====================" + mtitle);
        if (!mtitle.startsWith("m.61park.cn")) {
            tv_page_title.setText(mtitle);
        }
    }*/
}
