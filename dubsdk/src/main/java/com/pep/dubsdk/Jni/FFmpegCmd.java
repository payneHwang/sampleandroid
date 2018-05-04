package com.pep.dubsdk.Jni;

import android.support.annotation.Keep;

import VideoHandle.OnEditorListener;


/**
 * Created by 杨杰 on 2017/2/14.
 */

@Keep
public class FFmpegCmd {
	/**
	 * 加载所有相关链接库
	 */
	static {
		System.loadLibrary("avutil");
		System.loadLibrary("avcodec");
		System.loadLibrary("swresample");
		System.loadLibrary("avformat");
		System.loadLibrary("swscale");
		System.loadLibrary("avfilter");
		System.loadLibrary("avdevice");
		System.loadLibrary("ffmpeg");
	}

	private static OnEditorListener listener;
	private static long duration;

	/**
	 * 调用底层执行
	 *
	 * @param argc
	 * @param argv
	 * @return
	 */
	@Keep
	public static native int exec(int argc, String[] argv);

	@Keep
	public static native void exit();

	@Keep
	public static void onExecuted(int ret) {
		if (listener != null) {
			if (ret == 0) {
				listener.onProgress(1);
				listener.onSuccess();
			} else {
				listener.onFailure();
			}
		}
	}

	@Keep
	public static void onProgress(float progress) {
		if (listener != null) {
			if (duration != 0) {
				listener.onProgress(progress / (duration / 1000000) * 0.95f);
			}
		}
	}


	/**
	 * 执行ffmoeg命令
	 *
	 * @param cmds
	 * @param listener
	 */
	@Keep
	public static void exec(String[] cmds, long duration, OnEditorListener listener) {
		FFmpegCmd.listener = listener;
		FFmpegCmd.duration = duration;
		exec(cmds.length, cmds);
	}
}
