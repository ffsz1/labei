package com.vslk.lbgx.ui.me.wallet.view;

import com.tongdaxing.xchat_core.pay.bean.ExchangeAwardInfo;
import com.tongdaxing.xchat_core.pay.bean.WalletInfo;

/**
 * Created by MadisonRong on 09/01/2018.
 */

public interface IExchangeGoldView extends IPayView {

    public void toastForError(int errorResId);

    public void displayResult(String result);

    public void requestExchangeGold(long value);

    public void exchangeGold(WalletInfo walletInfo);

    public void exchangeGoldFail(int code,String error);

    void showAward(ExchangeAwardInfo data);

    void requestExchangeGold(long value, String sms);
}
