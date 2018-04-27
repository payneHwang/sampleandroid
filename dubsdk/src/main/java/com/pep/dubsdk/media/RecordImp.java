package com.pep.dubsdk.media;

import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecordImp implements RecordInterface, OnErrorListener {

	private static final String TAG = "Record";
	private AtomicBoolean mIsRecording = new AtomicBoolean(false);
	private String mRecordFilePath;
	private MediaRecorder mediaRecorder;
	private File soundFile;
	private RecordCallback mRecordCallback;

	private static RecordImp mInstance = new RecordImp();
	private long startTime = -1L;
	private long recordTime;

	
	public static RecordImp getInstance() {
		return mInstance;
	}
	public static RecordImp getInstance(String recordFilePath) {
		mInstance.setmRecordFilePath(recordFilePath);
		return mInstance;
	}

	private RecordImp() {

	}

	private RecordImp(String recordFilePath) {
		this.mRecordFilePath = recordFilePath;
	}
	
	public File getSoundFile() {
		return soundFile;
	}

	public void setSoundFile(File soundFile) {
		this.soundFile = soundFile;
	}
	
	public String getmRecordFilePath() {
		return mRecordFilePath;
	}
	
	public void setmRecordFilePath(String mRecordFilePath) {
		this.mRecordFilePath = mRecordFilePath;
	}

//	public void change() {
//		stopRecord();
//	}

	class RecorderRunnable implements Runnable {

		@Override
		public void run() {
			start();
		}
	}

	public synchronized void start() {
		Log.i(TAG, "start --mRecordFilePathL:" + mRecordFilePath);
		if (mIsRecording.get()) {
			return;
		}

		if (TextUtils.isEmpty(mRecordFilePath)) {
			try {
				mRecordFilePath = Environment.getExternalStorageDirectory()
						.getCanonicalPath() + File.separator + "sound.mp3";
			} catch (IOException e) {
				e.printStackTrace();
				if (mRecordCallback != null) {
					mRecordCallback.recordError();
				}
			}
		}

		try {
			if (mediaRecorder == null) {
				mediaRecorder = new MediaRecorder();
			}

			soundFile = new File(mRecordFilePath);
			if (soundFile.getParentFile() != null
            		&& !soundFile.getParentFile().exists()) {
            	soundFile.getParentFile().mkdirs();
            }
            if (soundFile.exists()) {
                soundFile.delete();
            }
            System.out.println("Start before..."+System.currentTimeMillis());
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

//			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);

			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

			mediaRecorder.setAudioSamplingRate(8000);

			mediaRecorder.setOutputFile(soundFile.getAbsolutePath());

			mediaRecorder.prepare();

			mediaRecorder.start();
			System.out.println("Start..."+System.currentTimeMillis());
			mediaRecorder.setOnErrorListener(this);
			mIsRecording.set(true);
			startTime = System.currentTimeMillis();
			if(mRecordCallback!=null){
			    mRecordCallback.recordStart();
			}
			Log.i(TAG,
					"soundFile---->>" + soundFile.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
			if (mRecordCallback != null) {
				mRecordCallback.recordError();
			}
		}

	}

	@Override
	public void startRecorde() {
		RecorderRunnable r = new RecorderRunnable();
		new Thread(r).start();
	}

	@Override
	public void stopRecord() {
		stopRecord(true);
	}

	public synchronized void stopRecord(boolean hasCallback) {
		Log.i(TAG, "recordStop --- hasCallback=" + hasCallback);
		if (mediaRecorder == null) {
			return;
		}
		if (mIsRecording.get()) {
			try {
			    System.out.println("Stop before..."+System.currentTimeMillis());
				mediaRecorder.stop();
				System.out.println("Stop after..."+System.currentTimeMillis());
			} catch (Exception e) {
				Log.i(TAG, "recordStop Exception " + e);
				if (mRecordCallback != null) {
                    mRecordCallback.recordError();
                }
				/*try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				try {
					mediaRecorder.stop();
				} catch (Exception e2) {
					e2.printStackTrace();
					DebugLog.logi(TAG, "second stop error :" + e);
					if (mRecordCallback != null) {
						mRecordCallback.recordError();
					}
				}*/

			}

			mediaRecorder.reset();
			mediaRecorder.release();
			mediaRecorder = null;
			System.out.println("Start before..."+System.currentTimeMillis());
			mIsRecording.set(false);
			long recordTime = getRecordTime();
			startTime = -1;
			if (mRecordCallback != null && hasCallback) {
				mRecordCallback.recordEnd(soundFile.getAbsolutePath(),recordTime);
			}
			Log.i(TAG, "-----mediaRecorder   is  release ");
		}
	}

	public long getRecordTime() {
		recordTime = -1L;
		long endTime = System.currentTimeMillis();
		System.out.println("endTime..."+System.currentTimeMillis());
		if (startTime > -1) {
			recordTime = endTime - startTime;
		}
//		DebugLog.logi(TAG, "--getRecordTime-recordTime=" + recordTime
//				+ "---startTime=" + startTime);
		return recordTime;
	}

	@Override
	public void onError(MediaRecorder mr, int what, int extra) {
		if (mr != null) {
			try {
				mr.stop();
			} catch (Exception e) {
				Log.i(TAG, "onError" + e);
			}
			mr.reset();
			mr.release();
			mr = null;
			if (mRecordCallback != null) {
				mRecordCallback.recordError();
			}
		}
		startTime = -1;
		mIsRecording.set(false);
	}

	public void setRecordCallback(RecordCallback recordCallback) {
		this.mRecordCallback = recordCallback;
	}

	public float getVolume() {
		float maxAmplitude = 0f;
		if (mediaRecorder != null) {
			try {
				maxAmplitude = mediaRecorder.getMaxAmplitude() / 32768f;
				Log.i(TAG, maxAmplitude + "");
			} catch (Exception e) {
				e.printStackTrace();
				Log.i(TAG, "-getVolume-" + e.getMessage());
			}
		}
		return maxAmplitude;
	}
}
