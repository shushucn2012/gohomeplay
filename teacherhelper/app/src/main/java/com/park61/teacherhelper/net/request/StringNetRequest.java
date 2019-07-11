/**
 *
 */
package com.park61.teacherhelper.net.request;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.tool.FU;

import static android.content.Context.TELEPHONY_SERVICE;
//import com.wjl.comsmartpos.modules.user.UserManager;
//import com.wjl.comsmartpos.modules.user.bean.UserBean;

/**
 * @author cai
 * @description:string类型字符传导方式网络请求
 * @time:2014年12月11日下午12:37:13
 */
public class StringNetRequest extends DataRequest {
    private static StringNetRequest stringNetRequest;

    private StringNetRequest(Context context) {
        mContext = context;
    }

    public synchronized static StringNetRequest getInstance(Context context) {
        if (stringNetRequest == null) {
            stringNetRequest = new StringNetRequest(context);
        }
        return stringNetRequest;
    }

    @Override
    public Map<String, String> getPrivateHeaders() {
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE); //获取IMEI
        String IMEI = telephonyManager.getDeviceId();
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        header.put("cityId", GlobalParam.chooseCityCode);
        if (stringNetRequest.request.getUrl().contains(AppUrl.checkUpdateStatus)) {
            header.put("userToken", "");
        } else {
            header.put("userToken", GlobalParam.userToken);
        }
        header.put("timestamp", GlobalParam.timestamp);
        header.put("signCode", GlobalParam.x_sign);
        JSONObject clientInfoJot = new JSONObject();
        try {
            clientInfoJot.put("clientAppName", "android");
            clientInfoJot.put("clientAppVersion", GlobalParam.versionName);
            clientInfoJot.put("clientSystem", "android");
            clientInfoJot.put("clientVersion", android.os.Build.VERSION.RELEASE);
            clientInfoJot.put("IMEI", IMEI);
            clientInfoJot.put("deviceCode", GlobalParam.macAddress);
            if (GlobalParam.longitude == 0 || GlobalParam.longitude == 4.9e-324) {
                clientInfoJot.put("longitude", "");
            } else {
                clientInfoJot.put("longitude", GlobalParam.longitude);
            }
            if (GlobalParam.latitude == 0 || GlobalParam.latitude == 4.9e-324) {
                clientInfoJot.put("latitude", "");
            } else {
                clientInfoJot.put("latitude", GlobalParam.latitude);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        header.put("clientInfo", clientInfoJot.toString());
        header.put("bundleId", GlobalParam.BUNDLE_ID);
        header.put("deviceCode", GlobalParam.macAddress + "_" + IMEI);
        Log.e("httpheader", "------httpheader======" + header);
        return header;
    }

}
