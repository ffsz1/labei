package com.erban.web.controller;

import com.erban.main.service.AppVersionService;
import com.erban.main.service.room.RoomSearchService;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.SearchVo;
import com.erban.main.vo.UserVo;
import com.erban.web.common.BaseController;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.constant.Constant;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController extends BaseController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private RoomSearchService roomSearchService;
    @Autowired
    AppVersionService appVersionService;

    @ResponseBody
    @SignVerification
    @RequestMapping(value = "user", method = RequestMethod.GET)
    public BusiResult searchUsersBykey(String key, int pageSize, int pageNo) {
        if (StringUtils.isEmpty(key)) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL, "参数异常");
        }
        BusiResult<List<UserVo>> busiResult = null;
        try {
            busiResult = usersService.searchUsersBykey(key);
        } catch (Exception e) {
            logger.error("searchUsersBykey error..key=" + key, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;

    }

    /**
     * @param key 搜索关键字 （搜索拉贝号、房间title ，用户昵称nick）
     * @return
     */
    @ResponseBody
    @SignVerification
    @RequestMapping(value = "room", method = RequestMethod.GET)
    public BusiResult searchRoomBykey(String key,String os, String appVersion, HttpServletRequest request) {
        if (StringUtils.isEmpty(key)) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        try {
            List<SearchVo> searchVos;
            // IOS新版本在审核期内的首页数据要做特殊处理
            if ("ios".equalsIgnoreCase(os) && appVersionService.checkIsAuditingVersion(appVersion, request)) {
                searchVos = roomSearchService.searchRoomByKey(key, Constant.IOSAuditAccount.auditAccountList);
            }else{
                searchVos = roomSearchService.searchRoomByKey(key,null);
            }
            busiResult.setData(searchVos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return busiResult;

    }

}
