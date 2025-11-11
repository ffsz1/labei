package com.juxiao.xchat.service.api.charge.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.DefMsgType;
import com.juxiao.xchat.base.constant.DiamondExchangeGold;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.*;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.bill.BillGoldExchangeDao;
import com.juxiao.xchat.dao.bill.BillRecordDao;
import com.juxiao.xchat.dao.bill.BillUserGiveRecordDao;
import com.juxiao.xchat.dao.bill.domain.BillGoldExchangeDO;
import com.juxiao.xchat.dao.bill.domain.BillRecordDO;
import com.juxiao.xchat.dao.bill.enumeration.BillRecordType;
import com.juxiao.xchat.dao.charge.ChargeRecordDao;
import com.juxiao.xchat.dao.charge.ExchangeDiamondGoldRecordDao;
import com.juxiao.xchat.dao.charge.domain.ChargeRecordDO;
import com.juxiao.xchat.dao.charge.domain.ExchangeDiamondGoldRecordDO;
import com.juxiao.xchat.dao.charge.domain.UserGiveRecordDo;
import com.juxiao.xchat.dao.charge.enumeration.ChargePayChannel;
import com.juxiao.xchat.dao.charge.enumeration.ChargeRecordStatus;
import com.juxiao.xchat.dao.item.dto.GiftCarDTO;
import com.juxiao.xchat.dao.item.enumeration.CarGetType;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;
import com.juxiao.xchat.dao.user.AccountDao;
import com.juxiao.xchat.dao.user.dto.*;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.item.GiftCarManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.sysconf.SysConfManager;
import com.juxiao.xchat.manager.common.user.UserPurseManager;
import com.juxiao.xchat.manager.common.user.UserRealNameManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.netease.NetEaseMsgManager;
import com.juxiao.xchat.manager.external.netease.NetEaseSmsManager;
import com.juxiao.xchat.manager.external.netease.bo.Body;
import com.juxiao.xchat.manager.external.netease.bo.NeteaseMsgBO;
import com.juxiao.xchat.service.api.charge.DiamondExchangeService;
import com.juxiao.xchat.service.api.charge.vo.GiveGoldVO;
import com.juxiao.xchat.service.api.charge.vo.TransferG2GVo;
import com.juxiao.xchat.service.api.charge.vo.WithDrawCashVO;
import com.juxiao.xchat.service.api.user.bo.UserAuthorityVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 钻石兑换金币业务处理
 *
 * @class: DiamondExchangeServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/6/8
 */
@Service
@Slf4j
public class DiamondExchangeServiceImpl implements DiamondExchangeService {
    @Resource
    private BillGoldExchangeDao exchangeBillDao;

    @Resource
    private BillRecordDao recordDao;

    @Resource
    private ChargeRecordDao chargeRecordDao;

    @Resource
    private ExchangeDiamondGoldRecordDao exchangeRecordDao;

    @Resource
    private GiftCarManager carManager;

    @Resource
    private GiftCarManager purseManager;

    @Resource
    private RoomManager roomManager;

    @Resource
    private SysConfManager sysconfManager;

    @Resource
    private UserRealNameManager userRealNameManager;

    @Resource
    private UsersManager usersManager;

    @Resource
    private UserPurseManager userPurseManager;

    @Resource(name = "CaihSmsManager")
    private NetEaseSmsManager neteaseSmsManager;

    @Autowired
    private NetEaseMsgManager neteaseMsgManager;

    @Resource
    private BillUserGiveRecordDao billUserGiveRecordDao;

    @Resource
    private RedisManager redisManager;

    @Resource
    private Gson gson;

    @Autowired
    private AccountDao accountDao;

    private final long LIMIT_VERSION = Utils.version2long("1.0.22");

    /**
     * @see com.juxiao.xchat.service.api.charge.DiamondExchangeService#exchange2Gold(Long, Double, String, String, String, String)
     * 从使用验证码兑换改为使用二级密码兑换
     */
    @Override
    public UserPurseExchangeDTO exchange2Gold(Long uid, Double diamondCost, String os, String appVersion,
                                              String phone, String passwordSecond) throws WebServiceException {
        // 参数判断
        if (uid == null || uid == 0L || diamondCost == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        if (diamondCost <= 0) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        if (diamondCost % 10 != 0) {
            throw new WebServiceException(WebServiceCode.EXCHANGE_INPUT_ERROR);
        }

        // 1.0.22 版本必须增加短信验证码
//        if (Utils.version2long(appVersion) < LIMIT_VERSION) {
//            throw new WebServiceException("请更新版本");
//        }

        if (StringUtils.isEmpty(phone)) {
            throw new WebServiceException(WebServiceCode.PHONE_INVALID);
        }

        if (StringUtils.isEmpty(passwordSecond)) {
            throw new WebServiceException(WebServiceCode.PASSWORD_ERROR);
        }

        AccountDTO accountDTO = accountDao.getAccount(uid);
        if (accountDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
//        UsersDTO userDto = usersManager.getUser(uid);
//        if (userDto == null) {
//            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
//        }

        String userPhone = accountDTO.getPhone();
        if (StringUtils.isEmpty(userPhone)) {
            throw new WebServiceException("请先绑定手机");
        }
        if (!phone.equals(userPhone)) {
            throw new WebServiceException("该手机号与本账号绑定的手机号不一致");
        }

        //检验验证码
//        if (!neteaseSmsManager.verifySmsCode(phone, smsCode)) {
//            throw new WebServiceException(WebServiceCode.SMS_CODE_ERROR);
//        }

        passwordSecond = MD5Utils.getMD5(passwordSecond);
        if (!passwordSecond.equals(accountDTO.getPasswordSecond())) {
            throw new WebServiceException(WebServiceCode.PASSWORD_SECOND_ERROR);
        }

        UserPurseDTO purseDto = userPurseManager.getUserPurse(uid);
        if (purseDto == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        // 进行兑换
        double totalDiamondNum = purseDto.getDiamondNum();
        if (diamondCost > totalDiamondNum) {
            throw new WebServiceException(WebServiceCode.DIAMOND_NUM_NOT_ENOUGH);
        }

        Double goldAmount = diamondCost * DiamondExchangeGold.rate;
        UserPurseExchangeDTO exchangeDto = new UserPurseExchangeDTO();
        BeanUtils.copyProperties(purseDto, exchangeDto);

        long drawGoldAmount = 0;
//        // 是否抽奖
//        if (isDoDraw(goldAmount.longValue())) {
//            drawGoldAmount = this.draw(goldAmount, exchangeDto);
//        }

        // 保存相关记录
        Long finalGoldAmount = goldAmount.longValue() + drawGoldAmount;
        userPurseManager.updateAddGoldReduceDiamond(uid, finalGoldAmount, diamondCost, true);

        purseDto = userPurseManager.getUserPurse(uid);
        BeanUtils.copyProperties(purseDto, exchangeDto);
        String reocrdId = UUIDUtils.get();
        this.saveExchageRecord(reocrdId, uid, diamondCost, finalGoldAmount);
        this.saveBillRecord(reocrdId, uid, diamondCost, finalGoldAmount);
        this.saveChargeRecord(reocrdId, uid, diamondCost, finalGoldAmount);
        return exchangeDto;
    }

    private long draw(Double goldAmount, UserPurseExchangeDTO exchangeDto) throws WebServiceException {
        double randomNumber = RandomUtils.randomDouble();
        double drawGold = 0;
        Integer carId = null;
        Integer carDate = 0;
        GiftCarDTO giftCar = null;
        if (goldAmount < 1000) {
            if (randomNumber >= DiamondExchangeGold.EXCHANGE_RANGE_100[0] && randomNumber <= DiamondExchangeGold.EXCHANGE_RANGE_100[1]) {
                drawGold = 0;
            } else if (randomNumber > DiamondExchangeGold.EXCHANGE_RANGE_100[1] && randomNumber <= DiamondExchangeGold.EXCHANGE_RANGE_100[2]) {
                drawGold = goldAmount * DiamondExchangeGold.EXCHANGE_DRAW1;
            } else if (randomNumber > DiamondExchangeGold.EXCHANGE_RANGE_100[2] && randomNumber <= DiamondExchangeGold.EXCHANGE_RANGE_100[3]) {
                drawGold = goldAmount * DiamondExchangeGold.EXCHANGE_DRAW2;
            } else if (randomNumber > DiamondExchangeGold.EXCHANGE_RANGE_100[3] && randomNumber <= DiamondExchangeGold.EXCHANGE_RANGE_100[4]) {
                drawGold = goldAmount * DiamondExchangeGold.EXCHANGE_DRAW3;
            } else if (randomNumber > DiamondExchangeGold.EXCHANGE_RANGE_100[4] && randomNumber <= DiamondExchangeGold.EXCHANGE_RANGE_100[5]) {
                carId = DiamondExchangeGold.EXCHANGE_DRAW7;
                carDate = 3;
            } else if (randomNumber > DiamondExchangeGold.EXCHANGE_RANGE_100[5] && randomNumber <= DiamondExchangeGold.EXCHANGE_RANGE_100[6]) {
                carId = DiamondExchangeGold.EXCHANGE_DRAW8;
                carDate = 3;
            }
        } else if (goldAmount < 5000) {
            if (randomNumber >= DiamondExchangeGold.EXCHANGE_RANGE_1000[0] && randomNumber <= DiamondExchangeGold.EXCHANGE_RANGE_1000[1]) {
                drawGold = 0D;
            } else if (randomNumber > DiamondExchangeGold.EXCHANGE_RANGE_1000[1] && randomNumber <= DiamondExchangeGold.EXCHANGE_RANGE_1000[2]) {
                drawGold = goldAmount * DiamondExchangeGold.EXCHANGE_DRAW1;
            } else if (randomNumber > DiamondExchangeGold.EXCHANGE_RANGE_1000[2] && randomNumber <= DiamondExchangeGold.EXCHANGE_RANGE_1000[3]) {
                drawGold = goldAmount * DiamondExchangeGold.EXCHANGE_DRAW2;
            } else if (randomNumber > DiamondExchangeGold.EXCHANGE_RANGE_1000[3] && randomNumber <= DiamondExchangeGold.EXCHANGE_RANGE_1000[4]) {
                drawGold = goldAmount * DiamondExchangeGold.EXCHANGE_DRAW3;
            } else if (randomNumber > DiamondExchangeGold.EXCHANGE_RANGE_1000[4] && randomNumber <= DiamondExchangeGold.EXCHANGE_RANGE_1000[5]) {
                drawGold = goldAmount * DiamondExchangeGold.EXCHANGE_DRAW4;
            } else if (randomNumber > DiamondExchangeGold.EXCHANGE_RANGE_1000[5] && randomNumber <= DiamondExchangeGold.EXCHANGE_RANGE_1000[6]) {
                carId = DiamondExchangeGold.EXCHANGE_DRAW7;
                carDate = 7;
            } else if (randomNumber > DiamondExchangeGold.EXCHANGE_RANGE_1000[6] && randomNumber <= DiamondExchangeGold.EXCHANGE_RANGE_1000[7]) {
                carId = DiamondExchangeGold.EXCHANGE_DRAW9;
                carDate = 7;
            }
        } else {
            if (randomNumber >= DiamondExchangeGold.EXCHANGE_RANGE_5000[0] && randomNumber <= DiamondExchangeGold.EXCHANGE_RANGE_5000[1]) {
                drawGold = 0D;
            } else if (randomNumber > DiamondExchangeGold.EXCHANGE_RANGE_5000[1] && randomNumber <= DiamondExchangeGold.EXCHANGE_RANGE_5000[2]) {
                drawGold = goldAmount * DiamondExchangeGold.EXCHANGE_DRAW2;
            } else if (randomNumber > DiamondExchangeGold.EXCHANGE_RANGE_5000[2] && randomNumber <= DiamondExchangeGold.EXCHANGE_RANGE_5000[3]) {
                drawGold = goldAmount * DiamondExchangeGold.EXCHANGE_DRAW3;
            } else if (randomNumber > DiamondExchangeGold.EXCHANGE_RANGE_5000[3] && randomNumber <= DiamondExchangeGold.EXCHANGE_RANGE_5000[4]) {
                drawGold = goldAmount * DiamondExchangeGold.EXCHANGE_DRAW4;
            } else if (randomNumber > DiamondExchangeGold.EXCHANGE_RANGE_5000[4] && randomNumber <= DiamondExchangeGold.EXCHANGE_RANGE_5000[5]) {
                drawGold = goldAmount * DiamondExchangeGold.EXCHANGE_DRAW5;
            } else if (randomNumber > DiamondExchangeGold.EXCHANGE_RANGE_5000[5] && randomNumber <= DiamondExchangeGold.EXCHANGE_RANGE_5000[6]) {
                carId = DiamondExchangeGold.EXCHANGE_DRAW10;
                carDate = 7;
            } else if (randomNumber > DiamondExchangeGold.EXCHANGE_RANGE_5000[6] && randomNumber <= DiamondExchangeGold.EXCHANGE_RANGE_5000[7]) {
                carId = DiamondExchangeGold.EXCHANGE_DRAW11;
                carDate = 7;
            }
        }

        // 中了座驾
        if (carId != null) {
            giftCar = carManager.getGiftCar(carId);
            if (giftCar == null) {
                throw new WebServiceException(WebServiceCode.GIFT_CAR_NOT_EXISTS);
            }
            exchangeDto.setDrawMsg("获得" + giftCar.getCarName() + carDate + "天使用权");
            exchangeDto.setDrawUrl(giftCar.getPicUrl());
        } else if (drawGold > 0) {
            exchangeDto.setDrawMsg("额外爆出+" + (long) drawGold);
            exchangeDto.setDrawUrl("https://pic.chaoxuntech.com/dh_jinbi.png");
        } else {
            exchangeDto.setDrawMsg("很遗憾，没有爆中");
            exchangeDto.setDrawUrl("0");
        }
        if (giftCar != null) {
            purseManager.saveUserCar(exchangeDto.getUid(), carId, carDate, CarGetType.diamond_excharge.getValue(),
                    null);
        }
        return (long) drawGold;
    }

    /**
     * 判断是否进入抽奖，如果大于100并且是新版本，添加抽奖
     *
     * @param exchangeGoldNum
     * @return
     * @author: chenjunsheng
     * @date 2018/6/8
     */
    private boolean isDoDraw(Long exchangeGoldNum) {
        SysConfDTO isExchangeAwards = sysconfManager.getSysConf(SysConfigId.is_exchange_awards);
        if (isExchangeAwards == null || "0".equals(isExchangeAwards.getConfigValue())) {
            return false;
        }

        // 如果大于100，添加抽奖
        if (exchangeGoldNum >= 100) {
            return true;
        }

        return false;
    }


    /**
     * 保存兑换记录
     *
     * @param recordId
     * @param uid
     * @param diamondNum
     * @param exchangeGoldNum
     * @author: chenjunsheng
     * @date 2018/6/8
     */
    private void saveExchageRecord(String recordId, Long uid, double diamondNum, Long exchangeGoldNum) {
        ExchangeDiamondGoldRecordDO recordDo = new ExchangeDiamondGoldRecordDO();
        recordDo.setRecordId(recordId);
        recordDo.setUid(uid);
        recordDo.setExDiamondNum(diamondNum);
        recordDo.setExGoldNum(exchangeGoldNum);
        recordDo.setCreateTime(new Date());
        exchangeRecordDao.save(recordDo);
    }

    /**
     * 保存账单
     *
     * @param recordId
     * @param uid
     * @param diamondCost
     * @param goldAmount
     * @author: chenjunsheng
     * @date 2018/6/8
     */
    private void saveBillRecord(String recordId, Long uid, double diamondCost, Long goldAmount) {
        BillGoldExchangeDO exchangeDo = new BillGoldExchangeDO();
        exchangeDo.setRecordId(recordId);
        exchangeDo.setUid(uid);
        exchangeDo.setDiamondCost(diamondCost);
        exchangeDo.setGoldAmount(goldAmount.intValue());
        exchangeDo.setCreateTime(new Date());
        exchangeBillDao.save(exchangeDo);

        // FIXME: 兼容管理后台
        Date date = new Date();
        BillRecordDO billRecord = new BillRecordDO();
        billRecord.setUid(uid);
        billRecord.setTargetUid(uid);
        billRecord.setObjId(recordId);
        billRecord.setCreateTime(date);

        billRecord.setBillId(UUIDUtils.get());
        billRecord.setObjType(BillRecordType.exchangeDimondToGoldIncome);
        billRecord.setDiamondNum(null);
        billRecord.setGoldNum(goldAmount);
        billRecord.setMoney(null);
        recordDao.save(billRecord);

        billRecord.setBillId(UUIDUtils.get());
        billRecord.setObjType(BillRecordType.exchangeDimondToGoldPay);
        billRecord.setDiamondNum(-diamondCost);
        billRecord.setGoldNum(null);
        billRecord.setMoney(null);
        recordDao.save(billRecord);
    }

    private void saveChargeRecord(String recordId, Long uid, double diamondCost, Long goldAmount) {
        Double amountDouble = diamondCost * 10;//统一人民币单位：分
        int amount = amountDouble.intValue();
        Date date = new Date();
        ChargeRecordDO recordDo = new ChargeRecordDO();
        recordDo.setChargeRecordId(recordId);
        recordDo.setUid(uid);
        recordDo.setChannel(ChargePayChannel.exchange.name());
        recordDo.setChargeProdId(ChargePayChannel.exchange.name());
        recordDo.setAmount(amount);
        recordDo.setChargeStatus(ChargeRecordStatus.FINISH.getValue());
        recordDo.setTotalGold(goldAmount);
        recordDo.setChargeDesc(diamondCost + "钻石兑换" + goldAmount + "金币");
        recordDo.setCreateTime(date);
        recordDo.setUpdateTime(date);
        chargeRecordDao.save(recordDo);
    }

    /**
     * 钻石兑换金币
     *
     * @param uid         uid
     * @param diamondCost 兑换数
     * @param type        类型 1、钻石
     * @return WithDrawCashVO
     * @throws WebServiceException WebServiceException
     */
    @Override
    public WithDrawCashVO exchangeGold(Long uid, Double diamondCost, int type, String token) throws WebServiceException {
        // 参数判断
        if (uid == null || uid == 0L || diamondCost == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        if (diamondCost <= 0) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        String cacheToken = redisManager.get(RedisKey.withdraw_token.getKey(uid.toString()));
        if (StringUtils.isNotBlank(cacheToken)) {
            if (cacheToken.equalsIgnoreCase(token)) {
                JwtUtils.getClaimByToken(token);
            } else {
                throw new WebServiceException(WebServiceCode.VALID_TOKEN);
            }
        } else {
            throw new WebServiceException(WebServiceCode.VALID_TOKEN);
        }

        if (diamondCost % 10 != 0) {
            throw new WebServiceException(WebServiceCode.EXCHANGE_INPUT_ERROR);
        }

        UserPurseDTO purseDto = userPurseManager.getUserPurse(uid);
        if (purseDto == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        // 进行兑换
        double totalDiamondNum = purseDto.getDiamondNum();
        if (diamondCost > totalDiamondNum) {
            throw new WebServiceException(WebServiceCode.DIAMOND_NUM_NOT_ENOUGH);
        }

        String lockVal = redisManager.lock(RedisKey.lock_exchange_gold.getKey(uid.toString()), 10 * 1000);
        try {
            if (StringUtils.isEmpty(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            Double goldAmount = diamondCost * DiamondExchangeGold.rate;
            UserPurseExchangeDTO exchangeDto = new UserPurseExchangeDTO();
            BeanUtils.copyProperties(purseDto, exchangeDto);

            // 保存相关记录
            Long finalGoldAmount = goldAmount.longValue();
            userPurseManager.updateAddGoldReduceDiamond(uid, finalGoldAmount, diamondCost, true);
            purseDto = userPurseManager.getUserPurse(uid);
            BeanUtils.copyProperties(purseDto, exchangeDto);
            String reocrdId = UUIDUtils.get();
            this.saveExchageRecord(reocrdId, uid, diamondCost, finalGoldAmount);
            this.saveBillRecord(reocrdId, uid, diamondCost, finalGoldAmount);
            this.saveChargeRecord(reocrdId, uid, diamondCost, finalGoldAmount);
        } finally {
            redisManager.unlock(RedisKey.lock_exchange_gold.getKey(uid.toString()), lockVal);

        }
        WithDrawCashVO withDrawCashVO = new WithDrawCashVO();
        withDrawCashVO.setUid(uid);
        withDrawCashVO.setDiamondNum(purseDto.getDiamondNum());
        return withDrawCashVO;
    }

    @Override
    public WithDrawCashVO exchangeGoldCoin(Long uid, Double diamondCost, int type, String phone, String code, String token) throws WebServiceException {
        // 参数判断用户UID和兑换金币数额
        if (uid == null || uid == 0L || diamondCost == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        // 判断兑换金币数额
        if (diamondCost <= 0) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        // 校验授权令牌Token
        String cacheToken = redisManager.get(RedisKey.withdraw_token.getKey(uid.toString()));
        if (StringUtils.isNotBlank(cacheToken)) {
            if (cacheToken.equalsIgnoreCase(token)) {
                JwtUtils.getClaimByToken(token);
            } else {
                throw new WebServiceException(WebServiceCode.VALID_TOKEN);
            }
        } else {
            throw new WebServiceException(WebServiceCode.VALID_TOKEN);
        }

        UserRealNameDTO userRealNameDTO = userRealNameManager.getOneByJedisId(uid.toString());
        if (userRealNameDTO == null || userRealNameDTO.getAuditStatus() == 2) {
            throw new WebServiceException(WebServiceCode.USER_REAL_NAME_NTO_VERIFIED);
        } else if (userRealNameDTO.getAuditStatus() == 0) {
            throw new WebServiceException(WebServiceCode.USER_REAL_NAME_AUDITING);
        }

        // 校验兑换数额是否为10的倍数
        if (diamondCost % 10 != 0) {
            throw new WebServiceException(WebServiceCode.EXCHANGE_INPUT_ERROR);
        }

        // 判断用户是否存在
        UserPurseDTO purseDto = userPurseManager.getUserPurse(uid);
        if (purseDto == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        // 校验短信验证码
        boolean isRightCode = neteaseSmsManager.verifySmsCode(phone, code);
        if (!isRightCode) {
            throw new WebServiceException(WebServiceCode.SMS_CODE_ERROR);
        }

        // 进行兑换
        double totalDiamondNum = purseDto.getDiamondNum();
        if (diamondCost > totalDiamondNum) {
            throw new WebServiceException(WebServiceCode.DIAMOND_NUM_NOT_ENOUGH);
        }

        String lockVal = redisManager.lock(RedisKey.lock_exchange_gold.getKey(uid.toString()), 10 * 1000);
        try {
            if (StringUtils.isEmpty(lockVal)) {
                throw new WebServiceException(WebServiceCode.SERVER_BUSY);
            }
            Double goldAmount = diamondCost * DiamondExchangeGold.rate;
            UserPurseExchangeDTO exchangeDto = new UserPurseExchangeDTO();
            BeanUtils.copyProperties(purseDto, exchangeDto);

            // 保存相关记录
            Long finalGoldAmount = goldAmount.longValue();
            userPurseManager.updateAddGoldReduceDiamond(uid, finalGoldAmount, diamondCost, true);
            purseDto = userPurseManager.getUserPurse(uid);
            BeanUtils.copyProperties(purseDto, exchangeDto);
            String recordId = UUIDUtils.get();
            this.saveExchageRecord(recordId, uid, diamondCost, finalGoldAmount);
            this.saveBillRecord(recordId, uid, diamondCost, finalGoldAmount);
            this.saveChargeRecord(recordId, uid, diamondCost, finalGoldAmount);
        } finally {
            redisManager.unlock(RedisKey.lock_exchange_gold.getKey(uid.toString()), lockVal);
        }

        WithDrawCashVO withDrawCashVO = new WithDrawCashVO();
        withDrawCashVO.setUid(uid);
        withDrawCashVO.setDiamondNum(purseDto.getDiamondNum());
        withDrawCashVO.setGoldNum(purseDto.getGoldNum());
        return withDrawCashVO;
    }

    @Override
    @Transactional
    public GiveGoldVO userGiveGold(Long sendUid, Long recvUid, Integer goldNum, String smsCode) throws WebServiceException {
        // 参数判断
        if (sendUid == null || sendUid == 0L || recvUid == null || recvUid == 0L || goldNum == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        if (goldNum <= 0) {
            throw new WebServiceException(WebServiceCode.GIVE_INPUT_ERROR);
        }

        UsersDTO usersSendDto = usersManager.getUser(sendUid);
        if (usersSendDto == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        UsersDTO usersRecvDto = usersManager.getUser(recvUid);
        if (usersRecvDto == null) {
            throw new WebServiceException(WebServiceCode.TARGET_USER_NOT_EXISTS);
        }

//        // 权限校验
//        String result = redisManager.hget(RedisKey.author_give_gold.getKey(), String.valueOf(usersSendDto.getUid()));
//        if (StringUtils.isBlank(result)) {
//            throw new WebServiceException(WebServiceCode.NO_AUTHORITY);
//        }
//
//        UserAuthorityVO userAuthorityVO = gson.fromJson(result, UserAuthorityVO.class);
//        if (userAuthorityVO == null || !userAuthorityVO.getAuthority()) {
//            throw new WebServiceException(WebServiceCode.NO_AUTHORITY);
//        }

        Map<String, Long> num = new HashMap<>();

        // 赠送者减少金币
        userPurseManager.updateReduceGold(usersSendDto.getUid(), goldNum, false, "金币转账", num);

        // 接受者添加金币
        userPurseManager.updateAddGold(usersRecvDto.getUid(), Long.valueOf(goldNum), false,
                true, "红包", num,
                usersSendDto.getNick()+"给了"+usersRecvDto.getNick()+"一个"+goldNum+"开心的红包");

        // 保存赠送相关记录
        UserGiveRecordDo giveRecordDo = new UserGiveRecordDo();
        giveRecordDo.setGiveId(UUIDUtils.get());
        giveRecordDo.setSendUid(usersSendDto.getUid());
        giveRecordDo.setRecvUid(usersRecvDto.getUid());
        giveRecordDo.setGold(goldNum);
        giveRecordDo.setSendBeforeGoldNum(num.get("reduceBeforeGoldNum") == null ? 0L : num.get("reduceBeforeGoldNum"));
        giveRecordDo.setSendAfterGoldNum(num.get("reduceAfterGoldNum") == null ? 0L : num.get("reduceAfterGoldNum"));
        giveRecordDo.setRecvBeforeGoldNum(num.get("addBeforeGoldNum") == null ? 0L : num.get("addBeforeGoldNum"));
        giveRecordDo.setRecvAfterGoldNum(num.get("addAfterGoldNum") == null ? 0L : num.get("addAfterGoldNum"));
        giveRecordDo.setGiveDesc(null);
        giveRecordDo.setCreateTime(new Date());

        billUserGiveRecordDao.insert(giveRecordDo);

        TransferG2GVo transferG2GVo = new TransferG2GVo();
        transferG2GVo.setSendUid(usersSendDto.getUid());
        transferG2GVo.setRecvUid(usersRecvDto.getUid());
        transferG2GVo.setGoldNum(goldNum);
        transferG2GVo.setSendName(usersSendDto.getNick());
        transferG2GVo.setSendAvatar(usersSendDto.getAvatar());
        transferG2GVo.setRecvName(usersRecvDto.getNick());
        transferG2GVo.setRecvAvatar(usersRecvDto.getAvatar());

        try {
            this.sendPush(usersSendDto.getUid().toString(), usersRecvDto.getUid().toString(), DefMsgType.firstUserGiveGold, DefMsgType.secondUserGiveGold, transferG2GVo);
        } catch (Exception e){
            log.info("[金币赠送 用户to用户 报错] sendUid>:{}, recvUid>:{}, goldNum>:{} e>:{}", sendUid, recvUid, goldNum, e.getStackTrace());
            throw new WebServiceException("金币转赠 云信发送失败");
        }

        log.info("[金币赠送 用户to用户] sendUid>:{}, recvUid>:{}, goldNum>:{}", sendUid, recvUid, goldNum);
        GiveGoldVO giveGoldVO = new GiveGoldVO();
        giveGoldVO.setSendUid(usersSendDto.getUid());
        giveGoldVO.setSendAvatar(usersSendDto.getAvatar());
        giveGoldVO.setSendName(usersSendDto.getNick());
        giveGoldVO.setRecvUid(usersRecvDto.getUid());
        giveGoldVO.setRecvAvatar(usersRecvDto.getAvatar());
        giveGoldVO.setRecvName(usersRecvDto.getNick());
        giveGoldVO.setGoldNum(goldNum);

        return giveGoldVO;
    }

    @Override
    public boolean giveGoldCheck(Long uid) throws WebServiceException {
        // 权限校验
        String result = redisManager.hget(RedisKey.author_give_gold.getKey(), uid.toString());
        if (StringUtils.isBlank(result)) {
            return false;
        }

        UserAuthorityVO userAuthorityVO = gson.fromJson(result, UserAuthorityVO.class);
        if (userAuthorityVO == null || !userAuthorityVO.getAuthority()) {
            return false;
        }
        return true;
    }

    void sendPush(String sendUid, String recv, int first, int second, TransferG2GVo data) {
        Body body = new Body(first, second, data);

        //推送
        NeteaseMsgBO msgBo = new NeteaseMsgBO();
        msgBo.setFrom(sendUid);
        msgBo.setTo(recv);
        msgBo.setOpe(0);
        msgBo.setType(100);
        msgBo.setBody(gson.toJson(body));
        neteaseMsgManager.sendMsg(msgBo);
    }
}
