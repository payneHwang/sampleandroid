package com.sample_android.modules.singleton_sample;

import android.content.Context;

/**
 * Created by huang_jin on 2018/3/19.
 * 1、声明静态的成员变量实例；
 * 2、私有化构造方法；
 * 3、提供公共的静态方法用于获取当前的实例对象
 * <p>
 * 注意：当前的volatile关键字标识---线程每次使用变量时，都是取虚拟机中最后一次赋值的结果
 */

public class SingletonDemo {
    private static volatile SingletonDemo singleInstance;
    private Context mContext;

    private SingletonDemo(Context context) {
        this.mContext = context;
    }

    public static SingletonDemo getSingleInstance(Context context) {
        if (singleInstance == null) {
            synchronized (SingletonDemo.class) {
                if (singleInstance == null) {
                    singleInstance = new SingletonDemo(context);
                    return singleInstance;
                }
            }
        }
        return singleInstance;
    }
}
