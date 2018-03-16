package com.sample_android.modules.builder_sample;

/**
 * Created by huang_jin on 2018/3/15.
 */

public class IPhoneBuilder extends Builder {
    private IPhone iphone;

    @Override
    protected void setBrand(String sBrand) {
        iphone.setBrand(sBrand);
    }

    @Override
    protected void setCoreNum(int sCoreNum) {
        iphone.setCoreNum(sCoreNum);
    }

    @Override
    protected void setRam(int sRam) {
        iphone.setRam(sRam);
    }

    @Override
    protected void setDisplayMetrics(String sDisplayMetrics) {
        iphone.setDisplayMetrics(sDisplayMetrics);
    }

    public Phone build() {
        return iphone;
    }
}
