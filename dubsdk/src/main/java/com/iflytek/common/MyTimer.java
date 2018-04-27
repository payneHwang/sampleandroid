package com.iflytek.common;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import android.os.Message;

public class MyTimer /*extends Timer */{
	
	public static final int CHANGE_TIME_         = 3000; // 
	public static final int Ringht_TIME_         = 500;  //正确时图片显示的时间
	public static final int ivDO_Change_Word = 0x109;
	public static final int ivDo_Change_Black_Color = 0x1011;/*黑色*/
	public static final int ivDo_Change_Dkgray_Color = 0x1012; /*灰黑色*/
	public static final int ivDo_Change_Gray_Color   = 0x1013; /*灰色*/
	public static final int ivDo_Ringht_   = 0x1014; //换图片
	public static final int ivDo_TimeOut = 0x1015;
	
	private  int  changeCount = 1; 
	private Timer  mtimer;
	public int  mtimerID;

	long mdelay; 
	long mperiod;
	private static Handler mMsgHandler;
	public  MyTimer() {
		// TODO Auto-generated method stub
		mtimerID = 0;
		mMsgHandler = null;
		changeCount = 1;
	}
	
	
	public  MyTimer(int timeID) {
		// TODO Auto-generated method stub
		mtimerID = timeID;
	}
	
	public static void setHandler(Handler handler) {
		mMsgHandler = handler;
	}
	public void setSchedule(long delay, long period) {
		// TODO Auto-generated method stub
	     mdelay  = delay; 
		 mperiod = period;
	}
	
	public void start() {
		// TODO Auto-generated method stub
		 mtimer = new Timer();
		 TimerTask task = new TimerTask() {
				@Override
				public void run() {
				/*if (mperiod == Ringht_TIME_) {
					Message message = new Message();
					message.what = ivDo_Ringht_;
					message.arg1 = mtimerID;
					mMsgHandler.sendMessage(message);
					return;
				}*/

					// TODO Auto-generated method stub
					if(changeCount%4 == 3){
						   Message message = new Message();
				           message.what = ivDo_Change_Black_Color;
				           message.arg1 = mtimerID;
				           mMsgHandler.sendMessage(message);  
					}else if(changeCount%4 == 2){
						   Message message = new Message();
				           message.what = ivDo_Change_Dkgray_Color;
				           message.arg1 = mtimerID;
				           mMsgHandler.sendMessage(message);  
					}else if(changeCount%4 == 1){
						   Message message = new Message();
				           message.what = ivDo_Change_Gray_Color;
				           message.arg1 = mtimerID;
				           mMsgHandler.sendMessage(message);  
					}else{
						   Message message = new Message();
				           message.what = ivDO_Change_Word;
				           message.arg1 = mtimerID;
				           mMsgHandler.sendMessage(message);  
					}
					changeCount++;    	
				}
			} ;
			mtimer.schedule(task, mdelay, mperiod);
	}
	public void start2() {
		// TODO Auto-generated method stub
		 mtimer = new Timer();
		 TimerTask task = new TimerTask() {
				@Override
				public void run() {					
				   Message message = new Message();
		           message.what = ivDo_TimeOut;
		           message.arg1 = mtimerID;
		           mMsgHandler.sendMessage(message);  	  	
				}
			} ;
			mtimer.schedule(task, mdelay, mperiod);
	}
	public void reset() {
		// TODO Auto-generated method stub
		if(mtimer != null){
			mtimer.cancel();
			mtimer = null;
		}
		changeCount = 1;
		
	}

	public void stop() {
		// TODO Auto-generated method stub
		if(mtimer != null){
			mtimer.cancel();
			mtimer = null;	
		}
		
	}


}
