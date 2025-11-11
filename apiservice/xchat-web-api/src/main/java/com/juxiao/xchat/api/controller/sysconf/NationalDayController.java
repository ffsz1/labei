package com.juxiao.xchat.api.controller.sysconf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.common.conf.NationalDayConf;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.sysconf.NationalDayService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
@author:tp
@date:2020年9月22日
*/
@RestController
@RequestMapping("/nationalDayActivity")
@Api(tags = "国庆活动接口", description = "国庆活动接口")
public class NationalDayController {

	@Autowired
	private NationalDayService nationalDayService;
	@Autowired
	private UsersManager usersManager;
	
	
	/**
          * 国庆活动页列表
     *
     * @param uid
     * @param os
     * @param appid
     * @return
     * @throws WebServiceException
     */
    @ApiOperation(value = "国庆活动页列表", notes = "国庆活动页列表")
    @RequestMapping("/getList")
    public WebServiceMessage getList(@RequestParam(value = "uid", required = false) Long uid,
                                               @RequestParam(value = "os", required = false) String os,
                                               @RequestParam(value = "appid", required = false) String appid) throws WebServiceException {
    	if (uid == null) {
			throw new WebServiceException(WebServiceCode.PARAM_ERROR);
		}
		UsersDTO users = usersManager.getUser(uid);
		if (users == null) {
			throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
		}
    	return WebServiceMessage.success(nationalDayService.loadNationalDayActivityData(uid, os, appid));
    }
    
    @ApiOperation(value = "积分兑换", notes = "积分兑换")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "optionId", value = "选项ID", dataType = "int", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "/exchange", method = RequestMethod.POST)
    public WebServiceMessage exchange(@RequestParam("uid") Long uid,
                                   @RequestParam("optionId") Integer optionId) throws WebServiceException {
        
        return nationalDayService.exchange(uid, optionId);
    }
    
    @ApiOperation(value = "分享记录", notes = "分享记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "shareType", value = "分享类型(1.微信 ,2.朋友圈,3.qq,4.qq空间)", dataType = "string", required = false),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    /**
     * 分享记录
     *
     * @param uid uid
     * @param shareType   1,微信;2,朋友圈;3,qq;4,qq空间
     * @return WebServiceMessage
     */
    @SignVerification
    @RequestMapping(value = "/share", method = RequestMethod.POST)
    public WebServiceMessage saveUserShareRecord(@RequestParam("uid") Long uid,
                                                 @RequestParam("shareType") String shareType) throws WebServiceException {
    	
    	//国庆活动页分享
    	nationalDayService.NationalDayPageShare(uid,shareType);
        return WebServiceMessage.success("分享成功");
    }
	
    
}
