package com.vslk.lbgx.presenter.invite;

import com.vslk.lbgx.model.invite.InviteFriendModel;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.redpacket.bean.RedPacketInfo;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.util.util.Json;

/**
 * 创建者      Created by dell
 * 创建时间    2019/1/4
 * 描述        邀请好友
 * <p>
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class InviteFriendPresenter extends AbstractMvpPresenter<IInviteFriendView> {

    private InviteFriendModel inviteModel;

    public InviteFriendPresenter() {
        if (this.inviteModel == null) {
            this.inviteModel = new InviteFriendModel();
        }
    }


    //红包提现接口已更新，无需验证是否绑定微信
    public void checkWxOpenId(String openId, String unionId, String accessToken) {


        inviteModel.checkWxOpenIdModel(openId, unionId, accessToken, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                if (e != null) {
                    getMvpView().checkFailure(e.getMessage());

                }
            }

            @Override
            public void onResponse(Json response) {
                if (response == null || getMvpView() == null) {
                    onError(new Exception());
                    return;
                }
                if (response.num("code") == 200) {

                    getMvpView().checkSucceed();

                } else if (response.num("code") == 500) {

                    getMvpView().checkFailure(response.str("message"));

                } else {
                    onError(new Exception());
                }
            }
        });
    }

    public void getRedPacket() {
        inviteModel.getRedPacket(new OkHttpManager.MyCallBack<ServiceResult<RedPacketInfo>>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onGetRedPacketFailView(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<RedPacketInfo> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().onGetRedPacketSuccessView(response.getData());
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().onGetRedPacketFailView(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().onGetRedPacketFailView("数据异常");
                    }
                }
            }
        });
    }

    /**
     * 保存邀请码
     */
    public void saveInviteCode(String inviteCode) {

        inviteModel.saveInviteCode(inviteCode, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onSaveInviteCodeFailView(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().onSaveInviteCodeSuccessView(inviteCode);
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().onSaveInviteCodeFailView(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().onSaveInviteCodeFailView("数据异常");
                    }
                }
            }
        });
    }

    /**
     * 获取微信验证码
     */
    public void getInviteCode() {
        inviteModel.getInviteCode(new OkHttpManager.MyCallBack<ServiceResult>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                }
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (null != response) {
                    String message = response.getMessage();
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {

                            getMvpView().onRemindToastSuc();
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().onRemindToastError(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().onRemindToastError("验证码获取异常");
                    }
                }
            }
        });
    }

    /**
     * 验证短信验证码
     */
    public void getCheckCode(String code) {
        inviteModel.getCheckCode(code, new OkHttpManager.MyCallBack<ServiceResult>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onRemindToastError(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().checkSucceed();
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().onRemindToastError(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().onRemindToastError("验证码验证异常");
                    }
                }
            }
        });
    }

}
