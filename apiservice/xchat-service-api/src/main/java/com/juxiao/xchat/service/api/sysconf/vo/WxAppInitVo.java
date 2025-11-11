package com.juxiao.xchat.service.api.sysconf.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WxAppInitVo {
    @ApiModelProperty(name = "isAuditing", value = "是否审核模式：true，是；false，否")
    private boolean isAuditing;
    @ApiModelProperty(name = "timestamp", value = "当前服务端的时间戳")
    private long timestamp;
    @ApiModelProperty(name = "luxuryGift", value = "捡海螺最高可得礼物名称")
    private String luxuryGift;
}
