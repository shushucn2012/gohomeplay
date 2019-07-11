package com.park61.teacherhelper.widget;

import android.os.Environment;
import android.util.Log;

//import com.github.barteksc.pdfviewer.PDFView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;

import java.io.File;

public class PdfReadActivity extends BaseActivity {

    public String Pdf_Url = "";//"http://park61.oss-cn-zhangjiakou.aliyuncs.com/teach/20170809171507064_609.pdf";
//    private PDFView pdfview;

    @Override
    public void setLayout() {
//        setContentView(R.layout.activity_pdfview);
    }

    @Override
    public void initView() {
        //pdfview = (PDFView) findViewById(R.id.pdfview);
//        pdfview = (PDFView) findViewById(R.id.pdfview);
    }

    @Override
    public void initData() {
        Pdf_Url = getIntent().getStringExtra("PDF_URL");
        downloadPdf();
    }

    @Override
    public void initListener() {

    }

    private void downloadPdf() {
        try {
            com.lidroid.xutils.HttpUtils http = new com.lidroid.xutils.HttpUtils();

            RequestParams params = new RequestParams();
            params.addQueryStringParameter("123", "123");
            http.send(HttpRequest.HttpMethod.POST, Pdf_Url, params, new RequestCallBack<Object>() {
                @Override
                public void onSuccess(ResponseInfo<Object> responseInfo) {

                }

                @Override
                public void onFailure(HttpException e, String s) {

                }
            });

            http.download(Pdf_Url, Environment.getExternalStorageDirectory() + "/provisional.pdf", true, false, new RequestCallBack<File>() {
                @Override
                public void onStart() {
                    showDialog();
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    dismissDialog();
                    File file = new File(Environment.getExternalStorageDirectory() + "/provisional.pdf");
                    readPdf(file);
                }
            });
        } catch (Exception e) {
            Log.d("error", e.toString());
        }
    }

    private void readPdf(File file) {
////        pdfview.fromFile(file)
//                .swipeHorizontal(false)
//                .defaultPage(0)
//                .enableDoubletap(true)
//                .load();
        /*pdfview.fromFile(file)
                .swipeHorizontal(false)
                .defaultPage(0)
                .enableDoubletap(true)
                .load();*/
    }

    //这个下载网络上的pdf到sd卡上的代码  然后进行本地读取  在失去焦点的时候删除下载的文件
    @Override
    protected void onPause() {
        super.onPause();
        File file = new File(Environment.getExternalStorageDirectory() + "/provisional.pdf");
        if (file.exists()) {
            file.delete();
        }
    }

}