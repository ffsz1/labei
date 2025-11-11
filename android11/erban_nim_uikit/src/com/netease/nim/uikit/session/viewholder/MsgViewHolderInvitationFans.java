package com.netease.nim.uikit.session.viewholder;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.glide.GlideApp;
import com.netease.nim.uikit.session.module.IShareFansCoreClient;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.Json;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderInvitationFans extends MsgViewHolderBase {

    protected TextView bodyTextView;
    private ImageView imageViewIcon;
    private View bg;
    private View buJoin;

    public MsgViewHolderInvitationFans(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_share_fans;
    }

    @Override
    protected void inflateContentView() {
        bodyTextView = findViewById(R.id.tv_title);
        imageViewIcon = findViewById(R.id.iv_user_icon);
        bg = findViewById(R.id.ll_bg);
        buJoin = findViewById(R.id.bu_join);
    }

    @Override
    protected void bindContentView() {


        Json json = Json.parse(message.getContent());



        bodyTextView.setText(json.str("title"));
//        bodyTextView.setTextColor(isReceivedMessage() ? Color.BLACK : Color.WHITE);

        bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        bodyTextView.setOnLongClickListener(longClickListener);
        GlideApp.with(context).load(json.str("avatar")).into(imageViewIcon);
        final long uid = json.num_l("uid");
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoreManager.notifyClients(IShareFansCoreClient.class, IShareFansCoreClient.onShareFansJoin, uid);
            }
        };
        bg.setOnClickListener(l);
        bodyTextView.setOnClickListener(l);
        buJoin.setOnClickListener(l);
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
