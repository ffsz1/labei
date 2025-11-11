package com.tongdaxing.xchat_core.home.view;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;
import com.tongdaxing.xchat_core.redpacket.bean.RedPacketInfoV2;
import com.tongdaxing.xchat_core.user.bean.NewRecommendBean;

/**
 * <p>  </p>
 *
 * @author jiahui
 * @date 2017/12/12
 */
public interface IMainView extends IMvpBaseView {
    /**
     * 退出房间
     */
    void exitRoom();

    default void onNewUserRecommendSuccessView(NewRecommendBean bean){}
    default void onNewUserRecommendFailView(RedPacketInfoV2 redPacketInfo){}
    void hasBindPhone();
    void hasBindPhoneFail(String error);
    void isShowTeenager();

}
