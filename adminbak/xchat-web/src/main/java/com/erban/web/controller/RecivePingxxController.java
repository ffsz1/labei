package com.erban.web.controller;

import com.beust.jcommander.internal.Maps;
import com.erban.main.param.recivepingxx.PingxxParamData;
import com.erban.main.service.ChargeService;
import com.erban.web.common.BaseController;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.pingplusplus.model.Charge;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuguofu on 2017/6/27.
 */
@Controller
@RequestMapping("pingxx")
public class RecivePingxxController extends BaseController {
    public static final Logger logger = LoggerFactory.getLogger(RecivePingxxController.class);
    private static String pubKeyString="-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApXyJ7+ilEKOuODYHesqPYP" +
            "CyxAmn/eXf+DKI3bc8W7VD7zSLXKPcLu7iCqbjrELptX9hX6eUSaDRRea+QweP2Jv2rI8p7V5VOTJGSdwJn9M9dqQkUki1MyF/DcGWYARIQsIkmQ93OUHeG" +
            "ayTzwdwHDYsqZFlr1PpmS5AZkxvgT7upIHo5Lw6Fspf5IHJ3NFcOll35E2d2WusT/kwgBHHJ40r5S+8bamZWIM77CQ9c1ZG1LmceVHWLWn7rZUKs6EBQlAZ" +
            "uuEudTUJyt7sxlUqIw61/QrT4AA/3HEkrTnfKKxb1FYye8nHaIbZgEWajrbG+JhorNvwUNb76a7MKePDGwIDAQAB-----END PUBLIC KEY-----";
    private Gson gson=new Gson();
    @Autowired
    private ChargeService chargeService;

    /**
     * 支付回调
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "callback", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String,Object> pingxxCallBack(HttpServletRequest request)throws Exception {
        logger.info("---------------pingxxCallBack begin-------------------");
        Map<String,Object> result = Maps.newHashMap();
        try {
            // 获取请求体
            byte[] body = readBody(request);
            if (body == null) {
                logger.error("支付回调,入参为null");
                throw  new RuntimeException("支付回调,入参为null");
            }
//            Enumeration enum1 = request.getHeaderNames();
//
//            while(enum1.hasMoreElements()){
//                String key = (String)enum1.nextElement();
//                logger.info(key + ":" + key);
//                String value = request.getHeader(key);
//                logger.info(key + ":" + value);
//            }

//            String signature=request.getHeader("x-pingplusplus-signature");
            String requestBody = new String(body, "utf-8");
            boolean verifyResult =true;
//            boolean verifyResult = verifyData(requestBody, signature, getPubKey());
            if(verifyResult){
                Map<String,Object> requstMap=gson.fromJson(requestBody,HashMap.class);
                LinkedTreeMap data=(LinkedTreeMap)requstMap.get("data");
                LinkedTreeMap dataMap=(LinkedTreeMap) data.get("object");
               chargeService.updateChargeData(getCharge(dataMap));
            }else{
                logger.info("pingxx校验不通过....");
                throw  new RuntimeException("支付回调,pingxx校验不通过");
            }
            // 将请求体转成String格式，并打印
            result.put("code", 200);
            result.put("message","支付回调成功");
            logger.info("---------------pingxxCallBack end-------------------");
            return result;
        } catch (Exception ex) {
            logger.error("支付回调异常，报错：{}", ex);
            throw new RuntimeException(ex);
        }
    }


    /**
     * 测试
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "callback2", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String,Object> pingxxCallBack2(HttpServletRequest request)
            throws Exception {
        logger.info("pingxxCallBack..................");
        Map<String,Object> result = Maps.newHashMap();
        try {

            boolean verifyResult =true;

            Charge charge=new Charge();
            charge.setPaid(true);
            charge.setOrderNo("ad04d49811564dce897f9443884ebbc3");
            chargeService.updateChargeData(charge);

            result.put("code", 400);
            return result;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            result.put("code", 444);
            return result;
        }
    }

    private Charge getCharge(LinkedTreeMap dataMap){
        Charge charge=new Charge();
        String id=(String)dataMap.get("id");
        String object=(String)dataMap.get("object");
        boolean livemode=dataMap.get("livemode")==null?false:(boolean)dataMap.get("livemode");
        boolean paid=dataMap.get("paid")==null?false:(boolean) dataMap.get("paid");
        boolean refunded=dataMap.get("refunded")==null?false:(boolean)dataMap.get("refunded");
        boolean reversed=dataMap.get("reversed")==null?false:(boolean) dataMap.get("reversed");
        String app=(String)dataMap.get("app");
        String channel=(String)dataMap.get("channel");
        String order_no=(String)dataMap.get("order_no");
        String client_ip=(String)dataMap.get("client_ip");
        Double amount=(Double)dataMap.getOrDefault("amount", 0.0);
        Double amount_settle=(Double)dataMap.getOrDefault("amount_settle", 0.0);
        String currency=(String)dataMap.get("currency");
        String subject=(String)dataMap.get("subject");
        String body=(String)dataMap.get("body");
        Double time_paid=(Double)dataMap.getOrDefault("time_paid",0.0);
        Double time_expire=(Double)dataMap.getOrDefault("time_expire",0.0);
        charge.setPaid(paid);
        charge.setId(id);
        charge.setObject(object);
        charge.setPaid(paid);
        charge.setLivemode(livemode);
        charge.setRefunded(refunded);
        charge.setReversed(reversed);
        charge.setApp(app);
        charge.setChannel(channel);
        charge.setOrderNo(order_no);
        charge.setClientIp(client_ip);
        charge.setAmount(amount.intValue());
        charge.setAmountSettle(amount_settle.intValue());
        charge.setCurrency(currency);
        charge.setSubject(subject);
        charge.setBody(body);
        charge.setTimePaid(time_paid.longValue());
        charge.setTimeExpire(time_expire.longValue());
        return charge;
    }

    /**
     * 获得公钥
     * @return
     * @throws Exception
     */
    private static PublicKey getPubKey() throws Exception {
        logger.info("pubKeyString="+pubKeyString);
        pubKeyString = pubKeyString.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "");
        byte[] keyBytes = Base64.decodeBase64(pubKeyString);

        // generate public key
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(spec);
        return publicKey;
    }

    /**
     * 验证签名
     * @param dataString
     * @param signatureString
     * @param publicKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    private static boolean verifyData(String dataString, String signatureString, PublicKey publicKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        byte[] signatureBytes = Base64.decodeBase64(signatureString);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(dataString.getBytes("UTF-8"));
        return signature.verify(signatureBytes);
    }

    public static void main(String args[]){
        String str="{\"id\":\"evt_400170707144522227491903\",\"created\":1499409921,\"livemode\":false,\"type\":\"charge.succeeded\",\"data\":{\"object\":{\"id\":\"ch_D480SS1qD4aTWLCKqH9mn1K4\",\"object\":\"charge\",\"created\":1499409913,\"livemode\":false,\"paid\":true,\"refunded\":false,\"reversed\":false,\"app\":\"app_KaPO844ej1G8T40a\",\"channel\":\"alipay\",\"order_no\":\"7b53855ad08b48a8a2ad218924dcc531\",\"client_ip\":\"192.168.0.100\",\"amount\":600,\"amount_settle\":600,\"currency\":\"cny\",\"subject\":\"600金币金币充值\",\"body\":\"600金币金币充值\",\"extra\":{\"rn_check\":\"T\",\"buyer_account\":\"alipay_account\"},\"time_paid\":1499409921,\"time_expire\":1499496313,\"time_settle\":null,\"transaction_no\":\"2017070758155903\",\"refunds\":{\"object\":\"list\",\"url\":\"/v1/charges/ch_D480SS1qD4aTWLCKqH9mn1K4/refunds\",\"has_more\":false,\"data\":[]},\"amount_refunded\":0,\"failure_code\":null,\"failure_msg\":null,\"metadata\":{},\"credential\":{},\"description\":null}},\"object\":\"event\",\"request\":\"iar_DOu9CCuXHqfTS8GGiPqvT088\",\"pending_webhooks\":0}";
        Gson gson=new Gson();
        PingxxParamData requstMap=gson.fromJson(str,PingxxParamData.class);
        System.out.println("");
    }


}
