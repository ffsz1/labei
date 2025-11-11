package com.juxiao.xchat.service.api.event.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.event.PopularityListDAO;
import com.juxiao.xchat.dao.event.dto.PopularityListDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.draw.conf.GiftDrawConf;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.event.PopularityListService;
import com.juxiao.xchat.service.api.event.vo.PopularityListVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PopularityListServiceImpl implements PopularityListService {
    @Resource
    private PopularityListDAO popularityListDAO;

    @Resource
    private UsersManager usersManager;

    @Resource
    private RedisManager redisManager;

    @Resource
    private GiftDrawConf giftDrawConf;

    private Gson gson = new Gson();

    @Override
    public Map<String, List<PopularityListVO>> getTop20List() {
        Map<String, List<PopularityListVO>> plvm = new HashMap<>();

        String girlsList = redisManager.get(RedisKey.popularity_list.getKey("girls"));
        List<PopularityListVO> gpll = gson.fromJson(girlsList, new TypeToken<List<PopularityListVO>>() {
        }.getType());
        plvm.put("girls", gpll);

        String boysList = redisManager.get(RedisKey.popularity_list.getKey("boys"));
        List<PopularityListVO> bpll = gson.fromJson(boysList, new TypeToken<List<PopularityListVO>>() {
        }.getType());
        plvm.put("boys", bpll);
        return plvm;
    }

    @Override
    public PopularityListVO getMyRank(Long uid) {
        PopularityListVO plv = new PopularityListVO();

        String myRank = redisManager.hget(RedisKey.popularity_list_mine.getKey(), uid.toString());
        if (StringUtils.isNotBlank(myRank)) {
            plv = gson.fromJson(myRank, new TypeToken<PopularityListVO>() {
            }.getType());
        } else {
            UsersDTO usersDTO = usersManager.getUser(uid);
            PopularityListDTO pld = popularityListDAO.queryMyRank(giftDrawConf.getTicketGiftId(), Integer.valueOf(usersDTO.getGender()), uid);
            Integer sendVotes = popularityListDAO.querySendVotes(giftDrawConf.getTicketGiftId(), uid);
            if (pld != null) {
                plv.setUid(uid);
                plv.setReceiptVotes(pld.getReceiptVotes());
                plv.setAvatar(pld.getAvatar());
                plv.setNick(pld.getNick());
                plv.setRank(pld.getRank());
                plv.setSendVotes(sendVotes);
            } else {
                plv.setUid(usersDTO.getUid());
                plv.setReceiptVotes(0);
                plv.setAvatar(usersDTO.getAvatar());
                plv.setNick(usersDTO.getNick());
                plv.setRank(0);
                plv.setSendVotes(0);
            }
        }

        return plv;
    }

    @Override
    public List<PopularityListVO> getUserRecommend(Long uid) {
        String userRecommend = redisManager.hget(RedisKey.popularity_list_user_recommend.getKey(), uid.toString());
        if (StringUtils.isNotBlank(userRecommend)) {
            List<PopularityListVO> popularityListVOList = gson.fromJson(userRecommend, new TypeToken<List<PopularityListVO>>() {
            }.getType());
            return popularityListVOList;
        } else {
            List<PopularityListVO> plvl = new ArrayList<>();
            List<PopularityListDTO> pldl = popularityListDAO.queryUserRecommend(giftDrawConf.getTicketGiftId(), uid);
            if (pldl != null && pldl.size() > 0) {
                pldl.forEach(item -> {
                    PopularityListVO plv = new PopularityListVO();
                    plv.setUid(item.getUid());
                    plv.setSendVotes(item.getSendVotes());
                    plv.setAvatar(item.getAvatar());
                    plv.setNick(item.getNick());
                    plv.setRank(item.getRank());
                    plvl.add(plv);
                });
            }
            return plvl;
        }
    }

    @Override
    public Map<String, List<PopularityListVO>> getLastWeekRank() {
        Map<String, List<PopularityListVO>> plvm = new HashMap<>();

        String boysList = redisManager.get(RedisKey.popularity_list_last_week.getKey("boys"));
        List<PopularityListVO> bpll = gson.fromJson(boysList, new TypeToken<List<PopularityListVO>>() {
        }.getType());
        plvm.put("boys", bpll);

        String girlsList = redisManager.get(RedisKey.popularity_list_last_week.getKey("girls"));
        List<PopularityListVO> gpll = gson.fromJson(girlsList, new TypeToken<List<PopularityListVO>>() {
        }.getType());
        plvm.put("girls", gpll);
        return plvm;
    }
}
