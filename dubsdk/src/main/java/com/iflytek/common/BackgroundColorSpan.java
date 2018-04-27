package com.iflytek.common;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;

public class BackgroundColorSpan extends CharacterStyle

implements UpdateAppearance, ParcelableSpan {

	public int mColor;

	public BackgroundColorSpan(int color) {
		mColor = color;
	}

	public BackgroundColorSpan(Parcel src) {
		mColor = src.readInt();
	}

	@Override
	public int getSpanTypeId() {
		return TextUtils.CAP_MODE_CHARACTERS;
	}

	public int getSpanTypeIdInternal() {
		return TextUtils.CAP_MODE_SENTENCES;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mColor);

	}

	public int getBackgroundColor() {
		return mColor;
	}

	public void setBackgroundColor(int color) {
		mColor = color;
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		ds.bgColor = mColor;

	}

}
