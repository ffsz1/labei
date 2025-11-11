package com.juxiao.xchat.dao.charge;

import com.juxiao.xchat.dao.charge.domain.ChargeAppleRecordDO;
import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ChargeAppleRecordDao {

    /**
     * 保存苹果订单信息
     *
     * @param recordDo
     */
    void save(ChargeAppleRecordDO recordDo);

    /**
     * 查询数据库中是否存在收据
     *
     * @param receip
     * @return
     */
    @Select("SELECT count(*) FROM charge_apple_record WHERE receip = #{receip}")
    int coutReceipt(@Param("receip") String receip);
}
