package com.sample_android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sample_android.R;

/**
 * 绘制各种图形自定义View
 */
public class ShapeView extends View {
    /**
     * 圆形
     */
    public static final int ROUND = 0;
    /**
     * 三角形
     */
    public static final int TRIANGLE = 1;
    /**
     * 矩形
     */
    public static final int RECTANGLE = 2;
    /**
     * 正方形
     */
    public static final int SQUARE = 3;
    /**
     * 平行四边形
     */
    public static final int PARALLELOGRAM = 4;

    /**
     * PaintStyle{STROKE,FILL}
     */
    public static final int STYLE_STROKE = 1;
    public static final int STYLE_FILL = 0;

    private Paint mPaint;//画笔工具
    private Path path;

    private int gradientColor;//形状的填充色
    private int borderColor;//形状的描边颜色
    private int borderWidth;//形状的描边宽度
    private int shapeType;//形状类型
    private int shapeStyle;//画笔风格

    public ShapeView(Context context) {
        this(context, null);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeView);
        gradientColor = typedArray.getColor(R.styleable.ShapeView_gradient_color, Color.GREEN);
        borderWidth = (int) typedArray.getDimension(R.styleable.ShapeView_sv_border_width, 20);
        borderColor = typedArray.getColor(R.styleable.ShapeView_border_color, Color.BLACK);
        shapeStyle = typedArray.getInt(R.styleable.ShapeView_paint_style, 0);
        shapeType = typedArray.getInt(R.styleable.ShapeView_shape_type, 0);
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        path = new Path();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //取正方形试图（长宽取最小边比例）
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        width = height = width > height ? height : width;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int width = w;
        int height = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (shapeStyle == STYLE_STROKE) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(borderWidth);
            mPaint.setColor(gradientColor);
        } else {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(gradientColor);
        }
        /**
         * 开始进行图形的绘画工作
         * {CIRCLE, TRIANGLE, RECTANGLE, SQUARE, PARALLELOGRAM};
         * */
        switch (shapeType) {
            case ROUND:
                drawCircle(canvas);
                break;
            case TRIANGLE:
                drawTriangle(canvas);
                break;
            case RECTANGLE:
                canvas.drawRect(borderWidth, 0, getWidth() * 0.5f - borderWidth, getHeight() - borderWidth, mPaint);
                break;
            case SQUARE:
                canvas.drawRect(borderWidth, 0, getWidth() - borderWidth, getHeight() - borderWidth, mPaint);
                break;
            case PARALLELOGRAM:
                path.moveTo(getWidth() * 0.3f, 0);
                path.lineTo(getWidth(), 0);
                path.lineTo(getWidth() * 0.7f, getHeight());
                path.lineTo(0, getHeight());
                path.lineTo(getWidth() * 0.3f, 0);
                path.close();
                canvas.drawPath(path, mPaint);
                break;
        }
    }

    /**
     * 画圆
     */
    private void drawCircle(Canvas canvas) {
        float cx = 0.0f;
        float cf = 0.0f;
        float radius = 0.0f;
        switch (shapeStyle) {
            case STYLE_STROKE:
                cx = getWidth() * 0.5f + borderWidth;
                cf = getHeight() * 0.5f - borderWidth;
                radius = getWidth() * 0.5f - borderWidth;
                break;
            case STYLE_FILL:
                cx = getWidth() * 0.5f;
                cf = getHeight() * 0.5f;
                radius = getWidth() * 0.5f;
                break;
        }
        canvas.drawCircle(cx, cf, radius, mPaint);
    }

    /**
     * 画三角形
     */
    private void drawTriangle(Canvas canvas) {
        switch (shapeStyle) {
            case STYLE_STROKE:
                path.moveTo((getWidth() + borderWidth) * 0.5f, 0);
                path.lineTo(borderWidth, getHeight() - borderWidth);
                path.lineTo(getWidth() - borderWidth, getHeight() - borderWidth);
                path.close();
                break;
            case STYLE_FILL:
                path.moveTo(getWidth() * 0.5f, 0);
                path.lineTo(0, getHeight());
                path.lineTo(getWidth(), getHeight());
                path.close();
                break;
        }
        canvas.drawPath(path, mPaint);
    }

    public int getGradientColor() {
        return gradientColor;
    }

    public void setGradientColor(int gradientColor) {
        this.gradientColor = gradientColor;
        postInvalidate();
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        postInvalidate();
    }

    public int getShapeType() {
        return shapeType;
    }

    public void setShapeType(int shapeType) {
        this.shapeType = shapeType;
        postInvalidate();
    }

    public int getShapeStyle() {
        return shapeStyle;
    }

    public void setShapeStyle(int shapeStyle) {
        this.shapeStyle = shapeStyle;
        postInvalidate();
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        postInvalidate();
    }
}
