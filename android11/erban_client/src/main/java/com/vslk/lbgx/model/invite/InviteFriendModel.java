package com.vslk.lbgx.model.invite;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.BaseMvpModel;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.Map;

/**
 * 创建者      Created by dell
 * 创建时间    2019/1/4
 * 描述        邀请好友
 * <p>
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class InviteFriendModel extends BaseMvpModel {

    /**
     * 查询服务器的openid与本地的openid是否相同
     */
    public void checkWxOpenIdModel(String openId, String unionId, String accessToken, OkHttpManager.MyCallBack callBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        param.put("openId", openId);
        param.put("unionId", unionId);
        param.put("accessToken", accessToken);
        getRequest(UriProvider.checkWxInfo(), param, callBack);
    }

    /**
     * 获取邀请好友信息
     */
    public void getRedPacket(OkHttpManager.MyCallBack callBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        getRequest(UriProvider.getRedPacket(), param, callBack);
    }

    /**
     * 保存用户的邀请码
     */
    public void saveInviteCode(String inviteCode, OkHttpManager.MyCallBack callBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        param.put("inviteCode", inviteCode);

        postRequest("", param, callBack);
    }

    /**
     * 获取短信验证码
     */
    public void getInviteCode(OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().getRequest(UriProvider.sendSmsCode(), params, myCallBack);
    }


    /**
     * 验证短信验证码
     */
    public void getCheckCode(String code, OkHttpManager.MyCallBack myCallBack) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("code", code);
        OkHttpManager.getInstance().doPostRequest(UriProvider.verificationCode(), params, myCallBack);
    }
}
