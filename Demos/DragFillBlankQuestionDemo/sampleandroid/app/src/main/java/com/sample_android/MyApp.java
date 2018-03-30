package com.sample_android;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by huang_jin on 2018/3/28.
 * 应用全局Application管理类
 * 初始化工具说明：
 * 1、LeakCanary内存泄漏检测工具：LeakCanary.install(this)返回一个RefWatcher对象用于监控回收的相关对象
 * 可以用RefWatcher对象监视Fragment对象的销毁过程---getRefWatcher.watch(this);
 */

public class MyApp extends Application {
    private static MyApp instance;
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initLeakCanary();
    }

    private void initLeakCanary() {
        refWatcher = LeakCanary.install(this);
    }

    public static MyApp getInstance() {
        return instance;
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }
}
