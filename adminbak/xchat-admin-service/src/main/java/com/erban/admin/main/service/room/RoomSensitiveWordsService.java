package com.erban.admin.main.service.room;

import com.erban.admin.main.mapper.RoomSensitiveWordsMapper;
import com.erban.admin.main.model.AdminUser;
import com.erban.admin.main.model.RoomSensitiveWords;
import com.erban.admin.main.model.RoomSensitiveWordsExample;
import com.erban.admin.main.service.room.enumeration.SensitiveWordEnum;
import com.erban.admin.main.service.system.AdminUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author laizhilong
 * @Title:
 * @Package com.erban.admin.main.service.room
 * @date 2018/8/9
 * @time 11:19
 */
@Service
public class RoomSensitiveWordsService {
    @Autowired
    private RoomSensitiveWordsMapper roomSensitiveWordsMapper;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private AdminUserService adminUserService;

    public PageInfo<RoomSensitiveWords> getList(Integer pageNumber, Integer pageSize, String sensitiveWrds,
                                                Integer type) {
        RoomSensitiveWordsExample roomSensitiveWordsExample = new RoomSensitiveWordsExample();
        roomSensitiveWordsExample.setOrderByClause("create_time desc");
        RoomSensitiveWordsExample.Criteria criteria = roomSensitiveWordsExample.createCriteria();

        if (!StringUtils.isEmpty(sensitiveWrds)) {
            criteria.andSensitiveWordsLike(sensitiveWrds);
        }

        if (type != null) {
            criteria.andTypeEqualTo(type.byteValue());
        }

        PageHelper.startPage(pageNumber, pageSize);
        List<RoomSensitiveWords> roomSensitiveWords = roomSensitiveWordsMapper.selectByExample(roomSensitiveWordsExample);

        roomSensitiveWords.forEach(item -> {
            AdminUser adminUser = this.adminUserService.getAdminUserById(item.getAdminId());
            if (adminUser != null) {
                item.setAdminName(adminUser.getUsername());
            } else {
                item.setAdminName("未知");
            }
        });

        return new PageInfo<>(roomSensitiveWords);
    }

    public int saveRoomSensitiveWords(RoomSensitiveWords roomSensitiveWords) {
        SensitiveWordEnum[] sw = SensitiveWordEnum.values();
        for (int i = 0; i < sw.length; i++) {
            jedisService.del(RedisKey.sensitive_words.getKey(sw[i].toString()));
        }
        roomSensitiveWords.setCreateTime(new Date());
        return roomSensitiveWordsMapper.insert(roomSensitiveWords);
    }

    public int deleteRoomSensitiveWords(Integer id) {
        SensitiveWordEnum[] sw = SensitiveWordEnum.values();
        for (int i = 0; i < sw.length; i++) {
            jedisService.del(RedisKey.sensitive_words.getKey(sw[i].toString()));
        }
        return roomSensitiveWordsMapper.deleteByPrimaryKey(id);
    }

    public int batchSave(List<RoomSensitiveWords> roomSensitiveWordsList) {
        SensitiveWordEnum[] sw = SensitiveWordEnum.values();
        for (int i = 0; i < sw.length; i++) {
            jedisService.del(RedisKey.sensitive_words.getKey(sw[i].toString()));
        }
        int result = 1;
        roomSensitiveWordsList.forEach((RoomSensitiveWords item) -> {
            roomSensitiveWordsMapper.insert(item);
        });
        return result;
    }
}
