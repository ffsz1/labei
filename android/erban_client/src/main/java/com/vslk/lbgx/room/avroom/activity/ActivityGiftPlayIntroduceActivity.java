package com.vslk.lbgx.room.avroom.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hncxco.library_ui.widget.AppToolBar;
import com.tongdaxing.erban.R;
import com.vslk.lbgx.base.activity.BaseActivity;

/**
 * author : yinxiuli
 * date   : 2020/8/19 11:23
 * desc   : 活动礼物玩法介绍
 */
public class ActivityGiftPlayIntroduceActivity extends BaseActivity implements View.OnClickListener {

    private AppToolBar mToolBar;

    public static void start(Context context) {
        Intent intent = new Intent(context, ActivityGiftPlayIntroduceActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_play_introduce);
        initView();
        setListener();
    }

    private void initView() {
        mToolBar = (AppToolBar) findViewById(R.id.toolbar);
    }

    private void setListener() {
        back(mToolBar);
    }
}
