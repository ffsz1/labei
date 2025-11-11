package com.tongdaxing.xchat_core.gift;

import com.netease.nim.uikit.session.module.Container;
import com.tongdaxing.xchat_core.bean.ChatRoomMessage;
import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by chenran on 2017/7/27.
 */

public interface IGiftCore extends IBaseCore {
    List<GiftInfo> getGiftInfosByType(int type);

    List<GiftInfo> getGiftInfosByType2And3();

    void sendLotteryMeg(GiftInfo giftInfo, int count);

    void sendWanFaMeg(String roomDesc,String roomNotice,long roomId);

    void sendRoomNameMeg(long uid,String roomName,long roomId);

    void sendCallGiftMeg(long uid, String giftName, String nick, String targetNick, String giftPic, long roomId);

    /**
     * 房间单人送礼
     */
    void sendRoomGift(int giftId, long targetUid, long roomUid, int giftNum, int goldPrice, int currentP);

    /**
     * 房间全麦送礼
     */
    void sendRoomMultiGift(int giftId, List<Long> targetUids, long roomUid, int giftNum, int goldPrice, int currentP);

    /**
     * 云信发P2P礼物专用
     */
    void sendPersonalGiftToNIM(int giftId, long targetUid, int giftNum, int goldPrice, WeakReference<Container> container, int currentP);

    GiftInfo findGiftInfoById(int giftId);

    void requestGiftInfos();

    void onReceiveChatRoomMessages(List<ChatRoomMessage> chatRoomMessageList);
}
