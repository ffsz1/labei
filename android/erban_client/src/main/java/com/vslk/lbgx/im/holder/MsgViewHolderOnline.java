
package com.vslk.lbgx.im.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.im.custom.bean.OpenRoomNotiAttachment;

/**
 * Created by chenran on 2017/9/21.
 */

public class MsgViewHolderOnline extends MsgViewHolderBase implements View.OnClickListener{
    private ImageView avatar;
    private TextView nick;
    private LinearLayout container;

    public MsgViewHolderOnline(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.layout_msg_view_holder_online;
    }

    @Override
    protected void inflateContentView() {
        avatar = findViewById(R.id.avatar);
        nick = findViewById(R.id.nick);
        container = findViewById(R.id.layout_container);
    }

    @Override
    protected void bindContentView() {
        OpenRoomNotiAttachment attachment = (OpenRoomNotiAttachment) message.getAttachment();
        if (attachment != null) {
            if (!StringUtil.isEmpty(attachment.getNick())) {
                nick.setText(attachment.getNick() + " 上线啦");
                ImageLoadUtils.loadAvatar(avatar.getContext(), attachment.getAvatar(), avatar);
            } else {
                NimUserInfo nimUserInfo = NIMClient.getService(UserService.class).getUserInfo(attachment.getUid() + "");
                if (nimUserInfo != null) {
                    nick.setText(nimUserInfo.getName() + " 上线啦");
                    ImageLoadUtils.loadAvatar(avatar.getContext(), nimUserInfo.getAvatar(), avatar);
                }
            }
            container.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        OpenRoomNotiAttachment attachment = (OpenRoomNotiAttachment) message.getAttachment();
        AVRoomActivity.start(v.getContext(), attachment.getUid());
    }
}
