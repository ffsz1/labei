package com.juxiao.xchat.service.api.family.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author laizhilong
 * @Title: <h2>编辑家族资料请求参数</h2>
 * @Package com.juxiao.xchat.service.api.family.bo
 * @date 2018/8/31
 * @time 10:25
 */
@Data
@ApiModel(value="编辑家族资料请求参数",description="编辑家族资料请求参数")
public class FamilyTeamEditParamBO {

    @ApiModelProperty(value="家族ID",name="familyId",required = true)
    private Long familyId;

    @ApiModelProperty(value="logo",name="logo" , required = true)
    private String logo;

    @ApiModelProperty(value="家族公告",name="notice",required = true)
    private String notice;

    @ApiModelProperty(value="背景图(暂不需要)",name="bgImg")
    private String bgImg;
}
