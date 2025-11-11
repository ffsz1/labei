package com.vslk.lbgx.ui.me.wallet.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pingplusplus.android.Pingpp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.WebUrl;
import com.tongdaxing.xchat_core.count.IChargeClient;
import com.tongdaxing.xchat_core.pay.IPayCoreClient;
import com.tongdaxing.xchat_core.pay.bean.ChargeBean;
import com.tongdaxing.xchat_core.pay.bean.WalletInfo;
import com.tongdaxing.xchat_core.user.VersionsCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.Json;
import com.tongdaxing.xchat_framework.util.util.LogUtil;
import com.tongdaxing.xchat_framework.util.util.SingleToastUtil;
import com.tongdaxing.xchat_framework.util.util.log.MLog;
import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.vslk.lbgx.room.egg.call.CallCoreImpl;
import com.vslk.lbgx.room.egg.call.ICallCore;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.me.wallet.adapter.ChargeAdapter;
import com.vslk.lbgx.ui.me.wallet.presenter.ChargePresenter;
import com.vslk.lbgx.ui.me.wallet.view.IChargeView;
import com.vslk.lbgx.ui.web.PayWebViewActivity;

import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 新皮：充值页面
 *
 * @author zwk 2018/5/30
 */
@CreatePresenter(ChargePresenter.class)
public class ChargeFragment extends BaseMvpFragment<IChargeView, ChargePresenter> implements IChargeView, View.OnClickListener {

    @BindView(R.id.tv_gold)
    TextView mTv_gold;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindString(R.string.charge)
    String activityTitle;
    @BindView(R.id.tv_charge)
    TextView btnCharge;

    private ChargeAdapter mChargeAdapter;
    private ChargeBean mSelectChargeBean;
    private ICallCore iCallCore;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_charge;
    }

    @Override
    public void onSetListener() {
        btnCharge.setOnClickListener(this);
    }

    @Override
    public void initiate() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3, OrientationHelper.VERTICAL, false));
        mChargeAdapter = new ChargeAdapter();
        mRecyclerView.setAdapter(mChargeAdapter);
        mChargeAdapter.setOnItemClickListener((baseQuickAdapter, view, position) -> {
            List<ChargeBean> list = mChargeAdapter.getData();
            if (ListUtils.isListEmpty(list)) {
                return;
            }
            mSelectChargeBean = list.get(position);
            int size = list.size();
            for (int i = 0; i < size; i++) {
                list.get(i).isSelected = position == i;
            }
            mChargeAdapter.notifyDataSetChanged();
        });
        getMvpPresenter().getChargeList();
        iCallCore = CallCoreImpl.getInstance().getICallCore();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getMvpPresenter().refreshWalletInfo(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_charge:
//                getMvpPresenter().showChargeOptionsDialog();
                PayWebViewActivity.start(getActivity(), WebUrl.CHARGE_URL);
//                callKefu();
                break;
            default:
                break;
        }
    }
    private void callKefu() {
        DialogManager mDialogManager = new DialogManager(getActivity());
        mDialogManager.setCanceledOnClickOutside(false);
        mDialogManager.showOkCancelDialog("充值功能正在开发中，请先移步微信公众号充值：\n" + getString(R.string.txt_common_num), "复制", "取消", new DialogManager.AbsOkDialogListener() {
            @Override
            public void onOk() {
                ClipboardManager mClipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("复制成功!", getString(R.string.txt_common_num));
                mClipboardManager.setPrimaryClip(clipData);
                toast("复制成功!");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (iCallCore != null)
            iCallCore.getGold((wallet -> mTv_gold.setText(String.valueOf(wallet.getGoldNum()))));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (iCallCore != null) iCallCore = null;
    }

    @Override
    public void setupUserInfo(UserInfo userInfo) {

    }

    @Override
    public void setupUserWalletBalance(WalletInfo walletInfo) {
        getActivity().runOnUiThread(() -> {
            mTv_gold.setText(getString(R.string.charge_gold, walletInfo.getGoldNum()));
        });
    }


    @Override
    public void getUserWalletInfoFail(String error) {
        toast(error);
    }

    @Override
    public void buildChargeList(List<ChargeBean> chargeBeanList) {
        if (chargeBeanList != null && chargeBeanList.size() > 0) {
            for (int i = 0; i < chargeBeanList.size(); i++) {
                ChargeBean chargeBean = chargeBeanList.get(i);
                chargeBean.isSelected = chargeBean.getMoney() == 48;
                if (48 == chargeBean.getMoney()) {
                    mSelectChargeBean = chargeBean;
                }
            }
            mChargeAdapter.setNewData(chargeBeanList);
        }
    }

    @Override
    public void getChargeListFail(String error) {
        toast(error);
    }

    private int aliPayChannel = 1;//1支付宝ping++支付渠道,2是汇潮支付渠道
    private int wxPayChannel = 1;//1微信ping++支付渠道,2是汇聚支付渠道
    private int payType = 1;//1是支付宝，2是微信
    private int payMoney = 488;//默认488人民币

    @Override
    public void displayChargeOptionsDialog() {
        if (mSelectChargeBean == null || mContext == null) {
            return;
        }
        ButtonItem buttonItem = new ButtonItem(getString(R.string.charge_alipay), () -> {
            payType = 1;
            Json json = CoreManager.getCore(VersionsCore.class).getConfigData();
            aliPayChannel = json.num("aliSwitch");
            if (aliPayChannel == 2) {
                //汇潮支付
                getMvpPresenter().requestHuiJuPay(mContext, String.valueOf(mSelectChargeBean.chargeProdId));
            } else {
                getMvpPresenter().requestCharge(mContext, String.valueOf(mSelectChargeBean.chargeProdId), Constants.CHARGE_ALIPAY);
            }
        });
        ButtonItem buttonItem1 = new ButtonItem(getString(R.string.charge_webchat), () -> {
            if (!ShareSDK.getPlatform(Wechat.NAME).isClientValid()) {
                SingleToastUtil.showToast("未安装微信APP");
                return;
            }
            payType = 2;
            Json json = CoreManager.getCore(VersionsCore.class).getConfigData();
            payMoney = json.num("payMoney");
            wxPayChannel = json.num("payChannel");
            if (wxPayChannel == 2 && mSelectChargeBean.getMoney() >= payMoney) {
                //汇聚支付
                getMvpPresenter().getJoinPayData(String.valueOf(mSelectChargeBean.chargeProdId), Constants.CHARGE_WX_JOINPAY);
            } else if (wxPayChannel == 1 || wxPayChannel == 2) {
                getMvpPresenter().requestCharge(mContext, String.valueOf(mSelectChargeBean.chargeProdId), Constants.CHARGE_WX);
            } else if (wxPayChannel == 3) {
                //汇聚app+支付
                getMvpPresenter().getJoinPayData(String.valueOf(mSelectChargeBean.chargeProdId), Constants.CHARGE_WX_JOINPAY3);
            } else {
                toast("支付异常!");
            }
        });

        List<ButtonItem> buttonItems = new ArrayList<>();
        if (CoreManager.getCore(VersionsCore.class).canUseAliPay()) {
            buttonItems.add(buttonItem);
        }
        buttonItems.add(buttonItem1);
        ((BaseActivity) mContext).getDialogManager().showCommonPopupDialog(buttonItems, getString(R.string.cancel), false);
    }

    private void huiChaoPayment(String data) {
        try {
            String payUrl = new Json(data).str("payUrl");
            LogUtil.e(TAG, payUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("alipays://platformapi/startApp?appId=10000011&url=" + URLEncoder.encode(payUrl, "UTF-8")));
            startActivity(intent);
        } catch (Exception e) {
            toast("发起充值失败，请联系客服人员");
        }
    }

    @Override
    public void getChargeOrOrderInfo(String data) {
        if (data != null) {
            if (payType == 1) {
                if (aliPayChannel == 2) {
                    huiChaoPayment(data);
                } else {
                    //Ping+支付
                    Pingpp.createPayment(this, data);
                }
            } else if (payType == 2) {
                if (wxPayChannel == 2 && mSelectChargeBean.getMoney() >= payMoney) {
                    //汇聚支付
                    joinPay(data);
                } else if (wxPayChannel == 3) {
                    //汇聚app+支付
                    joinPayAPP3(data);
                }else if (wxPayChannel == 1 || wxPayChannel == 2) {
                    //Ping+支付
                    Pingpp.createPayment(this, data);
                } else {
                    toast("发起充值失败，请联系客服人员!");
                }
            } else {
                toast("发起充值失败，请联系客服人员!");
            }
        }
    }

    //启动汇聚支付
    private void joinPay(String data) {
        try {
            Json json = new Json(data);
            String appId = json.getString("appId");
            String prepayId = json.getString("prepayId");
            String partnerId = json.getString("partnerId");
            String nonceStr = json.getString("nonceStr");
            String timeStamp = json.getString("timeStamp");
            String sign = json.getString("hmac");
            PayReq request = new PayReq();
            request.appId = appId;
            request.partnerId = partnerId;
            request.prepayId = prepayId;
            request.packageValue = "Sign=WXPay";
            request.nonceStr = nonceStr;
            request.timeStamp = timeStamp;
            request.sign = sign;

            IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId, false);
            api.registerApp(appId);
            api.sendReq(request);
        } catch (Exception e) {
            e.printStackTrace();
            toast("拉起微信支付失败");
        }
    }

    //启动汇聚app+支付
    private void joinPayAPP3(String data) {
        try {
            Json json = new Json(data);
            WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
            req.path = "pages/payIndex/payIndex?rc_result=" + data;
            req.userName = json.getString("original_id");

            IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), Constants.WX_APPID, false);
            // 可选打开 开发版，体验版和正式版
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
            api.registerApp(Constants.WX_APPID);
            api.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
            toast("拉起微信支付失败");
        }
    }


    @Override
    public void getChargeOrOrderInfoFail(String error) {
        toast("发起充值失败" + error);
    }

    @CoreEvent(coreClientClass = IPayCoreClient.class)
    public void onWalletInfoUpdate(WalletInfo walletInfo) {
        getActivity().runOnUiThread(() -> {
            if (walletInfo != null) {
                mTv_gold.setText(getString(R.string.charge_gold, walletInfo.getGoldNum()));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult: ");
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (data != null && data.getExtras() != null) {
                String result = data.getExtras().getString("pay_result");
                if (result != null) {

                    /* 处理返回值
                     * "success" - 支付成功
                     * "fail"    - 支付失败
                     * "cancel"  - 取消支付
                     * "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
                     * "unknown" - app进程异常被杀死(一般是低内存状态下,app进程被杀死)
                     */
                    // 错误信息
                    String errorMsg = data.getExtras().getString("error_msg");
                    String extraMsg = data.getExtras().getString("extra_msg");
                    MLog.error(TAG, "errorMsg:" + errorMsg + "extraMsg:" + extraMsg);
                    CoreManager.notifyClients(IChargeClient.class, IChargeClient.chargeAction, result);
                    if ("success".equals(result)) {
                        handler.sendMessageDelayed(handler.obtainMessage(), 1000);
                        toast("支付成功！");
                    } else if ("cancel".equals(result)) {
                        toast("支付被取消！");
                    } else {
                        toast("支付失败！");
                    }
                }
            }
        }
    }

    private WalletInfoHandler handler = new WalletInfoHandler(this);

    private static class WalletInfoHandler extends Handler {

        private WeakReference<ChargeFragment> mReference;

        WalletInfoHandler(ChargeFragment fragment) {
            mReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mReference == null || mReference.get() == null) {
                return;
            }
            mReference.get().getMvpPresenter().refreshWalletInfo(true);
        }
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    public static Fragment newInstance() {
        return new ChargeFragment();
    }
}
