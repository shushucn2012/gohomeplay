package com.park61.teacherhelper.widget.modular;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.home.bean.CmsItem;

import java.util.List;


/**
 * 首页组件化-快捷入口组件
 * Created by shubei on 2018/4/16.
 */

public class FastGoModule extends LinearLayout {

    public FastGoModule(Context context, List<CmsItem> quickList) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.home_banner2_layout, this, true);
        initFastGo(context, quickList);
    }

    public void initFastGo(Context mContext, List<CmsItem> quickList) {
        if (quickList.size() == 1) {
            findViewById(R.id.area_quick0).setVisibility(View.VISIBLE);
            findViewById(R.id.area_quick1).setVisibility(View.INVISIBLE);
            findViewById(R.id.area_quick2).setVisibility(View.INVISIBLE);
            findViewById(R.id.area_quick3).setVisibility(View.INVISIBLE);
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_quick0), quickList.get(0).getLinkPic());
            ((TextView) findViewById(R.id.tv_quick0)).setText(quickList.get(0).getTitle());//
            findViewById(R.id.area_quick0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.AddStatistics(mContext, quickList.get(0), false);
                    ViewInitTool.judgeGoWhere(quickList.get(0), mContext);
                }
            });
            findViewById(R.id.area_quick1).setOnClickListener(null);
            findViewById(R.id.area_quick2).setOnClickListener(null);
            findViewById(R.id.area_quick3).setOnClickListener(null);
        } else if (quickList.size() == 2) {
            findViewById(R.id.area_quick0).setVisibility(View.VISIBLE);
            findViewById(R.id.area_quick1).setVisibility(View.VISIBLE);
            findViewById(R.id.area_quick2).setVisibility(View.INVISIBLE);
            findViewById(R.id.area_quick3).setVisibility(View.INVISIBLE);
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_quick0), quickList.get(0).getLinkPic());
            ((TextView) findViewById(R.id.tv_quick0)).setText(quickList.get(0).getTitle());
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_quick1), quickList.get(1).getLinkPic());
            ((TextView) findViewById(R.id.tv_quick1)).setText(quickList.get(1).getTitle());
            findViewById(R.id.area_quick0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.AddStatistics(mContext, quickList.get(0), false);
                    ViewInitTool.judgeGoWhere(quickList.get(0), mContext);
                }
            });
            findViewById(R.id.area_quick1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.AddStatistics(mContext, quickList.get(1), false);
                    ViewInitTool.judgeGoWhere(quickList.get(1), mContext);
                }
            });
            findViewById(R.id.area_quick2).setOnClickListener(null);
            findViewById(R.id.area_quick3).setOnClickListener(null);
        } else if (quickList.size() == 3) {
            findViewById(R.id.area_quick0).setVisibility(View.VISIBLE);
            findViewById(R.id.area_quick1).setVisibility(View.VISIBLE);
            findViewById(R.id.area_quick2).setVisibility(View.VISIBLE);
            findViewById(R.id.area_quick3).setVisibility(View.INVISIBLE);
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_quick0), quickList.get(0).getLinkPic());
            ((TextView) findViewById(R.id.tv_quick0)).setText(quickList.get(0).getTitle());
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_quick1), quickList.get(1).getLinkPic());
            ((TextView) findViewById(R.id.tv_quick1)).setText(quickList.get(1).getTitle());
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_quick2), quickList.get(2).getLinkPic());
            ((TextView) findViewById(R.id.tv_quick2)).setText(quickList.get(2).getTitle());
            findViewById(R.id.area_quick0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.AddStatistics(mContext, quickList.get(0), false);
                    ViewInitTool.judgeGoWhere(quickList.get(0), mContext);
                }
            });
            findViewById(R.id.area_quick1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.AddStatistics(mContext, quickList.get(1), false);
                    ViewInitTool.judgeGoWhere(quickList.get(1), mContext);
                }
            });
            findViewById(R.id.area_quick2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.AddStatistics(mContext, quickList.get(2), false);
                    ViewInitTool.judgeGoWhere(quickList.get(2), mContext);
                }
            });
            findViewById(R.id.area_quick3).setOnClickListener(null);
        } else if (quickList.size() >= 4) {
            findViewById(R.id.area_quick0).setVisibility(View.VISIBLE);
            findViewById(R.id.area_quick1).setVisibility(View.VISIBLE);
            findViewById(R.id.area_quick2).setVisibility(View.VISIBLE);
            findViewById(R.id.area_quick3).setVisibility(View.VISIBLE);
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_quick0), quickList.get(0).getLinkPic());
            ((TextView) findViewById(R.id.tv_quick0)).setText(quickList.get(0).getTitle());
//            ((ImageView) findViewById(R.id.img_quick0)).setImageResource(R.mipmap.server_apply);
//            ((TextView) findViewById(R.id.tv_quick0)).setText("服务申请");
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_quick1), quickList.get(1).getLinkPic());
            ((TextView) findViewById(R.id.tv_quick1)).setText(quickList.get(1).getTitle());
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_quick2), quickList.get(2).getLinkPic());
            ((TextView) findViewById(R.id.tv_quick2)).setText(quickList.get(2).getTitle());
            ImageManager.getInstance().displayImg((ImageView) findViewById(R.id.img_quick3), quickList.get(3).getLinkPic());
            ((TextView) findViewById(R.id.tv_quick3)).setText(quickList.get(3).getTitle());
            findViewById(R.id.area_quick0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.AddStatistics(mContext, quickList.get(0), false);
                    ViewInitTool.judgeGoWhere(quickList.get(0), mContext);
//                    startActivity(new Intent(mContext, ServiceApplyActivity.class));
                }
            });
            findViewById(R.id.area_quick1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.AddStatistics(mContext, quickList.get(1), false);
                    ViewInitTool.judgeGoWhere(quickList.get(1), mContext);
                }
            });
            findViewById(R.id.area_quick2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.AddStatistics(mContext, quickList.get(2), false);
                    ViewInitTool.judgeGoWhere(quickList.get(2), mContext);
                }
            });
            findViewById(R.id.area_quick3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.AddStatistics(mContext, quickList.get(3), false);
                    ViewInitTool.judgeGoWhere(quickList.get(3), mContext);
                }
            });
        }
    }
}
