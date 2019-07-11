package com.park61.teacherhelper.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.DevAttr;

/**
 * 可以失败重刷的进度加载框
 * 
 * @author super
 */
public class CanRefreshProgressDialog {

	private Context mContext;
	private Dialog dialog;
	private View rootView;
	private LayoutInflater inflater;
	private ImageView dialog_image;
	private Animation dialogAnimation;

	public CanRefreshProgressDialog(Context context) {
		mContext = context;
		dialog = new Dialog(context, R.style.can_refresh_dialog);
		inflater = LayoutInflater.from(context);
		rootView = inflater.inflate(R.layout.canrefresh_progress_dialog_layout, null);
		Button left_img = (Button) rootView.findViewById(R.id.left_img);
		left_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((Activity) mContext).finish();
			}
		});
		dialog_image = (ImageView) rootView.findViewById(R.id.dialog_image);

		dialogAnimation = AnimationUtils.loadAnimation(mContext, R.anim.dialog_rotate);
		dialogAnimation.setInterpolator(new LinearInterpolator());

		dialog.setContentView(rootView);
		dialog.setCancelable(false);
		rootView.setFocusable(true);
		rootView.setFocusableInTouchMode(true);
		rootView.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
					if(mContext == null)
						return false;
					/*if(isShow()) {
						dialogDismiss();
					}*/
					((Activity)mContext).finish();
				}
				return false;
			}
		});
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		p.height = DevAttr.getScreenHeight(mContext);
		p.width = DevAttr.getScreenWidth(mContext);
		dialogWindow.setAttributes(p);
	}

	public void showDialog() {
		try {
			dialog_image.startAnimation(dialogAnimation);
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dialogDismiss() {
		try {
			dialog_image.clearAnimation();
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

}
