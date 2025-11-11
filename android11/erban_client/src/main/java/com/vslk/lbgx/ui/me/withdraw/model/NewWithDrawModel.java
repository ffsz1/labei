package com.vslk.lbgx.ui.me.withdraw.model;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.BaseMvpModel;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.Map;

/**
 * Function:
 * Author: Edward on 2019/4/15
 */
public class NewWithDrawModel extends BaseMvpModel {

    /**
     * 获取微信验证码
     */
    public void getInviteCode(OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().getRequest(UriProvider.sendSmsCode(), params, myCallBack);
    }

    /**
     * 验证微信验证码
     */
    public void getCheckCode(String code, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("code", code);
        OkHttpManager.getInstance().doPostRequest(UriProvider.verificationCode(), params, myCallBack);
    }

    /**
     * 绑定微信支付账号
     */
    public void bindWithdrawWeixin(String accessToken, String openId, String unionId, int type, OkHttpManager.MyCallBack myCallBack) {

        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("accessToken", accessToken);
        params.put("openId", openId);
        params.put("unionId", unionId);
        params.put("type", String.valueOf(type));
        OkHttpManager.getInstance().doPostRequest(UriProvider.bindingThird(), params, myCallBack);
    }

    /**
     * 获取提现列表
     */
    public void getWithdrawList(OkHttpManager.MyCallBack callBack) {
        Map<String, String> requestParam = CommonParamUtil.getDefaultParam();

        requestParam.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        requestParam.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        OkHttpManager.getInstance().getRequest(UriProvider.getWithdrawList(), requestParam, callBack);
    }

    /**
     * 获取提现页用户信息
     */
    public void getWithdrawUserInfo(OkHttpManager.MyCallBack callBack) {
        Map<String, String> requestParam = CommonParamUtil.getDefaultParam();

        requestParam.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        requestParam.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        OkHttpManager.getInstance().getRequest(UriProvider.getWithdrawInfo(), requestParam, callBack);
    }

    /**
     * 发起提现
     */
    public void requestExchange(int pid, int type, OkHttpManager.MyCallBack callBack) {
        Map<String, String> requestParam = CommonParamUtil.getDefaultParam();

        requestParam.put("pid", String.valueOf(pid));
        requestParam.put("type", String.valueOf(type));
        requestParam.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        requestParam.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        OkHttpManager.getInstance().doPostRequest(UriProvider.requestExchange(), requestParam, callBack);
    }

    /**
     * 获取绑定支付宝验证码
     */
    public void getSmsCode(OkHttpManager.MyCallBack callBack) {
        Map<String, String> requestParam = CommonParamUtil.getDefaultParam();

        requestParam.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        requestParam.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        OkHttpManager.getInstance().getRequest(UriProvider.getSms(), requestParam, callBack);
    }

    /**
     * 绑定支付宝
     */
    public void binderAlipay(String aliPayAccount, String aliPayAccountName, String code, OkHttpManager.MyCallBack callBack) {
        Map<String, String> requestParam = CommonParamUtil.getDefaultParam();
        requestParam.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        requestParam.put("aliPayAccount", aliPayAccount);
        requestParam.put("aliPayAccountName", aliPayAccountName);
        requestParam.put("code", code);
        requestParam.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.binder(), requestParam, callBack);
    }

}
