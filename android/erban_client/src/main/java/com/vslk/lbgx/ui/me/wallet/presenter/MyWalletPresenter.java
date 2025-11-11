package com.vslk.lbgx.ui.me.wallet.presenter;

import com.vslk.lbgx.ui.me.wallet.view.IMyWalletView;

/**
 * Created by MadisonRong on 08/01/2018.
 */

public class MyWalletPresenter extends PayPresenter<IMyWalletView> {

    public void loadWalletInfo() {
        refreshWalletInfo(false);
    }

    public void handleClick(int id) {
        getMvpView().handleClickByViewId(id);
    }
}
