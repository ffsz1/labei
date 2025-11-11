package com.juxiao.xchat.dao.charge;

import com.juxiao.xchat.dao.charge.dto.WithDrawCashProdDTO;
import com.juxiao.xchat.dao.config.TargetDataSource;

import java.util.List;

/**
 * @class: WithDrawCashProdDao.java
 * @author: chenjunsheng
 * @date 2018/6/11
 */
public interface WithDrawCashProdDao {

    /**
     * 根据产品ID查询对应的提现产品
     *
     * @param pid
     * @return
     */
    @TargetDataSource(name = "ds2")
    WithDrawCashProdDTO getCashProd(String pid);

    /**
     * 查询所有的兑换产品
     *
     * @return
     */
    @TargetDataSource(name = "ds2")
    List<WithDrawCashProdDTO> listAllCashProds();
}
