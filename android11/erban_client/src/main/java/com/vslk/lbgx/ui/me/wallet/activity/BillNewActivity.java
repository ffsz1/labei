package com.vslk.lbgx.ui.me.wallet.activity;

import android.content.Intent;
import android.os.Bundle;

import com.hncxco.library_ui.widget.AppToolBar;
import com.tongdaxing.erban.R;
import com.vslk.lbgx.base.activity.BaseActivity;

/**
 * 新账单
 */
public class BillNewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bill);
        ((AppToolBar) findViewById(R.id.toolbar)).getIvLeft().setOnClickListener(v -> finish());
        findViewById(R.id.ll_sl).setOnClickListener(v -> startBillList(1));
        findViewById(R.id.ll_songl).setOnClickListener(v -> startBillList(2));
        findViewById(R.id.ll_zongzhi).setOnClickListener(v -> startBillList(3));
        findViewById(R.id.ll_tx).setOnClickListener(v -> startBillList(4));
    }

    private void startBillList(int type) {
        Intent intent = new Intent();
        intent.setClass(BillNewActivity.this, BillListActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

}
