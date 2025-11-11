package com.vslk.lbgx.room.avroom.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.vslk.lbgx.base.activity.BaseMvpActivity;
import com.hncxco.library_ui.widget.AppToolBar;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.base.factory.CreatePresenter;
import com.tongdaxing.xchat_core.UriProvider;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.presenter.RoomSettingPresenter;
import com.tongdaxing.xchat_core.room.view.IRoomSettingView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Administrator
 * @date 2018/5/2
 */

@CreatePresenter(RoomSettingPresenter.class)
public class RoomTopicActivity extends BaseMvpActivity<IRoomSettingView, RoomSettingPresenter> implements IRoomSettingView {

    @BindView(R.id.toolbar)
    AppToolBar titleBar;
    @BindView(R.id.edt_topic_title)
    EditText edtTopicTitle;

    @BindView(R.id.edt_topic_content)
    EditText edtTopicContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        setContentView(R.layout.room_topic_edit);
        ButterKnife.bind(this);
        String roomNotice = roomInfo.getRoomNotice();
        if (!TextUtils.isEmpty(roomNotice)) {
            edtTopicContent.setText(roomNotice);
        }
        edtTopicTitle.setText(roomInfo.getRoomDesc());
        //标题栏点击事件
        titleBar.setOnBackBtnListener(view -> finish());
        titleBar.setOnRightTitleClickListener(this::checkValidate);
    }

    private void checkValidate() {
        String roomDesc = edtTopicTitle.getText().toString();
        String roomNotice = edtTopicContent.getText().toString();
        if (TextUtils.isEmpty(roomDesc)) {
            toast("标题不能为空");
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
        save(url, roomDesc, roomNotice);
    }

    private void save(String url, String roomDesc, String roomNotice) {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) {
            return;
        }
        getDialogManager().showProgressDialog(this);
        getMvpPresenter().saveRoomTopic(roomDesc, roomNotice, roomInfo, url);
    }

    @Override
    public void onSaveRoomTopicSuccessView(String roomDesc,String roomNotice) {
        if (getDialogManager() != null) {
            getDialogManager().dismissDialog();
        }
        CoreManager.getCore(IGiftCore.class).sendWanFaMeg(roomDesc, roomNotice, AvRoomDataManager.get().mCurrentRoomInfo.getRoomId());
        toast("保存成功");
        finish();
    }

    @Override
    public void onSaveRoomTopicFailView(String msg) {
        if (getDialogManager() != null) {
            getDialogManager().dismissDialog();
        }
        toast(msg);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, RoomTopicActivity.class);
        context.startActivity(intent);
    }
}
