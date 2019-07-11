package com.park61.teacherhelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ListDataSave;
import com.park61.teacherhelper.module.home.bean.AdvertsieBean;
import com.park61.teacherhelper.module.home.bean.CmsItem;
import com.park61.teacherhelper.module.login.LoginActivity;
import com.park61.teacherhelper.module.my.adapter.PhotosViewPagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdvertisingActivity extends BaseActivity {

    private View area_jump;
    private LinearLayout dotlayout;
    private TextView tv_left_sec;
    private ViewPager viewpager_photos;
    private ImageView img_show;

    // 图片数量
    private int sec = 4;
    // 底部小店图片
    private ImageView[] dots;
    // 记录当前选中位置
    private int currentIndex;
    private List<AdvertsieBean> advertsieBeanList = new ArrayList<>();
    private ArrayList<View> views = new ArrayList<View>();

    private Handler timeHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                tv_left_sec.setText(sec-- + "S");
                if (sec >= 0) {
                    timeHandler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    goHome();
                }
            }
        }
    };

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_advertise);
    }

    @Override
    public void initView() {
        area_jump = findViewById(R.id.area_jump);
        tv_left_sec = (TextView) findViewById(R.id.tv_left_sec);
        area_jump.setVisibility(View.VISIBLE);
        viewpager_photos = (ViewPager) findViewById(R.id.viewpager_photos);
        img_show = (ImageView) findViewById(R.id.img_show);
        dotlayout = (LinearLayout) findViewById(R.id.top_vp_dot);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void initData() {
        ListDataSave mListDataSave = new ListDataSave(mContext);
        advertsieBeanList = mListDataSave.getDataList();
        if (CommonMethod.isListEmpty(advertsieBeanList)) {
            goHome();
            return;
        }
        if (advertsieBeanList.size() == 1) {
            String zipFloderPath = Environment.getExternalStorageDirectory().getPath() + "/firstpagefloder/" + advertsieBeanList.get(0).getPicLocalPath();
            File file = new File(zipFloderPath);
            if (!file.exists()) {
                goHome();
                return;
            }
            Bitmap bp = ImageManager.getInstance().readFileBitMap(zipFloderPath);
            img_show.setImageBitmap(bp);
            CmsItem bi = new CmsItem();
            bi.setTitle(advertsieBeanList.get(0).getTitle());
            bi.setLinkType(advertsieBeanList.get(0).getLinkType());
            bi.setLinkData(advertsieBeanList.get(0).getLinkData());
            bi.setLinkUrl(advertsieBeanList.get(0).getLinkUrl());
            bi.setLinkPic(advertsieBeanList.get(0).getPicUrl());
            img_show.setOnClickListener(new OnBannerItemClickLsner(bi));
            timeHandler.sendEmptyMessageDelayed(0, 1000);
        } else if (advertsieBeanList.size() > 1) {
            viewpager_photos.setVisibility(View.VISIBLE);
            dotlayout.setVisibility(View.VISIBLE);
            img_show.setVisibility(View.GONE);
            for (int i = 0; i < advertsieBeanList.size(); i++) {
                View viewItem = LayoutInflater.from(mContext).inflate(R.layout.showbigpic_vp_item, null);
                ImageView img_big_photo = (ImageView) viewItem.findViewById(R.id.img_big_photo);
                img_big_photo.setScaleType(ImageView.ScaleType.FIT_XY);
                String path = advertsieBeanList.get(i).getPicLocalPath();
                String zipFloderPath = Environment.getExternalStorageDirectory().getPath() + "/firstpagefloder/" + path;
//                ImageManager.getInstance().displayImg(img_big_photo, "file:///" + zipFloderPath, R.mipmap.ad_bg);
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .cacheInMemory(false).cacheOnDisk(false)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .delayBeforeLoading(100)
                        .build();
                com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage("file:///" + zipFloderPath, img_big_photo, options);
                CmsItem bi = new CmsItem();
                bi.setTitle(advertsieBeanList.get(i).getTitle());
                bi.setLinkType(advertsieBeanList.get(i).getLinkType());
                bi.setLinkData(advertsieBeanList.get(i).getLinkData());
                bi.setLinkUrl(advertsieBeanList.get(i).getLinkUrl());
                bi.setLinkPic(advertsieBeanList.get(i).getPicUrl());
                img_big_photo.setOnClickListener(new OnBannerItemClickLsner(bi));
                views.add(viewItem);
            }
            viewpager_photos.setAdapter(new PhotosViewPagerAdapter(views));
            // 绑定回调
            viewpager_photos.addOnPageChangeListener(mOnPageChangeListener);
            // 初始化底部小点
            initDots();
            viewpager_photos.setCurrentItem(0);
            timeHandler.sendEmptyMessageDelayed(0, 1000);
        }
    }

    private void goHome() {
        if (GlobalParam.userToken != null) {
            startActivity(new Intent(mContext, MainTabActivity.class));
        } else {
            startActivity(new Intent(mContext, LoginActivity.class));
        }
        finish();
    }

    @Override
    public void initListener() {
        area_jump.setOnClickListener(v -> {
            timeHandler.removeMessages(0);
            goHome();
        });
    }


    /**
     * 初始化引导点
     */
    private void initDots() {
        dots = new ImageView[advertsieBeanList.size()];
        // 循环取得小点图片
        for (int i = 0; i < advertsieBeanList.size(); i++) {
            dots[i] = (ImageView) dotlayout.getChildAt(i);
            dots[i].setVisibility(View.VISIBLE);
            dots[i].setEnabled(true);// 都设为灰色
            dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
        }
        for (int j = advertsieBeanList.size(); j < dotlayout.getChildCount(); j++) {
            dotlayout.getChildAt(j).setVisibility(View.GONE);
        }
        currentIndex = 0;
        dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
    }

    /**
     * 页面切换监听
     */
    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            setCurDot(position % advertsieBeanList.size());
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        // 覆写该方法实现轮播效果的暂停和恢复
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    /**
     * 这只当前引导小点的选中
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > advertsieBeanList.size() - 1 || currentIndex == positon) {
            return;
        }
        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        currentIndex = positon;
    }

    private class OnBannerItemClickLsner implements View.OnClickListener {

        private CmsItem bi;

        public OnBannerItemClickLsner(CmsItem bi) {
            this.bi = bi;
        }

        @Override
        public void onClick(View view) {
            if (GlobalParam.userToken != null) {
                Intent it = new Intent(mContext, MainTabActivity.class);
                it.putExtra("isAdClickIn", true);
                it.putExtra("CmsItem", bi);
                startActivity(it);
            } else {
                startActivity(new Intent(mContext, LoginActivity.class));
            }
            timeHandler.removeMessages(0);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeHandler.removeMessages(0);
    }
}
