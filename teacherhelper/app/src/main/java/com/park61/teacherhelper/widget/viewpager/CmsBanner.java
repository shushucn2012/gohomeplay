package com.park61.teacherhelper.widget.viewpager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.park61.teacherhelper.GuideViewPagerAdapter;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.ImageManager;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.home.bean.CmsItem;
import com.umeng.analytics.MobclickAgent;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CmsBanner {

    private Context context;
    private ViewPager vp;
    private LinearLayout dotlayout;
    private List<View> views;
    private GuideViewPagerAdapter vpAdapter;
    private int num = 400;
    private boolean isAutoPlay;//是否自动播放
    private int count = 0;
    private int WHAT_AUTO_PLAY = 1000;
    private int DELAY_MILLIS = 4000;

    private final Handler viewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_AUTO_PLAY) {
                vp.setCurrentItem(vp.getCurrentItem() + 1, true);
                viewHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, DELAY_MILLIS);
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 开始自动轮播
     */
    public void startAutoPlay() {
        if (!isAutoPlay) {
            viewHandler.sendEmptyMessageDelayed(WHAT_AUTO_PLAY, DELAY_MILLIS);
            isAutoPlay = true;
        }
    }

    /**
     * 停止自动轮播
     */
    public void stopAutoPlay() {
        if (isAutoPlay) {
            viewHandler.removeMessages(WHAT_AUTO_PLAY);
            isAutoPlay = false;
        }
    }

    // 图片数量
    private int picNum = 0;

    // 底部小店图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    private String category;

    public CmsBanner(Context context, ViewPager viewpager, LinearLayout dotlayout, String category) {
        this.context = context;
        this.vp = viewpager;
        this.dotlayout = dotlayout;
        this.category = category;
    }

    /**
     * 通过url加载banner
     */
    public void init(List<CmsItem> bannerList) {
        if (CommonMethod.isListEmpty(bannerList)) {
//            BannerItem item = new BannerItem();
//            item.setImg("http://ghpprod.oss-cn-qingdao.aliyuncs.com/client/9d346d13-2f93-482a-8cf0-45f63c8bb9db.jpg");
//            bannerList.add(item);
            return;
        }
        picNum = bannerList.size();
        if (picNum < 3) {//两张图的时候特殊处理
            bannerList.addAll(bannerList);
        }
        views = new ArrayList<View>();
        // 初始化引导图片列表
        for (int i = 0; i < bannerList.size(); i++) {
            View viewItem = LayoutInflater.from(context).inflate(R.layout.main_banner_vp_item, null);
            ImageView img_big_photo = (ImageView) viewItem.findViewById(R.id.img_big_photo);
            CmsItem bi = bannerList.get(i);
            img_big_photo.setOnClickListener(new OnBannerItemClickLsner(bi));
            ImageManager.getInstance().displayImg(img_big_photo, bannerList.get(i).getLinkPic(), R.mipmap.img_default_h);
            views.add(viewItem);
        }
        if (picNum > 1) {
            dotlayout.setVisibility(View.VISIBLE);
            // 初始化Adapter
            vpAdapter = new GuideViewPagerAdapter(views);
            vp.setAdapter(vpAdapter);
            // 绑定回调
            vp.addOnPageChangeListener(mOnPageChangeListener);
            // 初始化底部小点
            initDots();
            vp.setCurrentItem(num);
            startAutoPlay();
        } else {//只有一张图的时候隐藏掉底部小点
            dotlayout.setVisibility(View.GONE);
            vp.setAdapter(new SimpleVpAdapter(views.subList(0, 1)));
        }
    }

    /**
     * 停在轮播定时器线程
     */
    public void clear() {
        stopAutoPlay();
    }

    /**
     * 页面切换监听
     */
    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            setCurDot(position % picNum);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        // 覆写该方法实现轮播效果的暂停和恢复
        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case ViewPager.SCROLL_STATE_DRAGGING:// 正在拖动页面时执行此处
                    stopAutoPlay();
                    count++;
                    final int rec = count;
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (!isAutoPlay && rec == count) {
                                startAutoPlay();
                            }
                        }
                    }, 5 * 1000);
                    break;
                case ViewPager.SCROLL_STATE_IDLE:// 未拖动页面时执行此处
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 初始化引导点
     */
    private void initDots() {
        dots = new ImageView[picNum];
        // 循环取得小点图片
        for (int i = 0; i < picNum; i++) {
            dots[i] = (ImageView) dotlayout.getChildAt(i);
            dots[i].setVisibility(View.VISIBLE);
            dots[i].setEnabled(true);// 都设为灰色
            dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
        }
        for (int j = picNum; j < dotlayout.getChildCount(); j++) {
            dotlayout.getChildAt(j).setVisibility(View.GONE);
        }
        currentIndex = 0;
        dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
    }

    /**
     * 这只当前引导小点的选中
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > picNum - 1 || currentIndex == positon) {
            return;
        }
        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        currentIndex = positon;
    }

    private class OnBannerItemClickLsner implements OnClickListener {

        private CmsItem bi;

        public OnBannerItemClickLsner(CmsItem bi) {
            this.bi = bi;
        }

        @Override
        public void onClick(View view) {
            if ("top".equals(category)) {
                HashMap<String, String> map = new HashMap<String, String>();
                if (1 == bi.getLinkType() || 2 == bi.getLinkType()) {
                    map.put("target", "TB_" + bi.getLinkUrl());
                } else if (3 == bi.getLinkType()) {//分类
                    JSONObject jsonObject = new JSONObject();
                    try {
                        map.put("target", "TB_CLA_" + jsonObject.getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (4 == bi.getLinkType()) {//活动
                    map.put("target", "TB_ACT_" + bi.getLinkData());
                } else {//内容
                    map.put("target", "TB_CNT_" + bi.getLinkData());
                }
                MobclickAgent.onEvent(context, "clicktopbanner", map);
            } else if ("mid1".equals(category)) {
                HashMap<String, String> map = new HashMap<String, String>();
                if (1 == bi.getLinkType() || 2 == bi.getLinkType()) {
                    map.put("target", "TB_" + bi.getLinkUrl());
                } else if (3 == bi.getLinkType()) {//分类
                    JSONObject jsonObject = new JSONObject();
                    try {
                        map.put("target", "TB_CLA_" + jsonObject.getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (4 == bi.getLinkType()) {//活动
                    map.put("target", "TB_ACT_" + bi.getLinkData());
                } else {//内容
                    map.put("target", "TB_CNT_" + bi.getLinkData());
                }
                MobclickAgent.onEvent(context, "clickmiddlebanner1", map);
            }

            ViewInitTool.judgeGoWhere(bi, context);
            /*if (bi.getUrl() != null && !"".equals(bi.getUrl())) {
                if (bi.getUrl().contains("activity")) {
                    Map<String, String> mapRequest = CRequest.URLRequest(bi.getUrl());
                    for (String strRequestKey : mapRequest.keySet()) {
                        String strRequestValue = mapRequest.get(strRequestKey);
                        if ("actid".equals(strRequestKey)) {
                            Intent it = new Intent(context, ActDetailsActivity.class);
                            it.putExtra("id", Long.parseLong(strRequestValue));
                            context.startActivity(it);
                            return;
                        }
                    }
                } else if (bi.getUrl().contains("http://m.61park.cn/page/requirement-house/page.html")) {
                    context.startActivity(new Intent(context, DreamHouseMainActivity.class));
                } else if (bi.getUrl().contains("DAP")) {
                    if (GlobalParam.userToken == null) {// 未登录则先去登录
                        context.startActivity(new Intent(context, LoginActivity.class));
                        return;
                    }
                    context.startActivity(new Intent(context, DAPFirstActivity.class));
                } else if (bi.getUrl().contains("http://m.61park.cn/vote/toVoteIndex.do")) {
                    //http://m.61park.cn/vote/toVoteIndex.do
                    String url = bi.getUrl(); // web address
                    Log.out("url======" + url);
                    Intent intent = new Intent(context, CanBackWebViewActivity.class);
                    intent.putExtra("url", url);
                    intent.putExtra("picUrl", bi.getImg());
                    intent.putExtra("PAGE_TITLE", bi.getDescription());
                    context.startActivity(intent);
                } else if (bi.getUrl().contains("popularize:")) {
                    context.startActivity(new Intent(context, ToyShareActivity.class));
                } else {
                    String url = bi.getUrl(); // web address
                    Log.out("url======" + url);
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("url", url);
                    intent.putExtra("picUrl", bi.getImg());
                    intent.putExtra("PAGE_TITLE", bi.getDescription());
                    context.startActivity(intent);
                }
            }*/
        }
    }
}
