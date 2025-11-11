package com.vslk.lbgx.presenter.home;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.home.BannerInfo;
import com.tongdaxing.xchat_core.home.HomeIcon;
import com.tongdaxing.xchat_core.home.TabInfo;

import java.util.List;

public interface IHomeView extends IMvpBaseView {

    default void getHomeTabListSuccess(List<TabInfo> tabs) {
    }

    default void getHomeTabListFail(String errors) {
    }



    default void getHomeBannerFail(String errors) {
    }

    default void setupHomeIconsSuccessView(List<HomeIcon> homeIcons) {
    }

    default void setupHomeIconsFailView(String msg) {
    }

    void getHomeBannerSuccess(List<BannerInfo> response);

    void getRecommendBannerFailure();

    void getRecommendBannerSuccess(List<BannerInfo> response);
}
