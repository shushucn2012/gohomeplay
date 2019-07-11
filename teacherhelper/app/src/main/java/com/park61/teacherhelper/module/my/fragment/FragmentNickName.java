package com.park61.teacherhelper.module.my.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.park61.teacherhelper.BaseFragment;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.databinding.FragmentInputNickBinding;
import com.park61.teacherhelper.module.my.AgentInforActivity;

/**
 * Created by chenlie on 2018/5/7.
 *
 * 选择幼儿园引导页面
 */

public class FragmentNickName extends BaseFragment {

    FragmentInputNickBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_input_nick, container, false);
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

        binding.kgName.setOnEditorActionListener((v, actionId, event) -> event != null &&  event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
        binding.kgName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    binding.goNext.setBackgroundResource(R.drawable.shape_corner_100f4);
                }else{
                    binding.goNext.setBackgroundResource(R.drawable.shape_corner_100c9);
                }
                if(s.length()>8){
                    showShortToast("昵称最多输入8个字符");
                    binding.kgName.setText(s.toString().subSequence(0,8));
                    binding.kgName.setSelection(8);
                }
            }
        });

        binding.goNext.setOnClickListener(v -> {
            String name = binding.kgName.getText().toString();
            if(name.length() >0){
                //设置昵称
                ((AgentInforActivity)getActivity()).setNickName(name);
                ((AgentInforActivity)getActivity()).goNext();
            }
        });
    }
}
