package com.vslk.lbgx.utils;

import android.content.Context;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;

import java.util.Map;

public class HttpUtil {


    /**
     * 保存关注消息发送状态
     *
     * @param uid
     * @param likedSend
     */
    public static void saveFocusMsgState(long uid, int likedSend) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", uid + "");
        params.put("likedSend", likedSend + "");
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.saveFocusMsgSwitch(), params, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                SingleToastUtil.showToast(e.getMessage());
            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200) {
                    SingleToastUtil.showToast(likedSend == 1 ? "已开启消息通知" : "已关闭消息通知");
                } else {
                    SingleToastUtil.showToast(response != null ? response.str("message") : "数据异常");
                }
            }
        });
    }

    /**
     * 验证用户是否可以和对方进行私聊
     * @param context
     * @param isFriend
     * @param userId
     */
    public static void checkUserIsDisturb(Context context,boolean isFriend, long userId) {
        if (isFriend){
            SessionHelper.startPrivateChat(context,userId);
            return;
        }
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("queryUid", userId + "");
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.getFocusMsgSwitch(), params, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                SingleToastUtil.showToast("网络异常，请重试！");
            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200) {
                    Json json = response.json("data");
                    if (json != null) {
                        int chatPermission = json.num("chatPermission");
                        if (chatPermission == 1){
                            SingleToastUtil.showToast("对方已设置私信免打扰");
                            return;
                        }
                        if (chatPermission == 2){
                            UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheLoginUserInfo();
                            if (userInfo == null|| userInfo.getExperLevel() < 10){
                                SingleToastUtil.showToast("对方已设置私信免打扰");
                                return;
                            }
                        }
                        SessionHelper.startPrivateChat(context,userId);
                    }
                } else {
                    SingleToastUtil.showToast("进入私聊失败！");
                }
            }
        });
    }
}
