package com.vslk.lbgx.room.avroom.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.hncxco.library_ui.widget.AppToolBar;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.presenter.RoomSettingPresenter;
import com.tongdaxing.xchat_core.room.view.IRoomSettingView;

/**
 * @author dell
 */
@CreatePresenter(RoomSettingPresenter.class)
public class RoomPlayTipActivity extends BaseMvpActivity<IRoomSettingView, RoomSettingPresenter> implements IRoomSettingView {

    private EditText etPlay;
    private AppToolBar mAppToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_play_tip);
        initView();
    }

    private void initView() {
        etPlay = (EditText) findViewById(R.id.et_room_play);
        String roomRule = AvRoomDataManager.get().getRoomRule();
        if (!TextUtils.isEmpty(roomRule)) {
            etPlay.setText(roomRule);
        }
        mAppToolBar = (AppToolBar) findViewById(R.id.toolbar);
        mAppToolBar.setOnBackBtnListener(view -> finish());
        mAppToolBar.setOnRightTitleClickListener(this::save);
    }

    private void save() {
        String playInfo = etPlay.getText().toString();
        if (TextUtils.isEmpty(playInfo)) {
            toast("内容不能为空");
            return;
        }
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) {
            return;
        }
        String url;
        if (AvRoomDataManager.get().isRoomOwner()) {
            url = UriProvider.updateRoomInfo();
        } else if (AvRoomDataManager.get().isRoomAdmin()) {
            url = UriProvider.updateByAdimin();
        } else {
            return;
        }
        getDialogManager().showProgressDialog(this);
        getMvpPresenter().saveRoomPlayTip(playInfo, roomInfo, url);
    }

    @Override
    public void onSaveRoomPlayTipSuccessView() {
        if (getDialogManager() != null) {
            getDialogManager().dismissDialog();
        }
        toast("保存成功");
        finish();
    }

    @Override
    public void onSaveRoomPlayTipFailView(String msg) {
        if (getDialogManager() != null) {
            getDialogManager().dismissDialog();
        }
        toast(msg);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, RoomPlayTipActivity.class);
        context.startActivity(intent);
    }
}
