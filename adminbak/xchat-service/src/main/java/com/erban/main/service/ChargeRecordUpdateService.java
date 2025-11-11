package com.erban.main.service;

import com.alibaba.fastjson.JSON;
import com.erban.main.config.SystemConfig;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.ChargeAppleRecordMapper;
import com.erban.main.mybatismapper.ChargeRecordMapper;
import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.drawprize.UserDrawService;
import com.erban.main.service.duty.DutyService;
import com.erban.main.service.duty.DutyType;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.user.UserPurseService;
import com.erban.main.service.user.UserPurseUpdateService;
import com.erban.main.service.user.UserShareRecordService;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.ReceiptVo;
import com.erban.main.vo.RecepitVo;
import com.erban.main.vo.UserVo;
import com.google.gson.Gson;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Map;

@org.springframework.stereotype.Service
public class ChargeRecordUpdateService {

    private static final Logger logger = LoggerFactory.getLogger(ChargeRecordUpdateService.class);

    @Autowired
    private JedisService redisService;

    private static final String DEFAULT_COUNT_VALUE = "2";

    private static final String DEFAULT_SIZE = "1";

    private static final String tickets = "tiantian_uid_ticket";


    private String picUrl;

    private String webUrl;

    public String getPicUrl() {
        return picUrl;
    }


    /**
     * 重写X509TrustManager
     */
    private static TrustManager myX509TrustManager = new X509TrustManager() {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }
    };

    @Autowired
    private ChargeRecordMapper chargeRecordMapper;
    @Autowired
    private UserPurseService userPurseService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private BillRecordService billRecordService;
    @Autowired
    private ChargeProdService chargeProdService;
    @Autowired
    private UserPurseUpdateService userPurseUpdateService;
    @Autowired
    private ChargeAppleRecordMapper chargeAppleRecordMapper;
    @Autowired
    private ChargeService chargeService;
    @Autowired
    private UserShareRecordService userShareRecordService;
    @Autowired
    private UserDrawService userDrawService;

    @Autowired
    private SendSysMsgService sendSysMsgService;
    @Autowired
    private DutyService dutyService;


   /*     @Autowired
        private ChargeRecordUpdateService chargeRecordUpdateService;*/

    //订单更新,钱包更新
        /*
                1. 判断二次验证是否成功

         */
    @Transactional(rollbackFor = Exception.class)
    public BusiResult chargeRecordUpdate(String uid, String chargeRecordId, String receip, String receiptmd5) throws Exception {
        BusiResult recepitVoBusiResult = new BusiResult(BusiStatus.BUSIERROR);
        //解析apple服务器返回的信息
        if (receip == null && (!StringUtils.isNotEmpty(receip))) {
            logger.info("获取Apple服务器信息失败，用户付款失败，");
            return recepitVoBusiResult;
        }
        //判断用户是否存在
        //==============判断用户是否存在======================//
        UserVo userVo = usersService.getUserByUid(Long.parseLong(uid)).getData();
        ChargeRecord chargeRecord = getChargeRecordById(chargeRecordId);
        if (userVo == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
                                /*
                                进行订单更新,钱包更新
                                 */
        //=======================判断订单状态===================//
        if (chargeRecord == null || (chargeRecord.getUid() != (Long.parseLong(uid)))) {//  || !"com.yingtao.ios.iap.08".equals(chargeRecord.getChargeProdId())
            logger.info("订单不存在或者不属于该用户" + "\n用户付款失败，不做存储处理");
            return recepitVoBusiResult;
        }

        Byte chargeRecordStatus = chargeRecord.getChargeStatus();                     //读取订单状态
        if (chargeRecordStatus != null) {
            if (chargeRecordStatus == Constant.ChargeRecordStatus.error) {
                logger.info("订单错误！！！" + "\n用户付款失败，不做存储处理");
            } else if (chargeRecordStatus == Constant.ChargeRecordStatus.timeout) {
                logger.info("订单超时" + "\n用户付款失败，不做存储处理");

            } else if (chargeRecordStatus == Constant.ChargeRecordStatus.finish) {
                logger.info("订单已支付，禁止重复支付" + "\n用户付款失败，不做存储处理");
            } else if (chargeRecordStatus == Constant.ChargeRecordStatus.create) {

//                                JSONObject object = JSONObject.fromObject ( receip );
//                                Integer status = ( Integer ) object.get ( "status" );
                Gson gson = new Gson();
                ReceiptVo receiptVo = gson.fromJson(receip, ReceiptVo.class);
                //用户付款成功，进行存储操作
                //测试
                if (receiptVo.getStatus() == 0) {
                    // 把内购订单id保存到数据库
                    ChargeAppleRecord chargeAppleRecord = new ChargeAppleRecord();
                    chargeAppleRecord.setChargeRecordId(chargeRecordId);
                    chargeAppleRecord.setUid(Long.valueOf(uid));
                    chargeAppleRecord.setReceip(receiptmd5);
                    chargeAppleRecord.setCreateTime(new Date());
                    chargeAppleRecordMapper.insertSelective(chargeAppleRecord);

                    logger.info("返回码：" + receiptVo.getStatus());
                    //订单处于未支付状态
                    String proId = String.valueOf(chargeRecord.getAmount());           //充值的金额
                    //1. 充值金额的修改
                    //2. 修改订单状态
                    recepitVoBusiResult = updatePurse(uid, proId, chargeRecord, receip);
                    //TODO 执行图文推送操作
                    String count = redisService.get(RedisKey.user_apple_charge_record.getKey(uid));
                    if (count == null || count.isEmpty()) {
                        redisService.set(RedisKey.user_apple_charge_record.getKey(uid), DEFAULT_SIZE);
                    } else if (count.equals(DEFAULT_COUNT_VALUE)) {
                        redisService.set(RedisKey.user_send_message_record.getKey(uid), JSON.toJSONString(chargeAppleRecord));
                        String title = "小喵推荐";
                        String desc = "官方充值渠道";
                        String ticket = redisService.hget(tickets, uid);
                        String url = webUrl + uid + "&ticket=" + ticket;
                        sendSysMsgService.sendAppleMishu(uid, title, desc, picUrl, url);
                        BigDecimal num = new BigDecimal(DEFAULT_SIZE);
                        BigDecimal num2 = new BigDecimal(count);
                        BigDecimal result = num.add(num2);
                        redisService.set(RedisKey.user_apple_charge_record.getKey(uid), String.valueOf(result));
                        logger.info("发送推文成功!");
                    } else {
                        BigDecimal num = new BigDecimal(DEFAULT_SIZE);
                        BigDecimal num2 = new BigDecimal(count);
                        BigDecimal result = num.add(num2);
                        redisService.set(RedisKey.user_apple_charge_record.getKey(uid), String.valueOf(result));
                    }

                    try {
                        dutyService.updateDailyDuty(Long.valueOf(uid), DutyType.charge.getDutyId());
                    } catch (Exception e) {
                    }
                } else {
                    //二次验证失败，用户付款失败，不进行存储操作
                    logger.info("Apple服务器返回码：" + receiptVo.getStatus() + "\n用户付款失败，不做存储处理");
                }
            }
            return recepitVoBusiResult;
        } else {
            //订单状态获取失败
            logger.info("===================订单状态获取失败==================");
        }

                /*
                //二次验证失败，用户付款失败，不进行存储操作
                else {
                        logger.info ( "Apple服务器返回码：" + status + "\n用户付款失败，不做存储处理" );
                }*/
        return recepitVoBusiResult;

    }


    //钱包更新
        /*
                首充：只有第一次充值68才送100金币，第二次开始不赠送。（其他都不存在首充）
                正常充值：充值6 68都不赠送金币，
                        其余
                                198 -> 1400 + 80 = 1480
                                 488 -> 3400 + 280 = 3680
                                 998 -> 7100 + 580 = 7680
         */
    private BusiResult updatePurse(String uid, String proId, ChargeRecord chargeRecord, String receip) throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.BUSIERROR);
        boolean flag = userPurseService.getUserPurseFromDb(Long.parseLong(uid)).getIsFirstCharge();
        //进行钱包充值操作
        if (proId != null) {
            Long chargeGoldNum = 0L;
            Integer firstChargeGoldNum = null;
            switch (proId) {
                case "800":
                    chargeGoldNum = 50L;
                    firstChargeGoldNum = 20;
                    break;
                case "9800"://有首充
                    chargeGoldNum = 980L;
                    firstChargeGoldNum = 20;
//                                        flag = true;
                    break;
                case "19800":
                    chargeGoldNum = 1980L;
                    firstChargeGoldNum = 20;
                    break;
                case "99800":
                    chargeGoldNum = 9980L;
                    firstChargeGoldNum = 20;
                    break;
                default:
                    break;
            }
            try {
                //更改订单状态
                chargeRecord.setChargeStatus(Constant.ChargeRecordStatus.finish);
                updateChargeRecord(chargeRecord);
                userPurseService.updateGoldByCharge(Long.parseLong(uid), chargeGoldNum, firstChargeGoldNum);
                //写入流水账中
                if (flag) {
                    chargeGoldNum += firstChargeGoldNum;
                    // 首冲大礼包
                    chargeService.firstGiftBag(Long.parseLong(uid), chargeRecord.getAmount(), chargeRecord.getChargeRecordId());
                }
                billRecordService.insertBillRecord(
                        Long.parseLong(uid), Long.parseLong(uid), chargeRecord.getChargeRecordId(), Constant.BillType.charge, null, +chargeGoldNum, null);
                //充值更新操作
                busiResult.setCode(200);
                busiResult.setMessage("success");
                RecepitVo recepitVo = new RecepitVo();
                recepitVo.setData(receip);
                busiResult.setData(recepitVo);
            } catch (Exception e) {
                //钱包充值异常
                logger.debug("=======================钱包充值或订单修改异常==============");
                e.printStackTrace();
                return busiResult;
            }
            return busiResult;
        }
        logger.debug("=======================充值金额异常==============");
        return busiResult;

    }
    //钱包更新二（微信公众号版）
        /*
                首充：只有第一次充值8或者48才送10或者100金币，第二次开始不赠送。（其他都不存在首充）
                正常充值：充值1,8,48不赠送金币，
                        其余

                                 98 -> 980 + 50 = 1030
                                  198 -> 1980 + 150 = 2130
                                   498 -> 4980 + 400 =5380
                                    998 -> 9980 + 1000 = 10980

<xml>
  <appid><![CDATA[wx2421b1c4370ec43b]]></appid>
  <attach><![CDATA[支付测试]]></attach>
  <bank_type><![CDATA[CFT]]></bank_type>
  <fee_type><![CDATA[CNY]]></fee_type>
  <is_subscribe><![CDATA[Y]]></is_subscribe>
  <mch_id><![CDATA[10000100]]></mch_id>
  <nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str>
  <openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid>
  <out_trade_no><![CDATA[1409811653]]></out_trade_no>
  <result_code><![CDATA[SUCCESS]]></result_code>
  <return_code><![CDATA[SUCCESS]]></return_code>
  <sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign>
  <sub_mch_id><![CDATA[10000100]]></sub_mch_id>
  <time_end><![CDATA[20140903131540]]></time_end>
  <total_fee>1</total_fee>
  <trade_type><![CDATA[JSAPI]]></trade_type>
  <transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id>
</xml>


         */


    /**
     * @param map 微信支付通知集合
     * @return
     * @throws Exception
     */
    public BusiResult updatePurse(Map<String, String> map) throws Exception {
        //获取订单信息
        ChargeRecord chargeRecord = getChargeRecordById(map.get("out_trade_no"));
        //获取uid
        Long uid = chargeRecord.getUid();
        //获取订单类型
        String chargeRecordId = chargeRecord.getChargeRecordId();
        //3.判断该通知是否已经处理过
        if (chargeRecord.getChargeStatus() != Constant.ChargeRecordStatus.create) {
            logger.error("======订单号：" + chargeRecordId + "=======订单异常=======================");
            return new BusiResult(BusiStatus.BUSIERROR);
        }
//        logger.info("----------微信公众号支付：测试环境下不用进行金额检验----------");
        //4.订单金额校验(单位为分 1元=100分)
        //TODO 正式环境下不用除100(测试环境下不用进行金额检验)
        if ((chargeRecord.getAmount()) != Long.parseLong(map.get("total_fee"))) {
            logger.warn("======订单号" + chargeRecordId + "=======充值金额有误=======================");
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        //5.数据库写入
        //验证成功
        //充值金额处理
        //是否为首充
        boolean isFirstCharge = userPurseService.getPurseByUid(uid).getIsFirstCharge();

        String prodId = chargeRecord.getChargeProdId();

        ChargeProd chargeProd = chargeProdService.getChargeProdById(prodId);
        Integer goldRate = chargeProd.getChangeGoldRate();
        Long chargeGoldNum = goldRate * chargeProd.getMoney();
        Integer giftGoldNum = chargeProd.getGiftGoldNum();
        logger.warn("[ 微信公众号充值 ] 判断用户是否首冲：uid:>{},isFirstCharge:>{}", uid, isFirstCharge);
        if (isFirstCharge) {
            Integer firstChargeGoldNum = chargeProd.getFirstGiftGoldNum();
            chargeGoldNum += firstChargeGoldNum;
            Users users = usersService.getUsersByUid(chargeRecord.getUid());
            if (users != null) {
                // 首冲大礼包
                chargeService.firstGiftBag(users.getUid(), chargeRecord.getAmount(), chargeRecord.getChargeRecordId());
            }
        }
        chargeGoldNum = chargeGoldNum + giftGoldNum.longValue();

        //改变订单状态
        //更改订单状态
        chargeRecord.setTotalGold(chargeGoldNum);
        chargeRecord.setChargeStatus(Constant.ChargeRecordStatus.finish);
        //写入更新时间
        chargeRecord.setUpdateTime(new Date());
        updateChargeRecord(chargeRecord);

//        int count = getChargeRecoredCount(uid);
//        logger.info("isFirstCharge={}, count={}", isFirstCharge, count);
//        if (count > 0) {
//
//        }
        userPurseUpdateService.addChargeGoldDbAndCache(uid, chargeGoldNum);
//        userPurseService.updateGoldByCharge(uid, chargeGoldNum, firstChargeGoldNum);

        //充值更新操作
        billRecordService.insertBillRecord(uid, uid, chargeRecordId, Constant.BillType.charge, null, +chargeGoldNum, null);
        try {
            userShareRecordService.saveUserBonusRecord(chargeRecord.getUid(), chargeRecord.getChargeProdId(), chargeRecord.getAmount().intValue());
        } catch (Exception e) {
            logger.error("updateChargeData  error while saveUserBonusRecord,still continue save user...uid="
                    + chargeRecord.getUid(), e);
            throw new RuntimeException("updateChargeData  error while saveUserBonusRecord,still continue save user");
        }
        try {
            userDrawService.genUserDrawChanceByCharge(chargeRecord.getUid(), chargeRecord.getAmount(), chargeRecord.getChargeProdId());
        } catch (Exception e) {
            logger.error("updateChargeData  error while genUserDrawChance,still continue save user...uid="
                    + chargeRecord.getUid(), e);
            throw new RuntimeException("updateChargeData  error while genUserDrawChance,still continue save user");
        }
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        busiResult.setData(chargeRecord);

        try {

        } catch (Exception e) {

        }
        return busiResult;
    }

    private ChargeRecord getChargeRecordById(String chargeRecordId) {
        ChargeRecord chargeRecord = chargeRecordMapper.selectByPrimaryKey(chargeRecordId);
        return chargeRecord;
    }

    private void updateChargeRecord(ChargeRecord chargeRecord) {
        chargeRecord.setUpdateTime(new Date());
        chargeRecordMapper.updateByPrimaryKeySelective(chargeRecord);
    }

    private int getChargeRecoredCount(Long uid) {
        ChargeRecordExample example = new ChargeRecordExample();
        example.createCriteria().andChargeStatusEqualTo((byte) 2).andUidEqualTo(uid);
        return chargeRecordMapper.countByExample(example);
    }

    /**
     * 发送请求
     *
     * @param url
     * @param code
     * @return
     */

    public String sendHttpsCoon(String url, String code) {
        if (url.isEmpty()) {
            return null;
        }
        try {
            //设置SSLContext
            SSLContext ssl = SSLContext.getInstance("SSL");
            ssl.init(null, new TrustManager[]{myX509TrustManager}, null);

            //打开连接
            HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
            //设置套接工厂
            conn.setSSLSocketFactory(ssl.getSocketFactory());
            //加入数据
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-type", "application/json");

/*
                      JSONObject obj = new JSONObject ( );
                        obj.put ( "receipt-data", code );*/

            //Gson
                       /* ReceiptDataVo receiptDataVo = new ReceiptDataVo(code);
                        Gson gson = new Gson () ;
                        String string = gson.toJson ( receiptDataVo );*/
            //
            BufferedOutputStream buffOutStr = new BufferedOutputStream(conn.getOutputStream());
            String str = "{\"receipt-data\":\"" + code + "\"}";
//                     buffOutStr.write ( obj.toString ( ).getBytes ( ) );
            buffOutStr.write(str.getBytes());
            buffOutStr.flush();
            buffOutStr.close();

            //获取输入流
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();

        } catch (Exception e) {
            return null;
        }
    }

        /*public String sendHttpsCoon ( String url, String code ) {


                String receiptData ="{\n" +
                        "\t\"receipt-data\":\""+code +"\"}" ;
//                String reData = HttpUtils.sendPost ( "https://sandbox.itunes.apple.com/verifyReceipt",receiptData );
                return HttpUtils.sendPost (url,receiptData  );

        }*/
        /*
        测试内容:
        1. 生成订单
                （1）正确的uid和proID（√）
                （2）不正确的uid和proID（√）
       2. 二次检验
                  （1）正确订单号但是订单不属于该uid(√)
                  （2）uid错误和订单号错误（√）
                     (3)充值金额（√）
         */



    @Value("${PIC_URL}")
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }


    @Value("${WEB_URL}")
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
