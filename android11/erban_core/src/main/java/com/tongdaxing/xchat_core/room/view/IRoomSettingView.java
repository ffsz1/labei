package com.tongdaxing.xchat_core.room.view;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.home.TabInfo;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;

import java.util.List;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2017/12/15
 */
public interface IRoomSettingView extends IMvpBaseView {

    default void onResultRequestTagAllSuccess(List<TabInfo> tabInfoList){}
    default void onResultRequestTagAllFail(String error){}

    default void updateRoomInfoSuccess(RoomInfo roomInfo){}
    default void updateRoomInfoFail(String error){}

    default void onSaveRoomPlayTipSuccessView(){}
    default void onSaveRoomPlayTipFailView(String msg){}

    default void onSaveRoomTopicSuccessView(String roomDesc,String roomNotice){}
    default void onSaveRoomTopicFailView(String msg){}
}
