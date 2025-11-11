package com.vslk.lbgx.presenter.sign;

import com.vslk.lbgx.model.mengcoin.MengCoinModel;
import com.vslk.lbgx.ui.sign.view.ITaskCenterView;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.mengcoin.MengCoinTaskBean;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

/**
 * Function:
 * Author: Edward on 2019/5/22
 */
public class TaskCenterPresenter extends AbstractMvpPresenter<ITaskCenterView> {
    private MengCoinModel mengCoinModel;

    public TaskCenterPresenter() {
        mengCoinModel = new MengCoinModel();
    }

    /**
     * 获取当前用户的萌币任务列表接口
     */
    public void getCurrentUserMengCoinTaskList() {
        mengCoinModel.getCurrentUserMengCoinTaskList(new OkHttpManager.MyCallBack<ServiceResult<MengCoinTaskBean>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().showMengCoinErrorView(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<MengCoinTaskBean> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().showMengCoinTaskListView(response.getData());
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().showMengCoinErrorView(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().showMengCoinErrorView("数据异常");
                    }
                }
            }
        });
    }

    /**
     * 通过missionId领取对应萌币任务奖励接口
     *
     * @param missionId
     */
    public void receiveMengCoinByMissionId(int missionId) {
        mengCoinModel.receiveMengCoinByMissionId(missionId, new OkHttpManager.MyCallBack<ServiceResult<String>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().receiveMengCoinFailToast(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<String> response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        if (getMvpView() != null) {
                            getMvpView().receiveMengCoinSucToast(missionId);
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().receiveMengCoinFailToast(response.getMessage());
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().receiveMengCoinFailToast("数据异常");
                    }
                }
            }
        });
    }
}
