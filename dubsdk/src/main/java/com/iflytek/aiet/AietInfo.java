package com.iflytek.aiet;

import java.io.Serializable;

import android.util.Log;

public class AietInfo {

	public static class RecResultInfo implements Serializable {
		private static final long serialVersionUID = 821054460040064331L;
		private static final int S_MAXCOUNT = 4;
		private int iResultCnt = 0;
		private int iCodePage = 0;
		private int[] iCmdWordScore = null;
		private int[] iCmdWordID = null;
		private int[] iCmdWordSize = null;
		private String[] pCmdWord = null;


		public boolean  createRecInfo(int count, int codepage, String Score,
				String WordID, String WordSize, String Word) {
			if (count > S_MAXCOUNT ) {
				return false;
			}
			//Log.d("RecResultInfo","count = "+ count);
			//Log.d("RecResultInfo","codepage = "+codepage);
			//Log.d("RecResultInfo","Score="+Score);
			//Log.d("RecResultInfo","WordID+="+WordID);
			//Log.d("RecResultInfo","WordSize="+WordSize);
			//Log.d("RecResultInfo","Word="+Word	);
			iResultCnt = count;
			iCodePage = codepage;
			iCmdWordScore = getIntArray(Score);
			iCmdWordID = getIntArray(WordID);
			iCmdWordSize = getIntArray(WordSize);
			pCmdWord = getStringArray(Word);
			return true;
		}
		private int[] getIntArray(String content) {
			int[] iarr = null;

			String[] arrContent = content.split("/");
			if(content.equals("") ||arrContent.length == 0 ){
				return null;
			}
			iarr = new int[arrContent.length];
			for (int i = 0; i < arrContent.length; i++) {
				iarr[i] = Integer.parseInt(arrContent[i]);
			}
			return iarr;

		}

		private String[] getStringArray(String content) {
			int[] iarr = null;
			String[] arrContent = content.split("/");
			return arrContent;

		}

		public int getResultCnt() {
			return iResultCnt;
		}

		public int getCodePage() {
			return iCodePage;
		}

		public int getCmdWordScore(int index) {
			return iCmdWordScore[index];
		}

		public int getWordSize(int index) {
			return iCmdWordSize[index];
		}

		// 获取所有的单词的ID号
		public int[] getAllWordID() {
			return iCmdWordID;
		}

		public int  getWordID(int index) {
			return  iCmdWordID[index];
		}

		public String getWord(int index) {
			return pCmdWord[index];
		}


		// 获取所有的单词
		public String[] getAllWord() {
			return pCmdWord;
		}

		// 获取等分最高的单词
		public String getHigherSorceWord() {
			int MaxScore = 0;
			int iIndex = 0;
			for (int index = 0; index < iResultCnt; index++) {
				if (iCmdWordScore[index] >= MaxScore) {
					iIndex = index;
				}
			}
			return pCmdWord[iIndex];
		}


		// 获取等分最低的单词
		public String getLowerSorceWord() {
			int MinScore = 100;
			int iIndex = 0;
			for (int index = 0; index < iResultCnt; index++) {
				if (iCmdWordScore[index] <= MinScore) {
					iIndex = index;
				}
			}
			return pCmdWord[iIndex];
		}
	}



public static  class ErrorPhone implements Serializable{
		public int nsentenceId;
		public int nwordId;
		public int nphoneId;;
		public int nphonesize;  // 错误音素的长度如c:和ai、p等是不同的
		public int nerrorInfo;  //错误的信息
		public int iPosition; //在单词的位置
		public String  curPhone;//当前的音素
		public int ninsert;//增读的个数
		
		public ErrorPhone(){
			nsentenceId  =-1;
		    nwordId      =-1;
			nphoneId     =-1;
			nerrorInfo   =-1;
			nphonesize   = 0;
			iPosition  = 0;
			curPhone = null;
			ninsert = 0;
		}
		public void creat(byte[] position){
			nsentenceId  = position[0];
		    nwordId      = position[1];
			nphoneId     = position[2];
			nerrorInfo   = position[3];
			iPosition  = position[4]/2;
			nphonesize   = position[5]/2;
			ninsert = position[6];//增读的个数 add by qunzhu
			
			//Log.d("positon","nwordId:" +nwordId);
			//Log.d("positon","nphoneId:" +nphoneId);
			//Log.d("positon","nerrorInfo:" +nerrorInfo);
			//Log.d("positon","ierrorIndex:" +iPosition);
			//Log.d("positon","nphonesize:" +nphonesize);
			
		}
	}
public static  class RecResult implements Serializable{
	private static final long serialVersionUID = 821054460040064331L;
	private static final int S_MAXCOUNT = 4;
	private int iResultCnt = 0;
	private int iCodePage = 0;
	private int[] iCmdWordScore = null;
	private int[] iCmdWordID = null;
	private int[] iCmdWordSize = null;
	private String[] pCmdWord = null;

	public void createRecInfo(int count, int codepage, String Score, String WordID, String WordSize, String Word){
		if(count >= S_MAXCOUNT ){
			return ;
		}
		iResultCnt = count;
		iCodePage = codepage;
		iCmdWordScore = getIntArray(Score);
		iCmdWordID =getIntArray(WordID);
		iCmdWordSize=getIntArray(WordSize);
		pCmdWord = getStringArray(Word);
	}

	private int [] getIntArray (String   content ) {
		int[] iarr=null;
		String[] arrContent = content.split("/");
		iarr = new int [arrContent.length];
		for (int i=0 ;i<arrContent.length;i++){
			iarr[i] = Integer.parseInt(arrContent[i]);
		}
		return iarr;

	}

	private String [] getStringArray (String   content ) {
		int[] iarr=null;
		String[] arrContent = content.split("/");
		return arrContent;

	}

	public int getResultCnt()		{ return iResultCnt; }
	public int getCodePage()		{ return iCodePage; }
	public int getCmdWordScore(int index)	{ return iCmdWordScore[index]; }
	public int getWordSize(int index)		{ return iCmdWordSize[index]; }
	public String getWord(int index)			{ return pCmdWord[index]; }

	//获取所有的单词
	public String[] getAllWord(){return pCmdWord;}

	// 获取等分最高的单词
	public String getHigherSorceWord(){
		int MaxScore = 0;
		int iIndex = 0;
		for(int index =0;index<iResultCnt;index++){
			if(iCmdWordScore[index]>=MaxScore){
				iIndex = index;
			}
		}
		return pCmdWord[iIndex];
	}

	// 获取等分最低的单词
	public String getLowerSorceWord(){
		int MinScore = 100;
		int iIndex = 0;
		for(int index =0;index<iResultCnt;index++){
			if(iCmdWordScore[index]<= MinScore){
				iIndex = index;
			}
		}
		return pCmdWord[iIndex];
	}
}
}
