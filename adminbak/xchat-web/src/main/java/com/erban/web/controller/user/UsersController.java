package com.erban.web.controller.user;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.Users;
import com.erban.main.model.level.LevelCharmVo;
import com.erban.main.model.level.LevelExerpenceVo;
import com.erban.main.param.UserParam;
import com.erban.main.service.SmsService;
import com.erban.main.service.level.LevelCharmService;
import com.erban.main.service.level.LevelExperienceService;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.user.UserPacketService;
import com.erban.main.service.user.UserShareRecordService;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.UserVo;
import com.erban.web.common.BaseController;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.device.DeviceInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;
import com.xchat.oauth2.service.service.account.AccountBlockService;
import com.xchat.oauth2.service.service.account.LoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UsersController extends BaseController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private UserShareRecordService userShareRecordService;
    @Autowired
    private UserPacketService userPacketService;
    @Autowired
    private LevelExperienceService levelExperienceService;
    @Autowired
    private LevelCharmService levelCharmService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private LoginRecordService loginRecordService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private AccountBlockService accountBlockService;

    @ResponseBody
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public BusiResult getUserByUid(Long uid, String os, String appVersion, HttpServletRequest request) {
        if (uid == null || uid == 0L) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        try {
            Integer version = 103;
            if (StringUtils.isNotBlank(appVersion)) {
                version = Integer.valueOf(appVersion.replaceAll("\\.", ""));
            }
            if ("ios".equalsIgnoreCase(os) && version < 103) {
                try {
                    accountBlockService.doAccountBlock(uid);
                    roomService.closeRoom(uid);
                    return new BusiResult(BusiStatus.NOAUTHORITY, "请更新app版本", "");
                } catch (Exception e) {
                    return new BusiResult(BusiStatus.SERVERERROR);
                }
            }
            BusiResult busiResult = usersService.getUserByUid(uid);
            if (busiResult.getData() != null) {
                //modify by zhoamiao 2018/03/10 add experLevel and charmLevel
                UserVo userVo = (UserVo) busiResult.getData();
                addUserLevel(userVo);
            }
            // 记录用户登录记录
            try {
                loginRecordService.saveLoginRecord(request, uid);
            } catch (Exception e) {
                logger.error("登录记录" + e.getMessage());
            }
            return busiResult;
        } catch (Exception e) {
            logger.error("getUserByUid error..uid=" + uid, e.getMessage());
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    @RequestMapping(value = "/isBindPhone", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult bindPhoneByUid(Long uid) {
        if (uid == null || uid == 0L) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        try {
            return usersService.isBindPhone(uid);
        } catch (Exception e) {
            logger.error("bindPhoneByUid error..uid=" + uid, e.getMessage());
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    /**
     * 批量查询用户
     *
     * @param uids
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getUserByUids(String uids) {
        try {
            if (StringUtils.isEmpty(uids)) {
                return new BusiResult(BusiStatus.PARAMERROR);
            }
            List<Long> uidsLong = Lists.newArrayList();
            String uidsArray[] = uids.split(",");
            for (int i = 0; i < uidsArray.length; i++) {
                uidsLong.add(Long.valueOf(uidsArray[i]));
            }
            return usersService.getResultUsersVoListByUids(uidsLong);
        } catch (Exception e) {
            logger.error("getUserByUids error..uids=" + uids, e.getMessage());
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    @RequestMapping(value = "update")
    @ResponseBody
    @Authorization
    public BusiResult saveOrUpdateUserByUid(UserParam userParam, DeviceInfo deviceInfo) {
        if (userParam == null || userParam.getUid() == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        if (!BlankUtil.isBlank(userParam.getNick())) {
            if (userParam.getNick().length() > 15) {
                return new BusiResult(BusiStatus.NICKTOOLONG);
            }
        }
        try {
            Users users = usersService.convertUserParamToUsers(userParam);
            BusiResult<UserVo> busiResult = usersService.saveOrUpdateUserByUidV2(users, deviceInfo);

            UserVo userVo = busiResult.getData();
            if (busiResult.getCode() == 200 && userVo.getOperType() == 2) {
                //Async异步执行红包邀请活动,用户首次注册，并且属于被人邀请人，则邀请人获得红包
                // TODO:邀请注册得红包
                if (userVo.isHasRegPacket()) {
                    try {
                        String shareUidStr = userParam.getShareUid();
                        if (StringUtils.isNotBlank(shareUidStr)) {
                            String[] strings = shareUidStr.split("&");
                            Long shareUid = Long.valueOf(strings[0]);
                            userShareRecordService.saveUserShareRegisterRecord(shareUid, userParam.getUid());
                        }
                    } catch (Exception e) {
                        logger.error("邀请注册---邀请人获得红包" + e.getMessage());
                    }
                }
                userPacketService.checkAndGetFirsetPacket(userParam.getUid());
            }

//            if (org.apache.commons.lang3.StringUtils.isNotBlank(userVo.getUserDesc())) {
//                try {
//                    dutyService.updateFreshDuty(userVo.getUid(), DutyType.user_desc.getDutyId());
//                } catch (Exception e) {
//                }
//            }
            return busiResult;
        } catch (Exception e) {
            logger.error("saveOrUpdateUserByUid error..uid=" + userParam.getUid(), e.getMessage());
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }


    @RequestMapping(value = "updatev2", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    public BusiResult saveOrUpdateUserByUidV2(UserParam userParam, DeviceInfo deviceInfo) {
        if (userParam == null || userParam.getUid() == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
            Users users = usersService.convertUserParamToUsers(userParam);
            BusiResult<UserVo> busiResult = usersService.saveOrUpdateUserByUidV2(users, deviceInfo);

            UserVo userVo = busiResult.getData();
            if (userVo.getOperType() == 2) {
                //Async异步执行红包邀请活动,用户首次注册，并且属于被人邀请人，则邀请人获得红包
                if (userVo.isHasRegPacket()) {
                    try {
                        String shareUidStr = userParam.getShareUid();
                        if (StringUtils.isNotBlank(shareUidStr)) {
                            String[] strings = shareUidStr.split("&");
                            Long shareUid = Long.valueOf(strings[0]);
                            userShareRecordService.saveUserShareRegisterRecord(shareUid, userParam.getUid());
                        }
                    } catch (Exception e) {
                        logger.error("邀请注册---邀请人获得红包" + e.getMessage());
                    }
                }
                userPacketService.checkAndGetFirsetPacket(userParam.getUid());
            }
//            if (org.apache.commons.lang3.StringUtils.isNotBlank(userVo.getUserDesc())) {
//                try {
//                    dutyService.updateFreshDuty(userVo.getUid(), DutyType.user_desc.getDutyId());
//                } catch (Exception e) {
//                }
//            }
            return busiResult;
        } catch (Exception e) {
            logger.error("saveOrUpdateUserByUidV2 error..uid=" + userParam.getUid(), e.getMessage());
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    /**
     * 确认手机
     *
     * @return
     */
    @RequestMapping(value = "confirm", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult confirm(String phone, String smsCode) {
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(smsCode)) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        //检验验证码
        try {
            if (smsService.verifySmsCodeByNetEase(phone, smsCode)) {
                return new BusiResult(BusiStatus.SUCCESS);
            } else {
                return new BusiResult(BusiStatus.SMSCODEERROR);
            }
        } catch (Exception e) {
            logger.error("confirm error..phone=" + phone, e.getMessage());
            return new BusiResult(BusiStatus.SERVERBUSY);
        }
    }

    /**
     * 更换手机
     *
     * @return
     */
    @RequestMapping(value = "replace", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult replace(Long uid, String phone, String smsCode) {
        if (uid == null || StringUtils.isBlank(smsCode) || StringUtils.isBlank(phone)) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        //检验验证码
        try {
            if (smsService.verifySmsCodeByNetEase(phone, smsCode)) {
                return usersService.replace(uid, phone);
            } else {
                return new BusiResult(BusiStatus.SMSCODEERROR);
            }
        } catch (Exception e) {
            logger.error("replace error..phone=" + phone, e.getMessage());
            return new BusiResult(BusiStatus.SERVERBUSY);
        }
    }

    @RequestMapping(value = "/getUidByToken", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getUidByToken(String token) {
        if (token == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        try {
            return usersService.getUidByToken(token);
        } catch (Exception e) {
            logger.error("getUidByToken error..token=" + token, e.getMessage());
            return new BusiResult(BusiStatus.SERVERBUSY);
        }
    }

    /**
     * 获取财富等级和魅力等级
     *
     * @param userVo
     * @return
     */
    private UserVo addUserLevel(UserVo userVo) {
        LevelExerpenceVo levelExerpenceVo = levelExperienceService.getLevelExperience(userVo.getUid());
        if (levelExerpenceVo != null) {
            userVo.setExperLevel(Integer.valueOf(levelExerpenceVo.getLevelName().substring(2)));
        } else {
            userVo.setExperLevel(0);
        }
        LevelCharmVo levelCharmVo = levelCharmService.getLevelCharm(userVo.getUid());
        if (levelCharmVo != null) {
            userVo.setCharmLevel(Integer.valueOf(levelCharmVo.getLevelName().substring(2)));
        } else {
            userVo.setCharmLevel(0);
        }
        return userVo;
    }

    @RequestMapping(value = "/checkIdfa", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult checkIdfa(String idfa) {
        if (idfa == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        try {
            return usersService.checkIdfa(idfa);
        } catch (Exception e) {
            logger.error("user/check error..idfa=" + idfa, e.getMessage());
            return new BusiResult(BusiStatus.SERVERBUSY);
        }
    }

    /**
     * 查询新注册用户
     *
     * @return
     */
    @RequestMapping("/newUserList")
    @ResponseBody
    public BusiResult newUserList(Long uid, Byte gender, Integer pageNum, Integer pageSize){
        try {
            return usersService.findNewUsers(uid, gender, pageNum, pageSize);
        } catch (Exception e) {
            logger.error("user/newUserList error", e.getMessage());
            return new BusiResult(BusiStatus.SERVERBUSY);
        }
    }

}
