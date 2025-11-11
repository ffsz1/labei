package com.juxiao.xchat.manager.external.pingxx;

import com.juxiao.xchat.dao.charge.enumeration.ChargePayChannel;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import io.netty.channel.ChannelException;

/**
 * ping++服务
 *
 * @class: PingxxManager.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
public interface PingxxManager {
    /***
     * 创建ping++充值订单
     * @param chargeRecordId
     * @param payChannel
     * @param amount
     * @param subject
     * @param body
     * @param clientIp
     * @param successUrl
     * @return
     * @throws RateLimitException
     * @throws APIException
     * @throws ChannelException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws AuthenticationException
     */
    Charge createCharge(String chargeRecordId, ChargePayChannel payChannel, int amount, String subject, String body, String clientIp, String successUrl) throws RateLimitException, APIException, ChannelException, InvalidRequestException, APIConnectionException, AuthenticationException, com.pingplusplus.exception.ChannelException;


    /**
     * @param id
     * @return
     */
    Charge retrieve(String id, String orderNo, boolean paid) throws Exception;
}
