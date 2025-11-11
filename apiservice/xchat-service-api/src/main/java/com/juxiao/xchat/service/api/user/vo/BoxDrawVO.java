package com.juxiao.xchat.service.api.user.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 宝盒抽奖结果
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoxDrawVO {
    @ApiModelProperty(name = "nick", value = "用户昵称")
    private String nick;
    @ApiModelProperty(name = "prizeType", value = "奖品类型")
    private Integer prizeType;
    @ApiModelProperty(name = "prizeName", value = "奖品名")
    private String prizeName;
    @ApiModelProperty(name = "prizePic", value = "奖品url")
    private String prizePic;
    @ApiModelProperty(name = "prizeDate", value = "奖品天数")
    private Integer prizeDate;
    @ApiModelProperty(name = "prizeNum", value = "奖品数量")
    private Integer prizeNum;
    @ApiModelProperty(name = "boxNum", value = "剩余礼盒次数")
    private Integer boxNum;
    @ApiModelProperty(name = "desc", value = "奖品描述")
    private String desc;
    @ApiModelProperty(name = "createTime", value = "礼盒抽奖时间")
    private Date createTime;
}
