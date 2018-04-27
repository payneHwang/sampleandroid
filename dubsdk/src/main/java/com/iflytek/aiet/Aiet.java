package com.iflytek.aiet;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.iflytek.aiet.AietInfo.ErrorPhone;
import com.iflytek.aiet.AietInfo.RecResultInfo;
import com.iflytek.common.TracPlayer;
import com.pep.dubsdk.R;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Aiet {
	//add by qunzhu 7-31
	public static final int  PARAGRAPH_LEVEL          =0;
	public static final int  SENTENCE_LEVEL           =1;
	public static final int  WORD_LEVEL               =2;
	public static final int  SYLLABLE_LEVEL           =3;
	public static final int  PHONE_LEVEL              =4;
	

	public static final int  WORD_WRONG          =1;
	public static final int  WORD_INSERT         =2;
	/*public static final int  PHONE_WRONG         =3;*/
	
	//add by qunzhu 7-30
	public static final int PHONE_INSERT        =3;
	public static final int EASY_MIX_CONSONANT_WRONG =4; /*易混辅音错误*/
	public static final int NOTABLE_CONSONANT_WRONG  =5;  /*显著辅音错误*/
	public static final int EASY_MIX_VOWEL_WRONG     =6; /*易混元音错误*/
	public static final int NOTABLE_VOWEL_WRONG      =7; /*显著元音错误*/
	public static final int FIRST_CONSONANT_DELETE   =8; /*重读音节首辅音删除*/
	public static final int OTHER_CONSONANT_DELETE   =9;  /*其他辅音删除*/
	public static final int VOWEL_DELETE             =10; /*元音删除*/
	
	private final static String TAG = "aiet";
	public final static String  REC_KEY = "com.iflytek.aiet.Aiet";
	public final static String  SCORE_KEY = "score";
	// Thread wait time
	private static final int TIMEOUT_WAIT_QUEUE = 2000;
	// Thread lock
	private static final ReentrantLock asrRunLock = new ReentrantLock();
	public static boolean mbisRunTreadStop = false;
	public static Handler mMsgHandler;
	static int m_nMsgVaule = -1;
	static int[] PaperLevel = new int[3];
	static TracPlayer mplayer = null;
	static Thread mRunPlayHead;
	private static int  mInputTime = 0;
	private static String mLibPath = null;
	public static void SetHandler(Handler handler) {
		mMsgHandler = handler;
	}

	// Thread for aiet engine RunSetup
	public  static void stopSetupThread(){
		mbisRunTreadStop = true;
	}

	public static void runSetupThread() {
		mbisRunTreadStop = false;
		class AietRunThread implements Runnable {
			boolean isAietRunable = false;
			int nRet = 0;
			public void run() {
				try {
					//Log.i(TAG, "AietRunThread to start");
					// 1. lock run
					isAietRunable = asrRunLock.tryLock(TIMEOUT_WAIT_QUEUE,
							TimeUnit.MILLISECONDS);
					if (!isAietRunable) {
						/*Asr.errorCallback( RecognitionResult.ASR_BUSY);*/
						Thread.sleep(100);
						return;
					}
					// 开始评测
					while (true) {
						if (mbisRunTreadStop) {
							//Log.i(TAG, "JniAietRunStep :mbisRunTreadStop ");
							break;
						}
						nRet = JniAietRunStep();
						if (mbisRunTreadStop) {
							//Log.i(TAG, "JniAietRunStep :mbisRunTreadStop ");
							break;
						}
						//Log.i(TAG,"JniAietRunStep()+nRet="+nRet);
						if(ErrorCode.ivAiETP_STARTSPEAKING == nRet){
							//Log.i(TAG,"JniAietRunStep : ivAiETP_STARTSPEAKING");
							continue;
						}
						if(ErrorCode.ivAiETP_STOPSPEAKING == nRet){
							//Log.i(TAG,"JniAietRunStep : ivAiETP_STOPSPEAKING");
							continue;
						}
						if (ErrorCode.ivAiETP_BUFFEREMPTY == nRet) {
							Thread.sleep(10);
							continue;
						}
						if (ErrorCode.ivAiETP_OK == nRet) {
							continue;
						}
						if(ErrorCode.ivAiETP_OVERTIMESPEAKING == nRet){
							//Log.i(TAG,"JniAietRunStep : ivAiETP_OVERTIMESPEAKING");
							/*onCallCommMessage(ErrorCode.ivAiETP_OVERTIMESPEAKING);*/
							continue;
						}
						if (ErrorCode.ivAiETP_RESPONSETIMEOUT == nRet) {
							onCallCommMessage(ErrorCode.ivAiETP_RESPONSETIMEOUT);
							//Log.i(TAG,"JniAietRunStep :ivAiETP_RESPONSETIMEOUT");
							break;
						}
						if (ErrorCode.ivAiETP_DONE == nRet) {
							//Log.i(TAG, "JniAietRunStep :ivAiETP_DONE");
							onCallCommMessage(ErrorCode.ivAiETP_DONE);
							break;
						}
						if (ErrorCode.ivAiETP_LOWVOICE == nRet) {
							//Log.i(TAG, "JniAietRunStep :ivAiETP_LOWVOICE ");
							continue;
						}
						if (ErrorCode.ivAiETP_HIGHVOICE == nRet) {
							//Log.i(TAG, "JniAietRunStep :ivAiETP_HIGHVOICE ");
							continue;
						}
						// 异常现象的处理
						if (ErrorCode.ivAiETP_OUTOFMEMORY == nRet||
							ErrorCode.ivAiETP_INVCAL   == nRet
					        ||ErrorCode.ivAiETP_INVRES    == nRet
					        ||ErrorCode.ivAiETP_INVOBJ    ==nRet
				          	||-1 == nRet) {
							onCallCommMessage(ErrorCode.RunStep_ERR);
							break;
						}
	    				
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					if (isAietRunable) {
						asrRunLock.unlock();
					}
					//Log.i(TAG, "AsrRunThread run End!");
					if (mbisRunTreadStop) {
						onCallCommMessage(ErrorCode.ivAiETP_USERCANCEL);
					}
				}
			}
		}

		Thread aietRun = (new Thread(new AietRunThread()));
		aietRun.setPriority(Thread.MAX_PRIORITY);
		aietRun.start();
	}

	/*** For C call java function */
	// 发送track消息
	public static int onCallTrackMessage(int msgType, int trackCount, int trackInfo[]  ){
		//double speechtime = 0.0d;
		//speechtime =  mInputTime / 320;//(帧数)
	/*	Log.d("speechtime", "speechtime =" + speechtime);
		Log.d("time", "onCallTrackMessage start \t" + getCurTime());
		Log.i(TAG,"onCallTrackMessage");*/
		Message s_msg = new Message();
		s_msg.what = msgType;
		s_msg.arg1 = trackCount;
		/*Log.i(TAG,"trackCount="+trackCount);*/
		if(trackInfo !=null ){
		   //Log.i(TAG,"trackCount="+trackInfo.length);
		}
		Bundle db =new Bundle();
		db.putIntArray("trackInfo", trackInfo);
		s_msg.setData(db);
		mMsgHandler.sendMessage(s_msg);
	/*	Log.d("time", "onCallTrackMessage end \t" +getCurTime());*/
		return 0;

	}

	// 发送result消息
	public static void  onCallRecResultMsg(int msgType,int count, int codepage, String Score, String WordID, String WordSize, String Word){
		Message s_msg = new Message();
		s_msg.what = msgType;
		Bundle db =new Bundle();
		RecResultInfo recResult =  new RecResultInfo();
		recResult.createRecInfo(count, codepage, Score, WordID, WordSize, Word);
		db.putSerializable(REC_KEY, recResult);
		s_msg.setData(db);
		mMsgHandler.sendMessageDelayed(s_msg, 0);
	}

	// 发送result消息
	public static void  onCallGetScoreMsg(int msgType, int []score){
		//Log.i(TAG, "onCallGetScoreMsg");
		Message s_msg = new Message();
		s_msg.what = msgType;
		Bundle db =new Bundle();
		db.putIntArray(SCORE_KEY, score);
		s_msg.setData(db);
		mMsgHandler.sendMessageDelayed(s_msg, 0);

	}

	//
	public static int onCallUnCommMessage(int msgType, int nValue) {
		//Log.i(TAG, "onCallUnCommMessage=" + msgType);
		/*
		 * Message s_msg = mMsgHandler.obtainMessage(msgType);
		 * mMsgHandler.sendMessageDelayed(s_msg, 0);
		 */
		Message s_msg = new Message();
		// 消息类型
		s_msg.what = msgType;
		// 消息的值
		s_msg.arg1 = nValue;
		mMsgHandler.sendMessageDelayed(s_msg, 0);
		return 0;
	}

	/*** For C call java function */
	public static int onCallCommMessage(int msgType) {
		//Log.i(TAG, "onCallCommMessage=" + msgType);
		Message s_msg = mMsgHandler.obtainMessage(msgType);
		mMsgHandler.sendMessageDelayed(s_msg, 0);
		return 0;
	}

	/** * Java native interface code */
	static {
		System.loadLibrary("AiET31");

	}

/*	public static void setLibPath(Boolean proModel){
		if(proModel){//美式
			System.loadLibrary("AiET31_AE");
		}
		else{//英式
			System.loadLibrary("AiET31_EE");
		}
	}*/

	public String getVersion() {
		int[] arrVersion = JniAietGetVersion();
		String szVersion = "版本号：" + arrVersion[0];
		return szVersion;
	}

	// 1 初始化
	public static int Init(Handler handler,boolean bProModel) {
		int ret = 0;
		//美式bProModel =true 英式 bProModel =false
		/*setLibPath(bProModel);*/
		//Log.i(TAG, "call JniAietCreate.");
		ret = JniAietCreate(bProModel);
		//Log.i(TAG, "call JniAietCreate：" + ret);
		SetHandler(handler);
		return ret;
	}

	// 2.逆初始化
	public static int Finit() {
		int ret = 0;
		//Log.i(TAG, "begin  JniAietDestroy");
		ret = JniAietDestroy();
		//Log.i(TAG, "end JniAietDestroy：" + ret);
		return ret;
	}

	// 3 开始评测
	public static int start(boolean buseGrm) {
		int ret = JniAietStart(buseGrm);
		//Log.d(TAG,"AietStart");
		mInputTime = 0;
		return ret;
	}

	// 获取引擎处理后的结果
    public static String getTestPaper(){
    	return JniAietGetTestPaper();
    }
	// 4 结束评测
	public static int stop() {
		int ret = JniAietEndAudio();
		//Log.d(TAG,"AietEndAudio");
		return ret;
	}

	public static int setParam(int nParamID, int nParamValue) {
		int ret = JniAietSetParam(nParamID, nParamValue);
		return ret;

	}

	// 设置默认的参数
	public int setAllParam(SetAietParam param) {
		int ret = 0;
		ret = setParam(AietParam.AiET_PARAM_SENSITIVITY, param.nRejectionLevel);
		assert (ret == 0);
		ret = setParam(AietParam.AiET_PARAM_AGE, param.nCurAge);
		assert (ret == 0);
		ret = setParam(AietParam.AiET_PARAM_TPLSAMPLERATE,
				param.nSecStdSamplingRate);
		assert (ret == 0);
		ret = setParam(AietParam.AiET_PARAM_RESPONSETIMEOUT, param.nTimeOut); // 默认为4秒
		assert (ret == 0);
		ret = setParam(AietParam.AiET_PARAM_SPEECHTIME, param.nSpeechTime);
		//Log.d(TAG,"nSpeechTime = "+param.nSpeechTime);
		assert (ret == 0);
		ret = setParam(AietParam.AiET_PARAM_FOLLOWSENTENCE, param.nSecWorkMode);
		assert (ret == 0);
		ret = setParam(AietParam.AiET_PARAM_TRACK, param.nArtiTrack);
		assert (ret == 0);
		ret = setParam(AietParam.AiET_PARAM_VADSILENCEWAIT,
				param.nVadSilenceWait);
		assert (ret == 0);
		ret = setParam(AietParam.AiET_PARAM_GARBAGEROLLBACK,
				param.nArtiGabageRollback);
		assert (ret == 0);
		ret = setParam(AietParam.AiET_PARAM_VAD, param.nVad);
		assert (ret == 0);
		ret = setParam(AietParam.AiET_PARAM_ENHANCEVAD, param.nEnhanceVad);
		assert (ret == 0);
		return ret;
	}

	public static int grmBegin(int nAietFunc, int nCodePage) {
		int ret = JniAietGrmBegin(nAietFunc, nCodePage);
		//Log.d(TAG,"GrmBegin");
		return ret;
	}

	public static int grmAppend(String strPape, int PaperSize) {
		int ret = JniAietGrmAppend(strPape, PaperSize);
		//Log.d(TAG,"GrmAppend");
		return ret;
	}

	public static int grmEnd() {
		int ret = JniAietGrmEnd();
		//Log.d(TAG,"GrmEnd");
		return ret;
	}

	public static int appendAudio(byte[] dataArray, int dataSize) {
		int ret = JniAietAppendAudio(dataArray, dataSize);
		//Log.d(TAG,"AppendAudio");
		mInputTime += dataSize;
		return ret;
	}

	public static int endAudio() {
		int ret = JniAietEndAudio();
		//Log.d(TAG,"EndAudio");
		return ret;
	}

	//统计单词中音素的个数//add by qunzhu
	public static int getPhoneCount(int wordIndex){
		int count =0;
	//	int phoneCount =0;
		String phone  = null;
		while(true){
			
			phone = getOralPaper(-1, wordIndex,-1, count++, PHONE_LEVEL);
			//Log.d(TAG, count + "" +phone);
			if(null == phone){
				break;
			}
		}
		return count-1;		
	}
	
	// 获取分数
	public int[] getScore() {
		int[] score = null;
		score = JniGetScore();
		return  score;
	}

	// 获取  所有的分数
	static public int[] getAllScore(int sent,int  word, int syll, int phone, int level) {
		int[] score = JniGetAllScore(sent, word, syll, phone, level);
		return  score;
	}

	// 获取跟读消息的个数
	public int getTrackCount( ) {
		int ret =0;
		ret = JniGetTrackInfoCount();
		return  ret;
	}

	// 获取跟读消息
	public int[] getTrackInf(int index ) {
		int[] track = new int[2];
		int ret = JniGetTrackInfo(index,track);
		if(ret != 1){
			return null;
		}
		return  track;
	}


	public int[] getAllScore(int[] ArrSubLevel) {
		int[] score = new int[7];
		return JniAietGetAllScore(ArrSubLevel, score);
	}

	public int[] getErrorCount() {
		return JniAietGetErrorCount();
	}

	public String getErrorData(int index) {
		String s = "";
		JniAietGetErrorData(index);
		return s;
	}

	public String getPaper() {
		int len = JniAietGetPaperLen();
		byte[] paper = new byte[len];
		JniAietGetPaper(paper, len);
		return new String(paper);
	}

	public static String getOralPaper(int sent, int word, int syll, int phone, int level){
		return JniGetOralPaper(sent, word, syll, phone, level);
	}
	

	public ErrorPhone getErrPhonePostion(int nerrorIndex) {

		ErrorPhone  ep = new ErrorPhone();
		byte[] position = new byte[7];
		int ret =JniGetErrPhonePoistion(nerrorIndex,  position);

		//Log.i(TAG,"JniGetErrPhonePoistion return:" +ret);
		if (ret != 0){
			return null;
		}
		ep.creat(position);
		return ep;
	}


	public int[] getErrWordePostion(int nerrorIndex) {
		int[] position = new int[3];
		int ret =JniGetErrWordPoistion(nerrorIndex,  position);
		return position;
	}

	public static int RunStep() {
		int ret = 0;
		ret = JniAietRunStep();
		return ret;

	}

	public String getPhone() {
		// TODO Auto-generated method stub
		String strPhone = null;
      	byte[] phone = null;
      	//Log.i(TAG, "begin getPhone");
		phone = JniAietGetPhone();
		//Log.i(TAG, "end getPhone");
		if (phone == null) {
			return strPhone;
		}
		try {
			strPhone = new String(phone, "utf-16le");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strPhone;
	}

	// 本地代码
	private native static int[] JniAietGetVersion();
	private native static int JniAietCreate(boolean bProModel);
	private native static int JniAietDestroy();
	private native static int JniAietGrmBegin(int nAietFunc, int nCodePage);

	private native static int JniAietGrmAppend(String strPaper, int nPapaerSize);
	
	private native static int JniAietGrmDelete(String strPaper, int nPapaerSize);
	
	private native static int JniAietGrmEnd();

	private native static int JniAietStart(boolean buseGrm);
	private native static  String	JniAietGetTestPaper();

/*	private native static int JniAietGetPhoneLen();
	private native static void JniAietGetPhone(byte[] arrPhone, int len);*/
	private native static byte[]JniAietGetPhone();

	private native static int JniAietAppendAudio(byte[] dataArray, int dataSize);

	private native static int JniAietEndAudio();

	private native static int JniAietRunStep();

	private native static int JniAietGetResResult(int[] arrScore);

	private native static int JniGetTrackInfoCount();
	private native static int JniGetTrackInfo(int index,int[] arrTrackInfo);

	private native static int[] JniGetScore();
	// 获取所有得分
	private native static int[] JniGetAllScore(int sent, int word, int syll, int phone, int level);

	private native static int[] JniAietGetAllScore(int[] ArrSubLevel,
			int[] score);

	private native static int[]  JniAietGetErrorCount();

	private native static int JniAietGetErrorData(int nerrorIndex);

	private native static boolean JniAietGetPaper(byte[] arrPaper, int len);

	private native int JniAietGetPaperLen();

	private native static String  JniGetOralPaper(int sent, int word, int syll, int phone, int level);
	
    //add by qunzhu
	/*private native int JniAietGetOralPaperLen(int sent, int word, int syll, int phone, int level);
*/
	// 获取错误的信息
	private native static int JniGetErrPhonePoistion(int nErrorIndex,
			byte[] posion);
	private native static int JniGetErrWordPoistion(int nErrorIndex,
			int[] posion);
	private native static int JniAietSetParam(int nParamID, int nParamValue);

	// //////////// 音频播放函数
	// //////////// 音频播放函数
	public static void playSound(final String wavPath) {
		// 播放音频
		class playSoundThread implements Runnable {
			private String mwavPath;

			public void setPalyPath(String path) {
				mwavPath = path;
			}
		@Override
			public void run() {
				mplayer = new TracPlayer();
				//Log.i(TAG, "run");
				mplayer.Play(mwavPath);
				if (!mplayer.mIsPlaying) {
					//Log.i(TAG, "mplayer.mIsPlaying");
					Message msg = mMsgHandler
							.obtainMessage(AietMsgID.AiET_MSG_STOPPLAY);
					mMsgHandler.sendMessageDelayed(msg, 0);
				}
			}
		}
		playSoundThread playThead = new playSoundThread();
		playThead.setPalyPath(wavPath);
		mRunPlayHead = new Thread(playThead);
		mRunPlayHead.start();
	}


	public static void stopPlaySound() {
		mplayer.Stop();
		mRunPlayHead = null;
	}

	// 评测错误时的提示 信息
	public static void errorMsg(Context ct, int lastErrorId) {
		String msg = "评测错误";

		switch (lastErrorId) {
		case ErrorCode.RunStep_ERR: // Audio recording error.
			msg = ct.getString(R.string.res_error);
			break;
		case ErrorCode.ivAiETP_RESPONSETIMEOUT: // No recognition result// matched.
			msg = ct.getString(R.string.res_response_timeout);
			break;
		case ErrorCode.ivAiETP_REJECT: // User response timeout.
			msg = ct.getString(R.string.res_reject);
			break;
		case ErrorCode.ivAiETP_TEMP_ERR:
		     msg = ct.getString(R.string.res_temp_error)+",请停止评测";
		     break;
		case ErrorCode.ivAiETP_TEMP_WARNING:
			/*msg = ct.getString(R.string.res_temp_warring)+",是否接着评测？";*/
			msg = ct.getString(R.string.res_temp_warring);
			break;
		case ErrorCode.ivAiETP_START_WRONG:
			msg = ct.getString(R.string.res_start_wrong);
			break;
		case ErrorCode.ivAiETP_GETPHONE_WRONG:
		    msg = ct.getString(R.string.res_getphone_wrong);
		    break;
		case ErrorCode.ivAiETP_OVERTIMESPEAKING:
			msg = ct.getString(R.string.res_overtime_speaking);
			break;

		default:
			break;
		}
		System.err.println(msg);
//		Builder builder = new AlertDialog.Builder(ct)
//		.setTitle(ct.getString(R.string.msg_title))
//		.setMessage(msg /*ct.getString(R.string. msg_ok_to_repea )*/)
//		.setIcon(R.drawable.ico_mic_retry)
//		.setNegativeButton(ct.getString(R.string.msg_ok),
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//					//
//					}
//				});
//		AlertDialog  dialog = builder.create();
//		dialog.show();
	}

	public static String  getCurTime(){
   	     long currentTimeMillis = System.currentTimeMillis();
		 Date date = new Date(currentTimeMillis);
    	 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
    	 String time = simpleDateFormat.format(date);
    	 System.out.println(time) ;
    	 return time;
	}

	public static int grmDelete(String strPape, int PaperSize) {
		// TODO Auto-generated method stub
			int ret = JniAietGrmDelete(strPape, PaperSize);
			//Log.d(TAG,"GrmDelete");
			return ret;
	}
}


