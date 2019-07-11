package com.park61.teacherhelper.module.activity.bean;

import java.io.Serializable;

/**
 * Created by nieyu on 2017/11/2.
 */

public class BannerListDataBean implements Serializable {

private  TopIndacaterBean bannerAdLink;
private BannerBean indicatorAdLink;


    public BannerBean getIndicatorAdLink() {
        return indicatorAdLink;
    }

    public TopIndacaterBean getBannerAdLink() {
        return bannerAdLink;
    }

    public void setBannerAdLink(TopIndacaterBean bannerAdLink) {
        this.bannerAdLink = bannerAdLink;
    }

    public void setIndicatorAdLink(BannerBean indicatorAdLink) {
        this.indicatorAdLink = indicatorAdLink;
    }
}
