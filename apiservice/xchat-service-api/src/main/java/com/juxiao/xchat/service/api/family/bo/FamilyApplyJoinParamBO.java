package com.juxiao.xchat.service.api.family.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author laizhilong
 * @Title: <h2>申请加入请求参数</h2>
 * @Package com.juxiao.xchat.service.api.family.bo
 * @date 2018/8/30
 * @time 21:11
 */
@Data
@ApiModel(value="申请加入请求参数",description="申请加入请求参数")
public class FamilyApplyJoinParamBO {

    @ApiModelProperty(value="userId",name="userId",required = true)
    private Long userId;

    @ApiModelProperty(value = "家族ID",name="familyId",required = true)
    private Long familyId;
}
