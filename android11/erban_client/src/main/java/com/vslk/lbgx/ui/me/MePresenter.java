package com.vslk.lbgx.ui.me;

import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.log.MLog;
import com.vslk.lbgx.model.mengcoin.MengCoinModel;
import com.vslk.lbgx.ui.me.task.view.IMeView;

import java.util.Map;

/**
 * Created by MadisonRong on 04/01/2018.
 */

public class MePresenter extends AbstractMvpPresenter<IMeView> {

    private final UserMvpModel userMvpModel;
    private UserInfo mUserInfo;
    private MengCoinModel mengCoinModel;

    public MePresenter() {
        userMvpModel = new UserMvpModel();
        mengCoinModel = new MengCoinModel();
    }

    public void initUserData() {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("queryUid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        //新增参数
        params.put("uid", CoreManager.getCore(IAuthCore.class).getCurrentUid() + "");
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().getRequest(UriProvider.getUserInfo(), params, new OkHttpManager.MyCallBack<ServiceResult<UserInfo>>() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(ServiceResult<UserInfo> response) {
                if (null != response) {
                    mUserInfo = response.getData();
                }else {
                    mUserInfo = userMvpModel.getUserDate();
                }
                setupUserData();
            }
        });
    }

    public void setupUserData() {
        if (mUserInfo == null) {
            MLog.error(this, "用户信息不存在！");
        }
        getMvpView().updateUserInfoUI(mUserInfo);
    }

    /**
     * 加测是否绑定手机
     */
    public void isPhone() {

        userMvpModel.isPhones(new OkHttpManager.MyCallBack<ServiceResult>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onIsBindPhoneFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().onIsBindPhone();
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().onIsBindPhoneFail(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().onIsBindPhoneFail("数据异常");
                    }
                }
            }
        });
    }

    /**
     * 检测是否设置过密码
     */
    public void checkPwd() {

        userMvpModel.checkPwd(new OkHttpManager.MyCallBack<Json>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onIsSetPwdFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(Json response) {
                if (response != null) {
                    if (response.num("code") == 200) {
                        if (getMvpView() != null) {
                            getMvpView().onIsSetPwd(response.boo("data"));
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().onIsSetPwdFail(response.str("message"));
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().onIsSetPwdFail("数据异常");
                    }
                }
            }
        });
    }

    /**
     * 修改或设置登录密码
     */
    public void modifyPassword(String oldPwd, String newPwd, String confirmPwd, String url) {

        userMvpModel.modifyPassword(oldPwd, newPwd, confirmPwd, url, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().modifyPasswordFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().modifyPassword();
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().modifyPasswordFail(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().modifyPasswordFail("数据异常");
                    }
                }
            }
        });
    }

    /**
     * 获取修改旧手机验证码
     */
    public void getModifyPhoneSMSCode(String phoneNumber, String type) {

        userMvpModel.getModifyPhoneSMSCode(phoneNumber, type, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().getModifyPhoneSMSCodeFail("换绑手机失败!");
                }
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().getModifyPhoneSMSCodeSuccess();
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().getModifyPhoneSMSCodeFail(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().getModifyPhoneSMSCodeFail("数据异常");
                    }
                }
            }
        });
    }

    /**
     * //绑定手机
     */
    public void getSMSCode(String phone) {

        userMvpModel.getSMSCode(phone, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().getModifyPhoneSMSCodeFail("绑定手机失败!");
                }
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().getModifyPhoneSMSCodeSuccess();
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().getModifyPhoneSMSCodeFail(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().getModifyPhoneSMSCodeFail("数据异常");
                    }
                }
            }
        });
    }

    /**
     * 验证手机
     */
    public void getPwSmsCode(String phone) {
        userMvpModel.getPwSmsCode(phone, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().getModifyPhoneSMSCodeFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().getModifyPhoneSMSCodeSuccess();
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().getModifyPhoneSMSCodeFail(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().getModifyPhoneSMSCodeFail("数据异常");
                    }
                }
            }
        });
    }

    /**
     * 换绑手机
     */
    public void modifyBinderPhone(String phone, String smsCode, String url) {

        userMvpModel.modifyBinderPhone(phone, smsCode, url, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onMoidfyOnBinnerFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null && response.isSuccess()) {
                    IUserCore core = CoreManager.getCore(IUserCore.class);
                    if (core != null && core.getCacheLoginUserInfo() != null) {
                        core.requestUserInfo(core.getCacheLoginUserInfo().getUid());
                    }
                    if (getMvpView() != null) {
                        getMvpView().onModifyOnBinner();
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().onMoidfyOnBinnerFail(response.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 验证手机
     */
    public void verifierPhone(String phone, String smsCode) {

        userMvpModel.verifierPhone(phone, smsCode, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().verifierPhoneFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null && response.isSuccess()) {
                    if (getMvpView() != null) {
                        getMvpView().verifierPhone();
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().verifierPhoneFail(response.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 绑定手机
     */
    public void binderPhone(String phone, String smsCode) {

        userMvpModel.bindPhone(phone, smsCode, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onBinderPhoneFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null && response.isSuccess()) {
                    IUserCore core = CoreManager.getCore(IUserCore.class);
                    if (core != null && core.getCacheLoginUserInfo() != null) {
                        core.requestUserInfo(core.getCacheLoginUserInfo().getUid());
                    }
                    if (getMvpView() != null) {
                        getMvpView().onBinderPhone();
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().onBinderPhoneFail(response.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 用户提交反馈
     */
    public void commitFeedback(String feedbackDesc, String contact) {

        userMvpModel.commitFeedback(feedbackDesc, contact, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().commitFeedbackFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().commitFeedback();
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().commitFeedbackFail(response.getMessage());
                        }
                    }
                }
            }
        });
    }

}
