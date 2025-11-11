package com.juxiao.xchat.manager.common.user.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateFormatUtils;
import com.juxiao.xchat.dao.user.UserPurseHotRoomRecordDao;
import com.juxiao.xchat.dao.user.domain.UserPurseHotRoomRecordDO;
import com.juxiao.xchat.dao.user.dto.UserPurseHotRoomRecordDTO;
import com.juxiao.xchat.dao.user.dto.UserPurseHotRoomResultDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.user.UserPurseHotRoomManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class UserPurseHotRoomManagerImpl implements UserPurseHotRoomManager {
    @Autowired
    private Gson gson;
    @Autowired
    private UserPurseHotRoomRecordDao hotRoomRecordDao;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private UsersManager usersManager;

    @Override
    public void save(Long uid, Long erbanNo, int goldCost, Date startTime, Date endTime, Date createTime) {
        UserPurseHotRoomRecordDO recordDo = new UserPurseHotRoomRecordDO();
        recordDo.setUid(uid);
        recordDo.setErbanNo(erbanNo.intValue());
        recordDo.setGoldNum(goldCost);
        recordDo.setStartTime(startTime);
        recordDo.setEndTime(endTime);
        recordDo.setCreateTime(createTime);
        hotRoomRecordDao.save(recordDo);

        List<String> list = this.listUserPurseHotRoomRecordId(uid);
        if (CollectionUtils.isEmpty(list)) {
            redisManager.hset(RedisKey.user_purse_hot_room_record_list.getKey(), uid.toString(), String.valueOf(recordDo.getRecordId()));
            return;
        }

        if (list.contains(String.valueOf(recordDo.getRecordId()))) {
            return;
        }

        list.add(String.valueOf(recordDo.getRecordId()));
        redisManager.hset(RedisKey.user_purse_hot_room_record_list.getKey(), uid.toString(), StringUtils.join(list, ","));
    }

    @Override
    public UserPurseHotRoomResultDTO getHotRoomResult(String recordId) {
        String resultStr = redisManager.hget(RedisKey.user_purse_hot_room_record.getKey(), recordId);
        if (StringUtils.isNotBlank(resultStr)) {
            return gson.fromJson(resultStr, UserPurseHotRoomResultDTO.class);
        }

        UserPurseHotRoomRecordDTO recordDto = hotRoomRecordDao.getHotRoomRecord(recordId);
        if (recordDto == null) {
            return null;
        }
        UsersDTO users = usersManager.getUser(recordDto.getUid());

        UserPurseHotRoomResultDTO resultDto = new UserPurseHotRoomResultDTO();
        resultDto.setUserNo(users.getErbanNo());
        resultDto.setRoomNo(recordDto.getErbanNo());
        resultDto.setGoldNum(recordDto.getGoldNum());
        resultDto.setDate(DateFormatUtils.YYYY_MM_DD.date2Str(recordDto.getStartTime()));
        resultDto.setHour(DateFormatUtils.HH_MM.date2Str(recordDto.getStartTime()) + "-" + DateFormatUtils.HH_MM.date2Str(recordDto.getEndTime()));
        resultDto.setCreateTime(recordDto.getCreateTime());
        redisManager.hset(RedisKey.user_purse_hot_room_record.getKey(), recordId, gson.toJson(resultDto));
        return resultDto;
    }

    @Override
    public List<String> listUserPurseHotRoomRecordId(Long uid) {
        String recordIdStr = redisManager.hget(RedisKey.user_purse_hot_room_record_list.getKey(), String.valueOf(uid));
        if (StringUtils.isNotBlank(recordIdStr)) {
            return Lists.newArrayList(recordIdStr.split(","));
        }

        List<String> list = hotRoomRecordDao.listUserHotRoomRecordIds(uid);
        if (list != null) {
            redisManager.hset(RedisKey.user_purse_hot_room_record_list.getKey(), String.valueOf(uid), StringUtils.join(list, ","));
        }

        return list;
    }
}
