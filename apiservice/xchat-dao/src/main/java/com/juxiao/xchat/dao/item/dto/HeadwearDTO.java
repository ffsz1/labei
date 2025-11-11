package com.juxiao.xchat.dao.item.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class HeadwearDTO {
    private Integer headwearId;
    private String headwearName;
    private Long goldPrice;
    private Integer seqNo;
    private Byte headwearStatus;
    private String picUrl;
    private Boolean hasGifPic;
    private String gifUrl;
    private Boolean hasVggPic;
    private String vggUrl;
    private Boolean isTimeLimit;
    private Integer effectiveTime;
    private Date createTime;

    /** 是否允许购买 */
    private Boolean allowPurse;
    /** 允许购买的最低等级 */
    private Integer leftLevel;
    /** 角标图片 */
    private String markPic;
}