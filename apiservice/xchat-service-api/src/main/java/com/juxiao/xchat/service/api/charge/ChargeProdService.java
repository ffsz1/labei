package com.juxiao.xchat.service.api.charge;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.charge.dto.ChargeChannelProdDTO;

import java.util.List;

/**
 * 充值产品业务处理接口
 *
 * @class: ChargeProdService.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
public interface ChargeProdService {

    /**
     * 查询所有充值的产品
     *
     * @param channelType
     * @param os
     * @return
     * @author: chenjunsheng
     * @date 2018/6/7
     */
    List<ChargeChannelProdDTO> listChargeProd(Integer channelType, String os, String app, String appVersion) throws WebServiceException;
}
