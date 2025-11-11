package com.juxiao.xchat.service.api.room.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;
import com.juxiao.xchat.dao.user.AccountLoginRecordDao;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.room.vo.RoomVo;
import com.juxiao.xchat.manager.common.sysconf.SysConfManager;
import com.juxiao.xchat.service.api.room.RoomRcmdService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class RoomRcmdServiceImpl implements RoomRcmdService {
    private final Random random = new Random();
    @Autowired
    private Gson gson;
    @Autowired
    private AccountLoginRecordDao loginRecordDao;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private SysConfManager sysConfManager;


    @Override
    public RoomVo getUserRcmdRoom(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        SysConfDTO conf = sysConfManager.getSysConf(SysConfigId.rcmd_room_option);
        if (conf == null || !Boolean.valueOf(conf.getConfigValue())) {
            // 推荐房间功能已关闭
            throw new WebServiceException(WebServiceCode.ROOM_RCMD_OPTION_CLOSE);
        }

        // 判断是否是新用户
        int loginCount = loginRecordDao.countUserLogin(uid);
        if (loginCount > 5) {
            throw new WebServiceException(WebServiceCode.ROOM_RCMD_NOT_NEW);
        }

        // 查询当前是否存在有推荐的时间段
        long poolSize = redisManager.size(RedisKey.rcmd_room_pool.getKey());
        if (poolSize == 0) {
            throw new WebServiceException(WebServiceCode.ROOM_RCMD_POOL_EMPTY);
        }
        // 随机获取一个房间
        int index = random.nextInt((int) poolSize);
        List<String> list = redisManager.range(RedisKey.rcmd_room_pool.getKey(), index, index);
        if (list == null || list.size() == 0 || StringUtils.isBlank(list.get(0))) {
            throw new WebServiceException(WebServiceCode.ROOM_RCMD_NO_INFO);
        }

        RoomVo roomVo = gson.fromJson(list.get(0), RoomVo.class);
        roomVo.setRoomId(roomVo.getUid());
        return roomVo;
    }
}
