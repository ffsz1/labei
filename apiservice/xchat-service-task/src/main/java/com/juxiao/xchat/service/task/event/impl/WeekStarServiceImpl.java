package com.juxiao.xchat.service.task.event.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.event.WeekStarGiftDAO;
import com.juxiao.xchat.dao.event.domain.WeekStarGiftDO;
import com.juxiao.xchat.dao.event.domain.WeekStarItemRewardDO;
import com.juxiao.xchat.dao.event.dto.WeekStarGiftDTO;
import com.juxiao.xchat.dao.event.dto.WeekStarGiftNoticeDTO;
import com.juxiao.xchat.dao.event.dto.WeekStarItemRewardDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.service.task.event.WeekStarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author chris
 * @Title:
 * @date 2019-05-20 16:57
 */
@Service
public class WeekStarServiceImpl implements WeekStarService {
    @Autowired
    private WeekStarGiftDAO weekStarGiftDAO;

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private Gson gson;

    /**
     * 周星礼物
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void weekStartGift() {
        // 查询周星礼物,礼物预告,礼物奖励
        List<WeekStarGiftDTO> weekStarGiftDTOList = weekStarGiftDAO.findWeekStarGift();
        List<WeekStarGiftNoticeDTO> weekStarGiftNoticeDTOS = weekStarGiftDAO.findWeekStarGiftNotice();
        List<WeekStarItemRewardDTO> weekStarItemRewardDTOS = weekStarGiftDAO.findWeekStarItemReward();
        List<WeekStarItemRewardDTO> weekStarItemRewardDTOList = weekStarGiftDAO.findWeekStarItemNoticeReward();
        redisManager.del(RedisKey.week_last_star_gift.getKey());
        if (weekStarGiftDTOList.size() > 0) {
            weekStarGiftDTOList.forEach(item -> {
                weekStarGiftDAO.updateByWeekStarGiftStatus(item.getId());
                redisManager.hdel(RedisKey.week_star_gift.getKey(), item.getGiftId().toString());
                redisManager.hset(RedisKey.week_last_star_gift.getKey(), item.getGiftId().toString(), item.getGiftId().toString());
            });
        }
        if (weekStarGiftNoticeDTOS.size() > 0) {
            weekStarGiftNoticeDTOS.forEach(item -> {
                WeekStarGiftDO weekStarGiftDO = new WeekStarGiftDO();
                weekStarGiftDO.setCreateTime(new Date());
                weekStarGiftDO.setGiftId(item.getGiftId());
                weekStarGiftDO.setSeq(item.getSeq());
                weekStarGiftDO.setStatus(1);
                weekStarGiftDAO.save(weekStarGiftDO);
                weekStarGiftDAO.updateByWeekStarGiftNoticeStatus(item.getId());
                redisManager.hdel(RedisKey.week_star_gift_notice.getKey(), item.getGiftId().toString());
            });
        }
        if (weekStarItemRewardDTOS.size() > 0) {
            weekStarItemRewardDTOS.forEach(item -> {
                weekStarGiftDAO.updateByWeekStarItemRewardStatus(item.getId());
                redisManager.hdel(RedisKey.week_star_item_reward.getKey(), item.getItemId().toString());
            });
        }

        if (weekStarItemRewardDTOList.size() > 0) {
            weekStarItemRewardDTOList.forEach(item -> {
                WeekStarItemRewardDO weekStarItemRewardDO = new WeekStarItemRewardDO();
                weekStarItemRewardDO.setAdminId(item.getAdminId());
                weekStarItemRewardDO.setContent(item.getContent());
                weekStarItemRewardDO.setCreateTime(item.getCreateTime());
                weekStarItemRewardDO.setDays(item.getDays());
                weekStarItemRewardDO.setGiftId(item.getGiftId());
                weekStarItemRewardDO.setItemId(item.getItemId());
                weekStarItemRewardDO.setSeq(item.getSeq());
                weekStarItemRewardDO.setStatus(1);
                weekStarItemRewardDO.setType(item.getType());
                int result = weekStarGiftDAO.insert(weekStarItemRewardDO);
                if (result > 0) {
                    weekStarGiftDAO.updateByWeekStarItemNoticeRewardStatus(item.getId());
                    redisManager.hset(RedisKey.week_star_item_reward.getKey(), item.getItemId().toString(), new Gson().toJson(item));
                }

            });
        }


        Set<String> cacheGiftIds = redisManager.keys(RedisKey.week_last_star.getKey("*"));
        if (cacheGiftIds.size() > 0) {
            cacheGiftIds.forEach(item -> {
                redisManager.del(RedisKey.week_last_star.getKey(item));
            });
        }

        List<String> giftIds = getGiftIds();
        Integer pageNum = 1;
        Integer pageSize = 10;
        Integer skip = (pageNum - 1) * pageSize;
        if (giftIds.size() > 0) {
            giftIds.forEach(giftId -> {
                Set<String> stringSet = redisManager.reverseZsetRange(RedisKey.week_star_the_week.getKey(giftId), skip, skip + pageSize - 1);
                if (stringSet != null && stringSet.size() > 0) {
                    for (String str : stringSet) {
                        Double score = redisManager.zscore(RedisKey.week_star_the_week.getKey(giftId), str);
                        redisManager.zadd(RedisKey.week_last_star.getKey(giftId), str, score);
                    }
                }
                redisManager.del(RedisKey.week_star_the_week.getKey(giftId));
            });
        }
    }

    private List<String> getGiftIds() {
        Map<String, String> result = Optional.ofNullable(redisManager.hgetAll(RedisKey.week_last_star_gift.getKey())).orElse(new HashMap<>(16));
        List<String> giftIds = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(result)) {
            for (Map.Entry<String, String> entry : result.entrySet()) {
                giftIds.add(entry.getValue());
            }
        }
        return giftIds;
    }
}
