package com.park61.teacherhelper.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.R;

/**
 * 公共ProgressDialog
 * 
 * @author super
 */
public class AlipayWaitProgressDialog {

	private Context mContext;
	private Dialog dialog;
	private View view;
	private LayoutInflater inflater;
	private ImageView dialog_image;
	private TextView dialog_text;
	private Animation dialogAnimation;

	public AlipayWaitProgressDialog(Context context) {
		this(context, null);
	}

	public AlipayWaitProgressDialog(Context context, AttributeSet attrs) {
		super();
		mContext = context;
		dialog = new Dialog(context, R.style.dialog);
		inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.alipay_progress_dialog_layout, null);
		dialog_image = (ImageView) view.findViewById(R.id.dialog_image);
		dialog_text = (TextView) view.findViewById(R.id.dialog_text);
		dialogAnimation = AnimationUtils.loadAnimation(mContext, R.anim.dialog_rotate);
		dialog.setContentView(view);
	}

	public void showDialog(String msg) {
		if (TextUtils.isEmpty(msg)) {
			dialog_text.setText("加载中...");
		} else {
			dialog_text.setText(msg);
		}
		if (!dialog.isShowing()) {
			try {
				dialog.show();
				dialog_image.startAnimation(dialogAnimation);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			dialog_text.setText(msg);
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

	public void setMessage(String msg) {
		dialog_text.setText(msg);
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

}
