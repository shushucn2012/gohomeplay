package com.park61.teacherhelper.module.login.bean;

import android.content.Context;

import com.park61.teacherhelper.common.tool.CommonMethod;

import org.json.JSONObject;

import java.io.File;

/**
 * @Description:账号（用户管理）
 * @author:Cai
 * @time:2013-11-26 下午4:01:01
 */
public class UserManager {
	public static final String fileName = "userBean";
	private UserBean userInfo = null;
	private static UserManager userManager;

	public UserManager() {
	}

	public static UserManager getInstance() {
		if (userManager == null) {
			userManager = new UserManager();
		}
		return userManager;
	}

	/**
	 * 设置（保存）userInfo（账号相关信息）
	 */
	public void setAccountInfo(UserBean userInfo, Context mContext) {
		this.userInfo = userInfo;
		CommonMethod.saveObject(mContext, userInfo, fileName);
	}

	/**
	 * 重设(保存)账号信息
	 */
	public void reSetAccountInfo(Context mContext) {
		CommonMethod.saveObject(mContext, userInfo, fileName);
	}

	/**
	 * 获取accountInfo（账号相关信息）
	 */
	public UserBean getAccountInfo(Context mContext) {
		if (userInfo == null) {
			userInfo = (UserBean) CommonMethod.readObject(mContext, fileName);
		}
		return userInfo;
	}

	/**
	 * 删除accountInfo（账号相关信息），切换账号、退出程序需要用到
	 */
	public void deleteAccountInfo(Context mContext) {
		userInfo = null;
		File data = mContext.getFileStreamPath(fileName);
		data.delete();
	}

}
