package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.user.UserPurseHotRoomService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/purseHotRoom")
public class UserHotRoomController {
    @Autowired
    private UserPurseHotRoomService hotRoomService;

    /**
     * 推荐位-购买记录
     *
     * @param uid
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @Authorization
    public WebServiceMessage list(@RequestParam("uid") Long uid) throws WebServiceException {
        return WebServiceMessage.success(hotRoomService.listHotRoomRecord(uid));
    }

    /**
     * 购买推荐位
     *
     * @param uid
     * @param erbanNo
     * @param date
     * @param hour
     * @return
     */
    @ApiOperation(value = "购买推荐位", tags = "user")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "erbanNo", value = "购买推荐的房间主官方号", dataType = "int", required = true),
            @ApiImplicitParam(name = "date", value = "购买日期", dataType = "long"),
            @ApiImplicitParam(name = "hour", value = "购买小时数", dataType = "long")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @RequestMapping(value = "purse", method = RequestMethod.POST)
    @Authorization
    public WebServiceMessage purse(@RequestParam("uid") Long uid,
                                   @RequestParam("erbanNo") Long erbanNo,
                                   @RequestParam("date") String date,
                                   @RequestParam("hour") String hour) throws WebServiceException {
        hotRoomService.purse(uid, erbanNo, date, hour);
        return WebServiceMessage.success(null);
    }

}
