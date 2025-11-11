package com.juxiao.xchat.service.task.room.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.dao.room.RoomDao;
import com.juxiao.xchat.dao.room.RoomRcmdDao;
import com.juxiao.xchat.dao.room.RoomRcmdPoolDao;
import com.juxiao.xchat.dao.room.RoomTagDao;
import com.juxiao.xchat.dao.room.domain.RoomRcmdDTO;
import com.juxiao.xchat.dao.room.domain.RoomRobotGroup;
import com.juxiao.xchat.dao.room.domain.RoomRobotGroupRela;
import com.juxiao.xchat.dao.room.domain.StatRoomFlowOnlinePeriod;
import com.juxiao.xchat.dao.room.dto.HomeRoomFlowPeriod;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.room.dto.RoomFlowWeekVo;
import com.juxiao.xchat.dao.room.dto.RoomTagDTO;
import com.juxiao.xchat.dao.room.query.RoomRcmdQuery;
import com.juxiao.xchat.dao.task.TaskDao;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.room.RoomTagManager;
import com.juxiao.xchat.manager.common.room.vo.RoomVo;
import com.juxiao.xchat.manager.common.room.vo.RunningRoomVo;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.netease.NetEaseRoomManager;
import com.juxiao.xchat.service.task.room.RoomPkVoteTaskService;
import com.juxiao.xchat.service.task.room.RoomTaskService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.NumberFormat;
import java.util.*;

@Service
public class RoomTaskServiceImpl implements RoomTaskService {
    private Logger logger = LoggerFactory.getLogger(RoomPkVoteTaskService.class);

    @Autowired
    private Gson gson;

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private RoomTagDao roomTagDao;

    @Autowired
    private RoomTagManager roomTagManager;

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private UsersManager usersManager;

    @Autowired
    private NetEaseRoomManager netEaseRoomManager;

    @Autowired
    private RoomRcmdDao roomRcmdDao;

    @Autowired
    private RoomRcmdPoolDao roomRcmdPoolDao;

    @Autowired
    private RoomDao roomDao;

    // 缓存最大房间的数量
    public static final Integer CACHE_MAX_SIZE = 200;
    // 缓存首页优质推荐房间数量上限
    public static final Integer CACHE_MAX_RECOMMEND = 3;

    @Override
    public void cacheTagRoom() {
        try {
            List<RoomTagDTO> list = roomTagDao.listAppAllTags();
            if (list != null) {
                // 缓存房间标签列表
                redisManager.set(RedisKey.room_tag_room.getKey(), gson.toJson(list));
                for (RoomTagDTO roomTag : list) {
                    // 缓存每个标签的数据
                    redisManager.hset(RedisKey.room_tag_list.getKey(), roomTag.getId().toString(), gson.toJson(roomTag));
                }
            }

            // 缓存顶部标签数据
            list = roomTagDao.listAppTopTags();
            redisManager.set(RedisKey.room_tag_top.getKey(), gson.toJson(list));

            // 缓存分类标签数据
            list = roomTagDao.listAppSearchTags();
            redisManager.set(RedisKey.room_tag_search.getKey(), gson.toJson(list));
        } catch (Exception e) {
            logger.error("[ 缓存房间标签错误 ]", e);
        }
    }

    @Override
    public void cacheTagIndexRooms() {
        try {
            List<Long> hideUids = getHideRunningRoom();
            List<RoomTagDTO> topTags = roomTagManager.getTopTagList(null, null);
            for (RoomTagDTO roomTag : topTags) {
                // 缓存每个标签下前面的180条数据，前端请求时也只是查找缓存
                List<Integer> isPermitRoom = new ArrayList<>();
                isPermitRoom.add(1);
                isPermitRoom.add(2);
                isPermitRoom.add(3);
                List<RoomVo> list = this.getRoomByTagFromDB(roomTag.getId(), hideUids, CACHE_MAX_SIZE, isPermitRoom);
                if (list != null && list.size() > 0) {
                    // 过滤没有活人的房间
                    // list = this.filteHasRobotRoom(list);
                    if (list != null && list.size() > 0) {
                        redisManager.set(RedisKey.room_tag_index.getKey(roomTag.getId().toString()), gson.toJson(list));
                    } else {
                        redisManager.del(RedisKey.room_tag_index.getKey(roomTag.getId().toString()));
                    }
                } else {
                    // 清除缓存
                    redisManager.del(RedisKey.room_tag_index.getKey(roomTag.getId().toString()));
                }
            }
//            List<RoomTagDTO> searchTags = roomTagManager.getSearchTags(false);
//            for (RoomTagDTO roomTag : searchTags) {
//                // 新秀用单独的逻辑刷新
//                if (roomTag.getId() == 9) {
//                    continue;
//                }
//                // 缓存每个标签下前面的180条数据，前端请求时也只是查找缓存
//                List<Integer> isPermitRoom = new ArrayList<>();
//                isPermitRoom.add(1);
//                isPermitRoom.add(2);
//                isPermitRoom.add(3);
//                List<RoomVo> list = this.getRoomByTagFromDB(roomTag.getId(), hideUids, CACHE_MAX_SIZE, isPermitRoom);
//                if (list == null || list.size() == 0) {
//                    redisManager.del(RedisKey.room_tag_index.getKey(roomTag.getId().toString()));
//                    continue;
//                }
//
//                if (roomTag.getId() == 8) {
//                    // 过滤有机器人的房间
//                    list = filterHasRobotRoom(list);
//                } else {
//                    // 过滤没有活人的房间
//                    list = filterRobotRoom(list);
//                }
//                redisManager.set(RedisKey.room_tag_index.getKey(roomTag.getId().toString()), gson.toJson(list));
//            }
        } catch (Exception e) {
            logger.error("cacheTagIndexRooms error", e);
        }
    }

    @Override
    public void cacheHomeRooms() {
        try {
            // 获取需要隐藏的在线房间
//            List<Long> hideRooms = getHideRunningRoom();

            // [优质推荐列表] 若 CACHE_MAX_RECOMMEND == 0 需要另外做逻辑
            List<HomeRoomFlowPeriod> homeRoomFlowPeriodList = taskDao.getHomeHotManualRecommendPositionList(CACHE_MAX_RECOMMEND);
            List<RoomVo> recommendRoom = Lists.newArrayList();
            List<Long> recommendUidList = Lists.newArrayList();
            // 当前推荐房间数量;
            Integer curRecommendSize = 0;
            if (homeRoomFlowPeriodList != null && homeRoomFlowPeriodList.size() > 0) {
                recommendRoom = getHomeRoomToRoomVo(homeRoomFlowPeriodList);
                if (!CollectionUtils.isEmpty(recommendRoom)) {
                    for (RoomVo recRoom : recommendRoom) {
                        recommendUidList.add(recRoom.getUid());
                    }
                    curRecommendSize = recommendRoom.size();
                } else {
                    curRecommendSize = 0;
                }
            }
            logger.info("[ 优质推荐UID ] {}", recommendUidList);

            // 当前推荐房间数量需要从热门房间补充的数量;
            Integer needSupplySize = CACHE_MAX_RECOMMEND - curRecommendSize;
            // 所需热门房间的数量;
            Integer needHotRoomSize = CACHE_MAX_SIZE + needSupplySize;

            // [热门房间列表], 过滤已推荐的房间缓存前面的200条数据
            List<Integer> isPermitRoom = Lists.newArrayList();
            // 1.牌照房; 2.非牌照房; 3.应该是审核中的牌照房
            isPermitRoom.add(1);
            // 根据房间标签获取房间信息(去除推荐列表的房间还有非牌照房)
            List<RoomVo> roomList = getRoomByTagFromDB(null, recommendUidList, needHotRoomSize, isPermitRoom);

            LinkedList<RoomVo> roomLinkedList = new LinkedList<>(roomList);

//            // 若没有后台设置推荐位，添加普通的热门前三为推荐位
//            for (int i = 0; i < needSupplySize; i++) {
//                recommendRoom.add(roomLinkedList.pollFirst());
//            }
//
//            if (!CollectionUtils.isEmpty(recommendRoom)) {
//                redisManager.set(RedisKey.home_hot_recom.getKey(), gson.toJson(recommendRoom));
//            } else {
//                redisManager.del(RedisKey.home_hot_recom.getKey());
//            }

            // 添加到热门的头部
            for (RoomVo roomVo : recommendRoom){
                roomLinkedList.addFirst(roomVo);
            }

            if (!CollectionUtils.isEmpty(roomLinkedList)) {
                redisManager.set(RedisKey.home_room_list.getKey(), gson.toJson(roomLinkedList));
            } else {
                redisManager.del(RedisKey.home_room_list.getKey());
            }

//            logger.info("[ 热门房间 ] {}", JSON.toJSONString(roomLinkedList));

            // 缓存首页[热门房间列表], 不过滤已推荐的房间用于分类列表
//            roomList = getRoomByTagFromDB(null, null, CACHE_MAX_SIZE, isPermitRoom);
//            if (roomList != null && roomList.size() > 0) {
//                // 过滤存在机器人并且在线人数小于3个的房间
//                // roomList = filterRobotRoom(roomList);
//                redisManager.set(RedisKey.room_tag_hot.getKey(), gson.toJson(roomList));
//            } else {
//                redisManager.del(RedisKey.room_tag_hot.getKey());
//            }

            // // 缓存首页[新秀房间列表], 过滤已推荐的房间, 缓存前面的200条数据
            // isPermitRoom = new ArrayList<>();
            // isPermitRoom.add(3);
            // list = getRoomByTagFromDB(null, recommendUidList, CACHE_MAX_SIZE, isPermitRoom);
            // if (list != null && list.size() > 0) {
            //     // 过滤掉机器人房间
            //     list = filterRobotRoom(list);
            //     redisManager.set(RedisKey.room_home_new.getKey(), gson.toJson(list));
            // } else {
            //     redisManager.del(RedisKey.room_home_new.getKey());
            // }
        } catch (Exception e) {
            logger.error("[ 缓存主页失败 ]", e);
        }
    }

    @Override
    public void clearInvalidRoom() {
        Map<String, String> map = redisManager.hgetAll(RedisKey.room_running.getKey());
        if (map == null) {
            return;
        }
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            try {
                Long uid = Long.valueOf(key);
                // RunningRoomVo runningRoomVo = gson.fromJson(map.get(key), RunningRoomVo.class);
                RoomDTO room = roomManager.getUserRoom(uid);
                if (room == null || !room.getValid()) {
                    redisManager.hdel(RedisKey.room_running.getKey(), uid.toString());
                    roomManager.close(uid);
                    continue;
                }
            } catch (Exception e) {
                logger.error("cleanInvalidRoom error, room uid: " + key, e);
            }
        }
    }

    private List<RoomVo> getHomeRoomToRoomVo(List<HomeRoomFlowPeriod> roomFlowlist) {
        List<RoomVo> roomVoList = Lists.newArrayList();
        for (HomeRoomFlowPeriod homeRoomFlow : roomFlowlist) {
            Long uid = homeRoomFlow.getUid();
            RoomDTO room = roomManager.getUserRoom(uid);
            // 若配置了不展示，需要过滤
            if (room == null || room.getCanShow().intValue() != 1) {
                continue;
            }
            RoomVo roomVo = roomManager.convertRoomToVo(room);
            Double flowSum = homeRoomFlow.getPersonFlowSumSeqNoValue();
            roomVo.setCalcSumDataIndex(flowSum.intValue());
            roomVo.setSeqNo(homeRoomFlow.getSeqNo());
            int onlineNum = homeRoomFlow.getOnlineNum();
            if (onlineNum == 0) {
                onlineNum = 1;
            }
            roomVo.setOnlineNum(onlineNum + roomManager.getNeedAddNum(uid, onlineNum));
            UsersDTO users = usersManager.getUser(uid);
            if (users != null) {
                roomVo.setAvatar(users.getAvatar());
                roomVo.setNick(users.getNick());
                roomVo.setGender(users.getGender());
                roomVo.setErbanNo(users.getErbanNo());
            }
            roomVoList.add(roomVo);
        }
        return roomVoList;
    }

    /**
     * 获取需要隐藏的在线房间，如牌照房没人时的房间
     *
     * @return
     */
    private List<Long> getHideRunningRoom() {
        List<Long> list = Lists.newArrayList();
        // list.add(100L);
        // list.add(1000L);
        // list.addAll(IOSData.MMYY_AUDIT_ALIST);
        // list.addAll(IOSData.MMJY_AUDIT_ALIST);
        // list.addAll(IOSData.MMXQ_AUDIT_ALIST);
        return list;
    }

    /**
     * 从推荐列表过滤掉需要隐藏的房间
     *
     * @param homeRooms
     * @param hideRooms
     * @return
     */
    public List<RoomVo> filterHideRoomForHomeHotManual(List<RoomVo> homeRooms, List<Long> hideRooms) {
        List<RoomVo> resultRoom = Lists.newArrayList();
        Integer robotNum;
        for (RoomVo roomVo : homeRooms) {
            if (!roomVo.getValid() || roomVo.getOnlineNum() < 1) {
                continue;
            }
            // robotNum = roomManager.getRobotNum(roomVo.getUid());
            // if (robotNum > 0 && roomVo.getOnlineNum() <= robotNum) {
            //     continue;
            // }
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
    private List<RoomVo> filterRobotRoom(List<RoomVo> rooms) {
        Iterator<RoomVo> it = rooms.iterator();
        while (it.hasNext()) {
            RoomVo roomVo = it.next();
            int robotNum = roomManager.getRobotNum(roomVo.getUid());
            if (robotNum <= 0) {
                continue;
            }

            // if (roomVo.getOnlineNum() < (robotNum + 1)) {
            if (roomVo.getOnlineNum() < 3) {
                it.remove();
            }
        }
        return rooms;
    }

    @Override
    public void refreshPzPerson() {
        try {
            List<RoomDTO> roomList = taskDao.findIsPermitRoom(1);
            for (RoomDTO room : roomList) {
                if (room.getValid()) {
                    updateRoomOnline(room);
                }
            }
        } catch (Exception e) {
            logger.error("[ 更新房间人数错误 ]", e);
        }
    }

    @Override
    public void refreshShpzPerson() {
        try {
            List<RoomDTO> roomList = taskDao.findIsPermitRoom(3);
            for (RoomDTO room : roomList) {
                if (room.getValid()) {
                    updateRoomOnline(room);
                }
            }
        } catch (Exception e) {
            logger.error("[ 更新房间人数错误 ]", e);
        }
    }

    @Override
    public void refreshPtPerson() {
        try {
            List<RoomDTO> roomList = taskDao.findIsPermitRoom(2);
            for (RoomDTO room : roomList) {
                if (room.getValid()) {
                    updateRoomOnline(room);
                }
            }
        } catch (Exception e) {
            logger.error("[ 更新房间人数错误 ]", e);
        }
    }

    public void updateRoomOnline(RoomDTO room) {
        // RoomResult roomRet = netEaseRoomManager.getRoomMessage(room.getRoomId());
        // Map<String, Object> chatroom = roomRet.getChatroom();
        // if (chatroom.get("onlineusercount") != null) {
        //     Integer personNum = (Integer) chatroom.get("onlineusercount");
        //     RunningRoomVo runningRoomVo = new RunningRoomVo();
        //                 if (personNum > 0) {
        //                     // 牌照房需要过滤机器人
        //                     if (roomVo.getIsPermitRoom() == 1) {
        //                         if (roomCleanService.hasRealUserInRoom(roomVo.getRoomId())) {
        //                             runningRoomVo.setCount(0);
        //                         } else {
        //                             // 开房状态时，每扫描一次若在线人数为0，计数器加1
        //                             runningRoomVo.setCount(roomVo.getCount() + 1);
        //                         }
        //                     } else {
        //                         runningRoomVo.setCount(0);
        //                     }
        //                 } else {
        //                     // 开房状态时，每扫描一次若在线人数为0，计数器加1
        //                     runningRoomVo.setCount(roomVo.getCount() + 1);
        //                 }
        //     runningRoomVo.setCount(0);
        //     runningRoomVo.setRoomId(room.getRoomId());
        //     runningRoomVo.setUid(room.getUid());
        //     runningRoomVo.setOnlineNum(personNum);
        //     if (personNum > 0) {
        //         redisManager.hset(RedisKey.room_running.getKey(), room.getUid().toString(), gson.toJson(runningRoomVo));
        //     } else {
        //         redisManager.hdel(RedisKey.room_running.getKey(), room.getUid().toString());
        //     }
        //     jdbcTemplate.update("UPDATE room set online_num = ? where uid = ?", personNum, room.getUid());
        // }
    }

    @Override
    public void refreshOnlineNum() {
        Map<String, String> map = redisManager.hgetAll(RedisKey.im_online_num.getKey());
        if (map != null && map.keySet().size() > 0) {
            for (String key : map.keySet()) {
                jdbcTemplate.update("UPDATE room set online_num = ? where uid = ?", map.get(key), key);
                redisManager.hdel(RedisKey.room.getKey(), key);
                redisManager.hdel(RedisKey.im_online_num.getKey(), key);
            }
        }
    }

    @Override
    public void refreshRobot() {
        List<RoomRobotGroupRela> roomRobotGroupRelaList = taskDao.findRoomRobotList();
        for (RoomRobotGroupRela roomRobotGroupRela : roomRobotGroupRelaList) {
            Integer groupNo = roomRobotGroupRela.getGroupNo();
            List<RoomRobotGroup> roomRobotGroupList = taskDao.findRoomRobotGropuList(groupNo);
            if (roomRobotGroupList == null || roomRobotGroupList.size() == 0) {
                continue;
            }

            Long roomId = roomRobotGroupRela.getRoomId();
            for (RoomRobotGroup roomRobotGroup : roomRobotGroupList) {
                Long robotUid = roomRobotGroup.getRobotUid();
                List<String> rotbotAccidList = Lists.newArrayList();
                rotbotAccidList.add(robotUid.toString());
                String robotAccidsListStr = gson.toJson(rotbotAccidList);
                // netEaseRoomManager.deleteRobot(roomId, robotAccidsListStr);
                // netEaseRoomManager.addRobot(roomId, robotAccidsListStr);
            }
        }
    }

    /**
     * 根据房间标签获取房间信息
     *
     * @param tagId            房间标签ID
     * @param recommendUidList 推荐房UID
     * @param pageSize         每页显示大小
     * @param isPermitRoom     是否是牌照房
     * @return
     */
    private List<RoomVo> getRoomByTagFromDB(Integer tagId, List<Long> recommendUidList, Integer pageSize, List<Integer> isPermitRoom) {
        List<StatRoomFlowOnlinePeriod> tmpList;
        if (tagId == null) { // 没有分类 拿热门房间
            tmpList = taskDao.selectRoomFlowOnlinePeriod(recommendUidList, null, isPermitRoom, pageSize);
        } else {
            List<Integer> tagIds = Lists.newArrayList();
            tagIds.add(tagId);
            tmpList = taskDao.selectRoomFlowOnlinePeriod(recommendUidList, tagIds, isPermitRoom, pageSize);
        }
        logger.info("sql tagId 执行结果 tagId:>{} recommendUidList:>{} list:>{}", tagId, recommendUidList, JSON.toJSONString(tmpList));
        return convertToRoom(tmpList);
    }

    /**
     * 转换为RoomVO
     *
     * @param list
     * @return
     */
    private List<RoomVo> convertToRoom(List<StatRoomFlowOnlinePeriod> list) {
        List<RoomVo> rooms = Lists.newArrayList();
        UsersDTO users;
        int onlineNum = 0;
        if (list == null && list.size() == 0) {
            return rooms;
        }

        for (StatRoomFlowOnlinePeriod period : list) {
            users = usersManager.getUser(period.getUid());
            if ((period.getTagId() == null || period.getTagId() <= 0 || period.getTagId() == 8) && StringUtils.isBlank(period.getRoomTag())) {
                logger.error("tagNull convertToRoomWrong>:{}", gson.toJson(period));
            }

            RoomVo roomVo = new RoomVo();
            roomVo.setOnlineNum(period.getOnlineNum() + onlineNum);
            roomVo.setUid(period.getUid());
            roomVo.setErbanNo(users == null ? 1987456L : users.getErbanNo());
            roomVo.setNick(period.getNick());
            roomVo.setAvatar(period.getAvatar());
            roomVo.setGender(period.getGender());
            roomVo.setBackPic(period.getBackPic() == null ? "" : period.getBackPic());
            roomVo.setRoomId(period.getRoomId());
            roomVo.setRoomDesc(period.getRoomDesc());
            roomVo.setRoomTag(period.getRoomTag());
            roomVo.setTagId(period.getTagId());
            roomVo.setTagPict(period.getTagPict());
            roomVo.setTitle(period.getTitle());
            roomVo.setRecomSeq(period.getRecomSeq());
            roomVo.setType(period.getType());
            roomVo.setOfficeUser((byte) 1);
            roomVo.setOperatorStatus(period.getOperatorStatus());
            roomVo.setIsPermitRoom(period.getIsPermitRoom());
            roomVo.setOfficeUser(period.getOfficialRoom());
            roomVo.setBadge("".equals(period.getBadge()) ? null : period.getBadge());
            roomVo.setScore(period.getScore());
            roomVo.setUserDescription(users.getUserDesc());
            rooms.add(roomVo);
        }
        return rooms;
    }

    private List<RoomVo> getHomeRunningRoomList() {
        List<RoomVo> roomVoList = Lists.newArrayList();
        Map<String, String> map = redisManager.hgetAll(RedisKey.room_running.getKey());
        if (map != null && map.size() > 0) {
            RunningRoomVo runningRoomVo;
            RoomDTO room;
            RoomVo roomVo;
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String value = entry.getKey();
                runningRoomVo = gson.fromJson(entry.getValue(), RunningRoomVo.class);
                room = roomManager.getUserRoom(Long.parseLong(value));
                if (room == null) {
                    continue;
                }
                roomVo = roomManager.convertRoomToVo(room);
                if (roomVo == null) {
                    continue;
                }
                if (roomVo.getValid()) {
                    roomVo.setCount(runningRoomVo.getCount());
                    roomVoList.add(roomVo);
                }
            }
        }
        return roomVoList;
    }

    @Override
    public void refreshWeekRoomFlowCache() {
        try {
            redisManager.del(RedisKey.room_flow_proportion.getKey());
            // 记录更新时间
            redisManager.hset(RedisKey.room_flow_proportion.getKey(), "-1", DateTimeUtils.convertDate(new Date(), "yyyy-MM-dd"));
            List<RoomFlowWeekVo> flowWeekVos = taskDao.sumByTowWeeks();
            Long uid = 0L;
            for (RoomFlowWeekVo vo : flowWeekVos) {
                long first = vo.getFirstWeek();
                long second = vo.getSecondWeek();
                double pro;
                if (second == 0) {
                    pro = first;
                } else {
                    pro = ((double) first - second) / second;
                }
                NumberFormat nf = NumberFormat.getNumberInstance();
                // 保留四位小数
                nf.setMaximumFractionDigits(4);
                nf.setGroupingUsed(false);
                vo.setProportion(nf.format(pro));
                vo.setCreateDate(new Date());
                uid = vo.getUid();
                redisManager.hset(RedisKey.room_flow_proportion.getKey(), uid == null ? "0" : vo.getUid().toString(), gson.toJson(vo));
            }
        } catch (Exception e) {
            logger.error("[ 更新房间周流水错误 ]", e);
        }
    }

    private List<RoomVo> getNewRoom() {
        List<HomeRoomFlowPeriod> oneList = jdbcTemplate.query("select uid,online_num as onlineNum,1 as seqNo from room where uid in (1002729,100448) AND valid = TRUE", new BeanPropertyRowMapper<>(HomeRoomFlowPeriod.class));
        return getHomeRoomToRoomVo(oneList);
    }

    /**
     * 过滤存在机器人的房间
     *
     * @param rooms
     * @return
     */
    // public List<RoomVo> filterHasRobotRoom(List<RoomVo> rooms) {
    //     Iterator<RoomVo> it = rooms.iterator();
    //     while (it.hasNext()) {
    //         RoomVo roomVo = it.next();
    //         Integer robotNum = roomManager.getRobotNum(roomVo.getUid());
    //         if (robotNum > 0) {
    //             it.remove();
    //         }
    //     }
    //     return rooms;
    // }

    /**
     * 过滤存在机器人并且在线人数小于3的房间
     *
     * @param rooms
     * @return
     */
    public List<RoomVo> filterHasRobotRoom(List<RoomVo> rooms) {
        Iterator<RoomVo> it = rooms.iterator();
        while (it.hasNext()) {
            RoomVo roomVo = it.next();
            Integer robotNum = roomManager.getRobotNum(roomVo.getUid());
            if (robotNum > 0 && roomVo.getOnlineNum() < (robotNum + 1)) {
                it.remove();
            }
        }
        return rooms;
    }

    @Override
    public void cacheRecommendRoom() {
        redisManager.del(RedisKey.rcmd_room_pool.getKey());
        Date now = new Date();
        RoomRcmdQuery query = new RoomRcmdQuery();
        query.setStartDate(now);
        query.setEndDate(now);
        List<RoomRcmdDTO> list = roomRcmdDao.listRcmd(query);
        if (list == null || list.isEmpty() || list.get(0) == null) {
            // 没有推荐房间
            return;
        }

        RoomRcmdDTO recommendDto = list.get(0);
        List<Long> pools = roomRcmdPoolDao.listRoomFkId(recommendDto.getRcmdId());
        if (pools == null || pools.size() == 0) {
            return;
        }

        if (recommendDto.getRcmdType() == 2) {
            pools.forEach((roomId) -> {
                RoomDTO room = roomManager.getUserRoom(roomId);
                if (room != null) {
                    this.push(room);
                }
            });
            return;
        }

        // 根据标签
        final List<Integer> tagIdPools = new ArrayList<>();
        pools.forEach((id) ->
                tagIdPools.add(id.intValue())
        );
        List<RoomDTO> roomList = roomDao.listByTagIds(tagIdPools, recommendDto.getMinOnline());
        if (roomList == null || roomList.isEmpty()) {
            return;
        }
        roomList.forEach((room) ->
                this.push(room)
        );
    }

    @Override
    public void cacheHotRoom() {
        // // 缓存首页[热门房间列表], 过滤已推荐的房间缓存前面的200条数据
        // List<Integer> isPermitRoom = Lists.newArrayList();
        // // 1.牌照房; 2.非牌照房
        // isPermitRoom.add(1);
        // // 根据房间标签获取房间信息(去除推荐列表的房间还有非牌照房)
        // List<RoomVo> roomList = getRoomByTagFromDB(null, recommendUidList, CACHE_MAX_SIZE, isPermitRoom);
        // logger.info("[ 热门房间list过滤前 ] {}", JSON.toJSONString(roomList));
        // if (roomList != null && roomList.size() > 0) {
        //     // 过滤存在机器人并且在线人数小于3个的房间
        //     roomList = filterRobotRoom(roomList); // TODO upline mod
        //     redisManager.set(RedisKey.home_room_list.getKey(), gson.toJson(roomList));
        //     logger.info("[ 热门房间list过滤后 ] {}", JSON.toJSONString(roomList));
        // } else {
        //     redisManager.del(RedisKey.home_room_list.getKey());
        // }
        //
        // // 缓存首页[热门房间列表], 不过滤已推荐的房间用于分类列表
        // roomList = getRoomByTagFromDB(null, null, CACHE_MAX_SIZE, isPermitRoom);
        // if (roomList != null && roomList.size() > 0) {
        //     // 过滤存在机器人并且在线人数小于3个的房间
        //     roomList = filterRobotRoom(roomList);
        //     redisManager.set(RedisKey.room_tag_hot.getKey(), gson.toJson(roomList));
        // } else {
        //     redisManager.del(RedisKey.room_tag_hot.getKey());
        // }
    }

    private void push(RoomDTO room) {
        RoomVo roomVo = new RoomVo();
        roomVo.setRoomId(room.getRoomId());
        roomVo.setUid(room.getUid());
        roomVo.setTitle(room.getTitle());
        roomVo.setAvatar(room.getAvatar());
        redisManager.lpush(RedisKey.rcmd_room_pool.getKey(), gson.toJson(roomVo));
    }

    @Override
    public void refreshRoomRecommend() {
        List<RoomVo> recommendRoom;
        List<HomeRoomFlowPeriod> homeRoomFlowPeriods = taskDao.findRoomRecomm();
        if (homeRoomFlowPeriods != null && homeRoomFlowPeriods.size() > 0) {
            recommendRoom = getHomeRoomToRoomVo(homeRoomFlowPeriods);
            redisManager.set(RedisKey.room_recommend_list.getKey(), gson.toJson(recommendRoom));
            return;
        }
    }
}
