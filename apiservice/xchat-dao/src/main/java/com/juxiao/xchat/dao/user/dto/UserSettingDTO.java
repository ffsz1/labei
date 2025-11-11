package com.juxiao.xchat.dao.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@ApiModel("UserSetting")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSettingDTO {
    @ApiModelProperty(name = "uid", value = "当前用户ID", required = true)
    private Long uid;
    @ApiModelProperty(name = "likedSend", value = "是否屏蔽关注消息：1，发送；2，不发送", required = true)
    private Byte likedSend;
    @ApiModelProperty(name = "chatPermission", value = "私聊的权限 0关闭, 1 所有人, 2 10级以下用户")
    private Integer chatPermission;
    @ApiModelProperty(name = "createTime", value = "")
    private Date createTime;

}
