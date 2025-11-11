package com.vslk.lbgx.presenter.home;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.home.BannerInfo;
import com.tongdaxing.xchat_core.home.HomeInfo;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.tongdaxing.xchat_core.im.custom.bean.PublicChatRoomAttachment;

import java.util.List;

public interface IHomeHotView extends IMvpBaseView {
    void getSquareData(List<PublicChatRoomAttachment> list);

    //获取首页热门数据成功
    void getHomeHotRoomSuccess(HomeInfo result);


    void getPeiPeiList(List<HomeRoom> result);

    //获取热门数据失败
    void getHomeHotRoomFail(Exception error);

    void getHomeBannerSuccess(List<BannerInfo> response);

    void getHomeBannerFail(String errors);
}
