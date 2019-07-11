package com.park61.teacherhelper.widget.modular;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.home.bean.CmsItem;

import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

import static com.park61.teacherhelper.common.tool.ViewInitTool.AddStatistics;

/**
 * 首页组件化-中部banner2组件
 * Created by shubei on 2018/4/16.
 */

public class BannerMidTwoModule extends LinearLayout {

    private BGABanner banner;

    public BannerMidTwoModule(Context context, List<CmsItem> bannerList) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.banner_mid2_module, this, true);
        initMiddleBanner2(context, bannerList);
    }

    public void initMiddleBanner2(Context mContext, List<CmsItem> midBanner2List) {
        if (midBanner2List.size() == 1) {
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_mid0), midBanner2List.get(0).getLinkPic());
            findViewById(R.id.img_mid0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(mContext, midBanner2List.get(0), true);
                    ViewInitTool.judgeGoWhere(midBanner2List.get(0), mContext);
                }
            });
            findViewById(R.id.img_mid1).setOnClickListener(null);
            findViewById(R.id.img_mid2).setOnClickListener(null);
        } else if (midBanner2List.size() == 2) {
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_mid0), midBanner2List.get(0).getLinkPic());
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_mid1), midBanner2List.get(1).getLinkPic());
            findViewById(R.id.img_mid0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(mContext, midBanner2List.get(0), true);
                    ViewInitTool.judgeGoWhere(midBanner2List.get(0), mContext);
                }
            });
            findViewById(R.id.img_mid1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(mContext, midBanner2List.get(1), true);
                    ViewInitTool.judgeGoWhere(midBanner2List.get(1), mContext);
                }
            });
            findViewById(R.id.img_mid2).setOnClickListener(null);
        } else if (midBanner2List.size() == 3) {
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_mid0), midBanner2List.get(0).getLinkPic());
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_mid1), midBanner2List.get(1).getLinkPic());
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_mid2), midBanner2List.get(2).getLinkPic());
            findViewById(R.id.img_mid0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(mContext, midBanner2List.get(0), true);
                    ViewInitTool.judgeGoWhere(midBanner2List.get(0), mContext);
                }
            });
            findViewById(R.id.img_mid1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(mContext, midBanner2List.get(1), true);
                    ViewInitTool.judgeGoWhere(midBanner2List.get(1), mContext);
                }
            });
            findViewById(R.id.img_mid2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddStatistics(mContext, midBanner2List.get(2), true);
                    ViewInitTool.judgeGoWhere(midBanner2List.get(2), mContext);
                }
            });
        }
    }

}
