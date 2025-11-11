package com.erban.admin.web.controller.User;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.dto.UsersAvatarDTO;
import com.erban.admin.main.service.user.UserCheckAdminService;
import com.erban.admin.main.vo.UsersAdminVo;
import com.erban.admin.web.base.BaseController;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.model.vo.UserAdminListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/admin/userCheckAdmin")
@ResponseBody
public class UserCheckAdminController extends BaseController {
    @Autowired
    private UserCheckAdminService userCheckAdminService;

    /**
     * 根据条件查询用户信息
     *
     * @param erbanNoList
     * @param uidList
     * @param gender
     * @param defType
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping("/getlist")
    @ResponseBody
    public BusiResult getList(@RequestParam(value = "erbanNoList", required = false) String erbanNoList,
                              @RequestParam(value = "uidList", required = false) String uidList,
                              @RequestParam(value = "gender", required = false) Integer gender,
                              @RequestParam(value = "defType", required = false) Integer defType,
                              @RequestParam(value = "startDate", required = false) String startDate,
                              @RequestParam(value = "endDate", required = false) String endDate) {
        List<UserAdminListVo> list;
        try {
            list = userCheckAdminService.getUsersList(erbanNoList, uidList, gender, defType, startDate, endDate);
        } catch (ParseException e) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        busiResult.setData(list);
        return busiResult;
    }

    @RequestMapping("/getUserList")
    @ResponseBody
    public void getUserList(@RequestParam(value = "erbanNoList", required = false) String erbanNoList,
                            @RequestParam(value = "uidList", required = false) String uidList,
                            @RequestParam(value = "gender", required = false) Integer gender,
                            @RequestParam(value = "defType", required = false) Integer defType,
                            @RequestParam(value = "hasCharge", required = false) Integer hasCharge,
                            @RequestParam(value = "startDate", required = false) String startDate,
                            @RequestParam(value = "endDate", required = false) String endDate) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<UserAdminListVo> pageInfo = null;
        try {
            pageInfo = userCheckAdminService.getUsersListWithPages(erbanNoList, uidList, gender, defType, hasCharge, startDate,
                    endDate, getPageNumber(), getPageSize());
        } catch (ParseException e) {
            jsonObject.put("message", BusiStatus.PARAMETERILLEGAL);
        }
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/getOne")
    @ResponseBody
    public BusiResult getOne(Long uid) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        UsersAdminVo usersAdminVo = userCheckAdminService.getOne(uid);
        busiResult.setData(usersAdminVo);
        return busiResult;
    }

    @RequestMapping("/saveUser")
    @ResponseBody
    public BusiResult saveUser(String nick, int gender, String avatar, String erBanNo, Long uid, String phone) {
        BusiResult busiResult = null;
        try {
            busiResult = userCheckAdminService.saveUser(nick, gender, avatar, erBanNo, uid, phone);
        } catch (Exception e) {
            logger.error("saveUser error..uid=" + uid, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

    //liangxiao saveUser接口基础上增加修改用户类型
    @RequestMapping("/saveUsers")
    @ResponseBody
    public BusiResult saveUsers(String nick, int gender, String avatar, String erBanNo, Long uid, String phone,
                                Byte defType) {
        BusiResult busiResult = null;
        try {
            busiResult = userCheckAdminService.saveUsers(nick, gender, avatar, erBanNo, uid, phone, defType);
        } catch (Exception e) {
            logger.error("saveUser error..uid=" + uid, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

    @RequestMapping("/removePhone")
    @ResponseBody
    public BusiResult removePhone(String phone, Long uid) {
        BusiResult busiResult = null;
        try {
            busiResult = userCheckAdminService.removePhone(phone, uid);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

    @RequestMapping("/outRoom")
    @ResponseBody
    public BusiResult outRoom(Long uid) {
        try {
            return userCheckAdminService.outRoom(uid);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    @RequestMapping("/out")
    @ResponseBody
    public BusiResult out(Long uid) {
        try {
            return userCheckAdminService.out(uid);
        } catch (Exception e) {
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

}
