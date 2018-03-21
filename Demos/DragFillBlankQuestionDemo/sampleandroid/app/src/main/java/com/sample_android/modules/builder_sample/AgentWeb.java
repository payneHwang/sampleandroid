package com.sample_android.modules.builder_sample;

import android.content.Context;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by huang_jin on 2018/3/19.
 */

public class AgentWeb {
    private WebView mWebInstance;
    private WebSettings mWebSettings;
    private WebChromeClient mWebChromeClient;
    private WebViewClient mWebViewClient;
    private JsToJava jsToJava;

    private AgentWeb(Builder builder) {
        this.jsToJava = builder.jsToJava;
        this.mWebInstance = builder.mWebInstance;
        this.mWebViewClient = builder.mWebViewClient;
        this.mWebChromeClient = builder.mWebChromeClient;
        this.mWebSettings = builder.mWebSettings;
    }


//    public static AgentWeb get() {
//        if (agentWeb == null) {
//            synchronized (AgentWeb.class) {
//                if (agentWeb == null) {
//                    agentWeb = new AgentWeb();
//                    return agentWeb;
//                }
//            }
//        }
//        return agentWeb;
//    }

    /**
     * 利用构建者模式提供访问实例
     */
    public static class Builder {
        private Context mContext;
        private WebView mWebInstance;
        private WebSettings mWebSettings;
        private WebChromeClient mWebChromeClient;
        private WebViewClient mWebViewClient;
        private JsToJava jsToJava;


        //提供共有的构造方法传入相应的Context上下文对象
        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setWebView(WebView web) {
            this.mWebInstance = web;
            return this;
        }

        public Builder setWebViewClient(WebViewClient webViewClient) {
            this.mWebViewClient = webViewClient;
            return this;
        }

        public Builder setWebChromeClient(WebChromeClient webChromeClient) {
            this.mWebChromeClient = webChromeClient;
            return this;
        }

        public Builder setWebSetting(WebSettings webSetting) {
            this.mWebSettings = webSetting;
            return this;
        }

        public Builder addJsInterface(JsToJava jsObejct) {
            this.jsToJava = jsObejct;
            return this;
        }

        /**
         * 提供构建方法
         */
        public AgentWeb build() {
            return new AgentWeb(this);
        }
    }

}
