package com.erban.admin.main.service.room;

import com.erban.admin.main.mapper.HomeRecommMapperMgr;
import com.erban.admin.main.vo.HomeHotRecommendVO;
import com.erban.main.model.HomeHotManualRecomm;
import com.erban.main.model.HomeHotManualRecommExample;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.HomeHotManualRecommMapper;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.user.UsersService;
import com.erban.admin.main.vo.HomeHotManualRecommVo;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.ExpressWallVo;
import com.erban.main.vo.RoomVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.reflect.TypeToken;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HomeHotManualRecommService extends BaseService {
    @Autowired
    private HomeRecommMapperMgr homeRecommMapperMgr;
    @Autowired
    private UsersService usersService;
    @Autowired
    private HomeHotManualRecommMapper homeHotManualRecommMapper;

    public PageInfo<HomeHotManualRecommVo> getHomeHotRecommVoList(Integer pageNumber, Integer pageSize, Long erbanNo,
                                                                  Integer viewType) {
        PageHelper.startPage(pageNumber, pageSize);
        Map<String, Object> map = new HashMap<>();
        if (erbanNo != null) {
            map.put("erbanNo", erbanNo);
        }
        if (viewType != null) {
            map.put("viewType", viewType);
        }
        return new PageInfo(homeRecommMapperMgr.selectByParam(map));
    }

    public BusiResult addHomeHotManualRecomm(Long erbanNo, int seqNo, String startTimeString, String endTimeString,
                                             Integer viewType) {
        Users users = usersService.getUsresByErbanNo(erbanNo);
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        HomeHotManualRecomm homeHotManualRecomm = new HomeHotManualRecomm();
        homeHotManualRecomm.setUid(users.getUid());
        homeHotManualRecomm.setStatus(new Byte("1"));
        homeHotManualRecomm.setCreateTime(new Date());
        homeHotManualRecomm.setSeqNo(seqNo);
        homeHotManualRecomm.setViewType(viewType.byteValue());
        if (startTimeString != null) {
            Date date1 = DateTimeUtil.convertStrToDate(startTimeString);
            homeHotManualRecomm.setStartValidTime(date1);
        }
        if (endTimeString != null) {
            Date date2 = DateTimeUtil.convertStrToDate(endTimeString);
            homeHotManualRecomm.setEndValidTime(date2);
        }
        homeHotManualRecommMapper.insert(homeHotManualRecomm);
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public BusiResult updateRecomm(Integer recommId, Integer seqNo, String startTimeString, String endTimeString,
                                   Integer viewType) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        HomeHotManualRecommExample homeHotManualRecommExample = new HomeHotManualRecommExample();
        homeHotManualRecommExample.createCriteria().andRecommIdEqualTo(recommId);
        List<HomeHotManualRecomm> homeHotManualRecommList =
                homeHotManualRecommMapper.selectByExample(homeHotManualRecommExample);
        if (!CollectionUtils.isEmpty(homeHotManualRecommList)) {
            HomeHotManualRecomm homeHotManualRecomm = homeHotManualRecommList.get(0);
            homeHotManualRecomm.setSeqNo(seqNo);
            homeHotManualRecomm.setViewType(viewType.byteValue());
            if (startTimeString != null) {
                Date date1 = DateTimeUtil.convertStrToDate(startTimeString);
                homeHotManualRecomm.setStartValidTime(date1);
            }
            if (endTimeString != null) {
                Date date2 = DateTimeUtil.convertStrToDate(endTimeString);
                homeHotManualRecomm.setEndValidTime(date2);
            }
            homeHotManualRecommMapper.updateByPrimaryKeySelective(homeHotManualRecomm);
        }
        return busiResult;
    }

    public BusiResult deleteHomManualRecomm(Integer recommId) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        homeHotManualRecommMapper.deleteByPrimaryKey(recommId);
        return busiResult;
    }

    public BusiResult getOneRecomm(Integer recommId) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        HomeHotManualRecomm homeHotManualRecomm = homeHotManualRecommMapper.selectByPrimaryKey(recommId);
        busiResult.setData(homeHotManualRecomm);
        return busiResult;
    }

    /**
     * 获取首页推荐规则
     *
     * @return
     */
    public BusiResult getRule() {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        HomeHotRecommendVO homeHotRecommendVO = new HomeHotRecommendVO();
        String rule = jedisService.get(RedisKey.home_hot_recommend_rule.getKey());
        if (StringUtils.isBlank(rule)) {
            homeHotRecommendVO.setPeopleCount(0);
            homeHotRecommendVO.setGiftFlow(0);
            homeHotRecommendVO.setBackGiftFlow(0);
            jedisService.set(RedisKey.home_hot_recommend_rule.getKey(), gson.toJson(homeHotRecommendVO));
        } else {
            homeHotRecommendVO = gson.fromJson(rule, new TypeToken<HomeHotRecommendVO>() {
            }.getType());
        }
        busiResult.setData(homeHotRecommendVO);
        return busiResult;
    }


    public BusiResult saveRule(Integer people, Integer gift, Integer back) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        HomeHotRecommendVO homeHotRecommendVO = new HomeHotRecommendVO();
        homeHotRecommendVO.setPeopleCount(people);
        homeHotRecommendVO.setGiftFlow(gift);
        homeHotRecommendVO.setBackGiftFlow(back);
        jedisService.set(RedisKey.home_hot_recommend_rule.getKey(), gson.toJson(homeHotRecommendVO));
        busiResult.setData(homeHotRecommendVO);
        return busiResult;
    }
}
