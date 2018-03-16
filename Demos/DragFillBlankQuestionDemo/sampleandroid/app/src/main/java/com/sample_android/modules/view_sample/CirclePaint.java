package com.sample_android.modules.view_sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.sample_android.util.ScreenUtil;

/**
 * Created by huang_jin on 2018/3/16.
 * 绘制圆弧形
 * 1、利用canvas和paint API画出对应的圆弧效果--canvas.drawArc(RectF rectF,float startAngle,Float sweepAngle,boolean userCenter,Paint paint);
 *
 * @param rectF:定义圆弧的形状和大小的范围
 * @param startAngle:圆弧的基本起始角度（以时钟的三点钟方向为0度，逆时针为正方向）
 * @param sweepAngle:圆弧的弧度面积（以时钟的三点钟方向为0度，逆时针为正方向）
 * @param userCenter:是否包含圆心
 * @param paint:画笔工具对象
 */

public class CirclePaint extends View {
    private int centerX;
    private int centerY;
    private int radius;
    private Paint mPaint;//绘制背景圆弧的画笔
    private RectF mRectF;
    private int strokeWidth;//画笔的描边宽度
    private float sweepAngle;//当前圆弧的绘制角度(以时钟的三点钟方向为0度,逆时针为正方向)
    private float startAngle;//圆弧的开始角度（以时钟的三点钟方向为0度,逆时针方向为正方向）
    private float maxAngle;//圆弧的最大角度

    //绘制文字
    private Rect mRect;
    private String stepValue;
    private int textSize;
    private int textColor;

    public CirclePaint(Context context) {
        this(context, null);
    }

    public CirclePaint(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CirclePaint(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);


    }

    private void init(Context context) {
        mPaint = new Paint();
        //设置画笔的颜色
        mPaint.setColor(Color.parseColor("#3F51B5"));
        //设置画笔的宽度（当仅当画笔的风格为STROKE或者FILL_OR_STROKE时）
        strokeWidth = 50;
        mPaint.setStrokeWidth(strokeWidth);
        //当画笔的风格STROKE或者STROKE_OR_FILL时，可以设置画笔的图形样式（圆形等）
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);//绘制图像是否防抖动的，图形更加平稳平滑
        //设置画笔的样式（FILL,FILL_OR_STROKE,STROKE）
        mPaint.setStyle(Paint.Style.STROKE);
        //半径中心点
        centerX = ScreenUtil.getScreenWidth(context) / 2;
        centerY = ScreenUtil.getScreenHeight(context) / 2;
        //画背景圆弧--确定半径
        radius = (centerX - 100);
        mRectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        sweepAngle = 90;
        startAngle = 180;
        maxAngle = 180;

        mRect = new Rect();
        textColor = Color.parseColor("#FF4081");
        textSize = 20;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        //绘制背景圆弧图形
        canvas.drawArc(mRectF, startAngle, maxAngle, false, mPaint);
        mPaint.setColor(Color.parseColor("#FF4081"));
        //绘制进度条
        canvas.drawArc(mRectF, startAngle, sweepAngle, false, mPaint);
        //绘制文字
        drawText(canvas);

    }

    private void drawText(Canvas canvas) {
        mPaint.reset();//重置画笔
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setDither(true);
        mPaint.setColor(textColor);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        stepValue = "1009";
        //测量文字的宽高
        mPaint.getTextBounds(stepValue, 0, stepValue.length(), mRect);
        int dx = (getWidth() - mRect.width()) / 2;
        //获取画笔的FontMetrics
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        //计算文字的基准线
        int baseLine = (int) (getHeight() / 2 + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);
        canvas.drawText(stepValue, dx, baseLine, mPaint);
    }


}
