package com;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Administrator on 2018/1/20.
 */

    public class TestVersion {
    public static void main(String[] args) {
        String str = "2.9.0";
        str = str.replace(".", "");
        int i = Integer.parseInt(str);
        boolean boot = StringUtils.isBlank("你好");
        if(i<300){
            System.out.println(i);
        }
        System.out.println(str);
        System.out.println(boot);
    }
}

