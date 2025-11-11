package com.vslk.lbgx.ui.me.wallet.presenter;

import android.content.Context;

import com.google.gson.JsonObject;
import com.vslk.lbgx.ui.me.wallet.model.PayModel;
import com.vslk.lbgx.ui.me.wallet.view.IChargeView;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.pay.bean.ChargeBean;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.NetworkUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by MadisonRong on 05/01/2018.
 */
public class ChargePresenter extends PayPresenter<IChargeView> {

    private static final String TAG = "ChargePresenter";

    public ChargePresenter() {
        this.payModel = new PayModel();
    }

    /**
     * 加载用户信息(显示用户账号和钱包余额)
     */
    public void loadUserInfo() {
        UserInfo userInfo = payModel.getUserInfo();
        if (userInfo != null) {
            getMvpView().setupUserInfo(userInfo);
        }
        refreshWalletInfo(false);
    }

    /**
     * 获取充值产品列表
     */
    public void getChargeList() {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("channelType", "1");
        OkHttpManager.getInstance().getRequest(UriProvider.getChargeList(), params, new OkHttpManager.MyCallBack<ServiceResult<List<ChargeBean>>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().getChargeListFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<List<ChargeBean>> response) {
                if (response != null && response.isSuccess()) {
                    if (getMvpView() != null) {
                        getMvpView().buildChargeList(response.getData());
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().getChargeListFail(response == null ? "数据异常" : response.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 显示充值选项(支付宝或者微信)
     */
    public void showChargeOptionsDialog() {
        if (getMvpView() != null)
            getMvpView().displayChargeOptionsDialog();

    }

    //请求使用汇聚支付
    public void getJoinPayData(String chargeProdId, String payChannel) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        param.put("chargeProdId", chargeProdId);
        param.put("payChannel", payChannel);
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.getJoinPay(), param, new OkHttpManager.MyCallBack<ServiceResult<JsonObject>>() {
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                getMvpView().getChargeOrOrderInfoFail(e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<JsonObject> response) {
                if (null != response && response.isSuccess()) {
                    getMvpView().getChargeOrOrderInfo(response.getData().toString());
                } else {
                    getMvpView().getChargeOrOrderInfoFail(null != response ? response.getErrorMessage() : "");
                }
            }
        });
    }

    /**
     * 发起充值
     *
     * @param context      context
     * @param chargeProdId 充值产品 ID
     * @param payChannel   充值渠道，目前只支持
     *                     {@link com.tongdaxing.xchat_core.Constants#CHARGE_ALIPAY} 和
     *                     {@link com.tongdaxing.xchat_core.Constants#CHARGE_WX}
     */
    public void requestCharge(Context context, String chargeProdId, String payChannel) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        param.put("chargeProdId", chargeProdId);
        param.put("payChannel", payChannel);
        param.put("clientIp", NetworkUtils.getIPAddress(context));
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.requestCharge(), param, new OkHttpManager.MyCallBack<ServiceResult<JsonObject>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null)
                    getMvpView().getChargeOrOrderInfoFail(e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<JsonObject> response) {
                if (response != null && response.isSuccess()) {
                    if (getMvpView() != null)
                        getMvpView().getChargeOrOrderInfo(response.getData().toString());
                } else {
                    if (getMvpView() != null)
                        getMvpView().getChargeOrOrderInfoFail(response == null ? "数据异常" : response.getMessage());
                }
            }
        });
    }

    /**
     * 支付宝汇潮充值渠道
     *
     * @param context
     * @param chargeProdId
     */
    public void requestHuiJuPay(Context context, String chargeProdId) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        param.put("chargeProdId", chargeProdId);
        param.put("payChannel", "ecpss_alipay");
        param.put("clientIp", NetworkUtils.getIPAddress(context));
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.requestAliHuiChaoCharge(), param,
                new OkHttpManager.MyCallBack<ServiceResult<JsonObject>>() {
                    @Override
                    public void onError(Exception e) {
                        if (getMvpView() != null)
                            getMvpView().getChargeOrOrderInfoFail(e.getMessage());
                    }

                    @Override
                    public void onResponse(ServiceResult<JsonObject> response) {
                        if (response != null && response.isSuccess()) {
                            if (getMvpView() != null)
                                getMvpView().getChargeOrOrderInfo(response.getData().toString());
                        } else {
                            if (getMvpView() != null)
                                getMvpView().getChargeOrOrderInfoFail(response == null ? "数据异常" : response.getMessage());
                        }
                    }
                });

    }
}
