package com.tongdaxing.xchat_core.utils;

import java.util.regex.Pattern;

/**
 * 创建者      Created by dell
 * 创建时间    2018/12/5
 * 描述        验证手机号码是否合理
 * <p>
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class VerifyPhoneUtils {

    public static final int PHONE_LENGTH = 11;
//    private static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(16[6])|(17[0,1,3,5-8])|(18[0-9])|(19[1,8,9]))\\d{8}$";
    private static final String REGEX_MOBILE_EXACT = "^1\\d{10}$";

    /**
     * Return whether input matches the regex.
     *
     * @param input The input.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isMatch(final CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(REGEX_MOBILE_EXACT, input);
    }

}
