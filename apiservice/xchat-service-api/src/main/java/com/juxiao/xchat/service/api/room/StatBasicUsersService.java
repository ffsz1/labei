package com.juxiao.xchat.service.api.room;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.room.domain.StatBasicUsersDO;

public interface StatBasicUsersService {

    StatBasicUsersDO addBasicUser(Long uid, Long roomUid) throws WebServiceException;

    /**
     * 统计用户在线时长
     *
     * @param roomUid
     * @param time
     * @param uid
     */
    void countRoomOnline(Long roomUid, Long time, Long uid);
}
