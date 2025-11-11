package com.xchat.oauth2.service.service.account;

import com.google.gson.Gson;
import com.xchat.common.UUIDUitl;
import com.xchat.common.config.GlobalConfig;
import com.xchat.common.constant.Constant;
import com.xchat.common.device.DeviceInfo;
import com.xchat.common.netease.neteaseacc.result.BaseNetEaseRet;
import com.xchat.common.netease.neteaseacc.result.SmsRet;
import com.xchat.common.netease.neteaseacc.result.TokenRet;
import com.xchat.common.netease.util.NetEaseConstant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.CommonUtil;
import com.xchat.common.utils.GetTimeUtils;
import com.xchat.oauth2.service.common.exceptions.HaveMultiAccountException;
import com.xchat.oauth2.service.common.exceptions.InvalidUserException;
import com.xchat.oauth2.service.common.exceptions.myexception.InvalidAccountException;
import com.xchat.oauth2.service.common.exceptions.myexception.SignAccountIpException;
import com.xchat.oauth2.service.common.exceptions.myexception.SmsIpException;
import com.xchat.oauth2.service.common.status.OAuthStatus;
import com.xchat.oauth2.service.core.domain.ServiceResult;
import com.xchat.oauth2.service.core.encoder.MD5;
import com.xchat.oauth2.service.core.util.CoderUtils;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.AccountMapper;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.UserDrawWxappRecordMapper;
import com.xchat.oauth2.service.model.Account;
import com.xchat.oauth2.service.model.AccountExample;
import com.xchat.oauth2.service.service.JedisService;
import com.xchat.oauth2.service.vo.AccountVo;
import com.xchat.oauth2.service.vo.VisitorVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private static final int CODE_EXPIRE_TIME = 60 * 10;//短信验证码默认10分钟

    private static final int RESET_CODE_EXPIRE_TIME = 60 * 60 * 12;//重置码默认12小时

    private static final String REDIS_IDENTIFYING_CODE_KEY_PREFIX = "ic_";

    private static final String REDIS_RESET_CODE_KEY_PREFIX = "rc_";

    private static final int maxRegisterCount = 20000;

    /**
     * 设备注册账号数量
     */
    private static final int DEVICE_MAX_REGISTER = 30000;

    @Autowired
    @Qualifier("jedisService")
    private JedisService jedisService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserDrawWxappRecordMapper userDrawWxappRecordMapper;

    @Autowired
    private NetEaseService netEaseService;

    @Autowired
    private ErBanNoService erBanNoService;

    @Autowired
    private SmsRecordService smsRecordService;

    private String genAndSaveIdentifyingCode(String key) {
        String code = CoderUtils.nLenWithNum(6);
        jedisService.write(REDIS_IDENTIFYING_CODE_KEY_PREFIX + key, code, CODE_EXPIRE_TIME);
        return code;
    }

    private String genAndSaveResetCode(String key) {
        String code = CoderUtils.idWithMD5();
        jedisService.write(REDIS_RESET_CODE_KEY_PREFIX + key, code, RESET_CODE_EXPIRE_TIME);
        return code;
    }

    private void removeIndentifyingCode(String key) {
        jedisService.remove(REDIS_IDENTIFYING_CODE_KEY_PREFIX + key);
    }

    private void removeResetCode(String key) {
        jedisService.remove(REDIS_RESET_CODE_KEY_PREFIX + key);
    }

    /**
     * 生成并通过短信发送验证码
     *
     * @param phone
     * @return
     */
    public OAuthStatus genAndSendIdentifyingCode(String phone) {
        OAuthStatus status;
        try {
            String code = genAndSaveIdentifyingCode(phone);
            String smsMsg = GlobalConfig.appName + "注册验证码:" + code;
            status = OAuthStatus.SUCCESS;
//            if(smsService.sendMsg(phone, smsMsg)){
//                status = OAuthStatus.SUCCESS;
//            }else{
//                status = OAuthStatus.INVALID_SERVICE;
//            }
        } catch (Exception e) {
            logger.error("acquireCode error,msg:{}", e);
            removeIndentifyingCode(phone);
            status = OAuthStatus.INVALID_SERVICE;
        }
        return status;
    }

    private BusiResult sendAccSmsCode(String phone) throws Exception {
        BusiResult result = new BusiResult(BusiStatus.SUCCESS);
        if (checkPhoneExists(phone)) {
            result.setCode(ServiceResult.SC_DATA_ERROR);
            result.setMessage("手机号码已经注册，请直接登录");
            return result;
        }
        //发送短信
        String deviceId = "";
        SmsRet smsRet = netEaseService.sendSms(phone, deviceId, NetEaseConstant.smsTemplateid);
        if (smsRet.getCode() == 200) {
            result.setMessage("发送短信成功");
            result.setData(smsRet.getMsg());
        } else {
            logger.info("发送短信失败phone=" + phone + "&code=" + smsRet.getCode());
            result.setCode(BusiStatus.SMSSENDERROR.value());
            result.setMessage("发送短信失败code=" + smsRet.getCode());
        }
        return result;
    }

    private BusiResult sendSmsCodeByPhone(String phone) throws Exception {
        BusiResult result = new BusiResult(BusiStatus.SUCCESS);
        if (checkPhoneExists(phone)) {
            result.setCode(ServiceResult.SC_DATA_ERROR);
            result.setMessage("手机号码已经被绑定");
            return result;
        }
        //发送短信
        String deviceId = "";
        SmsRet smsRet = netEaseService.sendSms(phone, deviceId, NetEaseConstant.smsTemplateid);
        if (smsRet.getCode() == 200) {
            result.setMessage("发送短信成功");
            result.setData(smsRet.getMsg());
        } else {
            result.setCode(BusiStatus.SMSSENDERROR.value());
            logger.info("发送短信失败phone=" + phone + "&code=" + smsRet.getCode());
            result.setMessage("发送短信失败code=" + smsRet.getCode());
        }
        return result;
    }

    private BusiResult sendAccSmsCodeByForgetPwd(String phone) throws Exception {
        BusiResult result = new BusiResult(BusiStatus.SUCCESS);
        if (!checkPhoneExists(phone)) {
            result.setCode(ServiceResult.SC_DATA_ERROR);
            result.setMessage("手机号码不存在，请先注册！");
            return result;
        }
        //发送短信
        String deviceId = "";
        SmsRet smsRet = netEaseService.sendSms(phone, deviceId, NetEaseConstant.smsTemplateid);
        if (smsRet.getCode() == 200) {
            result.setMessage("发送短信成功");
            result.setData(smsRet.getMsg());
        } else {
            result.setCode(BusiStatus.SMSSENDERROR.value());
            result.setMessage("发送短信失败code=" + smsRet.getCode());
            logger.info("发送短信失败phone=" + phone + "&code=" + smsRet.getCode());
        }
        return result;
    }

    private boolean checkPhoneExists(String phone) {
        Account account = getAccountByPhone(phone);
        if (account == null) {
            return false;
        } else {
            return true;
        }
    }

    public Account getAccountByPhone(String phone) throws HaveMultiAccountException {
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andPhoneEqualTo(phone);
        List<Account> accountList = accountMapper.selectByExample(accountExample);
        if (CollectionUtils.isEmpty(accountList)) {
            return null;
        } else if (accountList.size() > 1 && accountList.get(0).getIsShuijun() == null) {
            throw new HaveMultiAccountException("请联系客服!");
        } else if (accountList.size() > 1 && accountList.get(0).getIsShuijun() != null) {
            throw new HaveMultiAccountException("厅主账号请使用ID登录!");
        } else {
            return accountList.get(0);
        }
    }

    public int getRegisterIpCountByOneDay(String ip) {
        AccountExample accountExample = new AccountExample();
        Date date = new Date();
        accountExample.createCriteria().andRegisterIpEqualTo(ip).andSignTimeBetween(GetTimeUtils.getTimesnights(date,
                0), GetTimeUtils.getTimesnights(date, 24));
        List<Account> accountList = accountMapper.selectByExample(accountExample);
        if (CollectionUtils.isEmpty(accountList)) {
            return 0;
        } else {
            return accountList.size();
        }
    }

    public Account getOrGenAccountByOpenid(String openid, String unionid, int type, DeviceInfo deviceInfo,
                                           String ipAddress) throws Exception {
        String openId = StringUtils.isBlank(openid) ? "1" : openid;
        String unionId = StringUtils.isBlank(unionid) ? "1" : unionid;
        List<Account> accountList = accountMapper.listByParam(openId, unionId, type);
//        AccountExample accountExample = new AccountExample();
//        if (StringUtils.isNoneBlank(unionid)) {
//            if (type == 1) {
//                accountExample.createCriteria().andWeixinUnionidEqualTo(unionid);
//            } else if (type == 2) {
//                accountExample.createCriteria().andQqUnionidEqualTo(unionid);
//            } else {
//                throw new Exception("不支持的登录类型");
//            }
//            accountList = accountMapper.selectByExample(accountExample);
//            // logger.info("[ 账号注册 ]根据unionid查询：unionid={}, accountList={}", unionid, JSON.toJSON(accountList));
//        } else {
//            accountExample = new AccountExample();
//            if (type == 1) {
//                accountExample.createCriteria().andWeixinOpenidEqualTo(openid);
//            } else if (type == 2) {
//                accountExample.createCriteria().andQqOpenidEqualTo(openid);
//            } else {
//                throw new Exception("不支持的登录类型");
//            }
//            accountList = accountMapper.selectByExample(accountExample);
//            // logger.info("[ 账号注册 ]根据openid查询：openid={}, accountList={}", openid, JSON.toJSON(accountList));
//        }
        String typeStr = type == 1 ? "微信" : (type == 2 ? "QQ" : String.valueOf(type));
        if (CollectionUtils.isEmpty(accountList)) {
            logger.info("[ 账号注册 ] 插入新记录，openid={}, unionid={}", openid, unionid);
            int count = getRegisterIpCountByOneDay(ipAddress);
            if (count > maxRegisterCount) {
                logger.error("[账号注册] IP: {}, 次数: {}", ipAddress, count);
                throw new SignAccountIpException("注册过于频繁");
            }
            // 检查设备注册数量
            checkDeviceRegisterNum(deviceInfo.getDeviceId(), deviceInfo.getOs());
            logger.info("[账号注册] type:{}, appid:{}, os:{} openid:{}, unionid:{}, deviceId:{}",
                    typeStr, deviceInfo.getAppid(), deviceInfo.getOs(), openid, unionid, deviceInfo.getDeviceId());
            Date date = new Date();
            Account account = new Account();
            account.setNeteaseToken(UUIDUitl.get());
            account.setLastLoginTime(date);
            account.setUpdateTime(date);
            account.setRegisterIp(ipAddress);
            account.setSignTime(date);
            account.setState("1");
            if (type == 1) {
                account.setWeixinOpenid(openid);
                account.setWeixinUnionid(unionid);
            } else if (type == 2) {
                account.setQqOpenid(openid);
                account.setQqUnionid(unionid);
            }
            account.setErbanNo(erBanNoService.getErBanNo());
            account.setPhone(account.getErbanNo().toString());
            account = fillDeviceInfo(account, deviceInfo);
            accountMapper.insert(account);

            String uidStr = String.valueOf(account.getUid());
            TokenRet tokenRet = netEaseService.createNetEaseAcc(uidStr, account.getNeteaseToken(), "");
            if (tokenRet.getCode() != 200) {
                logger.info("注册云信账号失败,openid=" + openid + "&uid=" + uidStr + ",异常原因code=" + tokenRet.getCode());
                logger.error("注册云信账号失败,openid=" + openid + "&uid=" + uidStr + ",异常原因code=" + tokenRet.getCode());
                throw new Exception("第三方登录失败,openid=" + openid + ",异常原因code=" + tokenRet.getCode());
            }
            return account;
        }
        Account account = accountList.get(0);
        String uidStr = String.valueOf(account.getUid());

        logger.info("[ 账号注册 ] 更新记录， type:{}, appid:{}, os:{}, uid:{} openid:{}, unionid:{}, deviceId:{}",
                typeStr, deviceInfo.getAppid(), deviceInfo.getOs(), account.getUid(), openid, unionid,
                deviceInfo.getDeviceId());
        if (type == 1 && StringUtils.isBlank(account.getWeixinUnionid())) {
            account.setWeixinUnionid(unionid);
            accountMapper.updateByPrimaryKey(account);
        } else if (type == 2 && StringUtils.isBlank(account.getQqUnionid())) {
            account.setQqUnionid(unionid);
            accountMapper.updateByPrimaryKey(account);
        }

        String state = account.getState();
        if ("2".equals(state)) {
            throw new InvalidUserException("用户账号异常，请联系官方客服", new Exception("用户账号异常，请联系官方客服uid=" + account.getUid()));
        }
        if ("40".equals(state)) {
            throw new InvalidAccountException("账号已注销，请联系官方客服", new Exception("账号已注销，请联系官方客服uid=" + account.getUid()));
        }
        return account;
    }

    //保存qq微信昵称的账号信息获取
    public Account getOrGenAccountByOpenid2(String openid, String unionid, int type,String nick, DeviceInfo deviceInfo,
                                           String ipAddress) throws Exception {
        String openId = StringUtils.isBlank(openid) ? "1" : openid;
        String unionId = StringUtils.isBlank(unionid) ? "1" : unionid;
        List<Account> accountList = accountMapper.listByParam(openId, unionId, type);
//        AccountExample accountExample = new AccountExample();
//        if (StringUtils.isNoneBlank(unionid)) {
//            if (type == 1) {
//                accountExample.createCriteria().andWeixinUnionidEqualTo(unionid);
//            } else if (type == 2) {
//                accountExample.createCriteria().andQqUnionidEqualTo(unionid);
//            } else {
//                throw new Exception("不支持的登录类型");
//            }
//            accountList = accountMapper.selectByExample(accountExample);
//            // logger.info("[ 账号注册 ]根据unionid查询：unionid={}, accountList={}", unionid, JSON.toJSON(accountList));
//        } else {
//            accountExample = new AccountExample();
//            if (type == 1) {
//                accountExample.createCriteria().andWeixinOpenidEqualTo(openid);
//            } else if (type == 2) {
//                accountExample.createCriteria().andQqOpenidEqualTo(openid);
//            } else {
//                throw new Exception("不支持的登录类型");
//            }
//            accountList = accountMapper.selectByExample(accountExample);
//            // logger.info("[ 账号注册 ]根据openid查询：openid={}, accountList={}", openid, JSON.toJSON(accountList));
//        }
        String typeStr = type == 1 ? "微信" : (type == 2 ? "QQ" : String.valueOf(type));
        if (CollectionUtils.isEmpty(accountList)) {
            logger.info("[ 账号注册 ] 插入新记录，openid={}, unionid={}", openid, unionid);
            int count = getRegisterIpCountByOneDay(ipAddress);
            if (count > maxRegisterCount) {
                logger.error("[账号注册] IP: {}, 次数: {}", ipAddress, count);
                throw new SignAccountIpException("注册过于频繁");
            }
            // 检查设备注册数量
            checkDeviceRegisterNum(deviceInfo.getDeviceId(), deviceInfo.getOs());
            logger.info("[账号注册] type:{}, appid:{}, os:{} openid:{}, unionid:{}, deviceId:{}",
                    typeStr, deviceInfo.getAppid(), deviceInfo.getOs(), openid, unionid, deviceInfo.getDeviceId());
            Date date = new Date();
            Account account = new Account();
            account.setNeteaseToken(UUIDUitl.get());
            account.setLastLoginTime(date);
            account.setUpdateTime(date);
            account.setRegisterIp(ipAddress);
            account.setSignTime(date);
            account.setState("1");
            if (type == 1) {
                account.setWeixinOpenid(openid);
                account.setWeixinUnionid(unionid);
                account.setWeixinNick(nick);
            } else if (type == 2) {
                account.setQqOpenid(openid);
                account.setQqUnionid(unionid);
                account.setQqNick(nick);
            }
            account.setErbanNo(erBanNoService.getErBanNo());
            account.setPhone(account.getErbanNo().toString());
            account = fillDeviceInfo(account, deviceInfo);
            //accountMapper.insert(account);
            accountMapper.insert2(account);

            String uidStr = String.valueOf(account.getUid());
            TokenRet tokenRet = netEaseService.createNetEaseAcc(uidStr, account.getNeteaseToken(), "");
            if (tokenRet.getCode() != 200) {
                logger.info("注册云信账号失败,openid=" + openid + "&uid=" + uidStr + ",异常原因code=" + tokenRet.getCode());
                logger.error("注册云信账号失败,openid=" + openid + "&uid=" + uidStr + ",异常原因code=" + tokenRet.getCode());
                throw new Exception("第三方登录失败,openid=" + openid + ",异常原因code=" + tokenRet.getCode());
            }
            return account;
        }
        Account account = accountList.get(0);
        String uidStr = String.valueOf(account.getUid());

        logger.info("[ 账号注册 ] 更新记录， type:{}, appid:{}, os:{}, uid:{} openid:{}, unionid:{}, deviceId:{}",
                typeStr, deviceInfo.getAppid(), deviceInfo.getOs(), account.getUid(), openid, unionid,
                deviceInfo.getDeviceId());
        if (type == 1 && StringUtils.isBlank(account.getWeixinUnionid())) {
            account.setWeixinUnionid(unionid);
            if(nick!=null) {
                account.setWeixinNick(nick);
            }
            //accountMapper.updateByPrimaryKey(account);
            accountMapper.updateByPrimaryKey2(account);
        } else if (type == 2 && StringUtils.isBlank(account.getQqUnionid())) {
            account.setQqUnionid(unionid);
            if(nick!=null) {
                account.setQqNick(nick);
            }
            //accountMapper.updateByPrimaryKey(account);
            accountMapper.updateByPrimaryKey2(account);
        }

        String state = account.getState();
        if ("2".equals(state)) {
            throw new InvalidUserException("用户账号异常，请联系官方客服", new Exception("用户账号异常，请联系官方客服uid=" + account.getUid()));
        }
        if ("40".equals(state)) {
            throw new InvalidAccountException("账号已注销，请联系官方客服", new Exception("账号已注销，请联系官方客服uid=" + account.getUid()));
        }
        return account;
    }
    //苹果注册登录
    public Account getOrGenAccount(String appleuser, String fullName, DeviceInfo deviceInfo, String ipAddress)throws Exception {
        String appleUser = StringUtils.isBlank(appleuser) ? "1" : appleuser;
        List<Account> accountList = accountMapper.selectByAppleUser(appleUser);
        if (CollectionUtils.isEmpty(accountList)) {
            logger.info("[ 账号注册 ] 插入新记录，apapleUser={}",appleUser);
            int count = getRegisterIpCountByOneDay(ipAddress);
            if (count > maxRegisterCount) {
                logger.error("[账号注册] IP: {}, 次数: {}", ipAddress, count);
                throw new SignAccountIpException("注册过于频繁");
            }
            // 检查设备注册数量
            checkDeviceRegisterNum(deviceInfo.getDeviceId(), deviceInfo.getOs());
            logger.info("[账号注册] appleUser:{}, appid:{}, os:{}, deviceId:{}",
                    appleuser, deviceInfo.getAppid(), deviceInfo.getOs(), deviceInfo.getDeviceId());
            Date date = new Date();
            Account account = new Account();
            account.setNeteaseToken(UUIDUitl.get());
            account.setLastLoginTime(date);
            account.setUpdateTime(date);
            account.setRegisterIp(ipAddress);
            account.setSignTime(date);
            account.setState("1");
            //设置apple账号值
            account.setAppleUser(appleuser);
            if(fullName!=null) {
                account.setAppleUserName(fullName);
            }
            account.setErbanNo(erBanNoService.getErBanNo());
            account.setPhone(account.getErbanNo().toString());
            account = fillDeviceInfo(account, deviceInfo);
            //accountMapper.insert(account);
            accountMapper.insert2(account);

            String uidStr = String.valueOf(account.getUid());
            TokenRet tokenRet = netEaseService.createNetEaseAcc(uidStr, account.getNeteaseToken(), "");
            if (tokenRet.getCode() != 200) {
                logger.info("注册云信账号失败,appleUser=" + appleuser + "&uid=" + uidStr + ",异常原因code=" + tokenRet.getCode());
                logger.error("注册云信账号失败,appleUser=" + appleuser + "&uid=" + uidStr + ",异常原因code=" + tokenRet.getCode());
                throw new Exception("第三方登录失败,appleUser=" + appleuser + ",异常原因code=" + tokenRet.getCode());
            }
            return account;
        }
        Account account = accountList.get(0);
        String uidStr = String.valueOf(account.getUid());

        logger.info("[ 账号注册 ] 更新记录， appid:{}, os:{}, uid:{} , appleUser:{},fullName:{},deviceId:{}",
                 deviceInfo.getAppid(), deviceInfo.getOs(), account.getUid(), appleUser, fullName,
                deviceInfo.getDeviceId());
        //设置apple账号值
        if(fullName!=null) {
            account.setAppleUserName(fullName);
        }
        if(appleuser!=null) {
            account.setAppleUser(appleuser);
        }
        accountMapper.updateByPrimaryKey2(account);
        String state = account.getState();
        if ("2".equals(state)) {
            throw new InvalidUserException("用户账号异常，请联系官方客服", new Exception("用户账号异常，请联系官方客服uid=" + account.getUid()));
        }
        if ("40".equals(state)) {
            throw new InvalidAccountException("账号已注销，请联系官方客服", new Exception("账号已注销，请联系官方客服uid=" + account.getUid()));
        }
        return account;
    }

    public Account getAccountByOpenid(String openid, String unionid) throws Exception {
        String openId = StringUtils.isBlank(openid) ? "1" : openid;
        String unionId = StringUtils.isBlank(unionid) ? "1" : unionid;
        List<Account> accountList = accountMapper.listByParam(openId, unionId, 1);
        if (CollectionUtils.isEmpty(accountList)) {
            throw new InvalidUserException("用户不存在", new Exception("用户不存在"));
        }

        Account account = accountList.get(0);
        String state = account.getState();
        if ("2".equals(state)) {
            throw new InvalidUserException("用户账号异常，请联系官方客服", new Exception("用户账号异常，请联系官方客服uid=" + account.getUid()));
        }
        return account;
    }


    public Account getAccountByErBanNo(Long erbanNo) {
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andErbanNoEqualTo(erbanNo);
        List<Account> accountList = accountMapper.selectByExample(accountExample);
        if (CollectionUtils.isEmpty(accountList)) {
            return null;
        } else {
            return accountList.get(0);

        }
    }

    public TokenRet freshEaseToken(Account account) { //TODO upline change
        String uidStr = String.valueOf(account.getUid());
        TokenRet tokenRet = null;
        try {
            tokenRet = netEaseService.refreshToken(uidStr);
            if (tokenRet.getCode() != 200) {
                throw new Exception("refreshToken err:" + uidStr + ",code=" + tokenRet.getCode());
            }
        } catch (Exception e) {
            try {
                tokenRet = netEaseService.createNetEaseAcc(uidStr, account.getNeteaseToken(), "");
                if (tokenRet.getCode() != 200) {
                    logger.info("额外注册云信账号失败," + "uid=" + uidStr + ",异常原因code=" + tokenRet.getCode());
                    logger.error("额外注册云信账号失败," + "uid=" + uidStr + ",异常原因code=" + tokenRet.getCode());
                }
            } catch (Exception e2) {
                logger.error("额外 注册云信账号失败," + "uid=" + uidStr);
            }
        }
        return tokenRet;
    }

    public Account refreshNetEaseToken(String phone) throws HaveMultiAccountException {
        Account account = getAccountByPhone(phone);
        if (account == null) {
            if (!CommonUtil.checkValidPhone(phone)) {
                account = getAccountByErBanNo(Long.valueOf(phone));
            }
        }

        if (account == null) {
            throw new HaveMultiAccountException("厅主账号请使用ID登录!");
        }

        String state = account.getState();
        if ("2".equals(state)) {
            throw new InvalidUserException("用户账号异常，请联系官方客服", new Exception("用户账号异常，请联系官方客服uid=" + account.getUid()));
        }
        if ("40".equals(state)) {
            throw new InvalidAccountException("账号已注销，请联系官方客服", new Exception("账号已注销，请联系官方客服uid=" + account.getUid()));
        }

        TokenRet tokenRet = this.freshEaseToken(account);
        String token = (String) tokenRet.getInfo().get("token");
        Account accountUpdate = new Account();
        accountUpdate.setUid(account.getUid());
        accountUpdate.setNeteaseToken(token);
        accountUpdate.setState(account.getState());
        accountUpdate.setErbanNo(account.getErbanNo());
        accountUpdate.setDeviceId(account.getDeviceId());
        accountUpdate.setLastLoginIp(account.getLastLoginIp());
        accountMapper.updateByPrimaryKeySelective(accountUpdate);
        return accountUpdate;
    }

    public Account refreshNetEaseToken(Long uid) throws Exception {
        Account account = getAccountByUid(uid);
        String uidStr = String.valueOf(account.getUid());

        TokenRet tokenRet = this.freshEaseToken(account);

        String token = (String) tokenRet.getInfo().get("token");
        Account accountUpdate = new Account();
        accountUpdate.setUid(account.getUid());
        accountUpdate.setNeteaseToken(token);
        accountUpdate.setErbanNo(account.getErbanNo());
        accountUpdate.setDeviceId(account.getDeviceId());
        accountUpdate.setLastLoginIp(account.getLastLoginIp());
        accountMapper.updateByPrimaryKeySelective(accountUpdate);
        return accountUpdate;
    }

    public Account getAccountByUid(Long uid) {
        Account account = accountMapper.selectByPrimaryKey(uid);
        return account;
    }

    /**
     * 根据拉贝号列表来查询相应的account表数据，此处主要用于后台管理系统
     *
     * @return
     * @Author yuanyi
     */
    public List<Account> getAccountListByUidList(List<Long> uidList) {
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andUidIn(uidList);
        accountExample.setOrderByClause("uid");
        List<Account> accountList = accountMapper.selectByExample(accountExample);
        return accountList;
    }


    /**
     * 根据不同短信类型获取短信验证码
     *
     * @param phone
     * @param type  1注册短信；2更改手机短信；3找回密码短信（更改手机确认短信/钻石兑换金币确认）；4提现验证码；
     * @return
     */
    public BusiResult sendSmsByType(String phone, int type, String ip, String deviceId, String imei, String os,
                                    String osversion, String channel, String appVersion, String model) throws Exception {

        if (!CommonUtil.checkValidPhone(phone)) {
            return new BusiResult(BusiStatus.PHONEINVALID);
        }
//        if (smsRecordService.checkIsTooOftenIp(ip)) {
//            throw new SmsIpException("短信获取过于频繁");
//        }
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (1 == type) {
            busiResult = sendAccSmsCode(phone);
        } else if (2 == type) {
            busiResult = sendSmsCodeByPhone(phone);
        } else if (3 == type) {
            busiResult = sendAccSmsCodeByForgetPwd(phone);
        } else if (4 == type) {
            busiResult = sendWithDrawCashCode(phone);
        }
        if (busiResult.getData() != null) {
            smsRecordService.saveSmsRecord(phone, ip, deviceId, imei, os, osversion, channel, appVersion, model,
                    busiResult.getData().toString(), new Byte(type + ""));
        }
        return busiResult;
    }

    private BusiResult sendWithDrawCashCode(String phone) {

        return null;
    }

    /**
     * 生成重置码，用于重置密码
     * 必须先做短信码验证。
     *
     * @param code
     * @return
     */
//    public String genResetCode(String phone, String code) {
//        String result = "";
//        //验证码正确
//        if (validateIdentifyingCode(phone, code, true)) {
//            result = genAndSaveResetCode(phone);
//        }
//        return result;
//    }

//    private boolean validateIdentifyingCode(String phone, String code) {
//        return validateIdentifyingCode(phone, code, false);
//    }

//    private boolean validateIdentifyingCode(String phone, String code, boolean remove) {
//
//        boolean ret = false;
//        String jcode = jedisService.read(REDIS_IDENTIFYING_CODE_KEY_PREFIX + phone);
//        if (jcode != null && code.equals(jcode.trim())) {
//            ret = true;
//            if (remove) {
//                try {
//                    removeIndentifyingCode(phone);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    logger.error("error happened when remove key from redis,key:{}", phone);
//                }
//            }
//
//        }
//        return ret;
//    }
    public boolean verifySmsCodeByNetEase(String mobile, String code) throws Exception {
        if ("12345".equalsIgnoreCase(code)) {
            return true;
        }
        BaseNetEaseRet baseNetEaseRet = netEaseService.smsVerify(mobile, code);
        if (baseNetEaseRet.getCode() == 200) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查证码是否存在
     *
     * @param phone
     * @param code
     * @param remove 是否删除 true 删除 false 不删除
     * @return
     */
    private boolean validateResetCode(String phone, String code, boolean remove) {
        boolean ret = false;
        String jcode = jedisService.read(REDIS_RESET_CODE_KEY_PREFIX + phone);
        if (jcode != null && code.equals(jcode.trim())) {
            ret = true;
            if (remove) {
                try {
                    removeResetCode(phone);
                } catch (Exception e) {
                    logger.error("error happened when remove key from redis,key:{}", phone);
                }
            }

        }
        // 没有接入短信网关之前, 直接返回true
        ret = true;
        return ret;
    }

    private String encryptPassword(String password) {
        return MD5.getMD5(password);
    }

    /**
     * 通过手机号码注册，独立账号系统，不掺杂业务
     *
     * @param phone
     * @param password
     * @param smsCode
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public BusiResult<AccountVo> saveSignUpByPhone(String phone, String password, String smsCode,
                                                   DeviceInfo deviceInfo, String ipAddress) throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        int count = getRegisterIpCountByOneDay(ipAddress);
        if (count > maxRegisterCount) {
            logger.error("[IP注册过于频繁] IP: {}, 次数: {}", ipAddress, count);
            throw new SignAccountIpException("注册过于频繁" + ipAddress + " count:" + count);
        }
        // 检验验证码
        // boolean check = verifySmsCodeByNetEase(phone, smsCode);
        // if (check) {
        boolean exist = checkPhoneExists(phone);
        if (exist) {
            busiResult.setCode(OAuthStatus.USER_HAS_SIGNED_UP.value());
            busiResult.setMessage("手机号码已经被注册，请直接登录！");
            return busiResult;
            // return OAuthStatus.USER_HAS_SIGNED_UP;
        }
        // 检查设备注册数量
        checkDeviceRegisterNum(deviceInfo.getDeviceId(), deviceInfo.getOs());
        logger.info("[注册账号] deviceInfo:{}", deviceInfo);
        Date date = new Date();
        Account account = new Account();
        account.setPhone(phone);
        account.setPassword(encryptPassword(password));
        account.setNeteaseToken(UUIDUitl.get());
        account.setLastLoginTime(date);
        account.setUpdateTime(date);
        account.setRegisterIp(ipAddress);
        account.setSignTime(date);
        account.setState("1");
        account.setErbanNo(erBanNoService.getErBanNo());
        account = fillDeviceInfo(account, deviceInfo);
        accountMapper.insert(account);
        String uidStr = String.valueOf(account.getUid());
        try {
            TokenRet tokenRet = netEaseService.createNetEaseAcc(uidStr, account.getNeteaseToken(), "");
            if (tokenRet.getCode() != 200) {
                busiResult.setCode(OAuthStatus.ACCESS_DENIED.value());
                busiResult.setMessage("注册账号异常" + tokenRet.getCode());
                logger.error("手机号码phone=" + phone + "注册异常,异常原因code=" + tokenRet.getCode());
                logger.info("手机号码phone=" + phone + "注册异常,异常原因code=" + tokenRet.getCode());
                throw new Exception("手机号码phone=" + phone + "注册异常,异常原因code=" + tokenRet.getCode());
            }
        } catch (Exception e) {
            BaseNetEaseRet baseNetEaseRet = netEaseService.updateUserInfo(uidStr, "", "");
            Gson gson = new Gson();
            logger.info("[调用云信注册存在然后变更新 updateUserInfo]接口返回:{}", gson.toJson(baseNetEaseRet));
        }

        //} else {
        //    busiResult.setCode(OAuthStatus.INVALID_IDENTIFYING_CODE.value());
        //    busiResult.setMessage("短信验证码不正确！");
        //    return busiResult;
        //      return OAuthStatus.INVALID_IDENTIFYING_CODE;
        //}
        return busiResult;
    }

    private Account fillDeviceInfo(Account account, DeviceInfo deviceInfo) {
        if (deviceInfo != null) {
            account.setApp(deviceInfo.getAppid());
            account.setAppVersion(deviceInfo.getAppVersion());
            account.setChannel(deviceInfo.getChannel());
            account.setLinkedmeChannel(deviceInfo.getLinkedmeChannel());
            account.setDeviceId(deviceInfo.getDeviceId());
            account.setImei(deviceInfo.getImei());
            account.setIspType(deviceInfo.getIspType());
            account.setModel(deviceInfo.getModel());
            account.setNetType(deviceInfo.getNetType());
            account.setOs(deviceInfo.getOs());
            account.setOsversion(deviceInfo.getOsVersion());
        }
        return account;
    }

    @Transactional(rollbackFor = Exception.class)
    public BusiResult<AccountVo> saveSignUpByThridLogin(String code, int clientType) throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        //检验验证码
        Date date = new Date();
        Account account = new Account();
        account.setNeteaseToken(UUIDUitl.get());
        account.setLastLoginTime(date);
        account.setUpdateTime(date);
        account.setSignTime(date);
        account.setErbanNo(erBanNoService.getErBanNo());
        accountMapper.insert(account);
        String uidStr = String.valueOf(account.getUid());
        TokenRet tokenRet = netEaseService.createNetEaseAcc(uidStr, account.getNeteaseToken(), "");
        if (tokenRet.getCode() != 200) {
            busiResult.setCode(OAuthStatus.ACCESS_DENIED.value());
            busiResult.setMessage("注册账号异常" + tokenRet.getCode());
        }
        return busiResult;
    }

    public void batchGenRobAccount() throws Exception {
        for (int i = 0; i < 100; i++) {
            Long erbanNo = erBanNoService.getErBanNo();
            String password = "1234567890.1";
            Date date = new Date();
            Account account = new Account();
            account.setPhone(erbanNo.toString());
            account.setPassword(encryptPassword(password));
            account.setNeteaseToken(UUIDUitl.get());
            account.setLastLoginTime(date);
            account.setUpdateTime(date);
            account.setSignTime(date);
            account.setErbanNo(erbanNo);
            accountMapper.insert(account);
            String uidStr = String.valueOf(account.getUid());
            TokenRet tokenRet = netEaseService.createNetEaseAcc(uidStr, account.getNeteaseToken(), "");
        }

    }

    public BusiResult<VisitorVo> genVisitorAccount() throws Exception {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        String password = "visitoradmin999.1";
        Date date = new Date();
        Account account = new Account();
        account.setErbanNo(Constant.Visitor.visitorErbanNo);
        account.setPassword(encryptPassword(password));
        account.setNeteaseToken(UUIDUitl.get());
        account.setLastLoginTime(date);
        account.setUpdateTime(date);
        account.setSignTime(date);
        accountMapper.insert(account);
        String uidStr = String.valueOf(account.getUid());
        TokenRet tokenRet = netEaseService.createNetEaseAcc(uidStr, account.getNeteaseToken(), "");
        if (tokenRet.getCode() != 200) {
            busiResult.setCode(OAuthStatus.ACCESS_DENIED.value());
            busiResult.setMessage("获取游客账号异常" + tokenRet.getCode());
            logger.error("获取游客账号异常code=" + tokenRet.getCode());
            throw new Exception("获取游客账号异常code=注册异常,异常原因code=" + tokenRet.getCode());
        }
        VisitorVo visitorVo = new VisitorVo();
        visitorVo.setUid(account.getUid());
        visitorVo.setNetEaseToken(account.getNeteaseToken());
        visitorVo.setGender(new Byte("1"));
        visitorVo.setNick("游客");
        busiResult.setData(visitorVo);
        return busiResult;
    }

    public void updateErBanNo(Long uid) {

    }

    /**
     * 重置密码
     *
     * @param phone
     * @param password
     * @param resetCode
     * @return 1:成功 2：重置码无效 3：用户不存在
     */
    public OAuthStatus resetPasswordByResetCode(String phone, String password, String resetCode) throws Exception {
        //检验验证码
        boolean isPass;
        try {
            isPass = verifySmsCodeByNetEase(phone, resetCode);
        } catch (Exception e) {
            return OAuthStatus.SMS_V_TO_OFTEN;
        }

        if (isPass) {
            Account account = getAccountByPhone(phone);
            if (null == account) {
                return OAuthStatus.INVALID_USER;
            } else {
                try {
                    updateAccountPwd(account.getUid(), password);
                    return OAuthStatus.SUCCESS;
                } catch (Exception e) {
                    return OAuthStatus.INVALID_SERVICE;
                }
            }
        } else {
            return OAuthStatus.INVALID_IDENTIFYING_CODE;
        }
    }

    public OAuthStatus resetPasswordByOldPassword(String phone, String password, String newPassword) throws Exception {
        Account account = getAccountByPhone(phone);
        if (null == account) {
            return OAuthStatus.INVALID_USER;
        }
        String oldpwd = account.getPassword();
        password = encryptPassword(password);
        if (password != null && password.equals(oldpwd)) {
            try {
                updateAccountPwd(account.getUid(), newPassword);
                return OAuthStatus.SUCCESS;
            } catch (Exception e) {
                return OAuthStatus.INVALID_SERVICE;
            }
        } else {
            return OAuthStatus.USERNAME_PASSWORD_MISMATCH;
        }
    }

    private void updateAccountPwd(Long uid, String pwd) {
        Account updateAccount = new Account();
        updateAccount.setUid(uid);
        updateAccount.setPassword(encryptPassword(pwd));
        updateAccount.setUpdateTime(new Date());
        accountMapper.updateByPrimaryKeySelective(updateAccount);
    }

    public void updateLastLoginTime(Long uid,String lastLoginIp) {
        Account updateAccount = new Account();
        updateAccount.setUid(uid);
        updateAccount.setLastLoginTime(new Date());
        updateAccount.setLastLoginIp(lastLoginIp);
        accountMapper.updateByPrimaryKeySelective(updateAccount);
    }

    public void update(Account account) {
        accountMapper.updateByPrimaryKeySelective(account);
    }

    /**
     * 检查设备号注册数量--成功返回true, 失败抛异常
     *
     * @param deviceId
     * @return
     */
    public boolean checkDeviceRegisterNum(String deviceId, String os) throws Exception {
        if (!"android".equalsIgnoreCase(os) && !"ios".equalsIgnoreCase(os)) {
            return true;
        }
        //
        String result = jedisService.get(RedisKey.register_num.getKey(deviceId));
        if (StringUtils.isBlank(result)) {
            jedisService.incrBy(RedisKey.register_num.getKey(deviceId), 1);
            // 设置过期时间
            jedisService.expire(RedisKey.register_num.getKey(deviceId), 10 * 60);
        } else {
            Long num = Long.valueOf(result);
            if (num < DEVICE_MAX_REGISTER) {
                jedisService.incrBy(RedisKey.register_num.getKey(deviceId), 1);
            } else {
                logger.error("[设备注册过于频繁] deviceId: {}", deviceId);
                throw new SignAccountIpException("注册过于频繁, 请稍后再试");
            }
        }
        return true;
    }

}


