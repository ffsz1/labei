package com.juxiao.xchat.service.api.charge.impl;

import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.UUIDUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.bill.BillGoldChargeDao;
import com.juxiao.xchat.dao.bill.BillRecordDao;
import com.juxiao.xchat.dao.bill.domain.BillGoldChargeDO;
import com.juxiao.xchat.dao.bill.domain.BillRecordDO;
import com.juxiao.xchat.dao.bill.enumeration.BillRecordType;
import com.juxiao.xchat.dao.charge.ChargeRecordDao;
import com.juxiao.xchat.dao.charge.domain.ChargeRecordDO;
import com.juxiao.xchat.dao.charge.dto.ChargeProdDTO;
import com.juxiao.xchat.dao.charge.dto.ChargeRecordDTO;
import com.juxiao.xchat.dao.charge.enumeration.ChargePayChannel;
import com.juxiao.xchat.dao.charge.enumeration.ChargeRecordBussType;
import com.juxiao.xchat.dao.charge.enumeration.ChargeRecordStatus;
import com.juxiao.xchat.dao.user.dto.UserPurseDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.charge.ChargeProdManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.user.UserDrawManager;
import com.juxiao.xchat.manager.common.user.UserPurseManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.weixin.WeixinPayManager;
import com.juxiao.xchat.manager.external.weixin.bo.WeixinReceiverBO;
import com.juxiao.xchat.manager.external.weixin.vo.UnifiedOrderVO;
import com.juxiao.xchat.manager.external.weixin.vo.WeixinReturnCodeVO;
import com.juxiao.xchat.manager.external.weixin.vo.WxappRequestPaymentVO;
import com.juxiao.xchat.manager.mq.ActiveMqManager;
import com.juxiao.xchat.manager.mq.constant.MqDestinationKey;
import com.juxiao.xchat.service.api.charge.WeixinChargeService;
import com.juxiao.xchat.service.api.charge.vo.WeixinUnifiedOrderVO;
import com.juxiao.xchat.service.api.event.PublicChargeActivityService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 微信充值处理
 *
 * @class: WeixinChargeServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/6/12
 */
@Service
public class WeixinChargeServiceImpl implements WeixinChargeService {
    private static final WeixinReturnCodeVO FAILURE = new WeixinReturnCodeVO("FAIL", "ERROR");
    private final Logger logger = LoggerFactory.getLogger(WeixinChargeService.class);
    @Autowired
    private SystemConf systemConf;
    @Autowired
    private BillGoldChargeDao goldChargeDao;
    @Autowired
    private BillRecordDao billRecordDao;
    @Autowired
    private ChargeRecordDao chargeRecordDao;
    @Autowired
    private ActiveMqManager activeMqManager;
    @Autowired
    private ChargeProdManager chargeProdManager;
    @Autowired
    private WeixinPayManager payManager;
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private UserPurseManager purseManager;
    @Autowired
    private RedisManager redisManager;

    @Autowired
    private PublicChargeActivityService publicChargeActivityService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDrawManager userDrawManager;

    @Autowired
    private ChargeServiceImpl chargeService;

    @Override
    public WeixinUnifiedOrderVO createPaySign(String ip, Long erbanNo, String phone, String chargeProdId, String openId) throws Exception {
        if (erbanNo == null && StringUtils.isBlank(phone)) {
            logger.warn("[ 微信支付 ] 生成订单，官方号和手机号都为空。");
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        if (StringUtils.isBlank(chargeProdId) || StringUtils.isBlank(openId)) {
            logger.warn("[ 微信支付 ] 生成订单，必要参数为空，erban_no={}&phone={}&chargeProdId={}&openId={}", erbanNo, phone, chargeProdId, openId);
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        UsersDTO usersDto;
        if (erbanNo != null) {
            usersDto = usersManager.getUserByErbanNo(erbanNo);
        } else if (StringUtils.isNotBlank(phone)) {
            usersDto = usersManager.getUserByPhone(phone);
        } else {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        if (usersDto == null) {
            logger.warn("[ 微信支付 ] 生成订单，用户不存在，erbanNo:>{},phone:>{}", erbanNo, phone);
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        ChargeProdDTO chargeProd = chargeProdManager.getChargeProd(chargeProdId);
        if (chargeProd == null) {
            logger.warn("[ 微信支付 ] 生成订单，充值的产品不存在，erbanNo:>{},phone:>{}", erbanNo, phone);
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        String outTradeNo = UUIDUtils.get();
        int amount = chargeProd.getMoney() * 100;
        String goodName = "充值" + (amount / 10) + " 金币";
        UnifiedOrderVO unifiedOrderVo = payManager.submitWxpubPay(ip, outTradeNo, openId, goodName, amount);

        // 保存数据库
        String subject = chargeProd.getProdName() + "金币充值";
        ChargeRecordDO chargeRecordDo = new ChargeRecordDO();
        chargeRecordDo.setChargeRecordId(outTradeNo);
        chargeRecordDo.setChargeProdId(chargeProdId);
        chargeRecordDo.setUid(usersDto.getUid());
        //微信公众号支付
        chargeRecordDo.setChannel(ChargePayChannel.wx_pub.name());
        chargeRecordDo.setChargeStatus(ChargeRecordStatus.CREATE.getValue());
        chargeRecordDo.setAmount(amount);
        chargeRecordDo.setSubject(subject);
        chargeRecordDo.setBody(subject);
        chargeRecordDo.setClientIp(ip);
        chargeRecordDo.setWxPubOpenid(openId);
        chargeRecordDo.setCreateTime(new Date());
        chargeRecordDao.save(chargeRecordDo);

        WeixinUnifiedOrderVO orderVo = new WeixinUnifiedOrderVO();
        BeanUtils.copyProperties(unifiedOrderVo, orderVo);
        orderVo.setOutTradeNo(outTradeNo);
        orderVo.setNick(usersDto.getNick());
        orderVo.setErban_no(usersDto.getErbanNo());
        return orderVo;
    }

    @Override
    public WxappRequestPaymentVO createWxappPaySign(Long uid, String openId, String chargeProdId, String ip) throws Exception {
        UsersDTO usersDto = usersManager.getUser(uid);
        if (usersDto == null) {
            logger.warn("[ 微信支付 ] 发起小程序支付，生成订单，用户不存在，uid:>{}", uid);
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        ChargeProdDTO prodDto = chargeProdManager.getChargeProd(chargeProdId);
        if (prodDto == null) {
            logger.warn("[ 微信支付 ] 发起小程序支付，充值产品不存在，chargeProdId:>{}", chargeProdId);
            throw new WebServiceException(WebServiceCode.CHARGE_NOCHANNEL);
        }

        // 以分为单位，测试服充值分
        int amount = "prod".equalsIgnoreCase(systemConf.getEnv()) ? prodDto.getMoney() * 100 : prodDto.getMoney();
        String body = prodDto.getProdName() + "金币充值";
        String subject = prodDto.getProdName() + "金币充值";
        String chargeRecordId = UUIDUtils.get();

        // 保存充值记录
        ChargeRecordDO chargeRecord = new ChargeRecordDO();
        chargeRecord.setChargeRecordId(chargeRecordId);
        chargeRecord.setChargeProdId(chargeProdId);
        chargeRecord.setUid(uid);
        chargeRecord.setChannel(ChargePayChannel.wx_app.name());
        chargeRecord.setBussType(ChargeRecordBussType.CHARGE.getValue());
        chargeRecord.setChargeStatus(ChargeRecordStatus.CREATE.getValue());
        chargeRecord.setAmount(amount);
        chargeRecord.setSubject(subject);
        chargeRecord.setBody(body);
        chargeRecord.setChargeDesc(body);//充值描述
        chargeRecord.setClientIp(ip);
        chargeRecord.setCreateTime(new Date());
        chargeRecordDao.save(chargeRecord);
        return payManager.submitWxappPay(ip, chargeRecordId, openId, body, amount);
    }

    /**
     * @see com.juxiao.xchat.service.api.charge.WeixinChargeService#receive(WeixinReceiverBO)
     */
    @Override
    public WeixinReturnCodeVO receive(WeixinReceiverBO receiverBo) throws WebServiceException {
        //1.签名验证
        WeixinReturnCodeVO returnCode = payManager.receive(receiverBo);
        if (!"SUCCESS".equalsIgnoreCase(returnCode.getReturnCode())) {
            return returnCode;
        }

        //2.结果判断
        if (!"SUCCESS".equals(receiverBo.getReturn_code()) || !"SUCCESS".equals(receiverBo.getResult_code())) {
            return FAILURE;
        }

        String redisKey = RedisKey.charge_lock.getKey(receiverBo.getOut_trade_no());
        String lockVal = redisManager.lock(redisKey, 10000);
        try {
            if (StringUtils.isBlank(lockVal)) {
                return FAILURE;
            }

            ChargeRecordDTO chargeRecordDto = chargeRecordDao.getChargeRecord(receiverBo.getOut_trade_no());
            if (chargeRecordDto == null || chargeRecordDto.getAmount() == null) {
                logger.warn("[ 微信支付{} ] 找不到对应的订单号。", receiverBo.getOut_trade_no());
                return FAILURE;
            }

            //3.判断该通知是否已经处理过
            if (chargeRecordDto.getChargeStatus() != ChargeRecordStatus.CREATE.getValue()) {
                logger.warn("[ 微信支付{} ] 该订单已经处理，订单状态:>{}", receiverBo.getOut_trade_no(), chargeRecordDto.getChargeStatus());
                return new WeixinReturnCodeVO("SUCCESS", "OK");
            }

            //4.订单金额校验(单位为分 1元=100分)
            if (chargeRecordDto.getAmount().intValue() != receiverBo.getTotal_fee().intValue()) {
                logger.warn("[ 微信支付{} ] 订单金额校验失败，保存金额:>{}，回调金额:>{}", receiverBo.getOut_trade_no(), chargeRecordDto.getAmount(), receiverBo.getTotal_fee());
                return FAILURE;
            }

            //5.数据库写入
            UserPurseDTO purseDto = purseManager.getUserPurse(chargeRecordDto.getUid());
            //是否为首充
            boolean isFirstCharge = purseDto.getIsFirstCharge();
            //验证成功
            //充值金额处理
            ChargeProdDTO chargeProd = chargeProdManager.getChargeProd(chargeRecordDto.getChargeProdId());
            if (chargeProd == null) {
                logger.warn("[ 微信支付{} ] 充值产品不存在，chargeProdId:>{}", receiverBo.getOut_trade_no(), chargeRecordDto.getChargeProdId());
                return FAILURE;
            }

            int goldRate = chargeProd.getChangeGoldRate();
            Integer chargeGoldNum = goldRate * chargeProd.getMoney();
            Integer giftGoldNum = chargeProd.getGiftGoldNum();
            if (isFirstCharge) {
                // 首冲礼包
                chargeService.firstChargeBag(chargeRecordDto.getUid());
                Integer firstChargeGoldNum = chargeProd.getFirstGiftGoldNum();
                chargeGoldNum += firstChargeGoldNum;
            }

            chargeGoldNum = chargeGoldNum + giftGoldNum;

            ChargeRecordDO chargeRecordDo = new ChargeRecordDO();
            chargeRecordDo.setChargeRecordId(chargeRecordDto.getChargeRecordId());
            chargeRecordDo.setPingxxChargeId(receiverBo.getTransaction_id());
            //改变订单状态
            chargeRecordDo.setChargeStatus(ChargeRecordStatus.FINISH.getValue());
            //写入更新时间
            chargeRecordDo.setUpdateTime(new Date());
            chargeRecordDao.update(chargeRecordDo);

            //充值更新操作
            insertBillRecord(chargeRecordDto.getChargeRecordId(), chargeRecordDto.getUid(), chargeGoldNum, receiverBo.getTotal_fee() / 100);

            // 获得抽奖机会
//            userDrawManager.saveChargeDraw(chargeRecordDto.getUid(), chargeRecordDto.getAmount(), chargeRecordDto.getChargeRecordId());
            JSONObject object = new JSONObject();
            object.put("uid", chargeRecordDto.getUid());
            object.put("chargeRecordProdId", chargeRecordDto.getChargeProdId());
            object.put("amount", chargeRecordDto.getAmount());
            object.put("chargeId", chargeRecordDto.getChargeRecordId());
            object.put("money", chargeRecordDto.getAmount());
            activeMqManager.sendQueueMessage(MqDestinationKey.CHARGE_QUEUE, object.toJSONString());
            //6.返回信息
            purseManager.updateAddGold(chargeRecordDto.getUid(), chargeGoldNum.longValue(), true, true, "微信公众号充值", null, null);
            chargeService.chargeActivity(chargeRecordDto.getUid(), chargeRecordDto.getAmount(), chargeRecordDto.getChargeRecordId());

            return new WeixinReturnCodeVO("SUCCESS", "OK");
        } catch (Exception e) {
            throw e;
        } finally {
            redisManager.unlock(redisKey, lockVal);
        }
    }


    private void insertBillRecord(String recordId, Long uid, Integer goldAmount, Integer money) {
        BillGoldChargeDO chargeDo = new BillGoldChargeDO();
        chargeDo.setChargeId(recordId);
        chargeDo.setUid(uid);
        chargeDo.setGoldAmount(goldAmount);
        chargeDo.setMoney(money);
        chargeDo.setCreateTime(new Date());
        goldChargeDao.save(chargeDo);

        // FIXME: 管理后台切换表之后删除
        Date date = new Date();
        BillRecordDO billRecord = new BillRecordDO();
        billRecord.setBillId(UUIDUtils.get());
        billRecord.setUid(uid);
        billRecord.setTargetUid(uid);
        billRecord.setObjId(recordId);
        billRecord.setObjType(BillRecordType.charge);
        billRecord.setDiamondNum(null);
        billRecord.setGoldNum(goldAmount.longValue());
        billRecord.setMoney(money.longValue());
        billRecord.setCreateTime(date);
        billRecordDao.save(billRecord);
    }
}
