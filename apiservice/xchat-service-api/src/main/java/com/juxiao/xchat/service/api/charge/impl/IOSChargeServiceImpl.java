package com.juxiao.xchat.service.api.charge.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.base.utils.HttpUtils;
import com.juxiao.xchat.base.utils.MD5Utils;
import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.bill.BillGoldChargeDao;
import com.juxiao.xchat.dao.bill.BillRecordDao;
import com.juxiao.xchat.dao.bill.domain.BillGoldChargeDO;
import com.juxiao.xchat.dao.bill.domain.BillRecordDO;
import com.juxiao.xchat.dao.bill.enumeration.BillRecordType;
import com.juxiao.xchat.dao.charge.ChargeAppleIdRecordDao;
import com.juxiao.xchat.dao.charge.ChargeAppleRecordDao;
import com.juxiao.xchat.dao.charge.ChargeRecordDao;
import com.juxiao.xchat.dao.charge.domain.ChargeAppleIdRecordDO;
import com.juxiao.xchat.dao.charge.domain.ChargeAppleRecordDO;
import com.juxiao.xchat.dao.charge.domain.ChargeRecordDO;
import com.juxiao.xchat.dao.charge.dto.ChargeProdDTO;
import com.juxiao.xchat.dao.charge.dto.ChargeRecordDTO;
import com.juxiao.xchat.dao.charge.enumeration.ChargePayChannel;
import com.juxiao.xchat.dao.charge.enumeration.ChargeRecordStatus;
import com.juxiao.xchat.dao.user.dto.UserPurseDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.charge.ChargeManager;
import com.juxiao.xchat.manager.common.charge.ChargeProdManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.user.UserDrawManager;
import com.juxiao.xchat.manager.common.user.UserPurseManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.mq.ActiveMqManager;
import com.juxiao.xchat.manager.mq.constant.MqDestinationKey;
import com.juxiao.xchat.service.api.charge.IOSChargeService;
import com.juxiao.xchat.service.api.charge.ret.ReceiptRet;
import com.juxiao.xchat.service.api.charge.vo.IOSOrderPlaceVO;
import com.juxiao.xchat.service.api.charge.vo.ReceiptVO;
import com.juxiao.xchat.service.api.event.PublicChargeActivityService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class IOSChargeServiceImpl implements IOSChargeService {
    //购买凭证验证地址
    private static final String BUY_RECEIPT_URL = "https://buy.itunes.apple.com/verifyReceipt";
    //测试的购买凭证验证地址
    private static final String SANDBOX_RECEIPT_URL = "https://sandbox.itunes.apple.com/verifyReceipt";
    // 日志
    private final Logger logger = LoggerFactory.getLogger(IOSChargeService.class);
    @Autowired
    private Gson gson;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private BillGoldChargeDao goldChargeDao;
    @Autowired
    private BillRecordDao billRecordDao;
    @Autowired
    private ChargeAppleRecordDao appleRecordDao;
    @Autowired
    private ChargeAppleIdRecordDao chargeAppleIdRecordDao;
    @Autowired
    private ChargeRecordDao chargeRecordDao;

    @Autowired
    private ChargeProdManager chargeProdManager;
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private UserPurseManager userPurseManager;
    @Autowired
    private ChargeManager chargeManager;
    @Autowired
    private SystemConf systemConf;

    @Autowired
    private ActiveMqManager activeMqManager;

    @Autowired
    private PublicChargeActivityService publicChargeActivityService;

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private UserDrawManager userDrawManager;

    @Autowired
    private ChargeServiceImpl chargeService;

    @Override
    public IOSOrderPlaceVO placeOrder(String ip, Long uid, String chargeProdId, Integer isJailbroken, String os) throws WebServiceException {
        if (uid == null || StringUtils.isBlank(chargeProdId)) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

//        if (isJailbroken == null || isJailbroken != 0) {
//            throw new WebServiceException(WebServiceCode.CHARGE_ILLEGAL);
//        }

        UsersDTO userDto = usersManager.getUser(uid);
        if (userDto == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        ChargeProdDTO prodDto = chargeProdManager.getChargeProd(chargeProdId);
        if (prodDto == null) {
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
        }
        if (!systemConf.getAuditAccountList().contains(uid.toString())) {
            Date now = new Date();
            Integer num = jdbcTemplate.queryForObject("SELECT COUNT(1) from charge_apple_record where uid = ? and create_time BETWEEN ? and ?", Integer.class, uid, DateTimeUtils.formatBeginDate(now), DateTimeUtils.formatEndDate(now));
            if (num >= 3) {
                throw new WebServiceException(WebServiceCode.CHARGE_SERVER_BUSY);
            }
        }

        //保存充值记录
        //1.创建订单号
        //UUID不会重复，所以不需要判断是否生成重复的订单号
        String chargeRecordId = UUIDUtils.get();
        Integer amount = prodDto.getMoney() * 100;
        ChargeRecordDO chargeRecord = new ChargeRecordDO();
        chargeRecord.setChargeRecordId(chargeRecordId);
        chargeRecord.setChargeProdId(chargeProdId);
        chargeRecord.setUid(uid);
        chargeRecord.setChannel(ChargePayChannel.ios_pay.name());
        chargeRecord.setChargeStatus(ChargeRecordStatus.CREATE.getValue());
        chargeRecord.setAmount(amount);
        chargeRecord.setSubject(prodDto.getProdName() + "金币充值");
        chargeRecord.setBody(prodDto.getProdName() + "金币充值");
        chargeRecord.setClientIp(ip);
        chargeRecord.setCreateTime(new Date());
        //写入数据库
        chargeRecordDao.save(chargeRecord);

        IOSOrderPlaceVO placeVo = new IOSOrderPlaceVO();
        placeVo.setRecordId(chargeRecordId);
        return placeVo;
    }

    @Override
    public ReceiptRet verifyReceipt(String uid, String chooseEnv, String receipt, Integer isJailbroken, String trancid, String os, String appVersion) throws WebServiceException {
        if (StringUtils.isBlank(uid) || StringUtils.isBlank(receipt)) {
            logger.warn("[ iOS支付验证 ] 输入参数错误：uid:>{},trancid:>{},receipt:>{},os:>{},appVersion:>{}", uid, trancid, receipt, os, appVersion);
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        if (!StringUtils.isNumeric(uid)) {
            logger.warn("[ iOS支付验证 ] 错误的uid格式：uid:>{},trancid:>{},receipt:>{}", uid, trancid, receipt);
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

//        if (isJailbroken != 0) {
//            logger.warn("[ iOS支付验证 ] 越狱客户端：uid:>{},trancid:>{},receipt:>{},appVersion:>{}", uid, trancid, receipt, appVersion);
//            throw new WebServiceException(WebServiceCode.CHARGE_ILLEGAL);
//        }

        UsersDTO usersDto = usersManager.getUser(Long.valueOf(uid));
        if (usersDto == null) {
            logger.warn("[ iOS支付验证 ] 用户不存在，不作处理：uid:>{}, trancid:>{},receipt:>{}", uid, trancid, receipt);
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        if (!systemConf.getAuditAccountList().contains(uid)) {
            Date now = new Date();
            Integer num = jdbcTemplate.queryForObject("SELECT COUNT(1) from charge_apple_record where uid = ? and create_time BETWEEN ? and ?", Integer.class, uid, DateTimeUtils.formatBeginDate(now), DateTimeUtils.formatEndDate(now));
            if (num >= 3) {
                throw new WebServiceException(WebServiceCode.CHARGE_SERVER_BUSY);
            }
        }


        // 校验唯一receipt
        String receiptMd5 = MD5Utils.encode(receipt);
        int receiptCout = appleRecordDao.coutReceipt(receiptMd5);
        if (receiptCout > 0) {
            throw new WebServiceException(WebServiceCode.INVALID_SERVICE);
        }
        //环境测试处理
        String url;
        boolean istAuditAccount = systemConf.getAuditAccountList().contains(uid);
        if (istAuditAccount) {
            url = SANDBOX_RECEIPT_URL;
        } else {
            url = "true".equalsIgnoreCase(chooseEnv) ? BUY_RECEIPT_URL : SANDBOX_RECEIPT_URL;
        }

        JSONObject object = new JSONObject();
        object.put("receipt-data", receipt);
        String receiptData = object.toJSONString();
        String data = HttpUtils.httpsPost(url, receiptData);
        logger.info("[ iOS支付验证 ] uid:>{},chooseEnv:>{},url:>{},请求JSON:>{},响应:>{}", uid, chooseEnv, url, receiptData, data);

        ReceiptRet receiptRet = StringUtils.isNotBlank(data) ? gson.fromJson(data, ReceiptRet.class) : null;
        //正式环境测试失败，进行测试环境
        if ("true".equalsIgnoreCase(chooseEnv) && (receiptRet == null || receiptRet.getStatus() != 0)) {
            data = HttpUtils.httpsPost(SANDBOX_RECEIPT_URL, receiptData);
            logger.info("[ iOS支付验证 ] 正式环境验证失败，进行沙盒环境的验证：uid:>{}, url:>{}, 请求JSON:>{}, 响应:>{}", uid, SANDBOX_RECEIPT_URL, receiptData, data);
            receiptRet = StringUtils.isNotBlank(data) ? gson.fromJson(data, ReceiptRet.class) : null;
        }

        if (receiptRet == null) {
            throw new WebServiceException(WebServiceCode.INVALID_RECEIPT);
        }
        return receiptRet;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReceiptVO updateChargeRecord(String ip, Long uid, String chargeRecordId, ReceiptRet receiptRet, String receiptmd5, String trancid) throws WebServiceException {
        if (!systemConf.getBundleIds().contains(receiptRet.getReceipt().get("bundle_id"))) {
            logger.warn("[ iOS支付验证 ] 非法的bundle_id:>{}", receiptRet.getReceipt().get("bundle_id"));
            throw new WebServiceException(WebServiceCode.INVALID_SERVICE);
        }

        if (receiptRet.getStatus() != 0) {
            logger.warn("[ iOS支付验证 ] Apple服务器返回码:>{}，不做存储处理。", receiptRet.getStatus());
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
        }

        List<Map<String, Object>> inApp = (List<Map<String, Object>>) receiptRet.getReceipt().get("in_app");
        if (inApp == null || inApp.size() == 0) {
            logger.warn("[ iOS支付验证 ] 非法的inApp");
            throw new WebServiceException(WebServiceCode.INVALID_SERVICE);
        }

        if (trancid != null && !trancid.equalsIgnoreCase(inApp.get(0).get("transaction_id").toString())) {
            throw new WebServiceException(WebServiceCode.CHARGE_VALIDATE_ERROR);
        }

        String productId = String.valueOf(inApp.get(0).get("product_id"));
        if (systemConf.getIosChargeProds() == null || !systemConf.getIosChargeProds().contains(productId)) {
            throw new WebServiceException(WebServiceCode.CHARGE_PROD_NOT_EXISTS);
        }

        ChargeProdDTO prodDto = chargeProdManager.getChargeProd(productId);
        if (prodDto == null || prodDto.getProdStatus() != 1) {
            throw new WebServiceException(WebServiceCode.CHARGE_PROD_NOT_EXISTS);
        }

        ChargeRecordDTO chargeRecordDto = chargeRecordDao.getChargeRecord(chargeRecordId);
        if (chargeRecordDto == null) {
            logger.warn("[ iOS支付验证 ]订单不存在，不做存储处理：chargeRecordId:>{}", chargeRecordId);
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
        }

        if (!uid.equals(chargeRecordDto.getUid())) {
            logger.warn("[ iOS支付验证 ]订单不属于该用户，不做存储处理：uid:>{},chargeRecordUid:>{}", uid, chargeRecordDto.getUid());
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
        }

        if (!ip.equalsIgnoreCase(chargeRecordDto.getClientIp())) {
            throw new WebServiceException(WebServiceCode.CHARGE_VALIDATE_ERROR);
        }

        if (!productId.equals(chargeRecordDto.getChargeProdId())) {
            throw new WebServiceException(WebServiceCode.CHARGE_VALIDATE_ERROR);
        }

        if (chargeRecordDto.getChargeStatus() == null) {
            logger.warn("[ iOS支付验证 ] 订单错误：空的状态码，不做存储处理。");
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
        } else if (ChargeRecordStatus.ERROR.valueEquals(chargeRecordDto.getChargeStatus())) {
            logger.warn("[ iOS支付验证 ] 订单错误：用户付款失败，不做存储处理。");
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
        } else if (ChargeRecordStatus.TIMEOUT.valueEquals(chargeRecordDto.getChargeStatus())) {
            logger.warn("[ iOS支付验证 ] 订单错误：订单超时，不做存储处理。");
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
        } else if (ChargeRecordStatus.FINISH.valueEquals(chargeRecordDto.getChargeStatus())) {
            logger.warn("[ iOS支付验证 ] 订单错误：重复支付，不做存储处理。");
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
        }

        chargeService.teensMode(uid,chargeRecordDto.getAmount());

        if (chargeRecordDto.getAmount() == null) {
            logger.warn("[ iOS支付验证 ] 订单金额异常，不作处理");
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
        }

        try {
            //用户付款成功，把内购订单id保存到数据库
            ChargeAppleIdRecordDO chargeAppleIdRecordDO = new ChargeAppleIdRecordDO();
            chargeAppleIdRecordDO.setChargeRecordId(chargeRecordId);
            chargeAppleIdRecordDO.setUid(uid);
            chargeAppleIdRecordDO.setTransactionId(inApp.get(0).get("transaction_id").toString());
            chargeAppleIdRecordDO.setCreateTime(new Date());
            chargeAppleIdRecordDao.save(chargeAppleIdRecordDO);
        } catch (Exception e) {
            logger.warn("[ iOS支付验证 ] 订单号异常，同一订单id号重复调用多次");
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
        }

        try {
            //用户付款成功，把内购订单id保存到数据库
            ChargeAppleRecordDO recordDo = new ChargeAppleRecordDO();
            recordDo.setChargeRecordId(chargeRecordId);
            recordDo.setUid(uid);
            recordDo.setReceip(receiptmd5);
            recordDo.setCreateTime(new Date());
            appleRecordDao.save(recordDo);
        } catch (Exception e) {
            logger.warn("[ iOS支付验证 ] 订单号异常，同一订单号重复调用多次");
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
        }

        ChargeRecordDO chargeRecord = new ChargeRecordDO();
        chargeRecord.setChargeRecordId(chargeRecordId);
        chargeRecord.setChargeStatus(ChargeRecordStatus.FINISH.getValue());
        chargeRecord.setUpdateTime(new Date());
        chargeRecordDao.update(chargeRecord);

        //充值的金额
        //1. 充值金额的修改
        //2. 修改订单状态
        //进行钱包充值操作
        int chargeGoldNum;
        switch (prodDto.getChargeProdId()) {
            case "com.ify_8":
                chargeGoldNum = 56;
                break;
            case "com.ify_30":
                chargeGoldNum = 210;
                break;
            case "com.ify_98":
                chargeGoldNum = 686;
                break;
            case "com.ify_488":
                chargeGoldNum = 3146;
                break;
            case "com.ify_998":
                chargeGoldNum = 6986;
                break;
            case "com.ify_2998":
                chargeGoldNum = 20986;
                break;
            default:
                throw new WebServiceException(WebServiceCode.CHARGE_PROD_NOT_EXISTS);
        }

        UserPurseDTO userPurse = userPurseManager.getUserPurse(uid);
        if (userPurse.getIsFirstCharge()) {
            // 首冲礼包
            chargeService.firstChargeBag(uid);
            chargeGoldNum += prodDto.getFirstGiftGoldNum();
        }

        Date date = new Date();
        BillGoldChargeDO goldChargeDo = new BillGoldChargeDO();
        goldChargeDo.setUid(uid);
        goldChargeDo.setGoldAmount(chargeGoldNum);
        goldChargeDo.setMoney(chargeRecordDto.getAmount() / 100);
        goldChargeDo.setChargeId(chargeRecordId);
        goldChargeDo.setCreateTime(date);
        goldChargeDao.save(goldChargeDo);

        // FIXME:管理后台修改之后删除
        BillRecordDO billRecord = new BillRecordDO();
        billRecord.setBillId(UUIDUtils.get());
        billRecord.setUid(uid);
        billRecord.setTargetUid(uid);
        billRecord.setObjId(chargeRecord.getChargeRecordId());
        billRecord.setObjType(BillRecordType.charge);
        billRecord.setDiamondNum(null);
        billRecord.setGoldNum((long) chargeGoldNum);
        billRecord.setMoney(null);
        billRecord.setCreateTime(date);
        billRecordDao.save(billRecord);

        ReceiptVO recepitVo = new ReceiptVO();
        recepitVo.setData(gson.toJson(receiptRet));

        JSONObject object = new JSONObject();
        object.put("uid", uid);
        object.put("money", chargeRecordDto.getAmount());
        object.put("chargeGoldAmount", chargeGoldNum);
        object.put("amount", chargeRecordDto.getAmount());
        object.put("chargeId", chargeRecordDto.getChargeRecordId());
        activeMqManager.sendQueueMessage(MqDestinationKey.CHARGE_QUEUE, object.toJSONString());
        userPurseManager.updateAddGold(uid, (long) chargeGoldNum, true, true, "IOS内购充值", null, null);
//         获得抽奖机会
//        userDrawManager.saveChargeDraw(chargeRecordDto.getUid(), chargeRecordDto.getAmount(), chargeRecordDto.getChargeRecordId());
        chargeManager.sumUserCharge(chargeRecordDto.getUid(), prodDto.getMoney() * 100);
        //充值记录
        chargeService.chargeActivity(chargeRecordDto.getUid(), chargeRecordDto.getAmount(), chargeRecordDto.getChargeRecordId());

        return recepitVo;
    }
}
