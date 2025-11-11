package com.juxiao.xchat.manager.common.charge;

import com.juxiao.xchat.dao.charge.dto.RedPacketCashProdDTO;

import java.util.List;

/**
 * @class: RedPacketWithDrawManager.java
 * @author: chenjunsheng
 * @date 2018/6/13
 */
public interface RedPacketWithDrawManager {

    /**
     * @return
     */
    List<RedPacketCashProdDTO> listUsingPacketCashProd();
}
