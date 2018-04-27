package com.iflytek.aiet;

public class AietParam {
	public static final int AiET_PARAM_DEFAULT          =  0;       /* Set All Param to Default */
	public static final int AiET_PARAM_SENSITIVITY      =  1;       /* Set Refuse Sensitivity */
	    public static final int SENSITIVITY_ACTIVE      =  0;
	    public static final int SENSITIVITY_NORMAL      =  1;
	    public static final int SENSITIVITY_PRECISE     =  2;
	    public static final int SENSITIVITY_STRICT      =  3;
	    public static final int SENSITIVITY_DEFAULT     = SENSITIVITY_NORMAL ; /* Set Default(Normal; */

	public static final int AiET_PARAM_AGE              =  2;       /* Set Adult or Child */
	    public static final int AGE_ADULT               =  0;
	    public static final int AGE_CHILD               =  1;        /* Set Child */
	    public static final int AGE_DEFAULT             = AGE_ADULT   ;        /* Set Default(Adult; */
	public static final int AiET_PARAM_TPLAGE           =  3;       /* Set Template Adult or Child */
	    public static final int TPLAGE_ADULT            = AGE_ADULT ;
	    public static final int TPLAGE_CHILD            = AGE_CHILD   ;        /* Set Child */
	    public static final int TPLAGE_DEFAULT          = TPLAGE_ADULT   ;     /* Set Template Default(Adult; */
	public static final int AiET_PARAM_SAMPLERATE       =  4;       /* Set Sample Rate */
	    public static final int SAMPLE_RATE_16K         =  1;
	    public static final int SAMPLE_RATE_22K         =  2;
	    public static final int SAMPLE_RATE_32K         =  3;
	    public static final int SAMPLE_RATE_44K         =  4;
	public static final int AiET_PARAM_TPLSAMPLERATE    =  5;       /* Set Template Sample Rate */
	    public static final int TPLSAMPLE_RATE_16K     = SAMPLE_RATE_16K;
	    public static final int TPLSAMPLE_RATE_22K     = SAMPLE_RATE_22K;
	    public static final int TPLSAMPLE_RATE_32K     = SAMPLE_RATE_32K;
	    public static final int TPLSAMPLE_RATE_44K     = SAMPLE_RATE_44K;

	public static final int AiET_PARAM_VAD              =  6;
	    public static final int VAD_ON                  =  0;
	    public static final int VAD_OFF                 =  1;
	    public static final int VAD_DEFAULT            = VAD_ON;

	public static final int AiET_PARAM_ENHANCEVAD       =  7;
	    public static final int ENHANCEVAD_ON           =  0;
	    public static final int ENHANCEVAD_OFF          =  1;
	    public static final int ENHANCEVAD_DEFAULT      = ENHANCEVAD_ON;

	public static final int AiET_PARAM_RESPONSETIMEOUT  =  8;       /* Set Response Time */
	    public static final int RESPONSETIMEOUT_OFF     =  -1;       /* Not Use RESPONSETIMEOUT */
	    public static final int RESPONSETIMEOUT_DEFAULT =  0;        /* Set Default(4 seconds; */

	public static final int AiET_PARAM_FOLLOWSENTENCE   =  9;
	    public static final int FOLLOWSENTENCE_OFF      =  0;        /* Not Use Template */
	    public static final int FOLLOWSENTENCE_ON       =  1;        /* Use Template  */
	    public static final int FOLLOWSENTENCE_DEFAULT = FOLLOWSENTENCE_OFF ; /* Set Default: Not Use Template */

	public static final int AiET_PARAM_SPEECHTIME       =  10;      /* Set Speech Time */
	    public static final int SPEECHTIME_DEFAULT      =  0;        /* Set default(6 seconds; */

	public static final int AiET_PARAM_TRACK            =  11;      /* Set Input Speech Type */
	    public static final int TRACK_ON_EASY           =  0;
	    public static final int TRACK_ON_HARD           =  1;
	    public static final int TRACK_OFF               =  -1;
	    public static final int TRACK_DEFAULT           =TRACK_OFF;

	public static final int AiET_PARAM_VADSILENCEWAIT   =  12;      /* Set Input Speech Type */
	    public static final int SILENCEWAIT_DEFAULT     =  0;

	public static final int AiET_PARAM_GARBAGEROLLBACK  =  13;      /* Set Input Speech Type */
	    public static final int GARBAGEROLLBACK_ON      =  0;
	    public static final int GARBAGEROLLBACK_OFF     =  -1;
	    public static final int GARBAGEROLLBACK_DEFAULT =GARBAGEROLLBACK_OFF;

	public static final int AiET_PARAM_DISCARD          =  14;      /* Set Input Speech Type */
	    public static final int DISCARD_DEFAULT         =  0;


	/* 兼容老参数设置 */
	public static final int AiETP_PARAM_DEFAULT         =AiET_PARAM_DEFAULT;

	public static final int AiETP_PARAM_SENSITIVITY     =AiET_PARAM_SENSITIVITY ;

	public static final int AiETP_PARAM_AGE             =AiET_PARAM_AGE;

	public static final int AiETP_PARAM_TEMPAGE         =AiET_PARAM_TPLAGE;
	    public static final int TEMPAGE_ADULT           =TPLAGE_ADULT  ;
	    public static final int TEMPAGE_CHILD           =TPLAGE_CHILD ;    /* Set Child */
	    public static final int TEMPAGE_DEFAULT         =TPLAGE_ADULT ;          /* Set Template Default(Adult; */

	    public static final int AiETP_PARAM_SAMPLERATE      =AiET_PARAM_SAMPLERATE;

	public static final int AiETP_PARAM_TEMPSAMPLERATE  =AiET_PARAM_TPLSAMPLERATE;
	    public static final int TEMPSAMPLE_RATE_16K     =TPLSAMPLE_RATE_16K;
	    public static final int TEMPSAMPLE_RATE_22K     =TPLSAMPLE_RATE_22K;
	    public static final int TEMPSAMPLE_RATE_32K     = TPLSAMPLE_RATE_32K;
	    public static final int TEMPSAMPLE_RATE_44K     =TPLSAMPLE_RATE_44K   ;

	public static final int AiETP_PARAM_SPEECHDETECT    =AiET_PARAM_VAD ;   /* Set Speech Detect */
	    public static final int SPEECHDETECT_ON         =VAD_ON     ;
	    public static final int SPEECHDETECT_OFF        =VAD_OFF;
	    public static final int SPEECHDETECT_DEFAULT    =SPEECHDETECT_ON     ;/* Set Default(On; */

	    public static final int AiETP_PARAM_RESPONSETIMEOUT =AiET_PARAM_RESPONSETIMEOUT;

	public static final int AiETP_PARAM_WORKMODEL       =AiET_PARAM_FOLLOWSENTENCE   ; /* Set Input Speech Type */
	    public static final int WORKMODEL_NOUSETEMP     =FOLLOWSENTENCE_OFF  ;   /* Not Use Template */
	    public static final int WORKMODEL_USETEMP       =FOLLOWSENTENCE_ON  ;   /* Use Template  */
	    public static final int WORKMODEL_DEFAULT       =WORKMODEL_NOUSETEMP  ;   /* Set Default: Not Use Template */

}
