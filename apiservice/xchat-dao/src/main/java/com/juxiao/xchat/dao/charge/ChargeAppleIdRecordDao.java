package com.juxiao.xchat.dao.charge;

import com.juxiao.xchat.dao.charge.domain.ChargeAppleIdRecordDO;

public interface ChargeAppleIdRecordDao {

    /**
     * 保存苹果订单id信息
     *
     * @param recordDo
     */
    void save(ChargeAppleIdRecordDO recordDo);

}
