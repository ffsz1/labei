package com.juxiao.xchat.service.task.event.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.event.PopularityListDAO;
import com.juxiao.xchat.dao.event.dto.PopularityListDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.draw.conf.GiftDrawConf;
import com.juxiao.xchat.service.task.event.TaskPopularityListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TaskPopularityListServiceImpl implements TaskPopularityListService {
    private Logger logger = LoggerFactory.getLogger(TaskPopularityListService.class);

    @Resource
    private GiftDrawConf giftDrawConf;

    @Resource
    private RedisManager redisManager;

    @Resource
    private PopularityListDAO popularityListDAO;

    private Gson gson = new Gson();

    @Override
    public void top20List() {
        // 缓存女神榜
        List<PopularityListDTO> girlsList = popularityListDAO.queryTop20List(giftDrawConf.getTicketGiftId(), 2);
        if (girlsList != null && girlsList.size() > 0) {
            redisManager.set(RedisKey.popularity_list.getKey("girls"), gson.toJson(girlsList));
        } else {
            redisManager.set(RedisKey.popularity_list.getKey("girls"), "[]");
        }

        // 缓存男神榜
        List<PopularityListDTO> boysList = popularityListDAO.queryTop20List(giftDrawConf.getTicketGiftId(), 1);
        if (boysList != null && boysList.size() > 0) {
            redisManager.set(RedisKey.popularity_list.getKey("boys"), gson.toJson(boysList));
        } else {
            redisManager.set(RedisKey.popularity_list.getKey("boys"), "[]");
        }
    }

    @Override
    public void lastWeekRank() {
        // 缓存上周女神榜
        List<PopularityListDTO> girlsList = popularityListDAO.queryLastWeekRank(giftDrawConf.getTicketGiftId(), 2);
        for (int i = 0; i < girlsList.size(); i++) {
            List<PopularityListDTO> popularityListDTO = popularityListDAO.queryUserRecommendLastWeek(giftDrawConf.getTicketGiftId(), girlsList.get(i).getReceiptId());
            logger.info("[ 上周女神推荐人 ] {}", JSON.toJSONString(popularityListDTO));
            if (popularityListDTO.size() > 0) {
                girlsList.get(i).setSendNick(popularityListDTO.get(0).getNick());
                girlsList.get(i).setSendVotes(popularityListDTO.get(0).getSendVotes());
            } else {
                girlsList.get(i).setSendNick("");
                girlsList.get(i).setSendVotes(0);
            }
            logger.info("[ 上周男神榜 ] {}", JSON.toJSONString(girlsList));
        }
        if (girlsList != null && girlsList.size() > 0) {
            redisManager.set(RedisKey.popularity_list_last_week.getKey("girls"), gson.toJson(girlsList));
        } else {
            redisManager.set(RedisKey.popularity_list_last_week.getKey("girls"), "[]");
        }

        // 缓存上周男神榜
        List<PopularityListDTO> boysList = popularityListDAO.queryLastWeekRank(giftDrawConf.getTicketGiftId(), 1);
        for (int i = 0; i < boysList.size(); i++) {
            List<PopularityListDTO> popularityListDTO = popularityListDAO.queryUserRecommendLastWeek(giftDrawConf.getTicketGiftId(), boysList.get(i).getReceiptId());
            logger.info("[ 上周男神推荐人 ] {}", JSON.toJSONString(popularityListDTO));
            if (popularityListDTO.size() > 0) {
                boysList.get(i).setSendNick(popularityListDTO.get(0).getNick());
                boysList.get(i).setSendVotes(popularityListDTO.get(0).getSendVotes());
            } else {
                boysList.get(i).setSendNick("");
                boysList.get(i).setSendVotes(0);
            }
            logger.info("[ 上周男神榜 ] {}", JSON.toJSONString(boysList));
        }
        if (boysList != null && boysList.size() > 0) {
            redisManager.set(RedisKey.popularity_list_last_week.getKey("boys"), gson.toJson(boysList));
        } else {
            redisManager.set(RedisKey.popularity_list_last_week.getKey("boys"), "[]");
        }
    }
}
