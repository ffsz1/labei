package com.juxiao.xchat.api.controller.room;

import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.room.PublicRoomService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chris
 * @Title:
 * @date 2019-05-22
 * @time 17:33
 */
@Slf4j
@RestController
@RequestMapping("/publicRoom")
@Api(description = "房间接口", tags = "房间接口")
public class PublicRoomController {

    @Autowired
    private PublicRoomService publicRoomService;


    @RequestMapping(value = "/v1/pushMsg", method = {RequestMethod.GET, RequestMethod.POST})
    public WebServiceMessage receiveMsg(@RequestBody String body) {
        log.info("[receiveMsg] 大厅发言内容:{}",body);
        publicRoomService.receiveMsg(body);
        return WebServiceMessage.success(null);
    }
}
