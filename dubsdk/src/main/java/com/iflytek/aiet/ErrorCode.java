package com.iflytek.aiet;

public class ErrorCode {

	public static final int   RunStep_ERR = 0x70000001;              // 引擎处理错误
	public static final int   AUDIO_ERROR                            =0x70000000;
	public static final int   SUCCEEDED_FLAG                         =0x80000000;
	/* ivAiETP_OK */
	public static final int   AiETPErrID_OK                          =0;
	;public static final int  AiETPErrID_FALSE                       =1;

	/* Error Code */
	public static final int  AiETPErr_InvCal                         =0x80000001;
	public static final int  AiETPErr_InvObj                         =0x80000002;
	public static final int  AiETPErr_InvArg                         =0x80000003;
	public static final int  AiETPErr_OutOfMemory                    =0x80000004;
	public static final int  AiETPErr_RamLen                         =0x80000005;
	public static final int  AiETPErr_ReEnter                        =0x80000006;
	public static final int  AiETPErr_InvRes                         =0x80000007;
	public static final int  AiETPErr_InvSentIndex                   =0x80000008;
	public static final int  AiETPErr_InvWordIndex                   =0x80000009;
	public static final int  AiETPErr_InvPhoneIndex                  =0x8000000A;
	public static final int  AiETPErr_ParamNotSupport                =0x8000000B;
	public static final int  AiETPErr_TempErr                        =0x8000000C;
	public static final int  AiETPErr_InvSN                          =0x8000000D;
	public static final int  AiETPErr_Failed                         =0x8000000E;
	public static final int  AiETPErr_Reject                         =0x8000000F;

	/* Warning Code */
	public static final int  AiETPErr_BufferFull                     =0x00000011;
	public static final int  AiETPErr_Result                         =0x00000012;
	public static final int  AiETPErr_BufferEmpty                    =0x00000013;
	public static final int  AiETPErr_SizeCalculated                 =0x00000014;
	public static final int  AiETPErr_StartSpeaking                  =0x00000015;
	public static final int  AiETPErr_StopSpeaking                   =0x00000016;
	public static final int  AiETPErr_OverTimeSpeaking               =0x00000017;
	public static final int  AiETPErr_ResponseTimeOut                =0x00000018;
	public static final int  AiETPErr_ResultNone                     =0x00000019;
	public static final int  AiETPErr_HighVoice                      =0x0000001A;
	public static final int  AiETPErr_LowVoice                       =0x0000001B;
	public static final int  AiETPErr_TooSlowly                      =0x0000001C;
	public static final int  AiETPErr_TooNoisy                       =0x0000001D;
	public static final int  AiETPErr_TextWarning                    =0x0000001E;
	public static final int  AiETPErr_ResetSentFail                  =0x0000001F;
	public static final int  AiETPErr_TempDone                       =0x00000020;
	public static final int  AiETPErr_TempWarning                    =0x00000021;
	public static final int  AiETPErr_Done                           =0x00000022;
	public static final int  ivAiETP_START_WRONG                     =0x00000023;
	public static final int  ivAiETP_GETPHONE_WRONG                  =0x00000024;
	
	public static final int  ivAiETP_UserCancel		                  =0x00000025;

	public static final int  ivAiETP_OK                              =AiETPErrID_OK;
	public static final int  ivAiETP_FALSE                           =AiETPErrID_FALSE;
	//add by qunzhu
	public static final int  ivAiET_OK                               = AiETPErrID_OK;
	public static final int  ivAiET_FALSE                            = AiETPErrID_FALSE;

	public static final int ivAiETP_INVCAL                           =AiETPErr_InvCal;
	public static final int ivAiETP_INVOBJ                           =AiETPErr_InvObj;
	public static final int ivAiETP_INVARG                           =AiETPErr_InvArg;
	public static final int ivAiETP_OUTOFMEMORY                      =AiETPErr_OutOfMemory;
	public static final int ivAiETP_RAMLEN                           =AiETPErr_RamLen;
	public static final int ivAiETP_INVRES                           =AiETPErr_InvRes;
	public static final int ivAiETP_INVSENTINDEX                     =AiETPErr_InvSentIndex;
	public static final int ivAiETP_INVWORDINDEX                     =AiETPErr_InvWordIndex;
	public static final int ivAiETP_INVPHONEINDEX                    =AiETPErr_InvPhoneIndex;
	public static final int ivAiETP_REENTER                          =AiETPErr_ReEnter;
	public static final int ivAiETP_NOT_SUPPORT                      =AiETPErr_ParamNotSupport;
	public static final int ivAiETP_TEMP_ERR                         =AiETPErr_TempErr;
	public static final int ivAiETP_INVSN                            =AiETPErr_InvSN;
	public static final int ivAiETP_FAILED                           =AiETPErr_Failed;
	public static final int ivAiETP_REJECT                           =AiETPErr_Reject;

	/* ivAiETP Warning Status return */
	public static final int ivAiETP_BUFFERFULL                       =AiETPErr_BufferFull;
	public static final int ivAiETP_RESULT                           =AiETPErr_Result;
	public static final int ivAiETP_BUFFEREMPTY                      =AiETPErr_BufferEmpty;
	public static final int ivAiETP_SIZECALC                         =AiETPErr_SizeCalculated;
	public static final int ivAiETP_SIZECALCULATED                   =ivAiETP_SIZECALC;
	public static final int ivAiETP_STARTSPEAKING                    =AiETPErr_StartSpeaking;
	public static final int ivAiETP_STOPSPEAKING                     =AiETPErr_StopSpeaking;
	public static final int ivAiETP_OVERTIMESPEAKING                 =AiETPErr_OverTimeSpeaking;
	public static final int ivAiETP_RESPONSETIMEOUT                  =AiETPErr_ResponseTimeOut;
	public static final int ivAiETP_NONE                             =AiETPErr_ResultNone;
	public static final int ivAiETP_HIGHVOICE                        =AiETPErr_HighVoice;
	public static final int ivAiETP_LOWVOICE                         =AiETPErr_LowVoice;
	public static final int ivAiETP_TOOSLOWLY                        =AiETPErr_TooSlowly;
	public static final int ivAiETP_TOONOISY                         =AiETPErr_TooNoisy;
	public static final int ivAiETP_TEXTWARNING                      =AiETPErr_TextWarning;
	public static final int ivAiETP_RESETSENTFAIL                    =AiETPErr_ResetSentFail;
	public static final int ivAiETP_TEMP_DONE                        =AiETPErr_TempDone;
	public static final int ivAiETP_TEMP_WARNING                     =AiETPErr_TempWarning;
	public static final int ivAiETP_DONE                             =AiETPErr_Done;


	public static final int ivAiETP_USERCANCEL						=ivAiETP_UserCancel;
}
