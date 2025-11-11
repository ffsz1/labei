package com.juxiao.xchat.manager.common.user.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.base.utils.HttpUtils;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.domain.AccountBlock;
import com.juxiao.xchat.dao.user.domain.AccountLoginRecord;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.user.AccountManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.common.user.vo.QQUserInfoVO;
import com.juxiao.xchat.manager.common.user.vo.WXErrorVO;
import com.juxiao.xchat.manager.common.user.vo.WXUserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
 * @class: AccountManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/22
 */
@Slf4j
@Service
public class AccountManagerImpl implements AccountManager {

    public static final String WX_GET_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo";
//    public static final String IOS_QQ_APP_ID = "1108246537";
//    public static final String ANDROID_QQ_APP_ID = "1108246537";
    public static final String IOS_QQ_APP_ID = "101752908";
    public static final String ANDROID_QQ_APP_ID = "101752908";
    public static final String QQ_GET_USER_INFO_URL = "https://graph.qq.com/user/get_user_info";
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Gson gson;
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private RoomManager roomManager;

    @Override
    public void block(Long uid, Integer adminId, Integer day, Integer blockType) throws Exception {
        if (uid == null) {
            return;
        }
        UsersDTO users = usersManager.getUser(uid);
        if (users == null) {
            return;
        }
        // 查询最后登录的信息
        String login = redisManager.hget(RedisKey.acc_latest_login.getKey(), uid.toString());
        AccountLoginRecord accountLoginRecord;
        String device;
        String ip = "";
        if (!StringUtils.isEmpty(login)) {
            accountLoginRecord = gson.fromJson(login, AccountLoginRecord.class);
            device = accountLoginRecord.getDeviceId();
            ip = accountLoginRecord.getLoginIp();
        } else {
            device = users.getDeviceId();
        }

        AccountBlock account = new AccountBlock();
        account.setBlockDesc("一键封禁");
        account.setAdminId(adminId);
        account.setBlockIp(ip);
        account.setBlockStatus(new Byte("1"));
        account.setDeviceId(device);
        account.setErbanNo(users.getErbanNo());
        account.setBlockStartTime(new Date());
        account.setCreateTime(new Date());
        account.setPhone(users.getPhone());
        // 设置封禁时间
        Date endDate = DateTimeUtils.addDay(new Date(), day);
        account.setBlockEndTime(endDate);
        account.setBlockMinute((int) (endDate.getTime() - System.currentTimeMillis()) * 60);
        account.setBlockType(blockType.byteValue());
        if (blockType == 0) {
            // 表示一键封禁
            redisManager.hset(RedisKey.block_account.getKey(), account.getErbanNo().toString(), gson.toJson(account));
            redisManager.hset(RedisKey.block_deivce.getKey(), device, gson.toJson(account));
            redisManager.hset(RedisKey.block_ip.getKey(), account.getBlockIp(), gson.toJson(account));
        } else if (blockType == 1) {
            // 封禁用户账号
            redisManager.hset(RedisKey.block_account.getKey(), account.getErbanNo().toString(), gson.toJson(account));
        } else if (blockType == 2) {
            // 封禁用户设备号
            redisManager.hset(RedisKey.block_deivce.getKey(), device, gson.toJson(account));
        } else if (blockType == 3) {
            // 封禁用户最后一次登录的IP
            redisManager.hset(RedisKey.block_ip.getKey(), account.getBlockIp(), gson.toJson(account));
        }
        blockAccount(uid);
        roomManager.close(uid);
    }

    @Override
    public void blockAccount(Long uid) throws Exception {
        String accid = String.valueOf(uid);
        //调用云信封禁账号功能，主要用于踢用户下线
//        neteaseManager.block(accid, "true");
//        neteaseManager.unblock(accid);
    }

    @Override
    public void validateThirdInfo(String openId, String accessToken, int type, String app,String os) throws WebServiceException {
        String typeStr = type == 2 ? "QQ" : type == 1 ? "微信" : String.valueOf(type);
        if (StringUtils.isEmpty(openId)) {
            log.error("[第三方校验] openid 为空 type:{}", typeStr);
            throw new WebServiceException("第三方授权失败");
        }
        if (StringUtils.isEmpty(accessToken)) {
            log.error("[第三方校验] token 为空 type:{}, openid:{}", typeStr, openId);
            throw new WebServiceException("第三方授权失败");
        }
        log.info("[第三方校验] 获取用户信息: type:{} openid:{}, access_token:{}", typeStr, openId, accessToken);
        switch (type) {
            case 1:
                WXUserInfoVO wxUserInfo = getWXUserInfo(accessToken, openId);
                if (wxUserInfo == null) {
                    log.error("[微信登录] 信息获取为空 openid:{}, access_token:{}", openId, accessToken);
                    throw new WebServiceException("微信授权失败");
                }
                log.info("[微信登录成功]");
                break;
            case 2:
                QQUserInfoVO vo = getQQUseInfo(accessToken, openId, app,os);
                if (vo == null) {
                    log.error("[QQ登录] 获取信息失败 openid:{}, access_token:{}", openId, accessToken);
                    throw new WebServiceException("QQ授权失败");
                }
                log.info("[QQ登录成功]");
                break;
            default:
                log.error("[第三方校验] type:{}, openid:{}, access_token:{}", type, openId, accessToken);
                throw new WebServiceException("第三方授权失败");
        }
    }


    /**
     * 获取微信用户信息
     *
     */
    @Override
    public  WXUserInfoVO getWXUserInfo(String accessToken, String openId) throws WebServiceException {
        StringBuilder sb = new StringBuilder();
        sb.append("access_token=").append(accessToken);
        sb.append("&openid=").append(openId);
        String result = null;
        try {
            result = HttpUtils.get(WX_GET_USER_INFO_URL, sb.toString(), null);
        } catch (IOException e) {
            throw new WebServiceException("获取微信信息失败");
        }
        log.info("[微信登录] 获取用户信息: result:{}", result);
        if (org.springframework.util.StringUtils.isEmpty(result)) {
            //
            return null;
        }
        WXErrorVO errorVO = gson.fromJson(result, WXErrorVO.class);
        if (errorVO != null && !org.springframework.util.StringUtils.isEmpty(errorVO.getErrcode())) {
            // 微信接口调用失败
            return null;
        }
        WXUserInfoVO vo = gson.fromJson(result, WXUserInfoVO.class);

        return vo;
    }

    /**
     * 获取QQ用户信息
     *
     */
    private QQUserInfoVO getQQUseInfo(String accessToken, String openId, String app,String os) throws WebServiceException {
        StringBuilder sb = new StringBuilder();
        sb.append("access_token=").append(accessToken);
        if("iOS".equalsIgnoreCase(os)){
            sb.append("&oauth_consumer_key=").append(IOS_QQ_APP_ID);
        }else{
            sb.append("&oauth_consumer_key=").append(ANDROID_QQ_APP_ID);
        }
        sb.append("&openid=").append(openId);
        String result = null;
        try {
            result = HttpUtils.get(QQ_GET_USER_INFO_URL, sb.toString(), null);
        } catch (IOException e) {
            throw new WebServiceException("获取QQ信息失败");
        }
        log.info("[QQ登录] result:{}", result);
        if (org.springframework.util.StringUtils.isEmpty(result)) {
            return null;
        }
        QQUserInfoVO vo = gson.fromJson(result, QQUserInfoVO.class);
        if ("0".equals(vo.getRet())) {
            return vo;
        }
        return null;
    }
}
