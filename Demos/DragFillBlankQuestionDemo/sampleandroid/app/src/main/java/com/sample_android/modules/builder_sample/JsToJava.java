package com.sample_android.modules.builder_sample;

import android.webkit.JavascriptInterface;

/**
 * Created by huang_jin on 2018/3/19.
 * js调用Android客户端对象
 */

public class JsToJava {
    //内部定义js调用函数，Android调用js的函数
    @JavascriptInterface
    public void sendUserInfoParam(String userInfo) {

    }


}
