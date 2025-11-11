package com.juxiao.xchat.service.api.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 捡海螺vo
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrawConchVO {
    @ApiModelProperty(name = "giftList", value = "捡海螺获得礼物列表")
    private List<DrawGiftVO> giftList;
    @ApiModelProperty(name = "ticketId", value = "人气礼物ID")
    private Integer ticketId;
    @ApiModelProperty(name = "conchNum", value = "捡海螺次数")
    private Long conchNum;
    @ApiModelProperty(name = "tryCoinNum", value = "体验币")
    private Long tryCoinNum;
}
