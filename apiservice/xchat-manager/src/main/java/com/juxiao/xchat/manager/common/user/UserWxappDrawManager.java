package com.juxiao.xchat.manager.common.user;

import com.juxiao.xchat.dao.user.dto.UserWxappDrawDTO;

public interface UserWxappDrawManager {

    void redueceUserWxappDraw(Long uid);

    /**
     * @param uid
     * @return
     */
    UserWxappDrawDTO getUserWxappDraw(Long uid);
}
