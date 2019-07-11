package com.park61.teacherhelper.module.workplan;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.tool.MessageEvent;
import com.park61.teacherhelper.module.workplan.adapter.TempsAdapter;
import com.park61.teacherhelper.module.workplan.bean.TempBean;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 模板首页
 * Created by shubei on 2018/7/27.
 */

public class TempMainActivity extends BaseActivity {

    private ListView lv_temp;

    private List<TempBean> dataList = new ArrayList<>();
    private TempsAdapter adapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_tempmain);
    }

    @Override
    public void initView() {
        //EventBus.getDefault().register(this);  //事件的注册
        lv_temp = (ListView) findViewById(R.id.lv_temp);
    }

    @Override
    public void initData() {
        setPagTitle("模版管理");
        adapter = new TempsAdapter(mContext, dataList);
        lv_temp.setAdapter(adapter);
        asyncGetTemplateList();
    }

    @Override
    public void initListener() {
        adapter.setOnAllcateClickedListener(new TempsAdapter.OnAllcateClickedListener() {
            @Override
            public void onClicked(int position) {
                Intent intent = new Intent(mContext, ChooseGroupAllocateActivity.class);
                intent.putExtra("taskCalendarTemplateId", dataList.get(position).getId());
                startActivity(intent);
            }
        });
    }

    /**
     * 获取模版列表
     */
    public void asyncGetTemplateList() {
        String wholeUrl = AppUrl.host + AppUrl.templateList;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, alistener);
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
                    TempBean g = gson.fromJson(itemJot.toString(), TempBean.class);
                    dataList.add(g);
                }
                adapter.notifyDataSetChanged();
            }
        }
    };

    /*@Subscribe
    public void onMessage(MessageEvent event) {
        if ("ALLOCATE_DONE".equals(event.getMessage())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dDialog.showDialog("提示", "行事历已分配成功，是否立即发送短信通知园长和老师来使用呢？", "暂时不用", "发送短信", null,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showShortToast("短信发送成功！");
                                }
                            });
                }
            }, 500);
        }
    }*/

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this); //解除注册
    }*/
}
