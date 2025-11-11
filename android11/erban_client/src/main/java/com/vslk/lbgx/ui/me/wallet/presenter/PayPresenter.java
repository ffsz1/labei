package com.vslk.lbgx.ui.me.wallet.presenter;

import com.vslk.lbgx.ui.me.wallet.model.PayModel;
import com.vslk.lbgx.ui.me.wallet.view.IPayView;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.pay.IPayCore;
import com.tongdaxing.xchat_core.pay.bean.WalletInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.Map;

/**
 * Created by MadisonRong on 08/01/2018.
 */

public class PayPresenter<T extends IPayView> extends AbstractMvpPresenter<T> {

    protected PayModel payModel;
    protected WalletInfo walletInfo;

    public PayPresenter() {
        this.payModel = new PayModel();
    }

    /**
     * 刷新钱包信息
     */
    public void refreshWalletInfo(boolean force) {
        Map<String,String> params = CommonParamUtil.getDefaultParam();
        String cacheStrategy = force ? "no-cache" : "max-stale";
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("Cache-Control", cacheStrategy);
        OkHttpManager.getInstance().getRequest(UriProvider.getWalletInfos(), params, new OkHttpManager.MyCallBack<ServiceResult<WalletInfo>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null && e != null) {
                    getMvpView().getUserWalletInfoFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<WalletInfo> response) {
                if (null != response && response.isSuccess() && response.getData() != null) {
                    walletInfo = response.getData();
                    CoreManager.getCore(IPayCore.class).setCurrentWalletInfo(walletInfo);
                    if (getMvpView() != null) {
                        getMvpView().setupUserWalletBalance(response.getData());
                    }
                } else {
                    if (getMvpView() != null && response != null) {
                        getMvpView().getUserWalletInfoFail(response.getErrorMessage());
                    }
                }
            }
        });
    }
}
