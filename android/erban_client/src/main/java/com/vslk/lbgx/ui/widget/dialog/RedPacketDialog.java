package com.vslk.lbgx.ui.widget.dialog;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.utils.UIHelper;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.redpacket.bean.RedPacketInfoV2;

/**
 * @author chenran
 * @date 2017/10/4
 */

public class RedPacketDialog extends BaseActivity implements View.OnClickListener {
    private ImageView imgClose;
    private ImageView checkDetail;
    private TextView redMoney;
    private RelativeLayout checkRedPacket;
    private RedPacketInfoV2 redPacketInfo;

    private ObjectAnimator mObjectAnimator;

    public static void start(Context context, RedPacketInfoV2 redPacketInfoV2) {
        Intent intent = new Intent(context, RedPacketDialog.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("redPacketInfo", redPacketInfoV2);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.red_packet_dialog);
        redPacketInfo = (RedPacketInfoV2) getIntent().getSerializableExtra("redPacketInfo");
        initView();
        setListener();
    }

    private void setListener() {
        imgClose.setOnClickListener(this);
        checkDetail.setOnClickListener(this);
    }

    private void initView() {
        checkRedPacket = (RelativeLayout) findViewById(R.id.check_red_packet);
        imgClose = (ImageView) findViewById(R.id.img_close);
        redMoney = (TextView) findViewById(R.id.tv_red_money);
        checkDetail = (ImageView) findViewById(R.id.tv_look);
        redMoney.setText(String.valueOf(redPacketInfo.getPacketNum()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.img_open:
//                startRedPacketAnim(v);
//                break;
            case R.id.img_close:
                finish();
                break;
            case R.id.tv_look:
                UIHelper.showWalletAct(this);
                finish();
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mObjectAnimator != null) {
            if (mObjectAnimator.isRunning()) {
                mObjectAnimator.cancel();
            }
            mObjectAnimator = null;
        }
    }
}
