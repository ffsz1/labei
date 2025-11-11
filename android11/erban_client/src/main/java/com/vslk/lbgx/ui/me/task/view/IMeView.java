package com.vslk.lbgx.ui.me.task.view;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.user.bean.UserInfo;

/**
 * Created by MadisonRong on 04/01/2018.
 */

public interface IMeView extends IMvpBaseView {

    default void updateUserInfoUI(UserInfo userInfo) {
    }

    /**
     * 获取是否绑定过手机
     */
    default void onIsBindPhone() {
    }

    default void onIsBindPhoneFail(String msg) {
    }

    /**
     * 获取是否设置过密码
     */
    default void onIsSetPwd(boolean isSet) {
    }

    default void onIsSetPwdFail(String msg) {
    }

    /**
     * 修改或设置登录密码
     */
    default void modifyPassword() {
    }

    default void modifyPasswordFail(String msg) {
    }

    /**
     * 获取更改绑定手机验证码
     */
    default void getModifyPhoneSMSCodeSuccess() {
    }

    default void getModifyPhoneSMSCodeFail(String msg) {
    }

    /**
     * 换绑手机
     */
    default void onModifyOnBinner() {
    }

    default void onMoidfyOnBinnerFail(String msg) {
    }

    /**
     * 绑定手机
     */
    default void onBinderPhone() {
    }

    default void onBinderPhoneFail(String msg) {
    }

    /**
     * 验证手机
     */
    default void verifierPhone() {
    }

    default void verifierPhoneFail(String msg) {
    }

    /**
     * 提交反馈
     */
    default void commitFeedback() {
    }

    default void commitFeedbackFail(String errorMsg) {
    }

}
