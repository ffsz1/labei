package com.vslk.lbgx.im.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vslk.lbgx.ui.find.activity.InviteAwardActivity;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.im.custom.bean.RedPacketAttachment;

/**
 * Created by chenran on 2017/9/21.
 */

public class MsgViewHolderRedPacket extends MsgViewHolderBase implements View.OnClickListener {
    private TextView text;
    private LinearLayout container;

    public MsgViewHolderRedPacket(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.layout_msg_view_holder_red_packet;
    }

    @Override
    protected void inflateContentView() {
        text = findViewById(R.id.tip_text);
        container = findViewById(R.id.layout_container);
    }

    @Override
    protected void bindContentView() {
        RedPacketAttachment attachment = (RedPacketAttachment) message.getAttachment();
        text.setText("收到" + attachment.getRedPacketInfo().getPacketName() + "红包，快去看看吧！");
        container.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        InviteAwardActivity.start(context);
    }
}
