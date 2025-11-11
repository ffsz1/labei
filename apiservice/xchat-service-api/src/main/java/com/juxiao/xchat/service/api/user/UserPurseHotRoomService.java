package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.dto.UserPurseHotRoomResultDTO;

import java.util.List;

/**
 * @class: UserPurseHotRoomService.java
 * @author: chenjunsheng
 * @date 2018/7/12
 */
public interface UserPurseHotRoomService {

    /**
     * 推荐位-购买记录
     *
     * @param uid
     * @return
     */
    List<UserPurseHotRoomResultDTO> listHotRoomRecord(Long uid) throws WebServiceException;

    /**
     * 购买推荐位
     *
     * @param uid
     * @param erbanNo
     * @param date
     * @param hour
     */
    void purse(Long uid, Long erbanNo, String date, String hour) throws WebServiceException;
}
