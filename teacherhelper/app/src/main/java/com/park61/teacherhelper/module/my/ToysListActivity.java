package com.park61.teacherhelper.module.my;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;

import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.databinding.ActivityToysListBinding;
import com.park61.teacherhelper.module.my.adapter.ToysAdapter;
import com.park61.teacherhelper.module.my.bean.ToysBean;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlie on 2017/12/29.
 *
 * 老师查看 一键购买装备列表
 */

public class ToysListActivity extends BaseActivity {

    ActivityToysListBinding binding;

    @Override
    public void setLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_toys_list);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        String list = getIntent().getStringExtra("list");
        List<ToysBean> data = new ArrayList<>();
        JSONArray arr = null;
        try {
            arr = new JSONArray(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(arr !=null && arr.length()>0){
            for(int i=0; i<arr.length(); i++){
                ToysBean b = gson.fromJson(arr.optJSONObject(i).toString(), ToysBean.class);
                data.add(b);
            }
        }

        ToysAdapter adapter = new ToysAdapter(this, data);
        binding.toysLv.setLayoutManager(new LinearLayoutManager(mContext));
        LRecyclerViewAdapter lAdapter = new LRecyclerViewAdapter(adapter);
        binding.toysLv.setAdapter(lAdapter);
    }

    @Override
    public void initListener() {
        binding.setGoBack(v -> finish());
        binding.toysLv.setPullRefreshEnabled(false);
        binding.toysLv.setLoadMoreEnabled(false);
    }
}
