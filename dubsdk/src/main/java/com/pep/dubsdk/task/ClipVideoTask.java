package com.pep.dubsdk.task;

import android.os.AsyncTask;
import android.util.Log;

import com.pep.dubsdk.audio.AudioMaster;
import com.pep.dubsdk.model.Sentence;

public class ClipVideoTask extends AsyncTask<Sentence, Integer, Boolean> {
    private static final String TAG = "DubActivity";
    private Sentence sentence;
    private String fileName;
    private String rootVideoPath;

    public ClipVideoTask(Sentence sentence, String fileName, String rootVideoPath) {
        this.sentence = sentence;
        this.fileName = fileName;
        this.rootVideoPath = rootVideoPath;
    }

    @Override
    protected void onPreExecute() {
        Log.e(TAG, "ClipVideoTask onPreExecute start...");
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Sentence... sentences) {
        Log.e(TAG, "ClipVideoTask doInBackground,sentences:::" + sentence);
        if (sentence != null) {
            AudioMaster.execVideo(sentence.fromSeconds, sentence.toSeconds - sentence.fromSeconds, fileName, rootVideoPath);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        Log.e(TAG, "ClipVideoTask onPostExecute,:::" + aBoolean);
        super.onPostExecute(aBoolean);
    }


}
