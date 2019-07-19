package com.gogotalk.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.gogotalk.R;
import com.gogotalk.model.util.Constant;
import com.gogotalk.view.widget.FullScreenVideoView;
import butterknife.BindView;
import butterknife.OnClick;

public class VideoActivity extends BaseActivity implements MediaPlayer.OnPreparedListener, SeekBar.OnSeekBarChangeListener {
    @BindView(R.id.id_mVideo_Vis1)
    FullScreenVideoView videoView;
    @BindView(R.id.play_video)
    ImageView playVideo;
    @BindView(R.id.time_video)
    TextView timeVideo;
    @BindView(R.id.seekbar_video)
    SeekBar mSeekBar;
    private String videoUrl;
    private Dialog dialog;
    public boolean threadRun = true;
    @BindView(R.id.control_video)
    RelativeLayout control_video;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video;
    }

    @Override
    protected void initInject() {
    }

    @Override
    public void getIntentData() {
        super.getIntentData();
        Intent mIntent = getIntent();
        videoUrl = mIntent.getStringExtra(Constant.INTENT_DATA_KEY_VIDEO_URL);
        showLoading(null);
        Uri uri = Uri.parse(videoUrl);
        videoView.setVideoURI(uri);
    }

    @Override
    public void initView() {
        videoView.setOnPreparedListener(this);
        //设置视频控制器
        //videoView.setMediaController(new MediaController(VideoActivity.this));
        //播放完成回调
        videoView.setOnCompletionListener(new MyPlayerOnCompletionListener());
        //设置视频路径
        videoView.requestFocus();
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //开始播放视频
        videoView.start();
        new Thread(timerRunnable).start();
        mSeekBar.setMax(videoView.getDuration());
        playVideo.setClickable(true);
        hideLoading();
    }

    @OnClick({R.id.play_video, R.id.goback_video, R.id.id_mVideo_Vis1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_video://关闭房间
                if (videoView.isPlaying()) {
                    videoView.pause();
                    playVideo.setImageResource(R.mipmap.video_play);
                } else {
                    playVideo.setImageResource(R.mipmap.video_pause);
                    videoView.start();
                }
                break;
            case R.id.goback_video:
                finish();
                break;
            case R.id.id_mVideo_Vis1:
                if (control_video.getVisibility() == View.VISIBLE) {
                    control_video.setVisibility(View.INVISIBLE);
                } else {
                    control_video.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    //seekbar进度变化监听
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        // 取得当前进度条的刻度
        if (videoView.isPlaying()) {
            // 设置当前播放的位置
            videoView.seekTo(progress);
        }
    }

    private class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(VideoActivity.this, "播放完毕", Toast.LENGTH_LONG).show();
            mSeekBar.setProgress(0);
            timeVideo.setText("00:00");
            playVideo.setImageResource(R.mipmap.video_play);
        }
    }

    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                while (threadRun) {
                    if (videoView.isPlaying()) {
                        // 如果正在播放，没0.5.毫秒更新一次进度条
                        final int current = videoView.getCurrentPosition();
                        mSeekBar.setProgress(current);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timeVideo.setText(getTimeText(current / 1000));
                            }
                        });
                    }
                    Thread.sleep(800);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public String getTimeText(int current) {
        if (current < 60) {
            if (current < 10) {
                return "00:0" + current;
            }
            return "00:" + current;
        }
        int currentTen = current / 60;
        int currentOne = current % 60;
        if (currentTen < 10) {
            if (currentOne < 10) {
                return "0" + currentTen + ":0" + currentOne;
            }
            return "0" + currentTen + ":" + currentOne;
        }
        if (currentOne < 10) {
            return currentTen + ":0" + currentOne;
        }
        return currentTen + ":" + currentOne;
    }

    @Override
    public void onDestroy() {
        if (videoView != null) {
            videoView.stopPlayback();
        }
        threadRun = false;
        super.onDestroy();
    }
}
