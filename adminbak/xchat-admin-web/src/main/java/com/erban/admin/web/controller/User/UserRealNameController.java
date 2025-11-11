package com.erban.admin.web.controller.User;


import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.dto.UserRealInfoDTO;
import com.erban.admin.main.mapper.query.UserRealNameQuery;
import com.erban.admin.main.service.user.UserRealNameExpandService;
import com.erban.admin.web.base.BaseController;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author laizhilong
 * @Title: 实名认证
 * @Package com.erban.admin.web.controller.User
 * @date 2018/8/16
 * @time 09:54
 */
@Controller
@RequestMapping("/admin/user/real")
public class UserRealNameController extends BaseController {
    @Autowired
    private UserRealNameExpandService userRealNameExpandService;

    /**
     * 获取所有实名认证列表
     *
     * @param query
     */
    @RequestMapping("/getall")
    @ResponseBody
    public void getAll(UserRealNameQuery query) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<UserRealInfoDTO> pageInfo = userRealNameExpandService.getListWithPage(query, getPageNumber(),
                getPageSize());
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 获取单个用户的信息
     *
     * @param uid
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public BusiResult getByUid(Long uid) {
        UserRealInfoDTO infoDTO = userRealNameExpandService.getOne(uid);
        return new BusiResult(BusiStatus.SUCCESS, infoDTO);
    }

    /**
     * 审核通过
     *
     * @param uid
     * @return
     */
    @RequestMapping("/checkSuccess")
    @ResponseBody
    public BusiResult checkSuccess(Long uid, String erno) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        if (erno.isEmpty()) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        BusiResult busiResult = userRealNameExpandService.updateCheckSuccess(uid, getAdminId());
        return busiResult;
    }

    /**
     * 审核不通过
     *
     * @param uid
     * @return
     */
    @RequestMapping("/checkFailure")
    @ResponseBody
    public BusiResult checkFailure(Long uid, String remark) {
        if (uid == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }
        if (StringUtils.isBlank(remark)) {
            return new BusiResult(BusiStatus.SUCCESS, "审核不通过的原因不能为空", null);
        }
        BusiResult busiResult = userRealNameExpandService.updateCheckFailure(uid, getAdminId(), remark);
        return busiResult;
    }
}
