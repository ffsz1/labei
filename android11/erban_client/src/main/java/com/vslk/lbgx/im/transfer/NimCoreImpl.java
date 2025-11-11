package com.vslk.lbgx.im.transfer;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.Map;

public class NimCoreImpl implements INimCore {

    private NimCoreImpl() {
    }

    public static NimCoreImpl getInstance() {
        return NimCoreImpl.Holder.sInstance;
    }

//    @Override
//    public void sendTransfer(TransferBean it) {
//        Map<String, String> params = CommonParamUtil.getDefaultParam();
//        String uid = CoreManager.getCore(IAuthCore.class).getCurrentUid() + "";
//        params.put("uid", uid);
//        params.put("recvUid", it.getSessionId());
//        params.put("sendUid", uid);
//        params.put("goldNum", it.getGold());
//        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
//        OkHttpManager.getInstance().doPostRequest(UriProvider.getGold2gold(), params, new OkHttpManager.MyCallBack<ServiceResult<TransUserBean>>() {
//            @Override
//            public void onError(Exception e) {
//
//            }
//
//            @Override
//            public void onResponse(ServiceResult<TransUserBean> response) {
//                if (response.isSuccess()) {
//                    TransferAttachment transferAttachment = new TransferAttachment(CustomAttachment.CUSTOM_MSG_HEADER_TYPE_TRANSFER_FIRST, CustomAttachment.CUSTOM_MSG_HEADER_TYPE_TRANSFER_SECOND);
//                    transferAttachment.setRecvUid(response.getData().getRecvUid());
//                    transferAttachment.setSendUid(response.getData().getSendUid());
//                    transferAttachment.setGoldNum(response.getData().getGoldNum());
//                    transferAttachment.setRecvName(response.getData().getRecvName());
//                    transferAttachment.setRecvAvatar(response.getData().getRecvAvatar());
//                    transferAttachment.setSendName(response.getData().getSendName());
//                    transferAttachment.setSendAvatar(response.getData().getSendAvatar());
//                    CustomMessageConfig customMessageConfig = new CustomMessageConfig();
//                    customMessageConfig.enablePush = false;
//                    IMMessage imMessage = MessageBuilder.createCustomMessage(it.getSessionId() + "", SessionTypeEnum.P2P, "", transferAttachment, customMessageConfig);
//                    it.getContainer().proxy.sendMessage(imMessage);
//                    NimCallback.getInstance().release(NimType.CLOSE, true);
//                } else {
//                    String message = response.getMessage();
//                    ToastUtils.showLong(message + "");
//                    NimCallback.getInstance().release(NimType.CLOSE, false);
//                }
//            }
//        });
//    }

//    @Override
//    public void sendMsgVerify(MsgBean bean) {
//        Map<String, String> params = CommonParamUtil.getDefaultParam();
//        params.put("sendUid", bean.getMessage().getSessionId());
//        params.put("recvUid", bean.getMessage().getFromAccount());
//        String type;
//        if (bean.getMessage().getMsgType() == MsgTypeEnum.text) {
//            type = "10";
//        } else if (bean.getMessage().getMsgType() == MsgTypeEnum.image) {
//            type = "20";
//        } else {
//            type = "10";
//        }
//        params.put("type", type);
//        params.put("content", bean.getMessage().getContent());
//        String uid = CoreManager.getCore(IAuthCore.class).getCurrentUid() + "";
//        params.put("uid", uid);
//        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
//        OkHttpManager.getInstance().doPostRequest(UriProvider.chatPrivate(), params, new OkHttpManager.MyCallBack<Json>() {
//            @Override
//            public void onError(Exception e) {
//
//            }
//
//            @Override
//            public void onResponse(Json response) {
//                JSONObject obj = JSON.parseObject(response.toString());
//                int code = obj.getIntValue("code");
//                if (code == 200) {
//                    bean.getListener().onMsgVerify(bean.getMessage());
//                } else {
//                    String msg = obj.getString("message");
//                    ToastUtils.showLong(msg);
//                }
//            }
//        });
//    }

    @Override
    public void givegoldcheck(INimCallbackListener<Boolean> listener) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.givegoldcheck(), params, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                listener.success(true);
            }

            @Override
            public void onResponse(Json response) {
                listener.success(true);
//                JSONObject obj = JSON.parseObject(response.toString());
//                int code = obj.getIntValue("code");
//                if (code == 200) {
//                    Boolean data = obj.getBoolean("data");
//                    listener.success(data);
//                } else {
//                    listener.success(false);
//                }
            }
        });
    }

    public interface INimCallbackListener<T> {
        void success(T data);
    }

    private static class Holder {
        private static final NimCoreImpl sInstance = new NimCoreImpl();
    }

    public INimCore get() {
        return this;
    }

}
