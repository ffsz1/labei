package com.juxiao.xchat.service.api.event.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.event.WeekStarGiftDAO;
import com.juxiao.xchat.dao.event.dto.WeekStarGiftDTO;
import com.juxiao.xchat.dao.event.dto.WeekStarGiftNoticeDTO;
import com.juxiao.xchat.dao.event.dto.WeekStarItemRewardDTO;
import com.juxiao.xchat.dao.item.dto.GiftCarDTO;
import com.juxiao.xchat.dao.item.dto.GiftDTO;
import com.juxiao.xchat.dao.item.dto.HeadwearDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.item.GiftCarManager;
import com.juxiao.xchat.manager.common.item.GiftManager;
import com.juxiao.xchat.manager.common.item.HeadwearManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.event.WeekStarGiftService;
import com.juxiao.xchat.service.api.event.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author chris
 * @Title: 周星榜service接口实现
 * @date 2019-05-20
 * @time 10:31
 */
@Slf4j
@Service
public class WeekStarGiftServiceImpl implements WeekStarGiftService {

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private UsersManager usersManager;

    @Autowired
    private GiftManager giftManager;

    @Autowired
    private GiftCarManager giftCarManager;

    @Autowired
    private HeadwearManager headwearManager;

    @Autowired
    private Gson gson;

    @Autowired
    private WeekStarGiftDAO weekStarGiftDAO;

    /**
     * 获取本周礼物,周星奖励,礼物预告
     *
     * @return WeekStarVO
     */
    @Override
    public WeekStarVO getWeekStarGift() {
        WeekStarVO weekStarVO = new WeekStarVO();
        weekStarVO.setWeekStarGiftVO(getWeekStarGiftList());
        weekStarVO.setWeekStarGiftNoticeVO(getWeekStarGiftNoticeList());
        weekStarVO.setWeekStarItemRewardVO(getWeekStarItemRewardList());
        weekStarVO.setLastWeekStarGiftVO(getLastWeekStarGiftList());
        return weekStarVO;
    }

    /**
     * 上周周星礼物
     * @return List<WeekStarGiftVO>
     */
    private List<WeekStarGiftVO> getLastWeekStarGiftList() {
        Map<String, String> result = Optional.ofNullable(redisManager.hgetAll(RedisKey.week_last_star_gift.getKey())).orElse(new HashMap<>());
        List<WeekStarGiftVO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            int i = 1;
            for (Map.Entry<String, String> entry : result.entrySet()) {
                WeekStarGiftVO weekStarGiftVO = new WeekStarGiftVO();
                weekStarGiftVO.setGiftId(Integer.valueOf(entry.getValue()));
                GiftDTO giftDTO = giftManager.getGiftById(Integer.valueOf(entry.getValue()));
                weekStarGiftVO.setGiftName(giftDTO.getGiftName());
                weekStarGiftVO.setGoldPrice(giftDTO.getGoldPrice());
                weekStarGiftVO.setPicUrl(giftDTO.getPicUrl());
                weekStarGiftVO.setSeq(i + 1);
                list.add(weekStarGiftVO);
            }
            return list;
        }
        return list;
    }

    /**
     * 获取本周周星礼物
     *
     * @return list
     */
    private List<WeekStarGiftVO> getWeekStarGiftList() {
        List<WeekStarGiftVO> weekStarGiftVOList = Lists.newArrayList();
        List<WeekStarGiftDTO> weekStarGiftDTOS = getWeekStartGift();
        if(weekStarGiftDTOS != null && weekStarGiftDTOS.size() > 0){
            weekStarGiftDTOS.forEach(item ->{
                WeekStarGiftVO weekStarGiftVO = new WeekStarGiftVO();
                weekStarGiftVO.setGiftId(item.getGiftId());
                GiftDTO giftDTO = giftManager.getGiftById(item.getGiftId());
                weekStarGiftVO.setGiftName(giftDTO.getGiftName());
                weekStarGiftVO.setGoldPrice(giftDTO.getGoldPrice());
                weekStarGiftVO.setPicUrl(giftDTO.getPicUrl());
                weekStarGiftVO.setSeq(item.getSeq());
                weekStarGiftVOList.add(weekStarGiftVO);
            });
        }
        Collections.sort(weekStarGiftVOList, Comparator.comparing(WeekStarGiftVO::getSeq));
        return weekStarGiftVOList;
    }

    /**
     * 获取上周明星TOP10
     *
     * @return list
     */
    @Override
    public List<WeekStartRankVO> getLastWeekStarList(Integer giftId) throws WebServiceException{
        if(giftId == null){
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        List<WeekStartRankVO> weekStartVO = Lists.newArrayList();
        Integer pageNum = 1;
        Integer pageSize = 10;
        Integer skip = (pageNum - 1) * pageSize;
        Set<String> stringSet = redisManager.reverseZsetRange(RedisKey.week_last_star.getKey(giftId.toString()), skip, skip + pageSize - 1);
        if (stringSet != null && stringSet.size() > 0) {
            for (String str : stringSet) {
                weekStartVO.add(getUsersLastWeekStartInfo(Long.valueOf(str),giftId));
            }
        }
        return weekStartVO;
    }

    private WeekStartRankVO getUsersLastWeekStartInfo(Long uid, Integer giftId) throws WebServiceException{
        GiftDTO giftDTO = giftManager.getGiftById(giftId);
        if(giftDTO == null){
            throw new WebServiceException(WebServiceCode.GIFT_NOT_EXISTS);
        }
        UsersDTO usersDTO = usersManager.getUser(uid);
        if(usersDTO == null){
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        WeekStartRankVO lastWeekStarVO = new WeekStartRankVO();
        Long ranking = redisManager.zrevrank(RedisKey.week_last_star.getKey(giftId.toString()), uid.toString());
        if(ranking != null){
            lastWeekStarVO.setRank(ranking + 1);
            lastWeekStarVO.setTotal(redisManager.zscore(RedisKey.week_last_star.getKey(giftId.toString()), uid.toString()));
        }

        lastWeekStarVO.setAvatar(usersDTO.getAvatar());
        lastWeekStarVO.setErbanNo(usersDTO.getErbanNo());
        lastWeekStarVO.setGender(usersDTO.getGender());
        lastWeekStarVO.setNick(usersDTO.getNick());
        lastWeekStarVO.setUid(usersDTO.getUid());
        return lastWeekStarVO;
    }

    /**
     * 获取下周周星礼物预告
     *
     * @return list
     */
    private List<WeekStarGiftNoticeVO> getWeekStarGiftNoticeList() {
        List<WeekStarGiftNoticeVO> weekStarGiftNoticeVOList = Lists.newArrayList();
        List<WeekStarGiftNoticeDTO> weekStarGiftNoticeDTOS = getWeekStarGiftNotice();
        if(weekStarGiftNoticeDTOS != null && weekStarGiftNoticeDTOS.size() > 0){
            weekStarGiftNoticeDTOS.forEach(item ->{
                WeekStarGiftNoticeVO weekStarGiftNoticeVO = new WeekStarGiftNoticeVO();
                weekStarGiftNoticeVO.setGiftId(item.getGiftId());
                GiftDTO giftDTO = giftManager.getGiftById(item.getGiftId());
                weekStarGiftNoticeVO.setGiftName(giftDTO.getGiftName());
                weekStarGiftNoticeVO.setGoldPrice(giftDTO.getGoldPrice());
                weekStarGiftNoticeVO.setPicUrl(giftDTO.getPicUrl());
                weekStarGiftNoticeVO.setSeq(item.getSeq());
                weekStarGiftNoticeVOList.add(weekStarGiftNoticeVO);
            });
        }
        Collections.sort(weekStarGiftNoticeVOList, Comparator.comparing(WeekStarGiftNoticeVO::getSeq));
        return weekStarGiftNoticeVOList;
    }

    /**
     * 获取周星奖励
     *
     * @return list
     */
    private List<WeekStarItemRewardVO> getWeekStarItemRewardList() {
        List<WeekStarItemRewardVO> weekStarItemRewardVOS = Lists.newArrayList();
        List<WeekStarItemRewardDTO> weekStarItemRewardDTOS = weekStarGiftDAO.findWeekStarItemReward();
        if(weekStarItemRewardDTOS != null && weekStarItemRewardDTOS.size() > 0){
            weekStarItemRewardDTOS.forEach(item ->{
                WeekStarItemRewardVO weekStarItemRewardVO = new WeekStarItemRewardVO();
                weekStarItemRewardVO.setGiftId(item.getGiftId());
                GiftDTO giftDTO = giftManager.getGiftById(item.getGiftId());
                weekStarItemRewardVO.setGiftName(giftDTO.getGiftName());
                weekStarItemRewardVO.setItemContent(item.getContent());
                weekStarItemRewardVO.setItemDays(item.getDays());
                if(item.getType() == 1){
                    GiftCarDTO giftCarDTO = giftCarManager.getGiftCar(item.getItemId());
                    weekStarItemRewardVO.setItemName(giftCarDTO.getCarName());
                    weekStarItemRewardVO.setItemPrice(giftCarDTO.getGoldPrice());
                    weekStarItemRewardVO.setItemId(giftCarDTO.getCarId());
                }else{
                    HeadwearDTO headwearDTO = headwearManager.getHeadwear(item.getItemId());
                    weekStarItemRewardVO.setItemName(headwearDTO.getHeadwearName());
                    weekStarItemRewardVO.setItemPrice(headwearDTO.getGoldPrice());
                    weekStarItemRewardVO.setItemId(headwearDTO.getHeadwearId());
                }
                weekStarItemRewardVO.setItemSeq(item.getSeq());
                weekStarItemRewardVOS.add(weekStarItemRewardVO);

            });
        }
        Collections.sort(weekStarItemRewardVOS, Comparator.comparing(WeekStarItemRewardVO::getItemSeq));
        return weekStarItemRewardVOS;
    }

    /**
     * 获取自己周星排名
     *
     * @param uid uid
     * @param giftId  giftId
     * @return WeekStartRankVO
     */
    @Override
    public WeekStartRankVO getMyWeekStar(Long uid,Integer giftId) throws WebServiceException {
        if(uid == null && giftId == null){
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        return getUsersWeekStartInfo(uid,giftId);
    }

    /**
     * 获取本周明星排名
     * @param giftId giftId
     * @return list
     */
    @Override
    public List<WeekStartRankVO> getWeekStartRank(Integer giftId) throws WebServiceException{
        if(giftId == null){
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        List<WeekStartRankVO> weekStartVO = Lists.newArrayList();

        Integer pageNum = 1;
        Integer pageSize = 10;
        Integer skip = (pageNum - 1) * pageSize;
        Set<String> stringSet = redisManager.reverseZsetRange(RedisKey.week_star_the_week.getKey(giftId.toString()), skip, skip + pageSize - 1);
        if (stringSet != null && stringSet.size() > 0) {
            for (String str : stringSet) {
                weekStartVO.add(getUsersWeekStartInfo(Long.valueOf(str),giftId));
            }
        }
        return weekStartVO;
    }

    private WeekStartRankVO getUsersWeekStartInfo(Long uid,Integer giftId) throws WebServiceException{
        GiftDTO giftDTO = giftManager.getGiftById(giftId);
        if(giftDTO == null){
            throw new WebServiceException(WebServiceCode.GIFT_NOT_EXISTS);
        }
        UsersDTO usersDTO = usersManager.getUser(uid);
        if(usersDTO == null){
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        Long ranking = redisManager.zrevrank(RedisKey.week_star_the_week.getKey(giftId.toString()), uid.toString());
        WeekStartRankVO weekStartRankVO = new WeekStartRankVO();
        if(ranking != null){
            weekStartRankVO.setRank(ranking + 1);
            weekStartRankVO.setTotal(redisManager.zscore(RedisKey.week_star_the_week.getKey(giftId.toString()), uid.toString()));
        }
        weekStartRankVO.setAvatar(usersDTO.getAvatar());
        weekStartRankVO.setErbanNo(usersDTO.getErbanNo());
        weekStartRankVO.setGender(usersDTO.getGender());
        weekStartRankVO.setNick(usersDTO.getNick());
        weekStartRankVO.setUid(usersDTO.getUid());

        return weekStartRankVO;
    }

    /**
     * 获取缓存中周星礼物 如缓存不存在时查询数据库
     * @return List<WeekStarGiftDTO>
     */
    @Override
    public List<WeekStarGiftDTO> getWeekStartGift(){
        Map<String, String> result = Optional.ofNullable(redisManager.hgetAll(RedisKey.week_star_gift.getKey())).orElse(new HashMap<>());
        List<WeekStarGiftDTO> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(result)) {
            for(Map.Entry<String, String> entry : result.entrySet()) {
                WeekStarGiftDTO weekStarGiftDTO = gson.fromJson(entry.getValue(), WeekStarGiftDTO.class);
                list.add(weekStarGiftDTO);
            }
            return list;
        }else{
            list = weekStarGiftDAO.findWeekStarGift();
            if(list.size() > 0){
                list.forEach(item -> redisManager.hset(RedisKey.week_star_gift.getKey(),item.getGiftId().toString(),gson.toJson(item)));
            }
            return list;
        }
    }


    /**
     * 获取缓存中下周周星礼物预告 如缓存不存在时查询数据库
     * @return List<WeekStarGiftNoticeDTO>
     */
    private List<WeekStarGiftNoticeDTO> getWeekStarGiftNotice(){
        Map<String, String> result = Optional.ofNullable(redisManager.hgetAll(RedisKey.week_star_gift_notice.getKey())).orElse(new HashMap<>());
        List<WeekStarGiftNoticeDTO> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(result)) {
            for(Map.Entry<String, String> entry : result.entrySet()) {
                WeekStarGiftNoticeDTO weekStarGiftNoticeDTO = gson.fromJson(entry.getValue(), WeekStarGiftNoticeDTO.class);
                list.add(weekStarGiftNoticeDTO);
            }
            return list;
        }else{
            list = weekStarGiftDAO.findWeekStarGiftNotice();
            if(list.size() > 0){
                list.forEach(item -> redisManager.hset(RedisKey.week_star_gift_notice.getKey(),item.getGiftId().toString(),gson.toJson(item)));
            }
            return list;
        }
    }

}
