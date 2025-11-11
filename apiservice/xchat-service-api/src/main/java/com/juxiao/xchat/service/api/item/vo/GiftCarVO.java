package com.juxiao.xchat.service.api.item.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GiftCarVO {
    private Integer carId;

    private String carName;

    private Long goldPrice;

    private String picUrl;

    private Boolean hasGifPic;

    private String gifUrl;

    private Boolean hasVggPic;

    private String vggUrl;

    private Boolean isTimeLimit;

    private Long effectiveTime;

    private Integer isPurse;

    private Integer daysRemaining;

    private Integer days;

    /** 是否可以购买 */
    private Boolean allowPurse;
    /** 允许购买的最小等级 */
    private Integer leftLevel;
    /** 角标图片 */
    private String markPic;
    /**1：新上架 0：旧上架*/
    private Integer isNew;
}
