package com.vslk.lbgx.presenter.home;

import com.vslk.lbgx.model.home.HomeModel;
import com.tongdaxing.erban.libcommon.base.AbstractMvpPresenter;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import java.util.List;

public class HomeListPresenter extends AbstractMvpPresenter<IHomeListView> {
    HomeModel homeModel;

    public HomeListPresenter() {
        if (homeModel == null){
            homeModel = new HomeModel();
        }
    }

    public void getHomeRoomListById(int tagId,int pageNum,int pageSize){
        homeModel.getHomeRoomListByTabId(tagId, pageNum, pageSize, new OkHttpManager.MyCallBack<ServiceResult<List<HomeRoom>>>() {
            @Override
            public void onError(Exception e) {
                if (getMvpView() != null){
                    getMvpView().getHomeRoomListByIdFail(e);
                }
            }

            @Override
            public void onResponse(ServiceResult<List<HomeRoom>> response) {
                if (response != null){
                    if (response.isSuccess()){
                        if (getMvpView() != null){
                            getMvpView().getHomeRoomListByIdSuccess(response);
                        }
                    }else {
                        if (getMvpView() != null){
                            getMvpView().getHomeRoomListByIdFail(new Exception(response.getMessage()));
                        }
                    }
                }else {
                    if (getMvpView() != null){
                        getMvpView().getHomeRoomListByIdFail(new Exception("数据异常！"));
                    }
                }
            }
        });
    }
}
