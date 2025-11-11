package com.vslk.lbgx.ui.login.validator;

import android.support.annotation.NonNull;

import com.rengwuxian.materialedittext.validation.RegexpValidator;

/**
 * Created by zhouxiangfeng on 2017/5/3.
 */

public class PasswordValidator extends RegexpValidator {

    String errorStr;

    public PasswordValidator(@NonNull String errorMessage, @NonNull String regex) {
        super(errorMessage, regex);
    }

    @Override
    public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
        if(!isEmpty){
            if(16 >= text.length() && text.length() >= 6){
                return true;
            }else{
                errorStr = "密码长度6-16个字符";
                return false;
            }
        }else{
            errorStr = "密码不能为空！";
        }
        return super.isValid(text, isEmpty);
    }

    @NonNull
    @Override
    public String getErrorMessage() {
        return errorStr;
    }
}