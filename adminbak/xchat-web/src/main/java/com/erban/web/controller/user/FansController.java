package com.erban.web.controller.user;

import com.erban.main.service.AppVersionService;
import com.erban.main.service.user.FansService;
import com.erban.main.util.StringUtils;
import com.erban.web.common.BaseController;
import com.xchat.common.annotation.Authorization;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
@RequestMapping("/fans")
public class FansController extends BaseController {
    @Autowired
    private FansService fansService;
    @Autowired
    private AppVersionService appVersionService;

    /**
     * 喜欢某人，或者取消喜欢某人
     *
     * @param uid
     * @param likedUid
     * @param type     1喜欢某人，2取消喜欢某人
     * @return
     */
    @RequestMapping(value = "like", method = RequestMethod.POST)
    @ResponseBody
    @Authorization
    @SignVerification
    public BusiResult likeSomeBody(Long uid, Long likedUid, String type) {
        if (uid == null || likedUid == null || StringUtils.isEmpty(type)) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL, "参数异常");
        }
        try {
            return fansService.likeSomeBody(uid, likedUid, type);
        } catch (Exception e) {
            logger.error("likeSomeBody error..uid=" + uid + "&likedUid=" + likedUid, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    /**
     * 删除好友关系
     *
     * @param uid
     * @param likedUid
     * @return
     */
//    @RequestMapping(value = "fdelete", method = RequestMethod.POST)
//    @ResponseBody
//    @Authorization
//    public BusiResult deleteFriend(Long uid, Long likedUid) {
//        if (uid == null || likedUid == null) {
//            return new BusiResult(BusiStatus.PARAMETERILLEGAL, "参数异常");
//        }
//        BusiResult<List<UserVo>> busiResult = null;
//        try {
//            busiResult = fansService.deleteFriend(uid, likedUid);
//        } catch (Exception e) {
//            logger.error("likeSomeBody error..uid=" + uid + "&likedUid=" + likedUid, e);
//            return new BusiResult(BusiStatus.BUSIERROR);
//        }
//        return busiResult;
//    }

    /**
     * 获取关注列表
     *
     * @param uid
     * @return
     */
    @RequestMapping(value = "following", method = RequestMethod.GET)
    @ResponseBody
    @SignVerification
    public BusiResult getFollowing(String os, String appVersion, Long uid, int pageSize, int pageNo) {
        if (uid == null || uid == 0L) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL, "参数异常");
        }
        try {
//            // IOS新版本在审核期内数据要做特殊处理
//            if ("ios".equalsIgnoreCase(os) && appVersionService.checkIsAuditingVersion(appVersion)) {
//                return new BusiResult(BusiStatus.SUCCESS, new ArrayList<>());
//            }
            return fansService.getFollowingList(uid, pageSize, pageNo);
        } catch (Exception e) {
            logger.error("getFollowingList error..uid=" + uid, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    /**
     * 查询uid是否喜欢checkUid
     *
     * @param uid
     * @return
     */
    @RequestMapping(value = "islike", method = RequestMethod.GET)
    @ResponseBody
    @SignVerification
    public BusiResult<Boolean> checkUserIsLike(Long uid, Long isLikeUid) {
        if (uid == null || uid == 0L) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL, "参数异常");
        }
        try {
            return fansService.checkUserIsLike(uid, isLikeUid);
        } catch (Exception e) {
            logger.error("getFollowingList error..uid=" + uid, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    @RequestMapping(value = "fanslist", method = RequestMethod.GET)
    @ResponseBody
    @SignVerification
    public BusiResult getFansList(String os, String appVersion, Long uid, Integer pageNo, Integer pageSize) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        try {
//            // IOS新版本在审核期内数据要做特殊处理
//            if ("ios".equalsIgnoreCase(os) && appVersionService.checkIsAuditingVersion(appVersion)) {
//                return new BusiResult(BusiStatus.SUCCESS, new ArrayList<>());
//            }
            return fansService.getFansList(uid, pageNo, pageSize);
        } catch (Exception e) {
            logger.error("getfanslist error..uid=" + uid, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

}
