package com.vslk.lbgx.room.avroom.fragment.base;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

import com.vslk.lbgx.model.attention.AttentionModel;
import com.vslk.lbgx.room.avroom.fragment.audio.BaseAudioFragment;
import com.vslk.lbgx.room.avroom.widget.GiftV2View;
import com.vslk.lbgx.ui.widget.dialog.ShareDialog;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.praise.IPraiseClient;
import com.tongdaxing.xchat_core.redpacket.bean.ActionDialogInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.share.IShareCore;
import com.tongdaxing.xchat_framework.coremanager.CoreEvent;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.util.DESUtils;

import java.util.List;

import cn.sharesdk.framework.Platform;

import static com.tongdaxing.xchat_framework.util.util.DESUtils.giftCarSecret;

public abstract class BaseRootFragment extends AbsRoomFragment implements View.OnClickListener, ShareDialog.OnShareDialogItemClick, BaseAudioFragment.UserComeAction {

    protected BaseRoomFragment roomFragment;
    protected AttentionModel attentionModel;
    protected GiftV2View giftV2View;

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_chatroom_game_main;
    }

    /***
     * 房间
     * @return
     */
    protected abstract BaseAudioFragment itemRoomFragment();

    /***
     * 更新礼物信息
     */
    public abstract void updateGiftInfo();


    @Override
    public void initiate() {
        roomFragment = itemRoomFragment();
        roomFragment.setUserComeAction(this);
        getChildFragmentManager().beginTransaction().replace(R.id.fm_content, roomFragment).commitAllowingStateLoss();
        updateGiftInfo();
        if (attentionModel == null) {
            attentionModel = new AttentionModel();
        }
    }


    @Override
    public void showCar(String carImageUrl) {
        try {
            RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
            if (roomInfo != null && roomInfo.getGiftCardSwitch() == 0) {
                final String carUrl = DESUtils.DESAndBase64Decrypt(carImageUrl, giftCarSecret);
                giftV2View.giftEffectView.drawSvgaEffect(carUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSharePlatformClick(Platform platform) {
        RoomInfo currentRoomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (currentRoomInfo != null) {
            CoreManager.getCore(IShareCore.class).shareRoom(platform, currentRoomInfo.getUid(),
                    currentRoomInfo.getTitle());
        }
    }

    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onCanceledPraiseFaith(String error) {
        getDialogManager().dismissDialog();
    }


    //关注更新用户资料
    @CoreEvent(coreClientClass = IPraiseClient.class)
    public void onPraiseFaith(String error) {
        getDialogManager().dismissDialog();
    }


    /***
     * 置空
     */
    @Override
    public void release() {
        super.release();
        if (roomFragment != null) {
            roomFragment = null;
        }
    }

    @Override
    public void onFindViews() {

        giftV2View = mView.findViewById(R.id.gift_view);

    }

    @Override
    public void onSetListener() {
    }

    @Override
    public void onClick(View view) {
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        roomFragment.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onShowActivity(List<ActionDialogInfo> dialogInfos) {
        if (roomFragment != null && roomFragment.isAdded()) {
            roomFragment.showActivity(dialogInfos);
        }
    }

    @Override
    public void onRoomOnlineNumberSuccess(int onlineNumber) {
        if (roomFragment != null) roomFragment.onRoomOnlineNumberSuccess(onlineNumber);
    }

    @Override
    public void onDestroy() {
        giftV2View.release();
        super.onDestroy();
    }
}
