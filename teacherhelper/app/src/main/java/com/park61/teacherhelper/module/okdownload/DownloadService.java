package com.park61.teacherhelper.module.okdownload;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.aliyun.vodplayer.downloader.AliyunDownloadInfoListener;
import com.aliyun.vodplayer.downloader.AliyunDownloadManager;
import com.aliyun.vodplayer.downloader.AliyunDownloadMediaInfo;
import com.aliyun.vodplayer.downloader.AliyunRefreshPlayAuthCallback;
import com.aliyun.vodplayer.media.AliyunPlayAuth;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.park61.teacherhelper.common.set.AppUrl;
import com.park61.teacherhelper.module.okdownload.db.DownloadDAO;
import com.park61.teacherhelper.module.okdownload.db.DownloadTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.park61.teacherhelper.module.home.CourseAudioDetailsActivity.ACTION_DOWNLOAD_FINISHED;

/**
 * Created by chenlie on 2018/2/2.
 *
 */

public class DownloadService extends Service implements AliyunDownloadInfoListener {

    public static String TAG = "park61.aliYun.download";
    public static final int TYPE_VIDEO = 0;
    public static final int TYPE_AUDIO = 1;
    private AliyunDownloadManager downloadManager;
    private ProgressListener mListener;
    private HashMap<String, String> map = new HashMap<>();

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mListener = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        downloadManager = AliyunDownloadManager.getInstance(this);
        downloadManager.setDownloadInfoListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        if(intent != null){
            String title = intent.getStringExtra("title");
            long contentId = intent.getLongExtra("contentId", 0);
            long sid = intent.getLongExtra("sid", 0);
            String vid = intent.getStringExtra("vid");
            String playAuth = intent.getStringExtra("playAuth");
            String quality = intent.getStringExtra("quality");
            int type = intent.getIntExtra("type", TYPE_VIDEO);
            String icon = intent.getStringExtra("icon");

            //构建playAuth
            AliyunPlayAuth.AliyunPlayAuthBuilder authBuilder = new AliyunPlayAuth.AliyunPlayAuthBuilder();
            //不加密
            authBuilder.setIsEncripted(0);
            authBuilder.setVid(vid);
            authBuilder.setPlayAuth(playAuth);
            authBuilder.setQuality(TextUtils.isEmpty(quality)? IAliyunVodPlayer.QualityValue.QUALITY_FLUENT :quality);
            authBuilder.setTitle(title);
            authBuilder.setFormat(type == 0 ? "m3u8" : "mp3");

            //添加数据库
            DownloadTask task = new DownloadTask();
            task.setTask_type(type);
            task.setTask_name(title);
            task.setTask_time(fomatCurrentTime());
            task.setSourceId(sid+"");
            task.setContentId(contentId+"");
            task.setTask_vid(vid);
            task.setTask_icon(icon);
            DownloadDAO.getInstance().insertOrUpdate(task);
            map.put(vid, sid+"");
            //下载
            aliDownload(authBuilder.build());
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void aliDownload(AliyunPlayAuth auth){

        downloadManager.setRefreshAuthCallBack(new AliyunRefreshPlayAuthCallback() {
            @Override
            public AliyunPlayAuth refreshPlayAuth(String vid, String quality, String format, String title, boolean encript) {
                //根据 vid刷新 playAuth
                Log.e(TAG, "refresh playAuth");
                // 由vid获取 sid， sid调用后台接口获取playAuth
                Log.e(TAG, "vid:"+vid);

                JSONObject data = null;
                //请求后台接口 刷新playAuth
                HttpURLConnection mHttpURLConnection=null;
                try {
                    String sid = map.get(vid);
                    Log.e(TAG, "sid:"+sid);

                    URL mUrl=new URL(AppUrl.host + AppUrl.videoPlayAuth);
                    mHttpURLConnection=(HttpURLConnection)mUrl.openConnection();
                    //设置链接超时时间
                    mHttpURLConnection.setConnectTimeout(15000);
                    //设置读取超时时间
                    mHttpURLConnection.setReadTimeout(15000);
                    //设置请求参数
                    mHttpURLConnection.setRequestMethod("POST");
                    //添加Header
                    mHttpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                    mHttpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    //接收输入流
                    mHttpURLConnection.setDoInput(true);
                    //传递参数时需要开启
                    mHttpURLConnection.setDoOutput(true);

                    InputStream mInputStream = null;
                    OutputStreamWriter writer=new OutputStreamWriter(mHttpURLConnection.getOutputStream(),"UTF-8");
                    writer.write("id="+sid);
                    writer.flush();
                    writer.close();
                    mInputStream = mHttpURLConnection.getInputStream();
//                    int code = mHttpURLConnection.getResponseCode();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(mInputStream));
                    StringBuffer sb = new StringBuffer();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    String response = sb.toString();
                    JSONObject j = new JSONObject(response);
                    data = j.optJSONObject("data");
                    mInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e(TAG, data!=null?data.optString("videoPlayAuth") : "playAuth null");
                AliyunPlayAuth.AliyunPlayAuthBuilder authBuilder = new AliyunPlayAuth.AliyunPlayAuthBuilder();
                authBuilder.setPlayAuth(data!=null?data.optString("videoPlayAuth") : "");
                authBuilder.setVid(vid);
                authBuilder.setTitle(title);
                authBuilder.setQuality(quality);
                authBuilder.setFormat(format);
                authBuilder.setIsEncripted(0);

                return authBuilder.build();
            }
        });
        downloadManager.prepareDownloadMedia(auth);
    }

    @Override
    public void onPrepared(List<AliyunDownloadMediaInfo> list) {
        if(list != null && list.size() > 1){
            AliyunDownloadMediaInfo info =  list.get(1);
            downloadManager.addDownloadMedia(info);
            int result = downloadManager.startDownloadMedia(info);
            Log.e(TAG, "---"+ result);
            //2代表下载中
            DownloadDAO.getInstance().update(info.getVid(), 2);
        }
    }

    @Override
    public void onStart(AliyunDownloadMediaInfo aliyunDownloadMediaInfo) {
        Log.e(TAG, "onStart");
    }

    @Override
    public void onProgress(AliyunDownloadMediaInfo aliyunDownloadMediaInfo, int i) {
        Log.e(TAG, "onProgress");
        if(mListener != null){
            mListener.newProgress(aliyunDownloadMediaInfo);
        }
    }

    @Override
    public void onStop(AliyunDownloadMediaInfo aliyunDownloadMediaInfo) {
        Log.e(TAG, "onStop");
        if(mListener != null){
            mListener.onStop(aliyunDownloadMediaInfo);
        }
    }

    @Override
    public void onCompletion(AliyunDownloadMediaInfo aliyunDownloadMediaInfo) {
        Log.e(TAG, "onCompletion:"+ aliyunDownloadMediaInfo.getSavePath());
        // 1代表下载完成
        DownloadDAO.getInstance().update(aliyunDownloadMediaInfo.getVid(), 1, aliyunDownloadMediaInfo.getSavePath(), aliyunDownloadMediaInfo.getSize());
        // 删除map value
        map.remove(aliyunDownloadMediaInfo.getVid());
        if(mListener != null){
            mListener.onComplete(aliyunDownloadMediaInfo);
        }
        //发送已完成广播
        Intent it = new Intent();
        it.setAction(ACTION_DOWNLOAD_FINISHED);
        sendBroadcast(it);
    }

    @Override
    public void onError(AliyunDownloadMediaInfo aliyunDownloadMediaInfo, int i, String s, String s1) {
        Log.e(TAG, "onError\ncode:"+i + "\nmsg:"+s+"\nrequestId:"+s1);
        if(aliyunDownloadMediaInfo.getProgress() == 100 || i == 102){
            downloadManager.removeDownloadMedia(aliyunDownloadMediaInfo);
            DownloadDAO.getInstance().delete(aliyunDownloadMediaInfo.getVid());
        }
        if(mListener != null){
            mListener.onError(aliyunDownloadMediaInfo);
        }
    }

    @Override
    public void onWait(AliyunDownloadMediaInfo aliyunDownloadMediaInfo) {
        Log.e(TAG, "onWait");
        if(mListener != null){
            mListener.onWait();
        }
    }

    @Override
    public void onM3u8IndexUpdate(AliyunDownloadMediaInfo aliyunDownloadMediaInfo, int i) {
        Log.e(TAG, "onM3u8IndexUpdate");
    }

    public static String fomatCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = format.format(new Date(System.currentTimeMillis()));
        return time;
    }

    public interface ProgressListener{
        void newProgress(AliyunDownloadMediaInfo aliyunDownloadMediaInfo);
        void onStop(AliyunDownloadMediaInfo aliyunDownloadMediaInfo);
        void onComplete(AliyunDownloadMediaInfo aliyunDownloadMediaInfo);
        void onError(AliyunDownloadMediaInfo aliyunDownloadMediaInfo);
        void onWait();
    }

    MyBinder myBinder = new MyBinder();
    public class MyBinder extends Binder{

        public void setDownloadingListener(ProgressListener listener){
            mListener = listener;
        }
    }
}
