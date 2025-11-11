package com.juxiao.xchat.service.api.play.impl;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.DefMsgType;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.item.dto.GiftDTO;
import com.juxiao.xchat.dao.mora.MoraDAO;
import com.juxiao.xchat.dao.mora.MoraRecordDAO;
import com.juxiao.xchat.dao.mora.domain.MoraRecordDO;
import com.juxiao.xchat.dao.mora.dto.MoraAwardDTO;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;
import com.juxiao.xchat.dao.user.dto.UserPurseDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.item.GiftManager;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.manager.common.level.vo.LevelVO;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.sysconf.SysConfManager;
import com.juxiao.xchat.manager.common.user.UserGiftPurseManager;
import com.juxiao.xchat.manager.common.user.UserPurseManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.manager.external.im.ImRoomManager;
import com.juxiao.xchat.manager.external.im.bo.Custom;
import com.juxiao.xchat.manager.external.im.bo.ImRoomMessage;
import com.juxiao.xchat.service.api.play.MoraService;
import com.juxiao.xchat.service.api.play.bo.MoraRecordMessage;
import com.juxiao.xchat.service.api.play.dto.GiftInfoDTO;
import com.juxiao.xchat.service.api.play.dto.MoraConfigDTO;
import com.juxiao.xchat.service.api.play.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @author chris
 * @Title:
 * @date 2019-06-01
 * @time 22:10
 */
@Slf4j
@Service
public class MoraServiceImpl implements MoraService {

    @Autowired
    private UsersManager usersManager;

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private Gson gson;

    @Autowired
    private MoraDAO moraDAO;

    private final int MAX_NUM = 50;

    @Autowired
    private GiftManager giftManager;

    @Autowired
    private LevelManager levelManager;

    @Autowired
    private ImRoomManager imRoomManager;

    @Autowired
    private UserPurseManager userPurseManager;

    @Autowired
    private MoraRecordDAO moraRecordDAO;

    @Autowired
    private UserGiftPurseManager userGiftPurseManager;

    @Autowired
    private AsyncNetEaseTrigger asyncNetEaseTrigger;

    @Autowired
    private SysConfManager sysConfManager;


    /**
     * 获取配置状态
     *
     * @param uid    uid
     * @param roomId 房间ID
     * @return int
     */
    @Override
    public int getState(Long uid, Long roomId) throws WebServiceException {
        if (uid == null || roomId == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        RoomDTO roomDTO = roomManager.getRoom(roomId);
        if (roomDTO == null) {
            throw new WebServiceException(WebServiceCode.ROOM_NOT_EXIST);
        }
        String json = redisManager.hget(RedisKey.mora_config.getKey(),String.valueOf(roomDTO.getUid()));
        if(StringUtils.isNotBlank(json)){
            MoraConfigDTO moraConfigDTO = gson.fromJson(json,MoraConfigDTO.class);
            if(moraConfigDTO == null){
                return 2;
            }
            if(!DateUtils.between(moraConfigDTO.getStart(),moraConfigDTO.getEnd())){
                return 2;
            }
            return 1;
        }
        return 2;
    }

    /**
     * 获取猜拳发起记录
     *
     * @param uid    uid
     * @param roomId roomId
     * @return MoraVO
     * @throws WebServiceException WebServiceException
     */
    @Override
    public List<MoraMessageVO> getMoraRecord(Long uid, Long roomId) throws WebServiceException {
        List<MoraMessageVO> moraMessageVOS = Lists.newArrayList();
        List<MoraRecordDO> moraRecordDO = moraRecordDAO.selectByRoomId(roomId);
        if(moraRecordDO.size() > 0) {
            moraRecordDO.forEach(item -> {
                MoraMessageVO msgInfo = new MoraMessageVO();
                Map<String, Object> data = new HashMap<>(8);
                MoraVO moraVO = new MoraVO();
                UsersDTO usersDTO = usersManager.getUser(item.getUid());
                GiftDTO giftDTO = giftManager.getValidGiftById(item.getGiftId());
                moraVO.setUid(usersDTO.getUid());
                moraVO.setErbanNo(usersDTO.getErbanNo());
                moraVO.setNick(usersDTO.getNick());
                moraVO.setAvatar(usersDTO.getAvatar());
                moraVO.setRecordId(item.getId());
                moraVO.setGiftUrl(giftDTO.getPicUrl());
                moraVO.setGiftId(item.getGiftId());
                moraVO.setGiftName(giftDTO.getGiftName());
                moraVO.setGiftNum(item.getNum().toString());
                moraVO.setCharmLevel(levelManager.getUserCharmLevelSeq(usersDTO.getUid()));
                moraVO.setExperienceLevel(levelManager.getUserExperienceLevelSeq(usersDTO.getUid()));
                moraVO.setSubject("发起猜拳");
                moraVO.setCreateTime(item.getCreateTime());
                data.put("roomId", roomId);
                data.put("timestamps", System.currentTimeMillis());
                data.put("moraRecordMessage", gson.toJson(moraVO));
                msgInfo.setData(data);
                msgInfo.setFirst(DefMsgType.moraPK);
                msgInfo.setSecond(DefMsgType.moraSend);
                moraMessageVOS.add(msgInfo);
            });
        }
        return moraMessageVOS;
    }



    /**
     * 获取猜拳信息
     *
     * @param uid         uid
     * @param roomId      房间ID
     * @param probability 概率(1.高 2.中 3.低) 默认低
     * @return MoraInfoVO
     */
    @Override
    public MoraInfoVO getMoraInfo(Long uid, Long roomId, Integer probability) throws WebServiceException{
        UsersDTO usersDTO = checkParams(uid,probability,null);
        RoomDTO roomDTO = roomManager.getRoom(roomId);
        if(roomDTO == null){
            throw new WebServiceException(WebServiceCode.ADMIN_ROOM_NOTEXIT);
        }
        MoraAwardDTO moraAwardDTO = getMoraAwardInfo(probability);
        if(moraAwardDTO == null){
            throw new WebServiceException(WebServiceCode.MORA_PROBABILITY_NOT_EXISTS);
        }

        LevelVO levelVO = levelManager.getLevelExperience(usersDTO.getUid());
        MoraInfoVO moraInfoVO = new MoraInfoVO();
        if(levelVO.getLevel() >= moraAwardDTO.getGrade()){
            moraInfoVO.setGiftInfoVOList(giftInfo(moraAwardDTO,probability));
        }else{
            throw new WebServiceException(WebServiceCode.MORA_LEVEL_NOT_ENOUGH);
        }
        moraInfoVO.setNum(getMoraNumByUid(uid));
        SysConfDTO sysConfDTO = sysConfManager.getSysConf(SysConfigId.mora_timeout);
        moraInfoVO.setMoraTime(sysConfDTO == null ? 5 : Integer.valueOf(sysConfDTO.getConfigValue()));
        return moraInfoVO;
    }

    /**
     * 根据获取礼物信息
     * @param probability 概率(1.高 2.中 3.低)
     * @param moraAwardDTO moraAwardDTO 等级
     * @return List<GiftInfoVO>
     */
    private List<GiftInfoVO> giftInfo(MoraAwardDTO moraAwardDTO,Integer probability)throws WebServiceException{
        List<GiftInfoVO> giftInfoVOS = Lists.newArrayList();
        if(moraAwardDTO == null){
            return giftInfoVOS;
        }
        Type type = new TypeToken<ArrayList<GiftInfoDTO>>() {}.getType();
        List<GiftInfoDTO> giftInfoDTOList =  gson.fromJson(moraAwardDTO.getJson(),type);
        if(giftInfoDTOList.size() > 0){
            giftInfoDTOList.forEach(item ->{
                GiftInfoVO giftInfoVO = new GiftInfoVO();
                GiftDTO giftDTO = giftManager.getValidGiftById(item.getGiftId());
                giftInfoVO.setGiftId(item.getGiftId());
                giftInfoVO.setGiftGold(giftDTO.getGoldPrice());
                giftInfoVO.setGiftName(giftDTO.getGiftName());
                giftInfoVO.setGiftNum(item.getNum());
                giftInfoVO.setGiftUrl(giftDTO.getPicUrl());
                giftInfoVOS.add(giftInfoVO);
            });
        }
        Collections.sort(giftInfoVOS, new Comparator<GiftInfoVO>() {
            @Override
            public int compare(GiftInfoVO o1, GiftInfoVO o2) {
                return o1.getGiftGold().intValue() - o2.getGiftGold().intValue();
            }
        });
        return giftInfoVOS;
    }



    /**
     * 获取次数
     * @param uid uid
     * @return Integer
     */
    private Integer getMoraNum(Long uid){
        Long cache = redisManager.hincrBy(RedisKey.mora_num.getKey(),uid.toString(),1L);
        redisManager.hset(RedisKey.mora_lave_num.getKey(),uid.toString(),String.valueOf(cache));
        return cache.intValue();
    }

    private Integer getTotalMoraNumByUid(Long uid){
        String cache = redisManager.hget(RedisKey.mora_lave_num.getKey(),uid.toString());
        if(cache == null){
            return 0;
        }
        return Integer.valueOf(cache);
    }

    private Integer getMoraNumByUid(Long uid){
        String cache = redisManager.hget(RedisKey.mora_lave_num.getKey(),uid.toString());
        if(StringUtils.isNotBlank(cache)){
            if(MAX_NUM - Integer.valueOf(cache) < 0){
                return 0;
            }
            return  MAX_NUM - Integer.valueOf(cache);
        }else{
            return MAX_NUM;
        }
    }

    /**
     * 根据概率获取奖励配置信息
     * @param probability 概率(1.高 2.中 3.低)
     * @return MoraAwardDTO
     */
    private MoraAwardDTO getMoraAwardInfo(Integer probability){
        String cacheResult = redisManager.hget(RedisKey.mora_award.getKey(),probability.toString());
        if(StringUtils.isNotBlank(cacheResult)){
            return gson.fromJson(cacheResult,MoraAwardDTO.class);
        }else{
            MoraAwardDTO moraAwardDTO = moraDAO.selectByProbability(probability);
            if(moraAwardDTO != null){
                redisManager.hset(RedisKey.mora_award.getKey(),probability.toString(),gson.toJson(moraAwardDTO));
                return moraAwardDTO;
            }
        }
        return null;
    }

    /**
     * 确认发起Pk
     *
     * @param uid         uid
     * @param roomId      房间ID
     * @param probability 概率(1.高 2.中 3.低)
     * @param choose      选择(1.剪刀 2.石头 3.布)
     * @param giftId      礼物ID
     * @param giftNum     数量
     * @return String
     */
    @Override
    public String confirmPk(Long uid, Long roomId, Integer probability, Integer choose, Integer giftId, Integer giftNum) throws WebServiceException{
        UsersDTO usersDTO = checkParams(uid,probability,choose);

        RoomDTO roomDTO = roomManager.getRoom(roomId);
        Integer moraNum = getTotalMoraNumByUid(uid);
        if(moraNum >= MAX_NUM){
            throw new WebServiceException(WebServiceCode.MORA_NUMBER_TIMES_EXCEEDED);
        }
        log.info("[mora] 用户官方号:{},昵称:{},uid:{}在{}发起了猜拳,选择概率:{},选择结果:{},剩余次数:{}",usersDTO.getErbanNo(),usersDTO.getNick(),usersDTO.getUid(),roomId,probability,choose,moraNum);
        //查询礼物
        GiftDTO giftDTO = giftManager.getValidGiftById(giftId);
        //获取扣除金币总数
        Integer goldPrice = giftDTO.getGoldPrice().intValue() * giftNum;
        //减金币
        userPurseManager.updateReduceGold(usersDTO.getUid(), goldPrice, false,"发起PK猜拳", null);
        MoraRecordDO moraRecordDO = new MoraRecordDO();
        moraRecordDO.setRoomId(roomId);
        moraRecordDO.setRoomUid(roomDTO.getUid());
        moraRecordDO.setType(1);
        moraRecordDO.setUid(uid);
        moraRecordDO.setProbability(probability);
        moraRecordDO.setTotal(goldPrice.longValue());
        moraRecordDO.setGiftId(giftId);
        moraRecordDO.setNum(giftNum);
        moraRecordDO.setChoose(choose);
        moraRecordDO.setRefId(null);
        moraRecordDO.setIsReturnGold(3);
        moraRecordDO.setIsFinish(2);
        moraRecordDO.setIsValid(1);
        moraRecordDO.setExpirationDate(DateUtils.dateToStr(new Date()));
        moraRecordDO.setResult(null);
        moraRecordDO.setCreateTime(new Date());
        moraRecordDAO.save(moraRecordDO);
        sendRoomMsg(usersDTO,moraRecordDO.getId(),giftDTO,roomDTO.getRoomId(),giftNum,"发起猜拳",DefMsgType.moraSend,"");
        asyncNetEaseTrigger.sendMsg(usersDTO.getUid().toString(),"您发起猜拳，扣除了"+giftDTO.getGiftName()+"礼物");
        getMoraNum(uid);
        return moraRecordDO.getId().toString();
    }

    /**
     * 校验参数
     * @param uid uid
     * @param probability 概率(1.高 2.中 3.低)
     * @return UsersDTO
     * @throws WebServiceException WebServiceException
     */
    private UsersDTO checkParams(Long uid,Integer probability,Integer choose)throws WebServiceException{
        if(!probability.equals(1) && !probability.equals(2) && !probability.equals(3)){
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        if(choose != null){
            if(!choose.equals(1) && !choose.equals(2) && !choose.equals(3)){
                throw new WebServiceException(WebServiceCode.PARAM_ERROR);
            }
        }

        UsersDTO usersDTO =  usersManager.getUser(uid);
        if(usersDTO == null){
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        return usersDTO;
    }

    /**
     * 加入参与PK
     *
     * @param uid      uid
     * @param recordId 发起记录ID
     * @return JoinInfoVO
     */
    @Override
    public JoinInfoVO joinPk(Long uid, String recordId)throws WebServiceException {
        Integer moraNum = getTotalMoraNumByUid(uid);
        if(moraNum >= MAX_NUM){
            throw new WebServiceException(WebServiceCode.MORA_NUMBER_TIMES_EXCEEDED);
        }
        String lockValue = redisManager.lock(RedisKey.mora_award_lock.getKey(recordId),3 * 10000);
        if (StringUtils.isBlank(lockValue)) {
            throw new WebServiceException(WebServiceCode.MORA_PK_SLOW_DOWN);
        }
        try {
            JoinInfoVO joinInfoVO = new JoinInfoVO();
            UsersDTO usersDTO =  checkUsersParams(uid);
            MoraRecordDO moraRecordDO =  checkMoraRecordParams(recordId);
            if(moraRecordDO.getIsFinish() == 1){
                throw new WebServiceException(WebServiceCode.MORA_PK_OVER);
            }

            if(uid.equals(moraRecordDO.getUid())){
                throw new WebServiceException(WebServiceCode.MORA_SELF_NOT_PK_OVER);
            }

            UserPurseDTO userPurseDTO = userPurseManager.getUserPurse(uid);
            if(userPurseDTO.getGoldNum() <= moraRecordDO.getTotal()){
                throw new WebServiceException(WebServiceCode.MORA_INSUFFICIEN_BALANCE);
            }
            UsersDTO opponentUsersDTO = usersManager.getUser(moraRecordDO.getUid());
            GiftDTO giftDTO = giftManager.getValidGiftById(moraRecordDO.getGiftId());
            joinInfoVO.setOpponentAvatar(opponentUsersDTO.getAvatar());
            joinInfoVO.setOpponentErBanNo(opponentUsersDTO.getErbanNo());
            joinInfoVO.setOpponentUid(opponentUsersDTO.getUid());
            joinInfoVO.setOpponentNick(opponentUsersDTO.getNick());
            joinInfoVO.setAvatar(usersDTO.getAvatar());
            joinInfoVO.setUid(usersDTO.getUid());
            joinInfoVO.setGiftUrl(giftDTO.getPicUrl());
            joinInfoVO.setErBanNo(usersDTO.getErbanNo());
            joinInfoVO.setGiftId(moraRecordDO.getGiftId());
            joinInfoVO.setGiftNum(moraRecordDO.getNum());
            joinInfoVO.setGiftName(giftDTO.getGiftName());
            joinInfoVO.setNick(usersDTO.getNick());
            joinInfoVO.setRecordId(moraRecordDO.getId().toString());
            return joinInfoVO;
        }finally {
            redisManager.unlock(RedisKey.mora_award_lock.getKey(recordId),lockValue);
        }

    }


    /**
     * 确认加入
     * @param uid uid
     * @param recordId 发起记录ID
     * @param choose 选择(1.剪刀 2.石头 3.布)
     * @throws WebServiceException WebServiceException
     */
    @Override
    public void confirmJoinPk(Long uid,String recordId,Integer choose)throws WebServiceException{
        Integer moraNum = getTotalMoraNumByUid(uid);
        if(moraNum >= MAX_NUM){
            throw new WebServiceException(WebServiceCode.MORA_NUMBER_TIMES_EXCEEDED);
        }
        //参与人
        UsersDTO participateUsers =  checkUsersParams(uid);
        if(participateUsers == null){
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        MoraRecordDO moraRecordDO = checkMoraRecordParams(recordId);
        if(moraRecordDO == null){
            throw new WebServiceException(WebServiceCode.MORA_RECORD_NOT_EXISTS);
        }

        if(moraRecordDO.getIsFinish() == 1){
            throw new WebServiceException(WebServiceCode.MORA_PK_OVER);
        }

        moraRecordDO.setIsFinish(1);
        moraRecordDAO.updateById(moraRecordDO);

        GiftDTO giftDTO = giftManager.getValidGiftById(moraRecordDO.getGiftId());
        //发起人
        UsersDTO initiateUsers = usersManager.getUser(moraRecordDO.getUid());
        userPurseManager.updateReduceGold(participateUsers.getUid(), moraRecordDO.getTotal().intValue(), false,"参与PK猜拳", null);
        asyncNetEaseTrigger.sendMsg(participateUsers.getUid().toString(),"您参与了猜拳，扣除了"+giftDTO.getGiftName()+"礼物");
        MoraRecordDO recordDO = new MoraRecordDO();
        recordDO.setRoomId(moraRecordDO.getRoomId());
        recordDO.setRoomUid(moraRecordDO.getRoomUid());
        recordDO.setUid(participateUsers.getUid());
        recordDO.setType(2);
        recordDO.setProbability(moraRecordDO.getProbability());
        recordDO.setTotal(moraRecordDO.getTotal());
        recordDO.setGiftId(moraRecordDO.getGiftId());
        recordDO.setNum(moraRecordDO.getNum());
        recordDO.setChoose(choose);
        recordDO.setIsValid(moraRecordDO.getIsValid());
        recordDO.setRefId(moraRecordDO.getId());
        recordDO.setIsReturnGold(3);
        recordDO.setIsFinish(2);
        recordDO.setExpirationDate(moraRecordDO.getExpirationDate());
        recordDO.setResult(null);
        recordDO.setCreateTime(new Date());
        moraRecordDAO.save(recordDO);
        moraRecordDO.setIsValid(2);
        moraRecordDAO.updateById(moraRecordDO);

        //1:发起人胜 2:平局 3:发起人负
        Integer result = getResult(moraRecordDO.getChoose(),choose);
        log.info("[mora] 用户官方号:{},昵称:{},uid:{}在{}加入猜拳PK,选择结果:{},PK结果:{},recordId:{}",participateUsers.getErbanNo(),participateUsers.getNick(),participateUsers.getUid(),moraRecordDO.getRoomId(),choose,result,recordId);

        //发起人胜了
        if(result == 1){
            moraRecordDO.setResult(1);
            recordDO.setResult(2);
            moraRecordDO.setIsReturnGold(1);
            recordDO.setIsReturnGold(2);
            sendRoomMsg(initiateUsers,moraRecordDO.getId(),giftDTO,moraRecordDO.getRoomId(),moraRecordDO.getNum(),"赢了",DefMsgType.moraResult,participateUsers.getNick());
            asyncNetEaseTrigger.sendMsg(participateUsers.getUid().toString(),"您参与的猜拳跪了。失去"+ giftDTO.getGiftName() +"礼物");
            asyncNetEaseTrigger.sendMsg(initiateUsers.getUid().toString(),"您猜拳胜利归还您的礼物，并获得"+ giftDTO.getGiftName() +"礼物");
            userPurseManager.updateAddGold(initiateUsers.getUid(), moraRecordDO.getTotal(), false,false,"猜拳PK赢", null, null);
            userGiftPurseManager.updateUserGiftPurse(initiateUsers.getUid(), moraRecordDO.getGiftId(), moraRecordDO.getNum());

        }else if(result == 3){
            moraRecordDO.setResult(2);
            recordDO.setResult(1);
            recordDO.setIsReturnGold(1);
            moraRecordDO.setIsReturnGold(2);
            userPurseManager.updateAddGold(participateUsers.getUid(), moraRecordDO.getTotal(), false,false,"猜拳PK赢", null, null);

            asyncNetEaseTrigger.sendMsg(initiateUsers.getUid().toString(),"您参与的猜拳跪了。失去"+ giftDTO.getGiftName() +"礼物");
            asyncNetEaseTrigger.sendMsg(participateUsers.getUid().toString(),"您猜拳胜利归还您的礼物，并获得"+ giftDTO.getGiftName() +"礼物");
            userGiftPurseManager.updateUserGiftPurse(participateUsers.getUid(), moraRecordDO.getGiftId(), moraRecordDO.getNum());
            sendRoomMsg(participateUsers,moraRecordDO.getId(),giftDTO,moraRecordDO.getRoomId(),moraRecordDO.getNum(),"赢了",DefMsgType.moraResult,initiateUsers.getNick());

        }else if(result == 2){
            moraRecordDO.setResult(3);
            recordDO.setResult(3);
            moraRecordDO.setIsReturnGold(1);
            recordDO.setIsReturnGold(1);
            //发起人退回金币
            userPurseManager.updateAddGold(initiateUsers.getUid(), moraRecordDO.getTotal(), false,false,"猜拳PK平局", null, null);
            //参与人退回金币
            userPurseManager.updateAddGold(participateUsers.getUid(), moraRecordDO.getTotal(), false,false,"猜拳PK平局", null, null);
            asyncNetEaseTrigger.sendMsg(initiateUsers.getUid().toString(),"你们太有默契了,选择了一样的手势。");
            asyncNetEaseTrigger.sendMsg(participateUsers.getUid().toString(),"你们太有默契了,选择了一样的手势。");
            sendRoomMsg(initiateUsers,moraRecordDO.getId(),giftDTO,moraRecordDO.getRoomId(),moraRecordDO.getNum(),"平",DefMsgType.moraDraw,participateUsers.getNick());
        }
        recordDO.setIsFinish(1);
        recordDO.setRefId(moraRecordDO.getId());
        moraRecordDAO.updateById(moraRecordDO);
        moraRecordDAO.updateByRefId(recordDO);
        getMoraNum(uid);
    }

    /**
     * 获取PK结果
     * @param joinChoose 加入者 选择(1.剪刀 2.石头 3.布)
     * @param sponsorChoose 创建者 选择(1.剪刀 2.石头 3.布)
     * @return Integer 1:发起人胜 2:平局 3:发起人负
     */
    private Integer getResult(Integer sponsorChoose,Integer joinChoose)throws WebServiceException{
        if (sponsorChoose <= 0 || sponsorChoose >= 4 || joinChoose <= 0 || joinChoose >= 4) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        if (((sponsorChoose == 1) && (joinChoose == 2)) || ((sponsorChoose == 2) && (joinChoose == 3)) || ((sponsorChoose == 3) && (joinChoose == 1))) {
            return 3;
        } else if (sponsorChoose == 1 && joinChoose == 3) {
            return 1;
        } else if (sponsorChoose == 2 && joinChoose == 1 || sponsorChoose == 3 && joinChoose == 2) {
            return 1;
        }
        return 2;
    }

    private UsersDTO checkUsersParams(Long uid)throws WebServiceException{
        UsersDTO usersDTO =  usersManager.getUser(uid);
        if(usersDTO == null){
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        return usersDTO;
    }

    private MoraRecordDO checkMoraRecordParams(String recordId)throws WebServiceException{
        MoraRecordDO moraRecordDO =  moraRecordDAO.selectByMoraRecordId(Integer.valueOf(recordId));
        if(moraRecordDO == null){
            throw new WebServiceException(WebServiceCode.MORA_RECORD_NOT_EXISTS);
        }
        return moraRecordDO;
    }

    /**
     * 猜拳记录
     *
     * @param uid uid
     * @return MoraRecordVO
     */
    @Override
    public List<MoraRecordVO> getMoraRecord(Long uid,Integer current, Integer pageSize) throws WebServiceException{
        if (current == null && pageSize == null) {
            current = 0;
            pageSize = 20;
        }
        current = current * pageSize;
        List<MoraRecordVO> moraRecordVOS = new ArrayList<>();
        List<MoraRecordDO> moraRecordDOS = moraRecordDAO.selectByPage(uid,current,pageSize);
        if(moraRecordDOS.size() > 0){
            moraRecordDOS.forEach(item ->{
                GiftDTO giftDTO = giftManager.getValidGiftById(item.getGiftId());
                MoraRecordVO moraRecordVO = new MoraRecordVO();
                UsersDTO usersDTO = usersManager.getUser(item.getUid());
                moraRecordVO.setCreateTime(item.getCreateTime());
                moraRecordVO.setGiftId(item.getGiftId());
                moraRecordVO.setGiftName(giftDTO.getGiftName());
                moraRecordVO.setGiftUrl(giftDTO.getPicUrl());
                moraRecordVO.setNum(item.getNum());
                moraRecordVO.setAvatar(usersDTO.getAvatar());
                moraRecordVO.setErbanNo(usersDTO.getErbanNo());
                moraRecordVO.setUid(usersDTO.getUid());
                moraRecordVO.setNick(usersDTO.getNick());
                if(item.getResult() == 1){
                    moraRecordVO.setSubject("出了" + getChooseByName(item.getChoose()) + "赢了");
                }else if(item.getResult() == 2){
                    moraRecordVO.setSubject("出了" + getChooseByName(item.getChoose()) +  "输了");
                }else if(item.getResult() == 3){
                    moraRecordVO.setSubject("默契一样,平了");
                }
                moraRecordVOS.add(moraRecordVO);
            });
        }
        return moraRecordVOS;
    }

    private String getChooseByName(Integer choose){
        String result = "";
        if(choose == 1){
            result= "剪刀";
        }else if(choose == 2){
            result = "石头";
        }else if(choose == 3){
            result =  "布";
        }
        return result;
    }


    /**
     * 封装发起PK房间自定义消息
     * @param usersDTO  用户数据
     * @param recordId 记录ID
     * @param giftDTO 礼物数据
     * @param roomId 房间id
     * @param giftNum 数量
     */
    private void sendRoomMsg(UsersDTO usersDTO,Integer recordId,GiftDTO giftDTO,Long roomId,Integer giftNum,String subject,Integer second,String opponentNick){
        MoraRecordMessage moraRecordMessage = new MoraRecordMessage();
        moraRecordMessage.setRecordId(recordId.toString());
        moraRecordMessage.setAvatar(usersDTO.getAvatar());
        moraRecordMessage.setCharmLevel(levelManager.getUserCharmLevelSeq(usersDTO.getUid()));
        moraRecordMessage.setErbanNo(usersDTO.getErbanNo());
        moraRecordMessage.setExperienceLevel(levelManager.getUserExperienceLevelSeq(usersDTO.getUid()));
        moraRecordMessage.setGiftId(giftDTO.getGiftId());
        moraRecordMessage.setGiftName(giftDTO.getGiftName());
        moraRecordMessage.setGiftNum(giftNum);
        moraRecordMessage.setGiftUrl(giftDTO.getPicUrl());
        moraRecordMessage.setNick(usersDTO.getNick());
        moraRecordMessage.setSubject(subject);
        moraRecordMessage.setUid(usersDTO.getUid());
        moraRecordMessage.setOpponentNick(opponentNick);
        ImRoomMessage msgInfo = new ImRoomMessage();
        Map<String, Object> data = new HashMap<>(8);
        data.put("roomId", roomId);
        data.put("timestamps", System.currentTimeMillis());
        data.put("moraRecordMessage", gson.toJson(moraRecordMessage));
        msgInfo.setRoomId(String.valueOf(roomId));
        msgInfo.setCustom(new Custom(DefMsgType.moraPK, second, data));
        try {
            imRoomManager.pushRoomCustomMsg(msgInfo);
        } catch (Exception e) {
            log.error("发送猜拳PK消息异常，消息:>{}，异常信息：", msgInfo, e);
        }
    }

    /**
     * 获取概率
     * @param uid    uid
     * @param roomId roomId
     * @return List<ProbabilityVO> 概率(1.高 2.中 3.低)
     */
    @Override
    public List<ProbabilityVO> getProbability(Long uid, Long roomId) throws WebServiceException{
        List<MoraAwardDTO> moraAwardDTOList = moraDAO.selectByMoraAward();
        if(moraAwardDTOList == null){
            return null;
        }
        List<ProbabilityVO> probabilityVOS = Lists.newArrayList();
        for (MoraAwardDTO item : moraAwardDTOList) {
            ProbabilityVO probabilityVO = new ProbabilityVO();
            int levelSeq = levelManager.getUserExperienceLevelSeq(uid);
            if (levelSeq >= item.getGrade()) {
                if (item.getProbability() == 1) {
                    probabilityVO.setProbability(1);
                    probabilityVO.setName("高");
                } else if (item.getProbability() == 2) {
                    probabilityVO.setProbability(2);
                    probabilityVO.setName("中");
                } else if (item.getProbability() == 3) {
                    probabilityVO.setProbability(3);
                    probabilityVO.setName("低");
                }
                probabilityVOS.add(probabilityVO);
            }
        }
        Collections.sort(probabilityVOS, new Comparator<ProbabilityVO>() {
            @Override
            public int compare(ProbabilityVO o1, ProbabilityVO o2) {
                return o2.getProbability() - o1.getProbability();
            }
        });
        return probabilityVOS;
    }
}
