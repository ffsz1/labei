package com.tongdaxing.xchat_core.redpacket;

import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.RedPacketAttachment;
import com.tongdaxing.xchat_core.im.message.IIMMessageCoreClient;
import com.tongdaxing.xchat_core.redpacket.bean.ActionDialogInfo;
import com.tongdaxing.xchat_core.redpacket.bean.RedDrawListInfo;
import com.tongdaxing.xchat_core.redpacket.bean.RedPacketInfo;
import com.tongdaxing.xchat_core.redpacket.bean.WithdrawRedListInfo;
import com.tongdaxing.xchat_core.redpacket.bean.WithdrawRedSucceedInfo;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by ${Seven} on 2017/9/20.
 */

public class RedPacketCoreImpl extends AbstractBaseCore implements IRedPacketCore {
    public RedPacketCoreImpl() {
        CoreManager.addClient(this);
    }

    //获取红包页面数据
    @Override
    public void getRedPacketInfo() {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        OkHttpManager.getInstance().getRequest(UriProvider.getRedPacket(), params, new OkHttpManager.MyCallBack<ServiceResult<RedPacketInfo>>() {
            @Override
            public void onError(Exception e) {
                notifyClients(IRedPacketCoreClient.class, IRedPacketCoreClient.METHOD_ON_GET_RED_INFO_ERROR, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<RedPacketInfo> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IRedPacketCoreClient.class, IRedPacketCoreClient.METHOD_ON_GET_RED_INFO, response.getData());
                    } else {
                        notifyClients(IRedPacketCoreClient.class, IRedPacketCoreClient.METHOD_ON_GET_RED_INFO_ERROR, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void getActionDialog(int type) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("type", String.valueOf(type));
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        OkHttpManager.getInstance().getRequest(UriProvider.getRedBagDialogType(), params, new OkHttpManager.MyCallBack<ServiceResult<List<ActionDialogInfo>>>() {
            @Override
            public void onError(Exception e) {
                notifyClients(IRedPacketCoreClient.class, IRedPacketCoreClient.METHOD_ON_GET_ACTION_DIALOG_ERROR, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<List<ActionDialogInfo>> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IRedPacketCoreClient.class, IRedPacketCoreClient.METHOD_ON_GET_ACTION_DIALOG, response.getData());
                    } else {
                        notifyClients(IRedPacketCoreClient.class, IRedPacketCoreClient.METHOD_ON_GET_ACTION_DIALOG_ERROR, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void getRedList() {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().getRequest(UriProvider.getRedBagList(), params, new OkHttpManager.MyCallBack<ServiceResult<List<WithdrawRedListInfo>>>() {
            @Override
            public void onError(Exception e) {
                notifyClients(IRedPacketCoreClient.class, IRedPacketCoreClient.METHOD_ON_GET_RED_LIST_ERROR, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<List<WithdrawRedListInfo>> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IRedPacketCoreClient.class, IRedPacketCoreClient.METHOD_ON_GET_RED_LIST, response.getData());
                    } else {
                        notifyClients(IRedPacketCoreClient.class, IRedPacketCoreClient.METHOD_ON_GET_RED_LIST_ERROR, response.getMessage());
                    }
                }
            }
        });

    }

    @Override
    public void getRedWithdraw(long uid, int packetId) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(uid));
        params.put("packetId", String.valueOf(packetId));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.getRedWithdraw(), params, new OkHttpManager.MyCallBack<ServiceResult<WithdrawRedSucceedInfo>>() {
            @Override
            public void onError(Exception e) {
                notifyClients(IRedPacketCoreClient.class, IRedPacketCoreClient.METHOD_ON_GET_WITHDRAW_ERROR, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<WithdrawRedSucceedInfo> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IRedPacketCoreClient.class, IRedPacketCoreClient.METHOD_ON_GET_WITHDRAW, response.getData());
                    } else {
                        notifyClients(IRedPacketCoreClient.class, IRedPacketCoreClient.METHOD_ON_GET_WITHDRAW_ERROR, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void getRedDrawList() {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        OkHttpManager.getInstance().getRequest(UriProvider.getRedDrawList(), params, new OkHttpManager.MyCallBack<ServiceResult<List<RedDrawListInfo>>>() {
            @Override
            public void onError(Exception e) {
                notifyClients(IRedPacketCoreClient.class, IRedPacketCoreClient.METHOD_ON_GET_RED_DRAW_LIST_ERROR, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<List<RedDrawListInfo>> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IRedPacketCoreClient.class, IRedPacketCoreClient.METHOD_ON_GET_RED_DRAW_LIST, response.getData());
                    } else {
                        notifyClients(IRedPacketCoreClient.class, IRedPacketCoreClient.METHOD_ON_GET_RED_DRAW_LIST_ERROR, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void getRedWithdraw(long uid, int packetId, String openId) {

        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(uid));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("packetId", String.valueOf(packetId));
        params.put("type", String.valueOf(1));
        params.put("openId", String.valueOf(openId));
        OkHttpManager.getInstance().doPostRequest(UriProvider.getRedWithdrawSec(), params,
                new OkHttpManager.MyCallBack<ServiceResult<WithdrawRedSucceedInfo>>() {
                    @Override
                    public void onError(Exception e) {
                        notifyClients(IRedPacketCoreClient.class, IRedPacketCoreClient.METHOD_ON_GET_WITHDRAW_ERROR, e.getMessage());
                    }

                    @Override
                    public void onResponse(ServiceResult<WithdrawRedSucceedInfo> response) {
                        if (null != response) {
                            if (response.isSuccess()) {
                                notifyClients(IRedPacketCoreClient.class, IRedPacketCoreClient.METHOD_ON_GET_WITHDRAW, response.getData());
                            } else {
                                notifyClients(IRedPacketCoreClient.class, IRedPacketCoreClient.METHOD_ON_GET_WITHDRAW_ERROR, response.getMessage());
                            }
                        }
                    }
                });
    }

    @CoreEvent(coreClientClass = IIMMessageCoreClient.class)
    public void onReceivePersonalMessages(List<IMMessage> imMessages) {
        if (imMessages != null && imMessages.size() > 0) {
            for (IMMessage msg : imMessages) {
                if (msg.getMsgType() == MsgTypeEnum.custom) {
                    IMCustomAttachment attachment = (IMCustomAttachment) msg.getAttachment();
                    if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_PACKET) {
                        RedPacketAttachment redPacketAttachment = (RedPacketAttachment) msg.getAttachment();
                        notifyClients(IRedPacketCoreClient.class, IRedPacketCoreClient.METHOD_ON_RECEIVE_NEW_PACKET, redPacketAttachment.getRedPacketInfo());
                    }
                }
            }
        }
    }
}
