package com.vslk.lbgx.ui.me.wallet.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.tongdaxing.erban.R;
import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.ui.me.bills.fragment.BillBaseFragment;
import com.vslk.lbgx.ui.me.bills.fragment.BillChargeFragment;
import com.vslk.lbgx.ui.me.bills.fragment.BillExpenseFragment;
import com.vslk.lbgx.ui.me.bills.fragment.BillIncomeFragment;
import com.vslk.lbgx.ui.me.bills.fragment.BillRedFragment;
import com.vslk.lbgx.ui.me.bills.fragment.WithdrawBillsFragment;

public class BillListActivity extends BaseActivity implements IBillNewListener {

    private TextView tv_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_list);
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
        TextView title = (TextView) findViewById(R.id.tv_rule);
        tv_date = (TextView) findViewById(R.id.tv_date);
        int type = getIntent().getIntExtra("type", 0);
        String str;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        BillBaseFragment mCententFragment;
        switch (type) {
            case 2:
                str = "送礼记录";
                mCententFragment = new BillExpenseFragment();
                break;
            case 3:
                str = "开心收入";
                mCententFragment = new BillChargeFragment();
                break;
            case 4:
                str = "主播收益分成兑换记录";
                mCententFragment = new WithdrawBillsFragment();
                break;
            case 5:
                str = "红包提现";
                mCententFragment = new BillRedFragment();
                break;
            default:
                str = "收礼记录";
                mCententFragment = new BillIncomeFragment();
                break;
        }
        title.setText(str);
        mCententFragment.setBillNewListener(this::setDate);
        ft.replace(R.id.fl_content, mCententFragment);
        ft.commit();
        findViewById(R.id.fl_date).setOnClickListener(v -> mCententFragment.showDate());
    }

    @Override
    public void setDate(String date) {
//        tv_date.setText(date);
    }
}
