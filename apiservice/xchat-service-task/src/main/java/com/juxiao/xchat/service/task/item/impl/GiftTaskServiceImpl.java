package com.juxiao.xchat.service.task.item.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.dao.event.RankDao;
import com.juxiao.xchat.dao.event.dto.RankDTO;
import com.juxiao.xchat.dao.event.query.RankQuery;
import com.juxiao.xchat.dao.room.HomeHotManualRecommDao;
import com.juxiao.xchat.dao.task.TaskDao;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.draw.conf.GiftDrawConf;
import com.juxiao.xchat.manager.common.event.ActivityHtmlManager;
import com.juxiao.xchat.manager.common.event.conf.RankDatetype;
import com.juxiao.xchat.manager.common.event.conf.RankType;
import com.juxiao.xchat.manager.common.event.vo.RankVo;
import com.juxiao.xchat.manager.common.item.GiftMessageManager;
import com.juxiao.xchat.manager.common.item.mq.BigGiftMessage;
import com.juxiao.xchat.manager.common.item.mq.GiftMessage;
import com.juxiao.xchat.manager.common.item.mq.RoomMessage;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.user.UserGiftPurseManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.task.item.GiftTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GiftTaskServiceImpl implements GiftTaskService {
    private Logger logger = LoggerFactory.getLogger(GiftTaskServiceImpl.class);

    @Autowired
    private Gson gson;

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private GiftMessageManager giftMessageManager;

    @Autowired
    private UsersManager usersManager;

    @Autowired
    private LevelManager levelManager;

    @Autowired
    private RankDao rankDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private HomeHotManualRecommDao homeHotManualRecommDao;

    @Autowired
    private ActivityHtmlManager activityHtmlManager;

    @Autowired
    private UserGiftPurseManager userGiftPurseManager;

    @Autowired
    private GiftDrawConf giftDrawConf;

    @Autowired
    private RoomManager roomManager;

    private static Date appStartDate = DateUtils.parser("2017-08-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
    private static Date appEndDate = DateUtils.parser("2037-08-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
    private static int[] rankType = {RankType.charm, RankType.wealth, RankType.room};
    private static int[] dateType = {RankDatetype.day, RankDatetype.week, RankDatetype.total};


    @Override
    public void saveOneDayHome() {
        try {
            Date date = new Date();
            Date startTime = DateTimeUtils.setTime(date, 0, 0, 0);
            Date endTime = DateTimeUtils.setTime(date, 24, 0, 0);
            Date sqlTime = DateTimeUtils.setTime(date, 12, 0, 0);
            jdbcTemplate.update("REPLACE INTO `one_day_room_recv_sum` (`uid`, `recv_uid`,`normal_sum_gold`,`draw_sum_gold`,`xq_sum_gold`, `sum_gold`, " +
                            "`create_time`, `update_time`) select ifnull(room_uid,0), recive_uid," +
                            "SUM( CASE g.gift_type WHEN 2 THEN re.total_gold_num ELSE 0 END ) normal_sum_gold, " +
                            "SUM( CASE g.gift_type WHEN 3 THEN re.total_gold_num ELSE 0 END ) draw_sum_gold," +
                            "SUM( CASE g.gift_type WHEN 5 THEN re.total_gold_num ELSE 0 END ) xq_sum_gold," +
                            "SUM(total_gold_num), ?, ? from " +
                            "gift_send_record re LEFT JOIN gift g ON g.gift_id = re.gift_id where (re.create_time BETWEEN ? and ?) " +
                            "GROUP BY ifnull(room_uid,0), recive_uid", sqlTime, sqlTime, startTime, endTime);

            jdbcTemplate.update("REPLACE INTO `one_day_room_send_sum` (`uid`, `send_uid`, `normal_sum_gold`,`draw_sum_gold`,`xq_sum_gold`, `sum_gold`," +
                            "`create_time`, `update_time`) select ifnull(room_uid,0), uid, " +
                            "SUM( CASE g.gift_type WHEN 2 THEN re.total_gold_num ELSE 0 END ) normal_sum_gold," +
                            "SUM( CASE g.gift_type WHEN 3 THEN re.total_gold_num ELSE 0 END ) draw_sum_gold," +
                            "SUM( CASE g.gift_type WHEN 5 THEN re.total_gold_num ELSE 0 END ) xq_sum_gold," +
                            "SUM(total_gold_num), ?, ? from " +
                            "gift_send_record re LEFT JOIN gift g ON g.gift_id = re.gift_id where (re.create_time BETWEEN ? and ?) " +
                            "GROUP BY ifnull(room_uid,0), uid", sqlTime, sqlTime, startTime, endTime);


//            int[] giftList = giftDrawConf.getHighDrawGifts();
//            StringBuffer sb = new StringBuffer();
//            for (int i = 0; i < giftList.length; i++) {
//                sb.append(giftList[i] + ",");
//            }
//            sb.append("0");
//            logger.info("[ 定时写onedayroom ] drawgiftlist:{}", sb.toString());
//            jdbcTemplate.update("REPLACE INTO `normal_one_day_room_recv_sum` (`uid`, `recv_uid`, `sum_gold`, " +
//                    "`create_time`, `update_time`) select ifnull(room_uid,0), recive_uid, SUM(total_gold_num), ?, ? " +
//                    "from gift_send_record where gift_id not in (" + sb.toString() + ") and (create_time BETWEEN ? " +
//                    "and ?) GROUP BY ifnull(room_uid,0), recive_uid", sqlTime, sqlTime, startTime, endTime);

            redisManager.del(RedisKey.room_ctrb_list.getKey());
        } catch (Exception e) {
            logger.error("[ 刷新每天贡献失败 ]", e);
        }
    }

    @Override
    public void saveLastDayHome() {
        Date now = new Date();
        Date date = DateTimeUtils.getLastDay(now, 1);
        Date startTime = DateTimeUtils.setTime(date, 0, 0, 0);
        Date endTime = DateTimeUtils.setTime(date, 24, 0, 0);
        Date sqlTime = DateTimeUtils.setTime(date, 12, 0, 0);
        jdbcTemplate.update("REPLACE INTO `one_day_room_recv_sum` (`uid`, `recv_uid`,`normal_sum_gold`,`draw_sum_gold`,`xq_sum_gold`, `sum_gold`, " +
                "`create_time`, `update_time`) select ifnull(room_uid,0), recive_uid," +
                "SUM( CASE g.gift_type WHEN 2 THEN re.total_gold_num ELSE 0 END ) normal_sum_gold, " +
                "SUM( CASE g.gift_type WHEN 3 THEN re.total_gold_num ELSE 0 END ) draw_sum_gold," +
                "SUM( CASE g.gift_type WHEN 5 THEN re.total_gold_num ELSE 0 END ) xq_sum_gold," +
                "SUM(total_gold_num), ?, ? from " +
                "gift_send_record re LEFT JOIN gift g ON g.gift_id = re.gift_id where (re.create_time BETWEEN ? and ?) " +
                "GROUP BY ifnull(room_uid,0), recive_uid", sqlTime, sqlTime, startTime, endTime);

        jdbcTemplate.update("REPLACE INTO `one_day_room_send_sum` (`uid`, `send_uid`, `normal_sum_gold`,`draw_sum_gold`,`xq_sum_gold`, `sum_gold`," +
                "`create_time`, `update_time`) select ifnull(room_uid,0), uid, " +
                "SUM( CASE g.gift_type WHEN 2 THEN re.total_gold_num ELSE 0 END ) normal_sum_gold," +
                "SUM( CASE g.gift_type WHEN 3 THEN re.total_gold_num ELSE 0 END ) draw_sum_gold," +
                "SUM( CASE g.gift_type WHEN 5 THEN re.total_gold_num ELSE 0 END ) xq_sum_gold," +
                "SUM(total_gold_num), ?, ? from " +
                "gift_send_record re LEFT JOIN gift g ON g.gift_id = re.gift_id where (re.create_time BETWEEN ? and ?) " +
                "GROUP BY ifnull(room_uid,0), uid", sqlTime, sqlTime, startTime, endTime);

//        int[] giftList = giftDrawConf.getHighDrawGifts();
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < giftList.length; i++) {
//            sb.append(giftList[i] + ",");
//        }
//        sb.append("0");
//        jdbcTemplate.update("REPLACE INTO `normal_one_day_room_recv_sum` (`uid`, `recv_uid`, `sum_gold`, " +
//                "`create_time`, `update_time`) select ifnull(room_uid,0), recive_uid, SUM(total_gold_num), ?, ? from " +
//                "gift_send_record where gift_id not in (" + sb.toString() + ") and (create_time BETWEEN ? and ?) " +
//                "GROUP BY ifnull(room_uid,0), recive_uid", sqlTime, sqlTime, startTime, endTime);

        // 1.0.0 暂时注释掉缓存 男神/女神/娱乐/电台上一天流水第一的房间
//        List<Map<String, Object>> list = taskDao.findLastDayOne(startTime, endTime);
//        if (list != null && list.size() > 0) {
//            HomeHotManualRecommDO homeHotManualRecomm;
//            Date startValidTime = DateTimeUtils.setTime(now, 19, 0, 0);
//            Date endValidTime = DateTimeUtils.setTime(now, 20, 0, 0);
//            for (Map<String, Object> l : list) {
//                homeHotManualRecomm = new HomeHotManualRecommDO();
//                homeHotManualRecomm.setUid(Long.valueOf(l.get("uid").toString()));
//                homeHotManualRecomm.setSeqNo(1);
//                homeHotManualRecomm.setStatus(new Byte("1"));
//                homeHotManualRecomm.setStartValidTime(startValidTime);
//                homeHotManualRecomm.setEndValidTime(endValidTime);
//                homeHotManualRecomm.setCreateTime(now);
//                homeHotManualRecommDao.save(homeHotManualRecomm);
//            }
//        }
    }

    @Override
    public void refreshOneWeek() {
        activityHtmlManager.refreshOneWeek();
    }

    @Override
    public void refreshAll() {
        activityHtmlManager.refreshAll();
    }

    @Override
    public void refreshRoomRank() {
        activityHtmlManager.refreshRoomRank();
    }

    @Override
    public void saveGiftRankToCache() {
        for (int i = 0; i < rankType.length; i++) {
            for (int j = 0; j < dateType.length; j++) {
                int type = rankType[i];
                int dateType = GiftTaskServiceImpl.dateType[j];
                List<RankVo> rankList = getRankListFromDb(type, dateType);
                saveToCache(type, dateType, rankList);
            }
        }
    }

    private final List<Long> filterUid = Arrays.asList(510429L, 510430L, 510439L);

    /**
     * 从数据库查询排行信息
     *
     * @param type     排行类型
     * @param dateType 时间类型
     * @return
     */
    public List<RankVo> getRankListFromDb(int type, int dateType) {
        Date now = new Date();
        Date beginDate, endDate;
        RankQuery query = new RankQuery();
        switch (dateType) {
            case RankDatetype.day:
                beginDate = DateTimeUtils.setTime(now, 0, 0, 0);
                endDate = DateTimeUtils.setTime(now, 23, 59, 59);
                break;
            case RankDatetype.week:
                beginDate = DateUtils.getCurrentMonday();
                endDate = DateUtils.getCurrentSundayTime(23, 59, 59);
                break;
            case RankDatetype.total:
                beginDate = appStartDate;
                endDate = appEndDate;
                break;
            default:
                beginDate = DateTimeUtils.setTime(now, 0, 0, 0);
                endDate = DateTimeUtils.setTime(now, 23, 59, 59);
                break;
        }
        query.setEndTime(endDate);
        query.setStartTime(beginDate);
        List<RankDTO> rankList;
        switch (type) {
            case RankType.charm:
                rankList = rankDao.getAllStarRankList(query);
                break;
            case RankType.wealth:
                rankList = rankDao.getNobelRankList(query);
                break;
            case RankType.room:
                rankList = rankDao.getAllRoomRankList(query);
                break;
            default:
                rankList = Lists.newArrayList();
        }
        rankList = rankList.stream().filter(item -> !filterUid.contains(item.getUid())).collect(Collectors.toList());
        return converToRankVo(rankList);
    }

    /**
     * 保存排行信息到缓存中
     *
     * @param type     排行类型
     * @param dateType 时间类型
     * @param list     排行数据
     */
    public void saveToCache(int type, int dateType, List<RankVo> list) {
        String key = type + "" + dateType;
        redisManager.hset(RedisKey.rank.getKey(), key, gson.toJson(list));
        // 考虑使用LIST
        // List<String> voList = Lists.newArrayList();
        // for (RankVo vo : list) {
        //     voList.add(gson.toJson(vo));
        // }
        // String key = RedisKey.rank.getKey() + getKey(type, dateType);
        // redisManager.leftPushAll(key , voList);
    }

    public List<RankVo> converToRankVo(List<RankDTO> rankList) {
        if (rankList.isEmpty()) {
            return Lists.newArrayList();
        }
        List<RankVo> list = Lists.newArrayList();
        UsersDTO user;
        RankVo vo;
        for (RankDTO dto : rankList) {
            user = usersManager.getUser(dto.getUid());
            if (user != null) {
                vo = converToRankVo(dto, user);
                list.add(vo);
            }
        }
        return list;
    }

    public RankVo converToRankVo(RankDTO dto, UsersDTO user) {
        RankVo rankVo = new RankVo();
        rankVo.setUid(user.getUid());
        rankVo.setGender(user.getGender());
        rankVo.setNick(user.getNick());
        rankVo.setAvatar(user.getAvatar());
        rankVo.setErbanNo(user.getErbanNo());
        rankVo.setTotalNum(dto.getTotalNum());
        // 增加财富等级和魅力等级
        rankVo.setExperLevel(levelManager.getUserExperienceLevelSeq(user.getUid()));
        rankVo.setCharmLevel(levelManager.getUserCharmLevelSeq(user.getUid()));
        return rankVo;
    }

    @Override
    public void refreshLast() {
        activityHtmlManager.refreshLast();
    }

    @Override
    public void savePeriodData() {
        jdbcTemplate.update("delete from stat_room_ctrb_sum_period");
        jdbcTemplate.update("INSERT INTO stat_room_ctrb_sum_period(uid,flow_sum_total,create_time) SELECT re.room_uid" +
                " AS room_uid, SUM(re.total_gold_num) sum_gold, NOW() FROM gift_send_record re WHERE re.create_time >" +
                " DATE_SUB(NOW(),INTERVAL  10 MINUTE) AND re.room_uid IS NOT NULL GROUP BY re.room_uid ORDER BY " +
                "sum_gold DESC");
        Long totalFlow = jdbcTemplate.queryForObject("select sum(ifnull(flow_sum_total,0)) from " +
                "stat_room_ctrb_sum_period", Long.class);
        Long totalOnline = jdbcTemplate.queryForObject("select sum(ifnull(online_num,0)) from room", Long.class);
        jdbcTemplate.update("REPLACE INTO stat_room_flow_online_period (uid, room_id, room_pwd,operator_status, " +
                "title, tag_id, tag_pict,room_tag, badge, room_desc, back_pic,valid,avatar,nick,gender, " +
                "official_room, is_permit_room, type,recom_seq, online_num, flow_sum_total,score) SELECT rt.uid,rt" +
                ".room_id,rt.room_pwd,rt.operator_status,rt.title,rt.tag_id, rt.tag_pict,rt.room_tag ,if(rt.badge = " +
                "'',null,rt.badge),rt.room_desc,rt.back_pic,rt.valid ,(select ut3.avatar from users ut3 where rt" +
                ".uid=ut3.uid) ,(select ut.nick from users ut where rt.uid=ut.uid) ,(select ut2.gender from users ut2" +
                " where rt.uid=ut2.uid) ,rt.official_room,rt.is_permit_room,rt.type,rt.recom_seq, coalesce(rt" +
                ".online_num,0), coalesce(st.flow_sum_total,0), (coalesce(0.3 * st.flow_sum_total, 0) + 0.7 * rt" +
                ".online_num) FROM stat_room_ctrb_sum_period st RIGHT JOIN  room rt ON rt.uid = st.uid WHERE  rt" +
                ".valid=1 AND rt.online_num>0 AND rt.can_show=1 AND (rt.room_pwd IS NULL OR rt.room_pwd='')");

        // // 删表stat_room_ctrb_sum_period区间内流水表
        // jdbcTemplate.update("delete from stat_room_ctrb_sum_period");
        // // 插入数据到stat_room_ctrb_sum_period区间内流水表
        // jdbcTemplate.update("INSERT INTO stat_room_ctrb_sum_period(uid, flow_sum_total, create_time) SELECT record
        // .room_uid" +
        //         " AS room_uid, SUM(record.total_gold_num) sum_gold, NOW() FROM gift_send_record record WHERE
        //         record.create_time >" +
        //         " DATE_SUB(NOW(), INTERVAL 10 MINUTE) AND record.room_uid IS NOT NULL GROUP BY record.room_uid
        //         ORDER BY" +
        //         " sum_gold DESC");
        // // 总流水
        // Long totalFlow = jdbcTemplate.queryForObject("select sum(ifnull(flow_sum_total, 0)) from
        // stat_room_ctrb_sum_period", Long.class);
        // // 总在线人数
        // Long totalOnline = jdbcTemplate.queryForObject("select sum(ifnull(online_num, 0)) from room", Long.class);
        // // score计算公式: score = 真实人数 * 10 + 协议*1 + 该房间10分钟流水 / 100 + 后台输入值*1
        // jdbcTemplate.update("REPLACE INTO stat_room_flow_online_period (uid, room_id, room_pwd, operator_status, " +
        //                 "title, tag_id, tag_pict,room_tag, badge, room_desc, back_pic,valid,avatar,nick,gender, " +
        //                 "official_room, is_permit_room, type, recom_seq, online_num, flow_sum_total, score) " +
        //                 "SELECT rt.uid, rt.room_id, rt.room_pwd, rt.operator_status, rt.title, rt.tag_id, rt
        //                 .tag_pict, rt.room_tag, if(rt.badge = " +
        //                 "'', null, rt.badge), rt.room_desc, rt.back_pic, rt.valid, " +
        //                 "(select ut3.avatar from users ut3 where rt.uid = ut3.uid), " +
        //                 "(select ut.nick from users ut where rt.uid=ut.uid), " +
        //                 "(select ut2.gender from users ut2 where rt.uid = ut2.uid), " +
        //                 "rt.official_room, rt.is_permit_room, rt.type, rt.recom_seq, " +
        //                 "coalesce(rt.online_num, 0), coalesce(st.flow_sum_total, 0), " +
        //                 "((rt.online_num * 10) + robot_num + coalesce(st.flow_sum_total / 100, 0) + rc
        //                 .room_control_value) " +
        //                 // "(coalesce(60 * st.flow_sum_total / ?, 0) + 40 * rt.online_num / ?) " +
        //                 "FROM stat_room_ctrb_sum_period st RIGHT JOIN room rt ON rt.uid = st.uid inner join " +
        //                 "(select COUNT(1) robot_num, u.uid from room_robot_group r INNER JOIN users u on r
        //                 .group_no =" +
        //                 " u.erban_no) t " +
        //                 "on t.uid = rt.uid " +
        //                 "inner join room_control rc on rc.room_id = rt.room_id" +
        //                 "WHERE rt.valid = 1 AND rt.online_num > 0 AND rt.can_show = 1 AND (rt.room_pwd IS NULL OR
        //                 rt.room_pwd = '')");
    }

    @Override
    public void retryGiftQueue() {
        Map<String, String> map = redisManager.hgetAll(RedisKey.mq_gift_status.getKey());
        if (map == null || map.size() == 0) {
            return;
        }
        Set<String> keySet = map.keySet();
        long curTime = System.currentTimeMillis();
        long gapTime = 30000;  // 30s内没被消费

        for (String key : keySet) {
            try {
                String val = map.get(key);
                GiftMessage giftMessage = gson.fromJson(val, GiftMessage.class);
                if (curTime - giftMessage.getMessTime() > gapTime) {
                    giftMessageManager.handleGiftMessage(giftMessage);
                }
            } catch (Exception e) {
                logger.error("[ 处理礼物队列失败 ]", e);
            }
        }
    }

    @Override
    public void retryGiftPropQueue() {
        Map<String, String> map = redisManager.hgetAll(RedisKey.mq_gift_prop_status.getKey());
        if (map == null || map.size() == 0) {
            return;
        }
        Set<String> keySet = map.keySet();
        long curTime = System.currentTimeMillis();
        long gapTime = 30000;  // 30s内没被消费

        for (String key : keySet) {
            try {
                String val = map.get(key);
                GiftMessage giftMessage = gson.fromJson(val, GiftMessage.class);
                if (curTime - giftMessage.getMessTime() > gapTime) {
                    giftMessageManager.handleGiftPropMessage(giftMessage);
                }
            } catch (Exception e) {
                logger.error("[ 处理礼物队列失败 ]", e);
            }
        }
    }

    @Override
    public void retryBigGiftQueue() {
        Map<String, String> map = redisManager.hgetAll(RedisKey.mq_big_gift_status.getKey());
        if (map == null || map.size() == 0) {
            return;
        }
        Set<String> keySet = map.keySet();
        long curTime = System.currentTimeMillis();

        for (String key : keySet) {
            try {
                String val = map.get(key);
                BigGiftMessage giftMessage = gson.fromJson(val, BigGiftMessage.class);
                if (curTime - giftMessage.getMessTime() > 60000) {
                    giftMessageManager.handleBigGiftMessage(giftMessage);
                }
            } catch (Exception e) {
                logger.error("[ 处理全服礼物队列失败 ]", e);
            }
        }
    }

    @Override
    public void retryGiftFullQueue() {
        Map<String, String> map = redisManager.hgetAll(RedisKey.mq_room_message_status.getKey());
        if (map == null || map.size() == 0) {
            return;
        }
        Set<String> keySet = map.keySet();
        long curTime = System.currentTimeMillis();
        long gapTime = 30000;  // 30s内没被消费
        for (String key : keySet) {
            try {
                String val = map.get(key);
                RoomMessage roomMessage = gson.fromJson(val, RoomMessage.class);
                if (curTime - roomMessage.getMessTime() > gapTime) {
                    userGiftPurseManager.handleMQRoomMessage(roomMessage);
                }
            } catch (Exception e) {
                logger.error("[ 处理捡海螺全服礼物队列失败 ]", e);
            }
        }
    }

    @Override
    public void retryCallQueue() {
        Map<String, String> map = redisManager.hgetAll(RedisKey.mq_call_status.getKey());
        if (map == null || map.size() == 0) {
            return;
        }
        Set<String> keySet = map.keySet();
        long curTime = System.currentTimeMillis();
        long gapTime = 30000;  // 30s内没被消费

        for (String key : keySet) {
            try {
                String val = map.get(key);
                GiftMessage giftMessage = gson.fromJson(val, GiftMessage.class);
                if (curTime - giftMessage.getMessTime() > gapTime) {
                    giftMessageManager.handleCallMessage(giftMessage);
                }
            } catch (Exception e) {
                logger.error("[ 处理礼物队列失败 ]", e);
            }
        }
    }

    // @Override
    // public void retryMysticGiftQueue() {
    //     Map<String, String> map = redisManager.hgetAll(RedisKey.mq_mystic_gift_status.getKey());
    //     if (map == null || map.isEmpty()) {
    //         return;
    //     }
    //     long curTime = System.currentTimeMillis();
    //     // 一分钟内没被消费
    //     long gapTime = 1000 * 60 * 1;
    //     map.keySet().forEach(key -> {
    //         String message = map.get(key);
    //         if (StringUtils.isNotBlank(message)) {
    //             try {
    //                 MysticGiftMessage giftMessage = gson.fromJson(message, MysticGiftMessage.class);
    //                 if (curTime - giftMessage.getMessTime() > gapTime) {
    //                     giftMessageManager.handleMysticMessage(giftMessage);
    //                 }
    //             } catch (Exception e ) {
    //                 logger.error("[处理神秘礼物队列失败]", e);
    //             }
    //         }
    //     });
    // }
}
