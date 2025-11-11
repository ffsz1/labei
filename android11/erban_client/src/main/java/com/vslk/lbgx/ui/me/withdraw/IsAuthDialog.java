package com.vslk.lbgx.ui.me.withdraw;

import android.view.View;

import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.WebUrl;
import com.vslk.lbgx.ui.dialog.BaseDialog;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;

public class IsAuthDialog extends BaseDialog {
    @Override
    protected int layout() {
        return R.layout.dialog_is_auth;
    }

    @Override
    protected int setAnim() {
        return 0;
    }

    @Override
    protected int close() {
        return R.id.cancel;
    }

    @Override
    protected void bindView(View view) {
        view.findViewById(R.id.ok).setOnClickListener(v -> {
            CommonWebViewActivity.start(getContext(), WebUrl.VERIFIED_REAL_NAME);
            dismiss();
        });
    }
}
