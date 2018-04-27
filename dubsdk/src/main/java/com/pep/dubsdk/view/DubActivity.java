package com.pep.dubsdk.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pep.dubsdk.R;
import com.pep.dubsdk.audio.EpEditor;
import com.pep.dubsdk.audio.EpVideo;
import com.pep.dubsdk.audio.OnEditorListener;
import com.pep.dubsdk.SdcardUtil;
import com.pep.dubsdk.UriUtils;
import com.pep.dubsdk.video.VideoPlayer;
import com.pep.dubsdk.video.VideoPlayerStandard;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class DubActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = DubActivity.class.getSimpleName();
    private static final int VIDEO_FROM_NET = 0;
    private static final int VIDEO_FROM_LOCAL = 1;
    private static final int VIDEO_FROM_SDCARD = 2;
    private static final int VIDEO_FROM_ASSETS = 3;
    private static final String SAMPLE_PIC_URL = "http://img4.jiecaojingxuan.com/2016/11/23/00b026e7-b830-4994-bc87-38f4033806a6.jpg@!640_360";
    private static final String SAMPLE_VIDEO_URL = "http://video.jiecao.fm/11/23/xin/%E5%81%87%E4%BA%BA.mp4";
    private String testVideoFilePath;

    private Context mContext;
    /**
     * view
     */
    private static final int CHOOSE_FILE = 10;
    private CheckBox cb_clip, cb_rotation, cb_mirror;
    private EditText et_clip_start, et_clip_end, et_crop_w, et_crop_h, et_rotation;
    private TextView tv_file;
    private Button bt_file, bt_exec;
    private String videoUrl;
    private ProgressDialog mProgressDialog;
    private TextView tvCurrentItem;
    //    private ListView lvContent;
    //video
    private VideoPlayerStandard videoPlayer;
    SensorManager mSensorManager;
    VideoPlayer.JCAutoFullscreenListener mSensorEventListener;

    /**
     * extras
     */
    private int source = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dub);
        init();
        initView();
        loadData();
    }

    private void init() {
        mContext = this;
        getExtraFromIntent();
        testVideoFilePath = SdcardUtil.getSaveFilePath("test_video.mp4");
        Log.e(TAG, "localVideoFilePath------" + testVideoFilePath);
    }

    /**
     * 获取intent中的参数---获取需要加载的视频地址来源
     */
    private void getExtraFromIntent() {
        Intent intent = getIntent();
        source = intent.getIntExtra("source", 0);
    }

    private void initView() {
        videoPlayer = findViewById(R.id.vps_audio_player);
        tvCurrentItem = findViewById(R.id.tv_course_progress);
//        lvContent = findViewById(R.id.lv_course_sentence);

        cb_clip = (CheckBox) findViewById(R.id.cb_clip);
        cb_rotation = (CheckBox) findViewById(R.id.cb_rotation);
        cb_mirror = (CheckBox) findViewById(R.id.cb_mirror);
        et_clip_start = (EditText) findViewById(R.id.et_clip_start);
        et_clip_end = (EditText) findViewById(R.id.et_clip_end);
        et_crop_w = (EditText) findViewById(R.id.et_crop_w);
        et_crop_h = (EditText) findViewById(R.id.et_crop_h);
        et_rotation = (EditText) findViewById(R.id.et_rotation);
        tv_file = (TextView) findViewById(R.id.tv_file);
        bt_file = (Button) findViewById(R.id.bt_file);
        bt_exec = (Button) findViewById(R.id.bt_exec);
        bt_file.setOnClickListener(this);
        bt_exec.setOnClickListener(this);
        cb_mirror.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_rotation.setChecked(true);
                }
            }
        });
        cb_rotation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    cb_mirror.setChecked(false);
                }
            }
        });
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMax(100);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setTitle("正在处理");
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bt_file) {
            chooseFile();

        } else if (i == R.id.bt_exec) {
            execVideo();
//				test();

        }
    }

    /**
     * 选择文件
     */
    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, CHOOSE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_FILE:
                if (resultCode == RESULT_OK) {
                    videoUrl = UriUtils.getPath(mContext, data.getData());
                    tv_file.setText(videoUrl);
                    break;
                }
        }
    }

    /**
     * 开始编辑
     */
    private void execVideo() {
        if (videoUrl != null && !"".equals(videoUrl)) {
            EpVideo epVideo = new EpVideo(videoUrl);
            if (cb_clip.isChecked())
                epVideo.clip(Float.parseFloat(et_clip_start.getText().toString().trim()), Float.parseFloat(et_clip_end.getText().toString().trim()));
            if (cb_rotation.isChecked())
                epVideo.rotation(Integer.parseInt(et_rotation.getText().toString().trim()), cb_mirror.isChecked());
            mProgressDialog.setProgress(0);
            mProgressDialog.show();
            final String outPath = SdcardUtil.getSavePath() + "out.mp4";
            EpEditor.exec(epVideo, new EpEditor.OutputOption(outPath), new OnEditorListener() {
                @Override
                public void onSuccess() {
//					Toast.makeText(EditActivity.this, "编辑完成:"+outPath, Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();

                    Intent v = new Intent(Intent.ACTION_VIEW);
                    v.setDataAndType(Uri.parse(outPath), "video/mp4");
                    startActivity(v);
                }

                @Override
                public void onFailure() {
//					Toast.makeText(EditActivity.this, "编辑失败", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }

                @Override
                public void onProgress(float v) {
                    mProgressDialog.setProgress((int) (v * 100));
                }
            });
        } else {
//			Toast.makeText(this, "选择一个视频", Toast.LENGTH_SHORT).show();
        }
    }

    private void test() {
        final String outPath = "/storage/emulated/0/Download/music.mp4";
        EpEditor.music(videoUrl, "/storage/emulated/0/DownLoad/huluwa.aac", outPath, 1.0f, 1.0f, new OnEditorListener() {
            @Override
            public void onSuccess() {
//				Toast.makeText(EditActivity.this, "编辑完成:"+outPath, Toast.LENGTH_SHORT).show();

                Intent v = new Intent(Intent.ACTION_VIEW);
                v.setDataAndType(Uri.parse(outPath), "video/mp4");
                startActivity(v);
            }

            @Override
            public void onFailure() {
//				Toast.makeText(EditActivity.this, "编辑失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(float v) {

            }
        });
    }

    /**
     * 加载数据
     */
    private void loadData() {
        switch (source) {
            case VIDEO_FROM_NET:
                loadVideoFromNet();
                break;
            case VIDEO_FROM_LOCAL:
                loadVideoFromLocal();
                break;
            case VIDEO_FROM_ASSETS:
                loadVideoFromAssets();
                break;
            case VIDEO_FROM_SDCARD:
                loadVideoFromSdcard();
                break;
        }
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new VideoPlayer.JCAutoFullscreenListener();
    }

    private void loadVideoFromAssets() {
        videoPlayer.setUp("file:///android_asset/local_video.mp4"
                , VideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN, "加载assets的视频地址");
    }

    private void loadVideoFromSdcard() {
        cpAssertVideoToLocalPath();
        videoPlayer.setUp(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/local_video.mp4"
                , VideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "记载存储卡上的视频地址");
    }

    /**
     * 加载本地视频地址
     */
    private void loadVideoFromLocal() {
        videoPlayer.setUp(testVideoFilePath, VideoPlayer.SCREEN_LAYOUT_NORMAL, "第10页 Main Scene...");
        Glide.with(mContext).load(SAMPLE_PIC_URL).into(videoPlayer.thumbImageView);
        videoPlayer.loop = true;
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


    public void cpAssertVideoToLocalPath() {
        try {
            InputStream myInput;
            OutputStream myOutput = new FileOutputStream(SdcardUtil.getVideoCachePath(mContext, "video", "test.mp4"));
            myInput = this.getAssets().open("local_video.mp4");
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while (length > 0) {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
            }

            myOutput.flush();
            myInput.close();
            myOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onPause() {
        super.onPause();
        VideoPlayer.releaseAllVideos();
        mSensorManager.unregisterListener(mSensorEventListener);
        VideoPlayer.clearSavedProgress(this, null);
    }

    private void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (VideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
}
