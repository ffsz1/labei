package com.erban.web.controller.activity;

import com.erban.main.model.vo.GuessNameRecordDTO;
import com.erban.main.service.activity.GuessNameService;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.StringUtils;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 猜嘉宾的名字
 */
@Controller
@RequestMapping("activity/guessName")
public class GuessNameController {

    @Autowired
    private GuessNameService guessNameService;
    @Autowired
    private JedisService jedisService;

    @RequestMapping("guess")
    @ResponseBody
    public BusiResult guess(String ticket, Long uid, String name) {
        if (StringUtils.isBlank(ticket)) {
            return new BusiResult(BusiStatus.NOAUTHORITY);
        }
        String result = jedisService.hget(RedisKey.uid_ticket.getKey(), uid.toString());
        if (ticket.equals(result)) {
            return guessNameService.guess(uid, name);
        }
        return new BusiResult(BusiStatus.NOAUTHORITY);
    }
}
