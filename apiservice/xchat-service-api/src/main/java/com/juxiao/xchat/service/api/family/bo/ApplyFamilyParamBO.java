package com.juxiao.xchat.service.api.family.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author laizhilong
 * @Title:
 * @Package com.juxiao.xchat.service.api.family.bo
 * @date 2018/9/2
 * @time 12:18
 */
@Data
@ApiModel(value="申请审核退出家族及审核加入家族请求参数",description="申请审核退出家族及审核加入家族请求参数")
public class ApplyFamilyParamBO {

    @ApiModelProperty(value="userId",name="userId",required = true)
    private Long userId;

    @ApiModelProperty(value = "家族ID",name="familyId",required = true)
    private Long familyId;

    @ApiModelProperty(value = "状态(1.同意,2.拒绝)",name="status",required = true)
    public Integer status;

    @ApiModelProperty(value = "类型(1.加入 2.退出)",name="type",required = true)
    public Integer type;
}
