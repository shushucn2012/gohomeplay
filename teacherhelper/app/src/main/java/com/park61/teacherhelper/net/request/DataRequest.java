package com.park61.teacherhelper.net.request;

import android.content.Intent;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.park61.teacherhelper.common.tool.ExitAppUtils;
import com.park61.teacherhelper.common.tool.ViewInitTool;
import com.park61.teacherhelper.module.login.LoginActivity;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.net.request.interfa.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author cai
 * @description:数据请求(抽象)
 * @time:2014年12月11日下午12:22:31
 */
public abstract class DataRequest extends BaseRequest<String> {
    /**
     * json类型字符传导模式网络,开始请求
     *
     * @param url           请求地址
     * @param requestMethod 网络请求方式，Method.GET==0, "get"方式; Method.POST==1, "post"方式
     * @param requstData    请求数据
     * @param requestId     请求id
     * @param listener      监听接口
     */
    public void startRequest(String url, int requestMethod, String requstData,
                             final int requestId, final BaseRequestListener listener) {
        Log.e("startRequest", "url======" + url);
        super.startBaseRequest(url, requestMethod, requstData, requestId, listener);
    }

    public void startRequest(String url, int requestMethod, String requstData,
                             final int requestId, final BaseRequestListener listener, int sec) {
        Log.e("startRequest", "url======" + url);
        super.startBaseRequest(url, requestMethod, requstData, requestId, listener, sec);
    }

    @Override
    public void doRequest(final String url, int requestMethod,
                          final String requstData, final int requestId,
                          final BaseRequestListener listener) {
        request = new StringRequest(requestMethod, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                GlobalParam.x_sign = null;
                Log.e("response", response);
                if (listener instanceof JsonRequestListener) {
                    JSONObject jsonResponse;
                    try {
                        jsonResponse = new JSONObject(response);
                        String status = jsonResponse.optString("status");
                        String code = jsonResponse.optString("code");
                        if ("200".equals(status)) {
                            ((JsonRequestListener) listener).onSuccess(requestId, url, jsonResponse);
                            return;
                        }
                        if (code.equals("0") || code.equals("00000100010")
                                || code.equals("0000010011")
                                || code.equals("0000010004")
                                || code.equals("0000010005")
                                || code.equals("0000010008")
                                || code.equals("0000027002")) {
                            if (jsonResponse.optJSONObject("data") != null) {
                                ((JsonRequestListener) listener).onSuccess(requestId, url,
                                        jsonResponse.optJSONObject("data"));
                            } else {
                                JSONArray arr = jsonResponse.optJSONArray("data");
                                JSONObject job = new JSONObject();
                                job.put("list", arr);
                                if (arr == null) {
                                    job.put("data", jsonResponse.optString("data"));
                                }
                                ((JsonRequestListener) listener).onSuccess(requestId, url, job);
                            }
                        } else {
                            String msg = jsonResponse.optString("msg");
                            if (msg == null || msg.equals("")) {
                                msg = jsonResponse.optString("ext");
                            }
                            final String innerMsg = msg;
                            if (code.equals("0000000002")) {// token失效
                                goToLogin(innerMsg);
                                if (GlobalParam.userToken == null) {
                                    listener.onError(requestId, code + "", "未登录，请登录！");
                                } else {
                                    listener.onError(requestId, code + "", msg);
                                }
                            } else if (code.equals("0000025025")) {
                                String dataStr = jsonResponse.optString("data");
                                listener.onError(requestId, code + "", dataStr);
                            } else {
                                listener.onError(requestId, code + "", msg);
                            }
                        }
                    } catch (JSONException e) {
                        listener.onError(requestId, null, mContext.getString(R.string.net_request_error));
                    }
                } else if (listener instanceof StringRequestListener) {
                    ((StringRequestListener) listener).onSuccess(requestId, url, response);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                GlobalParam.x_sign = null;
                listener.onError(requestId, null, mContext.getString(R.string.net_request_fail));
                Log.e("onErrorResponse", "error-toString===" + error.toString());
                Log.e("onErrorResponse", "error-getMessage===" + error.getMessage());
                Log.e("onErrorResponse", "error-getCause===" + error.getCause());
                error.printStackTrace();
            }
        }) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                if (requstData == null) {
                    return super.getBody();
                } else {
                    return requstData.getBytes();
                }
            }

            /*
             * (non-Javadoc)
             *
             * @see com.android.volley.Request#getHeaders()
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getPrivateHeaders();
            }

            /*
             * (non-Javadoc)
             *
             * @see
             * com.android.volley.toolbox.StringRequest#parseNetworkResponse
             * (com.android.volley.NetworkResponse)
             */
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String parsed;
                try {
                    parsed = new String(response.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    parsed = new String(response.data);
                }
                return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
    }

    public void goToLogin(final String innerMsg) {
        GlobalParam.userToken = null;
        CommonMethod.startOnlyNewActivity(mContext, LoginActivity.class, new Intent(mContext, LoginActivity.class));
        Intent changeIt = new Intent();
        changeIt.setAction("ACTION_TAB_CHANGE");
        changeIt.putExtra("TAB_NAME", "tab_main");
        mContext.sendBroadcast(changeIt);
    }

    /**
     * 获取私有请求头
     */
    public abstract Map<String, String> getPrivateHeaders();

}
