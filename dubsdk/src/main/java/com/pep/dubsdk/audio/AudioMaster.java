package com.pep.dubsdk.audio;

import android.util.Log;

import com.pep.dubsdk.SdcardUtil;
import com.pep.dubsdk.task.MuxerAudioTask;

import VideoHandle.EpEditor;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;

public class AudioMaster {
    private static final String TAG = "AudioMaster";

    /**
     * 分离音视频
     *
     * @param inputPath        视频路径
     * @param outputPath       输出路径
     * @param onEditorListener 输出类型、回调结果
     */
    public static void muxer(String inputPath, String outputPath, OnEditorListener onEditorListener) {
        EpEditor.demuxer(inputPath, outputPath, EpEditor.Format.MP4, onEditorListener);
    }

    /**
     * 执行分离、录音、合成工作
     */
    public static void execVideo(int fromSecond, int toSecond, final String fileName, String videoUrl) {
        EpVideo epVideo = new EpVideo(videoUrl);
        epVideo.clip(fromSecond, toSecond);
        final String outPath = SdcardUtil.getSaveFilePath(fileName + ".mp4");
        Log.e(TAG, "execVideo,the outPath:::" + outPath);
        VideoHandle.EpEditor.exec(epVideo, new VideoHandle.EpEditor.OutputOption(outPath), new VideoHandle.OnEditorListener() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "execVideo,onSuccess:::" + outPath);
                new MuxerAudioTask(outPath, SdcardUtil.getSavePath() + fileName + "_audio.mp4").execute();
//                //发送Event消息通知主线程进行相关的操作
//                PlayControlEvent event = new PlayControlEvent(PlayControlEvent.PLAY);
//                event.setFileName(fileName);
//                event.setVideoUrl(outPath);
//                EventBus.getDefault().post(event);
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "execVideo,onFailure:::" + outPath);
            }

            @Override
            public void onProgress(float v) {
                Log.e(TAG, "execVideo,onProgress:::" + v);
            }
        });

    }
}
