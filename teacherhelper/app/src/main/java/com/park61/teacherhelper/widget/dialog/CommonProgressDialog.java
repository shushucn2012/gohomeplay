package com.park61.teacherhelper.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.module.workplan.WorkManageMainActivity;

/**
 * 公共ProgressDialog
 *
 * @author super
 */
public class CommonProgressDialog {

    private Context mContext;
    private Dialog dialog;
    private View view;
    private LayoutInflater inflater;
    private ImageView dialog_image;
    private TextView dialog_text;
    private Animation dialogAnimation;
    private AnimationDrawable animationDrawable;

    public CommonProgressDialog(Context context) {
        this(context, null);
    }

    public CommonProgressDialog(Context context, AttributeSet attrs) {
        super();
        mContext = context;
        dialog = new Dialog(context, R.style.dialog);
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.common_progress_dialog_layout, null);
        dialog_image = (ImageView) view.findViewById(R.id.dialog_image);
        dialog_text = (TextView) view.findViewById(R.id.dialog_text);

//        dialog_image.setImageResource(R.drawable.animation_list_loading);
//        animationDrawable = (AnimationDrawable) dialog_image.getDrawable();


        dialogAnimation = AnimationUtils.loadAnimation(mContext, R.anim.dialog_rotate);
        dialogAnimation.setInterpolator(new LinearInterpolator());
        dialog.setContentView(view);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mContext == null)
                        return false;
                    if (isShow()) {
                        dialogDismiss();
                    }
                    if (((Activity) mContext) instanceof WorkManageMainActivity)//4个主页不关闭
                        ((Activity) mContext).finish();
                }
                return false;
            }
        });
    }

    public void showDialog(String msg) {
        if (TextUtils.isEmpty(msg)) {
            dialog_text.setText("loading......");
        } else {
            dialog_text.setVisibility(View.VISIBLE);
            dialog_text.setText(msg);
        }
        dialog_image.startAnimation(dialogAnimation);
//        animationDrawable.start();
        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMessage(String msg) {
        dialog_text.setText(msg);
    }

    public void dialogDismiss() {
        try {
            dialog_image.clearAnimation();
//            animationDrawable.stop();
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isShow() {
        if (dialog.isShowing()) {
            return true;
        } else {
            return false;
        }
    }

    public void setCancelable(boolean is) {
        dialog.setCancelable(is);
    }

    public Dialog getDialog() {
        return dialog;
    }


}
