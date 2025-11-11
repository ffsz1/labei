package com.tongdaxing.xchat_core.praise;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.RoomTipAttachment;
import com.tongdaxing.xchat_core.im.room.IIMRoomCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by zhouxiangfeng on 2017/5/18.
 */

public class PraiseCoreImpl extends AbstractBaseCore implements IPraiseCore {

    public PraiseCoreImpl() {
        CoreManager.addClient(this);
    }

    @Override
    public void praise(final long likedUid) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("type", String.valueOf(1));
        params.put("likedUid", likedUid + "");

        OkHttpManager.getInstance().doPostRequest(UriProvider.praise(), params, new OkHttpManager.MyCallBack<ServiceResult>() {
            @Override
            public void onError(Exception e) {
                notifyClients(IPraiseClient.class, IPraiseClient.METHOD_ON_PRAISE_FAITH, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response.isSuccess()) {
                    RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
                    if (roomInfo != null && roomInfo.getUid() == likedUid) {
                        sendAttentionRoomTipMsg(likedUid);
                    }
                    notifyClients(IPraiseClient.class, IPraiseClient.METHOD_ON_PRAISE, likedUid);
                } else {
                    notifyClients(IPraiseClient.class, IPraiseClient.METHOD_ON_PRAISE_FAITH, response.getMessage());
                }

            }
        });
    }

    @Override
    public void cancelPraise(final long canceledUid, final boolean showNotice) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("type", String.valueOf(2));
        params.put("likedUid", canceledUid + "");

        OkHttpManager.getInstance().doPostRequest(UriProvider.praise(), params, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IPraiseClient.class, IPraiseClient.METHOD_ON_CANCELED_PRAISE_FAITH, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null && response.isSuccess()) {
                    notifyClients(IPraiseClient.class, IPraiseClient.METHOD_ON_CANCELED_PRAISE, canceledUid, showNotice);
                } else {
                    notifyClients(IPraiseClient.class, IPraiseClient.METHOD_ON_CANCELED_PRAISE_FAITH, response.getMessage());
                }
            }
        });
    }

    @Override
    public void deleteLike(final long deletedUid) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("likedUid", deletedUid + "");

        OkHttpManager.getInstance().doPostRequest(UriProvider.deleteLike(), params, new OkHttpManager.MyCallBack<ServiceResult>() {
            @Override
            public void onError(Exception e) {
                notifyClients(IPraiseClient.class, IPraiseClient.METHOD_ON_CANCELED_PRAISE_FAITH, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IPraiseClient.class, IPraiseClient.METHOD_ON_CANCELED_PRAISE, deletedUid);
                    } else {
                        notifyClients(IPraiseClient.class, IPraiseClient.METHOD_ON_CANCELED_PRAISE_FAITH, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void getAllFans(long uid, int pageSize, int pageNo) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("pageSize", String.valueOf(pageSize));
        params.put("pageNo", String.valueOf(pageNo));

        OkHttpManager.getInstance().doPostRequest(UriProvider.getAllFans(), params, new OkHttpManager.MyCallBack<ServiceResult<List<UserInfo>>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IPraiseClient.class, IPraiseClient.METHOD_ON_GET_ALL_FANS_FAITH, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<List<UserInfo>> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IPraiseClient.class, IPraiseClient.METHOD_ON_GET_ALL_FANS, response.getData());
                    } else {
                        notifyClients(IPraiseClient.class, IPraiseClient.METHOD_ON_GET_ALL_FANS_FAITH, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void isPraised(long uid, final long isLikeUid) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(uid));
        params.put("isLikeUid", String.valueOf(isLikeUid));

        OkHttpManager.getInstance().getRequest(UriProvider.isLike(), params, new OkHttpManager.MyCallBack<ServiceResult<Boolean>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IPraiseClient.class, IPraiseClient.METHOD_ON_ISLIKED_FAITH, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<Boolean> response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        if (null != response.getData()) {
                            notifyClients(IPraiseClient.class, IPraiseClient.METHOD_ON_ISLIKED, response.getData(), isLikeUid);
                        } else {
                            notifyClients(IPraiseClient.class, IPraiseClient.METHOD_ON_ISLIKED_FAITH, response.getMessage());
                        }
                    } else {
                        notifyClients(IPraiseClient.class, IPraiseClient.METHOD_ON_ISLIKED_FAITH, response.getMessage());
                    }
                }
            }
        });
    }

    private void sendAttentionRoomTipMsg(long targetUid) {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(targetUid);
        if (roomInfo != null && userInfo != null) {
            long myUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
            UserInfo myUserInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(myUid);

            RoomTipAttachment roomTipAttachment = new RoomTipAttachment(IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_ROOM_TIP, IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_ROOM_TIP_ATTENTION_ROOM_OWNER);
            roomTipAttachment.setUid(myUid);
            roomTipAttachment.setNick(myUserInfo.getNick());
            roomTipAttachment.setTargetUid(targetUid);
            roomTipAttachment.setTargetNick(userInfo.getNick());

            roomTipAttachment.setCharmLevel(userInfo.getCharmLevel());
            roomTipAttachment.setExperLevel(userInfo.getExperLevel());

            ChatRoomMessage message = new ChatRoomMessage();
            message.setAttachment(roomTipAttachment);
            message.setRoom_id(roomInfo.getRoomId()+"");
            CoreManager.getCore(IIMRoomCore.class).sendMessage(message);
        }
    }
}
