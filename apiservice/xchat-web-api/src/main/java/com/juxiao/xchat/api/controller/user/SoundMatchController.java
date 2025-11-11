package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.Client;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.item.dto.GiftDTO;
import com.juxiao.xchat.dao.user.dto.UserSoundDTO;
import com.juxiao.xchat.manager.common.sysconf.AppVersionManager;
import com.juxiao.xchat.manager.common.user.vo.UserSimpleVO;
import com.juxiao.xchat.service.api.event.vo.RankParentVo;
import com.juxiao.xchat.service.api.sysconf.ChannelService;
import com.juxiao.xchat.service.api.sysconf.enumeration.ChannelEnum;
import com.juxiao.xchat.service.api.sysconf.vo.ChannelAuditVO;
import com.juxiao.xchat.service.api.sysconf.vo.IndexParam;
import com.juxiao.xchat.service.api.user.SoundMatchService;
import com.juxiao.xchat.service.api.user.bo.SoundMatchBO;
import com.juxiao.xchat.service.api.user.vo.SoundMatchConfBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 声音匹配
 * @Author: alwyn
 * @Date: 2018/11/26 10:48
 */
@Api(tags = "声音匹配接口", description = "声音匹配接口")
@RequestMapping("user/soundMatch")
@RestController
public class SoundMatchController {
    @Autowired
    private SoundMatchService soundMatchService;

    @Autowired
    private AppVersionManager appVersionService;

    @Autowired
    private ChannelService channelService;

    /**
     * 获取速配用户列表
     * @param gender    性别
     * @return
     * @throws WebServiceException
     */
    @ApiOperation(value = "获取魅力用户列表", notes = "需要登录和加密")
    @ApiResponse(code = 200, message = "success", response = UserSimpleVO.class)
    @GetMapping("charmUser")
    @Authorization
    @SignVerification(client = Client.APP)
    public WebServiceMessage charmUser(IndexParam IndexParam,
                                       @ApiParam(value = "性别") @RequestParam(value = "gender") Integer gender) throws WebServiceException {
        List<UserSimpleVO> list = soundMatchService.charmUserV2(gender);
        return WebServiceMessage.success(list);
    }

    /**
     * 获取速配随机用户列表
     * @param uid           用户ID
     * @param gender        性别
     * @param minAge        最小年龄
     * @param maxAge        最大年龄
     * @param ticket
     * @param appid
     * @param os            操作系统
     * @param channel       App渠道
     * @param appVersion    App版本
     * @param request
     * @return
     * @throws WebServiceException
     */
    @ApiOperation(value = "获取随机用户列表", notes = "需要登录和加密")
    @ApiResponse(code = 200, message = "success", response = UserSoundDTO.class)
    @GetMapping("randomUser")
    @Authorization
    @SignVerification(client = Client.APP)
    public WebServiceMessage randomUser(@ApiParam(value = "用户ID") @RequestParam(value = "uid") Long uid,
                                        @ApiParam(value = "性别") @RequestParam(value = "gender") Integer gender,
                                        @ApiParam(value = "最小年龄") @RequestParam(value = "minAge") Integer minAge,
                                        @ApiParam(value = "最大年龄") @RequestParam(value = "maxAge") Integer maxAge,
                                        @ApiParam(value = "ticket") @RequestParam(value = "ticket") String ticket,
                                        @RequestParam(value = "appid", required = false) String appid,
                                        @RequestParam(value = "os", required = false) String os,
                                        @RequestParam(value = "channel", required = false) String channel,
                                        @RequestParam(value = "appVersion", required = false) String appVersion, HttpServletRequest request) throws WebServiceException {
        if("1001".equals(appid) || "android".equals(os)){
            ChannelEnum channelEnum = ChannelEnum.nameOf(channel);
            ChannelAuditVO auditVo = channelService.checkAudit(channelEnum, appVersion, uid);
            if (auditVo != null && auditVo.isAudit()) {
                List<UserSoundDTO> list = soundMatchService.checkAuditRandomUser(channelEnum,uid, gender, minAge, maxAge);
                return WebServiceMessage.success(list);
            }
            String ip = HttpServletUtils.getRealIp(request);
            if (appVersionService.checkAuditingVersion(os, appid, appVersion, ip, uid)) {
                List<UserSoundDTO> list = soundMatchService.checkAuditRandomUser(channelEnum,uid, gender, minAge, maxAge);
                return WebServiceMessage.success(list);
            }
        }
        List<UserSoundDTO> list = soundMatchService.randomUser(uid, gender, minAge, maxAge);
        return WebServiceMessage.success(list);
    }

    @ApiOperation(value = "喜欢某用户", notes = "需要登录和加密")
    @PostMapping("likeUser")
    @Authorization
    @SignVerification(client = Client.APP)
    public WebServiceMessage likeUser(SoundMatchBO soundMatchBO) throws WebServiceException {
        boolean flag = soundMatchService.likeUser(soundMatchBO.getUid(), soundMatchBO.getLikeUid());
        return WebServiceMessage.success(flag);
    }

    @ApiOperation(value = "获取礼物列表", notes = "需要登录")
    @ApiResponse(code = 200, message = "success", response = GiftDTO.class)
    @GetMapping("listGift")
    @Authorization
    public WebServiceMessage listGift(@ApiParam(value = "用户ID") @RequestParam(value = "uid") Long uid,
                                      @ApiParam(value = "ticket") @RequestParam(value = "ticket") String ticket) {
        List<GiftDTO> list = soundMatchService.listGift(uid);
        return WebServiceMessage.success(list);
    }

    @ApiOperation(value = "获取用户配置", notes = "需要登录")
    @ApiResponse(code = 200, message = "success", response = SoundMatchConfBO.class)
    @GetMapping("getConfig")
    @Authorization
    public WebServiceMessage getConfig(@ApiParam(value = "用户ID") @RequestParam(value = "uid") Long uid,
                                       @ApiParam(value = "ticket") @RequestParam(value = "ticket") String ticket) throws WebServiceException {
        SoundMatchConfBO confBO = soundMatchService.getConfig(uid);
        return WebServiceMessage.success(confBO);
    }

    @ApiOperation(value = "保存用户配置", notes = "需要登录和加密")
    @ApiResponse(code = 200, message = "success", response = Boolean.class)
    @PostMapping("saveConfig")
    @Authorization
    @SignVerification(client = Client.APP)
    public WebServiceMessage saveConfig(SoundMatchConfBO confBO,
                                        @ApiParam(value = "ticket") String ticket) {
        boolean flag = soundMatchService.setConfig(confBO);
        return WebServiceMessage.success(flag);
    }
}
