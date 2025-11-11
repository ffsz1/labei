package com.vslk.lbgx.im.transfer;

import android.widget.TextView;

import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.auth.IAuthCore;
import com.tongdaxing.xchat_core.im.custom.bean.nim.TransferAttachment;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

public class MsgTransferHolder extends MsgViewHolderBase {

    private TextView tvNick;
    private TextView tvGold;

    public MsgTransferHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.layout_msg_transfer;
    }

    @Override
    protected void inflateContentView() {
        tvNick = view.findViewById(R.id.tv_nick);
        tvGold = view.findViewById(R.id.tv_gold);
    }

    @Override
    protected void bindContentView() {
        TransferAttachment attachment = (TransferAttachment) message.getAttachment();
        long currentUid = CoreManager.getCore(IAuthCore.class).getCurrentUid();
        String nick;
//        if (attachment.getSendUid() == currentUid) {
//            nick = "转账给" + attachment.getRecvName();
//        } else {
//            nick = "转账给你";
//        }
        tvGold.setText(attachment.getGoldNum()+"开心 ");
//        tvNick.setText(nick);
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }
}
