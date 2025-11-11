package com.tongdaxing.xchat_core.user;


import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.IBaseCore;

/**
 * Created by chenran on 2017/3/15.
 */

public interface IUserDbCore extends IBaseCore {

    /**
     * 保存用户详细信息
     *
     * @param userInfo
     */
    public void saveDetailUserInfo(final UserInfo userInfo);

    /**
     * 查询用户详细信息
     *
     * @param uid
     */
    public UserInfo queryDetailUserInfo(final long uid);


    void updateHasWx(UserInfo userInfo, boolean hasWx);

    void updateHasQq(UserInfo userInfo, boolean hasQq);

    void updatePhone(UserInfo userInfo,String phone);
}
