package com.juxiao.xchat.service.api.event.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.event.RankDao;
import com.juxiao.xchat.dao.event.dto.RankDTO;
import com.juxiao.xchat.dao.event.query.RankQuery;
import com.juxiao.xchat.dao.sysconf.dto.ChannelDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.event.conf.RankDatetype;
import com.juxiao.xchat.manager.common.event.conf.RankType;
import com.juxiao.xchat.manager.common.event.vo.RankVo;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.event.RankService;
import com.juxiao.xchat.service.api.sysconf.ChannelService;
import com.juxiao.xchat.service.api.sysconf.enumeration.ChannelEnum;
import com.juxiao.xchat.service.api.sysconf.vo.RankHomeVo;
import com.juxiao.xchat.service.api.event.vo.RankParentVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class RankServiceImpl implements RankService {

    private static final Date appStartDate = DateUtils.parser("2017-08-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
    private static final Date appEndDate = DateUtils.parser("2037-08-01 00:00:00", "yyyy-MM-dd HH:mm:ss");

    @Autowired
    private Gson gson;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private LevelManager levelManager;
    @Autowired
    private RankDao rankDao;
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private ChannelService channelService;


    @Override
    public RankHomeVo getRankHomeByCache() {
        String result = redisManager.get(RedisKey.rank_home.getKey());
        if (StringUtils.isNotBlank(result)) {
            return gson.fromJson(result, RankHomeVo.class);
        }
        return null;
    }

    @Override
    public RankParentVo getH5RankList(Integer type, Integer dateType, Integer pageSize) {
        type = type == null ? RankType.charm : type;
        dateType = dateType == null ? RankDatetype.day : dateType;
        pageSize = pageSize == null ? 50 : pageSize;
        //
        List<RankVo> list = getRankByCache(type, dateType, pageSize);
        RankParentVo vo = new RankParentVo();
        vo.setRankVoList(list);
        return vo;
    }

    public List<RankVo> getRankByCache(int type, int dateType, int pageSize) {
        String key = type + "" + dateType;
        String rankVoListStr = redisManager.hget(RedisKey.rank.getKey(), key);
        List<RankVo> rankVos = Lists.newArrayList();
        if (StringUtils.isNotEmpty(rankVoListStr)) {
            Type typeJson = new TypeToken<List<RankVo>>(){}.getType();
            rankVos = gson.fromJson(rankVoListStr, typeJson);
        }
        int index = pageSize > rankVos.size() ? rankVos.size() : pageSize;
        rankVos = rankVos.subList(0, index);

        double previous = 0;
        boolean isFirst = true;
        for (RankVo rankVo : rankVos) {
            double current = rankVo.getTotalNum();
            rankVo.setTotalNum(null);
            if (isFirst) {
                rankVo.setDistance(0d);
                isFirst = false;
            } else {
                rankVo.setDistance(previous - current);
            }

            previous = current;
        }
        return rankVos;
    }

    /**
     * 获取排行的key后缀
     * @param type 排行类型
     * @param dateType 时间类型
     * @return
     */
    public String getKey (int type, int dateType) {
        StringBuilder sb = new StringBuilder("_");
        switch (type) {
            case RankType.charm:
                sb.append("star");
                break;
            case RankType.wealth:
                sb.append("noble");
                break;
            case RankType.room:
                sb.append("room");
                break;
        }
        switch (dateType) {
            case RankDatetype.day:
                sb.append("_day");
            case RankDatetype.week:
                sb.append("_week");
                break;
            case RankDatetype.total:
                sb.append("_total");
                break;
        }
        return sb.toString();
    }


    @Override
    public RankParentVo getAuditingRankList(ChannelEnum channelEnum, Integer type, Integer dateType) {
        ChannelDTO channelDto = channelService.getByName(channelEnum);
        if (channelDto == null) {
            return new RankParentVo();
        }

        String redisKey = RedisKey.rank_auditing.getKey();
        String fieldKey = channelDto.getId() + "_" + type + "_" + dateType;
        String json = redisManager.hget(redisKey, fieldKey);
        if (StringUtils.isNotBlank(json)) {
            try {
                return new RankParentVo().setRankVoList(gson.fromJson(json, new TypeToken<List<RankVo>>() {
                }.getType()));
            } catch (Exception e) {
                redisManager.hdel(redisKey, fieldKey);
            }
        }

        type = type == null ? RankType.charm : type;
        dateType = dateType == null ? RankDatetype.day : dateType;

        Date now = new Date();
        RankQuery query = new RankQuery();
        switch (dateType) {
            case RankDatetype.day:
                query.setStartTime(DateTimeUtils.setTime(now, 0, 0, 0));
                query.setEndTime(DateTimeUtils.setTime(now, 23, 59, 59));
                break;
            case RankDatetype.week:
                query.setStartTime(DateUtils.getCurrentMonday());
                query.setEndTime(DateUtils.getCurrentSundayTime(23, 59, 59));
                break;
            case RankDatetype.total:
                query.setStartTime(appStartDate);
                query.setEndTime(appEndDate);
                break;
            default:
                query.setStartTime(DateTimeUtils.setTime(now, 0, 0, 0));
                query.setEndTime(DateTimeUtils.setTime(now, 23, 59, 59));
                break;
        }

        List<Long> uidlist = channelService.listByChannelId(channelDto.getId());
        List<RankDTO> rankList = Lists.newArrayList();
        for (Long uid : uidlist) {
            query.setUid(uid);
            switch (type) {
                case RankType.charm:
                    rankList.addAll(rankDao.getAllStarRankList(query));
                    break;
                case RankType.wealth:
                    rankList.addAll(rankDao.getNobelRankList(query));
                    break;
                case RankType.room:
                    rankList.addAll(rankDao.getAllRoomRankList(query));
                    break;
                default:
            }
        }

        if (rankList.isEmpty()) {
            return new RankParentVo();
        }

        rankList.sort((rankVo1, rankVo2) -> {
            if (rankVo1 == rankVo2) {
                return 0;
            }

            if (rankVo1 == null) {
                return 1;
            }

            if (rankVo2 == null) {
                return -1;
            }

            return rankVo1.getTotalNum().compareTo(rankVo2.getTotalNum());
        });

        List<RankVo> list = Lists.newArrayList();
        UsersDTO user;
        RankVo rankVo;
        double previous = 80;
        boolean isFirst = true;
        for (RankDTO dto : rankList) {
            user = usersManager.getUser(dto.getUid());
            if (user == null) {
                continue;
            }

            rankVo = new RankVo();
            rankVo.setUid(user.getUid());
            rankVo.setGender(user.getGender());
            rankVo.setNick(user.getNick());
            rankVo.setAvatar(user.getAvatar());
            rankVo.setErbanNo(user.getErbanNo());
            rankVo.setTotalNum(null);
            //增加财富等级和魅力等级
            rankVo.setExperLevel(levelManager.getUserExperienceLevelSeq(user.getUid()));
            rankVo.setCharmLevel(levelManager.getUserCharmLevelSeq(user.getUid()));

            double current = dto.getTotalNum();
            if (isFirst) {
                rankVo.setDistance(0d);
                isFirst = false;
            } else {
                rankVo.setDistance(previous - current);
            }
            previous = current;
            list.add(rankVo);
        }
        redisManager.hset(redisKey, fieldKey, gson.toJson(list));
        return new RankParentVo().setRankVoList(list);
    }


    @Override
    public RankVo getMeh5Rank(Integer type, Integer dateType, Long uid) throws WebServiceException{
        if(uid == 0){
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        RankDTO rankDTO = new RankDTO();
        if(type.equals(RankType.charm)){
            rankDTO = rankDao.getUidStarRank(getRandQuery(dateType, uid));
        }else if(type.equals(RankType.wealth)){
            rankDTO = rankDao.getUidNobelRank(getRandQuery(dateType, uid));
        }
        UsersDTO usersDTO = usersManager.getUser(uid);
        if(usersDTO != null){
            return converToRankVo(rankDTO,usersDTO);
        }else{
            RankVo rankVo = new RankVo();
            rankVo.setTotalNum(0L);
            return rankVo;
        }
    }

    private RankVo converToRankVo(RankDTO dto, UsersDTO user) {
        RankVo rankVo = new RankVo();
        rankVo.setUid(user.getUid());
        rankVo.setGender(user.getGender());
        rankVo.setNick(user.getNick());
        rankVo.setAvatar(user.getAvatar());
        rankVo.setErbanNo(user.getErbanNo());
        if(dto != null){
            rankVo.setTotalNum(dto.getTotalNum());
        }else{
            rankVo.setTotalNum(0L);
        }
        //增加财富等级和魅力等级
        rankVo.setExperLevel(levelManager.getUserExperienceLevelSeq(user.getUid()));
        rankVo.setCharmLevel(levelManager.getUserCharmLevelSeq(user.getUid()));
        return rankVo;
    }

    private RankQuery getRandQuery(Integer dateType, Long uid){
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
            case RankDatetype.last_day:
                Date lastDay = DateTimeUtils.getLastDay(now, 1);
                beginDate = DateTimeUtils.setTime(lastDay, 0, 0, 0);
                endDate = DateTimeUtils.setTime(lastDay, 23, 59, 59);
                break;
            default:
                beginDate = DateTimeUtils.setTime(now, 0, 0, 0);
                endDate = DateTimeUtils.setTime(now, 23, 59, 59);
                break;
        }
        query.setEndTime(endDate);
        query.setStartTime(beginDate);
        query.setUid(uid);
        return query;
    }


}
