package com.juxiao.xchat.dao.mcoin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class McoinDrawInvolvedUserDTO {

    @ApiModelProperty(name = "uid", value = "用户昵称")
    private Long uid;

    @ApiModelProperty(name = "avatar", value = "参与用户头像")
    private String avatar;

    @ApiModelProperty(name = "nick", value = "参与用户昵称")
    private String nick;

    @ApiModelProperty(name = "drawCount", value = "用户参与次数")
    private Integer drawCount;

    @ApiModelProperty(name = "createTime", value = "用户参与时间")
    private Date createTime;
}
