package com.tongdaxing.xchat_core.find.family;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamMessageNotifyTypeEnum;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.Map;

import static com.tongdaxing.xchat_core.find.family.IFamilyCoreClient.BG;
import static com.tongdaxing.xchat_core.find.family.IFamilyCoreClient.LOGO;
import static com.tongdaxing.xchat_core.find.family.IFamilyCoreClient.NOTICE;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/27
 * 描述
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 */
public class FamilyCoreImpl extends AbstractBaseCore implements IFamilyCore {

    private FamilyInfo mCacheInfo;
    private FamilyInfo myJoinInfo;

    @Override
    public void setFamilyInfo(FamilyInfo info) {
        this.myJoinInfo = info;
    }

    @Override
    public FamilyInfo getFamilyInfo() {
        return myJoinInfo;
    }

    @Override
    public void setCacheInfo(FamilyInfo cacheInfo) {
        this.mCacheInfo = cacheInfo;
    }

    @Override
    public FamilyInfo getCacheInfo() {
        return mCacheInfo;
    }

    @Override
    public boolean checkIsMyFamily(FamilyInfo info) {
        if (info == null || myJoinInfo == null) {
            return false;
        }
        return info.getFamilyId() == myJoinInfo.getFamilyId();
    }

    @Override
    public void newBuild(FamilyInfo cacheInfo) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("hall", "");
        param.put("bgImg", cacheInfo.getFamilyBg());
        param.put("logo", cacheInfo.getFamilyLogo());
        param.put("name", cacheInfo.getFamilyName());
        param.put("notice", cacheInfo.getFamilyNotice());

        param.put("ticket", String.valueOf(CoreManager.getCore(IAuthCore.class).getTicket()));
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        param.put("userId", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        OkHttpManager.getInstance().doPostRequest(UriProvider.createFamilyTeam(), param, new OkHttpManager.MyCallBack<Json>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IFamilyCoreClient.class, IFamilyCoreClient.METHOD_ON_NEW_BUILD_FAMILY_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200) {
                    notifyClients(IFamilyCoreClient.class, IFamilyCoreClient.METHOD_ON_NEW_BUILD_FAMILY, response.str("data"));
                } else {
                    notifyClients(IFamilyCoreClient.class, IFamilyCoreClient.METHOD_ON_NEW_BUILD_FAMILY_FAIL, response.str("message"));
                }
            }
        });
    }

    @Override
    public void muteTeam(long roomId, int ope) {
        NIMClient.getService(TeamService.class)
                .muteTeam(roomId + "", (ope == 1) ? TeamMessageNotifyTypeEnum.Mute : TeamMessageNotifyTypeEnum.All)
                .setCallback(null);
    }

    @Override
    public void checkFamilyJoin() {
        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        param.put("ticket", String.valueOf(CoreManager.getCore(IAuthCore.class).getTicket()));
        param.put("userId", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        OkHttpManager.getInstance().getRequest(UriProvider.checkFamilyJoin(), param, new OkHttpManager.MyCallBack<ServiceResult<FamilyInfo>>() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(ServiceResult<FamilyInfo> response) {
                if (response != null && response.isSuccess()) {
                    CoreManager.getCore(IFamilyCore.class).setFamilyInfo(response.getData());
                    notifyClients(IFamilyCoreClient.class, IFamilyCoreClient.METHOD_ON_CHECK_FAMILY_JOIN, response.getData());
                }
            }
        });
    }

    @Override
    public void applyJoinFamilyTeam(FamilyInfo info) {

        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("familyId", String.valueOf(info.getFamilyId()));
        param.put("ticket", String.valueOf(CoreManager.getCore(IAuthCore.class).getTicket()));
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        param.put("userId", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        OkHttpManager.getInstance().doPostRequest(UriProvider.applyJoinFamilyTeam(), param, new OkHttpManager.MyCallBack<Json>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IFamilyCoreClient.class, IFamilyCoreClient.METHOD_ON_APPLY_JOIN_FAMILY_TEAM_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200) {
                    notifyClients(IFamilyCoreClient.class, IFamilyCoreClient.METHOD_ON_APPLY_JOIN_FAMILY_TEAM);
                } else {
                    notifyClients(IFamilyCoreClient.class, IFamilyCoreClient.METHOD_ON_APPLY_JOIN_FAMILY_TEAM_FAIL, response.str("message"));
                }
            }

        });
    }

    /**
     * 修改家族信息
     *
     * @param info     家族信息
     * @param type     类型
     * @param content  内容
     */
    @Override
    public void editFamilyTeam(FamilyInfo info, final int type, final String content) {

        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("familyId", String.valueOf(info.getFamilyId()));
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        param.put("userId", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        //公告
        if (type == NOTICE) {
            param.put("notice", content);
            param.put("bgImg", info.getFamilyBg());
            param.put("logo", info.getFamilyLogo());
        } else if (type == LOGO) {
            //头像
            param.put("logo", content);
            param.put("notice", info.getFamilyNotice());
            param.put("bgImg", info.getFamilyBg());
        } else if (type == BG) {
            //背景
            param.put("bgImg", content);
            param.put("notice", info.getFamilyNotice());
            param.put("logo", info.getFamilyLogo());
        }

        OkHttpManager.getInstance().doPostRequest(UriProvider.editFamilyTeam(), param, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IFamilyCoreClient.class, IFamilyCoreClient.METHOD_ON_MODIFY_INFO_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null && response.isSuccess()) {
                    notifyClients(IFamilyCoreClient.class, IFamilyCoreClient.METHOD_ON_MODIFY_INFO, type, content);
                } else {
                    notifyClients(IFamilyCoreClient.class, IFamilyCoreClient.METHOD_ON_MODIFY_INFO_FAIL, response.getMessage());
                }
            }
        });
    }

    @Override
    public void setMsgNotify(FamilyInfo familyInfo) {

        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("familyId", String.valueOf(familyInfo.getFamilyId()));
        param.put("ope", String.valueOf(familyInfo.getOpe() == 1 ? 2 : 1));

        param.put("ticket", String.valueOf(CoreManager.getCore(IAuthCore.class).getTicket()));
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        param.put("userId", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        OkHttpManager.getInstance().doPostRequest(UriProvider.setMsgNotify(), param, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IFamilyCoreClient.class, IFamilyCoreClient.METHOD_ON_SET_MSG_NOTIFY_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null && response.isSuccess()) {
                    notifyClients(IFamilyCoreClient.class, IFamilyCoreClient.METHOD_ON_SET_MSG_NOTIFY);
                } else {
                    notifyClients(IFamilyCoreClient.class, IFamilyCoreClient.METHOD_ON_SET_MSG_NOTIFY_FAIL, response.getMessage());
                }
            }
        });
    }

    @Override
    public void exitFamily(FamilyInfo familyInfo) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("familyId", String.valueOf(familyInfo.getFamilyId()));
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        param.put("ticket", String.valueOf(CoreManager.getCore(IAuthCore.class).getTicket()));
        param.put("userId", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        OkHttpManager.getInstance().doPostRequest(UriProvider.applyExitTeam(), param, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IFamilyCoreClient.class, IFamilyCoreClient.METHOD_ON_EXIT_FAMILY_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null && response.isSuccess()) {
                    notifyClients(IFamilyCoreClient.class, IFamilyCoreClient.METHOD_ON_EXIT_FAMILY);
                } else {
                    notifyClients(IFamilyCoreClient.class, IFamilyCoreClient.METHOD_ON_EXIT_FAMILY_FAIL, response.getMessage());
                }
            }
        });
    }
}
