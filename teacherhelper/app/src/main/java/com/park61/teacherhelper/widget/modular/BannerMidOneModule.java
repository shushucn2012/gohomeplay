package com.park61.teacherhelper.widget.modular;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.home.bean.CmsItem;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 首页组件化-中部banner1组件
 * Created by shubei on 2018/4/16.
 */

public class BannerMidOneModule extends LinearLayout {

    private BGABanner banner;

    public BannerMidOneModule(Context context, List<CmsItem> bannerList) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.banner_mid1_module, this, true);
        banner = (BGABanner) findViewById(R.id.banner);
        initBanner(context, bannerList);
    }

    public void initBanner(Context context, List<CmsItem> bannerList) {
        banner.setData(bannerList, null);
        banner.setAdapter(new MyAdapter());
        banner.setDelegate((bgaBanner, itemView, model, position) -> {
            CmsItem bi = bannerList.get(position);
            HashMap<String, String> map = new HashMap<String, String>();
            if (1 == bi.getLinkType() || 2 == bi.getLinkType()) {//内连
                map.put("target", "TB_" + bi.getLinkUrl());
            } else if (3 == bi.getLinkType()) {//分类
                map.put("target", "TB_CLA_" + bi.getLinkData());
            } else if (4 == bi.getLinkType()) {//活动
                map.put("target", "TB_ACT_" + bi.getLinkData());
            } else {//内容
                map.put("target", "TB_CNT_" + bi.getLinkData());
            }
            MobclickAgent.onEvent(context, "clickmiddlebanner1", map);
            ViewInitTool.judgeGoWhere(bi, context);
        });
        if (bannerList.size() > 1) {
            banner.setAutoPlayAble(true);
        } else {
            banner.setAutoPlayAble(false);
        }
    }

    public class MyAdapter implements BGABanner.Adapter<ImageView, CmsItem> {

        @Override
        public void fillBannerItem(BGABanner banner, ImageView itemView, CmsItem model, int position) {
            ImageManager.getInstance().displayImg(itemView, model.getLinkPic(), R.mipmap.img_default_h);
        }
    }

}
