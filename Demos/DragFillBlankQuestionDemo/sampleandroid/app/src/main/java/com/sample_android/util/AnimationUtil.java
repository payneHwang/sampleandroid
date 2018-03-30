package com.sample_android.util;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;

/**
 * 项目相关动画属性集工具类
 */
public class AnimationUtil {
    /**
     * 获取属性动画
     */
    public static ValueAnimator getValueAnimator(long duration, int startValue, int endValue, TimeInterpolator interpolator, ValueAnimator.AnimatorUpdateListener animatorListener) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startValue, endValue);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(interpolator);
        if (animatorListener != null) {
            valueAnimator.addUpdateListener(animatorListener);
        }
        return valueAnimator;
    }


}
