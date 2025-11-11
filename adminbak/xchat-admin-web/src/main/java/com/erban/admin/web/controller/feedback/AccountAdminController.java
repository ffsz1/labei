package com.erban.admin.web.controller.feedback;

import com.alibaba.fastjson.JSON;
import com.erban.admin.main.service.account.AccountAdminService;
import com.erban.admin.main.utils.FileUtils;
import com.erban.admin.web.base.BaseController;
import com.erban.admin.web.bo.StatReportBO;
import com.erban.admin.web.bo.UserRegisterBO;
import com.github.pagehelper.PageInfo;
import com.xchat.oauth2.service.param.AccountParam;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.vo.admin.AccountVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 注册管理表
 */
@Controller
@RequestMapping("/admin/account")
public class AccountAdminController extends BaseController {
    @Autowired
    private AccountAdminService accountAdminService;

    /**
     * 注册列表
     *
     * @param accountParam
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult getRedPacketWithDrawList(@RequestBody AccountParam accountParam) {
        logger.info("注册列表接口, 接口入参: {}", JSON.toJSONString(accountParam));
        BusiResult busiResult;
        if (accountParam == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        try {
            busiResult = accountAdminService.getList(accountParam);
        } catch (Exception e) {
            logger.error("注册列表数据出错, 错误原因: {}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
        }
        return busiResult;
    }

    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
    @ResponseBody
    public void exportTable(AccountParam accountParam, HttpServletResponse response) {
        List<AccountVo> accountVoList = accountAdminService.exportList(accountParam);
        List<UserRegisterBO> userRegisterBOList = new ArrayList<>();
        accountVoList.forEach(item -> {
            UserRegisterBO userRegisterBO = new UserRegisterBO();
            BeanUtils.copyProperties(item, userRegisterBO);
            if (item.getGender() == 1) {
                userRegisterBO.setGender("男");
            } else if (item.getGender() == 2) {
                userRegisterBO.setGender("女");
            } else {
                userRegisterBO.setGender("其他");
            }
            userRegisterBOList.add(userRegisterBO);
        });
        // 导出操作
        FileUtils.exportExcel(userRegisterBOList, "注册记录", "注册记录", UserRegisterBO.class, "注册记录.xls", response);
    }
}
