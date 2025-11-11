package com.erban.admin.web.controller.feedback;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.model.AccountBanned;
import com.erban.admin.main.service.account.AccountBannedAdminService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.Users;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/admin/*")
public class AccountBannedController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AccountBannedController.class);

    @Autowired
    private AccountBannedAdminService accountBannedAdminService;


    /**
     * 分页查询
     *
     * @return
     */
    @RequestMapping(value = "accountBanned/list", method = RequestMethod.GET)
    @ResponseBody
    public void getAccountBlockList(Integer pageSize, Integer pageNum, int type, Long erbanNo, String deviceId, String userIp) {
        PageInfo<AccountBanned> pageInfo = accountBannedAdminService.getAccountBlockList(pageSize, pageNum, type, erbanNo, deviceId, userIp);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 查询用户列表
     *
     * @param erbanNo
     * @return
     */
    @RequestMapping(value = "accountBanned/AllUserList")
    @ResponseBody
    public BusiResult selectBlockedAccount(Long erbanNo) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        Users users = accountBannedAdminService.selectBlockedAccount(erbanNo);
        if (users != null) {
            busiResult.setData(users);
        }
        return busiResult;
    }

    /**
     * 保存封禁
     *
     * @param erbanNo
     * @return
     */
    @RequestMapping(value = "accountBanned/saveAccountBanned")
    @ResponseBody
    public int saveBannedAccount(Long erbanNo, String bannedType, String startBannedTime,
                                 String endBannedTime, String bannedDesc, HttpServletRequest request, HttpServletResponse response) {
        int adminId = getAdminId();
        try {
            if (adminId == -1) {
                response.sendRedirect("/login/index");
            }
        } catch (Exception e) {
            logger.error("会话已过期，请重新登录！", e);
        }

        return accountBannedAdminService.saveBannedAccount(erbanNo, bannedType, startBannedTime, endBannedTime, bannedDesc, adminId, request, response);
    }


    /**
     * 编辑时查询
     */
    @RequestMapping(value = "accountBanned/getUpdateAccountBlocked")
    @ResponseBody
    public BusiResult getUpdateAccountBlocked(Integer rowId) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        AccountBanned users = accountBannedAdminService.getUpdateAccountBanned(rowId);
        if (users != null) {
            busiResult.setData(users);
        }
        return busiResult;
    }

    /**
     * 编辑弹窗保存
     *
     * @param
     * @return
     */
    @RequestMapping(value = "accountBanned/updateAccountBlocked")
    @ResponseBody
    public int updateBannedAccount(Integer rowId, Byte bannedType, String startBannedTime, String endBannedTime, String bannedDesc, HttpServletRequest request, HttpServletResponse response) {
        return accountBannedAdminService.updateBannedAccount(rowId, bannedType, startBannedTime, endBannedTime, bannedDesc, getAdminId());
    }

    /**
     * 解除封禁
     */
    @RequestMapping(value = "accountBanned/unBlockAccountBlocked")
    @ResponseBody
    public int updateBlockStatus(Integer rowId, HttpServletRequest request, HttpServletResponse response) {
        return accountBannedAdminService.updateBannedStatus(rowId, request, response);
    }


}
