package com.juxiao.xchat.manager.external.pingxx.impl;

import com.google.common.collect.Maps;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.charge.enumeration.ChargePayChannel;
import com.juxiao.xchat.manager.external.pingxx.PingxxManager;
import com.juxiao.xchat.manager.external.pingxx.conf.PingxxConf;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import io.netty.channel.ChannelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * ping++接口服务类
 *
 * @class: PingxxManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
@Service
public class PingxxManagerImpl implements PingxxManager {
    private final Logger logger = LoggerFactory.getLogger(PingxxManager.class);
    @Autowired
    private PingxxConf pingxxConf;

    @PostConstruct
    private void init() {
        Pingpp.apiKey = pingxxConf.getApiKey();
        Pingpp.privateKeyPath = pingxxConf.getPrivateKeyPath();
    }

    /**
     * @see com.juxiao.xchat.manager.external.pingxx.PingxxManager#createCharge(String, ChargePayChannel, int, String, String, String, String)
     */
    @Override
    public Charge createCharge(String chargeRecordId, ChargePayChannel payChannel, int amount, String subject, String body, String clientIp, String successUrl) throws RateLimitException, APIException, ChannelException, InvalidRequestException, APIConnectionException, AuthenticationException, com.pingplusplus.exception.ChannelException {
        // 发起ping++充值
        Map<String, Object> map = new HashMap<String, Object>();
        // 订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100）
        map.put("amount", amount);
        map.put("currency", "cny");
        // 商品的标题，该参数最长为 32 个 Unicode 字符，银联全渠道（upacp / upacp_wap ）限制在 32 个字节。
        map.put("subject", subject);
        // 商品的描述信息，该参数最长为 128 个 Unicode
        map.put("body", body);
        // 字符，yeepay_wap 对于该参数长度限制为 100 个 Unicode字符。
        // 推荐使用 8-20 位，要求数字或字母，不允许其他字符
        map.put("order_no", chargeRecordId);
        // 支付使用的第三方支付渠道取值，请参考：https://www.pingxx.com/api#api-c-new
        map.put("channel", payChannel.name());
        // 发起支付请求客户端的 IP 地址，格式为 IPV4，如:
        map.put("client_ip", clientIp);
        map.put("extra", payChannel.extra(successUrl));

        Map<String, String> app = new HashMap<String, String>();
        app.put("id", pingxxConf.getAppId());
        map.put("app", app);
        return Charge.create(map);
    }

    @Override
    public Charge retrieve(String id, String orderNo, boolean paid) throws Exception {
        Map<String, Object> params = Maps.newHashMap();
        Charge charge = Charge.retrieve(id, params);
        if (charge == null) {
            logger.error("[ ping++回调 ]retrieve charge not exist,charge id:>{},orderNo:>{}", id, orderNo);
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
        }

        if (!paid || !charge.getPaid()) {
            logger.error("[ ping++回调 ]paid is false,charge id:>{},orderNo:>{}", id, orderNo);
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
        }

        return charge;
    }
}
