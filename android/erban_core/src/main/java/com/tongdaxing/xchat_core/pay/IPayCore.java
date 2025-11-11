package com.tongdaxing.xchat_core.pay;

import com.tongdaxing.xchat_core.pay.bean.DianDianCoinInfo;
import com.tongdaxing.xchat_core.pay.bean.WalletInfo;
import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

/**
 * 访问网络 的方法
 * Created by zhouxiangfeng on 2017/6/19.
 */

public interface IPayCore extends IBaseCore {
    void updatePrice(int price);

    DianDianCoinInfo getDianDianCoinInfo();

    void loadDianDianCoinInfos();

    void isShowTeenagerModel();

    WalletInfo getCurrentWalletInfo();

    void minusGold(int price);

    void setCurrentWalletInfo(WalletInfo walletInfo);

    void requestChargeOrOrderInfo();

    //    查询钱包的方法，需要加权限ticket
    void getWalletInfo(long uid);

    /*
        获取充值产品列表
        1,支付宝
        2，微信公众号充值
        3，苹果充值
     */
    void getChargeList(int channelType);

    //发起充值u
    void requestCharge(int chargeProdId, String payChannel);

    void requestCDKeyCharge(String code);

    void exchangeGold(int diamondNum);
}
