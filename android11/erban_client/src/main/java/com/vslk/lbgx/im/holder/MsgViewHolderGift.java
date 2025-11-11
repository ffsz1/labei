package com.vslk.lbgx.im.holder;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vslk.lbgx.utils.ImageLoadUtils;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.im.custom.bean.nim.NimGiftAttachment;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

/**
 * Created by chenran on 2017/10/3.
 */

public class MsgViewHolderGift extends MsgViewHolderBase {
    private ImageView avatar;
    private TextView number;
    private TextView giftName;
    private LinearLayout container;

    public MsgViewHolderGift(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.layout_msg_view_holder_gift;
    }

    @Override
    protected void inflateContentView() {
        avatar = findViewById(R.id.avatar);
        number = findViewById(R.id.gift_number);
        container = findViewById(R.id.layout_container);
        giftName = findViewById(R.id.gift_name);
    }

    @Override
    protected void bindContentView() {
        NimGiftAttachment attachment = (NimGiftAttachment) message.getAttachment();
        GiftInfo giftInfo = CoreManager.getCore(IGiftCore.class).findGiftInfoById(attachment.getGiftRecieveInfo().getGiftId());
        if (giftInfo != null) {
            ImageLoadUtils.loadImage(avatar.getContext(), giftInfo.getGiftUrl(), avatar);
            number.setText("X" + attachment.getGiftRecieveInfo().getGiftNum());
            giftName.setText(giftInfo.getGiftName());
        }
    }
}
