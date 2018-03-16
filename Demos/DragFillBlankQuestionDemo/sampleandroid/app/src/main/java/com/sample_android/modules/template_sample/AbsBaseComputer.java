package com.sample_android.modules.template_sample;

/**
 * Created by huang_jin on 2018/3/15.
 * 基础template-object(Computer)
 * 释义：模版方法就是封装一套简单的程序执行流程或者算法执行过程，
 * 而子类可以在此模版不被修改的情况下，有具体步骤的自身实现方式；
 * <p>
 * 示例：Android--AsyncTask(异步任务程序)
 * onPreExecute()
 * doInBackground()
 * onPostExecute()
 */

public abstract class AbsBaseComputer {
    /**
     * 开机模版程序
     */
    public final void startUp() {
        powerOn();
        checkHardware();
        userValidate();
        loadOs();
    }

    /**
     * 打开电源开关
     */
    protected abstract void powerOn();

    /**
     * 检查硬件
     */
    protected abstract void checkHardware();

    /**
     * 用户身份验证
     */
    protected abstract void userValidate();

    /**
     * 加载操作系统
     */
    protected abstract void loadOs();

}
