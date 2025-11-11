package com.erban.main.service;

import java.util.List;

import com.xchat.oauth2.service.service.account.SmsRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.xchat.common.netease.neteaseacc.result.BaseNetEaseRet;
import com.xchat.common.netease.neteaseacc.result.SmsRet;
import com.xchat.common.netease.util.NetEaseConstant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.CommonUtil;
import com.xchat.oauth2.service.core.domain.ServiceResult;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.AccountMapper;
import com.xchat.oauth2.service.model.Account;
import com.xchat.oauth2.service.model.AccountExample;
import com.xchat.oauth2.service.service.JedisService;
import com.xchat.oauth2.service.service.account.NetEaseService;

/**
 * @author yanhaoyu
 */
@Service
public class SmsService {

    @Autowired
    @Qualifier("jedisService")
    private JedisService jedisService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private SmsRecordService smsRecordService;

    @Autowired
    private NetEaseService netEaseService;
    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    /**
     * 生成并通过短信发送验证码
     *
     * @param phone
     * @return
     */

    private BusiResult sendAccSmsCode(String phone) throws Exception {
        BusiResult result = new BusiResult(BusiStatus.SUCCESS);
        if (checkPhoneExists(phone)) {
            result.setCode(ServiceResult.SC_DATA_ERROR);
            result.setMessage("手机号码已经注册，请直接登录");
            return result;
        }
        // 发送短信
        String deviceId = "";
        SmsRet smsRet = netEaseService.sendSms(phone, deviceId, NetEaseConstant.smsTemplateid);
        if (smsRet.getCode() == 200) {
            result.setMessage("发送短信成功");
            result.setData(smsRet.getMsg());
        } else {
            logger.info("发送短信失败phone="+phone+"&code="+smsRet.getCode());
            result.setCode(BusiStatus.SMSSENDERROR.value());
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
        // 发送短信
        String deviceId = "";
        SmsRet smsRet = netEaseService.sendSms(phone, deviceId, NetEaseConstant.smsTemplateid);
        if (smsRet.getCode() == 200) {
            result.setMessage("发送短信成功");
            result.setData(smsRet.getMsg());
        } else {
            logger.info("发送短信失败phone="+phone+"&code="+smsRet.getCode());
            result.setCode(BusiStatus.SMSSENDERROR.value());
            result.setMessage("发送短信失败code=" + smsRet.getCode());
        }
        return result;
    }

    public boolean checkPhoneExists(String phone) {
        Account account = getAccountByPhone(phone);
        if (account == null) {
            return false;
        } else {
            return true;
        }
    }

    public Account getAccountByPhone(String phone) {
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andPhoneEqualTo(phone);
        List<Account> accountList = accountMapper.selectByExample(accountExample);
        if (CollectionUtils.isEmpty(accountList)) {
            return null;
        } else {
            return accountList.get(0);

        }
    }


    /**
     * 根据不同短信类型获取短信验证码
     *
     * @param phone
     * @param type  1注册短信；2登录短信；3找回密码短信；4提现验证码；
     * @return
     */
    public BusiResult sendSmsByType(String phone, int type,String ip,String deviceId,String imei,String os,String osversion,String channel,String appVersion,String model) throws Exception {

        if (!CommonUtil.checkValidPhone(phone)) {
            return new BusiResult(BusiStatus.PHONEINVALID);
        }
//        if(smsRecordService.checkIsTooOftenIp(ip)){
//            new BusiResult(BusiStatus.SMSIPTOOFTEN);
//        }
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (1 == type) {
            busiResult = sendAccSmsCode(phone);
        } else if (2 == type) {
            // busiResult = sendAccSmsCode(phone);
        } else if (3 == type) {
            busiResult = sendAccSmsCodeByForgetPwd(phone);
        } else if (4 == type) {
            busiResult = sendAccSmsCodeByBindPhone(phone);
        } else if (5 == type) {
            busiResult = sendAccSmsCodeByBindAliPay(phone);
        }
        if(busiResult.getData()!=null){
            smsRecordService.saveSmsRecord( phone, ip, deviceId, imei, os, osversion, channel, appVersion, model, busiResult.getData().toString(), new Byte(type+""));
        }
        return busiResult;
    }

    private BusiResult sendAccSmsCodeByBindAliPay(String phone) throws Exception {
        BusiResult result = new BusiResult(BusiStatus.SUCCESS);
        if (!checkPhoneExists(phone)) {
            result.setCode(BusiStatus.PHONEINVALID.value());
            result.setMessage("您还没有绑定手机号码，请先绑定手机号！");
            return result;
        }
        // 发送短信
        String deviceId = "";
        SmsRet smsRet = netEaseService.sendSms(phone, deviceId, NetEaseConstant.smsTemplateid);
        if (smsRet.getCode() == 200) {
            result.setMessage("发送短信成功");
        } else {
            result.setCode(BusiStatus.SMSSENDERROR.value());
            result.setMessage("发送短信失败code=" + smsRet.getCode());
        }
        return result;
    }


    private BusiResult sendAccSmsCodeByBindPhone(String phone) throws Exception {
        BusiResult result = new BusiResult(BusiStatus.SUCCESS);
        if (checkPhoneExists(phone)) {
            result.setCode(ServiceResult.SC_DATA_ERROR);
            result.setMessage("此号码已被绑定，请重新输入!");
            return result;
        }
        // 发送短信
        String deviceId = "";
        SmsRet smsRet = netEaseService.sendSms(phone, deviceId, NetEaseConstant.smsTemplateid);
        if (smsRet.getCode() == 200) {
            result.setMessage("发送短信成功");
            result.setData(smsRet.getMsg());
        } else {
            logger.info("发送短信失败phone="+phone+"&code="+smsRet.getCode());
            result.setCode(BusiStatus.SMSSENDERROR.value());
            result.setMessage("发送短信失败code=" + smsRet.getCode());
        }
        return result;
    }


    public boolean verifySmsCodeByNetEase(String mobile, String code) throws Exception {
        BaseNetEaseRet baseNetEaseRet = netEaseService.smsVerify(mobile, code);
        if (baseNetEaseRet.getCode() == 200) {
            return true;
        } else {
            return false;
        }
    }
}
