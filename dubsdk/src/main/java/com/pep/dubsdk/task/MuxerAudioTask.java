package com.pep.dubsdk.task;

import android.os.AsyncTask;
import android.util.Log;

import com.pep.dubsdk.audio.AudioMaster;
import com.pep.dubsdk.event.PlayControlEvent;

import org.greenrobot.eventbus.EventBus;

import VideoHandle.OnEditorListener;

public class MuxerAudioTask extends AsyncTask<Void, Integer, Boolean> {
    private static final String TAG = "DubActivity";
    private String inputPath;
    private String outputPath;

    public MuxerAudioTask(String inputPath, String outputPath) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        AudioMaster.muxer(inputPath, outputPath, new OnEditorListener() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "MuxerAudioTask doInBackground onSuccess,:::");
                PlayControlEvent event = new PlayControlEvent(PlayControlEvent.PLAY);
                event.setVideoUrl(outputPath);
                event.setFileName("_apart");
                EventBus.getDefault().post(event);
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "MuxerAudioTask doInBackground onFailure,:::");
            }

            @Override
            public void onProgress(float progress) {
                Log.e(TAG, "MuxerAudioTask doInBackground onProgress,:::" + progress);
            }
        });
        return null;
    }
}


