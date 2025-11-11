package com.juxiao.xchat.service.api.charge.impl;

import com.juxiao.xchat.dao.charge.dto.ChargeChannelProdDTO;
import com.juxiao.xchat.manager.common.charge.ChargeProdManager;
import com.juxiao.xchat.manager.common.constant.AppClient;
import com.juxiao.xchat.service.api.charge.ChargeProdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 充值产品业务处理接口
 *
 * @class: ChargeProdServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
@Service
public class ChargeProdServiceImpl implements ChargeProdService {
    @Autowired
    private ChargeProdManager chargeProdManager;

    /**
     * @see com.juxiao.xchat.service.api.charge.ChargeProdService#listChargeProd(Integer, String, String, String)
     */
    @Override
    public List<ChargeChannelProdDTO> listChargeProd(Integer channelType, String os, String app, String appVersion) {
        //判断是否是iOS   审核:全部列表;非审核:显示一个支付方式。)
        if ("iOS".equalsIgnoreCase(os) && channelType == 1) {
            List<ChargeChannelProdDTO> list = chargeProdManager.findChargeProd(5, appVersion);
            if (list.isEmpty()) {
                return list;
            }
            ChargeChannelProdDTO chargeProdDto = list.get(0);
            chargeProdDto.setProdDesc("请点击底部优惠充值");
            return list;
        }

        return chargeProdManager.findChargeProd(channelType, appVersion);
    }

}
