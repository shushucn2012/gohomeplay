package com.park61.teacherhelper.common.set;

import android.os.Environment;

import com.park61.teacherhelper.module.login.bean.UserBean;

import java.io.File;

/**
 * 静态全局参数，变量
 *
 * @author super
 */
public class GlobalParam {

    /**
     * 系统版本号
     */
    public static int versionCode = 0;
    /**
     * 系统版本名称
     */
    public static String versionName = "";
    /**
     * 新系统版本名称
     */
    public static String versionNameNext = "";
    /**
     * 系统版本名称
     */
    public static String macAddress;
    /**
     * userToken
     */
    public static String userToken = null;
    /**
     * 客户端当前纬度
     */
    public static double latitude = 0;
    /**
     * 客户端当前经度
     */
    public static double longitude = 0;
    /**
     * 客户端当前定位出的城市名称
     */
    public static String locationCityStr = "";
    /**
     * 客户端当前定位出的区域名称
     */
    public static String locationCountryStr = "";
    /**
     * 客户端当前用户选择的城市名称
     */
    public static String chooseCityStr = "";
    /**
     * 客户端当前用户选择的城市数据库编号
     */
    public static String chooseCityCode = "";
    /**
     * 当前用户信息
     */
    public static UserBean currentUser;
    /**
     * 请求头x_sign
     */
    public static String x_sign;
    /**
     * 请求头timestamp
     */
    public static String timestamp;
    /**
     * tencent appid
     */
    public static final String TENCENT_APP_ID = "1106397092";// "222222"1104835049;
    /**
     * weixin appid
     */
    public static final String WX_APP_ID = "wx7ca78f48145c63db";// 测试：wx9f6a8153fff62089;正式：wx753971e884df0fe0
    /**
     * weixin AppSecret
     */
    public static final String WX_APP_SECRET = "3965a54cdcc817e33d5dea12a51cd681";//"55a1d1f512244a7228d7a2af84bb99ee";

    /**
     * 秀萌照主页是否需要刷新
     */
    public static boolean GrowingMainNeedRefresh = true;

    /**
     * 商品首页是否需要刷新
     */
    public static boolean SalesMainNeedRefresh = true;

    /**
     * 我的选图页面是否需要刷新数据
     */
    public static boolean PhotosNeedRefresh = false;

    /**
     * 我的订单页面刷新数据
     */
    public static boolean MyOrderNeedRefresh = false;
    /**
     * 购物车页面刷新数据
     */
    public static boolean TradeCartNeedRefresh = false;
    /**
     * 图片下载本机路径
     */
    public static String IMAGE_FILE_PATH = Environment.getExternalStorageDirectory() + "/qjw/Qjw_Images/";

    /**
     * 商品订单详情页面刷新数据
     */
    public static boolean TradeOrderDetailNeedRefresh = false;
    /**
     * 订单列表页面刷新数据
     */
    public static boolean TradeOrderListNeedRefresh = false;

    /**
     * 登录类型
     */
    public static int LOGIN_TYPE = 0;// 061区用户登录；1联合登陆

    /**
     * 小课游戏类型码
     */
    public final static String SMALL_CLASS_CODE = "7";

    /**
     * 活动订单状态值
     */
    public static class ActOrderState {
        /**
         * 待付款
         */
        public static final String WAITFORPAY = "waitforpay";
        /**
         * 已报名
         */
        public static final String APPLIED = "applied";
        /**
         * 待评价
         */
        public static final String WAITFORCOMT = "waitforcomt";
        /**
         * 已参加
         */
        public static final String DONE = "done";
    }

    /**
     * 我的分红页面刷新数据
     */
    public static boolean ProfitActivityNeedRefresh = false;
    /**
     * 拼团页面刷新数据
     */
    public static boolean GroupPurchaseActivityNeedRefresh = false;
    /**
     * 售后列表页面刷新数据
     */
    public static boolean AfterSaleListNeedRefresh = false;
    /**
     * you
     */
    public static String YOUMENG_DEVICE_TOKEN = "";
    /**
     * 用于区分应用的标识，跟iOS保持一致
     */
    public static final String BUNDLE_ID = "com.61Park.TeacherAssistant";
    /**
     * 当前店铺id
     */
    public static long CUR_SHOP_ID = 0;
    /**
     * 当前区域id
     */
    public static long CUR_COUNTRY_ID = 0;
    /**
     * 当前孩子id
     */
    public static long CUR_CHILD_ID = 0;

    public static long CUR_TOYSHARE_APPLY_ID = 0;

    public static int MSG_NUM = 0;

    public static String CUR_SHOP_PHONE;

    public static String CUR_SHOP_NAME;

    public static String CUR_SHOP_IMG;

    public static int CUR_COURSE_ID = 0;

    /***
     * 4.4以下(也就是kitkat以下)的版本
     */
    public static final int KITKAT_LESS = 0;
    /***
     * 4.4以上(也就是kitkat以上)的版本,当然也包括最新出的5.0棒棒糖
     */
    public static final int KITKAT_ABOVE = 1;

    /***
     * 裁剪图片成功后返回
     */
    public static final int INTENT_CROP = 2;

    public static String ORDER_TYPE = "teacher_meet";//double11
    //服务器时间
    public static String SERVER_TIMESTAMP = "";
    //是否是园中校管理员
    public static boolean isTempManager = false;
    //是否是幼儿园管理员
    public static boolean isGroupManager = false;
    //当前幼儿园班级id
    public static int teachClassId = 0;
    //模板id
    public static int taskCalendarClassId = 0;

    public static File APP_EXTERNAL_CACHE_DIR;
}
