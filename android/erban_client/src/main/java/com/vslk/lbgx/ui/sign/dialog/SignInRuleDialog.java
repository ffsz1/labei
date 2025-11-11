package com.vslk.lbgx.ui.sign.dialog;

import android.view.ViewGroup;

import com.hncxco.library_ui.widget.ViewHolder;
import com.hncxco.library_ui.widget.dialog.BaseDialog;
import com.tongdaxing.erban.R;

/**
 * Function:
 * Author: Edward on 2019/5/24
 */
public class SignInRuleDialog extends BaseDialog {
    @Override
    public void convertView(ViewHolder viewHolder) {

    }

    @Override
    protected int getDialogHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
        //        return ConvertUtils.dp2px(getContext(), 325);
    }

    @Override
    public int getContentView() {
        return R.layout.dialog_sign_in_rule;
    }
}
