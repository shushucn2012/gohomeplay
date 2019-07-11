package com.park61.teacherhelper.module.clazz;

import android.widget.ImageView;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;
import com.park61.teacherhelper.common.tool.DevAttr;
import com.park61.teacherhelper.common.tool.QRCodeCreator;
import com.park61.teacherhelper.module.clazz.bean.TeachGClass;


/**
 * Created by zhangchi on 2017/8/21.
 */

public class ClazzQRCodeActivity extends BaseActivity {

    private static final String TAG = "ClazzQRCodeActivity";
    /**
     * 班级ID
     */
    //private String clazzId;
    private TeachGClass clazz;
    /**
     * 班级名称View
     */
    private TextView clazzNameView;

    /**
     * 二维码View
     */
    private ImageView qrCodeView;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_clazz_qrcode);
    }

    @Override
    public void initView() {
        setPagTitle("班级二维码");
        qrCodeView = (ImageView) findViewById(R.id.img_qrcode);
        clazzNameView = (TextView) findViewById(R.id.tv_labl_clazz_name);
    }

    @Override
    public void initData() {
        clazz = (TeachGClass) getIntent().getExtras().getSerializable("clazz");

        // Modify clazz name
        clazzNameView.setText(clazz.getFullName());
        String shareUrl = clazz.getUrl();
        QRCodeCreator.createQRImage(shareUrl, DevAttr.dip2px(mContext, 200),
                DevAttr.dip2px(mContext, 200), qrCodeView);
    }

    @Override
    public void initListener() {

    }

   /* class GenerateQrTask extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {

            //调用QrCodeGenerator
            //shareUrl = "http://www.61park.cn";
            shareUrl = clazz.getUrl();
            android.util.Log.e(TAG, "shareUrl=" + shareUrl);

            if (null == shareUrl) {
                finish();
                return null;
            }

            //获取屏幕尺寸
            int width = 200;
            int height = width;

//            int foregroundColor = 0xff000000;
//            int backgroundColor = 0xffffffff;
//            return QRCodeCreator.generateQRCode(shareUrl, width, height, foregroundColor, backgroundColor, null);


            // Use Google Zxing
            Bitmap bitmap = null;
            BitMatrix result = null;

            long start = System.currentTimeMillis();
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            long end1 =  System.currentTimeMillis();
            try {
                result = multiFormatWriter.encode(shareUrl, BarcodeFormat.QR_CODE, width, height);
                long end2 =  System.currentTimeMillis();
                // 使用 ZXing Android Embedded 要写的代码
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                long end3 =  System.currentTimeMillis();
                bitmap = barcodeEncoder.createBitmap(result);
                long end4 =  System.currentTimeMillis();

                Log.d(Tag, "end1=" + (end1-start) + ", end2=" + (end2-end1) + ", end3=" + (end3-end2) + ", end4=" + (end4-end3));
            } catch (WriterException e){
                e.printStackTrace();
            } catch (IllegalArgumentException iae){ // ?
                return null;
            }

            return bitmap;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            qrCodeView.setImageBitmap(bitmap);
        }
    }*/
}

