package com.erban.main.service;

import com.erban.main.model.NobleUsers;
import com.erban.main.model.Users;
import com.erban.main.model.level.LevelCharmVo;
import com.erban.main.model.level.LevelExerpenceVo;
import com.erban.main.mybatismapper.RankMapperMgr;
import com.erban.main.param.RankParam;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.level.LevelCharmService;
import com.erban.main.service.level.LevelExperienceService;
import com.erban.main.service.noble.NobleUsersService;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.RankHomeVo;
import com.erban.main.vo.RankMineVo;
import com.erban.main.vo.RankParentVo;
import com.erban.main.vo.RankVo;
import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.common.utils.GetTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RankService extends BaseService{
    public static final Logger logger = LoggerFactory.getLogger(RankService.class);
    private static Date appStartDate=DateTimeUtil.convertStrToDate("2017-08-01 00:00:00");
    private static Date appEndDate=DateTimeUtil.convertStrToDate("2037-08-01 00:00:00");
    private static int[] rankType={Constant.RankType.star,Constant.RankType.noble,Constant.RankType.room};
    private static int[] dateType={Constant.RankDatetype.day,Constant.RankDatetype.week,Constant.RankDatetype.total};

    @Autowired
    private RankMapperMgr rankMapperMgr;
    @Autowired
    private UsersService usersService;
    @Autowired
    private NobleUsersService nobleUsersService;
    @Autowired
    private LevelExperienceService levelExperienceService;
    @Autowired
    private LevelCharmService levelCharmService;


    public BusiResult getRankHomeVo() {
        RankHomeVo rankHomeVo=getRankHomeDataOnlyCache();
        if(rankHomeVo==null){
            rankHomeVo=new RankHomeVo();
        }
        BusiResult busiResut=new BusiResult(BusiStatus.SUCCESS);
        busiResut.setData(rankHomeVo);
        return busiResut;
    }
    /**
     *
     * @param uid
     * @param type 排行榜类型：1巨星榜；2贵族榜；3房间榜
     * @param datetype 	榜单统计周期类型：1、日榜；2周榜；3总榜
     * @return
     */
    public BusiResult<RankParentVo> getH5RankParentVoList(Long uid, int type, int datetype,Integer pageSize) {
        List<RankVo> rankList=getAllRankListByType(type,datetype);
        RankParentVo rankParentVo =new RankParentVo();
        if(pageSize==null){
            pageSize = 50;
        }
        if (rankList != null && rankList.size() > pageSize) {
            rankList = rankList.subList(0, pageSize);
        }
        rankParentVo.setRankVoList(rankList);
        BusiResult<RankParentVo> busiResult=new BusiResult<>(BusiStatus.SUCCESS);
        busiResult.setData(rankParentVo);
        return busiResult;
    }

    /**
     *
     * @return
     */
    public BusiResult<RankParentVo> getLastRankVoList(Long uid,Integer pageSize) {
        List<RankVo> rankList=getLastRankList();
        RankParentVo rankParentVo =new RankParentVo();
        if(pageSize==null){
            pageSize = 20;
        }
        // 截取列表返回前20名
        if (rankList != null && rankList.size() > pageSize) {
            rankList = rankList.subList(0, pageSize);
        }
        rankParentVo.setRankVoList(rankList);
        BusiResult<RankParentVo> busiResult=new BusiResult<>(BusiStatus.SUCCESS);
        busiResult.setData(rankParentVo);
        return busiResult;
    }

    /**
     * 获取个人的排名信息
     *
     * @param rankList
     * @param uid
     * @return
     */
    private RankMineVo getRankMine(List<RankVo> rankList, Long uid) {
        if (rankList != null && uid != null) {
            Users users = usersService.getUsersByUid(uid);
            if (users != null) {
                RankMineVo mineVo = new RankMineVo();
                mineVo.setAvatar(users.getAvatar());
                mineVo.setErbanNo(users.getErbanNo());
                mineVo.setGender(users.getGender());
                mineVo.setNick(users.getNick());
                mineVo.setSeqNo(0);
                mineVo.setTotalNum(0);
                NobleUsers nobleUsers = nobleUsersService.getNobleUser(uid);
                if (nobleUsers != null) {
                    mineVo.setNobleId(nobleUsers.getNobleId());
                    mineVo.setNobleName(nobleUsers.getNobleName());
                } else {
                    mineVo.setNobleId(0);
                }

                for (int i = 0; i < rankList.size(); i++) {
                    RankVo rankVo = rankList.get(i);
                    if (uid.equals(rankVo.getUid())) {
                        mineVo.setSeqNo(i + 1);
                        // TODO 去掉所有的分数
//                        mineVo.setTotalNum(rankVo.getTotalNum());
                        break;
                    }
                }
                return mineVo;
            }
        }
        return null;
    }

    private List<RankVo> removeTotalNum(List<RankVo> list) {
        for (RankVo rankVo : list) {
            rankVo.setTotalNum(0.0);
        }
        return list;
    }

    private RankHomeVo doRankHomeData(){
        //首页默认展示日榜，如果没有日榜，展示周榜
        List<RankVo> starList=getRankCacheByType(Constant.RankType.star,Constant.RankDatetype.day);
        if(CollectionUtils.isEmpty(starList)){//
            starList=getRankCacheByType(Constant.RankType.star,Constant.RankDatetype.week);
        }
        List<RankVo> nobleList=getRankCacheByType(Constant.RankType.noble,Constant.RankDatetype.day);
        if(CollectionUtils.isEmpty(nobleList)){//
            nobleList=getRankCacheByType(Constant.RankType.noble,Constant.RankDatetype.week);
        }
        List<RankVo> roomList= getRankCacheByType(Constant.RankType.room,Constant.RankDatetype.day);
        if(CollectionUtils.isEmpty(roomList)){//
            roomList= getRankCacheByType(Constant.RankType.room,Constant.RankDatetype.week);
        }

        RankHomeVo rankHomeVo=new RankHomeVo();
        if(!CollectionUtils.isEmpty(starList)&&starList.size()>3){
            starList=starList.subList(0,3);
        }
        rankHomeVo.setStarList(starList);
        if(!CollectionUtils.isEmpty(nobleList)&&nobleList.size()>3){
            nobleList=nobleList.subList(0,3);
        }
        rankHomeVo.setNobleList(nobleList);
        if(!CollectionUtils.isEmpty(roomList)&&roomList.size()>3){
            roomList=roomList.subList(0,3);
        }
        fillUserVo(starList);
        fillUserVo(nobleList);
        fillUserVo(roomList);
        rankHomeVo.setRoomList(roomList);
        saveRankHomeDataOnlyCache(rankHomeVo);
        return rankHomeVo;

    }
    private void  saveAllRankHomeVoCache(List<RankVo> rankVoList,int type,int datetype){
        if(CollectionUtils.isEmpty(rankVoList)){
            return;
        }
        String key=type+""+datetype;
        jedisService.hwrite(RedisKey.rank.getKey(),key,gson.toJson(rankVoList));
    }

    private void  saveRankHomeDataOnlyCache(RankHomeVo rankHomeVo){
        if(rankHomeVo==null){
            return;
        }
        jedisService.set(RedisKey.rank_home.getKey(),gson.toJson(rankHomeVo));
    }

    public RankHomeVo  getRankHomeDataOnlyCache(){
        String rankHomeStr=jedisService.read(RedisKey.rank_home.getKey());
        if(StringUtils.isEmpty(rankHomeStr)){
            return null;
        }
        RankHomeVo rankHomeVo=gson.fromJson(rankHomeStr,RankHomeVo.class);
        return rankHomeVo;

    }

    private List<RankVo> fillUserVo(List<RankVo> rankList){
        if(CollectionUtils.isEmpty(rankList)){
            return Lists.newArrayList();
        }
        List<RankVo> rankVoList = Lists.newArrayList();
        String uidArray[]=new String[rankList.size()];
        for(int i=0,len=rankList.size();i<len;i++){
            Long uid=rankList.get(i).getUid();
            if(uid==null){
                uid=0L;
            }
            uidArray[i]=uid.toString();
        }
        Map<Long,Users> usersMap= usersService.getUsersMapBatch(uidArray);

        for(int i=0,len=rankList.size();i<len;i++){
            RankVo rankVo=rankList.get(i);
            Long uid=rankVo.getUid();
            Users user=usersMap.get(uid);
            if(user!=null){
                rankVo.setGender(user.getGender());
                rankVo.setNick(user.getNick());
                rankVo.setAvatar(user.getAvatar());
                rankVo.setErbanNo(user.getErbanNo());
                //增加财富等级和魅力等级
                addLevel(rankVo);
                rankVoList.add(rankVo);
            }
        }
        return rankVoList;
    }
    private List<RankVo> getRankCacheByType(int type,int datetype){
        String key= type+""+datetype;
        String rankVoListStr=jedisService.hget(RedisKey.rank.getKey(),key);
        List<RankVo> rankVoList;
        if(StringUtils.isEmpty(rankVoListStr)){
            return null;
        }else{
            Type typeJson = new TypeToken<List<RankVo>>(){}.getType();
            rankVoList = gson.fromJson(rankVoListStr, typeJson);
        }
        return rankVoList;
    }

    private List<RankVo> getRankCacheByType(){
        String rankVoListStr=jedisService.hget(RedisKey.rank_last.getKey(), "1");
        List<RankVo> rankVoList;
        if(StringUtils.isEmpty(rankVoListStr)){
            return null;
        }else{
            Type typeJson = new TypeToken<List<RankVo>>(){}.getType();
            rankVoList = gson.fromJson(rankVoListStr, typeJson);
        }
        return rankVoList;
    }

    public void doAllKindRankHomeVoJob(){
        for(int i=0;i<rankType.length;i++){
            for(int j=0;j<dateType.length;j++){
                int type=rankType[i];
                int datetype=dateType[j];
                List<RankVo> rankList=getRankListFromDb(type,datetype);
                saveAllRankHomeVoCache(rankList,type,datetype);
            }
        }
        doRankHomeData();
    }

    public void doLastRankJob(){
        RankParam rankParam=new RankParam();
        Date startTime=DateTimeUtil.getCurrentMonday(0,0,0);
        Date endTime=DateTimeUtil.getCurrentSunday(23,59,59);
        rankParam.setStartTime(startTime);
        rankParam.setEndTime(endTime);
        List<RankVo> rankList=getNobelRankList(rankParam);
        fillUserVo(rankList);
        if(CollectionUtils.isEmpty(rankList)){
            return;
        }
        jedisService.hwrite(RedisKey.rank_last.getKey(),"1",gson.toJson(rankList));
    }

    public List<RankVo> getRankListFromDb(int type,int datetype){
        RankParam rankParam=new RankParam();
        Date date=new Date();
        Date startTime=null;
        Date endTime=null;
        if(datetype==1){
            startTime=GetTimeUtils.getTimesnights(date,0);
            endTime=GetTimeUtils.getTimesnights(date,24);
        }else if(datetype==2){
            startTime= DateTimeUtil.getCurrentMonday(0,0,0);
            endTime=DateTimeUtil.getCurrentSunday(23,59,59);
        }else if(datetype==3){
            startTime=appStartDate;
            endTime=appEndDate;
        }else{
            return  Lists.newArrayList();
        }
        rankParam.setStartTime(startTime);
        rankParam.setEndTime(endTime);
        List<RankVo> rankList=null;
        if(type==1){
            rankList=getAllStarRankList(rankParam);
        }else if(type==2){
            rankList=getNobelRankList(rankParam);
        }else if(type==3){
            rankList=getAllRoomRankList(rankParam);
        }else{
            return  Lists.newArrayList();
        }
        fillUserVo(rankList);
        return rankList;
    }

    private List<RankVo> getAllRankListByType(int type,int datetype){
        List<RankVo> rankVoList=getRankCacheByType(type,datetype);
        return rankVoList;
    }

    private List<RankVo> getLastRankList(){
        List<RankVo> rankVoList=getRankCacheByType();
        return rankVoList;
    }

    private List<RankVo> getAllStarRankList(RankParam rankParam){
        List<RankVo> rankList=rankMapperMgr.getAllStarRankList(rankParam);
        return rankList;
    }
    private List<RankVo> getNobelRankList(RankParam rankParam){
        List<RankVo> rankList=rankMapperMgr.getNobelRankList(rankParam);
        return rankList;
    }
    private List<RankVo> getAllRoomRankList(RankParam rankParam){
        List<RankVo> rankList=rankMapperMgr.getAllRoomRankList(rankParam);
        return rankList;
    }

    /**
     * 获取财富等级和魅力等级
     * @param rankVo
     * @return
     */
    private void addLevel( RankVo rankVo) {
        LevelExerpenceVo levelExerpenceVo = levelExperienceService.getLevelExperience(rankVo.getUid());
        if (levelExerpenceVo != null) {
            rankVo.setExperLevel(Integer.valueOf(levelExerpenceVo.getLevelName().substring(2)));
        } else {
            rankVo.setExperLevel(0);
        }
        LevelCharmVo levelCharmVo = levelCharmService.getLevelCharm(rankVo.getUid());
        if (levelCharmVo != null) {
            rankVo.setCharmLevel(Integer.valueOf(levelCharmVo.getLevelName().substring(2)));
        } else {
            rankVo.setCharmLevel(0);
        }
    }
}
