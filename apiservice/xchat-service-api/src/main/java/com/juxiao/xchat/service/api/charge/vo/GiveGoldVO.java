package com.juxiao.xchat.service.api.charge.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 赠送金币Vo
 */

@Data
public class GiveGoldVO {
    @ApiModelProperty(value = "赠送者uid", name = "sendUid")
    private Long sendUid;
    @ApiModelProperty(value = "赠送者name", name = "sendName")
    private String sendName;
    @ApiModelProperty(value = "赠送者avatar", name = "sendAvatar")
    private String sendAvatar;
    @ApiModelProperty(value = "接收者uid", name = "recvUid")
    private Long recvUid;
    @ApiModelProperty(value = "接收者name", name = "recvName")
    private String recvName;
    @ApiModelProperty(value = "接收者avatar", name = "recvAvatar")
    private String recvAvatar;
    @ApiModelProperty(value = "金币", name = "goldNum")
    private Integer goldNum;
    @ApiModelProperty(value = "留言", name = "sendDesc")
    private String sendDesc;
}
