package com.vslk.lbgx.ui.login.validator;

import android.support.annotation.NonNull;

import com.rengwuxian.materialedittext.validation.RegexpValidator;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

/**
 * Created by zhouxiangfeng on 2017/5/3.
 */

public class SMSCodeValidator extends RegexpValidator {

    String errorStr;

    public SMSCodeValidator(@NonNull String errorMessage, @NonNull String regex) {
        super(errorMessage, regex);
    }

    public static boolean isValidMobileNumber(String phone) {
        if (phone == null || phone.length() != 5 )
            return false;

        if (!isAllDigits(phone))
            return false;

        return true;
    }

    public static boolean isAllDigits(String str) {
        if (StringUtils.isEmpty(str)) return false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
        if (!isEmpty) {
            char c = text.charAt(0);

            if (text.length() != 5) {
                errorStr = "验证码长度为5个字符";
                return false;
            }

        } else {
            errorStr = "验证码不能为空！";
            return false;
        }
        return super.isValid(text, isEmpty);
    }

    @NonNull
    @Override
    public String getErrorMessage() {
        return errorStr;
    }
}