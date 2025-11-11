package com.juxiao.xchat.service.api.charge.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
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
import com.juxiao.xchat.dao.charge.enumeration.*;
import com.juxiao.xchat.dao.item.GiftCarGetRecordDao;
import com.juxiao.xchat.dao.item.HeadwearGetRecordDao;
import com.juxiao.xchat.dao.item.domain.GiftCarGetRecordDO;
import com.juxiao.xchat.dao.item.domain.HeadwearGetRecordDO;
import com.juxiao.xchat.dao.item.dto.GiftCarDTO;
import com.juxiao.xchat.dao.item.dto.HeadwearDTO;
import com.juxiao.xchat.dao.item.enumeration.CarGetType;
import com.juxiao.xchat.dao.item.enumeration.HeadwearGetType;
import com.juxiao.xchat.dao.user.UsersPwdTeensModeDao;
import com.juxiao.xchat.dao.user.domain.UsersPwdTeensMode;
import com.juxiao.xchat.dao.user.dto.UserPurseDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.charge.ChargeManager;
import com.juxiao.xchat.manager.common.charge.ChargeProdManager;
import com.juxiao.xchat.manager.common.item.GiftCarManager;
import com.juxiao.xchat.manager.common.item.HeadwearManager;
import com.juxiao.xchat.manager.common.user.UserDrawManager;
import com.juxiao.xchat.manager.common.user.UserPurseManager;
import com.juxiao.xchat.manager.common.user.UserShareRecordManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.manager.external.ecpss.EcpssManager;
import com.juxiao.xchat.manager.external.ecpss.bo.OrderCallBackBO;
import com.juxiao.xchat.manager.external.joinpay.JoinpayManager;
import com.juxiao.xchat.manager.external.joinpay.ret.AppJoinpayAPP3Ret;
import com.juxiao.xchat.manager.external.joinpay.ret.AppJoinpayRet;
import com.juxiao.xchat.manager.external.joinpay.ret.JoinpayReciverRet;
import com.juxiao.xchat.manager.external.joinpay.ret.JoinpayRet;
import com.juxiao.xchat.manager.external.joinpay.vo.JoinpayReciver;
import com.juxiao.xchat.manager.external.pingxx.PingxxManager;
import com.juxiao.xchat.manager.external.sand.SandpayManager;
import com.juxiao.xchat.manager.external.sand.vo.SandpayReciver;
import com.juxiao.xchat.manager.mq.ActiveMqManager;
import com.juxiao.xchat.manager.mq.constant.MqDestinationKey;
import com.juxiao.xchat.service.api.charge.ChargeService;
import com.juxiao.xchat.service.api.charge.vo.EcpssAlipayVO;
import com.juxiao.xchat.service.api.event.PublicChargeActivityService;
import com.juxiao.xchat.service.api.sysconf.NationalDayService;
import com.pingplusplus.model.Charge;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @class: ChargeServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
@Service
public class ChargeServiceImpl implements ChargeService {
    private final Logger logger = LoggerFactory.getLogger(ChargeService.class);
    @Autowired
    private BillGoldChargeDao goldChargeDao;
    @Autowired
    private BillRecordDao billRecordDao;
    @Autowired
    private ChargeRecordDao recordDao;
    @Autowired
    private ActiveMqManager activeMqManager;
    @Autowired
    private ChargeProdManager prodManager;
    @Autowired
    private GiftCarManager giftCarManager;
    @Autowired
    private PingxxManager pingxxManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private UserDrawManager userDrawManager;
    @Autowired
    private UserPurseManager userPurseManager;
    @Autowired
    private UserShareRecordManager shareRecordManager;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private HeadwearManager headwearManager;

    @Autowired
    private PublicChargeActivityService publicChargeActivityService;

    @Autowired
    private ChargeManager chargeManager;

    @Autowired
    private Gson gson;

    @Autowired
    private JoinpayManager joinpayManager;

    @Autowired
    private SandpayManager sandpayManager;

    @Autowired
    private EcpssManager ecpssManager;


    @Autowired
    private GiftCarGetRecordDao carGetRecordDao;

    @Autowired
    private UsersPwdTeensModeDao usersPwdTeensModeDao;

    @Autowired
    private HeadwearGetRecordDao headwearGetRecordDao;

    @Autowired
    private AsyncNetEaseTrigger asyncNetEaseTrigger;

//    @Autowired
//    private NationalDayService nationalDayService;

    public static final String WEB_APP = "webApp";

    /**
     * @see com.juxiao.xchat.service.api.charge.ChargeService#applyCharge(Long, String, String, String, String)
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Charge applyCharge(Long uid, String chargeProdId, String payChannel, String clientIp, String successUrl) throws Exception {
        if (StringUtils.isEmpty(payChannel) || uid == null || uid == 0L || StringUtils.isEmpty(chargeProdId) || StringUtils.isEmpty(clientIp)) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        // 支持的充值方式，支付宝app充值和微信公众号充值
        ChargePayChannel channel = ChargePayChannel.valueOf(payChannel);
        if (channel == null) {
            logger.error("[ 发起充值支付 ] 不支持的充值支付渠道，payChannel:>{}", payChannel);
            throw new WebServiceException(WebServiceCode.CHARGE_NOCHANNEL);
        }

        ChargeProdDTO prodDto = prodManager.getChargeProd(chargeProdId);
        if (prodDto == null) {
            logger.error("[ 发起充值支付 ] 充值产品数据库中不存在，chargeProdId:>{}", chargeProdId);
            throw new WebServiceException("充值产品不存在");
        }

        // 保存充值记录
        int amount = prodDto.getMoney() * 100;// 支付宝充值，以分为单位
        String body = prodDto.getProdName() + "金币充值";
        String subject = prodDto.getProdName() + "金币充值";
        String chargeRecordId = UUIDUtils.get();

        Charge charge = pingxxManager.createCharge(chargeRecordId, channel, amount, subject, body, clientIp, successUrl);

        ChargeRecordDO chargeRecord = new ChargeRecordDO();
        chargeRecord.setChargeRecordId(chargeRecordId);
        chargeRecord.setChargeProdId(chargeProdId);
        chargeRecord.setUid(uid);
        chargeRecord.setChannel(payChannel);
        chargeRecord.setBussType(ChargeRecordBussType.CHARGE.getValue());
        chargeRecord.setChargeStatus(ChargeRecordStatus.CREATE.getValue());
        chargeRecord.setAmount(amount);
        chargeRecord.setSubject(subject);
        chargeRecord.setBody(body);
        chargeRecord.setChargeDesc(body);//充值描述
        chargeRecord.setClientIp(clientIp);
        chargeRecord.setCreateTime(new Date());
        chargeRecord.setPingxxChargeId(charge.getId());
        recordDao.save(chargeRecord);
        logger.info("[ 创建订单 {} ] 创建ping++订单成功，pingxxChargeId:>{}", chargeRecordId, charge.getId());
        return charge;
    }

    @Transactional(rollbackFor = {Exception.class, WebServiceException.class})
    @Override
    public void reciveCharge(Charge charge) throws Exception {
        // 1.校验ping++的charge
        pingxxManager.retrieve(charge.getId(), charge.getOrderNo(), charge.getPaid());

        // 2.校验本地的充值记录
        String redisKey = RedisKey.charge_lock.getKey(charge.getId());
        String lockVal = redisManager.lock(redisKey, 10000);
        try {
            if (StringUtils.isBlank(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            ChargeRecordDTO chargeRecordDto = recordDao.getChargeRecord(charge.getOrderNo());
            if (chargeRecordDto == null) {
                logger.warn("[ ping++回调 {} ]不存在的订单，chargeId:>{}", charge.getOrderNo(), charge.getId());
                throw new WebServiceException(WebServiceCode.BUSI_ERROR);
            }

            if (!ChargeRecordStatus.CREATE.valueEquals(chargeRecordDto.getChargeStatus())) {
                logger.warn("[ ping++回调 {} ]该笔订单已经处理，chargeId:>{}，chargeStatus:>{}", charge.getOrderNo(), charge.getId(), chargeRecordDto.getChargeStatus());
                throw new WebServiceException(WebServiceCode.BUSI_ERROR);
            }

            // 3.按业务类型进行后续的处理
            if (ChargeRecordBussType.CHARGE.getValue() != chargeRecordDto.getBussType()) {
                logger.warn("[ ping++回调 {} ]业务不支持，chargeId:>{}，bussType:>{}", charge.getOrderNo(), charge.getId(), chargeRecordDto.getBussType());
                throw new WebServiceException("支付回调失败，原因：业务不存在");
            }

            String chargeRecordProdId = chargeRecordDto.getChargeProdId();
            ChargeProdDTO chargeProdDto = prodManager.getChargeProd(chargeRecordProdId);
            int money = chargeProdDto.getMoney();

            // 充值金额与实际价格不一致
            if (charge.getAmount() != (money * 100)) {
                logger.warn("[ ping++回调 {} ]金额对比异常，chargeId:>{}，chargeAmount:>{}，chargeProdMoney:>{}", charge.getOrderNo(), charge.getId(), charge.getAmount(), chargeProdDto.getMoney());
                throw new WebServiceException("支付回调失败，原因：数据异常");
            }

            // 转换后的金币数
            Long chargeGoldAmount = chargeProdDto.getChangeGoldRate().longValue() * money;
            // 赠送金币数量(每次充值均赠送)
            Integer giftGoldNum = chargeProdDto.getGiftGoldNum();
            // 赠送金币与购买的金币
            chargeGoldAmount = chargeGoldAmount + giftGoldNum.longValue();
            UserPurseDTO userPurseDto = userPurseManager.getUserPurse(chargeRecordDto.getUid());
            // 判断是否首次充值（如果首次充值还需要加上首次充值赠送的金币数量）
            boolean isFirstCharge = userPurseDto.getIsFirstCharge();

            if (isFirstCharge) {// 首笔充值送头饰
                firstChargeBag(userPurseDto.getUid());
                chargeGoldAmount = chargeGoldAmount + chargeProdDto.getFirstGiftGoldNum();
            }
            ChargeRecordDO recordDo = new ChargeRecordDO();
            recordDo.setChargeRecordId(chargeRecordDto.getChargeRecordId());
            recordDo.setChargeStatus(ChargeRecordStatus.FINISH.getValue());
            recordDo.setTotalGold(chargeGoldAmount);
            recordDo.setUpdateTime(new Date());
            recordDao.update(recordDo);

            BillGoldChargeDO chargeDo = new BillGoldChargeDO();
            chargeDo.setChargeId(chargeRecordDto.getChargeRecordId());
            chargeDo.setUid(chargeRecordDto.getUid());
            chargeDo.setGoldAmount(chargeGoldAmount.intValue());
            chargeDo.setMoney(money);
            chargeDo.setCreateTime(new Date());
            goldChargeDao.save(chargeDo);
//
//            // 获得抽奖机会
//            userDrawManager.saveChargeDraw(chargeRecordDto.getUid(), charge.getAmount(), charge.getId());
            //充值记录
            chargeActivity(chargeRecordDto.getUid(), charge.getAmount(), charge.getId());

            // FIXME: 管理后台切换表之后删除
            this.saveBillRecord(chargeRecordDto.getUid(), chargeRecordDto.getChargeRecordId(), BillRecordType.charge, chargeGoldAmount, money);

            JSONObject object = new JSONObject();
            object.put("uid", chargeRecordDto.getUid());
            object.put("chargeRecordProdId", chargeRecordProdId);
            object.put("amount", charge.getAmount());
            object.put("chargeId", charge.getId());
            object.put("money", charge.getAmount());

            activeMqManager.sendQueueMessage(MqDestinationKey.CHARGE_QUEUE, object.toJSONString());

            chargeManager.sumUserCharge(chargeRecordDto.getUid(), charge.getAmount());
            userPurseManager.updateAddGold(chargeRecordDto.getUid(), chargeGoldAmount, true, false, "ping++充值", null, null);
        } catch (Exception e) {
            throw e;
        } finally {
            redisManager.unlock(redisKey, lockVal);
        }


    }

    /**
     * 青少年模式 充值限制 100
     *
     * @param uid
     * @param amount 金额 单位人民币
     * @return
     * @throws WebServiceException
     */
    public void teensMode(Long uid, int amount) throws WebServiceException {
        UsersPwdTeensMode usersPwdTeensMode = usersPwdTeensModeDao.selectUsersPwdTeensModeByUid(uid);
        if (usersPwdTeensMode == null) {// 没开启青少年模式
            return;
        } else {
            if (amount >= 100) {// 开启青少年模式并且充值金额大于100
                throw new WebServiceException(WebServiceCode.TEENS_CHARGE_LIMIT);
            }
        }

//        String str = redisManager.hget(RedisKey.day_app_charge.getKey(), uid.toString());
//        if (StringUtils.isBlank(str)) {
//            return ;
//        }
//        Integer chargeNum = Integer.parseInt(str);
//        if ((amount + chargeNum) > 100) {
//            throw new WebServiceException(WebServiceCode.TEENS_CHARGE_LIMIT);
//        } else {
//            redisManager.incr(RedisKey.day_app_charge.getKey(), chargeNum);
//        }
    }

    /**
     * 1、每天首笔充值赠送一次开礼盒机会
     * 2、充值满1000元送专属头饰和座驾（每个用户只有一次）
     *
     * @param uid
     * @param amount
     * @param chargeId
     */
    public void chargeActivity(Long uid, int amount, String chargeId) {
        try {
            UsersDTO users = usersManager.getUser(uid);
            if (users == null) {
                return;
            }

//            publicChargeActivityService.noviceFirstChargeCheckInActivity(uid,(amount / 100),chargeId);
            Integer before = getUserCharge(uid);
            Integer after = before + amount / 100;
            logger.info("[ initFirstCharge ] uid:{},充值前:>{},充值后:>{}", uid, before, after);
            redisManager.hset(RedisKey.user_charge.getKey(), uid.toString(), after.toString());
            //国庆活动页充值操作
            //nationalDayService.NationalDayRecharge(uid, before, after);
//
//            // 每天任意首笔充值
//            String str = redisManager.hget(RedisKey.day_first_charge.getKey(), uid.toString());
//            if (StringUtils.isBlank(str)) {
//                // 添加每日礼盒次数
//                redisManager.hset(RedisKey.day_first_charge.getKey(), uid.toString(), "1");// 每日一次
//                asyncNetEaseTrigger.sendMsg(String.valueOf(uid), "今天首笔充值成功，已获得1次开礼盒机会，仅限当天有效，记得去活动页面领取您的幸运哦~");
//            }
//
//            // todo  记得修改id
//            int headwearId = 70;// 指定专属拉贝星球头饰Id
//            int carId = 72;     // 指定专属星月鲸座驾Id
//            int date = 15;      // 累冲道具时长
//            if (after >= 1000) {
//                // 送专属头饰和座驾 只能领取一次
//                boolean hadCar = false;
//                List<GiftCarGetRecordDO> giftCarGetRecords = carGetRecordDao.listUserCarGetRecord(uid);
//                for (GiftCarGetRecordDO recordDO : giftCarGetRecords) {
//                    if (recordDO.getType() == CarGetType.first_charge.getValue() && recordDO.getCarId() == carId) {// 存在
//                        hadCar = true;
//                    }
//                }
//                boolean hadHeadwear = false;
//                List<HeadwearGetRecordDO> headwearGetRecords = headwearGetRecordDao.listUserHeadWearGetRecord(uid);
//                for (HeadwearGetRecordDO recordDO : headwearGetRecords) {
//                    if (recordDO.getType() == HeadwearGetType.first_charge.getValue() && recordDO.getHeadwearId() == headwearId) {// 存在
//                        hadHeadwear = true;
//                    }
//                }
//                if (!hadCar || !hadHeadwear) {
//                    logger.info("[ 累冲赠送 ] 送头饰，uid:{} headwearId:{}", uid, headwearId);
//                    HeadwearDTO headwearDto = headwearManager.getHeadwear(headwearId);
//                    if (headwearDto != null) {
//                        List<String> headwearIds = headwearManager.listUserHeadwearid(uid);
//                        if (headwearIds.contains(headwearId + "")) {
//                            headwearIds.add(headwearId + "");
//                        }
//                        redisManager.hset(RedisKey.headwear_purse_list.getKey(), uid.toString(), StringUtils.join(headwearIds, ","));
//                        headwearManager.saveUserHeadwear(uid, headwearId, date, HeadwearGetType.first_charge.getValue(),
//                                null);
//                    }
//
//                    logger.info("[ 累冲赠送 ] 送座驾，uid:{}, carId:{}", uid, carId);
//                    GiftCarDTO carDTO = giftCarManager.getGiftCar(carId);
//                    if (carDTO != null) {
//                        List<String> carids = giftCarManager.listUserCarids(uid);
//                        if (!carids.contains(carId + "")) {
//                            carids.add(carId + "");
//                        }
//                        redisManager.hset(RedisKey.gift_car_purse_list.getKey(), uid.toString(), StringUtils.join(carids, ","));
//                        // 保存用户座驾信息
//                        giftCarManager.saveUserCar(uid, carDTO.getCarId(), date, CarGetType.first_charge.getValue(),
//                                "恭喜您参与累计充值送专属中获得了：限定专属【" + (headwearDto != null ? headwearDto.getHeadwearName() : "未知") + "】头饰"
//                                        + date + "天,【" + carDTO.getCarName() + "】" + date + "天,已发放请查收哦~");
//                    }
//                }
//            }

        } catch (Exception e) {
            logger.error("[ initFirstCharge error ]" + e.getMessage());
        }
    }

    @Override
    public Integer getUserCharge(Long uid) {
        String str = redisManager.hget(RedisKey.user_charge.getKey(), uid.toString());
        if (StringUtils.isBlank(str)) {
            Integer result = jdbcTemplate.queryForObject("SELECT SUM(c.amount) FROM charge_record c WHERE c.uid = ? AND c.charge_status = '2' AND NOT c.charge_prod_id IN ('exchange', 'company')", Integer.class, uid);
            if (result == null) result = 0;
            result = result / 100;
            redisManager.hset(RedisKey.user_charge.getKey(), uid.toString(), result.toString());
            return result;
        } else {
            return Integer.valueOf(str);
        }
    }

    @Value("${common.system.env}")
    private String env = "prod";

    @Override
    public void testCharge(Long uid, String chargeProdId) throws WebServiceException {
        if (env.equals("prod")) {
            logger.error("[ testCharge error ] 环境不允许");
            throw new WebServiceException(WebServiceCode.BUSI_ERROR);
        }

        String payChannel = "WEIXIN_APP3";
        JoinpayChannel channel;
        // 支持的充值方式，支付宝app充值和微信公众号充值
        try {
            channel = JoinpayChannel.valueOf(payChannel);
        } catch (IllegalArgumentException e) {
            throw new WebServiceException(WebServiceCode.PAY_CHANNEL_NOT_EXISTS);
        }
        if (channel == null) {
            logger.error("[ 发起充值支付 ] 不支持的充值支付渠道，payChannel:>{}", payChannel);
            throw new WebServiceException(WebServiceCode.CHARGE_NOCHANNEL);
        }

        ChargeProdDTO prodDto = prodManager.getChargeProd(chargeProdId);
        if (prodDto == null) {
            logger.error("[ 发起充值支付 ] 充值产品数据库中不存在，chargeProdId:>{}", chargeProdId);
            throw new WebServiceException("充值产品不存在");
        }

        int money = prodDto.getMoney();

        Integer chargeGoldAmount = prodDto.getChangeGoldRate() * money;

        UserPurseDTO userPurseDto = userPurseManager.getUserPurse(uid);
        // 判断是否首次充值（如果首次充值还需要加上首次充值赠送的金币数量）
        boolean isFirstCharge = userPurseDto.getIsFirstCharge();

        if (isFirstCharge) {// 首笔充值送头饰
            firstChargeBag(userPurseDto.getUid());
            chargeGoldAmount = chargeGoldAmount + prodDto.getFirstGiftGoldNum();
        }

        userPurseManager.updateAddGold(uid, (long) chargeGoldAmount, true, true, "汇聚充值", null, null);

        int amount = prodDto.getMoney() * 100;
        chargeActivity(uid, amount, prodDto.getChargeProdId());

        // 数据库单位元
        String body = prodDto.getProdName() + "模拟充值";
        String subject = prodDto.getProdName() + "模拟充值";
        ChargeRecordDO chargeRecord = new ChargeRecordDO();
        chargeRecord.setChargeRecordId(String.valueOf(System.currentTimeMillis()));
        chargeRecord.setChargeProdId(prodDto.getChargeProdId());
        chargeRecord.setUid(uid);
        chargeRecord.setChannel(channel.toString());
        chargeRecord.setBussType(ChargeRecordBussType.CHARGE.getValue());
        chargeRecord.setChargeStatus(ChargeRecordStatus.FINISH.getValue());
        chargeRecord.setAmount(amount);
        chargeRecord.setTotalGold((long) chargeGoldAmount);
        chargeRecord.setSubject(subject);
        chargeRecord.setBody(body);
        //充值描述
        chargeRecord.setChargeDesc(body);
        chargeRecord.setCreateTime(new Date());
        recordDao.save(chargeRecord);
    }

    @Deprecated
    private void saveBillRecord(Long uid, String objId, BillRecordType objType, Long goldNum, Integer money) {
        Date date = new Date();
        BillRecordDO billRecord = new BillRecordDO();
        billRecord.setBillId(UUIDUtils.get());
        billRecord.setUid(uid);
        billRecord.setTargetUid(uid);
        billRecord.setObjId(objId);
        billRecord.setObjType(objType);
        billRecord.setDiamondNum(null);
        billRecord.setGoldNum(goldNum);
        billRecord.setMoney(money.longValue());
        billRecord.setCreateTime(date);
        billRecordDao.save(billRecord);
    }

    @Override
    public void firstChargeBag(Long uid) {
        String str = redisManager.hget(RedisKey.first_charge.getKey(), uid.toString());
        if (StringUtils.isNotBlank(str)) {
            return;
        }

        Integer headwearId = 71;    // 首冲 新贵头饰Id
        Integer headwearDate = 7;   // 首冲头饰时长
        logger.info("[ 首冲赠送 ] 送头饰，uid:{} headwearId:{}", uid, headwearId);
        HeadwearDTO headwearDto = headwearManager.getHeadwear(headwearId);
        if (headwearDto != null) {
            List<String> headwearIds = headwearManager.listUserHeadwearid(uid);
            if (headwearIds.contains(headwearId.toString())) {
                headwearIds.add(headwearId.toString());
            }
            redisManager.hset(RedisKey.headwear_purse_list.getKey(), uid.toString(), StringUtils.join(headwearIds, ","));
            headwearManager.saveUserHeadwear(uid, headwearId, headwearDate, HeadwearGetType.first_charge.getValue(),
                    "恭喜首笔充值成功，获得【" + headwearDto.getHeadwearName() + "】头饰" + headwearDate.toString() + "天,已发放请查收！");
        }

        redisManager.hset(RedisKey.first_charge.getKey(), uid.toString(), "1");
    }

    @Override
    public UsersDTO checkUser(Long userNo, Long uid) throws WebServiceException {
        UsersDTO result = new UsersDTO();
        UsersDTO usersDTO;
        if (userNo != null) {
            usersDTO = usersManager.getUserByErbanNo(userNo);
        } else if (uid != null) {
            usersDTO = usersManager.getUser(uid);
        } else {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        result.setErbanNo(usersDTO.getErbanNo());
        result.setNick(usersDTO.getNick());
        return result;
    }


    @Override
    public Charge webApply(Long userNo, String chargeProdId, String payChannel, String clientIp, String successUrl) throws Exception {
        if (StringUtils.isEmpty(payChannel) || userNo == null || StringUtils.isEmpty(chargeProdId) || StringUtils.isEmpty(clientIp)) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        // 支持的充值方式，支付宝app充值和微信公众号充值
        ChargePayChannel channel = ChargePayChannel.valueOf(payChannel);
        if (channel == null) {
            logger.error("[ web发起充值支付 ] 不支持的充值支付渠道，payChannel:>{}", payChannel);
            throw new WebServiceException(WebServiceCode.CHARGE_NOCHANNEL);
        }

        ChargeProdDTO prodDto = prodManager.getChargeProd(chargeProdId);
        if (prodDto == null) {
            logger.error("[ web发起充值支付 ] 充值产品数据库中不存在，chargeProdId:>{}", chargeProdId);
            throw new WebServiceException("充值产品不存在");
        }
        UsersDTO usersDTO = usersManager.getUserByErbanNo(userNo);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        // 保存充值记录
        int amount = prodDto.getMoney() * 100;// 支付宝充值，以分为单位
        String body = prodDto.getProdName() + "金币充值";
        String subject = prodDto.getProdName() + "金币充值";
        String chargeRecordId = UUIDUtils.get();

        Charge charge = pingxxManager.createCharge(chargeRecordId, channel, amount, subject, body, clientIp, successUrl);

        ChargeRecordDO chargeRecord = new ChargeRecordDO();
        chargeRecord.setChargeRecordId(chargeRecordId);
        chargeRecord.setChargeProdId(chargeProdId);
        chargeRecord.setUid(usersDTO.getUid());
        chargeRecord.setChannel(payChannel);
        chargeRecord.setBussType(ChargeRecordBussType.CHARGE.getValue());
        chargeRecord.setChargeStatus(ChargeRecordStatus.CREATE.getValue());
        chargeRecord.setAmount(amount);
        chargeRecord.setSubject(subject);
        chargeRecord.setBody(body);
        chargeRecord.setChargeDesc(body);//充值描述
        chargeRecord.setClientIp(clientIp);
        chargeRecord.setCreateTime(new Date());
        chargeRecord.setPingxxChargeId(charge.getId());
        recordDao.save(chargeRecord);
        logger.info("[ web创建订单 {} ] 创建ping++订单成功，pingxxChargeId:>{}", chargeRecordId, charge.getId());
        return charge;
    }

    @Override
    public boolean checkApply(String chargeRecordId) {
        ChargeRecordDTO chargeRecordDto = recordDao.getChargeRecord(chargeRecordId);
        if (chargeRecordDto == null) {
            return false;
        }
        if (chargeRecordDto.getChargeStatus().intValue() == 2) {
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Object joinpayCharge(Long uid, String chargeProdId, String payChannel, String clientIp, String successUrl, String openId) throws Exception {
        UsersDTO usersDTO = usersManager.getUser(uid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        JoinpayChannel channel;
        // 支持的充值方式，支付宝app充值和微信公众号充值
        try {
            channel = JoinpayChannel.valueOf(payChannel);
        } catch (IllegalArgumentException e) {
            throw new WebServiceException(WebServiceCode.PAY_CHANNEL_NOT_EXISTS);
        }
        if (channel == null) {
            logger.error("[ 发起充值支付 ] 不支持的充值支付渠道，payChannel:>{}", payChannel);
            throw new WebServiceException(WebServiceCode.CHARGE_NOCHANNEL);
        }

        ChargeProdDTO prodDto = prodManager.getChargeProd(chargeProdId);
        if (prodDto == null) {
            logger.error("[ 发起充值支付 ] 充值产品数据库中不存在，chargeProdId:>{}", chargeProdId);
            throw new WebServiceException("充值产品不存在");
        }

        teensMode(uid, prodDto.getMoney());

        // 保存充值记录
        int amount = prodDto.getMoney() * 100;// 数据库以分为单位
        String body = prodDto.getProdName() + "金币充值";
        String subject = prodDto.getProdName() + "金币充值";
        String chargeRecordId = UUIDUtils.get();
        JoinpayRet joinpayRet = joinpayManager.createCharge(chargeRecordId, prodDto.getMoney(), subject, body, clientIp, successUrl, payChannel, uid, openId);
        Object result;
        if ("ALIPAY_H5".equalsIgnoreCase(payChannel)) {
            JoinpayRet ret = new JoinpayRet();
            ret.setR2_OrderNo(joinpayRet.getR2_OrderNo());
            ret.setRc_Result(joinpayRet.getRc_Result());
            result = ret;
        } else if ("WEIXIN_GZH".equalsIgnoreCase(payChannel)) {
            result = joinpayRet;
        } else if ("WEIXIN_APP3".equalsIgnoreCase(payChannel)) {
            result = gson.fromJson(joinpayRet.getRc_Result(), AppJoinpayAPP3Ret.class);
        } else {
            AppJoinpayRet appJoinpayRet = gson.fromJson(joinpayRet.getRc_Result(), AppJoinpayRet.class);
            appJoinpayRet.setHmac(joinpayRet.getHmac());
            result = appJoinpayRet;
        }

        ChargeRecordDO chargeRecord = new ChargeRecordDO();
        chargeRecord.setChargeRecordId(chargeRecordId);
        chargeRecord.setChargeProdId(chargeProdId);
        chargeRecord.setUid(uid);
        chargeRecord.setChannel(payChannel);
        chargeRecord.setBussType(ChargeRecordBussType.CHARGE.getValue());
        chargeRecord.setChargeStatus(ChargeRecordStatus.CREATE.getValue());
        chargeRecord.setAmount(amount);
        chargeRecord.setSubject(subject);
        chargeRecord.setBody(body);
        chargeRecord.setChargeDesc(body);//充值描述
        chargeRecord.setClientIp(clientIp);
        chargeRecord.setCreateTime(new Date());
        chargeRecord.setPingxxChargeId(joinpayRet.getR7_TrxNo());
        recordDao.save(chargeRecord);
        logger.info("[ 创建订单2 {} ] 创建汇聚订单成功，pingxxChargeId:>{}", chargeRecordId, joinpayRet.getR7_TrxNo());
        return result;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void reciveJoinpayCharge(JoinpayReciver joinpayReciver) throws Exception {
        // 1.校验汇聚的charge
        JoinpayReciverRet joinpayReciverRet = joinpayManager.retrieve(joinpayReciver.getR1_MerchantNo(), joinpayReciver.getR2_OrderNo(), joinpayReciver.getHmac());

        // 2.校验本地的充值记录
        String redisKey = RedisKey.charge_lock.getKey(joinpayReciverRet.getR2_OrderNo());
        String lockVal = redisManager.lock(redisKey, 10000);
        try {
            if (StringUtils.isBlank(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            ChargeRecordDTO chargeRecordDto = recordDao.getChargeRecord(joinpayReciverRet.getR2_OrderNo());
            if (chargeRecordDto == null) {
                logger.warn("[ 汇聚回调 {} ]不存在的订单，trxNo:>{}", joinpayReciverRet.getR2_OrderNo(), joinpayReciverRet.getR5_TrxNo());
                throw new WebServiceException(WebServiceCode.BUSI_ERROR);
            }

            if (!ChargeRecordStatus.CREATE.valueEquals(chargeRecordDto.getChargeStatus())) {
                logger.warn("[ 汇聚回调 {} ]该笔订单已经处理，trxNo:>{}，chargeStatus:>{}", joinpayReciverRet.getR2_OrderNo(), joinpayReciverRet.getR5_TrxNo(), chargeRecordDto.getChargeStatus());
                throw new WebServiceException(WebServiceCode.BUSI_ERROR);
            }

            // 3.按业务类型进行后续的处理
            if (ChargeRecordBussType.CHARGE.getValue() != chargeRecordDto.getBussType()) {
                logger.warn("[ 汇聚回调 {} ]业务不支持，trxNo:>{}，bussType:>{}", joinpayReciverRet.getR2_OrderNo(), joinpayReciverRet.getR5_TrxNo(), chargeRecordDto.getBussType());
                throw new WebServiceException("支付回调失败，原因：业务不存在");
            }

            String chargeRecordProdId = chargeRecordDto.getChargeProdId();
            int money;
            Double amount = Double.valueOf(joinpayReciverRet.getR3_Amount());
            Long chargeGoldAmount;

            // 正常金币充值
            ChargeProdDTO chargeProdDto = prodManager.getChargeProd(chargeRecordProdId);
            money = chargeProdDto.getMoney();

            // 充值金额与实际价格不一致
            if (amount.intValue() != money) {
                logger.warn("[ 汇聚回调 {} ]金额对比异常，trxNo:>{}，chargeAmount:>{}，chargeProdMoney:>{}", joinpayReciverRet.getR2_OrderNo(), joinpayReciverRet.getR5_TrxNo(), joinpayReciverRet.getR3_Amount(), chargeProdDto.getMoney());
                throw new WebServiceException("支付回调失败，原因：数据异常");
            }

            // 转换后的金币数
            chargeGoldAmount = chargeProdDto.getChangeGoldRate().longValue() * money;
            // 赠送金币数量(每次充值均赠送)
            Integer giftGoldNum = chargeProdDto.getGiftGoldNum();
            // 赠送金币与购买的金币
            chargeGoldAmount = chargeGoldAmount + giftGoldNum.longValue();
            UserPurseDTO userPurseDto = userPurseManager.getUserPurse(chargeRecordDto.getUid());
            // 判断是否首次充值（如果首次充值还需要加上首次充值赠送的金币数量）
            boolean isFirstCharge = userPurseDto.getIsFirstCharge();

            if (isFirstCharge) { // 首笔充值送头饰
                firstChargeBag(userPurseDto.getUid());
                chargeGoldAmount = chargeGoldAmount + chargeProdDto.getFirstGiftGoldNum();
            }
            userPurseManager.updateAddGold(chargeRecordDto.getUid(), chargeGoldAmount, true, true, "汇聚充值", null, null);
            // 邀请分成
//            shareRecordManager.saveUserBonusRecord(chargeRecordDto.getUid(), null, chargeRecordProdId, money * 100);
            // 抽奖机会
//                userDrawManager.saveChargeDraw(chargeRecordDto.getUid(), money * 100, joinpayReciverRet.getR5_TrxNo());
            //新手礼包
            chargeActivity(chargeRecordDto.getUid(), chargeRecordDto.getAmount(), chargeRecordProdId);

            JSONObject object = new JSONObject();
            object.put("uid", chargeRecordDto.getUid());
            object.put("chargeRecordProdId", chargeRecordProdId);
            object.put("amount", chargeRecordDto.getAmount());
            object.put("chargeId", joinpayReciverRet.getR1_MerchantNo());
            object.put("money", chargeRecordDto.getAmount());

            activeMqManager.sendQueueMessage(MqDestinationKey.CHARGE_QUEUE, object.toJSONString());


            ChargeRecordDO recordDo = new ChargeRecordDO();
            recordDo.setChargeRecordId(chargeRecordDto.getChargeRecordId());
            recordDo.setChargeStatus(ChargeRecordStatus.FINISH.getValue());
            recordDo.setTotalGold(chargeGoldAmount);
            recordDo.setUpdateTime(new Date());
            recordDao.update(recordDo);

            BillGoldChargeDO chargeDo = new BillGoldChargeDO();
            chargeDo.setChargeId(chargeRecordDto.getChargeRecordId());
            chargeDo.setUid(chargeRecordDto.getUid());
            chargeDo.setGoldAmount(chargeGoldAmount.intValue());
            chargeDo.setMoney(money);
            chargeDo.setCreateTime(new Date());
            goldChargeDao.save(chargeDo);

            // FIXME: 管理后台切换表之后删除
            this.saveBillRecord(chargeRecordDto.getUid(), chargeRecordDto.getChargeRecordId(), BillRecordType.charge, chargeGoldAmount, money);
        } catch (Exception e) {
            throw e;
        } finally {
            redisManager.unlock(redisKey, lockVal);
        }
    }

    @Override
    public Object joinpayWebApply(Long userNo, String chargeProdId, String payChannel, String clientIp, String successUrl, String openId) throws Exception {
        UsersDTO usersDTO = usersManager.getUserByErbanNo(userNo);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        return joinpayCharge(usersDTO.getUid(), chargeProdId, payChannel, clientIp, successUrl, openId);
    }

    @Override
    public void reciveSandCharge(SandpayReciver sandpayReciver) throws WebServiceException {
        String redisKey = RedisKey.charge_lock.getKey(sandpayReciver.getOrderCode());
        String lockVal = redisManager.lock(redisKey, 10000);
        try {
            if (StringUtils.isBlank(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            ChargeRecordDTO chargeRecordDto = recordDao.getChargeRecord(sandpayReciver.getOrderCode());
            if (chargeRecordDto == null) {
                logger.error("[ 衫德回调 {} ]不存在的订单，orderNo:>{}", sandpayReciver.getOrderCode(), sandpayReciver.getOrderCode());
                throw new WebServiceException(WebServiceCode.BUSI_ERROR);
            }

            if (!ChargeRecordStatus.CREATE.valueEquals(chargeRecordDto.getChargeStatus())) {
                logger.error("[ 衫德回调 {} ]该笔订单已经处理，orderNo:>{}，chargeStatus:>{}", sandpayReciver.getOrderCode(), sandpayReciver.getOrderCode(), chargeRecordDto.getChargeStatus());
                throw new WebServiceException(WebServiceCode.BUSI_ERROR);
            }

            // 3.按业务类型进行后续的处理
            if (ChargeRecordBussType.CHARGE.getValue() != chargeRecordDto.getBussType()) {
                logger.error("[ 衫德回调 {} ]业务不支持，orderNo:>{}，bussType:>{}", sandpayReciver.getOrderCode(), sandpayReciver.getOrderCode(), chargeRecordDto.getBussType());
                throw new WebServiceException("支付回调失败，原因：业务不存在");
            }
            String chargeRecordProdId = chargeRecordDto.getChargeProdId();

            // 正常金币充值
            ChargeProdDTO chargeProdDto = prodManager.getChargeProd(chargeRecordProdId);
            int money = chargeProdDto.getMoney();

            Double amount = Double.valueOf(sandpayReciver.getTotalAmount()) / 100; // 实际金额
            int chargeGoldAmount;

            // 充值金额与实际价格不一致
            if (amount.intValue() != money) {
                logger.error("[ 衫德回调 {} ]金额对比异常，orderNo:>{}，chargeAmount:>{}，chargeProdMoney:>{}", sandpayReciver.getOrderCode(), sandpayReciver.getOrderCode(), sandpayReciver.getTotalAmount(), chargeProdDto.getMoney());
                throw new WebServiceException("支付回调失败，原因：数据异常");
            }

            // 充值金币数
            chargeGoldAmount = chargeProdDto.getGoldNum();
            // 赠送金币数量(每次充值均赠送)
            Integer giftGoldNum = chargeProdDto.getGiftGoldNum();
            // 赠送金币与购买的金币
            chargeGoldAmount = chargeGoldAmount + giftGoldNum;
            UserPurseDTO userPurseDto = userPurseManager.getUserPurse(chargeRecordDto.getUid());
            // 判断是否首次充值（如果首次充值还需要加上首次充值赠送的金币数量）
            boolean isFirstCharge = userPurseDto.getIsFirstCharge();

//            if (isFirstCharge) { // 首笔充值送头饰
//                firstChargeBag(userPurseDto.getUid());
//                chargeGoldAmount = chargeGoldAmount + chargeProdDto.getFirstGiftGoldNum();
//            }
            userPurseManager.updateAddGold(chargeRecordDto.getUid(), (long) chargeGoldAmount, true, false, "衫德充值", null, null);
            // 邀请分成
//            shareRecordManager.saveUserBonusRecord(chargeRecordDto.getUid(), null, chargeRecordProdId, money * 100);
            // 抽奖机会
//                userDrawManager.saveChargeDraw(chargeRecordDto.getUid(), money * 100, joinpayReciverRet.getR5_TrxNo());
            //新手礼包
            chargeActivity(chargeRecordDto.getUid(), chargeRecordDto.getAmount(), chargeRecordProdId);

            JSONObject object = new JSONObject();
            object.put("uid", chargeRecordDto.getUid());
            object.put("chargeRecordProdId", chargeRecordProdId);
            object.put("amount", chargeRecordDto.getAmount());
            // 商户号
            object.put("chargeId", sandpayReciver.getMid());
            object.put("money", chargeRecordDto.getAmount());

            activeMqManager.sendQueueMessage(MqDestinationKey.CHARGE_QUEUE, object.toJSONString());

            ChargeRecordDO recordDo = new ChargeRecordDO();
            recordDo.setChargeRecordId(chargeRecordDto.getChargeRecordId());
            recordDo.setChargeStatus(ChargeRecordStatus.FINISH.getValue());
            recordDo.setTotalGold((long) chargeGoldAmount);
            recordDo.setUpdateTime(new Date());
            // todo 交易流水号??
            recordDo.setPingxxChargeId(sandpayReciver.getPayordercode());
            recordDao.update(recordDo);

            BillGoldChargeDO chargeDo = new BillGoldChargeDO();
            chargeDo.setChargeId(chargeRecordDto.getChargeRecordId());
            chargeDo.setUid(chargeRecordDto.getUid());
            chargeDo.setGoldAmount(chargeGoldAmount);
            chargeDo.setMoney(money);
            chargeDo.setCreateTime(new Date());
            goldChargeDao.save(chargeDo);

            // FIXME: 管理后台切换表之后删除
            this.saveBillRecord(chargeRecordDto.getUid(), chargeRecordDto.getChargeRecordId(), BillRecordType.charge, (long) chargeGoldAmount, money);
        } catch (Exception e) {
            throw e;
        } finally {
            redisManager.unlock(redisKey, lockVal);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String sandpayCharge(Long uid, String chargeProdId, String clientIp, String payChannel, String successUrl) throws Exception {
        SandChannel channel;
        // 支持的充值方式，支付宝app充值和微信公众号充值
        try {
            channel = SandChannel.valueOf(payChannel);
        } catch (IllegalArgumentException e) {
            throw new WebServiceException(WebServiceCode.PAY_CHANNEL_NOT_EXISTS);
        }
        if (channel == null) {
            logger.error("[ 发起充值支付 ] 不支持的充值支付渠道，payChannel:>{}", payChannel);
            throw new WebServiceException(WebServiceCode.CHARGE_NOCHANNEL);
        }

        UsersDTO usersDTO = usersManager.getUser(uid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        ChargeProdDTO prodDto = prodManager.getChargeProd(chargeProdId);
        if (prodDto == null) {
            logger.error("[ 发起充值支付 ] 充值产品数据库中不存在，chargeProdId:>{}", chargeProdId);
            throw new WebServiceException("充值产品不存在");
        }

        teensMode(uid, prodDto.getMoney());

        // 保存充值记录
        int amount = prodDto.getMoney() * 100;  // 以分为单位
//        int midFree = prodDto.getMoney();       // 0.01 * 100 = 1 衫德收取0.85%的手续费 我们直接收取1%（以分为单位）
        String body = prodDto.getProdName() + "金币充值";
        String subject = prodDto.getProdName();
        String chargeRecordId = UUIDUtils.get();
        // 注意衫德 金额要求：12位，最小单位分 金额不足前端以0计 如0.02 = 000000000002
        String result = sandpayManager.createCharge(chargeRecordId, amount, subject, clientIp, uid);

        ChargeRecordDO chargeRecord = new ChargeRecordDO();
        chargeRecord.setChargeRecordId(chargeRecordId);
        chargeRecord.setChargeProdId(chargeProdId);
        chargeRecord.setUid(uid);
        chargeRecord.setChannel(payChannel);
        chargeRecord.setBussType(ChargeRecordBussType.CHARGE.getValue());
        chargeRecord.setChargeStatus(ChargeRecordStatus.CREATE.getValue());
        chargeRecord.setAmount(amount);
//        chargeRecord.setMidFree(midFree);
        chargeRecord.setSubject(subject);
        chargeRecord.setBody(body);
        chargeRecord.setChargeDesc(body);//充值描述
        chargeRecord.setClientIp(clientIp);
        chargeRecord.setCreateTime(new Date());
        chargeRecord.setPingxxChargeId(chargeRecordId);
        recordDao.save(chargeRecord);
        logger.info("[ 创建订单2 {} ] 创建衫德订单成功", chargeRecordId);
        return result;
    }

    @Override
    public String sandWebApply(Long userNo, String chargeProdId, String clientIp, String payChannel, String successUrl) throws Exception {
        UsersDTO usersDTO = usersManager.getUserByErbanNo(userNo);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        return sandpayCharge(usersDTO.getUid(), chargeProdId, clientIp, payChannel, successUrl);
    }


    @Override
    public EcpssAlipayVO ecpssApply(Long uid, String chargeProdId, String app, String clientIp) throws WebServiceException {
        UsersDTO usersDTO = usersManager.getUser(uid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        ChargeProdDTO prodDto = prodManager.getChargeProd(chargeProdId);
        if (prodDto == null) {
            logger.error("[ 发起充值支付 ] 充值产品数据库中不存在，chargeProdId:>{}", chargeProdId);
            throw new WebServiceException("充值产品不存在");
        }
        return new EcpssAlipayVO(ecpssAlipay(usersDTO, prodDto, app, clientIp, EcpssChannel.ecpss_alipay));
    }


    @Override
    public EcpssAlipayVO ecpssWebApply(Long userNo, String chargeProdId, String clientIp) throws WebServiceException {
        UsersDTO usersDTO = usersManager.getUserByErbanNo(userNo);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        ChargeProdDTO prodDto = prodManager.getChargeProd(chargeProdId);
        if (prodDto == null) {
            logger.error("[ 发起充值支付 ] 充值产品数据库中不存在，chargeProdId:>{}", chargeProdId);
            throw new WebServiceException("充值产品不存在");
        }
        return new EcpssAlipayVO(ecpssAlipay(usersDTO, prodDto, WEB_APP, clientIp, EcpssChannel.ecpss_alipay_h5));
    }

    /**
     * 支付宝支付
     *
     * @param usersDTO
     * @param prodDto
     * @param clientIp
     * @return 支付调转的链接
     */
    private String ecpssAlipay(UsersDTO usersDTO, ChargeProdDTO prodDto, String app, String clientIp, EcpssChannel channel) {
        String chargeRecordId = UUIDUtils.get();
        // 数据库单位元
        int amount = prodDto.getMoney() * 100;
        String body = prodDto.getProdName() + "金币充值";
        String subject = prodDto.getProdName() + "金币充值";
        ChargeRecordDO chargeRecord = new ChargeRecordDO();
        chargeRecord.setChargeRecordId(chargeRecordId);
        chargeRecord.setChargeProdId(prodDto.getChargeProdId());
        chargeRecord.setUid(usersDTO.getUid());
        chargeRecord.setChannel(channel.toString());
        chargeRecord.setBussType(ChargeRecordBussType.CHARGE.getValue());
        chargeRecord.setChargeStatus(ChargeRecordStatus.CREATE.getValue());
        chargeRecord.setAmount(amount);
        chargeRecord.setSubject(subject);
        chargeRecord.setBody(body);
        //充值描述
        chargeRecord.setChargeDesc(body);
        chargeRecord.setClientIp(clientIp);
        chargeRecord.setCreateTime(new Date());
        recordDao.save(chargeRecord);
        // 创建支付的URL
        return ecpssManager.alipayOrder(chargeRecordId, amount, usersDTO.getUid(), body);
    }

    @Override
    public String reciveEcpssCharge(OrderCallBackBO callBackBO) throws WebServiceException {
        // 回调签名验证
        ecpssManager.verifySignature(callBackBO);
        // 2.校验本地的充值记录
        String redisKey = RedisKey.charge_lock.getKey(callBackBO.getMerchantOutOrderNo());
        String lockVal = redisManager.lock(redisKey, 10000);
        try {
            if (StringUtils.isBlank(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            ChargeRecordDTO chargeRecordDto = recordDao.getChargeRecord(callBackBO.getMerchantOutOrderNo());
            if (chargeRecordDto == null) {
                logger.warn("[ 汇潮回调 {} ]不存在的订单，orderNo:>{}", callBackBO.getMerchantOutOrderNo(), callBackBO.getOrderNo());
                throw new WebServiceException(WebServiceCode.BUSI_ERROR);
            }

            if (!ChargeRecordStatus.CREATE.valueEquals(chargeRecordDto.getChargeStatus())) {
                logger.warn("[ 汇潮回调 {} ]该笔订单已经处理，orderNo:>{}，chargeStatus:>{}", callBackBO.getMerchantOutOrderNo(), callBackBO.getOrderNo(), chargeRecordDto.getChargeStatus());
                throw new WebServiceException(WebServiceCode.BUSI_ERROR);
            }

            // 3.按业务类型进行后续的处理
            if (ChargeRecordBussType.CHARGE.getValue() != chargeRecordDto.getBussType()) {
                logger.warn("[ 汇潮回调 {} ]业务不支持，orderNo:>{}，bussType:>{}", callBackBO.getMerchantOutOrderNo(), callBackBO.getOrderNo(), chargeRecordDto.getBussType());
                throw new WebServiceException("支付回调失败，原因：业务不存在");
            }
            int money;
            String chargeRecordProdId = chargeRecordDto.getChargeProdId();

            ChargeProdDTO chargeProdDto = prodManager.getChargeProd(chargeRecordProdId);
            money = chargeProdDto.getMoney();
            String msg = callBackBO.getMsg();
            if (StringUtils.isBlank(msg)) {
                logger.warn("[ 汇潮回调 {} ]订单详情为空，orderNo:>{}，msg:>{}", callBackBO.getMerchantOutOrderNo(), callBackBO.getOrderNo(), msg);
                throw new WebServiceException(WebServiceCode.PARAM_ERROR);
            }
            JSONObject jsonObject = JSONObject.parseObject(msg);
            Double payMoney = jsonObject.getDoubleValue("payMoney");
            if (payMoney == null) {
                logger.warn("[ 汇潮回调 {} ]支付金额为空，orderNo:>{}，msg:>{}", callBackBO.getMerchantOutOrderNo(), callBackBO.getOrderNo(), msg);
                throw new WebServiceException(WebServiceCode.PARAM_ERROR);
            }
            // 充值金额与实际价格不一致
            if (payMoney.intValue() != money) {
                logger.warn("[ 汇潮回调 {} ]金额对比异常，orderNo:>{}，chargeAmount:>{}，chargeProdMoney:>{}", callBackBO.getMerchantOutOrderNo(), callBackBO.getOrderNo(), payMoney, chargeProdDto.getMoney());
                throw new WebServiceException("支付回调失败，原因：数据异常");
            }
            // 转换后的金币数
            Long chargeGoldAmount = chargeProdDto.getChangeGoldRate().longValue() * money;
            // 赠送金币数量(每次充值均赠送)
            Integer giftGoldNum = chargeProdDto.getGiftGoldNum();
            // 赠送金币与购买的金币
            chargeGoldAmount = chargeGoldAmount + giftGoldNum.longValue();
            UserPurseDTO userPurseDto = userPurseManager.getUserPurse(chargeRecordDto.getUid());
            // 判断是否首次充值（如果首次充值还需要加上首次充值赠送的金币数量）
            boolean isFirstCharge = userPurseDto.getIsFirstCharge();

            if (isFirstCharge) {// 首笔充值送头饰
                firstChargeBag(userPurseDto.getUid());
                chargeGoldAmount = chargeGoldAmount + chargeProdDto.getFirstGiftGoldNum();
            }
            userPurseManager.updateAddGold(chargeRecordDto.getUid(), chargeGoldAmount, true, true, "汇潮充值", null, null);
            // 邀请分成
//            shareRecordManager.saveUserBonusRecord(chargeRecordDto.getUid(), null, chargeRecordProdId, money * 100);
            // 抽奖机会
//            userDrawManager.saveChargeDraw(chargeRecordDto.getUid(), money * 100, callBackBO.getMerchantOutOrderNo());
            //新手礼包
            chargeActivity(chargeRecordDto.getUid(), chargeRecordDto.getAmount(), chargeRecordProdId);

            JSONObject object = new JSONObject();
            object.put("uid", chargeRecordDto.getUid());
            object.put("chargeRecordProdId", chargeRecordProdId);
            object.put("amount", chargeRecordDto.getAmount());
            object.put("chargeId", callBackBO.getMerchantOutOrderNo());
            object.put("money", chargeRecordDto.getAmount());

            activeMqManager.sendQueueMessage(MqDestinationKey.CHARGE_QUEUE, object.toJSONString());

            ChargeRecordDO recordDo = new ChargeRecordDO();
            recordDo.setChargeRecordId(chargeRecordDto.getChargeRecordId());
            recordDo.setChargeStatus(ChargeRecordStatus.FINISH.getValue());
            recordDo.setTotalGold(chargeGoldAmount);
            recordDo.setUpdateTime(new Date());
            recordDao.update(recordDo);

            BillGoldChargeDO chargeDo = new BillGoldChargeDO();
            chargeDo.setChargeId(chargeRecordDto.getChargeRecordId());
            chargeDo.setUid(chargeRecordDto.getUid());
            chargeDo.setGoldAmount(chargeGoldAmount.intValue());
            chargeDo.setMoney(money);
            chargeDo.setCreateTime(new Date());
            goldChargeDao.save(chargeDo);

            // FIXME: 管理后台切换表之后删除
            this.saveBillRecord(chargeRecordDto.getUid(), chargeRecordDto.getChargeRecordId(), BillRecordType.charge, chargeGoldAmount, money);
        } finally {
            redisManager.unlock(redisKey, lockVal);
        }
        // 支付成功  返回
        return "success";
    }

}
