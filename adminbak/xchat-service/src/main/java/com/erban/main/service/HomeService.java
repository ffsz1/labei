package com.erban.main.service;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.Room;
import com.erban.main.model.Users;
import com.erban.main.model.beanmap.HomeRoomFlowPeriod;
import com.erban.main.mybatismapper.HomeRoomFlowPeriodMapperMgr;
import com.erban.main.service.icon.IconService;
import com.erban.main.service.room.RoomOnlineNumService;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.statis.StatRoomCtrbsumPeriodService;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class HomeService {
    private static final Logger logger = LoggerFactory.getLogger(HomeService.class);
    @Autowired
    private RoomService roomService;
    @Autowired
    private JedisService jedisService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private HomeRoomFlowPeriodMapperMgr homeRoomFlowMapperMgr;
    @Autowired
    private IconService iconService;
    @Autowired
    private SysConfService sysConfService;
    @Autowired
    private StatRoomCtrbsumPeriodService statRoomCtrbsumPeriodService;
    @Autowired
    private RoomOnlineNumService roomOnlineNumService;
    @Autowired
    private AppStoreService appStoreService;

    private static int homeHotRecommCount=3;

    private static int gameRoomsCountMax=8;
    private static int chatRoomsCountMax=4;
    private String result="";

    private Gson gson = new Gson();

    public BusiResult getHomeData(String os) {
        logger.info("getHomeData os=" + os);
        List<RoomVo> roomVoList = roomService.getHomeRunningRoomList();
        BusiResult<List<RoomVo>> busiResult = new BusiResult<List<RoomVo>>(BusiStatus.SUCCESS);
        for (RoomVo roomVo : roomVoList) {
            String runningRoomStr = jedisService.hget(RedisKey.room_running.getKey(), roomVo.getUid().toString());
            RunningRoomVo runningRoomVo = gson.fromJson(runningRoomStr, RunningRoomVo.class);
            Integer onlineNum = runningRoomVo.getOnlineNum();
            if (onlineNum == null || onlineNum == 0) {
                roomVo.setOnlineNum(1);
            } else {
                roomVo.setOnlineNum(onlineNum);
            }
            Users users = usersService.getUsersByUid(roomVo.getUid());
            if (users != null) {
                roomVo.setAvatar(users.getAvatar());
                roomVo.setNick(users.getNick());
                roomVo.setGender(users.getGender());
            }
        }
        List<RoomVo> oldVersionIOSRoomList = Lists.newArrayList();
        if ("iOS".equals(os)) {
            for (RoomVo roomVo : roomVoList) {
                if (Constant.RoomType.game.equals(roomVo.getType())) {
                    continue;
                }
                oldVersionIOSRoomList.add(roomVo);
            }
            Collections.sort(oldVersionIOSRoomList);
            busiResult.setData(oldVersionIOSRoomList);
        } else {
            Collections.sort(roomVoList);
            busiResult.setData(roomVoList);
        }
        return busiResult;
    }

    public BusiResult<HomeVo> getHomeDataV2(String type, String os,String appVersion) throws Exception {
        if("iOS".equals(os)&&appStoreService.checkVersionBy(appVersion)){
            BusiResult busiResult=getIOSAuditHomeDataV2();
            return busiResult;
        }else{
            HomeVo homeVo = new HomeVo();
            if(Constant.HomePageTag.game.equals(type)){
                List<RoomVo> roomVoList=getGameHomeRoomList();
                homeVo.setGameRooms(roomVoList);
            }else if(Constant.HomePageTag.radio.equals(type)){
                List<RoomVo> roomVoList=getRadioHomeRoomList();
                homeVo.setChatRooms(roomVoList);
            }else{
                homeVo= getHotHomeRoomList();
            }
            BusiResult<HomeVo> busiResult = new BusiResult<HomeVo>(BusiStatus.SUCCESS);
            busiResult.setData(homeVo);
            return busiResult;
        }


    }

    public void doHomeDataJob() {

        doHomeVoJob();
        doGameHomeJob();
        doRadioHomeRoomJob();
    }
    public void refreshHomData(){
        HomeVo hotHomVo=getHotHomeRoomList();

        List<RoomVo> hotChatRoomList= hotHomVo.getChatRooms();
        List<RoomVo> newHotChatRoomList= Lists.newArrayList();
        List<RoomVo> hotRecomRoomList=hotHomVo.getRecomRooms();
        List<RoomVo> newHotRecomRoomList= Lists.newArrayList();
        List<RoomVo> hotGameRoomList=hotHomVo.getGameRooms();
        List<RoomVo> newHotGameRoomList= Lists.newArrayList();
        if(!CollectionUtils.isEmpty(hotChatRoomList)){
            for(RoomVo roomVo:hotChatRoomList){
                Long uid=roomVo.getUid();
                roomVo=roomService.getRoomVoWithUsersByUid(roomVo.getUid());
                roomVo.setOnlineNum(roomVo.getOnlineNum());
                newHotChatRoomList.add(roomVo);
            }
        }
        if(!CollectionUtils.isEmpty(hotRecomRoomList)){
            for(RoomVo roomVo:hotRecomRoomList){
                Long uid=roomVo.getUid();
                roomVo=roomService.getRoomVoWithUsersByUid(roomVo.getUid());
                roomVo.setOnlineNum(roomVo.getOnlineNum());
                newHotRecomRoomList.add(roomVo);
            }
        }
        if(!CollectionUtils.isEmpty(hotGameRoomList)){
            for(RoomVo roomVo:hotGameRoomList){
                Long uid=roomVo.getUid();
                roomVo=roomService.getRoomVoWithUsersByUid(roomVo.getUid());
                roomVo.setOnlineNum(roomVo.getOnlineNum());
                newHotGameRoomList.add(roomVo);
            }
        }
        hotHomVo.setGameRooms(newHotGameRoomList);
        hotHomVo.setRecomRooms(newHotRecomRoomList);
        hotHomVo.setChatRooms(newHotChatRoomList);
        saveHotHomeRoomCacheByKey(hotHomVo);



        List<RoomVo> gameHomeVoList= getGameHomeRoomList();
        List<RoomVo> newGameHomeVoList= Lists.newArrayList();
        if(!CollectionUtils.isEmpty(gameHomeVoList)){
            for(RoomVo roomVo:gameHomeVoList){
                Long uid=roomVo.getUid();
                roomVo=roomService.getRoomVoWithUsersByUid(roomVo.getUid());
                roomVo.setOnlineNum(roomVo.getOnlineNum());
                newGameHomeVoList.add(roomVo);
            }
            saveHomeRoomCacheByKey(RedisKey.home_game.getKey(),newGameHomeVoList);
        }

        List<RoomVo>  radioHomeVoList=getRadioHomeRoomList();
        List<RoomVo> newRadioHomeVoList= Lists.newArrayList();
        if(!CollectionUtils.isEmpty(radioHomeVoList)){
            for(RoomVo roomVo:radioHomeVoList){
                Long uid=roomVo.getUid();
                roomVo=roomService.getRoomVoWithUsersByUid(roomVo.getUid());
                roomVo.setOnlineNum(roomVo.getOnlineNum());
                newRadioHomeVoList.add(roomVo);
            }
            saveHomeRoomCacheByKey(RedisKey.home_radio.getKey(),newRadioHomeVoList);
        }
    }


    private void saveHomeRoomCacheByKey(String key,List<RoomVo> roomVoList) {
        jedisService.set(key,gson.toJson(roomVoList));
    }
    private void saveHotHomeRoomCacheByKey(HomeVo homeVo) {
        jedisService.set(RedisKey.home_hot.getKey(),gson.toJson(homeVo));
    }

//    List<HomeRoomFlowPeriod> getHotHomeRoomForRecomOneTwoListFromDb(){
//        return homeRoomFlowMapperMgr.getHotHomeRoomForRecomOneTwoList();
//    }
//    List<HomeRoomFlowPeriod> getHotHomeRoomForRecomThirdForthListFromDb(List<Long> uid){
//        return homeRoomFlowMapperMgr.getHotHomeRoomForRecomThirdForthList(uid);
//    }
//    List<HomeRoomFlowPeriod> getHotHomeRoomForGameListFromDb(List<Long> uid){
//        return homeRoomFlowMapperMgr.getHotHomeRoomForGameList(uid);
//    }
//    List<HomeRoomFlowPeriod> getHotHomeRoomForRadioListFromDb(List<Long> uids){
//        return homeRoomFlowMapperMgr.getHotHomeRoomForRadioList(uids);
//    }


    List<HomeRoomFlowPeriod> getGameHomeRoomListFromDb(){
        return homeRoomFlowMapperMgr.getGameHomeRoomPeriodList();
    }
    List<HomeRoomFlowPeriod> getGameHomeRoomListFromDbOthers(){
        return homeRoomFlowMapperMgr.getGameHomeRoomPeriodListOthers();
    }
    List<HomeRoomFlowPeriod> getRadioHomeRoomListFromDb(){
        return homeRoomFlowMapperMgr.getRadioHomeRoomPrriodList();
    }

    private List<RoomVo> getRadioHomeRoomList(){
        List<RoomVo> roomVoList=getHomeRoomVoListCacheByKey(RedisKey.home_radio.getKey());
        if(CollectionUtils.isEmpty(roomVoList)){
            roomVoList=doRadioHomeRoomJob();
            return roomVoList;
        }else{
            return roomVoList;
        }
    }
    private List<RoomVo> doRadioHomeRoomJob(){
        List<HomeRoomFlowPeriod>radioRoomFlowList=doRadioHomeRoomFlowPeriodJob();
        List<RoomVo>  roomVoList=convertHomeRoomToRoomVo(radioRoomFlowList);
        saveHomeRoomCacheByKey(RedisKey.home_radio.getKey(),roomVoList);
        return roomVoList;
    }
    private List<HomeRoomFlowPeriod> doRadioHomeRoomFlowPeriodJob(){
        List<HomeRoomFlowPeriod> radioRoomFlowList=getRadioHomeRoomListFromDb();
        if(CollectionUtils.isEmpty(radioRoomFlowList)){
            return Lists.newArrayList();
        }
        int totalPersonNum=getTotalPersonNum(radioRoomFlowList);
        Long totalFlowSum=getTotalFlowSum(radioRoomFlowList);
//        result=result+"轻聊房：总人数="+totalPersonNum+"总流水="+result+"\n";
        for(HomeRoomFlowPeriod homeRoomFlow:radioRoomFlowList){
            Room room=roomService.getRoomByUid(homeRoomFlow.getUid());
            if(room.getType().equals(Constant.RoomType.radio)){
                int currentPerson=homeRoomFlow.getOnlineNum();
                int currentFlowSum=homeRoomFlow.getFlowSumTotal()==null?0:homeRoomFlow.getFlowSumTotal().intValue();
                double roomPersonFlowSumSeqNo=getRoomPersonFlowSumSeqNoValue(currentPerson,currentFlowSum,totalPersonNum,totalFlowSum.intValue());
                homeRoomFlow.setPersonFlowSumSeqNoValue(roomPersonFlowSumSeqNo);
//                result=result+"轻聊房uid="+room.getUid()+"&person="+currentPerson+"&currentFlowSum="+currentFlowSum+"&roomPersonFlowSumSeqNo="+roomPersonFlowSumSeqNo+"\n";
            }else{
//                result=result+"竞拍房uid="+room.getUid()+"&roomPersonFlowSumSeqNo=999999999.00\n";
                homeRoomFlow.setPersonFlowSumSeqNoValue(999999999.00);
            }
        }
        Collections.sort(radioRoomFlowList);
        return radioRoomFlowList;
    }
    private HomeVo getHotHomeRoomList(){
        HomeVo homeVo  =getHotHomeRoomVoListCacheByKey();

        if(homeVo==null||homeVo.getRecomRooms()==null){
            homeVo=doHomeVoJob();
            return homeVo;
        }else{
            return homeVo;
        }

    }
    private HomeVo doHomeVoJob(){
        HomeVo homeVo=new HomeVo();
        // TODO 独立到GiftTask中执行
//        //生成半个小时内流水数据
//        genPeriodData();
        //获取所有牌照房及流水
        List<HomeRoomFlowPeriod> homeRoomFlowPeriodList=getHomePermitFlowList();
        if(CollectionUtils.isEmpty(homeRoomFlowPeriodList)){
            return homeVo;
        }
        //过滤密码房
        List<HomeRoomFlowPeriod> homeRoomFlowPeriodListPwdFilter=Lists.newArrayList();
        for(HomeRoomFlowPeriod homeRoomFlowPeriod:homeRoomFlowPeriodList){
            Long flowSumTotal=homeRoomFlowPeriod.getFlowSumTotal();
            Room room=roomService.getRoomByUid(homeRoomFlowPeriod.getUid());
            if(room==null){
                continue;
            }
            if(room.getType().equals(Constant.RoomType.auct)){
                homeRoomFlowPeriod.setFlowSumTotal(9999999999L);
            }
            if(StringUtils.isNotBlank(room.getRoomPwd())){
                continue;
            }
            //房间不是开播状态，过滤
            if(!room.getValid()){
                continue;
            }
            if(flowSumTotal==null){
                homeRoomFlowPeriod.setFlowSumTotal(0L);
            }
            homeRoomFlowPeriodListPwdFilter.add(homeRoomFlowPeriod);
        }
        homeRoomFlowPeriodList=homeRoomFlowPeriodListPwdFilter;

        //牌照数据排序
        homeRoomFlowPeriodList=getHomeHotSortList(homeRoomFlowPeriodList);

        List<HomeRoomFlowPeriod> recommHomeRoomFlowPeriod=Lists.newArrayList();
        //获取人工置顶首页数据，组装人工置顶数据
        List<HomeRoomFlowPeriod> homeHotManualRecommList= getHomeHotManualRecommList();
        if(!CollectionUtils.isEmpty(homeHotManualRecommList)){
            recommHomeRoomFlowPeriod=homeHotManualRecommList;
        }
        //人工置顶热门数据，两种排序方式，一种是直接人工指定，另一种是在房间搞活动期间，根据流水及人气进行排序
        String sortType=sysConfService.getSysConfValueById(Constant.SysConfId.homerecomm_sort_type);
        if("2".equals(sortType)){//1为人工默认指定排序，2为按照流水和人气排序
            recommHomeRoomFlowPeriod=getHomeHotSortList(recommHomeRoomFlowPeriod);
        }
        //推荐数据，不能重复出现在游戏和轻聊房中
        List<HomeRoomFlowPeriod> homeRoomFlowPeriodListRecommFilter=Lists.newArrayList();
        if(!CollectionUtils.isEmpty(homeHotManualRecommList)){
            for(HomeRoomFlowPeriod homeRoomFlowPeriod:homeRoomFlowPeriodList){
                if(isDumpRecommData(homeRoomFlowPeriod.getUid(),recommHomeRoomFlowPeriod)){
                    continue;
                }else{
                    homeRoomFlowPeriodListRecommFilter.add(homeRoomFlowPeriod);
                }
            }
        }else{
            homeRoomFlowPeriodListRecommFilter=homeRoomFlowPeriodList;
        }
        homeRoomFlowPeriodList=homeRoomFlowPeriodListRecommFilter;

        //组装推荐，除去人工置顶之外，取牌照房中流水第一的位置补充在后面
//        List<RoomVo> recomRooms=Lists.newArrayList();
        int recommHomeCount=recommHomeRoomFlowPeriod.size();
        //剩余推荐位=固定推荐数量3-人工置顶数量
        int leftHomeRecommCount=homeHotRecommCount-recommHomeCount;
        int homeRoomFlowPeriodSize=homeRoomFlowPeriodList.size();
        if(leftHomeRecommCount>0){
           for(int i=0;i<leftHomeRecommCount;i++){
               if(homeRoomFlowPeriodSize>i){
                   recommHomeRoomFlowPeriod.add(homeRoomFlowPeriodList.get(i));
               }
           }
        }
        //处理推荐数据
        for(int i=0;i<recommHomeRoomFlowPeriod.size();i++){
            HomeRoomFlowPeriod homeRoomFlowPeriod=recommHomeRoomFlowPeriod.get(i);
            homeRoomFlowPeriodList.add(homeRoomFlowPeriod);
        }
        homeVo.setRecomRooms(convertHomeRoomToRoomVo(recommHomeRoomFlowPeriod));
        if(leftHomeRecommCount>0){
            homeRoomFlowPeriodList=homeRoomFlowPeriodList.subList(leftHomeRecommCount,homeRoomFlowPeriodList.size());
        }
        List<HomeRoomFlowPeriod> gameRoomFlowPeriod=Lists.newArrayList();
        int gameRoomsCount=0;
        int chatRoomsCount=0;
        List<HomeRoomFlowPeriod> chatRoomFlowPeriod=Lists.newArrayList();
        for(int i=0;i<homeRoomFlowPeriodList.size();i++){
            HomeRoomFlowPeriod homeRoomFlowPeriod=homeRoomFlowPeriodList.get(i);
            RoomVo roomVo=roomService.getRoomVoWithUsersByUid(homeRoomFlowPeriod.getUid());
            if(roomVo.getType().equals(Constant.RoomType.game)){
                if(gameRoomsCount<gameRoomsCountMax){
                    gameRoomFlowPeriod.add(homeRoomFlowPeriod);
                    gameRoomsCount++;
                }
            }else{
                if(chatRoomsCount<chatRoomsCountMax){
                    chatRoomFlowPeriod.add(homeRoomFlowPeriod);
                    chatRoomsCount++;
                }

            }
        }

        homeVo.setChatRooms(convertHomeRoomToRoomVo(chatRoomFlowPeriod));
        homeVo.setGameRooms(convertHomeRoomToRoomVo(gameRoomFlowPeriod));
        saveHotHomeRoomCacheByKey(homeVo);
//        logger.info("获取首页result="+result);
        return homeVo;
    }


    private boolean isDumpRecommData(Long checkUid,List<HomeRoomFlowPeriod> recommHomeRoomFlowPeriod){
        boolean result=false;
        for(int i=0;i<recommHomeRoomFlowPeriod.size();i++){
            HomeRoomFlowPeriod homeRoomFlowPeriod=recommHomeRoomFlowPeriod.get(i);
            if(homeRoomFlowPeriod.getUid().equals(checkUid)){
                result=true;
                break;
            }
        }
        return result;

    }
    public void genPeriodData(){
        statRoomCtrbsumPeriodService.deleteAllPriodData();
        statRoomCtrbsumPeriodService.genPriodDataHalfHour();
    }

    public  List<HomeRoomFlowPeriod>  getHomePermitFlowList(){
        List<HomeRoomFlowPeriod>  homeRoomFlowPeriodList=homeRoomFlowMapperMgr.getHotHomePermitRoomList();
        return  homeRoomFlowPeriodList;
    }

    private List<HomeRoomFlowPeriod> getHomeHotSortList(List<HomeRoomFlowPeriod> homeRoomFlowPeriodList){
        int totalPersonNum=getTotalPersonNum(homeRoomFlowPeriodList);
        Long totalFlowSum=getTotalFlowSum(homeRoomFlowPeriodList);
        result=result+"热门房间：总人数="+totalPersonNum+"总流水="+totalFlowSum+"\n";
        for(HomeRoomFlowPeriod homeRoomFlow:homeRoomFlowPeriodList){

            int currentPerson=homeRoomFlow.getOnlineNum();
            int currentFlowSum=homeRoomFlow.getFlowSumTotal()==null?0:homeRoomFlow.getFlowSumTotal().intValue();

            double flowSum=getRoomPersonFlowSumSeqNoValue(currentPerson,currentFlowSum,totalPersonNum,totalFlowSum.intValue());
            result=result+"热门房间uid="+homeRoomFlow.getUid()+"&person="+currentPerson+"&currentFlowSum="+currentFlowSum+"&flowSum="+flowSum+"\n";
            homeRoomFlow.setPersonFlowSumSeqNoValue(flowSum);
        }
        Collections.sort(homeRoomFlowPeriodList);
        return  homeRoomFlowPeriodList;

    }

    private List<RoomVo> doGameHomeJob(){
        List<HomeRoomFlowPeriod> gameRoomFlowList=doGameHomeFlowPeriodJob();
        List<RoomVo> roomVoList=convertHomeRoomToRoomVo(gameRoomFlowList);
        saveHomeRoomCacheByKey(RedisKey.home_game.getKey(),roomVoList);
        return roomVoList;
    }
    private List<HomeRoomFlowPeriod> doGameHomeFlowPeriodJob(){
        List<HomeRoomFlowPeriod> wholeHomeRoomFlowPeriod=Lists.newArrayList();
        List<HomeRoomFlowPeriod> gameRoomFlowList=getGameHomeRoomListFromDb();
//        List<HomeRoomFlowPeriod> gameRoomFlowListOthers=getGameHomeRoomListFromDbOthers();
        if(!CollectionUtils.isEmpty(gameRoomFlowList)){
            wholeHomeRoomFlowPeriod.addAll(gameRoomFlowList);
        }
//        if(!CollectionUtils.isEmpty(gameRoomFlowListOthers)){
//            wholeHomeRoomFlowPeriod.addAll(gameRoomFlowListOthers);
//        }
        if(CollectionUtils.isEmpty(wholeHomeRoomFlowPeriod)){
            return Lists.newArrayList();
        }
        int totalPersonNum=getTotalPersonNum(wholeHomeRoomFlowPeriod);
        Long totalFlowSum=getTotalFlowSum(wholeHomeRoomFlowPeriod);
//        result=result+"游戏房：总人数="+totalPersonNum+"总流水="+result+"\n";

        for(HomeRoomFlowPeriod homeRoomFlow:wholeHomeRoomFlowPeriod){
            int currentPerson=homeRoomFlow.getOnlineNum();
            int currentFlowSum=homeRoomFlow.getFlowSumTotal()==null?0:homeRoomFlow.getFlowSumTotal().intValue();
            double flowSum=getRoomPersonFlowSumSeqNoValue(currentPerson,currentFlowSum,totalPersonNum,totalFlowSum.intValue());
//            result=result+"轻聊房间uid="+homeRoomFlow.getUid()+"&person="+currentPerson+"&currentFlowSum="+currentFlowSum+"&flowSum="+flowSum+"\n";
            homeRoomFlow.setPersonFlowSumSeqNoValue(flowSum);
        }
        Collections.sort(wholeHomeRoomFlowPeriod);
        return wholeHomeRoomFlowPeriod;
    }
//    private List<HomeRoomFlowPeriod> doHomeHotFlowPeriodSortJob(List<HomeRoomFlowPeriod>homeRoomFlowPeriodList){
//        int totalPersonNum=getTotalPersonNum(homeRoomFlowPeriodList);
//        Long totalFlowSum=getTotalFlowSum(homeRoomFlowPeriodList);
//        result=result+"热门房间：总人数="+totalPersonNum+"总流水="+totalFlowSum+"\n";
//
//        for(HomeRoomFlowPeriod homeRoomFlow:homeRoomFlowPeriodList){
//            int currentPerson=getRunningRoomOnlineNumCacheVo(homeRoomFlow.getUid());
//            int currentFlowSum=homeRoomFlow.getFlowSumTotal()==null?0:homeRoomFlow.getFlowSumTotal().intValue();
//            double flowSum=getRoomPersonFlowSumSeqNoValue(currentPerson,currentFlowSum,totalPersonNum,totalFlowSum.intValue());
//            result=result+"热门房间uid="+homeRoomFlow.getUid()+"&person="+currentPerson+"&currentFlowSum="+currentFlowSum+"&flowSum="+flowSum+"\n";
//            homeRoomFlow.setPersonFlowSumSeqNoValue(flowSum);
//        }
//        Collections.sort(homeRoomFlowPeriodList);
//        return homeRoomFlowPeriodList;
//    }

    private List<RoomVo> getGameHomeRoomList(){
        List<RoomVo> roomVoList=getHomeRoomVoListCacheByKey(RedisKey.home_game.getKey());
        if(CollectionUtils.isEmpty(roomVoList)){
            roomVoList=doGameHomeJob();
            return  roomVoList;
        }else{
            return roomVoList;
        }
    }
    public List<HomeRoomFlowPeriod> getHomeHotManualRecommList(){
        List<HomeRoomFlowPeriod> homeHotManualRecommList= homeRoomFlowMapperMgr.getHomeHotManualRecommList();
        return homeHotManualRecommList;
    }

    private Long getTotalFlowSum(List<HomeRoomFlowPeriod> homeRoomFlowList) {
        Long totalFlowSum=0L;
        if(CollectionUtils.isEmpty(homeRoomFlowList)){
            return totalFlowSum;
        }
        for(HomeRoomFlowPeriod homeRoomFlow: homeRoomFlowList){
            Long flowSum=homeRoomFlow.getFlowSumTotal()==null?0L:homeRoomFlow.getFlowSumTotal();
            totalFlowSum=totalFlowSum+flowSum;
        }
        return totalFlowSum;
    }
    private int getTotalPersonNum(List<HomeRoomFlowPeriod> homeRoomFlowList){
        int totalPersonNum=0;
        if(CollectionUtils.isEmpty(homeRoomFlowList)){
            return totalPersonNum;
        }
        for(HomeRoomFlowPeriod homeRoomFlow: homeRoomFlowList){
            int roomOnlineNum=homeRoomFlow.getOnlineNum();
            totalPersonNum=totalPersonNum+roomOnlineNum;
        }
        return totalPersonNum;
    }
    /**
     * 计算首页排序规则，计算指定直播间的指定排序综合值。综合值越高，排名越靠前
     * @param currentPerson
     * @param currentFlowSum
     * @param totalPersonNum
     * @param totalFlowSum
     * @return
     */
    private double getRoomPersonFlowSumSeqNoValue(int currentPerson,int currentFlowSum,int totalPersonNum,int totalFlowSum ){
        double personValue=0.00;
        if(currentPerson==0||totalPersonNum==0){
            personValue=0.00;
        }else{
            personValue= Constant.HomeRankCalc.personNumValue*100*currentPerson/totalPersonNum;
        }

        double flowSumValue=0.00;
        if(currentFlowSum==0||totalFlowSum==0){
            flowSumValue=0.00;
        }else{
            flowSumValue= (Constant.HomeRankCalc.goldFlow*100*currentFlowSum)/totalFlowSum;
        }
        double roomPersonFlowSumSeqNoValue=personValue+flowSumValue;
        return  roomPersonFlowSumSeqNoValue;

    }
//    private int getRunningRoomOnlineNumCacheVo(Long uid){
//        int onlineNum=0;
//        String runningRoomStr=jedisService.hget(RedisKey.room_running.getKey(),uid.toString());
//        if(StringUtils.isEmpty(runningRoomStr)){
//            return onlineNum;
//        }
//        RunningRoomVo runningRoomVo=gson.fromJson(runningRoomStr,RunningRoomVo.class);
//        return  runningRoomVo.getOnlineNum();
//    }

    private List<RoomVo> getHomeRoomVoListCacheByKey(String key){
        String homeData = jedisService.read(key);
        if(StringUtils.isEmpty(homeData)){
            return Lists.newArrayList();
        }
        Type type = new TypeToken<List<RoomVo>>(){}.getType();
        List<RoomVo> roomVoList = gson.fromJson(homeData, type);
        return roomVoList;
    }

    private HomeVo getHotHomeRoomVoListCacheByKey(){
        String homeData = jedisService.read(RedisKey.home_hot.getKey());
        if(StringUtils.isEmpty(homeData)){
            return null;
        }
        HomeVo homeVo = gson.fromJson(homeData,HomeVo.class);
        return homeVo;
    }

    public BusiResult getIOSAuditHomeData(Long uid) {
        List<RoomVo> roomVoList = roomService.getHomeRunningRoomList();
        BusiResult<List<RoomVo>> busiResult = new BusiResult<List<RoomVo>>(BusiStatus.SUCCESS);
        List<RoomVo> iosRoomVoList = Lists.newArrayList();
        for (RoomVo roomVo : roomVoList) {
            Long roomVoUid = roomVo.getUid();
            if (Constant.IOSAuditAccount.auditAccountList.contains(roomVoUid)) {
                String runningRoomStr = jedisService.hget(RedisKey.room_running.getKey(), roomVo.getUid().toString());
                RunningRoomVo runningRoomVo = gson.fromJson(runningRoomStr, RunningRoomVo.class);
                Integer onlineNum = runningRoomVo.getOnlineNum();
                if (onlineNum == null || onlineNum == 0) {
                    roomVo.setOnlineNum(1);
                } else {
                    roomVo.setOnlineNum(onlineNum);
                }
                Users users = usersService.getUsersByUid(roomVo.getUid());
                roomVo.setAvatar(users.getAvatar());
                roomVo.setNick(users.getNick());
                roomVo.setGender(users.getGender());
                iosRoomVoList.add(roomVo);
            }

        }
        Collections.sort(iosRoomVoList);
        busiResult.setData(iosRoomVoList);
        return busiResult;
    }

    public BusiResult getIOSAuditHomeDataV2() {
        BusiResult<HomeVo> busiResult = new BusiResult<HomeVo>(BusiStatus.SUCCESS);
        List<Long> auditAccountListUid = Constant.IOSAuditAccount.auditAccountList;
        HomeVo homeVo=new HomeVo();
        List<RoomVo> recomRooms=Lists.newArrayList();
        List<RoomVo> gameRooms=Lists.newArrayList();
        List<RoomVo> chatRooms=Lists.newArrayList();

        for(Long uid:auditAccountListUid){
           RoomVo roomVo=roomService.getRoomVoWithUsersByUid(uid);
           if(roomVo!=null&&roomVo.getValid()){
               recomRooms.add(roomVo);
               gameRooms.add(roomVo);
               chatRooms.add(roomVo);
           }
        }
        homeVo.setChatRooms(chatRooms);
        homeVo.setGameRooms(gameRooms);
        homeVo.setRecomRooms(recomRooms);
        busiResult.setData(homeVo);
        return busiResult;
    }

    public BusiResult getIOSAuditHomeDataV3(String appVersion){
        BusiResult<HomeV2Vo> busiResult = new BusiResult<HomeV2Vo>(BusiStatus.SUCCESS);
        List<Long> auditAccountListUid = Constant.IOSAuditAccount.auditAccountList;
        HomeV2Vo homeVo=new HomeV2Vo();
        List<RoomVo> recomRooms=Lists.newArrayList();
        List<RoomVo> gameRooms=Lists.newArrayList();
        List<RoomVo> chatRooms=Lists.newArrayList();
        Room room;
        for(Long uid:auditAccountListUid){
            RoomVo roomVo=roomService.getRoomVoWithUsersByUid(uid);
            if(roomVo!=null&&roomVo.getValid()){
                room=roomService.getRoomByUid(roomVo.getUid());
                roomVo.setOnlineNum((room.getOnlineNum()==null?1:room.getOnlineNum())+(roomVo.getFactor()==null?0:roomVo.getFactor()));
//                recomRooms.add(roomVo);
                gameRooms.add(roomVo);
                chatRooms.add(roomVo);
            }
        }
        List<IconVo> iconVoList = iconService.getIcon(true, appVersion);
        if (iconVoList == null) iconVoList = com.google.common.collect.Lists.newArrayList();
        List<BannerVo> bannerList=new ArrayList<>();
        BannerVo bannerVo = new BannerVo();
        bannerVo.setBannerId(1);
        bannerVo.setBannerName("拉贝手册");
        bannerVo.setBannerPic("http://res.91fb.com/ios_banner.png");
        bannerVo.setSkipType(new Byte("3"));
        bannerVo.setSkipUri("https://www.47huyu.cn/mm/question/question.html");
        bannerVo.setSeqNo(1);
        bannerVo.setIsNewUser(new Byte("0"));
        bannerList.add(bannerVo);
        homeVo.setBanners(bannerList);
        homeVo.setHomeIcons(iconVoList);
        homeVo.setListRoom(gameRooms);
        homeVo.setHotRooms(recomRooms);
        busiResult.setData(homeVo);
        return busiResult;
    }

    public List<RoomVo> convertHomeRoomToRoomVo(List<HomeRoomFlowPeriod> roomFlowlist){
        if(CollectionUtils.isEmpty(roomFlowlist)){
            return Lists.newArrayList();
        }
        List<RoomVo> roomVoList=Lists.newArrayList();

        for(HomeRoomFlowPeriod homeRoomFlow:roomFlowlist){
            Long uid=homeRoomFlow.getUid();
            RoomVo roomVo =roomService.getRoomVoByUid(uid);
            Double flowSum=new Double(homeRoomFlow.getPersonFlowSumSeqNoValue());
            roomVo.setCalcSumDataIndex(flowSum.intValue());
            roomVo.setSeqNo(homeRoomFlow.getSeqNo());
            int onlineNum= homeRoomFlow.getOnlineNum();
            if (onlineNum==0){
                onlineNum=1;
            }
            roomVo.setOnlineNum(onlineNum+roomOnlineNumService.getNeedAddNum(uid.toString(), onlineNum));
            Users users = usersService.getUsersByUid(uid);
            if(users!=null){
                roomVo.setAvatar(users.getAvatar());
                roomVo.setNick(users.getNick());
                roomVo.setGender(users.getGender());
            }
            roomVoList.add(roomVo);
        }
        return roomVoList;

    }

}
