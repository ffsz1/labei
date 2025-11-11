package com.vslk.lbgx.model.find;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.manager.BaseMvpModel;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.vslk.lbgx.ui.me.UserMvpModel;

import java.util.Map;

/**
 * 创建者      Created by dell
 * 创建时间    2019/1/3
 * 描述
 * <p>
 * 更新者      dell
 * 更新时间
 * 更新描述
 */
public class SoundMatchModel extends BaseMvpModel {

    public void preLoadNextMatchData(OkHttpManager.MyCallBack callBack) {

        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().getRequest(UriProvider.getLink(), param, callBack);
    }

    /**
     * 获取大厅聊天数据
     */
    public void getLobbyChatInfo(OkHttpManager.MyCallBack callBack) {

        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());

        OkHttpManager.getInstance().getRequest(UriProvider.getLobbyChatInfo(), param, callBack);
    }

    /**
     * 获取用户卡片数据
     */
    public void soundMatchRandomUser(OkHttpManager.MyCallBack callBack) {

        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        param.put("minAge", String.valueOf(0));
        param.put("gender", String.valueOf(0));
        param.put("maxAge", String.valueOf(99));

        getRequest(UriProvider.randomUser(), param, callBack);
    }

    /**
     * 获取用户标签云数据
     */
    public void soundMatchCharmUser(OkHttpManager.MyCallBack callBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        UserMvpModel userMvpModel = new UserMvpModel();
        UserInfo userDate = userMvpModel.getUserDate();
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        param.put("gender", userDate != null ? String.valueOf(userDate.getGender()) : String.valueOf(1));
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());

        getRequest(UriProvider.charmUser(), param, callBack);
    }

    /**
     * 喜欢某个用户
     */
    public void soundMatchLikeUser(OkHttpManager.MyCallBack callBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());

        getRequest(UriProvider.likeUser(), param, callBack);
    }

}
