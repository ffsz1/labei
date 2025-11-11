package com.juxiao.xchat.dao.charge;

import com.juxiao.xchat.dao.charge.dto.RedPacketCashProdDTO;
import com.juxiao.xchat.dao.charge.dto.WithDrawPacketCashProdDTO;
import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 红包提现产品
 */
public interface WithDrawPacketCashProdDao {

    /**
     * 获取有效的的红包产品
     *
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<RedPacketCashProdDTO> listUseingPacketCashProd();

    /**
     * @param packetProdId
     * @return
     */
    @TargetDataSource(name = "ds2")
    WithDrawPacketCashProdDTO getPacketCashProd(@Param("packetProdId") Integer packetProdId);
}
