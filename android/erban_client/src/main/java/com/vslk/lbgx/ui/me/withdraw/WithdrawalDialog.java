package com.vslk.lbgx.ui.me.withdraw;

import android.view.View;
import android.widget.TextView;

import com.tongdaxing.erban.R;
import com.vslk.lbgx.ui.dialog.BaseDialog;

import butterknife.BindView;

public class WithdrawalDialog extends BaseDialog {
    @BindView(R.id.title)
    TextView title;

    @Override
    protected int layout() {
        return R.layout.dialog_withdraw;
    }

    @Override
    protected int setAnim() {
        return 0;
    }

    @Override
    protected int close() {
        return R.id.ok;
    }

    @Override
    protected void bindView(View view) {
        title.setText("您兑换的主播收益分成会在3个工作日内到账，请关注您的账户余额变动。");
    }
}
