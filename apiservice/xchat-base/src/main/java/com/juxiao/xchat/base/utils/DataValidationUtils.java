package com.juxiao.xchat.base.utils;


import org.springframework.util.StringUtils;

/**
 * 数据验证工具类
 *
 * @class: DataValidationUtils.java
 * @author: chenjunsheng
 * @date 2018/6/11
 */
public final class DataValidationUtils {
    public static String phoneRegx = "1(3|4|5|6|7|8|9)[0-9]{9}";

    /**
     * 验证是否电话号码
     *
     * @param phone
     * @return
     */
    public static boolean validatePhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        return phone.matches(phoneRegx);
    }
}
