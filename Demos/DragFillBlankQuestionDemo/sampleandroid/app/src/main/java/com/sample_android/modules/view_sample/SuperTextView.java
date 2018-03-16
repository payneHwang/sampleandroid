package com.sample_android.modules.view_sample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sample_android.R;

/**
 * Created by huang_jin on 2018/3/16.
 */

public class SuperTextView extends View {
    private String text;
    private int textColor;
    private int textSize;

    public SuperTextView(Context context) {
        this(context, null);
    }

    public SuperTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SuperTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainAttrs(context, attrs);
    }

    /**
     * 获取自定义属性
     */
    private void obtainAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SuperTextView);
//        获取属性
        text = typedArray.getString(R.styleable.SuperTextView_super_text);
        textColor = typedArray.getInt(R.styleable.SuperTextView_super_text_color, 0);
        textSize = typedArray.getInt(R.styleable.SuperTextView_super_text_size, 0);
        typedArray.recycle();

    }

    /**
     * View的size测量方法
     * 测量模式：
     * 1、AT_MOST
     * 2、EXACTLY
     * 3、UNSPECIFIC
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    /**
     * 利用canvas API进行图形的绘画
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * 处理用户的点击事件
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
