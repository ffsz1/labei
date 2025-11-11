package com.juxiao.xchat.service.api.user.impl;

import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DataValidationUtils;
import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.base.utils.RandomUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.bill.BillTransferDao;
import com.juxiao.xchat.dao.bill.dto.TransFerUsersDTO;
import com.juxiao.xchat.dao.sysconf.SmsRecordDao;
import com.juxiao.xchat.dao.sysconf.domain.SmsRecordDO;
import com.juxiao.xchat.dao.user.AccountDao;
import com.juxiao.xchat.dao.user.UsersDao;
import com.juxiao.xchat.dao.user.domain.AccountDO;
import com.juxiao.xchat.dao.user.domain.UsersDO;
import com.juxiao.xchat.dao.user.dto.AccountDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.user.AccountManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.common.user.vo.WXUserInfoVO;
import com.juxiao.xchat.manager.external.netease.NetEaseSmsManager;
import com.juxiao.xchat.manager.external.netease.ret.NetEaseSmsRet;
import com.juxiao.xchat.service.api.user.AccountsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class AccountsServiceImpl implements AccountsService {

    @Resource(name = "CaihSmsManager")
    private NetEaseSmsManager netEaseSmsManager;

    @Resource
    private RedisManager redisManager;

    @Resource
    private SmsRecordDao recordDao;

    @Resource
    private UsersManager usersManager;

    @Resource
    private AccountDao accountDao;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private AccountManager accountManager;

    @Resource
    private BillTransferDao billTransferDao;

    @Resource
    private UsersDao usersDao;

    /**
     * 获取短信验证码
     *
     * @param ip
     * @param uid
     * @param deviceId
     * @param imei
     * @param os
     * @param osversion
     * @param channel
     * @param appVersion
     * @param model
     * @return
     */
    @Override
    public WebServiceMessage getSmsCode(String ip, Long uid, String deviceId, String imei, String os, String osversion, String channel, String appVersion, String model)throws Exception {
        if (uid == null) {
            return WebServiceMessage.failure(WebServiceCode.PARAMETER_ILLEGAL);
        }

        UsersDTO usersDto = usersManager.getUser(uid);
        if (usersDto == null) {
            return WebServiceMessage.failure(WebServiceCode.PARAMETER_ILLEGAL);
        }

        String phone = usersDto.getPhone();
        if (StringUtils.isBlank(phone)) {
            return new WebServiceMessage(WebServiceCode.PHONE_INVALID.getValue(), "您还没有绑定手机号码，请先绑定手机号！");
        }

        String code = RandomUtils.getRandomNumStr(5);
        NetEaseSmsRet ret = netEaseSmsManager.sendSms(phone, deviceId, null, code);
        if (ret == null) {
            return WebServiceMessage.failure(WebServiceCode.SMS_SEND_ERROR);
        }

        if (ret.getCode() != 200) {
            return new WebServiceMessage(WebServiceCode.SMS_SEND_ERROR.getValue(), "短信发送失败：" + ret.getMsg());
        }

        redisManager.hdel(RedisKey.phone_uid.getKey(), phone);

        SmsRecordDO recordDo = new SmsRecordDO();
        recordDo.setPhone(phone);
        recordDo.setIp(ip);
        recordDo.setDeviceId(deviceId);
        recordDo.setImei(imei);
        recordDo.setOs(os);
        recordDo.setOsversion(osversion);
        recordDo.setChannel(channel);
        recordDo.setAppVersion(appVersion);
        recordDo.setModel(model);
        recordDo.setSmsCode(code + "-" + ret.getMsg());
        recordDo.setSmsType((byte) 5);
        recordDao.save(recordDo);
        WebServiceMessage message = WebServiceMessage.success(null);
        message.setMessage("短信发送成功");
        return message;
    }

    /**
     * 校验手机验证码
     *
     * @param code
     * @param uid
     * @return
     * @throws WebServiceException
     */
    @Override
    public void validateCode(Long uid,String code) throws WebServiceException {
        UsersDTO usersDTO = usersManager.getUser(uid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        boolean isSmsCode = netEaseSmsManager.verifySmsCode(usersDTO.getPhone(), code);
        if (isSmsCode) {
            redisManager.hset(RedisKey.user_check_code.getKey(), uid.toString(), code);
        } else {
            throw new WebServiceException(WebServiceCode.SMS_CODE_ERROR);
        }
    }

    /**
     * 解绑第三方
     *
     * @param uid
     * @param type
     * @return
     * @throws WebServiceException
     */
    @Override
    public WebServiceMessage untiedThird(Long uid, int type) throws WebServiceException {
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
        TransFerUsersDTO transFerUsersDTO = billTransferDao.selectBillTransfer(uid);
        if(transFerUsersDTO != null){
            int days = DateUtils.getDutyDays(DateUtils.dateToStr(transFerUsersDTO.getCreateTime()),DateUtils.dateToStr(new Date()));
            if(days <= 7){
                throw new WebServiceException("近期申请过提现或者已提现,请7个工作日后再申请解绑，现状态不支持解绑。(申请当天的第二天算第一个工作日)");
            }
        }
        int result = 0;
        if (type == 1) {
            result = jdbcTemplate.update("UPDATE account set weixin_openid = null, weixin_unionid = null where uid = ?", uid);
        } else if (type == 2) {
            result = jdbcTemplate.update("UPDATE account set qq_openid = null, qq_unionid = null where uid = ?", uid);
        }
        return WebServiceMessage.success(result);
    }

    /**
     * 绑定第三方
     *
     * @param uid
     * @param openId
     * @param unionId
     * @param accessToken
     * @param type
     * @param app
     * @return
     * @throws WebServiceException
     */
    @Override
    public WebServiceMessage bindThird(Long uid, String openId, String unionId, String accessToken, int type, String app,String os) throws WebServiceException {
        if (type != 1 && type != 2) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }
        UsersDTO usersDTO = usersManager.getUser(uid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        AccountDTO accountDTO = accountDao.getAccount(uid);
        if (type == 1 && StringUtils.isNotBlank(accountDTO.getWeixinOpenid())) {
            throw new WebServiceException("你已经绑定微信");
        }
        if (type == 2 && StringUtils.isNotBlank(accountDTO.getQqOpenid())) {
            throw new WebServiceException("你已经绑定QQ");
        }
        String str = redisManager.hget(RedisKey.user_check_code.getKey(), uid.toString());
        if (StringUtils.isBlank(str)) {
            throw new WebServiceException(WebServiceCode.SMS_CODE_ERROR);
        }
        try {
            accountManager.validateThirdInfo(openId, accessToken, type, app,os);
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
        return WebServiceMessage.success(accountDao.update(accountDo));
    }

    @Override
    public WebServiceMessage getWxUserInfo(Long uid, String openId, String unionId, String accessToken, String appid, String os) throws WebServiceException{
        AccountDTO accountDTO = accountDao.getAccount(uid);
        if (accountDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        if(!openId.equals(accountDTO.getWeixinOpenid())){
            throw new WebServiceException(WebServiceCode.WX_OPENID_UNION_ID_NOT_MISMATCH);
        }
        WXUserInfoVO wxUserInfoVO = accountManager.getWXUserInfo(accessToken,openId);
        return WebServiceMessage.success(wxUserInfoVO);
    }

    @Override
    public WebServiceMessage checkWxInfo(Long uid, String openId, String unionId, String accessToken, String appid, String os) throws WebServiceException{
        AccountDTO accountDTO = accountDao.getAccount(uid);
        if (accountDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS.getMessage());
        }
        if(accountDTO.getWeixinOpenid() != null && !"".equalsIgnoreCase(accountDTO.getWeixinOpenid())){
            if(!openId.equals(accountDTO.getWeixinOpenid())){
                throw new WebServiceException(WebServiceCode.WX_OPENID_UNION_ID_NOT_DIFFER.getMessage());
            }
        }
        return WebServiceMessage.success(null);
    }

    @Override
    public WebServiceMessage getAccountSmsCode(Long uid, String phone) throws Exception {
        if (uid==null) {
            return WebServiceMessage.failure(WebServiceCode.PARAMETER_ILLEGAL);
        }
        if (!DataValidationUtils.validatePhone(phone)) {
            return WebServiceMessage.failure(WebServiceCode.PHONE_INVALID);
        }
        UsersDTO user = usersManager.getUser(uid);
        if (user == null) {
            return WebServiceMessage.failure(WebServiceCode.USER_NOT_EXISTS);
        }

        if(!Objects.equals(user.getPhone(), phone)){
            return WebServiceMessage.failure(WebServiceCode.PHONE_INVALID);
        }

        String code = RandomUtils.getRandomNumStr(5);
        NetEaseSmsRet ret = netEaseSmsManager.sendSms(phone, "", null, code);
        if (ret == null) {
            return WebServiceMessage.failure(WebServiceCode.SMS_SEND_ERROR);
        }

        if (ret.getCode() != 200) {
            return new WebServiceMessage(WebServiceCode.SMS_SEND_ERROR.getValue(), "短信发送失败：" + ret.getMsg());
        }

        WebServiceMessage message = WebServiceMessage.success(null);
        message.setMessage("短信发送成功");
        return message;
    }

    @Override
    public WebServiceMessage getSmsByCode(String phone) throws Exception{
        if (!DataValidationUtils.validatePhone(phone)) {
            return WebServiceMessage.failure(WebServiceCode.PHONE_INVALID);
        }

        // Long uid = usersDao.getUidByPhone(phone);
        // if (uid == null) {
        //     return WebServiceMessage.failure(WebServiceCode.PHONE_NO_BINDED);
        // }
        Long uid;
        List<UsersDO> usersDO = usersDao.getUserByPhone(phone);
        if (usersDO == null || usersDO.size() == 0) {
            return WebServiceMessage.failure(WebServiceCode.PHONE_NO_BINDED);
        } else {
            uid = usersDO.get(0).getUid();
        }

        String code = RandomUtils.getRandomNumStr(5);
        NetEaseSmsRet ret = netEaseSmsManager.sendSms(phone, "", null, code);
        if (ret == null) {
            return WebServiceMessage.failure(WebServiceCode.SMS_SEND_ERROR);
        }

        if (ret.getCode() != 200) {
            return new WebServiceMessage(WebServiceCode.SMS_SEND_ERROR.getValue(), "短信发送失败：" + ret.getMsg());
        }

        WebServiceMessage message = WebServiceMessage.success(null);
        message.setMessage("短信发送成功");
        return message;
    }


   public  Map<String, String> getBindNick(Long uid){

       Map<String, String> map=new HashMap<>();
       if(uid!=null) {
          map = accountDao.getBindNickMap(uid);
       }
       return map;
   }
}
