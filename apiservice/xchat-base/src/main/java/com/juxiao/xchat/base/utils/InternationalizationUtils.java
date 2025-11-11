package com.juxiao.xchat.base.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 国际化相关工具
 */
public class InternationalizationUtils {

    /**
     * 根据状态码返回对应的文字
     * @param code
     * @return
     */
    public static String InternationalizationByCode(int code){
        Locale locale = Locale.getDefault();
        ResourceBundle bundle = ResourceBundle.getBundle("msg", locale);

        // 根据key获取配置文件中的值
        String message = bundle.getString(code+"");
        return message;
    }



}
