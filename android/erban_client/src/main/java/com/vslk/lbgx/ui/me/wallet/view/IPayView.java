package com.vslk.lbgx.ui.me.wallet.view;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.pay.bean.WalletInfo;

/**
 * Created by MadisonRong on 08/01/2018.
 */

public interface IPayView extends IMvpBaseView {

    public void setupUserWalletBalance(WalletInfo walletInfo);

    public void getUserWalletInfoFail(String error);
}
