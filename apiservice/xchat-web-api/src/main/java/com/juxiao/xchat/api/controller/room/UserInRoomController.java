package com.juxiao.xchat.api.controller.room;

import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.room.dto.RoomUserinDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.room.RoomService;
import com.juxiao.xchat.service.api.room.UserRoomService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userroom")
@Api(description = "房间接口", tags = "房间接口")
public class UserInRoomController {
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private RoomService roomService;
    @Autowired
    private UserRoomService userRoomService;


    /**
     * 用户进入房间
     *
     * @param uid
     * @param roomUid
     * @return
     */
    @SignVerification
    @RequestMapping(value = "in", method = RequestMethod.POST)
    public WebServiceMessage userIntoRoom(@RequestParam(value = "uid", required = false) Long uid,
                                          @RequestParam(value = "roomUid", required = false) Long roomUid) throws Exception {
        RoomUserinDTO userinDto = userRoomService.userIntoRoom(uid, roomUid);
        if (uid != null && uid.equals(roomUid)) {
            UsersDTO usersDto = usersManager.getUser(uid);
            if (usersDto != null) {
                roomService.sendOpenRoomNoticeToFollowers(usersDto);
            }
        }
        return WebServiceMessage.success(userinDto);
    }

    /**
     * 用户退出房间（如果有在房间）
     *
     * @param uid
     * @return
     */
    @SignVerification
    @RequestMapping(value = "out", method = RequestMethod.POST)
    public WebServiceMessage userOutRoom(@RequestParam(value = "uid", required = false) Long uid) throws WebServiceException {
        userRoomService.userOutRoom(uid);
        return WebServiceMessage.success(null);
    }

    /**
     * 获取指定用户当前所在房间
     *
     * @param uid
     * @return
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public WebServiceMessage getUserInRoomInfo(Long uid) throws WebServiceException {
        return WebServiceMessage.success(userRoomService.getRoomByUid(uid));
    }

}
