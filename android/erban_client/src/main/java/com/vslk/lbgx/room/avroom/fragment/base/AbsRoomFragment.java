package com.vslk.lbgx.room.avroom.fragment.base;

import com.tongdaxing.xchat_core.redpacket.bean.ActionDialogInfo;
import com.vslk.lbgx.base.fragment.BaseFragment;

import java.util.List;

/**
 * @author chenran
 * @date 2017/8/8
 */

public abstract class AbsRoomFragment extends BaseFragment {


    public abstract void onShowActivity(List<ActionDialogInfo> dialogInfo);


    public abstract void onRoomOnlineNumberSuccess(int onlineNumber);

    public void release() {
    }
}
