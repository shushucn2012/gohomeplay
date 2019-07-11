package com.park61.teacherhelper.net.request;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;

/**
 * @Description:网络请求类
 * @author:Cai
 * @time:2014-6-17 下午2:21:33
 */
public abstract class BaseRequest<T> {
    public Context mContext;
    public String actTag;
    /**
     * 请求队列
     */
    private static RequestQueue mRequestQueue;
    /**
     * 插入队列中的请求
     */
    public Request<T> request = null;

    /**
     * 开始请求
     *
     * @param url           请求地址
     * @param requestMethod 网络请求方式，Method.GET==0, "get"方式; Method.POST==1, "post"方式
     * @param requstData    请求数据
     * @param requestId     请求id
     * @param listener      监听接口
     */
    public void startBaseRequest(String url, int requestMethod, String requstData, final int requestId, final BaseRequestListener listener) {
        listener.onStart(requestId);
        if (!isNetworkOk(mContext)) {
            listener.onError(requestId, null, mContext.getString(R.string.net_request_wrong));
        } else {
            doRequest(url, requestMethod, requstData, requestId, listener);
            request.setTag(actTag + "_" + requestId);//设置请求的标签
            request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 0, 1f));//设置请求的超时时间，重试次数，重试因子
            Log.out("---startBaseRequest---addQueue---TAG---" + request.getTag());
            getRequestQueue().add(request);
        }
    }

    /**
     * 开始请求
     *
     * @param url           请求地址
     * @param requestMethod 网络请求方式，Method.GET==0, "get"方式; Method.POST==1, "post"方式
     * @param requstData    请求数据
     * @param requestId     请求id
     * @param listener      监听接口
     * @param sec            超时秒数
     */
    public void startBaseRequest(String url, int requestMethod, String requstData, final int requestId, final BaseRequestListener listener, int sec) {
        listener.onStart(requestId);
        if (!isNetworkOk(mContext)) {
            listener.onError(requestId, null, mContext.getString(R.string.net_request_wrong));
        } else {
            doRequest(url, requestMethod, requstData, requestId, listener);
            request.setTag(actTag + "_" + requestId);//设置请求的标签
            request.setRetryPolicy(new DefaultRetryPolicy(sec * 1000, 0, 1f));//设置请求的超时时间，重试次数，重试因子
            Log.out("---startBaseRequest---addQueue---TAG---" + request.getTag());
            getRequestQueue().add(request);
        }
    }

    /**
     * 网络请求
     *
     * @param url           请求地址
     * @param requestMethod 网络请求方式，Method.GET==0, "get"方式; Method.POST==1, "post"方式
     * @param requstData    请求数据
     * @param requestId     请求id
     * @param listener      监听接口
     */
    public abstract void doRequest(String url, int requestMethod, String requstData, int requestId, BaseRequestListener listener);

    /**
     * 设置context 用于请求队列tag
     */
    public void setMainContext(Context context) {
        this.mContext = context;
    }

    /**
     * 设置用于请求队列tag
     */
    public void setActTag(String actTag) {
        this.actTag = actTag;
    }

    /**
     * 获取网络请求队列
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    /**
     * 中断（取消）当前页面单个请求
     *
     * @param requestId 请求id
     */
    public void cancelRequest(Context context, int requestId) {
        getRequestQueue().cancelAll(context.getClass().getSimpleName() + "_" + requestId);
    }

    /**
     * 中断（取消）单个页面所有请求
     */
    public void cancelAllRequest(String actTag) {
        getRequestQueue().cancelAll(actTag);
    }

    /**
     * 检验网络是否有连接，有则true，无则false
     *
     * @param context
     */
    public boolean isNetworkOk(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }

}
