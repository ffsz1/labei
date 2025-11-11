package com.juxiao.xchat.base.constant;

/**
 * 云信推送消息类型：由客户端开发分配
 */
public interface DefMsgType {

    int GiftServiceSend = 14;


    // ---------------micro version2-----------------------
    int Purse = 5;
    int PurseGoldMinus = 51;// 用户金币消费
    int RoomOpen = 6;
    int RoomOpenNotice = 61;// 用户金币消费

    int RoomPk = 19;
    int RoomPkResult = 28;
    int RoomPkCancel = 25;
    int RoomCharm = 33;

    int Packet = 11;
    int PacketFirst = 111;
    int PacketShare = 112;
    int PacketBouns = 114;

    int roomMessage = 16;

    /** 神秘礼物消息 */
    int roomMysticGiftMsg = 31;
    /** 大厅消息 */
    int roomOfficialMsg = 15;

    int moraPK = 34;

    /**
     * 发起
     */
    int moraSend = 1;
    /**
     * 结果
     */
    int moraResult = 2;
    /**
     * 平局
     */
    int moraDraw = 3;

    /**
     * 心愿
     */
    int wishMsg =119;
    int wishTip=120;


    /**
     * 房间PK
     */
    int RoomVs = 20;   //房间PK first
    int RoomVsStart = 21;   //房间PK second   发起PK
    int RoomVsEnd = 22;   //房间PK second   结束PK
    int RoomVsUpdate = 23;   //房间PK second   更新PK信息

    /******************************************************以前的逻辑end ******************************************************/


    int firstUserGiveGold = 51;
    int secondUserGiveGold = 1;
}
