package com.tongdaxing.xchat_core.room.presenter;

import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.room.model.RoomBaseModel;
import com.tongdaxing.xchat_core.room.view.IRoomBlackView;
import com.tongdaxing.xchat_framework.im.IMReportResult;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.List;

/**
 * <p> </p>
 *
 * @author jiahui
 * @date 2017/12/19
 */
public class RoomBlackPresenter extends AbstractMvpPresenter<IRoomBlackView> {
    private final RoomBaseModel mRoomBaseModel;

    public RoomBlackPresenter() {
        mRoomBaseModel = new RoomBaseModel();
    }

    public void queryNormalListFromIm(int limit, int start) {
//        mRoomBaseModel.queryNormalList(limit, time)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<IMChatRoomMember>>() {
//                    @Override
//                    public void accept(List<IMChatRoomMember> chatRoomMemberList) throws Exception {
//                        if (getMvpView() != null)
//                            getMvpView().queryNormalListSuccess(chatRoomMemberList);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        if (getMvpView() != null)
//                            getMvpView().queryNormalListFail();
//                    }
//                });
        mRoomBaseModel.fetchRoomBlackList(start,limit,new OkHttpManager.MyCallBack<IMReportResult<List<IMChatRoomMember>>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null)
                    getMvpView().queryNormalListFail();
            }

            @Override
            public void onResponse(IMReportResult<List<IMChatRoomMember>> response) {
                if (response != null && response.isSuccess()){
                    if (getMvpView() != null)
                        getMvpView().queryNormalListSuccess(response.getData());
                }else {
                    if (getMvpView() != null)
                        getMvpView().queryNormalListFail();
                }
            }
        });
    }

    /**
     * 拉黑操作
     *
     * @param roomId
     * @param account
     * @param mark    true，拉黑，false：移除拉黑
     */
    public void markBlackList(long roomId, final String account, final boolean mark) {


        mRoomBaseModel.markBlackList(account, mark, new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null)
                    getMvpView().makeBlackListFail(e.getMessage());
            }

            @Override
            public void onResponse(Json json) {
                if (json.num("errno") == 0) {
                    if (getMvpView() != null)
                        getMvpView().makeBlackListSuccess(account, mark);
                } else {
                    if (getMvpView() != null)
                        getMvpView().makeBlackListFail(json.num("errno")+ " : " + json.str("errmsg"));
                }
            }
        });



    }
}
