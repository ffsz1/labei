package com.tongdaxing.xchat_core.home.presenter;

import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.listener.CallBack;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.home.view.IMainView;
import com.tongdaxing.xchat_core.redpacket.bean.RedPacketInfoV2;
import com.tongdaxing.xchat_core.room.model.AvRoomModel;
import com.tongdaxing.xchat_core.user.bean.NewRecommendBean;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2017/12/12
 */
public class MainPresenter extends AbstractMvpPresenter<IMainView> {

    private final AvRoomModel mAvRoomMode;

    public MainPresenter() {
        mAvRoomMode = new AvRoomModel();
    }

    public void exitRoom() {
        mAvRoomMode.exitRoom(new CallBack<String>() {
            @Override
            public void onSuccess(String data) {
                if (getMvpView() != null) {
                    getMvpView().exitRoom();
                }
            }

            @Override
            public void onFail(int code, String error) {

            }
        });
    }

    public void newUserRecommend(RedPacketInfoV2 redPacketInfo) {

        mAvRoomMode.newUserRecommend(new OkHttpManager.MyCallBack<ServiceResult<NewRecommendBean>>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onNewUserRecommendFailView(redPacketInfo);
                }
            }

            @Override
            public void onResponse(ServiceResult<NewRecommendBean> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().onNewUserRecommendSuccessView(response.getData());
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().onNewUserRecommendFailView(redPacketInfo);
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().onNewUserRecommendFailView(redPacketInfo);
                    }
                }
            }
        });
    }

    public void hasBindPhone() {
        mAvRoomMode.isPhones(new OkHttpManager.MyCallBack<ServiceResult>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null && e != null)
                    getMvpView().hasBindPhoneFail(e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult response) {
                if (response != null && response.isSuccess()) {
                    if (getMvpView() != null)
                        getMvpView().hasBindPhone();
                } else {
                    if (getMvpView() != null && response != null)
                        getMvpView().hasBindPhoneFail(response.getErrorMessage());
                }
            }
        });
    }

}
