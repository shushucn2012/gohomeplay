/**
 * 
 */
package com.park61.teacherhelper.net.request;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

/**
 * @description:json类型字符传导方式网络请求
 * @author cai
 * @time:2014年12月11日下午12:37:13
 */
public class JsonNetRequest extends DataRequest {
	private static JsonNetRequest jsonRequest;

	private JsonNetRequest(Context context) {
		mContext = context;
	}

	public synchronized static JsonNetRequest getInstance(Context context) {
		if (jsonRequest == null) {
			jsonRequest = new JsonNetRequest(context);
		}
		return jsonRequest;
	}
    	
	@Override
	public Map<String, String> getPrivateHeaders() {
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");
		return header;
	}

}
