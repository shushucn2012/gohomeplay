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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.teacherhelper.R;

import java.util.List;

/**
 * 通用选择框
 */
public class ComSelectDialogBottom {
    private OnItemSelect mOnItemSelect;
    private Dialog dialog;
    private View view;
    private LayoutInflater inflater;
    private ListView listView;
    private BottomDialogAdapter adapter;
    private List<String> lists;
    private TextView cancel;

    public ComSelectDialogBottom(Context context, List<String> lists) {
        dialog = new Dialog(context, R.style.dialog);
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.job_select_dialog, null);
        this.lists = lists;
        cancel = (TextView) view.findViewById(R.id.cancel);
        listView = (ListView) view.findViewById(R.id.dialog_listview);
        adapter = new BottomDialogAdapter(lists, context);
        listView.setAdapter(adapter);

        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.width = (int) (d.getWidth());
        p.height = (int) (d.getHeight() * 0.6);
        p.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(p);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnItemSelect != null) {
                    mOnItemSelect.onSelect(position);
                }
                dismissDialog();
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
