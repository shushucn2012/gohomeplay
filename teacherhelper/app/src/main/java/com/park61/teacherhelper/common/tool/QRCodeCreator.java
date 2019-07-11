package com.park61.teacherhelper.common.tool;

import java.util.HashMap;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.widget.ImageView;

public class QRCodeCreator {

	/**
	 * 二维码生成
	 * 
	 * @param url
	 *            url地址 要转换的地址或字符串,可以是中文
	 * @param QR_WIDTH
	 *            宽
	 * @param QR_HEIGHT
	 *            长
	 * @param imgView
	 *            显示的控件
	 */
	public static void createQRImage(String url, int QR_WIDTH, int QR_HEIGHT, ImageView imgView) {
		createQRImage(url, QR_WIDTH, QR_HEIGHT, imgView, 0xff000000, null, 0xffffffff);
	}

	public static void createQRImage(String url, int QR_WIDTH, int QR_HEIGHT, ImageView imgView, int foregroundColor,
			Bitmap logoBm, int backgroundColor) {
		try {
			// 判断URL合法性
			if (url == null || "".equals(url) || url.length() < 1) {
				return;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT,
					hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < QR_HEIGHT; y++) {
				for (int x = 0; x < QR_WIDTH; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * QR_WIDTH + x] = foregroundColor;
					} else {
						pixels[y * QR_WIDTH + x] = backgroundColor;
					}
				}
			}
			// 生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			if (logoBm != null) {
				bitmap = addLogo(bitmap, logoBm);
			}
			// 显示到一个ImageView上面
			imgView.setImageBitmap(bitmap);
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}

	public static Bitmap generateQRCode(String content, int width, int height, int foregroundColor, int backgroundColor,
			Bitmap logoBm) {
		try {
			HashMap hints = new HashMap();
			// 设置编码方式utf-8
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 设置二维码的纠错级别为h
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
			Bitmap b = bitMatrix2Bitmap(matrix, foregroundColor, backgroundColor);
			if (logoBm != null) {
				b = addLogo(b, logoBm);
			}
			return b;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Bitmap bitMatrix2Bitmap(BitMatrix matrix, int foregroundColor, int backgroundColor) {
		matrix = updateBit(matrix, 0);
		int w = matrix.getWidth();
		int h = matrix.getHeight();
		int[] rawData = new int[w * h];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (matrix.get(x, y)) {
					rawData[y * w + x] = foregroundColor;
				} else {
					rawData[y * w + x] = backgroundColor;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(w, h, Config.RGB_565);
		bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
		return bitmap;
	}

	private static BitMatrix updateBit(BitMatrix matrix, int margin) {
		int tempM = margin * 2;
		int[] rec = matrix.getEnclosingRectangle(); // 获取二维码图案的属性
		int resWidth = rec[2] + tempM;
		int resHeight = rec[3] + tempM;
		BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // 按照自定义边框生成新的BitMatrix
		resMatrix.clear();
		for (int i = margin; i < resWidth - margin; i++) { // 循环，将二维码图案绘制到新的bitMatrix中
			for (int j = margin; j < resHeight - margin; j++) {
				if (matrix.get(i - margin + rec[0], j - margin + rec[1])) {
					resMatrix.set(i, j);
				}
			}
		}
		return resMatrix;
	}

	/**
	 * 在二维码中间添加Logo图案
	 */
	private static Bitmap addLogo(Bitmap src, Bitmap logo) {
		if (src == null) {
			return null;
		}

		if (logo == null) {
			return src;
		}

		// 获取图片的宽高
		int srcWidth = src.getWidth();
		int srcHeight = src.getHeight();
		int logoWidth = logo.getWidth();
		int logoHeight = logo.getHeight();

		if (srcWidth == 0 || srcHeight == 0) {
			return null;
		}

		if (logoWidth == 0 || logoHeight == 0) {
			return src;
		}

		// logo大小为二维码整体大小的1/5
		float scaleFactor = srcWidth * 1.0f / 8 / logoWidth;
		Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
		try {
			Canvas canvas = new Canvas(bitmap);
			canvas.drawBitmap(src, 0, 0, null);
			canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
			canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
		} catch (Exception e) {
			bitmap = null;
			e.getStackTrace();
		}

		return bitmap;
	}
}
