package com.sample_android.modules.singleton_sample;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by huang_jin on 2018/3/19.
 * Activity管理栈
 */

public class ActivityManager {
    private static volatile ActivityManager activityManager;
    //管理当前APP中所有activity实例的数据对象实体
    private Stack<Activity> mStack = new Stack<>();

    private ActivityManager() {
    }

    public static ActivityManager getActivityManager() {
        if (activityManager == null) {
            synchronized (ActivityManager.class) {
                activityManager = new ActivityManager();
                return activityManager;
            }
        }
        return activityManager;
    }

    public void addActivity(Activity activity) {
        mStack.add(activity);
    }

    public void removeActivity(Activity activity) {
        mStack.remove(activity);
    }

    private void finishActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
        }
    }

    public void killProgress() {
        int mCount = mStack.size();
        //删除栈内的数据
        if (mCount >= 0) {
            for (int i = 0; i < mCount; i++) {
                Activity activity = mStack.get(i);
                if (activity != null) {
                    finishActivity(activity);
                }
            }
        }
        //结束当前进程
        mStack.clear();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
