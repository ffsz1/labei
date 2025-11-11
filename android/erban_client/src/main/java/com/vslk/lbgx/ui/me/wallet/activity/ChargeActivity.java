package com.vslk.lbgx.ui.me.wallet.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.ui.me.wallet.adapter.ChargeAdapter;
import com.vslk.lbgx.ui.me.wallet.presenter.ChargePresenter;
import com.vslk.lbgx.ui.me.wallet.view.IChargeView;
import com.pingplusplus.android.Pingpp;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.erban.libcommon.widget.ButtonItem;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.count.IChargeClient;
import com.tongdaxing.xchat_core.pay.IPayCoreClient;
import com.tongdaxing.xchat_core.pay.bean.ChargeBean;
import com.tongdaxing.xchat_core.pay.bean.WalletInfo;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.log.MLog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/23
 * 描述        充值界面
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
@Deprecated
@CreatePresenter(ChargePresenter.class)
public class ChargeActivity extends BaseMvpActivity<IChargeView, ChargePresenter> implements IChargeView, View.OnClickListener {

    private static final String TAG = ChargeActivity.class.getSimpleName();

    public static void start(Context context) {
        Intent intent = new Intent(context, ChargeActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.tv_gold)
    TextView mTv_gold;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_charge)
    TextView btnCharge;
    private ChargeAdapter mChargeAdapter;
    private ChargeBean mSelectChargeBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        ButterKnife.bind(this);
        initiate();
        setListener();
    }

    private void initiate() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3, OrientationHelper.VERTICAL, false));
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
        getMvpPresenter().refreshWalletInfo(true);
    }

    private void setListener() {
        btnCharge.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_charge:
                getMvpPresenter().showChargeOptionsDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public void setupUserInfo(UserInfo userInfo) {

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

    @Override
    public void displayChargeOptionsDialog() {
        if (mSelectChargeBean == null) {
            return;
        }
        ButtonItem buttonItem = new ButtonItem(getString(R.string.charge_alipay), () -> getMvpPresenter()
                .requestCharge(this, String.valueOf(mSelectChargeBean.chargeProdId), Constants.CHARGE_ALIPAY));
        ButtonItem buttonItem1 = new ButtonItem(getString(R.string.charge_webchat), () -> getMvpPresenter()
                .requestCharge(this, String.valueOf(mSelectChargeBean.chargeProdId), Constants.CHARGE_WX));
        List<ButtonItem> buttonItems = new ArrayList<>();
        buttonItems.add(buttonItem);
        buttonItems.add(buttonItem1);
        getDialogManager().showCommonPopupDialog(buttonItems, getString(R.string.cancel), false);
    }

    @Override
    public void getChargeOrOrderInfo(String data) {
        if (data != null) {
            Pingpp.createPayment(this, data);
        }
    }

    @Override
    public void getChargeOrOrderInfoFail(String error) {
        toast("发起充值失败" + error);
    }

    @Override
    public void setupUserWalletBalance(WalletInfo walletInfo) {
        mTv_gold.setText(String.format(Locale.getDefault(), "%.1f", walletInfo.goldNum));
    }

    @Override
    public void getUserWalletInfoFail(String error) {
        toast(error);
    }

    @CoreEvent(coreClientClass = IPayCoreClient.class)
    public void onWalletInfoUpdate(WalletInfo walletInfo) {
        if (walletInfo != null) {
            mTv_gold.setText(getString(R.string.charge_gold, walletInfo.getGoldNum()));
        }
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
                        //间隔1200毫秒请求数据
                        handler.sendMessageDelayed(handler.obtainMessage(), 1200);
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

        private WeakReference<ChargeActivity> mReference;

        WalletInfoHandler(ChargeActivity activity) {
            mReference = new WeakReference<>(activity);
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
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
