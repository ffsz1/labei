package com.vslk.lbgx.presenter.find;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.find.MicroMatch;
import com.tongdaxing.xchat_core.find.SpeedUserInfo;
import com.tongdaxing.xchat_core.user.bean.UserInfo;

import java.util.List;

public interface MicroMatchingView extends IMvpBaseView {

    default void onGetNextMatchView(List<MicroMatch> microMatch){}

    default void onGetNextMatchFailView(String msg){}

    default void onCharmUserListView(List<UserInfo> charmUsers){}

    default void onCharmUserListFailView(String msg){}

    default void onLobbyChatInfoSuccessView(List<SpeedUserInfo> speedUserInfos){}

    default void onLobbyChatInfoFailView(String msg){}
}

