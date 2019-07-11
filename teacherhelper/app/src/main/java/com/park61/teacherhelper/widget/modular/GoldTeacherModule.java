package com.park61.teacherhelper.widget.modular;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.module.home.ExpertListActivity;
import com.park61.teacherhelper.module.home.TeachHouseActivity;
import com.park61.teacherhelper.module.home.adapter.ExpertListAdapter;
import com.park61.teacherhelper.module.home.bean.GoldTeacher;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.park61.teacherhelper.common.tool.ViewInitTool.AddStatistics;

/**
 * 首页组件化-金牌培训师
 * Created by shubei on 2018/4/16.
 */

public class GoldTeacherModule extends LinearLayout {

    private ListView lv_teacher;
    private ExpertListAdapter mExpertListAdapter;
    private List<GoldTeacher> gList;
    private OnChangeClickCallBack callBack;

    public GoldTeacherModule(Context context, List<GoldTeacher> gList, String moduleName) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.gold_teacher_module, this, true);
        this.gList = gList;
        ((TextView) findViewById(R.id.tv_gold_teacher)).setText(moduleName);
        lv_teacher = (ListView) findViewById(R.id.lv_teacher);
        lv_teacher.setFocusable(false);
        mExpertListAdapter = new ExpertListAdapter(context, gList);
        lv_teacher.setAdapter(mExpertListAdapter);

        lv_teacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AddStatistics(context, gList.get(i).getId(), "goldTeach");
                Intent intent = new Intent(context, TeachHouseActivity.class);
                intent.putExtra("teachId", gList.get(i).getId());
                context.startActivity(intent);
            }
        });
        findViewById(R.id.area_see_more).setOnClickListener(v -> {
            context.startActivity(new Intent(context, ExpertListActivity.class));
        });
        findViewById(R.id.area_change_teacher).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null)
                    callBack.onChange();
            }
        });
    }

    /**
     * 切换数据
     */
    public void changeData(List<GoldTeacher> list) {
        gList.clear();
        gList.addAll(list);
        mExpertListAdapter.notifyDataSetChanged();
    }

    public interface OnChangeClickCallBack {
        void onChange();
    }

    public void setOnChangeClickCallBack(OnChangeClickCallBack c) {
        this.callBack = c;
    }
}
