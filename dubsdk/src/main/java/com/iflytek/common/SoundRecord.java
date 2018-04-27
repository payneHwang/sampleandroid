package com.iflytek.common;

import android.media.MediaRecorder;
import android.util.Log;

public class SoundRecord {

	private MediaRecorder recorder;
	private static final int OUTPUTFORMAT   = MediaRecorder.OutputFormat.THREE_GPP;
	private static final int AUDIOENCODER   = MediaRecorder.AudioEncoder.AMR_NB;

	public SoundRecord() {
		// TODO Auto-generated constructor stub
		recorder = new MediaRecorder();  //初始化Recorder
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);  //设置麦克风

	}

	public void set(){
		recorder.setOutputFormat(OUTPUTFORMAT);//设置输出格式
		recorder.setAudioEncoder(AUDIOENCODER);  //设置音频编码Encoder
		String outputfile = "/sdcard/aiet/1.3gp";
		recorder.setOutputFile(outputfile);  //设置音频文件保存路径
	}

	public void play(){
		try{
			recorder.prepare();  //prepare
		}catch(Exception e){
			Log.v("SoundRecord", "Recorder prepare fail...");
		}
		recorder.start();  //开始录制
	}

	public void stop(){
		recorder.release();
		recorder.stop();
	}

	public void reset(){
		recorder.reset();
	}
}
