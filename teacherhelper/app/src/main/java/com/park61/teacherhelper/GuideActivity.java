package com.park61.teacherhelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.module.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity implements OnPageChangeListener {

    private ViewPager vp;
    private MViewPagerAdapter vpAdapter;
    private List<View> views;

    // 引导图片资源
    private static final int[] pics = {R.mipmap.guide_pic1, R.mipmap.guide_pic2, R.mipmap.guide_pic3};
//	private static final int[] pics = { R.drawable.guide_3_0_pic1};

    // 底部小店图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_kk_guide);
        views = new ArrayList<View>();
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        // 初始化引导图片列表
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setBackgroundResource(pics[i]);
            if (i == pics.length - 1) {
                iv.setOnTouchListener(new OnTouchListener() {

                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        int btn_h = view.getHeight() / 5 * 4;
                        if (event.getY() >= btn_h) {
                            SharedPreferences isFirstRunSp = getSharedPreferences("IsFirstFlag", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor mEditor = isFirstRunSp.edit();
                            mEditor.putBoolean("IsFirstRun", false);
                            mEditor.putString("AppVersion", GlobalParam.versionName);
                            mEditor.commit();
                            if (GlobalParam.userToken != null) {
                                startActivity(new Intent(mContext, MainTabActivity.class));
                            } else {
                                startActivity(new Intent(mContext, LoginActivity.class));
                            }
                            finish();
                        }
                        return false;
                    }
                });
            }
            views.add(iv);
        }
        vp = (ViewPager) findViewById(R.id.viewpager);
        // 初始化Adapter
        vpAdapter = new MViewPagerAdapter(views);
        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.setOnPageChangeListener(this);

        // 初始化底部小点
        initDots();

    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        dots = new ImageView[pics.length];
        // 循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);// 都设为灰色
            dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
        }
        currentIndex = 0;
        dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
    }

    /**
     * 这只当前引导小点的选中
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }
        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        currentIndex = positon;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        // 设置底部小点选中状态
        setCurDot(arg0);
    }

    class MViewPagerAdapter extends PagerAdapter {

        // 界面列表
        private List<View> views;

        public MViewPagerAdapter(List<View> views) {
            this.views = views;
        }

        // 销毁arg1位置的界面
        @Override
        public void destroyItem(View container, int position, Object object) {
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        // 获得当前界面数
        @Override
        public int getCount() {
            return views.size();
        }

        // 初始化arg1位置的界面
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            try {
                ((ViewPager) container).addView((View) views.get(position % views.size()), 0);
            } catch (Exception e) {
            }
            return views.get(position % views.size());
        }

        // 判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

}