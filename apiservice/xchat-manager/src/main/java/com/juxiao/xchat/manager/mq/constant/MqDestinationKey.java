package com.juxiao.xchat.manager.mq.constant;

/**
 * MQ的目的地关键字
 */
public interface MqDestinationKey {
    /**
     * 用户关注某人
     */
    String LIKE_SOME_BODY_QUEUE = "like-user-queue";

    /**
     * 用户上传照片
     */
    String PHOTO_UPLOAD_QUEUE = "photo-upload-queue";

    /**
     * 用户绑定手机
     */
    String BIND_PHONE_QUEUE = "bind-phone-queue";

    /**
     * 用户更新信息
     */
     String UPDATE_USER_INFO_QUEUE = "update-userinfo-queue";

    /**
     * 邀请用户注册
     */
    String SHARE_REGISTER_QUEUE = "share-register-queue";

    /**
     * 大厅发言
     */
    String SPEAK_IN_PUBLIC_QUEUE = "speak-in-public-queue";

    String GIFT_QUEUE = "gift-queue";

    String BIG_GIFT_QUEUE = "big-gift-queue";

    String CALL_QUEUE = "call-queue";

    String GIFT_PROP_QUEUE = "call-prop-queue";

    /**
     * 分享
     */
    String USER_SHARE_QUEUE = "user-share-queue";

    String CHARGE_QUEUE = "charge-queue";

    String ROOM_MESSAGE_QUEUE = "room-message-queue";

    String USER_DRAW_GIFT = "user_draw_gift";

    String USER_REAL_QUEUE = "user_real_queue";

    /**
     * 捡海螺队列
     */
    String GIFT_DRAW_QUEUE = "gift-draw-queue";

    /**
     * try
     */
    String GIFT_TRYDRAW_QUEUE = "gift-trydraw-queue";

    /**
     * 捡海螺队列
     */
    String GIFT_DY_DRAW_QUEUE = "gift-dy-draw-queue";

//    /** 送礼爆出神秘礼物 */
//    String SEND_MYSTIC_GIFT_QUEUE = "send_mystic_gift_queue";

    /**
     * 延迟5s发送房间魅力值消息
     */
    String ROOM_CHARM_SEND_MSG = "room_charm_send_msg";

    /**
     * 萌币Pk开奖
     */
    String MCOIN_PK_QUEUE = "mcoin_pk_queue";
}