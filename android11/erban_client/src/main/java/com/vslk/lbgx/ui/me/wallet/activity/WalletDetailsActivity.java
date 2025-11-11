package com.vslk.lbgx.ui.me.wallet.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hncxco.library_ui.widget.AppToolBar;
import com.tongdaxing.erban.R;
import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.ui.me.wallet.fragment.ChargeFragment;
import com.vslk.lbgx.ui.me.withdraw.WithdrawFragment;
import com.vslk.lbgx.ui.message.fragment.FriendBlackFragment;

public class WalletDetailsActivity extends BaseActivity {
    public static void start(Context context, int type) {
        Intent intent = new Intent(context, WalletDetailsActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square);
        AppToolBar appToolBar = (AppToolBar) findViewById(R.id.toolbar);
        int type = getIntent().getIntExtra("type", 0);
        String simpleName, title;
        Fragment fragment = null;
        switch (type) {
            case 1:
                simpleName = FriendBlackFragment.class.getSimpleName();
                title = "黑名单";
                fragment = new FriendBlackFragment();
                break;
            case 2:
                simpleName = WithdrawFragment.class.getSimpleName();
                title = "兑换";
                fragment = new WithdrawFragment();
                break;
            default:
                simpleName = ChargeFragment.class.getSimpleName();
                title = "充值";
                fragment = ChargeFragment.newInstance();
                break;
        }
        appToolBar.setTitle(title);
        appToolBar.setOnBackBtnListener(view -> finish());
        //替换页面
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, simpleName).commitAllowingStateLoss();
    }
}
