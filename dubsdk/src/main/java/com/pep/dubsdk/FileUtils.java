package com.pep.dubsdk;

import android.content.Context;

import java.io.File;

/**
 * 缓存文件目录
 */
public class FileUtils {
    public static String getTempRecordPath(Context context) {
        String cachePath = SdcardUtil.getCacheRootDir(context)
                + File.separator + "temp.mp3";

        return cachePath;
    }

}
