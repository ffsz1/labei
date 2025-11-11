package com.juxiao.xchat.service.task.family.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.dao.family.dao.FamilyExitRecordDAO;
import com.juxiao.xchat.dao.family.dao.FamilyGiftRecordDAO;
import com.juxiao.xchat.dao.family.dao.FamilyJoinDAO;
import com.juxiao.xchat.dao.family.dao.FamilyTeamRecordDAO;
import com.juxiao.xchat.dao.family.domain.FamilyExitRecordDO;
import com.juxiao.xchat.dao.family.domain.FamilyTeamRecordDO;
import com.juxiao.xchat.dao.family.dto.FamilyTeamRecordDTO;
import com.juxiao.xchat.dao.task.TaskDao;
import com.juxiao.xchat.manager.external.netease.NetEaseTeamManager;
import com.juxiao.xchat.manager.external.netease.ret.BaseTeamRet;
import com.juxiao.xchat.manager.external.netease.utils.NetEaseTeamUtils;
import com.juxiao.xchat.service.task.family.FamilyTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author laizhilong
 * @Title: 家族定时任务
 * @Package com.juxiao.xchat.service.task.family.impl
 * @date 2018/9/2
 * @time 15:57
 */
@Service
@Slf4j
public class FamilyTaskServiceImpl implements FamilyTaskService {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private FamilyExitRecordDAO familyExitRecordDAO;

    @Autowired
    private FamilyJoinDAO familyJoinDAO;

    @Autowired
    private FamilyGiftRecordDAO familyGiftRecordDAO;

    @Autowired
    private NetEaseTeamManager netEaseTeamManager;


    @Autowired
    private Gson gson;

    @Autowired
    private FamilyTeamRecordDAO familyTeamRecordDAO;

    /**
     * 定时刷新是否退出家族
     * 7天后自动退出
     */
    @Override
    public void refreshStatus() {
        log.info("[定时刷新不同意退出家族定时任务开始 ->主动退群]");
        long start = System.currentTimeMillis();
        List<FamilyExitRecordDO> familyExitRecordDOS = this.taskDao.findFamilyExitRecord();
        familyExitRecordDOS.forEach(item ->{
            //7天后自动退出
            if(DateTimeUtils.isLastWeek(item.getUpdateTime())) {
                try {
                    log.info("[定时刷新不同意退出家族定时任务开始 ->主动退群] 退群UID:{},家族ID:{},群聊TID:{}",item.getUid(),item.getFamilyId(),item.getRoomId());
                    BaseTeamRet baseTeamRet = NetEaseTeamUtils.buildReturn(netEaseTeamManager.leave(item.getRoomId().toString(),item.getUid().toString()));
                    if (baseTeamRet.getCode() != WebServiceCode.SUCCESS.getValue()) {
                        log.error("[ 定时刷新不同意退出家族定时任务 ]调用云信->主动退群回调出现错误 -> 错误信息:>{} , 耗时:>{} /ms", gson.toJson(baseTeamRet), (System.currentTimeMillis() - start));
                    }else{
                        int status = familyExitRecordDAO.updateStatusByTeamIdAndUid(item.getTeamId(), item.getUid());
                        if (status > 0) {
                            familyGiftRecordDAO.removeUserGiftRecord(item.getTeamId(), item.getUid());
                            familyJoinDAO.deleteByUidAndTeamId(item.getUid(), item.getTeamId());
                        }
                    }
                } catch (Exception e) {
                    log.error("[ 定时刷新不同意退出家族定时任务 ]->主动退群回调出现异常 -> 异常信息:>{} , 耗时:>{} /ms", e, (System.currentTimeMillis() - start));
                }
            }
        });
        log.info("[定时刷新不同意退出家族定时任务结束 - > ->主动退群] -> 耗时:{} /ms" , (System.currentTimeMillis() - start));
    }

    /**
     * 统计并清除威望
     */
    @Override
    public void countCleanPrestige() {
        log.info("[定时刷新统计并清除威望定时任务开始]");
        long startTime = System.currentTimeMillis();
        List<FamilyTeamRecordDO> familyTeamRecordList = new ArrayList<>();
        List<FamilyTeamRecordDTO> familyTeamRecordDTOS = familyGiftRecordDAO.selectByTeamGiftRecord();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        familyTeamRecordDTOS.forEach(item -> {
            String now = LocalDateTime.now().minusDays(1).format(dateTimeFormatter);
            FamilyTeamRecordDO familyTeamRecordDO = new FamilyTeamRecordDO();
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDate localDate = LocalDate.now().minusDays(1);
            ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
            familyTeamRecordDO.setCreateTime(Date.from(zdt.toInstant()));
            familyTeamRecordDO.setNum(item.getNum());
            familyTeamRecordDO.setTeamId(item.getTeamId());
            familyTeamRecordDO.setYears(now);
            familyTeamRecordList.add(familyTeamRecordDO);
        });
        int status = familyTeamRecordDAO.batchInsert(familyTeamRecordList);
        if(status > 0){
            familyGiftRecordDAO.cleanTeamGiftRecord();
        }
        log.info("[定时刷新统计并清除威望定时任务结束] -> 耗时:{} /ms" , (System.currentTimeMillis() - startTime));
    }
}
