package com.juxiao.xchat.service.api.family;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.family.dto.FamilyTeamDTO;
import com.juxiao.xchat.service.api.family.bo.*;

/**
 * @author laizhilong
 * @Title:
 * @Package com.juxiao.xchat.service.api.family
 * @date 2018/8/30
 * @time 18:09
 */
public interface FamilyTeamService {

    /**
     * 保存
     * @param familyTeamParamBO
     * @return
     */
    WebServiceMessage save(FamilyTeamParamBO familyTeamParamBO);

    /**
     * 更新
     * @param familyTeamEditParamBO
     * @return
     */
    WebServiceMessage update(FamilyTeamEditParamBO familyTeamEditParamBO);


    /**
     * 根据族长 ID查询族长信息
     * @param teamId
     * @return
     */
    FamilyTeamDTO getFamilyTeamInfo(Long teamId);

    /**
     * 设置申请加入方式
     * @param teamId
     * @return
     */
    WebServiceMessage setApplyJoinMethod(Long teamId, Integer joinmode);

    /**
     * 获取家族成员列表
     * @param teamParamsBO
     * @return
     */
    WebServiceMessage getFamilyTeamJoin(TeamParamsBO teamParamsBO);

    /**
     * 审核加入家族
     * @param applyFamilyParamBO
     * @return
     */
    WebServiceMessage applyJoinFamily(ApplyFamilyParamBO applyFamilyParamBO);

    /**
     * 获取家族列表信息
     * @param teamParamsBO 搜索家族ID
     * @param clientIp
     * @return
     */
    WebServiceMessage getList(TeamParamsBO teamParamsBO, String clientIp, String app) throws WebServiceException;

    /**
     * 获取家族消息
     * @param familyId
     * @return
     */
    WebServiceMessage getFamilyMessage(Long familyId, Long uid,Integer current,Integer pageSize);

    /**
     * 移除管理员
     * @param applyTeamParamBO
     * @return
     */
    WebServiceMessage removeAdmin(ApplyTeamParamBO applyTeamParamBO);

    /**
     * 设置邀请权限
     * @param invitationPermissionBO
     * @return
     */
    WebServiceMessage invitationPermission(FamilyInvitationPermissionBO invitationPermissionBO);
}
