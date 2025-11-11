package com.vslk.lbgx.presenter.find;

import com.vslk.lbgx.model.find.SoundMatchModel;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.find.MicroMatch;
import com.tongdaxing.xchat_core.find.SpeedUserInfo;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import java.util.List;

public class MicroMatchingPresenter extends AbstractMvpPresenter<MicroMatchingView> {

    private SoundMatchModel mMatchModel;

    public MicroMatchingPresenter() {
        if (mMatchModel == null) {
            mMatchModel = new SoundMatchModel();
        }
    }

    /**
     * 获取聊天大厅数据
     */
    public void getLobbyChatInfo() {

        mMatchModel.getLobbyChatInfo(new OkHttpManager.MyCallBack<ServiceResult<List<SpeedUserInfo>>>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onLobbyChatInfoFailView(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<List<SpeedUserInfo>> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().onLobbyChatInfoSuccessView(response.getData());
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().onLobbyChatInfoFailView(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().onLobbyChatInfoFailView("数据异常");
                    }
                }
            }
        });
    }

    /**
     * 获取用户魅力球数据
     */
    public void soundMatchCharmUser() {

        mMatchModel.soundMatchCharmUser(new OkHttpManager.MyCallBack<ServiceResult<List<UserInfo>>>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onCharmUserListFailView(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<List<UserInfo>> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().onCharmUserListView(response.getData());
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().onCharmUserListFailView(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().onCharmUserListFailView("数据异常");
                    }
                }
            }
        });
    }

    /**
     * 获取卡片推荐数据
     */
    public void soundMatchRandomUser() {
        mMatchModel.soundMatchCharmUser(new OkHttpManager.MyCallBack<ServiceResult<List<MicroMatch>>>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().onGetNextMatchFailView(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<List<MicroMatch>> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().onGetNextMatchView(response.getData());
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().onGetNextMatchFailView(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().onGetNextMatchFailView("数据异常!");
                    }
                }
            }
        });
    }
}
