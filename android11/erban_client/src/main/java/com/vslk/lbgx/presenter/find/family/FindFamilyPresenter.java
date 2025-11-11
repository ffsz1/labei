package com.vslk.lbgx.presenter.find.family;

import com.vslk.lbgx.model.find.family.FindFamilyModel;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.find.family.FamilyInfo;
import com.tongdaxing.xchat_core.find.family.IFamilyCore;
import com.tongdaxing.xchat_core.find.family.bean.ApplyMsgInfo;
import com.tongdaxing.xchat_core.find.family.bean.FamilyListInfo;
import com.tongdaxing.xchat_core.find.family.bean.MemberInfo;
import com.tongdaxing.xchat_core.find.family.bean.MemberListInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.List;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/20
 * 描述
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class FindFamilyPresenter extends AbstractMvpPresenter<IFindFamilyView> {

    private FindFamilyModel mFindModel;

    public FindFamilyPresenter() {
        if (mFindModel == null) {
            mFindModel = new FindFamilyModel();
        }
    }

    /**
     * 获取家族列表
     */
    public void getFamilyList(int pageNum) {
        mFindModel.getFamilyList(pageNum, new OkHttpManager.MyCallBack<ServiceResult<FamilyListInfo>>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().getFamilyListFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<FamilyListInfo> response) {
                if (response != null && response.isSuccess()) {
                    FamilyListInfo listInfo = response.getData();
                    if (getMvpView() != null) {
                        getMvpView().getFamilyListSuccess(listInfo.getFamilyList());
                    }
                    CoreManager.getCore(IFamilyCore.class).setFamilyInfo(listInfo.getFamilyTeam());
                } else {
                    getMvpView().getFamilyListFail(response.getMessage());
                }
            }
        });
    }

    /**
     * 获取已加入的家族信息
     */
    public void checkFamilyJoin() {
        mFindModel.checkFamilyJoin(new OkHttpManager.MyCallBack<ServiceResult<FamilyInfo>>() {

            @Override
            public void onError(Exception e) {
                getMvpView().getCheckFamilyJoinFail(e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<FamilyInfo> response) {
                if (response != null && response.isSuccess()) {
                    if (getMvpView() != null) {
                        getMvpView().getCheckFamilyJoinSuccess(response.getData());
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().getCheckFamilyJoinFail(response.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 获取家族成员列表
     */
    public void getMemberList(int current, FamilyInfo familyInfo) {

        mFindModel.getMemberList(current, familyInfo, new OkHttpManager.MyCallBack<ServiceResult<MemberListInfo>>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().getMemberListFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<MemberListInfo> response) {
                if (response != null && response.isSuccess()) {
                    if (getMvpView() != null) {
                        getMvpView().getMemberListSuccess(response.getData());
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().getMemberListFail(response.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 踢出成员
     */
    public void removeMember(FamilyInfo familyInfo, MemberInfo info, int position) {
        mFindModel.removeMember(familyInfo, info, new OkHttpManager.MyCallBack<Json>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().removeMemberFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200) {
                    if (getMvpView() != null) {
                        getMvpView().removeMemberSuccess(position);
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().removeMemberFail(response.str("message"));
                    }
                }
            }
        });
    }

    /**
     * 移除管理员权限
     */
    public void removeAdmin(FamilyInfo familyInfo, MemberInfo info, int position) {

        mFindModel.removeAdmin(familyInfo, info, new OkHttpManager.MyCallBack<Json>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().removeAdminFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200) {
                    if (getMvpView() != null) {
                        getMvpView().removeAdmin(position);
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().removeAdminFail(response.str("message"));
                    }
                }
            }
        });
    }

    /**
     * 设置家族副族长
     */
    public void setupAdministrator(FamilyInfo familyInfo, String members) {

        mFindModel.setupAdministrator(familyInfo, members, new OkHttpManager.MyCallBack<Json>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().setupAdministratorFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200) {
                    if (getMvpView() != null) {
                        getMvpView().setupAdministrator();
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().setupAdministratorFail(response.str("message"));
                    }
                }
            }
        });
    }

    /**
     * 获取家族申请记录
     */
    public void getFamilyMessage(int currentPage, FamilyInfo familyInfo) {
        mFindModel.getFamilyMessage(currentPage, familyInfo, new OkHttpManager.MyCallBack<ServiceResult<List<ApplyMsgInfo>>>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().getApplyMsgListFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<List<ApplyMsgInfo>> response) {
                if (response != null && response.isSuccess()) {
                    if (getMvpView() != null) {
                        getMvpView().getApplyMsgList(response.getData());
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().getApplyMsgListFail(response.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 处理申请消息
     */
    public void applyFamily(FamilyInfo familyInfo, long userId, int status, int type, int position) {
        mFindModel.applyFamily(familyInfo, userId, status, type, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().applyFamilyFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200) {
                    if (getMvpView() != null) {
                        getMvpView().applyFamily(response.str("message"), status, position);
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().applyFamilyFail(response.str("message"));
                    }
                }
            }
        });
    }

    /**
     * 设置申请方式
     */
    public void setApplyJoinMethod(FamilyInfo info, int joinMode) {

        mFindModel.setApplyJoinMethod(info, joinMode, new OkHttpManager.MyCallBack<Json>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().setApplyJoinMethodFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200) {
                    if (getMvpView() != null) {
                        getMvpView().setApplyJoinMethod();
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().setApplyJoinMethodFail(response.str("message"));
                    }
                }
            }
        });
    }

    /**
     * 修改家族信息
     *
     * @param type 类型
     */
    public void editFamilyTeam(FamilyInfo info, int type, String content) {

        mFindModel.editFamilyTeam(info, type, content, new OkHttpManager.MyCallBack<Json>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().editFamilyTeamFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200) {
                    if (getMvpView() != null) {
                        getMvpView().editFamilyTeam(content);
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().editFamilyTeamFail(response.str("message"));
                    }
                }
            }
        });
    }
}
