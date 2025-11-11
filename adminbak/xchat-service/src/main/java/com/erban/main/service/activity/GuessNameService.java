package com.erban.main.service.activity;

import com.erban.main.model.SysConf;
import com.erban.main.model.UserGuessNameRecord;
import com.erban.main.model.Users;
import com.erban.main.model.vo.GuessNameRecordDTO;
import com.erban.main.mybatismapper.UserGuessNameRecordMapper;
import com.erban.main.service.SysConfService;
import com.erban.main.service.user.UsersService;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateUtil;
import com.xchat.common.utils.StringUtils;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GuessNameService {

    private static final Logger logger = LoggerFactory.getLogger(GuessNameService.class);
    @Autowired
    private SysConfService sysConfService;
    @Autowired
    private JedisService jedisService;
    @Autowired
    private UserGuessNameRecordMapper guessNameRecordMapper;
    @Autowired
    private UsersService usersService;

    /**
     * 返回提示的图片
     * @return
     */
    public String prompt() {
        Date startDate = getStartDate();
        Date now = new Date();
        int days = 1;
        if (now.getTime() < startDate.getTime()) {
            // 还没到活动时间
            days = 1;
        } else {
            int activityDays = getDays();
            int d = days + (int) ((now.getTime() - startDate.getTime()) / (1000 * 3600 * 24));
            if (d > activityDays) {
                // 超过了活动天数
                days = activityDays;
            } else {
                days = d;
            }
        }
        // 获取提示图片
        SysConf conf = sysConfService.getSysConfById(Constant.SysConfId.guess_name_prompt_pic + days);
        return conf.getConfigValue();
    }

    /**
     * 猜名字--一个用户只能猜一次
     * @param uid
     * @param name
     */
    public BusiResult guess(Long uid, String name) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        Users users = usersService.getUsersByUid(uid);
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        if(StringUtils.isBlank(name)) {
            return new BusiResult(BusiStatus.PARAMERROR, "名字不能为空", null);
        }
        // 活动开始时间
        Date startDate = getStartDate();
        long nowTime = System.currentTimeMillis();
        if (nowTime < startDate.getTime()) {
            return new BusiResult(BusiStatus.ACTIVITYNOTSTART);
        }
        // 活动结束时间
        Date endDate = DateUtil.addDay(startDate, getDays());
        if (nowTime > endDate.getTime()) {
            return new BusiResult(BusiStatus.ACTIVITYNOTEND);
        }
        // 参数校验结束
        String result = jedisService.hget(RedisKey.user_guess_name_record.getKey(), uid.toString());
        if(StringUtils.isBlank(result)) {
            // 还没有猜过, 添加记录
            UserGuessNameRecord record = new UserGuessNameRecord();
            record.setCreateDate(new Date());
            record.setUid(uid);
            record.setGuessName(name);
            guessNameRecordMapper.insert(record);
            jedisService.hset(RedisKey.user_guess_name_record.getKey(), uid.toString(), name);
            return new BusiResult(BusiStatus.SUCCESS, "您已提交成功", null);
        } else {
            // 已经猜过了
            return new BusiResult(BusiStatus.ACTIVITYNOTEND, "您已经提交过了", null);
        }
    }

    public List<GuessNameRecordDTO> export(Integer pageNum) {
        //
        pageNum = (pageNum == null || pageNum < 1) ? 0 : pageNum - 1;
        return guessNameRecordMapper.listAll(pageNum);
    }

    /**
     * 获取活动开始时间
     * @return
     */
    public Date getStartDate() {
        SysConf conf = sysConfService.getSysConfById(Constant.SysConfId.guess_name_start_date);
        String date = conf.getConfigValue();
        try {
            return DateUtil.str2Date(date, DateUtil.DateFormat.YYYY_MM_DD);
        } catch (Exception e) {
            throw new RuntimeException("时间格式化失败...");
        }
    }

    /**
     * 获取活动天数
     * @return
     */
    public int getDays () {
        SysConf conf = sysConfService.getSysConfById(Constant.SysConfId.guess_name_days);
        String days = conf.getConfigValue();
        return Integer.valueOf(days);
    }
}
