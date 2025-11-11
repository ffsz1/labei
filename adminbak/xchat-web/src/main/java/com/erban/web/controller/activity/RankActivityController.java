package com.erban.web.controller.activity;

import com.erban.main.service.activity.HalloweenActivityService;
import com.erban.main.service.activity.RankActivityService;
import com.erban.web.common.BaseController;
import com.google.common.collect.Maps;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.constant.ActivityConf;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 排行榜活动
 */
@Controller
@RequestMapping("/rank/act")
public class RankActivityController extends BaseController {

    @Autowired
    private HalloweenActivityService halloweenActivityService;
    @Autowired
    private RankActivityService rankActivityService;

    /**
     * 获取活动排行榜
     *
     * @param gameType 1淘汰赛，2半决赛，3总决赛
     * @param rankType 1富豪榜，2明星榜，3房间榜
     * @return
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public BusiResult getRankList(int gameType, int rankType) {
        int curGame = ActivityConf.TYPE_TTS;      // 当前进行的赛事
        long curTime = System.currentTimeMillis();
        String startStr = jedisService.hget(RedisKey.activity_conf.getKey(), ActivityConf.ZJ_RANK_START);
        long start = DateTimeUtil.convertStrToDate(startStr).getTime();
        if (curTime < start) {
            startStr = jedisService.hget(RedisKey.activity_conf.getKey(), ActivityConf.BJ_RANK_START);
            start = DateTimeUtil.convertStrToDate(startStr).getTime();
            if (curTime >= start) {
                curGame = ActivityConf.TYPE_BJS;
            }
        } else {
            curGame =  ActivityConf.TYPE_ZJS;
        }
        Map<String, Object> result = Maps.newHashMap();
        result.put("curGame", curGame);
        result.put("list", rankActivityService.getRankFromCache(gameType,rankType));
        return new BusiResult(BusiStatus.SUCCESS, result);
    }


    @RequestMapping(value = "/refresh")
    @ResponseBody
    public BusiResult doHalloweenActRank() {
        halloweenActivityService.doHalloweenActRank();
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        return busiResult;
    }
}
