package com.tongdaxing.xchat_core.room.view;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.redpacket.bean.ActionDialogInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;

import java.util.List;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2017/12/11
 */
public interface IAvRoomView extends IMvpBaseView {

    void requestRoomInfoSuccessView(RoomInfo roomInfo);

    void requestRoomInfoFailView(String errorStr);

    /**
     * 进去房间成功
     */
    void enterRoomSuccess();

    /** 进入房间失败 */
    void enterRoomFail(int code, String error);

    /** 显示结束直播界面 */
    void showFinishRoomView();

    /** 显示黑名单进入房间错误页面 */
    void showBlackEnterRoomView();

    /** 获取活动信息成功 */
    void onGetActionDialog(List<ActionDialogInfo> actionDialogInfoList);

    /** 获取活动信息失败 */
    void onGetActionDialogError(String error);

    /**
     * 退出房间
     *
     * @param currentRoomInfo
     */
    void exitRoom(RoomInfo currentRoomInfo);

    /**
     * 定时拉取人数成功
     *
     * @param onlineNumber
     */
    void onRoomOnlineNumberSuccess(int onlineNumber);
}
