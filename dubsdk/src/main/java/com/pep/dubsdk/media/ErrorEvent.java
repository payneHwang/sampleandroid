package com.pep.dubsdk.media;

import com.pep.dubsdk.AppApplication;
import com.pep.dubsdk.R;

public class ErrorEvent {

    public ErrorEvent(int errorCode, String errorMessage) {
        mErrorMessage = errorMessage;
        mErrorCode = errorCode;
    }

    /**
     * 网路类错误
     */
    public static final int NET_NO_CODE = 1000;

    public static final String NET_NO_TEXT = AppApplication.getInstance().getResources().getString(R.string.networkUnavailable);

    public static final int NET_ERROR_CODE = 1001;

    public static final String NET_ERROR_TEXT = AppApplication.getInstance().getResources().getString(R.string.networkUnavailable);

    /**
     * 系统类错误
     */
    public static final int SYSTEM_PLAYER_ERROR_CODE = 2000;

    public static final String SYSTEM_PLAYER_ERROR_TEXT = AppApplication.getInstance().getResources().getString(R.string.sys_error);

    public static final int SYSTEM_RECORDER_ERROR_CODE = 2001;

    public static final String SYSTEM_RECORDER_ERROR_TEXT = AppApplication.getInstance().getResources().getString(R.string.sys_error);

    /**
     * 文件类错误
     */
    public static final int FILE_CREATE_ERROR_CODE = 3000;

    public static final String FILE_CREATE_ERROR_TEXT = AppApplication.getInstance().getResources().getString(R.string.sys_error);

    public static final int FILE_OPEN_ERROR_CODE = 3001;

    public static final String FILE_OPEN_ERROR_TEXT = AppApplication.getInstance().getResources().getString(R.string.sys_error);

    public static final int FILE_READ_ERROR_CODE = 3002;

    public static final String FILE_READ_ERROR_TEXT = AppApplication.getInstance().getResources().getString(R.string.sys_error);

    public static final int FILE_WRITE_ERROR_CODE = 3003;

    public static final String FILE_WRITE_ERROR_TEXT = AppApplication.getInstance().getResources().getString(R.string.sys_error);

    public static final int FILE_SPACE_NOT_ENOUGH = 3004;

    /**
     * 错误类型
     */
    private int mErrorCode;

    /**
     * 错误信息
     */
    private String mErrorMessage;

    public int getmErrorCode() {
        return mErrorCode;
    }

    public void setmErrorCode(int mErrorCode) {
        this.mErrorCode = mErrorCode;
    }

    public String getmErrorMessage() {
        return mErrorMessage;
    }

    public void setmErrorMessage(String mErrorMessage) {
        this.mErrorMessage = mErrorMessage;
    }

}
