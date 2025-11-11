package com.erban.web.controller.noble;

import com.erban.main.model.NobleRight;
import com.erban.main.model.NobleUsers;
import com.erban.main.model.Users;
import com.erban.main.service.noble.NobleRecomService;
import com.erban.main.service.noble.NobleRightService;
import com.erban.main.service.noble.NobleUsersService;
import com.erban.main.service.user.UsersService;
import com.erban.web.common.BaseController;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/noble/users")
public class NobleUsersController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(NobleUsersController.class);

    @Autowired
    private NobleUsersService nobleUsersService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private NobleRecomService nobleRecomService;
    @Autowired
    private NobleRightService nobleRightService;

    /**
     * 设置隐身进场
     *
     * @param uid
     * @param val
     * @return
     */
    @RequestMapping(value = "/hideenter")
    @ResponseBody
    @Authorization
    public BusiResult setHideEnter(long uid, byte val) {
        try {
            NobleUsers nobleUsers = nobleUsersService.getNobleUser(uid);
            if (nobleUsers == null) {
                return new BusiResult(BusiStatus.NOBLENOTEXISTS);
            }
            NobleRight nobleRight = nobleRightService.getNobleRight(nobleUsers.getNobleId());
            // 判断是否拥有该特权
            if (!nobleRight.getEnterHide().equals((byte) 1)) {
                return new BusiResult(BusiStatus.NOBLENOAUTH);
            }
            nobleUsers.setEnterHide(val);
            nobleUsersService.updateNobleUserDbAndCache(nobleUsers);
            return new BusiResult(BusiStatus.SUCCESS);
        } catch (Exception e) {
            logger.error("setHideEnter error, uid:" + uid + ", val: " + val, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    /**
     * 设置榜单隐身
     *
     * @param uid
     * @param val
     * @return
     */
    @RequestMapping(value = "/hiderank")
    @ResponseBody
    @Authorization
    public BusiResult setHideRank(long uid, byte val) {
        try {
            NobleUsers nobleUsers = nobleUsersService.getNobleUser(uid);
            if (nobleUsers == null) {
                return new BusiResult(BusiStatus.NOBLENOTEXISTS);
            }
            NobleRight nobleRight = nobleRightService.getNobleRight(nobleUsers.getNobleId());
            // 判断是否拥有该特权
            if (!nobleRight.getRankHide().equals((byte) 1)) {
                return new BusiResult(BusiStatus.NOBLENOAUTH);
            }
            nobleUsers.setRankHide(val);
            nobleUsersService.updateNobleUserDbAndCache(nobleUsers);
            return new BusiResult(BusiStatus.SUCCESS);
        } catch (Exception e) {
            logger.error("setHideRank error, uid:" + uid + ", val: " + val, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    /**
     * 获取用户的贵族信息
     *
     * @param uid
     * @return
     */
    @RequestMapping(value = "/get")
    @ResponseBody
    @Authorization
    public BusiResult getNobleUser(long uid) {
        try {
            NobleUsers nobleUsers = nobleUsersService.getNobleUser(uid);
            if (nobleUsers == null) {
                return new BusiResult(BusiStatus.NOTEXISTS);
            }
            return new BusiResult(BusiStatus.SUCCESS, nobleUsers);
        } catch (Exception e) {
            logger.error("getNobleUser error, uid: " + uid, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    /**
     * 获取贵族推荐上热门的机会
     *
     * @param uid 推荐人
     * @return
     */
    @RequestMapping(value = "/getrecomcount")
    @ResponseBody
    @Authorization
    public BusiResult getRecomCount(long uid) {
        try {
            NobleUsers nobleUsers = nobleUsersService.getNobleUser(uid);
            if (nobleUsers == null) {
                return new BusiResult(BusiStatus.NOTEXISTS);
            }
            return new BusiResult(BusiStatus.SUCCESS, nobleUsers.getRecomCount());
        } catch (Exception e) {
            logger.error("getRecomCount error, uid: " + uid, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

    /**
     * 推荐房间上热门
     *
     * @param uid     推荐人
     * @param erbanNo 推荐的房主拉贝号
     * @return
     */
    @RequestMapping(value = "/recom")
    @ResponseBody
    @Authorization
    public BusiResult recomHotRoom(long uid, long erbanNo) {
        try {
            // 判断推荐的房主是否存在
            Users users = usersService.getUsersByErBanNo(erbanNo);
            if (users == null) {
                return new BusiResult(BusiStatus.USERNOTEXISTS);
            }
            logger.info("recomHotRoom uid:{}, erbanNo:{}", uid, erbanNo);
            // 扣减推荐机会，成功返回1
            int result = nobleUsersService.reduceRecomCount(uid);
            if (result != 1) {
                return new BusiResult(BusiStatus.NOBLENORECOMCOUNT);
            }

            // 更新贵族信息的缓存
            nobleUsersService.updateNobleUserCache(uid);
            NobleUsers nobleUsers = nobleUsersService.getNobleUser(uid);
            // 加入推荐列表
            nobleRecomService.handleRecomRoom(nobleUsers, users.getUid());
            return new BusiResult(BusiStatus.SUCCESS, "推荐成功，稍后生效成功，小秘书将会通知您"
                    , nobleUsers.getRecomCount());
        } catch (Exception e) {
            logger.error("recomHotRoom error, uid: " + uid, e);
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

}
