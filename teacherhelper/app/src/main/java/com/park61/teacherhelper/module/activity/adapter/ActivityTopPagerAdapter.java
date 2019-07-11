package com.park61.teacherhelper.module.activity.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by nieyu on 2017/11/1.
 */

public class ActivityTopPagerAdapter extends PagerAdapter {
    private List <LinearLayout> mListViews;
    public ActivityTopPagerAdapter(List <LinearLayout> mListViews){
        this.mListViews = mListViews;//构造方法，参数是我们的页卡，这样比较方便。

    }

    @Override
    public int getCount() {
        return mListViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListViews.get(position));//删除页卡
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mListViews.get(position), 0);//添加页卡
        return mListViews.get(position);
    }
}
