package com.iflytek.common;

import java.io.Serializable;
import android.graphics.Color;
import com.iflytek.common.BackgroundColorSpan;
import com.iflytek.common.ForegroundColorSpan;


public class AddFont implements Serializable ,Cloneable{
	private static final long serialVersionUID = -7060210544600464331L;
	public ForegroundColorSpan foreColor;
	public BackgroundColorSpan backColor;

	public int    wordIndex;
	public int    realIndex;
	public int    len;
	public int    type;
	public int    endIndex ;


	public AddFont(){
		foreColor = new ForegroundColorSpan(Color.RED);
		backColor = new BackgroundColorSpan(Color.TRANSPARENT);
		wordIndex =-1;
		realIndex =-1;
		type = -1;
		len  = -1;
		endIndex = -1;

	}

	public ForegroundColorSpan getForeColor(){
		return foreColor;
	}
	public BackgroundColorSpan getBackColor(){
		return backColor;
	}
	public int getWordIndex(){
		return wordIndex;
	}
	public int getRealIndex(){
		return realIndex;
	}

//	public void setForeColor(ForegroundColorSpan color){
//		 foreColor = color;
//	}
//	public void setBackColor(BackgroundColorSpan color){
//		 backColor = color;
//	}
	public void  setWordIndex(int indedx){
		wordIndex = indedx;
	}
	public void  setRealIndex(int index){
		 realIndex =index;
	}
}
