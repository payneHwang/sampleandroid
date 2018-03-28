package com.sample_android.modules.lock_screen_sample;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by huang_jin on 2018/3/21.、
 * 锁屏服务对象（内部开启广播检测锁屏动作意图，检测到之后跳转到自定义锁屏页面）
 */

public class LockService extends Service {
    private BroadcastReceiver mLockIntentReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //开启自定义广播检测动作意图
        mLockIntentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == Intent.ACTION_SCREEN_OFF) {
                    //检测到系统的锁屏动作
                    Intent toLockActivity = new Intent(LockService.this, LockScreenActivity.class);
                    startActivity(toLockActivity);
                }
            }
        };
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
