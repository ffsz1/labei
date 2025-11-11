package com.xchat.oauth2.service.service.account;

import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.DateUtil;
import com.xchat.oauth2.service.core.util.StringUtils;
import com.xchat.oauth2.service.http.HttpUitls;
import com.xchat.oauth2.service.infrastructure.myaccountmybatis.AccountLoginRecordMapper;
import com.xchat.oauth2.service.model.Account;
import com.xchat.oauth2.service.model.AccountLoginRecord;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by fxw on 2017/12/28.
 */

/**
 * 增加登录记录
 */
@Service
public class LoginRecordService {
    @Autowired
    private JedisService jedisService;
    @Autowired
    private AccountService accountService;
    @Autowired
    AccountLoginRecordMapper accountLoginRecordMapper;

    public int addAccountLoginRecord(AccountLoginRecord accountLoginRecord) {
        jedisService.hset(RedisKey.account_login_record.getKey(), accountLoginRecord.getUid().toString(), DateUtil.date2Str(new Date(), DateUtil.DateFormat.YYYY_MM_DD_HH_MM_SS));
        return accountLoginRecordMapper.insert(accountLoginRecord);
    }

    // @Transactional(readOnly = true)
    public void saveLoginRecord(HttpServletRequest request, Long uid) {
        if (uid == null) {
            return;
        }
        // 更新登录的缓存记录
        // 数据库添加一条登录记录
        // 暂时每天记录一次, 使用定时任务定时清理redis中的信息,控制清理周期,改变记录频率;
        String result = jedisService.hget(RedisKey.account_login_record.getKey(), uid.toString());
        if (StringUtils.isBlank(result)) {
            Account account = accountService.getAccountByUid(uid);
            if (account != null) {
                // 账号不为空时记录
                String ip = HttpUitls.getRealIpAddress(request);
                Date nowDate = new Date();
                AccountLoginRecord record = new AccountLoginRecord();
                record.setUid(uid);
                record.setCreateTime(nowDate);
                record.setLoginIp(ip);
                record.setDeviceId(request.getParameter("deviceId"));
                record.setAppVersion(request.getParameter("appVersion"));
                record.setIspType(request.getParameter("ispType"));
                record.setModel(request.getParameter("model"));
                record.setOs(request.getParameter("os"));
                record.setOsversion(request.getParameter("osVersion"));
                String loginType = request.getParameter("loginType");
                record.setLoginType(StringUtils.isBlank(loginType) ? 3 : Byte.valueOf(loginType));
                record.setPhone(account.getPhone());
                record.setErbanNo(account.getErbanNo());
                account.setLastLoginTime(nowDate);
                account.setLastLoginIp(ip);

                // 更新登录时间
                // accountService.update(account);
                // 更新到缓存
                // jedisService.hset(RedisKey.acc_latest_login.getKey(), record.getUid().toString(), gson.toJson(record));
                // 记录到数据库
                accountLoginRecordMapper.insertSelective(record);
                // 记录当前用户登录的时间, 会定时清理这个key
                jedisService.hset(RedisKey.account_login_record.getKey(), record.getUid().toString(), DateUtil.date2Str(nowDate, DateUtil.DateFormat.YYYY_MM_DD_HH_MM_SS));
            }
        }
    }

}
