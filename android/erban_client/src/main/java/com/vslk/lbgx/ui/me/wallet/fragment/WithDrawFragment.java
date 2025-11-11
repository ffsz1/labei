package com.vslk.lbgx.ui.me.wallet.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vslk.lbgx.base.fragment.BaseMvpFragment;
import com.vslk.lbgx.ui.me.bills.activity.WithdrawBillsActivity;
import com.vslk.lbgx.ui.me.wallet.activity.BinderPhoneActivity;
import com.vslk.lbgx.ui.me.wallet.presenter.IncomePresenter;
import com.vslk.lbgx.ui.me.wallet.view.IIncomeView;
import com.vslk.lbgx.ui.me.withdraw.WithdrawFragment;
import com.hncxco.library_ui.widget.DrawableTextView;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.pay.bean.WalletInfo;
import com.tongdaxing.xchat_core.withdraw.IWithdrawCoreClient;
import com.tongdaxing.xchat_core.withdraw.bean.ExchangerInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;

import java.util.Locale;

import butterknife.BindView;

/**
 * 创建者      Created by dell
 * 创建时间    2018/12/21
 * 描述
 * <p>
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
@CreatePresenter(IncomePresenter.class)
public class WithDrawFragment extends BaseMvpFragment<IIncomeView, IncomePresenter> implements IIncomeView, View.OnClickListener {

    @BindView(R.id.diamond_menu)
    ImageView diamondMenu;
    @BindView(R.id.withdrawGold)
    DrawableTextView withDrawGold;
    @BindView(R.id.withdraw)
    DrawableTextView withDraw;
    @BindView(R.id.tv_diamond)
    TextView tvDiamond;

    private boolean isRequest;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_with_draw;
    }

    @Override
    public void onFindViews() {

    }

    @Override
    public void initiate() {
        getMvpPresenter().loadWalletInfo();
    }

    @Override
    public void onSetListener() {
        diamondMenu.setOnClickListener(this);
        withDrawGold.setOnClickListener(this);
        withDraw.setOnClickListener(this);
    }

    @Override
    public void setupUserWalletBalance(WalletInfo walletInfo) {
        if (walletInfo != null) {
            tvDiamond.setText(String.format(Locale.getDefault(), "%.2f", walletInfo.diamondNum));
        }
    }

    @CoreEvent(coreClientClass = IWithdrawCoreClient.class)
    public void onRequestExchange(ExchangerInfo exchangerInfo) {
        if (exchangerInfo != null) {
            tvDiamond.setText(String.format(Locale.getDefault(), "%.2f", exchangerInfo.diamondNum));
        }
    }

    @Override
    public void getUserWalletInfoFail(String error) {
        tvDiamond.setText("0.00");
        toast(error);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.diamond_menu:
                WithdrawBillsActivity.start(getActivity());
                break;
            case R.id.withdrawGold:
//                ExchangeGoldFragment.start(getActivity());
                break;
            case R.id.withdraw:
                isRequest = true;
                getMvpPresenter().hasBindPhone();
                break;
            default:
                break;
        }
    }

    @Override
    public void hasBindPhone() {
        if (!isRequest) {
            return;
        }
        isRequest = false;
        startActivity(new Intent(getActivity(), WithdrawFragment.class));
    }

    @Override
    public void hasBindPhoneFail(String error) {
        if (!isRequest) {
            return;
        }
        startActivity(new Intent(getActivity(), BinderPhoneActivity.class));
    }

    public static Fragment newInstance() {
        return new WithDrawFragment();
    }

}
