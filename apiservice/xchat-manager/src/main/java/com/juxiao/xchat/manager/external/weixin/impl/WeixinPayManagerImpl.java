package com.juxiao.xchat.manager.external.weixin.impl;

import com.juxiao.xchat.base.utils.HttpUtils;
import com.juxiao.xchat.base.utils.MD5Utils;
import com.juxiao.xchat.base.utils.PojoUtils;
import com.juxiao.xchat.base.utils.XmlUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.manager.external.weixin.WeixinPayManager;
import com.juxiao.xchat.manager.external.weixin.bo.WeixinReceiverBO;
import com.juxiao.xchat.manager.external.weixin.conf.WeixinConf;
import com.juxiao.xchat.manager.external.weixin.req.WeixinSignReqBean;
import com.juxiao.xchat.manager.external.weixin.ret.UnifiedOrderRet;
import com.juxiao.xchat.manager.external.weixin.vo.UnifiedOrderVO;
import com.juxiao.xchat.manager.external.weixin.vo.WeixinReturnCodeVO;
import com.juxiao.xchat.manager.external.weixin.vo.WxappRequestPaymentVO;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @class: WeixinPayManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/12
 */
@Service
public class WeixinPayManagerImpl implements WeixinPayManager {
    private static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    private final String SUCCESS = "SUCCESS";
    private final Logger logger = LoggerFactory.getLogger(WeixinPayManager.class);

    @Autowired
    private WeixinConf weixinConf;

    @Override
    public UnifiedOrderVO submitWxpubPay(String ip, String outTradeNo, String openid, String goodName, Integer money) throws Exception {
//        WeixinSignReqBean reqBean = new WeixinSignReqBean();
//        reqBean.setAppid(weixinConf.getAppid());
//        reqBean.setMch_id(weixinConf.getMchid());
//        reqBean.setNonce_str(RandomStringUtils.randomAlphabetic(32));
//        reqBean.setBody(goodName);
//        reqBean.setDevice_info(deviceinfo);
//        reqBean.setOut_trade_no(outTradeNo);
//        reqBean.setTotal_fee(money);
//        reqBean.setSpbill_create_ip(ip);
//        reqBean.setNotify_url(weixinConf.getNotifyUrl());
//        reqBean.setTrade_type(tradeType);
//        reqBean.setOpenid(openid);
//        reqBean.sign(weixinConf.getPaykey());
//
//        String data = reqBean.toXml();
//        String result = HttpUtils.post(UNIFIED_ORDER_URL, data);
//        if (StringUtils.isBlank(result)) {
//            logger.warn("[ 微信支付{} ] 请求:>{}，请求网络异常。", outTradeNo, data.replaceAll("\n", ""));
//            throw new WebServiceException("微信接口请求出错。");
//        }
//
//        UnifiedOrderRet orderRet = XmlUtils.fromXml(result, UnifiedOrderRet.class);
//        logger.info("[ 微信支付{} ] 请求:>{}, 响应:{}", outTradeNo, data.replaceAll("\n", ""), result);
//        if (!SUCCESS.equals(orderRet.getReturn_code())) {
//            throw new WebServiceException("微信接口响应错误：" + orderRet.getReturn_code());
//        }
//
//        if (!orderRet.validateSign(weixinConf.getPaykey())) {
//            logger.info("[ 微信支付{} ] 验证返回内容签名错误。", outTradeNo);
//            throw new WebServiceException("验证信息错误。");
//        }
        UnifiedOrderRet orderRet = this.submitUnifiedorder(weixinConf.getAppid(), weixinConf.getMchid(), weixinConf.getPaykey(), ip, outTradeNo, openid, goodName, money, "JSAPI", "WEB");
        Map<String, Object> payToken = new HashMap<>();
        payToken.put("appId", weixinConf.getAppid());
        payToken.put("timeStamp", System.currentTimeMillis() / 1000);
        payToken.put("nonceStr", orderRet.getNonce_str());
        payToken.put("package", "prepay_id=" + orderRet.getPrepay_id());
        payToken.put("signType", "MD5");

        String preSign = PojoUtils.keyValuePair(payToken) + "&key=" + weixinConf.getPaykey();
        String paySign = MD5Utils.encode(preSign);
        UnifiedOrderVO orderVo = new UnifiedOrderVO();
//        orderVo.setAppid(weixinConf.getAppid());
        orderVo.setSign_type(String.valueOf(payToken.get("signType")));
        orderVo.setNonce_str(orderRet.getNonce_str());
        orderVo.setPrepay_id(String.valueOf(payToken.get("package")));
        orderVo.setTimestamp(String.valueOf(payToken.get("timeStamp")));
        orderVo.setSign(paySign.toUpperCase());
        return orderVo;
    }

    @Override
    public WxappRequestPaymentVO submitWxappPay(String ip, String outTradeNo, String openid, String goodName, Integer money) throws Exception {
        UnifiedOrderRet orderRet = this.submitUnifiedorder(weixinConf.getWxappId(), weixinConf.getMchid(), weixinConf.getPaykey(), ip, outTradeNo, openid, goodName, money, "JSAPI", null);
        WxappRequestPaymentVO payToken = new WxappRequestPaymentVO();

        Map<String, Object> map = new HashMap<>();
        map.put("appId", weixinConf.getWxappId());
        map.put("timeStamp", payToken.getTimeStamp());
        map.put("nonceStr", orderRet.getNonce_str());
        map.put("package", "prepay_id=" + orderRet.getPrepay_id());
        map.put("signType", payToken.getSignType());

        String preSign = PojoUtils.keyValuePair(map) + "&key=" + weixinConf.getPaykey();
        String paySign = MD5Utils.encode(preSign);
        logger.info("[ 微信支付 ] 加密前:>{}，加密:>{}", preSign, paySign);

        payToken.setPrepayId(orderRet.getPrepay_id());
        payToken.setNonceStr(orderRet.getNonce_str());
        payToken.setPaySign(paySign.toUpperCase());
        return payToken;
    }


    private UnifiedOrderRet submitUnifiedorder(String appId, String mchid, String paykey, String ip, String outTradeNo, String openid, String goodName, Integer money, String tradeType, String deviceinfo) throws Exception {
        WeixinSignReqBean reqBean = new WeixinSignReqBean();
        reqBean.setAppid(appId);
        reqBean.setMch_id(mchid);
        reqBean.setNonce_str(RandomStringUtils.randomAlphabetic(32));
        reqBean.setBody(goodName);
        if (StringUtils.isNotBlank(deviceinfo)) {
            reqBean.setDevice_info(deviceinfo);
        }
        reqBean.setOut_trade_no(outTradeNo);
        reqBean.setTotal_fee(money);
        reqBean.setSpbill_create_ip(ip);
        reqBean.setNotify_url(weixinConf.getNotifyUrl());
        reqBean.setTrade_type(tradeType);
        reqBean.setOpenid(openid);
        reqBean.sign(paykey);

        String data = reqBean.toXml();
        String result = HttpUtils.post(UNIFIED_ORDER_URL, data);
        if (StringUtils.isBlank(result)) {
            logger.warn("[ 微信支付{} ] 请求:>{}，请求网络异常。", outTradeNo, data.replaceAll("\n", ""));
            throw new WebServiceException(WebServiceCode.CHARGE_WX_REQUEST);
        }

        logger.info("[ 微信支付{} ] 请求:>{}, 响应:{}", outTradeNo, data.replaceAll("\n", ""), result);
        UnifiedOrderRet orderRet = XmlUtils.fromXml(result, UnifiedOrderRet.class);
        if (!SUCCESS.equals(orderRet.getReturn_code())) {
            throw new WebServiceException(orderRet.getReturn_code() + "-" + orderRet.getErr_code_des());
        }

        if (!orderRet.validateSign(paykey)) {
            logger.info("[ 微信支付{} ] 验证返回内容签名错误。", outTradeNo);
            throw new WebServiceException(WebServiceCode.CHARGE_VALIDATE_ERROR);
        }
        return orderRet;
    }

    /**
     * 微信回调
     *
     * @param receiverBo
     * @return
     */
    @Override
    public WeixinReturnCodeVO receive(WeixinReceiverBO receiverBo) {
        if (StringUtils.isEmpty(receiverBo.getOut_trade_no())) {
            return new WeixinReturnCodeVO(SUCCESS, "OK");
        }

        String preSign = PojoUtils.keyValuePair(receiverBo, "sign") + "&key=" + weixinConf.getPaykey();
        String sign = MD5Utils.encode(preSign);

        if (StringUtils.isEmpty(sign) || !sign.equalsIgnoreCase(receiverBo.getSign())) {
            logger.info("[ 微信支付 {} ] 验证签名错误，本地生成签名:>{},接收签名:>{}", receiverBo.getOut_trade_no(), sign, receiverBo.getSign());
            return new WeixinReturnCodeVO("FAIL", "sign validate error!");
        }

        return new WeixinReturnCodeVO(SUCCESS, "OK");
    }

    public static void main(String[] args) {
        String result = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg><appid><![CDATA[wxc5e7b2a205845e4b]]></appid><mch_id><![CDATA[1507655831]]></mch_id><nonce_str><![CDATA[l50exprIp1psOfC4]]></nonce_str><sign><![CDATA[21065D7EEB59B34A87E5A4D8D4A06792]]></sign><result_code><![CDATA[SUCCESS]]></result_code><prepay_id><![CDATA[wx111654031165343113be98f02168747526]]></prepay_id><trade_type><![CDATA[JSAPI]]></trade_type></xml>";
        UnifiedOrderRet orderRet = XmlUtils.fromXml(result, UnifiedOrderRet.class);
        System.out.println(orderRet);
    }
}
