package com.tongdaxing.xchat_core.pay;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.activity.bean.TeenagerModelInfo;
import com.tongdaxing.xchat_core.auth.AccountInfo;
import com.tongdaxing.xchat_core.auth.IAuthClient;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.im.notification.INotificationCoreClient;
import com.tongdaxing.xchat_core.pay.bean.ChargeBean;
import com.tongdaxing.xchat_core.pay.bean.DianDianCoinInfo;
import com.tongdaxing.xchat_core.pay.bean.WalletInfo;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.NetworkUtils;

import java.util.List;
import java.util.Map;

import static com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_ACCOUNT;

/**
 * 实现网络的方法
 * Created by zhouxiangfeng on 2017/6/19.
 */

public class PayCoreImpl extends AbstractBaseCore implements IPayCore {

    public static final String TAG = "PayCoreImpl";
    private WalletInfo walletInfo;
    private DianDianCoinInfo dianDianCoinInfo;

    public PayCoreImpl() {
        CoreManager.addClient(this);
    }

    @Override
    public DianDianCoinInfo getDianDianCoinInfo() {
        return dianDianCoinInfo;
    }

    @Override
    public WalletInfo getCurrentWalletInfo() {
        return walletInfo;
    }

    @Override
    public void updatePrice(int price) {
        if (dianDianCoinInfo != null) {
            double gold = dianDianCoinInfo.getMcoinNum();
            if (gold > 0 && gold >= price) {
                dianDianCoinInfo.setMcoinNum(gold - price);
                notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_DIAN_DIAN_COIN_INFO_UPDATE, dianDianCoinInfo);
            }
        }
    }

    @Override
    public void minusGold(int price) {
        if (walletInfo != null) {
            double gold = walletInfo.getGoldNum();
            if (gold > 0 && gold >= price) {
                walletInfo.setGoldNum(gold - price);
                notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_WALLET_INFO_UPDATE, walletInfo);
            }
        }
    }

    @Override
    public void setCurrentWalletInfo(WalletInfo walletInfo) {
        this.walletInfo = walletInfo;
        notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_WALLET_INFO_UPDATE, walletInfo);
    }

    @CoreEvent(coreClientClass = INotificationCoreClient.class)
    public void onReceivedCustomNotification(JSONObject attachment) {
        int headType = attachment.getIntValue("first");
        int subType = attachment.getIntValue("second");
        if (headType > 0 && subType > 0) {
            if (headType == CUSTOM_MSG_HEADER_TYPE_ACCOUNT) {
                JSONObject jsonObject = (JSONObject) attachment.get("data");
                WalletInfo walletInfo = new WalletInfo();
                walletInfo.setUid(jsonObject.getLong("uid"));
                walletInfo.setDepositNum(jsonObject.getIntValue("depositNum"));
                walletInfo.setDiamondNum(jsonObject.getDouble("diamondNum"));
                walletInfo.setGoldNum(jsonObject.getIntValue("goldNum"));
                this.walletInfo = walletInfo;
                notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_WALLET_INFO_UPDATE, this.walletInfo);
            }
        }
    }

    @Override
    public void requestChargeOrOrderInfo() {
        String data = "";
        notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_GET_CHARGE_OR_ORDER_INFO, data);
    }

    //获取钱包信息
    @Override
    public void getWalletInfo(long uid) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(uid));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().getRequest(UriProvider.getWalletInfos(), params, new OkHttpManager.MyCallBack<ServiceResult<WalletInfo>>() {
            @Override
            public void onError(Exception e) {
                notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_GET_WALLENT_INOF_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<WalletInfo> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        walletInfo = response.getData();
                        notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_GET_WALLENT_INOF, response.getData());
                    } else {
                        notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_GET_WALLENT_INOF_FAIL, response.getMessage());
                    }
                }
            }
        });
    }

    //获取点点币信息
    @Override
    public void loadDianDianCoinInfos() {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.getDianDianCoinInfos(), params, new OkHttpManager.MyCallBack<ServiceResult<DianDianCoinInfo>>() {
            @Override
            public void onError(Exception e) {
                notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_GET_DIAN_DIAN_COIN_INOF_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<DianDianCoinInfo> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        dianDianCoinInfo = response.getData();
                        notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_GET_DIAN_DIAN_COIN_INOF, response.getData());
                    } else {
                        notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_GET_DIAN_DIAN_COIN_INOF_FAIL, response.getMessage());
                    }
                }
            }
        });
    }

    //青少年模式
    @Override
    public void isShowTeenagerModel() {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().getRequest(UriProvider.isShowTeenager(), params, new OkHttpManager.MyCallBack<ServiceResult<TeenagerModelInfo>>() {
            @Override
            public void onError(Exception e) {
//                notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_GET_DIAN_DIAN_COIN_INOF_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<TeenagerModelInfo> response) {
                Log.e("isShowTeenagerModel",response.toString());

                if (response != null) {
                    if (response.isSuccess()) {
                        notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_GET_TEENAGER_MODEL_INOF, response.getData());
                    } else {
//                        notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_GET_DIAN_DIAN_COIN_INOF_FAIL, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void getChargeList(int channelType) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("channelType", String.valueOf(channelType));

        OkHttpManager.getInstance().getRequest(UriProvider.getChargeList(), params, new OkHttpManager.MyCallBack<ServiceResult<List<ChargeBean>>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_GET_CHARGE_LIST_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<List<ChargeBean>> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_GET_CHARGE_LIST, response.getData());
                    } else {
                        notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_GET_CHARGE_LIST_FAIL, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void requestCharge(int chargeProdId, String payChannel) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("chargeProdId", String.valueOf(chargeProdId));
        params.put("payChannel", String.valueOf(payChannel));
        params.put("clientIp", NetworkUtils.getIPAddress(getContext()));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());

        OkHttpManager.getInstance().doPostRequest(UriProvider.requestCharge(), params, new OkHttpManager.MyCallBack<ServiceResult<JsonObject>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_GET_CHARGE_OR_ORDER_INFO_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<JsonObject> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_GET_CHARGE_OR_ORDER_INFO, response.getData().toString());
                    } else {
                        notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_GET_CHARGE_OR_ORDER_INFO_FAIL, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void requestCDKeyCharge(String code) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("code", code);
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());

        OkHttpManager.getInstance().doPostRequest(UriProvider.requestCDKeyCharge(), params, new OkHttpManager.MyCallBack<ServiceResult<WalletInfo>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_CD_KEY_CHARGE_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<WalletInfo> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        PayCoreImpl.this.walletInfo = response.getData();
                        notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_WALLET_INFO_UPDATE, response.getData());
                        notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_CD_KEY_CHARGE, response.getData().getAmount());
                    } else {
                        notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_CD_KEY_CHARGE_FAIL, response.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void exchangeGold(int diamondNum) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("diamondNum", String.valueOf(diamondNum));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());

        OkHttpManager.getInstance().doPostRequest(UriProvider.changeGold(), params, new OkHttpManager.MyCallBack<ServiceResult<WalletInfo>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_EXCHANGE_GOLD_FAIL, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<WalletInfo> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_EXCHANGE_GOLD, response.getData());

                        PayCoreImpl.this.walletInfo = response.getData();
                        notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_WALLET_INFO_UPDATE, response.getData());
                    } else {
                        notifyClients(IPayCoreClient.class, IPayCoreClient.METHOD_ON_EXCHANGE_GOLD_FAIL, response.getMessage());
                    }
                }
            }
        });
    }
}
