package com.sample_android.modules.lock_screen_sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.sample_android.modules.template_sample.BaseActivity;

/**
 * Created by huang_jin on 2018/3/21.
 * 锁屏展示页面
 */

public class LockScreenActivity extends BaseActivity {
    private Intent mLockServiceIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //添加当前锁屏页面的flag标记---让当前的界面总是处于系统锁屏页面之上
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Override
    protected void initView() {
        //开启服务任务
        mLockServiceIntent = new Intent(this, LockService.class);
        startService(mLockServiceIntent);

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLockServiceIntent != null) {
            stopService(mLockServiceIntent);
        }
    }
}
