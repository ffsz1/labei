package com.vslk.lbgx.room.avroom.fragment.audio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.vslk.lbgx.room.avroom.fragment.base.BaseRootFragment;
import com.tongdaxing.xchat_core.Constants;
import com.tongdaxing.xchat_core.gift.IGiftCore;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;

/**
 * 轰趴房
 * Created by 2016/9/22.
 *
 * @author Administrator
 */
public class AudioRootFragment extends BaseRootFragment {

    public static AudioRootFragment newInstance(long roomUid) {
        Log.e("onNewIntent", "HomePartyFragment - newInstance");
        AudioRootFragment audioRootFragment = new AudioRootFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.ROOM_UID, roomUid);
        audioRootFragment.setArguments(bundle);
        return audioRootFragment;
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (roomFragment != null) {
            roomFragment.onNewIntent(intent);
        }
    }

    @Override
    protected BaseAudioFragment itemRoomFragment() {
        return new AudioFragment();
    }

    @Override
    public void updateGiftInfo() {
        CoreManager.getCore(IGiftCore.class).requestGiftInfos();
    }
}