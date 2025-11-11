package com.tongdaxing.xchat_core.find.family;

import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/27
 * 描述
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public interface IFamilyCore extends IBaseCore {

    void setFamilyInfo(FamilyInfo info);

    FamilyInfo getFamilyInfo();

    void setCacheInfo(FamilyInfo cacheInfo);

    FamilyInfo getCacheInfo();

    boolean checkIsMyFamily(FamilyInfo info);

    /**
     * 新建家族
     */
    void newBuild(FamilyInfo cacheInfo);

    /**
     * 调用云信免打扰
     */
    void muteTeam(long roomId, int ope);

    /**
     * 获取当前用户是否加入房间的操作
     */
    void checkFamilyJoin();

    /**
     * 申请加入家族
     */
    void applyJoinFamilyTeam(FamilyInfo info);


    void editFamilyTeam(FamilyInfo info, int type, String content);

    /**
     * 修改提醒状态
     */
    void setMsgNotify(FamilyInfo info);

    /**
     * 退出家族
     */
    void exitFamily(FamilyInfo info);
}
