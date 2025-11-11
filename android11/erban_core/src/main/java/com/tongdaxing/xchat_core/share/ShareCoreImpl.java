package com.tongdaxing.xchat_core.share;

import static com.tongdaxing.xchat_core.share.IShareCoreClient.SHARE_SHORT_SHARE_UID;
import static com.tongdaxing.xchat_core.share.IShareCoreClient.SHARE_SHORT_UID;
import static com.tongdaxing.xchat_core.share.IShareCoreClient.SHARE_SHORT_URL;

import com.netease.nim.uikit.common.util.log.LogUtil;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.R;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.RoomTipAttachment;
import com.tongdaxing.xchat_core.im.room.IIMRoomCore;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.redpacket.bean.ShareRedBagInfo;
import com.tongdaxing.xchat_core.redpacket.bean.WebViewInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by chenran on 2017/8/14.
 */

public class ShareCoreImpl extends AbstractBaseCore implements IShareCore {

    public ShareCoreImpl() {
        CoreManager.addClient(this);
    }

    @Override
    public void shareH5(final WebViewInfo webViewInfo, Platform platform) {
        if (null != webViewInfo && platform != null) {
            Platform.ShareParams sp = new Platform.ShareParams();
            sp.setText(webViewInfo.getDesc());
            sp.setTitle(webViewInfo.getTitle());
            sp.setImageUrl(webViewInfo.getImgUrl());
            //QQ空间分享
            sp.setSite(webViewInfo.getDesc());
            sp.setSiteUrl(webViewInfo.getShowUrl() + SHARE_SHORT_SHARE_UID + String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
            //QQ分享
            sp.setTitleUrl(webViewInfo.getShowUrl() + SHARE_SHORT_SHARE_UID + String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
            //微信朋友圈分享
            sp.setUrl(webViewInfo.getShowUrl() + SHARE_SHORT_SHARE_UID + String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
            sp.setShareType(Platform.SHARE_WEBPAGE);
            platform.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    String url = UriProvider.getLotteryActivityPage();
                    if (url.contains("/mm/luckdraw/index.html") &&
                            webViewInfo.getShowUrl() != null &&
                            webViewInfo.getShowUrl().contains("/mm/luckdraw/index.html")) {
                        reportShare(888, platform);
                    } else {
                        reportShare(1, platform);
                    }
                    notifyClients(IShareCoreClient.class, IShareCoreClient.METHOD_ON_SHARE_H5, webViewInfo.getShowUrl());
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    System.out.println("i = " + i);
                    System.out.println("error = " + throwable.getMessage());
                    notifyClients(IShareCoreClient.class, IShareCoreClient.METHOD_ON_HSARE_H5_FAIL);
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    notifyClients(IShareCoreClient.class, IShareCoreClient.METHOD_ON_HSARE_H5_CANCEL);
                }
            });
            platform.share(sp);
        }
    }

    @Override
    public void sharePage(WebViewInfo webViewInfo, Platform platform) {
        shareH5(webViewInfo, platform);
    }

    @Override
    public void shareRoom(Platform platform, final long roomUid, String title) {
        //房间主人信息
        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(roomUid);
        //无UI API
        if (userInfo != null && platform != null) {
            Platform.ShareParams sp = new Platform.ShareParams();
            String nick = userInfo.getNick();
            if (nick.length() >= 5) {
                nick = nick.substring(0, 5).concat("...");
            }
            sp.setText(getContext().getString(R.string.share_room_site));
            sp.setTitle("我在"+title+"连麦互动，来和我一起快乐呀");
            sp.setImageUrl(userInfo.getAvatar());
            //QQ空间分享
            sp.setSite(getContext().getString(R.string.share_room_site));
            String uid = String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid());
            String roomuid = String.valueOf(roomUid);
            sp.setSiteUrl(UriProvider.IM_SERVER_URL + SHARE_SHORT_URL + uid + SHARE_SHORT_UID + roomuid);
            //QQ分享
            sp.setTitleUrl(UriProvider.IM_SERVER_URL + SHARE_SHORT_URL + uid + SHARE_SHORT_UID + roomuid);
            //微信朋友圈分享
            sp.setUrl(UriProvider.IM_SERVER_URL + SHARE_SHORT_URL + uid + SHARE_SHORT_UID + roomuid);
            sp.setShareType(Platform.SHARE_WEBPAGE);


            platform.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    sendShareRoomTipMsg(roomUid);
                    reportShare(1, platform);
                    notifyClients(IShareCoreClient.class, IShareCoreClient.METHOD_ON_SHARE_ROOM);
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    notifyClients(IShareCoreClient.class, IShareCoreClient.METHOD_ON_SHARE_ROOM_FAIL);
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    notifyClients(IShareCoreClient.class, IShareCoreClient.METHOD_ON_SHARE_ROOM_CANCEL);
                }
            });
            platform.share(sp);
        }
    }

    @Override
    public void reportShare(int sharePageId, Platform platform) {
        LogUtil.d("reportShare", "1");
        int shareType = 0;
        if (platform.getName().equals(Wechat.NAME)) {
            shareType = 1;
        } else if (platform.getName().equals(WechatMoments.NAME)) {
            shareType = 2;
        } else if (platform.getName().equals(QQ.NAME)) {
            shareType = 3;
        } else if (platform.getName().equals(QZone.NAME)) {
            shareType = 4;
        }
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("shareType", String.valueOf(shareType));
        params.put("sharePageId", sharePageId + "");
        params.put("token", CoreManager.getCore(IAuthCore.class).getTicket());

        OkHttpManager.getInstance().doPostRequest(UriProvider.getShareRedPacket(), params, new OkHttpManager.MyCallBack<ServiceResult<ShareRedBagInfo>>() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(ServiceResult<ShareRedBagInfo> response) {
                if (response != null && response.isSuccess()) {
                    notifyClients(IShareCoreClient.class, IShareCoreClient.METHOD_ON_SHARE_REPORT);
                }
            }
        });
    }

    private void sendShareRoomTipMsg(long targetUid) {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(targetUid);
        if (roomInfo != null && userInfo != null) {
            long myUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
            UserInfo myUserInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(myUid);

            RoomTipAttachment roomTipAttachment = new RoomTipAttachment(IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_ROOM_TIP, IMCustomAttachment.CUSTOM_MSG_SUB_TYPE_ROOM_TIP_SHARE_ROOM);
            roomTipAttachment.setUid(myUid);
            roomTipAttachment.setNick(myUserInfo.getNick());
            roomTipAttachment.setTargetUid(targetUid);
            roomTipAttachment.setTargetNick(userInfo.getNick());
            ChatRoomMessage message = new ChatRoomMessage();
            message.setRoom_id(roomInfo.getRoomId()+"");
            message.setAttachment(roomTipAttachment);
            CoreManager.getCore(IIMRoomCore.class).sendMessage(message);
        }
    }


}
