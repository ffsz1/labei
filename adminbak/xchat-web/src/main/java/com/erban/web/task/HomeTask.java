package com.erban.web.task;

import com.erban.main.model.FaceJson;
import com.erban.main.model.HomeHotManualRecomm;
import com.erban.main.model.Room;
import com.erban.main.model.SysConf;
import com.erban.main.model.beanmap.HomeRoomFlowPeriod;
import com.erban.main.model.domain.HotManualRuleDO;
import com.erban.main.mybatismapper.HomeHotManualRecommMapper;
import com.erban.main.mybatismapper.RoomMapperExpand;
import com.erban.main.mybatismapper.dao.HotManualRuleDAO;
import com.erban.main.mybatismapper.query.HotManualRuleQuery;
import com.erban.main.service.SysConfService;
import com.erban.main.service.room.FaceJsonService;
import com.google.common.collect.Lists;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.BlankUtil;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.common.utils.DateUtil;
import com.xchat.common.utils.GetTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class HomeTask extends BaseTask{
    private static final Logger logger = LoggerFactory.getLogger(HomeTask.class);
    public static volatile boolean isDoHomeDataJob = false;
    @Autowired
    private HomeHotManualRecommMapper homeHotManualRecommMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RoomMapperExpand roomMapperExpand;
    @Autowired
    private FaceJsonService faceJsonService;
    @Autowired
    private SysConfService sysConfService;
    @Autowired
    private HotManualRuleDAO hotManualRuleDAO;

    /**
     * 定时刷新首页数据, 2min
     */
//    @Scheduled(cron = "0 */2 * * * ?")
//    public void doHomeDataJob(){
//        try {
//            isDoHomeDataJob = true;
//            logger.info("HomeDataJob doHomeDataJob start.....");
//            homeService.doHomeDataJob();
//            logger.info("HomeDataJob doHomeDataJob finish.....");
//        } catch (Exception e) {
//            logger.error("doHomeDataJob error,", e);
//        }finally {
//            isDoHomeDataJob = false;
//        }
//    }

    /**
     * 将规则表中的数据, 添加到热门推荐表</br>
     * 每天六点更新一次
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void listHotManualRule() {
        HotManualRuleQuery query = new HotManualRuleQuery();
        // 获取今天是这周第几天, 周日是第一天 返回的是 1
        int weekDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        // 周日开始, 0 表示周日
        query.setWeekDay(String.valueOf(weekDay - 1));
        List<HotManualRuleDO> list = hotManualRuleDAO.listByToday(query);
        if (list.isEmpty()) {
            return ;
        }
        HomeHotManualRecomm homeHotManualRecomm;
        Date now = new Date();
        Date startDate, endDate;
        String[] startArr, endArr;
        for (HotManualRuleDO ruleDO : list) {
            startArr = ruleDO.getStartDate().split(":");
            endArr = ruleDO.getEndDate().split(":");
            startDate = DateUtil.setTimeHourOfDay(now, Integer.valueOf(startArr[0]), Integer.valueOf(startArr[1]), 0);
            endDate = DateUtil.setTimeHourOfDay(now, Integer.valueOf(endArr[0]), Integer.valueOf(endArr[1]), 0);
            homeHotManualRecomm = new HomeHotManualRecomm();
            homeHotManualRecomm.setUid(ruleDO.getUid());
            int seqNo = ruleDO.getSeqNo() == null ? 1 : ruleDO.getSeqNo();
            homeHotManualRecomm.setSeqNo(seqNo);
            homeHotManualRecomm.setStatus(new Byte("1"));
            homeHotManualRecomm.setStartValidTime(startDate);
            homeHotManualRecomm.setEndValidTime(endDate);
            homeHotManualRecomm.setCreateTime(now);
            homeHotManualRecommMapper.insertSelective(homeHotManualRecomm);
        }
    }

    /**
     * 从配置池中获取房间列表，随机排序后保存到缓存；
     * 首页加载时从缓存中获取，分页返回
     */
    @Scheduled(cron = "0 */2 * * * ?")
    public void cacheRandomRoom(){
        try {
            List<Room> list = roomMapperExpand.selectPoolRooms();
            if (BlankUtil.isBlank(list)) {
                jedisService.set(RedisKey.home_room_random.getKey(), "[]");
                return;
            }
            // 打乱列表的排序
            Random random = new Random();
            for (int i = 0; i < list.size(); i++) {
                int pos = random.nextInt(list.size());
                Room curRoom = list.get(i);
                // 当前位置的元素换成随机位置上的元素
                list.set(i, list.get(pos));
                list.set(pos, curRoom);
            }
            jedisService.set(RedisKey.home_room_random.getKey(), gson.toJson(list));
        } catch (Exception e) {
            logger.error("cacheRandomRoom error", e.getMessage());
        }
    }

    /**
     * 缓存当前有效的表情JSON
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void cacheFaceJson() {
        logger.info("cacheFaceJson start ============");
        int tryTime = 0;
        FaceJson faceJson = null;
        while (tryTime++ < 3 && faceJson == null) {
            try {
                faceJson = faceJsonService.getFaceJsonFromDB();
                if (faceJson != null) {
                    jedisService.set(RedisKey.face_json.getKey(), gson.toJson(faceJson));
                    break;
                }
                TimeUnit.SECONDS.sleep(2);
            } catch (Exception e) {
                logger.error("cacheFaceJson error", e.getMessage());
            }
        }
        logger.info("cacheFaceJson finish ============");
    }

    /**
     * 保存每天贡献
     *
     */
    @Scheduled(cron = "0 */2 * * * ?")
    public void saveOneDayHome() {
        logger.info("saveOneDayHome start ============");
        Date date=new Date();
        Date startTime = GetTimeUtils.getTimesnights(date,0);
        Date endTime = GetTimeUtils.getTimesnights(date,24);
        Date sqlTime = GetTimeUtils.getTimesnights(date,12);
        jdbcTemplate.update("REPLACE INTO `one_day_room_recv_sum` (`uid`, `recv_uid`, `sum_gold`, `create_time`, `update_time`) select ifnull(room_uid,0), recive_uid, SUM(total_gold_num), ?, ? from gift_send_record where (create_time BETWEEN ? and ?) GROUP BY ifnull(room_uid,0), recive_uid",sqlTime,sqlTime, startTime, endTime);
        jdbcTemplate.update("REPLACE INTO `one_day_room_send_sum` (`uid`, `send_uid`, `sum_gold`, `create_time`, `update_time`) select ifnull(room_uid,0), uid, SUM(total_gold_num), ?, ? from gift_send_record where (create_time BETWEEN ? and ?) GROUP BY ifnull(room_uid,0), uid",sqlTime,sqlTime, startTime, endTime);
        logger.info("saveOneDayHome finish ============");
    }

    /**
     * 确保上一天贡献完全保存，每天0点3分再统计一次上一天的数据
     *
     */
    @Scheduled(cron = "0 3 0 * * ?")
    public void saveLastDayHome() {
        logger.info("saveLastDayHome start ============");
        Date date= DateTimeUtil.getLastDay(new Date(), 1);
        Date startTime = GetTimeUtils.getTimesnights(date,0);
        Date endTime = GetTimeUtils.getTimesnights(date,24);
        Date sqlTime = GetTimeUtils.getTimesnights(date,12);
        jdbcTemplate.update("REPLACE INTO `one_day_room_recv_sum` (`uid`, `recv_uid`, `sum_gold`, `create_time`, `update_time`) select ifnull(room_uid,0), recive_uid, SUM(total_gold_num), ?, ? from gift_send_record where (create_time BETWEEN ? and ?) GROUP BY ifnull(room_uid,0), recive_uid",sqlTime,sqlTime, startTime, endTime);
        jdbcTemplate.update("REPLACE INTO `one_day_room_send_sum` (`uid`, `send_uid`, `sum_gold`, `create_time`, `update_time`) select ifnull(room_uid,0), uid, SUM(total_gold_num), ?, ? from gift_send_record where (create_time BETWEEN ? and ?) GROUP BY ifnull(room_uid,0), uid",sqlTime,sqlTime, startTime, endTime);
        logger.info("saveLastDayHome finish ============");
    }

    /**
     * 清空分成等级
     *
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void cleanBonusLevel() {
        jedisService.del(RedisKey.bonus_level.getKey());
    }

    /**
     * 每小时消费金额达三万金币（服务端不要写死）以上进入推荐位
     *
     */
    @Scheduled(cron = "0 0 */1 * * ?")
    public void saveLastHourRecom() {
        logger.info("saveLastHourRecom start ============");
        Date startValidTime = new Date();
        Date endValidTime = DateTimeUtil.getNextHour(startValidTime, 1);
        Date date= DateTimeUtil.getNextHour(startValidTime, -1);
        Integer tol = 30000;
        SysConf hourRecom = sysConfService.getSysConfById("hour_recom");
        if(hourRecom!=null){
            try{
                tol = Integer.valueOf(hourRecom.getConfigValue());
            }catch (Exception e){
                logger.error("推荐位金额配置错误");
            }
        }
        List<HomeRoomFlowPeriod> list = jdbcTemplate.query("select r.uid,r.online_num as onlineNum,1 as seqNo,SUM(o.total_gold_num) as tol from gift_send_record o INNER JOIN room r on o.room_uid = r.uid where o.create_time BETWEEN ? and ? GROUP BY o.room_uid HAVING tol >= ? ORDER BY tol desc", new BeanPropertyRowMapper<>(HomeRoomFlowPeriod.class), date, startValidTime, tol);
        if(list!=null&&list.size()>0){
            HomeHotManualRecomm homeHotManualRecomm;
            for(HomeRoomFlowPeriod l:list){
                homeHotManualRecomm = new HomeHotManualRecomm();
                homeHotManualRecomm.setUid(l.getUid());
                homeHotManualRecomm.setSeqNo(1);
                homeHotManualRecomm.setStatus(new Byte("1"));
                homeHotManualRecomm.setStartValidTime(startValidTime);
                homeHotManualRecomm.setEndValidTime(endValidTime);
                homeHotManualRecomm.setCreateTime(startValidTime);
                homeHotManualRecommMapper.insertSelective(homeHotManualRecomm);
            }
        }
        logger.info("saveLastHourRecom finish ============");
    }

}
