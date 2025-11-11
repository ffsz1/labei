package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.UserBoxDrawRecordDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @class: UserDrawDao.java
 * @author: chenjunsheng
 * @date 2018/6/21
 */
public interface UserBoxDrawDao {

    /**
     * @param drawDo
     */
    @TargetDataSource
    void save(UserBoxDrawRecordDO drawDo);

    @TargetDataSource
    List<UserBoxDrawRecordDO> listUserBoxDrawRecord(@Param("uid") Long uid);
}
