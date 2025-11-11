package com.juxiao.xchat.service.api.charge.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.*;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.bill.BillRecordDao;
import com.juxiao.xchat.dao.bill.BillTransferDao;
import com.juxiao.xchat.dao.bill.domain.BillRecordDO;
import com.juxiao.xchat.dao.bill.domain.BillTransferDO;
import com.juxiao.xchat.dao.bill.enumeration.BillRecordType;
import com.juxiao.xchat.dao.bill.enumeration.BillTransferStatus;
import com.juxiao.xchat.dao.charge.dto.WithDrawCashProdDTO;
import com.juxiao.xchat.dao.user.AccountDao;
import com.juxiao.xchat.dao.user.UsersDao;
import com.juxiao.xchat.dao.user.domain.AccountDO;
import com.juxiao.xchat.dao.user.domain.UsersDO;
import com.juxiao.xchat.dao.user.dto.AccountDTO;
import com.juxiao.xchat.dao.user.dto.UserPurseDTO;
import com.juxiao.xchat.dao.user.dto.UserRealNameDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.charge.WithDrawCashProdManager;
import com.juxiao.xchat.manager.common.user.AccountManager;
import com.juxiao.xchat.manager.common.user.UserPurseManager;
import com.juxiao.xchat.manager.common.user.UserRealNameManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.netease.NetEaseSmsManager;
import com.juxiao.xchat.service.api.charge.WithDrawService;
import com.juxiao.xchat.service.api.charge.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户提现业务实现
 *
 * @class: WithDrawServiceImpl.java
 * @author: chenjunsheng
 * @date 2018/6/11
 */
@Service
public class WithDrawServiceImpl implements WithDrawService {
    @Resource
    private BillRecordDao recordDao;

    @Resource
    private BillTransferDao transferDao;

    @Resource
    private UsersDao usersDao;

    @Resource
    private WithDrawCashProdManager cashProdManager;

    @Resource
    private UsersManager usersManager;

    @Resource
    private UserPurseManager userPurseManager;

    @Resource
    private UserRealNameManager userRealNameManager;

    @Resource(name = "CaihSmsManager")
    private NetEaseSmsManager neteaseSmsManager;

    @Resource
    private RedisManager redisManager;

    @Resource
    private Gson gson;

    @Resource
    private AccountDao accountDao;

    @Resource
    private AccountManager accountManager;

    @Resource
    private JdbcTemplate jdbcTemplate;

    private final long LIMIT_VERSION = Utils.version2long("1.0.22");

    /**
     * @see com.juxiao.xchat.service.api.charge.WithDrawService#getWithDraw(Long)
     */
    @Override
    public WithDrawVO getWithDraw(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        WithDrawVO withDrawVo = new WithDrawVO();
        UsersDTO users = usersManager.getUser(uid);
        if (users == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        withDrawVo.setUid(uid);
        if (StringUtils.isNotBlank(users.getAlipayAccount())) {
            withDrawVo.setAlipayAccount(users.getAlipayAccount());
        }

        if (StringUtils.isNotBlank(users.getAlipayAccountName())) {
            withDrawVo.setAlipayAccountName(users.getAlipayAccountName());
        }

        withDrawVo.setIsNotBoundPhone(!DataValidationUtils.validatePhone(users.getPhone()));
        UserPurseDTO purseDto = userPurseManager.getUserPurse(uid);
        withDrawVo.setDiamondNum(purseDto.getDiamondNum());
        AccountDTO accountDTO = accountDao.getAccount(uid);
        if (accountDTO == null) {
            withDrawVo.setHasWx(false);
        } else {
            withDrawVo.setHasWx(StringUtils.isNotBlank(accountDTO.getWeixinOpenid()));
        }

        String str = redisManager.hget(RedisKey.with_draw_type.getKey(), uid.toString());
        if (StringUtils.isBlank(str)) {
            withDrawVo.setWithDrawType(1);
        } else {
            withDrawVo.setWithDrawType(Integer.valueOf(str));
        }
        return withDrawVo;
    }

    /**
     * @see com.juxiao.xchat.service.api.charge.WithDrawService#boundAliPay(Long, String, String, String)
     */
    @Override
    public AliPayBoundVO boundAliPay(Long uid, String aliPayAccount, String aliPayAccountName, String code) throws WebServiceException {
        if (uid == null || StringUtils.isBlank(aliPayAccount) || StringUtils.isBlank(aliPayAccountName) || StringUtils.isBlank(code)) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        // 将用户输入验证码进行判断
        UsersDTO usersDto = usersManager.getUser(uid);
        if (usersDto == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        boolean isSmsCode = neteaseSmsManager.verifySmsCode(usersDto.getPhone(), code);
        if (!isSmsCode) {
            throw new WebServiceException(WebServiceCode.SMS_CODE_ERROR);
        }

        UsersDO usersDo = new UsersDO();
        usersDo.setUid(uid);
        usersDo.setAlipayAccount(aliPayAccount);
        usersDo.setAlipayAccountName(aliPayAccountName);
        usersDao.update(usersDo);

        usersDto.setAlipayAccount(aliPayAccount);
        usersDto.setAlipayAccountName(aliPayAccountName);
        redisManager.hset(RedisKey.user.getKey(), String.valueOf(uid), gson.toJson(usersDto));
        return new AliPayBoundVO(uid, aliPayAccount, aliPayAccountName);
    }

    @Override
    public void checkCode(Long uid, String code) throws WebServiceException {
        UsersDTO usersDTO = usersManager.getUser(uid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        boolean isSmsCode = neteaseSmsManager.verifySmsCode(usersDTO.getPhone(), code);
        if (isSmsCode) {
            redisManager.hset(RedisKey.user_check_code.getKey(), uid.toString(), code);
        } else {
            throw new WebServiceException(WebServiceCode.SMS_CODE_ERROR);
        }
    }

    @Override
    public int boundThird(Long uid, String openId, String unionId, String accessToken, int type, String app,
                          String os) throws WebServiceException {
        if (type != 1 && type != 2) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        UsersDTO usersDTO = usersManager.getUser(uid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        AccountDTO accountDTO = accountDao.getAccount(uid);
        if (type == 1 && !openId.equalsIgnoreCase(accountDTO.getWeixinOpenid())) {
            throw new WebServiceException("与已绑定微信不一致,请确认!");
        }
        if (type == 2 && StringUtils.isNotBlank(accountDTO.getQqOpenid())) {
            throw new WebServiceException("你已经绑定QQ");
        }
        String str = redisManager.hget(RedisKey.user_check_code.getKey(), uid.toString());
        if (StringUtils.isBlank(str)) {
            throw new WebServiceException(WebServiceCode.SMS_CODE_ERROR);
        }
        try {
            accountManager.validateThirdInfo(openId, accessToken, type, app, os);
        } catch (Exception e) {
            throw new WebServiceException("校验第三方失败");
        }
        int count;
        AccountDO accountDo = new AccountDO();
        accountDo.setUid(uid);
        if (type == 1) {
            count = accountDao.countWx(openId);
            if (count > 0) {
                throw new WebServiceException("该微信已经被绑定");
            }
            accountDo.setWeixinOpenid(openId);
            accountDo.setWeixinUnionid(unionId);
        } else if (type == 2) {
            count = accountDao.countQQ(openId);
            if (count > 0) {
                throw new WebServiceException("该QQ已经被绑定");
            }
            accountDo.setQqOpenid(openId);
            accountDo.setQqUnionid(unionId);
        }
        redisManager.hdel(RedisKey.user_check_code.getKey(), uid.toString());
        return accountDao.update(accountDo);
    }

    @Override
    public int unBoundThird(Long uid, int type) throws WebServiceException {
        if (type != 1 && type != 2) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        UsersDTO usersDTO = usersManager.getUser(uid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        AccountDTO accountDTO = accountDao.getAccount(uid);
        if (type == 1 && StringUtils.isBlank(accountDTO.getWeixinOpenid())) {
            throw new WebServiceException("你还没有绑定微信");
        }

        if (type == 2 && StringUtils.isBlank(accountDTO.getQqOpenid())) {
            throw new WebServiceException("你还没有绑定QQ");
        }

        if (accountDTO.getErbanNo().toString().equals(accountDTO.getPhone()) && (StringUtils.isBlank(accountDTO.getWeixinOpenid()) || StringUtils.isBlank(accountDTO.getQqOpenid()))) {
            throw new WebServiceException("你还没有绑定手机，不能解绑全部工具");
        }

        int result = 0;
        if (type == 1) {
            result = jdbcTemplate.update("UPDATE account set weixin_openid = null, weixin_unionid = null where uid = " +
                    "?", uid);
        } else if (type == 2) {
            result = jdbcTemplate.update("UPDATE account set qq_openid = null, qq_unionid = null where uid = ?", uid);
        }
        return result;
    }

    @Override
    public WithDrawCashVO withDrawCashNowxali(Long uid, String pid, int type) throws Exception {
        if (uid == null || StringUtils.isBlank(pid)) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        if (type != 1 && type != 2) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        // 1.判断用户是否生成钱包 || 钱包是否有钻石
        UserPurseDTO purseDto = userPurseManager.getUserPurse(uid);
        if (purseDto == null || purseDto.getDiamondNum() == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        // 2.判断用户是否存在
        UsersDTO usersDto = usersManager.getUser(uid);
        if (usersDto == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        // 提现间隙时间限制
        long incr = redisManager.incrByTime(RedisKey.withdraw_cash_limit.getKey(uid + ""), 5);
        if (incr > 1) {
            throw new WebServiceException(WebServiceCode.REQUEST_FREQUENT);
        }

        Long cashNum = cashProdManager.getCashNum(pid);
        if (cashNum <= 0) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        // 提现金额换算成钻石数量 (1:10)
        Double diamondCost = (cashNum * 10.0);

        // 3.比较用户余额与取现列表的金额并更新UserPurse用户钱包
        userPurseManager.updateReduceDiamond(uid, diamondCost, false);

        // 4.生成BillTransferRecord记录用作提现
        BillTransferDO transferDo = new BillTransferDO();
        Date now = new Date();
        transferDo.setUid(uid);
        transferDo.setBillId(UUIDUtils.get());
        // 提现类型 [1.钻石提现; 2.红包提现]
        transferDo.setTranType((byte) 1);
        // 提现方式 [1.微信提现; 2.支付宝提现; 3.银行卡提现]
        transferDo.setRealTranType((byte) type);
        transferDo.setCost(diamondCost);
        transferDo.setMoney(cashNum.intValue());
        transferDo.setBillStatus(BillTransferStatus.ING.getValue());
        transferDo.setCreateTime(now);
        transferDo.setUpdateTime(now);
        transferDo.setBankCard(usersDto.getBankCard());
        transferDo.setBankCardName(usersDto.getBankCardName());
        transferDao.save(transferDo);

        // FIXME: 兼容管理后台
        Date date = new Date();
        BillRecordDO billRecord = new BillRecordDO();
        billRecord.setBillId(transferDo.getBillId());
        billRecord.setUid(uid);
        billRecord.setTargetUid(uid);
        billRecord.setObjId(null);
        billRecord.setObjType(BillRecordType.getCash);
        billRecord.setBillStatus(BillTransferStatus.ING.getValue());
        billRecord.setDiamondNum(-diamondCost);
        billRecord.setGiftId(type);
        billRecord.setGoldNum(null);
        billRecord.setMoney(cashNum);
        billRecord.setCreateTime(date);
        recordDao.save(billRecord);

        redisManager.hset(RedisKey.with_draw_type.getKey(), uid.toString(), type + "");
        //用户钱包剩余钻石余额
        return new WithDrawCashVO(uid, purseDto.getDiamondNum() - diamondCost);
    }

    @Override
    public WithDrawCashVO withDrawCash(Long uid, String pid, Integer diamondNum, int type) throws Exception {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        if (type != 1 && type != 2 && type != 3) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        // 1.判断用户是否生成钱包 || 钱包是否有钻石
        UserPurseDTO purseDto = userPurseManager.getUserPurse(uid);
        if (purseDto == null || purseDto.getDiamondNum() == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        // 2.判断用户是否存在
        UsersDTO usersDto = usersManager.getUser(uid);
        if (usersDto == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        // 提现间隙时间限制
        long incr = redisManager.incrByTime(RedisKey.withdraw_cash_limit.getKey(uid + ""), 5);
        if (incr > 1) {
            throw new WebServiceException(WebServiceCode.REQUEST_FREQUENT);
        }

        // 判断用户 1.是否绑定微信账号 / 2.是否绑定支付宝账号
        if (type == 1) {
            AccountDTO accountDTO = accountDao.getAccount(uid);
            if (StringUtils.isBlank(accountDTO.getWeixinOpenid())) {
                throw new WebServiceException("你还没有绑定微信");
            }
        } else if (type == 2 && (StringUtils.isBlank(usersDto.getAlipayAccount()) || StringUtils.isBlank(usersDto.getAlipayAccountName()))) {
            throw new WebServiceException(WebServiceCode.ALIAPY_ACCOUNT_NOTEXISTS);
        }

        Long cashNum;
        // 兼容旧版的提现选项ID, 新版自定义提现数量
        if (StringUtils.isBlank(pid)) {
            cashNum = Long.valueOf(diamondNum);
        } else {
            cashNum = cashProdManager.getCashNum(pid);
        }

        if (cashNum <= 0) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        // 提现金额换算成钻石数量 (1:10)
        Double diamondCost = (cashNum * 10.0);

        // 3.比较用户余额与取现列表的金额并更新UserPurse用户钱包
        userPurseManager.updateReduceDiamond(uid, diamondCost, false);

        // 4.生成BillTransferRecord记录用作提现
        BillTransferDO transferDo = new BillTransferDO();
        Date now = new Date();
        transferDo.setUid(uid);
        transferDo.setBillId(UUIDUtils.get());
        // 提现类型 [1.钻石提现; 2.红包提现]
        transferDo.setTranType((byte) 1);
        // 提现方式 [1.微信提现; 2.支付宝提现; 3.银行卡提现]
        transferDo.setRealTranType((byte) type);
        transferDo.setCost(diamondCost);
        transferDo.setMoney(cashNum.intValue());
        transferDo.setBillStatus(BillTransferStatus.ING.getValue());
        transferDo.setCreateTime(now);
        transferDo.setUpdateTime(now);
        transferDo.setBankCard(usersDto.getBankCard());
        transferDo.setBankCardName(usersDto.getBankCardName());
        transferDao.save(transferDo);

        // FIXME: 兼容管理后台
        Date date = new Date();
        BillRecordDO billRecord = new BillRecordDO();
        billRecord.setBillId(transferDo.getBillId());
        billRecord.setUid(uid);
        billRecord.setTargetUid(uid);
        billRecord.setObjId(null);
        billRecord.setObjType(BillRecordType.getCash);
        billRecord.setBillStatus(BillTransferStatus.ING.getValue());
        billRecord.setDiamondNum(-diamondCost);
        billRecord.setGiftId(type);
        billRecord.setGoldNum(null);
        billRecord.setMoney(cashNum);
        billRecord.setCreateTime(date);
        recordDao.save(billRecord);

        redisManager.hset(RedisKey.with_draw_type.getKey(), uid.toString(), type + "");

        // 返回用户钱包剩余钻石余额
        return new WithDrawCashVO(uid, purseDto.getDiamondNum() - diamondCost);
    }

    /**
     * @see WithDrawService#listAllCashProds()
     */
    @Override
    public List<WithDrawCashProdDTO> listAllCashProds() {
        return cashProdManager.listAllCashProds();
    }

    @Override
    public BindAccountVO bindAccount(Long uid, String diamondId, Integer diamondNum, String account, String accountName,
                                     Integer accountType, String appVersion, String phone, String passwordSecond) throws Exception {
        if (uid == null || StringUtils.isBlank(account) || StringUtils.isBlank(accountName) || accountType <= 0
                || accountType == null || StringUtils.isEmpty(passwordSecond)) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

//        // 1.0.22 版本必须增加短信验证码
//        if (Utils.version2long(appVersion) < LIMIT_VERSION) {
//            throw new WebServiceException("请更新版本");
//        }

        if (StringUtils.isEmpty(phone)) {
            throw new WebServiceException(WebServiceCode.PHONE_INVALID);
        }

//        if (StringUtils.isEmpty(smsCode)) {
//            throw new WebServiceException(WebServiceCode.SMS_CODE_ERROR);
//        }

        AccountDTO accountDTO = accountDao.getAccount(uid);
        if (accountDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

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

        // 从验证码验证改为使用二级密码验证
        passwordSecond = MD5Utils.getMD5(passwordSecond);
        if (!passwordSecond.equals(accountDTO.getPasswordSecond())) {
            throw new WebServiceException(WebServiceCode.PASSWORD_SECOND_ERROR);
        }

        UsersDTO usersDto = usersManager.getUser(uid);
        if (usersDto == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        if (accountType == 1) {
            UsersDO usersDo = new UsersDO();
            usersDo.setUid(uid);
            usersDo.setAlipayAccount(account);
            usersDo.setAlipayAccountName(accountName);
            usersDao.update(usersDo);

            usersDto.setAlipayAccount(account);
            usersDto.setAlipayAccountName(accountName);
            redisManager.hset(RedisKey.user.getKey(), String.valueOf(uid), gson.toJson(usersDto));

            this.withDrawCash(uid, diamondId, diamondNum, 2);
        } else if (accountType == 2) {
            UsersDO usersDo = new UsersDO();
            usersDo.setUid(uid);
            usersDo.setBankCard(account);
            usersDo.setBankCardName(accountName);
            usersDao.update(usersDo);

            usersDto.setBankCard(account);
            usersDto.setBankCardName(accountName);
            redisManager.hset(RedisKey.user.getKey(), String.valueOf(uid), gson.toJson(usersDto));

            this.withDrawCash(uid, diamondId, diamondNum, 3);
        }

        return new BindAccountVO(uid, account, accountName, accountType);
    }

    @Override
    public FinancialAccountVO getAccount(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        UsersDTO usersDto = usersManager.getUser(uid);
        if (usersDto == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        FinancialAccountVO financialAccountVO = new FinancialAccountVO();
        financialAccountVO.setUid(usersDto.getUid());
        financialAccountVO.setAlipayAccount(usersDto.getAlipayAccount());
        financialAccountVO.setAlipayAccountName(usersDto.getAlipayAccountName());
        financialAccountVO.setBankCard(usersDto.getBankCard());
        financialAccountVO.setBankCardName(usersDto.getBankCardName());

        return financialAccountVO;
    }

    /**
     * 根据手机及短信验证码获取钻石提现信息
     *
     * @param phone 手机号
     * @param code  短信验证码
     * @return UserWithdrawVO
     */
    @Override
    public UserWithdrawVO getSmsCodeByWithdrawInfo(String phone, String code) throws WebServiceException {
        UsersDTO usersDTO = usersManager.getUserByPhone(phone);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        boolean isSmsCode = neteaseSmsManager.verifySmsCode(usersDTO.getPhone(), code);
        if (!isSmsCode) {
            throw new WebServiceException(WebServiceCode.SMS_CODE_ERROR);
        }
        Long uid = usersDTO.getUid();
        UserWithdrawVO userWithdrawVO = new UserWithdrawVO();
        BeanUtils.copyProperties(usersDTO, userWithdrawVO);
        UserPurseDTO userPurseDTO = userPurseManager.getUserPurse(uid);
        userWithdrawVO.setDiamondNum(userPurseDTO.getDiamondNum());
        userWithdrawVO.setGoldNum(userPurseDTO.getGoldNum().doubleValue());
        redisManager.set(RedisKey.user_sms_code_string.getKey(uid.toString()), code, 10, TimeUnit.MINUTES);
        String token = JwtUtils.generateToken(usersDTO.getUid());
        redisManager.set(RedisKey.withdraw_token.getKey(uid.toString()), token, 10, TimeUnit.MINUTES);
        userWithdrawVO.setToken(token);
        return userWithdrawVO;
    }

    @Override
    public UserWithdrawVO getWithdrawInfo(String userName, String password) throws WebServiceException {
        UsersDTO usersDTO;
        List<UsersDTO> usersDTOList = usersManager.getUserByUserPhone(userName);
        if (usersDTOList == null || usersDTOList.size() == 0) {
            usersDTO = usersManager.getUserByErbanNo(Long.valueOf(userName));
            if (usersDTO == null) {
                throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
            }
        } else if (usersDTOList.size() > 1) {
            throw new WebServiceException(WebServiceCode.PHONE_A_LOT);
        } else {
            usersDTO = usersDTOList.get(0);
        }

        Long uid = usersDTO.getUid();
        AccountDTO accountDTO = accountDao.getAccount(uid);
        if (accountDTO.getPassword() == null) {
            throw new WebServiceException("请先前往App内设置登录密码");
        } else if (accountDTO.getPassword() != null && !accountDTO.getPassword().equalsIgnoreCase(password)) {
            throw new WebServiceException(WebServiceCode.PASSWORD_ERROR);
        }

        UserWithdrawVO userWithdrawVO = new UserWithdrawVO();
        BeanUtils.copyProperties(usersDTO, userWithdrawVO);

        UserPurseDTO userPurseDTO = userPurseManager.getUserPurse(uid);
        userWithdrawVO.setDiamondNum(userPurseDTO.getDiamondNum());
        userWithdrawVO.setGoldNum(userPurseDTO.getGoldNum().doubleValue());
        String token = JwtUtils.generateToken(usersDTO.getUid());
        redisManager.set(RedisKey.withdraw_token.getKey(uid.toString()), token, 10, TimeUnit.MINUTES);
        userWithdrawVO.setToken(token);
        return userWithdrawVO;
    }

    /**
     * 检查是否绑定微信
     *
     * @param unionId unionId
     * @return UserWithdrawVO
     * @throws WebServiceException WebServiceException
     */
    @Override
    public UserWithdrawVO checkBindWx(String unionId) throws WebServiceException {
        if (StringUtils.isEmpty(unionId)) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        AccountDTO account = accountDao.getByWeixinUnionId(unionId);
        if (account == null) {
            throw new WebServiceException(WebServiceCode.WX_OPENID_NOT_REGISTER);
        }
        UsersDTO users = usersManager.getUser(account.getUid());
        if (users == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        UserWithdrawVO userWithdrawVO = new UserWithdrawVO();
        BeanUtils.copyProperties(users, userWithdrawVO);
        UserPurseDTO userPurseDTO = userPurseManager.getUserPurse(users.getUid());
        if (userPurseDTO != null) {
            userWithdrawVO.setDiamondNum(userPurseDTO.getDiamondNum());
            userWithdrawVO.setGoldNum(userPurseDTO.getGoldNum().doubleValue());
        }
        userWithdrawVO.setToken(getWithdrawToken(users.getUid()));
        return userWithdrawVO;
    }

    /**
     * 检测是否绑定支付宝
     *
     * @param uid uid
     * @return UserWithdrawVO
     * @throws WebServiceException WebServiceException
     */
    @Override
    public UserWithdrawVO checkBindAliPay(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        UsersDTO users = usersManager.getUser(uid);
        if (users == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        UserWithdrawVO userWithdrawVO = new UserWithdrawVO();
        AccountDTO account = accountDao.getAccount(uid);
        userWithdrawVO.setIsBindAlipay(false);
        BeanUtils.copyProperties(users, userWithdrawVO);
        if (account != null) {
            //优先取取现绑定的支付宝
            if (!StringUtils.isEmpty(account.getAlipayAccount())
                    && !StringUtils.isEmpty(account.getAlipayName())) {
                userWithdrawVO.setIsBindAlipay(true);
                userWithdrawVO.setAlipayAccount(account.getAlipayAccount());
                userWithdrawVO.setAlipayAccountName(account.getAlipayName());
            } else {
                if (!StringUtils.isEmpty(users.getAlipayAccount())) {
                    userWithdrawVO.setIsBindAlipay(true);
                    userWithdrawVO.setAlipayAccount(users.getAlipayAccount());
                    userWithdrawVO.setAlipayAccountName(users.getAlipayAccountName());
                }
            }
        }

        UserPurseDTO userPurseDTO = userPurseManager.getUserPurse(users.getUid());
        if (userPurseDTO != null) {
            userWithdrawVO.setDiamondNum(userPurseDTO.getDiamondNum());
            userWithdrawVO.setGoldNum(userPurseDTO.getGoldNum().doubleValue());
        }
        userWithdrawVO.setToken(getWithdrawToken(users.getUid()));
        return userWithdrawVO;
    }

    /**
     * 公众号钻石提现
     *
     * @param uid           uid
     * @param type          1，微信；2，支付宝
     * @param withdrawType  提现类型 1、钻石
     * @param openId        微信openId
     * @param weixinName    微信名
     * @param alipayAccount 支付宝账号
     * @param realName      支付宝真实姓名
     * @param pid           提现项目id
     * @return WithDrawCashVO
     * @throws WebServiceException WebServiceException
     */
    @Override
    public WithDrawCashVO noPublicWithDrawCash(Long uid, int type, int withdrawType, String openId, String weixinName
            , String alipayAccount, String realName, String pid, String token) throws WebServiceException {
        if (uid == null || StringUtils.isBlank(pid)) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        if (type != 1 && type != 2) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        checkWithdrawToken(token, uid);

        // 1.用户是否生成钱包
        UserPurseDTO purseDto = userPurseManager.getUserPurse(uid);
        if (purseDto == null || purseDto.getDiamondNum() == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        // 2.是否有绑定支付包账号
        UsersDTO usersDto = usersManager.getUser(uid);
        if (usersDto == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        long incr = redisManager.incrByTime(RedisKey.withdraw_cash_limit.getKey(uid + ""), 5);
        if (incr > 1) {
            throw new WebServiceException(WebServiceCode.REQUEST_FREQUENT);
        }
        AccountDTO accountDTO = accountDao.getAccount(uid);
        if (type == 1) {
            //为空说明微信已经绑定
            if (StringUtils.isEmpty(openId) || StringUtils.isEmpty(weixinName)) {
                if (StringUtils.isBlank(accountDTO.getWeixinOpenid())) {
                    throw new WebServiceException("你还没有绑定微信");
                }
                openId = accountDTO.getWeixinOpenid();
            }
        } else if (type == 2 && (StringUtils.isBlank(accountDTO.getAlipayAccount()) || StringUtils.isBlank(accountDTO.getAlipayName()))) {
            if (StringUtils.isBlank(alipayAccount) || StringUtils.isBlank(realName)) {
                //如果未绑定支付宝
                if (StringUtils.isEmpty(accountDTO.getAlipayAccount())
                        || StringUtils.isEmpty(accountDTO.getAlipayName())) {
                    if (StringUtils.isEmpty(usersDto.getAlipayAccount())) {
                        throw new WebServiceException("未绑定支付宝！");
                    }
                    alipayAccount = usersDto.getAlipayAccount();
                } else {
                    alipayAccount = accountDTO.getAlipayAccount();
                    realName = accountDTO.getAlipayName();
                }
            }
        }

        WithDrawCashProdDTO withDrawCashProdDTO = cashProdManager.getWithDrawCashProdByPid(pid);
        if (withDrawCashProdDTO == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        if (withDrawCashProdDTO.getDiamondNum() >= purseDto.getDiamondNum()) {
            throw new WebServiceException(WebServiceCode.DIAMOND_NUM_NOT_ENOUGH);
        }

        Long cashNum = cashProdManager.getCashNum(pid);
        if (cashNum <= 0) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        Double diamondCost = (cashNum * 10.0);

        //3.比较用户余额与取现列表的金额，更新userPurse
        userPurseManager.updateReduceDiamond(uid, diamondCost, false);

        //4.提现，生成billRecord记录
        BillTransferDO transferDo = new BillTransferDO();
        Date now = new Date();
        transferDo.setUid(uid);
        transferDo.setBillId(UUIDUtils.get());
        transferDo.setTranType((byte) type);
        transferDo.setCost(diamondCost);
        transferDo.setMoney(cashNum.intValue());
        transferDo.setBillStatus(BillTransferStatus.ING.getValue());
        transferDo.setCreateTime(now);
        transferDo.setUpdateTime(now);
        if (type == 1) {
            transferDo.setApplyWithdrawalAccount(openId);
            transferDo.setApplyWithdrawalName(weixinName);
        } else if (type == 2) {
            transferDo.setApplyWithdrawalAccount(alipayAccount);
            transferDo.setApplyWithdrawalName(realName);
        }
        transferDao.insert(transferDo);
        // FIXME: 兼容管理后台
        Date date = new Date();
        BillRecordDO billRecord = new BillRecordDO();
        billRecord.setBillId(transferDo.getBillId());
        billRecord.setUid(uid);
        billRecord.setTargetUid(uid);
        billRecord.setObjId(null);
        billRecord.setObjType(BillRecordType.getCash);
        billRecord.setBillStatus(BillTransferStatus.ING.getValue());
        billRecord.setDiamondNum(-diamondCost);
        billRecord.setGiftId(type);// 用了区分微信还是支付宝提现
        billRecord.setGoldNum(null);
        billRecord.setMoney(cashNum);
        billRecord.setCreateTime(date);
        recordDao.save(billRecord);
        redisManager.hset(RedisKey.with_draw_type.getKey(), uid.toString(), type + "");
        //更新提现微信账号
        AccountDO accountDo = new AccountDO();
        accountDo.setUid(usersDto.getUid());
        if (type == 1) {
            accountDo.setWidthdrawWxOpenId(openId);
            accountDo.setWidthdrawWxName(weixinName);
        } else {
            accountDo.setAlipayName(realName);
            accountDo.setAlipayAccount(alipayAccount);
        }
        accountDao.updateBySelective(accountDo);
        return new WithDrawCashVO(uid, purseDto.getDiamondNum() - diamondCost);
    }

    @Override
    public WithDrawCashVO bindAndWithdraw(Long uid, int type, Integer diamondNum, String account, String accountName,
                                          String pid, String token, String phone, String code) throws WebServiceException {
        // 判断UID是否存在
        if (uid == null || StringUtils.isBlank(pid)) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        // 判断提现类型是否正确
        if (type != 1 && type != 2 && type != 3) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        if (StringUtils.isBlank(account) || StringUtils.isBlank(accountName)) {
            throw new WebServiceException("请填写提现账号信息!");
        }

        checkWithdrawToken(token, uid);

        // 校验短信验证码
        boolean isRightCode = neteaseSmsManager.verifySmsCode(phone, code);
        if (!isRightCode) {
            throw new WebServiceException(WebServiceCode.SMS_CODE_ERROR);
        }

        // 判断用户是否存在
        UsersDTO usersDto = usersManager.getUser(uid);
        if (usersDto == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        // 判断是否有绑定支付宝或者银行卡
        if (StringUtils.isBlank(account) || StringUtils.isBlank(accountName)) {
            if ((StringUtils.isEmpty(usersDto.getAlipayAccount()) || StringUtils.isEmpty(usersDto.getAlipayAccountName())) && type == 2) {
                throw new WebServiceException("您还未完善支付宝账号信息！");
            } else if ((StringUtils.isEmpty(usersDto.getBankCard()) || StringUtils.isEmpty(usersDto.getBankCardName())) && type == 3) {
                throw new WebServiceException("您还未完善银行卡账号信息！");
            }
        }

        UserRealNameDTO userRealNameDTO = userRealNameManager.getOneByJedisId(uid.toString());
        if (userRealNameDTO == null || userRealNameDTO.getAuditStatus() == 2) {
            throw new WebServiceException(WebServiceCode.USER_REAL_NAME_NTO_VERIFIED);
        } else if (userRealNameDTO.getAuditStatus() == 0) {
            throw new WebServiceException(WebServiceCode.USER_REAL_NAME_AUDITING);
        }

        // 判断用户是否生成钱包
        UserPurseDTO purseDto = userPurseManager.getUserPurse(uid);
        if (purseDto == null || purseDto.getDiamondNum() == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        long increment = redisManager.incrByTime(RedisKey.withdraw_cash_limit.getKey(uid + ""), 5);
        if (increment > 1) {
            throw new WebServiceException(WebServiceCode.REQUEST_FREQUENT);
        }

        // 判断提现选项ID是否正确
        // WithDrawCashProdDTO withDrawCashProdDTO = cashProdManager.getWithDrawCashProdByPid(pid);
        // if (withDrawCashProdDTO == null) {
        //     throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        // }
        //
        // // 判断提现钻石数额是否正确
        // if (withDrawCashProdDTO.getDiamondNum() > purseDto.getDiamondNum()) {
        //     throw new WebServiceException(WebServiceCode.DIAMOND_NUM_NOT_ENOUGH);
        // }

        if (diamondNum == null || diamondNum <= 0) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        } else if (diamondNum > purseDto.getDiamondNum()) {
            throw new WebServiceException(WebServiceCode.DIAMOND_NUM_NOT_ENOUGH);
        }

        // 判断提现选项ID的提现金额是否正确
        // Long cashNum = cashProdManager.getCashNum(pid);
        // if (cashNum <= 0) {
        //     throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        // }

        // Double diamondCost = (cashNum * 10.0);
        Double diamondCost = (diamondNum * 10.0);

        // 更新用户钱包
        userPurseManager.updateReduceDiamond(uid, diamondCost, false);

        // 提现, 生成BillTransferRecord记录
        BillTransferDO transferDo = new BillTransferDO();
        Date now = new Date();
        transferDo.setUid(uid);
        transferDo.setBillId(UUIDUtils.get());
        transferDo.setTranType((byte) 1);
        transferDo.setRealTranType((byte) type);
        transferDo.setCost(diamondCost);
        // transferDo.setMoney(cashNum.intValue());
        transferDo.setMoney(diamondNum.intValue());
        transferDo.setBillStatus(BillTransferStatus.ING.getValue());
        transferDo.setCreateTime(now);
        transferDo.setUpdateTime(now);
        transferDo.setApplyWithdrawalAccount(account);
        transferDo.setApplyWithdrawalName(accountName);
        transferDao.insert(transferDo);

        // FIXME: 兼容管理后台
        Date date = new Date();
        BillRecordDO billRecord = new BillRecordDO();
        billRecord.setBillId(transferDo.getBillId());
        billRecord.setUid(uid);
        billRecord.setTargetUid(uid);
        billRecord.setObjId(null);
        billRecord.setObjType(BillRecordType.getCash);
        billRecord.setBillStatus(BillTransferStatus.ING.getValue());
        billRecord.setDiamondNum(-diamondCost);
        billRecord.setGiftId(type); // 用来区分微信, 支付宝还是银行卡提现
        billRecord.setGoldNum(null);
        // billRecord.setMoney(cashNum);
        billRecord.setMoney((long) diamondNum);
        billRecord.setCreateTime(date);
        recordDao.save(billRecord);
        redisManager.hset(RedisKey.with_draw_type.getKey(), uid.toString(), type + "");

        // 更新提现微信账号
        // AccountDO accountDo = new AccountDO();
        // accountDo.setUid(usersDto.getUid());
        // if (type == 1) {
        //     accountDo.setWidthdrawWxOpenId(account);
        //     accountDo.setWidthdrawWxName(accountName);
        // } else {
        //     accountDo.setAlipayName(account);
        //     accountDo.setAlipayAccount(accountName);
        // }
        // accountDao.updateBySelective(accountDo);

        UsersDO usersDO = new UsersDO();
        usersDO.setUid(usersDto.getUid());
        if (type == 2) {
            usersDO.setAlipayAccount(account);
            usersDO.setAlipayAccountName(accountName);
        } else if (type == 3) {
            usersDO.setBankCard(account);
            usersDO.setBankCardName(accountName);
        }
        usersDao.update(usersDO);

        redisManager.hdel(RedisKey.user.getKey(), String.valueOf(usersDO.getUid()));

        return new WithDrawCashVO(uid, purseDto.getDiamondNum() - diamondCost);
    }

    private void checkWithdrawToken(String token, Long uid) throws WebServiceException {
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
    }

    private String getWithdrawToken(Long uid) {
        String cacheToken = redisManager.get(RedisKey.withdraw_token.getKey(uid.toString()));
        if (StringUtils.isEmpty(cacheToken)) {
            String token = JwtUtils.generateToken(uid);
            redisManager.set(RedisKey.withdraw_token.getKey(uid.toString()), token, 10, TimeUnit.MINUTES);
            return token;
        } else {
            return cacheToken;
        }
    }

	@Override
	public WebServiceMessage resetUserAccount(Long uid, String account, String accountName, Integer accountType,
			String appVersion) throws WebServiceException {
		if (uid == null ||StringUtils.isBlank(account) || StringUtils.isBlank(accountName) || accountType <= 0 || accountType == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        UsersDTO usersDto = usersManager.getUser(uid);
        if (usersDto == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        if (accountType == 1) {
            UsersDO usersDo = new UsersDO();
            usersDo.setUid(uid);
            usersDo.setAlipayAccount(account);
            usersDo.setAlipayAccountName(accountName);
            usersDao.update(usersDo);
            
            usersDto.setAlipayAccount(account);
            usersDto.setAlipayAccountName(accountName);
            redisManager.hset(RedisKey.user.getKey(), String.valueOf(uid), gson.toJson(usersDto));
        } else if (accountType == 2) {
            UsersDO usersDo = new UsersDO();
            usersDo.setUid(uid);
            usersDo.setBankCard(account);
            usersDo.setBankCardName(accountName);
            usersDao.update(usersDo);

            usersDto.setBankCard(account);
            usersDto.setBankCardName(accountName);
            redisManager.hset(RedisKey.user.getKey(), String.valueOf(uid), gson.toJson(usersDto));
        }
        return WebServiceMessage.success("提交成功");
	}
}
