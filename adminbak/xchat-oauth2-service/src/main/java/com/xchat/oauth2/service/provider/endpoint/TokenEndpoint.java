/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xchat.oauth2.service.provider.endpoint;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.*;

import com.google.gson.Gson;
import com.xchat.common.config.GlobalConfig;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.common.wx.MD5Utils;
import com.xchat.oauth2.service.common.exceptions.*;
import com.xchat.oauth2.service.common.exceptions.myexception.InvalidAccountException;
import com.xchat.oauth2.service.common.exceptions.myexception.InvalidDeceiveException;
import com.xchat.oauth2.service.common.exceptions.myexception.InvalidVersionException;
import com.xchat.oauth2.service.http.HttpUitls;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.AccountMapper;
import com.xchat.oauth2.service.model.*;
import com.xchat.oauth2.service.provider.*;
import com.xchat.oauth2.service.service.JedisService;
import com.xchat.oauth2.service.service.account.LoginRecordService;
import com.xchat.oauth2.service.service.account.AccountService;
import com.xchat.oauth2.service.service.sign.AppSignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import com.xchat.oauth2.service.common.OAuth2AccessToken;
import com.xchat.oauth2.service.common.util.OAuth2Utils;
import com.xchat.oauth2.service.core.util.HttpServletUtils;
import com.xchat.oauth2.service.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * Endpoint for token requests as described in the OAuth2 spec. Clients post requests with a <code>grant_type</code>
 * parameter (e.g. "authorization_code") and other parameters as determined by the grant type. Supported grant types are
 * handled by the provided {@link #setTokenGranter(com.xchat.oauth2.service.provider.TokenGranter) token
 * granter}.
 * </p>
 * <p>
 * <p>
 * Clients must be authenticated using a Spring Security {@link org.springframework.security.core.Authentication} to
 * access this endpoint, and the client
 * id is extracted from the authentication token. The best way to arrange this (as per the OAuth2 spec) is to use HTTP
 * basic authentication for this endpoint with standard Spring Security support.
 * </p>
 *
 * @author Dave Syer
 */
@FrameworkEndpoint
public class TokenEndpoint extends AbstractEndpoint {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private OAuth2RequestValidator oAuth2RequestValidator = new DefaultOAuth2RequestValidator();

    private Set<HttpMethod> allowedRequestMethods = new HashSet<>(Arrays.asList(HttpMethod.POST, HttpMethod.GET));
    @Autowired
    private AccountService accountService;
    @Autowired
    private AppSignService signService;
    @Autowired
    private JedisService jedisService;
    @Autowired
    private LoginRecordService loginRecordService;
    @Autowired
    private AccountMapper accountMapper;

    private Gson gson = new Gson();

    // @RequestMapping(value = "/oauth/token")
    // public ResponseEntity<OAuth2AccessToken> getAccessToken(Principal principal, @RequestParam
    //         Map<String, String> parameters, HttpMethod requestMethod, HttpServletRequest request,
    //                                                         HttpServletResponse response) throws Exception {
    //     Iterator it = parameters.keySet().iterator();
    //     String params = "";
    //     while (it.hasNext()) {
    //         String paramName = (String) it.next();
    //         String paramValue = parameters.get(paramName);
    //         params = params + paramName + "=" + paramValue + "&";
    //     }
    //
    //     if (!this.isSign(request, response)) {
    //         return null;
    //     }
    //
    //     logger.info("user login,uri=/oauth/token?" + params);
    //     if (!allowedRequestMethods.contains(requestMethod)) {
    //         logger.error("[ oauthToken ]HttpRequestMethodNotSupportedException:>{}", parameters);
    //         throw new HttpRequestMethodNotSupportedException(requestMethod.toString());
    //     }
    //
    //     if (!(principal instanceof Authentication)) {
    //         logger.error("[ oauthToken ]InsufficientAuthenticationException:>{}", parameters);
    //         throw new InsufficientAuthenticationException(
    //                 "There is no client authentication. Try adding an appropriate authentication filter.");
    //     }
    //
    //     String clientId = getClientId(principal);
    //     ClientDetails authenticatedClient = getClientDetailsService().loadClientByClientId(clientId);
    //
    //     TokenRequest tokenRequest = getOAuth2RequestFactory().createTokenRequest(parameters, authenticatedClient);
    //
    //     if (clientId != null && !clientId.equals("")) {
    //         // Only validate the client details if a client authenticated during this
    //         // request.
    //         if (!clientId.equals(tokenRequest.getClientId())) {
    //             // double check to make sure that the client ID in the token request is the same as that in the
    //             // authenticated client
    //             logger.error("[ oauthToken ]InvalidClientException:>{}", parameters);
    //             throw new InvalidClientException("Given client ID does not match authenticated client");
    //         }
    //     }
    //     if (authenticatedClient != null) {
    //         oAuth2RequestValidator.validateScope(tokenRequest, authenticatedClient);
    //     }
    //     if (!StringUtils.hasText(tokenRequest.getGrantType())) {
    //         logger.error("[ oauthToken ]InvalidRequestException:>{}", parameters);
    //         throw new InvalidRequestException("Missing grant type");
    //     }
    //     if (tokenRequest.getGrantType().equals("implicit")) {
    //         logger.error("[ oauthToken ]InvalidGrantException:>{}", parameters);
    //         throw new InvalidGrantException("Implicit grant type not supported from token endpoint");
    //     }
    //
    //     if (isAuthCodeRequest(parameters)) {
    //         // The scope was requested or determined during the authorization step
    //         if (!tokenRequest.getScope().isEmpty()) {
    //             logger.debug("Clearing scope of incoming token request");
    //             tokenRequest.setScope(Collections.<String>emptySet());
    //         }
    //     }
    //
    //     if (isRefreshTokenRequest(parameters)) {
    //         // A refresh token has its own default scopes, so we should ignore any added by the factory here.
    //         tokenRequest.setScope(OAuth2Utils.parseParameterList(parameters.get(OAuth2Utils.SCOPE)));
    //     }
    //
    //     TokenGranter tokenGranter = getTokenGranter();
    //     OAuth2AccessToken token = tokenGranter.grant(tokenRequest.getGrantType(), tokenRequest);
    //     if (token == null) {
    //         logger.error("[ oauthToken ]UnsupportedGrantTypeException:>{}", parameters);
    //         throw new UnsupportedGrantTypeException("Unsupported grant type: " + tokenRequest.getGrantType());
    //     }
    //     Account account = accountService.refreshNetEaseTokne(parameters.get("phone"));
    //     // TODO 上线前去掉判断
    //     if (jedisService.get("erban_check_version") != null) {
    //         checkVersion(parameters.get("appVersion"));
    //     }
    //     queryBlockedAccount(account);
    //     String deviceId = parameters.get("deviceId");
    //     checkBlockedDevice(deviceId, "", null, 3);
    //     token.setUid(account.getUid());
    //     token.setNetEaseToken(account.getNeteaseToken());
    //     saveOauthTokenCache(account.getUid(), token.getValue());
    //     //将用户信息登记
    //     byte loginType = 3;//微信为1，qq为2，手机登或者拉贝号默认为3
    //     String ipAddress = HttpUitls.getRealIpAddress(request);
    //     AccountLoginRecord accountLoginRecord = new AccountLoginRecord();
    //     accountLoginRecord.setLoginIp(ipAddress);
    //     accountLoginRecord.setUid(account.getUid());
    //     accountLoginRecord.setErbanNo(account.getErbanNo());
    //     accountLoginRecord.setLoginType(loginType);
    //     accountLoginRecord.setDeviceId(parameters.get("deviceId"));
    //     accountLoginRecord.setPhone(parameters.get("phone"));
    //     accountLoginRecord.setAppVersion(parameters.get("appVersion"));
    //     accountLoginRecord.setIspType(parameters.get("ispType"));
    //     accountLoginRecord.setModel(parameters.get("model"));
    //     accountLoginRecord.setOs(parameters.get("os"));
    //     accountLoginRecord.setOsversion(parameters.get("osVersion"));
    //     accountLoginRecord.setCreateTime(new Date());
    //     loginRecordService.addAccountLoginRecord(accountLoginRecord);
    //     jedisService.hset(RedisKey.acc_latest_login.getKey(), account.getUid().toString(), gson.toJson
    //             (accountLoginRecord));
    //     return getResponse(token);
    // }

    @RequestMapping(value = "/oauth/token")
    public ResponseEntity<?> getAccessToken(HttpServletRequest request,
                                            HttpServletResponse response,
                                            HttpMethod requestMethod,
                                            @RequestParam Map<String, String> parameters,
                                            Principal principal) throws Exception {
        Iterator it = parameters.keySet().iterator();
        StringBuilder builder = new StringBuilder();
        while (it.hasNext()) {
            String paramName = (String) it.next();
            String paramValue = parameters.get(paramName);
            builder.append(paramName).append("=").append(paramValue).append("&");
        }

        logger.info("[ oauthToken ]手机号登录 clientIp:>"+HttpServletUtils.getRemoteIp(request)+",phone:>"+parameters.get("phone")+",os:>"+parameters.get("os")+",model:>"+parameters.get("model")+",user login,uri=/oauth/token?" + builder);

        if (!this.isSign(request, parameters)) {
            BusiResult result = new BusiResult(BusiStatus.SIGN_AUTHORITY);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        if (!allowedRequestMethods.contains(requestMethod)) {
            logger.error("[ oauthToken ]HttpRequestMethodNotSupportedException:>{}", parameters);
            throw new HttpRequestMethodNotSupportedException(requestMethod.toString());
        }

        if (!(principal instanceof Authentication)) {
            logger.error("[ oauthToken ]InsufficientAuthenticationException:>{}", parameters);
            throw new InsufficientAuthenticationException("There is no client authentication. Try adding an " +
                    "appropriate authentication filter.");
        }

        String clientId = getClientId(principal);
        ClientDetails authenticatedClient = getClientDetailsService().loadClientByClientId(clientId);

        TokenRequest tokenRequest = getOAuth2RequestFactory().createTokenRequest(parameters, authenticatedClient);

        if (clientId != null && !clientId.equals("")) {
            // Only validate the client details if a client authenticated during this
            // request.
            if (!clientId.equals(tokenRequest.getClientId())) {
                // double check to make sure that the client ID in the token request is the same as that in the
                // authenticated client
                logger.error("[ oauthToken ]InvalidClientException:>{}", parameters);
                throw new InvalidClientException("Given client ID does not match authenticated client");
            }
        }

        if (authenticatedClient != null) {
            oAuth2RequestValidator.validateScope(tokenRequest, authenticatedClient);
        }

        if (!StringUtils.hasText(tokenRequest.getGrantType())) {
            logger.error("[ oauthToken ]InvalidRequestException:>{}", parameters);
            throw new InvalidRequestException("Missing grant type");
        }

        if (tokenRequest.getGrantType().equals("implicit")) {
            logger.error("[ oauthToken ]InvalidGrantException:>{}", parameters);
            throw new InvalidGrantException("Implicit grant type not supported from token endpoint");
        }

        if (isAuthCodeRequest(parameters)) {
            // The scope was requested or determined during the authorization step
            if (!tokenRequest.getScope().isEmpty()) {
                logger.debug("Clearing scope of incoming token request");
                tokenRequest.setScope(Collections.<String>emptySet());
            }
        }

        if (isRefreshTokenRequest(parameters)) {
            // A refresh token has its own default scopes, so we should ignore any added by the factory here.
            tokenRequest.setScope(OAuth2Utils.parseParameterList(parameters.get(OAuth2Utils.SCOPE)));
        }

        TokenGranter tokenGranter = getTokenGranter();
        OAuth2AccessToken token = tokenGranter.grant(tokenRequest.getGrantType(), tokenRequest);
        if (token == null) {
            logger.error("[ oauthToken ]UnsupportedGrantTypeException:>{}", parameters);
            throw new UnsupportedGrantTypeException("Unsupported grant type: " + tokenRequest.getGrantType());
        }

        Account account = accountService.refreshNetEaseToken(parameters.get("phone"));
        if (jedisService.get("erban_check_version") != null) {
            checkVersion(parameters.get("appVersion"));
        }

        queryBlockedAccount(account);
        String deviceId = parameters.get("deviceId");
        checkBlockedDevice(deviceId, "", null, 3);
        token.setUid(account.getUid());
        token.setNetEaseToken(account.getNeteaseToken());
        saveOauthTokenCache(account.getUid(), token.getValue());
        // 将用户信息登记
        byte loginType = 3; // 1.微信; 2.QQ; 3.手机登录或者拉贝号登录
        String ipAddress = HttpUitls.getRealIpAddress(request);
        AccountLoginRecord accountLoginRecord = new AccountLoginRecord();
        accountLoginRecord.setLoginIp(ipAddress);
        accountLoginRecord.setUid(account.getUid());
        accountLoginRecord.setErbanNo(account.getErbanNo());
        accountLoginRecord.setLoginType(loginType);
        accountLoginRecord.setDeviceId(parameters.get("deviceId"));
        accountLoginRecord.setPhone(parameters.get("phone"));
        accountLoginRecord.setAppVersion(parameters.get("appVersion"));
        accountLoginRecord.setIspType(parameters.get("ispType"));
        accountLoginRecord.setModel(parameters.get("model"));
        accountLoginRecord.setOs(parameters.get("os"));
        accountLoginRecord.setOsversion(parameters.get("osVersion"));
        accountLoginRecord.setCreateTime(new Date());
        loginRecordService.addAccountLoginRecord(accountLoginRecord);
        jedisService.hset(RedisKey.acc_latest_login.getKey(), account.getUid().toString(), gson.toJson(accountLoginRecord));
        logger.info("[ oauthToken ]手机号登录 clientIp:>{},lastLoginIp:>{},uid:>{},erbanNo:>{},phone:>{},os:>{},model:>{}",HttpServletUtils.getRemoteIp(request),account.getLastLoginIp(),account.getUid(),account.getErbanNo(),parameters.get("phone"),parameters.get("os"),parameters.get("model"));
        Boolean isAse = (Boolean) request.getAttribute("ase");
        if (isAse != null && isAse) {
            // BusiResult 格式
            return AccountTokenUtil.getResponseVO(token);
        }
        return getResponse(token);
    }

    private boolean isSign(HttpServletRequest request, Map<String, String> parameters) {
        String appVersion = parameters.get("appVersion");
        String uri = request.getRequestURI();

        if (org.apache.commons.lang3.StringUtils.isBlank(appVersion)) {
            // 拿不到appCode，再次去app_version进行校验
            logger.warn("[ 签名验证拦截 ] 无法获取到版本信息，进行拦截，接口:>{}", uri);
            return false;
        }

        String sn = this.getSn(request);
        if (!"prod".equalsIgnoreCase(GlobalConfig.sysEnv) && "123456".equalsIgnoreCase(sn)) {
            return true;
        }

        if (org.apache.commons.lang3.StringUtils.isBlank(sn)) {
            logger.warn("[ 签名验证拦截 ] 无签名字段，进行拦截，接口:>{}", uri);
            // BusiResult result = new BusiResult(BusiStatus.SIGN_AUTHORITY);
            // this.print(response, gson.toJson(result));
            return false;
        }

        String t = this.getTimeStamps(request);
        parameters.put("t", t);
        parameters.remove("sn");

        String preSign = signService.getPreSign(parameters.get("os"), appVersion, uri, parameters);
        String sign = MD5Utils.encode(preSign);
        if (sign.length() > 7) {
            sign = sign.substring(0, 7);
        }

        if (!sn.equalsIgnoreCase(sign)) {
            logger.warn("[ 签名验证拦截 ] 验证错误，接口:>{},sn:>{},签名字符:>{},签名:>{}", uri, sn, preSign, sign);
            // this.print(response, gson.toJson(new BusiResult(BusiStatus.SIGN_AUTHORITY)));
            return false;
        }
        return true;
    }

    private String getSn(HttpServletRequest request) {
        String sn = request.getHeader("sn");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(sn)) {
            return sn;
        }
        return request.getParameter("sn");
    }

    private String getTimeStamps(HttpServletRequest request) {
        String t = request.getHeader("t");
        if (org.apache.commons.lang3.StringUtils.isNotBlank(t)) {
            return t;
        }
        return request.getParameter("t");
    }

    /**
     * 向页面返回消息
     *
     * @param response
     * @param body
     * @throws IOException
     */
    private void print(HttpServletResponse response, String body) {
        PrintWriter writer = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            writer = response.getWriter();
            writer.write(body);
            writer.flush();
        } catch (IOException e) {
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 保存授权Token到Redis
     *
     * @param uid         用户UID
     * @param accessToken 访问Token
     * @throws Exception
     */
    private void saveOauthTokenCache(Long uid, String accessToken) throws Exception {
        jedisService.hwrite(RedisKey.uid_access_token.getKey(), uid.toString(), accessToken);
    }

    /**
     * @param principal the currently authentication principal
     * @return a client id if there is one in the principal
     */
    protected String getClientId(Principal principal) {
        Authentication client = (Authentication) principal;
        if (!client.isAuthenticated()) {
            throw new InsufficientAuthenticationException("The client is not authenticated.");
        }
        String clientId = client.getName();
        if (client instanceof OAuth2Authentication) {
            // Might be a client and user combined authentication
            clientId = ((OAuth2Authentication) client).getOAuth2Request().getClientId();
        }
        return clientId;
    }

    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<OAuth2Exception> handleException(Exception e) throws Exception {
    // 	e.printStackTrace();
    //     logger.info("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
    //     return getExceptionTranslator().translate(e);
    // }

    @ExceptionHandler(ClientRegistrationException.class)
    public ResponseEntity<OAuth2Exception> handleClientRegistrationException(Exception e) throws Exception {
        e.printStackTrace();
        logger.info("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
        return getExceptionTranslator().translate(new BadClientCredentialsException());
    }

    @ExceptionHandler(OAuth2Exception.class)
    public ResponseEntity<OAuth2Exception> handleException(OAuth2Exception e) throws Exception {
        e.printStackTrace();
        logger.info("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
        return getExceptionTranslator().translate(e);
    }

    private ResponseEntity<OAuth2AccessToken> getResponse(OAuth2AccessToken accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        return new ResponseEntity<OAuth2AccessToken>(accessToken, headers, HttpStatus.OK);
    }

    private boolean isRefreshTokenRequest(Map<String, String> parameters) {
        return "refresh_token".equals(parameters.get("grant_type")) && parameters.get("refresh_token") != null;
    }

    private boolean isAuthCodeRequest(Map<String, String> parameters) {
        return "authorization_code".equals(parameters.get("grant_type")) && parameters.get("code") != null;
    }

    public void setOAuth2RequestValidator(OAuth2RequestValidator oAuth2RequestValidator) {
        this.oAuth2RequestValidator = oAuth2RequestValidator;
    }

    public void setAllowedRequestMethods(Set<HttpMethod> allowedRequestMethods) {
        this.allowedRequestMethods = allowedRequestMethods;
    }

    public static Object JSONToObject(String json, Class beanClass) {
        Gson gson = new Gson();
        Object res = gson.fromJson(json, beanClass);
        return res;
    }

    //查询账号设备是否被封禁
    public void queryBlockedAccount(Account account) {
        if (account != null) {
            String erbanNo = String.valueOf(account.getErbanNo());
            String erbanNoache = jedisService.hget(RedisKey.block_account.getKey(), erbanNo);
            if (erbanNoache != null) {
                AccountBlock accountBlock = (AccountBlock) JSONToObject(erbanNoache, AccountBlock.class);
                if (erbanNo.equals(accountBlock.getErbanNo().toString())) {
                    String status = accountBlock.getBlockStatus().toString();
                    Date currentTime = new Date();
                    boolean betweenDate = DateTimeUtil.isBetweenDate(currentTime, accountBlock.getBlockStartTime(),
                            accountBlock.getBlockEndTime());
                    if (betweenDate) {
                        if ("1".equals(status)) {
                            logger.info("账号被封禁,erbanNo=" + accountBlock.getErbanNo());
                            throw new InvalidAccountException("用户账号异常，请联系官方客服");
                        }
                    }
                }
            }
        }
    }

    //查询设备是否被封禁
    public void checkBlockedDevice(String deviceId, String openId, String unionid, int loginType) {
        if (deviceId != null) {//传进来的设备id不为空
            queryBlockAccountAche(deviceId);
            return;
        }//参数为空需要去查询

        AccountExample accountExample = new AccountExample();
        List<Account> accounts = null;
        if (!StringUtils.isEmpty(unionid)) {
            if (loginType == 2) {
                accountExample.createCriteria().andQqUnionidEqualTo(unionid);
                accounts = accountMapper.selectByExample(accountExample);
            } else if (loginType == 1) {
                accountExample.createCriteria().andWeixinUnionidEqualTo(unionid);
                accounts = accountMapper.selectByExample(accountExample);
            }
        }

        if (CollectionUtils.isEmpty(accounts)) {
            if (!StringUtils.isEmpty(openId) && loginType == 2) {
                accountExample.createCriteria().andQqOpenidEqualTo(openId);
                accounts = accountMapper.selectByExample(accountExample);
            }
            if (!StringUtils.isEmpty(openId) && loginType == 1) {
                accountExample.createCriteria().andWeixinOpenidEqualTo(openId);
                accounts = accountMapper.selectByExample(accountExample);
            }
        }

        if (!CollectionUtils.isEmpty(accounts)) {
            String lastLogin = jedisService.hget(RedisKey.acc_latest_login.getKey(),
                    accounts.get(0).getUid().toString());
            if (!StringUtils.isEmpty(lastLogin)) {
                AccountLoginRecord accountLoginRecord = (AccountLoginRecord) JSONToObject(lastLogin,
                        AccountLoginRecord.class);
                queryBlockAccountAche(accountLoginRecord.getDeviceId());
            } else {//去account表查
                String accountDeviceId = accounts.get(0).getDeviceId();
                if (!StringUtils.isEmpty(accountDeviceId)) {
                    queryBlockAccountAche(accountDeviceId);
                }
            }
        }
    }
    //苹果设备
    public void checkBlockedDevice(String deviceId, String appleUser) {
        if (deviceId != null) {//传进来的设备id不为空
            queryBlockAccountAche(deviceId);
            return;
        }//参数为空需要去查询


        List<Account> accounts = null;
        accounts = accountMapper.selectByAppleUser(appleUser);

        if (!CollectionUtils.isEmpty(accounts)) {
            String lastLogin = jedisService.hget(RedisKey.acc_latest_login.getKey(),
                    accounts.get(0).getUid().toString());
            if (!StringUtils.isEmpty(lastLogin)) {
                AccountLoginRecord accountLoginRecord = (AccountLoginRecord) JSONToObject(lastLogin,
                        AccountLoginRecord.class);
                queryBlockAccountAche(accountLoginRecord.getDeviceId());
            } else {//去account表查
                String accountDeviceId = accounts.get(0).getDeviceId();
                if (!StringUtils.isEmpty(accountDeviceId)) {
                    queryBlockAccountAche(accountDeviceId);
                }
            }
        }
    }

    public void queryBlockAccountAche(String deviceId) {
        List deviceList = new ArrayList();
        String deviceAche = jedisService.hget(RedisKey.block_deivce.getKey(), deviceId);
        if (deviceAche != null) {
            AccountBlock accountBlock = (AccountBlock) JSONToObject(deviceAche, AccountBlock.class);
            if (!StringUtils.isEmpty(accountBlock.getDeviceId())) {
                String status = accountBlock.getBlockStatus().toString();
                Date currentTime = new Date();
                boolean betweenDate = DateTimeUtil.isBetweenDate(currentTime, accountBlock.getBlockStartTime(),
                        accountBlock.getBlockEndTime());
                if (betweenDate) {
                    if ("1".equals(status)) {
                        if (deviceList.contains(deviceId)) {
                            throw new InvalidDeceiveException("用户设备异常，请联系官方客服");
                        }
                        logger.info("设备被封禁,deviceId=" + accountBlock.getDeviceId());
                        deviceList.add(accountBlock.getDeviceId());
                        throw new InvalidDeceiveException("用户设备异常，请联系官方客服");
                    }
                }
            }
        }
    }

    /**
     * 检查版本
     *
     * @param version
     */
    public void checkVersion(String version) {
        if (StringUtils.isEmpty(version)) {
            throw new InvalidVersionException("版本为空，请检查版本号");
        }
    }
}
