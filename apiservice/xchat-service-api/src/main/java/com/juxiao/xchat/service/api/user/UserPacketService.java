package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.service.api.user.vo.UserPacketRecordVO;

/**
 * @class: UserPacketService.java
 * @author: chenjunsheng
 * @date 2018/6/22
 */
public interface UserPacketService {

    /**
     * 获取首次红包
     *
     * @param uid
     * @return
     */
    UserPacketRecordVO getFirstPacket(Long uid);
}
