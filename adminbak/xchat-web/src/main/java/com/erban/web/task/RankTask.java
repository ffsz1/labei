package com.erban.web.task;

import com.erban.main.model.ActivityConfig;
import com.erban.main.service.activity.RankActivityService;
import com.erban.main.vo.activity.RankActVo;
import com.xchat.common.constant.ActivityConf;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.BlankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class RankTask extends BaseTask {

    private static final Logger logger = LoggerFactory.getLogger(RankTask.class);
    @Autowired
    private RankActivityService rankActivityService;


    /**
     * 缓存活动配置， 5s
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void cacheActivityConfig() {
        List<ActivityConfig> list = rankActivityService.getAllConfig();
        for (ActivityConfig config : list) {
            jedisService.hset(RedisKey.activity_conf.getKey(), config.getAkey(), config.getAval());
        }
        logger.info("cacheActivityConfig finish ===========");
    }

    /**
     * 缓存淘汰赛排行榜，10s
     */
    @Scheduled(cron = " */10 * * * * ?")
    public void cacheRankTTS() {
        Map<String, String> configMap = jedisService.hgetAll(RedisKey.activity_conf.getKey());
        String startTimeStr = configMap.get(ActivityConf.TT_RANK_START);
        String endTimeStr = configMap.get(ActivityConf.TT_RANK_END);
//        long startTime = DateTimeUtil.convertStrToDate(startTimeStr).getTime();
//        long endTime = DateTimeUtil.convertStrToDate(endTimeStr).getTime();
//        long curTime = System.currentTimeMillis();
//        // 判断是否在活动期内
//        if (curTime < startTime || curTime > endTime) {
//            return;
//        }
        int size = Integer.valueOf(configMap.get(ActivityConf.TT_RANK_COUNT));
        String giftIdStr = configMap.get(ActivityConf.ACT_GIFT_LIST);
        List<String> giftIds = null;
        if (!BlankUtil.isBlank(giftIdStr)) {
            giftIds = Arrays.asList(giftIdStr.split(","));
        }

        try {
            // 土豪榜
            List<RankActVo> list1 = rankActivityService.getRankFromDBWapper(ActivityConf.RANK_RICH, giftIds, null
                    , startTimeStr, endTimeStr, size);
            jedisService.hset(RedisKey.activity_rank.getKey(ActivityConf.TYPE_TTS + "")
                    , ActivityConf.RANK_RICH + "", gson.toJson(list1));

            // 明星榜
            List<RankActVo> list2 = rankActivityService.getRankFromDBWapper(ActivityConf.RANK_STAR, giftIds, null
                    , startTimeStr, endTimeStr, size);
            jedisService.hset(RedisKey.activity_rank.getKey(ActivityConf.TYPE_TTS + "")
                    , ActivityConf.RANK_STAR + "", gson.toJson(list2));

            // 房间榜
            List<RankActVo> list3 = rankActivityService.getRankFromDBWapper(ActivityConf.RANK_ROOM, giftIds, null
                    , startTimeStr, endTimeStr, size);
            jedisService.hset(RedisKey.activity_rank.getKey(ActivityConf.TYPE_TTS + "")
                    , ActivityConf.RANK_ROOM + "", gson.toJson(list3));
        } catch (Exception e) {
            logger.error("getRankFromDBWapper error, gameType:" + ActivityConf.TYPE_TTS, e);
        } finally {
            logger.info("cacheRankTTS finish =========");
        }
    }


    /**
     * 缓存半决赛排行榜，10s
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void cacheRankBJS() {
        Map<String, String> configMap = jedisService.hgetAll(RedisKey.activity_conf.getKey());
        String startTimeStr = configMap.get(ActivityConf.BJ_RANK_START);
        String endTimeStr = configMap.get(ActivityConf.BJ_RANK_END);
//        long startTime = DateTimeUtil.convertStrToDate(startTimeStr).getTime();
//        long endTime = DateTimeUtil.convertStrToDate(endTimeStr).getTime();
//        long curTime = System.currentTimeMillis();
//        // 判断是否在活动期内
//        if (curTime < startTime || curTime > endTime) {
//            return;
//        }

        int size = Integer.valueOf(configMap.get(ActivityConf.BJ_RANK_COUNT));
        String giftIdStr = configMap.get(ActivityConf.ACT_GIFT_LIST);
        List<String> giftIds = null;
        if (!BlankUtil.isBlank(giftIdStr)) {
            giftIds = Arrays.asList(giftIdStr.split(","));
        }

        try {
            // 获取前一次比赛的结果
            List<Long> richList = rankActivityService.getWinList(ActivityConf.TYPE_TTS, ActivityConf.RANK_RICH, size);
//            logger.info("+++++giftIds {}, startTimeStr: {}, endTimeStr:{}", giftIds, startTimeStr, endTimeStr);
//            logger.info("+++++richList {}", richList);
            // 土豪榜
            List<RankActVo> list1 = rankActivityService.getRankFromDBWapper(ActivityConf.RANK_RICH, giftIds, richList, startTimeStr
                    , endTimeStr, size);
            jedisService.hset(RedisKey.activity_rank.getKey(ActivityConf.TYPE_BJS + "")
                    , ActivityConf.RANK_RICH + "", gson.toJson(list1));

            // 获取前一次比赛的结果
            List<Long> starList = rankActivityService.getWinList(ActivityConf.TYPE_TTS, ActivityConf.RANK_STAR, size);
            // 明星榜
            List<RankActVo> list2 = rankActivityService.getRankFromDBWapper(ActivityConf.RANK_STAR, giftIds, starList, startTimeStr
                    , endTimeStr, size);
            jedisService.hset(RedisKey.activity_rank.getKey(ActivityConf.TYPE_BJS + "")
                    , ActivityConf.RANK_STAR + "", gson.toJson(list2));

            // 获取前一次比赛的结果
            List<Long> roomList = rankActivityService.getWinList(ActivityConf.TYPE_TTS, ActivityConf.RANK_ROOM, size);
            // 房间榜
            List<RankActVo> list3 = rankActivityService.getRankFromDBWapper(ActivityConf.RANK_ROOM, giftIds, roomList, startTimeStr
                    , endTimeStr, size);
            jedisService.hset(RedisKey.activity_rank.getKey(ActivityConf.TYPE_BJS + "")
                    , ActivityConf.RANK_ROOM + "", gson.toJson(list3));
        } catch (Exception e) {
            logger.error("getRankFromDBWapper error, gameType:" + ActivityConf.TYPE_BJS, e);
        } finally {
            logger.info("cacheRankBJS finish =========");
        }
    }

    /**
     * 缓存总决赛排行榜，10s
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void cacheRankZJS() {
        Map<String, String> configMap = jedisService.hgetAll(RedisKey.activity_conf.getKey());
        String startTimeStr = configMap.get(ActivityConf.ZJ_RANK_START);
        String endTimeStr = configMap.get(ActivityConf.ZJ_RANK_END);
//        long startTime = DateTimeUtil.convertStrToDate(startTimeStr).getTime();
//        long endTime = DateTimeUtil.convertStrToDate(endTimeStr).getTime();
//        long curTime = System.currentTimeMillis();
//        // 判断是否在活动期内
//        if (curTime < startTime || curTime > endTime) {
//            return;
//        }

        int size = Integer.valueOf(configMap.get(ActivityConf.ZJ_RANK_COUNT));
        String giftIdStr = configMap.get(ActivityConf.ACT_GIFT_LIST);
        List<String> giftIds = null;
        if (!BlankUtil.isBlank(giftIdStr)) {
            giftIds = Arrays.asList(giftIdStr.split(","));
        }

        try {
            // 获取前一次比赛的结果
            List<Long> richList = rankActivityService.getWinList(ActivityConf.TYPE_BJS, ActivityConf.RANK_RICH, size);
            // 土豪榜
            List<RankActVo> list1 = rankActivityService.getRankFromDBWapper(ActivityConf.RANK_RICH, giftIds, richList
                    , startTimeStr, endTimeStr, size);
            jedisService.hset(RedisKey.activity_rank.getKey(ActivityConf.TYPE_ZJS + "")
                    , ActivityConf.RANK_RICH + "", gson.toJson(list1));

            // 获取前一次比赛的结果
            List<Long> starList = rankActivityService.getWinList(ActivityConf.TYPE_BJS, ActivityConf.RANK_STAR, size);
            // 明星榜
            List<RankActVo> list2 = rankActivityService.getRankFromDBWapper(ActivityConf.RANK_STAR, giftIds, starList
                    , startTimeStr, endTimeStr, size);
            jedisService.hset(RedisKey.activity_rank.getKey(ActivityConf.TYPE_ZJS + "")
                    , ActivityConf.RANK_STAR + "", gson.toJson(list2));

            // 获取前一次比赛的结果
            List<Long> roomList = rankActivityService.getWinList(ActivityConf.TYPE_BJS, ActivityConf.RANK_ROOM, size);
            // 房间榜
            List<RankActVo> list3 = rankActivityService.getRankFromDBWapper(ActivityConf.RANK_ROOM, giftIds, roomList
                    , startTimeStr, endTimeStr, size);
            jedisService.hset(RedisKey.activity_rank.getKey(ActivityConf.TYPE_ZJS + "")
                    , ActivityConf.RANK_ROOM + "", gson.toJson(list3));
        } catch (Exception e) {
            logger.error("getRankFromDBWapper error, gameType:" + ActivityConf.TYPE_ZJS, e);
        } finally {
            logger.info("cacheRankZJS finish =========");
        }
    }


//    @Scheduled(cron = "*/3 * * * * ?")
//    @Async
//    public void testss() throws InterruptedException {
//        System.out.println("testss start ==================++++");
////        TimeUnit.SECONDS.sleep(30);
//        System.out.println("invake testss++++++");
//        System.out.println("testss finish ==================++++");
//    }


}
