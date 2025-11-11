package com.juxiao.xchat.manager.common.user;

import com.juxiao.xchat.dao.user.dto.UserPurseHotRoomResultDTO;

import java.util.Date;
import java.util.List;

/**
 * @class: UserPurseHotRoomManager.java
 * @author: chenjunsheng
 * @date 2018/7/12
 */
public interface UserPurseHotRoomManager {

    /**
     * 保存
     *
     * @param uid
     * @param erbanNo
     * @param goldCost
     * @param startTime
     * @param endTime
     * @param createTime
     */
    void save(Long uid, Long erbanNo, int goldCost, Date startTime, Date endTime, Date createTime);

    /**
     * 获取单条记录
     *
     * @param recordId
     * @return
     */
    UserPurseHotRoomResultDTO getHotRoomResult(String recordId);

    /**
     * 查询用户购买推荐位的记录ID
     *
     * @param uid
     * @return
     */
    List<String> listUserPurseHotRoomRecordId(Long uid);
}
