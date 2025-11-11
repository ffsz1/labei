package com.vslk.lbgx.ui.me;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.BaseMvpModel;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.Map;

/**
 * Created by MadisonRong on 04/01/2018.
 * <p>
 */

public class UserMvpModel extends BaseMvpModel {

    public UserMvpModel() {

    }

    public UserInfo getUserDate() {
        long uid = CoreManager.getCore(IAuthCore.class).getCurrentAccount().getUid();
        return CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(uid, true);
    }


    /**
     * 获取绑定手机
     */
    public void isPhones(OkHttpManager.MyCallBack<ServiceResult> callBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        getRequest(UriProvider.isPhones(), param, callBack);
    }

    /**
     * 检测是否设置过密码
     */
    public void checkPwd(OkHttpManager.MyCallBack<Json> callBack) {

        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        postRequest(UriProvider.checkPwd(), param, callBack);
    }

    /**
     * 修改或设置登录密码
     */
    public void modifyPassword(String oldPwd, String newPwd, String confirmPwd, String url,
                               OkHttpManager.MyCallBack<ServiceResult> callBack) {

        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("oldPwd", oldPwd);
        param.put("password", newPwd);
        param.put("confirmPwd", confirmPwd);

        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        postRequest(url, param, callBack);
    }

    /**
     * 获取修改旧手机验证码
     */
    public void getModifyPhoneSMSCode(String phoneNumber, String type,
                                      OkHttpManager.MyCallBack<ServiceResult> callBack) {

        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("phone", phoneNumber);
        param.put("type", type);

        getRequest(UriProvider.getModifyPhoneSMS(), param, callBack);

    }

    /**
     * //绑定手机验证码
     */
    public void getSMSCode(String phone, OkHttpManager.MyCallBack<ServiceResult> callBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("phone", phone);

        getRequest(UriProvider.getSmS(), param, callBack);
    }

    /**
     * 获取验证手机验证码
     */
    public void getPwSmsCode(String phone, OkHttpManager.MyCallBack<ServiceResult> callBack) {

        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("phone", phone);
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        postRequest(UriProvider.getSMSCode(), params, callBack);

    }

    /**
     * 修改绑定手机
     */
    public void modifyBinderPhone(String phone, String smsCode, String url, OkHttpManager.MyCallBack<ServiceResult> callBack) {

        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("phone", phone);
        params.put("smsCode", smsCode);
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());

        postRequest(url, params, callBack);
    }

    /**
     * 绑定手机
     */
    public void bindPhone(String phone, String smsCode, OkHttpManager.MyCallBack<ServiceResult> callBack) {

        Map<String, String> params = CommonParamUtil.getDefaultParam();

        params.put("phone", phone);
        params.put("code", smsCode);
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        postRequest(UriProvider.binderPhone(), params, callBack);

    }

    /**
     * 验证手机
     */
    public void verifierPhone(String phone, String smsCode, OkHttpManager.MyCallBack<ServiceResult> callBack) {

        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("phone", phone);
        param.put("code", smsCode);

        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        getRequest(UriProvider.checkSmsCode(), param, callBack);

    }

    /**
     * 用户提交反馈
     */
    public void commitFeedback(String feedbackDesc, String contact, OkHttpManager.MyCallBack<ServiceResult> callBack) {

        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        params.put("contact", contact);
        params.put("feedbackDesc", feedbackDesc);
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());

        postRequest(UriProvider.commitFeedback(), params, callBack);

    }

}
