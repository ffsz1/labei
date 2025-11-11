package com.juxiao.xchat.api.controller.charge;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.charge.WithDrawService;
import com.juxiao.xchat.service.api.charge.vo.WithDrawCashVO;
import com.juxiao.xchat.service.api.user.UsersService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/withDraw")
@Api(tags = "钻石提现接口")
public class WithdrawController {
    @Resource
    private UsersService usersService;

    @Resource
    private WithDrawService withDrawService;

    /**
     * 获取提现用户信息(提现页面)
     *
     * @param uid 用户UID
     * @return
     */
    @SignVerification
    @RequestMapping(value = "/exchange", method = RequestMethod.GET)
    public WebServiceMessage exchange(@RequestParam("uid") Long uid) throws WebServiceException {
        return WebServiceMessage.success(withDrawService.getWithDraw(uid));
    }

    /**
     * 绑定手机号
     *
     * @param uid   用户UID
     * @param phone 绑定手机号
     * @param code  手机验证码
     * @return
     */
    @Authorization
    @SignVerification
    @RequestMapping(value = "/phone", method = RequestMethod.POST)
    public WebServiceMessage boundPhone(@RequestParam(value = "uid", required = false) Long uid,
                                        @RequestParam(value = "phone", required = false) String phone,
                                        @RequestParam(value = "code", required = false) String code) {
        WebServiceMessage message;
        try {
            usersService.bindPhone(uid, phone, code);
            message = WebServiceMessage.success(true);
        } catch (WebServiceException e) {
            message = WebServiceMessage.failure(e);
        }
        return message;
    }

    /**
     * 获取绑定手机号验证码(绑定手机号码页面)
     *
     * @param phone
     * @param request
     * @param deviceId
     * @param imei
     * @param os
     * @param osversion
     * @param channel
     * @param appVersion
     * @param model
     * @return
     */
    @SignVerification
    @RequestMapping(value = "/phoneCode", method = RequestMethod.GET)
    public WebServiceMessage getBoundPhoneCode(HttpServletRequest request,
                                               @RequestParam(value = "phone", required = false) String phone,
                                               @RequestParam(value = "deviceId", required = false) String deviceId,
                                               @RequestParam(value = "imei", required = false) String imei,
                                               @RequestParam(value = "os", required = false) String os,
                                               @RequestParam(value = "osversion", required = false) String osversion,
                                               @RequestParam(value = "channel", required = false) String channel,
                                               @RequestParam(value = "appVersion", required = false) String appVersion,
                                               @RequestParam(value = "model", required = false) String model) throws Exception {
        String ip = HttpServletUtils.getRemoteIpV4(request);
        return usersService.getBoundPhoneCode(ip, phone, deviceId, imei, os, osversion, channel, appVersion, model);
    }

    /**
     * 点击获取验证码
     *
     * @param uid
     * @param request
     * @param deviceId
     * @param imei
     * @param os
     * @param osversion
     * @param channel
     * @param appVersion
     * @param model
     * @return
     */
    @SignVerification
    @RequestMapping(value = "/getSms", method = RequestMethod.GET)
    public WebServiceMessage getSms(HttpServletRequest request,
                                    @RequestParam(value = "uid", required = false) Long uid,
                                    @RequestParam(value = "deviceId", required = false) String deviceId,
                                    @RequestParam(value = "imei", required = false) String imei,
                                    @RequestParam(value = "os", required = false) String os,
                                    @RequestParam(value = "osversion", required = false) String osversion,
                                    @RequestParam(value = "channel", required = false) String channel,
                                    @RequestParam(value = "appVersion", required = false) String appVersion,
                                    @RequestParam(value = "model", required = false) String model) throws Exception {
        String ip = HttpServletUtils.getRemoteIpV4(request);
        return usersService.getCode(ip, uid, deviceId, imei, os, osversion, channel, appVersion, model);
    }

    /**
     * 绑定支付宝
     *
     * @param uid               用户UID
     * @param aliPayAccount     支付宝账号
     * @param aliPayAccountName 支付宝账号名称
     * @param code              验证码
     * @return
     */
    @Authorization
    @SignVerification
    @RequestMapping(value = "/bound", method = RequestMethod.POST)
    public WebServiceMessage boundAliPay(@RequestParam(value = "uid", required = false) Long uid,
                                         @RequestParam(value = "aliPayAccount", required = false) String aliPayAccount,
                                         @RequestParam(value = "aliPayAccountName", required = false) String aliPayAccountName,
                                         @RequestParam(value = "code", required = false) String code) throws WebServiceException {
        return WebServiceMessage.success(withDrawService.boundAliPay(uid, aliPayAccount, aliPayAccountName, code));
    }

    /**
     * 绑定提现账户
     *
     * @param uid         用户UID
     * @param phone       绑定手机号
     * @param smsCode     手机验证码
     * @param diamondId   提现选项ID
     * @param diamondNum  提现钻石数量
     * @param account     账户号码
     * @param accountName 账户名称
     * @param accountType 账户类型 [ 1.支付宝; 2.银行卡 ]
     * @param appVersion  App版本
     * @return
     * @throws Exception
     */
    @Authorization
    @SignVerification
    @RequestMapping(value = "/bindWithdrawAccount", method = RequestMethod.POST)
    public WebServiceMessage bindWithdrawAccount(@RequestParam(value = "uid") Long uid,
                                                 @RequestParam(value = "phone", required = false) String phone,
                                                 @RequestParam(value = "passwordSecond", required = false) String passwordSecond,
                                                 @RequestParam(value = "diamondId", required = false) String diamondId,
                                                 @RequestParam(value = "diamondNum", required = false) Integer diamondNum,
                                                 @RequestParam(value = "account") String account,
                                                 @RequestParam(value = "accountName") String accountName,
                                                 @RequestParam(value = "accountType") Integer accountType,
                                                 @RequestParam("appVersion") String appVersion) throws Exception {
        return WebServiceMessage.success(withDrawService.bindAccount(uid, diamondId, diamondNum, account, accountName,
                accountType, appVersion, phone, passwordSecond));
    }

    /**
     * 获取所有提现账户
     *
     * @param uid 用户ID
     * @return
     * @throws WebServiceException
     */
    @Authorization
    @SignVerification
    @RequestMapping(value = "/getFinancialAccount", method = RequestMethod.GET)
    public WebServiceMessage getFinancialAccount(@RequestParam(value = "uid", required = false) Long uid) throws WebServiceException {
        return WebServiceMessage.success(withDrawService.getAccount(uid));
    }

    /**
     * 验证绑定第三方短信
     *
     * @param uid  用户UID
     * @param code 验证码
     * @return
     * @throws WebServiceException
     */
    @ApiOperation(value = "验证绑定第三方短信", notes = "验证绑定第三方短信")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户UID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "code", value = "验证码", dataType = "string", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Boolean.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/checkCode", method = RequestMethod.POST)
    public WebServiceMessage checkCode(@RequestParam(value = "uid") Long uid,
                                       @RequestParam(value = "code") String code) throws WebServiceException {
        withDrawService.checkCode(uid, code);
        return WebServiceMessage.success(null);
    }

    /**
     * 绑定第三方账户
     *
     * @param uid         用户UID
     * @param openId      第三方OpenId
     * @param unionId     第三方UnionId
     * @param accessToken 访问Token令牌
     * @param type        绑定类型 [1.微信; 2.QQ]
     * @param appid       AppId
     * @param os          操作系统
     * @return
     * @throws WebServiceException
     */
    @ApiOperation(value = "绑定第三方", notes = "绑定第三方")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户UID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "openId", value = "对应OpenId", dataType = "string", required = true),
            @ApiImplicitParam(name = "unionId", value = "对应UnionId", dataType = "string", required = true),
            @ApiImplicitParam(name = "accessToken", value = "对应AccessToken", dataType = "string", required = true),
            @ApiImplicitParam(name = "type", value = "1.微信; 2.QQ", dataType = "int", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Integer.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/boundThird", method = RequestMethod.POST)
    public WebServiceMessage boundAliPay(@RequestParam(value = "uid") Long uid,
                                         @RequestParam(value = "openId") String openId,
                                         @RequestParam(value = "unionId") String unionId,
                                         @RequestParam(value = "accessToken") String accessToken,
                                         @RequestParam(value = "type") int type,
                                         @RequestParam(value = "appid") String appid,
                                         @RequestParam(value = "os") String os) throws WebServiceException {
        return WebServiceMessage.success(withDrawService.boundThird(uid, openId, unionId, accessToken, type, appid,
                os));
    }

    /**
     * 解绑第三方账户
     *
     * @param uid  用户UID
     * @param type 解绑类型 [1.微信; 2.QQ]
     * @return
     * @throws WebServiceException
     */
    @ApiOperation(value = "解绑第三方", notes = "解绑第三方")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "type", value = "1.微信; 2.QQ", dataType = "int", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Integer.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/unBoundThird", method = RequestMethod.POST)
    public WebServiceMessage boundAliPay(@RequestParam(value = "uid") Long uid,
                                         @RequestParam(value = "type") int type) throws WebServiceException {
        return WebServiceMessage.success(withDrawService.unBoundThird(uid, type));
    }

    /**
     * 客户端钻石提现 (安卓)
     *
     * @param uid        用户UID
     * @param pid        提现选项ID
     * @param diamondNum 提现钻石数量
     * @param type       提现类型 [1.微信; 2.支付宝; 3.银行卡]
     * @param os         操作系统
     * @param appVersion App版本
     * @return
     */
    @ApiOperation(value = "钻石提现", notes = "根据项目ID和提现类型生成用户提现记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户UID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "pid", value = "提现选项ID", dataType = "string", required = true),
            @ApiImplicitParam(name = "type", value = "1.微信; 2.支付宝; 3.银行卡", dataType = "int", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = WithDrawCashVO.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/withDrawCash", method = RequestMethod.POST)
    public WebServiceMessage withDrawCash(@RequestParam(value = "uid") Long uid,
                                          @RequestParam(value = "pid") String pid,
                                          @RequestParam(value = "diamondNum") Integer diamondNum,
                                          @RequestParam(value = "type") int type,
                                          @RequestParam(value = "os", required = false) String os,
                                          @RequestParam(value = "appVersion", required = false) String appVersion) throws Exception {
        return WebServiceMessage.success(withDrawService.withDrawCash(uid, pid, diamondNum, type));
    }

    /**
     * 钻石提现
     *
     * @param uid
     * @param pid
     * @return
     */
    @ApiOperation(value = "钻石提现", notes = "根据项目id和提现类型生成用户提现记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "pid", value = "提现项目id", dataType = "string", required = true),
            @ApiImplicitParam(name = "type", value = "1微信2支付宝", dataType = "int", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = WithDrawCashVO.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/withDrawCashNowxali", method = RequestMethod.POST)
    public WebServiceMessage withDrawCashNowxali(@RequestParam(value = "uid") Long uid,
                                                 @RequestParam(value = "pid") String pid,
                                                 @RequestParam(value = "type") int type,
                                                 @RequestParam(value = "os", required = false) String os,
                                                 @RequestParam(value = "appVersion", required = false) String appVersion) throws Exception {
        return WebServiceMessage.success(withDrawService.withDrawCashNowxali(uid, pid, type));
    }

    /**
     * 返回提现列表
     *
     * @return
     */
    @SignVerification
    @RequestMapping(value = "/findList", method = RequestMethod.GET)
    public WebServiceMessage listAllCashProds() {
        return WebServiceMessage.success(withDrawService.listAllCashProds());
    }

    /**
     * 绑定账户
     *
     * @param uid         用户UID
     * @param account     账户
     * @param accountName 账户名称
     * @param accountType 账户类型 [ 1.支付宝; 2.银行卡 ]
     * @param appVersion  App版本
     * @return
     * @throws WebServiceException
     */
    @Authorization
    @SignVerification
    @RequestMapping(value = "/resetUserAccount", method = RequestMethod.POST)
    public WebServiceMessage resetUserAccount(@RequestParam(value = "uid") Long uid,
                                              @RequestParam(value = "account") String account,
                                              @RequestParam(value = "accountName") String accountName,
                                              @RequestParam(value = "accountType") Integer accountType,
                                              @RequestParam(value = "appVersion") String appVersion) throws Exception {
        return withDrawService.resetUserAccount(uid, account, accountName, accountType, appVersion);
    }
}
