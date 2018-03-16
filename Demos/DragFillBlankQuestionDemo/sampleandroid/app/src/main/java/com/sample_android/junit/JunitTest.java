package com.sample_android.junit;

import com.sample_android.modules.builder_sample.IPhoneBuilder;
import com.sample_android.modules.builder_sample.PhoneFactory;

/**
 * Created by huang_jin on 2018/3/15.
 */

public class JunitTest {
    public static final void main(String[] args) {
        /**
         *模版方法的调用
         * */
//        MilitaryComputer militaryComputer = new MilitaryComputer();
//        militaryComputer.startUp();
        /**
         *构建模式
         * */
        IPhoneBuilder builder = new IPhoneBuilder();
        PhoneFactory factory = new PhoneFactory(builder);
        factory.create("IPhone 8 plus", 4, 64, "1920*1080");

    }
}
