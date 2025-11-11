package com.juxiao.xchat.dao.sysconf;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.sysconf.domain.NetEaseConversationDO;

/**
 * @class: NetEaseConversationDao.java
 * @author: chenjunsheng
 * @date 2018/6/27
 */
public interface NetEaseConversationDao {

    /**
     * @param conversationDo
     */
    @TargetDataSource
    void save(NetEaseConversationDO conversationDo);
}