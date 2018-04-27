package com.pep.dubsdk.constant;

import android.os.Environment;

import java.io.File;

public class AppConstants {

    public static final boolean DEBUG_MODE = false;
    // public static final boolean VERSION_2_DEBUG_MODE = BuildConfig.DEBUG;
    // public static final boolean DEBUG_MODE = BuildConfig.DEBUG;

    public static final String PICK_APP = "pick_app";
    public static final String HEAD_FILE_TMP = "headfile_tmp";

    public static final int MAX_INPUT = 200;
    public static final String ARECORD_NAME = "Android" + File.separator
            + "data" + File.separator + "com.spokenenglish.merry"
            + File.separator + "files";
    public static final String DB_DIR_NAME = "db";
    public static final String ARECORD_DB_DIR = File.separator + ARECORD_NAME
            + File.separator + DB_DIR_NAME + File.separator;

    public static final String[] AUTO_EMAILS = {"@gmail.com", "@163.com",
            "@126.com", "@sina.com", "@sohu.com", "@yeah.net", "@139.com",
            "@21cn.com", "@qq.com", "@hotmail.com"};
    // shared preference
    public static final String SP_HAS_LOGIN = "has_login";
    public static final String SP_FIRST_USE = "first_use";
    public static final String SP_INSTALL_FLAG = "install_flag";
    public static final String SP_LASTEST_VERSION = "lastest_version";
    public static final String JUMP_MAINTAB_TAG = "main_tag";
    public static final String IMAGE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "spokenenglish"
            + File.separator + "Image" + File.separator;

    public static final String INFORMA_CITY = "informa_city";
    public static final int PERSONAL_UPDATECITY = 0;

    public static int ZIP_BUFFER_SIZE = 1024;

    public static String[] mWeekDays;
    public static String[] mWeekTypes;
    public static String[] mDegrees;
    public static String[] mYears;
    public static String[] mTerms;
    public static String[] mMonths;
    public static String[] mEndWeeks;
    public static String[] mTimes;
    public static String[] mLessonLengths;
    public static String[] mBeforeDays;
    public static String[] mHours;
    public static String[] mMinutes;

    public static boolean NEED_LEFT_MENU = true;
    public static boolean NEED_RIGHT_MENU = false;

    // 加密密钥
    public final static String SECRET_KEY = "b!@767pd7@#$%71f*&^a0E72Ge6i0Aa%^&*7g1343Et24FdaE$%71f*&^a0E72Gc";
    // 加密因子
    public final static String SECRET_IV = "03125476";
    // 加解密统一使用的编码方式
    public final static String SECRET_ENCODING = "utf-8";

    public final static String CMCC_PLATFORM = "CMCCNM";


    public static String[] getMinutes(int count) {
        String[] minutes = new String[count];
        for (int i = 0; i < count; i++) {
            if (i < 10) {
                minutes[i] = "0" + i;
            } else {
                minutes[i] = "" + i;
            }
        }
        return minutes;
    }

}
