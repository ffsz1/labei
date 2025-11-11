package com.vslk.lbgx.presenter.rankinglist;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.bean.RankingInfo;
import com.tongdaxing.xchat_core.bean.UserLevelInfo;

import java.util.List;

public interface IRankingListView extends IMvpBaseView {

    default void setupFailView(String message) {

    }

    default void setupSuccessView(List<RankingInfo> rankingList) {

    }

    /**
     * 获取用户等级与魅力
     */
    default void getUserLevelSuccess(UserLevelInfo info) {

    }

    default void getUserLevelFail(String msg) {

    }
}

