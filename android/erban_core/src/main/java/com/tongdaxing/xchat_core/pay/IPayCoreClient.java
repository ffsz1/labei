package com.tongdaxing.xchat_core.pay;

import com.tongdaxing.xchat_core.activity.bean.TeenagerModelInfo;
import com.tongdaxing.xchat_core.pay.bean.ChargeBean;
import com.tongdaxing.xchat_core.pay.bean.DianDianCoinInfo;
import com.tongdaxing.xchat_core.pay.bean.WalletInfo;
import com.tongdaxing.xchat_framework.coremanager.ICoreClient;

import java.util.List;

/**
 * 　请求网络回调的方法
 * Created by zhouxiangfeng on 2017/6/19.
 */
public interface IPayCoreClient extends ICoreClient {
    public static final String METHOD_ON_DIAN_DIAN_COIN_INFO_UPDATE = "onDianDianCoinInfoUpdate";

    public static final String METHOD_ON_GET_CHARGE_OR_ORDER_INFO = "onGetChargeOrOrderInfo";
    public static final String METHOD_ON_GET_CHARGE_OR_ORDER_INFO_FAIL = "onGetChargeOrOrderInfoFail";

    public static final String METHOD_ON_GET_WALLENT_INOF = "onGetWalletInfo";
    public static final String METHOD_ON_GET_WALLENT_INOF_FAIL = "onGetWalletInfoFail";

    public static final String METHOD_ON_GET_DIAN_DIAN_COIN_INOF = "onGetDianDianCoinInfo";
    public static final String METHOD_ON_GET_DIAN_DIAN_COIN_INOF_FAIL = "onGetDianDianCoinInfoFail";

    public static final String METHOD_ON_GET_CHARGE_LIST = "onGetChargeList";
    public static final String METHOD_ON_GET_CHARGE_LIST_FAIL = "onGetChargeListFail";
    public static final String METHOD_ON_WALLET_INFO_UPDATE = "onWalletInfoUpdate";
    public static final String METHOD_ON_EXCHANGE_GOLD = "onExchangeGold";
    public static final String METHOD_ON_EXCHANGE_GOLD_FAIL = "onExchangeGoldFail";
    public static final String METHOD_ON_CD_KEY_CHARGE = "onCDKeyCharge";
    public static final String METHOD_ON_CD_KEY_CHARGE_FAIL = "onCDKeyChargeFail";

    public static final String METHOD_ON_GET_TEENAGER_MODEL_INOF = "onGetTeenagerModelInfo";

    void onDianDianCoinInfoUpdate(DianDianCoinInfo dianDianCoinInfo);

    void onWalletInfoUpdate(WalletInfo walletInfo);

    void onGetChargeOrOrderInfo(String data);

    void onGetChargeOrOrderInfoFail(String error);

    void onGetDianDianCoinInfo(DianDianCoinInfo dianDianCoinInfo);

    void onGetDianDianCoinInfoFail(String error);

    void onGetTeenagerModelInfo(TeenagerModelInfo teenagerModelInfo);

    void onGetWalletInfo(WalletInfo walletInfo);

    void onGetWalletInfoFail(String error);

    void onGetChargeList(List<ChargeBean> chargeBeanList);

    void onGetChargeListFail(String error);

    void onExchangeGold(WalletInfo walletInfo);

    void onExchangeGoldFail(String error);

    void onCDKeyCharge(int gold);

    void onCDKeyChargeFail(String error);

}
