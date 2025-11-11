package com.vslk.lbgx.ui.me.withdraw.presenter;

import android.util.Log;

import com.vslk.lbgx.ui.me.withdraw.model.WithDrawModel;
import com.vslk.lbgx.ui.me.withdraw.view.IWithdrawView;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.withdraw.bean.ExchangerInfo;
import com.tongdaxing.xchat_core.withdraw.bean.WithdrawInfo;
import com.tongdaxing.xchat_core.withdraw.bean.WithdrwaListInfo;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import java.util.List;

/**
 * 文件描述：提现功能的Presenter
 *
 * @auther：zwk
 * @data：2019/2/15
 */
public class WithdrawPresenter extends AbstractMvpPresenter<IWithdrawView> {

    private WithDrawModel mWithDrawModel;

    public WithdrawPresenter() {
        if (mWithDrawModel == null) {
            mWithDrawModel = new WithDrawModel();
        }
    }

    /**
     * 获取微信提现绑定验证码
     */
    public void getInviteCode() {

        mWithDrawModel.getInviteCode(new OkHttpManager.MyCallBack<ServiceResult>() {
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
     * 验证微信提现绑定验证码
     */
    public void checkCode(String code) {

        mWithDrawModel.getCheckCode(code, new OkHttpManager.MyCallBack<ServiceResult>() {
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
                            getMvpView().onCheckCodeSucWeixinLogin();
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


    /**
     * 微信提现绑定
     */
    public void bindWithdrawWeixin(String accessToken, String openId, String unionId, String phoneNumber, String code, String nickName, String realName) {

        mWithDrawModel.bindWithdrawWeixin(accessToken, openId, unionId, phoneNumber, code, nickName, realName, new OkHttpManager.MyCallBack<ServiceResult>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onRemindBindWeixinSucFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().onRemindBindWeixinSucToast(0);//这个0是无效值
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().onRemindBindWeixinSucFail(response.getMessage());
                            Log.e("bindWithdrawWeixin", "onResponse: " + response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().onRemindBindWeixinSucFail("验证码获取异常");
                    }
                }
            }
        });
    }

    /**
     * 获取提现列表
     */
    public void getWithdrawList() {

        mWithDrawModel.getWithdrawList(new OkHttpManager.MyCallBack<ServiceResult<List<WithdrwaListInfo>>>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onGetWithDrawListFailView(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<List<WithdrwaListInfo>> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().onGetWithdrawListSuccessView(response.getData());
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().onGetWithDrawListFailView(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().onGetWithDrawListFailView("数据异常");
                    }
                }
            }
        });
    }

    /**
     * 获取提现页用户信息
     */
    public void getWithdrawUserInfo() {

        mWithDrawModel.getWithdrawUserInfo(new OkHttpManager.MyCallBack<ServiceResult<WithdrawInfo>>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onGetWithdrawUserInfoFailView(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<WithdrawInfo> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().onGetWithdrawUserInfoSuccessView(response.getData());
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().onGetWithdrawUserInfoFailView(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().onGetWithdrawUserInfoFailView("数据异常");
                    }
                }
            }
        });
    }

    /**
     * 发起提现
     */
    public void requestExchange(int pid, int type) {

        mWithDrawModel.requestExchange(pid, type, new OkHttpManager.MyCallBack<ServiceResult<ExchangerInfo>>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onRequestExchangeFailView(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<ExchangerInfo> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().onRequestExchangeSuccessView(response.getData());
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().onRequestExchangeFailView(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().onRequestExchangeFailView("数据异常");
                    }
                }
            }
        });
    }

    /**
     * 获取绑定支付宝验证码
     */
    public void getSmsCode() {

        mWithDrawModel.getSmsCode(new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onGetSmsCodeFailView(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().onGetSmsCodeSuccessView();
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().onGetSmsCodeFailView(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().onGetSmsCodeFailView("数据异常");
                    }
                }
            }
        });
    }

    /**
     * 绑定支付宝
     */
    public void binderAlipay(String aliPayAccount, String aliPayAccountName, String code) {

        mWithDrawModel.binderAlipay(aliPayAccount, aliPayAccountName, code, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onBinderAlipayFailView(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().onBinderAlipaySuccessView();
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().onBinderAlipayFailView(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().onBinderAlipayFailView("数据异常");
                    }
                }
            }
        });
    }

}
