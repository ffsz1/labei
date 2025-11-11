package com.vslk.lbgx.ui.message.msgsend;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.Map;

public class MsgSendCoreImpl implements IMsgSendCore {

    private MsgSendCoreImpl() {
    }

    public static MsgSendCoreImpl getInstance() {
        return Holder.sInstance;
    }

    @Override
    public void onSendMsg(final IMMessage message, final IMsgCallBackListener<IMMessage> listener) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("sendUid", message.getSessionId());
        params.put("recvUid", message.getFromAccount());
        String type = "10";
        if (message.getMsgType() == MsgTypeEnum.text) {
            type = "10";
        } else if (message.getMsgType() == MsgTypeEnum.image) {
            type = "20";
        }
        params.put("type", type);
        params.put("content", message.getContent());
        OkHttpManager.getInstance().doPostRequest(UriProvider.IM_SERVER_URL + "/monitor/chat/private", params, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(Json response) {
                JSONObject obj = JSON.parseObject(response.toString());
                int code = obj.getIntValue("code");
                if (code == 200) {
                    listener.onSuccess(message);
                } else {
                    String msg = obj.getString("message");
                    ToastUtils.showLong(msg);
                }
            }
        });
    }


    private static class Holder {
        private static final MsgSendCoreImpl sInstance = new MsgSendCoreImpl();
    }

    public IMsgSendCore getIMsgSendCore() {
        return this;
    }

    public interface IMsgCallBackListener<T> {
        void onSuccess(T response);
    }
}
