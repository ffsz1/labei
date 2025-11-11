package com.juxiao.xchat.service.api.item.params;

import lombok.Data;

import java.util.List;

/**
 * @author chris
 * @Title: 全服礼物
 * @date 2018/11/16
 * @time 15:59
 */
@Data
public class FullGift {

    private Long uid;
    private String avatar;
    private String nick;
    private Long targetUid;
    /**
     * 全麦送使用
     */
    private List<Long> targetUids;
    private String targetAvatar;
    private String targetNick;
    private Integer giftId;
    private Integer giftNum;
    private String giftPic;
    private String giftName;
    private Long goldPrice;
    private Integer experLevel;
    /**
     * 剩余多少用户礼物
     */
    private Long userGiftPurseNum;
    /**
     * 使用了多少用户礼物（金币值）
     */
    private Long useGiftPurseGold;
    private Long roomId;
    private Long userNo;
}
