package com.erban.admin.web.controller.User;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.dto.UsersPhotoDTO;
import com.erban.admin.main.service.user.UserPhotoService;
import com.erban.admin.web.base.BaseController;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin/user/photo")
public class UserPhotoController extends BaseController {
    @Autowired
    private UserPhotoService userPhotoService;

    @RequestMapping("/getall")
    @ResponseBody
    public void getAll(Long erbanNo, Integer status, String startDate, String endDate) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<UsersPhotoDTO> pageInfo = userPhotoService.getListWithPage(erbanNo, status, startDate, endDate,
                getPageNumber(), getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 审核通过
     */
    @RequestMapping("/checkSuccess")
    @ResponseBody
    public BusiResult checkSuccess(String pids) {
        if (pids == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        return userPhotoService.updateCheckSuccess(pids,getAdminId());
    }

    /**
     * 审核不通过
     */
    @RequestMapping("/checkFailure")
    @ResponseBody
    public BusiResult checkFailure(Long id) {
        if (id == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        return userPhotoService.updateCheckFailure(id,getAdminId());
    }


}
