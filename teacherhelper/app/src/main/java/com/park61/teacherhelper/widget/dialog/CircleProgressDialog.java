package com.park61.teacherhelper.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;

/**
 * 公共ProgressDialog
 *
 * @author super
 */
public class CircleProgressDialog {

    private Context mContext;
    private Dialog dialog;
    private View view;
    private LayoutInflater inflater;
    private ImageView dialog_image;
    private TextView dialog_text;
    private Animation dialogAnimation;
    private AnimationDrawable animationDrawable;

    private RoundProgressBar mRoundProgressBar2;
    private int progress = 0;


    public CircleProgressDialog(Context context) {
        this(context, null);
    }

    public CircleProgressDialog(Context context, AttributeSet attrs) {
        super();
        mContext = context;
        dialog = new Dialog(context, R.style.dialog);
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.circle_progress_dialog_layout, null);
        mRoundProgressBar2 = (RoundProgressBar) view.findViewById(R.id.roundProgressBar2);

        dialog.setContentView(view);
        dialog.setCancelable(false);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                   return true;
                }
                return false;
            }
        });
    }

    public void setProgress(int progress1){
        mRoundProgressBar2.setProgress(progress1);
    }

    public void showDialog() {
        try {
            dialog.show();
//            new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//                    while(progress <= 100){
//                        progress += 3;
//                        System.out.println(progress);
//                        mRoundProgressBar2.setProgress(progress);
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dialogDismiss() {
        try {
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
