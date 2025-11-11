package com.vslk.lbgx.model.find.family;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.find.family.FamilyInfo;
import com.tongdaxing.xchat_core.find.family.IFamilyCore;
import com.tongdaxing.xchat_core.find.family.bean.ApplyMsgInfo;
import com.tongdaxing.xchat_core.find.family.bean.FamilyListInfo;
import com.tongdaxing.xchat_core.find.family.bean.MemberInfo;
import com.tongdaxing.xchat_core.find.family.bean.MemberListInfo;
import com.tongdaxing.xchat_core.manager.BaseMvpModel;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.List;
import java.util.Map;

import static com.tongdaxing.xchat_core.find.family.IFamilyCoreClient.PAGE_FAMILY_MEMBER_SIZE;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/20
 * 描述        发现Fragment -- Model
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 */
public class FindFamilyModel extends BaseMvpModel {

    /**
     * 获取家族列表
     *
     * @param pageNum 页数
     */
    public void getFamilyList(int pageNum, OkHttpManager.MyCallBack<ServiceResult<FamilyListInfo>> callBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("current", String.valueOf(pageNum));
        param.put("pageSize", PAGE_FAMILY_MEMBER_SIZE + "");

        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        getRequest(UriProvider.getList(), param, callBack);
    }

    /**
     * 获取用户加入的家族数据
     */
    public void checkFamilyJoin(OkHttpManager.MyCallBack<ServiceResult<FamilyInfo>> callBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        param.put("ticket", String.valueOf(CoreManager.getCore(IAuthCore.class).getTicket()));
        param.put("userId", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        getRequest(UriProvider.checkFamilyJoin(), param, new OkHttpManager.MyCallBack<ServiceResult<FamilyInfo>>() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(ServiceResult<FamilyInfo> response) {
                if (response != null && response.isSuccess()) {
                    CoreManager.getCore(IFamilyCore.class).setFamilyInfo(response.getData());
                }
                callBack.onResponse(response);
            }
        });
    }

    /**
     * 获取家族成员列表
     */
    public void getMemberList(int current, FamilyInfo familyInfo,
            OkHttpManager.MyCallBack<ServiceResult<MemberListInfo>> callBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("current", String.valueOf(current));
        param.put("pageSize", String.valueOf(PAGE_FAMILY_MEMBER_SIZE));

        param.put("familyId", String.valueOf(familyInfo.getFamilyId()));
        param.put("ticket", String.valueOf(CoreManager.getCore(IAuthCore.class).getTicket()));
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        getRequest(UriProvider.getFamilyTeamJoin(), param, callBack);
    }

    /**
     * 移除成员
     */
    public void removeMember(FamilyInfo familyInfo, MemberInfo info, OkHttpManager.MyCallBack<Json> callBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("userId", String.valueOf(info.getUid()));
        param.put("familyId", String.valueOf(familyInfo.getFamilyId()));
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        postRequest(UriProvider.kickOutTeam(), param, callBack);
    }

    /**
     * 移除管理员权限
     */
    public void removeAdmin(FamilyInfo familyInfo, MemberInfo info, OkHttpManager.MyCallBack<Json> callBack) {

        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("userId", String.valueOf(info.getUid()));
        param.put("familyId", String.valueOf(familyInfo.getFamilyId()));
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        postRequest(UriProvider.removeAdmin(), param, callBack);
    }

    /**
     * 设置副族长
     */
    public void setupAdministrator(FamilyInfo familyInfo, String members, OkHttpManager.MyCallBack<Json> callBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("userIds", members);
        param.put("familyId", String.valueOf(familyInfo.getFamilyId()));
        param.put("ticket", String.valueOf(CoreManager.getCore(IAuthCore.class).getTicket()));
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        postRequest(UriProvider.setupAdministrator(), param, callBack);
    }

    /**
     * 获取申请消息
     */
    public void getFamilyMessage(int pageNum, FamilyInfo familyInfo,
            OkHttpManager.MyCallBack<ServiceResult<List<ApplyMsgInfo>>> callBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("current", String.valueOf(pageNum));
        param.put("pageSize", String.valueOf(PAGE_FAMILY_MEMBER_SIZE));

        param.put("familyId", String.valueOf(familyInfo.getFamilyId()));
        param.put("ticket", String.valueOf(CoreManager.getCore(IAuthCore.class).getTicket()));
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        param.put("userId", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        getRequest(UriProvider.getFamilyMessage(), param, callBack);
    }

    /**
     * 处理申请消息
     */
    public void applyFamily(FamilyInfo info, long userId, int status, int type, OkHttpManager.MyCallBack<Json> callBack) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("type", String.valueOf(type));
        param.put("userId", String.valueOf(userId));
        param.put("status", String.valueOf(status));
        param.put("familyId", String.valueOf(info.getFamilyId()));

        param.put("ticket", String.valueOf(CoreManager.getCore(IAuthCore.class).getTicket()));
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        postRequest(UriProvider.applyFamily(), param, callBack);
    }

    /**
     * 设置申请加入方式
     */
    public void setApplyJoinMethod(FamilyInfo familyInfo, int joinmode, OkHttpManager.MyCallBack<Json> callBack) {

        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("joinmode", String.valueOf(joinmode));
        param.put("familyId", String.valueOf(familyInfo.getFamilyId()));
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        param.put("userId", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        postRequest(UriProvider.setApplyJoinMethod(), param, callBack);
    }

    /**
     * 编辑家族信息
     */
    public void editFamilyTeam(FamilyInfo info, int type, String content, OkHttpManager.MyCallBack<Json> callBack) {

        Map<String, String> param = CommonParamUtil.getDefaultParam();

        param.put("familyId", String.valueOf(info.getFamilyId()));
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        param.put("userId", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        //公告
        if (type == 1) {
            param.put("notice", content);
            param.put("bgImg", info.getFamilyBg());
            param.put("logo", info.getFamilyLogo());
        } else if (type == 2) {
            //头像
            param.put("notice", content);
            param.put("bgImg", info.getFamilyBg());
            param.put("logo", info.getFamilyLogo());
        } else if (type == 3) {
            //背景
            param.put("notice", content);
            param.put("bgImg", info.getFamilyBg());
            param.put("logo", info.getFamilyLogo());
        }

        postRequest(UriProvider.editFamilyTeam(), param, callBack);
    }

}
