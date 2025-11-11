package com.vslk.lbgx.ui.me.wallet.presenter;

import android.content.Context;

import com.netease.nim.uikit.common.util.string.StringUtil;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.pay.IPayCore;
import com.tongdaxing.xchat_core.pay.bean.ExchangeAwardInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.http_image.util.CommonParamUtil;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.me.wallet.view.IExchangeGoldView;

import java.util.Map;

/**
 * Created by MadisonRong on 09/01/2018.
 */

public class ExchangeGoldPresenter extends PayPresenter<IExchangeGoldView> {

    public void calculateResult(String input) {
        if (!StringUtil.isEmpty(input) && isNumeric(input)) {
            long value = 0;
            try {
                value = Long.parseLong(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isTenMultiple(value)) {
                if (getMvpView() != null)
                    getMvpView().displayResult(value + "");
            } else {
                if (getMvpView() != null)
                    getMvpView().displayResult(0 + "");
            }
        } else {
            if (getMvpView() != null)
                getMvpView().displayResult(0 + "");
        }
    }

//    public void confirmToExchangeGold(String input, Context context,String sms,String phone) {
//        confirmToExchangeGold(input,context,sms,phone);
//    }

    public void confirmToExchangeGold(String input, Context context,String sms,String phone) {
        if (StringUtil.isEmpty(input)) {
            getMvpView().toastForError(R.string.exchange_gold_error_empty_input);
            return;
        }

        long value = Long.parseLong(input);
        if (!isTenMultiple(value)) {
            getMvpView().toastForError(R.string.exchange_gold_error_is_not_ten_multiple);
            return;
        }
        if (walletInfo == null) {
            return;
        }
        if (value > walletInfo.getDiamondNum()) {
            getMvpView().toastForError(R.string.exchange_gold_error_diamond_less);
            return;
        }

        DialogManager mDialogManager = new DialogManager(context);
        mDialogManager.showOkCancelDialog("是否确认兑换开心？",true,new DialogManager.AbsOkDialogListener(){
            @Override
            public void onOk() {
                exchangeGold(String.valueOf(value),sms,phone);
            }
        });

//        if (TextUtils.isEmpty(sms))
//            getMvpView().requestExchangeGold(value);
//        else
//            getMvpView().requestExchangeGold(value, sms);
    }


    public void exchangeGold(String diamondNum,String sms,String phone) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        param.put("diamondNum", diamondNum);
        param.put("smsCode", sms);
        param.put("phone", phone);
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.changeGold(), param, new OkHttpManager.MyCallBack<ServiceResult<ExchangeAwardInfo>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null && e != null)
                    getMvpView().exchangeGoldFail(-1, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<ExchangeAwardInfo> data) {
                if (null != data && data.isSuccess() && data.getData() != null) {
                    walletInfo = data.getData();
                    if (getMvpView() != null) {
                        getMvpView().exchangeGold(data.getData());
                        getMvpView().setupUserWalletBalance(data.getData());
                        getMvpView().showAward(data.getData());
                    }
                    CoreManager.getCore(IPayCore.class).setCurrentWalletInfo(data.getData());
                } else {
                    if (getMvpView() != null && data != null)
                        getMvpView().exchangeGoldFail(data.getCode(), data.getErrorMessage());
                }
            }
        });
    }

    public void exchangeGold(String diamondNum, String sms) {
        Map<String, String> param = CommonParamUtil.getDefaultParam();
        param.put("uid", String.valueOf(CoreManager.getCore(IAuthCore.class).getCurrentUid()));
        param.put("diamondNum", diamondNum);
        param.put("smsCode", sms);
        param.put("ticket", CoreManager.getCore(IAuthCore.class).getTicket());
        OkHttpManager.getInstance().doPostRequest(UriProvider.changeGold(), param, new OkHttpManager.MyCallBack<ServiceResult<ExchangeAwardInfo>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null)
                    getMvpView().exchangeGoldFail(-1, e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<ExchangeAwardInfo> data) {
                if (null != data && data.isSuccess()) {
                    walletInfo = data.getData();
                    if (getMvpView() != null) {
                        getMvpView().exchangeGold(data.getData());
                        getMvpView().setupUserWalletBalance(data.getData());
                        getMvpView().showAward(data.getData());
                    }
                    CoreManager.getCore(IPayCore.class).setCurrentWalletInfo(data.getData());
                } else {
                    getMvpView().exchangeGoldFail(data.getCode(), data.getErrorMessage());
                }
            }
        });
    }

    private boolean isTenMultiple(long number) {
        long value = number % 10;
        if (value == 0) {
            return true;
        }
        return false;
    }

    private boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
