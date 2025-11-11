package com.juxiao.xchat.service.api.family.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author laizhilong
 * @Title:
 * @Package com.juxiao.xchat.service.api.family.bo
 * @date 2018/8/31
 * @time 11:20
 */
@Data
@ApiModel(value="申请退出或逐出家族参数",description="申请退出或逐出家族参数")
public class ApplyTeamParamBO {


    @ApiModelProperty(value="userId",name="userId")
    private Long userId;

    @ApiModelProperty(value = "家族ID",name="familyId",required = true)
    private Long familyId;

    @ApiModelProperty(value="userIds",name="userIds")
    private Long[] userIds;
}
