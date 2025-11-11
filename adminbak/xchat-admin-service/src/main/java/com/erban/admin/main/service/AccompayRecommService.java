package com.erban.admin.main.service;

import com.erban.main.dto.UsersDTO;
import com.erban.main.model.AccompanyManualRecomm;
import com.erban.main.model.AccompanyManualRecommExample;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.AccompanyManualRecommMapper;
import com.erban.main.mybatismapper.UsersMapper;
import com.erban.main.service.user.UsersService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chris
 * @Title:
 * @date 2018/11/13
 * @time 10:38
 */
@Service
public class AccompayRecommService {
    @Autowired
    private AccompanyManualRecommMapper accompanyManualRecommMapper;

    @Autowired
    private UsersService usersService;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private UsersMapper usersMapper;


    public PageInfo getAccompayRecommByList(Integer pageNumber, Integer pageSize, Long erbanNo) {
        PageHelper.startPage(pageNumber,pageSize);
        Map<String, Object> map = new HashMap<>();
        if(erbanNo!=null){
            map.put("erbanNo", erbanNo);
        }
        return new PageInfo(accompanyManualRecommMapper.selectByParam(map));
    }

    public BusiResult addAccompayManualRecomm(Long erbanNo, int seqNo, String startTimeString, String endTimeString) {
        Users users=usersService.getUsresByErbanNo(erbanNo);
        if(users==null){
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        AccompanyManualRecomm homeHotManualRecomm=new AccompanyManualRecomm();
        homeHotManualRecomm.setUid(users.getUid());
        homeHotManualRecomm.setType(1);
        homeHotManualRecomm.setStatus(1);
        homeHotManualRecomm.setIsDisplay(0);
        homeHotManualRecomm.setCreateTime(new Date());
        homeHotManualRecomm.setSeqNo(seqNo);
        if(startTimeString != null){
            Date date1 = DateTimeUtil.convertStrToDate(startTimeString);
            homeHotManualRecomm.setStartTime(date1);
        }
        if(endTimeString != null){
            Date date2 = DateTimeUtil.convertStrToDate(endTimeString);
            homeHotManualRecomm.setEndTime(date2);
        }
        accompanyManualRecommMapper.insert(homeHotManualRecomm);
        int status = usersMapper.updateByDefaultTag(users.getUid());
        if(status > 0){
            UsersDTO userDto = usersMapper.getUser(users.getUid());
            Gson gson = new Gson();
            jedisService.hset(RedisKey.user.getKey(), String.valueOf(users.getUid()), gson.toJson(userDto));
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }

    public BusiResult deleteAccompayManualRecomm(Integer id) {

        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        accompanyManualRecommMapper.deleteByPrimaryKey(id);
        return busiResult;
    }

    public BusiResult getOneAccompayManualRecomm(Integer id) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        AccompanyManualRecomm accompanyManualRecomm = accompanyManualRecommMapper.selectByPrimaryKey(id);
        Users users = usersService.getUsersByUid(accompanyManualRecomm.getUid());
        accompanyManualRecomm.setUid(users.getErbanNo());
        busiResult.setData(accompanyManualRecomm);
        return busiResult;
    }

    public BusiResult updateRecomm(Integer id, Integer seqNo, String startTimeString, String endTimeString) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        AccompanyManualRecommExample homeHotManualRecommExample = new AccompanyManualRecommExample();
        homeHotManualRecommExample.createCriteria().andIdEqualTo(id);
        List<AccompanyManualRecomm> homeHotManualRecommList = accompanyManualRecommMapper.selectByExample(homeHotManualRecommExample);
        if(!CollectionUtils.isEmpty(homeHotManualRecommList)){
            AccompanyManualRecomm homeHotManualRecomm = homeHotManualRecommList.get(0);
            homeHotManualRecomm.setSeqNo(seqNo);
            if(startTimeString != null){
                Date date1 = DateTimeUtil.convertStrToDate(startTimeString);
                homeHotManualRecomm.setStartTime(date1);
            }
            if(endTimeString != null){
                Date date2 = DateTimeUtil.convertStrToDate(endTimeString);
                homeHotManualRecomm.setEndTime(date2);
            }
            accompanyManualRecommMapper.updateByPrimaryKeySelective(homeHotManualRecomm);
        }
        return busiResult;
    }



    public BusiResult settingAccompayManualRecomm(Integer id) {
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        AccompanyManualRecomm accompanyManualRecomm = accompanyManualRecommMapper.selectByPrimaryKey(id);
        if(accompanyManualRecomm.getIsDisplay() == 0){
            accompanyManualRecomm.setIsDisplay(1);
        }else{
            accompanyManualRecomm.setIsDisplay(0);
        }
        accompanyManualRecommMapper.updateByPrimaryKeySelective(accompanyManualRecomm);
        return busiResult;
    }
}
