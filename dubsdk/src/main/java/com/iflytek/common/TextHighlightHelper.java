package com.iflytek.common;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 此类的作用是动态的添加文本更新文本的颜色
 *
 * @author hlcheng
 *
 */

public class TextHighlightHelper {

	private String TAG = "TextHighlightHelper";
	private Activity m_activity;
	private LinearLayout m_lLayout;
	private static int m_viewNum = 0;
	TextView  textview;;
	// 构造函数 :传入 Activity上下文 及 子View对齐方式 以及 layout_width layout_height
	public TextHighlightHelper(Activity a, int l) {
		m_activity = a;

		m_lLayout = new LinearLayout(m_activity);
		m_lLayout.setOrientation(l);
		m_lLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
	}

	// 定义函数 用于接收字符串
	public void addText(CharSequence cs) {
		Typeface mFace=Typeface.createFromAsset(m_activity.getAssets(), "font/segoeui.ttf");
		for (int i = 0; i < cs.length(); i++) {
			textview = new TextView(m_activity);
			textview.setText(cs.charAt(i) + "");
			textview.setTextSize(22);
			textview.setTextColor(Color.BLACK);
			textview.setTypeface(mFace);
			m_lLayout.addView(textview);
		}
		// 编辑文本的大小
		m_viewNum = cs.length();
	}

	// 定义函数 用于接收字符串
	public void addText(String cs) {
		for (int i = 0; i < cs.length(); i++) {
			textview = new TextView(m_activity);
			textview.setText(cs.charAt(i) + "");
			textview.setTextSize(22);
			//默认的字体颜色为白色
			textview.setTextColor(Color.WHITE);
			m_lLayout.addView(textview);
		}
		// 编辑文本的大小
		m_viewNum = cs.length();
	}

	// 从nbegin 开始 选取nlen个字符 字体大小都size
	public void SetTextSize(int nbegin, int nlen, float size) {
		if (nlen > m_lLayout.getChildCount()) {
			// error argument
		}
		for (int i = nbegin; i < nbegin + nlen; i++) {
			TextView item = (TextView) m_lLayout.getChildAt(i);
			item.setTextSize(size);
		}

	}

	// 函数解释： 从s开始 选取l个字符 颜色都设定为i
	public void addColor(int nbegin, int nlen, int ncolor) {
		if (nlen > m_lLayout.getChildCount()) {
			// error argument
		} else {
			for (int i = nbegin; i < nbegin + nlen; i++) {
				TextView item = (TextView) m_lLayout.getChildAt(i);
				item.setTextColor(ncolor);
			}
		}
	}

	// 设定所有字符的背景 最好使用*.9.png 资源 因为长度可变
	public void addBackResource(int r) {
		m_lLayout.setBackgroundResource(r);
	}
	// 设定所有字符的背景 透明
	public void setBackgroundTranspatrent() {
		/*m_lLayout.setBackgroundResource(r);*/
		m_lLayout.setBackgroundColor(Color.TRANSPARENT);
	}

	// 得到整个LinearLayout 并供使用
	public View loadView() {
		return m_lLayout;
	}

	public TextView getTextView(){
		return textview;
	}
	// 得到整个LinearLayout 并供使用
	public int GetViewNum() {
		return m_viewNum;
	}

}
