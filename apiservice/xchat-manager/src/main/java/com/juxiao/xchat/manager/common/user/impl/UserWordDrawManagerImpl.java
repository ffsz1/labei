package com.juxiao.xchat.manager.common.user.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.user.UserWordDrawDao;
import com.juxiao.xchat.dao.user.UserWordDrawOverviewDao;
import com.juxiao.xchat.dao.user.UserWordDrawPrizeConfDao;
import com.juxiao.xchat.dao.user.domain.UserWordDrawDO;
import com.juxiao.xchat.dao.user.domain.UserWordDrawOverviewDO;
import com.juxiao.xchat.dao.user.dto.UserWordDrawDTO;
import com.juxiao.xchat.dao.user.dto.UserWordDrawOverviewDTO;
import com.juxiao.xchat.dao.user.dto.UserWordDrawPrizeConfDTO;
import com.juxiao.xchat.dao.user.enumeration.UserWordDrawWordType;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.user.UserWordDrawManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserWordDrawManagerImpl implements UserWordDrawManager {

    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Gson gson;
    @Autowired
    private UserWordDrawDao userWordDrawDao;
    @Autowired
    private UserWordDrawPrizeConfDao userWordDrawPrizeConfDao;
    @Autowired
    private UserWordDrawOverviewDao userWordDrawOverviewDao;

    @Override
    public UserWordDrawOverviewDTO getUserWordDrawOverview(Long userId, Integer activityType){
        String key = "userId:" + userId + "_activityType:" + activityType;
        String listStr = redisManager.hget(RedisKey.user_word_draw_overview.getKey(), key);
        if(StringUtils.isNotBlank(listStr)){
            try {
                return gson.fromJson(listStr, UserWordDrawOverviewDTO.class);
            }catch (Exception e){
                redisManager.hdel(RedisKey.user_word_draw_overview.getKey(), key);
            }
        }
        UserWordDrawOverviewDTO result =  userWordDrawOverviewDao.queryByUidActivityType(userId, activityType);
        if(result == null){

            result = new UserWordDrawOverviewDTO();

            UserWordDrawOverviewDO overviewDO = new UserWordDrawOverviewDO();
            overviewDO.setUid(userId);
            overviewDO.setActivityType(activityType);
            overviewDO.setLeftDrawNum(0);
            overviewDO.setTotalDrawNum(0);
            overviewDO.setWinDrawNum(0);
            overviewDO.setCreateTime(new Date());
            overviewDO.setUpdateTime(new Date());
            userWordDrawOverviewDao.insert(overviewDO);
            BeanUtils.copyProperties(overviewDO, result);
        }
        redisManager.hset(RedisKey.user_word_draw_overview.getKey(), key, gson.toJson(result));
        return result;
    }


    public void saveUserWordDrawOverview(UserWordDrawOverviewDO overviewDO){
        if(overviewDO.getId() != null){
            userWordDrawOverviewDao.save(overviewDO);
        }else{
            userWordDrawOverviewDao.insert(overviewDO);
        }
        String key = "userId:" + overviewDO.getUid() + "_activityType:" + overviewDO.getActivityType();
        redisManager.hdel(RedisKey.user_word_draw_overview.getKey(), key);
    }


    @Override
    public List<UserWordDrawDTO> getUnUsedWordList(Long userId, Integer activityType) {
        String key = "userId:" + userId + "_activityType:" + activityType;
        String listStr = redisManager.hget(RedisKey.user_word_draw_list.getKey(), key);
        if(StringUtils.isNotBlank(listStr)){
            try {
                return gson.fromJson(listStr, new TypeToken<List<UserWordDrawDTO>>(){}.getType());
            }catch (Exception e){
                redisManager.hdel(RedisKey.user_word_draw_list.getKey(), key);
            }
        }
        List<UserWordDrawDTO> list =  userWordDrawDao.queryListBy(userId, activityType, false);
        if(list != null){
            redisManager.hset(RedisKey.user_word_draw_list.getKey(), key, gson.toJson(list));
        }
        return list;
    }

    public void updateUserWordStatus(Long uid, Integer activityType, Boolean isUse){
        userWordDrawDao.updateIsUseByUidActivityType(uid, activityType, isUse, new Date());
        String key = "userId:" + uid + "_activityType:" + activityType;
        redisManager.hdel(RedisKey.user_word_draw_list.getKey(), key);
    }


    public void saveDrawWord(Long uid, UserWordDrawWordType wordType){
        UserWordDrawDTO drawDTO = userWordDrawDao.queryByWord(uid, wordType.getActivityType().getType(), wordType.getWord());
        if(drawDTO == null){
            UserWordDrawDO drawDO = new UserWordDrawDO();
            drawDO.setUid(uid);
            drawDO.setActivityType(wordType.getActivityType().getType());
            drawDO.setWord(wordType.getWord());
            drawDO.setIsUse(false);
            drawDO.setCreateTime(new Date());
            drawDO.setUpdateTime(new Date());
            userWordDrawDao.insert(drawDO);

        }else if(drawDTO != null && drawDTO.getIsUse()){
            userWordDrawDao.updateIsUseById(drawDTO.getUserWordDrawId(), false, new Date());
        }
        String key = "userId:" + uid + "_activityType:" + wordType.getActivityType().getType();
        redisManager.hdel(RedisKey.user_word_draw_list.getKey(), key);
    }


    @Override
    public List<UserWordDrawPrizeConfDTO> getPrizeConfList(Integer activityType) {

        String listStr = redisManager.get(RedisKey.user_word_draw_conf.getKey(activityType.toString()));
        if(StringUtils.isNotBlank(listStr)){
            try {
                return gson.fromJson(listStr, new TypeToken<List<UserWordDrawPrizeConfDTO>>(){}.getType());
            }catch (Exception e){
                redisManager.del(RedisKey.user_word_draw_conf.getKey(activityType.toString()));
            }
        }
        List<UserWordDrawPrizeConfDTO> list =  userWordDrawPrizeConfDao.queryByActivityType(activityType);
        if(list != null){
            redisManager.set(RedisKey.user_word_draw_conf.getKey(activityType.toString()), gson.toJson(list));
        }
        return list;
    }




}
