package com.erban.main.service.activity;

import com.erban.main.model.ActivityConfig;
import com.erban.main.model.ActivityConfigExample;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.ActivityConfigMapper;
import com.erban.main.mybatismapper.ActivityRankMapperMgr;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.activity.RankActVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import com.xchat.common.constant.ActivityConf;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.BlankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 排行榜活动
 */
@Service
public class RankActivityService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(RankActivityService.class);
    @Autowired
    private ActivityConfigMapper activityConfigMapper;
    @Autowired
    private ActivityRankMapperMgr activityRankMapperMgr;
    @Autowired
    private UsersService usersService;


    /**
     * 从缓存获取排行榜
     *
     * @param gameType 1淘汰赛，2半决赛，3总决赛
     * @param rankType 1富豪榜，2明星榜，3房间榜
     * @return
     */
    public List<RankActVo> getRankFromCache(Integer gameType, Integer rankType) {
        String json = jedisService.hget(RedisKey.activity_rank.getKey(gameType.toString()), rankType.toString());
        if (BlankUtil.isBlank(json)) {
            return Lists.newArrayList();
        } else {
            return gson.fromJson(json, new TypeToken<List<RankActVo>>() {}.getType());
        }
    }

    public List<ActivityConfig> getAllConfig(){
        ActivityConfigExample example = new ActivityConfigExample();
        return activityConfigMapper.selectByExample(example);
    }

    /**
     * 从DB中获取排行榜列表
     *
     * @param rankType  榜单类型
     * @param giftIds   礼物列表，可选
     * @param uids      用户列表，可选
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param size      榜单大小
     * @return
     */
    public List<RankActVo> getRankFromDB(Integer rankType, List<String> giftIds, List<Long> uids, String startTime
            , String endTime, Integer size, Integer rate) {
        Map<String, Object> param = Maps.newHashMap();
        if (!BlankUtil.isBlank(giftIds)) {
            param.put("giftIds", giftIds);
        }
        if (!BlankUtil.isBlank(uids)) {
            param.put("uids", uids);
        }
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        param.put("rate", rate);
        param.put("size", size);
        switch (rankType) {
            case ActivityConf.RANK_RICH: // 土豪榜
                return activityRankMapperMgr.queryRichActRank(param);
            case ActivityConf.RANK_STAR: // 明星榜
                return activityRankMapperMgr.queryStarActRank(param);
            case ActivityConf.RANK_ROOM: // 房间榜
                return activityRankMapperMgr.queryRoomActRank(param);
        }
        return Lists.newArrayList();
    }

    /**
     * 从DB中获取排行榜列表
     *
     * @param rankType  榜单类型
     * @param giftIds   礼物列表，可选
     * @param uids      用户列表，可选
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param size      榜单大小
     * @return
     */
    public List<RankActVo> getRankFromDBWapper(Integer rankType, List<String> giftIds, List<Long> uids, String startTime
            , String endTime, Integer size) {
        int rate = Integer.valueOf(jedisService.hget(RedisKey.activity_conf.getKey(), ActivityConf.ACT_GOLD_RATE));
        List<RankActVo> list = getRankFromDB(rankType, giftIds, uids, startTime, endTime, size, rate);
        for (RankActVo rankActVo : list) {
            Users users = usersService.getUsersByUid(rankActVo.getUid());
            if(users == null){
                users = new Users();
                users.setAvatar(Constant.DEFAULT_HEAD);
                users.setNick(Constant.DEFAULT_NICK);
                users.setErbanNo(0L);
            }
            rankActVo.setAvatar(users.getAvatar());
            rankActVo.setNick(users.getNick());
            rankActVo.setErbanNo(users.getErbanNo());
            rankActVo.setTotal(rankActVo.getTotal() * rate);
        }
        return list;
    }

    /**
     * 获取比赛结果名单， cache -> db -> cache
     *
     * @param gameType
     * @param rankType
     * @param maxSize
     * @return
     */
    public List<Long> getWinList(Integer gameType, Integer rankType, int maxSize) {
        List<RankActVo> preRank = getRankFromCache(gameType, rankType);
        List<Long> uids = Lists.newArrayList();
        for (int i = 0; i < preRank.size() && i < maxSize; i++) {
            uids.add(preRank.get(i).getUid());
        }
        return uids;
    }
}
