/**
 * 
 */
package com.park61.teacherhelper.net.request.interfa;

/**
 * @description:基本请求监听
 * @author cai
 * @time:2014年12月9日上午11:47:09
 */
public interface BaseRequestListener {
	/**
	 * 开始请求
	 * 
	 * @param requestId
	 *            请求编号
	 */
	public void onStart(int requestId);

	/**
	 * 请求失败
	 * 
	 * @param requestId
	 *            请求编号
	 * @param errorCode
	 *            错误码
	 * @param errorMsg
	 *            错误信息
	 */
	public void onError(int requestId, String errorCode, String errorMsg);
}
