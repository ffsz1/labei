package com.erban.web.task;

import com.google.gson.Gson;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseTask {

    @Autowired
    protected JedisService jedisService;

    protected Gson gson = new Gson();
}
