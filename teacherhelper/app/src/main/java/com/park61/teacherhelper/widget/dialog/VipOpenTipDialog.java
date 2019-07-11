package com.park61.teacherhelper.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.okhttp.OkHttpUtils;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.webview.ShowImageWebView;

import org.json.JSONObject;

/**
 * 会员开通提示框
 */
public class VipOpenTipDialog {
    private Dialog dialog;
    private View view;
    private LayoutInflater inflater;
    private Button btn_start;
    private TextView tv_tip_title;
    private ShowImageWebView wv_content_start;
    private OkHttpUtils netRequest;

    public VipOpenTipDialog(Context context, OkHttpUtils netRequest) {
        dialog = new Dialog(context, R.style.dialog);
        this.netRequest = netRequest;
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.vip_open_dialog_layout, null);
        tv_tip_title = view.findViewById(R.id.tv_tip_title);
        btn_start = view.findViewById(R.id.btn_start);
        wv_content_start = view.findViewById(R.id.wv_content_start);
        dialog.setContentView(view);

        //Window dialogWindow = dialog.getWindow();
        //WindowManager m = ((Activity) context).getWindowManager();
        //Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        //WindowManager.LayoutParams p = dialogWindow.getAttributes();
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        //p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65
        //dialogWindow.setAttributes(p);

        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                if (context == null)
                    return false;
                if (isShow()) {
                    dismissDialog();
                }
            }
            return false;
        });
        asyncGetMemberRightIntro();
    }

    /***
     * 显示对话框
     * @param title 标题
     * @param listenerRight 确定按钮事件
     */
    public void showDialog(String title, OnClickListener listenerRight) {
        //asyncGetMemberRightIntro();
        tv_tip_title.setText(title);
        if (listenerRight == null) {
            dialog.dismiss();
            btn_start.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else {
            btn_start.setOnClickListener(listenerRight);
        }
        setCancelable(false);
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }

    public boolean isShow() {
        if (dialog.isShowing()) {
            return true;
        } else {
            return false;
        }
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setCancelable(boolean is) {
        dialog.setCancelable(is);
    }

    /*
     * 会员权益文案
     */
    public void asyncGetMemberRightIntro() {
        String wholeUrl = AppUrl.host + AppUrl.memberRightIntro;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, glistener);
    }

    BaseRequestListener glistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            ViewInitTool.initShowimgWebview(wv_content_start);
            ViewInitTool.setWebData(wv_content_start, jsonResult.optString("data"));
        }
    };

}
