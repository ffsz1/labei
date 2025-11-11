package com.vslk.lbgx.ui.me.wallet.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hncxco.library_ui.widget.AppToolBar;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.WebUrl;
import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.room.egg.call.CallCoreImpl;
import com.vslk.lbgx.room.egg.call.ICallCore;
import com.vslk.lbgx.ui.common.widget.dialog.DialogManager;
import com.vslk.lbgx.ui.me.wallet.fragment.ChargeFragment;
import com.vslk.lbgx.ui.me.wallet.fragment.WithDrawFragment;
import com.vslk.lbgx.ui.me.wallet.presenter.IncomePresenter;
import com.vslk.lbgx.ui.me.withdraw.DiamondsActivity;
import com.vslk.lbgx.ui.web.PayWebViewActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新皮：我的钱包页面
 *
 * @author zwk 2018/5/30
 */
@CreatePresenter(IncomePresenter.class)
public class WalletActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    AppToolBar mToolBar;
    @BindView(R.id.tv_gold)
    TextView tvGold;
    @BindView(R.id.rl_gold)
    View rlGold;

    @BindView(R.id.tv_jewel)
    TextView tvJewel;
    @BindView(R.id.rl_jewel)
    View rlJewel;
    private ICallCore iCallCore;

    public static void start(Context context) {
        Intent intent = new Intent(context, WalletActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        ButterKnife.bind(this);
        mToolBar.setOnBackBtnListener(view -> finish());
        mToolBar.setOnRightTitleClickListener(() -> {
            //账单
            startActivity(new Intent(WalletActivity.this, BillNewActivity.class));
        });
        rlGold.setOnClickListener(v -> {
            PayWebViewActivity.start(WalletActivity.this, WebUrl.CHARGE_URL);
//            WalletDetailsActivity.start(this, 0);
        });
        rlJewel.setOnClickListener(v -> {
            Intent intent = new Intent(WalletActivity.this, DiamondsActivity.class);
            startActivity(intent);
        });
        iCallCore = CallCoreImpl.getInstance().getICallCore();
    }

    private void callKefu() {
        getDialogManager().showOkCancelDialog("充值功能正在开发中，请先移步微信公众号充值：\n" + getString(R.string.txt_common_num), "复制", "取消", new DialogManager.AbsOkDialogListener() {
            @Override
            public void onOk() {
                ClipboardManager mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("复制成功!", getString(R.string.txt_common_num));
                mClipboardManager.setPrimaryClip(clipData);
                toast("复制成功!");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (iCallCore != null)
            iCallCore.getGold((wallet -> {
                tvGold.setText(String.valueOf(wallet.getGoldNum()));
                tvJewel.setText(String.format(Locale.getDefault(), "%.2f", wallet.diamondNum));
            }));
    }

    private List<Fragment> getFragmentsList() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ChargeFragment.newInstance());
        fragments.add(WithDrawFragment.newInstance());
        return fragments;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iCallCore != null) iCallCore = null;
    }
}

