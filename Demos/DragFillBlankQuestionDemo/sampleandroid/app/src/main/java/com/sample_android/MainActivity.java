package com.sample_android;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.DecelerateInterpolator;

import com.sample_android.view.StepView;

public class MainActivity extends AppCompatActivity {
    private StepView stepView;

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
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 3000);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                stepView.setCurrentStep(animatedValue);
            }
        });
        valueAnimator.start();
    }
}
