package com.erban.main.service.statis;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.StatRoomCtrbSum;
import com.erban.main.model.StatRoomCtrbSumExample;
import com.erban.main.model.Users;
import com.erban.main.model.level.LevelCharmVo;
import com.erban.main.model.level.LevelExerpenceVo;
import com.erban.main.mybatismapper.StatRoomCtrbSumMapper;
import com.erban.main.mybatismapper.StatRoomCtrbSumMapperMgr;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.level.LevelCharmService;
import com.erban.main.service.level.LevelExperienceService;
import com.erban.main.service.noble.NobleUsersService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.StatRoomCtrbSumVo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.common.utils.GetTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class StatRoomCtrbSumService extends BaseService{
    private static final Logger logger = LoggerFactory.getLogger(StatRoomCtrbSumService.class);
    private static Date appStartDate=DateTimeUtil.convertStrToDate("2017-08-01 00:00:00");
    private static Date appEndDate=DateTimeUtil.convertStrToDate("2037-08-01 00:00:00");
    @Autowired
    private StatRoomCtrbSumMapper statRoomCtrbSumMapper;
    @Autowired
    private StatRoomCtrbSumMapperMgr statRoomCtrbSumMapperMgr;
    @Autowired
    private UsersService usersService;
    @Autowired
    private NobleUsersService nobleUsersService;
    @Autowired
    private LevelExperienceService levelExperienceService;
    @Autowired
    private LevelCharmService levelCharmService;

    public BusiResult<List<StatRoomCtrbSumVo>> getStatRoomCtrbSumListByUid(Long uid){
        //TODO 缓存待实现
        List<StatRoomCtrbSum> statRoomoCtrbSumList=statRoomCtrbSumMapperMgr.getStatRoomCtrbSumListByUid(uid);
        List<StatRoomCtrbSumVo> statRoomCtrbSumVos= convertStatRoomCtrbSumListToVo(statRoomoCtrbSumList);
        BusiResult busiResult=new BusiResult(BusiStatus.SUCCESS);
        busiResult.setData(statRoomCtrbSumVos);
        return busiResult;
    }

    public BusiResult getRoomCtrbSumListByType(Long uid, Integer dataType, Integer type){
        Map<String,Object> param = new HashMap<>();
        if(dataType==1){
            Date date=new Date();
            param.put("startTime", GetTimeUtils.getTimesnights(date,0));
            param.put("endTime", GetTimeUtils.getTimesnights(date,24));
        }else if(dataType==2){
            param.put("startTime", DateTimeUtil.getCurrentMonday(0,0,0));
            param.put("endTime", DateTimeUtil.getCurrentSunday(23,59,59));
        }else if(dataType==3){
            param.put("startTime", appStartDate);
            param.put("endTime", appEndDate);
        }else{
            return new BusiResult(BusiStatus.SUCCESS, new ArrayList<>());
        }
        param.put("uid", uid);
        if(type==1){
            return new BusiResult(BusiStatus.SUCCESS, convertStatRoomCtrbSumListToVo(statRoomCtrbSumMapperMgr.getRoomCharismaList(param)));
        }else if(type==2){
            return new BusiResult(BusiStatus.SUCCESS, convertStatRoomCtrbSumListToVo(statRoomCtrbSumMapperMgr.getRoomWealthList(param)));
        }else {
            return new BusiResult(BusiStatus.SUCCESS, new ArrayList<>());
        }
    }

    /**
     *
     * @param uid 房间UID
     * @param ctrbUid 送礼物人UID
     * @param sumGold 送礼物金币
     */
    public void addAndUpdateStatRoomCtrbSumList(Long uid,Long ctrbUid,Long sumGold){
        StatRoomCtrbSumExample statRoomCtrbSumExample=new StatRoomCtrbSumExample();
        logger.info("addAndUpdateStatRoomCtrbSumList uid="+uid+"&ctrbUid="+ctrbUid+"&sumGold="+sumGold);
        statRoomCtrbSumExample.createCriteria().andUidEqualTo(uid).andCtrbUidEqualTo(ctrbUid);
        List<StatRoomCtrbSum> statRoomoCtrbSumList=statRoomCtrbSumMapper.selectByExample(statRoomCtrbSumExample);
        Date date=new Date();
        if(CollectionUtils.isEmpty(statRoomoCtrbSumList)){
            logger.info("addAndUpdateStatRoomCtrbSumList uid="+uid+"&ctrbUid="+ctrbUid+"&sumGold="+sumGold+" isEmpty");
            StatRoomCtrbSum statRoomCtrbSum=new StatRoomCtrbSum();
            statRoomCtrbSum.setUid(uid);
            statRoomCtrbSum.setSumGold(sumGold);
            statRoomCtrbSum.setCtrbUid(ctrbUid);
            statRoomCtrbSum.setCreateTime(date);
            statRoomCtrbSum.setUpdateTime(date);
            statRoomCtrbSumMapper.insertSelective(statRoomCtrbSum);
        }else{
            logger.info("addAndUpdateStatRoomCtrbSumList uid="+uid+"&ctrbUid="+ctrbUid+"&sumGold="+sumGold+" isNotEmpty");
            StatRoomCtrbSum statRoomCtrbSum=statRoomoCtrbSumList.get(0);
            Map<String,Object> param=new HashMap<>();
            param.put("ctrbId",statRoomCtrbSum.getCtrbId());
            param.put("sumGold",sumGold);
            statRoomCtrbSumMapperMgr.addSumGoldByCtrbId(param);
        }

    }

    private List<StatRoomCtrbSumVo> convertStatRoomCtrbSumListToVo(List<StatRoomCtrbSum> statRoomoCtrbSumList){
        List<StatRoomCtrbSumVo> statRoomCtrbSumVos=Lists.newArrayList();
        if(CollectionUtils.isEmpty(statRoomoCtrbSumList)){
            return statRoomCtrbSumVos;
        }
        String []uidsArray=new String[statRoomoCtrbSumList.size()];
        for(int i=0;i<statRoomoCtrbSumList.size();i++){
            uidsArray[i]=statRoomoCtrbSumList.get(i).getCtrbUid().toString();
        }
        Map<Long,Users> usersMap=usersService.getUsersMapBatch(uidsArray);

        for(StatRoomCtrbSum statRoomCtrbSum:statRoomoCtrbSumList){
            StatRoomCtrbSumVo statRoomCtrbSumVo=new StatRoomCtrbSumVo();
            statRoomCtrbSumVo.setUid(statRoomCtrbSum.getUid());
            statRoomCtrbSumVo.setCtrbUid(statRoomCtrbSum.getCtrbUid());
            statRoomCtrbSumVo.setSumGold(statRoomCtrbSum.getSumGold());
            Users user=usersMap.get(statRoomCtrbSum.getCtrbUid());
            if(user!=null){
                statRoomCtrbSumVo.setAvatar(user.getAvatar());
                statRoomCtrbSumVo.setNick(user.getNick());
                statRoomCtrbSumVo.setGender(user.getGender());
            }
            statRoomCtrbSumVos.add(statRoomCtrbSumVo);
            LevelExerpenceVo levelExerpenceVo = levelExperienceService.getLevelExperience(statRoomCtrbSumVo.getCtrbUid());
            if (levelExerpenceVo != null) {
                statRoomCtrbSumVo.setExperLevel(Integer.valueOf(levelExerpenceVo.getLevelName().substring(2)));
            } else {
                statRoomCtrbSumVo.setExperLevel(0);
            }
            LevelCharmVo levelCharmVo = levelCharmService.getLevelCharm(statRoomCtrbSumVo.getCtrbUid());
            if (levelCharmVo != null) {
                statRoomCtrbSumVo.setCharmLevel(Integer.valueOf(levelCharmVo.getLevelName().substring(2)));
            } else {
                statRoomCtrbSumVo.setCharmLevel(0);
            }
        }
        return statRoomCtrbSumVos;
    }

}
