package com.park61.teacherhelper.module.my.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.databinding.FragmentChooseKgBinding;
import com.park61.teacherhelper.module.my.ChooseGroupListActivity;
import com.park61.teacherhelper.module.my.KidGardenActivity;


/**
 * Created by chenlie on 2018/5/7.
 * <p>
 * 选择幼儿园引导页面
 */

public class FragmentChooseKG extends BaseFragment {

    private static final int REQ_CHOOSE_GROUP = 0;//选择幼儿园
    FragmentChooseKgBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_kg, container, false);
        parentView = binding.getRoot();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        binding.kgArea.setOnClickListener(v -> {
            //跳转幼儿园选择界面
            //binding.kgName.setText("测试测试");
            Intent it = new Intent(parentActivity, ChooseGroupListActivity.class);
            it.putExtra("from", "register");
            startActivityForResult(it, REQ_CHOOSE_GROUP);
        });

        if (GlobalParam.currentUser != null) {
            if (GlobalParam.currentUser.getIsAdmin() == 1) {//是管理员
                binding.kgName.setText(GlobalParam.currentUser.getSchoolName());
                ((KidGardenActivity) getActivity()).setGroupId((int) GlobalParam.currentUser.getGroupId());
                binding.goNext.setBackgroundResource(R.drawable.shape_corner_100f4);
                binding.kgArea.setOnClickListener(null);
            }
        }

        binding.kgName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.goNext.setBackgroundResource(R.drawable.shape_corner_100f4);
                } else {
                    binding.goNext.setBackgroundResource(R.drawable.shape_corner_100c9);
                }
            }
        });

        binding.goNext.setOnClickListener(v -> {
            String gName = binding.kgName.getText().toString();
            if (gName.length() > 0) {
                //去选择职务页面
                ((KidGardenActivity) getActivity()).goNext();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CHOOSE_GROUP) {
            if (resultCode == parentActivity.RESULT_OK) {
                String groupName = data.getStringExtra("groupName");
                int groupId = data.getIntExtra("groupId", -1);

                binding.kgName.setText(groupName);
                ((KidGardenActivity) getActivity()).setGroupId(groupId);
            }
        }
    }
}
