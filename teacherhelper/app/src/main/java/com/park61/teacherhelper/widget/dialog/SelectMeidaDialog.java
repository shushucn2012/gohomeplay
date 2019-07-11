package com.park61.teacherhelper.widget.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.teacherhelper.R;

/**
 * 通用选择框
 */
public class SelectMeidaDialog {
    private OnItemSelect mOnItemSelect;
    private Dialog dialog;
    private View view;
    private LayoutInflater inflater;
    private TextView cancel,item2;
    private LinearLayout item1;

    public SelectMeidaDialog(Context context) {
        dialog = new Dialog(context, R.style.dialog);
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.media_select_dialog, null);
        cancel = (TextView) view.findViewById(R.id.cancel);
        item1 = (LinearLayout) view.findViewById(R.id.area_camera);
        item2 = (TextView) view.findViewById(R.id.area_photo);

        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.width = (int) (d.getWidth());
        p.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(p);

        item1.setOnClickListener(v -> {
            dismissDialog();
            if (mOnItemSelect != null) {
                mOnItemSelect.onSelect(1);
            }
        });

        item2.setOnClickListener(v -> {
            dismissDialog();
            if (mOnItemSelect != null) {
                mOnItemSelect.onSelect(2);
            }
        });

        cancel.setOnClickListener(v -> dialog.dismiss());
    }

    public void showDialog() {
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

    public interface OnItemSelect {
        void onSelect(int position);
    }

    public void setOnItemSelectLsner(OnItemSelect lsner) {
        this.mOnItemSelect = lsner;
    }

}
