package com.vslk.lbgx.ui.launch.activity;

import android.os.Bundle;

import com.vslk.lbgx.base.activity.BaseActivity;
import com.vslk.lbgx.room.AVRoomActivity;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.tongdaxing.xchat_core.im.custom.bean.CustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.IMCustomAttachment;
import com.tongdaxing.xchat_core.im.custom.bean.OpenRoomNotiAttachment;

import java.util.ArrayList;

/**
 * Created by chenran on 2017/8/5.
 */

public class NimMiddleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            ArrayList<IMMessage> messages = (ArrayList<IMMessage>)
                    getIntent().getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
            if (messages != null && messages.size() > 0) {
                IMMessage imMessage = messages.get(messages.size()-1);
                if (imMessage.getMsgType() == MsgTypeEnum.custom) {
                    CustomAttachment attachment = (CustomAttachment) imMessage.getAttachment();
                    if (attachment.getFirst() == IMCustomAttachment.CUSTOM_MSG_HEADER_TYPE_OPEN_ROOM_NOTI) {
                        OpenRoomNotiAttachment noticeAttachment = (OpenRoomNotiAttachment) attachment;
                        if (noticeAttachment.getUid() > 0) {
//                            boolean isImLogin = CoreManager.getCore(IIMLoginCore.class).isImLogin();
//                            if (isImLogin) {
                                AVRoomActivity.start(this, noticeAttachment.getUid());
//                            }
                        }
                    }
                }
            }
        }
        finish();
    }
}
