package com.juxiao.xchat.manager.external.dingtalk.bo;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chris
 * @Title:
 * @date 2018/10/8
 * @time 09:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DingtalkTextMessageBO implements DingtalkMessageBO {

    private String text;
    private List<String> atMobiles;
    private boolean isAtAll = false;


    /**
     * 返回消息的Json格式字符串
     *
     * @return 消息的Json格式字符串
     */
    @Override
    public String toJsonString() {
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException("text should not be blank");
        }

        Map<String, String> textContent = new HashMap<String, String>();
        textContent.put("content", text);

        Map<String, Object> items = new HashMap<String, Object>();
        items.put("msgtype", "text");
        items.put("text", textContent);

        Map<String, Object> atItems = new HashMap<String, Object>();
        if (atMobiles != null && !atMobiles.isEmpty()) {
            atItems.put("atMobiles", atMobiles);
        }

        if (isAtAll) {
            atItems.put("isAtAll", true);
        }

        items.put("at", atItems);
        return JSON.toJSONString(items);
    }
}
