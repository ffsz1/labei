package com.juxiao.xchat.dao.bill;

import com.juxiao.xchat.dao.bill.domain.BillRecordDO;

@Deprecated
public interface BillRecordDao {

    /**
     * 保存记录
     *
     * @param recordDo
     */
    void save(BillRecordDO recordDo);
}
