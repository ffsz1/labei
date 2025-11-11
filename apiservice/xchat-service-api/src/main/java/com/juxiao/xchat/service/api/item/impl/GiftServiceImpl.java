package com.juxiao.xchat.service.api.item.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.juxiao.xchat.dao.item.dto.GiftDTO;
import com.juxiao.xchat.dao.sysconf.dto.SysConfDTO;
import com.juxiao.xchat.dao.sysconf.enumeration.SysConfigId;
import com.juxiao.xchat.dao.user.dto.UserGiftPurseDTO;
import com.juxiao.xchat.manager.common.item.GiftManager;
import com.juxiao.xchat.manager.common.sysconf.SysConfManager;
import com.juxiao.xchat.manager.common.user.UserGiftPurseManager;
import com.juxiao.xchat.service.api.item.GiftService;
import com.juxiao.xchat.service.api.item.vo.GiftVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GiftServiceImpl implements GiftService {
    @Autowired
    private GiftManager giftManager;
    @Autowired
    private SysConfManager sysconfManager;
    @Autowired
    private UserGiftPurseManager giftPurseManager;

    @Override
    public Map<String, Object> listGifts(Long uid) {

//
//        List<GiftDTO> gifts = giftManager.listGifts(RedisKey.gift, GiftType.NORMAL);
//        // 根据礼物状态, 过滤礼物
//        gifts = gifts.stream().filter((GiftDTO gift) -> gift.getGiftStatus() == 1).collect(Collectors.toList());
//        List<GiftVO> list = toVoList(uid, gifts);
//        Map<String, Object> giftData = Maps.newHashMap();
//        Collections.sort(list);
//        //获取礼物版本
//        SysConfDTO sysconfDto = sysconfManager.getSysConf(SysConfigId.cur_gift_version);
//        giftData.put("giftVersion", sysconfDto.getConfigValue());
//        giftData.put("gift", list);
//        return giftData;
        return null;
    }

    @Override
    public Map<String, Object> listGifts(Long uid, Integer type) {
        List<GiftDTO> gifts = giftManager.listGift(null);

        // 获取用户的拥有的礼物对象  并转VO对象
        List<GiftVO> list = toVoList(uid, gifts);
        List<GiftVO> newsList = Lists.newArrayList();
        list.forEach(item ->{
            if(!newsList.contains(item)){
                newsList.add(item);
            }
        });
        Map<String, Object> giftData = Maps.newHashMap();
        Collections.sort(newsList);
        // 获取礼物版本
        SysConfDTO sysconfDto = sysconfManager.getSysConf(SysConfigId.cur_gift_version);
        if (sysconfDto != null) {
            giftData.put("giftVersion", sysconfDto.getConfigValue());
        }
        giftData.put("gift", newsList);

        return giftData;
    }

    /**
     * 转换成vo
     *
     * @param uid
     * @param gifts
     * @return
     */
    public List<GiftVO> toVoList(Long uid, List<GiftDTO> gifts) {
        if (gifts == null || gifts.isEmpty()) {
            return Lists.newArrayList();
        }
        List<GiftVO> list = Lists.newArrayList();
        if (uid == null) {
            gifts.forEach(gift -> list.add(toVo(gift)));
        } else {
            gifts.forEach(gift -> {
                GiftVO giftVo = toVo(gift);
                UserGiftPurseDTO giftPurseDto = giftPurseManager.getUserGiftPurse(uid, gift.getGiftId());
                if (giftPurseDto == null || giftPurseDto.getCountNum() == null) {
                    giftVo.setUserGiftPurseNum(0);
                } else {
                    giftVo.setUserGiftPurseNum(giftPurseDto.getCountNum());
                }
                list.add(giftVo);
            });
        }
        return list;
    }

    private GiftVO toVo(GiftDTO giftDTO) {
        GiftVO giftVo = new GiftVO();
        BeanUtils.copyProperties(giftDTO, giftVo);
        giftVo.setGiftUrl(giftDTO.getPicUrl());
        giftVo.setHasLatest(giftDTO.getIsLatest());
        giftVo.setHasTimeLimit(giftDTO.getIsTimeLimit());
        return giftVo;
    }
}
