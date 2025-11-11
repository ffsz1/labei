package com.tongdaxing.xchat_core.room.publicchatroom;

import android.content.Context;

import com.netease.nim.uikit.common.util.log.LogUtil;
import com.tongdaxing.xchat_framework.util.util.SpUtils;

/**
 * Created by Administrator on 2018/3/29.
 *
 * 云信的坑，进入多个房间的时候可能会发送更换队列的信息
 * 做标记
 */

public class ChatRoomEnterCache {

    public static final String chatRoomType = "chatRoomType";
    public static final int isInPublicRoom = 1;


    public static void setType(Context context, int type) {
        LogUtil.d("incomingChatObserver5", type + "");
        SpUtils.put(context, chatRoomType, type);
    }

    public static int getType(Context context) {
        int i = (Integer) SpUtils.get(context, chatRoomType, 0);
        return i;
    }

}
