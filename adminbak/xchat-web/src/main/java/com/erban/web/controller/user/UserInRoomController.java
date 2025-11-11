package com.erban.web.controller.user;

import com.erban.main.service.activity.YoungGService;
import com.erban.main.service.user.UserInRoomService;
import com.erban.main.vo.RoomVo;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by liuguofu on 2017/10/18.
 */
@Controller
@RequestMapping("/userroom")
public class UserInRoomController {
    private static final Logger logger = LoggerFactory.getLogger(UserInRoomController.class);
    @Autowired
    private UserInRoomService userInRoomService;
    @Autowired
    private YoungGService youngGService;

    /**
     * 用户进入房间
     *
     * @param uid
     * @param roomUid
     * @return
     */
    @RequestMapping(value = "in", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult userIntoRoom(Long uid, Long roomUid) {
        BusiResult busiResult = null;
        if (uid == null || roomUid == null) {
            busiResult = new BusiResult(BusiStatus.PARAMETERILLEGAL);
            return busiResult;
        }
        try {
            busiResult = userInRoomService.userIntoRoom(uid, roomUid);
        } catch (Exception e) {
            logger.error("userIntoRoom error", e);
            busiResult = new BusiResult(BusiStatus.BUSIERROR);
            return busiResult;
        }

        try {
            youngGService.increaseRoomTime(uid);
        } catch (Exception e) {
            logger.error("[ 养鸡任务 ] 统计进入房间异常：", e);
        }

        return busiResult;
    }

    /**
     * 用户退出房间（如果有在房间）
     *
     * @param uid
     * @return
     */
    @RequestMapping(value = "out", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult userOutRoom(Long uid) {
        BusiResult busiResult = null;
        if (uid == null) {
            busiResult = new BusiResult(BusiStatus.PARAMETERILLEGAL);
            return busiResult;
        }
        try {
            busiResult = userInRoomService.userOutRoom(uid);
        } catch (Exception e) {
            logger.error("userOutRoom error", e);
            busiResult = new BusiResult(BusiStatus.BUSIERROR);
            return busiResult;
        }
        return busiResult;
    }

    /**
     * 获取指定用户当前所在房间
     *
     * @param uid
     * @return
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    @SignVerification
    public BusiResult<RoomVo> getUserInRoomInfo(Long uid) {
        BusiResult<RoomVo> busiResult = null;
        if (uid == null) {
            busiResult = new BusiResult<RoomVo>(BusiStatus.PARAMETERILLEGAL);
            return busiResult;
        }
        try {
            busiResult = userInRoomService.getUserInRoomInfo(uid);
        } catch (Exception e) {
            logger.error("getUserInRoomInfo error", e);
            busiResult = new BusiResult(BusiStatus.BUSIERROR);
            return busiResult;
        }
        return busiResult;
    }

}
