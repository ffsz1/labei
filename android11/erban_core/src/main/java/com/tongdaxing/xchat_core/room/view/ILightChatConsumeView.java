package com.tongdaxing.xchat_core.room.view;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.room.queue.bean.RoomConsumeInfo;

import java.util.List;

/**
 * <p> </p>
 *
 * @author jiahui
 * @date 2017/12/24
 */
public interface ILightChatConsumeView extends IMvpBaseView {

    void onGetRoomConsumeListSuccess(List<RoomConsumeInfo> roomConsumeInfos);

    void onGetRoomConsumeListFail(String error);
}
