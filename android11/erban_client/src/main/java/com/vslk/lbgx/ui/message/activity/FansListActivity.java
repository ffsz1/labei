package com.vslk.lbgx.ui.message.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hncxco.library_ui.widget.AppToolBar;
import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.ui.message.fragment.FansListFragment;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.Constants;

/**
 * 粉丝列表
 *
 * @author dell
 */
public class FansListActivity extends BaseActivity {

    private AppToolBar mToolBar;

    public static void start(Context context) {
        Intent intent = new Intent(context, FansListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans);
        mToolBar = findView(R.id.toolbar);
        mToolBar.setOnBackBtnListener(view -> finish());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, FansListFragment.newInstance(Constants.FAN_NO_MAIN_PAGE_TYPE),
                        FansListFragment.class.getSimpleName()).commitAllowingStateLoss();


    }
}
