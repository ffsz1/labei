package com.juxiao.xchat.service.task.guild.impl;

import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.dao.guild.GuildDailyTurnoverReportDao;
import com.juxiao.xchat.dao.guild.GuildDao;
import com.juxiao.xchat.dao.guild.GuildHallDao;
import com.juxiao.xchat.dao.guild.GuildHallMemberDao;
import com.juxiao.xchat.dao.guild.domain.GuildDO;
import com.juxiao.xchat.dao.guild.domain.GuildDailyTurnoverReportDO;
import com.juxiao.xchat.dao.guild.domain.GuildHallDO;
import com.juxiao.xchat.dao.guild.domain.GuildHallMemberDO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.service.task.guild.GuildDailyTurnoverReportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * 描述：公会流水相关处理
 *
 * @创建时间： 2020/10/16 17:15
 * @作者： carl
 */
@Slf4j
@Service
public class GuildDailyTurnoverReportServiceImpl implements GuildDailyTurnoverReportService {

    @Autowired
    private Gson gson;

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private GuildDao guildDao;

    @Autowired
    private GuildHallDao guildHallDao;

    @Autowired
    private GuildHallMemberDao guildHallMemberDao;

    @Autowired
    private GuildDailyTurnoverReportDao guildDailyTurnoverReportDao;

    /**
     * 次日生成或更新昨日数据
     */
    @Override
    public void updateYesterdayTurnovers() {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        List<GuildDO> guilds = guildDao.getValidList();
        for (GuildDO guildDO : guilds) {
            this.updateGuildTurnovers(yesterday, guildDO);
        }
    }

    @Override
    public void updateTodayTurnovers() {
        log.info("实时流水更新开始时间：{}", DateUtils.dateFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        LocalDateTime date = LocalDateTime.now();
        List<GuildDO> guilds = guildDao.getValidList();
        for (GuildDO guildDO : guilds) {
            this.updateGuildTurnovers(date, guildDO);
        }
        log.info("实时流水更新结束时间：{}", DateUtils.dateFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }


    /**
     * 统计公会的流水
     * @param date 统计日期
     * @param guildDO 公会记录
     */
    private void updateGuildTurnovers(LocalDateTime date, GuildDO guildDO) {
        Date now = new Date();
        LocalDateTime yesterday_start = LocalDateTime.of(date.getYear(), date.getMonth(),date.getDayOfMonth(),0,0,0);
        LocalDateTime yesterday_end = LocalDateTime.of(date.getYear(), date.getMonth(),date.getDayOfMonth(),23,59,59);

        String reportDate_start = yesterday_start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String reportDate_end = yesterday_end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Date reportDate = Date.from(yesterday_start.atZone(ZoneId.systemDefault()).toInstant());

        log.info("准备开始统计公会流水({}): > {}", reportDate_start, gson.toJson(guildDO));

        String lock = redisManager.lock(RedisKey.guild_daily_turnover_report_lock.getKey(String.valueOf(guildDO.getId())), 60 * 1000);
        if (StringUtils.isEmpty(lock)) {
            log.warn("统计公会流水获取不到锁({}): > {}", reportDate_start, gson.toJson(guildDO));
            return;
        }

        try {
            GuildDailyTurnoverReportDO reportDO = guildDailyTurnoverReportDao.getByParams(null, null, guildDO.getId(), reportDate_start);
            Long gold = guildDailyTurnoverReportDao.sumGuildTurnover(reportDate_start, reportDate_end, guildDO.getId());

            if (reportDO == null) {
                reportDO = new GuildDailyTurnoverReportDO();
                reportDO.setGold(gold);
                reportDO.setCreateTime(now);
                reportDO.setReportDate(reportDate);
                reportDO.setGuildId(guildDO.getId());
                guildDailyTurnoverReportDao.insert(reportDO);
            }
            else {
                reportDO.setGold(gold);
                guildDailyTurnoverReportDao.updateByPrimaryKey(reportDO);
            }

            this.updateHallTurnovers(guildDO, reportDate_start, reportDate_end, now, reportDate);
        }
        finally {
            redisManager.unlock(RedisKey.guild_daily_turnover_report_lock.getKey(String.valueOf(guildDO.getId())), lock);
        }

        log.info("统计公会流水完成({}): > {}", reportDate_start, gson.toJson(guildDO));
    }

    private void updateHallTurnovers(GuildDO guildDO, String reportDate_start, String reportDate_end, Date now, Date reportDate) {
        List<GuildHallDO> halls = guildHallDao.findByGuildId(guildDO.getId());
        log.info("准备开始统计公会的厅流水({}): > {}", reportDate_start, gson.toJson(guildDO));
        for (GuildHallDO hallDO : halls) {
            GuildDailyTurnoverReportDO reportDO = guildDailyTurnoverReportDao.getByParams(null, hallDO.getId(), guildDO.getId(), reportDate_start);
            Long gold = guildDailyTurnoverReportDao.sumHallTurnover(reportDate_start, reportDate_end, hallDO.getId());

            if (reportDO == null) {
                reportDO = new GuildDailyTurnoverReportDO();
                reportDO.setGold(gold);
                reportDO.setCreateTime(now);
                reportDO.setReportDate(reportDate);
                reportDO.setGuildId(guildDO.getId());
                reportDO.setHallId(hallDO.getId());
                reportDO.setRoomId(hallDO.getRoomId());
                guildDailyTurnoverReportDao.insert(reportDO);
            }
            else {
                reportDO.setGold(gold);
                guildDailyTurnoverReportDao.updateByPrimaryKey(reportDO);
            }

            this.updateMemberTurnovers(hallDO, reportDate_start, reportDate_end, now, reportDate);
        }
        log.info("统计公会的厅流水完成({}): > {}", reportDate_start, gson.toJson(guildDO));
    }

    private void updateMemberTurnovers(GuildHallDO hallDO, String reportDate_start, String reportDate_end, Date now, Date reportDate) {
        log.info("准备开始统计公会的厅的成员流水({}): > {}", reportDate_start, gson.toJson(hallDO));
        List<GuildHallMemberDO> members = guildHallMemberDao.findByHallId(hallDO.getId());
        for (GuildHallMemberDO member : members) {
            GuildDailyTurnoverReportDO reportDO = guildDailyTurnoverReportDao.getByParams(member.getId(), hallDO.getId(), hallDO.getGuildId(), reportDate_start);
            Long gold = guildDailyTurnoverReportDao.sumMemberTurnover(reportDate_start, reportDate_end, hallDO.getId(), member.getId());


            if (reportDO == null) {
                reportDO = new GuildDailyTurnoverReportDO();
                reportDO.setGold(gold);
                reportDO.setCreateTime(now);
                reportDO.setReportDate(reportDate);
                reportDO.setGuildId(hallDO.getGuildId());
                reportDO.setHallId(hallDO.getId());
                reportDO.setRoomId(hallDO.getRoomId());
                reportDO.setMemberId(member.getId());
                guildDailyTurnoverReportDao.insert(reportDO);
            }
            else {
                reportDO.setGold(gold);
                guildDailyTurnoverReportDao.updateByPrimaryKey(reportDO);
            }
        }

        log.info("统计公会的厅的成员流水完成({}): > {}", reportDate_start, gson.toJson(hallDO));
    }
}
