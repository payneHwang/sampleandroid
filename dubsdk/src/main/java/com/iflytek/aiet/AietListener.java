package com.iflytek.aiet;


public interface AietListener {

/**
 * 录音数据回调
 * @param dataBuffer
 */
	void onBufferReceived(byte[] dataBuffer,int len );

	/**
	 * 测试开始
	 */
	//public void onBeginningOfTest();

	/**
	 * 停止测试
	 */
	public void onEndOfTest();

	/**
	 * 识别错误返回
	 */
	public void onError(int error );
	/**
	 *
	 * 	/**
	 * 识别警告返回
	 */
	public void onWarring(int MsgType, int value);
	/**
	 * 识别结果返回
	 */
	public void onResults(Integer index);
	/**
	 * 录音开始
	 */
	public void onBeginningOfRecord();
	/**
	 * 录音结束
	 */
	public void onEndOfRecord();
	
	/**
	 * @function 录音时间
	 * @param recordTime 录音时间
	 */
	public void onRecordTime(long recordTime);
	
	void onBeginningOfTest(String testContent);


}
