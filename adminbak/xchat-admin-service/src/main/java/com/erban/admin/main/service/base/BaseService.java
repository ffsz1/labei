package com.erban.admin.main.service.base;

import com.google.gson.Gson;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class BaseService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected Gson gson = new Gson();

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected JedisService jedisService;
}
