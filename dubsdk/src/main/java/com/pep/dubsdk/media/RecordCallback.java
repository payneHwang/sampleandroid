package com.pep.dubsdk.media;

import java.io.File;

public interface RecordCallback {
		
		public File recordEnd(String recordFile, long recordTime);

		public void recordError();
		
		public void recordStart();
	}