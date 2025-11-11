package com.tongdaxing.xchat_framework.util.util.net;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by levyyoung on 14-3-3.
 */
public class MobileNumberUtils {

    /*
    * 国内各运营商手机号段
    * 移动： 134~139、147、150~152、157~159、182、183、187、188
    * 联通:  130~132、154、155、156、185、186
    * 电信:  133、153、189、180
    * */

    //中移动
    private static final String REG_CHINA_MOBILE = "^1(3[4-9]|47|5[012789]|8[2378])\\d{8}$";
    //中联通
    private static final String REG_CHINA_UNICOM = "^1(3[0-2]|5[456]|8[56])\\d{8}$";
    //中电信
    private static final String REG_CHINA_TELECOM = "^1(33|53|8[019])\\d{8}$";
    //中国国内
    private static final String REG_CHINA_INTERNAL = "^1(3[0-9]|47|5[0-9]|8[023456789])\\d{8}$";

    public static void hightLightChineseMobileNumber(int c, String text, TextView targetTv) {
        String reg = "\\d{11}";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            SpannableString s = new SpannableString(text);
            Object blue = new ForegroundColorSpan(c);
            s.setSpan(blue, matcher.start(), matcher.end(), Spannable.SPAN_MARK_POINT);
            targetTv.setText(s);
        } else {
            targetTv.setText(text);
        }
    }

    public static boolean isChinaMobileNumber(CharSequence phoneNumber) {
        Pattern pattern = Pattern.compile(REG_CHINA_MOBILE);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean isChinaUnicomNumber(CharSequence phoneNumber) {
        Pattern pattern = Pattern.compile(REG_CHINA_UNICOM);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean isChinaTelecomNumber(CharSequence phoneNumber) {
        Pattern pattern = Pattern.compile(REG_CHINA_TELECOM);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean isChinaInternalNumber(CharSequence phoneNumber) {
        Pattern pattern = Pattern.compile(REG_CHINA_INTERNAL);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

}
