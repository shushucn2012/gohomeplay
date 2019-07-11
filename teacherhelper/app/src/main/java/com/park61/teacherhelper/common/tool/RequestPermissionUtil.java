package com.park61.teacherhelper.common.tool;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * 权限类
 */
public class RequestPermissionUtil {

    public RequestPermissionUtil() {
    }

    public static RequestPermissionUtil requestPermissionUtil;


    public static synchronized RequestPermissionUtil getRequestPermissionUtilInstance() {
        if (requestPermissionUtil == null) {
            requestPermissionUtil = new RequestPermissionUtil();
        }
        return requestPermissionUtil;
    }

    /**
     * 检测授权
     *
     * @param context
     * @param ManifestPermission
     * @param requestCode        请求吗
     * @return true:已经授权   false没有授权
     */
    public boolean checkPermission(Activity context, int requestCode, String ManifestPermission) {
        int hasPermission = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasPermission = context.checkSelfPermission(ManifestPermission);
        }
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(new String[]{ManifestPermission}, requestCode);
            }
            return false;
        } else {
            return true;
        }
    }

    // 判断权限集合
    public boolean checkPermissions(Activity context, int requestCode, String... permissions) {
        int hasPermission = 0;
        for (String permission : permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hasPermission = context.checkSelfPermission(permission);
                if (hasPermission != 0) {
                    break;
                }
            }
        }

        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(permissions, requestCode);
            }
            return false;
        } else {
            return true;
        }
    }
}