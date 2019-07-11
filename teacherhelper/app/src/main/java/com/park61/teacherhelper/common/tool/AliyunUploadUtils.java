package com.park61.teacherhelper.common.tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.park61.teacherhelper.common.okhttp.OkHttpUtils;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.common.set.Log;
import com.park61.teacherhelper.common.set.ParamBuild;
import com.park61.teacherhelper.module.login.bean.AliyunUploadBean;
import com.android.volley.Request.Method;
import com.park61.teacherhelper.net.request.interfa.BaseRequestListener;
import com.park61.teacherhelper.net.request.interfa.JsonRequestListener;
import com.park61.teacherhelper.widget.dialog.CommonProgressDialog;

public class AliyunUploadUtils {

	private static final int UPLOAD_SUCCESS = 0;

	private static final int UPLOAD_FAILED = 1;

	//private Context mContext;
	private Activity mActivity;

	private CommonProgressDialog commonProgressDialog;
	private List<AliyunUploadBean> piclist;
	private List<String> pathlist;
	private List<String> urllist;
	private List<String> countllist;// 计数list
	private OnUploadFinish mOnUploadFinish;
	private OnUploadListFinish mOnUploadListFinish;

	public AliyunUploadUtils(Activity activity) {
		mActivity = activity;
		commonProgressDialog = new CommonProgressDialog(mActivity);
		commonProgressDialog.setCancelable(false);
		piclist = new ArrayList<AliyunUploadBean>();
		pathlist = new ArrayList<String>();
		urllist = new ArrayList<String>();
		countllist = new ArrayList<String>();
	}

	/**
	 * 获取阿里云oss加密
	 */
	public void uploadPic(String filePath, OnUploadFinish onUploadFinish) {
		mOnUploadFinish = onUploadFinish;
		String wholeUrl = AppUrl.host + AppUrl.GET_ALIYUN_SEC_URL;
		String expandedName = ""; // 文件扩展名
		int index = filePath.lastIndexOf(".");
		expandedName = filePath.substring(index, filePath.length());
		String objectKey = "client/" + UUID.randomUUID().toString()
				+ expandedName;
		AliyunUploadBean aub = new AliyunUploadBean();
		aub.setKey(objectKey);
		aub.setContentType("image/jpeg");
		piclist.clear();
		piclist.add(aub);
		pathlist.clear();
		pathlist.add(filePath);
		String requestBodyData = ParamBuild.getAliyunUrl(piclist);
		OkHttpUtils.getInstance().setMainActivity(mActivity).startRequest(wholeUrl, Method.POST, requestBodyData, 0, aliyunlsner);
	}

	BaseRequestListener aliyunlsner = new JsonRequestListener() {

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
			final String key = piclist.get(0).getKey();
			logout("jsonResult======" + jsonResult);
			logout("key======" + key);
			final String urlm = jsonResult.optString(key);
			new Thread() {
				public void run() {
					submitPut(urlm, pathlist.get(0));
				};
			}.start();
		}
	};

	/**
	 * 手机APP上传文件示例。注意必须使用put请求
	 * 
	 * @param url
	 *            动态签名
	 * @param filepath
	 *            文件路径
	 */
	private void submitPut(String url, String filepath) {
		HttpClient httpclient = new DefaultHttpClient();
		try {
			logout("url======" + url);
			logout("filepath======" + filepath);
			HttpPut httpput = new HttpPut(url);
			File file = new File(filepath);
			String ContentType = FileTypeAnalysis
					.getContentType(FileTypeAnalysis.getFileByFile(file));
			httpput.addHeader("Content-Type", ContentType);
			HttpEntity reqEntity = new FileEntity(file, ContentType);
			httpput.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httpput);
			int statusCode = response.getStatusLine().getStatusCode();
			logout("statusCode======" + statusCode);
			if (statusCode == HttpStatus.SC_OK) {
				logout("url:" + url + "------upload:0k------");
				mOnUploadFinish.onSuccess(url.split("\\?")[0]);
			} else {
				mOnUploadFinish.onError();
			}
		} catch (ParseException e) {
			e.printStackTrace();
			mOnUploadFinish.onError();
		} catch (IOException e) {
			e.printStackTrace();
			mOnUploadFinish.onError();
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception ignore) {
				mOnUploadFinish.onError();
			}
		}
	}

	/**
	 * 获取阿里云oss加密
	 */
	public void uploadPicList(List<String> filePathList,
			OnUploadListFinish onUploadListFinish) {
		mOnUploadListFinish = onUploadListFinish;
		String wholeUrl = AppUrl.host + AppUrl.GET_ALIYUN_SEC_URL;
		piclist.clear();
		pathlist.clear();
		urllist.clear();
		countllist.clear();
		for (String filePath : filePathList) {
			String expandedName = ""; // 文件扩展名
			int index = filePath.lastIndexOf(".");
			expandedName = filePath.substring(index, filePath.length());
			String objectKey = "client/" + UUID.randomUUID().toString()
					+ expandedName;
			AliyunUploadBean aub = new AliyunUploadBean();
			aub.setKey(objectKey);
			aub.setContentType("image/jpeg");
			piclist.add(aub);
			pathlist.add(filePath);
		}
		String requestBodyData = ParamBuild.getAliyunUrl(piclist);
		OkHttpUtils.getInstance().setMainActivity(mActivity).startRequest(wholeUrl, Method.POST, requestBodyData, 0, aliyunListlsner);
	}

	BaseRequestListener aliyunListlsner = new JsonRequestListener() {

		@Override
		public void onStart(int requestId) {
			showDialog("正在获取图片信息...");
		}

		@Override
		public void onError(int requestId, String errorCode, String errorMsg) {
			dismissDialog();
			showShortToast(errorMsg);
		}

		@Override
		public void onSuccess(int requestId, String url, JSONObject jsonResult) {
			dismissDialog();
			logout("jsonResult======" + jsonResult);
			for (int i = 0; i < piclist.size(); i++) {
				AliyunUploadBean uploadBean = piclist.get(i);
				final String key = uploadBean.getKey();
				logout("key======" + key);
				showDialog("正在上传图片...");
				final String urlf = jsonResult.optString(key);
				urllist.add(urlf.split("\\?")[0]);
				final String pathf = pathlist.get(i);
				new Thread() {
					public void run() {
						submitPutList(urlf, pathf);
					};
				}.start();
			}
		}
	};

	/**
	 * 手机APP上传文件示例。注意必须使用put请求 多条
	 * 
	 * @param url
	 *            动态签名
	 * @param filepath
	 *            文件路径
	 */
	private void submitPutList(String url, String filepath) {
		HttpClient httpclient = new DefaultHttpClient();
		try {
			logout("url======" + url);
			logout("filepath======" + filepath);
			HttpPut httpput = new HttpPut(url);
			File file = new File(filepath);
			String ContentType = FileTypeAnalysis
					.getContentType(FileTypeAnalysis.getFileByFile(file));
			httpput.addHeader("Content-Type", ContentType);
			HttpEntity reqEntity = new FileEntity(file, ContentType);
			httpput.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httpput);
			int statusCode = response.getStatusLine().getStatusCode();
			logout("statusCode======" + statusCode);
			if (statusCode == HttpStatus.SC_OK) {
				Message msg = mHandler.obtainMessage();
				msg.what = UPLOAD_SUCCESS;
				msg.obj = url.split("\\?")[0];
				mHandler.sendMessage(msg);
			} else {
				Message msg = mHandler.obtainMessage();
				msg.what = UPLOAD_FAILED;
				msg.obj = url.split("\\?")[0];
				mHandler.sendMessage(msg);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception ignore) {
			}
		}
	}

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPLOAD_SUCCESS:
				String url = (String) msg.obj;
				countllist.add(url);
				logout("countllist.size()===" + countllist.size()
						+ "and piclist.size()===" + piclist.size());
				if (countllist.size() == piclist.size()) {
					dismissDialog();
					mOnUploadListFinish.onSuccess(urllist);
					deleteDir();
				}
				break;
			case UPLOAD_FAILED:
				dismissDialog();
				String fail_url = (String) msg.obj;
				String path = null;
				for (int i = 0; i < piclist.size(); i++) {
					AliyunUploadBean uploadBean = piclist.get(i);
					if (uploadBean.getKey().contains(fail_url)) {
						path = pathlist.get(i);
						break;
					}
				}
				mOnUploadListFinish.onError(path);
				break;
			}
		};
	};

	/**
	 * 删除缓存文件
	 */
	public static void deleteDir() {
		File dir = new File(Environment.getExternalStorageDirectory().getPath()
				+ "/GHPCacheFolder/Format");
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 删除所有文件
			else if (file.isDirectory())
				deleteDir(); // 递规的方式删除文件夹
		}
		dir.delete();// 删除目录本身
	}

	/**
	 * 上传完成回调
	 */
	public interface OnUploadFinish {
		public void onError();

		public void onSuccess(String picUrl);
	}

	/**
	 * 上传列表完成回调
	 */
	public interface OnUploadListFinish {
		public void onError(String path);

		public void onSuccess(List<String> urllist);
	}

	/**
	 * toast短提示
	 */
	public void showShortToast(String text) {
		Toast.makeText(mActivity, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 打印日志
	 */
	public void logout(String msg) {
		Log.out(msg);
	}

	public void showDialog() {
		try {
			if (commonProgressDialog.isShow()) {
				return;
			} else {
				commonProgressDialog.showDialog(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showDialog(String msg) {
		try {
			if (commonProgressDialog.isShow()) {
				return;
			} else {
				commonProgressDialog.showDialog(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dismissDialog() {
		try {
			if (commonProgressDialog.isShow()) {
				commonProgressDialog.dialogDismiss();
			} else {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
