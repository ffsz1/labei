package com.juxiao.xchat.manager.external.weixin;

import com.juxiao.xchat.manager.external.weixin.bo.WeixinReceiverBO;
import com.juxiao.xchat.manager.external.weixin.vo.UnifiedOrderVO;
import com.juxiao.xchat.manager.external.weixin.vo.WeixinReturnCodeVO;
import com.juxiao.xchat.manager.external.weixin.vo.WxappRequestPaymentVO;

/**
 * 微信支付接入服务
 *
 * @class: WeixinPayManager.java
 * @author: chenjunsheng
 * @date 2018/6/12
 */
public interface WeixinPayManager {

    /**
     * @param ip
     * @param outTradeNo
     * @param openid
     * @param goodName
     * @param money
     * @return
     */
    UnifiedOrderVO submitWxpubPay(String ip, String outTradeNo, String openid, String goodName, Integer money) throws Exception;

    /**
     * 小程序下单
     *
     * @param ip
     * @param outTradeNo
     * @param openid
     * @param goodName
     * @param money
     * @return
     * @throws Exception
     */
    WxappRequestPaymentVO submitWxappPay(String ip, String outTradeNo, String openid, String goodName, Integer money) throws Exception;

    /**
     * 微信回调
     *
     * @param receiverBo
     * @return
     */
    WeixinReturnCodeVO receive(WeixinReceiverBO receiverBo);
}
