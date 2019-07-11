package com.park61.teacherhelper.module.clazz;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.FU;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.pw.ComBottomPopWin;
import com.park61.teacherhelper.widget.pw.StuChoseSexPopWin;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 添加学生
 *
 * @author shubei
 * @time 2018/11/22 15:49
 */
public class AddStudentToClazzActivity extends BaseActivity {

    private TextView tv_student_sex;
    private EditText edit_name;

    private String teachClassId;
    private String name;
    private int sex;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_add_student_to_clazz);
    }

    @Override
    public void initView() {
        setPagTitle("添加学生");
        ((TextView) findViewById(R.id.tv_area_right)).setText("添加");
        tv_student_sex = findViewById(R.id.tv_student_sex);
        edit_name = findViewById(R.id.edit_name);
    }

    @Override
    public void initData() {
        teachClassId = getIntent().getStringExtra("gClassId");
    }

    @Override
    public void initListener() {
        findViewById(R.id.area_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edit_name.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    showShortToast("请填写学生姓名！");
                    return;
                }
                String sexStr = tv_student_sex.getText().toString().trim();
                sex = "男".equals(sexStr) ? 0 : 1;
                asyncAddStudent();
            }
        });
        findViewById(R.id.area_choose_sex).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopFormBottom();
            }
        });
    }

    /**
     * 请求数据
     */
    private void asyncAddStudent() {
        String wholeUrl = AppUrl.host + AppUrl.addChild;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("gClassId", teachClassId);
        map.put("name", name);
        map.put("sex", sex);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
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
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            showShortToast("添加成功");
            finish();
        }
    };

    public void showPopFormBottom() {
        List<String> items = new ArrayList<String>();
        items.add("男");
        items.add("女");
        StuChoseSexPopWin mComBottomPopWin = new StuChoseSexPopWin(mContext, items, "选择性别");
        // 设置Popupwindow显示位置（从底部弹出）
        mComBottomPopWin.showAtLocation(findViewById(R.id.main_view), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        mComBottomPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
        mComBottomPopWin.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_student_sex.setText(items.get(position));
                mComBottomPopWin.dismiss();
            }
        });
    }
}
