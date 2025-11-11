package com.juxiao.xchat.manager.external.netease;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.manager.external.netease.ret.NetEaseSmsRet;

import java.util.List;

/**
 * 网易短信服务
 *
 * @class: NetEaseSmsManager.java
 * @author: chenjunsheng
 * @date 2018/6/11
 */
public interface NetEaseSmsManager {
    /**
     * 发送短信
     *
     * @param mobile
     * @param deviceId
     * @param smsTemplateid
     * @param code
     * @return
     * @author: chenjunsheng
     * @date 2018/6/11
     */
    NetEaseSmsRet sendSms(String mobile, String deviceId, String smsTemplateid, String code) throws Exception;

    NetEaseSmsRet sendTemplateSms(List<String> mobiles, String message, String smsTemplateid) throws Exception;

    /**
     * @param message
     * @return
     * @throws Exception
     */
    NetEaseSmsRet sendAlarmSms(String message) throws Exception;

    /**
     * 验证短信验证码
     *
     * @param mobile
     * @param code
     * @return
     * @author: chenjunsheng
     * @date 2018/6/8
     */
    boolean verifySmsCode(String mobile, String code) throws WebServiceException;
}
