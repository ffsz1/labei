package com.tongdaxing.xchat_core.find.family;

import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

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
public interface IFamilyCoreClient extends ICoreClient {

    int LOGO = 1;
    int BG = 2;
    int NOTICE = 3;

    int MANAGER = 1;
    int ADMIN = 2;
    int NORMAL = 3;

    int FAMILY_MANAGER_MAX_COUNT = 4;

    int PAGE_FAMILY_MEMBER_SIZE = 20;
    long FORBID_TIME = 30 * 24 * 60 * 60 * 1000L;

    String METHOD_ON_REFRESH_INFO = "onRefreshInfo";
    String METHOD_ON_NOTIFY_MODIFY_INFO = "onNotifyModifyInfo";

    String METHOD_ON_GET_FAMILY_INFO = "getFamilyInfo";

    String METHOD_ON_NEW_BUILD_FAMILY = "onNewBuildFamily";
    String METHOD_ON_NEW_BUILD_FAMILY_FAIL = "onNewBuildFamilyFail";

    String METHOD_ON_CHECK_FAMILY_JOIN = "onCheckFamilyJoin";
    String METHOD_ON_CHECK_FAMILY_JOIN_FAIL = "onCheckFamilyJoinFail";

    String METHOD_ON_APPLY_JOIN_FAMILY_TEAM = "onApplyJoinFamilyTeam";
    String METHOD_ON_APPLY_JOIN_FAMILY_TEAM_FAIL = "onApplyJoinFamilyTeamFail";

    String METHOD_ON_MODIFY_INFO = "onModifyInfo";
    String METHOD_ON_MODIFY_INFO_FAIL = "onModifyInfoFail";

    String METHOD_ON_SET_MSG_NOTIFY = "onSetMsgNotify";
    String METHOD_ON_SET_MSG_NOTIFY_FAIL = "onSetMsgNotifyFail";

    String METHOD_ON_EXIT_FAMILY = "onExitFamily";
    String METHOD_ON_EXIT_FAMILY_FAIL = "onExitFamilyFail";

    void getFamilyInfo();

    void onRefreshInfo();

    void onNotifyModifyInfo();

    void onNewBuildFamily(String isNewBuild);
    void onNewBuildFamilyFail(String msg);

    void onCheckFamilyJoin(FamilyInfo familyInfo);
    void onCheckFamilyJoinFail();

    void onApplyJoinFamilyTeam();

    void onApplyJoinFamilyTeamFail(String errorMsg);

    void onModifyInfo(int type, String content);
    void onModifyInfoFail(String message);

    void onSetMsgNotify();
    void onSetMsgNotifyFail(String msg);

    void onExitFamily();
    void onExitFamilyFail(String msg);
}
