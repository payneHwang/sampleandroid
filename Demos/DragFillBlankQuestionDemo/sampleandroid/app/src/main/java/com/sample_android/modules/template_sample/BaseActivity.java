package com.sample_android.modules.template_sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by huang_jin on 2018/3/15.
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        initView();
        initListener();
        loadData();
    }

    /**
     * 查找布局资源id
     */
    protected abstract int getLayoutResId();

    /**
     * 初始化控件UI
     */
    protected abstract void initView();

    /**
     * 初始化点击事件
     */
    protected abstract void initListener();

    /**
     * 加载网络数据
     */
    protected abstract void loadData();

    protected <T extends View> T findView(int viewId) {
        return (T) super.findViewById(viewId);
    }


}
