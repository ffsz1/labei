package com.tongdaxing.xchat_core.room.presenter;

import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.room.model.RoomBaseModel;
import com.tongdaxing.xchat_core.room.view.IRoomManagerView;
import com.tongdaxing.xchat_framework.im.IMReportResult;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.List;

/**
 * <p> </p>
 *
 * @author jiahui
 * @date 2017/12/19
 */
public class RoomManagerPresenter extends AbstractMvpPresenter<IRoomManagerView> {

    private final RoomBaseModel mRoomBaseModel;

    public RoomManagerPresenter() {
        mRoomBaseModel = new RoomBaseModel();
    }

    public void queryManagerList(int limit) {
        mRoomBaseModel.fetchRoomManagers(new OkHttpManager.MyCallBack<IMReportResult<List<IMChatRoomMember>>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {//
                    getMvpView().queryManagerListFail();
                }
            }

            @Override
            public void onResponse(IMReportResult<List<IMChatRoomMember>> response) {
                if (response != null && response.isSuccess()) {//
                    if (!ListUtils.isListEmpty(response.getData()))
                        AvRoomDataManager.get().mRoomManagerList = response.getData();
                    if (getMvpView() != null)
                        getMvpView().queryManagerListSuccess(response.getData());
                } else {
                    if (getMvpView() != null) {//
                        getMvpView().queryManagerListFail();
                    }
                }
            }
        });
    }

    /**
     * 设置管理员
     *
     * @param roomId
     * @param account
     * @param mark    true:设置管理员 ,false：移除管理员
     */
    public void markManagerList(long roomId, final String account, boolean mark) {
        mRoomBaseModel.markManagerList(roomId, account, mark, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().markManagerListFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(Json json) {
                if (json.num("errno") == 0) {
                    if (getMvpView() != null) {
                        getMvpView().markManagerListSuccess(account);
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().markManagerListFail(json.num("errno")+ " : " + json.str("errmsg"));
                    }
                }
            }
        });


    }
}
