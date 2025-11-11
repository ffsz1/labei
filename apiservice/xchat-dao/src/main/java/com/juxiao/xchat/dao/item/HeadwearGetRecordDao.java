package com.juxiao.xchat.dao.item;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.item.domain.HeadwearGetRecordDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @class: HeadwearGetRecordDao.java
 * @author: chenjunsheng
 * @date 2018/6/19
 */
public interface HeadwearGetRecordDao {

    /**
     * @param recordDo
     */
    @TargetDataSource
    void save(HeadwearGetRecordDO recordDo);


    @TargetDataSource
    List<HeadwearGetRecordDO> listUserHeadWearGetRecord(@Param("uid") Long uid);
}
