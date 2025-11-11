package com.juxiao.xchat.manager.common.mcoin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class McoinDrawWiningDTO {

    /**
     * 用户ID
     */
    @ApiModelProperty(name = "uid", value = "用户ID")
    private Long uid;

    /**
     * 参与用户头像
     */
    @ApiModelProperty(name = "avatar", value = "参与用户头像")
    private String avatar;

    /**
     * 参与用户昵称
     */
    @ApiModelProperty(name = "nick", value = "参与用户昵称")
    private String nick;

    /**
     * 中奖号码
     */
    @ApiModelProperty(name = "ticketNo", value = "中奖号码")
    private String ticketNo;

    /**
     * 参与次数
     */
    @ApiModelProperty(name = "drawCount", value = "参与次数")
    private Integer drawCount;

    /**
     * 中奖时间
     */
    @ApiModelProperty(name = "prizeCreateTime", value = "中奖时间")
    private Date prizeCreateTime;
}
