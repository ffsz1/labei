package com.vslk.lbgx.ui.widget.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.ui.web.CommonWebViewActivity;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.activity.bean.LotteryInfo;

/**
 * Created by chenran on 2017/12/27.
 */

public class LotteryDialog extends BaseActivity implements View.OnClickListener{
    private LotteryInfo lotteryInfo;
    private ImageView closeImg;
    private ImageView goRightNowImg;

    public static void start(Context context, LotteryInfo lotteryInfo) {
        Intent intent = new Intent(context, LotteryDialog.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("lotteryInfo", lotteryInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottery_dialog);
        lotteryInfo = (LotteryInfo) getIntent().getSerializableExtra("lotteryInfo");
        initView();
    }

    private void initView() {
        closeImg = (ImageView) findViewById(R.id.img_close);
        goRightNowImg = (ImageView) findViewById(R.id.go_right_now);
        closeImg.setOnClickListener(this);
        goRightNowImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.go_right_now:
                Intent intent = new Intent(this, CommonWebViewActivity.class);
                intent.putExtra("url", UriProvider.getLotteryActivityPage());
                startActivity(intent);
                finish();
                break;
        }
    }
}
