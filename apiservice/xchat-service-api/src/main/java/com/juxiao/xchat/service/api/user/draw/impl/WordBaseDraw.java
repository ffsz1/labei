package com.juxiao.xchat.service.api.user.draw.impl;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;
import com.juxiao.xchat.dao.user.enumeration.UserWordDrawWordType;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.sysconf.SysConfManager;
import com.juxiao.xchat.service.api.user.draw.WordDrawService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class WordBaseDraw implements WordDrawService {

    @Autowired
    protected RedisManager redisManager;
    @Autowired
    protected SysConfManager sysconfManager;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 名称
     * @return
     */
    protected String getName() {
        return "";
    }

    /**
     * 执行逻辑
     *
     * @param uid 用户
     * @return 返回字体
     */
    protected abstract UserWordDrawWordType draw(Long uid) throws WebServiceException;

    /**
     * 检查状态
     *
     * @return
     */
    protected abstract boolean check(Long uid);



    /**
     * 是否需要加锁
     *
     * @return
     */
    protected String lock(Long uid) {
        return "";
    }

    @Override
    public UserWordDrawWordType doDraw(Long uid) throws WebServiceException {
        String key = lock(uid);
        String lockValue;
        if (StringUtils.isNotBlank(key)) {
            lockValue = redisManager.lock(key, 5 * 1000);
            if (StringUtils.isBlank(lockValue)) {
                // 加锁失败
                return null;
            }
            try {
                if (!check(uid)) {
                    // 没有权限
                    return null;
                }
                return draw(uid);
            } finally {
                redisManager.unlock(key, lockValue);
            }
        }
        if (check(uid)) {
            return draw(uid);
        }
        return null;
    }

}
