package com.juxiao.xchat.service.api.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "响应数据封装")
public class AccountBannedVO {
    @ApiModelProperty(value = "全部禁言")
    private Boolean all;
    @ApiModelProperty(value = "房间禁言")
    private Boolean room;
    @ApiModelProperty(value = "私聊禁言")
    private Boolean chat;
    @ApiModelProperty(value = "广播禁言")
    private Boolean broadcast;
}
