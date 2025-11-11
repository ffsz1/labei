package com.vslk.lbgx.presenter.find;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.find.AlertInfo;
import com.tongdaxing.xchat_core.find.family.SquareMemberInfo;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.tongdaxing.xchat_framework.im.IMReportBean;

import java.util.List;

public interface IFindSquareView extends IMvpBaseView{
    default  void showVerifiedDialog(int errorno, String error){}

    default void getSquareRoomIdSuccess(boolean audit){}
    default void resetSquareLayout(){}

    default void enterPublicRoomSuccess(IMReportBean imReportBean){}
    default void enterPublicRoomFail(String error){}

    default void reportSuccess(){}

    default void sendMessageSuccess(){}
    default void sendMessageFail(String error){}

    default void getFindActivity(List<AlertInfo> findInfos){}
    default void getFindActivityFail(String msg){}

    default void getMeetYouList(List<HomeRoom> homeRooms){}
    default void getMeetYouListFail(String msg){}

    default void getPublicTitle(SquareMemberInfo memberInfo){}
}
