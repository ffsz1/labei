package com.erban.admin.web.controller.User;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.service.UsersWithdrawWhitelistService;
import com.erban.admin.web.base.BaseController;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户提现白名单
 * @author Administrator
 */
@Controller
@RequestMapping("/admin/users/withdraw/whitelist")
public class UsersWithdrawWhitelistController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UsersWithdrawWhitelistController.class);

    @Autowired
    private UsersWithdrawWhitelistService usersWithdrawWhitelistService;

    /**
     * 获取列表
     *
     * @return
     */
    @RequestMapping(value = "list")
    @ResponseBody
    public void getList(Integer pageNumber, Integer pageSize, Long erbanNo){
        PageInfo pageInfo = usersWithdrawWhitelistService.getList(pageNumber, pageSize, erbanNo);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",pageInfo.getTotal());
        jsonObject.put("rows",pageInfo);
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 增加
     * @param erbanNo 拉贝号
     * @return
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public BusiResult add(Long erbanNo) {
        try {
            if(erbanNo==null){
                return new BusiResult(BusiStatus.PARAMERROR);
            }
            return usersWithdrawWhitelistService.add(erbanNo, getAdminId());
        } catch (Exception e) {
            logger.error("add error,erbanNo=" + erbanNo , e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/del")
    @ResponseBody
    public BusiResult delete(Integer id) {
        try {
            return usersWithdrawWhitelistService.delete(id);
        } catch (Exception e) {
            logger.error("delete error,id=" + id, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }



    /**
     * 设置
     * @param id
     * @return
     */
    @RequestMapping(value = "/setting")
    @ResponseBody
    public BusiResult setting(Integer id) {
        try {
            return usersWithdrawWhitelistService.setting(id);
        } catch (Exception e) {
            logger.error("setting error,id=" + id, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

}
