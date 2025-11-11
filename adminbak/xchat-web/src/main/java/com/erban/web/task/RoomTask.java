package com.erban.web.task;

import com.erban.main.model.RoomTag;
import com.erban.main.model.beanmap.HomeRoomFlowPeriod;
import com.erban.main.service.HomeService;
import com.erban.main.service.RobotService;
import com.erban.main.service.home.HomeV2Service;
import com.erban.main.service.noble.NobleRecomService;
import com.erban.main.service.room.RoomCleanService;
import com.erban.main.service.room.RoomRcmdService;
import com.erban.main.service.room.RoomTagService;
import com.erban.main.vo.RoomVo;
import com.google.common.collect.Lists;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.BlankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RoomTask extends BaseTask {
    private static final Logger logger = LoggerFactory.getLogger(RoomTask.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RoomRcmdService rcmdService;
    @Autowired
    private RoomTagService roomTagService;
    @Autowired
    private HomeService homeService;
    @Autowired
    private HomeV2Service homeV2Service;
    @Autowired
    private RoomCleanService roomCleanService;
    @Autowired
    private NobleRecomService nobleRecomService;
    @Autowired
    private RobotService robotService;

    /**
     * 清除房间内周贡献榜数据
     */
    @Scheduled(cron = "0 0 5 ? * 1")
    public void clearWeekRank() {
        logger.info("clearWeekRank start ===============");
        jedisService.hdeleteKey(RedisKey.send_gift_weeklist.getKey());
        logger.info("clearWeekRank finish ===============");
    }

    /**
     * 缓存标签数据， 30s
     */
    @Scheduled(cron = "*/30 * * ? * *")
    public void cacheTagRoom() {
        logger.info("cache room_tag_list start ===============");
        List<RoomTag> list = roomTagService.getAllRoomTagsFromDB();
        if (list != null) {
            // 缓存房间标签列表
            jedisService.set(RedisKey.room_tag_room.getKey(), gson.toJson(list));
            for (RoomTag roomTag : list) {
                // 缓存每个标签的数据
                jedisService.hset(RedisKey.room_tag_list.getKey(), roomTag.getId().toString(), gson.toJson(roomTag));
            }
        }
        logger.info("cache room_tag_list finish ===============");

        // 缓存顶部标签数据
        logger.info("cache room_tag_top start ===============");
        list = roomTagService.getAppTopTagsFromDB();
        jedisService.set(RedisKey.room_tag_top.getKey(), gson.toJson(list));
        logger.info("cache room_tag_top finish ===============");

        // 缓存分类标签数据
        logger.info("cache room_tag_search start ===============");
        list = roomTagService.getAppSearchTagsFromDB();
        jedisService.set(RedisKey.room_tag_search.getKey(), gson.toJson(list));
        logger.info("cache room_tag_search finish ===============");
    }

    /**
     * 缓存标签主页的数据，2min
     */
    @Scheduled(cron = "10 */2 * * * ?")
    public void cacheTagIndexRooms() {
        while (true) {
            try {
                if (GiftTask.isDoHomeDataJob) {
                    TimeUnit.SECONDS.sleep(2);
                    continue;
                }
                List<Long> hideUids = getHideRunningRoom();
                logger.info("cacheTagRooms start =================");
                List<RoomTag> topTags = roomTagService.getTopTagList();
                for (RoomTag roomTag : topTags) {
                    // 缓存每个标签下前面的180条数据，前端请求时也只是查找缓存
                    List<Integer> isPermitRoom = new ArrayList<>();
                    isPermitRoom.add(2);
                    isPermitRoom.add(3);
                    List<RoomVo> list = homeV2Service.getRoomByTagFromDB(roomTag.getId(), hideUids, 1, Constant.CACHE_MAX_SIZE, isPermitRoom);
                    if (!BlankUtil.isBlank(list)) {
                        // 过滤没有活人的房间
                        list = filteRobotRoom(list);
                        jedisService.set(RedisKey.room_tag_index.getKey(roomTag.getId().toString()), gson.toJson(list));
                    } else {
//                        jedisService.del(RedisKey.room_tag_index.getKey(roomTag.getId().toString()));
                    }
                }
                logger.info("cacheTagRooms finish =================");

                logger.info("cacheSearchRooms start =================");
                List<RoomTag> searchTags = roomTagService.getSearchTags();
                for (RoomTag roomTag : searchTags) {
                    if (roomTag.getId() == 9) {// 新秀用单独的逻辑刷新
                        continue;
                    }
                    // 缓存每个标签下前面的180条数据，前端请求时也只是查找缓存
                    List<Integer> isPermitRoom = new ArrayList<>();
                    isPermitRoom.add(1);
                    isPermitRoom.add(2);
                    isPermitRoom.add(3);
                    List<RoomVo> list = homeV2Service.getRoomByTagFromDB(roomTag.getId(), hideUids, 1, Constant.CACHE_MAX_SIZE, isPermitRoom);
                    if (!BlankUtil.isBlank(list)) {
                        if (roomTag.getId() == 8) {
                            // 过滤有机器人的房间
                            list = filteHasRobotRoom(list);
                        } else {
                            // 过滤没有活人的房间
                            list = filteRobotRoom(list);
                        }
                        jedisService.set(RedisKey.room_tag_index.getKey(roomTag.getId().toString()), gson.toJson(list));
                    } else {
//                        jedisService.del(RedisKey.room_tag_index.getKey(roomTag.getId().toString()));
                    }
                }
                logger.info("cacheSearchRooms finish =================");
                break;
            } catch (Exception e) {
                logger.error("cacheTagIndexRooms error", e);
                break;
            }
        }
    }

    /**
     * 缓存首页的数据， 2min
     */
    @Scheduled(cron = "10 */2 * ? * *")
    public void cacheHomeRooms() {
        while (true) {
            try {
                if (GiftTask.isDoHomeDataJob) {
                    TimeUnit.SECONDS.sleep(2);
                    continue;
                }

                List<Long> hideRooms = getHideRunningRoom();
                logger.info("cacheRecomRooms start =================");
                // 缓存首页热门推荐
                List<HomeRoomFlowPeriod> homeRoomFlowPeriodList = homeService.getHomeHotManualRecommList();
                List<RoomVo> recomRoom;
                if (!BlankUtil.isBlank(homeRoomFlowPeriodList)) {
                    recomRoom = homeV2Service.getHomeRoomToRoomVo(homeRoomFlowPeriodList);
                    if (!BlankUtil.isBlank(recomRoom)) {
                        List<RoomVo> resultRooms = filteHideRoomForHomeHotManual(recomRoom, hideRooms);
                        if (resultRooms.size() <= 4) {
                            resultRooms.addAll(filteHideRoomForHomeHotManual(getNewRoom(), hideRooms));
                        }
                        jedisService.set(RedisKey.home_hot_recom.getKey(), gson.toJson(resultRooms));
                    }
                } else {
                    recomRoom = filteHideRoomForHomeHotManual(getNewRoom(), hideRooms);
                    if (!BlankUtil.isBlank(recomRoom)) {
                        jedisService.set(RedisKey.home_hot_recom.getKey(), gson.toJson(recomRoom));
                    } else {
                        jedisService.set(RedisKey.home_hot_recom.getKey(), "[]");
                    }
                }
                List<Long> recomUids = Lists.newArrayList();
//                if (!BlankUtil.isBlank(recomRoom)) {
//                    for (RoomVo roomVo : recomRoom) {
//                        recomUids.add(roomVo.getUid());
//                    }
//                }
                recomUids.addAll(hideRooms);
                logger.info("cacheRecomRooms finish =================");

                // 缓存绿色厅列表
                logger.info("cacheGreenHomeRooms start =================");
                List<RoomVo> list = homeV2Service.getGreenHome(recomUids, 1, Constant.CACHE_MAX_SIZE);
                if (!BlankUtil.isBlank(list)) {
                    // 过滤没有活人的房间
                    list = filteRobotRoom(list);
                    if (list.size() < 2) {
                        jedisService.del(RedisKey.green_room_list.getKey());
                    } else {
                        if (list.size() > 6) {
                            list = list.subList(0, 6);
                        }
                        jedisService.set(RedisKey.green_room_list.getKey(), gson.toJson(list));
                        for (RoomVo roomVo : list) {
                            recomUids.add(roomVo.getUid());
                        }
                    }
                }
                logger.info("cacheGreenHomeRooms finish =================");

                if (GiftTask.isDoHomeDataJob) continue;
                logger.info("cacheHomeRooms start =================");
                // 缓存热门下房间列表，过滤已推荐的房间, 缓存前面的180条数据
                List<Integer> isPermitRoom = new ArrayList<>();
                isPermitRoom.add(1);
                list = homeV2Service.getRoomByTagFromDB(null, recomUids, 1, Constant.CACHE_MAX_SIZE, isPermitRoom);
                if (!BlankUtil.isBlank(list)) {
                    // 过滤没有活人的房间
                    list = filteRobotRoom(list);
                    jedisService.set(RedisKey.home_room_list.getKey(), gson.toJson(list));
                }

                // 缓存新秀下房间列表，过滤已推荐的房间, 缓存前面的180条数据
                isPermitRoom = new ArrayList<>();
                isPermitRoom.add(2);
                isPermitRoom.add(3);
                list = homeV2Service.getRoomByTagFromDB(9, recomUids, 1, Constant.CACHE_MAX_SIZE, isPermitRoom);
                if (!BlankUtil.isBlank(list)) {
                    // 过滤没有活人的房间
                    list = filteRobotRoom(list);
                    jedisService.set(RedisKey.room_home_new.getKey(), gson.toJson(list));
                }

                // 发送提醒通知，通知推荐生效
                nobleRecomService.sendErbanNotice();
                logger.info("cacheHomeRooms finish =================");
                break;
            } catch (Exception e) {
                logger.error("cacheHomeRooms error", e);
                break;
            }
        }
    }

    private List<RoomVo> getNewRoom() {
        List<HomeRoomFlowPeriod> oneList = jdbcTemplate.query("select uid,online_num as onlineNum,1 as seqNo from room where uid in (1002729,100448) AND valid = TRUE", new BeanPropertyRowMapper<>(HomeRoomFlowPeriod.class));
        return homeV2Service.getHomeRoomToRoomVo(oneList);
    }

    /**
     * 清除僵尸房，分为两种情况：
     * 1、普通房间，没人在线时直接关闭；
     * 2、牌照房间，没人在线或者只有机器人在房间时，不在页面上显示
     */
    @Scheduled(cron = "0 */10 * ? * *")
    public void clearInvalidRoom() {
        logger.info("clearInvalidRoom start==============");
        roomCleanService.cleanInvalidRoom();
        logger.info("clearInvalidRoom finish=============");
    }

    /**
     * 每2分钟刷新推荐房间
     */
    @Scheduled(cron = "10 */2 * * * ?")
    public void cacheRcmdRoom() {
        logger.info("cacheRcmdRoom start==============");
        rcmdService.cacheRcmdRoom();
        logger.info("cacheRcmdRoom finish=============");
    }

    /**
     * 获取需要隐藏的在线房间，如牌照房没人时的房间
     *
     * @return
     */
    public List<Long> getHideRunningRoom() {
        List<Long> list = Lists.newArrayList();
        list.add(100L);
        list.add(1000L);
//        try {
//            Map<String, String> map = jedisService.hgetAll(RedisKey.room_permit_hide.getKey());
//            if (map != null) {
//                Set<String> keySet = map.keySet();
//                for (String key : keySet) {
//                    if (!BlankUtil.isBlank(key) && !BlankUtil.isBlank(map.get(key))) {
//                        list.add(Long.valueOf(map.get(key)));
//                    }
//                }
//            }
//        } catch (Exception e) {
//            logger.error("getHideRunningRoom error", e);
//        }
        return list;
    }

    public List<RoomVo> filteHideRoomForHomeHotManual(List<RoomVo> homeRooms, List<Long> hideRooms) {
        List<RoomVo> resultRoom = Lists.newArrayList();
        Integer robotNum;
        for (RoomVo roomVo : homeRooms) {
            if (!roomVo.getValid() || roomVo.getOnlineNum() < 1) {
                continue;
            }
            robotNum = robotService.getRobotNum(roomVo.getUid());
            if (robotNum > 0 && roomVo.getOnlineNum() <= robotNum) {
                continue;
            }
            boolean isExist = false;
            for (Long uid : hideRooms) {
                if (roomVo.getUid().equals(uid)) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                resultRoom.add(roomVo);
            }
        }
        return resultRoom;
    }

    /**
     * 过滤存在机器人并且在线人数小于3的房间
     *
     * @param rooms
     * @return
     */
    public List<RoomVo> filteRobotRoom(List<RoomVo> rooms) {
        Iterator<RoomVo> it = rooms.iterator();
        while (it.hasNext()) {
            RoomVo roomVo = it.next();
            Integer robotNum = robotService.getRobotNum(roomVo.getUid());
            if (robotNum > 0 && roomVo.getOnlineNum() < (robotNum + 3)) {
                it.remove();
            }
        }
        return rooms;
    }

    /**
     * 过滤存在机器人的房间
     *
     * @param rooms
     * @return
     */
    public List<RoomVo> filteHasRobotRoom(List<RoomVo> rooms) {
        Iterator<RoomVo> it = rooms.iterator();
        while (it.hasNext()) {
            RoomVo roomVo = it.next();
            Integer robotNum = robotService.getRobotNum(roomVo.getUid());
            if (robotNum > 0) {
                it.remove();
            }
        }
        return rooms;
    }

}
