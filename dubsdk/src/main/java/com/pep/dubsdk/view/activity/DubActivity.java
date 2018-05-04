package com.pep.dubsdk.view.activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pep.dubsdk.R;
import com.pep.dubsdk.SdcardUtil;
import com.pep.dubsdk.adapter.SentenceListAdapter;
import com.pep.dubsdk.event.PlayControlEvent;
import com.pep.dubsdk.model.Sentence;
import com.pep.dubsdk.model.Video;
import com.pep.dubsdk.task.ClipVideoTask;
import com.pep.dubsdk.video.MediaManager;
import com.pep.dubsdk.video.VideoPlayer;
import com.pep.dubsdk.video.VideoPlayerStandard;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DubActivity extends AppCompatActivity implements View.OnClickListener, SentenceListAdapter.VideoOperateCallBack {
    private static final String TAG = DubActivity.class.getSimpleName();
    private static final int PLAY = 0x100;
    private static final int PAUSE = 0x101;
    private static final int REMOVE = 0x102;
    private static final String SAMPLE_PIC_URL = "http://img4.jiecaojingxuan.com/2016/11/23/00b026e7-b830-4994-bc87-38f4033806a6.jpg@!640_360";
    private static final String SAMPLE_VIDEO_URL = "http://video.jiecao.fm/11/23/xin/%E5%81%87%E4%BA%BA.mp4";
    /**
     * view
     */
    private Context mContext;
    private TextView tvCurrentItem;
    private RecyclerView rvSentence;
    //video
    private String videoUrl;
    private VideoPlayerStandard videoPlayer;
    SensorManager mSensorManager;
    VideoPlayer.JCAutoFullscreenListener mSensorEventListener;

    /**
     * extras
     */
    private LinearLayoutManager layoutManager;
    private SentenceListAdapter sentenceListAdapter;
    private List<Sentence> sentenceList;
    /**
     * 视频片段播放控制器
     */
    private Handler playHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PLAY:
                    Video video;
                    video = (Video) msg.obj;
                    loadVideoFromLocal(video.fileName, video.videoUrl);
                    break;
                case PAUSE:
                    if (MediaManager.instance().mediaPlayer.isPlaying()) {
                        MediaManager.instance().mediaPlayer.pause();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dub);
        init();
        initView();
        loadData();
        setListener();
    }

    private void init() {
        mContext = this;
        if (!EventBus.getDefault().isRegistered(mContext)) {
            EventBus.getDefault().register(mContext);
        }
        videoUrl = SdcardUtil.getSaveFilePath("test_video.mp4");
        Log.e(TAG, "videoUrl------" + videoUrl);
    }


    private void initView() {
        videoPlayer = findViewById(R.id.vps_audio_player);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new VideoPlayer.JCAutoFullscreenListener();
        tvCurrentItem = findViewById(R.id.tv_course_progress);
        rvSentence = findViewById(R.id.rvSentence);
        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvSentence.setLayoutManager(layoutManager);
        sentenceList = new ArrayList<>();
        sentenceListAdapter = new SentenceListAdapter(mContext, sentenceList);
        rvSentence.setAdapter(sentenceListAdapter);
    }

    @Override
    public void onClick(View v) {
    }

    int tempSecond = 0;

    /**
     * 加载数据
     */
    private void loadData() {
        for (int i = 0; i < 18; i++) {
            Sentence sentence = new Sentence();
            sentence.sentenceId = i;
            sentence.sentenceCN = "那个下午，我发现自己满脑子都是那个提篮男孩的拍卖会" + i;
            sentence.sentenceEN = "That afternoon,I found myself obsessing about the Basket boy auction." + i;
            sentence.fromSeconds = tempSecond;
            sentence.toSeconds = tempSecond + 3;
            tempSecond = sentence.toSeconds;
            sentenceList.add(sentence);
        }
        sentenceListAdapter.notifyDataSetChanged();
    }

    private void setListener() {
        sentenceListAdapter.setVideoOperateCallBack(this);
    }


    /**
     * 加载本地视频地址
     */
    private void loadVideoFromLocal(String videoName, String videoUrl) {
        Log.e(TAG, "loadVideoFromLocal:::" + videoUrl);
        videoPlayer.setUp(videoUrl, VideoPlayer.SCREEN_LAYOUT_NORMAL, videoName);
        Glide.with(mContext).load(SAMPLE_PIC_URL).into(videoPlayer.thumbImageView);
        videoPlayer.loop = false;
        videoPlayer.startVideo();
    }

    /**
     * 记载网络视频地址
     */
    private void loadVideoFromNet() {
        videoPlayer.setUp(SAMPLE_VIDEO_URL, VideoPlayer.SCREEN_LAYOUT_NORMAL, "加载网络视频地址...");
        Glide.with(mContext).load(SAMPLE_PIC_URL).into(videoPlayer.thumbImageView);
        videoPlayer.loop = true;
        videoPlayer.headData = new HashMap<>();
        videoPlayer.headData.put("key", "value");
    }


    private void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadVideoFromLocal("第10课 Main scene", videoUrl);
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * 在执行跳转播放之后，开启任务进行视频分离工作
     */
    @Override
    public void playVideoPeriod(int position, int fromSeconds, final int toSeconds) {
        playHandler.removeMessages(PLAY);
        Log.e(TAG, "playVideoPeriod::fromSeconds:" + fromSeconds);
        Log.e(TAG, "playVideoPeriod::toSeconds:" + toSeconds);
        MediaManager.instance().mediaPlayer.seekTo(fromSeconds);
        MediaManager.instance().mediaPlayer.start();
        //TODO 延迟对应间隔之后发送暂停的消息
        Message message = Message.obtain();
        message.what = PAUSE;
        playHandler.sendMessageDelayed(message, toSeconds - fromSeconds);
    }


    @Override
    public void mixerVideo(int position, String fileName) {
        Log.e(TAG, "mixerVideo::fileName:" + fileName);
        if (sentenceList != null && sentenceList.size() != 0) {
            Sentence sentence = sentenceList.get(position);
            if (sentence != null) {
                new ClipVideoTask(sentence, fileName, videoUrl).execute();
            }
        }
    }

    @Override
    public void clipVideo(int position) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        VideoPlayer.releaseAllVideos();
        mSensorManager.unregisterListener(mSensorEventListener);
        VideoPlayer.clearSavedProgress(this, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mContext);
    }

    @Subscribe
    public void onEvent(PlayControlEvent event) {
        switch (event.getEventType()) {
            case PlayControlEvent.PLAY:
                String videoUrl = event.getVideoUrl();
                String fileName = event.getFileName();
                Video videoUrls = new Video(videoUrl, fileName);
                Message message = Message.obtain();
                message.what = PLAY;
                message.obj = videoUrls;
                playHandler.sendMessage(message);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if (VideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

}
