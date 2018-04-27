package com.iflytek.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.util.Log;

import com.iflytek.aiet.Aiet;


//public class ParserErrorWord{
//	
//	private static final String TAG = "ParserErrorWord";
//
//	// 获取错误的单词
//			public JSONArray parserErrorWord(int errorCount ,String mCurText,boolean mbreject) {
//				Log.i(TAG,"errorCount ="+errorCount) ;
//				if(errorCount == 0 && mbreject){
////					Log.i(TAG,"errorCount =0 ;mbreject="+mbreject) ;
////					TvText.setText(mCurText,TextView.BufferType.SPANNABLE);
////					spanForTextView = (Spannable)TvText.getText();
////				    Object foreColor=null;
////				    foreColor = new ForegroundColorSpan(Color.RED); ;
////					spanForTextView.setSpan(foreColor, 0,mCurText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//				    return null;
//				}
//				StringBuffer text = new StringBuffer(mCurText);
//				int increaseRead = 0;
//				AddFont[] addFonts = new AddFont[errorCount];
//				int realError = 0;
//				Map<Integer, LinkedList<Integer>> errorInfo = new HashMap<Integer, LinkedList<Integer>>(20);
//				for(int errorIndex = 0; errorIndex < errorCount; errorIndex++){
//				    int [] position = mAiet.getErrWordePostion(errorIndex);
//					if(position ==null || position[2]== Aiet.PHONE_INSERT 
//							|| position[2] == Aiet.EASY_MIX_CONSONANT_WRONG /*易混辅音错误*/
//		                    || position[2] == Aiet.NOTABLE_CONSONANT_WRONG /*显著辅音错误*/
//		                    || position[2] == Aiet.EASY_MIX_VOWEL_WRONG /*易混元音错误*/
//		                    || position[2] == Aiet.NOTABLE_VOWEL_WRONG /*显著元音错误*/
//		                    || position[2] == Aiet.FIRST_CONSONANT_DELETE /*重读音节首辅音删除*/
//		                    || position[2] == Aiet.OTHER_CONSONANT_DELETE    /*其他辅音删除*/
//		                    || position[2] == Aiet.VOWEL_DELETE              /*元音删除*/ 
//							/*position[2]== Aiet.PHONE_WRONG*/){
//						Log.i(TAG," poisition is error");
//						continue;
//					}
//					int index = position[1];
//					int type  = position[2] + 2;
//					 // 检测单词的索引是否有效
//					if(index >=  everyWordLen.size()  ||index <0){
//						continue ;
//					}
//					if (errorInfo.containsKey(index)) {
//						LinkedList<Integer> al = errorInfo.get(index);
//						if(al.contains(type)){
//							continue;
//						}
//						int eWrong = 3;             //	 WORD_WRONG  = 1
//						int eInsert =4;             //	 WORD_INSERT = 2
//						if( eWrong == type){
//							al.add(0, type);
//						}
//						else if(eInsert == type){
//							al.add(al.size(),type);
//						}
//						errorInfo.put(index, al);
//					} else {
//						LinkedList<Integer> al = new LinkedList<Integer>();
//						al.add(type);
//						errorInfo.put(index, al);
//					}
//				}
//
//				Object[] key = errorInfo.keySet().toArray();
//				Arrays.sort(key);
//				for(int iKey=0;iKey<key.length;iKey++){
//					Integer wordIndex= (Integer)key[iKey];
//					LinkedList<Integer> al = errorInfo.get(wordIndex);
//					for(int i=0; i<al.size(); i++) {
//						Integer type = al.get(i);
//						if(wordIndex >everyWordLen.size() || wordIndex < 0){
//							continue ;
//						}
//						AddFont addFont = new AddFont();
//						addFont = deposeContextAddFontSpan(type, wordIndex, text, increaseRead);
//						if( 3 == type){
//							increaseRead++;
//						}
//						addFonts[realError++] = addFont;
//
//					}
//				}
//				Log.i("addFont",text.toString());
//				JSONArray errorWordJson = new JSONArray();
//				// 显示错误信息
//				int eChecking =2;
//				for(int errorIndex = 0; errorIndex < realError; errorIndex++){
//					AddFont addFont = addFonts[errorIndex];
//					if(addFont == null ||addFont.type== eChecking){
//						Log.i(TAG, "TvPaperText.getText() == null");
//						continue;
//					}
//					if((addFont.realIndex + addFont.len ) > mCurText.length()){
//						Log.i(TAG, "index error");
//						continue;
//					}
//					Log.i("addFont",text.substring(addFont.realIndex, addFont.realIndex + addFont.len)+"; wordIndex="+addFont.wordIndex+";len="+addFont.len+";type="+ addFont.type);
//					
//					JSONObject fontJson = new JSONObject();
//					try {
//						fontJson.put("wordIndex", addFont.wordIndex);
//						fontJson.put("realIndex", addFont.realIndex);
//						fontJson.put("len", addFont.len);
//						fontJson.put("endIndex", addFont.endIndex);
//						fontJson.put("type", addFont.type);
//						errorWordJson.put(errorIndex, fontJson);
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//				}
//				return errorWordJson;
//			}
//
//			private AddFont deposeContextAddFontSpan(int type,int index, StringBuffer text, int increaseRead)
//			{
//				ForegroundColorSpan color1 = null;
//				BackgroundColorSpan color2 = null;
//				int realIndex = 0;
//				int showLen = 0;
//
//				int wordnum = everyWordLen.size();
//				if (index > wordnum - 1) {
//					try {
//						throw new MyException(TAG, "deposeContextAddFont", "索引值太大");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//				for (int i = 0; i < index; i++) {//在单词的后面进行增读(int i = 0; i <index; i++)
//					realIndex = realIndex + everyWordLen.get(i) + 1;// 1为空格
//				}
//				
//				String ERRORINSERT = "[...] ";
//				realIndex = realIndex + ERRORINSERT.length()*increaseRead;
//				
//				showLen = everyWordLen.get(index);
//				int eUnread = 0;
//				int eHasRead = 1;
//				int eChecking = 2;
//				int eWrong = 3; // WORD_WRONG = 1
//				int eInsert = 4;     
//				
////				if( eInsert == type){
////					realIndex += everyWordLen.get(index)+1;
////					text.insert(realIndex, ERRORINSERT ,0, ERRORINSERT.length());
////				}
//				if ( eUnread == type) {
//					color1 = new ForegroundColorSpan(Color.GRAY);
//					color2 = new BackgroundColorSpan(Color.TRANSPARENT);
//				} else if (eHasRead == type) {
//					color1 = new ForegroundColorSpan(Color.BLACK);
//					color2 = new BackgroundColorSpan(Color.TRANSPARENT);
//				} else if (eChecking == type) {
//					color1 = new ForegroundColorSpan(Color.GRAY);
//					color2 = new BackgroundColorSpan(Color.BLUE);
//				}else if (eWrong == type){
//					color1 = new ForegroundColorSpan(Color.RED);
//					color2 = new BackgroundColorSpan(Color.TRANSPARENT);}
////				else if (eInsert == type){
////					color1 = new ForegroundColorSpan(Color.GREEN);
////					color2 = new BackgroundColorSpan(Color.TRANSPARENT);
////					showLen  = ERRORINSERT.length() -1;
////				}
//
//				AddFont addFont  = new AddFont();
//				addFont.type = type;
//				addFont.foreColor = color1;
//				addFont.backColor = color2;
//				addFont.wordIndex = index;
//				addFont.realIndex = realIndex;
//				addFont.len = showLen;
//				return addFont;
//			}
//			
//			private void getEveryWordLen(String text) {
//				everyWordLen.clear();
//				String[] word = text.split(" ");
//				for (int i = 0; i < word.length; i++) {
//					everyWordLen.add(word[i].length());
//					Log.i(TAG,"index = "+i+";word="+word[i]+" ;len ="+word[i].length());
//				}
//			}
//}
