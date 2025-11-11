package com.juxiao.xchat.manager.common.user.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.event.OneDayRoomRecvSumDAO;
import com.juxiao.xchat.dao.user.*;
import com.juxiao.xchat.dao.user.domain.UserSettingDO;
import com.juxiao.xchat.dao.user.domain.UsersDO;
import com.juxiao.xchat.dao.user.dto.UserExtendDTO;
import com.juxiao.xchat.dao.user.dto.UserSettingDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.common.user.vo.UserSimpleVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @class: UsersManagerImpl.java
 * @author: chenjunsheng
 * @date 2018/6/7
 */
@Service
public class UsersManagerImpl implements UsersManager {
    @Autowired
    private FansDao fansDao;
    @Autowired
    private UsersDao usersDao;
    @Autowired
    private UserSettingDao settingDao;
    @Autowired
    private UserExtendDao userExtendDao;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Gson gson;
    @Autowired
    private OneDayRoomRecvSumDAO oneDayRoomRecvSumDAO;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private LevelManager levelManager;

    /**
     * @see com.juxiao.xchat.manager.common.user.UsersManager#updateFollowNum(Long)
     */
    @Override
    public void updateFollowNum(Long uid) {
        UsersDTO usersDto = this.getUser(uid);
        if (usersDto == null) {
            return;
        }

        int followCount = fansDao.countUserFollow(uid);
        if (followCount < 0) {
            followCount = 0;
        }

        UsersDO usersDo = new UsersDO();
        usersDo.setUid(uid);
        usersDo.setFollowNum(followCount);
        usersDao.update(usersDo);

        usersDto.setFollowNum(followCount);
        redisManager.hset(RedisKey.user.getKey(), String.valueOf(uid), gson.toJson(usersDto));
    }

    /**
     * @see com.juxiao.xchat.manager.common.user.UsersManager#updateFansNum(Long)
     */
    @Override
    public void updateFansNum(Long uid) {
        UsersDTO usersDto = this.getUser(uid);
        if (usersDto == null) {
            return;
        }

        int fansCount = fansDao.countUserFans(uid);
        if (fansCount < 0) {
            fansCount = 0;
        }

        UsersDO usersDo = new UsersDO();
        usersDo.setUid(uid);
        usersDo.setFansNum(fansCount);
        usersDao.update(usersDo);

        usersDto.setFansNum(fansCount);
        redisManager.hset(RedisKey.user.getKey(), String.valueOf(uid), gson.toJson(usersDto));
    }

    /**
     * @see com.juxiao.xchat.manager.common.user.UsersManager#getUser(Long)
     */
    @Override
    public UsersDTO getUser(Long uid) {
        String jsuser = redisManager.hget(RedisKey.user.getKey(), String.valueOf(uid));
        if (StringUtils.isNotBlank(jsuser)) {
            return gson.fromJson(jsuser, UsersDTO.class);
        }

        UsersDTO user = usersDao.getUser(uid);
        if (user != null) {
            redisManager.hset(RedisKey.user.getKey(), String.valueOf(uid), gson.toJson(user));
        }
        return user;
    }

    /**
     * @see com.juxiao.xchat.manager.common.user.UsersManager#getUserByPhone(String)
     */
    @Override
    public UsersDTO getUserByPhone(String phone) {
        String uidStr = redisManager.hget(RedisKey.phone_uid.getKey(), phone);
        if (StringUtils.isNumeric(uidStr)) {
            return this.getUser(Long.valueOf(uidStr));
        }

        Long uid = usersDao.getUidByPhone(phone);
        if (uid == null) {
            return null;
        }

        redisManager.hset(RedisKey.phone_uid.getKey(), phone, String.valueOf(uid));
        return this.getUser(uid);
    }

    @Override
    public List<UsersDTO> getUserByUserPhone(String phone) {
        List<UsersDO> usersDOS = usersDao.getUserByPhone(phone);
        if (usersDOS == null) {
            return null;
        }
        List<UsersDTO> usersDTOS = new ArrayList<>();
        usersDOS.forEach(item -> {
            UsersDTO usersDTO = new UsersDTO();
            BeanUtils.copyProperties(item, usersDTO);
            usersDTOS.add(usersDTO);
        });
        return usersDTOS;
    }

    /**
     * @see com.juxiao.xchat.manager.common.user.UsersManager#getUserByErbanNo(Long)
     */
    @Override
    public UsersDTO getUserByErbanNo(Long erbanNo) {
        String uidStr = redisManager.hget(RedisKey.erban_no_uid.getKey(), String.valueOf(erbanNo));
        if (StringUtils.isNumeric(uidStr)) {
            return this.getUser(Long.valueOf(uidStr));
        }

        Long uid = usersDao.getUidByErbanNo(erbanNo);
        if (uid == null) {
            return null;
        }

        redisManager.hset(RedisKey.erban_no_uid.getKey(), String.valueOf(erbanNo), String.valueOf(uid));
        return this.getUser(uid);
    }

    @Override
    public UserSettingDTO getUserSetting(Long uid) {
        String setting = redisManager.hget(RedisKey.user_setting.getKey(), String.valueOf(uid));
        if (StringUtils.isNotBlank(setting)) {
            try {
                return gson.fromJson(setting, UserSettingDTO.class);
            } catch (Exception e) {
                redisManager.hdel(RedisKey.user_setting.getKey(), String.valueOf(uid));
            }
        }

        UserSettingDTO settingDto = settingDao.getUserSetting(uid);
        if (settingDto == null) {
            UserSettingDO settingDo = new UserSettingDO();
            settingDo.setUid(uid);
            settingDo.setLikedSend((byte) 1);
            settingDo.setCreateTime(new Date());
            settingDao.save(settingDo);

            settingDto = new UserSettingDTO();
            BeanUtils.copyProperties(settingDo, settingDto);
        }
        redisManager.hset(RedisKey.user_setting.getKey(), String.valueOf(uid), gson.toJson(settingDto));
        return settingDto;
    }

    @Override
    public void increaseUserLiveness(Long uid, Integer incNum) {
        UserExtendDTO userExtendDTO = getUserExtend(uid);
        if (userExtendDTO == null) {
            userExtendDTO = new UserExtendDTO();
            userExtendDTO.setUid(uid);
            userExtendDTO.setLiveness(incNum);

            userExtendDao.insert(userExtendDTO);
        } else {
            userExtendDao.increaseLiveness(uid, incNum);
            redisManager.hdel(RedisKey.user_extend.getKey(), String.valueOf(uid));
        }
    }

    @Override
    public UserExtendDTO getUserExtend(Long uid) {
        String setting = redisManager.hget(RedisKey.user_extend.getKey(), String.valueOf(uid));
        if (StringUtils.isNotBlank(setting)) {
            try {
                return gson.fromJson(setting, UserExtendDTO.class);
            } catch (Exception e) {
                redisManager.hdel(RedisKey.user_extend.getKey(), String.valueOf(uid));
            }
        }
        UserExtendDTO extendDTO = userExtendDao.getByUid(uid);
        if (extendDTO != null) {
            redisManager.hset(RedisKey.user_extend.getKey(), String.valueOf(uid), gson.toJson(extendDTO));
        }
        return extendDTO;
    }

    @Override
    public List<UserSimpleVO> getAuditSpeedMatchUsers() {
        String listStr = redisManager.get(RedisKey.user_audit_speed_match_list.getKey());
        if (StringUtils.isNotEmpty(listStr)) {
            List<UserSimpleVO> list = gson.fromJson(listStr, new TypeToken<List<UserSimpleVO>>() {
            }.getType());
            return list;
        } else {
            final List list = new ArrayList<>();
            List<Long> idList = usersDao.listAuditSpeedMatchUser();

            idList.forEach(id -> {
                UsersDTO u = getUser(id);
                UserSimpleVO simpleVO = new UserSimpleVO();
                simpleVO.setUid(u.getUid());
                simpleVO.setNick(u.getNick());
                simpleVO.setGender(u.getGender());
                simpleVO.setAvatar(u.getAvatar());
                list.add(simpleVO);
            });
            redisManager.set(RedisKey.user_audit_speed_match_list.getKey(), gson.toJson(list), 120, TimeUnit.SECONDS);
            return list;
        }
    }

    @Override
    public List<UserSimpleVO> getCharmSpeedMatchUsers() {
        String listStr = redisManager.get(RedisKey.user_charm_speed_match_list.getKey());
        if (StringUtils.isNotBlank(listStr)) {
            List<UserSimpleVO> list = gson.fromJson(listStr, new TypeToken<List<UserSimpleVO>>() {
            }.getType());
            return list;
        } else {
            List<UserSimpleVO> userSimpleVOS = new ArrayList<>();
            List<Long> idList = oneDayRoomRecvSumDAO.queryLastData(0, 50);
            if (idList != null && idList.size() > 0) {
                idList.forEach(id -> {
                    UsersDTO u = getUser(id);
                    if (u != null) {
                        UserSimpleVO simpleVO = new UserSimpleVO();
                        simpleVO.setUid(u.getUid());
                        simpleVO.setNick(u.getNick());
                        simpleVO.setGender(u.getGender());
                        simpleVO.setAvatar(u.getAvatar());
                        userSimpleVOS.add(simpleVO);
                    }
                });
                redisManager.set(RedisKey.user_charm_speed_match_list.getKey(), gson.toJson(userSimpleVOS), 120, TimeUnit.SECONDS);
                return userSimpleVOS;
            }
            return userSimpleVOS;
        }
    }

    @Override
    public List<UserSimpleVO> getCharmSpeedMatchUsersV2(Integer gender, Integer pageNum) {
        String listStr = redisManager.get(RedisKey.user_charm_speed_match_list.getKey(pageNum + "_" +  gender));
        if (StringUtils.isNotBlank(listStr)) {
            List<UserSimpleVO> list = gson.fromJson(listStr, new TypeToken<List<UserSimpleVO>>() {
            }.getType());
            return list;
        } else {
            List<UserSimpleVO> userSimpleList = new ArrayList<>();
            List<UsersDTO> accountList = accountDao.queryCharmUser(gender, (pageNum - 1) * 50, 50);
            if (accountList != null && accountList.size() > 0) {
                for (int index = 0; index < accountList.size(); index++) {
                    UserSimpleVO simpleVO = new UserSimpleVO();
                    simpleVO.setUid(accountList.get(index).getUid());
                    simpleVO.setNick(accountList.get(index).getNick());
                    simpleVO.setSignature(accountList.get(index).getSignture());
                    simpleVO.setGender(accountList.get(index).getGender());
                    simpleVO.setAvatar(accountList.get(index).getAvatar());
                    simpleVO.setUserVoice(accountList.get(index).getUserVoice());
                    simpleVO.setVoiceDuration(accountList.get(index).getVoiceDura());
                    int glamourLevel = levelManager.getUserCharmLevelSeq(simpleVO.getUid());
                    int fortuneLevel = levelManager.getUserExperienceLevelSeq(simpleVO.getUid());
                    simpleVO.setGlamourLevel(glamourLevel);
                    simpleVO.setFortuneLevel(fortuneLevel);
                    userSimpleList.add(simpleVO);
                };
                redisManager.set(RedisKey.user_charm_speed_match_list.getKey(pageNum + "_" +  gender), gson.toJson(userSimpleList), 120, TimeUnit.SECONDS);
            }
            return userSimpleList;
        }
    }

    @Override
    public Integer getCountByLoginTime(Integer gender) {
        int count = 0;
        String totalCount = redisManager.get(RedisKey.account_last_login_user_count.getKey() + "_" + gender);
        if (StringUtils.isNotBlank(totalCount)) {
            count = gson.fromJson(totalCount, new TypeToken<Integer>() {
            }.getType());
        } else {
            count = accountDao.queryTotalCount(gender);
            redisManager.set(RedisKey.account_last_login_user_count.getKey() + "_" + gender, gson.toJson(count), 120, TimeUnit.SECONDS);
        }
        return count;
    }

    @Override
    public List<Long> getCharmLastChangeUids(Integer size, Long uid) {
        String listStr = redisManager.get(RedisKey.user_charm_last_change_list.getKey());
        if (StringUtils.isNotEmpty(listStr)) {
            List<Long> list = gson.fromJson(listStr, new TypeToken<List<Long>>() {
            }.getType());
            return list;
        } else {
            // 里面的数据可能重复，需要去重
            List<Long> idList = oneDayRoomRecvSumDAO.queryLastsData(0, 10 * size, uid);
            List<Long> list = new ArrayList<>(size);
            int count = 0;
            if (!CollectionUtils.isEmpty(idList)) {
                for (int i = 0; i < idList.size() && count < size; i++) {
                    if (!list.contains(idList.get(i))) {
                        list.add(idList.get(i));
                        count++;
                    }
                }
            }
            redisManager.set(RedisKey.user_charm_last_change_list.getKey(), gson.toJson(list), 120, TimeUnit.SECONDS);
            return list;
        }
    }
}