package com.sample_android.modules.template_sample;

/**
 * Created by huang_jin on 2018/3/15.
 */

public class MilitaryComputer extends AbsBaseComputer {
    @Override
    protected void powerOn() {
        //开启模拟机组

    }

    @Override
    protected void checkHardware() {
        checkGPSDevice();
        checkVoiceDevice();
    }

    private void checkVoiceDevice() {
        //...
    }

    private void checkGPSDevice() {
        //...
    }

    @Override
    protected void userValidate() {
        //人脸识别，虹膜识别，指纹识别，密码解锁程序

    }

    @Override
    protected void loadOs() {

    }
}
