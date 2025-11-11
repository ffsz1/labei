package com.juxiao.xchat.base.constant;

import com.google.common.collect.Lists;

import java.util.List;

public interface GiftDraw {
    /*int gift1 = 1;// 棒棒糖
    int gift2 = 9;// 玫瑰花
    int gift3 = 3;// 抱抱熊
    int gift4 = 31;// 甜蜜恋语
    int gift5 = 6;// 跑车
    int gift6 = 4;// 大飞机*/
    int gift1 = 1;// 玫瑰花
    int gift2 = 2;//小铃铛
    int gift3 = 4;// 猫物语
    int gift4 = 14;// 余生有你
    int gift5 = 15;// 跑车
    int gift6 = 9;// 大飞机
    /** 测试概率 */
    double[] test_pro = {0, 0, 0, 0, 0, 0.3, 0.1, 0.3, 0.3, 0.3, 0};

    int mystic[] = {50, 51, 13};
    // 抽奖礼物列表，保留旧的礼物用于显示
    int giftDrawList[] = {1, 2, 4, 14, 15, 9};
    /** 参与抽奖的礼物, 礼物的顺序和概率的顺序要一致 */
    // 1玫瑰花	    2棒棒糖	    3西瓜	    4礼物2	    5空气猫	    6神秘礼物A	    7跑车	8大飞机	    9神秘礼物B	    10余生有你	    11神秘礼物
    // 13 46 51
    int mysticGiftDrawList[] = {1, 2, 23, 36, 4, 51, 15, 9, 46, 14, 13};
    /** 没有命中概率时的默认礼物 */
    int defaultGift = 1;

    /** 神秘礼物发送全服消息的最小金额 */
    int minGoldPrice = 30000;

    /** 特殊概率的时间 */
    List<Integer> special_pro_time  = Lists.newArrayList(3, 6, 9, 12, 15, 18, 21);
    /** 第一次捡海螺的概率 */
    double first_pro[]      = {0.201,    0.49198,   0.12,   0.1,  0.08,     0,      0.009,  0.001, 0,       0.00002, 0};
    /** 日常-概率 */
    double daily_pro[]      = {0.67997,  0.21488,   0.08,   0.01, 0.005,    0.0015, 0.0075, 0.001, 0.00003, 0.00011, 0.00001};
    /** 日常-特殊概率 */
    double special_pro[]    = {0.691088, 0.2138,    0.08,   0.01, 0,        0.0001, 0.006,  0.003, 0.00008, 0.00003, 0.000002};
    /** 高概率-用于活动 */
    double high_pro[]       = {0.701002, 0.17008,   0.07,   0.04, 0.01,     0,      0.0058, 0.003, 0,       0.0001,  0};
}
