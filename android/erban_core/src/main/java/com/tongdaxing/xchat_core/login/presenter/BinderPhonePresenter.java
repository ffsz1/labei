//package com.tongdaxing.xchat_core.login.presenter;
//
//import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
//import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
//import com.tongdaxing.xchat_core.login.model.UserMvpModel;
//import com.tongdaxing.xchat_core.login.view.IBinderPhoneView;
//import com.tongdaxing.xchat_core.user.IUserCore;
//import com.tongdaxing.xchat_core.user.bean.UserInfo;
//import com.tongdaxing.xchat_framework.coremanager.CoreManager;
//import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
//
///**
// * Function:
// * Author: Edward on 2019/6/5
// */
//public class BinderPhonePresenter extends AbstractMvpPresenter<IBinderPhoneView> {
////    private final UserMvpModel userMvpModel;
//    private UserInfo mUserInfo;
//
//    public BinderPhonePresenter() {
////        userMvpModel = new UserMvpModel();
//    }
//
//    /**
//     * //绑定手机
//     */
//    public void getSMSCode(String phone) {
//        userMvpModel.getSMSCode(phone, new OkHttpManager.MyCallBack<ServiceResult>() {
//            @Override
//            public void onError(Exception e) {
//                if (getMvpView() != null) {
//                    getMvpView().getModifyPhoneSMSCodeFail("绑定手机失败!");
//                }
//            }
//
//            @Override
//            public void onResponse(ServiceResult response) {
//                if (response != null) {
//                    if (response.isSuccess()) {
//                        if (getMvpView() != null) {
//                            getMvpView().getModifyPhoneSMSCodeSuccess();
//                        }
//                    } else {
//                        if (getMvpView() != null) {
//                            getMvpView().getModifyPhoneSMSCodeFail(response.getMessage());
//                        }
//                    }
//                } else {
//                    if (getMvpView() != null) {
//                        getMvpView().getModifyPhoneSMSCodeFail("数据异常");
//                    }
//                }
//            }
//        });
//    }
//
//    /**
//     * 绑定手机
//     */
//    public void binderPhone(String phone, String smsCode) {
//        userMvpModel.bindPhone(phone, smsCode, new OkHttpManager.MyCallBack<ServiceResult>() {
//            @Override
//            public void onError(Exception e) {
//                if (getMvpView() != null) {
//                    getMvpView().onBinderPhoneFail(e.getMessage());
//                }
//            }
//
//            @Override
//            public void onResponse(ServiceResult response) {
//                if (response != null && response.isSuccess()) {
//                    IUserCore core = CoreManager.getCore(IUserCore.class);
//                    if (core != null && core.getCacheLoginUserInfo() != null) {
//                        core.requestUserInfo(core.getCacheLoginUserInfo().getUid());
//                    }
//                    if (getMvpView() != null) {
//                        getMvpView().onBinderPhone();
//                    }
//                } else {
//                    if (getMvpView() != null) {
//                        getMvpView().onBinderPhoneFail(response.getMessage());
//                    }
//                }
//            }
//        });
//    }
//}
