package com.netease.nim.uikit.session.fragment;

import com.netease.nim.uikit.ait.AitManager;
import com.netease.nim.uikit.session.module.list.MessageListPanelEx;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import lombok.Data;

@Data
public class MsgBean {
    private MessageListPanelEx messageListPanel;
    private IMMessage message;
    private AitManager aitManager;
}
