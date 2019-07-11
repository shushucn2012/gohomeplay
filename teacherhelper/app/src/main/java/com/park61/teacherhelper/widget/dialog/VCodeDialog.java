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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.teacherhelper.R;
import com.park61.teacherhelper.widget.textview.TimeTextView;


/**
 * 验证码对话框 by super
 */
public class VCodeDialog {

	private Context mContext;
	private Dialog dialog;
	private View view;
	private LayoutInflater inflater;
	private Button button_confirm, button_cancel;
	private LinearLayout btn_send;
	private TextView tv_title, tv_send;
	private EditText edit_vcode;
	private TimeTextView ttv_time;

	@SuppressWarnings("deprecation")
	public VCodeDialog(Context context, String phoneNum) {
		mContext = context;
		dialog = new Dialog(context, R.style.dialog);
		inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.dialog_vcode_layout, null);
		button_confirm = (Button) view.findViewById(R.id.button_confirm);
		button_cancel = (Button) view.findViewById(R.id.button_cancel);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		edit_vcode = (EditText) view.findViewById(R.id.edit_vcode);
		btn_send = (LinearLayout) view.findViewById(R.id.btn_send);
		ttv_time = (TimeTextView) view.findViewById(R.id.ttv_time);
		tv_send = (TextView) view.findViewById(R.id.tv_send);

		tv_title.setText("验证码已发送到尾号" + phoneNum + "的手机");

		ttv_time.setIsFromVcode();
		ttv_time.setTimes(new long[] { 0, 0, 0, 61 });
		if (!ttv_time.isRun()) {
			ttv_time.run();
		}
		ttv_time.setOnTimeEndLsner(new TimeTextView.OnTimeEnd() {

			@Override
			public void onEnd() {
				btn_send.setBackgroundResource(R.drawable.btn_apply_selector);
				btn_send.setClickable(true);
				tv_send.setTextColor(mContext.getResources().getColor(
						R.color.gffffff));
				ttv_time.setTextColor(mContext.getResources().getColor(
						R.color.gffffff));
				ttv_time.setText("");
			}
		});

		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
		WindowManager m = ((Activity) context).getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		// p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
		p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65
		dialogWindow.setAttributes(p);
		dialog.setCancelable(false);
	}

	/**
	 * 开始计时
	 */
	public void reStartMyTimer() {
		if (!ttv_time.isRun()) {
			btn_send.setBackgroundResource(R.drawable.edit_search_shape);
			tv_send.setTextColor(mContext.getResources().getColor(
					R.color.g666666));
			ttv_time.setTextColor(mContext.getResources().getColor(
					R.color.g666666));
			ttv_time.setTimes(new long[] { 0, 0, 0, 61 });
			ttv_time.run();
		}
	}

	/**
	 * 返回输入的验证码
	 */
	public String getInputCode() {
		return edit_vcode.getText().toString().trim();
	}

	/**
	 * showDialog
	 * 
	 * @param listenerRight
	 *            第二个按钮事件
	 * @param listenerSend
	 *            发送按钮事件
	 */
	public void showDialog(OnClickListener listenerRight,
			OnClickListener listenerSend) {
		button_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismissDialog();
			}
		});
		button_confirm.setOnClickListener(listenerRight);
		btn_send.setOnClickListener(listenerSend);
		dialog.show();
	}

	/**
	 * 关闭对话框
	 */
	public void dismissDialog() {
		dialog.dismiss();
	}

	/**
	 * 判断对话框是否显示
	 */
	public boolean isShow() {
		if (dialog.isShowing()) {
			return true;
		} else {
			return false;
		}
	}

}
