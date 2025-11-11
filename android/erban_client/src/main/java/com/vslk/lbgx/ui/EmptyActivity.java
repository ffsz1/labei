package com.vslk.lbgx.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.ui.launch.presenter.EmptyPresenter;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.Constants;

/**
 * 空的activity，做跳转
 */
public class EmptyActivity extends BaseMvpActivity<IMvpBaseView, EmptyPresenter> implements IMvpBaseView {

    public static void startAvRoom(Context context, long roomUid) {
        Intent intent = new Intent(context, EmptyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.TYPE, 1);
        intent.putExtra(Constants.ROOM_UID, roomUid);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_transparent);
        View contentView = findViewById(R.id.content_layout);
        contentView.setOnTouchListener((v, event) -> {
            finish();
            return false;
        });
        Intent intent = getIntent();
        int type = intent.getIntExtra(Constants.TYPE, 0);
        //进入直播间
        if (type == 1) {
            AVRoomActivity.start(this, getIntent().getLongExtra(Constants.ROOM_UID, 0));
        }
    }

    @Override
    protected boolean needSteepStateBar() {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}
