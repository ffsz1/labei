package com.juxiao.xchat.manager.common.charge;

import com.juxiao.xchat.dao.charge.dto.ChargeChannelProdDTO;
import com.juxiao.xchat.dao.charge.dto.ChargeProdDTO;

import java.util.List;

/**
 * @class: ChargeProdManager.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
public interface ChargeProdManager {
    /**
     * @param chargeProdId
     * @return
     * @author: chenjunsheng
     * @date 2018/6/7
     */
    ChargeProdDTO getChargeProd(String chargeProdId);

    List<ChargeChannelProdDTO> findChargeProd(int type, String appVersion);

}
