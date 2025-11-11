package com.juxiao.xchat.dao.charge;

import com.juxiao.xchat.dao.charge.domain.ExchangeDiamondGoldRecordDO;
import com.juxiao.xchat.dao.config.TargetDataSource;

/**
 * 保存钻石兑换金币记录数据库接口
 *
 * @class: ExchangeDiamondGoldRecordDao.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
public interface ExchangeDiamondGoldRecordDao {

    /**
     * 保存
     *
     * @param recordDo
     */
    @TargetDataSource
    void save(ExchangeDiamondGoldRecordDO recordDo);
}
