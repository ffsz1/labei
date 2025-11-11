package com.juxiao.xchat.manager.common.guild.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.guild.GuildDailyTurnoverReportDao;
import com.juxiao.xchat.dao.guild.GuildDao;
import com.juxiao.xchat.dao.guild.GuildHallDao;
import com.juxiao.xchat.dao.guild.GuildHallMemberDao;
import com.juxiao.xchat.dao.guild.domain.GuildDailyTurnoverReportDO;
import com.juxiao.xchat.dao.guild.dto.*;
import com.juxiao.xchat.dao.guild.enumeration.GuildHallMemberType;
import com.juxiao.xchat.dao.room.StatRoomCtrbSumDao;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.guild.GuildHallMemberManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 描述：
 *
 * @创建时间： 2020/10/10 16:59
 * @作者： carl
 */
@Slf4j
@Service
public class GuildHallMemberManagerImpl implements GuildHallMemberManager {

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private Gson gson;

    @Autowired
    private GuildHallMemberDao guildHallMemberDao;

    @Autowired
    private StatRoomCtrbSumDao statRoomCtrbSumDao;

    @Autowired
    private GuildDailyTurnoverReportDao guildDailyTurnoverReportDao;

    @Autowired
    private GuildDao guildDao;

    @Autowired
    private GuildHallDao guildHallDao;

    /**
     * 获取“我的公会”页面的信息
     * @param uid
     * @return
     */
    @Override
    public GuildHallMemberDTO getCommonInfo(Long uid) {
        String jsdata = redisManager.get(RedisKey.guild_member.getKey(String.valueOf(uid)));
        if (StringUtils.isNotBlank(jsdata)) {
            return gson.fromJson(jsdata, GuildHallMemberDTO.class);
        }

        GuildHallMemberDTO guildHallMemberDTO = guildHallMemberDao.getGuildMemberInfo(uid);
        if (guildHallMemberDTO == null) {
            return null;
        }

        //是否会长
        if (uid.compareTo(guildHallMemberDTO.getGuildPresidentUid()) == 0) {
            TurnoverDWADto guildTurnover = this.getTurnover_president(guildHallMemberDTO.getGuildId());
            guildHallMemberDTO.setGuildTurnover(guildTurnover);
        }
        //是否厅主
        if (guildHallMemberDTO.getHallUid() != null && uid.compareTo(guildHallMemberDTO.getHallUid()) == 0) {
            TurnoverDWADto hallTurnover = this.getTurnover_room(guildHallMemberDTO.getGuildId(), guildHallMemberDTO.getHallId());
            guildHallMemberDTO.setHallTurnover(hallTurnover);
        }

        //获取个人流水数据
        TurnoverDWADto memberTurnover = this.getTurnover_user(guildHallMemberDTO.getGuildId(), guildHallMemberDTO.getHallId(), guildHallMemberDTO.getMemberId());
        guildHallMemberDTO.setMemberTurnover(memberTurnover);

        redisManager.set(RedisKey.guild_member.getKey(String.valueOf(uid)), gson.toJson(guildHallMemberDTO), 5, TimeUnit.MINUTES);

        return guildHallMemberDTO;
    }

    @Override
    public boolean hasJoinGuild(Long uid) {
        String jsdata = redisManager.hget(RedisKey.guild_member_exist.getKey(), String.valueOf(uid));
        if (StringUtils.isNotBlank(jsdata)) {
            return Boolean.valueOf(jsdata);
        }

        int count = guildHallMemberDao.countByUid(uid);   //检查是否成员
        boolean hasJoin = count > 0;

        redisManager.hset(RedisKey.guild_member_exist.getKey(), String.valueOf(uid), String.valueOf(hasJoin));

        return hasJoin;
    }

    /**
     * 目前默认获取30天以内的数据，算上今天
     * @param uid
     * @return
     */
    @Override
    public List<GuildDailyTurnoverReportDO> getMemberTurnovers(Long uid, Long hallId) {
        String jsdata = redisManager.get(RedisKey.guild_member_turnover.getKey(String.valueOf(uid)));
        if (StringUtils.isNotBlank(jsdata)) {
            return gson.fromJson(jsdata, new TypeToken<List<GuildDailyTurnoverReportDO>>(){}.getType());
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startdate = now.minusDays(29);
        String starttime_30days = LocalDateTime.of(startdate.getYear(), startdate.getMonthValue(), startdate.getDayOfMonth(), 0, 0 ,0).format(pattern);

        List<GuildDailyTurnoverReportDO> list = guildDailyTurnoverReportDao.getMemberTurnovers(uid, hallId, starttime_30days, null);

        if (list != null && list.size() > 0) {
            redisManager.set(RedisKey.guild_member_turnover.getKey(String.valueOf(uid)), gson.toJson(list), 10, TimeUnit.MINUTES);
        }

        return list;
    }

    @Override
    public List<GuildHallMemberListDTO> getGuildMembers(Long guildId) throws Exception {
        String jsHall = redisManager.get(RedisKey.guild_member_list.getKey(String.valueOf(guildId)));
        if (StringUtils.isNotBlank(jsHall)) {
            return gson.fromJson(jsHall,new TypeToken<List<GuildHallMemberListDTO>>(){}.getType());
        }

        GuildDTO guildDTO = guildDao.getDTOById(guildId);
        List<GuildHallMemberListDTO> list = guildHallMemberDao.getGuildMembers(guildId);

        // 将会长放到最前面
        Optional<GuildHallMemberListDTO> temp = list.stream().filter(o -> o.getUid().compareTo(guildDTO.getPresidentUid()) == 0).findFirst();
        if (temp.isPresent()) {
            GuildHallMemberListDTO president = temp.get();
            Iterator<GuildHallMemberListDTO> iterator = list.iterator();
            while (iterator.hasNext()) {
                GuildHallMemberListDTO dto = iterator.next();
                if (dto.getUid().compareTo(president.getUid()) == 0) {
                    iterator.remove();
                }
            }

            list.add(0, president);
        }


        if (list != null && list.size() > 0) {
            redisManager.set(RedisKey.guild_member_list.getKey(String.valueOf(guildId)), gson.toJson(list), 5, TimeUnit.MINUTES);
        }

        return list;
    }

    @Override
    public List<GuildHallMemberListDTO> getHallMembers(Long hallId) throws Exception {
        String jsdata = redisManager.get(RedisKey.guild_hall_member_list.getKey(String.valueOf(hallId)));
        if (StringUtils.isNotBlank(jsdata)) {
            return gson.fromJson(jsdata,new TypeToken<List<GuildHallMemberListDTO>>(){}.getType());
        }

        GuildHallDTO hallDTO = guildHallDao.getDTOById(hallId);
        List<GuildHallMemberListDTO> list = guildHallMemberDao.getHallMembers(hallId);

        // 将厅主放到最前面
        Optional<GuildHallMemberListDTO> temp = list.stream().filter(o -> o.getUid().compareTo(hallDTO.getHallUid()) == 0).findFirst();
        if (temp.isPresent()) {
            GuildHallMemberListDTO hallMaster = temp.get();
            Iterator<GuildHallMemberListDTO> iterator = list.iterator();
            while (iterator.hasNext()) {
                GuildHallMemberListDTO dto = iterator.next();
                if (dto.getUid().compareTo(hallMaster.getUid()) == 0) {
                    iterator.remove();
                }
            }

            hallMaster.setType(1);
            list.add(0, hallMaster);
        }

        if (list != null && list.size() > 0) {
            redisManager.set(RedisKey.guild_hall_member_list.getKey(String.valueOf(hallId)), gson.toJson(list), 5, TimeUnit.MINUTES);
        }
        return list;
    }

    /**
     * 获取公会指定的日、周、汇总流水数据
     * @return
     */
    @Override
    public TurnoverDWADto getTurnover_president(Long guildId) {
        String jsdata = redisManager.get(RedisKey.guild_period_turnover.getKey(String.valueOf(guildId)));
        if (StringUtils.isNotBlank(jsdata)) {
            return gson.fromJson(jsdata, TurnoverDWADto.class);
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String starttime_today = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 0, 0 ,0).format(pattern);
        LocalDateTime week_datetime = now.minusDays(6);
        String starttime_week = LocalDateTime.of(week_datetime.getYear(), week_datetime.getMonthValue(), week_datetime.getDayOfMonth(), 0, 0 ,0).format(pattern);

        Long guildFlow_today = guildDailyTurnoverReportDao.sumGuildDailyTurnover(null, null, guildId, starttime_today, null);
        Long guildFlow_week = guildDailyTurnoverReportDao.sumGuildDailyTurnover(null, null, guildId, starttime_week, null);
        Long guildFlow_all = guildDailyTurnoverReportDao.sumGuildDailyTurnover(null, null, guildId, null, null);

        TurnoverDWADto turnoverDWADto = new TurnoverDWADto();
        turnoverDWADto.setGold_day(guildFlow_today);
        turnoverDWADto.setGold_week(guildFlow_week);
        turnoverDWADto.setGold_all(guildFlow_all);

        redisManager.set(RedisKey.guild_period_turnover.getKey(String.valueOf(guildId)), gson.toJson(turnoverDWADto), 5, TimeUnit.MINUTES);

        return turnoverDWADto;
    }

    /**
     * 获取指定房间的日、周、汇总流水数据
     * @param hallId 厅id
     * @return
     */
    @Override
    public TurnoverDWADto getTurnover_room(Long guildId, Long hallId) {
        String jsdata = redisManager.get(RedisKey.guild_hall_period_turnover.getKey(String.valueOf(hallId)));
        if (StringUtils.isNotBlank(jsdata)) {
            return gson.fromJson(jsdata, TurnoverDWADto.class);
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String starttime_today = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 0, 0 ,0).format(pattern);
        LocalDateTime week_datetime = now.minusDays(6);
        String starttime_week = LocalDateTime.of(week_datetime.getYear(), week_datetime.getMonthValue(), week_datetime.getDayOfMonth(), 0, 0 ,0).format(pattern);

        Long hallFlow_today = guildDailyTurnoverReportDao.sumGuildDailyTurnover(null, hallId, guildId, starttime_today, null);
        Long hallFlow_week = guildDailyTurnoverReportDao.sumGuildDailyTurnover(null, hallId, guildId, starttime_week, null);
        Long hallFlow_all = guildDailyTurnoverReportDao.sumGuildDailyTurnover(null, hallId, guildId, null, null);

        TurnoverDWADto turnoverDWADto = new TurnoverDWADto();
        turnoverDWADto.setGold_day(hallFlow_today);
        turnoverDWADto.setGold_week(hallFlow_week);
        turnoverDWADto.setGold_all(hallFlow_all);

        redisManager.set(RedisKey.guild_hall_period_turnover.getKey(String.valueOf(hallId)), gson.toJson(turnoverDWADto), 5, TimeUnit.MINUTES);

        return turnoverDWADto;
    }

    /**
     * 获取用户指定房间的日、周、汇总流水数据
     * @param guildId 公会id
     * @param hallId 厅id
     * @param memberId 成员id
     * @return
     */
    @Override
    public TurnoverDWADto getTurnover_user(Long guildId, Long hallId, Long memberId) {
        String jsdata = redisManager.get(RedisKey.guild_hall_member_period_turnover.getKey(String.valueOf(hallId) + "_" + String.valueOf(memberId)));
        if (StringUtils.isNotBlank(jsdata)) {
            return gson.fromJson(jsdata, TurnoverDWADto.class);
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String starttime_today = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 0, 0 ,0).format(pattern);
        LocalDateTime week_datetime = now.minusDays(6);
        String starttime_week = LocalDateTime.of(week_datetime.getYear(), week_datetime.getMonthValue(), week_datetime.getDayOfMonth(), 0, 0 ,0).format(pattern);

        Long userFlow_today = guildDailyTurnoverReportDao.sumGuildDailyTurnover(memberId, hallId, guildId, starttime_today, null);
        Long userFlow_week = guildDailyTurnoverReportDao.sumGuildDailyTurnover(memberId, hallId, guildId, starttime_week, null);
        Long userFlow_all = guildDailyTurnoverReportDao.sumGuildDailyTurnover(memberId, hallId, guildId, null, null);

        TurnoverDWADto turnoverDWADto = new TurnoverDWADto();
        turnoverDWADto.setGold_day(userFlow_today);
        turnoverDWADto.setGold_week(userFlow_week);
        turnoverDWADto.setGold_all(userFlow_all);

        redisManager.set(RedisKey.guild_hall_member_period_turnover.getKey(String.valueOf(hallId) + "_" + String.valueOf(memberId)), gson.toJson(turnoverDWADto), 5, TimeUnit.MINUTES);

        return turnoverDWADto;
    }
}
