package com.erban.main.service.charge;


import com.erban.main.model.*;
import com.erban.main.mybatismapper.BillRecordMapper;
import com.erban.main.mybatismapper.RedeemCodeMapper;
import com.erban.main.mybatismapper.RedeemCodeRecordMapper;
import com.erban.main.mybatismapper.UserPurseMapperExpand;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.user.UserPurseService;
import com.erban.main.service.user.UserPurseUpdateService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.UserPurseRedeemVo;
import com.xchat.common.UUIDUitl;
import com.xchat.common.constant.Constant;
import com.xchat.common.device.DeviceInfo;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class RedeemCodeService extends BaseService {

    @Autowired
    private RedeemCodeMapper redeemCodeMapper;
    @Autowired
    private UsersService usersService;
    @Autowired
    private RedeemCodeRecordMapper redeemCodeRecordMapper;
    @Autowired
    private BillRecordMapper billRecordMapper;
    @Autowired
    private UserPurseService userPurseService;
    @Autowired
    private UserPurseMapperExpand userPurseMapperExpand;
    @Autowired
    private UserPurseUpdateService userPurseUpdateService;


    /**
     * 使用兑换码
     *
     * @param code 
     * @param uid
     * @param clientIp
     * @param deviceInfo
     * @return
     */
    public BusiResult useRedeemCode(String code, Long uid, String clientIp, DeviceInfo deviceInfo, String inputKey) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        RedeemCode redeemCode = null;
        try {
            // 加分布式锁，防止
            jedisService.lock(RedisKey.redeem_code_lock.getKey(), 15000, 30000);
            redeemCode = redeemCodeMapper.selectByPrimaryKey(code);


            if (redeemCode == null) {
                busiResult.setCode(0);
                busiResult.setMessage("您输入的兑换码有误，请核对后重新输入");
                long errorCount = jedisService.incr(inputKey);
                if (errorCount >= 3) {
                    busiResult.setMessage(String.format("您今天已累计%d次输入有误，累计5次输入错误后，系统将停止您的兑换码充值功能", errorCount));
                }
                return busiResult;
            }

            // 多台机器情况下，判断使用状态可能存在并发问题，加分布式锁
            if (redeemCode.getUseStatus() != 1) {
                busiResult.setCode(1);
                busiResult.setMessage("该兑换码已被兑换，请核对后输入");
                return busiResult;
            }

            Date date = new Date();
            // 更新DB兑换状态
            redeemCode.setUseUid(uid);
            redeemCode.setUseIp(clientIp);
            if (deviceInfo != null) {
                redeemCode.setUseImei(deviceInfo.getDeviceId());
            }
            redeemCode.setUseStatus(2);
            redeemCode.setUseTime(date);
            redeemCodeMapper.updateByPrimaryKeySelective(redeemCode);

            // 插入兑换记录表
            String recordId = saveRedeemCodeRecord(redeemCode);
            // 插入账单记录表
            saveBillRecord(redeemCode, recordId);
            // 更新用户的金币余额
//            UserPurse userPurse = updateUserPurseGold(redeemCode);
            UserPurse userPurse =userPurseUpdateService.addGoldDbAndCache(redeemCode.getUseUid(), redeemCode.getAmount());
            busiResult.setData(transToUserPurseVo(userPurse,redeemCode.getAmount()));
        } catch (Exception e) {
            busiResult.setCode(BusiStatus.SERVEXCEPTION.value());
            busiResult.setMessage("系统错误，请稍候再试");
            logger.error("useRedeemCode error", e);
            return busiResult;
        } finally {
            jedisService.unlock(RedisKey.redeem_code_lock.getKey());
        }
        return busiResult;
    }

    public int insertRedeemCode(RedeemCode redeemCode) {
        return redeemCodeMapper.insertSelective(redeemCode);
    }

    /**
     * 保存兑换记录
     *
     * @param redeemCode
     */
    public String saveRedeemCodeRecord(RedeemCode redeemCode) {
        String recordId = UUIDUitl.get();
        RedeemCodeRecord redeemCodeRecord = new RedeemCodeRecord();
        redeemCodeRecord.setRecordId(recordId);
        redeemCodeRecord.setAmount(redeemCode.getAmount());
        redeemCodeRecord.setUid(redeemCode.getUseUid());
        redeemCodeRecord.setCode(redeemCode.getCode());
        redeemCodeRecord.setCreateTime(new Date());
        redeemCodeRecord.setIp(redeemCode.getUseIp());
        redeemCodeRecord.setImei(redeemCode.getUseImei());
        redeemCodeRecordMapper.insertSelective(redeemCodeRecord);
        return recordId;
    }

    /**
     * 保存账单记录
     *
     * @param redeemCode
     * @param redeemCodeRecordId
     */
    public void saveBillRecord(RedeemCode redeemCode, String redeemCodeRecordId) {
        String billId = UUIDUitl.get();
        BillRecord billRecord = new BillRecord();
        billRecord.setBillId(billId);
        billRecord.setUid(redeemCode.getUseUid());
        billRecord.setTargetUid(redeemCode.getUseUid());
        billRecord.setGoldNum(redeemCode.getAmount());
        billRecord.setRoomUid(redeemCode.getUseUid());
        billRecord.setObjId(redeemCodeRecordId);
        billRecord.setObjType(Constant.BillType.redeemCode);
        Date date = new Date();
        billRecord.setCreateTime(date);
        billRecord.setUpdateTime(date);
        billRecordMapper.insertSelective(billRecord);
    }

    /**
     * 更新用户金币数量
     *
     * @param redeemCode
     */
    public UserPurse updateUserPurseGold(RedeemCode redeemCode) {
        UserPurse userPurse = userPurseService.getUserPurseFromDb(redeemCode.getUseUid());
        if (userPurse == null) {
            userPurse = new UserPurse();
            userPurse.setUid(redeemCode.getUseUid());
            userPurse.setDiamondNum(0.00);
            userPurse.setGoldNum(redeemCode.getAmount());
            userPurse.setConchNum(0L);
            userPurse.setDepositNum(0L);
            userPurse.setIsFirstCharge(true);
            userPurse.setUpdateTime(new Date());
            userPurseService.insertUserPurse(userPurse);
        } else {
            // DB原子更新金币数量
            userPurseMapperExpand.updatePurseGold(redeemCode.getAmount(), redeemCode.getUseUid());
            userPurse = userPurseService.getUserPurseFromDb(redeemCode.getUseUid());
            userPurseService.saveUserPurseCache(userPurse);
        }
        return userPurse;
    }

    /**
     * 判断用户是否存在
     *
     * @param uid
     * @return
     */
    public boolean isExistUser(Long uid) {
        String uidKey = RedisKey.redeem_code_uid.getKey(uid.toString());
        if (jedisService.read(uidKey) != null) {
            return false;
        }
        Users users = usersService.getUsersByUid(uid);
        if (users == null) {
            // 防止缓存穿透，短期缓存
            jedisService.set(uidKey, uid.toString(), 60); // 失效时间为60s
            return false;
        }
        return true;
    }

    /**
     *
     *
     * @param userPurse
     * @return
     */
    public UserPurseRedeemVo transToUserPurseVo(UserPurse userPurse, Long amount) {
        UserPurseRedeemVo userPurseRedeemVo = new UserPurseRedeemVo();
        userPurseRedeemVo.setAmount(amount);
        userPurseRedeemVo.setDepositNum(userPurse.getDepositNum());
        userPurseRedeemVo.setFirstRechargeTime(userPurse.getFirstRechargeTime());
        userPurseRedeemVo.setGoldNum(userPurse.getGoldNum());
        userPurseRedeemVo.setChargeGoldNum(userPurse.getChargeGoldNum());
        userPurseRedeemVo.setNobleGoldNum(userPurse.getNobleGoldNum());
        userPurseRedeemVo.setDiamondNum(userPurse.getDiamondNum());
        userPurseRedeemVo.setIsFirstCharge(userPurse.getIsFirstCharge());
        userPurseRedeemVo.setUid(userPurse.getUid());
        userPurseRedeemVo.setUpdateTime(userPurse.getUpdateTime());
        return userPurseRedeemVo;
    }

    private static final String[] CODE_STR = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"
            , "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    /**
     * 构建随机生成的字符串
     *
     * @param len 指定生成的字符串长度
     * @return
     */
    public static String buildRandomCode(int len) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            builder.append(CODE_STR[random.nextInt(CODE_STR.length)]);
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(buildRandomCode(16));
        }
    }
}
