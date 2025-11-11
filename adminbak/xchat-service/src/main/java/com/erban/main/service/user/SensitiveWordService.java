package com.erban.main.service.user;

import com.erban.main.model.RoomSensitiveWords;
import com.erban.main.util.StringUtils;
import com.google.common.collect.Lists;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class SensitiveWordService {

    /**
     * 敏感词正则模板
     */
    private static final String SENSITIVE_WORD_PATTERN = ".*({0}).*";
    @Autowired
    private JedisService jedisService;
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public String regex() {
        // 生成正则表达式
        return MessageFormat.format(SENSITIVE_WORD_PATTERN, getWords());
    }

    /**
     * 敏感词列表
     * @return
     */
    public List<String> list() {
        String words = getWords();
        if (StringUtils.isBlank(words)) {
            return Lists.newArrayList();
        }
        // 将字符串以竖线 分割
        String[] arr = words.split("[|]");
        return Lists.asList(arr[0], arr);
    }

    /**
     * 获取所有的敏感词
     *
     * @return
     */
    public String getWords() {
        // 获取系统配置里的敏感词-聊天的敏感词
        String value = jedisService.get(RedisKey.sensitive_words.getKey());
        if (StringUtils.isBlank(value)) {
            // 查询聊天的敏感词
            List<RoomSensitiveWords> list = jdbcTemplate.query("SELECT * from room_sensitive_words where type = 2", new BeanPropertyRowMapper<>(RoomSensitiveWords.class));
            if (list == null || list.isEmpty()) {
                return "fuck";
            }
            StringBuilder sb = new StringBuilder();
            list.forEach((word) -> {
                sb.append(word.getSensitiveWords()).append("|");
            });
            value = sb.toString();
            value = value.substring(0, value.length() - 1);
            jedisService.set(RedisKey.sensitive_words.getKey(), value);
        }
        return value;
    }

}
