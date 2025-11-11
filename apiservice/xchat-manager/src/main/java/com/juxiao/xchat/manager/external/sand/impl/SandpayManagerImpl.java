package com.juxiao.xchat.manager.external.sand.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.utils.DateFormatUtils;
import com.juxiao.xchat.base.utils.HttpRequester;
import com.juxiao.xchat.base.utils.HttpRespons;
import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.manager.external.joinpay.ret.JoinpayRet;
import com.juxiao.xchat.manager.external.sand.SandpayManager;
import com.juxiao.xchat.manager.external.sand.conf.SandpayConf;
import com.juxiao.xchat.manager.external.sand.utils.CertUtil;
import com.juxiao.xchat.manager.external.sand.utils.CryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SandpayManagerImpl implements SandpayManager {

    private static final String PRODUCT_CREATEORDER_URL = "https://cashier.sandpay.com.cn/gw/web/order/create";
    private static final String TEST_CREATEORDER_URL = "http:// :8003/gw/web/order/create";

    @Autowired
    private SandpayConf sandpayConf;

    @Override
    public String createCharge(String chargeRecordId, int amount, String subject, String clientIp, Long uid) throws Exception {
        // 参考文档：https://open.sandpay.com.cn/product/detail/43310/43772/43773

        // 组后台报文 公共请求报文
        JSONObject head = new JSONObject();
        head.put("version", "1.0"); // 版本号
        head.put("method", "sandpay.trade.orderCreate");
        head.put("productId", "00002000");
        head.put("accessType", "1");
        head.put("mid", sandpayConf.getMerchMid());
//        head.put("plMid", map.get("plMid"));
        head.put("channelType", "07");
        head.put("reqTime", DateFormatUtils.YYYYMMDDHHMMSS.date2Str(new Date()));

        JSONObject bodyInBody = new JSONObject();
        bodyInBody.put("mallOrderCode",chargeRecordId); // 商城订单号
        bodyInBody.put("receiveAddress",chargeRecordId); // 收货地址
        bodyInBody.put("goodsDesc",chargeRecordId); // 商品描述

        JSONObject body = new JSONObject();
        body.put("orderCode", chargeRecordId);
//        body.put("userId", map.get("userId"));
        body.put("totalAmount", String.format("%012d", amount));// 订单金额
        body.put("subject", subject); // 订单标题
        body.put("body",bodyInBody);
//        body.put("txnTimeOut", map.get("txnTimeOut")); // 订单超时时间
//        body.put("payModeList", map.get("payModeList")); // 支付方式列表
        body.put("notifyUrl", sandpayConf.getGateWayBacknoticeUrl());// 异步通知地址 需报备

        body.put("frontUrl", sandpayConf.getGateWayFrontUrl()); // 必填
//        body.put("storeId", map.get("storeId"));  // 商户门店编号
//        body.put("terminalId", map.get("terminalId")); // 商户终端编号
//        body.put("operatorId", map.get("operatorId")); // 操作员编号
//        body.put("clearCycle", map.get("clearCycle")); // 清算模式
//        body.put("accountingMode", map.get("accountingMode")); // 入账模式
//        body.put("riskRateInfo", map.get("riskRateInfo")); // 风控信息域
//        body.put("bizExtendParams", map.get("bizExtendParams")); // 业务扩展参数
//        body.put("merchExtendParams", map.get("merchExtendParams")); // 商户扩展参数
//        body.put("extend", map.get("extend")); // 扩展域
        JSONObject data = new JSONObject();
        data.put("head", head);
        data.put("body", body);
        try {
            // 报文签名
            String reqSign = digitalSign(data.toJSONString());

            JSONObject respJson = new JSONObject();
            respJson.put("data", JSON.toJSONString(data));
            respJson.put("sign", reqSign);
            log.info("respJson :>{}", respJson);

            return respJson.toJSONString();
        } catch (Exception e) {
            log.error("CashierPayServlet post error <<<", e);
        }

        return null;
    }

    /**
     * 签名方法
     *
     * @param data 待签名数据
     * @return
     * @throws Exception
     */
    public String digitalSign(String data) throws Exception {
        if (null == data) {
            return null;
        } else {
            try {
                // 获取密钥配置
                String publicKeyPath = sandpayConf.getPublicKey();
                String privateKeyPath = sandpayConf.getPrivateKey();
                String keyPassword = sandpayConf.getPrivateKeyPassword();

                // 初始化密钥信息
                CertUtil.init(publicKeyPath, privateKeyPath, keyPassword);
                byte[] dataBytes = data.getBytes("UTF-8");
                // 签名
                String signData = new String(Base64.encodeBase64(CryptoUtil.digitalSign(dataBytes, CertUtil.getPrivateKey(), "SHA1WithRSA")), "UTF-8");
                log.info("digitalSign(String) =>>sign:{}", signData);
                return URLEncoder.encode(signData, "UTF-8");
            } catch (Exception var6) {
                log.error("digitalSign(String, String)", var6);
                throw new Exception("签名异常", var6);
            }
        }
    }
}
