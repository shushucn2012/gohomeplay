package com.park61.teacherhelper.net.request.interfa;

import org.json.JSONObject;

/**
 * @Description:网络请求监听接口,适用返回结果为json
 * @author:Cai
 * @time:2014年12月9日 11:41:34
 */
public interface JsonRequestListener extends BaseRequestListener {

	/**
	 * 请求成功
	 * 
	 * @param requestId
	 *            请求编号
	 * @param url
	 *            本地请求的url
	 * @param jsonResult
	 *            结果集
	 */
	public void onSuccess(int requestId, String url, JSONObject jsonResult);
	
}
