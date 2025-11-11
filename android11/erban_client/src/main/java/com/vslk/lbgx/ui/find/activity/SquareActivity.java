package com.vslk.lbgx.ui.find.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hncxco.library_ui.widget.AppToolBar;
import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.ui.find.fragment.SquareFragment;
import com.tongdaxing.erban.R;

/**
 * 创建者      Created by dell
 * 创建时间    2018/12/7
 * 描述        广场界面
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class SquareActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, SquareActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square);
        AppToolBar appToolBar = (AppToolBar) findViewById(R.id.toolbar);
        appToolBar.setOnBackBtnListener(view -> finish());

        //替换页面
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                SquareFragment.newInstance(), SquareFragment.class.getSimpleName()).commitAllowingStateLoss();
    }
}
