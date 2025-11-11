package com.juxiao.xchat.service.api.family.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chris
 * @Title:
 * @date 2018/9/22
 * @time 13:36
 */
@Data
@ApiModel(value="设置禁言/解禁",description="设置禁言/解禁请求参数")
public class FamilyUserBannedBO {

    @ApiModelProperty(value="uid",name="uid",required = true)
    private Long uid;

    @ApiModelProperty(value = "家族ID",name="familyId",required = true)
    private Long familyId;

    @ApiModelProperty(value = "需要禁言uid",name="operatorUid",required = true)
    public Long operatorUid;

    @ApiModelProperty(value = "1-禁言，0-解禁",name="mute",required = true)
    public Integer mute;
}
