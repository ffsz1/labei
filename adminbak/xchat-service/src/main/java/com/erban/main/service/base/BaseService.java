package com.erban.main.service.base;

import com.erban.main.service.common.JedisLockService;
import com.google.gson.Gson;
import com.xchat.common.utils.GsonUtil;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BaseService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected Gson gson = new Gson();
    /**
     * date类型转成long类型
     */
    protected Gson gsonDefine = GsonUtil.getGson();
    @Autowired
    protected JedisService jedisService;
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    protected JedisLockService jedisLockService;

}
