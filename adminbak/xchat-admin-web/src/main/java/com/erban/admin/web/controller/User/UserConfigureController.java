package com.erban.admin.web.controller.User;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.ErrorCode;
import com.erban.admin.main.service.user.UserConfiguresService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.UserConfigure;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author laizhilong
 * @Title: 用户配置
 * @Package com.erban.admin.web.controller.User
 * @date 2018/8/16
 * @time 09:54
 */
@Controller
@RequestMapping("/admin/user/configure")
public class UserConfigureController extends BaseController{

    @Autowired
    private UserConfiguresService userConfiguresService;


    @RequestMapping("/getall")
    @ResponseBody
    public void getAll(Long erbanNo, Integer roomType) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<UserConfigure> pageInfo = userConfiguresService.getListWithPage(erbanNo, roomType, getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }


    @RequestMapping("/save")
    @ResponseBody
    public void save(UserConfigure userConfigure, boolean isEdit) {
        int result = -1;
        if (userConfigure != null) {
            try {
                result = userConfiguresService.saveUserConfigure(userConfigure, isEdit);
            } catch (Exception e) {
                logger.warn("save fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    @RequestMapping("/getOne")
    @ResponseBody
    public void getOne(Integer id) {
        JSONObject jsonObject = new JSONObject();
        if (id != null) {

            UserConfigure userConfigure = userConfiguresService.getOne(id);
            if (userConfigure != null) {
                jsonObject.put("entity", userConfigure);
            }
        }
        writeJson(jsonObject.toJSONString());
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    public BusiResult delete(@PathVariable Long id) {
        int count = userConfiguresService.delete(id);
        if (count > 0) {
            return new BusiResult(BusiStatus.SUCCESS);
        }
        return new BusiResult(BusiStatus.SERVERERROR);
    }
}
