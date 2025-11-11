package com.juxiao.xchat.service.api.family.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author laizhilong
 * @Title: <h2>封装创建家族请求参数</h2>
 * @Package com.juxiao.xchat.service.api.family.bo
 * @date 2018/8/30
 * @time 17:58
 */
@Data
@ApiModel(value="创建家族请求参数",description="创建家族请求参数")
public class FamilyTeamParamBO {

    @ApiModelProperty(value="userId",name="userId" , required = true)
    private String userId;

    @ApiModelProperty(value="背景图(暂不需要)",name="bgImg")
    private String bgImg;

    @ApiModelProperty(value="logo",name="logo" , required = true)
    private String logo;

    @ApiModelProperty(value="家族名称",name="name",required = true)
    private String name;

    @ApiModelProperty(value="家族公告",name="notice",required = true)
    private String notice;

    @ApiModelProperty(value="手上厅ID,多个以逗号隔开",name="hall",required = true)
    private String hall;
}
