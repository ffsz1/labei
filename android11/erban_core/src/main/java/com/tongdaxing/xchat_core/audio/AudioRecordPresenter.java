package com.tongdaxing.xchat_core.audio;

import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.xchat_core.room.model.AvRoomModel;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2018/1/4
 */
public class AudioRecordPresenter extends AbstractMvpPresenter<IAudioRecordView> {

    private final AvRoomModel mAvRoomModel;

    public AudioRecordPresenter() {
        mAvRoomModel = new AvRoomModel();
    }

    public void exitRoom() {
        mAvRoomModel.exitRoom(null);
    }
}
