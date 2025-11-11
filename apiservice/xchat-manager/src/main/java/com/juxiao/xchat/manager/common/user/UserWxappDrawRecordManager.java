package com.juxiao.xchat.manager.common.user;

import com.juxiao.xchat.dao.user.dto.UserTodayShareDTO;

import java.util.List;

public interface UserWxappDrawRecordManager {

    /**
     *
     * @param uid
     * @return
     */
    List<UserTodayShareDTO> listTodayShareUser(Long uid);
}
