package com.vslk.lbgx.ui.me.withdraw.view;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.withdraw.bean.ExchangerInfo;
import com.tongdaxing.xchat_core.withdraw.bean.WithdrawInfo;
import com.tongdaxing.xchat_core.withdraw.bean.WithdrwaListInfo;

import java.util.List;

/**
 * 文件描述：
 *
 * @auther：zwk
 * @data：2019/2/15
 */
public interface IWithdrawView extends IMvpBaseView {

    default void onRemindToastError(String error){}
    default void onRemindToastSuc(){}

    default void onCheckCodeFailToast(String error){}
    default void onCheckCodeSucWeixinLogin(){}

    default void onRemindBindWeixinSucFail(String error){}
    default void onRemindBindWeixinSucToast(int type){}

    default void onGetWithdrawListSuccessView(List<WithdrwaListInfo> infos){}
    default void onGetWithDrawListFailView(String msg){}

    default void onGetWithdrawUserInfoSuccessView(WithdrawInfo info){}
    default void onGetWithdrawUserInfoFailView(String msg){}

    default void onRequestExchangeSuccessView(ExchangerInfo info){}
    default void onRequestExchangeFailView(String msg){}

    default void onGetSmsCodeSuccessView(){}
    default void onGetSmsCodeFailView(String msg){}

    default void onBinderAlipaySuccessView(){}
    default void onBinderAlipayFailView(String msg){}
}
