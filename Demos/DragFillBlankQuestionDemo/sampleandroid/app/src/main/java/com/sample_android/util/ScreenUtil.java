package com.sample_android.util;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by huang_jin on 2018/3/16.
 * 屏幕参数工具类
 */

public class ScreenUtil {

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null && windowManager.getDefaultDisplay() != null) {
            return windowManager.getDefaultDisplay().getWidth();
        } else return -1;
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null && windowManager.getDefaultDisplay() != null) {
            return windowManager.getDefaultDisplay().getHeight();
        } else return -1;
    }

}
