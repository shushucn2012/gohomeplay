package com.park61.teacherhelper.common.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @Project: qjw
 * @Title: FileTypeAnalysis.java
 * @Package com.qujiawan.mobile.business.util
 * @Description: TODO
 * 
 * @author chencb chenchuanbo_fbb@duoooo.com
 * @date 2016-1-11 下午14:01:57
 * @Copyright: 2015 61区 All rights reserved.
 * @version V1.0
 */
public class FileTypeAnalysis {
	public final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();
	public final static Map<String, String> Content_Type_MAP = new HashMap<String, String>();

	private FileTypeAnalysis() {
	}

	static {
		setFileType(); // 初始化文件类型信息
		setContentType();
	}

	/**
	 * Created on 2016-1-11
	 * <p>
	 * Discription:[getAllFileType,常见文件头信息]
	 * </p>
	 * 
	 * @author:chencb
	 */
	private static void setFileType() {
		FILE_TYPE_MAP.put("jpg", "FFD8FF"); // JPEG (jpg)
		FILE_TYPE_MAP.put("png", "89504E47"); // PNG (png)
		FILE_TYPE_MAP.put("gif", "47494638"); // GIF (gif)
		FILE_TYPE_MAP.put("tif", "49492A00"); // TIFF (tif)
		FILE_TYPE_MAP.put("bmp", "424D"); // Windows Bitmap (bmp)
		FILE_TYPE_MAP.put("dwg", "41433130"); // CAD (dwg)
		FILE_TYPE_MAP.put("html", "68746D6C3E"); // HTML (html)
		FILE_TYPE_MAP.put("rtf", "7B5C727466"); // Rich Text Format (rtf)
		FILE_TYPE_MAP.put("xml", "3C3F786D6C");
		FILE_TYPE_MAP.put("zip", "504B0304");
		FILE_TYPE_MAP.put("rar", "52617221");
		FILE_TYPE_MAP.put("psd", "38425053"); // Photoshop (psd)
		FILE_TYPE_MAP.put("eml", "44656C69766572792D646174653A"); // Email
																	// [thorough
																	// only]
																	// (eml)
		FILE_TYPE_MAP.put("dbx", "CFAD12FEC5FD746F"); // Outlook Express (dbx)
		FILE_TYPE_MAP.put("pst", "2142444E"); // Outlook (pst)
		FILE_TYPE_MAP.put("xls", "D0CF11E0"); // MS Word
		FILE_TYPE_MAP.put("doc", "D0CF11E0"); // MS Excel 注意：word 和 excel的文件头一样
		FILE_TYPE_MAP.put("mdb", "5374616E64617264204A"); // MS Access (mdb)
		FILE_TYPE_MAP.put("wpd", "FF575043"); // WordPerfect (wpd)
		FILE_TYPE_MAP.put("eps", "252150532D41646F6265");
		FILE_TYPE_MAP.put("ps", "252150532D41646F6265");
		FILE_TYPE_MAP.put("pdf", "255044462D312E"); // Adobe Acrobat (pdf)
		FILE_TYPE_MAP.put("qdf", "AC9EBD8F"); // Quicken (qdf)
		FILE_TYPE_MAP.put("pwl", "E3828596"); // Windows Password (pwl)
		FILE_TYPE_MAP.put("wav", "57415645"); // Wave (wav)
		FILE_TYPE_MAP.put("avi", "41564920");
		FILE_TYPE_MAP.put("ram", "2E7261FD"); // Real Audio (ram)
		FILE_TYPE_MAP.put("rm", "2E524D46"); // Real Media (rm)
		FILE_TYPE_MAP.put("mpg", "000001BA"); //
		FILE_TYPE_MAP.put("mov", "6D6F6F76"); // Quicktime (mov)
		FILE_TYPE_MAP.put("asf", "3026B2758E66CF11"); // Windows Media (asf)
		FILE_TYPE_MAP.put("mid", "4D546864"); // MIDI (mid)
	}

	/**
	 * Created on 2016-1-11
	 * <p>
	 * Discription:[getAllFileType,常见文件头信息]
	 * </p>
	 * 
	 * @author:chencb
	 */
	private static void setContentType() {
		Content_Type_MAP.put("jpg", "image/jpeg"); // JPEG (jpg)
		Content_Type_MAP.put("JPEG", "image/jpeg"); // JPEG (jpg)
		Content_Type_MAP.put("png", "image/png"); // PNG (png)
		Content_Type_MAP.put("gif", "image/gif"); // GIF (gif)
		Content_Type_MAP.put("bmp", "424D"); // Windows Bitmap (bmp)
		Content_Type_MAP.put("html", "text/html"); // HTML (html)
		Content_Type_MAP.put("htm", "text/html"); // HTML (html)
		Content_Type_MAP.put("xml", "text/xml");
		Content_Type_MAP.put("txt", "text/plain");
		Content_Type_MAP.put("xls", "application/x-xls"); // MS Word
		Content_Type_MAP.put("doc", "application/msword"); // MS Excel 注意：word 和
															// excel的文件头一样
		// 根据实际情况增加。可参考：http://tool.oschina.net/commons
	}

	public static String getContentType(String key) {
		String result = null;
		result = Content_Type_MAP.get(key);
		return (result == null || "".equals(result)) ? null : result;
	}

	/**
	 * Created on 2016-1-11
	 * <p>
	 * Discription:[getFileByFile,获取文件类型,包括图片,若格式不是已配置的,则返回null]
	 * </p>
	 * 
	 * @param file
	 * @return fileType
	 * @author:chencb
	 */
	public final static String getFileByFile(File file) {
		String filetype = null;
		byte[] b = new byte[50];
		try {
			InputStream is = new FileInputStream(file);
			is.read(b);
			filetype = getFileTypeByStream(b);
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filetype;
	}

	/**
	 * Created on 2016-1-11
	 * <p>
	 * Discription:[getFileTypeByStream]
	 * </p>
	 * 
	 * @param b
	 * @return fileType
	 * @author:chencb
	 */
	public final static String getFileTypeByStream(byte[] b) {
		String filetypeHex = String.valueOf(getFileHexString(b));
		Iterator<Entry<String, String>> entryiterator = FILE_TYPE_MAP
				.entrySet().iterator();
		while (entryiterator.hasNext()) {
			Entry<String, String> entry = entryiterator.next();
			String fileTypeHexValue = entry.getValue();
			if (filetypeHex.toUpperCase().startsWith(fileTypeHexValue)) {
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * Created on 2016-1-11
	 * <p>
	 * Discription:[getFileHexString]
	 * </p>
	 * 
	 * @param b
	 * @return fileTypeHex
	 * @author:chencb
	 */
	public final static String getFileHexString(byte[] b) {
		StringBuilder stringBuilder = new StringBuilder();
		if (b == null || b.length <= 0) {
			return null;
		}
		for (int i = 0; i < b.length; i++) {
			int v = b[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

}
