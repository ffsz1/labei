package com.juxiao.xchat.service.api.family;

import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.family.bo.ApplyFamilyParamBO;
import com.juxiao.xchat.service.api.family.bo.ApplyTeamParamBO;

/**
 * @author laizhilong
 * @Title:
 * @Package com.juxiao.xchat.service.api.family
 * @date 2018/8/31
 * @time 11:34
 */
public interface FamilyExitRecordService {


    /**
     * 申请退出家族
     *
     * @param applyTeamParamBO
     * @return
     */
    WebServiceMessage applyExitTeam(ApplyTeamParamBO applyTeamParamBO);

    /**
     * 踢出家族
     *
     * @param applyTeamParamBO
     * @param currentUid
     * @return
     */
    WebServiceMessage kickOutTeam(ApplyTeamParamBO applyTeamParamBO, String currentUid);

    /**
     * 审核退出家族
     *
     * @param applyFamilyParamBO
     * @return
     */
    WebServiceMessage applyExitFamily(ApplyFamilyParamBO applyFamilyParamBO);

}
