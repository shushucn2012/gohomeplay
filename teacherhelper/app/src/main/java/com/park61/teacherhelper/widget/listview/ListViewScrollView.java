package com.park61.teacherhelper.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ListViewScrollView extends ListView {

	public ListViewScrollView(Context context) {
		super(context);
	}

	public ListViewScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ListViewScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	/**
	 * ��д�÷������ﵽʹListView��ӦScrollView��Ч��
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
