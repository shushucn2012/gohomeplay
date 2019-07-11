package com.park61.teacherhelper.module.course;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.module.course.bean.HotWords;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.AutoLinefeedLayout;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shubei on 2017/10/12.
 */

public class CourseSearchActivity extends BaseActivity {

    private AutoLinefeedLayout view_wordwrap, view_wordwrap_history;
    private TextView tv_sousuo;
    private EditText edit_sousuo;
    private ImageView img_del;
    private View area_delete_his;

    private List<HotWords> hotList = new ArrayList<>();
    private List<HotWords> hisList = new ArrayList<>();

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_course_search);
    }

    @Override
    public void initView() {
        view_wordwrap = (AutoLinefeedLayout) findViewById(R.id.view_wordwrap);
        view_wordwrap_history = (AutoLinefeedLayout) findViewById(R.id.view_wordwrap_history);
        tv_sousuo = (TextView) findViewById(R.id.tv_sousuo);
        edit_sousuo = (EditText) findViewById(R.id.edit_sousuo);
        img_del = (ImageView) findViewById(R.id.img_del);
        area_delete_his = findViewById(R.id.area_delete_his);
    }

    @Override
    public void initData() {
        asyncGetHot();
    }

    @Override
    protected void onResume() {
        super.onResume();
        asyncGetHis();
    }

    @Override
    public void initListener() {
        tv_sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStatistics("contentList");
                Intent it = new Intent(mContext, CourseSearchResultActivity.class);
                String keyword = edit_sousuo.getText().toString().trim();
                it.putExtra("keyword", keyword);
                startActivity(it);
                finish();
            }
        });
        edit_sousuo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(edit_sousuo.getText().toString())) {
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
                edit_sousuo.setText("");
                img_del.setVisibility(View.GONE);
            }
        });
        area_delete_his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncClearHis();
            }
        });
        if (!TextUtils.isEmpty(getIntent().getStringExtra("keyword"))) {
            edit_sousuo.setText(getIntent().getStringExtra("keyword"));
        }
        area_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                finish();
            }
        });
    }

    private void asyncClearHis() {
        String wholeUrl = AppUrl.host + AppUrl.search_history_clear;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, clistener);
    }

    BaseRequestListener clistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            hisList.clear();
            view_wordwrap_history.removeAllViews();
            view_wordwrap_history.invalidate();
            //setHis();
        }
    };

    private void asyncGetHot() {
        String wholeUrl = AppUrl.host + AppUrl.search_hot;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, trlistener);
    }

    BaseRequestListener trlistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray actJay = jsonResult.optJSONArray("list");
            hotList.clear();
            for (int i = 0; i < actJay.length(); i++) {
                hotList.add(gson.fromJson(actJay.optJSONObject(i).toString(), HotWords.class));
            }
            setHot();
        }
    };

    private void setHot() {
        for (int i = 0; i < hotList.size(); i++) {
            LinearLayout ll = new LinearLayout(mContext);
            ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DevAttr.dip2px(mContext, 40)));
            ll.setGravity(Gravity.CENTER);

            TextView textview = new TextView(mContext);
            String words = hotList.get(i).getKeyword();
            final String keywords = hotList.get(i).getKeyword();
            if (words.length() > 20) {
                words = words.substring(0, 20) + "...";
            }
            textview.setText(words);
            textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textview.setTextColor(getResources().getColor(R.color.g333333));
            textview.setBackgroundResource(R.drawable.rec_gray_stroke_gray_solid_corner30);
            textview.setPadding(DevAttr.dip2px(mContext, 10), DevAttr.dip2px(mContext, 5), DevAttr.dip2px(mContext, 10), DevAttr.dip2px(mContext, 5));
            ll.addView(textview);

            textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, CourseSearchResultActivity.class);
                    it.putExtra("keyword", keywords);
                    startActivity(it);
                    finish();
                }
            });

            View view = new View(mContext);
            LinearLayout.LayoutParams linearParams2 = new LinearLayout.LayoutParams(DevAttr.dip2px(mContext, 12), DevAttr.dip2px(mContext, 12));
            view.setLayoutParams(linearParams2);
            ll.addView(view);

            view_wordwrap.addView(ll);
        }
    }

    private void asyncGetHis() {
        String wholeUrl = AppUrl.host + AppUrl.search_history;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, hlistener);
    }

    BaseRequestListener hlistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            if ("0000000012".equals(errorCode)) {
                area_delete_his.setVisibility(View.GONE);
            } else {
                showShortToast(errorMsg);
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray actJay = jsonResult.optJSONArray("list");
            hisList.clear();
            for (int i = 0; i < actJay.length(); i++) {
                hisList.add(gson.fromJson(actJay.optJSONObject(i).toString(), HotWords.class));
            }
            if (CommonMethod.isListEmpty(hisList)) {
                area_delete_his.setVisibility(View.GONE);
            } else {
                area_delete_his.setVisibility(View.VISIBLE);
            }
            setHis();
        }
    };

    /*private void setHis() {
        SharedPreferences searchListSp = getSharedPreferences("SEARCH_LIST", Activity.MODE_PRIVATE);
        String his = searchListSp.getString("SEARCH_LIST", "");
        if(TextUtils.isEmpty(his)){
            return;
        }
        String[] hisArr = his.split(",");
        String[] hisArrNew = (String[]) CommonMethod.ifRepeat(hisArr);
        for (int i = 0; i < hisArrNew.length; i++) {
            LinearLayout ll = new LinearLayout(mContext);
            ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DevAttr.dip2px(mContext, 40)));
            ll.setGravity(Gravity.CENTER);

            TextView textview = new TextView(mContext);
            String words = hisArrNew[i];
            if (words.length() > 20) {
                words = words.substring(0, 20) + "...";
            }
            textview.setText(words);
            textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textview.setTextColor(getResources().getColor(R.color.g333333));
            textview.setBackgroundResource(R.drawable.rec_gray_stroke_gray_solid_corner30);
            textview.setPadding(DevAttr.dip2px(mContext, 10), DevAttr.dip2px(mContext, 5), DevAttr.dip2px(mContext, 10), DevAttr.dip2px(mContext, 5));
            ll.addView(textview);

            View view = new View(mContext);
            LinearLayout.LayoutParams linearParams2 = new LinearLayout.LayoutParams(DevAttr.dip2px(mContext, 12), DevAttr.dip2px(mContext, 12));
            view.setLayoutParams(linearParams2);
            ll.addView(view);

            view_wordwrap_history.addView(ll);
        }
    }*/

    private void setHis() {
        view_wordwrap_history.removeAllViews();
        for (int i = 0; i < hisList.size(); i++) {
            LinearLayout ll = new LinearLayout(mContext);
            ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DevAttr.dip2px(mContext, 40)));
            ll.setGravity(Gravity.CENTER);
//            ll.setBackground(this.getResources().getDrawable(R.drawable.textborder));
            TextView textview = new TextView(mContext);
            String words = hisList.get(i).getKeyword();
            final String keywords = hisList.get(i).getKeyword();
            if (words.length() > 20) {
                words = words.substring(0, 20) + "...";
            }
            textview.setText(words);
            textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textview.setTextColor(getResources().getColor(R.color.g333333));
            textview.setBackgroundResource(R.drawable.rec_gray_stroke_gray_solid_corner30);
            textview.setPadding(DevAttr.dip2px(mContext, 10), DevAttr.dip2px(mContext, 5), DevAttr.dip2px(mContext, 10), DevAttr.dip2px(mContext, 5));
            ll.addView(textview);

            textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, CourseSearchResultActivity.class);
                    it.putExtra("keyword", keywords);
                    startActivity(it);
                    finish();
                }
            });

            View view = new View(mContext);
            LinearLayout.LayoutParams linearParams2 = new LinearLayout.LayoutParams(DevAttr.dip2px(mContext, 12), DevAttr.dip2px(mContext, 12));

            view.setLayoutParams(linearParams2);
            ll.addView(view);

            view_wordwrap_history.addView(ll);
        }
    }

    public void AddStatistics(String type) {
        if ("contentList".equals(type)) {
            HashMap<String, String> map = new HashMap<String, String>();
            MobclickAgent.onEventValue(this, "ContentList", map,0);
        }
    }
}
