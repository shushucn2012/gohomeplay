package com.park61.teacherhelper.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.park61.teacherhelper.R;

/**
 * app提示更新对话框
 * createby super on 20180608
 */
public class AppUpdateDialog {
    private Dialog dialog;
    private View view;
    private LayoutInflater inflater;
    private Button button_confirm_unforce, button_confirm_force, button_cancel;
    private TextView tv_title, tv_msg;

    public AppUpdateDialog(Context context) {
        dialog = new Dialog(context, R.style.dialog);
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.app_update_dialog_layout, null);
        button_confirm_unforce = (Button) view.findViewById(R.id.button_confirm_unforce);
        button_confirm_force = (Button) view.findViewById(R.id.button_confirm_force);
        button_cancel = (Button) view.findViewById(R.id.button_cancel);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_msg = (TextView) view.findViewById(R.id.tv_msg);

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
                ((Activity) context).finish();
            }
            return false;
        });
    }

    /***
     * 显示对话框
     * @param title 标题
     * @param message 信息
     * @param isForce 是否强制更新
     * @param listenerLeft 取消按钮事件
     * @param listenerRight 确定按钮事件
     */
    public void showDialog(String title, String message, boolean isForce, OnClickListener listenerLeft, OnClickListener listenerRight) {
        tv_title.setText(title);
        tv_msg.setText(message);
        if(isForce){//强制更新
            button_cancel.setVisibility(View.GONE);
            button_confirm_unforce.setVisibility(View.GONE);
            button_confirm_force.setVisibility(View.VISIBLE);
            button_confirm_force.setOnClickListener(listenerRight);
        }else {
            button_cancel.setVisibility(View.VISIBLE);
            button_confirm_unforce.setVisibility(View.VISIBLE);
            button_confirm_force.setVisibility(View.GONE);
            button_cancel.setOnClickListener(listenerLeft);
            button_confirm_unforce.setOnClickListener(listenerRight);
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

}
