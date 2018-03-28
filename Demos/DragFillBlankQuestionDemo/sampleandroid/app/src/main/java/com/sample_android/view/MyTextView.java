package com.sample_android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sample_android.R;

/**
 * Created by huang_jin on 2018/3/27.
 * API:canvas.drawText(text,x,y,paint)
 * text:需要绘制的文本
 * x:字符串左边在屏幕的位置----->如果Paint设置了textAlignCenter(true)默认显示在中间
 * y:字符串的baseline在屏幕的位置
 * paint:画笔工具（颜色、显示属性、描边）
 */

public class MyTextView extends View {
    private String text;
    private int textColor;
    private int textSize;
    private Paint mPaint;
    private Rect mBound;

    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        text = typedArray.getString(R.styleable.MyTextView_my_text);
        textColor = typedArray.getColor(R.styleable.MyTextView_text_color, 0);
        textSize = 60;
        mPaint = new Paint();
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
        mBound = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), mBound);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        //获取测量模式和尺寸值
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            int textWidth = mBound.width();
            width = textWidth + getPaddingLeft() + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int textHeight = mBound.height();
            height = textHeight + getPaddingBottom() + getPaddingTop();
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawText(text, (getMeasuredWidth() / 2 - mBound.width() / 2), (getMeasuredHeight() / 2 - mBound.height() / 2), mPaint);
        //绘制文字
        canvas.drawText(text, 0, getHeight() / 2 + mBound.height() / 2, mPaint);
    }
}
