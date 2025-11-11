package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.manager.common.sysconf.AppVersionManager;
import com.juxiao.xchat.service.api.user.AccompanyManualService;
import com.juxiao.xchat.service.common.sysconf.AppVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chris
 * @Title:
 * @date 2018/11/14
 * @time 14:19
 */
@RestController
@RequestMapping("/accopany")
@Api(tags = "陪玩推荐接口",description = "陪玩推荐相关")
public class AccompanyManualController {

    @Autowired
    private AccompanyManualService accompanyManualService;

    @Autowired
    private AppVersionManager appVersionService;

    @ApiOperation(value = "获取陪玩推荐逻辑金额",notes = "获取陪玩推荐逻辑金额")
    @GetMapping("getAccompanyMoney")
    public WebServiceMessage getAccompanyMoney(){
        return WebServiceMessage.success(accompanyManualService.getAccompanyMoney());
    }



    @ApiOperation(value = "获取陪玩推荐列表数据",notes = "获取陪玩推荐列表数据")
    @Authorization
    @GetMapping("getList")
    public WebServiceMessage getList(@RequestParam(value = "uid", required = false) Long uid,
                                     @RequestParam(value = "os", required = false)String os,
                                     @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                     @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                     @RequestParam(value = "appVersion", required = false)String appVersion,
                                     @RequestParam(value = "appid", required = false)String appid,
                                     @RequestParam(value = "type" ,defaultValue = "人气排行")String type, HttpServletRequest request)throws WebServiceException {
        //审核模式
        if (appVersionService.checkAuditingVersion(os, appVersion,appid, HttpServletUtils.getRemoteIpV4(request),uid)) {
            return WebServiceMessage.success(accompanyManualService.getCheckAudition());
        }else{
            return WebServiceMessage.success(accompanyManualService.getList(uid,pageNum,pageSize,os,appVersion,appid,type));
        }
    }


    @ApiOperation(value = "获取陪玩推荐分类",notes = "获取陪玩推荐分类数据")
    @Authorization
    @GetMapping("getTags")
    public WebServiceMessage getTags(@RequestParam(value = "uid", required = false) Long uid,
                                     @RequestParam(value = "os", required = false)String os,
                                     @RequestParam(value = "appVersion", required = false)String appVersion,
                                     @RequestParam(value = "app", required = false)String app,
                                     @RequestParam(value = "channel", required = false)String channel){
        return WebServiceMessage.success(accompanyManualService.getAccompanyTypeByList(uid,os,appVersion,app,channel));
    }


    @ApiOperation(value = "获取陪玩推荐banner",notes = "获取陪玩推荐banner")
    @RequestMapping("/accompanyBanner")
    public WebServiceMessage getAccompanyBanner(@RequestParam(value = "uid", required = false) Long uid,
                                                @RequestParam(value = "os", required = false)String os,
                                                @RequestParam(value = "app", required = false)String app){
        return WebServiceMessage.success(accompanyManualService.getAccompanyBanner(uid, os,app));
    }
}
