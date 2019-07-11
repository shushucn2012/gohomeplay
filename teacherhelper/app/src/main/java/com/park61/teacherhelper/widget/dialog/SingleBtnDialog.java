package com.park61.teacherhelper.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.park61.teacherhelper.R;

/**
 * 单选按钮弹出框 by super
 */
public class SingleBtnDialog {
    private Dialog dialog;
    private View view;
    private LayoutInflater inflater;
    private Button button_cancel;
    private TextView tv_title, tv_msg;

    public SingleBtnDialog(Context context) {
        dialog = new Dialog(context, R.style.dialog);
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.single_btn_dialog_layout, null);
        button_cancel = (Button) view.findViewById(R.id.button_cancel);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_msg = (TextView) view.findViewById(R.id.tv_msg);

        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);
    }

    /**
     * showDialog
     */
    public void showDialog(String title, String message, String rightText, OnClickListener listenerRight) {
        tv_title.setText(title);
        tv_msg.setText(message);
        button_cancel.setText(rightText);
        if (listenerRight == null) {
            listenerRight = new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismissDialog();
                }
            };
        }
        button_cancel.setOnClickListener(listenerRight);
        setCancelable(false);
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
        //setCancelable(true);
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
