package com.xchat.oauth2.web.controller.account;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.constant.Constant;
import com.xchat.common.device.DeviceInfo;
import com.xchat.common.netease.neteaseacc.result.BaseNetEaseRet;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.CommonUtil;
import com.xchat.common.utils.Utils;
import com.xchat.oauth2.service.common.KeyStore;
import com.xchat.oauth2.service.common.OAuth2AccessToken;
import com.xchat.oauth2.service.common.exceptions.InvalidThirdTokenException;
import com.xchat.oauth2.service.common.exceptions.OAuth2Exception;
import com.xchat.oauth2.service.common.status.OAuthStatus;
import com.xchat.oauth2.service.common.util.DESUtils;
import com.xchat.oauth2.service.core.util.HttpServletUtils;
import com.xchat.oauth2.service.http.HttpUitls;
import com.xchat.oauth2.service.model.Account;
import com.xchat.oauth2.service.provider.endpoint.AccountTokenUtil;
import com.xchat.oauth2.service.provider.token.XchatTokenServices;
import com.xchat.oauth2.service.service.account.AccountService;
import com.xchat.oauth2.service.service.account.NetEaseService;
import com.xchat.oauth2.service.service.account.OauthSysConfService;
import com.xchat.oauth2.service.service.sms.SmsService;
import com.xchat.oauth2.service.service.wx.WeixinAuthService;
import com.xchat.oauth2.service.service.wx.bean.WeixinAuthBO;
import com.xchat.oauth2.service.sso.WebssoService;
import com.xchat.oauth2.service.vo.AccountVo;
import com.xchat.oauth2.service.vo.SysConf;
import com.xchat.oauth2.web.controller.BaseController;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Controller
@RequestMapping("/acc")
public class AccountController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private OauthSysConfService sysconfService;

    @Autowired
    private XchatTokenServices xchatTokenServices;

    @Autowired
    private WebssoService webssoService;

    @Autowired
    private WeixinAuthService weixinAuthService;

    @Autowired
    private NetEaseService netEaseService;

    private Gson gson = new Gson();
    /**
     * 通过手机号码注册
     *
     * @param phone
     * @param password
     * @param smsCode
     * @return
     */
    @RequestMapping(value = "signup", method = RequestMethod.POST)
    @ResponseBody
    @SignVerification
    public BusiResult signUp(String phone, String password, String smsCode, DeviceInfo deviceInfo,
                             HttpServletRequest request) throws Exception {
        // logger.debug("register invoked,param phone:{},password:{},code:{}",phone,password,sms_code);
        OAuthStatus status;
        if (StringUtils.isBlank(phone) || CommonUtil.checkValidPhone(phone) == false) {
            status = OAuthStatus.INVALID_REQUEST;
            return new BusiResult<>(BusiStatus.BUSIERROR, "请输入正确手机号", null);
        }

        BaseNetEaseRet baseNetEaseRet = netEaseService.smsVerify(phone, smsCode);
        if (baseNetEaseRet.getCode() != 200 || StringUtils.isBlank(smsCode)) {
            status = OAuthStatus.INVALID_REQUEST;
            return new BusiResult<>(BusiStatus.BUSIERROR, "验证码错误", null);
        }

        // 加入密码DES解密
        try {
            password = DESUtils.DESAndBase64Decrypt(password, KeyStore.DES_ENCRYPT_KEY);
        } catch (Exception e) {
            throw new BadCredentialsException("password illegal.");
        }

        if (StringUtils.isBlank(password)) {
            status = OAuthStatus.INVALID_REQUEST;
            return new BusiResult<>(BusiStatus.BUSIERROR, "密码不能为空", null);
        }

        String version = deviceInfo.getAppVersion();
        if (StringUtils.isBlank(version)) {
            return new BusiResult<>(BusiStatus.BUSIERROR, "版本为空，请检查版本号", null);
        }

        SysConf sysConf = sysconfService.getSysConfById("sms_register_switch");
        if (sysConf == null || !"on".equalsIgnoreCase(sysConf.getConfigValue())) {
            return new BusiResult<>(BusiStatus.BUSIERROR, "注册网络错误", null);
        }

        String ipAddress = HttpUitls.getRealIpAddress(request);
        BusiResult<AccountVo> result = null;
        try {
            result = accountService.saveSignUpByPhone(phone, password, smsCode, deviceInfo, ipAddress);
        } catch (Exception e) {
            logger.error("注册失败，当前注册手机号码phone=" + phone + "失败原因：" + e.getMessage());
            result = new BusiResult<>();
            result.setCode(500);
            result.setMessage(e.getMessage());
        }
        // int code=result.getCode();
        // if(code==OAuthStatus.SUCCESS.value()){
        //     Account account=(Account)result.get("data");
        //     return new ServiceRes(code,account);
        // }
        return result;
    }

    @RequestMapping(value = "batchgenrob", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult batchGenRobAccount() {
        try {
            accountService.batchGenRobAccount();
        } catch (Exception e) {
            logger.error("批量生成账号失败");
        }
        return new BusiResult(BusiStatus.SUCCESS);

    }

    @RequestMapping(value = "getvisitor", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getVisitorAccount() {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            busiResult = accountService.genVisitorAccount();
        } catch (Exception e) {
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            logger.error("获取游客账号失败");
        }
        return busiResult;

    }

    /**
     * 获取账号操作相关短信
     *
     * @param phone 手机号码
     * @param type  操作类型 1注册短信；2更改手机短信；3找回密码短信（更改手机确认短信/钻石兑换金币确认）；4提现验证码；
     * @return
     */
    @RequestMapping(value = "sms", method = RequestMethod.GET)
    @SignVerification
    @ResponseBody
    public BusiResult getAccountSmsCode(HttpServletRequest request, int type, String phone, String deviceId,
                                        String imei, String os, String app, String osversion, String channel,
                                        String appVersion, String model) {
        String ip = HttpUitls.getRealIpAddress(request);
        BusiResult busiResult = null;
        logger.info("acc/sms,phone=" + phone + "&type=" + type + "&ip=" + ip);
        if (type == 1 && "0000".equalsIgnoreCase(model)) {
            return new BusiResult(BusiStatus.SERVERERROR);
        }

        if (type == 1) {
            SysConf sysConf = sysconfService.getSysConfById("sms_register_switch");
            if (sysConf == null || !"on".equalsIgnoreCase(sysConf.getConfigValue())) {
                return new BusiResult<>(BusiStatus.BUSIERROR, "注册网络错误", null);
            }
        }

        try {
            busiResult = accountService.sendSmsByType(phone, type, ip, deviceId, imei, os, osversion, channel,
                    appVersion, model);
        } catch (Exception e) {
            busiResult = new BusiResult(BusiStatus.SMSIPTOOFTEN, e.getMessage());
            logger.error("getAccountSmsCode....发送短信异常：phone=" + phone, e);
        }
        return busiResult;
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    public Object logout(String access_token) {
        if (StringUtils.isBlank(access_token)) {
            return new ServiceRes(OAuthStatus.INVALID_REQUEST);
        }
        xchatTokenServices.revokeToken(access_token);
        return new ServiceRes(OAuthStatus.SUCCESS);
    }

    /**
     * 重置密码接口,用于用户忘记密码，找回密码服务
     *
     * @param phone   手机号码
     * @param newPwd  新密码
     * @param smsCode 重置码
     * @return 1:成功 2：重置码无效 3：不存在该用户 4：其它错误
     */
    @RequestMapping(value = "pwd/reset", method = RequestMethod.POST)
    @SignVerification
    @ResponseBody
    public Object resetPassword(String phone, String newPwd, String smsCode, String os, String version) {
//        logger.debug("resetPassword,param username:{},new_password:{},reset_code:{}",username,new_password,
//        reset_code);
        OAuthStatus status;
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(smsCode)) {
            return new ServiceRes(OAuthStatus.INVALID_REQUEST);
        }

        //加入密码DES解密
        try {
            newPwd = DESUtils.DESAndBase64Decrypt(newPwd, KeyStore.DES_ENCRYPT_KEY);
        } catch (Exception e) {
            throw new BadCredentialsException("password illegal.");
        }


        if (StringUtils.isBlank(newPwd)) {
            return new ServiceRes(OAuthStatus.INVALID_REQUEST);
        }

        try {
            status = accountService.resetPasswordByResetCode(phone, newPwd, smsCode);
        } catch (Exception e) {
//            logger.error("reset password error, username:{},reset_code:{},msg:{}",username,reset_code,e);
            status = OAuthStatus.INVALID_SERVICE;
        }
        return new ServiceRes(status);
    }

    /**
     * @param phone
     * @param pwd
     * @param newPwd
     * @param os
     * @param version
     * @return
     */
    @RequestMapping(value = "pwd/modify", method = RequestMethod.POST)
    @SignVerification
    @ResponseBody
    public Object modifyPassword(String phone, String pwd, String newPwd, String os, String version) {
//        logger.debug("modify password, param username:{} password:{} new_password:{}",username,password,new_password);
        OAuthStatus status;
        if (StringUtils.isBlank(phone)) {
            return new ServiceRes(OAuthStatus.INVALID_REQUEST);
        }

        //加入密码DES解密
        try {
            pwd = DESUtils.DESAndBase64Decrypt(pwd, KeyStore.DES_ENCRYPT_KEY);
            newPwd = DESUtils.DESAndBase64Decrypt(newPwd, KeyStore.DES_ENCRYPT_KEY);
        } catch (Exception e) {
            throw new BadCredentialsException("password illegal.");
        }

        if (StringUtils.isBlank(pwd) || StringUtils.isBlank(newPwd)) {
            return new ServiceRes(OAuthStatus.INVALID_REQUEST);
        }

        try {
            status = accountService.resetPasswordByOldPassword(phone, pwd, newPwd);

        } catch (Exception e) {
            logger.error("reset password error, phone:{},msg:{}", phone, e);
            status = OAuthStatus.INVALID_SERVICE;
        }
        return new ServiceRes(status);
    }

    @SignVerification
    @RequestMapping("third/login")
    public Object login(HttpServletRequest request, String openid, String unionid, int type, DeviceInfo deviceInfo) throws Exception {
        logger.info("/acc/third/login?type={}&openid={}&unionid={}&access_token={}", type, openid, unionid,
                request.getParameter("access_token"));
        if (StringUtils.isEmpty(openid) || type == 0) {
            throw new OAuth2Exception("参数异常");
        }

        if (type == 1 && StringUtils.isEmpty(unionid)) {
            throw new OAuth2Exception("必要参数为空");
        }

        if ("0000".equalsIgnoreCase(deviceInfo.getModel())) {
            throw new OAuth2Exception("错误参数");
        }
        String nick=null;
        if (webssoService.isValidate(deviceInfo.getAppid(), deviceInfo.getAppVersion())) {
            String access_token = request.getParameter("access_token");
            if (org.springframework.util.StringUtils.isEmpty(access_token)) {
                logger.error("[第三方登录] access_token 为空: type:{}, openid:{},clientIp:{},model:>{},os:>{}", type == 1 ? "微信" : type == 2 ? "QQ" :
                        type, openid,HttpServletUtils.getRemoteIp(request),deviceInfo.getModel(),deviceInfo.getOs());
                throw new InvalidThirdTokenException("access_token 为空");
            }
            // 检查用户信息
            nick=webssoService.validateUserInfo(type, openid, deviceInfo, access_token);
        }

        OAuth2AccessToken token = webssoService.login2(openid, unionid, type,nick, deviceInfo, request);

        Boolean isAse = (Boolean) request.getAttribute("ase");
        if (isAse != null && isAse) {
            // BusiResult 格式
            return AccountTokenUtil.getResponseVO(token);
        }
        return getResponse(token);
    }

    //@SignVerification
    @ResponseBody
    @RequestMapping("apple/login")
    public Object appleLogin(HttpServletRequest request,String identityToken,String appleuser,String fullName,DeviceInfo deviceInfo) throws Exception {
        logger.info("clientIp:{},model:>{},os:>{},method url: /acc/apple/login?identityToken={},appleuser:{},fullName={}", HttpServletUtils.getRemoteIp(request),
        		deviceInfo.getModel(),deviceInfo.getOs(),identityToken, appleuser,fullName);
        if ("0000".equalsIgnoreCase(deviceInfo.getModel())) {
            throw new OAuth2Exception("错误参数3");
        }
        if (org.springframework.util.StringUtils.isEmpty(appleuser)) {
            logger.error("[苹果登录] appleuser 为空: identityToken:{}, appleuser:{},fullName：{} ,clientIp:{},model:>{},os:>{}",identityToken,appleuser,fullName,HttpServletUtils.getRemoteIp(request),
            		deviceInfo.getModel(),deviceInfo.getOs());
            throw new OAuth2Exception("错误参数2");
        }
        if(org.springframework.util.StringUtils.isEmpty(identityToken)){
            logger.error("[苹果登录] identityToken 为空: identityToken:{}, appleuser:{},fullName：{} ,clientIp:{},model:>{},os:>{}",identityToken,appleuser,fullName,HttpServletUtils.getRemoteIp(request),
            		deviceInfo.getModel(),deviceInfo.getOs());
            throw new OAuth2Exception("错误参数1.1");
        }
        String [] identityTokens = identityToken.split("\\.");
        if(identityTokens.length<2) throw new OAuth2Exception("错误参数1.2");
        Map data2 =gson.fromJson(new String(Base64.decodeBase64(identityTokens[0]),"UTF-8"),Map.class);
        Map data =gson.fromJson(new String(Base64.decodeBase64(identityTokens[1]),"UTF-8"),Map.class);
        if(data==null||data2==null) throw new OAuth2Exception("错误参数1.3");
        String kid=(String)data2.get("kid");
        String iss = (String) data.get("iss");
        String aud = (String) data.get("aud");
        String sub = (String) data.get("sub");
       if (webssoService.isValidate(deviceInfo.getAppid(), deviceInfo.getAppVersion())) {
            // 检查用户信息
            if(webssoService.verify(identityToken,iss,aud,sub,kid)){
                logger.info("[苹果登录成功] ,clientIp:{},model:>{},os:>{}",HttpServletUtils.getRemoteIp(request),
                		deviceInfo.getModel(),deviceInfo.getOs());
            }else{
                logger.error("苹果认证失败: identityToken:{}, appleuser:{}，fullName：{} ,clientIp:{},model:>{},os:>{} ",identityToken,appleuser,fullName,HttpServletUtils.getRemoteIp(request),
                		deviceInfo.getModel(),deviceInfo.getOs());
                throw new InvalidThirdTokenException("苹果授权失败");
            }
        }

        OAuth2AccessToken token = webssoService.appleLogin(appleuser,fullName, deviceInfo,request);

        Boolean isAse = (Boolean) request.getAttribute("ase");
        if (isAse != null && isAse) {
            // BusiResult 格式
            return AccountTokenUtil.getResponseVO(token);
        }
        return getResponse(token);
    }
/*    public static void main(String[] args) throws UnsupportedEncodingException {
        //String identityToken2="ZXlKcmFXUWlPaUpsV0dGMWJtMU1JaXdpWVd4bklqb2lVbE15TlRZaWZRLmV5SnBjM01pT2lKb2RIUndjem92TDJGd2NHeGxhV1F1WVhCd2JHVXVZMjl0SWl3aVlYVmtJam9pWTI5dExubHBibWR4ZFM1VGFXZHVTVzVYYVhSb1FYQndiR1V4TWlJc0ltVjRjQ0k2TVRZd01qUXdNRGd6TWl3aWFXRjBJam94TmpBeU16RTBORE15TENKemRXSWlPaUl3TURFeU1EZ3VNRGd3WmpCbVptWmlZVEZrTkdJNU9XRXhaV1V6WXpkaE1qaGxaRE0zTTJRdU1ESXdNQ0lzSW1OZmFHRnphQ0k2SW0wdGNrWnRjekJoU0d0cFVHeEZVMFZ0VjJVNWMyY2lMQ0psYldGcGJDSTZJbmRoYm0xMVgyaGxRSE5rYldOMFpXTm9MbU52YlNJc0ltVnRZV2xzWDNabGNtbG1hV1ZrSWpvaWRISjFaU0lzSW1GMWRHaGZkR2x0WlNJNk1UWXdNak14TkRRek1pd2libTl1WTJWZmMzVndjRzl5ZEdWa0lqcDBjblZsZlEuSlg2NjViRmxabXRacjY2czNxNlV6UE1qdW4yZmxpd21nUUlFY01SeHR3SmRMUFJ6Rk9SVlVRTS1DTWR2T2lvSEFoMTFVVWV1Nzc3YTBCdTlhQlJBQVpJNUVqWEE5dVI4cjQyTjljV3FmaWdMQUt3RUtjVnBJdXNRMXNrb1JZUWgtTldxSnc1eFNBc0N0TXFmbE5tcVZ5QnE0a3BoQVAwaV95Q1hYTlFBZ1NzYjRIUUVMU29Kb1VGUndXTFM4WldkZy1hMXJ4cUs3OHEyUXZBUkJhNURuYVUxMXpZZ3BiYWRIWHdGVEtuaTFINjBpeHBsYjRzM1E4M2w4SGZiTVJhWW5OQ1lVUXY1OWc1SGloeUpBZERTS3I1M1laYkpvcDZIRUpvc3hIU2U2ejM4eW4yVTU5MmJIN0FGcVJjd0R4ejRPZTBhUkdtcHZOZFpWVkJGQjN4OU13";
        //String  identityToken = new String(Base64.decodeBase64(identityToken2));
        String identityToken="eyJraWQiOiJlWGF1bm1MIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJodHRwczovL2FwcGxlaWQuYXBwbGUuY29tIiwiYXVkIjoiY29tLmlyaC5oanRlc3QiLCJleHAiOjE2MDMxNzk4NjIsImlhdCI6MTYwMzA5MzQ2Miwic3ViIjoiMDAwNDk4LjZjMjVjMWFmNTdjZTQzYTc5MDBjODlkMDg5ODI5ZmYwLjA2MDYiLCJjX2hhc2giOiJSb0lKVUVFS2FmbjlYNG9NUld0Q01RIiwiZW1haWwiOiI1M2F3empidHduQHByaXZhdGVyZWxheS5hcHBsZWlkLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjoidHJ1ZSIsImlzX3ByaXZhdGVfZW1haWwiOiJ0cnVlIiwiYXV0aF90aW1lIjoxNjAzMDkzNDYyLCJub25jZV9zdXBwb3J0ZWQiOnRydWV9.0TX0-2uGOu19_1t4wmnaZlOhRj3KVCVksIYggjRkyZ4BuiPwsXPEuqNVpHfSlxXYRStSGD_d3nze8fitvNb4eQ3oAIjjLlvAECxNMz7RsPX1OrCOAJeTK-1vzvqSWJIc99clc8Ytivy5VG89Sr6qI713NmVLZ7VWvQ4LhPzDc69cg7H8Swm93DywkTCpMLtXBG-Rf-AT5sY5oXBWvFRXSHFo1zTcH5iGuH7LV1NJDVA6X-t-fLQvysw8HTpg15dTh5uLu1bWaX3-sq9GwqKwenvxa2N_4TGKOMtmI1xfEtQu22sw-H2OVV8tzmJCpGpEGvihP2AVbefbAlCWwWsbbg";
        System.out.println(identityToken);
        String [] identityTokens = identityToken.split("\\.");
        Gson gson = new Gson();
        Map data2 =gson.fromJson(new String(Base64.decodeBase64(identityTokens[0]),"UTF-8"),Map.class);
        String kid=(String)data2.get("kid");
        System.out.println(kid);
        Map data =gson.fromJson(new String(Base64.decodeBase64(identityTokens[1]),"UTF-8"),Map.class);
        if(data==null) throw new OAuth2Exception("错误参数");
        String iss = (String) data.get("iss");
        String aud = (String) data.get("aud");
        String sub = (String) data.get("sub");
        WebssoService webssoService=new WebssoService();
        boolean verify = webssoService.verify(identityToken, iss, aud, sub,kid);
        System.out.println(verify);
    }*/

    /**
     * 拉贝微信小程序登录
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "wxapp/login", method = RequestMethod.POST)
    public ResponseEntity<OAuth2AccessToken> wxappLogin(HttpServletRequest request, @RequestBody WeixinAuthBO authBo) throws Exception {
        BusiResult<JSONObject> result = weixinAuthService.auth(authBo.getCode(), authBo.getIv(),
                authBo.getSignature(), authBo.getEncryptedData());
        logger.info("[ 小程序登录 ]请求:>{}，返回:>{}", JSON.toJSONString(authBo), JSON.toJSONString(result));
        if (result.getCode() != BusiStatus.SUCCESS.value()) {
            throw new InvalidThirdTokenException(result.getMessage());
        }

        JSONObject object = result.getData();
        DeviceInfo deviceinfo = new DeviceInfo();
        deviceinfo.setOs(authBo.getOs());
        deviceinfo.setAppVersion(authBo.getAppVersion());
        deviceinfo.setShareUid(authBo.getShareUid());
        deviceinfo.setWxapp(true);

        OAuth2AccessToken token = webssoService.login(object.getString("openId"), object.getString("unionId"), 1,
                deviceinfo, request);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        headers.set("openId", object.getString("openId"));
        headers.set("unionId", object.getString("unionId"));

        return new ResponseEntity<>(token, headers, HttpStatus.OK);


    }

    @RequestMapping(value = "wx/auth", method = RequestMethod.GET)
    public Object wxauth(HttpServletRequest request, String openId, String unionId) throws Exception {
        String ip = HttpUitls.getRealIpAddress(request);
        OAuth2AccessToken token = webssoService.wxauth(openId, unionId, ip);
        return getResponse(token);
    }

    private ResponseEntity<OAuth2AccessToken> getResponse(OAuth2AccessToken accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        return new ResponseEntity<>(accessToken, headers, HttpStatus.OK);
    }

    private class ServiceRes {
        private int code;
        private String message;
        private Object data;

        public ServiceRes(OAuthStatus status) {
            this(status, null);
        }

        public ServiceRes(OAuthStatus status, Object data) {
            this.code = status.value();
            this.message = status.getReasonPhrase();
            this.data = data;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
    }
}
