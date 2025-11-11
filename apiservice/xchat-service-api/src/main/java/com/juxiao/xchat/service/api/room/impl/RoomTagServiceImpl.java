package com.juxiao.xchat.service.api.room.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.juxiao.xchat.dao.room.dto.RoomTagDTO;
import com.juxiao.xchat.dao.sysconf.dto.GeneralReviewWhitelist;
import com.juxiao.xchat.dao.sysconf.dto.ReviewConfigDTO;
import com.juxiao.xchat.manager.common.room.RoomTagManager;
import com.juxiao.xchat.service.api.room.RoomTagService;
import com.juxiao.xchat.service.api.sysconf.ReviewConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoomTagServiceImpl implements RoomTagService {


    @Autowired
    private RoomTagManager roomTagManager;

    @Autowired
    private ReviewConfigService reviewConfigService;

    @Autowired
    private Gson gson;

    /**
     * 搜索分类标签
     * @param os 系统
     * @param appVersion app版本
     * @param uid uid
     * @param channel 渠道
     * @return
     */
    @Override
    public List<RoomTagDTO> getSearchTags(String os,String appVersion,Long uid,String channel) {
        List<RoomTagDTO> roomTagDTOList = roomTagManager.getSearchTags();
        if(uid != null){
            //白名单
            List<GeneralReviewWhitelist> generalReviewWhitelists = reviewConfigService.getGeneralReviewWhitelistByList();
            if(generalReviewWhitelists.size() > 0) {
                //获取到需要过滤的用户uid
                boolean flag = generalReviewWhitelists.stream().anyMatch(item -> item.getUid().equals(uid));
                if (flag) {
                    return roomTagDTOList;
                }
            }
            List<ReviewConfigDTO> reviewConfigDTOS = reviewConfigService.selectByCacheList();
            if (reviewConfigDTOS.size() > 0) {
                Set<String> removeTag = reviewConfigService.getRemoveTags(channel,uid,os,appVersion);
                if(removeTag != null && removeTag.size() > 0){
                    roomTagDTOList = roomTagDTOList.stream().filter(item -> !removeTag.contains(item.getName()))
                            .collect(Collectors.toList());
                    return roomTagDTOList;
                }
            }
        }
        return roomTagDTOList;
    }
}
