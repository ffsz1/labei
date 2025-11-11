package com.juxiao.xchat.service.api.user.impl;

import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.dao.user.AccountLoginRecordDao;
import com.juxiao.xchat.dao.user.domain.AccountLoginRecordDO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.user.AccountLoginRecordService;
import com.juxiao.xchat.service.api.user.vo.UserVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class AccountLoginRecordServiceImpl implements AccountLoginRecordService {

    @Autowired
    private RedisManager redisManager;
    @Autowired
    private AccountLoginRecordDao accountLoginRecordDao;
    @Autowired
    private UsersManager usersManager;

    @Override
    public void saveRecord(HttpServletRequest request, UserVO user) {
        if (user == null) {
            return;
        }
        // 更新登录的缓存记录
        // 数据库添加一条登录记录
        // 暂时每天记录一次, 使用定时任务定时清理redis中的信息,控制清理周期,改变记录频率;
        String result = redisManager.hget(RedisKey.account_login_record.getKey(), user.getUid().toString());
        if (StringUtils.isBlank(result)) {
            // 账号不为空时记录
            String ip = HttpServletUtils.getRealIp(request);
            Date nowDate = new Date();
            AccountLoginRecordDO record = new AccountLoginRecordDO();
            record.setUid(user.getUid());
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
            record.setPhone(user.getPhone());
            record.setErbanNo(user.getErbanNo());
            // 记录到数据库
            accountLoginRecordDao.insert(record);
            // 记录当前用户登录的时间, 会定时清理这个key
            redisManager.hset(RedisKey.account_login_record.getKey(), record.getUid().toString(), DateTimeUtils.convertDate(nowDate, "yyyy-MM-dd HH:mm:ss"));

            //增加账号的活跃值
            usersManager.increaseUserLiveness(user.getUid(), 10);
        }
    }
}
