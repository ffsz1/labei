package com.vslk.lbgx.ui.message.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hncxco.library_ui.widget.AppToolBar;
import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.ui.message.fragment.FriendBlackFragment;
import com.tongdaxing.erban.R;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/22
 * 描述        黑名单列表
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class BlackFriendActivity extends BaseActivity {

    private AppToolBar mToolBar;

    public static void start(Context context) {
        Intent intent = new Intent(context, BlackFriendActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_friend);

        mToolBar = (AppToolBar) findViewById(R.id.toolbar);
        mToolBar.setOnBackBtnListener(view -> finish());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, FriendBlackFragment.newInstance(),
                        FriendBlackFragment.class.getSimpleName()).commitAllowingStateLoss();
    }
}
