package com.iflytek.common;

import com.iflytek.aiet.AietParam;
import com.iflytek.aiet.SetAietParam;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class GetParam {
	// 这个参数很重要
	private Activity activity;
	private SharedPreferences msharedPreferences;

	public final static int WORD = 0;
	public final static int SENTENCE = 1;
	public final static int PAPER = 2;
	public final static int REC= 3;



	public GetParam(Activity act) {
		// TODO Auto-generated constructor stub
		activity = act;
	}

	public SetAietParam getParam(int ntype) {
		String mdataSetname = null;

		switch (ntype) {
		case WORD://word
			mdataSetname = "aiet31param_word";
			msharedPreferences = activity.getSharedPreferences(mdataSetname,
					Context.MODE_PRIVATE);
			break;
		case SENTENCE:
			mdataSetname = "aiet31param_sentence";
			msharedPreferences = activity.getSharedPreferences(mdataSetname,
					Context.MODE_PRIVATE);
			break;
		case PAPER:
			mdataSetname = "aiet31param_Article";
			msharedPreferences = activity.getSharedPreferences(mdataSetname,
					Context.MODE_PRIVATE);
			break;
		case REC:
			mdataSetname = "aiet31param_Rec";
			msharedPreferences = activity.getSharedPreferences(mdataSetname,
					Context.MODE_PRIVATE);
			break;
		default:
			break;
		}
		SetAietParam ss = new SetAietParam();
		String module = msharedPreferences.getString("发音模式", "英式");
		if (module.equals("美式")) {
			ss.bPronounce = true;
		} else {
			ss.bPronounce = false;
		}
		module = msharedPreferences.getString("年龄", "成人");
		if (module.equals("成人")) {
			ss.nCurAge = AietParam.AGE_ADULT;
		} else {
			ss.nCurAge = AietParam.AGE_CHILD;
		}
		
		//add by qunzhu 8-8
		 module = msharedPreferences.getString("音素增读", "50%");
		 module = module.replace("%", "");
		 ss.phone_insert = Integer.parseInt(module);
		 
		 module = msharedPreferences.getString("易混辅音错误", "30%");
		 module = module.replace("%", ""); 
		 ss.easy_mix_consonany_wrong = Integer.parseInt(module);
		 
		 module = msharedPreferences.getString("显著辅音错误", "150%");
		 module = module.replace("%", ""); 
		 ss.notable_consonant_wrong = Integer.parseInt(module);
		 
		 module = msharedPreferences.getString("易混元音错误", "30%");
		 module = module.replace("%", ""); 
		 ss.easy_mix_vowel_wrong = Integer.parseInt(module);
		 
		 module = msharedPreferences.getString("显著元音错误", "200%");	
		 module = module.replace("%", "");
		 ss.notable_vowel_wrong =  Integer.parseInt(module);
		 
		 module = msharedPreferences.getString("重读音节首辅音删除", "150%");
		 module = module.replace("%", "");
		 ss.first_consonant_delete =  Integer.parseInt(module);
		 
		 module = msharedPreferences.getString("其他辅音删除", "40%");
		 module = module.replace("%", "");
		 ss.other_consonant_delete =  Integer.parseInt(module);
		 
		 module = msharedPreferences.getString("元音删除", "200%");
		 module = module.replace("%", "");
		 ss.vowel_delete =  Integer.parseInt(module);
		 
		 module = msharedPreferences.getString("判断对错的阈值", "25%");
		 module = module.replace("%", "");
		 ss.threshold = Integer.parseInt(module);
         //////////////////////////////
		 
		module = msharedPreferences.getString("反应超时", "4秒");
		module = module.replace("秒", "");
		ss.nTimeOut = Integer.parseInt(module);

		module = msharedPreferences.getString("最大录音时间", "120秒");
		module = module.replace("秒", "");
		ss.nSpeechTime = Integer.parseInt(module);


		module = msharedPreferences.getString("拒识灵敏度", "比较灵敏");
		if (module.equals("不灵敏")) {
			ss.nRejectionLevel = AietParam.SENSITIVITY_ACTIVE;
		} else if (module.equals("比较灵敏")) {
			ss.nRejectionLevel = AietParam.SENSITIVITY_NORMAL;
		} else if (module.equals("灵敏")) {
			ss.nRejectionLevel = AietParam.SENSITIVITY_PRECISE;
		} else {
			ss.nRejectionLevel = AietParam.SENSITIVITY_STRICT;
		}
		
		module = msharedPreferences.getString("VAD等待时间", "500毫秒");
		module = module.replace("毫秒", "");
		ss.nVadSilenceWait = Integer.parseInt(module);

		module = msharedPreferences.getString("句子评测工作模式", "非跟读");
		if (module.equals("跟读")) {
			ss.nSecWorkMode = AietParam.WORKMODEL_USETEMP;
		} else {
			ss.nSecWorkMode = AietParam.WORKMODEL_NOUSETEMP;
		}

		module = msharedPreferences.getString("带读语音采样率", "16K");
		if (module.equals("16K")) {
			ss.nSecStdSamplingRate = AietParam.SAMPLE_RATE_16K;
		} else if (module.equals("22K")) {
			ss.nSecStdSamplingRate = AietParam.SAMPLE_RATE_22K;
		} else if (module.equals("32K")) {
			ss.nSecStdSamplingRate = AietParam.SAMPLE_RATE_32K;
		} else {
			ss.nSecStdSamplingRate = AietParam.SAMPLE_RATE_44K;
		}
		module = msharedPreferences.getString("VAD", "关闭");
		if (module.equals("开启")) {
			ss.nVad = AietParam.VAD_ON;
		} else {
			ss.nVad = AietParam.VAD_OFF;
		}
		module = msharedPreferences.getString("增强效果VAD", "开启");
		if (module.equals("开启")) {
			ss.nEnhanceVad = AietParam.ENHANCEVAD_ON;
		} else {
			ss.nEnhanceVad  = AietParam.ENHANCEVAD_OFF;
		}

		module = msharedPreferences.getString("保证音频", "开启");
		if (module.equals("开启")) {
			ss.bSaveAudio= true;
		} else {
			ss.bSaveAudio  =false;
		}
		module = msharedPreferences.getString("自动跟踪垃圾回滚设置", "关闭");
		if (module.equals("关闭")) {
			ss.nArtiGabageRollback = AietParam.GARBAGEROLLBACK_OFF;
		} else {
			ss.nArtiGabageRollback =  AietParam.GARBAGEROLLBACK_ON;
		}
		module = msharedPreferences.getString("自动跟踪参数设置", "打开自动跟踪松模式");
		if (module.equals("打开自动跟踪松模式")) {
			ss.nArtiTrack =  AietParam.TRACK_ON_EASY;
		} else if (module.equals("打开自动跟踪严模式")) {
			ss.nArtiTrack = AietParam.TRACK_ON_HARD;
		} else {
			ss.nArtiTrack = AietParam.TRACK_OFF;
		}
		return ss;
	}
}
