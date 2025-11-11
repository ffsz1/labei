package com.juxiao.xchat.service.api.family.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chris
 * @Title:
 * @date 2018/9/25
 * @time 10:19
 */
@Data
@ApiModel(value="家族请求参数",description="家族请求参数")
public class TeamParamsBO {


    @ApiModelProperty(value = "家族ID",name="familyId")
    private Long familyId;
    @ApiModelProperty(value = "当前页",name="current",required = true)
    private Integer current;
    @ApiModelProperty(value = "每页显示数",name="pageSize",required = true)
    private Integer pageSize;
    @ApiModelProperty(value = "uid",name="uid")
    private Long uid;
    @ApiModelProperty(value = "系统",name="os")
    private String os;
    @ApiModelProperty(value = "app版本",name="appVersion")
    private String appVersion;
}
