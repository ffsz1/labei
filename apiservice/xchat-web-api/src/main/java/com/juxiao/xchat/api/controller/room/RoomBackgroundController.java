package com.juxiao.xchat.api.controller.room;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.room.domain.RoomBgDO;
import com.juxiao.xchat.service.api.room.RoomBgService;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/room/bg")
@Api(description = "房间接口", tags = "房间接口")
public class RoomBackgroundController {
    @Autowired
    private RoomBgService roomBgService;

    /**
     * 查询所有用户的房间背景图
     * @return
     */
    @GetMapping(value = "list")
    @Authorization
    public WebServiceMessage listByUid(Long roomId, HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        String[] roomArr = map.get("roomId");
        String room;
        if (roomArr != null) {
            for (int i = 0; i < roomArr.length; i ++) {
                room = roomArr[i];
                if (StringUtils.isNotBlank(room) && StringUtils.isNumeric(room)) {
                    roomId = Long.valueOf(room);
                    break;
                }
            }
        }
        List<RoomBgDO> list = roomBgService.listByRoomId(roomId);
        Collections.sort(list);
        return WebServiceMessage.success(list);
    }
}
