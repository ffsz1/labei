package com.vslk.lbgx.presenter.find.family;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.find.family.FamilyInfo;
import com.tongdaxing.xchat_core.find.family.bean.ApplyMsgInfo;
import com.tongdaxing.xchat_core.find.family.bean.MemberListInfo;

import java.util.List;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/20
 * 描述        发现界面
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 */
public interface IFindFamilyView extends IMvpBaseView {

    /**
     * 获取家族列表成功
     */
    default void getFamilyListSuccess(List<FamilyInfo> serviceResult) {

    }

    /**
     * 获取家族列表
     *
     * @param msg 错误消息
     */
    default void getFamilyListFail(String msg) {

    }

    /**
     * 获取当前用户是否加入家族
     */
    default void getCheckFamilyJoinSuccess(FamilyInfo info) {

    }

    default void getCheckFamilyJoinFail(String msg) {

    }

    /**
     * 退出家族
     */
    default void exitFamilySuccess() {

    }

    default void exitFamilyFail(String msg) {

    }

    /**
     * 设置消息免打扰
     */
    default void setMsgNotifySuccess() {

    }

    default void setMsgNotifyFail(String msg) {

    }

    /**
     * 获取家族成员列表
     */
    default void getMemberListSuccess(MemberListInfo memberListInfo) {

    }

    default void getMemberListFail(String msg) {

    }

    /**
     * 移除成员
     */
    default void removeMemberSuccess(int position) {

    }

    default void removeMemberFail(String msg) {

    }

    /**
     * 移除管理员权限
     */
    default void removeAdmin(int position) {

    }

    default void removeAdminFail(String msg) {

    }

    /**
     * 获取家族列表信息
     */
    default void getApplyMsgList(List<ApplyMsgInfo> list) {

    }

    default void getApplyMsgListFail(String msg) {

    }

    /**
     * 处理申请消息
     */
    default void applyFamily(String message, int status, int position) {

    }

    default void applyFamilyFail(String message) {

    }

    /**
     * 设置申请方式
     */
    default void setApplyJoinMethod() {

    }

    default void setApplyJoinMethodFail(String msg) {

    }

    /**
     * 编辑家族信息
     */
    default void editFamilyTeam(String familyDesc) {

    }

    default void editFamilyTeamFail(String msg) {

    }

    /**
     * 设置家族副族长
     */
    default void setupAdministrator() {

    }

    default void setupAdministratorFail(String msg){

    }

}
