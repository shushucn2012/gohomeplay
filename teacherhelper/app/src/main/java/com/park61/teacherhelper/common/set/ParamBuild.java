package com.park61.teacherhelper.common.set;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.park61.teacherhelper.module.login.bean.AliyunUploadBean;

/**
 * 通讯接口传入参数组件
 *
 * @author super
 */
public class ParamBuild {
    /**
     * 将map参数拼接为String
     *
     * @param params
     *            参数集合
     */
    /**
     * 将map参数拼接为String
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static String buildParams(Map params) {
        // 每一天请求都添加当前时间参数
        params.put("timestamp", System.currentTimeMillis());
        calcServerSign(params);
        boolean f = false;
        StringBuilder sb = new StringBuilder();
        StringBuilder sbp = new StringBuilder();//专为打印没有转码的数据方便调试
        for (Map.Entry kv : (Set<Map.Entry>) params.entrySet()) {
            if (f) {
                sb.append("&");
                sbp.append("&");
            }
            Object v = kv.getValue();
            if (v instanceof List) {
                List l = (List) v;
                for (Object item : l) {
                    sb.append("&");
                    // sb.append(kv.getkey()).append("=").append(urlencoder.encode((string) item));
                    sb.append(kv.getKey()).append("=").append((String) item);
                }
            } else {
                sb.append(kv.getKey()).append("=").append(kv.getValue() == null ? "" : URLEncoder.encode(kv.getValue().toString()));
                sbp.append(kv.getKey()).append("=").append(kv.getValue() == null ? "" : kv.getValue().toString());
            }
            f = true;
        }
        Log.e("http-params", "------http-params======" + sbp.toString());
        return sb.toString();
    }

    @SuppressWarnings("rawtypes")
    private static void calcServerSign(Map params) {
        List<String> sortedDataItemList = new ArrayList<String>();
        Iterator iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            // String paramName = (String) entry.getKey();
            // String paramValues = (String) entry.getValue();
            // if (paramValues != null) {
            sortedDataItemList.add(entry.getKey() + "=" + entry.getValue());
            // }
        }

        // 升序
        Collections.sort(sortedDataItemList);
        StringBuffer sb = new StringBuffer();
        for (String data : sortedDataItemList) {
            sb.append(data);
        }
        //GlobalParam.x_sign = encodeMD5(sb.append("sO&C%3Mq@lqY4PHt").toString());
        GlobalParam.timestamp = System.currentTimeMillis() + "";
        GlobalParam.x_sign = encodeMD5("D6DBD134138A42B3A6E14D2603A82C6E" + GlobalParam.timestamp);
    }

    /**
     * MD5加密
     */
    public static String encodeMD5(String strInput) {
        StringBuffer buf = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(strInput.getBytes("UTF-8"));
            byte b[] = md.digest();
            buf = new StringBuffer(b.length * 2);
            for (int i = 0; i < b.length; i++) {
                if (((int) b[i] & 0xff) < 0x10) { /* & 0xff转换无符号整型 */
                    buf.append("0");
                }
                buf.append(Long.toHexString((int) b[i] & 0xff)); /* 转换16进制,下方法同 */
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return buf.toString();
    }

    /**
     * 组建获取游戏列表数据
     *
     * @param curPage 当前页码
     */
    public static String getActList(int curPage, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        return buildParams(map);
    }

    /**
     * 组建获取店铺游戏列表数据
     *
     * @param curPage 当前页码
     */
    public static String getShopActList(Long merchantId, int curPage,
                                        int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("merchantId", merchantId);
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        return buildParams(map);
    }

    /**
     * 组建搜索店铺列表数据
     *
     * @param curPage  当前页码
     * @param pageSize 每页条数
     * @param name     店铺名称
     */
    public static String searchShop(String name, Long childId, int curPage,
                                    int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("childId", childId);
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        return buildParams(map);
    }

    /**
     * 组建获取店铺游戏列表数据通过条件
     *
     * @param merchantId       店铺id
     * @param curPage          当前页
     * @param pageSize         每页条数
     * @param actBussinessType 游戏业务类型
     * @param actType          游戏参与类型
     * @param isFree           是否免费
     * @param actStartTime     游戏开始时间
     * @param actEndTime       游戏结束时间
     * @return String
     */
    public static String getShopActListByCond(Long merchantId, int curPage,
                                              int pageSize, String actBussinessType, String actType,
                                              String isFree, String actStartTime, String actEndTime) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("merchantId", merchantId);
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        map.put("actBussinessType", actBussinessType);
        map.put("actType", actType);
        map.put("isFree", isFree);
        map.put("actStartTime", actStartTime);
        map.put("actEndTime", actEndTime);
        //map.put("currActDate", "2016-08-30");
        return buildParams(map);
    }

    /**
     * 组建获取（用户信息）请求参数 不传参数
     */
    public static String getUserInfo() {
        return "";
    }

    /**
     * 组建更新（用户信息）请求参数
     */
    public static String updateUser(String pictureUrl, String petName,
                                    String name, String address, String mobile) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(pictureUrl))
            map.put("pictureUrl", pictureUrl);
        if (!TextUtils.isEmpty(petName))
            map.put("petName", petName);
        if (!TextUtils.isEmpty(name))
            map.put("name", name);
        if (!TextUtils.isEmpty(address))
            map.put("address", address);
        if (!TextUtils.isEmpty(mobile))
            map.put("mobile", mobile);
        return buildParams(map);
    }

    /***
     * 组建更新（孩子信息）请求参数
     *
     * @param id
     *            孩子id
     * @param pictureUrl
     *            头像url
     * @param petName
     *            昵称
     * @param name
     *            姓名
     * @param birthday
     *            生日
     * @param sex
     *            性别
     * @param relationId
     *            关系id
     * @return
     */
    public static String updateChild(Long id, String pictureUrl,
                                     String petName, String name, String birthday, String sex,
                                     String relationId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        if (!TextUtils.isEmpty(pictureUrl))
            map.put("pictureUrl", pictureUrl);
        if (!TextUtils.isEmpty(petName))
            map.put("petName", petName);
        if (!TextUtils.isEmpty(name))
            map.put("name", name);
        if (!TextUtils.isEmpty(birthday))
            map.put("birthday", birthday);
        if (!TextUtils.isEmpty(sex))
            map.put("sex", sex);
        if (!TextUtils.isEmpty(relationId))
            map.put("relationId", relationId);
        return buildParams(map);
    }

    /**
     * 组建获取（用户孩子列表）请求参数 不传参数
     */
    public static String getUserChilds() {
        return "";
    }

    /**
     * 组建获取（用户孩子关系）请求参数
     *
     * @param childId 孩子id
     */
    public static String getRelation(Long childId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("childId", childId);
        return buildParams(map);
    }

    /**
     * 组建关注游戏请求参数
     *
     * @param activityId 游戏id
     */
    public static String focusAct(Long activityId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activityId", activityId);
        return buildParams(map);
    }

    /**
     * 组建获取游戏详情请求参数
     *
     * @param id 游戏id
     */
    public static String getActDetails(Long id, Long refTemplateId) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (id != null && id != -1)
            map.put("id", id);
        if (refTemplateId != null && refTemplateId != -1)
            map.put("refTemplateId", refTemplateId);
        return buildParams(map);
    }

    /**
     * 组建确认支付请求参数
     *
     * @param applyId 订单id
     */
    public static String payConfirm(Long applyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applyId", applyId);
        return buildParams(map);
    }

    /**
     * 组建获取订单详情请求参数
     *
     * @param applyId 订单id
     */
    public static String getApplyInfo(Long applyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applyId", applyId);
        return buildParams(map);
    }

    /**
     * 组建获取订单详情请求参数(小课)
     *
     * @param applyIds 订单ids
     */
    public static String getApplysInfo(String applyIds) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applyIds", applyIds);
        return buildParams(map);
    }

    /**
     * 组建取消报名请求参数
     *
     * @param applyId 订单id
     */
    public static String cancelApply(Long applyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applyId", applyId);
        return buildParams(map);
    }

    /**
     * 组建提交评价请求参数
     *
     * @param activityApplyId 订单id
     * @param deviceScore     设备分
     * @param exerciseScore   锻炼分
     * @param enjoyScore      乐趣分
     * @param content         评价内容
     * @param pictureUrlList  评价图片url
     */
    public static String evaluateAct(Long activityApplyId, Integer deviceScore,
                                     Integer exerciseScore, Integer enjoyScore, String content,
                                     List<String> pictureUrlList) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activityApplyId", activityApplyId);
        map.put("deviceScore", deviceScore);
        map.put("exerciseScore", exerciseScore);
        map.put("enjoyScore", enjoyScore);
        map.put("content", content);
        map.put("enjoyScore", enjoyScore);
        JSONArray jay = new JSONArray();
        for (int i = 0; i < pictureUrlList.size(); i++) {
            jay.put(pictureUrlList.get(i));
        }
        map.put("pictureUrlList", jay.toString());
        return buildParams(map);
    }

    /**
     * 组建获取游戏评论请求参数
     *
     * @param actId 游戏id
     */
    public static String getActComts(Long actId, Long refTemplateId,
                                     Integer curPage, Integer pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("actId", actId);
        map.put("refTemplateId", refTemplateId);
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        return buildParams(map);
    }

    /**
     * 组建发布游戏评论请求参数
     *
     * @param actId    游戏id
     * @param parentId 回复的回复id
     * @param rootId   回复的评论id
     * @param content  评论内容
     */
    public static String comtAct(Long actId, Long parentId, Long rootId,
                                 String content) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("actId", actId);
        map.put("parentId", parentId);
        map.put("rootId", rootId);
        map.put("content", content);
        return buildParams(map);
    }

    /**
     * 组建获取萌照列表数据
     *
     * @param curPage 当前页码
     */
    public static String getGrowingShowLis(int curPage, int pageSize,
                                           Long childId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        map.put("childId", childId);
        return buildParams(map);
    }

    /**
     * 组建添加萌照请求参数
     *
     * @param childId 孩子id
     * @param comment 内容
     */
    public static String addShowPhotos(Long childId, String comment,
                                       String pictureUrl) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("childId", childId);
        map.put("comment", comment);
        map.put("pictureUrl", pictureUrl);
        return buildParams(map);
    }

    public static String addBlogPhotos(String itemTitle, String coverUrl, String itemDesc, String summary, String imgUrls) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemTitle", itemTitle);
        map.put("coverUrl", coverUrl);
        map.put("itemDesc", itemDesc);
        map.put("summary", summary);
        map.put("imgUrl", imgUrls);
        return buildParams(map);
    }

    /**
     * 组建获取宝贝能力数据请求参数
     */
    public static String getBbCapacity(Long userChildId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userChildId", userChildId);
        return buildParams(map);
    }

    /**
     * 组建获取宝贝体格列表数据请求参数
     *
     * @param childId 孩子id
     * @param type    数据类型 0 体重；1身高
     */
    public static String getGrowingRecs(int curPage, int pageSize,
                                        Long childId, Integer type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        map.put("childId", childId);
        map.put("type", type);
        return buildParams(map);
    }

    /**
     * 组建获取宝贝成长日程列表数据请求参数
     *
     * @param childId 孩子id
     */
    public static String getBabySchedules(Long childId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("childId", childId);
        return buildParams(map);
    }

    /**
     * 组建添加体格数据请求参数
     *
     * @param childId     孩子id
     * @param weight      体重
     * @param height      身高
     * @param growingDate 成长日期
     */
    public static String addGrowingRec(Long childId, String weight,
                                       String height, String growingDate) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("childId", childId);
        map.put("weight", weight);
        map.put("height", height);
        map.put("growingDate", growingDate);
        return buildParams(map);
    }

    /**
     * 组建修改体格数据请求参数
     *
     * @param id          记录id
     * @param weight      体重
     * @param height      身高
     * @param growingDate 成长日期
     */
    public static String updateGrowingRec(Long id, String weight,
                                          String height, String growingDate) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        if (weight != null)
            map.put("weight", weight);
        if (height != null)
            map.put("height", height);
        if (growingDate != null)
            map.put("growingDate", growingDate);
        return buildParams(map);
    }

    /**
     * 组建获取体格数据正常范围请求参数
     *
     * @param childId     孩子id
     * @param growingDate 成长日期
     */
    public static String getGrowingDataRg(Long childId, String growingDate) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("childId", childId);
        map.put("growingDate", growingDate);
        return buildParams(map);
    }

    /**
     * 组建获取体格图表数据请求参数
     *
     * @param childId 孩子id
     * @param way     排序方式 0按月；1按季度；2按年
     * @param type    数据类型 0 体重；1身高
     */
    public static String getGrowingPoints(Long childId, Integer way,
                                          Integer type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("childId", childId);
        map.put("way", way);
        map.put("type", type);
        return buildParams(map);
    }

    /**
     * 组建登录请求参数
     *
     * @param mobile   手机号
     * @param password 密码
     */
    public static String login(String mobile, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userMobile", mobile);
        map.put("password", password);
        return buildParams(map);
    }

    /**
     * 组建注册请求参数
     *
     * @param mobile     手机号
     * @param password   密码
     * @param password2  确认密码
     * @param verifyCode 验证码
     */
    public static String register(String mobile, String password,
                                  String password2, String verifyCode, String recommendsCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mobile", mobile);
        map.put("password", password);
        map.put("password2", password2);
        map.put("verifyCode", verifyCode);
        map.put("recommendsCode", recommendsCode);
        return buildParams(map);
    }

    /**
     * 组建重置密码请求参数
     *
     * @param mobile     手机号
     * @param password   密码
     * @param verifyCode 验证码
     */
    public static String reset(String mobile, String password, String verifyCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mobile", mobile);
        map.put("password", password);
        map.put("verifyCode", verifyCode);
        return buildParams(map);
    }

    /**
     * 组建绑定请求参数
     *
     * @param mobile     手机号
     * @param verifyCode 验证码
     */
    public static String bind(String mobile, String verifyCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mobile", mobile);
        map.put("verifyCode", verifyCode);
        return buildParams(map);
    }

    /**
     * 组建发送验证码请求参数
     *
     * @param mobile 手机号
     */
    public static String getRegVcode(String mobile) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mobile", mobile);
        return buildParams(map);
    }

    /**
     * 组建添加孩子请求参数
     *
     * @param name       姓名
     * @param birthday   生日
     * @param relationId 与用户关系
     * @param sex        性别
     * @param petName    小名
     * @param pictureUrl 头像url
     */
    public static String addChild(String name, String birthday,
                                  String relationId, String sex, String petName, String pictureUrl) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("birthday", birthday);
        map.put("relationId", relationId);
        map.put("sex", sex);
        map.put("petName", petName);
        map.put("pictureUrl", pictureUrl);
        return buildParams(map);
    }

    /**
     * 组建获取阿里云oss加密url请求参数
     *
     * @param piclist 图片
     */
    public static String getAliyunUrl(List<AliyunUploadBean> piclist) {
        Map<String, Object> map = new HashMap<String, Object>();
        JSONArray jay = new JSONArray();
        for (AliyunUploadBean aliyunUploadBean : piclist) {
            JSONObject jot = new JSONObject();
            try {
                jot.put("key", aliyunUploadBean.getKey());
                jot.put("ContentType", aliyunUploadBean.getContentType());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jay.put(jot);
        }
        map.put("piclist", jay.toString());
        return buildParams(map);
    }

    /**
     * 组建删除孩子请求参数
     *
     * @param id 孩子id
     */
    public static String deleteChild(Long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return buildParams(map);
    }

    /**
     * 组建添加孩子请求参数
     *
     * @param loginName      登录账号
     * @param unionLoginType 联合登陆方式 : alipay,weixin,qq
     * @param name           姓名
     * @param email          邮箱
     * @param petName        昵称
     * @param sex            性别
     * @param pictureUrl     头像
     */
    public static String unionLogin(String loginName, String unionLoginType,
                                    String name, String email, String petName, String sex,
                                    String pictureUrl) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("loginName", loginName);
        map.put("unionLoginType", unionLoginType);
        map.put("name", name);
        map.put("email", email);
        map.put("petName", petName);
        map.put("sex", sex);
        map.put("pictureUrl", pictureUrl);
        return buildParams(map);
    }

    /**
     * 组建发布成长评论请求参数
     *
     * @param growId   成长记录id
     * @param parentId 回复的回复id
     * @param rootId   回复的评论id
     * @param content  评论内容
     * @param type     评论类型，0萌照，1身高体重
     */
    public static String comtGrow(Long growId, Long parentId, Long rootId,
                                  String content, int type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("growId", growId);
        map.put("parentId", parentId);
        map.put("rootId", rootId);
        map.put("content", content);
        map.put("type", type);
        return buildParams(map);
    }

    /**
     * 组建获取消息列表请求参数
     *
     * @param curPage  当前页码
     * @param pageSize 每页多少条
     */
    public static String getMsgList(Integer curPage, Integer pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        return buildParams(map);
    }

    /**
     * 组建删除萌照请求参数
     *
     * @param id 萌照id
     */
    public static String deleteShowPhoto(Long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return buildParams(map);
    }

    /**
     * 组建检测更新请求参数
     */
    public static String checkVersion(String curVersion, Integer appType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curVersion", curVersion);
        map.put("appType", appType);
        return buildParams(map);
    }

    /**
     * 组建获取微信参数请求参数
     *
     * @param actId   游戏id
     * @param applyId 报名id
     * @return
     */
    public static String getWxParam(Long actId, Long applyId, String total_fee) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("actId", actId);
        map.put("applyId", applyId);
        map.put("total_fee", total_fee);
        return buildParams(map);
    }

    /**
     * 组建获取微信参数请求参数
     *
     * @param actId    游戏id
     * @param applyIds 报名id
     * @return
     */
    public static String getWxParamCourse(Long actId, String applyIds, String total_fee) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("actId", actId);
        map.put("applyIds", applyIds);
        map.put("total_fee", total_fee);
        return buildParams(map);
    }

    /**
     * 组建获取充值支付参数请求参数
     *
     * @param type       充值类型 0:报名 1:会员充值 2:余额充值
     * @param rechargeId 充值记录id
     * @param total_fee  充值金额
     * @return
     */
    public static String getRechargeParam(Integer type, Long rechargeId,
                                          String total_fee) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", type);
        map.put("rechargeId", rechargeId);
        map.put("total_fee", total_fee);
        return buildParams(map);
    }

    /**
     * 组建获取店铺详情请求参数
     *
     * @param id 店铺id
     */
    public static String getShopDetails(Long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return buildParams(map);
    }

    /**
     * 组建开通会员请求参数
     *
     * @param childId      孩子id
     * @param shopId       店铺id
     * @param memberTypeId 会员类型id
     * @param cardId       会员卡id
     * @param type         充值方式(0:开通;1:续费;2:升级)
     * @return
     */
    public static String startMember(Long childId, Long shopId,
                                     Long memberTypeId, Long cardId, String type, String cardCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("childId", childId);
        map.put("shopId", shopId);
        map.put("memberTypeId", cardId);
        map.put("cardId", memberTypeId);
        map.put("type", type);
        map.put("cardCode", cardCode);
        return buildParams(map);
    }

    /**
     * 组建获取收藏的店铺列表数据
     *
     * @param curPage  当前页码
     * @param pageSize 每页条数
     */
    public static String getCollectedShopList(int curPage, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        return buildParams(map);
    }

    /**
     * 组建收藏店铺数据
     *
     * @param merchantId 店铺id
     */
    public static String collectShop(Long merchantId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("merchantId", merchantId);
        return buildParams(map);
    }

    /**
     * 组建获取商品评价请求参数
     *
     * @param pmInfoId 商品id
     * @param curPage  当前页码
     * @param pageSize 每页条数
     */
    public static String getGoodsComts(Long pmInfoId, Integer curPage,
                                       Integer pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pmInfoId", pmInfoId);
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        return buildParams(map);
    }


    /**
     * 组建获取支付信息请求参数
     *
     * @param orderId 订单id
     */
    public static String getPayInfo(Long orderId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", orderId);
        return buildParams(map);
    }

    /**
     * 组建余额支付请求参数
     *
     * @param orderId       订单id
     * @param paymentAmount 支付金额
     * @param verifyCode    验证码
     */
    public static String balancePayment(Long orderId, String paymentAmount,
                                        String verifyCode, long paymentMethodId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", orderId);
        map.put("paymentAmount", paymentAmount);
        map.put("verifyCode", verifyCode);
        map.put("paymentMethodId", paymentMethodId);
        return buildParams(map);
    }

    public static String toPay(Integer type, String out_trade_no) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", type);
        map.put("out_trade_no", out_trade_no);
        return buildParams(map);
    }

    /**
     * 组建添加地址请求参数
     */
    public static String addAddress(String from, String goodReceiverName,
                                    String goodReceiverAddress, String goodReceiverMobile,
                                    String goodReceiverProvinceName, String goodReceiverCityName,
                                    String goodReceiverCountyName, String townName, Integer goodReceiverType,
                                    Long goodReceiverCountryId, Long goodReceiverProvinceId,
                                    Long goodReceiverCityId, Long goodReceiverCountyId,
                                    Long goodReceiverTownId, Integer isDefault) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("from", from);//来源:order(结算订单);my(我的页面）
        map.put("goodReceiverName", goodReceiverName);
        map.put("goodReceiverAddress", goodReceiverAddress);
        map.put("goodReceiverMobile", goodReceiverMobile);
        map.put("goodReceiverProvinceName", goodReceiverProvinceName);
        map.put("goodReceiverCityName", goodReceiverCityName);
        map.put("goodReceiverCountyName", goodReceiverCountyName);
        map.put("townName", townName);
        map.put("goodReceiverType", goodReceiverType);
        map.put("goodReceiverCountryId", goodReceiverCountryId);
        map.put("goodReceiverProvinceId", goodReceiverProvinceId);
        map.put("goodReceiverCityId", goodReceiverCityId);
        map.put("goodReceiverCountyId", goodReceiverCountyId);
        map.put("goodReceiverTownId", goodReceiverTownId);
        map.put("isDefault", isDefault);
        return buildParams(map);
    }

    /**
     * 组建添加地址请求参数 by super
     */
    public static String addAddressSimple(String goodReceiverName,
                                          String goodReceiverAddress, String goodReceiverMobile,
                                          Integer goodReceiverType, Long goodReceiverCountryId,
                                          Long goodReceiverProvinceId, Long goodReceiverCityId,
                                          Long goodReceiverCountyId, Integer isDefault, Long storeId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("goodReceiverName", goodReceiverName);
        map.put("goodReceiverAddress", goodReceiverAddress);
        map.put("goodReceiverMobile", goodReceiverMobile);
        map.put("goodReceiverType", goodReceiverType);
        map.put("goodReceiverCountryId", goodReceiverCountryId);
        map.put("goodReceiverProvinceId", goodReceiverProvinceId);
        map.put("goodReceiverCityId", goodReceiverCityId);
        map.put("goodReceiverCountyId", goodReceiverCountyId);
        map.put("isDefault", isDefault);
        map.put("storeId", storeId);
        return buildParams(map);
    }

    /**
     * 获取省份id
     */
    public static String getPId(Long pid) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pid", pid);
        return buildParams(map);
    }

    /**
     * 获取城市id
     */
    public static String getGoodReceiverCityId(Long goodReceiverCityId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("goodReceiverCityId", goodReceiverCityId);
        return buildParams(map);
    }

    /**
     * 获取区域id
     */
    public static String goodReceiverCountyId(Long goodReceiverCountyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("goodReceiverCountyId", goodReceiverCountyId);
        return buildParams(map);
    }

    /**
     * 获取自提店铺列表通过区域id
     */
    public static String getStoresByCountyId(Long goodReceiverCountyId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("goodReceiverCountyId", goodReceiverCountyId);
        return buildParams(map);
    }

    /**
     * 组建更新（地址信息）请求参数
     */
    public static String updateDetailAddress(String goodReceiverName,
                                             String goodReceiverAddress, String goodReceiverMobile,
                                             String goodReceiverProvinceName, String goodReceiverCityName,
                                             String goodReceiverCountyName, String goodReceiverTownName, Integer goodReceiverType,
                                             Long goodReceiverCountryId, Long goodReceiverProvinceId,
                                             Long goodReceiverCityId, Long goodReceiverCountyId,
                                             Long goodReceiverTownId, Integer isDefault, Long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(goodReceiverName))
            map.put("goodReceiverName", goodReceiverName);
        if (!TextUtils.isEmpty(goodReceiverAddress))
            map.put("goodReceiverAddress", goodReceiverAddress);
        if (!TextUtils.isEmpty(goodReceiverMobile))
            map.put("goodReceiverMobile", goodReceiverMobile);
        if (!TextUtils.isEmpty(goodReceiverProvinceName))
            map.put("goodReceiverProvinceName", goodReceiverProvinceName);
        if (!TextUtils.isEmpty(goodReceiverCityName))
            map.put("goodReceiverCityName", goodReceiverCityName);
        if (!TextUtils.isEmpty(goodReceiverCountyName))
            map.put("goodReceiverCountyName", goodReceiverCountyName);
        if (!TextUtils.isEmpty(goodReceiverTownName))
            map.put("goodReceiverTownName", goodReceiverTownName);
        map.put("goodReceiverType", goodReceiverType);
        map.put("goodReceiverCountryId", goodReceiverCountryId);
        map.put("goodReceiverProvinceId", goodReceiverProvinceId);
        map.put("goodReceiverCityId", goodReceiverCityId);
        map.put("goodReceiverCountyId", goodReceiverCountyId);
        map.put("goodReceiverTownId", goodReceiverTownId);
        map.put("isDefault", isDefault);
        map.put("id", id);
        return buildParams(map);
    }

    /**
     * 通过id获取收货地址详情
     */
    public static String getAddressById(Long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return buildParams(map);
    }

    /**
     * 获取次级类目
     *
     * @param parentId 父类目id
     */
    public static String getChildNodes(Long parentId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("parentId", parentId);
        return buildParams(map);
    }

    /**
     * 获取特卖页面
     *
     * @param categoryId 顶部类别id
     */
    public static String goodsClassify(String categoryId, int curPage,
                                       int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("categoryId", categoryId);
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        return buildParams(map);
    }

    /**
     * 组建获取商品列表请求参数 通过种类id
     *
     * @param categoryId 种类id
     * @param curPage    当前页码
     * @param pageSize   每页多少条
     */
    public static String getGoodsList(Long categoryId, Integer curPage,
                                      Integer pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("categoryId", categoryId);
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        return buildParams(map);
    }


    /**
     * 组建获取订单列表数据
     */
    public static String getOrderList(int orderListType, int curPage,
                                      int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderListType", orderListType);
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        return buildParams(map);
    }

    /**
     * 组建获取订单详情数据
     */
    public static String getOrderDetail(long orderId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderId", orderId);
        return buildParams(map);
    }

    /**
     * 售后申请参数 通过关键字
     */
    public static String applyReturn(Long soId, Integer grfType, String remark,
                                     String grfPicUrls) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("soId", soId);
        map.put("grfType", grfType);
        map.put("remark", remark);
        map.put("grfPicUrls", grfPicUrls);
        return buildParams(map);
    }

    /**
     * 售后申请参数 通过关键字
     */
    public static String applyReturnV2(long soId, int grfType, String remark,
                                       String grfPicUrls, long pmInfoId, int pmNum) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("soId", soId);
        map.put("grfType", grfType);
        map.put("remark", remark);
        map.put("grfPicUrls", grfPicUrls);
        map.put("pmInfoId", pmInfoId);
        map.put("pmNum", pmNum);
        return buildParams(map);
    }

    /**
     * 通过订单id查询退货接口
     */
    public static String queryById(Long soId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("soId", soId);
        return buildParams(map);
    }

    /**
     * 通过订单id查询退货接口
     */
    public static String queryByIdV2(long soId, long pmInfoId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("soId", soId);
        map.put("pmInfoId", pmInfoId);
        return buildParams(map);
    }

    /**
     * 退货更新接口
     */
    public static String updateGrf(Long soId, String deliveryCompanyName,
                                   String waybillCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("soId", soId);
        map.put("deliveryCompanyName", deliveryCompanyName);
        map.put("waybillCode", waybillCode);
        return buildParams(map);
    }

    /**
     * 退货更新接口
     */
    public static String updateGrfV2(long soId, long pmInfoId, String deliveryCompanyName,
                                     String waybillCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("soId", soId);
        map.put("pmInfoId", pmInfoId);
        map.put("deliveryCompanyName", deliveryCompanyName);
        map.put("waybillCode", waybillCode);
        return buildParams(map);
    }

    /**
     * 组建获取特卖商品列表请求参数 通过特卖类别
     *
     * @param promotionType 关键字
     */
    public static String getTeMaiGoods(String promotionType, int curPage,
                                       int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (promotionType != null)
            map.put("promotionType", promotionType);
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        return buildParams(map);
    }

    /**
     * 组建获取会员卡列表请求参数
     *
     * @param cardCode 卡类型编号
     * @param childId  孩子id
     */
    public static String getCardLenListByType(String cardCode, Long childId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cardCode", cardCode);
        map.put("childId", childId);
        return buildParams(map);
    }


    /**
     * 组建获取店铺游戏列表数据通过条件
     *
     * @param merchantId 店铺id
     * @param actType    游戏参与类型
     * @param chargeMode 游戏收费类型
     * @param curPage    当前页
     * @param pageSize   每页条数
     */
    public static String getActListByFilter(Long merchantId, String actType,
                                            String chargeMode, int curPage, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("merchantId", merchantId);
        map.put("actType", actType);
        map.put("chargeMode", chargeMode);
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        return buildParams(map);
    }

    /**
     * 组建获取退货订单数据
     *
     * @param curPage 当前页码
     */
    public static String getGrforderList(int curPage, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        return buildParams(map);
    }

    /**
     * 组件获取密码数据
     *
     * @param oldPassword
     * @param newPassword1
     * @param newPassword2
     * @return
     */
    public static String updatePwd(String oldPassword, String newPassword1,
                                   String newPassword2) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("oldPassword", oldPassword);
        map.put("newPassword1", newPassword1);
        map.put("newPassword2", newPassword2);
        return buildParams(map);
    }

    /**
     * 意见反馈
     */
    public static String suggestionFeedback(int suggerstType,
                                            String suggerstText, String imgUrls, long userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("suggerstType", suggerstType);
        map.put("suggerstText", suggerstText);
        map.put("imgUrls", imgUrls);
        map.put("userId", userId);
        return buildParams(map);
    }

    /**
     * 组建获取优惠券列表数据
     *
     * @param status
     * @param curPage
     * @param pageSize
     * @return
     */
    public static String getCouponList(int status, int curPage, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", status);
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        return buildParams(map);
    }

    /**
     * 通过优惠券id查询优惠券详情
     */
    public static String getCouponDetailById(long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return buildParams(map);
    }

    /**
     * 获取结算页面优惠券
     */
    public static String getAccountCoupon(long userId, double amount) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("amount", amount);
        return buildParams(map);
    }

    /**
     * 获取购物车
     */
    public static String getTradeCart(long userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        return buildParams(map);
    }

    /**
     * 获取添加商品到购物车的数据
     */
    public static String addProductToTradeCart(long pmInfoId, int num) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pmInfoId", pmInfoId);
        map.put("num", num);
        return buildParams(map);
    }

    /**
     * 获取删除单个购物车的商品数据
     */
    public static String delOneProduct(long pmInfoId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pmInfoId", pmInfoId);
        return buildParams(map);
    }


    /**
     * 获取更新购物车商品的数据
     */
    public static String updateProductNum(long pmInfoId, int num) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pmInfoId", pmInfoId);
        map.put("num", num);
        return buildParams(map);
    }

    /**
     * 获取商铺下的相册列表数据
     */
    public static String getMerchantPictureList(String pMerchantId, String pType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("merchantId", pMerchantId);
        map.put("type", pType);
        return buildParams(map);
    }

    /**
     * 获取勾选购物车商品的数据
     */
    public static String checkCartItems(int type, int isChecked, String itemId,
                                        long merchantId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", type);
        map.put("itemId", itemId);
        map.put("isChecked", isChecked);
        map.put("merchantId", merchantId);
        return buildParams(map);
    }

    public static String getOrderSubmitV2(String recommenderCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uuid", UUID.randomUUID().toString());
        map.put("recommenderCode", recommenderCode);
        return buildParams(map);
    }

    public static String getOrderIdentitySubmitV2(String recommenderCode, String identityCardNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uuid", UUID.randomUUID().toString());
        map.put("recommenderCode", recommenderCode);
        map.put("identityCardNo", identityCardNo);
        return buildParams(map);
    }

    /**
     * 通过地址id获取数据
     */
    public static String saveAddr(Long goodReceiverById) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("goodReceiverById", goodReceiverById);
        return buildParams(map);
    }

    /**
     * 获取结算页面优惠券
     */
    public static String getSelectCoupon(long couponUseId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("couponUseId", couponUseId);
        return buildParams(map);
    }

    /**
     * 分时段获取秒杀列表
     */
    public static String getSecKill(int curPage, String promotionType, String startTime) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curPage", curPage);
        map.put("promotionType", promotionType);
        map.put("startTime", startTime);
        return buildParams(map);
    }

    /**
     * 拼团商品详情结算请求参数
     */
    public static String getFightGroupGoodsConfirmV2(String from, Long pmInfoId,
                                                     Integer buyNum, Long promotionId, String couponUseId, Long fightGroupOpenId, String isFightGroup) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buyFrom", from);// "cart"
        map.put("pmInfoId", pmInfoId);
        map.put("buyNum", buyNum);
        if (couponUseId != null)
            map.put("couponUseId", couponUseId);
        if (promotionId != null && promotionId != -1l)
            map.put("promotionId", promotionId);
        map.put("isFightGroup", isFightGroup);
        map.put("fightGroupOpenId", fightGroupOpenId);
        return buildParams(map);
    }

    /**
     * 参团商品详情结算请求参数
     */
    public static String getJoinGroupConfirmV2(String from, Long pmInfoId,
                                               Integer buyNum, Long promotionId, String couponUseId, String isFightGroup) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buyFrom", from);// "cart"
        map.put("pmInfoId", pmInfoId);
        map.put("buyNum", buyNum);
        if (couponUseId != null)
            map.put("couponUseId", couponUseId);
        if (promotionId != null && promotionId != -1l)
            map.put("promotionId", promotionId);
        map.put("isFightGroup", isFightGroup);
        return buildParams(map);
    }

    public static String insertFamilyTripInfo(long applyId, long activityId, long insuranceId,
                                              String insuranceName, String totalInsurance, ArrayList<Long> clist) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applyId", applyId);
        map.put("activityId", activityId);
        map.put("insuranceId", insuranceId);
        map.put("insuranceName", insuranceName);
        map.put("totalInsurance", totalInsurance);
        JSONArray jay = new JSONArray();
        for (Long cId : clist) {
            JSONObject jot = new JSONObject();
            try {
                jot.put("contactsId", cId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jay.put(jot);
        }
        map.put("list", jay.toString());
        return buildParams(map);
    }

    /**
     * 次卡、小时卡明细
     */
    public static String getCardDetail(int cardType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cardType", cardType);
        return buildParams(map);
    }

    /**
     * 组建获取订单列表数据
     */
    public static String getDetail(int currentPage, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curPage", currentPage);
        map.put("pageSize", pageSize);
        return buildParams(map);
    }

    /**
     * 获取结算页面优惠券
     */
    public static String getUserInfo(long userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        return buildParams(map);
    }

    /**
     * 根据品牌获取商品
     */
    public static String getProductByBrandId(int curPage, long productBrandId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curPage", curPage);
        map.put("productBrandId", productBrandId);
        return buildParams(map);
    }

    /**
     * 本期分红数据
     */
    public static String getMonthDivident(String contractType, int shopId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contractType", contractType);
        map.put("shopId", shopId);
        return buildParams(map);
    }

    /**
     * 历史分红数据
     */
    public static String getHistoryDivident(String curPage, String pageSize, String time, String shopId, String contractType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        map.put("time", time);
        map.put("shopId", shopId);
        map.put("contractType", contractType);
        return buildParams(map);
    }

    /**
     * 历史指定月份分红数据
     */
    public static String getHistoryMonthDivident(String shopId, String contractType, String date) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("shopId", shopId);
        map.put("contractType", contractType);
        map.put("date", date);
        return buildParams(map);
    }

    /**
     * 提现详情
     */
    public static String getWithdrawDetail(String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return buildParams(map);
    }

    /**
     * 提现详情
     */
    public static String getWithdrawDetailV2(String id, int type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("type", type);
        return buildParams(map);
    }

    /**
     * 组建获取物流详情数据
     */
    public static String getOrderTrack(long soId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("soId", soId);
        return buildParams(map);
    }

    /**
     * 计算退款金额数据
     */
    public static String getGrfAmount(int grfType, int pmNum, long pmInfoId, long soId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("grfType", grfType);//退货类型
        map.put("pmNum", pmNum);//商退货品数量
        map.put("pmInfoId", pmInfoId);//退货商品id
        map.put("soId", soId);//订单id
        return buildParams(map);
    }

    /**
     * 梦想详情发布评论请求参数
     *
     * @param parentId      回复的回复id
     * @param requirementId 回复的评论id
     * @param content       评论内容
     */
    public static String comtDreamDetail(Long requirementId, String content,
                                         Long parentId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("requirementId", requirementId);
        map.put("content", content);
        map.put("parentId", parentId);
        return buildParams(map);
    }

    /**
     * 组建获取梦想评论请求参数
     *
     * @param requirementId 梦想id
     */
    public static String getDreamComts(Long requirementId, int curPage, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("requirementId", requirementId);
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        return buildParams(map);
    }

    /**
     * 加入梦想数据
     *
     * @param requirementId
     * @param expectDate
     * @param shopId
     * @return
     */
    public static String getaddReqPred(Long requirementId, String expectDate, Long shopId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("requirementId", requirementId);
        map.put("expectDate", expectDate);
        map.put("shopId", shopId);
        return buildParams(map);
    }

    public static String getEaChildList(int curPage, int pageSize, Long childId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        map.put("childId", childId);
        return buildParams(map);
    }

    public static String getSinglePicComts(Long albumId, Long itemsId, int curPage, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("albumId", albumId);
        map.put("itemsId", itemsId);
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        return buildParams(map);
    }

    public static String getAlbumComts(Long albumId, int curPage, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("albumId", albumId);
        map.put("curPage", curPage);
        map.put("pageSize", pageSize);
        return buildParams(map);
    }
}
