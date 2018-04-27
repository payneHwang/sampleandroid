package com.pep.dubsdk.media;

import java.io.File;

public interface PlayCallback {

	public File playEnd(File recordFile);

	public void playError();
	
	public void playError(int errorCode);
	
	// 返回播放状态
	public void onPlayStatus(boolean isPlay);
}