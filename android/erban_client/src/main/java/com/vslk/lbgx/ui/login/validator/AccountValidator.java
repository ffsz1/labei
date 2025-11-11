package com.vslk.lbgx.ui.login.validator;

import android.support.annotation.NonNull;

import com.rengwuxian.materialedittext.validation.RegexpValidator;
import com.tongdaxing.xchat_framework.util.util.StringUtils;

/**
 * Created by zhouxiangfeng on 2017/5/3.
 */

public class AccountValidator extends RegexpValidator {

    String errorStr;

    public AccountValidator(@NonNull String errorMessage, @NonNull String regex) {
        super(errorMessage, regex);
    }

    public static boolean isValidMobileNumber(String phone) {
        if (phone == null || phone.length() != 11 || !phone.startsWith("1"))
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
//            if (!(c == '1')) {
//                errorStr = "手机号码错误";
//                return false;
//            }
//            if (text.length() > 11) {
//                errorStr = "账号长度为11个字符";
//                return false;
//            }
//            if(!MobileNumberUtils.isChinaInternalNumber(text) && !MobileNumberUtils.isChinaMobileNumber(text) && !MobileNumberUtils.isChinaTelecomNumber(text) && !MobileNumberUtils.isChinaUnicomNumber(text)){
//                errorStr = "请填写正确的手机号码";
//                return false;
//            }
        } else {
            errorStr = "账号不能为空！";
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