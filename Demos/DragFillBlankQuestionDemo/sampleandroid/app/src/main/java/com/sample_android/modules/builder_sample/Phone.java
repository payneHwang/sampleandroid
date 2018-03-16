package com.sample_android.modules.builder_sample;

/**
 * Created by huang_jin on 2018/3/15.
 * Phone产品的抽象类层
 */

public abstract class Phone {
    protected String brand;
    protected int coreNum;
    protected int ram;
    protected String displayMetrics;


    public abstract String getBrand();

    public abstract void setBrand(String brand);

    public abstract int getCoreNum();

    public abstract void setCoreNum(int coreNum);


    public abstract int getRam();

    public abstract void setRam(int ram);

    public abstract String getDisplayMetrics();

    public abstract void setDisplayMetrics(String displayMetrics);
}
