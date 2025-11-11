package com.tongdaxing.xchat_core.room.view;

import com.tongdaxing.erban.libcommon.base.IMvpBaseView;

/**
 * <p> </p>
 *
 * @author jiahui
 * @date 2017/12/19
 */
public interface IRoomMemberView extends IMvpBaseView {
    /**
     * 设置管理员成功
     *
     * @param uid
     */
    void markManagerListSuccess(String uid);

    /**
     * 设置管理员失败
     *
     */
    void markManagerListFail(String error);

}
