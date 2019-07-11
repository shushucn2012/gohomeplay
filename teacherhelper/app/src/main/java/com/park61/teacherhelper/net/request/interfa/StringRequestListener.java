package com.park61.teacherhelper.net.request.interfa;


/**
 * @Description:网络请求监听接口,适用返回结果为String类型字符串
 * @author:Cai
 * @time:2014年12月9日 11:41:34
 */
public interface StringRequestListener extends BaseRequestListener {

	/**
	 * 请求成功
	 * 
	 * @param requestId
	 *            请求编号
	 * @param url
	 *            本地请求的url
	 * @param result
	 *            结果集
	 */
	public void onSuccess(int requestId, String url, String result);

}
