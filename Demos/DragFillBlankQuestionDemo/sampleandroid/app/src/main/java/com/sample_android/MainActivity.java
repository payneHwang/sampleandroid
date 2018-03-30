package com.sample_android;

import android.animation.ValueAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.sample_android.util.AnimationUtil;
import com.sample_android.view.LoadingView;
import com.sample_android.view.StepView;

public class MainActivity extends AppCompatActivity {
    /**
     * 计步器View样式样例
     */
    private StepView stepView;
    /**
     * 属性动画实现样例
     */
    private ImageView ivLoading;

    /**
     * 记载中试图
     */
    private LoadingView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
    }

    private void findView() {
        stepView = findViewById(R.id.stepView);
        stepView.setMaxStep(4000);
        //创建属性动画实现变换的效果
        AnimationUtil.getValueAnimator(1500, 0, 3018, new DecelerateInterpolator(), new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                stepView.setCurrentStep(animatedValue);

            }
        }).start();

        ivLoading = findViewById(R.id.ivLoading);
        ivLoading.setImageResource(R.drawable.loading_view);
        AnimationDrawable drawable = (AnimationDrawable) ivLoading.getDrawable();
        drawable.start();

        loadingView = findViewById(R.id.loadingView);
    }
}
