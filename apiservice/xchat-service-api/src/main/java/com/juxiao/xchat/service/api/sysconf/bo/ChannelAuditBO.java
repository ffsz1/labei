package com.juxiao.xchat.service.api.sysconf.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: alwyn
 * @Description:
 * @Date: 2018/10/24 10:32
 */
@Data
@ApiModel
public class ChannelAuditBO {

    @ApiModelProperty(value = "UID")
    Long uid;
    @ApiModelProperty(value = "登录凭证")
    String ticket;
    @ApiModelProperty(value = "app版本")
    String appVersion;
    @ApiModelProperty(value = "渠道")
    String channel;

}
