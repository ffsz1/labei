package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.user.AccountsService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("accounts")
public class AccountsController {

    @Autowired
    private AccountsService accountsService;

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
    @Authorization
    @RequestMapping(value = "/getSmsCode", method = RequestMethod.GET)
    public WebServiceMessage getSmsCode(HttpServletRequest request,
                                    @RequestParam(value = "uid", required = false) Long uid,
                                    @RequestParam(value = "deviceId", required = false) String deviceId,
                                    @RequestParam(value = "imei", required = false) String imei,
                                    @RequestParam(value = "os", required = false) String os,
                                    @RequestParam(value = "osversion", required = false) String osversion,
                                    @RequestParam(value = "channel", required = false) String channel,
                                    @RequestParam(value = "appVersion", required = false) String appVersion,
                                    @RequestParam(value = "model", required = false) String model) throws Exception {
        String ip = HttpServletUtils.getRemoteIpV4(request);
        return accountsService.getSmsCode(ip, uid,deviceId, imei, os, osversion, channel, appVersion, model);
    }

    @ApiOperation(value = "验证绑定第三方短信", notes = "验证绑定第三方短信")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "code", value = "验证码", dataType = "string", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Boolean.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/validateCode", method = RequestMethod.POST)
    public WebServiceMessage validateCode(@RequestParam(value = "uid") Long uid, @RequestParam(value = "code") String code) throws WebServiceException {
        accountsService.validateCode(uid, code);
        return WebServiceMessage.success(null);
    }

    @ApiOperation(value = "解绑第三方", notes = "解绑第三方")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "type", value = "1微信2QQ", dataType = "int", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Integer.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/untiedThird", method = RequestMethod.POST)
    public WebServiceMessage untiedThird(@RequestParam(value = "uid") Long uid,
                                         @RequestParam(value = "type") int type) throws WebServiceException {
        return WebServiceMessage.success(accountsService.untiedThird(uid, type));
    }

    @ApiOperation(value = "绑定第三方", notes = "绑定第三方")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "openId", value = "对应openId", dataType = "string", required = true),
            @ApiImplicitParam(name = "unionId", value = "对应unionId", dataType = "string", required = true),
            @ApiImplicitParam(name = "accessToken", value = "对应accessToken", dataType = "string", required = true),
            @ApiImplicitParam(name = "type", value = "1微信2QQ", dataType = "int", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Integer.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/bindThird", method = RequestMethod.POST)
    public WebServiceMessage bindThird(@RequestParam(value = "uid") Long uid,
                                         @RequestParam(value = "openId") String openId,
                                         @RequestParam(value = "unionId") String unionId,
                                         @RequestParam(value = "accessToken") String accessToken,
                                         @RequestParam(value = "type") int type,
                                         @RequestParam(value = "appid") String appid,
                                         @RequestParam(value = "os") String os) throws WebServiceException {
        return WebServiceMessage.success(accountsService.bindThird(uid, openId, unionId, accessToken, type, appid,os));
    }


    @ApiOperation(value = "获取第三方信息", notes = "获取第三方信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "openId", value = "对应openId", dataType = "string", required = true),
            @ApiImplicitParam(name = "unionId", value = "对应unionId", dataType = "string", required = true),
            @ApiImplicitParam(name = "accessToken", value = "对应accessToken", dataType = "string", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Integer.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/getWxUserInfo", method = RequestMethod.GET)
    public WebServiceMessage getWxUserInfo(@RequestParam(value = "uid") Long uid,
                                       @RequestParam(value = "openId") String openId,
                                       @RequestParam(value = "unionId") String unionId,
                                       @RequestParam(value = "accessToken") String accessToken,
                                       @RequestParam(value = "appid") String appid,
                                       @RequestParam(value = "os") String os) throws WebServiceException {
        return WebServiceMessage.success(accountsService.getWxUserInfo(uid, openId, unionId, accessToken, appid,os));
    }


    @ApiOperation(value = "检测校验第三方信息", notes = "检测校验第三方信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "openId", value = "对应openId", dataType = "string", required = true),
            @ApiImplicitParam(name = "unionId", value = "对应unionId", dataType = "string", required = true),
            @ApiImplicitParam(name = "accessToken", value = "对应accessToken", dataType = "string", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = Integer.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/checkWxInfo", method = RequestMethod.GET)
    public WebServiceMessage checkWxInfo(@RequestParam(value = "uid") Long uid,
                                           @RequestParam(value = "openId") String openId,
                                           @RequestParam(value = "unionId") String unionId,
                                           @RequestParam(value = "accessToken") String accessToken,
                                           @RequestParam(value = "appid") String appid,
                                           @RequestParam(value = "os") String os) throws WebServiceException {
        return WebServiceMessage.success(accountsService.checkWxInfo(uid, openId, unionId, accessToken, appid,os));
    }

    //@Authorization
    @RequestMapping("/getBindNick")
    public WebServiceMessage getBindNick(@RequestParam(value = "uid")Long uid){
        if(uid==null) return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        Map<String,String> resultMap= accountsService.getBindNick(uid);
        Map<String,String> result=new HashMap<>();
        result.put("qqNick",resultMap.get("qqNick"));
        result.put("weixinNick",resultMap.get("weixinNick"));
        result.put("appleUserName",resultMap.get("appleUserName"));
        return WebServiceMessage.success(result);
    }
}
