package com.iflytek.aiet;

import android.R.integer;

//aiet31  Message define
public class AietMsgID {
	public static final int  GBK  = 0;
	public static final int  UTF8 = 1;
	public static final int  UTF16= 2;
	
//	/*灵活命令词识别*/
//	public static final int First_Word = 1;
//	public static final int Second_Word = 2;
//	public static final int Third_Word = 3;
//	public static final int Fourth_Word = 4;
//	public static final int Fifth_word = 5;
//	public static final int Sixth_word = 6;

	/* Tracking Information */
	public static final int AiET_MSG_STOPPLAY           = 0x200;
	public static final int AiET_MSG_TRACK             = 0x101;

	/* OS Schedule an Cooperation */
	public static final int AiET_MSG_SLEEP             = 0x102;

	/* Rec Result */
	public static final int AiET_MSG_REC               = 0x103;

	/* EVA Result */
	public static final int AiET_MSG_EVA               = 0x104;
	// 评测的类型
	    public static final int READ_CHAPTER           = 0x0;
	    public static final int READ_SENTENCE          = 0x1;
	    public static final int FOLLOW_SENTENCE        = 0x2;
	    public static final int READ_WORD              = 0x3;
	    public static final int FUZZY_CMD_REC          = 0x4;
	    public static final int COMMOND_CMD_REC        = 0x5;
	    //added by qunzhu 新增灵活命令词题型
	    public static final int FLEXIBLE_CMD_REC       = 0X6;
	    

	/* LOG mechanism */
	public static final int AiET_MSG_LOG               = 0x105;

	/* VAD Status */
	public static final int AiET_MSG_VAD               = 0x106;
	    public static final int ivSpeakingStart        = 0x0;
	    public static final int ivSpeakingStop         = 0x1;
	    public static final int ivSpeakingOverTime     = 0x2;
	    public static final int ivResponseTimeOut      = 0x3;

	/* Template Processed Information  */
	public static final int AiET_MSG_TEMPLATE          = 0x107;
	    public static final int ivTemplateDone         = 0x0;
	    public static final int ivTemplateWarning      = 0x1;
	    public static final int ivTemplateError        = 0x2;
    // 如果模板音处理反馈错误，请及时处理

	/* Warning Information */
	public static final int AiET_MSG_WARNING           = 0x108;
	    public static final int ivLowVoice             = 0x0;
	    public static final int ivHighVoice            = 0x1;
	    public static final int ivBufferEmpty          = 0x2;

	/* Error information */
	public static final int AiET_MSG_ERROR             = 0xffffffff;
	    public static final int ivInvalidObject        = 0x80000001;
	    public static final int ivReEnter              = 0x80000002;
	    public static final int ivOutOfMemory          = 0x80000003;

   public static final int AiET_MSG_RESERVE            = 0x0;


}
