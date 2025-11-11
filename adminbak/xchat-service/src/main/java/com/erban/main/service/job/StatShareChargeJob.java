package com.erban.main.service.job;

import com.erban.main.model.*;
import com.erban.main.mybatismapper.StatShareChargeMapper;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.user.UserPurseService;
import com.erban.main.service.user.UsersService;
import com.xchat.common.utils.GetTimeUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatShareChargeJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(CheckRoomExceptionJob.class);

    @Autowired
    private UsersService usersService;
    @Autowired
    private StatShareChargeMapper statShareChargeMapper;
    @Autowired
    private UserPurseService userPurseService;
    @Autowired
    private BillRecordService billRecordService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("定时任务：每日分享人领注册人数以及首冲人数");
        //根据分享人进行统计
        statShareChargeByShareUid();
        //根据分享渠道进行统计
        statShareChargeByShareChannel();
        logger.info("查询每日充值人员是否是被分享人的首充。");
        Map<Long, Integer> map = new HashMap<>();
        Map<Long, Integer> mapMoreThanThree = new HashMap<>();
        Map<Byte, Integer> map2 = new HashMap<>();
        Map<Byte, Integer> mapMoreThanThree2 = new HashMap<>();
        List<UserPurse> userPurseList = userPurseService.getPurseByShareCharge();
        if (!CollectionUtils.isEmpty(userPurseList)) {
            for (UserPurse userPurse : userPurseList) {
                Users users = usersService.getUsersByUid(userPurse.getUid());
                //统计shareUid分享人注册过来的首充记录
                if (users.getShareUid() != null) {
                    Integer num = map.put(users.getShareUid(), 1);
                    List<BillRecord> billRecordList = billRecordService.getShareChargeByMoreThanThree(userPurse.getUid());
                    if (!CollectionUtils.isEmpty(billRecordList)) {
                        long moneyNum = 0;
                        for (BillRecord billRecord : billRecordList) {
                            moneyNum += billRecord.getMoney();
                        }
                        if (moneyNum >= 30) {
                            Integer num2 = mapMoreThanThree.put(users.getShareUid(), 1);
                            if (num2 != null) {
                                mapMoreThanThree.put(users.getShareUid(), num2 + 1);
                            }
                        }
                    }
                    if (num != null) {
                        map.put(users.getShareUid(), num + 1);
                    }
                }
                //统计shareChannel分享渠道注册的首充记录
                if (users.getShareChannel() != null) {
                    Integer num = map2.put(users.getShareChannel(), 1);
                    List<BillRecord> billRecordList = billRecordService.getShareChargeByMoreThanThree(userPurse.getUid());
                    if (!CollectionUtils.isEmpty(billRecordList)) {
                        long moneyNum = 0;
                        for (BillRecord billRecord : billRecordList) {
                            moneyNum += billRecord.getMoney();
                        }
                        if (moneyNum >= 30) {
                            Integer num2 = mapMoreThanThree2.put(users.getShareChannel(), 1);
                            if (num2 != null) {
                                mapMoreThanThree2.put(users.getShareChannel(), num2 + 1);
                            }
                        }
                    }
                    if (num != null) {
                        map2.put(users.getShareChannel(), num + 1);
                    }
                }
            }
            for (Map.Entry<Long, Integer> entry : map.entrySet()) {
                logger.info("统计每日首充人数是否是分享人带领注册的，shareUid:" + entry.getKey() + "分享人当日的首充人数：" + entry.getValue());
                Users users = usersService.getUsersByUid(entry.getKey());
                StatShareCharge statShareCharge = new StatShareCharge();
                statShareCharge.setErbanNo(users.getErbanNo().intValue());
                statShareCharge.setNick(users.getNick());
                statShareCharge.setChargeCount(entry.getValue());
                statShareCharge.setUid(users.getUid());
                statShareCharge.setStatDate(new Date());
                for (Map.Entry<Long, Integer> mapEntry : mapMoreThanThree.entrySet()) {
                    if (entry.getKey().equals(mapEntry.getKey())) {
                        statShareCharge.setMorethanChargeCount(mapEntry.getValue());
                        break;
                    }
                }
                saveOrUpdateShareChargeByShareUid(statShareCharge);
            }
            for (Map.Entry<Byte, Integer> entry : map2.entrySet()) {
                logger.info("统计每日首充人数是否是分享人带领注册的，shareChannel:" + entry.getKey() + "分享人当日的首充人数：" + entry.getValue());
                StatShareCharge statShareCharge = new StatShareCharge();
                statShareCharge.setChargeCount(entry.getValue());
                statShareCharge.setShareChannel(entry.getKey());
                statShareCharge.setStatDate(new Date());
                for (Map.Entry<Byte, Integer> mapEntry : mapMoreThanThree2.entrySet()) {
                    if (entry.getKey().equals(mapEntry.getKey())) {
                        statShareCharge.setMorethanChargeCount(mapEntry.getValue());
                        break;
                    }
                }
                saveOrUpdateShareChargeByShareChannel(statShareCharge);
            }
        }
    }

    private void statShareChargeByShareChannel() {
        List<Users> usersList = usersService.getShareChargeByShareChannel();
        if (!CollectionUtils.isEmpty(usersList)) {
            Map<Byte, Integer> map = new HashMap<>();
            for (Users users : usersList) {
                Integer num = map.put(users.getShareChannel(), 1);
                if (num != null) {
                    map.put(users.getShareChannel(), num + 1);
                }
            }
            for (Map.Entry<Byte, Integer> entry : map.entrySet()) {
                logger.info("统计分享人注册人数,shareUid:" + entry.getKey() + ",成功注册人数:" + entry.getValue());
                StatShareCharge statShareCharge = new StatShareCharge();
                statShareCharge.setShareChannel(entry.getKey());
                statShareCharge.setRegisterCount(entry.getValue());
                statShareCharge.setStatDate(new Date());
                statShareChargeMapper.insertSelective(statShareCharge);
            }
        }
    }

    private void statShareChargeByShareUid() {
        List<Users> usersList = usersService.getShareCharge();
        if (!CollectionUtils.isEmpty(usersList)) {
            Map<Long, Integer> map = new HashMap<>();
            for (Users users : usersList) {
                Integer num = map.put(users.getShareUid(), 1);
                if (num != null) {
                    map.put(users.getShareUid(), num + 1);
                }
            }
            for (Map.Entry<Long, Integer> entry : map.entrySet()) {
                logger.info("统计分享人注册人数,shareChannel:" + entry.getKey() + ",成功注册人数:" + entry.getValue());
                StatShareCharge statShareCharge = new StatShareCharge();
                statShareCharge.setUid(entry.getKey());
                Users users = usersService.getUsersByUid(entry.getKey());
                statShareCharge.setNick(users.getNick());
                statShareCharge.setErbanNo(users.getErbanNo().intValue());
                statShareCharge.setRegisterCount(entry.getValue());
                statShareCharge.setStatDate(new Date());
                statShareChargeMapper.insertSelective(statShareCharge);
            }
        }
    }

    private void saveOrUpdateShareChargeByShareChannel(StatShareCharge statShareCharge) {
        StatShareChargeExample example = new StatShareChargeExample();
        example.createCriteria().andStatDateBetween(GetTimeUtils.getTimesnight(0), GetTimeUtils.getTimesnight(24)).andShareChannelEqualTo(statShareCharge.getShareChannel());
        List<StatShareCharge> statShareChargeList = statShareChargeMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(statShareChargeList)) {
            statShareChargeMapper.insertSelective(statShareCharge);
        } else {
            statShareCharge.setShareChargeId(statShareChargeList.get(0).getShareChargeId());
            statShareChargeMapper.updateByPrimaryKeySelective(statShareCharge);
        }

    }

    private void saveOrUpdateShareChargeByShareUid(StatShareCharge statShareCharge) {
        StatShareChargeExample example = new StatShareChargeExample();
        example.createCriteria().andStatDateBetween(GetTimeUtils.getTimesnight(0), GetTimeUtils.getTimesnight(24)).andUidEqualTo(statShareCharge.getUid());
        List<StatShareCharge> statShareChargeList = statShareChargeMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(statShareChargeList)) {
            statShareChargeMapper.insertSelective(statShareCharge);
        } else {
            statShareCharge.setShareChargeId(statShareChargeList.get(0).getShareChargeId());
            statShareChargeMapper.updateByPrimaryKeySelective(statShareCharge);
        }
    }
}
