package com.vslk.lbgx.presenter.home;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.user.bean.AttentionInfo;
import com.tongdaxing.xchat_framework.http_image.result.ServiceResult;

import java.util.List;

public interface IHomeAttentionView extends IMvpBaseView{
    default void getRoomAttentionListSuccess(ServiceResult<List<AttentionInfo>> datas){}
    default void getRoomAttentionListFail(String error){}

    default void getRoomRecommendListSuccess(ServiceResult<List<AttentionInfo>> response){}
    default void getRoomRecommendListFail(Exception error){}

    default void userAttentionSuccess(int position){}
    default void userAttentionFail(String error){}
}
