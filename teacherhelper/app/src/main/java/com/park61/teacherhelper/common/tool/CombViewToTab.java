package com.park61.teacherhelper.common.tool;

import java.util.List;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 控件联合体，用于将会产生联锁变化的控件归纳管理
 * 
 * @author super
 */
public class CombViewToTab {

	private List<View> tablist;
	private String defaultColor = "#FEF6D1";
	private int resDefault, resChoose;

	public CombViewToTab(List<View> tablist, int resDefault, int resChoose) {
		this.tablist = tablist;
		this.resDefault = resDefault;
		this.resChoose = resChoose;
	}

	public CombViewToTab(List<View> tablist) {
		this.tablist = tablist;
	}

	public void initNewCart() {
		View item = tablist.get(0);
		if (resDefault == 0)// 没有自定义资源时
			item.setBackgroundColor(Color.parseColor(defaultColor));
		else
			item.setBackgroundResource(resChoose);
		// tvItem.setTextColor(Color.parseColor("#e74c3c"));
		disColorOthers(0);
	}

	public void initLsner(final OnChangeEndLsner mOnChangeEndLsner) {
		for (int i = 0; i < tablist.size(); i++) {
			final View item = tablist.get(i);
			final int pos = i;
			item.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (resDefault == 0)// 没有自定义资源时
						item.setBackgroundColor(Color.parseColor(defaultColor));
					else
						item.setBackgroundResource(resChoose);
					// tvItem.setTextColor(Color.parseColor("#e74c3c"));
					disColorOthers(pos);
					mOnChangeEndLsner.doEnd(pos);
				}
			});
		}
	}

	protected void disColorOthers(int position) {
		for (int i = 0; i < tablist.size(); i++) {
			if (i != position) {
				View item = tablist.get(i);
				if (resDefault == 0)// 没有自定义资源时
					item.setBackgroundColor(Color.parseColor("#00000000"));
				else
					item.setBackgroundResource(resDefault);
				// tvItem.setTextColor(Color.parseColor("#000000"));
			}
		}
	}

	public interface OnChangeEndLsner {
		public void doEnd(int pos);
	}

}
