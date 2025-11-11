package com.tongdaxing.xchat_core.redpacket;

import com.tongdaxing.xchat_core.bills.IBillsCoreClient;
import com.tongdaxing.xchat_core.redpacket.bean.ActionDialogInfo;
import com.tongdaxing.xchat_core.redpacket.bean.RedDrawListInfo;
import com.tongdaxing.xchat_core.redpacket.bean.RedPacketInfo;
import com.tongdaxing.xchat_core.redpacket.bean.RedPacketInfoV2;
import com.tongdaxing.xchat_core.redpacket.bean.WithdrawRedListInfo;
import com.tongdaxing.xchat_core.redpacket.bean.WithdrawRedSucceedInfo;

import java.util.List;

/**
 * Created by ${Seven} on 2017/9/20.
 */

public interface IRedPacketCoreClient extends IBillsCoreClient {

    public static final String METHOD_ON_GET_RED_INFO = "onGetRedInfo";
    public static final String METHOD_ON_GET_RED_INFO_ERROR = "onGetRedInfoError";

    public static final String METHOD_ON_GET_ACTION_DIALOG="onGetActionDialog";
    public static final String METHOD_ON_GET_ACTION_DIALOG_ERROR="onGetActionDialogError";

    public static final String METHOD_ON_GET_RED_LIST ="onGetRedList";
    public static final String METHOD_ON_GET_RED_LIST_ERROR ="onGetRedListError";

    public static final String METHOD_ON_GET_RED_DRAW_LIST ="onGetRedDrawList";
    public static final String METHOD_ON_GET_RED_DRAW_LIST_ERROR ="onGetRedDrawListError";

    public static final String METHOD_ON_GET_WITHDRAW = "onGetWithdraw";
    public static final String METHOD_ON_GET_WITHDRAW_ERROR = "onGetWithdrawError";

    public static final String METHOD_ON_RECEIVE_NEW_PACKET= "onReceiveNewPacket";

    void onGetRedInfo(RedPacketInfo redPacketInfo);

    void onGetRedInfoError(String error);

    void onGetActionDialog(List<ActionDialogInfo> dialogInfo);
    void onGetActionDialogError(String error);

    void onGetRedList(List<WithdrawRedListInfo> withdrawRedListInfos);
    void onGetRedListError(String error);

    void onGetRedDrawList(List<RedDrawListInfo> redDrawListInfos);
    void onGetRedDrawListError(String error);

    void onGetWithdraw(WithdrawRedSucceedInfo succeedInfo);
    void onGetWithdrawError(String error);

    void onReceiveNewPacket(RedPacketInfoV2 redPacketInfo);

}
