package com.juxiao.xchat.manager.common.user;

import com.juxiao.xchat.dao.user.dto.UserConfigureDTO;

/**
 * @class: UserConfigureManager.java
 * @author: chenjunsheng
 * @date 2018/6/14
 */
public interface UserConfigureManager {

    /**
     * @param uid
     * @return
     */
    UserConfigureDTO getUserConfigure(Long uid);
}
