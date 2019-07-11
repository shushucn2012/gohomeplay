package com.park61.teacherhelper.module.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.module.activity.bean.ShareInfoBean;
import com.park61.teacherhelper.module.activity.fragment.FragmentCirsAct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubei on 2017/12/13.
 */

public class ChristmasActListActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private static int SORT_TYPE_NEWEST = 1;
    private static int SORT_TYPE_MOST = 2;

    private List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
    private int[] BUTION_IDS = {R.id.rb_first, R.id.rb_tv};
    private View[] stickArray = new View[2];
    private ViewPager pager;
    private EditText edit_input_word;
    private View btn_search;
    private ImageView img_del;

    private int cur_index = 0;//当前分类
    public String keyword;
    public int activityId;
    private ShareInfoBean mShareInfoBean;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_christmas_actlist);
    }

    @Override
    public void initView() {
        //setPagTitle("最美圣诞评比");
        ((ImageView) findViewById(R.id.img_right)).setImageResource(R.mipmap.icon_to_share_red);
        edit_input_word = (EditText) findViewById(R.id.edit_input_word);
        btn_search = findViewById(R.id.btn_search);
        img_del = (ImageView) findViewById(R.id.img_del);

        if (!CommonMethod.isListEmpty(fragmentList)) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            for (Fragment f : fragmentList) {
                ft.remove(f);
            }
            ft.commitAllowingStateLoss();
            getSupportFragmentManager().executePendingTransactions();
        }

        fragmentList.clear();
        FragmentCirsAct fragmentCirsActNewest = new FragmentCirsAct();
        Bundle argsNewest = new Bundle();
        argsNewest.putInt("sortType", SORT_TYPE_NEWEST);
        fragmentCirsActNewest.setArguments(argsNewest);
        fragmentList.add(fragmentCirsActNewest);

        FragmentCirsAct fragmentCirsActMost = new FragmentCirsAct();
        Bundle argsMost = new Bundle();
        argsMost.putInt("sortType", SORT_TYPE_MOST);
        fragmentCirsActMost.setArguments(argsMost);
        fragmentList.add(fragmentCirsActMost);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pager.setOffscreenPageLimit(2);
        stickArray[0] = findViewById(R.id.stick0);
        stickArray[1] = findViewById(R.id.stick1);
        pager.setCurrentItem(cur_index, false);
        ((RadioButton) findViewById(R.id.rb_first)).setChecked(true);
        ((RadioButton) findViewById(R.id.rb_first)).setOnCheckedChangeListener(ChristmasActListActivity.this);
        ((RadioButton) findViewById(R.id.rb_tv)).setOnCheckedChangeListener(ChristmasActListActivity.this);
        pager.addOnPageChangeListener(mOnPageChangeListener);
    }

    @Override
    public void initData() {
        activityId = getIntent().getIntExtra("activityId", -1);
        mShareInfoBean = new ShareInfoBean(getIntent().getStringExtra("shareUrl"),
                getIntent().getStringExtra("sharePic"),
                getIntent().getStringExtra("shareTitle"),
                getIntent().getStringExtra("shareDescription"));
        setPagTitle(mShareInfoBean.getTitle());
    }

    @Override
    public void initListener() {
        area_right.setVisibility(View.VISIBLE);
        area_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* String shareUrl = "http://m.61park.cn";
                String picUrl = AppUrl.SHARE_APP_ICON;
                String title = "61学院";
                String description = "更多精彩，尽在61学院";*/
                showShareDialog(mShareInfoBean);
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = edit_input_word.getText().toString().trim();
                ((FragmentCirsAct) fragmentList.get(0)).refreshList();
                ((FragmentCirsAct) fragmentList.get(1)).refreshList();
                hideKeyboard();
            }
        });
        edit_input_word.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(edit_input_word.getText().toString().trim())) {
                    img_del.setVisibility(View.GONE);
                } else {
                    img_del.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_input_word.setText("");
                img_del.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.rb_first:
                    changeTabList(0);
                    break;
                case R.id.rb_tv:
                    changeTabList(1);
                    break;
            }
        }
    }

    private void changeTabList(int tabindex) {
        if (tabindex != cur_index) {
            cur_index = tabindex;
            pager.setCurrentItem(tabindex, true);
            showStick(tabindex);
        }
    }

    /**
     * 变化标签组下方红杠
     */
    private void showStick(int which) {
        stickArray[which].setVisibility(View.VISIBLE);
        for (int i = 0; i < stickArray.length; i++) {
            if (i != which) {
                stickArray[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return BUTION_IDS.length;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) findViewById(BUTION_IDS[position]);
            radioButton.performClick();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
}
