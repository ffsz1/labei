package com.juxiao.xchat.api.controller.mcoin;

import com.alibaba.fastjson.JSONObject;
import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.mcoin.dto.UserMcoinPurseDTO;
import com.juxiao.xchat.manager.common.mcoin.McoinManager;
import com.juxiao.xchat.manager.common.mcoin.McoinMissionManager;
import com.juxiao.xchat.service.api.mcoin.McoinMissionService;
import com.juxiao.xchat.service.api.mcoin.vo.McoinMissionVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/mcoin")
@Api(tags = "点点币接口")
public class McoinController {
    @Autowired
    private McoinManager mcoinManager;
    @Autowired
    private McoinMissionManager missionManager;
    @Autowired
    private McoinMissionService mcoinMissionService;

    @ApiOperation(value = "点点币任务列表接口", notes = "需要登录ticket和加密")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = McoinMissionVO.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "v1/getInfo", method = RequestMethod.GET)
    public WebServiceMessage getInfo(HttpServletRequest request,
                                     @RequestParam("uid") @ApiParam(value = "用户ID") Long uid,
                                     @RequestParam("ticket") @ApiParam(value = "登录凭证") String ticket,
                                     @RequestParam("os") @ApiParam(value = "操作系统") String os,
                                     @RequestParam(value = "appid", required = false, defaultValue = "1001") @ApiParam(value = "操作系统", required = false) String appid,
                                     @RequestParam("appVersion") @ApiParam(value = "版本") String appVersion,
                                     @RequestParam(value = "sn", required = false) @ApiParam(value = "签名") String sn) throws WebServiceException {
        String ip = HttpServletUtils.getRemoteIpV4(request);
        McoinMissionVO missionVo = mcoinMissionService.getInfo(uid, os, appid, appVersion, ip);
        return WebServiceMessage.success(missionVo);
    }

    @ApiOperation(value = "领取点点币接口", notes = "需要登录ticket和加密")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "v1/gainMcoin", method = RequestMethod.POST)
    public WebServiceMessage gainMcoin(@RequestParam("missionId") @ApiParam(value = "萌币领取任务ID") Integer missionId,
                                       @RequestParam("uid") @ApiParam(value = "用户ID") Long uid,
                                       @RequestParam("ticket") @ApiParam(value = "登录凭证") String ticket,
                                       @RequestParam("os") @ApiParam(value = "操作系统") String os,
                                       @RequestParam("appVersion") @ApiParam(value = "版本") String appVersion,
                                       @RequestParam(value = "sn", required = false) @ApiParam(value = "签名") String sn) throws WebServiceException {
        mcoinMissionService.gainMcoin(uid, missionId);
        return WebServiceMessage.success(null);
    }

    @ApiOperation(value = "1.8 查询点点币余额接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = UserMcoinPurseDTO.class),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @RequestMapping(value = "v1/getMcoinNum", method = RequestMethod.POST)
    public WebServiceMessage getMcoinNum(@RequestParam("uid") @ApiParam(value = "用户ID") Long uid,
                                         @RequestParam("ticket") @ApiParam(value = "登录凭证") String ticket) throws WebServiceException {
        UserMcoinPurseDTO purseDto = mcoinManager.getUserMcoinPurse(uid);
        purseDto.setCreateTime(null);
        purseDto.setPurseStatus(null);
        purseDto.setUpdateTime(null);
        return WebServiceMessage.success(purseDto);
    }

    @ApiOperation(value = "3.1.10.点点币小红点接口")
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
            @ApiResponse(code = 403, message = "签名验证失败")
    })
    @Authorization
    @RequestMapping(value = "v1/getMissionCount", method = RequestMethod.POST)
    public WebServiceMessage getMissionCount(@RequestParam("uid") @ApiParam(value = "用户ID") Long uid,
                                             @RequestParam("ticket") @ApiParam(value = "登录凭证") String ticket) throws WebServiceException {
        int missionCount = missionManager.countUserMission(uid);
        JSONObject object = new JSONObject();
        object.put("missionCount", missionCount);
        return WebServiceMessage.success(object);
    }

}
