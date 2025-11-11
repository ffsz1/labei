package com.juxiao.xchat.dao.sysconf;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.sysconf.domain.NetEaseChatroomDO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @class: NetEaseChatroomDao.java
 * @author: chenjunsheng
 * @date 2018/6/27
 */
public interface NetEaseChatroomDao {

    /**
     * @param chatroomDo
     */
    @TargetDataSource
    void save(NetEaseChatroomDO chatroomDo);

    @Select("select * from netease_chatroom where room_id = 22551544 order by id desc limit 3")
    List<NetEaseChatroomDO> findList();

}