package com.juxiao.xchat.api.controller.family;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.service.api.family.FamilyApplyJoinRecordService;
import com.juxiao.xchat.service.api.family.FamilyExitRecordService;
import com.juxiao.xchat.service.api.family.FamilyJoinService;
import com.juxiao.xchat.service.api.family.FamilyTeamService;
import com.juxiao.xchat.service.api.family.bo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author laizhilong
 * @Title: 家族控制层
 * @Package com.juxiao.xchat.api.controller.family
 * @date 2018/8/30
 * @time 18:49
 */
@Slf4j
@RestController
@RequestMapping("/family")
@Api(value="家族controller",tags={"家族操作相关接口"})
public class FamilyController {

    private FamilyTeamService familyTeamService;

    private FamilyApplyJoinRecordService familyApplyRecordService;

    private FamilyExitRecordService familyExitRecordService;

    private FamilyJoinService familyJoinService;

    @Autowired
    public FamilyController(FamilyTeamService familyTeamService, FamilyApplyJoinRecordService familyApplyRecordService, FamilyExitRecordService familyExitRecordService, FamilyJoinService familyJoinService) {
        this.familyTeamService = familyTeamService;
        this.familyApplyRecordService = familyApplyRecordService;
        this.familyExitRecordService = familyExitRecordService;
        this.familyJoinService = familyJoinService;
    }


    @Authorization
    @GetMapping("getList")
    @ApiOperation("获取家族列表")
    public WebServiceMessage getList(TeamParamsBO teamParamsBO, HttpServletRequest request, String appid) throws WebServiceException {
        return this.familyTeamService.getList(teamParamsBO, HttpServletUtils.getRemoteIpV4(request), appid);
    }

    @Authorization
    @GetMapping("getFamilyTeamJoin")
    @ApiOperation("获取加入家族成员信息")
    public WebServiceMessage getFamilyTeamJoin(TeamParamsBO teamParamsBO){
        return this.familyTeamService.getFamilyTeamJoin(teamParamsBO);
    }


    @Authorization
    @GetMapping("getJoinFamilyInfo")
    @ApiOperation("获取根据uid获取家族信息")
    @ApiImplicitParam(name="userId",value="用户userId",dataType="long", paramType = "query")
    public WebServiceMessage getJoinFamilyInfo(@RequestParam(value = "userId" , required = true)Long userId){
        return this.familyJoinService.getJoinFamilyInfo(userId);
    }




    @Authorization
    @GetMapping("checkFamilyJoin")
    @ApiOperation("获取根据uid检测是否有加入家族")
    @ApiImplicitParam(name="userId",value="用户userId",dataType="long", paramType = "query")
    public WebServiceMessage checkFamilyJoin(@RequestParam(value = "userId" , required = true)Long userId){
        return this.familyJoinService.checkFamilyJoin(userId);
    }



    /**
     * 创建家族
     * @param familyTeamParamBO
     * @return
     */
    @Authorization
    @SignVerification
    @PostMapping("createFamilyTeam")
    @ApiOperation("创建家族")
    public WebServiceMessage createFamilyTeam(FamilyTeamParamBO familyTeamParamBO){
        return this.familyTeamService.save(familyTeamParamBO);
    }

    @Authorization
    @SignVerification
    @PostMapping("applyJoinFamilyTeam")
    @ApiOperation("申请加入家族")
    public WebServiceMessage applyJoinFamilyTeam(FamilyApplyJoinParamBO familyApplyJoinParamBO){
        return this.familyApplyRecordService.save(familyApplyJoinParamBO);
    }



    @Authorization
    @SignVerification
    @PostMapping("editFamilyTeam")
    @ApiOperation("编辑家族信息")
    public WebServiceMessage editFamilyTeam(FamilyTeamEditParamBO familyTeamEditParamBO){
        return this.familyTeamService.update(familyTeamEditParamBO);
    }

    @Authorization
    @SignVerification
    @PostMapping("applyExitTeam")
    @ApiOperation("申请退出家族")
    public WebServiceMessage applyExitTeam(ApplyTeamParamBO applyTeamParamBO){
        return this.familyExitRecordService.applyExitTeam(applyTeamParamBO);
    }


    @Authorization
    @SignVerification
    @PostMapping("kickOutTeam")
    @ApiOperation("踢出家族")
    public WebServiceMessage kickOutTeam(ApplyTeamParamBO applyTeamParamBO, HttpServletRequest request){
        return this.familyExitRecordService.kickOutTeam(applyTeamParamBO,request.getParameter("uid"));
    }

    @Authorization
    @SignVerification
    @PostMapping("setupAdministrator")
    @ApiOperation("设置管理员")
    public WebServiceMessage setupAdministrator(ApplyTeamParamBO applyTeamParamBO){
        return this.familyJoinService.setupAdministrator(applyTeamParamBO);
    }


    @Authorization
    @GetMapping("getFamilyMessage")
    @ApiOperation("获取家族消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="familyId",value="族长ID",dataType="long", paramType = "query"),
            @ApiImplicitParam(name="userId",value="用户userId",dataType="long", paramType = "query"),
            @ApiImplicitParam(name="current",value="当前页",dataType="int", paramType = "query"),
            @ApiImplicitParam(name="pageSize",value="每页显示",dataType="int", paramType = "query")

    })
    public WebServiceMessage getFamilyMessage(@RequestParam(value = "familyId" , required = true)Long familyId,
                                              @RequestParam(value = "userId" , required = true)Long userId,
                                              @RequestParam(value = "current" , defaultValue = "0")Integer current,
                                              @RequestParam(value = "pageSize" , defaultValue = "10")Integer pageSize){
        return this.familyTeamService.getFamilyMessage(familyId,userId,current,pageSize);
    }



    @Authorization
    @SignVerification
    @PostMapping("removeAdmin")
    @ApiOperation("移除管理员")
    public WebServiceMessage removeAdmin(ApplyTeamParamBO applyTeamParamBO){
        return this.familyTeamService.removeAdmin(applyTeamParamBO);
    }


    @Authorization
    @SignVerification
    @PostMapping("setApplyJoinMethod")
    @ApiOperation("设置申请加入方式")
    @ApiImplicitParams({
            @ApiImplicitParam(name="familyId",value="族长ID",dataType="long", paramType = "query"),
            @ApiImplicitParam(name="joinmode",value="申请加入验证(0.否1.是) && 0不用验证，1需要验证,2不允许任何人加入",dataType="int", paramType = "query")
    })
    public WebServiceMessage setApplyJoinMethod(@RequestParam(value = "familyId" , required = true)Long familyId,
                                                @RequestParam(value = "joinmode",required = true)Integer joinmode){
        return this.familyTeamService.setApplyJoinMethod(familyId,joinmode);
    }


    @Authorization
    @SignVerification
    @PostMapping("applyFamily")
    @ApiOperation("审核操作家族")
    public WebServiceMessage applyFamily(ApplyFamilyParamBO applyFamilyParamBO) {
        if (applyFamilyParamBO != null) {
            if (applyFamilyParamBO.getType() == 1) {
                return this.familyTeamService.applyJoinFamily(applyFamilyParamBO);
            } else if (applyFamilyParamBO.getType() == 2) {
                return this.familyExitRecordService.applyExitFamily(applyFamilyParamBO);
            }
        }
        return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
    }

    @Authorization
    @SignVerification
    @PostMapping("setMsgNotify")
    @ApiOperation("设置消息提醒")
    @ApiImplicitParams({
            @ApiImplicitParam(name="familyId",value="族长ID",dataType="long", paramType = "query"),
            @ApiImplicitParam(name="uid",value="uid",dataType="long", paramType = "query"),
            @ApiImplicitParam(name="ope",value="1：关闭消息提醒，2：打开消息提醒，其他值无效",dataType="int", paramType = "query")
    })
    public WebServiceMessage setMsgNotify(@RequestParam(value = "familyId" , required = true)Long familyId,
                                          @RequestParam(value = "uid" , required = true) Long uid,
                                          @RequestParam(value = "ope" , required = true) Integer ope){
        return WebServiceMessage.success(familyJoinService.setMsgNotify(familyId,uid,ope));
    }

    @Authorization
    @SignVerification
    @PostMapping("setBanned")
    @ApiOperation("设置禁言及解禁")
    public WebServiceMessage setBanned(FamilyUserBannedBO familyUserBannedBO){
        return WebServiceMessage.success(familyJoinService.settingBanned(familyUserBannedBO));
    }

    @Authorization
    @SignVerification
    @PostMapping("invitationPermission")
    @ApiOperation("邀请他人权限")
    public WebServiceMessage invitationPermission(FamilyInvitationPermissionBO invitationPermissionBO){
        return WebServiceMessage.success(familyTeamService.invitationPermission(invitationPermissionBO));
    }


    @Authorization
    @SignVerification
    @PostMapping("getFamilyInfo")
    @ApiOperation("根据家族Id获取家族信息")
    @ApiImplicitParam(name="familyId",value="家族familyId",dataType="long", paramType = "query")
    public WebServiceMessage getFamilyInfo(@RequestParam(value = "familyId" , required = true)Long familyId){
        return this.familyJoinService.getFamilyInfo(familyId);
    }
}
