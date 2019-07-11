package com.park61.teacherhelper.common.set;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.park61.teacherhelper.common.crash.CrashHandler;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.InstallUtils;
import com.park61.teacherhelper.common.set.Log;

/**
 * 应用程序初始化
 *
 * @author super
 */
public class AppInit {

    /**
     * 初始化系统环境
     *
     * @param context
     * @param isDebugModel 是否为调试状态
     * @param isTestServer 是否使用用测试服务器
     */
    public static void initEnvironment(Context context, boolean isDebugModel,
                                       boolean isTestServer) {
        Log.init(isDebugModel);
        if (!isDebugModel) {
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(context);
        }
        getVersionCode(context);
        com.park61.teacherhelper.common.set.GlobalParam.macAddress = getLocalMacAddress(context);
        Log.e("initEnvironment", "macAddress===" + com.park61.teacherhelper.common.set.GlobalParam.macAddress);
        deleteUpdateFile(context);
        AppUrl.host = isTestServer ? AppUrl.demoHost : AppUrl.releaseHost;
        Log.out(DevAttr.getScreenWidth(context) + "");
        Log.out(DevAttr.getScreenHeight(context) + "");
        Log.out(DevAttr.getScreenDensity(context) + "");
        GlobalParam.APP_EXTERNAL_CACHE_DIR = context.getExternalCacheDir();
    }

    /**
     * 删除更新文件
     */
    public static void deleteUpdateFile(Context context) {
        File file = new File(InstallUtils.getApkFilePath(context, 0));
        if (file.exists()) {
            file.delete();
        }
    }

    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    // 根据IP获取本地Mac
    public static String getLocalMacAddressFromIp(Context context) {
        String mac_s = "";
        try {
            byte[] mac;
            NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress
                    .getByName(getLocalIpAddress()));
            mac = ne.getHardwareAddress();
            mac_s = byte2hex(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mac_s;
    }

    public static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs = hs.append("0").append(stmp);
            else {
                hs = hs.append(stmp);
            }
        }
        return String.valueOf(hs);
    }

    // 获取本地IP
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("WifiPreference IpAddress", ex.toString());
        }
        return null;
    }

    /**
     * 获取客户端版本号
     */
    public static void getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            GlobalParam.versionName = pi.versionName;
            GlobalParam.versionCode = pi.versionCode;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

}
