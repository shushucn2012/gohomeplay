package com.park61.teacherhelper.widget.modular;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.json.NullStringToEmptyAdapterFactory;
import com.park61.teacherhelper.common.okhttp.OkHttpUtils;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.module.workplan.CalendarPlanMainActivity;
import com.park61.teacherhelper.module.workplan.TempMainActivity;
import com.park61.teacherhelper.module.workplan.adapter.TTaskDoneInfoAdapter;
import com.park61.teacherhelper.module.workplan.bean.TaskDoneInfoBean;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.dialog.CommonProgressDialog;
import com.park61.teacherhelper.widget.dialog.DoubleDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 行事历园中校管理员主页面
 * @author shubei
 * @time 2019/1/4 17:27
 */
public class TempManagerModule extends FrameLayout {

    //管理员行事历首页属性---------------start//
    private ListView lv_task_done_info;
    private EditText edit_sousuo;
    private ImageView img_del;
    private View area_to_temp_manage;

    private List<TaskDoneInfoBean> dataList = new ArrayList<>();
    private TTaskDoneInfoAdapter adapter;
    private String name;//搜索关键字
    private Context mContext;
    private OkHttpUtils netRequest;
    public CommonProgressDialog commonProgressDialog;
    public DoubleDialog dDialog;


    public TempManagerModule(Context context, View rootView,  OkHttpUtils myRequest) {
        super(context);
        mContext = context;
        netRequest = myRequest;
        commonProgressDialog = new CommonProgressDialog(mContext);;
        dDialog = new DoubleDialog(mContext);

        edit_sousuo = rootView.findViewById(R.id.edit_sousuo);
        edit_sousuo.setCursorVisible(false);
        lv_task_done_info = rootView.findViewById(R.id.lv_task_done_info);
        img_del = rootView.findViewById(R.id.img_del);
        area_to_temp_manage = rootView.findViewById(R.id.area_to_temp_manage);
    }

    public void init(){
        initData();
        initLsner();
    }

    private void initData() {
        //setPagTitle("行事历");
        //area_left.setVisibility(View.INVISIBLE);
        //area_right.setVisibility(View.VISIBLE);
        //((ImageView) findViewById(R.id.img_right)).setImageResource(R.mipmap.icon_to_temp_manage);

        adapter = new TTaskDoneInfoAdapter(mContext, dataList);
        lv_task_done_info.setAdapter(adapter);


        asyncAssignedTaskCalendarList();
    }

    private void initLsner() {
        area_to_temp_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, TempMainActivity.class));
            }
        });
        edit_sousuo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    name = edit_sousuo.getText().toString().trim();
                    asyncAssignedTaskCalendarList();
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
                    img_del.setVisibility(View.GONE);
                    name = "";
                    asyncAssignedTaskCalendarList();
                } else {
                    img_del.setVisibility(View.VISIBLE);
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
        img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_sousuo.setText("");
                img_del.setVisibility(View.GONE);
            }
        });
        adapter.setOnNoticeClickedListener(new TTaskDoneInfoAdapter.OnNoticeClickedListener() {
            @Override
            public void onClicked(int position) {
                asyncNotifyExecutor(dataList.get(position));
            }

            @Override
            public void onTitle(int position) {
                GlobalParam.teachClassId = dataList.get(position).getTeachClassId();
                Intent it = new Intent(mContext, CalendarPlanMainActivity.class);
                GlobalParam.taskCalendarClassId = dataList.get(position).getId();
                it.putExtra("taskCalendarClassId", dataList.get(position).getId());
                mContext.startActivity(it);
            }

            @Override
            public void deleteTask(int id, int position) {
                dDialog.showDialog("提示", "行事历一经删除，所有任务失效，不可再恢复，是否确认删除?", "取消", "确定", null,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dDialog.dismissDialog();
                                asyncDelTaskCalendarTemplateClass(id);
                            }
                        });
            }
        });
    }

    /**
     * 已分配模板列表
     */
    public void asyncAssignedTaskCalendarList() {
        String wholeUrl = AppUrl.host + AppUrl.assignedTaskCalendarList;
        Map<String, Object> map = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(name)) {
            map.put("name", name);
        }
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, alistener);
    }

    BaseRequestListener alistener = new JsonRequestListener() {

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
            dataList.clear();
            JSONArray actJay = jsonResult.optJSONArray("list");
            if (actJay != null && actJay.length() > 0) {
                for (int i = 0; i < actJay.length(); i++) {
                    JSONObject itemJot = actJay.optJSONObject(i);
                    Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
                    TaskDoneInfoBean g = gson.fromJson(itemJot.toString(), TaskDoneInfoBean.class);
                    dataList.add(g);
                }
                adapter.notifyDataSetChanged();
            }
        }
    };

    /**
     * 通知督办
     */
    public void asyncNotifyExecutor(TaskDoneInfoBean taskDoneInfoBean) {
        String wholeUrl = AppUrl.host + AppUrl.notifyExecutor;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", taskDoneInfoBean.getId());
        map.put("taskCalendarTemplateName", taskDoneInfoBean.getTaskCalendarTemplateName());
        map.put("taskOverdueNum", taskDoneInfoBean.getTaskOverdueNum());
        map.put("teachClassName", taskDoneInfoBean.getTeachClassName());
        map.put("teachGroupId", taskDoneInfoBean.getTeachGroupId());
        map.put("teachGroupName", taskDoneInfoBean.getTeachGroupName());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, tlistener);
    }

    BaseRequestListener tlistener = new JsonRequestListener() {

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
            showShortToast("通知短信已发送");
        }
    };

    /**
     * 行事历已分配模板列表删除
     */
    public void asyncDelTaskCalendarTemplateClass(int id) {
        String wholeUrl = AppUrl.host + AppUrl.delTaskCalendarTemplateClass;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskCalendarTemplateClassId", id);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, dlistener);
    }

    BaseRequestListener dlistener = new JsonRequestListener() {

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
            //重刷列表
            asyncAssignedTaskCalendarList();
        }
    };

    public void showDialog() {
        try {
            if (commonProgressDialog.isShow()) {
                return;
            } else {
                commonProgressDialog.showDialog(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissDialog() {
        try {
            if (commonProgressDialog.isShow()) {
                commonProgressDialog.dialogDismiss();
            } else {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showShortToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }


}
