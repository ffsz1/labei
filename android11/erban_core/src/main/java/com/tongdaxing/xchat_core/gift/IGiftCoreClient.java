package com.tongdaxing.xchat_core.gift;

import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

import java.util.List;

/**
 * Created by chenran on 2017/7/27.
 */

public interface IGiftCoreClient extends ICoreClient {

    public static final String METHOD_ON_REQUEST_GIFT_LIST = "onRequestGiftList";
    public static final String METHOD_ON_REQUEST_GIFT_LIST_FAIL = "onRequestGiftListFail";

    public static final String METHOD_ON_RECIEVE_GIFT_MSG = "onRecieveGiftMsg";
    public static final String METHOD_ON_MULTI_GIFT_MSG = "onRecieveMultiGiftMsg";
    public static final String METHOD_ON_RECEIVE_PERSONAL_GIFT = "onRecievePersonalGift";
    public static final String METHOD_ON_SEND_PERSONAL_GIFT = "onSendPersonalGift";
    public static final String METHOD_ON_SEND_PERSONAL_GIFT_FAIL = "onSendPersonalGiftFail";
    public static final String METHOD_ON_GIFT_PAST_DUE = "onGiftPastDue";
    public static final String METHOD_ON_GIFT_MYSTERY_NOT_ENOUGH = "onGiftMysteryNotEnough";
    public static final String METHOD_ON_GIFT_SEND_FAIL = "onSendGiftFail";
    String METHOD_ON_SUPER_GIFT_MSG = "onSuperGiftMsg";
    String refreshFreeGift = "refreshFreeGift";

    void refreshFreeGift();

    void onRequestGiftList(List<GiftInfo> giftInfoList);

    void onRequestGiftListFail();

    void onRecievePersonalGift(GiftReceiveInfo giftRecieveInfo);

    void onRecieveGiftMsg(GiftReceiveInfo giftRecieveInfo);

    void onSuperGiftMsg(GiftReceiveInfo giftRecieveInfo);

    void onRecieveMultiGiftMsg(MultiGiftReceiveInfo multiGiftRecieveInfo);

    void onRecieveAllGiftMst();

    void onSendPersonalGift(int giftId, long targetUid);

    void onSendPersonalGiftFail(int code);

    void onGiftPastDue();

    void onGiftMysteryNotEnough();

    void onSendGiftFail(int code,String message);
}
