package com.tongdaxing.xchat_core.room.presenter;


import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.listener.CallBack;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.tongdaxing.xchat_core.room.model.LightChatConsumeModel;
import com.tongdaxing.xchat_core.room.queue.bean.RoomConsumeInfo;
import com.tongdaxing.xchat_core.room.view.ILightChatConsumeView;

import java.util.List;

/**
 * <p> </p>
 *
 * @author jiahui
 * @date 2017/12/24
 */
public class LightChatConsumePresenter extends AbstractMvpPresenter<ILightChatConsumeView> {
    private final LightChatConsumeModel mLightChatConsumeModel;

    public LightChatConsumePresenter() {
        mLightChatConsumeModel = new LightChatConsumeModel();
    }

    public void getRoomConsumeList() {
        RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
        if (roomInfo == null) {
            if (getMvpView() != null)
                getMvpView().onGetRoomConsumeListFail("网络异常");
            return;
        }
        mLightChatConsumeModel.getRoomConsumeList(roomInfo.getUid(), new CallBack<List<RoomConsumeInfo>>() {
            @Override
            public void onSuccess(List<RoomConsumeInfo> data) {
                if (getMvpView() != null)
                    getMvpView().onGetRoomConsumeListSuccess(data);
            }

            @Override
            public void onFail(int code, String error) {
                if (getMvpView() != null)
                    getMvpView().onGetRoomConsumeListFail(error);
            }
        });
    }
}
