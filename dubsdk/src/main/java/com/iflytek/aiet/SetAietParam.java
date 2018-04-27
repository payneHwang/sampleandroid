package com.iflytek.aiet;

public class SetAietParam {

	public boolean bPronounce = true; // 发音模式{true：英式;else:美式}
	public int nCurAge = 0;     // 年龄{true:成人；false：儿童}
	public int nTimeOut = 0;    // 反应超时
	public int nSpeechTime = 0; // 最大的录音时间(默认6s)
	public int nRejectionLevel = 1; // 拒识灵敏度(默认设置)
	public int nVadSilenceWait = 0; // Vad末尾位置静音等待(默认500ms)

	public int nVad = 0;            // Vad默认开启0 1--关闭
	public int nEnhanceVad =0;      // 增强效果的语音状态检测

	public int nSecWorkMode = 0; // 句子评测工作模式{true:跟读；false:非跟读}
	public int nSecStdSamplingRate = 1; // 句子带读音的采样率{1-16K 2-22 3-32
	// 4-44}
	public int nArtiGabageRollback = -1; // 篇章自动跟踪垃圾回滚参数
	public int nArtiTrack = 0; // 篇章的自动跟踪参数（默认设置 ）
	public boolean bSaveAudio = true; // 是要保存语音（默认设置 ）
	
	
	public int phone_insert = 0;
	public int easy_mix_consonany_wrong = 0; /*易混辅音错误*/
	public int notable_consonant_wrong = 0;  /*显著辅音错误*/
	public int easy_mix_vowel_wrong = 0; /*易混元音错误*/
	public int notable_vowel_wrong =0; /*显著元音错误*/
	public int first_consonant_delete = 0;/*重读音节首辅音删除*/
	public int other_consonant_delete = 0;  /*其他辅音删除*/
	public int vowel_delete =0; /*元音删除*/
	public int threshold = 0;
	
	

	// 默认的参数设置
	public void SetDefalute() {
		bPronounce = true; // 发音模式{true：英式;else:美式}
		nCurAge = 0; // 年龄{true:成人；false：儿童}
		nTimeOut = 0; // 反应超时
		nSpeechTime = 0; // 最大的录音时间(默认6s)
		nRejectionLevel = 1; // 拒识灵敏度(默认设置)
		nVadSilenceWait = 0; // Vad末尾位置静音等待(默认500ms)

		nSecWorkMode = 0; // 句子评测工作模式{true:跟读；false:非跟读}
		nSecStdSamplingRate = 1; // 句子带读音的采样率{1-16K 2-22 3-32 4-44}
		nArtiGabageRollback = -1; // 篇章自动跟踪垃圾回滚参数
		nArtiTrack = 0; // 篇章的自动跟踪参数（默认设置 ）
		nVad = 0;
		nEnhanceVad =0;
		bSaveAudio = true;
		//add by qunzhu 8-8
		phone_insert = 50;
		easy_mix_consonany_wrong = 30; /*易混辅音错误*/
		notable_consonant_wrong = 150;  /*显著辅音错误*/
		easy_mix_vowel_wrong = 50; /*易混元音错误*/
		notable_vowel_wrong =200; /*显著元音错误*/
		first_consonant_delete = 150;/*重读音节首辅音删除*/
		other_consonant_delete = 40;  /*其他辅音删除*/
		vowel_delete =200; /*元音删除*/
		threshold = 25;
	}
}
