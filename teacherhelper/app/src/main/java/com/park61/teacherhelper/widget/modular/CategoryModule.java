package com.park61.teacherhelper.widget.modular;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.home.bean.CmsItem;

import java.util.ArrayList;
import java.util.List;

import static com.park61.teacherhelper.common.tool.ViewInitTool.CLICK_CATEGORY;


/**
 * 首页组件化-类目组件
 * Created by shubei on 2018/11/1.
 */

public class CategoryModule extends LinearLayout {

    private Context mContext;
    private TextView tv_module_title;
    private List<ImageView> viewBeanList = new ArrayList<>();

    private List<CmsItem> cateList;
    private String moduleTitle;

    public CategoryModule(Context context, List<CmsItem> _cateList, String moduleName) {
        super(context);
        mContext = context;
        cateList = _cateList;
        moduleTitle = moduleName;
        if (cateList.size() == 3) {
            LayoutInflater.from(context).inflate(R.layout.home_catemodule_threepart, this, true);
        } else if (cateList.size() == 4) {
            LayoutInflater.from(context).inflate(R.layout.home_catemodule_fourpart, this, true);
        } else if (cateList.size() == 5) {
            LayoutInflater.from(context).inflate(R.layout.home_catemodule_fivepart, this, true);
        }
        initView();
        initData();
        initListener();
    }

    public void initView() {
        tv_module_title = findViewById(R.id.tv_module_title);
        if (cateList.size() == 3) {
            viewBeanList.add(findViewById(R.id.img0));
            viewBeanList.add(findViewById(R.id.img1));
            viewBeanList.add(findViewById(R.id.img2));
        } else if (cateList.size() == 4) {
            viewBeanList.add(findViewById(R.id.img0));
            viewBeanList.add(findViewById(R.id.img1));
            viewBeanList.add(findViewById(R.id.img2));
            viewBeanList.add(findViewById(R.id.img3));
        } else if (cateList.size() == 5) {
            viewBeanList.add(findViewById(R.id.img0));
            viewBeanList.add(findViewById(R.id.img1));
            viewBeanList.add(findViewById(R.id.img2));
            viewBeanList.add(findViewById(R.id.img3));
            viewBeanList.add(findViewById(R.id.img4));
        }
    }

    private void initData() {
        tv_module_title.setText(moduleTitle);
        for (int i = 0; i < viewBeanList.size(); i++) {
            ImageManager.getInstance().displayImg(viewBeanList.get(i), cateList.get(i).getLinkPic());
        }
    }

    private void initListener() {
        for (int i = 0; i < viewBeanList.size(); i++) {
            int finalI = i;
            viewBeanList.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewInitTool.addYouMengPoint(mContext, cateList.get(finalI), CLICK_CATEGORY);
                    ViewInitTool.judgeGoWhere(cateList.get(finalI), mContext);
                }
            });
        }
    }

}
