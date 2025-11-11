//package com.juxiao.xchat.service.api.user.conf;
//
//import lombok.Data;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Data
//@Component
//@ConfigurationProperties(prefix = "gift.draw")
//public class GiftDrawConfig {
//
//    /** 抽奖礼物列表，保留旧的礼物用于显示 */
//    private int[] drawGiftAllArr;
//    /** 礼物列表 */
//    private int[] giftDrawArr;
//    /** 活动礼物列表 */
//    private int[] giftEventArr;
//    /** 测试概率 */
//    private double[] testProArr;
//    /** 日常概率 */
//    private double[] dailyProArr;
//    /** 日常特殊概率A */
//    private double[] specialProArrA;
//    /** 日常特殊概率B */
//    private double[] specialProArrB;
//    /** 高概率--活动用 */
//    private double[] highProArr;
//    /** 高概率--活动用(新用户) */
//    private double[] highProArrNew;
//    /** 活动礼物 */
//    private Integer eventGift;
//    /** 日常特殊概率的时间 */
//    private List<Integer> specialProTimeList;
//    /** 未命中概率时, 使用的默认礼物 */
//    private int defaultGift = 1;
//    /** 发送全服推送的最小金额 */
//    private int minGoldPrice = 30000;
//    /** 砸中活动礼物的最小等级 */
//    private int drawIphoneMinLevel = 20;
//    /** iphone的礼物ID */
//    private Integer iPhoneGiftId;
//    //
//    /** 日常-低概率 */
//    private double[] leftProArr;
//    /** 第一次捡海螺返回的默认礼物 */
//    private Integer firstDrawGift = 4;
//    /** 全服礼物 */
//    private Integer fullGift = 14;
//    /** 活动礼物 */
//    private int[] eventGiftArr;
//    /** 活动概率 */
//    private double[] eventProArr;
//    /** 捡海螺砸中全服的最小等级 */
//    private int fullGiftLeftLevel = 10;
//}
