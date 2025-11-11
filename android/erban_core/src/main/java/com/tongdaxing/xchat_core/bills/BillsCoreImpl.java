package com.tongdaxing.xchat_core.bills;

import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.bills.bean.ExpendListInfo;
import com.tongdaxing.xchat_core.bills.bean.IncomeListInfo;
import com.tongdaxing.xchat_core.bills.bean.RedBagListInfo;
import com.tongdaxing.xchat_framework.coremanager.AbstractBaseCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;

import java.util.Map;

/**
 * @author Seven
 * @date 2017/9/9
 */

public class BillsCoreImpl extends AbstractBaseCore implements IBillsCore {

    private static final String TAG = "BillsCoreImpl";

    /**
     * 账单获取情况
     *
     * @param type 1：礼物支出记录 2：礼物收入记录 3：密聊记录 4：金币收入 5：提现记录
     */
    private void getBillRecode(int pageNo, int pageSize, long time, final int type) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("date", String.valueOf(time));
        params.put("type", String.valueOf(type));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());

        OkHttpManager.getInstance().getRequest(UriProvider.getBillRecord(), params, getResponseListener(type));
    }

    private OkHttpManager.MyCallBack getResponseListener(int type) {
        switch (type) {
            case 1:
                return giftExpendListener;
            case 2:
                return giftInComeListener;
            case 3:
                return chatListener;
            case 4:
                return getChargeListener;
            case 5:
                return getWithdrawListener;
            default:
                return null;
        }
    }

    //礼物支出监听
    private OkHttpManager.MyCallBack<ServiceResult<ExpendListInfo>> giftExpendListener = new OkHttpManager.MyCallBack<ServiceResult<ExpendListInfo>>() {

        @Override
        public void onError(Exception e) {
            notifyClients(IBillsCoreClient.class, IBillsCoreClient.METHOD_GET_EXPEND_BILLS_ERROR, e.getMessage());
        }

        @Override
        public void onResponse(ServiceResult<ExpendListInfo> response) {
            if (response != null) {
                if (response.isSuccess()) {
                    notifyClients(IBillsCoreClient.class, IBillsCoreClient.METHOD_GET_EXPEND_BILLS, response.getData());
                } else {
                    notifyClients(IBillsCoreClient.class, IBillsCoreClient.METHOD_GET_EXPEND_BILLS_ERROR,
                            response.getMessage());
                }
            }
        }
    };

    //礼物收入监听
    private OkHttpManager.MyCallBack<ServiceResult<IncomeListInfo>> giftInComeListener = new OkHttpManager.MyCallBack<ServiceResult<IncomeListInfo>>() {

        @Override
        public void onError(Exception e) {
            notifyClients(IBillsCoreClient.class, IBillsCoreClient.METHOD_GET_INCOME_BILLS_ERROR, e.getMessage());
        }

        @Override
        public void onResponse(ServiceResult<IncomeListInfo> response) {
            if (response != null) {
                if (response.isSuccess()) {
                    notifyClients(IBillsCoreClient.class, IBillsCoreClient.METHOD_GET_INCOME_BILLS, response.getData());
                } else {
                    notifyClients(IBillsCoreClient.class, IBillsCoreClient.METHOD_GET_INCOME_BILLS_ERROR,
                            response.getMessage());
                }
            }
        }
    };

    //提现监听
    private OkHttpManager.MyCallBack<ServiceResult<IncomeListInfo>> getWithdrawListener = new OkHttpManager.MyCallBack<ServiceResult<IncomeListInfo>>() {

        @Override
        public void onError(Exception e) {
            notifyClients(IBillsCoreClient.class, IBillsCoreClient.METHOD_GET_WIHTDRAW_BILLS_ERROR, e.getMessage());
        }

        @Override
        public void onResponse(ServiceResult<IncomeListInfo> response) {
            if (response != null) {
                if (response.isSuccess()) {
                    notifyClients(IBillsCoreClient.class, IBillsCoreClient.METHOD_GET_WIHTDRAW_BILLS, response.getData());
                } else {
                    notifyClients(IBillsCoreClient.class, IBillsCoreClient.METHOD_GET_WIHTDRAW_BILLS_ERROR, response.getMessage());

                }
            }
        }
    };

    //充值记录监听
    private OkHttpManager.MyCallBack<ServiceResult<ExpendListInfo>> getChargeListener = new OkHttpManager.MyCallBack<ServiceResult<ExpendListInfo>>() {

        @Override
        public void onError(Exception e) {
            notifyClients(IBillsCoreClient.class, IBillsCoreClient.METHOD_GET_CHARGE_BILLS_ERROR, e.getMessage());
        }

        @Override
        public void onResponse(ServiceResult<ExpendListInfo> response) {
            if (response != null) {
                if (response.isSuccess()) {
                    notifyClients(IBillsCoreClient.class, IBillsCoreClient.METHOD_GET_CHARGE_BILLS, response.getData());
                } else {
                    notifyClients(IBillsCoreClient.class, IBillsCoreClient.METHOD_GET_CHARGE_BILLS_ERROR,
                            response.getMessage());
                }
            }
        }
    };

    //密聊监听
    private OkHttpManager.MyCallBack<ServiceResult<IncomeListInfo>> chatListener = new OkHttpManager.MyCallBack<ServiceResult<IncomeListInfo>>() {

        @Override
        public void onError(Exception e) {
            notifyClients(IBillsCoreClient.class, IBillsCoreClient.METHOD_GET_ORDER_INCOME_BILLS_ERROR, e.getMessage());
        }

        @Override
        public void onResponse(ServiceResult<IncomeListInfo> response) {
            if (response != null) {
                if (response.isSuccess()) {
                    notifyClients(IBillsCoreClient.class, IBillsCoreClient.METHOD_GET_ORDER_INCOME_BILLS,
                            response.getData());
                } else {
                    notifyClients(IBillsCoreClient.class, IBillsCoreClient.METHOD_GET_ORDER_INCOME_BILLS_ERROR,
                            response.getMessage());
                }
            }
        }
    };

    //礼物收入2
    @Override
    public void getGiftIncomeBills(int pageNo, int pageSize, long time) {
        getBillRecode(pageNo, pageSize, time, 2);
    }

    //礼物支出1
    @Override
    public void getGiftExpendBills(int pageNo, int pageSize, long time) {
        getBillRecode(pageNo, pageSize, time, 1);
    }

    //提现记录5
    @Override
    public void getWithdrawBills(int pageNo, int pageSize, long time) {
        getBillRecode(pageNo, pageSize, time, 5);
    }

    @Override
    public void getWithdrawRedBills(int pageNo, int pageSize, long time) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("date", String.valueOf(time));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());

        OkHttpManager.getInstance().getRequest(UriProvider.getPacketRecordDeposit(), params, new OkHttpManager.MyCallBack<ServiceResult<RedBagListInfo>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IBillsCoreClient.class, IBillsCoreClient.METHOD_GET_WITHDRAW_RED_BILLS_ERROR, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<RedBagListInfo> response) {
                if (response != null) {
                    if (response.isSuccess() && response.getData() != null) {
                        notifyClients(IBillsCoreClient.class, IBillsCoreClient.METHOD_GET_WITHDRAW_RED_BILLS,
                                response.getData());
                    } else {
                        notifyClients(IBillsCoreClient.class, IBillsCoreClient.METHOD_GET_WITHDRAW_RED_BILLS_ERROR,
                                response.getMessage());
                    }
                }
            }
        });
    }

    //密聊记录：3
    @Override
    public void getChatBills(int pageNo, int pageSize, long time) {
        getBillRecode(pageNo, pageSize, time, 3);
    }


    //充值记录4
    @Override
    public void getChargeBills(int pageNo, int pageSize, long time) {
        getBillRecode(pageNo, pageSize, time, 4);
    }

    @Override
    public void getRedBagBills(int pageNo, int pageSize, long time) {
        Map<String, String> params = CommonParamUtil.getDefaultParam();
        params.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("date", String.valueOf(time));
        params.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());

        OkHttpManager.getInstance().getRequest(UriProvider.getPacketRecordDeposit(), params, new OkHttpManager.MyCallBack<ServiceResult<RedBagListInfo>>() {

            @Override
            public void onError(Exception e) {
                notifyClients(IBillsCoreClient.class, IBillsCoreClient.METHOD_GET_RED_BAG_BILLS_ERROR, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<RedBagListInfo> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        notifyClients(IBillsCoreClient.class, IBillsCoreClient.METHOD_GET_RED_BAG_BILLS, response.getData());
                    } else {
                        notifyClients(IBillsCoreClient.class, IBillsCoreClient.METHOD_GET_RED_BAG_BILLS_ERROR, response.getMessage());
                    }
                }
            }
        });
    }
}
