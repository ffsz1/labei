package com.erban.admin.web.controller.feedback;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.account.AccountBlockAdminService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.Users;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.model.AccountBlock;
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
public class AccountBlockController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AccountBlockController.class);

    @Autowired
    private AccountBlockAdminService accountBlockAdminService;


    /**
     * 分页查询
     *
     * @return
     */
    @RequestMapping(value = "accountBlock/list", method = RequestMethod.GET)
    @ResponseBody
    public void getAccountBlockList(Integer pageSize, Integer pageNum, int type, Long erbanNo, String deviceId, String userIp) {
        byte typeByte = (byte) type;
        PageInfo<AccountBlock> pageInfo = accountBlockAdminService.getAccountBlockList(pageSize, pageNum, typeByte, erbanNo, deviceId, userIp);
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
    @RequestMapping(value = "accountBlock/AllUserList")
    @ResponseBody
    public BusiResult selectBlockedAccount(Long erbanNo) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        Users users = accountBlockAdminService.selectBlockedAccount(erbanNo);
        if (users != null) {
            busiResult.setData(users);
        }
        return busiResult;
    }

    /**
     * 编辑时查询
     */
    @RequestMapping(value = "accountBlock/getUpdateAccountBlocked")
    @ResponseBody
    public BusiResult getUpdateAccountBlocked(Integer rowId) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        AccountBlock users = accountBlockAdminService.getUpdateAccountBlocked(rowId);
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
    @RequestMapping(value = "accountBlock/updateAccountBlocked")
    @ResponseBody
    public int updateBlockedAccount(Integer rowId, Byte blockType, String startBlockTime, String endBlockTime, String blockDesc, HttpServletRequest request, HttpServletResponse response) {
        return accountBlockAdminService.updateBlockedAccount(rowId, blockType, startBlockTime, endBlockTime, blockDesc, getAdminId());
    }


    /**
     * 保存封禁
     *
     * @param erbanNo
     * @return
     */
    @RequestMapping(value = "accountBlock/saveAccountBlocked")
    @ResponseBody
    public int saveBlockedAccount(Long erbanNo, String blockType, String startBlockTime, String blockIp,
                                  String endBlockTime, String blockDesc, HttpServletRequest request, HttpServletResponse response) {
        int adminId = getAdminId();
        try {
            if (adminId == -1) {
                response.sendRedirect("/login/index");
            }
        } catch (Exception e) {
            logger.error("会话已过期，请重新登录！", e);
        }
        byte blockedType = (byte) Integer.parseInt(blockType);
        if(blockedType == 3){
            // 封禁IP
            return accountBlockAdminService.saveBlockIP(blockIp, blockDesc, adminId, startBlockTime, endBlockTime);
        }
        return accountBlockAdminService.saveBlockedAccount(erbanNo, blockedType, startBlockTime, endBlockTime, blockDesc, adminId, request, response);
    }

    /**
     * 解除封禁
     */
    @RequestMapping(value = "accountBlock/unBlockAccountBlocked")
    @ResponseBody
    public int updateBlockStatus(Integer rowId, HttpServletRequest request, HttpServletResponse response) {
        return accountBlockAdminService.updateBlockStatus(rowId, request, response);
    }

    @RequestMapping("/blockAll")
    @ResponseBody
    public int blockAll(Long uid, HttpServletRequest request, HttpServletResponse response){
        int adminId = getAdminId();
        try {
            if (adminId == -1) {
                response.sendRedirect("/login/index");
            }
        } catch (Exception e) {
            logger.error("会话已过期，请重新登录！", e);
        }
        return accountBlockAdminService.blockUserByUid(uid, adminId);
    }

}
