package com.juxiao.xchat.service.api.charge;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.manager.external.weixin.bo.WeixinReceiverBO;
import com.juxiao.xchat.manager.external.weixin.vo.WeixinReturnCodeVO;
import com.juxiao.xchat.manager.external.weixin.vo.WxappRequestPaymentVO;
import com.juxiao.xchat.service.api.charge.vo.WeixinUnifiedOrderVO;

/**
 * 处理微信公众号充值
 *
 * @class: WeixinChargeService.java
 * @author: chenjunsheng
 * @date 2018/6/12
 */
public interface WeixinChargeService {

    /**
     * 微信充值订单号生成
     *
     * @param erban_no
     * @param phone
     * @param chargeProdId
     * @param openId
     * @return
     */
    WeixinUnifiedOrderVO createPaySign(String ip, Long erban_no, String phone, String chargeProdId, String openId) throws Exception;

    /**
     * @param uid
     * @param openId
     * @param chargeProdId
     * @param ip
     * @return
     * @throws WebServiceException
     */
    WxappRequestPaymentVO createWxappPaySign(Long uid, String openId, String chargeProdId, String ip) throws Exception;

    /**
     * 微信支付回调充值
     *
     * @param receiverBo
     * @return
     */
    WeixinReturnCodeVO receive(WeixinReceiverBO receiverBo) throws WebServiceException;
}
