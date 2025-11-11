package com.juxiao.xchat.dao.item.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GiftCarDO {
    private Integer carId;
    private String carName;
    private Long goldPrice;
    private Integer seqNo;
    private Byte carStatus;
    private String picUrl;
    private Boolean hasGifPic;
    private String gifUrl;
    private Boolean hasVggPic;
    private String vggUrl;
    private Boolean isTimeLimit;
    private Long effectiveTime;
    private Date createTime;

    private Boolean allowPurse;
    private Integer leftLevel;
    private String markPic;
}