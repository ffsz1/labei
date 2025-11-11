package com.erban.admin.main.service.dingtalk.bo;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chris
 * @Title:
 * @date 2018/10/8
 * @time 09:58
 */
public class DingtalkTextMessageBO implements DingtalkMessageBO {

    private String text;
    private List<String> atMobiles;
    private boolean isAtAll = false;

    public DingtalkTextMessageBO() {
    }

    public DingtalkTextMessageBO(String text, List<String> atMobiles, boolean isAtAll) {
        this.text = text;
        this.atMobiles = atMobiles;
        this.isAtAll = isAtAll;
    }

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getAtMobiles() {
        return atMobiles;
    }

    public void setAtMobiles(List<String> atMobiles) {
        this.atMobiles = atMobiles;
    }

    public boolean isAtAll() {
        return isAtAll;
    }

    public void setAtAll(boolean atAll) {
        isAtAll = atAll;
    }
}
