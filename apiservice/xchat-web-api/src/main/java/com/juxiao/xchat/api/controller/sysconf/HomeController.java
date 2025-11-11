package com.juxiao.xchat.api.controller.sysconf;

import com.google.common.collect.Lists;
import com.juxiao.xchat.base.annotation.Client;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.user.dto.UserHotDTO;
import com.juxiao.xchat.manager.common.room.vo.RoomVo;
import com.juxiao.xchat.manager.common.sysconf.AppVersionManager;
import com.juxiao.xchat.service.api.sysconf.HomeV2Service;
import com.juxiao.xchat.service.api.sysconf.vo.HomeV2Vo;
import com.juxiao.xchat.service.api.sysconf.vo.IndexParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/home")
@Api(tags = "首页接口", description = "首页接口")
public class HomeController {
    @Resource(name = "homeV2Service")
    private HomeV2Service homeV2Service;

    // @Resource(name = "homeVivoService")
    // private HomeV2Service homeVivoService;

    @Autowired
    private AppVersionManager appVersionService;

    /**
     * 获取首页的数据, 包括: banner、排行榜、房间列表
     *
     * @param indexParam
     * @param request
     * @return
     * @throws WebServiceException
     */
    @RequestMapping(value = "/v2/hotindex", method = {RequestMethod.GET, RequestMethod.POST})
    public WebServiceMessage getHotHomeV2(IndexParam indexParam, HttpServletRequest request) throws WebServiceException {
        String ip = HttpServletUtils.getRemoteIpV4(request);
        HomeV2Vo v2Vo = homeV2Service.index(indexParam.getChannel(), indexParam, ip);
        return WebServiceMessage.success(v2Vo);
    }

    /**
     * 获取首页的数据, 包括: 横幅、排行榜、房间列表
     *
     * @param param
     * @param request
     * @return
     * @throws WebServiceException
     */
    @ApiOperation(value = "首页数据接口", tags = {"获取首页的数据", "小程序接口"})
    @ApiResponse(response = HomeV2Vo.class, code = 200, message = "success")
    @SignVerification(client = Client.WXAPP)
    @RequestMapping(value = "/v3/hotindex", method = {RequestMethod.GET, RequestMethod.POST})
    public WebServiceMessage getHotHomeV3(IndexParam param, HttpServletRequest request) throws WebServiceException {
        return WebServiceMessage.success(homeV2Service.getHotHomeV3(param, HttpServletUtils.getRemoteIpV4(request)));
    }

    /**
     * 查询热门房间列表
     *
     * @param indexParam
     * @return
     */
    @ApiOperation(value = "获取热门房间列表", tags = {"首页接口"})
    @ApiResponse(response = RoomVo.class, code = 200, message = "success")
    @RequestMapping(value = "/hotRoom", method = {RequestMethod.GET, RequestMethod.POST})
    public WebServiceMessage getHotRoom(IndexParam indexParam, HttpServletRequest request) throws WebServiceException {
        return WebServiceMessage.success(homeV2Service.listHotRoom(indexParam, HttpServletUtils.getRemoteIpV4(request)));
    }

    /**
     * 获取标签页内的房间数据
     *
     * @param tagId    标签ID
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "获取标签内的房间数据", tags = {"首页接口"})
    @ApiResponse(response = RoomVo.class, code = 200, message = "success")
    @RequestMapping(value = "/v2/tagindex", method = {RequestMethod.GET, RequestMethod.POST})
    public WebServiceMessage getTagHome(HttpServletRequest request,
                                        @ApiParam(value = "用户ID") @RequestParam(value = "uid", required = false) Long uid,
                                        @ApiParam(value = "标题ID") @RequestParam(value = "tagId", required = false) Long tagId,
                                        @ApiParam(value = "页码") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                        @ApiParam(value = "每页大小") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                        @ApiParam(value = "系统类型") @RequestParam(value = "os", required = false) String os,
                                        @ApiParam(value = "appid") @RequestParam(value = "appid", required = false) String appid,
                                        @ApiParam(value = "APP 版本") @RequestParam(value = "appVersion", required = false) String appVersion,
                                        @ApiParam(value = "渠道") @RequestParam(value = "channel", required = false) String channel) throws WebServiceException {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        List<RoomVo> list = homeV2Service.findRoomByTag(channel, uid, tagId, pageNum, pageSize, os, appid, appVersion, HttpServletUtils.getRemoteIpV4(request));
        return WebServiceMessage.success(list);
    }

    /**
     * 获取标签页内的房间数据
     *
     * @param tagId    标签ID
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "获取首页的数据", tags = {"首页接口", "小程序接口"})
    @ApiResponse(response = RoomVo.class, code = 200, message = "success")
    @RequestMapping(value = "/v3/tagindex", method = {RequestMethod.GET, RequestMethod.POST})
    public WebServiceMessage getTagHomeV3(HttpServletRequest request,
                                          @ApiParam(value = "用户ID") @RequestParam(value = "uid", required = false) Long uid,
                                          @ApiParam(value = "标签ID") @RequestParam(value = "tagId", required = false) Long tagId,
                                          @ApiParam(value = "页码") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                          @ApiParam(value = "每页大小") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                          @ApiParam(value = "系统类型") @RequestParam(value = "os", required = false) String os,
                                          @ApiParam(value = "appid") @RequestParam(value = "appid", required = false) String appid,
                                          @ApiParam(value = "APP 版本") @RequestParam(value = "appVersion", required = false) String appVersion) throws WebServiceException {
        return WebServiceMessage.success(homeV2Service.listRoomByTagV3(uid, tagId, pageNum, pageSize, os, appid, appVersion, HttpServletUtils.getRemoteIpV4(request)));
    }

    /**
     * 首页获取最近一周异性列表
     *
     * @param request
     * @param uid
     * @param os
     * @param appid
     * @param appVersion
     * @return
     * @throws WebServiceException
     */
    @ApiOperation(value = "获取首页的数据", tags = {"首页接口"})
    @RequestMapping(value = "/v2/getOppositeSex", method = {RequestMethod.GET, RequestMethod.POST})
    public WebServiceMessage getOppositeSex(HttpServletRequest request,
                                            @ApiParam(value = "用户ID") @RequestParam(value = "uid", required = false) Long uid,
                                            @ApiParam(value = "系统类型") @RequestParam(value = "os", required = false) String os,
                                            @ApiParam(value = "appid") @RequestParam(value = "appid", required = false) String appid,
                                            @ApiParam(value = "APP 版本") @RequestParam(value = "appVersion", required = false) String appVersion) throws WebServiceException {
        return WebServiceMessage.success(homeV2Service.getOppositeSex(uid, os, appid, appVersion, HttpServletUtils.getRemoteIpV4(request)));
    }

    /**
     * 首页热门推荐
     *
     * @param request
     * @param uid
     * @param pageNum
     * @param pageSize
     * @param os
     * @param appid
     * @param appVersion
     * @param channel
     * @return
     * @throws WebServiceException
     */
    @ApiOperation(value = "首页热门推荐", tags = {"首页接口"})
    @ApiResponse(response = RoomVo.class, code = 200, message = "success")
    @RequestMapping(value = "/v2/getindex", method = {RequestMethod.GET, RequestMethod.POST})
    public WebServiceMessage getList(HttpServletRequest request,
                                     @ApiParam(value = "用户ID") @RequestParam(value = "uid", required = false) Long uid,
                                     @ApiParam(value = "页码") @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                     @ApiParam(value = "每页大小") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                     @ApiParam(value = "系统类型") @RequestParam(value = "os", required = false) String os,
                                     @ApiParam(value = "appid") @RequestParam(value = "appid", required = false) String appid,
                                     @ApiParam(value = "APP 版本") @RequestParam(value = "appVersion", required = false) String appVersion,
                                     @ApiParam(value = "渠道") @RequestParam(value = "channel", required = false) String channel) throws WebServiceException {
        // IOS新版本在审核期内的首页数据要做特殊处理
        String ip = HttpServletUtils.getRemoteIpV4(request);
        if ("ios".equalsIgnoreCase(os) && appVersionService.checkAuditingVersion(os, appid, appVersion, ip, uid)) {
            return WebServiceMessage.success(Lists.newArrayList());
        }
        if (pageNum == null || pageNum < 1) pageNum = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;
        return WebServiceMessage.success(homeV2Service.getHomeHotRoom(channel, uid, appVersion, pageNum, pageSize, appid));
    }

    /**
     * 获取首页顶部横幅
     *
     * @param uid
     * @param os
     * @param appid
     * @return
     * @throws WebServiceException
     */
    @ApiOperation(value = "首页顶部banner", notes = "首页顶部banner")
    @RequestMapping("/getIndexTopBanner")
    public WebServiceMessage getIndexTopBanner(@RequestParam(value = "uid", required = false) Long uid,
                                               @RequestParam(value = "os", required = false) String os,
                                               @RequestParam(value = "appid", required = false) String appid) throws WebServiceException {
        return WebServiceMessage.success(homeV2Service.getIndexTopBanner(uid, os, appid));
    }

    /**
     * 获取发现页顶部Item
     *
     * @param uid
     * @param os
     * @param appid
     * @return
     * @throws WebServiceException
     */
    @ApiOperation(value = "发现页顶部Item", notes = "发现页顶部Item")
    @RequestMapping("/getDiscoverItem")
    public WebServiceMessage getDiscoverItem(@RequestParam(value = "uid", required = false) Long uid,
                                               @RequestParam(value = "os", required = false) String os,
                                               @RequestParam(value = "appid", required = false) String appid) throws WebServiceException {
        return WebServiceMessage.success(homeV2Service.getDiscoverItem(uid, os, appid));
    }

    /**
     * @param param
     * @param request
     * @return
     * @throws WebServiceException
     */
    @ApiOperation(value = "首页顶部icon", notes = "首页顶部icon")
    @RequestMapping("/getIndexHomeIcon")
    public WebServiceMessage getIndexHomeIcon(IndexParam param, HttpServletRequest request) throws WebServiceException {
        String ip = HttpServletUtils.getRemoteIpV4(request);
        return WebServiceMessage.success(homeV2Service.getIndexHomeIcon(param, ip));
    }

    /**
     * 获取优质陪陪列表
     *
     * @param param
     * @param request
     * @return
     * @throws WebServiceException
     */
    @ApiOperation(value = "获取优质陪陪列表", notes = "获取优质陪陪列表")
    @ApiResponse(response = UserHotDTO.class, code = 200, message = "success")
    @RequestMapping(value = "/bestCompanies", method = {RequestMethod.GET, RequestMethod.POST})
    public WebServiceMessage getBestCompanies(IndexParam param, HttpServletRequest request) throws WebServiceException {
        return WebServiceMessage.success(homeV2Service.getBestCompanies(param, HttpServletUtils.getRemoteIpV4(request)));
    }

    /**
     * 获取萌新列表
     *
     * @param param
     * @param request
     * @return
     * @throws WebServiceException
     */
    @ApiOperation(value = "获取萌新列表", notes = "获取萌新列表")
    @ApiResponse(response = UserHotDTO.class, code = 200, message = "success")
    @RequestMapping(value = "/newUsers", method = {RequestMethod.GET, RequestMethod.POST})
    public WebServiceMessage getNewUsers(IndexParam param, HttpServletRequest request) throws WebServiceException {
        return WebServiceMessage.success(homeV2Service.getNewUsers(param, HttpServletUtils.getRemoteIpV4(request)));
    }
    
    /**
     * 首页获取顶部Tab、banner图
     *
     * @param uid
     * @param os
     * @param appid
     * @return
     * @throws WebServiceException
     */
    @ApiOperation(value = "首页获取顶部Tab、banner图", notes = "首页获取顶部Tab、banner图")
    @RequestMapping("/getIndexTopTabBanner")
    public WebServiceMessage getIndexTopTabBanner(@RequestParam(value = "uid", required = false) Long uid,
                                               @RequestParam(value = "os", required = false) String os,
                                               @RequestParam(value = "appid", required = false) String appid) throws WebServiceException {
        return WebServiceMessage.success(homeV2Service.getIndexTopTabBanner(uid, os, appid));
    }
}
