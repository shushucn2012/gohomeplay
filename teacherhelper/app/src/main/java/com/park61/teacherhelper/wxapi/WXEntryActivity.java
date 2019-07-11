package com.park61.teacherhelper.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.park61.teacherhelper.common.set.GlobalParam;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.module.login.LoginActivity;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		api = WXAPIFactory.createWXAPI(this, GlobalParam.WX_APP_ID, false);
		api.handleIntent(getIntent(), this);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onReq(BaseReq req) {
		//finish();
	}

	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			if (LoginActivity.isWXLogin) {
				SendAuth.Resp sendResp = (SendAuth.Resp) resp;
				Log.e("", "sendResp.code======" + sendResp.code);
				Log.e("", "resp.errCode======" + resp.errCode);
				LoginActivity.WX_CODE = sendResp.code;
				finish();
				return;
			} else {
				
				Toast.makeText(this, "微信分享成功!", Toast.LENGTH_SHORT).show();
			}
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			// Toast.makeText(this, "取消!", Toast.LENGTH_LONG).show();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			// Toast.makeText(this, "被拒绝", Toast.LENGTH_LONG).show();
			break;
		default:
			// Toast.makeText(this, "失败!", Toast.LENGTH_LONG).show();
			break;
		}
		LoginActivity.isWXLogin = false;
		finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
		finish();
	}
}
