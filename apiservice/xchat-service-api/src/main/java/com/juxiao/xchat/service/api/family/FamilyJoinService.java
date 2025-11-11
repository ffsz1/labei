package com.juxiao.xchat.service.api.family;

import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.family.bo.ApplyTeamParamBO;
import com.juxiao.xchat.service.api.family.bo.FamilyUserBannedBO;

/**
 * @author laizhilong
 * @Title:
 * @Package com.juxiao.xchat.service.api.family
 * @date 2018/8/31
 * @time 11:34
 */
public interface FamilyJoinService {

    /**
     * 设置管理员
     *
     * @param applyTeamParamBO
     * @return
     */
    WebServiceMessage setupAdministrator(ApplyTeamParamBO applyTeamParamBO);

    /**
     * 根据uid 查询加入家族信息
     *
     * @param uid
     * @return
     */
    WebServiceMessage getJoinFamilyInfo(Long uid);

    /**
     * 根据uid检测是否加入家族
     *
     * @param uid
     * @return
     */
    WebServiceMessage checkFamilyJoin(Long uid);

    /**
     * 设置消息提醒
     *
     * @param familyId 家族ID
     * @param uid      用户uid
     * @param ope      1：关闭消息提醒，2：打开消息提醒，其他值无效
     * @return
     */
    WebServiceMessage setMsgNotify(Long familyId, Long uid, Integer ope);

    /**
     * 设置禁言及解禁
     *
     * @param familyUserBannedBO
     * @return
     */
    WebServiceMessage settingBanned(FamilyUserBannedBO familyUserBannedBO);

    /**
     * 根据家族Id获取家族信息
     *
     * @param familyId
     * @return
     */
    WebServiceMessage getFamilyInfo(Long familyId);

}
