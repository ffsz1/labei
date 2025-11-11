package com.juxiao.xchat.manager.common.level.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.sysconf.LevelCharmDao;
import com.juxiao.xchat.dao.sysconf.LevelExperienceDao;
import com.juxiao.xchat.dao.sysconf.dto.LevelCharmDTO;
import com.juxiao.xchat.dao.sysconf.dto.LevelExperienceDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.item.GiftRecordManager;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.manager.common.level.vo.LevelVO;
import com.juxiao.xchat.manager.common.user.UsersManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

/**
 * 用户经验等级公共操作
 *
 * @class: LevelExperienceManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/6
 */
@Service
public class LevelManagerImpl implements LevelManager {

    private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("0.0000");
    @Autowired
    private Gson gson;
    @Autowired
    private LevelCharmDao charmDao;
    @Autowired
    private LevelExperienceDao experienceDao;
    @Autowired
    private GiftRecordManager giftRecordManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private GiftRecordManager recordManager;


    /**
     * @see LevelManager#getUserExperienceLevelSeq(Long)
     */
    @Override
    public int getUserExperienceLevelSeq(Long uid) {
        long experience = giftRecordManager.getUserExerpence(uid);
        if (experience <= 0) {
            return 0;
        }

        LevelExperienceDTO experienceDto = experienceDao.getExperienceLevel(experience);
        if (experienceDto == null || experienceDto.getLevelSeq() == null) {
            return 0;
        }

        // FIXME: 如果是50级以上，则先返回50级
//        return experienceDto.getLevelSeq();
        return experienceDto.getLevelSeq() > 50 ? 50 : experienceDto.getLevelSeq();
    }

    /**
     * 获取用户魅力等级
     *
     * @param uid
     * @return
     */
    @Override
    public int getUserCharmLevelSeq(Long uid) {
        long experience = giftRecordManager.getUserCharm(uid);
        if (experience <= 0) {
            return 0;
        }

        LevelCharmDTO charmDto = charmDao.getCharmLevel(experience);
        if (charmDto == null || charmDto.getLevelSeq() == null) {
            return 0;
        }

        return charmDto.getLevelSeq();
    }

    @Override
    public LevelExperienceDTO getLevelExperienceByName(String levelName) {
        String result = redisManager.hget(RedisKey.level_exper_list.getKey(), levelName);
        LevelExperienceDTO experienceDto;
        if (StringUtils.isBlank(result)) {
            experienceDto = experienceDao.getByName(levelName);
            if (experienceDto == null) {
                return null;
            }
            redisManager.hset(RedisKey.level_exper_list.getKey(), levelName, gson.toJson(experienceDto));
        } else {
            experienceDto = gson.fromJson(result, LevelExperienceDTO.class);
        }
        return experienceDto;
    }

    @Override
    public LevelCharmDTO getLevelCharmByName(String levelName) {
        String str = redisManager.hget(RedisKey.level_charm_list.getKey(), levelName);
        if (StringUtils.isBlank(str)) {
            LevelCharmDTO levelCharm = charmDao.getLevelByName(levelName);
            if (levelCharm != null) {
                redisManager.hset(RedisKey.level_charm_list.getKey(), levelName, gson.toJson(levelCharm));
                return levelCharm;
            }
        } else {
            return gson.fromJson(str, LevelCharmDTO.class);
        }
        return null;
    }

    @Override
    public LevelVO getLevelExperience(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        UsersDTO users = usersManager.getUser(uid);
        if (users == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        long experience = recordManager.getUserExerpence(uid);
        //用户对应的等级
        LevelExperienceDTO experienceDto = experienceDao.getExperienceLevel(experience);
        if (experience > 0 && experienceDto != null) {
            LevelVO levelVo = new LevelVO();
            levelVo.setUid(uid);
            levelVo.setAvatar(users.getAvatar());
            // FIXME: 先限制最高50级，等客户端更新去除该逻辑
            if (experienceDto.getLevelSeq() != null && experienceDto.getLevelSeq() > 50) {
                levelVo.setLevel(50);
                levelVo.setLevelName("LV50");
                levelVo.setLeftGoldNum(999999999L - experience);
            } else {
                if(StringUtils.isNotEmpty(experienceDto.getLevelName())){
                    levelVo.setLevel(NumberUtils.toInt(experienceDto.getLevelName().replace("LV", "")));
                }else{
                    levelVo.setLevel(0);
                }
                levelVo.setLevelName(experienceDto.getLevelName());
                levelVo.setLeftGoldNum(experienceDto.getNeedMax() - experience);
            }


            double percent = ((double) experience - experienceDto.getNeedMin()) / (experienceDto.getNeedMax() - experienceDto.getNeedMin());
            levelVo.setLevelPercent(new Double(DOUBLE_FORMAT.format(percent)));
            return levelVo;
        }

        LevelExperienceDTO experienceDTO = this.getLevelExperienceByName("LV1");
        if (experienceDTO == null) {
            throw new WebServiceException(WebServiceCode.LEVEL_EXPERIENCE_NOLEVEL);
        }

        LevelVO levelVo = new LevelVO();
        levelVo.setUid(uid);
        levelVo.setAvatar(users.getAvatar());
        levelVo.setLevelName("LV0");
        levelVo.setLevel(0);
        levelVo.setLeftGoldNum(experienceDTO.getNeedMin() - experience);
        levelVo.setLevelPercent(((double) experience) / experienceDTO.getNeedMin());
        return levelVo;
    }

    @Override
    public LevelVO getLevelCharm(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        UsersDTO users = usersManager.getUser(uid);
        if (users == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        //获取用户魅力值
        long experience = recordManager.getUserCharm(uid);
        LevelCharmDTO charmDto = charmDao.getCharmLevel(experience);
        if (experience > 0 && charmDto != null) {
            LevelVO levelVo = new LevelVO();
            levelVo.setUid(uid);
            levelVo.setAvatar(users.getAvatar());
            levelVo.setLevelName(charmDto.getLevelName());
            if(StringUtils.isNotEmpty(charmDto.getLevelName())){
                levelVo.setLevel(NumberUtils.toInt(charmDto.getLevelName().replace("LV", "")));
            }else{
                levelVo.setLevel(0);
            }
            levelVo.setLeftGoldNum(charmDto.getNeedMax() - experience);
            levelVo.setLevelPercent((experience - charmDto.getNeedMin()) / Double.valueOf(charmDto.getNeedMax() - charmDto.getNeedMin()));
            return levelVo;
        }

        //用户对应的等级
        //魅力值不够评级：0级
        LevelCharmDTO lv1 = this.getLevelCharmByName("LV1");
        if (lv1 == null) {
            throw new WebServiceException(WebServiceCode.LEVEL_EXPERIENCE_NOLEVEL);
        }
        LevelVO levelVo = new LevelVO();
        levelVo.setUid(uid);
        levelVo.setAvatar(users.getAvatar());
        levelVo.setLevelName("LV0");
        levelVo.setLevel(0);
        levelVo.setLeftGoldNum(lv1.getNeedMin() - experience);
        levelVo.setLevelPercent(experience / Double.valueOf(lv1.getNeedMin()));
        return levelVo;
    }
}
