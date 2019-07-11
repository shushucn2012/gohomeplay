package com.park61.teacherhelper.module.clazz;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.json.NullStringToEmptyAdapterFactory;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.MessageEvent;
import com.park61.teacherhelper.common.tool.RegexValidator;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.clazz.adapter.FamilyConBookComtsAdapter;
import com.park61.teacherhelper.module.clazz.adapter.SwitchClazzListAdapter;
import com.park61.teacherhelper.module.clazz.bean.FamilyClazzComtItem;
import com.park61.teacherhelper.module.clazz.bean.TeachGClass;
import com.park61.teacherhelper.module.dict.PageParam;
import com.park61.teacherhelper.module.dict.RequestCode;
import com.park61.teacherhelper.module.home.adapter.ShowRvAdapter;
import com.park61.teacherhelper.module.home.bean.ContentItem;
import com.park61.teacherhelper.module.my.PhotoPublishActivity;
import com.park61.teacherhelper.module.my.adapter.PublishListAdapter;
import com.park61.teacherhelper.module.my.bean.PublishItem;
import com.park61.teacherhelper.module.workplan.adapter.OutTimeWorkRvAdapter;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.pw.FamilyConBookOperatePopWin;
import com.park61.teacherhelper.widget.pw.TaskChooseLevelPopWin;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;


/**
 * 家园联系薄主页
 *
 * @author shubei
 * @time 2018/11/22 12:22
 */
public class FamilyConBookMainActivity extends BaseActivity {

    private int PAGE_NUM = 0;
    private final int PAGE_SIZE = 6;
    private int totalPage = 100;

    private List<TeachGClass> clazzList = new ArrayList<>();
    private List<FamilyClazzComtItem> cList = new ArrayList<>();
    private int curClazzIndex = 0;//当前选择的班级序号
    private String searchChildName = "";//要搜索的孩子姓名

    private EditText edit_sousuo;
    private ImageView img_top_title_arrow;
    private ListView lv_classes;
    private SwitchClazzListAdapter mSwitchClazzListAdapter;
    private LRecyclerView rv_comments;
    private FamilyConBookComtsAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private FamilyConBookOperatePopWin mFamilyConBookOperatePopWin;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_familyconbook_main);
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        area_right = findViewById(R.id.area_right);
        lv_classes = findViewById(R.id.lv_classes);
        edit_sousuo = findViewById(R.id.edit_sousuo);
        edit_sousuo.setCursorVisible(false);
        img_top_title_arrow = findViewById(R.id.img_top_title_arrow);

        rv_comments = findViewById(R.id.rv_comments);
        rv_comments.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public void initData() {
        mSwitchClazzListAdapter = new SwitchClazzListAdapter(mContext, clazzList);
        lv_classes.setAdapter(mSwitchClazzListAdapter);

        cList = new ArrayList<>();
        adapter = new FamilyConBookComtsAdapter(mContext, cList);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rv_comments.setAdapter(mLRecyclerViewAdapter);
        asyncGetClazzList();
    }

    @Override
    public void initListener() {
        area_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonMethod.isListEmpty(clazzList)) {
                    dDialog.showDialog("提示", "您还没有添加班级，是否添加？", "取消", "确定",
                            null, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dDialog.dismissDialog();
                                    startActivity(new Intent(mContext, ClazzAddActivity.class));
                                }
                            });
                    return;
                }
               /* if (!"2".equals(GlobalParam.currentUser.getUserDuty())) {
                    showShortToast("没有发布权限");
                    return;
                }*/
                Intent it = new Intent(mContext, FamilyConBookPubActivity.class);
                it.putExtra("teachClassId", clazzList.get(curClazzIndex).getId() + "");
                startActivity(it);
            }
        });
        edit_sousuo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchChildName = edit_sousuo.getText().toString().trim();
                    refreshList();
                    hideKeyboard();
                }
                return false;
            }
        });
        edit_sousuo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(edit_sousuo.getText().toString())) {
                    findViewById(R.id.img_del).setVisibility(View.GONE);
                    searchChildName = "";
                    refreshList();
                    hideKeyboard();
                } else {
                    findViewById(R.id.img_del).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edit_sousuo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    edit_sousuo.setCursorVisible(true);// 再次点击显示光标
                }
                return false;
            }
        });
        findViewById(R.id.img_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_sousuo.setText("");
                findViewById(R.id.img_del).setVisibility(View.GONE);
            }
        });
        rv_comments.setOnRefreshListener(() -> {
            refreshList();
        });
        rv_comments.setPullRefreshEnabled(true);
        rv_comments.setOnLoadMoreListener(() -> {
            if (PAGE_NUM < totalPage - 1) {
                getNextPage();
            } else {
                rv_comments.setNoMore(true);
            }
        });
        adapter.setOnOperateClickedLsner(new FamilyConBookComtsAdapter.OnOperateClickedLsner() {
            @Override
            public void onDelOrEdit(View view, int pos) {
                int[] scr = new int[2];
                view.getLocationOnScreen(scr);//获取view的绝对位置
                showPopFormItem(view, scr, pos);
            }

            @Override
            public void onShare(int pos) {

            }
        });
        findViewById(R.id.area_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonMethod.isListEmpty(clazzList))//没有班级不能选
                    return;
                if (lv_classes.getVisibility() == View.VISIBLE) {
                    lv_classes.setVisibility(View.GONE);
                    img_top_title_arrow.setImageResource(R.mipmap.icon_fcb_toptitle_downarrow);
                } else {
                    lv_classes.setVisibility(View.VISIBLE);
                    img_top_title_arrow.setImageResource(R.mipmap.icon_fcb_toptitle_uparrow);
                }
            }
        });
        lv_classes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //lv_classes.setVisibility(View.GONE);
                //img_top_title_arrow.setImageResource(R.mipmap.icon_fcb_toptitle_downarrow);
                curClazzIndex = position;
                clazzList.get(curClazzIndex).setChosen(true);
                for (int i = 0; i < clazzList.size(); i++) {
                    if (i != curClazzIndex)
                        clazzList.get(i).setChosen(false);
                }
                setPagTitle("家园联系薄" + clazzList.get(curClazzIndex).getName());
                mSwitchClazzListAdapter.notifyDataSetChanged();
                refreshList();
            }
        });
        lv_classes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (lv_classes.getVisibility() == View.VISIBLE) {
                    lv_classes.setVisibility(View.GONE);
                    img_top_title_arrow.setImageResource(R.mipmap.icon_fcb_toptitle_downarrow);
                }
                return false;
            }
        });
    }

    /**
     * 刷新列表数据
     */
    public void refreshList() {
        PAGE_NUM = 0;
        //重置可以加载更多
        rv_comments.setNoMore(false);
        asyncGetPublishList();
    }

    /**
     * 获得列表下一页数据
     */
    public void getNextPage() {
        PAGE_NUM++;
        asyncGetPublishList();
    }

    /**
     * 请求数据
     */
    private void asyncGetClazzList() {
        String wholeUrl = AppUrl.host + AppUrl.userClassList;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            clazzList.clear();
            JSONArray dataJay = jsonResult.optJSONArray("list");
            // 指定Gson的日期格式
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
            if (dataJay != null && dataJay.length() > 0) {
                for (int i = 0; i < dataJay.length(); i++) {
                    TeachGClass c = gson.fromJson(dataJay.optJSONObject(i).toString(), TeachGClass.class);
                    if (i == 0)//默认选中第一个
                        c.setChosen(true);
                    clazzList.add(c);
                }
                mSwitchClazzListAdapter.notifyDataSetChanged();

                setPagTitle("家园联系薄" + clazzList.get(curClazzIndex).getName());
                findViewById(R.id.serach_area).setVisibility(View.VISIBLE);
                img_top_title_arrow.setVisibility(View.VISIBLE);
                findViewById(R.id.empty_view).setVisibility(View.GONE);
                asyncGetPublishList();
            } else {
                findViewById(R.id.serach_area).setVisibility(View.GONE);
                img_top_title_arrow.setVisibility(View.GONE);
                findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
            }
        }
    };

    private void asyncGetPublishList() {
        if (CommonMethod.isListEmpty(clazzList)) {
            showShortToast("没有加入班级");
            return;
        }
        String wholeUrl = AppUrl.host + AppUrl.teachCommentList;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        map.put("teachClassId", clazzList.get(curClazzIndex).getId());
        map.put("childName", searchChildName);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, eLsner);
    }

    BaseRequestListener eLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            rv_comments.refreshComplete(PAGE_SIZE);
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            rv_comments.refreshComplete(PAGE_SIZE);
            parseJosnToShow(jsonResult);
        }
    };

    /**
     * 解析列表数据
     */
    private void parseJosnToShow(JSONObject jsonResult) {
        JSONArray jayList = jsonResult.optJSONArray("rows");
        // 第一次查询的时候没有数据，则提示没有数据，页面置空
        if (PAGE_NUM == 0 && (jayList == null || jayList.length() <= 0)) {
            cList.clear();
            mLRecyclerViewAdapter.notifyDataSetChanged();
            findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
            return;
        } else {
            findViewById(R.id.empty_view).setVisibility(View.GONE);
        }
        // 首次加载清空所有项列表,并重置控件为可下滑
        if (PAGE_NUM == 0) {
            cList.clear();
        }
        ArrayList<FamilyClazzComtItem> currentPageList = new ArrayList<>();
        totalPage = jsonResult.optInt("pageCount");
        for (int i = 0; i < jayList.length(); i++) {
            JSONObject jot = jayList.optJSONObject(i);
            FamilyClazzComtItem contentItem = gson.fromJson(jot.toString(), FamilyClazzComtItem.class);
            currentPageList.add(contentItem);
        }
        cList.addAll(currentPageList);
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void showPopFormItem(View vew, int[] src, int position) {
        mFamilyConBookOperatePopWin = new FamilyConBookOperatePopWin(mContext);
        // 设置Popupwindow显示位置（从底部弹出）
        logout("=================showPopFormItem============x==================" + src[0]);
        logout("=================showPopFormItem============y==================" + src[1]);
        //mFamilyConBookOperatePopWin.showAtLocation(vew, Gravity.RIGHT | Gravity.TOP, src[0], src[1] + DevAttr.dip2px(mContext, 33));

        int maxHeight = DevAttr.getScreenHeight(mContext) - DevAttr.dip2px(mContext, 150);
        logout("=================showPopFormItem============maxHeight==================" + maxHeight);

        if (src[1] < maxHeight) {//屏幕显示不够时，上移显示
            mFamilyConBookOperatePopWin.area_top_arrow.setVisibility(View.VISIBLE);
            mFamilyConBookOperatePopWin.area_bottom_arrow.setVisibility(View.INVISIBLE);
            mFamilyConBookOperatePopWin.showAtLocation(vew, Gravity.RIGHT | Gravity.TOP, src[0], src[1] + DevAttr.dip2px(mContext, 33));
        } else {
            mFamilyConBookOperatePopWin.area_top_arrow.setVisibility(View.INVISIBLE);
            mFamilyConBookOperatePopWin.area_bottom_arrow.setVisibility(View.VISIBLE);
            mFamilyConBookOperatePopWin.showAtLocation(vew, Gravity.RIGHT | Gravity.TOP, src[0], src[1] - DevAttr.dip2px(mContext, 103));
        }

        final WindowManager.LayoutParams[] params = {getWindow().getAttributes()};
        //当弹出Popupwindow时，背景变半透明
        params[0].alpha = 0.7f;
        getWindow().setAttributes(params[0]);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        mFamilyConBookOperatePopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params[0] = getWindow().getAttributes();
                params[0].alpha = 1f;
                getWindow().setAttributes(params[0]);
            }
        });
        mFamilyConBookOperatePopWin.view_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFamilyConBookOperatePopWin.dismiss();
                if(!cList.get(position).isIsCommentCanEdit()) {
                    showShortToast("只能修改自己发布的评语");
                    return;
                }
                Intent it = new Intent(mContext, FamilyConBookPubActivity.class);
                it.putExtra("teachClassId", clazzList.get(curClazzIndex).getId() + "");
                it.putExtra("id", cList.get(position).getId());
                startActivity(it);
            }
        });
        mFamilyConBookOperatePopWin.view_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFamilyConBookOperatePopWin.dismiss();
                if(!cList.get(position).isIsCommentCanEdit()) {
                    showShortToast("只能删除自己发布的评语");
                    return;
                }
                dDialog.showDialog("提示", "确定删除吗？", "取消", "确定", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dDialog.dismissDialog();
                        asyncDeleteTeachComment(position);
                    }
                });
            }
        });
    }

    private void asyncDeleteTeachComment(int pos) {
        String wholeUrl = AppUrl.host + AppUrl.deleteTeachComment;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", cList.get(pos).getId());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, dLsner);
    }

    BaseRequestListener dLsner = new JsonRequestListener() {

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
            showShortToast("删除成功");
            refreshList();
        }
    };

    @Subscribe
    public void onMessage(MessageEvent event) {
        if ("REFRESH_FAMILYCONBOOK_MAIN".equals(event.getMessage())) {
            refreshList();
        } else if ("REFRESH_FAMILYCONBOOK_MAIN_ClASS".equals(event.getMessage())) {
            asyncGetClazzList();
        }
    }

    /**
     * 监控点击按钮如果点击在评论输入框之外就关闭输入框，变回报名栏
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus().getId() == edit_sousuo.getId()) {
                View v1 = edit_sousuo;
                if (isShouldHideInput(v1, ev)) {
                    edit_sousuo.setCursorVisible(false);// 隐藏光标
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
