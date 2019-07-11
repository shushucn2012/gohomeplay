package com.park61.teacherhelper.module.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.SoftKeyBoardListener;
import com.park61.teacherhelper.module.activity.adapter.TrainEvaQuestionListAdapter;
import com.park61.teacherhelper.module.activity.adapter.TrainEvaQuestionRvAdapter;
import com.park61.teacherhelper.module.activity.bean.TrainEvaQuestionBean;
import com.park61.teacherhelper.module.activity.bean.TrainQuestionSectionBean;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.listview.ListViewForScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 培训问卷页面
 *
 * @author shubei
 * @time 2019/3/8 17:33
 */
public class TrainFeedBackActivity extends BaseActivity {

    //private ListViewForScrollView lv_evaluate, lv_choose;
    private RecyclerView rv_evaluate, rv_choose;
    private TextView tv_section1_title, tv_section2_title;

    private int trainingId;//培训id
    private List<TrainQuestionSectionBean> sectionList = new ArrayList<>();

    //    private TrainEvaQuestionListAdapter section1QListAdapter, section2QListAdapter;
    private TrainEvaQuestionRvAdapter section1QListAdapter, section2QListAdapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_train_feedback);

    }

    @Override
    public void initView() {
        setPagTitle("评价反馈");
       /* lv_evaluate = findViewById(R.id.lv_evaluate);
        lv_evaluate.setFocusable(false);
        lv_choose = findViewById(R.id.lv_choose);
        lv_choose.setFocusable(false);*/
        tv_section1_title = findViewById(R.id.tv_section1_title);
        tv_section2_title = findViewById(R.id.tv_section2_title);

        rv_evaluate = findViewById(R.id.rv_evaluate);
        rv_evaluate.setLayoutManager(new LinearLayoutManager(mContext));
        rv_evaluate.setFocusable(false);
       /* rv_evaluate.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
                return false;
            }
        });*/
        //解决数据加载不完的问题
        rv_evaluate.setNestedScrollingEnabled(false);
        //rv_evaluate.setHasFixedSize(true);

        rv_choose = findViewById(R.id.rv_choose);
        rv_choose.setLayoutManager(new LinearLayoutManager(mContext));
        rv_choose.setFocusable(false);
       /* rv_choose.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
                return false;
            }
        });*/
        //解决数据加载不完的问题
        rv_choose.setNestedScrollingEnabled(false);
        //rv_choose.setHasFixedSize(true);
    }

    @Override
    public void initData() {
        trainingId = getIntent().getIntExtra("trainingId", -1);
        asyncGetData();
    }

    @Override
    public void initListener() {
        findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncSubmitQuestion();
            }
        });
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                //showShortToast("显示" + height);
                findViewById(R.id.btn_commit).setVisibility(View.GONE);
            }

            @Override
            public void keyBoardHide(int height) {
                //showShortToast("隐藏 " + height);
                findViewById(R.id.btn_commit).setVisibility(View.VISIBLE);
            }
        });
    }

    private void asyncSubmitQuestion() {
        String wholeUrl = AppUrl.host + AppUrl.submitQuestion;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("trainingId", trainingId);
        JSONArray jay = new JSONArray();
        try {
            for (int j = 0; j < Math.min(sectionList.size(), 2); j++) {
                for (int i = 0; i < sectionList.get(j).getListQuestion().size(); i++) {
                    TrainEvaQuestionBean questionBean = sectionList.get(j).getListQuestion().get(i);
                    JSONObject jsonObject = new JSONObject();
                    if (questionBean.getType() == 1) {//评分
                        if (questionBean.getChosenAnswerIndex() == 0) {
                            showShortToast("有问题还未填写");
                            return;
                        }
                        int postion = 0;
                        switch (questionBean.getChosenAnswerIndex()) {
                            case 4:
                                postion = 0;
                                break;
                            case 3:
                                postion = 1;
                                break;
                            case 2:
                                postion = 2;
                                break;
                            case 1:
                                postion = 3;
                                break;
                        }
                        jsonObject.put("answers", questionBean.getListAnswer().get(postion).getId());
                        jsonObject.put("type", questionBean.getType());
                        jsonObject.put("score", questionBean.getListAnswer().get(postion).getScore());
                        jsonObject.put("id", questionBean.getId());
                    } else if (questionBean.getType() == 2 || questionBean.getType() == 3) {
                        if (TextUtils.isEmpty(getChoseAnswer(questionBean))) {
                            showShortToast("有问题还未填写");
                            return;
                        }
                        jsonObject.put("answers", getChoseAnswer(questionBean));
                        jsonObject.put("type", questionBean.getType());
                        jsonObject.put("score", 0);
                        jsonObject.put("id", questionBean.getId());
                    } else {
                        if (TextUtils.isEmpty(questionBean.getAnswerContent())) {
                            showShortToast("有问题还未填写");
                            return;
                        }
                        jsonObject.put("answers", questionBean.getAnswerContent());
                        jsonObject.put("type", questionBean.getType());
                        jsonObject.put("score", 0);
                        jsonObject.put("id", questionBean.getId());
                    }
                    jay.put(jsonObject);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        map.put("listQuestion", jay.toString());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, sLsner);
    }

    /**
     * 获取所选item的id，多个逗号拼接
     */
    private String getChoseAnswer(TrainEvaQuestionBean questionBean) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < questionBean.getListAnswer().size(); i++) {
            if (questionBean.getListAnswer().get(i).isChosen()) {
                sb.append(questionBean.getListAnswer().get(i).getId());
                sb.append(",");
            }
        }
        String str = sb.toString();
        if (!TextUtils.isEmpty(str)) {
            str = str.substring(0, sb.length() - 1);
        }
        return str;
    }

    BaseRequestListener sLsner = new JsonRequestListener() {

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
            Intent it = new Intent(mContext, TrainFeedBackSucActivity.class);
            it.putExtra("trainingId", trainingId);
            startActivity(it);
            finish();
        }
    };

    /**
     * 请求列表数据
     */
    private void asyncGetData() {
        String wholeUrl = AppUrl.host + AppUrl.listQuestion;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("trainingId", trainingId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, mLsner);
    }

    BaseRequestListener mLsner = new JsonRequestListener() {

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
            findViewById(R.id.loading_cover).setVisibility(View.GONE);
            JSONArray actJay = jsonResult.optJSONArray("list");
            if (actJay != null || actJay.length() > 0) {
                for (int i = 0; i < actJay.length(); i++) {
                    sectionList.add(gson.fromJson(actJay.optJSONObject(i).toString(), TrainQuestionSectionBean.class));
                }
                tv_section1_title.setText(sectionList.get(0).getHeadline());
                section1QListAdapter = new TrainEvaQuestionRvAdapter(mContext, sectionList.get(0).getListQuestion());
                rv_evaluate.setAdapter(section1QListAdapter);

                if (sectionList.size() > 1) {
                    int questionNum = sectionList.get(1).getListQuestion().size();
                    tv_section2_title.setText(sectionList.get(1).getListQuestion().get(questionNum - 1).getQuestionName());
                    section2QListAdapter = new TrainEvaQuestionRvAdapter(mContext, sectionList.get(1).getListQuestion());
                    rv_choose.setAdapter(section2QListAdapter);
                }
            }
        }
    };

    EditText editTextCurFocus = null;

    //使editText点击外部时候失去焦点
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {//点击editText控件外部
                InputMethodManager imm = (InputMethodManager) getSystemService(mContext.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    assert v != null;
                    hideKeyboard();
                    if (editTextCurFocus != null) {
                        editTextCurFocus.clearFocus();
                    }
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            editTextCurFocus = (EditText) v;
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

}
