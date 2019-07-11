package com.park61.teacherhelper.module.login;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.MainTabActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.common.tool.CommonMethod;
import com.android.volley.Request;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.wxapi.HttpUtil;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by shubei on 2017/9/25.
 */

public class LoginActivity extends BaseActivity {

    private static final int REQ_UNION_BIND_PHONE = 1;
    public static final String FILE_NAME = "USER_INFO";
    public static boolean isWXLogin = false;// 是否微信登陆
    public static String WX_CODE = "";// 微信登陆授权返回code
    private IWXAPI wxApi;
    private String mopenId, nickname, sex, headimgurl, unionQQorWx;

    private Button btn_login_by_phone, right_img, left_img;
    private View area_weixin_login;
    private TextView tv_pwd_login;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_login_v2);
    }

    @Override
    public void initView() {
        btn_login_by_phone = (Button) findViewById(R.id.btn_login_by_phone);
        area_weixin_login = findViewById(R.id.area_weixin_login);
        tv_pwd_login = (TextView) findViewById(R.id.tv_pwd_login);
        right_img = (Button) findViewById(R.id.right_img);
        left_img = (Button) findViewById(R.id.left_img);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        btn_login_by_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, LoginByPhoneActivity.class));
            }
        });
        area_weixin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalParam.LOGIN_TYPE = 1;
                isWXLogin = true;
                showDialog();
                unionWXLogin();
            }
        });
        tv_pwd_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, LoginByPwdActivity.class));
            }
        });
        right_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, RegisterV2Activity.class));
            }
        });
        left_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 微信联合登陆
     */
    public void unionWXLogin() {
        wxApi = WXAPIFactory.createWXAPI(this, GlobalParam.WX_APP_ID, true);
        wxApi.registerApp(GlobalParam.WX_APP_ID);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo";
        wxApi.sendReq(req);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isWXLogin && !WX_CODE.equals("")) {// 如果是登录，并且返回不为空
            loadWXUserInfo();
        } else {
            dismissDialog();
        }
    }

    /**
     * @methods: 获得微信用户信息
     * @author: lianzhi
     * @Date: 2015-3-5
     */
    private void loadWXUserInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                        + GlobalParam.WX_APP_ID
                        + "&secret="
                        + GlobalParam.WX_APP_SECRET
                        + "&code="
                        + WX_CODE
                        + "&grant_type=authorization_code";
                Log.e("loadWXUserInfo", "accessTokenUrl======" + accessTokenUrl);
                String tokenResult = HttpUtil.httpsGet(accessTokenUrl);
                Log.e("loadWXUserInfo", "tokenResult======" + tokenResult);
                if (null != tokenResult) {
                    JSONObject tokenObj = null;
                    JSONObject wxUserInfoJot = null;
                    try {
                        tokenObj = new JSONObject(tokenResult);
                        String accessToken = tokenObj.getString("access_token");
                        String openId = tokenObj.getString("openid");
                        String userUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId;
                        String wxUserInfo = HttpUtil.httpsGet(userUrl);
                        Log.e("loadWXUserInfo", "wxUserInfo======" + wxUserInfo);
                        wxUserInfoJot = new JSONObject(wxUserInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (wxUserInfoJot != null) {
                        mopenId = wxUserInfoJot.optString("unionid");
                        nickname = wxUserInfoJot.optString("nickname");
                        sex = wxUserInfoJot.optInt("sex") + "";
                        headimgurl = wxUserInfoJot.optString("headimgurl");
                        unionQQorWx = "weixin";
                        asyncUnionLoginCheckMobile(mopenId, unionQQorWx, "", null, nickname, sex, headimgurl);
                    }
                }
                // 请求后重置
                isWXLogin = false;
                WX_CODE = "";
            }
        }).start();
    }

    /**
     * 联合登录
     */
    private void asyncUnionLoginCheckMobile(String loginName, String unionLoginType, String name, String email, String petName, String sex, String pictureUrl) {
        String wholeUrl = AppUrl.host + AppUrl.UNION_LOGIN_CHECK_MOBILE;
        String requestBodyData = ParamBuild.unionLogin(loginName, unionLoginType, name, email, petName, sex, pictureUrl);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, checkMobileloginLsner);
    }

    BaseRequestListener checkMobileloginLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            String needMobile = jsonResult.optString("needMobile");
            if ("0".equals(needMobile)) {
                asyncUnionLogin(mopenId, unionQQorWx, "", null, nickname, sex, headimgurl);
            } else if ("1".equals(needMobile)) {
                Intent it = new Intent(mContext, UnionWxBindActivity.class);
                it.putExtra("mopenId", mopenId);
                it.putExtra("unionQQorWx", unionQQorWx);
                it.putExtra("nickname", nickname);
                it.putExtra("sex", sex);
                it.putExtra("headimgurl", headimgurl);
                startActivityForResult(it, REQ_UNION_BIND_PHONE);
            }
        }
    };

    /**
     * 联合登录
     */
    private void asyncUnionLogin(String loginName, String unionLoginType, String name, String email, String petName, String sex, String pictureUrl) {
        String wholeUrl = AppUrl.host + AppUrl.UNION_LOGIN;
        String requestBodyData = ParamBuild.unionLogin(loginName, unionLoginType, name, email, petName, sex, pictureUrl);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, loginLsner);
    }

    BaseRequestListener loginLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            GlobalParam.userToken = jsonResult.optString("userToken");
            showShortToast("登录成功！");
            CommonMethod.saveCurrentUserName(mContext);
            startActivity(new Intent(mContext, MainTabActivity.class));
            finish();
        }
    };

}
