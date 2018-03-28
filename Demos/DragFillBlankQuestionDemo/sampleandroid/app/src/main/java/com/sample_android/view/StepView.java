package com.sample_android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sample_android.R;

/**
 * Created by huang_jin on 2018/3/28.
 * 仿QQ计步器
 */

public class StepView extends View {
    private static final String TAG = "StepView";
    /**
     * 外圆弧画笔工具
     */
    private Paint mOuterCirclePaint;
    private Paint mInnerCirclePaint;
    private RectF mOuterRectF;
    /**
     * 当前文字画笔工具
     */
    private Paint mStepTextPaint;
    private Rect mTextRect;

    private int outerBorderColor;//外部圆弧的颜色值
    private int innerBorderColor;//内部圆弧的颜色值
    private int borderWidth;//圆弧的宽度
    private int maxStep;//最大步数
    private int currentStep;//当前步数
    private float percent;//当前步数百分比
    private String text;//内部步数文字
    private int textColor;//当前步数文字的显示颜色
    private int textSize;//当前步数文字的显示大小


    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        initPaints();
    }

    /**
     * 初始化画笔工具
     */
    private void initPaints() {
        mOuterCirclePaint = new Paint();
        mOuterCirclePaint.setColor(outerBorderColor);
        mOuterCirclePaint.setAntiAlias(true);
        mOuterCirclePaint.setStrokeCap(Paint.Cap.ROUND);//设置封边为圆形
        mOuterCirclePaint.setStrokeWidth(borderWidth);//设置描边的宽度
        mOuterCirclePaint.setStyle(Paint.Style.STROKE);//描边属性

        mInnerCirclePaint = new Paint();
        mInnerCirclePaint.setColor(innerBorderColor);
        mInnerCirclePaint.setAntiAlias(true);
        mInnerCirclePaint.setStrokeCap(Paint.Cap.ROUND);//设置封边为圆形
        mInnerCirclePaint.setStrokeWidth(borderWidth);//设置描边的宽度
        mInnerCirclePaint.setStyle(Paint.Style.STROKE);//描边属性


        mStepTextPaint = new Paint();
        mStepTextPaint.setColor(textColor);
        mStepTextPaint.setAntiAlias(true);
        mStepTextPaint.setTextSize(textSize);


        mTextRect = new Rect();
        //获取文字的宽度范围
        Rect textRect = new Rect();
        mStepTextPaint.getTextBounds(text, 0, text.length(), textRect);
        //获取当前文字的高度
        Paint.FontMetrics fontMetrics = mStepTextPaint.getFontMetrics();
    }

    /**
     * 初始化内部属性和参数，获取xml控件指定的属性值
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StepView);
        outerBorderColor = typedArray.getColor(R.styleable.StepView_outer_border_color, Color.parseColor("#3F51B5"));
        innerBorderColor = typedArray.getColor(R.styleable.StepView_inner_border_color, Color.parseColor("#FF4081"));
        borderWidth = (int) typedArray.getDimension(R.styleable.StepView_border_width, 50);
        maxStep = typedArray.getInt(R.styleable.StepView_max_step, 10000);
        currentStep = typedArray.getInt(R.styleable.StepView_current_step, 3370);
        text = typedArray.getString(R.styleable.StepView_text);
        textColor = typedArray.getColor(R.styleable.StepView_textColor, Color.parseColor("#FF4081"));
        textSize = typedArray.getDimensionPixelSize(R.styleable.StepView_border_width, 50);
        typedArray.recycle();
    }


    /**
     * 保证当前view的图形为一个正方形，取当前较小的一边
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width > height ? height : width, width > height ? height : width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mOuterRectF = new RectF(borderWidth / 2, borderWidth / 2, getWidth() - borderWidth / 2, getHeight() - borderWidth / 2);
        //画外圆弧
        canvas.drawArc(mOuterRectF, 135, 270, false, mOuterCirclePaint);

        //计算当前的步数百分比，决定当前画多大的圆弧
        percent = (float) currentStep / maxStep;
        //画内圆弧
        canvas.drawArc(mOuterRectF, 135, percent * 270, false, mInnerCirclePaint);
        //画内部的文字
        //获取当前文字的宽度-- 确定当前文字的左边距x
        mStepTextPaint.getTextBounds(text, 0, text.length(), mTextRect);
        int x = getWidth() / 2 - mTextRect.width() / 2;

        //获取当前文字的高度--确定当前文字的基准线
        Paint.FontMetrics fontMetrics = mStepTextPaint.getFontMetrics();

        float baseline = (getHeight() / 2 - fontMetrics.top);
        canvas.drawText(text, x, (int) baseline, mStepTextPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    //提供相关的api


    public int getOuterBorderColor() {
        return outerBorderColor;
    }

    public void setOuterBorderColor(int outerBorderColor) {
        this.outerBorderColor = outerBorderColor;
    }

    public int getInnerBorderColor() {
        return innerBorderColor;
    }

    public void setInnerBorderColor(int innerBorderColor) {
        this.innerBorderColor = innerBorderColor;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public int getMaxStep() {
        return maxStep;
    }

    public void setMaxStep(int maxStep) {
        this.maxStep = maxStep;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
        setText(currentStep + "");
        invalidate();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
}
