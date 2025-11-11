package com.tongdaxing.xchat_core.withdraw;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.withdraw.bean.ExchangerInfo;
import com.tongdaxing.xchat_core.withdraw.bean.WithdrawInfo;
import com.tongdaxing.xchat_core.withdraw.bean.WithdrwaListInfo;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.LogUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/24.
 */

public class WithdrawCoreImpl extends AbstractBaseCore implements IWithdrawCore {

    /**
     * 获取提现列表
     */
    @Override
    public void getWithdrawList() {
        Map<String, String> params = CommonParamUtil.getDefaultParam();

        OkHttpManager.getInstance().getRequest(UriProvider.getWithdrawList(), params, new OkHttpManager.MyCallBack<ServiceResult<List<WithdrwaListInfo>>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IWithdrawCoreClient.class, IWithdrawCoreClient.METHOD_ON_GET_WITHDRAW_LIST_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<List<WithdrwaListInfo>> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        notifyClients(IWithdrawCoreClient.class, IWithdrawCoreClient.METHOD_ON_GET_WITHDRAW_LIST, response.getData());
                    } else {
                        notifyClients(IWithdrawCoreClient.class, IWithdrawCoreClient.METHOD_ON_GET_WITHDRAW_LIST_FAIL, response.getMessage());
                    }
                } else {
                    notifyClients(IWithdrawCoreClient.class, IWithdrawCoreClient.METHOD_ON_GET_WITHDRAW_LIST_FAIL);
                }
            }
        });
    }

    /**
     * 获取提现页用户信息
     */
    @Override
    public void getWithdrawUserInfo() {

        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid",String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));

        OkHttpManager.getInstance().getRequest(UriProvider.getWithdrawInfo(), params, new OkHttpManager.MyCallBack<ServiceResult<WithdrawInfo>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IWithdrawCoreClient.class, IWithdrawCoreClient.METHOD_ON_GET_WITHDRAW_USER_INFO_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<WithdrawInfo> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        notifyClients(IWithdrawCoreClient.class, IWithdrawCoreClient.METHOD_ON_GET_WITHDRAW_USER_INFO, response.getData());
                    } else {
                        notifyClients(IWithdrawCoreClient.class, IWithdrawCoreClient.METHOD_ON_GET_WITHDRAW_USER_INFO_FAIL, response.getMessage());
                    }
                } else {
                    notifyClients(IWithdrawCoreClient.class, IWithdrawCoreClient.METHOD_ON_GET_WITHDRAW_USER_INFO_FAIL);
                }
            }
        });
    }

    /**
     * 发起兑换
     */
    @Override
    public void requestExchange(long uid, int pid, int type) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(uid));
        params.put("pid",String.valueOf(pid));
        params.put("type", String.valueOf(type));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());

        OkHttpManager.getInstance().doPostRequest(UriProvider.requestExchange(), params, new OkHttpManager.MyCallBack<ServiceResult<ExchangerInfo>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IWithdrawCoreClient.class, IWithdrawCoreClient.METHOD_ON_REQUEST_EXCHANGE_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<ExchangerInfo> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        notifyClients(IWithdrawCoreClient.class, IWithdrawCoreClient.METHOD_ON_REQUEST_EXCHANGE, response.getData());
                    } else {
                        notifyClients(IWithdrawCoreClient.class, IWithdrawCoreClient.METHOD_ON_REQUEST_EXCHANGE_FAIL, response.getMessage());
                    }
                } else {
                    notifyClients(IWithdrawCoreClient.class, IWithdrawCoreClient.METHOD_ON_REQUEST_EXCHANGE_FAIL);
                }
            }
        });
    }

    /**
     * 获取绑定支付宝验证码
     */
    @Override
    public void getSmsCode(long uid) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(uid));

        OkHttpManager.getInstance().getRequest(UriProvider.getSms(), params, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IWithdrawCoreClient.class, IWithdrawCoreClient.METHOD_ON_GET_SMS_CODE_FAIL,e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (null != response) {
                    if (response.isSuccess()) {
                        notifyClients(IWithdrawCoreClient.class, IWithdrawCoreClient.METHOD_ON_GET_SMS_CODE);
                    } else {
                        notifyClients(IWithdrawCoreClient.class, IWithdrawCoreClient.METHOD_ON_GET_SMS_CODE_FAIL,response.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 绑定支付宝
     */
    @Override
    public void binderAlipay(String aliPayAccount, String aliPayAccountName, String code) {

        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("aliPayAccount",aliPayAccount);
        params.put("aliPayAccountName",aliPayAccountName);
        params.put("code",code);
        params.put("ticket",CoreManager.getCore(IAuthCore.class).getTicket());

        OkHttpManager.getInstance().doPostRequest(UriProvider.binder(), params, new OkHttpManager.MyCallBack<ServiceResult>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IWithdrawCoreClient.class, IWithdrawCoreClient.METHOD_ON_BINDER_ALIPAY_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        notifyClients(IWithdrawCoreClient.class, IWithdrawCoreClient.METHOD_ON_BINDER_ALIPAY);
                    } else {
                        notifyClients(IWithdrawCoreClient.class, IWithdrawCoreClient.METHOD_ON_BINDER_ALIPAY_FAIL, response.getMessage());
                    }
                }
            }
        });
    }
}
