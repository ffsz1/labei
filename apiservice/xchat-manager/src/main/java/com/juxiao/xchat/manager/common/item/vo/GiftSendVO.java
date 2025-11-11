package com.juxiao.xchat.manager.common.item.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GiftSendVO {
    private Long uid;
    private String avatar;
    private String nick;
    private Long targetUid;
    private List<Long> targetUids;//全麦送使用
    private String targetAvatar;
    private String targetNick;
    private Integer giftId;
    private Integer giftNum;
    private String giftPic;
    private String giftName;
    private Long goldPrice;
    private Integer experLevel;
    @ApiModelProperty(value = "剩余多少捡海螺次数")
    private Long conchNum;
    @ApiModelProperty(value = "剩余多少用户礼物")
    private Long userGiftPurseNum;
    @ApiModelProperty(value = "使用了多少用户礼物（金币值）")
    private Long useGiftPurseGold;
    private Integer roomId;
    private Long roomUid;
    private Long userNo;
    private Long giftSendTime = System.currentTimeMillis();
}
