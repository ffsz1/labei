package com.juxiao.xchat.dao.item.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GiftDO {
    private Integer giftId;
    private String giftName;
    private Long goldPrice;
    private Integer seqNo;
    private Integer nobleId;
    private String nobleName;
    private Boolean isNobleGift;
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
    /** 是否允许赠送 */
    private Boolean isCanGive;
}