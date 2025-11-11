package com.juxiao.xchat.dao.user;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.domain.UserShareRecordDO;
import com.juxiao.xchat.dao.user.query.TodayShareRecordQuery;
import org.apache.ibatis.annotations.Param;

/**
 * @class: UserShareRecordDao.java
 * @author: chenjunsheng
 * @date 2018/6/14
 */
public interface UserShareRecordDao {

    /**
     * @param recordDo
     */
    @TargetDataSource
    void save(UserShareRecordDO recordDo);

    /**
     * 统计用户的分享次数
     *
     * @param uid
     * @return
     */
    @TargetDataSource(name = "ds2")
    int countUserShareRecord(@Param("uid") Long uid);

    /**
     * @param query
     * @return
     */
    @TargetDataSource(name = "ds2")
    int countTodayShareRecord(TodayShareRecordQuery query);


}
