package com.erban.web.controller.user;

import com.erban.main.model.Users;
import com.erban.main.param.UserParam;
import com.erban.main.service.user.UserPacketService;
import com.erban.main.service.user.UserShareRecordService;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.UserVo;
import com.erban.web.common.BaseController;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.device.DeviceInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;
import com.xchat.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户控制器 V2版本
 */
@Controller
@RequestMapping("/user/v2")
public class UsersV2Controller extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UsersV2Controller.class);
    @Autowired
    private UsersService usersService;
    @Autowired
    private UserShareRecordService userShareRecordService;
    @Autowired
    private UserPacketService userPacketService;

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    public BusiResult saveOrUpdateUserByUidV2(UserParam userParam, DeviceInfo deviceInfo) {
        if (userParam == null || userParam.getUid() == null) {
            return new BusiResult<UserVo>(BusiStatus.PARAMETERILLEGAL);
        }
        if (!BlankUtil.isBlank(userParam.getNick())) {
            if (userParam.getNick().length() > 15) {
                return new BusiResult<UserVo>(BusiStatus.NICKTOOLONG);
            }
        }

        try {
            Users users = usersService.convertUserParamToUsers(userParam);
            BusiResult<UserVo> busiResult = usersService.saveOrUpdateUserByUidV2(users, deviceInfo);
            UserVo userVo = busiResult.getData();
            if (busiResult.getCode() == 200 && userVo.getOperType() == 2) {
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
            logger.error("saveOrUpdateUserByUidV2 error..uid=" + userParam.getUid(), e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }

    }


}
