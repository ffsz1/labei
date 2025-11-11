package com.erban.main.service.activity;

import com.erban.main.config.SystemConfig;
import com.erban.main.model.YoungGRecord;
import com.erban.main.model.YoungGTask;
import com.erban.main.model.YoungGTaskExample;
import com.erban.main.mybatismapper.YoungGRecordMapper;
import com.erban.main.mybatismapper.YoungGTaskMapper;
import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.SendSysMsgService;
import com.erban.main.service.activity.enumarator.YoungGPrizeType;
import com.erban.main.service.activity.enumarator.YoungGTaskType;
import com.erban.main.service.common.JedisLockService;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateUtil;
import com.xchat.common.utils.RandomUtils;
import com.xchat.common.utils.StringUtils;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @class: YoungGService.java
 * @author: chenjunsheng
 * @date 2018/8/17
 */
@Service
public class YoungGService {
    private static final double[] FIRST_RATE = new double[]{0.507614213, 0.609137056, 0.657360406, 0.66751269, 0.845177665, 0.895939086, 0.946700508, 0.956852792, 0.961928934, 0.964467005, 0.989847716, 1};
    private static final double[] SECOND_RATE = new double[]{0.519480519, 0.597402597, 0.638961039, 0.644155844, 0.825974026, 0.877922078, 0.92987013, 0.94025974, 0.945454545, 0.948051948, 1};

    private final Logger logger = LoggerFactory.getLogger(YoungGService.class);

    @Autowired
    private YoungGTaskMapper youngGTaskMapper;
    @Autowired
    private YoungGRecordMapper youngGRecordMapper;
    @Autowired
    private JedisService jedisService;
    @Autowired
    private JedisLockService lockService;
    @Autowired
    private SendSysMsgService sendSysMsgService;

    private Date endTime;

    public void increaseRoomTime(Long uid) {
        Date endTime = this.getEndTime();
        if (endTime == null || new Date().getTime() > endTime.getTime()) {
            return;
        }
        jedisService.hincr(RedisKey.youngg_room_time.getKey(), uid.toString());
        String json = jedisService.hget(RedisKey.youngg_room_time.getKey(), uid.toString());
//        try {
//            if (Long.valueOf(json) >= 3) {
//                this.updateTaskFinish(uid, YoungGTaskType.go_in_3_room);
//            }
//        } catch (Exception e) {
//
//        }
    }

//    /**
//     * @param uid
//     * @param task
//     */
//    public void updateTaskFinish(Long uid, YoungGTaskType task) {
//        Date endTime = this.getEndTime();
//        if (endTime == null || new Date().getTime() > endTime.getTime()) {
//            return;
//        }
//
//        String redisKey = RedisKey.youngg_task_lock.getKey(uid + "_" + task.getTaskId());
//        String lockVal = lockService.lock(redisKey, 3000);
//        if (StringUtils.isBlank(lockVal)) {
//            return;
//        }
//        try {
//            Date now = new Date();
//            Date startDate = DateUtil.setTime(now, 0, 0, 0);
//            Date endDate = DateUtil.setTime(now, 23, 59, 59);
//            YoungGTaskExample example = new YoungGTaskExample();
//            example.createCriteria().andUidEqualTo(uid).andCreateTimeBetween(startDate, endDate).andTaskIdEqualTo(task.getTaskId());
//            List<YoungGTask> youngGTasks = youngGTaskMapper.selectByExample(example);
//            if (youngGTasks == null || youngGTasks.size() == 0 || youngGTasks.get(0) == null) {
//                Date date = new Date();
//                YoungGTask youngGTask = new YoungGTask();
//                youngGTask.setUid(uid);
//                youngGTask.setTaskId(task.getTaskId());
//                youngGTask.setTaskStatus((byte) 2);
//                youngGTask.setCreateTime(date);
//                youngGTask.setUpdateTime(date);
//                youngGTaskMapper.insertSelective(youngGTask);
//                this.send(uid, "恭喜您完成【" + task.getTaskName() + "】，今天获得抽奖机会一次，养鸡签名照等你来拿！");
//                return;
//            }
//
//            YoungGTask youngGTask = youngGTasks.get(0);
//            if (youngGTask.getTaskStatus() == 1) {
//                youngGTask.setTaskStatus((byte) 2);
//                youngGTaskMapper.updateByPrimaryKey(youngGTask);
//                this.send(uid, "恭喜您完成【" + task.getTaskName() + "】，今天获得抽奖机会一次，养鸡签名照等你来拿！");
//            }
//        } catch (Exception e) {
//        } finally {
//            lockService.unlock(redisKey, lockVal);
//        }
//
//    }

    public BusiResult draw(Long uid) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }

        Date endTime = this.getEndTime();
        if (endTime == null) {
            return new BusiResult(BusiStatus.SERVERERROR);
        }

        if (new Date().getTime() > endTime.getTime()) {
            return new BusiResult(BusiStatus.YOUNGG_IS_END);
        }

        String lockVal = lockService.lock(RedisKey.youngg_draw_lock.getKey(), 3000);
        if (StringUtils.isEmpty(lockVal)) {
            return new BusiResult(BusiStatus.SERVERBUSY);
        }
        try {
            Date now = new Date();
            Date startDate = DateUtil.setTime(now, 0, 0, 0);
            Date endDate = DateUtil.setTime(now, 23, 59, 59);
            YoungGTaskExample example = new YoungGTaskExample();
            example.createCriteria().andUidEqualTo(uid).andTaskStatusEqualTo((byte) 2).andCreateTimeBetween(startDate, endDate);
            List<YoungGTask> list = youngGTaskMapper.selectByExample(example);
            if (list == null || list.size() == 0 || list.get(0) == null) {
                return new BusiResult<>(BusiStatus.YOUNGG_DRAW_COUNT_NOT_ENOUGH);
            }

            String value = jedisService.get(RedisKey.youngg_gift_pic.getKey());
            double randouble = RandomUtils.nextDoule();
            YoungGPrizeType type;
            if (StringUtils.isBlank(value) || Long.valueOf(value) <= 15) {
                type = this.drawFirst(randouble);
            } else {
                type = this.drawSecond(randouble);
            }

            type.updateGift(uid);
            if (type == YoungGPrizeType.prize_12) {
                jedisService.incr(RedisKey.youngg_gift_pic.getKey());
            }

            YoungGTask task = list.get(0);
            task.setTaskStatus((byte) 3);
            youngGTaskMapper.updateByPrimaryKey(task);

            YoungGRecord record = new YoungGRecord();
            record.setPrizeId(type.getPrizeId());
            record.setCreateTime(new Date());
            record.setUid(uid);
            youngGRecordMapper.insertSelective(record);
            this.send(uid, "恭喜在养鸡签名照大抽奖中获得【" + type.getPrizeName() + "】，请查收！");
            return new BusiResult<>(BusiStatus.SUCCESS, type.getPrizeId() + "");
        } catch (Exception e) {
            return new BusiResult<>(BusiStatus.SERVEXCEPTION);
        } finally {
            lockService.unlock(RedisKey.youngg_draw_lock.getKey(), lockVal);
        }

    }


    /**
     * 获取用户抽检次数
     *
     * @param uid
     * @return
     */
    public int countDailyTask(Long uid) {
        Date now = new Date();
        Date startDate = DateUtil.setTime(now, 0, 0, 0);
        Date endDate = DateUtil.setTime(now, 23, 59, 59);
        YoungGTaskExample example = new YoungGTaskExample();
        example.createCriteria().andUidEqualTo(uid).andTaskStatusEqualTo((byte) 2).andCreateTimeBetween(startDate, endDate);
        return youngGTaskMapper.countByExample(example);
    }

    public void clear() {
        jedisService.del(RedisKey.youngg_room_time.getKey());
    }

    /**
     * 优先从奖池中进行抽奖
     *
     * @param randouble
     * @return
     */
    private YoungGPrizeType drawFirst(double randouble) {
        if (randouble > FIRST_RATE[0] && randouble <= FIRST_RATE[1]) {
            // 棒棒糖
            return YoungGPrizeType.prize_2;
        }

        if (randouble > FIRST_RATE[1] && randouble <= FIRST_RATE[2]) {
            // 么么哒
            return YoungGPrizeType.prize_3;
        }

        if (randouble > FIRST_RATE[2] && randouble <= FIRST_RATE[3]) {
            // 小熊
            return YoungGPrizeType.prize_4;
        }

        if (randouble > FIRST_RATE[3] && randouble <= FIRST_RATE[4]) {
            // young-g专属头饰*3天
            return YoungGPrizeType.prize_5;
        }

        if (randouble > FIRST_RATE[4] && randouble <= FIRST_RATE[5]) {
            // 爱你哦*3天
            return YoungGPrizeType.prize_6;
        }

        if (randouble > FIRST_RATE[5] && randouble <= FIRST_RATE[6]) {
            // 霓虹灯*3天
            return YoungGPrizeType.prize_7;
        }

        if (randouble > FIRST_RATE[6] && randouble <= FIRST_RATE[7]) {
            // 可乐小埋*3天
            return YoungGPrizeType.prize_8;
        }

        if (randouble > FIRST_RATE[7] && randouble <= FIRST_RATE[8]) {
            // 猫耳小女仆*3天
            return YoungGPrizeType.prize_9;
        }

        if (randouble > FIRST_RATE[8] && randouble <= FIRST_RATE[9]) {
            // s级赛车*3天
            return YoungGPrizeType.prize_10;
        }
        if (randouble > FIRST_RATE[9] && randouble <= FIRST_RATE[10]) {
            // 养鸡专属座驾*3天
            return YoungGPrizeType.prize_11;
        }

        if (randouble > FIRST_RATE[10] && randouble <= FIRST_RATE[11]) {
            // 养鸡的签名照
            return YoungGPrizeType.prize_12;
        }

        //玫瑰花
        return YoungGPrizeType.prize_1;
    }

    private YoungGPrizeType drawSecond(double randouble) {
        if (randouble > SECOND_RATE[0] && randouble <= SECOND_RATE[1]) {
            // 棒棒糖
            return YoungGPrizeType.prize_2;
        }

        if (randouble > SECOND_RATE[1] && randouble <= SECOND_RATE[2]) {
            // 么么哒
            return YoungGPrizeType.prize_3;
        }

        if (randouble > SECOND_RATE[2] && randouble <= SECOND_RATE[3]) {
            // 小熊
            return YoungGPrizeType.prize_4;
        }

        if (randouble > SECOND_RATE[3] && randouble <= SECOND_RATE[4]) {
            // young-g专属头饰*3天
            return YoungGPrizeType.prize_5;
        }

        if (randouble > SECOND_RATE[4] && randouble <= SECOND_RATE[5]) {
            // 爱你哦*3天
            return YoungGPrizeType.prize_6;
        }

        if (randouble > SECOND_RATE[5] && randouble <= SECOND_RATE[6]) {
            // 霓虹灯*3天
            return YoungGPrizeType.prize_7;
        }

        if (randouble > SECOND_RATE[6] && randouble <= SECOND_RATE[7]) {
            // 可乐小埋*3天
            return YoungGPrizeType.prize_8;
        }

        if (randouble > SECOND_RATE[7] && randouble <= SECOND_RATE[8]) {
            // 猫耳小女仆*3天
            return YoungGPrizeType.prize_9;
        }

        if (randouble > SECOND_RATE[8] && randouble <= SECOND_RATE[9]) {
            // s级赛车*3天
            return YoungGPrizeType.prize_10;
        }
        if (randouble > SECOND_RATE[9] && randouble <= SECOND_RATE[10]) {
            // 养鸡专属座驾*3天
            return YoungGPrizeType.prize_11;
        }

        //玫瑰花
        return YoungGPrizeType.prize_1;
    }

    private void send(Long uid, String body) {
        try {
            // 发送消息给用户
            NeteaseSendMsgParam param = new NeteaseSendMsgParam();
            param.setFrom(SystemConfig.secretaryUid);
            param.setOpe(0);
            param.setType(0);
            param.setTo(uid + "");
            param.setBody(body);
            sendSysMsgService.sendMsg(param);
        } catch (Exception e) {
            logger.error("[ 推送小秘书消息 ] 发送出现异常：", e);
        }
    }

    private Date getEndTime() {
        if (endTime == null) {
            try {
                this.endTime = DateUtil.str2Date("2018-08-25 23:59:59", DateUtil.DateFormat.YYYY_MM_DD_HH_MM_SS);
            } catch (ParseException e) {
                this.endTime = null;
            }
        }
        return endTime;
    }
}
