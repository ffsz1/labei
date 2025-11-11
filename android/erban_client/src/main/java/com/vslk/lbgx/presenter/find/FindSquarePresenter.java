package com.vslk.lbgx.presenter.find;

import com.vslk.lbgx.model.find.FindSquareModel;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.find.AlertInfo;
import com.tongdaxing.xchat_core.find.family.SquareMemberInfo;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;
import com.tongdaxing.xchat_framework.im.IMError;
import com.tongdaxing.xchat_framework.im.IMProCallBack;
import com.tongdaxing.xchat_framework.im.IMReportBean;
import com.tongdaxing.xchat_framework.im.IMReportResult;
import com.tongdaxing.xchat_framework.util.util.Json;

import java.util.List;

public class FindSquarePresenter extends AbstractMvpPresenter<IFindSquareView> {
    private FindSquareModel findSquareModel;

    public FindSquarePresenter() {
        if (this.findSquareModel == null) {
            this.findSquareModel = new FindSquareModel();
        }
    }

    /**
     * 获取公聊大厅房间id
     */
    public void getSquareRoomId() {
        findSquareModel.checkSquareRoomVersion(new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().resetSquareLayout();
                    getMvpView().enterPublicRoomFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(Json response) {
                getMvpView().resetSquareLayout();
                if (response != null && response.num("code") == 200) {
                    Json data = response.json("data");
                    if (data != null) {
                        if (getMvpView() != null) {
                            getMvpView().getSquareRoomIdSuccess(data.boo("audit"));
                        }
                    } else {
                        if (getMvpView() != null) {
                            getMvpView().enterPublicRoomFail(response.str("message", "进入广场失败，请稍后重试"));
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        if (response != null) {
                            getMvpView().enterPublicRoomFail(response.str("message", "进入广场失败，请稍后重试"));
                        } else {
                            getMvpView().enterPublicRoomFail("进入广场失败，请稍后重试");
                        }

                    }
                }
            }
        });
    }

    /**
     * 进入公聊房间
     */
    public void enterRoom(String roomId) {
        findSquareModel.enterPublicRoom(roomId, new IMProCallBack() {
            @Override
            public void onSuccessPro(IMReportBean imReportBean) {
                if (imReportBean != null && imReportBean.getReportData().errno == 0) {
                    if (getMvpView() != null) {
                        getMvpView().enterPublicRoomSuccess(imReportBean);
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().resetSquareLayout();
                        getMvpView().enterPublicRoomFail(
                                imReportBean != null ? imReportBean.getReportData().errmsg : "进入广场失败，请稍后重试");
                    }
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                // 登录失败
                if (getMvpView() != null) {
                    getMvpView().resetSquareLayout();
                    getMvpView().enterPublicRoomFail(errorMsg);
                }
            }
        });
    }

    /**
     * 上报
     */
    public void getReportState() {
        findSquareModel.checkReport(new OkHttpManager.MyCallBack<Json>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(Json response) {
                if (response != null && response.num("code") == 200) {
                    if (getMvpView() != null) {
                        getMvpView().reportSuccess();
                    }
                }
            }
        });
    }

    /**
     * 发送广场的公聊消息
     */
    public void sendMessage(String roomId, String content) {

        findSquareModel.senPublicMsg(roomId, content, new IMProCallBack() {

            @Override
            public void onSuccessPro(IMReportBean imReportBean) {
                if (imReportBean != null && imReportBean.getReportData() != null) {
                    if (imReportBean.getReportData().errno == 0) {
                        if (getMvpView() != null) {
                            getMvpView().sendMessageSuccess();
                        }
                    } else {
                        if (getMvpView() != null) {
                            if (imReportBean.getReportData().errno == IMError.USER_REAL_NAME_NEED_PHONE
                                    || imReportBean.getReportData().errno == IMError.USER_REAL_NAME_AUDITING
                                    || imReportBean.getReportData().errno == IMError.USER_REAL_NAME_NEED_VERIFIED) {
                                getMvpView().showVerifiedDialog(imReportBean.getReportData().errno, imReportBean.getReportData().errmsg);
                            } else {
                                getMvpView().sendMessageFail(imReportBean.getReportData().errno + " : " + imReportBean.getReportData().errmsg);
                            }
                        }
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().sendMessageFail("消息发送失败，请稍后重试！");
                    }
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                if (getMvpView() != null) {
                    getMvpView().sendMessageFail(errorCode + " : " + errorMsg);
                }
            }
        });
    }

    /**
     * 发现 -- 活动列表
     */
    public void showActivities() {

        findSquareModel.findSquareActivity(new OkHttpManager.MyCallBack<ServiceResult<List<AlertInfo>>>() {

            @Override
            public void onError(Exception e) {
                if (getMvpView() != null) {
                    getMvpView().getFindActivityFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(ServiceResult<List<AlertInfo>> response) {
                if (response != null && response.isSuccess()) {
                    if (getMvpView() != null) {
                        getMvpView().getFindActivity(response.getData());
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().getFindActivityFail(response.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 获取交友列表数据
     */
    public void getMeetYouList(int currentPage) {

        findSquareModel.getMeetYouList(currentPage, new OkHttpManager.MyCallBack<ServiceResult<List<HomeRoom>>>() {

            @Override
            public void onError(Exception e) {
                getMvpView().getMeetYouListFail(e.getMessage());
            }

            @Override
            public void onResponse(ServiceResult<List<HomeRoom>> response) {
                if (response != null && response.isSuccess()) {
                    if (getMvpView() != null) {
                        getMvpView().getMeetYouList(response.getData());
                    }
                } else {
                    if (getMvpView() != null) {
                        getMvpView().getMeetYouListFail(response.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 获取广场头部数量
     *
     * @param roomId 房间版本
     */
    public void publicTitle(long roomId) {

        findSquareModel.publicTitle(roomId, new OkHttpManager.MyCallBack<IMReportResult<SquareMemberInfo>>() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(IMReportResult<SquareMemberInfo> response) {
                if (response != null && response.isSuccess()) {
                    if (getMvpView() != null) {
                        getMvpView().getPublicTitle(response.getData());
                    }
                }
            }
        });
    }

}
