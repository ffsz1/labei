package com.juxiao.xchat.dao.item;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.item.domain.GiftCarGetRecordDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @class: GiftCarGetRecordDao.java
 * @author: chenjunsheng
 * @date 2018/6/11
 */
public interface GiftCarGetRecordDao {

    /**
     * 保存数据库
     *
     * @param recordDo
     */
    @TargetDataSource
    void save(GiftCarGetRecordDO recordDo);

    @TargetDataSource
    List<GiftCarGetRecordDO> listUserCarGetRecord(@Param("uid") Long uid);
}