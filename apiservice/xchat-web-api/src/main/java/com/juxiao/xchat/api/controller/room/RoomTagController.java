package com.juxiao.xchat.api.controller.room;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.common.room.RoomTagManager;
import com.juxiao.xchat.service.api.room.RoomTagService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 房间标签
 */
@RestController
@RequestMapping("/room/tag")
@Api(description = "房间接口", tags = "房间接口")
public class RoomTagController {
    @Autowired
    private RoomTagManager roomTagManager;

    @Autowired
    private RoomTagService roomTagService;

    /**
     * 顶部标签
     */
    @RequestMapping(value = "/top")
    public WebServiceMessage getTopTags(@RequestParam("os") String os, @RequestParam("appVersion") String appVersion) throws WebServiceException {
        return WebServiceMessage.success(roomTagManager.getTopTagList(os, appVersion));
    }

    /**
     * 分类标签
     */
    @RequestMapping(value = "/classification")
    public WebServiceMessage getSearchTags() {
        return WebServiceMessage.success(roomTagManager.getSearchTags());
    }

    /**
     * 所有有效标签，房间设置时需要获取所有标签
     */
    @RequestMapping(value = "/all")
    public WebServiceMessage getAllTags(@RequestParam(value = "uid",required = false) Long uid) {
        return WebServiceMessage.success(roomTagManager.getAllTag(uid));
    }


    /**
     * 顶部分类标签
     * @param os 系统
     * @param appVersion  app 版本
     * @param uid UID
     * @param channel 渠道
     * @return
     */
    @RequestMapping(value = "/v2/classification")
    public WebServiceMessage getV2SearchTags(@RequestParam(value = "os",required = false) String os, @RequestParam(value = "appVersion",required = false) String appVersion, @RequestParam(value = "uid", required = false) Long uid,@RequestParam(value = "channel",required = false) String channel) {
        return WebServiceMessage.success(roomTagService.getSearchTags(os,appVersion,uid,channel));
    }
}
