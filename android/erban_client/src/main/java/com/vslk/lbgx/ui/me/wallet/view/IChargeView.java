package com.vslk.lbgx.ui.me.wallet.view;

import com.tongdaxing.xchat_core.pay.bean.ChargeBean;
import com.tongdaxing.xchat_core.user.bean.UserInfo;

import java.util.List;

/**
 * Created by MadisonRong on 05/01/2018.
 */

public interface IChargeView extends IPayView {

    public void setupUserInfo(UserInfo userInfo);

    public void buildChargeList(List<ChargeBean> chargeBeanList);

    public void getChargeListFail(String error);

    public void displayChargeOptionsDialog();

    public void getChargeOrOrderInfo(String data);

    public void getChargeOrOrderInfoFail(String error);
}
