package com.park61.teacherhelper.common.tool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

import com.park61.teacherhelper.common.set.GlobalParam;

/**
 * 客户端安装相关方法
 * 
 * @author super
 */
public class InstallUtils {

	/**
	 * 检测SD卡上最新APK是否已存在
	 * 
	 * @return 返回APK是否存在
	 */
	public static boolean isApkExist(Context context, int code) {
		File file = new File(getApkFilePath(context, code));
		if (file.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * 删除更新文件
	 * 
	 * @code 默认填入0
	 */
	public static void deleteUpdateFile(Context context, int code) {
		File file = new File(InstallUtils.getApkFilePath(context, code));
		if (file.exists()) {
			file.delete();
		}

	}

	/**
	 * get file stored path
	 */
	public static String getApkFilePath(Context context, int code) {
		if (code == 0) {
			code = GlobalParam.versionCode;
		}
		return context.getFilesDir().getAbsolutePath() + "/smartPos_" + code
				+ ".apk";
	}

	/**
	 * 获取个人中心人物图像文件名(临时文件，方形图片)
	 */
	public static String getImageFileNameOriginal(Context context) {
		DateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String time = formatter.format(new Date());
		return "people_big_image_" + time + ".png";
	}

	/**
	 * 获取图片本地硬缓存地址全路径（data/data目录下）
	 * 
	 * @param url
	 *            图片url
	 */
	public static String getAdImageName(Context context, String url) {
		int index = url.lastIndexOf("/");
		return context.getFilesDir().getAbsolutePath() + "/"
				+ url.substring(index + 1, url.length());

	}

	/**
	 * 检测SDCARD是否存在
	 * 
	 * @return
	 */
	public static boolean isSdcardMounted() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	/** 执行Linux命令，并返回执行结果。 */
	public static String exec(String[] args) {
		String result = "";
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		Process process = null;
		InputStream errIs = null;
		InputStream inIs = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = -1;
			process = processBuilder.start();
			errIs = process.getErrorStream();
			while ((read = errIs.read()) != -1) {
				baos.write(read);
			}
			baos.write("/n".getBytes());
			inIs = process.getInputStream();
			while ((read = inIs.read()) != -1) {
				baos.write(read);
			}
			byte[] data = baos.toByteArray();
			result = new String(data);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (errIs != null) {
					errIs.close();
				}
				if (inIs != null) {
					inIs.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (process != null) {
				process.destroy();
			}
		}
		return result;
	}

	/**
	 * 检测APK是否已安装
	 * 
	 * @param pkgName
	 *            包名
	 * @return boolean
	 */
	public static boolean isApkInstalled(Context context, String pkgName) {
		try {
			context.getPackageManager().getPackageInfo(pkgName, 0);
			return true;
		} catch (NameNotFoundException e) {
			// e.printStackTrace();
		}
		return false;
	}

	public static boolean copyApkFromAssets(Context context, String fileName,
			String path) {
		boolean copyIsFinish = false;
		try {
			InputStream is = context.getAssets().open("apk/" + fileName);
			File file = new File(path);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();
			copyIsFinish = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return copyIsFinish;
	}

	/*public static boolean isWeixinAvilible(Context context) {
		final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				if (pn.equals("com.tencent.mm")) {
					return true;
				}
			}
		}
		return false;
	}*/

}
