package com.erban.main.service.user;

import com.erban.main.model.UserPurse;
import com.erban.main.model.UserPurseExample;
import com.erban.main.mybatismapper.UserPurseMapper;
import com.erban.main.mybatismapper.UserPurseMapperMgr;
import com.erban.main.param.neteasepush.NeteasePushParam;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.common.JedisLockService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.UserPurseVo;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.xchat.common.constant.Attach;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.common.utils.GetTimeUtils;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by liuguofu on 2017/7/2.
 */
@Service
public class UserPurseService {

    private Logger logger = LoggerFactory.getLogger(UserPurseService.class);
    @Autowired
    private UserPurseMapper userPurseMapper;

    @Autowired
    private UserPurseMapperMgr userPurseMapperMgr;
    @Autowired
    private JedisService jedisService;
    @Autowired
    private JedisLockService jedisLockService;
    @Autowired
    private SendSysMsgService sendSysMsgService;
    private static DecimalFormat doubleFormat = new DecimalFormat("0.00");
    private Gson gson = new Gson();

    public BusiResult<UserPurseVo> queryUserPurse(Long uid) {
        UserPurse userPurse = getPurseByUid(uid);
        UserPurseVo userPurseVo = convertUserPurseToVo(userPurse);
        BusiResult<UserPurseVo> busiResult = new BusiResult<UserPurseVo>(BusiStatus.SUCCESS);
        busiResult.setData(userPurseVo);
        return busiResult;
    }

    public BusiResult queryFirst(Long uid) {
        UserPurse userPurse = getPurseByUid(uid);
        Integer hour = DateTimeUtil.getHour(new Date());
        if (hour >= 9 && hour <= 20) {
            return new BusiResult(BusiStatus.SUCCESS, false);
        }
        return new BusiResult(BusiStatus.SUCCESS, userPurse.getIsFirstCharge());
    }

    public UserPurseVo getUserPurseVo(Long uid) {
        UserPurseVo userPurseVo = convertUserPurseToVo(getPurseByUid(uid));
        return userPurseVo;
    }

    /**
     * TODO 用户的钱包记录应该在新建用户时创建
     *
     * @param uid
     * @return
     */
    public UserPurse getPurseByUid(Long uid) {
        UserPurse userPurse = getUserPurseCache(uid);
        if (userPurse == null) {
            userPurse = userPurseMapper.selectByPrimaryKey(uid);
            if (userPurse == null) {
                userPurse = new UserPurse();
                userPurse.setUid(uid);
                userPurse.setGoldNum(0L);
                userPurse.setConchNum(0L);
                userPurse.setDiamondNum(0.00);
                userPurse.setNobleGoldNum(0L);
                userPurse.setChargeGoldNum(0L);
                userPurse.setDepositNum(0L);
                userPurse.setIsFirstCharge(true);
                userPurse.setUpdateTime(new Date());
                insertUserPurse(userPurse);
            }
        }
        return userPurse;
    }

    public UserPurse createUserPurse(Long uid) {
        UserPurse userPurse = new UserPurse();
        userPurse.setUid(uid);
        userPurse.setGoldNum(0L);
        userPurse.setConchNum(0L);
        userPurse.setNobleGoldNum(0L);
        userPurse.setChargeGoldNum(0L);
        userPurse.setDiamondNum(0.00);
        userPurse.setDepositNum(0L);
        userPurse.setIsFirstCharge(true);
        userPurse.setUpdateTime(new Date());
        insertUserPurse(userPurse);
        return userPurse;
    }

    public void saveUserPurseCache(UserPurse userPurse) {
        if (userPurse == null) {
            return;
        }
        Double diamondNumD = userPurse.getDiamondNum();
        if (diamondNumD != 0) {
            diamondNumD = new Double(doubleFormat.format(diamondNumD));
        }
        userPurse.setDiamondNum(diamondNumD);
        String userPurseJson = gson.toJson(userPurse);
        jedisService.hwrite(RedisKey.user_purse.getKey(), userPurse.getUid().toString(), userPurseJson);
    }

    public BusiStatus updateAddGold(Long uid, Long goldAmount) {
        if (uid == null) {
            return BusiStatus.SERVERBUSY;
        }

        UserPurse userPurse = this.getPurseByUid(uid);
        if (userPurse == null) {
            return BusiStatus.SERVERBUSY;
        }

        String lockVal = jedisLockService.lock(RedisKey.lock_user_purse.getKey(uid.toString()), 10 * 1000);
        if (StringUtils.isEmpty(lockVal)) {
            return BusiStatus.SERVERBUSY;
        }
        try {
            userPurse.setChargeGoldNum(userPurse.getChargeGoldNum() + goldAmount);
            userPurse.setGoldNum(userPurse.getGoldNum() + goldAmount);
            userPurse.setIsFirstCharge(false);
            jedisService.hset(RedisKey.user_purse.getKey(), String.valueOf(uid), gson.toJson(userPurse));
        } catch (Exception e) {
            return BusiStatus.SERVERBUSY;
        } finally {
            jedisLockService.unlock(RedisKey.lock_user_purse.getKey(uid.toString()), lockVal);
        }
        try {
            userPurseMapperMgr.updateAddChargeGold(uid, goldAmount);
        } catch (Exception e) {
            logger.error("[ 更新用户钱包失败 ]", e);
        }
        return BusiStatus.SUCCESS;
    }

    // 钱包插入与更新 先更新数据库再删缓存 保持数据一致性
    public void insertUserPurse(UserPurse userPurse) {
        userPurseMapper.insertSelective(userPurse);
        jedisService.hdel(RedisKey.user_purse.getKey(), userPurse.getUid().toString());
    }
    // 钱包插入与更新 先更新数据库再删缓存 保持数据一致性
    public void updateUserPurse(UserPurse userPurse) {
        userPurseMapper.updateByPrimaryKeySelective(userPurse);
        jedisService.hdel(RedisKey.user_purse.getKey(), userPurse.getUid().toString());
    }

    public UserPurse updateGoldByCharge(Long uid, Long chargeGoldNum, Integer firstChargeGoldNum) throws Exception {
        if (chargeGoldNum < 1) {
            throw new Exception("充值数据异常-1");
        }
        UserPurse userPurseDb = getPurseByUid(uid);
        Date date = new Date();
        if (userPurseDb == null) {
            Long realGoldNum = chargeGoldNum + firstChargeGoldNum;// 首充奖励
            userPurseDb = new UserPurse();
            userPurseDb.setUid(uid);
            userPurseDb.setChargeGoldNum(realGoldNum);
            userPurseDb.setNobleGoldNum(0L);
            userPurseDb.setGoldNum(realGoldNum);
            userPurseDb.setConchNum(0L);
            userPurseDb.setFirstRechargeTime(date);
            userPurseDb.setIsFirstCharge(false);
            userPurseDb.setUpdateTime(date);
            userPurseDb.setDiamondNum(0.00);
            userPurseDb.setDepositNum(0L);
            insertUserPurse(userPurseDb);
        } else {
            Long goldNumDb = userPurseDb.getGoldNum();
            Long chargeGoldNumDb = userPurseDb.getChargeGoldNum();
            goldNumDb = goldNumDb + chargeGoldNum;
            chargeGoldNumDb = chargeGoldNumDb + chargeGoldNum;
            if (userPurseDb.getIsFirstCharge()) {
                goldNumDb = goldNumDb + firstChargeGoldNum;// 首充奖励
                chargeGoldNumDb = chargeGoldNumDb + firstChargeGoldNum;
                userPurseDb.setFirstRechargeTime(date);
            }
            userPurseDb.setGoldNum(goldNumDb);
            userPurseDb.setChargeGoldNum(chargeGoldNumDb);
            userPurseDb.setUpdateTime(date);
            userPurseDb.setIsFirstCharge(false);
            updateUserPurse(userPurseDb);
        }
        sendSysMsgByModifyGold(userPurseDb);
        return userPurseDb;
    }

    public boolean updateGoldByDeposite(Long uid, Long depositGoldMoneyNum, int plusOrUpdate) throws Exception {
        UserPurse userPurse = getPurseByUid(uid);
        Long goldPurseNum = userPurse.getGoldNum();
        Long purseDeposit = userPurse.getDepositNum();
        if (plusOrUpdate == Constant.DepositUpdateType.plus) {// 预扣款增加，可能同时做了几个预扣款的单
            purseDeposit = purseDeposit + depositGoldMoneyNum;
            goldPurseNum = goldPurseNum - depositGoldMoneyNum;
        } else if (plusOrUpdate == Constant.DepositUpdateType.replace) {
            goldPurseNum = goldPurseNum + purseDeposit - depositGoldMoneyNum;
            purseDeposit = depositGoldMoneyNum;
        } else {
            throw new Exception("操作预扣款异常，没有指定操作类型");
        }
        if (goldPurseNum < 0) {
            return false;
        }
        userPurse.setDepositNum(purseDeposit);
        userPurse.setGoldNum(goldPurseNum);
        updateUserPurse(userPurse);
//        sendSysMsgByModifyGold(userPurse);
        return true;
    }

    public boolean updateGoldBySendGift(Long uid, Long giftGoldPrice) throws Exception {
        UserPurse userPurse = getPurseByUid(uid);
        Long goldPurseNum = userPurse.getGoldNum();
        goldPurseNum = goldPurseNum - giftGoldPrice;
        if (goldPurseNum < 0) {
            return false;
        }
        userPurse.setGoldNum(goldPurseNum);
        updateUserPurse(userPurse);
        return true;
    }

    public UserPurse updateGoldBySendGiftCache(UserPurse userPurse, Long giftGoldPrice) {
        Long goldPurseNum = userPurse.getGoldNum();
        goldPurseNum = goldPurseNum - giftGoldPrice;
        if (goldPurseNum < 0) {
            return null;
        }
        userPurse.setGoldNum(goldPurseNum);
        userPurse.setChargeGoldNum(userPurse.getChargeGoldNum() - giftGoldPrice);
        saveUserPurseCache(userPurse);
        return userPurse;
    }

    public boolean updateGoldByReturnBackDeposit(Long uid, Long depositGoldNum) throws Exception {
        UserPurse userPurse = getPurseByUid(uid);
        Long goldPurseNum = userPurse.getGoldNum();
        Long minusDepositNum = userPurse.getDepositNum() - depositGoldNum;
        goldPurseNum = goldPurseNum + depositGoldNum;
        if (goldPurseNum < 0 || minusDepositNum < 0) {
            return false;
        }
        userPurse.setGoldNum(goldPurseNum);
        userPurse.setDepositNum(minusDepositNum);
        updateUserPurse(userPurse);
        return true;
    }

    public boolean updateGoldByGenOrder(Long uid, Long dealGoldMoney) throws Exception {
        UserPurse userPurse = getPurseByUid(uid);
        Long purseGoldNum = userPurse.getGoldNum();
        purseGoldNum = purseGoldNum - dealGoldMoney;
        if (purseGoldNum < 0) {
            throw new Exception("生成订单时扣款异常-1");
        }
        userPurse.setGoldNum(purseGoldNum);
        updateUserPurse(userPurse);
        return true;
    }

    public boolean updateDiamondByOrderIncome(Long uid, Double diamondNum) {
        UserPurse userPurse = getPurseByUid(uid);
        Double purseDiamondNum = userPurse.getDiamondNum();
        purseDiamondNum = purseDiamondNum + diamondNum;
        userPurse.setDiamondNum(purseDiamondNum);
        updateUserPurse(userPurse);
        return true;
    }

    public int updateDiamondNumByIncome(Long uid, double diamoudNum) {
        int result = userPurseMapperMgr.updateAddDiamond(uid, diamoudNum);
        return result;
    }
//
//    public int updateMinusGoldNum(Long uid,Long goldNum){
//        int result= userPurseMapperMgr.updateMinusGold(uid,goldNum);
//        return result;
//    }

    public int updateChargeGold(Long uid, Long goldNum) {
        int result = userPurseMapperMgr.updateAddChargeGold(uid, goldNum);
        return result;
    }


    public void sendSysMsgByModifyGold(UserPurse userPurse) throws Exception {
        NeteasePushParam neteasePushParam = new NeteasePushParam();
        neteasePushParam.setFrom(Constant.official.uid.toString());
        neteasePushParam.setTo(userPurse.getUid().toString());
        neteasePushParam.setMsgtype(0);
        neteasePushParam.setSave(1);
        Attach attach = new Attach();
        UserPurseVo userPurseVo = convertUserPurseToVo(userPurse);
        attach.setFirst(Constant.DefMsgType.Purse);
        attach.setSecond(Constant.DefMsgType.PurseGoldMinus);
        attach.setData(userPurseVo);
        neteasePushParam.setAttach(attach);
        sendSysMsgService.sendSysAttachMsg(neteasePushParam);
    }

    public UserPurse getUserPurseFromDb(Long uid) {
        UserPurse userPurse = userPurseMapper.selectByPrimaryKey(uid);
        return userPurse;
    }

    public UserPurse getUserPurseCache(Long uid) {
        String purseStr = jedisService.hget(RedisKey.user_purse.getKey(), uid.toString());
        if (StringUtils.isEmpty(purseStr)) {
            return null;
        }
        return gson.fromJson(purseStr, UserPurse.class);
    }

    private Long getGoldNum(Long uid) {
        jedisService.hget(RedisKey.user_purse_gold.getKey(), uid.toString());
        return null;
    }


    private UserPurseVo convertUserPurseToVo(UserPurse userPurse) {
        UserPurseVo userPurseVo = new UserPurseVo();
        userPurseVo.setUid(userPurse.getUid());
        userPurseVo.setGoldNum(userPurse.getGoldNum());
        userPurseVo.setChargeGoldNum(userPurse.getChargeGoldNum());
        userPurseVo.setNobleGoldNum(userPurse.getNobleGoldNum());
        userPurseVo.setDiamondNum(userPurse.getDiamondNum());
        userPurseVo.setDepositNum(userPurse.getDepositNum());
        return userPurseVo;
    }

    public boolean updateGoldBySendGiftByDB(Long uid, Long giftGoldPrice) throws Exception {
        UserPurse userPurse = getPurseByUid(uid);
        Long goldPurseNum = userPurse.getGoldNum();
        goldPurseNum = goldPurseNum - giftGoldPrice;
        if (goldPurseNum < 0) {
            return false;
        }
        userPurse.setGoldNum(goldPurseNum);
        updateUserPurse(userPurse);
//        sendSysMsgByModifyGold(userPurse);
        return true;
    }

    public List<UserPurse> getPurseByShareCharge() {
        Date date = new Date(System.currentTimeMillis() - 87400000);
        UserPurseExample example = new UserPurseExample();
        example.createCriteria().andFirstRechargeTimeIsNotNull().andFirstRechargeTimeBetween(GetTimeUtils.getTimesnights(date, 0), GetTimeUtils.getTimesnight(0));
        List<UserPurse> userPurseList = userPurseMapper.selectByExample(example);
        return userPurseList;
    }

    public UserPurseVo getUserPurseVo(UserPurse userPurse) {
        return convertUserPurseToVo(userPurse);
    }

    public List<UserPurseVo> getPurseListVoByErbanNoList(List<Long> erbanList) {
        UserPurseExample userPurseExample = new UserPurseExample();
        userPurseExample.createCriteria().andUidIn(erbanList);
        userPurseExample.setOrderByClause("uid");
        List<UserPurse> userPurseList = userPurseMapper.selectByExample(userPurseExample);
        return userPurseListToVo(userPurseList);
    }

    private List<UserPurseVo> userPurseListToVo(List<UserPurse> userPurseList) {
        List<UserPurseVo> list = Lists.newArrayList();
        for (UserPurse myUserPurse : userPurseList) {
            list.add(convertUserPurseToVo(myUserPurse));
        }
        return list;
    }

    class UserPurseDo {
        private Long uid;

        private Long chargeGoldNum;

        private Long nobleGoldNum;

        private Long goldNum;

        private Double diamondNum;

        private Long depositNum;

        private Boolean isFirstCharge;

        private String firstRechargeTime;

        private String updateTime;

        public Long getUid() {
            return uid;
        }

        public void setUid(Long uid) {
            this.uid = uid;
        }

        public Long getChargeGoldNum() {
            return chargeGoldNum;
        }

        public void setChargeGoldNum(Long chargeGoldNum) {
            this.chargeGoldNum = chargeGoldNum;
        }

        public Long getNobleGoldNum() {
            return nobleGoldNum;
        }

        public void setNobleGoldNum(Long nobleGoldNum) {
            this.nobleGoldNum = nobleGoldNum;
        }

        public Long getGoldNum() {
            return goldNum;
        }

        public void setGoldNum(Long goldNum) {
            this.goldNum = goldNum;
        }

        public Double getDiamondNum() {
            return diamondNum;
        }

        public void setDiamondNum(Double diamondNum) {
            this.diamondNum = diamondNum;
        }

        public Long getDepositNum() {
            return depositNum;
        }

        public void setDepositNum(Long depositNum) {
            this.depositNum = depositNum;
        }

        public Boolean getFirstCharge() {
            return isFirstCharge;
        }

        public void setFirstCharge(Boolean firstCharge) {
            isFirstCharge = firstCharge;
        }

        public String getFirstRechargeTime() {
            return firstRechargeTime;
        }

        public void setFirstRechargeTime(String firstRechargeTime) {
            this.firstRechargeTime = firstRechargeTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
}
