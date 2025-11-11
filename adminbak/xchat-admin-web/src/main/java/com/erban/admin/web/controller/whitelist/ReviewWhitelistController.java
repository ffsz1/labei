package com.erban.admin.web.controller.whitelist;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.ErrorCode;
import com.erban.admin.main.service.ReviewWhitelistService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.dto.ReviewWhitelistDTO;
import com.erban.main.model.ReviewWhitelist;
import com.github.pagehelper.PageInfo;
import com.xchat.common.utils.BlankUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chris
 * @Title:
 * @date 2018/10/19
 * @time 14:02
 */
@Controller
@RequestMapping("/admin/review/whitelist")
public class ReviewWhitelistController extends BaseController {

    @Autowired
    private ReviewWhitelistService reviewWhitelistService;

    /**
     * 查询用户列表
     *
     * @param searchText 用户名称
     */
    @RequestMapping("/getlist")
    @ResponseBody
    public void getList(String searchText) {
        JSONObject jsonObject = new JSONObject();
        PageInfo<ReviewWhitelistDTO> pageInfo = reviewWhitelistService.getList(getPageNumber(), getPageSize(),searchText);
        jsonObject.put("total", pageInfo.getTotal());
        jsonObject.put("rows", pageInfo.getList());
        writeJson(jsonObject.toJSONString());
    }



    /**
     * 保存/更新
     * @param reviewWhitelist
     */
    @RequestMapping("/save")
    @ResponseBody
    public void save(ReviewWhitelist reviewWhitelist) {
        int result = -1;
        if (reviewWhitelist != null) {
            try {
                result = reviewWhitelistService.save(reviewWhitelist);
            } catch (Exception e) {
                logger.warn("ReviewWhitelistController save fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }

    @RequestMapping("/del")
    @ResponseBody
    public void del(Long id) {
        int result = 1;
        if (!BlankUtil.isBlank(id)) {
            try {
                reviewWhitelistService.delete(id);
            } catch (Exception e) {
                result = ErrorCode.SERVER_ERROR;
                logger.warn("ReviewWhitelistController del fail,cause by " + e.getMessage(), e);
            }
        } else {
            result = ErrorCode.ERROR_NULL_ARGU;
        }
        writeJsonResult(result);
    }
}
