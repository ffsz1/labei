package com.juxiao.xchat.service.api.guild.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.guild.GuildDao;
import com.juxiao.xchat.dao.guild.dto.GuildDTO;
import com.juxiao.xchat.dao.guild.dto.GuildExtendDTO;
import com.juxiao.xchat.dao.guild.dto.GuildHallDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.guild.GuildHallManager;
import com.juxiao.xchat.manager.common.guild.GuildHallMemberManager;
import com.juxiao.xchat.manager.common.guild.GuildManager;
import com.juxiao.xchat.service.api.guild.GuildService;
import com.juxiao.xchat.service.api.guild.vo.GuildDetailVo;
import com.juxiao.xchat.service.api.sysconf.vo.IndexParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 描述：
 *
 * @创建时间： 2020/10/13 11:40
 * @作者： carl
 */
@Service
public class GuildServiceImpl implements GuildService {

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private Gson gson;

    @Autowired
    private GuildDao guildDao;

    @Autowired
    private GuildManager guildManager;

    @Autowired
    private GuildHallManager guildHallManager;

    @Autowired
    private GuildHallMemberManager guildHallMemberManager;

    @Override
    public List<GuildDTO> getRecommendList(IndexParam param) {
        Integer pageNum = param.getPageNum();
        Integer pageSize = param.getPageSize();

        String jsdata = redisManager.get(RedisKey.guild_recommend.getKey(pageNum + "_" + pageSize));
        if (StringUtils.isNotBlank(jsdata)) {
            return gson.fromJson(jsdata, new TypeToken<List<GuildDTO>>() {}.getType());
        }

        Integer startIndex = pageNum > 0 ? (pageNum - 1) * pageSize : 0;
        List<GuildDTO> list = guildDao.getRecommendList(startIndex, pageSize);
        if (list != null && list.size() > 0) {
            redisManager.set(RedisKey.guild_recommend.getKey(pageNum + "_" + pageSize), gson.toJson(list), 5, TimeUnit.MINUTES);
        }

        return list;
    }

    @Override
    public GuildDetailVo getDetail(Long guildId, Long uid) throws WebServiceException {
        if (guildId == null || uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        GuildDTO guild = guildManager.getGuild(guildId);
        if (guild == null) {
            throw new WebServiceException(WebServiceCode.GUILD_NOT_EXIST);
        }

        Date now = new Date();

        // 指定当天流水排序
        List<GuildHallDTO> halls = guildHallManager.getHallListOrderByGoldDesc(guildId, now);
        GuildHallDTO myHall = null;
        if (halls != null && halls.size() > 0) {
            // 查看用户属于哪个厅
            myHall = guildHallManager.getHallByMemberUid(uid);

            // 不是同一个公会，不显示我的厅
            if (myHall != null && myHall.getGuildId().compareTo(guild.getId()) != 0) {
                myHall = null;
            }

            //找出所属厅，从数组中移除，逻辑暂时不用
//            if (myHall != null) {
//                Iterator<GuildHallDTO> iterator = halls.iterator();
//                while (iterator.hasNext()) {
//                    GuildHallDTO temp = iterator.next();
//                    if (temp.getHallId().compareTo(myHall.getHallId()) == 0) {
//                        iterator.remove();
//                        break;
//                    }
//                }
//            }
        }

        boolean hasJoin = guildHallMemberManager.hasJoinGuild(uid);

        GuildDetailVo vo = new GuildDetailVo();
        vo.setGuild(guild);
        vo.setHalls(halls);
        vo.setMyHall(myHall);
        vo.setHasJoin(hasJoin);

        return vo;
    }

    @Override
    public List<GuildExtendDTO> search(String key, Long uid) throws WebServiceException {
        if (key == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        GuildHallDTO hallDTO = guildHallManager.getHallByMemberUid(uid);

        List<GuildExtendDTO> list = guildDao.searchValidList(key);

        if (hallDTO != null) {
            for (GuildExtendDTO temp : list) {
                if (hallDTO.getGuildId().compareTo(temp.getId()) == 0) {
                    temp.setMyGuild(true);
                    break;
                }
            }
        }

        return list;
    }
}
