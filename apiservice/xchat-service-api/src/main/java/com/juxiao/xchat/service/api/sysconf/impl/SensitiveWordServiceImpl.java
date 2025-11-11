package com.juxiao.xchat.service.api.sysconf.impl;

import com.google.common.collect.Lists;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.room.RoomSensitiveWordsDao;
import com.juxiao.xchat.dao.room.domain.RoomSensitiveWordsDO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.service.api.sysconf.SensitiveWordService;
import com.juxiao.xchat.service.api.sysconf.enumeration.SensitiveWordEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class SensitiveWordServiceImpl implements SensitiveWordService {
    /**
     * 敏感词正则模板
     */
    private static final String SENSITIVE_WORD_PATTERN = ".*({0}).*";
    @Autowired
    private RedisManager redisManager;
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    private RoomSensitiveWordsDao roomSensitiveWordsDao;

    @Override
    public String regex() {
        // 生成正则表达式
        return MessageFormat.format(SENSITIVE_WORD_PATTERN, getWords(SensitiveWordEnum.chat));
    }

    /**
     * 敏感词列表 默认返回聊天的敏感词
     * @return
     */
    @Override
    public List<String> list() {
        return list(SensitiveWordEnum.chat);
    }

    @Override
    public List<String> list(SensitiveWordEnum type) {
        String words = getWords(type);
        if (StringUtils.isBlank(words)) {
            return Lists.newArrayList();
        }
        // 将字符串以竖线 分割
        String[] arr = words.split("[|]");
        return Lists.asList(arr[0], arr);
    }

    @Override
    public String getWords(SensitiveWordEnum type) {
        // 获取系统配置里的敏感词-聊天的敏感词
        String value;
        if (type == null) {
            return "";
        } else {
            value = redisManager.get(RedisKey.sensitive_words.getKey(type.toString()));
        }
        if (StringUtils.isBlank(value)) {
            // 查询聊天的敏感词
            List<RoomSensitiveWordsDO> list = roomSensitiveWordsDao.listByType(type.getType());
            // jdbcTemplate.query("SELECT * from room_sensitive_words where type = 2", new BeanPropertyRowMapper<>(RoomSensitiveWordsDO.class));
            if (list == null || list.isEmpty()) {
                return "fuck";
            }
            StringBuilder sb = new StringBuilder();
            list.forEach((word) -> {
                sb.append(word.getSensitiveWords()).append("|");
            });
            value = sb.toString();
            value = value.substring(0, value.length() - 1);
            redisManager.set(RedisKey.sensitive_words.getKey(type.toString()), value);
        }
        return value;
    }
}
