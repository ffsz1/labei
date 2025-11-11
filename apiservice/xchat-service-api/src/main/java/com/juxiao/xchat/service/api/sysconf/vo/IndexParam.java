package com.juxiao.xchat.service.api.sysconf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel
@Getter
@Setter
@ToString
public class IndexParam {
    @ApiModelProperty(value = "用户ID")
    private Long uid;
    @ApiModelProperty(value = "系统类型")
    private String os;
    @ApiModelProperty(value = "appid")
    private String appid;
    @ApiModelProperty(value = "app版本号")
    private String appVersion;
    @ApiModelProperty(value = "页码")
    private Integer pageNum;
    @ApiModelProperty(value = "每页记录数")
    private Integer pageSize;
    // App渠道
    @ApiModelProperty(value = "渠道")
    private String channel;
    
    @ApiModelProperty(value = "过滤性别 ,0:不过滤  1:男 2:女")
    private Integer gender;

    public Integer getPageNum() {
        return pageNum == null ? 1 : pageNum;
    }

    public Integer getPageSize() {
        return pageSize == null ? 12 : pageSize;
    }
}
