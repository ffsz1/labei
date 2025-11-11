package com.juxiao.xchat.manager.common.charge;

import com.juxiao.xchat.dao.charge.dto.WithDrawCashProdDTO;

import java.util.List;

/**
 * @class: WithDrawCashProdManager.java
 * @author: chenjunsheng
 * @date 2018/6/11
 */
public interface WithDrawCashProdManager {

    /**
     * 获取体体现的金额
     *
     * @param pid
     * @return
     * @author: chenjunsheng
     * @date 2018/6/11
     */
    Long getCashNum(String pid);

    /**
     * 查询提现列表
     *
     * @return
     */
    List<WithDrawCashProdDTO> listAllCashProds();

    /**
     * 根据提现产品ID查询
     * @param pid pid
     * @return WithDrawCashProdDTO
     */
    WithDrawCashProdDTO getWithDrawCashProdByPid(String pid);
}