package com.juxiao.xchat.dao.item.dto;

import lombok.Data;

import java.util.Date;

@Data
public class GiftDTO {
    private Integer giftId;
    private String giftName;
    private Long goldPrice;
    private Integer seqNo;
    private Integer nobleId;
    private String nobleName;
    private Boolean isNobleGift;
    /**
     * 礼物类型：1：打call礼物 ; 2，普通礼物；3，海螺礼物 4.活动礼物 5.相亲礼物
     */
    private Byte giftType;
    private Byte giftStatus;
    private String picUrl;
    private Boolean hasGifPic;
    private String gifUrl;
    private Boolean hasVggPic;
    private String vggUrl;
    private Boolean isLatest;
    private Boolean isTimeLimit;
    private Boolean hasEffect;
    private Date startValidTime;
    private Date endValidTime;
    private Date createTime;
    private Boolean isCanGive; // 是否允许赠送
    private Boolean isExpressGift; // 是否是表白礼物
}