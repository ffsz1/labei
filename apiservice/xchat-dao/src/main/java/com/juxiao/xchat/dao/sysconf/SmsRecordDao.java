package com.juxiao.xchat.dao.sysconf;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.sysconf.domain.SmsRecordDO;

/**
 * 短信发送记录
 *
 * @class: SmsRecordDao.java
 * @author: chenjunsheng
 * @date 2018/6/11
 */
public interface SmsRecordDao {

    /**
     * 保存短信发送记录
     *
     * @param recordDo
     * @author: chenjunsheng
     * @date 2018/6/11
     */
    @TargetDataSource
    void save(SmsRecordDO recordDo);
}
