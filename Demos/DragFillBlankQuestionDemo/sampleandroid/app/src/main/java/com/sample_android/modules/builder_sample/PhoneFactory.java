package com.sample_android.modules.builder_sample;

/**
 * Created by huang_jin on 2018/3/15.
 * 构建工厂--内部维护一个构建器对象
 */

public class PhoneFactory {
    IPhoneBuilder builder = null;


    public PhoneFactory(IPhoneBuilder sBuilder) {
        this.builder = sBuilder;
    }

    public Phone create(String brand, int coreNum, int ram, String displayMetrics) {
        builder.setBrand(brand);
        builder.setCoreNum(coreNum);
        builder.setRam(ram);
        builder.setDisplayMetrics(displayMetrics);
        return builder.build();
    }

}
