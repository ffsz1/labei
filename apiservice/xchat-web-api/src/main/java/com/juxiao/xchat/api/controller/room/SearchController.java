package com.juxiao.xchat.api.controller.room;

import com.juxiao.xchat.base.constant.IOSData;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.room.dto.RoomSearchDTO;
import com.juxiao.xchat.manager.common.sysconf.AppVersionManager;
import com.juxiao.xchat.service.api.room.RoomService;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/search")
@Api(tags = "搜索接口")
public class SearchController {
    @Autowired
    private AppVersionManager appVersionService;
    @Autowired
    private RoomService roomService;

    /**
     * @param key 搜索关键字 （搜索官方号、房间title ，用户昵称nick）
     * @return
     */
    @RequestMapping(value = "room", method = RequestMethod.GET)
    public WebServiceMessage searchRoom(@RequestParam("key") String key,
                                        @RequestParam(value = "os", required = false) String os,
                                        @RequestParam(value = "appid", required = false) String appid,
                                        @RequestParam(value = "appVersion", required = false) String appVersion,
                                        HttpServletRequest request,@RequestParam(value = "uid", required = false) Long uid) throws WebServiceException {
        if (StringUtils.isEmpty(key)) {
            return WebServiceMessage.failure(WebServiceCode.PARAMETER_ILLEGAL);
        }
        List<Long> uids = null;
        if (appVersionService.checkAuditingVersion(os, appid, appVersion, HttpServletUtils.getRemoteIpV4(request),uid)) {
            uids = IOSData.AUDIT_ALIST;
        }
        return WebServiceMessage.success(roomService.search(key, uids));
    }

    /**
     * @param key
     * @return
     */
    @ApiOperation(value = "小程序搜索关键字 （搜索官方号、房间title ，用户昵称nick）", notes = "需要ticket、加密验证", tags = {"搜索接口", "小程序接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appid", value = "应用名称", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密串", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = RoomSearchDTO.class)
    })
    @RequestMapping(value = "v2/room", method = RequestMethod.GET)
    public WebServiceMessage searchRoomV2(HttpServletRequest request,
                                          @RequestParam("key") String key,
                                          @RequestParam(value = "os", required = false) String os,
                                          @RequestParam(value = "appid", required = false) String appid,
                                          @RequestParam(value = "uid", required = false) Long uid,
                                          @RequestParam(value = "appVersion", required = false) String appVersion) throws WebServiceException {
        return this.searchRoom(key, os, appid, appVersion, request,uid);
    }

}
