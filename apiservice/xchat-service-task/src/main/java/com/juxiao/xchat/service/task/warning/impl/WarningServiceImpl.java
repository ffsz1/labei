package com.juxiao.xchat.service.task.warning.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.base.utils.HttpUtils;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.task.TaskDao;
import com.juxiao.xchat.dao.task.dto.UserChargeCountDTO;
import com.juxiao.xchat.dao.task.dto.UserSumDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.conf.SystemConf;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.manager.external.dingtalk.DingtalkChatbotManager;
import com.juxiao.xchat.manager.external.dingtalk.bo.DingtalkMessageBO;
import com.juxiao.xchat.manager.external.dingtalk.bo.DingtalkTextMessageBO;
import com.juxiao.xchat.manager.external.dingtalk.conf.DingTalkConf;
import com.juxiao.xchat.manager.external.netease.NetEaseSmsManager;
import com.juxiao.xchat.service.task.warning.WarningService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class WarningServiceImpl implements WarningService {

    private final List<String> list = Lists.newArrayList("http://域名/home/v2/tagindex");
    private final Logger logger = LoggerFactory.getLogger(WarningServiceImpl.class);
    @Autowired
    private Gson gson;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private DingTalkConf dingTalkConf;
    @Autowired
    private SystemConf systemConf;
    @Autowired
    private DingtalkChatbotManager dingtalkChatbotManager;
    @Autowired
    private LevelManager levelManager;
    @Resource(name = "CaihSmsManager")
    private NetEaseSmsManager netEaseSmsManager;
    @Autowired
    private RedisManager redisManager;


    @Override
    public void checkHourCharge() {
        String value;
        Date now = new Date();
        Date startTime = DateTimeUtils.setTime(now, 0, 0, 0);
        Date endTime = DateTimeUtils.setTime(now, 23, 59, 59);
        List<UserSumDTO> chargeSums = taskDao.listUserChargeSum4New(100000, startTime, endTime);
        this.checkChargeSums(chargeSums, "新用户");

        chargeSums = taskDao.listUserChargeSum4Old(200000, startTime, endTime);
        this.checkChargeSums(chargeSums, "老用户");

        List<UserChargeCountDTO> chargeCounts = taskDao.listUserChargeCount4New(startTime, endTime);
        if (chargeCounts == null || chargeCounts.size() == 0) {
            return;
        }

        String chargeMsgFormat = "[ %s服充值预警 ] %s(新用户 %s)今天充值达到%s次，预警级别：中";
        for (UserChargeCountDTO countDto : chargeCounts) {
            value = redisManager.hget(RedisKey.check_excess.getKey("charge_count"), String.valueOf(countDto.getUid()));
            if (StringUtils.isNotBlank(value)) {
                continue;
            }

            String text = String.format(chargeMsgFormat, systemConf.getEnvName(), countDto.getNick(), countDto.getErbanNo(), countDto.getChargeCount());
            DingtalkMessageBO messageBo = new DingtalkTextMessageBO(text, dingTalkConf.getPm(), false);
            dingtalkChatbotManager.send(dingTalkConf.getDevelopChatbot(), messageBo);
            redisManager.hincrBy(RedisKey.check_excess.getKey("charge_count"), String.valueOf(countDto.getUid()), countDto.getChargeCount().longValue());
        }
    }

    @Override
    public void checkIosCharge() {
        String value;
        Date now = new Date();
        Date startTime = DateTimeUtils.setTime(now, 0, 0, 0);
        Date endTime = DateTimeUtils.setTime(now, 23, 59, 59);
        List<UserChargeCountDTO> chargeCounts = taskDao.listUserIosChargeCount(startTime, endTime);
        int experienceLevel;
        String chargeCountMsgFormat = "[ %s服内购预警 ] %s(%s等级%s)今天内购充值%s次，预警级别：中";
        for (UserChargeCountDTO countDto : chargeCounts) {
            value = redisManager.hget(RedisKey.check_excess.getKey("ios_charge_count"), String.valueOf(countDto.getUid()));
            if (StringUtils.isNotBlank(value)) {
                continue;
            }

            experienceLevel = levelManager.getUserExperienceLevelSeq(countDto.getUid());
            String text = String.format(chargeCountMsgFormat, systemConf.getEnvName(), countDto.getNick(), countDto.getErbanNo(), experienceLevel, countDto.getChargeCount());
            DingtalkMessageBO messageBo = new DingtalkTextMessageBO(text, dingTalkConf.getPm(), false);
            dingtalkChatbotManager.send(dingTalkConf.getDevelopChatbot(), messageBo);
            redisManager.hincrBy(RedisKey.check_excess.getKey("ios_charge_count"), String.valueOf(countDto.getUid()), countDto.getChargeCount().longValue());
        }
    }

    @Override
    public void checkDayCharge() {
        Date now = DateTimeUtils.addDay(new Date(), -7);
        Date startTime = DateTimeUtils.setTime(now, 0, 0, 0);
        Date endTime = DateTimeUtils.setTime(new Date(), 23, 59, 59);
        List<UserSumDTO> chargeSums = taskDao.listUserChargeSum4New(500000, startTime, endTime);
        if (chargeSums == null || chargeSums.size() == 0) {
            return;
        }

        String chargeMagFormat = "[ %s服充值预警 ] %s(新用户%s)7天累计充值%s元，预警级别：中";
        for (UserSumDTO sumDto : chargeSums) {
            String text = MessageFormat.format(chargeMagFormat, systemConf.getEnvName(), sumDto.getNick(), sumDto.getErbanNo(), sumDto.getSum() / 10);
            DingtalkMessageBO messageBo = new DingtalkTextMessageBO(text, dingTalkConf.getPm(), false);
            dingtalkChatbotManager.send(dingTalkConf.getDevelopChatbot(), messageBo);
        }

    }

    @Override
    public void checkSendGift() {
        Date date = new Date();
        Date startTime = DateTimeUtils.setTime(date, 0, 0, 0);
        Date endTime = DateTimeUtils.setTime(date, 23, 59, 59);
        List<UserSumDTO> list = taskDao.listOldUserGiftSendSum(startTime, endTime);
        this.checkGiftSendSum(list, "老用户");
        list = taskDao.listNewUserGiftSendSum(startTime, endTime);
        this.checkGiftSendSum(list, "新用户");
    }

    @Override
    public void checkRecvGift() {
        Date date = new Date();
        Date startTime = DateTimeUtils.setTime(date, 0, 0, 0);
        Date endTime = DateTimeUtils.setTime(date, 23, 59, 59);
        List<UserSumDTO> list = taskDao.listOldUserGiftRecvSum(startTime, endTime);
        this.checkGiftRecvSum(list, "老用户");
        list = taskDao.listNewUserGiftRecvSum(startTime, endTime);
        this.checkGiftRecvSum(list, "新用户");
    }


    @Override
    public void checkTomcat() {
        if (!"prod".equalsIgnoreCase(systemConf.getEnv())) {
            return;
        }
        try {
            for (String url : list) {
                String result = HttpUtils.get(url, "tagId=9&pageSize=1");
                //解析相应内容（转换成json对象
                WebServiceMessage webServiceMessage = gson.fromJson(result, WebServiceMessage.class);
                if (webServiceMessage.getCode() != 200) {
                    String content = "[ 服务预警 ] 请求" + url + "失败，预警级别：高";
                    netEaseSmsManager.sendAlarmSms(content);
                    DingtalkMessageBO messageBo = new DingtalkTextMessageBO(content, dingTalkConf.getProgrammer(), false);
                    dingtalkChatbotManager.send(dingTalkConf.getDevelopChatbot(), messageBo);
                }
            }
        } catch (Exception e) {
            logger.error("[ 请求tomcat失败 ]", e);
            try {
                netEaseSmsManager.sendAlarmSms("检测请求tomcat异常");
            } catch (Exception e1) {
            }

            DingtalkMessageBO messageBo = new DingtalkTextMessageBO("[ 服务预警 ]检测请求tomcat异常:" + e.getMessage() + "，预警级别：高", dingTalkConf.getProgrammer(), false);
            dingtalkChatbotManager.send(dingTalkConf.getDevelopChatbot(), messageBo);
        }
    }

    private void checkChargeSums(List<UserSumDTO> chargeSums, String userType) {
        if (chargeSums == null || list.size() == 0) {
            return;
        }

        String value;
        String msgFormat = "[ %s服充值预警 ] %s(%s %s)今天充值达到%s元，预警级别：中";
        String rediskey = RedisKey.check_excess.getKey("charge_sum");
        for (UserSumDTO sumDto : chargeSums) {
            value = redisManager.hget(rediskey, String.valueOf(sumDto.getUid()));
            if (StringUtils.isNotBlank(value)) {
                continue;
            }

            String text = String.format(msgFormat, systemConf.getEnvName(), sumDto.getNick(), userType, sumDto.getErbanNo(), (sumDto.getSum() / 10));
            DingtalkMessageBO messageBo = new DingtalkTextMessageBO(text, dingTalkConf.getPm(), false);
            dingtalkChatbotManager.send(dingTalkConf.getDevelopChatbot(), messageBo);
            redisManager.hincrBy(rediskey, String.valueOf(sumDto.getUid()), sumDto.getSum().longValue());
        }
    }

    private void checkGiftSendSum(List<UserSumDTO> giftSums, String userType) {
        if (giftSums == null || giftSums.size() == 0) {
            return;
        }
        String value;
        for (UserSumDTO sumDto : giftSums) {
            value = redisManager.hget(RedisKey.check_excess.getKey("send_gift"), String.valueOf(sumDto.getUid()));
            if (StringUtils.isNotBlank(value)) {
                continue;
            }

            String text = String.format("[ %s服送礼预警 ] %s(%s %s)今天送礼物达到%s金币，预警级别：中", systemConf.getEnvName(), sumDto.getNick(), userType, sumDto.getErbanNo(), sumDto.getSum());
            DingtalkMessageBO messageBo = new DingtalkTextMessageBO(text, dingTalkConf.getPm(), false);
            dingtalkChatbotManager.send(dingTalkConf.getDevelopChatbot(), messageBo);
            redisManager.hincrBy(RedisKey.check_excess.getKey("send_gift"), String.valueOf(sumDto.getUid()), sumDto.getSum().longValue());
        }
    }

    private void checkGiftRecvSum(List<UserSumDTO> giftSums, String userType) {
        if (giftSums == null || giftSums.size() == 0) {
            return;
        }
        String value;
        String text = "[ %s服收礼预警 ] %s(%s %s)今天收礼物达到%s金币，预警级别：中";
        for (UserSumDTO sumDto : giftSums) {
            value = redisManager.hget(RedisKey.check_excess.getKey("recv_gift"), String.valueOf(sumDto.getUid()));
            if (StringUtils.isNotBlank(value)) {
                continue;
            }

            // 如果没发过短信就发送
            text = String.format(text, systemConf.getEnvName(), sumDto.getNick(), userType, sumDto.getErbanNo(), sumDto.getSum());
            DingtalkMessageBO messageBo = new DingtalkTextMessageBO(text, dingTalkConf.getPm(), false);
            dingtalkChatbotManager.send(dingTalkConf.getDevelopChatbot(), messageBo);
            redisManager.hincrBy(RedisKey.check_excess.getKey("recv_gift"), String.valueOf(sumDto.getUid()), sumDto.getSum().longValue());
        }
    }
}
