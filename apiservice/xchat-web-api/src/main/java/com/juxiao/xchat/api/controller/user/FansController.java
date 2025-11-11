package com.juxiao.xchat.api.controller.user;

import com.google.common.collect.Lists;
import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.Client;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.user.dto.FansFollowDTO;
import com.juxiao.xchat.manager.common.sysconf.AppVersionManager;
import com.juxiao.xchat.service.api.sysconf.NationalDayService;
import com.juxiao.xchat.service.api.user.FansService;
import com.juxiao.xchat.service.api.user.vo.WxappShareInfoVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/fans")
@Api(tags = "用户信息接口", description = "用户信息接口")
public class FansController {
    @Autowired
    private FansService fansService;
    @Autowired
    private AppVersionManager appVersionService;
//    @Autowired
//    private NationalDayService nationalDayService;

    /**
     * 喜欢某人，或者取消喜欢某人
     *
     * @param uid
     * @param likedUid
     * @param type     1喜欢某人，2取消喜欢某人
     * @return
     */
    @Authorization
    @SignVerification
    @RequestMapping(value = "like", method = RequestMethod.POST)
    public WebServiceMessage likeSomeBody(@RequestParam(name = "uid", required = false) Long uid,
                                          @RequestParam(name = "likedUid", required = false) Long likedUid,
                                          @RequestParam(name = "type", required = false) Byte type) throws Exception {
        if (type == null) {
            return WebServiceMessage.failure(WebServiceCode.PARAMETER_ILLEGAL);
        }
        if (type == 1) {
            fansService.likeSomeBody(uid, likedUid);
            //国庆活动 关注操作--代码已废弃
            //nationalDayService.NationalDayFansLike(uid, likedUid);
        } else if (type == 2) {
            fansService.cancelLikeSomeBody(uid, likedUid);
            //国庆活动 取消关注记录操作 --代码已废弃
            //nationalDayService.NationalDayCancelFansLike(uid, likedUid);
        } else {
            return WebServiceMessage.failure(WebServiceCode.PARAMETER_ILLEGAL);
        }
        return WebServiceMessage.success(null);
    }

    @ApiOperation(value = "喜欢某人，或者取消喜欢某人", notes = "需要ticket、需要加密", tags = {"用户信息接口", "小程序接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户uid", dataType = "long", required = true),
            @ApiImplicitParam(name = "likedUid", value = "被喜欢或者取消喜欢的用户UID", dataType = "long", required = true),
            @ApiImplicitParam(name = "type", value = "1喜欢某人，2取消喜欢某人", dataType = "int", required = true),
            @ApiImplicitParam(name = "ticket", value = "用户登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "app版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "t", value = "当前时间戳", dataType = "long", required = true),
            @ApiImplicitParam(name = "sn", value = "签名", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = WxappShareInfoVO.class),
            @ApiResponse(code = 401, message = "需要登录"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification(client = Client.WXAPP)
    @RequestMapping(value = "v1/like", method = RequestMethod.POST)
    public WebServiceMessage likeUser(@RequestParam(name = "uid", required = false) Long uid,
                                      @RequestParam(name = "likedUid", required = false) Long likedUid,
                                      @RequestParam(name = "type", required = false) Byte type) throws Exception {
        return this.likeSomeBody(uid, likedUid, type);
    }

    /**
     * 查询uid是否喜欢checkUid
     *
     * @param uid
     * @return
     */
    @SignVerification
    @RequestMapping(value = "islike", method = RequestMethod.GET)
    public WebServiceMessage checkUserLike(@RequestParam("uid") Long uid, @RequestParam("isLikeUid") Long isLikeUid) throws WebServiceException {
        boolean isLike = fansService.checkUserLike(uid, isLikeUid);
        return WebServiceMessage.success(isLike);
    }

    @ApiOperation(value = "查询uid是否喜欢isLikeUid", notes = "需要ticket、需要加密", tags = {"用户信息接口", "小程序接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户uid", dataType = "long", required = true),
            @ApiImplicitParam(name = "isLikeUid", value = "被喜欢或者取消喜欢的用户UID", dataType = "long", required = true),
            @ApiImplicitParam(name = "ticket", value = "用户登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "app版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "t", value = "当前时间戳", dataType = "long", required = true),
            @ApiImplicitParam(name = "sn", value = "签名", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = WxappShareInfoVO.class),
            @ApiResponse(code = 401, message = "需要登录"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification(client = Client.WXAPP)
    @RequestMapping(value = "v2/islike", method = RequestMethod.GET)
    public WebServiceMessage checkUserLike4Wxapp(@RequestParam("uid") Long uid, @RequestParam("isLikeUid") Long isLikeUid) throws WebServiceException {
        boolean isLike = fansService.checkUserLike(uid, isLikeUid);
        return WebServiceMessage.success(isLike);
    }

    /**
     * 获取关注列表
     *
     * @param uid
     * @return
     */
    @SignVerification
    @RequestMapping(value = "following", method = RequestMethod.GET)
    public WebServiceMessage listFollowing(@RequestParam(value = "uid", required = false) Long uid,
                                           @RequestParam(value = "pageNo") Integer pageNo,
                                           @RequestParam(value = "pageSize") Integer pageSize,
                                           @RequestParam(value = "os", required = false) String os,
                                           @RequestParam(value = "appVersion", required = false) String appVersion,
                                           HttpServletRequest request) throws WebServiceException {
        /*if (appVersionService.checkAuditingVersion(os, appVersion, HttpServletUtils.getRemoteIp(request))) {
            return WebServiceMessage.success(Lists.newArrayList());
        }*/
        List<FansFollowDTO> list = fansService.listFollowing(uid, pageNo, pageSize);
        return WebServiceMessage.success(list);
    }

    @ApiOperation(value = "小程序获取我关注用户列表接口", notes = "根据uid获取房间信息", tags = {"用户信息接口", "小程序接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "pageNum", value = "查看页数", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页页数", dataType = "int"),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "int", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "app版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "t", value = "当前时间戳", dataType = "long", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密串", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = FansFollowDTO.class)
    })
    @Authorization
    @SignVerification(client = Client.WXAPP)
    @RequestMapping(value = "v2/following", method = RequestMethod.GET)
    public WebServiceMessage listFollowingV2(@RequestParam(value = "uid", required = false) Long uid,
                                             @RequestParam(value = "pageNum") Integer pageNum,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "50") Integer pageSize,
                                             @RequestParam(value = "os", required = false) String os,
                                             @RequestParam(value = "appVersion", required = false) String appVersion,
                                             HttpServletRequest request) throws WebServiceException {
        return this.listFollowing(uid, pageNum, pageSize, os, appVersion, request);
    }

    /**
     * @param uid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SignVerification
    @RequestMapping(value = "fanslist", method = RequestMethod.GET)
    public WebServiceMessage listFans(HttpServletRequest request,
                                      @RequestParam(value = "uid") Long uid,
                                      @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                      @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "50") Integer pageSize,
                                      @RequestParam(value = "os", required = false) String os,
                                      @RequestParam(value = "appid", required = false) String appid,
                                      @RequestParam(value = "appVersion", required = false) String appVersion) throws WebServiceException {
        if (appVersionService.checkAuditingVersion(os, appid, appVersion, HttpServletUtils.getRemoteIpV4(request),uid)) {
            return WebServiceMessage.success(Lists.newArrayList());
        }
        // 兼容pageNo和pageNum两种情况
        int page;
        if (pageNo != null) {
            page = pageNo;
        } else if (pageNum != null) {
            page = pageNum;
        } else {
            page = 1;
        }
        return WebServiceMessage.success(fansService.listFans(uid, page, pageSize));
    }

    @ApiOperation(value = "小程序粉丝列表接口", notes = "根据uid获取房间信息", tags = {"用户信息接口", "小程序接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "pageNum", value = "查看页数", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页页数", dataType = "int"),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "int", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "app版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "t", value = "当前时间戳", dataType = "long", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密串", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = FansFollowDTO.class)
    })
    @SignVerification
    @RequestMapping(value = "v2/fans", method = RequestMethod.GET)
    public WebServiceMessage listFansV2(HttpServletRequest request,
                                        @RequestParam(value = "uid") Long uid,
                                        @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "50") Integer pageSize,
                                        @RequestParam(value = "os", required = false) String os,
                                        @RequestParam(value = "appid", required = false) String appid,
                                        @RequestParam(value = "appVersion", required = false) String appVersion) throws WebServiceException {
        return this.listFans(request, uid, pageNum, pageNum, pageSize, os, appid, appVersion);
    }
}
