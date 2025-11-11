package com.erban.main.mybatismapper;

import com.erban.main.model.WithDrawPacketCashProd;
import com.erban.main.model.WithDrawPacketCashProdExample;
import java.util.List;

public interface WithDrawPacketCashProdMapper {
    int deleteByPrimaryKey(Integer packetProdCashId);

    int insert(WithDrawPacketCashProd record);

    int insertSelective(WithDrawPacketCashProd record);

    List<WithDrawPacketCashProd> selectByExample(WithDrawPacketCashProdExample example);

    WithDrawPacketCashProd selectByPrimaryKey(Integer packetProdCashId);

    int updateByPrimaryKeySelective(WithDrawPacketCashProd record);

    int updateByPrimaryKey(WithDrawPacketCashProd record);
}
