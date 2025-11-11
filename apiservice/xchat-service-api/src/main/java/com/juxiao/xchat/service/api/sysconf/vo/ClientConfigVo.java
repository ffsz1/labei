package com.juxiao.xchat.service.api.sysconf.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientConfigVo {
    private String isExchangeAwards;
    private String timestamps;
    private String micInListOption;
    private String lottery_box_option;
    private String greenRoomIndex;
    private String lotteryBoxBigGift;
    private String payChannel;
    private String payMoney;
    /**
     * 禁止修改资料（1.开 2.关）
     */
    private String prohibitModification;

    /**
     * 公聊大厅倒计时 (S)
     */
    private String publicChatHallTime;

    /** 发送图片的最小等级 */
    private String sendPicLeftLevel;

    /**
     * 支付宝开关 1开 2关
     */
    private String aliPaySwitch;

    /**
     * 支付开关
     */
    private String paySwitch;

    /**
     * 猜拳倒计时
     */
    private String moraTime;

    /**
     * 座驾开关 1开 2关
     */
    private String giftCarSwitch;

    /**
     * 支付宝支付渠道  1.ping++ 2.汇潮
     */
    private String aliSwitch;
    /**
     * 相亲房间ID
     */
    private String xqTagId;
}
