package com.vslk.lbgx.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vslk.lbgx.ui.EmptyActivity;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;

/**
 * Created by chenran on 2017/11/16.
 */

public class NotificationClickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 跳转之前要处理的逻辑
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo != null) {
            EmptyActivity.startAvRoom(context, roomInfo.getUid());
        }
    }
}
