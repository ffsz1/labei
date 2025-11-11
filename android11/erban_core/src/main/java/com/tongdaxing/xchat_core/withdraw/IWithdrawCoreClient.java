package com.tongdaxing.xchat_core.withdraw;

import com.tongdaxing.xchat_core.withdraw.bean.ExchangerInfo;
import com.tongdaxing.xchat_core.withdraw.bean.WithdrawInfo;
import com.tongdaxing.xchat_core.withdraw.bean.WithdrwaListInfo;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

import java.util.List;

/**
 * Created by Administrator on 2017/7/24.
 */

public interface IWithdrawCoreClient extends ICoreClient {
    public static final String METHOD_ON_GET_WITHDRAW_LIST  ="onGetWithdrawList";
    public static final String METHOD_ON_GET_WITHDRAW_LIST_FAIL  ="onGetWithdrawListFail";

    public static final String METHOD_ON_GET_WITHDRAW_USER_INFO  ="onGetWithdrawUserInfo";
    public static final String METHOD_ON_GET_WITHDRAW_USER_INFO_FAIL  ="onGetWithdrawUserInfoFail";

    public static final String METHOD_ON_GET_SMS_CODE_FAIL = "onGetSmsCodeFail";
    public static final String METHOD_ON_GET_SMS_CODE = "onGetSmsCode";

    public static final String METHOD_ON_REQUEST_EXCHANGE ="onRequestExchange";
    public static final String METHOD_ON_REQUEST_EXCHANGE_FAIL ="onRequestExchangeFail";


    public static final String METHOD_ON_BINDER_ALIPAY ="onBinderAlipay";
    public static final String METHOD_ON_BINDER_ALIPAY_FAIL ="onBinderAlipayFail";



    void onGetWithdrawList(List<WithdrwaListInfo> withdrwaListInfo);
    void onGetWithdrawListFail(String error);

    void onGetWithdrawUserInfo(WithdrawInfo withdrawInfo);
    void onGetWithdrawUserInfoFail(String error);

    void onRequestExchange(ExchangerInfo exchangerInfo);
    void onRequestExchangeFail(String error);

    void onBinderAlipay();
    void onBinderAlipayFail(String error);

    void onGetSmsCodeFail(String error);
    void onGetSmsCode();

}
