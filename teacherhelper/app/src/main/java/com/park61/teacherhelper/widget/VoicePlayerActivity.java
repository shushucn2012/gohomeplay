package com.park61.teacherhelper.widget;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.park61.teacherhelper.BaseActivity;
import com.park61.teacherhelper.R;

/**
 * 在线音乐播放Demo。
 */
public class VoicePlayerActivity extends BaseActivity implements OnCompletionListener,
        OnErrorListener, OnBufferingUpdateListener, OnPreparedListener,
        OnClickListener {


    private MediaPlayer mediaPlayer;
    private Button startButton, stopButton;
    private TextView statusTextView, bufferValueTextView;


    @Override
    public void setLayout() {
        setContentView(R.layout.activity_audio_httpplayer);
    }

    @Override
    public void initView() {
        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);

        statusTextView = (TextView) findViewById(R.id.statusDisplayTextView);
        bufferValueTextView = (TextView) findViewById(R.id.bufferValueTextView);

        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        statusTextView.setText("onCreate");

        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnPreparedListener(this);

        statusTextView.setText("MediaPlayer created");
        try {
            /**
             * 调用setDataSource();方法，并传入想要播放的音频文件的HTTP位置。
             */
            mediaPlayer.setDataSource("http://park61.oss-cn-zhangjiakou.aliyuncs.com/teach/20170818151450711_812.mp3");
        } catch (Exception e) {
            e.printStackTrace();
        }
        statusTextView.setText("setDataSource done");
        statusTextView.setText("calling prepareAsync");
        /**
         * 调用prepareAsync方法，它将在后台开始缓冲音频文件并返回。
         */
        mediaPlayer.prepareAsync();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }


    /**
     * 当MediaPlayer正在缓冲时，将调用该Activity的onBufferingUpdate方法。
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        bufferValueTextView.setText("" + percent + "%");
    }

    /**
     * 当完成prepareAsync方法时，将调用onPrepared方法，表明音频准备播放。
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        statusTextView.setText("onPrepared called");
        startButton.setEnabled(true);
    }

    /**
     * 当MediaPlayer完成播放音频文件时，将调用onCompletion方法。
     * 此时设置“播放”按钮可点击，“暂停”按钮不看点击（表示可以再次播放）。
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        statusTextView.setText("onCompletion called");
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    /**
     * 当按手机上的返回键的时候，会自动调用系统的onKeyDown方法，而onKeyDown方法又会调用onDestroy()方法销毁该Activity
     * ， 此时如果onDestroy()方法不重写，那么正在播放的音乐是不会停止的（大家可以试一下），所以这时候要重写onDestroy()方法，
     * 在该方法中 加入mediaPlayer.stop()方法，表示按返回键的时候，会调用mediaPlayer对象的stop方法，从而停止音乐的播放。
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        Log.d("zhongyao", "onDestroy()");
    }

    /**
     * 如果MediaPlayer出现错误，将调用onError方法。
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        statusTextView.setText("onError called");
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                statusTextView
                        .setText("MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK"
                                + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                statusTextView.setText("MEDIA_ERROR_SERVER_DIED" + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                statusTextView.setText("MEDIA_ERROR_UNKNOWN");
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * 当按下播放按钮时，调用MediaPlayer的start方法； 当按下暂停按钮时，将调用MediaPlayer的Pause方法。
     */
    @Override
    public void onClick(View v) {
        if (v == startButton) {
            mediaPlayer.start();
            statusTextView.setText("start called");
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        } else if (v == stopButton) {
            mediaPlayer.pause();
            statusTextView.setText("pause called");
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        }
    }

}
