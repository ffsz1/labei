package com.vslk.lbgx.room.egg;

import android.view.View;

import com.tongdaxing.erban.R;
import com.vslk.lbgx.ui.dialog.BaseDialog;

public class CallRuleDialog extends BaseDialog {

    @Override
    protected int layout() {
        return R.layout.dialog_call_rule;
    }

    @Override
    protected int setAnim() {
        return 1;
    }

    @Override
    protected int close() {
        return R.id.iv_close;
    }

    @Override
    protected void bindView(View view) {
        view.findViewById(R.id.fl_layout).setOnClickListener(v -> dismiss());
    }
}
