/**
 * 
 */
package com.park61.teacherhelper.net.request.interfa;

import android.graphics.Bitmap;

/**
 * @description:图片下载
 * @author cai
 * @time:2014年12月12日下午3:44:16
 */
public interface  ImageRequestListener extends BaseRequestListener {
	/**
	 * 请求成功
	 * 
	 * @param requestId
	 *            请求编号
	 * @param url
	 *            本地请求的url
	 * @param bitmap
	 *            图片bitmap
	 */
	public void onSuccess(int requestId, String url, Bitmap bitmap);
}
