package com.vslk.lbgx.ui.me.wallet.presenter;

import com.vslk.lbgx.ui.me.UserMvpModel;
import com.vslk.lbgx.ui.me.wallet.view.IIncomeView;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.pay.bean.WalletInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.Map;

/**
 * Created by MadisonRong on 08/01/2018.
 */

public class IncomePresenter extends PayPresenter<IIncomeView> {

    private UserMvpModel userMvpModel;

    public IncomePresenter() {
        super();
        this.userMvpModel = new UserMvpModel();
    }

    public void loadWalletInfo() {
        Map<String,String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        params.put("Cache-Control", "no-cache");
        OkHttpManager.getInstance().getRequest(UriProvider.getWalletInfos(), params, new OkHttpManager.MyCallBack<ServiceResult<WalletInfo>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null &&  e != null)
                    getMvpView().getUserWalletInfoFail(e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<WalletInfo> response) {
                if (null != response && response.isSuccess()) {
                    if (getMvpView() != null && response.getData() != null)
                        getMvpView().setupUserWalletBalance(response.getData());
                } else {
                    if (getMvpView() != null && response != null)
                        getMvpView().getUserWalletInfoFail(response.getErrorMessage());
                }
            }
        });
    }

    public void hasBindPhone() {
        userMvpModel.isPhones(new OkHttpManager.MyCallBack<ServiceResult>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null && e != null)
                    getMvpView().hasBindPhoneFail(e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null && response.isSuccess()) {
                    if (getMvpView() != null)
                        getMvpView().hasBindPhone();
                } else {
                    if (getMvpView() != null && response != null)
                        getMvpView().hasBindPhoneFail(response.getErrorMessage());
                }
            }
        });
    }

}
