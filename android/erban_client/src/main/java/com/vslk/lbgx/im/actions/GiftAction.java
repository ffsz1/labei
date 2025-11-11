package com.vslk.lbgx.im.actions;

import com.vslk.lbgx.room.egg.bean.NimGiftUser;
import com.vslk.lbgx.room.gift.GiftDialog;
import com.vslk.lbgx.ui.me.wallet.activity.WalletActivity;
import com.netease.nim.uikit.session.actions.BaseAction;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.gift.GiftInfo;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_core.room.queue.bean.MicMemberInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by chenran on 2017/10/2.
 */

public class GiftAction extends BaseAction implements GiftDialog.OnGiftDialogBtnClickListener {


    public GiftAction() {
        super(R.drawable.icon_gift_action, R.string.gift_action);
    }

    transient private GiftDialog giftDialog;

    @Override
    public void onClick() {
        if (giftDialog == null) {
            NimGiftUser nimGiftUser = new NimGiftUser();
            nimGiftUser.setUid(Long.valueOf(getAccount()));
            nimGiftUser.setAvatar("");
            nimGiftUser.setNick("");
            nimGiftUser.setInfoBtn(false);
            giftDialog = new GiftDialog(getActivity(), nimGiftUser);
            giftDialog.setGiftDialogBtnClickListener(this);
            giftDialog.setSinglePeople(true);
            giftDialog.setOnDismissListener(dialog -> giftDialog = null);
        }
        if (!giftDialog.isShowing()) {
            if (giftDialog != null) {
                giftDialog.show();
            }

        }
    }

    @Override
    public void onRechargeBtnClick() {
        WalletActivity.start(getActivity());
    }

    @Override
    public void onSendGiftBtnClick(GiftInfo giftInfo, long uid, int number, int currentP) {
        if (giftInfo != null) {
            CoreManager.getCore(IGiftCore.class).sendPersonalGiftToNIM(giftInfo.getGiftId(), uid, number, giftInfo.getGoldPrice(), new WeakReference<>(getContainer()), currentP);
        }
    }

    @Override
    public void onSendGiftBtnClick(GiftInfo giftInfo, List<MicMemberInfo> micMemberInfos, int number, int currentP) {
    }
}