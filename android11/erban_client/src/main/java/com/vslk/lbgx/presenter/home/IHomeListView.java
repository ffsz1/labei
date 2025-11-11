package com.vslk.lbgx.presenter.home;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import java.util.List;

public interface IHomeListView extends IMvpBaseView{

    void getHomeRoomListByIdSuccess(ServiceResult<List<HomeRoom>> homeRoomList);
    void getHomeRoomListByIdFail(Exception error);
}
