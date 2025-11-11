package com.tongdaxing.xchat_core.im.custom.bean;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

/**
 * 先定义一个自定义消息附件的基类，负责解析你的自定义消息的公用字段，比如类型等等。
 *
 * @author zhouxiangfeng
 * @date 2017/6/8
 */
public class CustomAttachment implements MsgAttachment {
    /**
     * 自定义消息附件的类型，根据该字段区分不同的自定义消息
     */
    protected int first;
    protected int second;
    protected long time;
    protected JSONObject data;

    public static final int CUSTOM_MSG_HEADER_TYPE_AUCTION = 1;
    public static final int CUSTOM_MSG_SUB_TYPE_AUCTION_START = 11;
    public static final int CUSTOM_MSG_SUB_TYPE_AUCTION_FINISH = 12;
    public static final int CUSTOM_MSG_SUB_TYPE_AUCTION_UPDATE = 13;

    public static final int CUSTOM_MSG_HEADER_TYPE_ROOM_TIP = 2;
    public static final int CUSTOM_MSG_SUB_TYPE_ROOM_TIP_SHARE_ROOM = 21;
    public static final int CUSTOM_MSG_SUB_TYPE_ROOM_TIP_ATTENTION_ROOM_OWNER = 22;

    public static final int CUSTOM_MSG_HEADER_TYPE_GIFT = 3;
    public static final int CUSTOM_MSG_SUB_TYPE_SEND_GIFT = 31;

    public static final int CUSTOM_MSG_HEADER_TYPE_ACCOUNT = 5;

    public static final int CUSTOM_MSG_HEADER_TYPE_OPEN_ROOM_NOTI = 6;

    public static final int CUSTOM_MSG_HEADER_TYPE_QUEUE = 8;
    public static final int CUSTOM_MSG_HEADER_TYPE_QUEUE_INVITE = 81;
    public static final int CUSTOM_MSG_HEADER_TYPE_QUEUE_KICK = 82;
    //屏蔽小礼物特效
    public static final int CUSTOM_MSG_HEADER_TYPE_SUB_GIFT_EFFECT_OPEN = 155;
    public static final int CUSTOM_MSG_HEADER_TYPE_SUB_GIFT_EFFECT_CLOSE = 156;
    //公屏消息开关
    public static final int CUSTOM_MSG_HEADER_TYPE_REMOVE_MSG_FILTER_OPEN = 153;
    public static final int CUSTOM_MSG_HEADER_TYPE_REMOVE_MSG_FILTER_CLOSE = 154;

    public static final int CUSTOM_MSG_HEADER_TYPE_FACE = 9;
    public static final int CUSTOM_MSG_SUB_TYPE_FACE_SEND = 91;

    public static final int CUSTOM_MSG_HEADER_TYPE_NOTICE = 10;


    public static final int CUSTOM_MSG_HEADER_TYPE_PACKET = 11;
    public static final int CUSTOM_MSG_SUB_TYPE_PACKET_FIRST = 111;

    public static final int CUSTOM_MSG_HEADER_TYPE_MULTI_GIFT = 12;
    public static final int CUSTOM_MSG_HEADER_TYPE_SUPER_GIFT = 14;
    public static final int CUSTOM_MSG_SUB_TYPE_SEND_MULTI_GIFT = 121;
    public static final int CUSTOM_MSG_SUB_PUBLIC_CHAT_ROOM = 15;

    public static final int CUSTOM_MSG_LOTTERY_BOX = 16;
    public static final int CUSTOM_MSG_MIC_IN_LIST = 17;


    //转盘抽奖
    public static final int CUSTOM_MSG_HEADER_TYPE_LOTTERY = 13;
    public static final int CUSTOM_MSG_SUB_TYPE_NOTI_LOTTERY = 131;

    //（龙珠）速配
    public static final int CUSTOM_MSG_HEADER_TYPE_MATCH =  18;
    public static final int CUSTOM_MSG_HEADER_TYPE_MATCH_SPEED =  23;
    public static final int CUSTOM_MSG_HEADER_TYPE_MATCH_CHOICE =  24;

    //PK消息
    public static final int CUSTOM_MSG_HEADER_TYPE_PK_FIRST= 19;
    public static final int CUSTOM_MSG_HEADER_TYPE_PK_SECOND_START = 27;//开始
    public static final int CUSTOM_MSG_HEADER_TYPE_PK_SECOND_END = 28;//结束
    public static final int CUSTOM_MSG_HEADER_TYPE_PK_SECOND_CANCEL = 25;//取消
    public static final int CUSTOM_MSG_HEADER_TYPE_PK_SECOND_ADD = 26;//投票

    //房间邀请好友消息
    public static final int CUSTOM_MSG_SHARE_FANS = 20;

    //房间内规则
    public static final int CUSTOM_MSG_TYPE_RULE_FIRST = 4;

    //爆出礼物的消息
    public static final int CUSTOM_MSG_TYPE_BURST_GIFT = 31;//first和second一致

    public CustomAttachment() {

    }

    //财富等级
    protected int experLevel;
    //魅力等级
    protected int charmLevel;

    public int getExperLevel() {
        return experLevel;
    }

    public void setExperLevel(int experLevel) {
        this.experLevel = experLevel;
    }

    public int getCharmLevel() {
        return charmLevel;
    }

    public void setCharmLevel(int charmLevel) {
        this.charmLevel = charmLevel;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public CustomAttachment(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    // 解析附件内容。
    public void fromJson(JSONObject data) {
        if (data != null) {
            parseData(data);
        }
    }

    // 实现 MsgAttachment 的接口，封装公用字段，然后调用子类的封装函数。
    @Override
    public String toJson(boolean send) {
        return IMCustomAttachParser.packData(first, second, packData());
    }


    // 子类的解析和封装接口。
    protected void parseData(JSONObject data) {

    }

    protected JSONObject packData() {
        return null;
    }


    protected JSONArray packArray() {
        return null;
    }


}
