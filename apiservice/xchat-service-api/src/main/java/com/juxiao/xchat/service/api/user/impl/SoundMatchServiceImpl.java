package com.juxiao.xchat.service.api.user.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.item.dto.GiftDTO;
import com.juxiao.xchat.dao.sysconf.dto.ChannelDTO;
import com.juxiao.xchat.dao.user.UserLikeSoundDAO;
import com.juxiao.xchat.dao.user.UserSettingDao;
import com.juxiao.xchat.dao.user.domain.UserLikeSoundDO;
import com.juxiao.xchat.dao.user.dto.SoundMatchConfDTO;
import com.juxiao.xchat.dao.user.dto.UserSoundDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.item.GiftManager;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.common.user.vo.UserSimpleVO;
import com.juxiao.xchat.service.api.event.vo.RankParentVo;
import com.juxiao.xchat.service.api.sysconf.ChannelService;
import com.juxiao.xchat.service.api.sysconf.enumeration.ChannelEnum;
import com.juxiao.xchat.service.api.sysconf.vo.ChannelAuditVO;
import com.juxiao.xchat.service.api.sysconf.vo.IndexParam;
import com.juxiao.xchat.service.api.user.SoundMatchService;
import com.juxiao.xchat.service.api.user.conf.SoundMatchConfig;
import com.juxiao.xchat.service.api.user.vo.SoundMatchConfBO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: alwyn
 * @Date: 2018/11/26 16:08
 */
@Service
public class SoundMatchServiceImpl implements SoundMatchService {
    public static final Logger logger = LoggerFactory.getLogger(SoundMatchServiceImpl.class);

    @Autowired
    private GiftManager giftManager;
    @Autowired
    private SoundMatchConfig soundMatchConfig;
    @Autowired
    private UserLikeSoundDAO userLikeSoundDAO;
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private LevelManager levelManager;
    @Autowired
    private UserSettingDao userSettingDao;
    @Autowired
    private Gson gson;
    @Autowired
    private ChannelService channelService;

    @Override
    public List<UserSimpleVO> charmUser(IndexParam indexParam) {
        if ("1001".equals(indexParam.getAppid())) {
            ChannelEnum channelEnum = toEnum(indexParam.getChannel());
            ChannelAuditVO vo = channelService.checkAudit(channelEnum, indexParam.getAppVersion(), indexParam.getUid());
            if (vo != null && vo.isAudit()) {
                return usersManager.getAuditSpeedMatchUsers();
            } else {
                return usersManager.getCharmSpeedMatchUsers();
            }
        } else {
            return usersManager.getCharmSpeedMatchUsers();
        }
    }

    @Override
    public List<UserSimpleVO> charmUserV2(Integer gender) {
        int pageSize = 0;
        gender = gender == 2 ? 1 : 2;
        String redisPageSize = redisManager.get(RedisKey.account_last_login_user_pagesize.getKey() + "_" + gender);
        if (StringUtils.isNotBlank(redisPageSize)) {
            pageSize = Integer.parseInt(redisPageSize);
        } else {
            int totalCount = usersManager.getCountByLoginTime(gender);
            if (totalCount >= 0) {
                pageSize = (int) Math.ceil((double) totalCount / 50);
            }
            redisManager.set(RedisKey.account_last_login_user_pagesize.getKey() + "_" + gender, String.valueOf(pageSize), 120, TimeUnit.SECONDS);
        }
        int count = (int) ((Math.random() * pageSize) + 1);
        return usersManager.getCharmSpeedMatchUsersV2(gender, count);
    }

    /**
     * 随机匹配用户
     *
     * @param uid       用户ID
     * @param gender    性别
     * @param minAge    最小年龄
     * @param maxAge    最大年龄
     * @return
     * @throws WebServiceException
     */
    @Override
    public List<UserSoundDTO> randomUser(Long uid, Integer gender, Integer minAge, Integer maxAge) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        if (gender == null || (gender != 0 && gender != 1 && gender != 2)) {
            logger.error("性别错误, gender:{}", gender);
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        if (minAge == null || minAge < 0) {
            logger.error("最小年龄错误, minAge:{}", minAge);
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        if (maxAge == null || maxAge > 99 || maxAge < minAge) {
            logger.error("最大年龄错误, minAge:{}, maxAge:{}", minAge, maxAge);
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        UsersDTO usersDTO = usersManager.getUser(uid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        SoundMatchConfBO confBO = getConfig(uid);
        if (confBO != null && confBO.getFilterGender() != 0) {
            gender = confBO.getFilterGender() == 1 ? 2 : 1;
        }

        Long size = getUserMatchTotalCount(gender, minAge, maxAge);
        if (size == null || size == 0) {
            return Lists.newArrayList();
        }

        List<UserSoundDTO> list = new ArrayList<>(soundMatchConfig.getRandomPageSize());
        Set<Long> temp = Sets.newHashSet();
        // 用户喜欢的用户
        String result = redisManager.hget(RedisKey.user_like_sound.getKey(), uid.toString());
        Set<Long> likeUsers = Sets.newHashSet();
        if (StringUtils.isNotBlank(result)) {
            likeUsers = gson.fromJson(result, new TypeToken<Set<Long>>() {
            }.getType());
        }

        long index, count = 0;
        UserSoundDTO soundDTO;
        while (temp.size() < soundMatchConfig.getRandomPageSize()) {
            count++;
            if (count > soundMatchConfig.getRandomPageSize() * 3) {
                return list;
            }
            String user = getRandomUser(gender, minAge, maxAge, size.intValue());

            if (StringUtils.isBlank(user)) {
                continue;
            }
            soundDTO = gson.fromJson(user, UserSoundDTO.class);
            if (soundDTO == null) {
                continue;
            }
            // 过滤自己
            if (uid.longValue() == soundDTO.getUid().longValue()) {
                continue;
            }
            // 已经喜欢过该用户
            if (likeUsers.contains(soundDTO.getUid())) {
                continue;
            }
            // 已经存在该用户
            if (!temp.add(soundDTO.getUid())) {
                continue;
            }

            // 设置房间状态
            soundDTO.setOperatorStatus(redisManager.hget(RedisKey.user_in_room.getKey(), soundDTO.getUid().toString()) == null ? (byte) 0 : 1);
            list.add(soundDTO);
        }
        return list;
    }

    /**
     * 获取用户的最大匹配数量
     *
     * @param gender
     * @param minAge
     * @param maxAge
     * @return
     */
    private Long getUserMatchTotalCount(Integer gender, Integer minAge, Integer maxAge) {
        if (gender == 0 && minAge == 0) {
            return redisManager.zcount(RedisKey.user_sound_gender_age_pool.getKey(), -999, maxAge);
        } else if (gender == 0) {
            return redisManager.zcount(RedisKey.user_sound_gender_age_pool.getKey(), minAge, maxAge);
        } else {
            return redisManager.zcount(RedisKey.user_sound_gender_age_pool.getKey(gender.toString()), minAge, maxAge);
        }
    }

    /**
     * 获取随机用户
     *
     * @param gender
     * @param minAge
     * @param maxAge
     * @param totalDataCount
     * @return
     */
    private String getRandomUser(Integer gender, Integer minAge, Integer maxAge, Integer totalDataCount) {

        Integer index = RandomUtils.nextInt(totalDataCount);
        Set<String> set = null;
        if (gender == 0) {

            set = redisManager.zrangeByScore(RedisKey.user_sound_gender_age_pool.getKey(), minAge, maxAge, index, 1);
        } else {
            set = redisManager.zrangeByScore(RedisKey.user_sound_gender_age_pool.getKey(gender.toString()), minAge, maxAge, index, 1);
        }
        if (CollectionUtils.isEmpty(set)) {
            return null;
        } else {
            return set.iterator().next();
        }
    }

    /**
     * 获取随机用户
     *
     * @param gender
     * @param minAge
     * @param maxAge
     * @param totalDataCount
     * @return
     */
    private String getCheckAuditRandomUser(ChannelEnum channelEnum, Integer gender, Integer minAge, Integer maxAge, Integer totalDataCount) {
        ChannelDTO channelDto = channelService.getByName(channelEnum);
        if (channelDto == null) {
            return null;
        }
        List<Long> uidlist = channelService.listByChannelId(channelDto.getId());
        if (uidlist.size() > 0) {
            Long time = System.currentTimeMillis() / 1000L;
            uidlist.forEach(item -> {
                UsersDTO usersDTO = usersManager.getUser(item);
                int age = usersDTO.getBirth() == null ? 0 : (int) ((time - usersDTO.getBirth().getTime()) / (12 * 30 * 24 * 3600L));
                age = age < 0 ? 0 : age;
                String str = gson.toJson(usersDTO);

                redisManager.zadd(RedisKey.user_sound_gender_age_pool.getKey(), str, age);
                if (usersDTO.getGender().intValue() == 1) {
                    redisManager.zadd(RedisKey.user_sound_gender_age_pool.getKey("1"), str, age);
                } else if (usersDTO.getGender().intValue() == 2) {
                    redisManager.zadd(RedisKey.user_sound_gender_age_pool.getKey("2"), str, age);
                }
            });
        }
        Integer index = RandomUtils.nextInt(totalDataCount);
        Set<String> set = null;
        if (gender == 0) {

            set = redisManager.zrangeByScore(RedisKey.check_audit_user_sound_gender_age_pool.getKey(), minAge, maxAge, index, 1);
        } else {
            set = redisManager.zrangeByScore(RedisKey.check_audit_user_sound_gender_age_pool.getKey(gender.toString()), minAge, maxAge, index, 1);
        }
        if (CollectionUtils.isEmpty(set)) {
            return null;
        } else {
            return set.iterator().next();
        }
    }


    @Override
    public boolean likeUser(Long uid, Long likeUid) throws WebServiceException {
        UsersDTO usersDTO = usersManager.getUser(uid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        usersDTO = usersManager.getUser(likeUid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        // 判断等级限制
        int level = levelManager.getUserExperienceLevelSeq(uid);
        if (level < soundMatchConfig.getLeftLevel()) {
            String result = redisManager.hget(RedisKey.user_like_num.getKey(), uid.toString());
            if (StringUtils.isNotBlank(result) && StringUtils.isNumeric(result)) {
                int num = Integer.valueOf(result);
                if (num > soundMatchConfig.getLikeNum()) {
                    throw new WebServiceException(WebServiceCode.LIKE_USER_ENOUGH);
                }
            }
        }
        // 判断是否喜欢过该用户
        String result = redisManager.hget(RedisKey.user_like_sound.getKey(), uid.toString());
        Set<Long> users = Sets.newHashSet();
        if (StringUtils.isNotBlank(result)) {
            users = gson.fromJson(result, new TypeToken<Set<Long>>() {
            }.getType());
        } else {
            List<Long> list = userLikeSoundDAO.listLikeByUid(uid);
            users.addAll(list);
        }
        if (users.add(likeUid)) {
            UserLikeSoundDO likeSoundDO = new UserLikeSoundDO();
            likeSoundDO.setUid(uid);
            likeSoundDO.setLikeUid(likeUid);
            likeSoundDO.setCreateDate(new Date());
            userLikeSoundDAO.save(likeSoundDO);
//            redisManager.hset(RedisKey.user_like_sound.getKey(), uid.toString(), gson.toJson(users));
            redisManager.hincrBy(RedisKey.user_like_num.getKey(), uid.toString(), 1L);
        }
        return true;
    }

    @Override
    public List<GiftDTO> listGift(Long uid) {
        List<GiftDTO> list = Lists.newArrayList();
        soundMatchConfig.getGiftList().forEach(id -> {
            GiftDTO gift = giftManager.getValidGiftById(id);
            if (gift != null) {
                list.add(gift);
            }
        });
        return list;
    }

    @Override
    public SoundMatchConfBO getConfig(Long uid) throws WebServiceException {
        if (uid == null) {
            return new SoundMatchConfBO();
        }
        UsersDTO usersDTO = usersManager.getUser(uid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        SoundMatchConfBO confBO = new SoundMatchConfBO();
        String result = redisManager.hget(RedisKey.user_sound_config.getKey(), uid.toString());
        if (StringUtils.isBlank(result)) {
            SoundMatchConfDTO confDTO = userSettingDao.getSoundConfig(uid);
            if (confDTO == null) {
                confDTO = new SoundMatchConfDTO();
                confDTO.setUid(uid);
                confDTO.setFilterGender(0);
                confDTO.setVoiceHide(false);
                userSettingDao.saveSoundConfig(confDTO);
            }
            BeanUtils.copyProperties(confDTO, confBO);
            redisManager.hset(RedisKey.user_sound_config.getKey(), uid.toString(), gson.toJson(confBO));
            return confBO;
        }
        return gson.fromJson(result, SoundMatchConfBO.class);
    }

    @Override
    public boolean setConfig(SoundMatchConfBO confBO) {
        if (confBO == null) {
            return false;
        }
        SoundMatchConfDTO confDTO = new SoundMatchConfDTO();
        confDTO.setUid(confBO.getUid());
        confDTO.setFilterGender(confBO == null ? 0 : confBO.getFilterGender());
        confDTO.setVoiceHide(confBO.getVoiceHide() == null ? false : confBO.getVoiceHide());
        userSettingDao.saveSoundConfig(confDTO);
        BeanUtils.copyProperties(confDTO, confBO);
        redisManager.hset(RedisKey.user_sound_config.getKey(), confBO.getUid().toString(), gson.toJson(confBO));
        return true;
    }


    @Override
    public List<UserSoundDTO> checkAuditRandomUser(ChannelEnum channelEnum, Long uid, Integer gender, Integer minAge, Integer maxAge) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        if (gender == null || (gender != 0 && gender != 1 && gender != 2)) {
            logger.error("性别错误, gender:{}", gender);
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        if (minAge == null || minAge < 0) {
            logger.error("最小年龄错误, minAge:{}", minAge);
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        if (maxAge == null || maxAge > 99 || maxAge < minAge) {
            logger.error("最大年龄错误, minAge:{}, maxAge:{}", minAge, maxAge);
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        UsersDTO usersDTO = usersManager.getUser(uid);
        if (usersDTO == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        SoundMatchConfBO confBO = getConfig(uid);

        if (confBO != null && confBO.getFilterGender() != 0) {
            gender = confBO.getFilterGender() == 1 ? 2 : 1;

        }
        Long size = getUserMatchTotalCount(gender, minAge, maxAge);
        if (size == null || size == 0) {
            return Lists.newArrayList();
        }
        List<UserSoundDTO> list = new ArrayList<>(soundMatchConfig.getRandomPageSize());
        Set<Long> temp = Sets.newHashSet();
        // 用户喜欢的用户
        String result = redisManager.hget(RedisKey.user_like_sound.getKey(), uid.toString());
        Set<Long> likeUsers = Sets.newHashSet();
        if (StringUtils.isNotBlank(result)) {
            likeUsers = gson.fromJson(result, new TypeToken<Set<Long>>() {
            }.getType());
        }
        long index, count = 0;

        UserSoundDTO soundDTO;
        while (temp.size() < soundMatchConfig.getRandomPageSize()) {
            count++;
            if (count > soundMatchConfig.getRandomPageSize() * 3) {
                return list;
            }
            String user = getCheckAuditRandomUser(channelEnum, gender, minAge, maxAge, size.intValue());

            if (StringUtils.isBlank(user)) {
                continue;
            }
            soundDTO = gson.fromJson(user, UserSoundDTO.class);
            if (soundDTO == null) {
                continue;
            }
            // 过滤自己
            if (uid.longValue() == soundDTO.getUid().longValue()) {
                continue;
            }
            // 已经喜欢过该用户
            if (likeUsers.contains(soundDTO.getUid())) {
                continue;
            }
            // 已经存在该用户
            if (!temp.add(soundDTO.getUid())) {
                continue;
            }
            soundDTO.setOperatorStatus(redisManager.hget(RedisKey.user_in_room.getKey(), soundDTO.getUid().toString()) == null ? (byte) 0 : 1);
            list.add(soundDTO);
        }
        return list;
    }

    /**
     * 将渠道号转换成枚举类
     *
     * @param channel
     * @return
     */
    private ChannelEnum toEnum(String channel) {
        ChannelEnum channelEnum;
        try {
            channelEnum = ChannelEnum.valueOf(channel);
        } catch (Exception e) {
            channelEnum = ChannelEnum.xbd;
        }
        return channelEnum;
    }

}
