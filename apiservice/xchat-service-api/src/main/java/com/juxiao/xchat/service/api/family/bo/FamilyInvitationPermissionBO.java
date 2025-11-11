package com.juxiao.xchat.service.api.family.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chris
 * @Title:
 * @date 2018/9/22
 * @time 14:01
 */
@Data
@ApiModel(value="邀请他人权限",description="邀请他人权限请求参数")
public class FamilyInvitationPermissionBO {

    @ApiModelProperty(value="uid",name="uid",required = true)
    private Long uid;

    @ApiModelProperty(value = "家族ID",name="familyId",required = true)
    private Long familyId;

    @ApiModelProperty(value = "谁可以邀请他人入群，0-管理员(默认),1-所有人",name="invitemode",required = true)
    public Integer invitemode;
}
