package com.sample_android.modules.builder_sample;

/**
 * Created by huang_jin on 2018/3/15.
 */

public class IPhone extends Phone {
    @Override
    public String getBrand() {
        return brand;
    }

    @Override
    public void setBrand(String sBrand) {
        brand = sBrand;
    }

    @Override
    public int getCoreNum() {
        return coreNum;
    }

    @Override
    public void setCoreNum(int sCoreNum) {
        coreNum = sCoreNum;
    }

    @Override
    public int getRam() {
        return ram;
    }

    @Override
    public void setRam(int sRam) {
        ram = sRam;
    }

    @Override
    public String getDisplayMetrics() {
        return displayMetrics;
    }

    @Override
    public void setDisplayMetrics(String sDisplayMetrics) {
        displayMetrics = sDisplayMetrics;
    }
}
