package com.vslk.lbgx.ui.me.setting.model;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.BaseMvpModel;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.Map;

/**
 * Function:
 * Author: Edward on 2019/4/11
 */
public class BindingQQModel extends BaseMvpModel {
    public void bindingThird(int type, String openId, String unionId, String accessToken, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        String uid = CoreManager.getCore(IAuthCore.class).getCurrentUid() + "";
        params.put("type", String.valueOf(type));
        params.put("openId", openId);
        params.put("unionId", unionId);
        params.put("accessToken", accessToken);
        params.put("uid", uid);
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.bindingThird(), params, myCallBack);
    }

    public void sendSmsCode(OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        String uid = CoreManager.getCore(IAuthCore.class).getCurrentUid() + "";
        params.put("uid", uid);
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().getRequest(UriProvider.sendSmsCode(), params, myCallBack);
    }

    public void verificationCodeModel(String code, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        String uid = CoreManager.getCore(IAuthCore.class).getCurrentUid() + "";
        params.put("uid", uid);
        params.put("code", code);
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.verificationCode(), params, myCallBack);
    }

    public void unbindingThird(int type, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        String uid = CoreManager.getCore(IAuthCore.class).getCurrentUid() + "";
        params.put("type", String.valueOf(type));
        params.put("uid", uid);
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.unbindingThird(), params, myCallBack);
    }
}
