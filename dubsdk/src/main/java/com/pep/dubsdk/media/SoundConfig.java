package com.pep.dubsdk.media;

import android.media.AudioFormat;

public class SoundConfig {

	 public static final int SAMPLE_RATE = 16000;
	// public static final int SAMPLE_RATE = 12000;
//	public static int SAMPLE_RATE = 11025;
//	 public static int SAMPLE_RATE = 16000;
	// public static final int SAMPLE_RATE = 24000;
	// public static final int SAMPLE_RATE = 22050;
	// public static final int SAMPLE_RATE = 32000;
	// public static final int SAMPLE_RATE = 48000;
	// public static final int SAMPLE_RATE = 44100;

//	static {
//		String[] whiteList = RenrenApplication.getmContext().getResources()
//				.getStringArray(R.array.sound_white_list);
//		String phoneName = Build.MODEL == null ? "" : Build.MODEL.toLowerCase();
//		final int whiteListNmu = whiteList == null ? 0 : whiteList.length;
//		for (int i = 0; i < whiteListNmu; i++) {
//			if (whiteList[i] == null || !phoneName.contains(whiteList[i])) {
//				continue;
//			}
//			SAMPLE_RATE = 8000;
//			break;
//		}
//	}

	public static final int CHANNEL_IN = AudioFormat.CHANNEL_IN_MONO;
	public static final int CHANNEL_OUT = AudioFormat.CHANNEL_OUT_MONO;
	public static final int NUM_CHANNEL = 1;
	public static final int _1MINUTE = 60;
	public static final int WAV_HEADER_LENGTH = 44;
	public static final long LEAST_SDCARD_SPARE_SIZE = 1024 * 1024 * 20;

}
