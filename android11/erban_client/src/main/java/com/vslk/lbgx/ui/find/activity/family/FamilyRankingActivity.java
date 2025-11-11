package com.vslk.lbgx.ui.find.activity.family;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hncxco.library_ui.widget.AppToolBar;
import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.ui.find.fragment.FamilyListFragment;
import com.tongdaxing.erban.R;

/**
 * 创建者      Created by dell
 * 创建时间    2018/11/21
 * 描述        家族排行
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class FamilyRankingActivity extends BaseActivity {

    private AppToolBar mToolBar;

    public static void start(Context context) {
        Intent intent = new Intent(context, FamilyRankingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_ranking);
        onFindViews();
        initiate();
    }

    private void onFindViews() {
        mToolBar = (AppToolBar) findViewById(R.id.toolbar);
        mToolBar.setOnBackBtnListener(view -> finish());
    }

    private void initiate() {
        getSupportFragmentManager().beginTransaction().add(R.id.container, FamilyListFragment.newInstance(),
                FamilyListFragment.class.getSimpleName()).commitAllowingStateLoss();
    }
}
