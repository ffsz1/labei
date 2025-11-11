package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.UserDrawDO;
import com.juxiao.xchat.dao.user.dto.UserDrawDTO;

/**
 * @class: UserDrawDao.java
 * @author: chenjunsheng
 * @date 2018/6/21
 */
public interface UserDrawDao {

    /**
     * @param drawDo
     */
    @TargetDataSource
    void save(UserDrawDO drawDo);

    /**
     * 更新数据表
     *
     * @param drawDo
     */
    @TargetDataSource
    void update(UserDrawDO drawDo);

    /**
     * 获取用户的抽奖信息
     *
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    UserDrawDTO getUserDraw(Long uid);

}
