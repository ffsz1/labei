package com.juxiao.xchat.manager.external.joinpay.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.juxiao.xchat.base.utils.HttpRequester;
import com.juxiao.xchat.base.utils.HttpRespons;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.manager.external.joinpay.JoinpayManager;
import com.juxiao.xchat.manager.external.joinpay.conf.JoinpayConf;
import com.juxiao.xchat.manager.external.joinpay.ret.JoinpayReciverRet;
import com.juxiao.xchat.manager.external.joinpay.ret.JoinpayRet;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class JoinpayManagerImpl implements JoinpayManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    final String key = "f0b782db976b424eab8d7c9b2868c038";
    final String uniPayApi = "https://www.joinpay.com/trade/uniPayApi.action";
    final String queryOrder = "https://www.joinpay.com/trade/queryOrder.action";
    @Autowired
    private Gson gson;
    @Autowired
    private JoinpayConf joinpayConf;

    @Override
    public JoinpayRet createCharge(String chargeRecordId, int amount, String subject, String body, String clientIp, String successUrl, String payChannel, Long uid,String openId) throws Exception {
        // 发起汇聚充值
        Map<String, String> map = Maps.newHashMap();
        map.put("p0_Version", "1.0");/** 版本号 */
        map.put("p1_MerchantNo", joinpayConf.getMerchantNo());/** 商户编号 */

        map.put("p2_OrderNo", chargeRecordId); /**商户订单号*/
        map.put("p3_Amount", amount+"");/**订单金额*/
        map.put("p4_Cur", "1"); /**交易币种 */
        map.put("p5_ProductName", subject); /** 商品名称 */
//        map.put("p8_ReturnUrl", successUrl); /** 商户页面通知地址 */
        map.put("p9_NotifyUrl", joinpayConf.getNotifyUrl()); /** 服务器异步通知地址 */
        map.put("q1_FrpCode", payChannel); /** 交易类型*/
        if ("WEIXIN_APP".equalsIgnoreCase(payChannel)) {
            map.put("q7_AppId", joinpayConf.getWxAppId()); /** APPID*/
            map.put("qa_TradeMerchantNo", joinpayConf.getTradeMerchantNo());
        }else if("WEIXIN_GZH".equalsIgnoreCase(payChannel)){
            map.put("q7_AppId", joinpayConf.getGzhAppId()); /** APPID*/
            map.put("q5_OpenId",openId);
            map.put("qa_TradeMerchantNo", joinpayConf.getTradeMerchantNo());
        }else if("WEIXIN_APP3".equalsIgnoreCase(payChannel)){
            map.put("q7_AppId", joinpayConf.getWxAppId()); /** APPID*/
            map.put("qa_TradeMerchantNo", joinpayConf.getTradeMerchantNoAPP3());
        }

        map.put("hmac", getSign(createLinkStringByGet(map)));/** 签名数据 */
        logger.info("请求汇聚参数:{}" , gson.toJson(map));

        String content = postJoinpay(uniPayApi, map);
        logger.info("汇聚返回参数:{}" , content);
        JoinpayRet joinpayRet = gson.fromJson(content, JoinpayRet.class);
        if (!"100".equals(joinpayRet.getRa_Code())) {
            throw new WebServiceException("创建订单失败："+joinpayRet.getRb_CodeMsg());
        }
        return joinpayRet;
    }

    @Override
    public JoinpayReciverRet retrieve(String merchantNo, String orderNo, String hmac) throws Exception {
        Map<String, String> map = Maps.newHashMap();
        map.put("p1_MerchantNo", joinpayConf.getMerchantNo());/** 商户编号 */
        map.put("p2_OrderNo", orderNo); /**商户订单号*/
        map.put("hmac", getSign(createLinkStringByGet(map)));/** 签名数据 */
        String content = postJoinpay(queryOrder, map);
        JoinpayReciverRet joinpayRet = gson.fromJson(content, JoinpayReciverRet.class);
        if (joinpayRet == null) {
            logger.error("[ 汇聚回调 ]retrieve charge not exist,charge merchantNo:>{},orderNo:>{}", merchantNo, orderNo);
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
        }

        if (!"100".equalsIgnoreCase(joinpayRet.getRb_Code())) {
            logger.error("[ 汇聚回调 ]charge is false,charge merchantNo:>{},orderNo:>{}", merchantNo, orderNo);
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
        }
        return joinpayRet;
    }

    private String createLinkStringByGet(Map<String, String> params){
        List<String> keys = Lists.newArrayList(params.keySet());
        Collections.sort(keys);
        String str1 ="";
        for(int i=0;i<keys.size();i++) {
            String key = keys.get(i);
            //(String) 强制类型转换
            Object value = params.get(key);
            if(value instanceof Integer) {
                value = (Integer)value;
            }
            if(i==keys.size()-1) {
                str1 = str1+value.toString();
            }else {
                str1 = str1+value;
            }
        }
        return str1;
    }

    private String getSign(String str) {
        return signByMD5(str, key);
    }

    private String signByMD5(String requestSign, String merchantKey) {
        return DigestUtils.md5Hex(requestSign + merchantKey).toUpperCase();
    }

    private String postJoinpay(String url, Map<String, String> map) throws Exception {
        // post请求参数内容
        HttpRequester hr = new HttpRequester();
        HttpRespons respons = hr.sendPost(url, map);
        logger.info("url:{},接收汇聚返回参数：{}", url, respons.getContent());
        if (!nosign(respons.getContent(), key)) {
            logger.error("创建订单失败：验签失败");
            throw new WebServiceException("创建订单失败：验签失败");
        }
        return respons.getContent();
    }

    private boolean nosign(String hp, String key) {
        JSONObject myJson = JSONObject.parseObject(hp);
        Map m = myJson;
        // 返回hmac
        String returnHmac = (String) m.remove("hmac");
        return signByMD5(createLinkStringByGet(m), key).equals(returnHmac);
    }

}
