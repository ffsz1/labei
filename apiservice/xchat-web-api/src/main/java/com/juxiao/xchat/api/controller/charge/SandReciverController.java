package com.juxiao.xchat.api.controller.charge;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.manager.external.sand.conf.SandpayConf;
import com.juxiao.xchat.manager.external.sand.utils.CertUtil;
import com.juxiao.xchat.manager.external.sand.utils.CryptoUtil;
import com.juxiao.xchat.manager.external.sand.vo.SandpayReciver;
import com.juxiao.xchat.service.api.charge.ChargeService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/sand")
@Slf4j
public class SandReciverController {
    @Autowired
    private Gson gson;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private SandpayConf sandpayConf;

    @ApiOperation(value = "衫德支付回调", hidden = true)
    @RequestMapping(value = "/callback", method = {RequestMethod.POST, RequestMethod.GET})
    public String sandCallBack(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String data = req.getParameter("data");
        String sign = req.getParameter("sign");
        log.info("接收到衫德通知数据：{}", data);
        log.info("接收到衫德通知签名：{}", sign);

        // 验证签名
        boolean valid;

        // 衫德demo的支付初始化是在预下单的时候初始化秘钥信息 负载均衡的情况下A 初始化了秘钥 B没初始化 导致CertUtil.getPublicKey() 为null

        // 获取密钥配置
        String publicKeyPath = sandpayConf.getPublicKey();
        String privateKeyPath = sandpayConf.getPrivateKey();
        String keyPassword = sandpayConf.getPrivateKeyPassword();

        // 初始化密钥信息
        CertUtil.init(publicKeyPath, privateKeyPath, keyPassword);

        valid = CryptoUtil.verifyDigitalSign(data.getBytes("utf-8"), Base64.decodeBase64(sign),
                CertUtil.getPublicKey(), "SHA1WithRSA");
        if (!valid) {
            log.error("[衫德支付回调]验签失败, data:{} , sign:{}", data, sign);
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        } else {
            JSONObject dataJson = JSONObject.parseObject(data);
            if (dataJson == null
                    || dataJson.getJSONObject("head") == null
                    || dataJson.getJSONObject("head").getString("respCode") == null
                    || !dataJson.getJSONObject("head").getString("respCode").equals("000000")
                    || dataJson.getJSONObject("body") == null
            ) {
                log.error("[衫德支付回调]失败, data:{} , sign:{}", data, sign);
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }

            SandpayReciver sandpayReciver = gson.fromJson(dataJson.getJSONObject("body").toString(), SandpayReciver.class);
            chargeService.reciveSandCharge(sandpayReciver);
            JSONObject ret = new JSONObject();
            ret.put("respCode", "000000");
            return ret.toJSONString();
        }
    }
}
