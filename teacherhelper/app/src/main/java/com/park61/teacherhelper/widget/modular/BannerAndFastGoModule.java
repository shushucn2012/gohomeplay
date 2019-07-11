package com.park61.teacherhelper.widget.modular;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.home.adapter.FastGoGvAdapter;
import com.park61.teacherhelper.module.home.bean.CmsItem;
import com.park61.teacherhelper.widget.gridview.GridViewForScrollView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 首页组件化-banner与快捷入口一体化组件
 * Created by shubei on 2018/4/16.
 */

public class BannerAndFastGoModule extends LinearLayout {

    private BGABanner banner;
    private GridViewForScrollView gv_fg;
    private FastGoGvAdapter adapter;

    public BannerAndFastGoModule(Context context, List<CmsItem> bannerList, List<CmsItem> quickList) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.banner_and_fastgo_module, this, true);
        banner = (BGABanner) findViewById(R.id.banner);

        gv_fg = (GridViewForScrollView) findViewById(R.id.gv_fg);
        gv_fg.setFocusable(false);

        initBanner(context, bannerList);
        initFastGo(context, quickList);
    }

    public void initBanner(Context context, List<CmsItem> bannerList) {
        banner.setData(bannerList, null);
        banner.setAdapter(new MyAdapter());
        banner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner bgaBanner, View itemView, Object model, int position) {
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
                MobclickAgent.onEvent(context, "clicktopbanner", map);
                ViewInitTool.judgeGoWhere(bi, context);
            }
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

    public void initFastGo(Context context, List<CmsItem> quickList){
        adapter = new FastGoGvAdapter(context, quickList);
        gv_fg.setAdapter(adapter);
        gv_fg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewInitTool.AddStatistics(context, quickList.get(position), false);
                ViewInitTool.judgeGoWhere(quickList.get(position), context);
            }
        });
    }

}
