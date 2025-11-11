package com.erban.main.service.job;

import com.erban.main.mybatismapper.StatWeekListsMapper;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class RefreshGiftWeekListJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(RefreshPersonJob.class);
    @Autowired
    private JedisService jedisService;
    @Autowired
    private StatWeekListsMapper statWeekListMapper;

    private Gson gson = new Gson();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("正在执行清除周贡献榜数据任务。");
        jedisService.hdeleteKey(RedisKey.send_gift_weeklist.getKey());
        logger.info("清除周榜数据成功");
    }
}
