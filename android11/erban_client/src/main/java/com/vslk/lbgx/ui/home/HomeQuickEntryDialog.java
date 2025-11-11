package com.vslk.lbgx.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;

import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

/**
 * Function:首页快捷入口
 * Author: Edward on 2019/7/10
 */
public class HomeQuickEntryDialog extends BottomSheetDialog {
    public HomeQuickEntryDialog(@NonNull Context context) {
        super(context, R.style.GiftBottomSheetDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoreManager.addClient(this);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_home_quick_entry);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CoreManager.removeClient(this);
    }

}
