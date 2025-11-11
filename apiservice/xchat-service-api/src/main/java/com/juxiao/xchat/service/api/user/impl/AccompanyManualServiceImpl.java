package com.juxiao.xchat.service.api.user.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.room.dto.RoomDTO;
import com.juxiao.xchat.dao.sysconf.dto.BannerDTO;
import com.juxiao.xchat.dao.sysconf.dto.GeneralReviewWhitelist;
import com.juxiao.xchat.dao.sysconf.dto.ReviewConfigDTO;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.user.AccompanyManualDao;
import com.juxiao.xchat.dao.user.UsersDao;
import com.juxiao.xchat.dao.user.domain.AccompanyTypeDO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.room.RoomManager;
import com.juxiao.xchat.manager.common.room.vo.RoomVo;
import com.juxiao.xchat.manager.common.sysconf.SysConfManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.sysconf.BannerService;
import com.juxiao.xchat.service.api.sysconf.ReviewConfigService;
import com.juxiao.xchat.service.api.user.AccompanyManualService;
import com.juxiao.xchat.service.api.user.vo.AccompanyManualVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chris
 * @Title:
 * @date 2018/11/14
 * @time 15:29
 */
@Service
public class AccompanyManualServiceImpl implements AccompanyManualService {


    @Autowired
    private RedisManager redisManager;

    @Autowired
    private Gson gson;

    @Autowired
    private SysConfManager sysConfManager;

    @Autowired
    private UsersManager usersManager;

    @Autowired
    private AccompanyManualDao accompanyManualDao;

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private ReviewConfigService reviewConfigService;

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private BannerService bannerService;

    /**
     * 获取陪玩推荐逻辑金额
     *
     * @return
     */
    @Override
    public String getAccompanyMoney() {
        SysConfDTO sysConfDTO = sysConfManager.getSysConf("accompany_manual_recomm");
        return  sysConfDTO.getConfigValue();
    }

    @Override
    public List<AccompanyManualVO> getList(Long uid, Integer pageNum, Integer pageSize, String os, String appVersion, String app, String type) {
        String resultJson = redisManager.get(RedisKey.accopany_manual_list.getKey());

        if(StringUtils.isEmpty(resultJson)){
            return Lists.newArrayList();
        }
        if (StringUtils.isNotBlank(resultJson) && !"[]".equals(resultJson)) {
            List<RoomVo> resultRooms = gson.fromJson(resultJson, new TypeToken<List<RoomVo>>() {
            }.getType());
            List<AccompanyManualVO> accompanyManualVOS = new ArrayList<>();
            if ("人气排行".equals(type)) {
                resultRooms.stream().forEach(item -> {
                    UsersDTO usersDTO = usersManager.getUser(item.getUid());
                    if(StringUtils.isNotBlank(usersDTO.getAccompanyType())) {
                        AccompanyManualVO accompanyManualVO = new AccompanyManualVO();
                        BeanUtils.copyProperties(item, accompanyManualVO);
                        accompanyManualVO.setAccompanyType(usersDTO.getAccompanyType());
                        accompanyManualVO.setAvatar(usersDTO.getAvatar());
                        accompanyManualVO.setDefaultTag(usersDTO.getDefaultTag());
                        accompanyManualVO.setErbanNo(usersDTO.getErbanNo());
                        accompanyManualVO.setUid(usersDTO.getUid());
                        accompanyManualVO.setGender(usersDTO.getGender());
                        accompanyManualVO.setHeight(usersDTO.getHeight());
                        accompanyManualVO.setUserVoice(usersDTO.getUserVoice());
                        accompanyManualVO.setVoiceDura(usersDTO.getVoiceDura());
                        accompanyManualVO.setWeight(usersDTO.getWeight());
                        accompanyManualVOS.add(accompanyManualVO);
                    }else{
                        UsersDTO userDto = usersDao.getUser(usersDTO.getUid());
                        redisManager.hset(RedisKey.user.getKey(), String.valueOf(usersDTO.getUid()), gson.toJson(userDto));
                    }
                });
                if (pageNum == null || pageSize == null) {
                    return accompanyManualVOS;
                }
                Integer size = accompanyManualVOS.size();
                Integer skip = (pageNum - 1) * pageSize;
                if (skip >= size) {
                    return Lists.newArrayList();
                }
                if(pageNum == 1){
                    if (skip + pageSize > size) {
                        return accompanyManualVOS.subList(skip, accompanyManualVOS.size());
                    }
                    return accompanyManualVOS.subList(skip, skip + pageSize);
                }else{
                    return Lists.newArrayList();
                }
            } else {
                resultRooms.stream().forEach(item -> {
                    UsersDTO usersDTO = usersManager.getUser(item.getUid());
                    if (StringUtils.isNotBlank(usersDTO.getAccompanyType())) {
                        if (usersDTO.getAccompanyType().equals(type)) {
                            AccompanyManualVO accompanyManualVO = new AccompanyManualVO();
                            BeanUtils.copyProperties(item, accompanyManualVO);
                            accompanyManualVO.setAccompanyType(usersDTO.getAccompanyType());
                            accompanyManualVO.setAvatar(usersDTO.getAvatar());
                            accompanyManualVO.setDefaultTag(usersDTO.getDefaultTag());
                            accompanyManualVO.setErbanNo(usersDTO.getErbanNo());
                            accompanyManualVO.setUid(usersDTO.getUid());
                            accompanyManualVO.setGender(usersDTO.getGender());
                            accompanyManualVO.setHeight(usersDTO.getHeight());
                            accompanyManualVO.setUserVoice(usersDTO.getUserVoice());
                            accompanyManualVO.setVoiceDura(usersDTO.getVoiceDura());
                            accompanyManualVO.setWeight(usersDTO.getWeight());
                            accompanyManualVOS.add(accompanyManualVO);
                        }
                    }else{
                        UsersDTO userDto = usersDao.getUser(usersDTO.getUid());
                        redisManager.hset(RedisKey.user.getKey(), String.valueOf(usersDTO.getUid()), gson.toJson(userDto));
                    }
                });

                if (pageNum == null || pageSize == null) {
                    return accompanyManualVOS;
                }
                Integer size = accompanyManualVOS.size();
                Integer skip = (pageNum - 1) * pageSize;
                if (skip >= size) {
                    return Lists.newArrayList();
                }
                if (skip + pageSize > size) {
                    return accompanyManualVOS.subList(skip, accompanyManualVOS.size());
                }
                return accompanyManualVOS.subList(skip, skip + pageSize);
            }


        }
        return Lists.newArrayList();

    }


    /**
     * 获取陪玩分类列表
     *
     * @param uid
     * @param os
     * @param appVersion
     * @param app
     * @return
     */
    @Override
    public List<AccompanyTypeDO> getAccompanyTypeByList(Long uid, String os, String appVersion, String app, String channel) {
        List<AccompanyTypeDO> accompanyTypeDOS = accompanyManualDao.selectAccompanyTypeByList();
        if(uid != null){
            //获取白名单列表数据
            List<GeneralReviewWhitelist> generalReviewWhitelists = reviewConfigService.getGeneralReviewWhitelistByList();
            if(generalReviewWhitelists.size() > 0){
                //判断是否有需要过滤的用户uid
                boolean flag = generalReviewWhitelists.stream().anyMatch(item -> item.getUid().equals(uid));
                if(flag){
                    return accompanyTypeDOS;
                }
            }
            List<ReviewConfigDTO> reviewConfigDTOS = reviewConfigService.selectByCacheList();
            if (reviewConfigDTOS.size() > 0) {
                //获取用户的充值记录
                Integer chargeTotal = Optional.ofNullable(redisManager.hget(RedisKey.user_charge.getKey(), uid.toString())).map(Integer::parseInt).orElse(0);
                String channels = Optional.ofNullable(channel).orElse("appstore");
                //根据条件获取需要过滤掉的审核内容
                Set<String> removeTags = reviewConfigDTOS.stream().filter(item -> os.equals(item.getSystem())
                        && ("all".equals(item.getChannel()) || channels.equals(item.getChannel()))
                        && appVersion.equals(item.getVersions()) && chargeTotal < item.getRechargeAmount())
                        .map(ReviewConfigDTO::getTagName)
                        .collect(Collectors.toSet());
               return accompanyTypeDOS.stream().filter(item -> !removeTags.contains(item.getName())).collect(Collectors.toList());
            }
        }
        return accompanyTypeDOS;
    }

    @Override
    public List<RoomVo> getCheckAudition() {
        List<RoomVo> roomVoList = Lists.newArrayList();
        List<Long> uidList = Arrays.asList(100033762L,100033763L,100033764L,100033766L,100033767L,100033770L,100033771L);
        for (Long uid : uidList) {
            RoomDTO room = roomManager.getUserRoom(uid);
            RoomVo roomVo = roomManager.convertRoomToVo(room);
            Double flowSum = new Double(0.0);
            roomVo.setCalcSumDataIndex(flowSum.intValue());
            roomVo.setSeqNo(1);
            roomVo.setOnlineNum(1 + roomManager.getNeedAddNum(uid, 1));
            UsersDTO users = usersManager.getUser(uid);
            if (users != null) {
                roomVo.setAvatar(users.getAvatar());
                roomVo.setNick(users.getNick());
                roomVo.setGender(users.getGender());
            }
            roomVoList.add(roomVo);
        }
        return roomVoList;
    }

    @Override
    public List<BannerDTO> getAccompanyBanner(Long uid, String os, String app) {
        return bannerService.findBannerListByTagId(uid, os, 5L);
    }
}
