package com.erban.web.controller;

import com.alibaba.fastjson.JSON;
import com.erban.main.service.WithDrawService;
import com.erban.main.vo.WithDrawVo;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.constant.Constant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.http.HttpUitls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 钻石提现controller
 *
 * @author yanghaoyu
 */
@Controller
@RequestMapping("/withDraw")
public class WithdrawController {
    private static final Logger logger = LoggerFactory.getLogger(WithdrawController.class);
    @Autowired
    private WithDrawService withDrawService;

    /**
     * 获取提现用户信息(提现页面)
     *
     * @param uid
     * @return
     */
    @ResponseBody
    @SignVerification
    @RequestMapping(value = "/exchange", method = RequestMethod.GET)
    public BusiResult exchange(Long uid) {
        logger.info("调用接口：/withDraw/exchange，获取提现用户信息,接口入参：{}", "无");
        BusiResult<WithDrawVo> busiResult = new BusiResult<>(BusiStatus.SUCCESS);
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        busiResult = withDrawService.getWithDraw(uid, busiResult);
        logger.info("获取提现用户信息接口（/withDraw/exchange），接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    /* 绑定手机号 */
    @ResponseBody
    @Authorization
    @SignVerification
    @RequestMapping(value = "/phone", method = RequestMethod.POST)
    public BusiResult boundPhone(Long uid, String phone, String code) {
        logger.info("调用接口：/withDraw/phone，绑定手机号,接口入参：uid{},phone:{},code:{}", uid, phone, code);
        BusiResult busiResult = null;
        if (uid == null || phone == null || code == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            busiResult = withDrawService.boundPhone(uid, phone, code);
        } catch (Exception e) {
            logger.error("boundPhone error..uid=" + uid, e);
            return new BusiResult(BusiStatus.SMSCODEERROR);
        }
        logger.info("绑定手机接口（/withDraw/phone），接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    /* 获取绑定手机号验证码(绑定手机号码页面) */
    @ResponseBody
    @SignVerification
    @RequestMapping(value = "/phoneCode", method = RequestMethod.GET)
    public BusiResult getboundPhoneCode(String phone, HttpServletRequest request, String deviceId, String imei, String os, String osversion, String channel, String appVersion, String model) {
        logger.info("调用接口：/withDraw/phoneCode，获取绑定手机号验证码,接口入参：phone:{},request:{},deviceId:{},imei:{},os:{},osversion:{},channel:{},appVersion:{},model:{}", phone, request, deviceId, imei, os, osversion, channel, appVersion, model);
        String ip = HttpUitls.getRealIpAddress(request);
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (phone == null || phone.matches(Constant.Regex.PHONE)) {
            return new BusiResult(BusiStatus.PHONEINVALID);
        }
        try {
            busiResult = withDrawService.getBoundPhoneCode(phone, ip, deviceId, imei, os, osversion, channel, appVersion, model);
        } catch (Exception e) {
            logger.error("phoneCode error phone=" + phone, e);
        }
        logger.info("获取绑定手机号验证码（/withDraw/phoneCode），接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    /* 点击获取验证码 */
    @ResponseBody
    @SignVerification
    @RequestMapping(value = "/getSms", method = RequestMethod.GET)
    public BusiResult getSms(Long uid, HttpServletRequest request, String deviceId, String imei, String os, String osversion, String channel, String appVersion, String model) {
        logger.info("调用接口：/withDraw/getSms，点击获取验证码,接口入参：request:{},deviceId:{},imei:{},os:{},osversion:{},channel:{},appVersion:{},model:{}", request, deviceId, imei, os, osversion, channel, appVersion, model);
        String ip = HttpUitls.getRealIpAddress(request);
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            busiResult = withDrawService.getCode(uid, ip, deviceId, imei, os, osversion, channel, appVersion, model);
        } catch (Exception e) {
            logger.error("getSms error uid=" + uid, e);
        }
        logger.info("点击获取验证码（/withDraw/getSms），接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    /* 绑定支付宝 */
    @ResponseBody
    @Authorization
    @SignVerification
    @RequestMapping(value = "/bound", method = RequestMethod.POST)
    public BusiResult bound(Long uid, String aliPayAccount, String aliPayAccountName, String code) {
        logger.info("调用接口：/withDraw/bound，绑定支付宝,接口入参：uid:{},aliPayAccount:{},aliPayAccountName:{},code:{}", uid, aliPayAccount, aliPayAccountName, code);
        BusiResult busiResult = null;
        if (uid == null || aliPayAccount == null || aliPayAccountName == null || code == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            busiResult = withDrawService.bound(uid, aliPayAccount, aliPayAccountName, code);
        } catch (Exception e) {
            logger.error("bound error..uids=" + uid, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        logger.info("绑定支付宝（/withDraw/bound），接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    /**
     * 钻石提现
     *
     * @param uid
     * @param pid
     * @return
     */
    @ResponseBody
    @Authorization
    @SignVerification
    @RequestMapping(value = "/withDrawCash", method = RequestMethod.POST)
    public BusiResult withDraw(Long uid, String pid) {
        logger.info("调用接口：/withDraw/withDrawCash，钻石提现,接口入参：uid:{},pid:{}", uid, pid);
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (uid == null || pid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        busiResult = withDrawService.withDraw(uid, pid);
        logger.info("钻石提现（/withDraw/withDrawCash），接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

    /* 返回提现列表 */
    @ResponseBody
    @SignVerification
    @RequestMapping(value = "/findList", method = RequestMethod.GET)
    public BusiResult findWithDrawList() {
        logger.info("调用接口：/withDraw/findList，提现列表,接口入参：无");
        BusiResult busiResult = withDrawService.findWithDrawList();
        logger.info("提现列表（/withDraw/findList），接口出参：{}", JSON.toJSONString(busiResult));
        return busiResult;
    }

}
