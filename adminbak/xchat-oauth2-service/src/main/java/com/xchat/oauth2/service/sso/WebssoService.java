package com.xchat.oauth2.service.sso;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.xchat.common.device.DeviceInfo;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.common.OAuth2AccessToken;
import com.xchat.oauth2.service.common.exceptions.InvalidThirdTokenException;
import com.xchat.oauth2.service.common.util.OAuth2Utils;
import com.xchat.oauth2.service.core.util.HttpServletUtils;
import com.xchat.oauth2.service.http.HttpUitls;
import com.xchat.oauth2.service.http.HttpUtils;
import com.xchat.oauth2.service.model.Account;
import com.xchat.oauth2.service.model.AccountLoginRecord;
import com.xchat.oauth2.service.provider.OAuth2Authentication;
import com.xchat.oauth2.service.provider.OAuth2Request;
import com.xchat.oauth2.service.provider.endpoint.TokenEndpoint;
import com.xchat.oauth2.service.provider.token.XchatTokenServices;
import com.xchat.oauth2.service.service.JedisService;
import com.xchat.oauth2.service.service.account.AccountService;
import com.xchat.oauth2.service.service.account.LoginRecordService;
import com.xchat.oauth2.service.sso.vo.*;
import com.xchat.oauth2.service.vo.SysConf;

import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sun.applet.resources.MsgAppletViewer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebssoService {
    @Autowired
    private XchatTokenServices tokenServices;
    @Autowired
    private AccountService accountService;
    @Autowired
    private JedisService jedisService;
    @Autowired
    private TokenEndpoint tokenEndpoint;
    @Autowired
    private LoginRecordService loginRecordService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取QQ用户信息 URL
     */
    public static final String QQ_GET_USER_INFO_URL = "https://graph.qq.com/user/get_user_info";
    /**
     * 获取QQ用户的UNIOID
     */
    private static final String QQ_GET_UNIOID_URL = "https://graph.qq.com/oauth2.0/me";
    /**
     * QQ APP ID
     */
//    public static final String QQ_APP_ID = "1108246537";
    public static final String QQ_APP_ID = "101894511";
    /**
     * QQ 的APP key
     */
//    public static final String QQ_APP_KEY = "W8mYlg2oaJ3kcy9a";
//    public static final String QQ_APP_KEY = "0354cdea08ce59dffae0d32e8fe96fba";
    /**
     * 获取微信用户的信息 URL
     */
    public static final String WX_GET_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo";
    /**
     * 是否需要验证信息的-系统配置的KEY
     */
    private static final String validate_key = "validate_third_info";
    private Gson gson = new Gson();
    private static final Logger logger = LoggerFactory.getLogger(WebssoService.class);

    public OAuth2AccessToken login(String openid, String unionid, int type, DeviceInfo deviceInfo, HttpServletRequest request) throws Exception {
        //检查版本
        tokenEndpoint.checkVersion(deviceInfo.getAppVersion());
        // 检查是否需要验证用户信息
        String ipAddress = HttpUitls.getRealIpAddress(request);
        logger.info("login openid=" + openid + "&ipAddress=" + ipAddress);
        if (type == 2) {
            unionid = getUnionid(request.getParameter("access_token"));
        }
        Account account = accountService.getOrGenAccountByOpenid(openid, unionid, type, deviceInfo, ipAddress);
        //查询账号、设备是否被封禁
        if (!StringUtils.isEmpty(account)) {
            tokenEndpoint.queryBlockedAccount(account);
            tokenEndpoint.checkBlockedDevice(deviceInfo.getDeviceId(), unionid, openid, type);
        }
        Long erbanNo = account.getErbanNo();
        Map<String, String> map = Maps.newHashMap();

        map.put(OAuth2Utils.CLIENT_ID, "bb-client");
        map.put("username", erbanNo.toString());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                erbanNo, null);
        OAuth2Request oAuth2Request = new OAuth2Request(map, "bb-client", null, false, null, null, null, null, null);
        OAuth2Authentication auth2Authentication = new OAuth2Authentication(oAuth2Request, usernamePasswordAuthenticationToken);

        OAuth2AccessToken token = tokenServices.createAccessToken(auth2Authentication);
        //将access_token存入cookie
        account = accountService.refreshNetEaseToken(account.getUid());
        token.setUid(account.getUid());
        token.setNetEaseToken(account.getNeteaseToken());
        saveOauthTokenCache(account.getUid(), token.getValue());
        //第三方登录信息插入到记录表
        AccountLoginRecord accountLoginRecord = new AccountLoginRecord();
        accountLoginRecord.setLoginIp(ipAddress);
        accountLoginRecord.setErbanNo(account.getErbanNo());
        accountLoginRecord.setUid(account.getUid());
        accountLoginRecord.setDeviceId(deviceInfo.getDeviceId());
        accountLoginRecord.setAppVersion(deviceInfo.getAppVersion());
        accountLoginRecord.setIspType(deviceInfo.getIspType());
        accountLoginRecord.setModel(deviceInfo.getModel());
        accountLoginRecord.setOs(deviceInfo.getOs());
        accountLoginRecord.setOsversion(deviceInfo.getOsVersion());
        accountLoginRecord.setCreateTime(new Date());
        byte a = (byte) type;
        accountLoginRecord.setLoginType(a);
        if (type == 1) {
            accountLoginRecord.setWeixinOpenid(openid);
        }
        if (type == 2) {
            accountLoginRecord.setQqOpenid(openid);
        }

        loginRecordService.addAccountLoginRecord(accountLoginRecord);
        jedisService.hset(RedisKey.acc_latest_login.getKey(), account.getUid().toString(), gson.toJson(accountLoginRecord));
        return token;
    }

    //保存昵称的登录
    public OAuth2AccessToken login2(String openid, String unionid, int type,String nick, DeviceInfo deviceInfo, HttpServletRequest request) throws Exception {
        //检查版本
        tokenEndpoint.checkVersion(deviceInfo.getAppVersion());
        // 检查是否需要验证用户信息
        String ipAddress = HttpUitls.getRealIpAddress(request);
        logger.info("login openid=" + openid + "&ipAddress=" + ipAddress);
        if (type == 2) {
            unionid = getUnionid(request.getParameter("access_token"));
        }
        Account account = accountService.getOrGenAccountByOpenid2(openid, unionid, type,nick, deviceInfo, ipAddress);
        //查询账号、设备是否被封禁
        if (!StringUtils.isEmpty(account)) {
            tokenEndpoint.queryBlockedAccount(account);
            tokenEndpoint.checkBlockedDevice(deviceInfo.getDeviceId(), unionid, openid, type);
        }
        Long erbanNo = account.getErbanNo();
        Map<String, String> map = Maps.newHashMap();

        map.put(OAuth2Utils.CLIENT_ID, "bb-client");
        map.put("username", erbanNo.toString());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                erbanNo, null);
        OAuth2Request oAuth2Request = new OAuth2Request(map, "bb-client", null, false, null, null, null, null, null);
        OAuth2Authentication auth2Authentication = new OAuth2Authentication(oAuth2Request, usernamePasswordAuthenticationToken);

        OAuth2AccessToken token = tokenServices.createAccessToken(auth2Authentication);
        //将access_token存入cookie
        account = accountService.refreshNetEaseToken(account.getUid());
        token.setUid(account.getUid());
        token.setNetEaseToken(account.getNeteaseToken());
        saveOauthTokenCache(account.getUid(), token.getValue());
        //第三方登录信息插入到记录表
        AccountLoginRecord accountLoginRecord = new AccountLoginRecord();
        accountLoginRecord.setLoginIp(ipAddress);
        accountLoginRecord.setErbanNo(account.getErbanNo());
        accountLoginRecord.setUid(account.getUid());
        accountLoginRecord.setDeviceId(deviceInfo.getDeviceId());
        accountLoginRecord.setAppVersion(deviceInfo.getAppVersion());
        accountLoginRecord.setIspType(deviceInfo.getIspType());
        accountLoginRecord.setModel(deviceInfo.getModel());
        accountLoginRecord.setOs(deviceInfo.getOs());
        accountLoginRecord.setOsversion(deviceInfo.getOsVersion());
        accountLoginRecord.setCreateTime(new Date());
        byte a = (byte) type;
        accountLoginRecord.setLoginType(a);
        if (type == 1) {
            accountLoginRecord.setWeixinOpenid(openid);
        }
        if (type == 2) {
            accountLoginRecord.setQqOpenid(openid);
        }

        loginRecordService.addAccountLoginRecord(accountLoginRecord);
        jedisService.hset(RedisKey.acc_latest_login.getKey(), account.getUid().toString(), gson.toJson(accountLoginRecord));
        String loginTypeStr= type == 1 ? "微信" : type == 2 ? "QQ" : ""; 
        logger.info("[第三方登录]登录类型:>{},clientIp:>{},lastLoginIp:>{},uid:>{},erbanNo:>{},model:>{},os:>{}",loginTypeStr,HttpServletUtils.getRemoteIp(request),account.getLastLoginIp(),account.getUid(),account.getErbanNo(),deviceInfo.getModel(),deviceInfo.getOs());
        return token;
    }


    //保存昵称的登录
    public OAuth2AccessToken appleLogin(String appleuser, String fullName,DeviceInfo deviceInfo, HttpServletRequest request) throws Exception {
        //检查版本
        tokenEndpoint.checkVersion(deviceInfo.getAppVersion());
        // 检查是否需要验证用户信息
        String ipAddress = HttpUitls.getRealIpAddress(request);
        logger.info("login appleuser=" + appleuser + "&ipAddress=" + ipAddress);

        Account account = accountService.getOrGenAccount(appleuser,fullName, deviceInfo, ipAddress);
        //查询账号、设备是否被封禁
        if (!StringUtils.isEmpty(account)) {
            tokenEndpoint.queryBlockedAccount(account);
            tokenEndpoint.checkBlockedDevice(deviceInfo.getDeviceId(),appleuser);
        }
        Long erbanNo = account.getErbanNo();
        Map<String, String> map = Maps.newHashMap();

        map.put(OAuth2Utils.CLIENT_ID, "bb-client");
        map.put("username", erbanNo.toString());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                erbanNo, null);
        OAuth2Request oAuth2Request = new OAuth2Request(map, "bb-client", null, false, null, null, null, null, null);
        OAuth2Authentication auth2Authentication = new OAuth2Authentication(oAuth2Request, usernamePasswordAuthenticationToken);

        OAuth2AccessToken token = tokenServices.createAccessToken(auth2Authentication);
        //将access_token存入cookie
        account = accountService.refreshNetEaseToken(account.getUid());
        token.setUid(account.getUid());
        token.setNetEaseToken(account.getNeteaseToken());
        saveOauthTokenCache(account.getUid(), token.getValue());
        //第三方登录信息插入到记录表
        AccountLoginRecord accountLoginRecord = new AccountLoginRecord();
        accountLoginRecord.setLoginIp(ipAddress);
        accountLoginRecord.setErbanNo(account.getErbanNo());
        accountLoginRecord.setUid(account.getUid());
        accountLoginRecord.setDeviceId(deviceInfo.getDeviceId());
        accountLoginRecord.setAppVersion(deviceInfo.getAppVersion());
        accountLoginRecord.setIspType(deviceInfo.getIspType());
        accountLoginRecord.setModel(deviceInfo.getModel());
        accountLoginRecord.setOs(deviceInfo.getOs());
        accountLoginRecord.setOsversion(deviceInfo.getOsVersion());
        accountLoginRecord.setCreateTime(new Date());
        byte a = 3;
        accountLoginRecord.setLoginType(a);
        //苹果登录
        loginRecordService.addAccountLoginRecord(accountLoginRecord);
        jedisService.hset(RedisKey.acc_latest_login.getKey(), account.getUid().toString(), gson.toJson(accountLoginRecord));
        logger.info("[苹果登录] clientIp:>{},lastLoginIp:>{},uid:>{},erbanNo:>{},model:>{},os:>{}",HttpServletUtils.getRemoteIp(request),account.getLastLoginIp(),account.getUid(),account.getErbanNo(),deviceInfo.getModel(),deviceInfo.getOs());
        return token;
    }

    public OAuth2AccessToken wxauth(String openid, String unionid, String ip) throws Exception {
        Account account = accountService.getAccountByOpenid(openid, unionid);
        //查询账号、设备是否被封禁
        if (!StringUtils.isEmpty(account)) {
            tokenEndpoint.queryBlockedAccount(account);
        }

        Long erbanNo = account.getErbanNo();
        Map<String, String> map = Maps.newHashMap();
        map.put(OAuth2Utils.CLIENT_ID, "bb-client");
        map.put("username", erbanNo.toString());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(erbanNo, null);
        OAuth2Request oAuth2Request = new OAuth2Request(map, "bb-client", null, false, null, null, null, null, null);
        OAuth2Authentication auth2Authentication = new OAuth2Authentication(oAuth2Request, usernamePasswordAuthenticationToken);

        OAuth2AccessToken token = tokenServices.createAccessToken(auth2Authentication);
        //将access_token存入cookie
        account = accountService.refreshNetEaseToken(account.getUid());
        token.setUid(account.getUid());
        token.setNetEaseToken(account.getNeteaseToken());
        saveOauthTokenCache(account.getUid(), token.getValue());
        //第三方登录信息插入到记录表
        AccountLoginRecord accountLoginRecord = new AccountLoginRecord();
        accountLoginRecord.setLoginIp(ip);
        accountLoginRecord.setErbanNo(account.getErbanNo());
        accountLoginRecord.setUid(account.getUid());
        accountLoginRecord.setOs("web");
        accountLoginRecord.setCreateTime(new Date());
        accountLoginRecord.setLoginType((byte) 3);
        accountLoginRecord.setWeixinOpenid(openid);
        loginRecordService.addAccountLoginRecord(accountLoginRecord);
        jedisService.hset(RedisKey.acc_latest_login.getKey(), account.getUid().toString(), gson.toJson(accountLoginRecord));
        return token;
    }


    /**
     * 验证用户第三方信息
     *
     * @param type
     * @param openid
     * @param deviceInfo
     * @param access_token
     * @throws IOException
     */
    public String validateUserInfo(int type, String openid, DeviceInfo deviceInfo, String access_token) throws IOException {
        String typeStr = type == 2 ? "QQ" : type == 1 ? "微信" : String.valueOf(type);
        if (StringUtils.isEmpty(openid)) {
            logger.error("[第三方登录] openid 为空 type:{}, deviceInfo:{}", typeStr, deviceInfo);
            throw new InvalidThirdTokenException("第三方授权失败");
        }
        // String access_token = request.getParameter("access_token");
        if (StringUtils.isEmpty(access_token)) {
            //
            logger.error("[第三方登录] token 为空 type:{}, openid:{}, deviceInfo:{}", typeStr, openid, deviceInfo);
            throw new InvalidThirdTokenException("第三方授权失败");
        }
        logger.info("[第三方登录] 获取用户信息: type:{} openid:{}, access_token:{}, device_info:{}", typeStr, openid, access_token, deviceInfo);
        switch (type) {
            case 1:
                WXUserInfoVO wxUserInfo = getWXUserInfo(access_token, openid);
                if (wxUserInfo == null) {
                    logger.error("[微信登录] 信息获取为空 openid:{}, access_token:{}, deviceInfo:{}", openid, access_token, deviceInfo);
                    throw new InvalidThirdTokenException("微信授权失败");
                }
                logger.info("[微信登录成功]");
                return wxUserInfo.getNickname();
                //break;
            case 2:
                QQUserInfoVO vo = getQQUseInfo(access_token, openid, deviceInfo);
                if (vo == null) {
                    logger.error("[QQ登录] 获取信息失败 openid:{}, access_token:{}, deviceInfo:{}", openid, access_token, deviceInfo);
                    throw new InvalidThirdTokenException("QQ授权失败");
                }
                logger.info("[QQ登录成功]");
                return vo.getNickname();
                //break;
            default:
                logger.error("[第三方登录] type:{}, openid:{}, access_token:{}, deviceInfo:{}", type, openid, access_token, deviceInfo);
                throw new InvalidThirdTokenException("第三方授权失败");
        }
    }

    /**
     * 获取微信用户信息
     *
     * @param access_token
     * @param openid
     * @return
     * @throws IOException
     */
    public WXUserInfoVO getWXUserInfo(String access_token, String openid) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("access_token=").append(access_token);
        sb.append("&openid=").append(openid);
        String result = HttpUtils.get(WX_GET_USER_INFO_URL, sb.toString());
        logger.info("[微信登录] 获取用户信息: result:{}", result);
        if (StringUtils.isEmpty(result)) {
            //
            return null;
        }
        WXErrorVO errorVO = gson.fromJson(result, WXErrorVO.class);
        if (errorVO != null && !StringUtils.isEmpty(errorVO.getErrcode())) {
            // 微信接口调用失败
            return null;
        }
        WXUserInfoVO vo = gson.fromJson(result, WXUserInfoVO.class);

        return vo;
    }

    /**
     * 获取QQ用户信息
     *
     * @param access_token
     * @param openid
     * @return
     * @throws IOException
     */
    public QQUserInfoVO getQQUseInfo(String access_token, String openid, DeviceInfo deviceInfo) throws IOException {
        if (deviceInfo == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("access_token=").append(access_token);
        sb.append("&oauth_consumer_key=");
        sb.append(QQ_APP_ID);
        sb.append("&openid=").append(openid);
        String result = HttpUtils.get(QQ_GET_USER_INFO_URL, sb.toString());
        logger.info("[QQ登录] result:{}", result);
        if (StringUtils.isEmpty(result)) {
            //
            return null;
        }
        QQUserInfoVO vo = gson.fromJson(result, QQUserInfoVO.class);
        if ("0".equals(vo.getRet())) {
            // 查询成功
            return vo;
        }
        return null;
    }

    public String getUnionid(String access_token) throws IOException {
        if (StringUtils.isEmpty(access_token)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("access_token=").append(access_token);
        sb.append("&unionid=1");
        String result = HttpUtils.get(QQ_GET_UNIOID_URL, sb.toString());
        if (StringUtils.isEmpty(result)) {
            return "";
        }
        logger.info("[获取QQ用户的UNIOID], result:{}", result);
        String[] ar = result.split("\\(");
        ar = ar[1].split("\\)");
        result = ar[0];
        QQUnioidVO vo = JSONObject.parseObject(result.trim(), QQUnioidVO.class);
        logger.info("[获取QQ用户的UNIOID],QQUnionidVO:{}", vo);
        if (vo != null && vo.getUnionid() != null) {
            return vo.getUnionid();
        }
        return "";
    }


    /**
     * 是否需要校验登录信息</br>
     *
     * @return
     */
    public boolean isValidate(String app, String appVersion) {
        String config = jedisService.hget(RedisKey.sys_conf.getKey(), validate_key);
        SysConf conf;
        if (StringUtils.isEmpty(config)) {
            List<SysConf> list = jdbcTemplate.query("SELECT * FROM sys_conf WHERE config_id = ? AND config_status = 1", new BeanPropertyRowMapper<>(SysConf.class), validate_key);
            if (list == null || list.isEmpty()) {
                return false;
            }
            conf = list.get(0);
            jedisService.hset(RedisKey.sys_conf.getKey(), validate_key, gson.toJson(conf));
        } else {
            conf = gson.fromJson(config, SysConf.class);
        }
        // 是否需要验证
        if ("true".equals(conf.getConfigValue())) {
            // 强制验证所有版本
            return true;
        } else {
            return true;
        }
    }

    private void saveOauthTokenCache(Long uid, String accessToken) throws Exception {
        jedisService.hwrite(RedisKey.uid_access_token.getKey(), uid.toString(), accessToken);
    }

    /**
     * 苹果登录身份认证
     * @param identityToken
     * @param iss
     * @param aud
     * @param sub
     * @return
     */
    public boolean verify(String identityToken,String iss,String aud,String sub,String kid)  {
        PublicKey publicKey = getPublicKey(kid);
        System.out.println(publicKey);
        JwtParser jwtParser = Jwts.parser().setSigningKey(publicKey);
        jwtParser.requireIssuer(iss);
        jwtParser.requireAudience(aud);
        jwtParser.requireSubject(sub);
        try {
            Jws<Claims> claim = jwtParser.parseClaimsJws(identityToken);
            if (null != claim && claim.getBody().containsKey("auth_time")) {
                return true;
            }
            return false;
        } catch (ExpiredJwtException e) {
            throw new InvalidThirdTokenException("授权码过期");
            //return false;
        } catch (Exception e) {
            throw new InvalidThirdTokenException("Apple授权失败");
            //return false;
        }
    }
    /**
     * 获取苹果登录验证的key
     * @return
     */
    public  PublicKey getPublicKey(String kid) {
        try {
            String forObject = HttpUtils.get("https://appleid.apple.com/auth/keys");
            logger.info("[获取苹果登录验证的key], result:{}", forObject);
            if(StringUtils.isEmpty(forObject)){ return null; }
            AppleKeys appleKeys = gson.fromJson(forObject, AppleKeys.class);
            //AppleKeys appleKeys = JsonUtils.parseObject(forObject, AppleKeys.class);
            List<AppleKeys.Keys> keys = appleKeys.getKeys();
            if(CollectionUtils.isEmpty(keys)) return null;
            for(AppleKeys.Keys key:keys){
                if(key.getKid().equals(kid)){
                    String n = key.getN();
                    String e = key.getE();
                    final BigInteger modulus = new BigInteger(1, Base64.decodeBase64(n));
                    final BigInteger publicExponent = new BigInteger(1, Base64.decodeBase64(e));
                    final RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, publicExponent);
                    final KeyFactory kf = KeyFactory.getInstance("RSA");
                    return kf.generatePublic(spec);
                }
            }
        } catch (final Exception e) {
            logger.info("[APPLE]ERROR:{}", e);
        }
        return null;
    }
}
